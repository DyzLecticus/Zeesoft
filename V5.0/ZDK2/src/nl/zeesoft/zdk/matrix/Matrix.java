package nl.zeesoft.zdk.matrix;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.Util;
import nl.zeesoft.zdk.function.Function;

public class Matrix {
	public Size 			size	= null;
	public Object[][][]		data	= null;
		
	public void initialize(Size size) {
		this.size = size.copy();
		this.data = new Object[this.size.x][this.size.y][this.size.z];
	}
	
	@Override
	public boolean equals(Object other) {
		boolean r = false;
		if (other!=null && other instanceof Matrix) {
			Matrix matrix = (Matrix) other;
			if (size==null && matrix.size==null) {
				r = true;
			} else if (
				size!=null && 
				matrix.size!=null &&
				size.equals(matrix.size)
				) {
				r = true;
				for (int x = 0; x < size.x; x++) {
					for (int y = 0; y < size.y; y++) {
						for (int z = 0; z < size.z; z++) {
							if (!Util.equals(data[x][y][z], matrix.data[x][y][z])) {
								r = false;
								break;
							}
						}
					}
				}
			}
		}
		return r;
	}
	
	public int volume() {
		int r = 0;
		if (size!=null) {
			r = size.volume();
		}
		return r;
	}

	public void applyFunction(Object caller, Function function) {
		if (size!=null) {
			for (int x = 0; x < size.x; x++) {
				for (int y = 0; y < size.y; y++) {
					for (int z = 0; z < size.z; z++) {
						function.param1 = new Position(x,y,z);
						function.param2 = data[x][y][z];
						data[x][y][z] = function.execute(caller);
					}
				}
			}
		}
	}

	public void setValue(Object caller, Object value) {
		Function function = new Function() {
			@Override
			protected Object exec() {
				return value;
			}
		};
		applyFunction(caller,function);
	}

	public void setValue(Position position, Object value) {
		data[position.x][position.y][position.z] = value;
	}

	public Object getValue(Position position) {
		return data[position.x][position.y][position.z];
	}
	
	public List<Position> getPositionsForValue(Object caller, Object value) {
		List<Position> r = new ArrayList<Position>();
		Function function = new Function() {
			@Override
			protected Object exec() {
				if ((value==null && param2==null) ||
					(param2!=null && value!=null && param2.equals(value))
					) {
					r.add((Position)param1);
				}
				return param2;
			}
		};
		applyFunction(caller,function);
		return r;
	}
	
	public Matrix copy(Object caller) {
		Matrix r = new Matrix();
		if (size!=null) {
			r.initialize(size);
			r.copyDataFrom(caller, this);
		}
		return r;
	}
	
	public void copyDataFrom(Object caller, Matrix other) {
		if (this.size!=null && other.size!=null && this.size.equals(other.size)) {
			Function function = new Function() {
				@Override
				protected Object exec() {
					Object value = other.getValue((Position) param1);
					if (value instanceof Matrix) {
						value = ((Matrix) value).copy(caller);
					}
					return value;
				}
			};
			applyFunction(caller,function);
		}
	}
}
