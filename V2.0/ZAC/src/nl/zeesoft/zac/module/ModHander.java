package nl.zeesoft.zac.module;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zac.database.model.Module;
import nl.zeesoft.zac.module.object.ObjModule;
import nl.zeesoft.zodb.Worker;

public class ModHander {
	private ObjModule				modules			= new ObjModule();
	private List<ModWorker>			workers			= new ArrayList<ModWorker>();	
	
	protected void handle() {
		// Stop inactive modules
		List<ModWorker> testWorkers = new ArrayList<ModWorker>(workers); 
		for (ModWorker worker: testWorkers) {
			for (Module module: modules.getModulesAsList()) {;
				if (worker.getModule().getId()==module.getId()) {
					if (!module.isActive()) {
						worker.stop();
						workers.remove(worker);
					}
					break;
				}
			}
		}
		// Start active modules
		for (Module module: modules.getModulesAsList()) {
			if (module.isActive()) {
				boolean foundTrainer = false;
				boolean foundTester = false;
				for (ModWorker worker: workers) {
					if (worker.getModule().getId()==module.getId()) {
						if (worker instanceof ModTrainingWorker) {
							foundTrainer = true;
						} else if (worker instanceof ModTestingWorker) {
							foundTester = true;
						}
						if (foundTrainer && foundTester) {
							break;
						}
					}
				}
				if (!foundTrainer) {
					ModWorker worker = new ModTrainingWorker(module);
					workers.add(worker);
					worker.start();
				}
				if (!foundTester) {
					ModWorker worker = new ModTestingWorker(module);
					workers.add(worker);
					worker.start();
				}
			}
		}
	}

	protected void stopHandling() {
		for (Worker worker: workers) {
			worker.stop();
		}
		workers.clear();
	}

	protected void refreshModules() {
		modules.reinitialize();
	}
}
