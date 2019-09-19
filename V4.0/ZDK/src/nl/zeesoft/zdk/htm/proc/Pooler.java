package nl.zeesoft.zdk.htm.proc;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.functions.ZRandomize;
import nl.zeesoft.zdk.htm.sdr.SDR;

public class Pooler implements Processable {
	protected PoolerConfig							config				= null;

	protected PoolerStats							stats				= new PoolerStats();
	
	protected List<PoolerColumn>					columns				= new ArrayList<PoolerColumn>();
	protected SortedMap<String,PoolerColumnGroup>	columnGroups		= new TreeMap<String,PoolerColumnGroup>();
	
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
		int min = config.inputLength; 
		int max = 0;
		int avg = 0;
		for (PoolerColumn col: columns) {
			int con = 0;
			for (ProximalLink lnk: col.proxLinks) {
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
			r.append("- Average proximal inputs per column: ");
			r.append("" + avg);
			if (min!=avg || max!=avg) {
				r.append(" (min: ");
				r.append("" + min);
				r.append(", max: ");
				r.append("" + max);
				r.append(")");
			}
		}
		min = config.outputLength;
		max = 0;
		avg = 0;
		for (PoolerColumnGroup pcg: columnGroups.values()) {
			avg = avg + pcg.columns.size();
			if (pcg.columns.size()<min) {
				min = pcg.columns.size();
			}
			if (pcg.columns.size()>max) {
				max = pcg.columns.size();
			}
		}
		if (avg>0) {
			avg = avg / columnGroups.size();
			r.append("\n");
			r.append("- Column groups: ");
			r.append("" + columnGroups.size());
			r.append(", average columns per group: ");
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

	@Override
	public SDR getSDRForInput(SDR input,boolean learn) {
		long total = System.nanoTime();
		SDR r = null;
		List<Integer> onBits = input.getOnBits();
		long start = 0;
		
		start = System.nanoTime();
		calculateOverlapScoresForSDROnBits(onBits);
		stats.calculateOverlapNs += System.nanoTime() - start;
		
		start = System.nanoTime();
		List<PoolerColumn> activeColumns = selectActiveColumns();
		stats.selectActiveNs += System.nanoTime() - start;
		
		if (learn) {
			start = System.nanoTime();
			learnActiveColumns(activeColumns,onBits);
			stats.learnActiveNs += System.nanoTime() - start;
		}
		
		if (config.boostStrength>0) {
			start = System.nanoTime();
			logActivity(activeColumns);
			stats.logActiveNs += System.nanoTime() - start;
			
			start = System.nanoTime();
			calculateColumnGroupActivity();
			stats.calculateActivityNs += System.nanoTime() - start;
			
			start = System.nanoTime();
			updateBoostFactors();
			stats.updateBoostNs += System.nanoTime() - start;
		}
		
		r = recordActiveColumnsInSDR(activeColumns);
		
		stats.total++;
		stats.totalNs += System.nanoTime() - total;
		
		return r;
	}
	
	@Override
	public void resetStats() {
		stats = new PoolerStats();
	}
	
	@Override
	public PoolerStats getStats() {
		return stats;
	}
	
	protected void calculateOverlapScoresForSDROnBits(List<Integer> onBits) {
		for (PoolerColumn col: columns) {
			col.calculateOverlapScoreForOnBits(onBits);
		}
	}
	
	protected List<PoolerColumn> selectActiveColumns() {
		List<PoolerColumn> r = new ArrayList<PoolerColumn>();
		SortedMap<Integer,List<PoolerColumn>> map = new TreeMap<Integer,List<PoolerColumn>>();
		for (PoolerColumn col: columns) {
			if (col.overlapScore>0) {
				int boostedScore = (int) ((float)col.overlapScore * col.boostFactor);
				List<PoolerColumn> list = map.get(boostedScore);
				if (list==null) {
					list = new ArrayList<PoolerColumn>();
					map.put(boostedScore,list);
				}
				list.add(col);
			}
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

	protected void calculateColumnGroupActivity() {
		if (config.boostStrength>0) {
			for (PoolerColumnGroup pcg: columnGroups.values()) {
				pcg.calculateAverageActivity();
			}
		}
	}
	
	protected void updateBoostFactors() {
		if (config.boostStrength>0) {
			for (PoolerColumn col: columns) {
				col.updateBoostFactor();
			}
		}
	}
	
	protected SDR recordActiveColumnsInSDR(List<PoolerColumn> activeColumns) {
		SDR r = new SDR(config.outputLength);
		for (PoolerColumn col: activeColumns) {
			r.setBit(col.index,true);
		}
		return r;
	}

	protected void initialize() {
		// Initialize columns
		int posX = 0;
		int posY = 0;
		for (int i = 0; i < config.outputLength; i++) {
			PoolerColumn col = new PoolerColumn(config,i,posX,posY);
			columns.add(col);
			posX++;
			if (posX % config.outputSizeX == 0) {
				posX = 0;
				posY++;
			}
		}
		
		// Initialize column groups
		for (PoolerColumn col: columns) {
			posX = col.getRelativePosX();
			posY = col.getRelativePosY();

			int minPosX = posX - config.outputRadius;
			int minPosY = posY - config.outputRadius;
			int maxPosX = posX + 1 + config.outputRadius;
			int maxPosY = posY + 1 + config.outputRadius;

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
			
			PoolerColumnGroup columnGroup = columnGroups.get(PoolerColumnGroup.getId(minPosX,minPosY));
			if (columnGroup==null) {
				columnGroup = new PoolerColumnGroup(minPosX,minPosY); 
				columnGroups.put(columnGroup.getId(),columnGroup);
				for (PoolerColumn colC: columns) {
					if (colC.posX>=minPosX && colC.posX<maxPosX && colC.posY>=minPosY && colC.posY<maxPosY) {
						columnGroup.columns.add(colC);
					}
				}
			}
			col.columnGroup = columnGroup;
		}
	}
}
