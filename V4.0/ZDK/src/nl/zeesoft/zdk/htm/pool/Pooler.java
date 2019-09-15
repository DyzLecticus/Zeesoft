package nl.zeesoft.zdk.htm.pool;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.functions.ZRandomize;
import nl.zeesoft.zdk.htm.sdr.SDR;

public class Pooler {
	protected PoolerConfig			config		= null;
	
	protected List<PoolerColumn>	columns		= new ArrayList<PoolerColumn>();
	
	public Pooler(PoolerConfig config) {
		this.config = config;
		initialize();
	}

	public void randomizeConnections() {
		for (PoolerColumn col: columns) {
			col.randomizeConnections();
		}
	}
	
	public ZStringBuilder getDescription() {
		ZStringBuilder r = config.getDescription();
		int min = config.inputSize; 
		int max = 0;
		int avg = 0;
		for (PoolerColumn col: columns) {
			int con = 0;
			for (ProximalLink lnk: col.proxLinks.values()) {
				if (lnk.connection>=0) {
					con++;
					avg++;
				}
			}
			if (con<min) {
				min = con;
			}
			if (con>max) {
				max = con;
			}
		}
		if (avg>0) {
			avg = avg / columns.size();
			r.append("\n");
			r.append("Average proximal inputs per column: ");
			r.append("" + avg);
			if (min!=avg || max!=avg) {
				r.append(" (min: ");
				r.append("" + min);
				r.append(", max: ");
				r.append("" + max);
				r.append(")");
			}
		}
		return r;
	}
	
	public SDR getSDRForInput(SDR input,boolean learn) {
		List<Integer> onBits = input.getOnBits();
		calculateOverlapScoresForSDROnBits(onBits);
		List<PoolerColumn> activeColumns = getActiveColumns();
		if (learn) {
			learnActiveColumns(activeColumns,onBits);
		}
		logActivity(activeColumns);
		updateBoostFactors();
		return recordActiveColumnsInSDR(activeColumns);
	}
	
	protected void calculateOverlapScoresForSDROnBits(List<Integer> onBits) {
		for (PoolerColumn col: columns) {
			col.calculateOverlapScoreForOnBits(onBits);
		}
	}
	
	protected List<PoolerColumn> getActiveColumns() {
		List<PoolerColumn> r = new ArrayList<PoolerColumn>();
		SortedMap<Integer,List<PoolerColumn>> map = new TreeMap<Integer,List<PoolerColumn>>();
		for (PoolerColumn col: columns) {
			int boostedScore = (int) ((float)col.overlapScore * col.boostFactor);
			List<PoolerColumn> list = map.get(boostedScore);
			if (list==null) {
				list = new ArrayList<PoolerColumn>();
				map.put(boostedScore,list);
			}
			list.add(col);
		}
		Object[] keys = map.keySet().toArray();
		for (int i = (map.size() - 1); i>=0; i--) {
			List<PoolerColumn> list = map.get(keys[i]);
			if (config.outputBits - r.size() < list.size()) {
				for (int s = 0; s < config.outputBits - r.size(); s++) {
					int sel = ZRandomize.getRandomInt(0,list.size() - 1);
					r.add(list.get(sel));
					list.remove(sel);
				}
			} else {
				for (PoolerColumn col: list) {
					r.add(col);
					if (r.size()>=config.outputBits) {
						break;
					}
				}
			}
			if (r.size()>=config.outputBits) {
				break;
			}
		}
		return r;
	}
	
	protected void learnActiveColumns(List<PoolerColumn> activeColumns,List<Integer> onBits) {
		for (PoolerColumn col: activeColumns) {
			col.learnOnBits(onBits);
		}
	}
	
	protected void logActivity(List<PoolerColumn> activeColumns) {
		if (config.boostStrength>0) {
			for (PoolerColumn col: columns) {
				col.logActivity(activeColumns.contains(col));
			}
		}
	}

	protected void updateBoostFactors() {
		// TODO: if radius squared + 1 > outputSizeX && outputSizeY than calculate global average activity
		float globalAverageActivity = 0;
		if (config.boostStrength>0) {
			for (PoolerColumn col: columns) {
				col.updateBoostFactor(globalAverageActivity);
			}
		}
	}

	protected SDR recordActiveColumnsInSDR(List<PoolerColumn> activeColumns) {
		SDR r = new SDR(config.outputSize);
		for (PoolerColumn col: activeColumns) {
			r.setBit(col.index,true);
		}
		return r;
	}

	protected void initialize() {
		int posX = 0;
		int posY = 0;
		for (int i = 0; i < config.outputSize; i++) {
			PoolerColumn col = new PoolerColumn(config,i,posX,posY);
			columns.add(col);
			posX++;
			if (posX % config.outputSizeX == 0) {
				posX = 0;
				posY++;
			}
		}
		// TODO: Use relative positional self projection
		for (PoolerColumn col: columns) {
			int minPosX = col.posX - config.outputRadius;
			int minPosY = col.posY - config.outputRadius;
			int maxPosX = col.posX + 1 + config.outputRadius;
			int maxPosY = col.posY + 1 + config.outputRadius;

			if (minPosX<0) {
				maxPosX = maxPosX + (minPosX * -1);
				minPosX = 0;
			}
			if (minPosY<0) {
				maxPosY = maxPosY + (minPosY * -1);
				minPosY = 0;
			}
			if (maxPosX>config.outputSizeX) {
				minPosX = minPosX - (maxPosX - config.outputSizeX);
				maxPosX = config.outputSizeX;
			}
			if (maxPosY>config.outputSizeY) {
				minPosY = minPosY - (maxPosY - config.outputSizeY);
				maxPosY = config.outputSizeY;
			}
			if (minPosX<0) {
				minPosX = 0;
			}
			if (minPosY<0) {
				minPosY = 0;
			}
			
			for (PoolerColumn colC: columns) {
				if (colC.posX>=minPosX && colC.posX<maxPosX && colC.posY>=minPosY && colC.posY<maxPosY) {
					col.localAreaColumns.add(colC);
				}
			}
		}
	}
}
