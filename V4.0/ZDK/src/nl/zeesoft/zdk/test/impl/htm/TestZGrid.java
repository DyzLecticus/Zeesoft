package nl.zeesoft.zdk.test.impl.htm;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.SortedMap;

import nl.zeesoft.zdk.ZDKFactory;
import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.htm.grid.ZGrid;
import nl.zeesoft.zdk.htm.grid.ZGridColumnEncoder;
import nl.zeesoft.zdk.htm.grid.ZGridRequest;
import nl.zeesoft.zdk.htm.grid.ZGridResult;
import nl.zeesoft.zdk.htm.grid.ZGridResultsListener;
import nl.zeesoft.zdk.htm.grid.enc.ZGridEncoderDateTime;
import nl.zeesoft.zdk.htm.grid.enc.ZGridEncoderValue;
import nl.zeesoft.zdk.htm.proc.Anomaly;
import nl.zeesoft.zdk.htm.proc.Classification;
import nl.zeesoft.zdk.htm.proc.ClassifierConfig;
import nl.zeesoft.zdk.htm.proc.DetectorConfig;
import nl.zeesoft.zdk.htm.proc.MemoryConfig;
import nl.zeesoft.zdk.htm.proc.MergerConfig;
import nl.zeesoft.zdk.htm.proc.PoolerConfig;
import nl.zeesoft.zdk.htm.util.HistoricalFloats;
import nl.zeesoft.zdk.htm.util.SDR;
import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.test.TestObject;
import nl.zeesoft.zdk.test.Tester;
import nl.zeesoft.zdk.thread.WorkerUnion;

public class TestZGrid extends TestObject implements ZGridResultsListener {
	private List<Long>				expectedIds				= new ArrayList<Long>();
	private List<Long>				returnedIds				= new ArrayList<Long>();
	
	private DecimalFormat			df						= new DecimalFormat("0.000");
	private Classification			previousClassification	= null;
	private HistoricalFloats		averageAccuracy			= new HistoricalFloats(); 
	
	public TestZGrid(Tester tester) {
		super(tester);
	}

	public static void main(String[] args) {
		(new TestZGrid(new Tester())).test(args);
	}

	@Override
	protected void describe() {
		System.out.println("This test shows how to create and configure a *ZGrid* instance to learn and predict sequences of values.");
		System.out.println("A *ZGrid* consists of several rows and columns where each column can process a certain input value.");
		System.out.println("It uses multithreading to maximize the throughput of grid requests.");
		System.out.println("The first row of a *ZGrid* is reserved for *ZGridColumnEncoder* objects that translate request input values into SDRs.");
		System.out.println("The remaining rows can be used for *Pooler*, *Memory*, *Classifier*, *Merger* and custom processors.");
		System.out.println("Context routing can be used to route the output of a column to the context of another column.");
		System.out.println();
		System.out.println("**Example implementation**  ");
		System.out.println("~~~~");
		System.out.println("// Create the grid");
		System.out.println("ZGrid grid = new ZGrid(4,2);");
		System.out.println("// Add encoders");
		System.out.println("grid.setEncoder(0,new ZGridEncoderDateTime());");
		System.out.println("grid.setEncoder(1,new ZGridEncoderValue(256));");
		System.out.println("// Add processors");
		System.out.println("grid.setProcessor(1,0,new PoolerConfig(256,1024,21));");
		System.out.println("grid.setProcessor(1,1,new PoolerConfig(256,1024,21));");
		System.out.println("MemoryConfig config = new MemoryConfig(1024,21);");
		System.out.println("config.addContextDimension(1024);");
		System.out.println("grid.setProcessor(2,1,config);");
		System.out.println("grid.setProcessor(3,1,new ClassifierConfig(1));");
		System.out.println("// Route output from date/time pooler to value memory context");
		System.out.println("grid.addColumnContext(2,1,1,0);");
		System.out.println("// Route output from value encoder to value classifier context");
		System.out.println("grid.addColumnContext(3,1,0,1);");
		System.out.println("// Add a listener for grid results");
		System.out.println("grid.addListener(this);");
		System.out.println("// Start the grid");
		System.out.println("grid.start();");
		System.out.println("// Add requests");
		System.out.println("ZGridRequest request = grid.getNewRequest();");
		System.out.println("request.inputValues[0] = request.datetime;");
		System.out.println("request.inputValues[1] = 1F;");
		System.out.println("request.inputLabels[1] = \"Normal\";");
		System.out.println("long requestId = grid.addRequest(request);");
		System.out.println("// Remember to stop and destroy the grid after use");
		System.out.println("grid.stop();");
		System.out.println("grid.whileActive();");
		System.out.println("grid.destroy();");
		System.out.println("~~~~");
		System.out.println();
		System.out.println("Class references;  ");
		System.out.println(" * " + getTester().getLinkForClass(TestZGrid.class));
		System.out.println(" * " + getTester().getLinkForClass(ZGrid.class));
		System.out.println(" * " + getTester().getLinkForClass(ZGridRequest.class));
		System.out.println(" * " + getTester().getLinkForClass(ZGridColumnEncoder.class));
		System.out.println();
		System.out.println("**Test output**  ");
		System.out.println("The output of this test shows;  ");
		System.out.println(" * A description of the grid after initialization  ");
		System.out.println(" * A JSON representation of the grid configuration  ");
		System.out.println(" * The classifier prediction accuracy of the grid while processing requests   ");
		System.out.println(" * A description of the grid after processing requests  ");
		System.out.println(" * The size of the state data for each grid processor  ");
	}
	
