package nl.zeesoft.zdk.htm.mdl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

public class Model {
	protected ModelConfig						config			= null;
	
	protected List<Column>						columns			= new ArrayList<Column>();
	protected SortedMap<String,ColumnGroup>		columnGroups	= new TreeMap<String,ColumnGroup>();
	
	protected HashMap<String,ModelObject>		objectsById		= new HashMap<String,ModelObject>();
	
	public Model(ModelConfig config) {
		this.config = config;
		initialize();
	}
	
	public int size() {
		return objectsById.size();
	}
	
	public void initializeProximalDendriteSynapses() {
		for (Column column: columns) {
			List<ProximalSynapse> synapses = column.initializeProximalDendriteSynapses();
			for (ProximalSynapse synapse: synapses) {
				objectsById.put(synapse.getId(),synapse);
			}
		}
	}
	
	public void clearProximalDendriteSynapses() {
		for (Column column: columns) {
			column.proximalDendrite.synapses.clear();
		}
	}
	
	protected ModelObject getModelObjectById(String id) {
		return objectsById.get(id);
	}
	
	protected void initialize() {
		config.initialized = true;
		
		int posX = 0;
		int posY = 0;
		for (int i = 0; i < config.outputLength; i++) {
			Column column = new Column(config,i,posX,posY);
			columns.add(column);
			objectsById.put(column.getId(),column);
			posX++;
			if (posX % config.columnSizeX == 0) {
				posX = 0;
				posY++;
			}
		}
		
		// Initialize column groups
		for (Column column: columns) {
			posX = column.getRelativePosX();
			posY = column.getRelativePosY();

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
			
			ColumnGroup columnGroup = columnGroups.get(ColumnGroup.getId(minPosX,minPosY));
			if (columnGroup==null) {
				columnGroup = new ColumnGroup(minPosX,minPosY); 
				columnGroups.put(columnGroup.getId(),columnGroup);
				objectsById.put(columnGroup.getId(),columnGroup);
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
				objectsById.put(cell.getId(),cell);
			}
		}
		
		// Initialize proximal segments
		for (Column column: columns) {
			ProximalDendrite dendrite = new ProximalDendrite(column.getId(),0);
			objectsById.put(dendrite.getId(),dendrite);
			column.proximalDendrite = dendrite;
			for (Cell cell: column.cells) {
				cell.proximalDendrites.add(dendrite);
			}
		}
		
		// Initialize distal segments
		for (Column column: columns) {
			for (Cell cell: column.cells) {
				for (int i = 0; i < config.columnDepth; i++) {
					DistalDendrite dendrite = new DistalDendrite(cell.getId(),i);
					objectsById.put(dendrite.getId(),dendrite);
					cell.distalDendrites.add(dendrite);
				}
			}
		}
	}
}
