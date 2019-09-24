package nl.zeesoft.zdk.htm.mdl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zdk.ZStringBuilder;

public class Model {
	public static final String[] SIZE_LABELS = {
		"Columns",
		"Column groups",
		"Cells",
		"Proximal dendrites",
		"Proximal synapses",
		"Distal dendrites",
		"Distal synapses"
	};

	public ModelConfig							config					= null;
	
	private long								uid						= 0;

	protected List<Column>						columns					= new ArrayList<Column>();

	protected HashMap<String,Column>			columnsById				= new HashMap<String,Column>();
	protected HashMap<String,ColumnGroup>		columnGroupsById		= new HashMap<String,ColumnGroup>();
	protected HashMap<String,Cell>				cellsById				= new HashMap<String,Cell>();
	protected HashMap<String,ProximalDendrite>	proximalDendritesById	= new HashMap<String,ProximalDendrite>();
	protected HashMap<String,ProximalSynapse>	proximalSynapsesById	= new HashMap<String,ProximalSynapse>();
	protected HashMap<String,DistalDendrite>	distalDendritesById		= new HashMap<String,DistalDendrite>();
	protected HashMap<String,DistalSynapse>		distalSynapsesById		= new HashMap<String,DistalSynapse>();
		
	public Model(ModelConfig config) {
		config.initialized = true;
		this.config = config;
	}

	public Model copy() {
		return copyTo(new Model(config),true,true);
	}

	public Model copyTo(Model copy,boolean includeProximalDendrites,boolean includeCells) {
		for (ColumnGroup group: columnGroupsById.values()) {
			copy.putObject(group.copy());
		}
		for (Column column: columns) {
			Column columnCopy = column.copy(includeProximalDendrites,includeCells);
			copy.columns.add(columnCopy);
			copy.putObject(columnCopy);
			ColumnGroup groupCopy = copy.columnGroupsById.get(column.columnGroup.getId());
			columnCopy.columnGroup = groupCopy;
			if (includeProximalDendrites) {
				copy.putObject(columnCopy.proximalDendrite);
				for (ProximalSynapse synapse: columnCopy.proximalDendrite.synapses) {
					copy.putObject(synapse);
				}
			}
			if (includeCells) {
				for (Cell cell: columnCopy.cells) {
					copy.putObject(cell);
					for (DistalDendrite dendrite: cell.distalDendrites) {
						copy.putObject(dendrite);
						for (DistalSynapse synapse: dendrite.synapses) {
							copy.putObject(synapse);
						}
					}
				}
			}
		}
		for (ColumnGroup groupCopy: copy.columnGroupsById.values()) {
			ColumnGroup group = columnGroupsById.get(groupCopy.getId());
			for (Column column: group.columns) {
				Column columnCopy = copy.columnsById.get(column.getId());
				groupCopy.columns.add(columnCopy);
			}
		}
		return copy;
	}
	
	public int size() {
		int r = columnsById.size();
		r += columnGroupsById.size();
		r += cellsById.size();
		r += proximalDendritesById.size();
		r += proximalSynapsesById.size();
		r += distalDendritesById.size();
		r += distalSynapsesById.size();
		return r;
	}

	public int[] sizes() {
		int[] r = {
			columnsById.size(),
			columnGroupsById.size(),
			cellsById.size(),
			proximalDendritesById.size(),
			proximalSynapsesById.size(),
			distalDendritesById.size(),
			distalSynapsesById.size()
		};
		return r;
	}
	
	public ZStringBuilder getDescription() {
		ZStringBuilder r = new ZStringBuilder();
		int[] sizes = sizes();
		for (int i = 0; i < Model.SIZE_LABELS.length; i++) {
			if (sizes[i]>0) {
				if (r.length()>0) {
					r.append("\n");
				}
				r.append("- ");
				r.append(Model.SIZE_LABELS[i]);
				r.append(": ");
				r.append("" + sizes[i]);
			}
		}
		return r;
	}
	