	@Override
	protected void test(String[] args) {
		ZDKFactory factory = new ZDKFactory();
		Messenger messenger = factory.getMessenger();
		messenger.setPrintDebugMessages(true);
		WorkerUnion union = factory.getWorkerUnion(messenger);

		messenger.start();
		
		ZGrid grid = new ZGrid(messenger,union,5,5);
		
		grid.addListener(this);

		// Create encoders
		ZGridEncoderDateTime dateTimeEncoder = new ZGridEncoderDateTime();
		dateTimeEncoder.setIncludeMonth(false);
		dateTimeEncoder.setIncludeDayOfWeek(false);
		dateTimeEncoder.setIncludeHourOfDay(false);
		dateTimeEncoder.setIncludeMinute(false);
		dateTimeEncoder.setScale(2);
		
		ZGridEncoderValue valueEncoder = new ZGridEncoderValue(64);
		valueEncoder.setMaxValue(20);
		
		ZGridEncoderValue posXEncoder = new ZGridEncoderValue(64,"POSX");
		posXEncoder.setMaxValue(20);

		ZGridEncoderValue posYEncoder = new ZGridEncoderValue(64,"POSY");
		posYEncoder.setMaxValue(20);

		ZGridEncoderValue posZEncoder = new ZGridEncoderValue(64,"POSZ");
		posZEncoder.setMaxValue(20);
		
		// Add encoders
		grid.setEncoder(0,dateTimeEncoder);
		grid.setEncoder(1,valueEncoder);
		grid.setEncoder(2,posXEncoder);
		grid.setEncoder(3,posYEncoder);
		grid.setEncoder(4,posZEncoder);
		
		// Add processors
		grid.setProcessor(2,0,new PoolerConfig(dateTimeEncoder.length(),1024,21));
		
		PoolerConfig poolerConfig = new PoolerConfig(valueEncoder.length(),1024,21);
		grid.setProcessor(2,1,poolerConfig);

		MemoryConfig memoryConfig = new MemoryConfig(poolerConfig);
		memoryConfig.addContextDimension(1024);
		memoryConfig.addContextDimension(1024);
		grid.setProcessor(3,1,memoryConfig);

		grid.setProcessor(4,0,new DetectorConfig());

		grid.setProcessor(4,1,new ClassifierConfig(1));

		grid.setProcessor(1,3,new MergerConfig());

		poolerConfig = new PoolerConfig(posXEncoder.length() * 3,1024,21);
		grid.setProcessor(2,3,poolerConfig);
		
		// Route output from position encoders to position merger context
		int[] posColumns = {2,4};
		grid.addColumnContexts(1,3,0,posColumns);
		
		// Route output from dateTime and position poolers to memory context
		int[] dtpColumns = {0,3};
		grid.addColumnContexts(3,1,2,dtpColumns);

		// Route output from value pooler and memory burst to detector context
		grid.addColumnContext(4,0,2,1);
		grid.addColumnContext(4,0,3,1,1);

		// Route value DateTimeSDR from encoder to classifier
		grid.addColumnContext(4,1,0,1);
		
		// Randomize pooler connections
		grid.randomizePoolerConnections();
		
		System.out.println(grid.getDescription());
		
		boolean test = false;
		ZGrid newGrid = new ZGrid(5,5);
		if (testJsAble(grid,newGrid,"Grid JSON does not match expectation")) {
			test = true;
			System.out.println();
			System.out.println("Grid JSON;");
			System.out.println(grid.toJson().toStringBuilderReadFormat());
		}
		
		if (test) {
			System.out.println();
			testGrid(grid);
			testNewGrid(grid,newGrid);
		}
		
		if (previousClassification!=null) {
			previousClassification.labelCounts = new HashMap<String,Integer>();
			previousClassification.labelCounts.put("Pizza",123);
			previousClassification.labelCounts.put("Coffee",45);
			previousClassification.mostCountedLabels = new ArrayList<String>();
			previousClassification.mostCountedLabels.add("Pizza");
			testJsAble(previousClassification,new Classification(),"Classification JSON does not match expectation");
		}
		
		grid.destroy();
		newGrid.destroy();

		messenger.stop();
		messenger.handleMessages();
		union.stopWorkers();
	}

