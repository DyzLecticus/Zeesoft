package nl.zeesoft.zdk.htm.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zdk.functions.ZRandomize;
import nl.zeesoft.zdk.htm.mdl.Column;
import nl.zeesoft.zdk.htm.mdl.ColumnGroup;
import nl.zeesoft.zdk.htm.mdl.Model;
import nl.zeesoft.zdk.htm.mdl.ProximalSynapse;
import nl.zeesoft.zdk.htm.sdr.SDR;

public class SpatialPooler extends Model {
	private SpatialPoolerConfig					poolerConfig					= null;
	
	protected List<SpatialPoolerColumn>			poolerColumns					= new ArrayList<SpatialPoolerColumn>();
	protected HashMap<Integer,Set<Integer>> 	connectedIndexesPerInputIndex	= new HashMap<Integer,Set<Integer>>();
	
	public SpatialPooler(Model model,SpatialPoolerConfig config) {
		super(model.config);
		model.copyTo(this,true,false);
		this.poolerConfig = config;
		config.initialized = true;
		initializeConnectedIndexesPerInputIndex();
	}
	
	public SDR getOutputSDRForInputSDR(SDR input,boolean learn) {
		SDR r = null;
		
		List<Integer> onBits = input.getOnBits();
		int[] columnOverlapScores = getColumnOverlapScores(onBits);
		Set<Column> activeColumns = selectActiveColumns(columnOverlapScores);
		
		if (learn) {
			learnOnBits(activeColumns,onBits);
		}
		
		if (poolerConfig.boostStrength>0) {
			logActivity(activeColumns);
			HashMap<ColumnGroup,Float> activity = calculateColumnGroupActivity();
			updateBoostFactors(activity);
		}
		
		r = recordActiveColumnsInSDR(activeColumns);
		
		return r;
	}
	
	public List<ProximalSynapse> initializeProximalDendriteSynapses() {
		clearProximalDendriteSynapses();
		List<ProximalSynapse> r = new ArrayList<ProximalSynapse>();
		for (SpatialPoolerColumn column: poolerColumns) {
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

	protected List<ProximalSynapse> initializeProximalDendriteSynapses(SpatialPoolerColumn column) {
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
				if (synapse.permanence>poolerConfig.permanenceThreshold) {
					Set<Integer> list = connectedIndexesPerInputIndex.get(synapse.inputIndex);
					if (list==null) {
						list = new HashSet<Integer>();
						connectedIndexesPerInputIndex.put(synapse.inputIndex,list);
					}
					list.add(column.index);
				}
			}
		}
	}
	
	protected int[] getColumnOverlapScores(List<Integer> onBits) {
		int[] r = new int[config.getOutputLength()];
		for (int i = 0; i < r.length; i++) {
			r[i] = 0;
		}
		for (Integer onBit: onBits) {
			Set<Integer> list = connectedIndexesPerInputIndex.get(onBit);
			if (list!=null) {
				for (Integer index: list) {
					r[index]++;
				}
			}
		}
		return r;
	}
	
	protected Set<Column> selectActiveColumns(int[] columnOverlapScores) {
		Set<Column> r = new HashSet<Column>();
		SortedMap<Integer,List<Column>> map = new TreeMap<Integer,List<Column>>();
		int i = 0;
		for (SpatialPoolerColumn column: poolerColumns) {
			if (columnOverlapScores[i]>0) {
				int boostedScore = (int) ((float)columnOverlapScores[i] * column.boostFactor);
				List<Column> list = map.get(boostedScore);
				if (list==null) {
					list = new ArrayList<Column>();
					map.put(boostedScore,list);
				}
				list.add(column);
			}
			i++;
		}
		Object[] keys = map.keySet().toArray();
		for (i = (map.size() - 1); i>=0; i--) {
			List<Column> list = map.get(keys[i]);
			if (config.getOutputBits() - r.size() < list.size()) {
				for (int s = 0; s < config.getOutputBits() - r.size(); s++) {
					int sel = ZRandomize.getRandomInt(0,list.size() - 1);
					r.add(list.get(sel));
					list.remove(sel);
				}
			} else {
				for (Column col: list) {
					r.add(col);
					if (r.size()>=config.getOutputBits()) {
						break;
					}
				}
			}
			if (r.size()>=config.getOutputBits()) {
				break;
			}
		}
		return r;
	}