	public String putObject(ModelObject modelObject) {
		if (modelObject.getId().length()==0) {
			uid++;
			modelObject.setId(modelObject.getClass().getSimpleName() + ":" + uid);
		}
		if (modelObject instanceof Column) {
			columnsById.put(modelObject.getId(),(Column)modelObject);
		} else if (modelObject instanceof ColumnGroup) {
			columnGroupsById.put(modelObject.getId(),(ColumnGroup)modelObject);
		} else if (modelObject instanceof Cell) {
			cellsById.put(modelObject.getId(),(Cell)modelObject);
		} else if (modelObject instanceof ProximalDendrite) {
			proximalDendritesById.put(modelObject.getId(),(ProximalDendrite)modelObject);
		} else if (modelObject instanceof ProximalSynapse) {
			proximalSynapsesById.put(modelObject.getId(),(ProximalSynapse)modelObject);
		} else if (modelObject instanceof DistalDendrite) {
			distalDendritesById.put(modelObject.getId(),(DistalDendrite)modelObject);
		} else if (modelObject instanceof DistalSynapse) {
			distalSynapsesById.put(modelObject.getId(),(DistalSynapse)modelObject);
		}
		return modelObject.getId();
	}
	
	public void removeObject(ModelObject modelObject) {
		if (modelObject instanceof Column) {
			columnsById.remove(modelObject.getId());
		} else if (modelObject instanceof ColumnGroup) {
			columnGroupsById.remove(modelObject.getId());
		} else if (modelObject instanceof Cell) {
			cellsById.remove(modelObject.getId());
		} else if (modelObject instanceof ProximalDendrite) {
			proximalDendritesById.remove(modelObject.getId());
		} else if (modelObject instanceof ProximalSynapse) {
			proximalSynapsesById.remove(modelObject.getId());
		} else if (modelObject instanceof DistalDendrite) {
			distalDendritesById.remove(modelObject.getId());
		} else if (modelObject instanceof DistalSynapse) {
			distalSynapsesById.remove(modelObject.getId());
		}
	}

	public void clearProximalDendriteSynapses() {
		for (Column column: columns) {
			column.proximalDendrite.synapses.clear();
		}
		proximalSynapsesById.clear();
	}

	public void clearDistalDendrites() {
		for (Cell cell: cellsById.values()) {
			cell.distalDendrites.clear();
		}
		distalDendritesById.clear();
		distalSynapsesById.clear();
	}

	public void initialize() {
		int posX = 0;
		int posY = 0;
		for (int i = 0; i < config.outputLength; i++) {
			Column column = new Column(config,i,posX,posY);
			putObject(column);
			columns.add(column);
			posX++;
			if (posX % config.columnSizeX == 0) {
				posX = 0;
				posY++;
			}
		}
		
		// Initialize column groups
		SortedMap<String,ColumnGroup> columnGroupsByPosId = new TreeMap<String,ColumnGroup>();
		for (Column column: columns) {
			posX = column.getColumnPosX();
			posY = column.getColumnPosY();

			int minPosX = posX - config.distalRadius;
			int minPosY = posY - config.distalRadius;
			int maxPosX = posX + 1 + config.distalRadius;
			int maxPosY = posY + 1 + config.distalRadius;

			if (minPosX<0) {
				maxPosX = maxPosX + (minPosX * -1);
				minPosX = 0;
			}
			if (minPosY<0) {
				maxPosY = maxPosY + (minPosY * -1);
				minPosY = 0;
			}
			if (maxPosX>config.columnSizeX) {
				minPosX = minPosX - (maxPosX - config.columnSizeX);
				maxPosX = config.columnSizeX;
			}
			if (maxPosY>config.columnSizeY) {
				minPosY = minPosY - (maxPosY - config.columnSizeY);
				maxPosY = config.columnSizeY;
			}
			if (minPosX<0) {
				minPosX = 0;
			}
			if (minPosY<0) {
				minPosY = 0;
			}
			
			ColumnGroup columnGroup = columnGroupsByPosId.get(ColumnGroup.getPosId(minPosX,minPosY));
			if (columnGroup==null) {
				columnGroup = new ColumnGroup(minPosX,minPosY);
				columnGroupsByPosId.put(columnGroup.getPosId(),columnGroup);
				putObject(columnGroup);
				for (Column colC: columns) {
					if (colC.posX>=minPosX && colC.posX<maxPosX && colC.posY>=minPosY && colC.posY<maxPosY) {
						columnGroup.columns.add(colC);
					}
				}
			}
			column.columnGroup = columnGroup;
		}
		
		// Initialize column cells
		for (Column column: columns) {
			for (int d = 0; d < config.columnDepth; d++) {
				Cell cell = new Cell(column.posX,column.posY,d);
				column.cells.add(cell);
				putObject(cell);
			}
		}
		
		// Initialize proximal dendrites
		for (Column column: columns) {
			ProximalDendrite dendrite = new ProximalDendrite(column.getId());
			putObject(dendrite);
			column.proximalDendrite = dendrite;
			for (Cell cell: column.cells) {
				cell.proximalDendrites.add(dendrite);
			}
		}
	}
}
