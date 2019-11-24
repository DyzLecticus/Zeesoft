package nl.zeesoft.zdk.test.impl.htm;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.htm.grid.ZGrid;
import nl.zeesoft.zdk.htm.grid.ZGridEncoderDateTime;
import nl.zeesoft.zdk.htm.grid.ZGridEncoderPosition;
import nl.zeesoft.zdk.htm.grid.ZGridListener;
import nl.zeesoft.zdk.htm.grid.ZGridRequest;
import nl.zeesoft.zdk.htm.proc.Pooler;
import nl.zeesoft.zdk.htm.proc.PoolerConfig;
import nl.zeesoft.zdk.htm.util.SDR;
import nl.zeesoft.zdk.test.TestObject;
import nl.zeesoft.zdk.test.Tester;

public class TestZGrid extends TestObject implements ZGridListener {
	private List<Long>	expectedIds		= new ArrayList<Long>();
	private List<Long>	returnedIds		= new ArrayList<Long>();
	
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
		
		ZGridEncoderDateTime dateTimeEncoder = new ZGridEncoderDateTime();
		assertEqual(dateTimeEncoder.testScalarOverlap(),new ZStringBuilder(),"Scalar overlap test results do not match expectation for scale 1");
		SDR sdr = dateTimeEncoder.getSDRForDateTime(System.currentTimeMillis());
		assertEqual(sdr.length(),256,"SDR (scale: 1) length does not match expectation");
		
		dateTimeEncoder.setScale(2);
		assertEqual(dateTimeEncoder.testScalarOverlap(),new ZStringBuilder(),"Scalar overlap test results do not match expectation for scale 2");
		sdr = dateTimeEncoder.getSDRForDateTime(System.currentTimeMillis());
		assertEqual(sdr.length(),529,"SDR (scale: 2) length does not match expectation");
		
		dateTimeEncoder.setScale(4);
		assertEqual(dateTimeEncoder.testScalarOverlap(),new ZStringBuilder(),"Scalar overlap test results do not match expectation for scale 4");
		sdr = dateTimeEncoder.getSDRForDateTime(System.currentTimeMillis());
		assertEqual(sdr.length(),1024,"SDR (scale: 4) length does not match expectation");
		
		// Add encoders
		grid.setEncoder(0,new ZGridEncoderDateTime());
		grid.setEncoder(2,new ZGridEncoderPosition(256,100,100,100));
		
		// Add processors
		PoolerConfig poolerConfig = new PoolerConfig(256,1024,21);
		Pooler dateTimePooler = new Pooler(poolerConfig);
		dateTimePooler.randomizeConnections();

		poolerConfig = new PoolerConfig(256,1024,21);
		Pooler positionPooler = new Pooler(poolerConfig);
		positionPooler.randomizeConnections();

		grid.setProcessor(1,0,dateTimePooler);
		grid.setProcessor(1,2,positionPooler);

		// Start grid
		grid.start();
		while(!grid.isWorking()) {
			sleep(10);
		}
		System.out.println("Started grid");
		
		// Add requests
		for (int r = 0; r < 10; r++) {
			int[] position = new int[3];
			position[0] = 4;
			position[1] = 5;
			position[2] = 6;
			ZGridRequest request = new ZGridRequest(3);
			request.inputValues[0] = request.dateTime + (r * 1000);
			request.inputValues[1] = r + 1;
			request.inputValues[2] = position;
			expectedIds.add(grid.addRequest(request));
		}
		System.out.println("Added requests");
		
		int i = 0;
		while(grid.isWorking()) {
			sleep(100);
			i++;
			if (i>=10) {
				break;
			}
		}
		
		// Stop & destroy grid
		grid.stop();
		System.out.println("Stopped grid");
		grid.destroy();
		System.out.println("Destroyed grid");

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
			System.out.println("Succesfully processed " + expectedIds.size() + " requests");
		}
	}

	@Override
	public void processedRequest(ZGridRequest request) {
		returnedIds.add(request.id);
		
		System.out.println("================================================");
		for (Entry<String,SDR> entry: request.columnOutputs.entrySet()) {
			ZStringBuilder value = new ZStringBuilder("null");
			if (entry.getValue()!=null) {
				value = entry.getValue().toStringBuilder();
			}
			System.out.println(entry.getKey() + " = " + value);
		}
	}
}