	protected void learnOnBits(Set<Column> activeColumns,List<Integer> onBits) {
		for (Column column: activeColumns) {
			for (ProximalSynapse synapse: column.proximalDendrite.synapses) {
				if (onBits.contains(synapse.inputIndex)) {
					if (synapse.permanence <= poolerConfig.permanenceThreshold &&
						synapse.permanence + poolerConfig.permanenceIncrement > poolerConfig.permanenceThreshold) {
						Set<Integer> list = connectedIndexesPerInputIndex.get(synapse.inputIndex);
						if (list==null) {
							list = new HashSet<Integer>();
							connectedIndexesPerInputIndex.put(synapse.inputIndex,list);
						}
						list.add(column.index);
					}
					synapse.permanence += poolerConfig.permanenceIncrement;
					if (synapse.permanence > 1) {
						synapse.permanence = 1;
					}
				} else {
					if (synapse.permanence > poolerConfig.permanenceThreshold &&
						synapse.permanence - poolerConfig.permanenceDecrement <= poolerConfig.permanenceThreshold) {
						Set<Integer> list = connectedIndexesPerInputIndex.get(synapse.inputIndex);
						if (list!=null) {
							list.remove((Integer)column.index);
							if (list.size()==0) {
								connectedIndexesPerInputIndex.remove(synapse.inputIndex);
							}
						}
					}
					synapse.permanence -= poolerConfig.permanenceDecrement;
					if (synapse.permanence < 0) {
						synapse.permanence = 0;
					}
				}
			}
		}
	}
	
	protected void logActivity(Set<Column> activeColumns) {
		if (poolerConfig.boostStrength>0) {
			for (SpatialPoolerColumn column: poolerColumns) {
				boolean active = activeColumns.contains(column);
				column.logActivity(active, poolerConfig);
			}
		}
	}

	protected HashMap<ColumnGroup,Float> calculateColumnGroupActivity() {
		HashMap<ColumnGroup,Float> r = new HashMap<ColumnGroup,Float>();
		if (poolerConfig.boostStrength>0) {
			for (ColumnGroup columnGroup: columnGroupsById.values()) {
				float averageActivity = 0;
				for (Column column: columnGroup.columns) {
					if (column instanceof SpatialPoolerColumn) {
						averageActivity += ((SpatialPoolerColumn)column).averageActivity;
					}
				}
				if (averageActivity>0) {
					averageActivity = averageActivity / columns.size();
					r.put(columnGroup,averageActivity);
				}
			}
		}
		return r;
	}
	
	protected void updateBoostFactors(HashMap<ColumnGroup,Float> activity) {
		if (poolerConfig.boostStrength>0) {
			for (SpatialPoolerColumn column: poolerColumns) {
				if (activity.containsKey(column.columnGroup)) {
					column.updateBoostFactors(poolerConfig,activity.get(column.columnGroup));
				}
			}
		}
	}
	
	protected SDR recordActiveColumnsInSDR(Set<Column> activeColumns) {
		SDR r = config.getNewSDR();
		for (Column column: activeColumns) {
			r.setBit(column.index,true);
		}
		return r;
	}
	
	@Override
	protected void addColumn(Column column,boolean includeProximalDendrites,boolean includeCells) {
		SpatialPoolerColumn sc = new SpatialPoolerColumn(config,column);
		column.copyTo(sc,includeProximalDendrites, includeCells);
		poolerColumns.add(sc);
		super.addColumn(sc,includeProximalDendrites,includeCells);
	}
}
