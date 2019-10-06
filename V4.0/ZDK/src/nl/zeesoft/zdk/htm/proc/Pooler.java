package nl.zeesoft.zdk.htm.proc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.functions.ZRandomize;
import nl.zeesoft.zdk.htm.util.SDR;

/**
 * A (Spatial) Pooler is used to transform encoder SDRs into sparse SDRs for temporal memory processing.
 */
public class Pooler extends ProcessorObject {
	protected PoolerConfig							config			= null;
	
	protected List<PoolerColumn>					columns			= new ArrayList<PoolerColumn>();
	protected SortedMap<String,PoolerColumnGroup>	columnGroups	= new TreeMap<String,PoolerColumnGroup>();
	protected PoolerConnections						connections		= null;
	
	public Pooler(PoolerConfig config) {
		this.config = config;
		config.initialized = true;
		connections = new PoolerConnections(config);
		initialize();
	}

	/**
	 * Initializes proximal links for all columns.
	 * 
	 * This method must be called before processing SDRs if the state of this pooler has not yet been initialized from a string builder.
	 * Without it, the spatial pooler will not have any proximal links to learn from input SDRs.
	 */
	public void randomizeConnections() {
		for (PoolerColumn col: columns) {
			col.randomizeConnections(connections);
		}
	}
	
	/**
	 * Returns a description of this spatial pooler.
	 * 
	 * @return A description of this spatial pooler
	 */
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
	protected SDR getSDRForInputSDR(SDR input,boolean learn) {
		SDR r = null;
		List<Integer> onBits = input.getOnBits();
		long start = 0;
		
		start = System.nanoTime();
		int[] columnOverlapScores = calculateOverlapScores(onBits);
		logStatsValue("calculateOverlapScores",System.nanoTime() - start);
		
		start = System.nanoTime();
		List<PoolerColumn> activeColumns = selectActiveColumns(columnOverlapScores);
		logStatsValue("selectActiveColumns",System.nanoTime() - start);
		
		if (learn) {
			start = System.nanoTime();
			learnActiveColumnsOnBits(activeColumns,onBits);
			logStatsValue("learnActiveColumnsOnBits",System.nanoTime() - start);
		}
		
		if (config.boostStrength>0) {
			start = System.nanoTime();
			logActivity(activeColumns);
			logStatsValue("logActivity",System.nanoTime() - start);
			
			start = System.nanoTime();
			HashMap<PoolerColumnGroup,Float> groupActivity = calculateColumnGroupActivity();
			logStatsValue("calculateColumnGroupActivity",System.nanoTime() - start);
			
			start = System.nanoTime();
			updateBoostFactors(groupActivity);
			logStatsValue("updateBoostFactors",System.nanoTime() - start);
		}
		
		r = recordActiveColumnsInSDR(activeColumns);
		
		return r;
	}
	
	@Override
	public ZStringBuilder toStringBuilder() {
		ZStringBuilder r = new ZStringBuilder();
		for (PoolerColumn col: columns) {
			if (r.length()>0) {
				r.append("|");
			}
			boolean first = true;
			for (ProximalLink lnk: col.proxLinks) {
				if (!first) {
					r.append(";");
				}
				r.append("" + lnk.connection);
				r.append(",");
				r.append("" + lnk.inputIndex);
				first = false;
			}
		}
		return r;
	}
	
	@Override
	public void fromStringBuilder(ZStringBuilder str) {
		List<ZStringBuilder> cols = str.split("|");
		if (cols.size()==columns.size()) {
			connections.clear();
			for (int i = 0; i < cols.size(); i++) {
				PoolerColumn col = columns.get(i);
				col.proxLinks.clear();
				List<ZStringBuilder> links = cols.get(i).split(";");
				for (ZStringBuilder link: links) {
					List<ZStringBuilder> vals = link.split(",");
					if (vals.size()==2) {
						ProximalLink lnk = new ProximalLink();
						lnk.connection = Float.parseFloat(vals.get(0).toString());
						lnk.inputIndex = Integer.parseInt(vals.get(1).toString());
						col.proxLinks.add(lnk);
						connections.addColumnLink(col,lnk);
					}
				}
			}
		}
	}
	
	@Override
	public void destroy() {
		connections.clear();
		for (PoolerColumnGroup columnGroup: columnGroups.values()) {
			columnGroup.columns.clear();
		}
		columnGroups.clear();
		for (PoolerColumn col: columns) {
			col.proxLinks.clear();
		}
		columns.clear();
	}
	
	protected int[] calculateOverlapScores(List<Integer> onBits) {
		int[] r = new int[config.outputLength];
		for (int i = 0; i < r.length; i++) {
			r[i] = 0;
		}
		for (Integer onBit: onBits) {
			Set<Integer> list = connections.connectedIndexesPerInputIndex.get(onBit);
			if (list!=null) {
				for (Integer index: list) {
					r[index]++;
				}
			}
		}
		return r;
	}
	
	protected List<PoolerColumn> selectActiveColumns(int[] columnOverlapScores) {
		List<PoolerColumn> r = new ArrayList<PoolerColumn>();
		SortedMap<Integer,List<PoolerColumn>> map = new TreeMap<Integer,List<PoolerColumn>>();
		for (PoolerColumn col: columns) {
			if (columnOverlapScores[col.index]>0) {
				int boostedScore = (int) ((float)columnOverlapScores[col.index] * col.boostFactor);
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
	
	protected void learnActiveColumnsOnBits(List<PoolerColumn> activeColumns,List<Integer> onBits) {
		for (PoolerColumn col: activeColumns) {
			col.learnOnBits(onBits,connections);
		}
	}
	
	protected void logActivity(List<PoolerColumn> activeColumns) {
		if (config.boostStrength>0) {
			for (PoolerColumn col: columns) {
				col.logActivity(activeColumns.contains(col));
			}
		}
	}

	protected HashMap<PoolerColumnGroup,Float> calculateColumnGroupActivity() {
		HashMap<PoolerColumnGroup,Float> r = new HashMap<PoolerColumnGroup,Float>();
		if (config.boostStrength>0) {
			for (PoolerColumnGroup columnGroup: columnGroups.values()) {
				float averageActivity = 0;
				for (PoolerColumn col: columnGroup.columns) {
					averageActivity += col.activityLog.average;
				}
				if (averageActivity>0) {
					averageActivity = averageActivity / columns.size();
					r.put(columnGroup,averageActivity);
				}
			}
		}
		return r;
	}
	
	protected void updateBoostFactors(HashMap<PoolerColumnGroup,Float> activity) {
		if (config.boostStrength>0) {
			for (PoolerColumn column: columns) {
				if (activity.containsKey(column.columnGroup)) {
					column.updateBoostFactor(activity.get(column.columnGroup));
				}
			}
		}
	}
	
	protected SDR recordActiveColumnsInSDR(List<PoolerColumn> activeColumns) {
		SDR r = config.getNewSDR();
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

			int minPosX = posX - config.boostInhibitionRadius;
			int minPosY = posY - config.boostInhibitionRadius;
			int maxPosX = posX + 1 + config.boostInhibitionRadius;
			int maxPosY = posY + 1 + config.boostInhibitionRadius;

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
