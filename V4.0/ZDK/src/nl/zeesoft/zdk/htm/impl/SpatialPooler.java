package nl.zeesoft.zdk.htm.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import nl.zeesoft.zdk.functions.ZRandomize;
import nl.zeesoft.zdk.htm.mdl.Column;
import nl.zeesoft.zdk.htm.mdl.Model;
import nl.zeesoft.zdk.htm.mdl.ProximalSynapse;
import nl.zeesoft.zdk.htm.sdr.SDR;

public class SpatialPooler extends Model {
	private SpatialPoolerConfig					poolerConfig					= null;
	
	protected HashMap<Integer,Set<Integer>> 	connectedIndexesPerInputIndex	= new HashMap<Integer,Set<Integer>>();
	
	public SpatialPooler(Model model,SpatialPoolerConfig config) {
		super(model.config);
		model.copyTo(this,true,false);
		this.poolerConfig = config;
		config.initialized = true;
		initializeConnectedIndexesPerInputIndex();
	}
	
	public List<ProximalSynapse> initializeProximalDendriteSynapses() {
		clearProximalDendriteSynapses();
		List<ProximalSynapse> r = new ArrayList<ProximalSynapse>();
		for (Column column: columns) {
			List<ProximalSynapse> synapses = initializeProximalDendriteSynapses(column);
			for (ProximalSynapse synapse: synapses) {
				putObject(synapse);
				r.add(synapse);
			}
		}
		initializeConnectedIndexesPerInputIndex();
		return r;
	}

	@Override
	public void clearProximalDendriteSynapses() {
		super.clearProximalDendriteSynapses();
		connectedIndexesPerInputIndex.clear();
	}
	
	protected SDR getSDRForInputSDR(SDR input,boolean learn,List<SDR> context) {
		SDR r = null;
		
		List<Integer> onBits = input.getOnBits();
		// TODO: Calculate overlap and select active columns
		
		r = config.getNewSDR();
		return r;
	}

	protected List<ProximalSynapse> initializeProximalDendriteSynapses(Column column) {
		List<ProximalSynapse> r = new ArrayList<ProximalSynapse>();
		column.proximalDendrite.synapses.clear();
		List<Integer> inputIndices = column.calculateInputIndices(column);
		List<ProximalSynapse> availableSynapses = new ArrayList<ProximalSynapse>();
		int i = 0;
		for (Integer idx: inputIndices) {
			ProximalSynapse synapse = new ProximalSynapse(column.proximalDendrite.getId(),idx);
			availableSynapses.add(synapse);
		}
		int sel = (int) ((float) availableSynapses.size() * poolerConfig.proximalConnections);
		for (i = 0; i < sel; i++) {
			ProximalSynapse synapse = availableSynapses.remove(ZRandomize.getRandomInt(0,availableSynapses.size() - 1));
			column.proximalDendrite.synapses.add(synapse);
			if (ZRandomize.getRandomInt(0,1)==1) {
				synapse.permanence = ZRandomize.getRandomFloat(0,poolerConfig.permanenceThreshold);
			} else {
				synapse.permanence = ZRandomize.getRandomFloat(poolerConfig.permanenceThreshold,1.0F);
			}
			r.add(synapse);
		}
		return r;
	}
	
	protected void initializeConnectedIndexesPerInputIndex() {
		connectedIndexesPerInputIndex.clear();
		for (Column column: columns) {
			for (ProximalSynapse synapse: column.proximalDendrite.synapses) {
				Set<Integer> list = connectedIndexesPerInputIndex.get(synapse.inputIndex);
				if (list==null) {
					list = new HashSet<Integer>();
					connectedIndexesPerInputIndex.put(synapse.inputIndex,list);
				}
				list.add(columns.indexOf(column));
			}
		}
	}
}
