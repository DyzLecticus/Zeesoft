package nl.zeesoft.zdk.neural.model;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import nl.zeesoft.zdk.Rand;
import nl.zeesoft.zdk.function.Executor;
import nl.zeesoft.zdk.function.ExecutorTask;
import nl.zeesoft.zdk.function.Function;
import nl.zeesoft.zdk.json.JsonTransient;
import nl.zeesoft.zdk.matrix.MatrixExecutor;

@JsonTransient
public class CellPruner {
	public AtomicInteger	prunedSynapses = new AtomicInteger(); 
			
	public void prune(Cells cells) {
		cells.applyFunction(this, getPruneFunction());
	}
	
	public ExecutorTask prune(Cells cells, Executor executor, Object caller, int timeoutMs) {
		MatrixExecutor exec = new MatrixExecutor(cells, executor) {
			@Override
			protected Function getFunctionForWorker() {
				return getPruneFunction();
			}
		};
		return exec.execute(caller, timeoutMs);
	}
	
	protected Function getPruneFunction() {
		prunedSynapses.set(0);
		Function function = new Function() {
			@Override
			protected Object exec() {
				int pruned = pruneCell((Cell) param2);
				prunedSynapses.addAndGet(pruned);
				return param2;
			}
		};
		return function;
	}

	protected int pruneCell(Cell cell) {
		return
			pruneCellSegments(cell.proximalSegments) +
			pruneCellSegments(cell.distalSegments) +
			pruneCellSegments(cell.apicalSegments);
	}
	
	protected int pruneCellSegments(CellSegments cellSegments) {
		return pruneSynapses(cellSegments) + pruneSegments(cellSegments);
	}
	
	protected int pruneSynapses(CellSegments cellSegments) {
		int r = 0;
		for (Segment segment: cellSegments.segments) {
			List<Synapse> list = new ArrayList<Synapse>(segment.synapses.values());
			for (Synapse synapse: list) {
				if (synapse.permanence < cellSegments.config.pruneMinPermanence &&
					Rand.getRandomFloat(0, 1) <= cellSegments.config.pruneSample
					) {
					segment.removeSynapse(synapse);
					r++;
				}
			}
		}
		return r;
	}
	
	protected int pruneSegments(CellSegments cellSegments) {
		int r = 0;
		List<Segment> list = new ArrayList<Segment>(cellSegments.segments);
		for (Segment segment: list) {
			if (segment.synapses.size() < cellSegments.config.matchingThreshold) {
				cellSegments.segments.remove(segment);
				r += segment.synapses.size();
			}
		}
		return r;
	}
}
