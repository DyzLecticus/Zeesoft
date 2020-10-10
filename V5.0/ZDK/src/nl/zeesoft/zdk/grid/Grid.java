package nl.zeesoft.zdk.grid;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zdk.Rand;
import nl.zeesoft.zdk.thread.CodeRunnerList;
import nl.zeesoft.zdk.thread.RunCode;

public class Grid {
	protected int 				sizeX			= 2;
	protected int 				sizeY			= 2;
	protected int 				sizeZ			= 1;
	
	protected List<GridColumn>	columns 		= new ArrayList<GridColumn>();
	protected GridColumn[][]	columnsByPos	= null;
	
	public Grid() {
		
	}
	
	public Grid(Grid grid) {
		copyFrom(grid);
	}
	
	public void copyFrom(Grid grid) {
		initialize(grid.sizeX(), grid.sizeY(), grid.sizeZ());
		copyValuesFrom(grid.getColumns());
	}
	
	public void copyValuesFrom(List<GridColumn> columns) {
		if (columns.size()>0) {
			for (GridColumn column: columns) {
				GridColumn columnTo = getColumn(column.index);
				if (columnTo!=null) {
					for (int z = 0; z < sizeZ; z++) {
						columnTo.setValue(z, copyValueFrom(column,z));
					}
				} else {
					break;
				}
			}
		}
	}
	
	public void initialize(int sizeX, int sizeY) {
		initialize(sizeX,sizeY,sizeZ,null);
	}
	
	public void initialize(int sizeX, int sizeY, int sizeZ) {
		initialize(sizeX,sizeY,sizeZ,null);
	}
	
	public void initialize(int sizeX, int sizeY, int sizeZ, Object value) {
		if (sizeX < 1) {
			sizeX = 1;
		}
		if (sizeY < 1) {
			sizeY = 1;
		}
		if (sizeZ < 1) {
			sizeZ = 1;
		}
		this.sizeX = sizeX;
		this.sizeY = sizeY;
		this.sizeZ = sizeZ;
		columns.clear();
		columnsByPos = new GridColumn[sizeX][sizeY];
		int index = 0;
		for (int x = 0; x < sizeX; x++) {
			for (int y = 0; y < sizeY; y++) {
				GridColumn column = new GridColumn();
				column.index = index;
				column.posX = x;
				column.posY = y;
				column.initialize(sizeZ, value);
				columns.add(column);
				columnsByPos[x][y] = column;
				index++;
			}
		}
	}
	
	public void flatten() {
		if (sizeY>1) {
			List<GridColumn> columns = getColumns();
			initialize(columns.size(), 1, sizeZ);
			copyValuesFrom(columns);
		} else if (sizeZ>1) {
			List<GridColumn> columns = getColumns();
			int oriSizeZ = sizeZ;
			int newSizeZ = columns.size() * sizeZ;
			initialize(newSizeZ, 1, 1);
			for (GridColumn column: columns) {
				boolean stop = false;
				int s = column.index * oriSizeZ;
				int e = s + oriSizeZ;
				int z = 0;
				for (int x = s; x < e; x++) {
					GridColumn columnTo = getColumn(x, 0);
					if (columnTo!=null) {
						columnTo.setValue(column.getValue(z));
					} else {
						stop = true;
						break;
					}
					z++;
				}
				if (stop) {
					break;
				}
			}
		}
	}
	
	public void square() {
		if (sizeX>=4 && sizeY==1) {
			List<GridColumn> columns = getColumns();
			int sX = (int) Math.sqrt(sizeX);
			int sY = sX;
			if (sX * sY < columns.size()) {
				sX = sX + 1;
			}
			if (sX * sY < columns.size()) {
				sY = sY + 1;
			}
			initialize(sX, sY, sizeZ);
			copyValuesFrom(columns);
		}
	}

	public int getPosXOn(Grid grid, int posX) {
		float relative = (float)posX / (float)sizeX;
		return (int)((float)grid.sizeX() * relative);
	}

	public int getPosYOn(Grid grid, int posY) {
		float relative = (float)posY / (float)sizeY;
		return (int)((float)grid.sizeY() * relative);
	}

	public int getPosZOn(Grid grid, int posZ) {
		float relative = (float)posZ / (float)sizeZ;
		return (int)((float)grid.sizeZ() * relative);
	}

	public static int getDistance(int pos1X, int pos1Y, int pos2X, int pos2Y) {
		int distX = 0;
		int distY = 0;
		if (pos1X > pos2X) {
			distX = pos1X - pos2X;
		} else {
			distX = pos2X - pos1X;
		}
		if (pos1Y > pos2Y) {
			distY = pos1Y - pos2Y;
		} else {
			distY = pos2Y - pos1Y;
		}
		return (int) Math.sqrt((distX * distX) + (distY * distY));
	}
	
	public int sizeX() {
		return sizeX;
	}
	
	public int sizeY() {
		return sizeY;
	}
	
	public int sizeZ() {
		return sizeZ;
	}
	