	@Override
	public void processedRequest(ZGrid grid,ZGridResult result) {
		returnedIds.add(result.getRequest().id);
		
		String noClassifications = "";
		List<Classification> classifications = result.getClassifications();
		if (classifications.size()>0) {
			if (previousClassification!=null) {
				Object value = result.getRequest().inputValues[1];
				float accuracy = 0;
				Classification classification = previousClassification;
				for (Object predicted: classification.mostCountedValues) {
					if (predicted.equals(value)) {
						accuracy = 1;
						break;
					}
				}
				averageAccuracy.addFloat(accuracy);
			}
			previousClassification = classifications.get(0);
		} else {
			noClassifications = " (!)";
		}
		
		List<Anomaly> anomalies = result.getAnomalies();
		if (anomalies.size()>0) {
			for (Anomaly anomaly: anomalies) {
				System.out.println("Detected anomaly at id " + result.getRequest().id + ", average: " + anomaly.averageAccuracy + ", detected: " + anomaly.detectedAccuracy + ", difference: " + anomaly.difference);
			}
		}
		
		if (returnedIds.size() % 100 == 0) {
			if (noClassifications.length()>0) {
				for (String columnId: result.getColumnIds()) {
					if (columnId.endsWith("-01")) {
						SDR sdr = result.getColumnOutput(columnId,0);
						if (sdr!=null) {
							assertEqual(sdr.onBits(),0,"Number of on bits does not match expectation");
						}
					}
				}
			}
			System.out.println("Processed requests: " + returnedIds.size() + ", accuracy: " + df.format(averageAccuracy.average) + noClassifications);
		}
		
		if (returnedIds.size()==expectedIds.size()) {
			grid.stop();
		}
	}
	
	private void testGrid(ZGrid grid) {
		// Start grid
		grid.start();
		grid.whileInactive();
		
		long started = System.currentTimeMillis();
		long dateTime = started;
		
		// Add requests
		ZGridRequest request = null;
		for (int c = 1; c <= 300; c++) {
			for (int r = 1; r <= 10; r++) {
				float[] position = new float[3];
				position[0] = 0;
				if (c==275) {
					position[1] = r;
				} else {
					position[1] = r + 10;
				}
				position[2] = r * 2;
				request = grid.getNewRequest();
				request.dateTime = dateTime;
				request.inputValues[0] = request.dateTime;
				if (c<=200 || c>210) {
					request.inputValues[1] = r;
					request.inputValues[2] = 0;
					request.inputValues[3] = r;
					request.inputValues[4] = r * 2;
				}
				expectedIds.add(grid.addRequest(request));
				dateTime += 1000;
			}
		}
		System.out.println("Added requests");
		
		testJsAble(request,grid.getNewRequest(),"Request JSON does not match expectation");
		
		int i = 0;
		while(grid.isActive()) {
			sleep(100);
			i++;
			if (i>=450) {
				break;
			}
		}

		long stopped = System.currentTimeMillis();

		// Stop & destroy grid
		if (grid.isActive()) {
			grid.stop();
		}
		grid.whileActive();

		// Test assertions
		boolean success = false;
		if (assertEqual(returnedIds.size(),expectedIds.size(),"Returned id list size does not match expectation")) {
			success = true;
			i = 0;
			for (Long expectedId: expectedIds) {
				Long returnedId = returnedIds.get(i);
				if (!assertEqual(returnedId,expectedId,"Request (" + i + ") id does not match expectation")) {
					success = false;
					break;
				}
				i++;
			}
		}
		if (success) {
			System.out.println("Processing " + expectedIds.size() + " requests took " + (stopped - started) + " ms");
		}
	}
	
	private void testNewGrid(ZGrid grid,ZGrid newGrid) {
		ZStringBuilder desc = grid.getDescription();
		System.out.println();
		System.out.println(desc);
		
		SortedMap<String,ZStringBuilder> columnIdStateDataMap = grid.getColumnStateData();
		System.out.println();
		System.out.println("Grid column state data;");
		for (Entry<String,ZStringBuilder> entry: columnIdStateDataMap.entrySet()) {
			System.out.println("- " + entry.getKey() + ": " + entry.getValue().length());
		}
		long started = System.currentTimeMillis();
		newGrid.setColumnStateData(columnIdStateDataMap);
		System.out.println("Loading state data took " + (System.currentTimeMillis() - started) + " ms");
		
		ZStringBuilder newDesc = newGrid.getDescription();
		if (!assertEqual(desc.equals(newDesc),true,"New grid description does not match expectation")) {
			System.err.println(newDesc);
		}
	}
}
