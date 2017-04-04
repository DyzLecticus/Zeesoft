package nl.zeesoft.zso.test;

import java.util.List;

import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zdk.test.LibraryObject;
import nl.zeesoft.zdk.test.TestObject;
import nl.zeesoft.zdk.test.Tester;
import nl.zeesoft.zdk.test.impl.ZDK;
import nl.zeesoft.zjmo.Orchestrator;
import nl.zeesoft.zjmo.orchestra.Orchestra;
import nl.zeesoft.zjmo.test.ZJMO;
import nl.zeesoft.zso.composition.Composition;
import nl.zeesoft.zso.composition.sequencer.Sequencer;

public class ZSO extends LibraryObject {
	public ZSO(Tester tester) {
		super(tester);
		setNameAbbreviated("ZSO");
		setNameFull("Zeesoft Sample Orchestration");
		setBaseProjectUrl("https://github.com/DyzLecticus/Zeesoft/tree/master/V3.0/ZSO/");
		setBaseReleaseUrl("https://github.com/DyzLecticus/Zeesoft/raw/master/V3.0/ZSO/releases/");
		setBaseSrcUrl("https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZSO/");
		getDependencies().add(new ZDK(null));
		getDependencies().add(new ZJMO(null));
	}

	public static void main(String[] args) {
		if (args!=null && args.length>1 && Orchestrator.isOrchestratorAction(args[0])) {
			Orchestrator.main(args);
		} else if (args!=null && args.length>1 && args[0].equals(Sequencer.PLAY_COMPOSITION)) {
			String err = "";
			Orchestra orch = Orchestrator.getOrchestraForClassName(args[1]);
			Composition comp = null;
			if (orch==null) {
				err = "The second parameter must refer to a valid orchestra class name";
			}
			if (err.length()==0) {
				JsFile json = new JsFile();
				err = json.fromFile("orchestra.json");
				if (err.length()==0) {
					orch.fromJson(json);
				}
			}
			if (err.length()==0) {
				JsFile json = new JsFile();
				err = json.fromFile("composition.json");
				if (err.length()==0) {
					comp = new Composition();
					comp.fromJson(json);
				}
			}
			if (err.length()==0) {
				Sequencer seq = new Sequencer(orch,comp);
				seq.start();
			}
			if (err.length()>0) {
				System.err.println(err);
				System.exit(1);
			}
		} else {
			(new ZSO(new Tester())).describeAndTest(args);
		}
	}

	@Override
	public void describe() {
		System.out.println("Zeesoft Sample Orchestration");
		System.out.println("============================");
		System.out.println("Zeesoft Sample Orchestration (ZSO) is an extendable sample based orchestra that can play compositions.");
		System.out.println("The goal of this software is to demonstrate the scalability, maintainability and fault tolerance of the Zeesoft JSON Machine Orchestration.");
		System.out.println();
		describeDependencies();
		System.out.println();
		describeRelease();
		System.out.println();
		describeTesting(ZSO.class);
		System.out.println();
	}

	@Override
	public void addTests(List<TestObject> tests) {
		tests.add(new TestDemoComposition(getTester()));
	}
}