	public List<GridColumn> getColumns() {
		return new ArrayList<GridColumn>(columns);
	}
	
	public GridColumn getColumn(int posX, int posY) {
		GridColumn r = null;
		if (columnsByPos!=null && posX < sizeX && posY < sizeY) {
			r = columnsByPos[posX][posY];
		}
		return r;
	}
	
	public GridColumn getColumn(int index) {
		GridColumn r = null;
		if (index < columns.size()) {
			r = columns.get(index);
		}
		return r;
	}
	
	public void setValue(Object value) {
		for (int x = 0; x < sizeX; x++) {
			for (int y = 0; y < sizeY; y++) {
				columnsByPos[x][y].setValue(value);
			}
		}
	}
	
	public void setValue(int posX, int posY, Object value) {
		setValue(posX,posY,0,value);
	}
	
	public void setValue(int posX, int posY, int posZ, Object value) {
		if (columnsByPos!=null && posX < sizeX && posY < sizeY && posZ < sizeZ) {
			columnsByPos[posX][posY].setValue(posZ,value);
		}
	}
	
	public void setValue(List<Position> positions, Object value) {
		for (Position pos: positions) {
			setValue(pos.x, pos.y, pos.z, value);
		}
	}
	
	public Object getValue(int posX, int posY) {
		return getValue(posX,posY,0);
	}
	
	public Object getValue(int posX, int posY, int posZ) {
		Object r = null;
		if (columnsByPos!=null && posX < sizeX && posY < sizeY && posZ < sizeZ) {
			r = columnsByPos[posX][posY].getValue(posZ);
		}
		return r;
	}
	
	public float getAverageValue() {
		float r = 0;
		if (columns.size()>0) {
			for (GridColumn column: columns) {
				r = r + column.getAverageValue();
			}
			r = r / (float)columns.size();
		}
		return r;
	}
	
	public void applyFunction(ColumnFunction function) {
		applyFunction(function, null);
	}
	
	public void applyFunction(ColumnFunction function, CodeRunnerList runnerList) {
		if (runnerList!=null) {
			int axis = 0;
			if (sizeY > sizeX) {
				axis = 1;
			}
			if (axis==0) {
				for (int x = 0; x < sizeX; x++) {
					RunCode code = new RunCode() {
						@Override
						protected boolean run() {
							int x = (int) params[0];
							for (int y = 0; y < sizeY; y++) {
								columnsByPos[x][y].applyFunction(function);
							}
							return true;
						}
					};
					code.params[0] = x;
					runnerList.add(code);
				}
			} else {
				for (int y = 0; y < sizeY; y++) {
					RunCode code = new RunCode() {
						@Override
						protected boolean run() {
							int y = (int) params[0];
							for (int x = 0; x < sizeX; x++) {
								columnsByPos[x][y].applyFunction(function);
							}
							return true;
						}
					};
					code.params[0] = y;
					runnerList.add(code);
				}
			}
		} else {
			for (GridColumn column: columns) {
				column.applyFunction(function);
			}
		}
	}
	
	public SortedMap<Float,List<GridColumn>> getColumnsByValue() {
		SortedMap<Float,List<GridColumn>> r = new TreeMap<Float,List<GridColumn>>();
		for (GridColumn column: columns) {
			Object value = column.getValue();
			if (value!=null) {
				float val = Float.MIN_VALUE;
				if (value instanceof Float) {
					val = (float)value;
				} else if (value instanceof Integer) {
					val = (int)value;
				}
				List<GridColumn> cols = r.get(val);
				if (cols==null) {
					cols = new ArrayList<GridColumn>();
					r.put(val,cols);
				}
				cols.add(column);
			}
		}
		return r;
	}
	
	public List<GridColumn> getColumnsByValue(boolean ascending) {
		return getColumnsByValue(ascending, 0);
	}
	
	public List<GridColumn> getColumnsByValue(boolean ascending, int limit) {
		List<GridColumn> r = new ArrayList<GridColumn>();
		SortedMap<Float,List<GridColumn>> sorted = getColumnsByValue();
		for (Entry<Float,List<GridColumn>> entry: sorted.entrySet()) {
			List<GridColumn> columns = entry.getValue();
			int size = columns.size();
			for (int i = 0; i < size; i++) {
				GridColumn add = columns.remove(Rand.getRandomInt(0,columns.size()-1));
				if (ascending) {
					r.add(add);
				} else {
					r.add(0,add);
				}
				
				if (ascending && limit>0 && r.size()>=limit) {
					break;
				}
			}
		}
		if (!ascending && limit>0 && r.size() > limit) {
			int remove = r.size() - limit;
			for (int n = 0; n < remove; n++) {
				r.remove(limit);
			}
		}
		return r;
	}
	
	public List<Position> getValuePositions(Object value) {
		List<Position> r = new ArrayList<Position>();
		for (GridColumn column: columns) {
			r.addAll(column.getValuePositions(value));
		}
		return r;
	}
	
	protected Object copyValueFrom(GridColumn column, int posZ) {
		return column.getValue(posZ);
	}
}
