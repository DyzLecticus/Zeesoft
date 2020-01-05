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
import nl.zeesoft.zdk.htm.grid.ZGridFactory;
import nl.zeesoft.zdk.htm.grid.ZGridRequest;
import nl.zeesoft.zdk.htm.grid.ZGridResult;
import nl.zeesoft.zdk.htm.grid.ZGridResultsListener;
import nl.zeesoft.zdk.htm.proc.Anomaly;
import nl.zeesoft.zdk.htm.proc.Classification;
import nl.zeesoft.zdk.htm.util.HistoricalFloats;
import nl.zeesoft.zdk.htm.util.SDR;
import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.test.TestObject;
import nl.zeesoft.zdk.test.Tester;
import nl.zeesoft.zdk.thread.WorkerUnion;

public class TestZGrid extends TestObject implements ZGridResultsListener {
	private List<Long>				expectedIds				= new ArrayList<Long>();
	private List<Long>				returnedIds				= new ArrayList<Long>();

	private long					firstExpectedAnomalyId	= 0;
	private long					firstDetectedAnomalyId	= 0;

	private DecimalFormat			df						= new DecimalFormat("0.000");
	private Classification			previousClassification	= null;
	private HistoricalFloats		averageAccuracy			= new HistoricalFloats();
	private ZGridResult 			lastResult				= null;
	
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
		System.out.println("// Randomize grid pooler connections");
		System.out.println("grid.randomizePoolerConnections();");
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
		
		ZGridFactory gridFactory = new ZGridFactory(messenger,union);
		gridFactory.initializeTestGrid();
		assertEqual(gridFactory.testConfiguration(),new ZStringBuilder(),"Factory configuration test results do not match expectation");
		testJsAble(gridFactory,new ZGridFactory(),"Grid factory JSON does not match expectation");
		
		ZGrid grid = gridFactory.buildNewGrid();
		grid.addListener(this);
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
			
			if (lastResult!=null) {
				testJsAble(lastResult,new ZGridResult(messenger),"Grid result JSON does not match expectation");
			}
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
		lastResult = result;
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
				if (accuracy==1) {
					accuracy = (accuracy / (float) classification.mostCountedValues.size());
				}
				averageAccuracy.addFloat(accuracy);
			}
			previousClassification = classifications.get(0);
		} else {
			noClassifications = " (!)";
		}
		
		List<Anomaly> anomalies = result.getAnomalies();
		if (anomalies.size()>0) {
			if (firstDetectedAnomalyId==0) {
				firstDetectedAnomalyId = result.getRequest().id;
			}
			for (Anomaly anomaly: anomalies) {
				System.out.println("Detected anomaly at id " + result.getRequest().id + ", detected: " + anomaly.detectedAccuracy + ", average: " + anomaly.averageLongTermAccuracy + ", difference: " + anomaly.difference);
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
				request = grid.getNewRequest();
				request.dateTime = dateTime;
				request.inputValues[0] = request.dateTime;
				if (c<=200 || c>210) {
					if (c>=275 && c<=278) {
						request.inputValues[1] = r + 19;
					} else {
						request.inputValues[1] = r;
					}
					request.inputValues[2] = 0;
					request.inputValues[3] = r;
					request.inputValues[4] = r * 2;
				}
				long id = grid.addRequest(request);
				expectedIds.add(id);
				if (c>=275 && c<=278 && firstExpectedAnomalyId==0) {
					firstExpectedAnomalyId = id;
				}
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
		boolean withinRange = (firstDetectedAnomalyId >= firstExpectedAnomalyId && firstDetectedAnomalyId <= firstExpectedAnomalyId + 10);
		assertEqual(withinRange,true,"Detected anomaly request id is not within the expected range");
		if (success) {
			System.out.println("Processing " + expectedIds.size() + " requests took " + (stopped - started) + " ms");
		}
	}
	
	private void testNewGrid(ZGrid grid,ZGrid newGrid) {
		ZStringBuilder desc = grid.getDescription();
		System.out.println();
		System.out.println(desc);
		
		SortedMap<String,ZStringBuilder> columnIdStateDataMap = grid.getStateData();
		System.out.println();
		System.out.println("Grid state data;");
		for (Entry<String,ZStringBuilder> entry: columnIdStateDataMap.entrySet()) {
			System.out.println("- " + entry.getKey() + ": " + entry.getValue().length());
		}
		long started = System.currentTimeMillis();
		newGrid.setStateData(columnIdStateDataMap);
		System.out.println("Loading state data took " + (System.currentTimeMillis() - started) + " ms");
		
		ZStringBuilder newDesc = newGrid.getDescription();
		if (!assertEqual(desc.equals(newDesc),true,"New grid description does not match expectation")) {
			System.err.println(newDesc);
		}
	}
}
