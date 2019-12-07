package nl.zeesoft.zdk.test.impl.htm;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.SortedMap;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.htm.grid.ZGrid;
import nl.zeesoft.zdk.htm.grid.ZGridRequest;
import nl.zeesoft.zdk.htm.grid.ZGridResult;
import nl.zeesoft.zdk.htm.grid.ZGridResultsListener;
import nl.zeesoft.zdk.htm.grid.enc.ZGridEncoderDateTime;
import nl.zeesoft.zdk.htm.grid.enc.ZGridEncoderPosition;
import nl.zeesoft.zdk.htm.grid.enc.ZGridEncoderValue;
import nl.zeesoft.zdk.htm.proc.Classification;
import nl.zeesoft.zdk.htm.proc.ClassifierConfig;
import nl.zeesoft.zdk.htm.proc.MemoryConfig;
import nl.zeesoft.zdk.htm.proc.PoolerConfig;
import nl.zeesoft.zdk.htm.util.HistoricalFloats;
import nl.zeesoft.zdk.test.TestObject;
import nl.zeesoft.zdk.test.Tester;

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
		/* TODO: Describe
		System.out.println("This test shows how to use a *Memory* instance to learn temporal sequences of SDRs.  ");
		System.out.println();
		System.out.println("**Please note** that this implementation differs greatly from the Numenta HTM implementation because it does not model dendrites;  ");
		System.out.println("Memory cells are directly connected to each other and dendrite activation is not limited.  ");
		System.out.println("Further more, distal connections do not need to be randomly initialized when the memory is created.  ");
		System.out.println();
		System.out.println("**Example implementation**  ");
		System.out.println("~~~~");
		System.out.println("// Create the configuration");
		System.out.println("MemoryConfig config = new MemoryConfig(1024);");
		System.out.println("// Create the memory");
		System.out.println("Memory memory = new Memory(config);");
		System.out.println("// Obtain the output SDR for a certain input SDR");
		System.out.println("SDR sdr = memory.getSDRForInput(new SDR(),true);");
		System.out.println("~~~~");
		System.out.println();
		getTester().describeMock(MockRegularSDRMap.class.getName());
		System.out.println();
		System.out.println("Class references;  ");
		System.out.println(" * " + getTester().getLinkForClass(TestGrid.class));
		System.out.println(" * " + getTester().getLinkForClass(SDR.class));
		System.out.println(" * " + getTester().getLinkForClass(MemoryConfig.class));
		System.out.println(" * " + getTester().getLinkForClass(Memory.class));
		System.out.println();
		System.out.println("**Test output**  ");
		System.out.println("The output of this test shows;  ");
		System.out.println(" * How memory column bursting is reduced after leaning several sequences  ");
		System.out.println(" * Information about the memory after passing the SDR test set through it  ");
		*/
	}
	
	@Override
	protected void test(String[] args) {
		ZGrid grid = new ZGrid(4,3);
		
		grid.addListener(this);

		// Create encoders
		ZGridEncoderDateTime dateTimeEncoder = new ZGridEncoderDateTime();
		dateTimeEncoder.setIncludeMonth(false);
		dateTimeEncoder.setIncludeDayOfWeek(false);
		dateTimeEncoder.setIncludeHourOfDay(false);
		dateTimeEncoder.setIncludeMinute(false);
		dateTimeEncoder.setScale(2);
		
		ZGridEncoderValue valueEncoder = new ZGridEncoderValue();
		valueEncoder.setLength(64);
		valueEncoder.setMaxValue(20);
		
		ZGridEncoderPosition positionEncoder = new ZGridEncoderPosition();
		
		// Add encoders
		grid.setEncoder(0,dateTimeEncoder);
		grid.setEncoder(1,valueEncoder);
		grid.setEncoder(2,positionEncoder);
		
		// Add processors
		PoolerConfig poolerConfig = new PoolerConfig(dateTimeEncoder.length(),1024,21);
		grid.setProcessor(1,0,poolerConfig);
		
		poolerConfig = new PoolerConfig(valueEncoder.length(),1024,21);
		grid.setProcessor(1,1,poolerConfig);

		MemoryConfig memoryConfig = new MemoryConfig(poolerConfig);
		memoryConfig.addContextDimension(1024);
		memoryConfig.addContextDimension(1024);
		grid.setProcessor(2,1,memoryConfig);
		
		ClassifierConfig classifierConfig = new ClassifierConfig(1);
		grid.setProcessor(3,1,classifierConfig);
		
		poolerConfig = new PoolerConfig(positionEncoder.length(),1024,21);
		grid.setProcessor(1,2,poolerConfig);
		
		// Route output from dateTime and position poolers to memory context
		grid.addColumnContext(2,1,1,0);
		grid.addColumnContext(2,1,1,2);

		// Route value DateTimeSDR from encoder to classifier
		grid.addColumnContext(3,1,0,1);
		
		// Randomize pooler connections
		grid.randomizePoolerConnections();
		
		System.out.println(grid.getDescription());
		System.out.println();
		
		boolean test = false;
		ZGrid newGrid = new ZGrid(4,3);
		if (testJsAble(grid,newGrid,"Grid JSON does not match expectation")) {
			test = true;
			System.out.println("Grid JSON;");
			System.out.println(grid.toJson().toStringBuilderReadFormat());
		}
		
		if (test) {
			System.out.println();
			testGrid(grid);
			testNewGrid(grid,newGrid);
		}
		
		grid.destroy();
		newGrid.destroy();
		System.out.println();
		System.out.println("Destroyed grid");
	}

	@Override
	public void processedRequest(ZGrid grid,ZGridResult result) {
		returnedIds.add(result.getRequest().id);
		
		List<Classification> classifications = result.getClassifications();
		if (classifications.size()>0) {
			if (previousClassification!=null) {
				Object value = result.getRequest().inputValues[1];
				float accuracy = 0;
				Classification classification = previousClassification;
				for (Object predicted: classification.mostCountedValues) {
					//System.out.println("Predicted: " + predicted + ", value: " + value);
					if (predicted.equals(value)) {
						accuracy = 1;
						break;
					}
				}
				averageAccuracy.addFloat(accuracy);
			}
			previousClassification = classifications.get(0);
		} 
			
		if (returnedIds.size() % 100 == 0) {
			System.out.println("Processed requests: " + returnedIds.size() + ", accuracy: " + df.format(averageAccuracy.average));
		}
		
		if (returnedIds.size()==expectedIds.size()) {
			grid.stop();
		}
	}
	
	private void testGrid(ZGrid grid) {
		// Start grid
		grid.start();
		grid.whileInactive();
		System.out.println("Started grid");
		
		long started = System.currentTimeMillis();
		long dateTime = started;
		
		// Add requests
		for (int c = 1; c <= 300; c++) {
			for (int r = 1; r <= 10; r++) {
				float[] position = new float[3];
				position[0] = 0;
				position[1] = r;
				position[2] = r * 2;
				ZGridRequest request = grid.getNewRequest();
				request.dateTime = dateTime;
				request.inputValues[0] = request.dateTime;
				request.inputValues[1] = (float) r;
				request.inputValues[2] = position;
				expectedIds.add(grid.addRequest(request));
				dateTime += 1000;
			}
		}
		System.out.println("Added requests");
		
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
		System.out.println("Stopped grid");

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
		newGrid.setColumnStateData(columnIdStateDataMap);
		ZStringBuilder newDesc = newGrid.getDescription();
		if (!assertEqual(desc.equals(newDesc),true,"New grid description does not match expectation")) {
			System.err.println(newDesc);
		}
	}
}
