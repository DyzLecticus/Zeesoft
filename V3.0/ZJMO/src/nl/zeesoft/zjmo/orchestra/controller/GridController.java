package nl.zeesoft.zjmo.orchestra.controller;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import nl.zeesoft.zjmo.orchestra.OrchestraMember;

@SuppressWarnings("serial")
public class GridController extends AbstractTableModel {
	private List<OrchestraMember>	members		= new ArrayList<OrchestraMember>();
	private String[]				headers		= new String[5];
	
	public GridController() {
		headers[0] = "ID";
		headers[1] = "State";
		headers[2] = "Error";
		headers[3] = "Work load";
		headers[4] = "Memory usage";
	}

	protected void updatedOrchestraMembers(List<OrchestraMember> members) {
		this.members = members;
	}
	
	@Override
    public int getColumnCount() {
		return headers.length;
    }

	@Override
    public int getRowCount() {
		return members.size();
    }

	@Override
    public String getColumnName(int col) {
		return headers[col];
    }

	@Override
    public Class<?> getColumnClass(int col) {
		Class<?> r = super.getColumnClass(col);
		if (headers[col].endsWith("Control port") ||
			headers[col].endsWith("Work port") ||
			headers[col].equals("Work load") ||
			headers[col].equals("Memory usage")
			) {
			r = Integer.class;
		} else {
			r = String.class;
		}
		return r;
    }

	@Override
    public boolean isCellEditable(int row, int col) {
        return false;
    }

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Object r = null;
		OrchestraMember member = members.get(rowIndex);
		if (headers[columnIndex].equals("ID")) {
			r = member.getId();
		} else if (headers[columnIndex].equals("Address")) {
			r = member.getIpAddressOrHostName();
		} else if (headers[columnIndex].equals("Control port")) {
			r = member.getControlPort();
		} else if (headers[columnIndex].equals("Work port")) {
			r = member.getWorkPort();
		} else if (headers[columnIndex].equals("State")) {
			r = member.getState().getCode();
		} else if (headers[columnIndex].equals("Work load")) {
			r = member.getWorkLoad();
		} else if (headers[columnIndex].equals("Memory usage")) {
			r = member.getMemoryUsage();
		} else if (headers[columnIndex].equals("Error")) {
			if (member.getErrorDate()!=null && member.getErrorMessage().length()>0) {
				r = member.getErrorDate().getDateTimeString() + ": " + member.getErrorMessage();   
			}
		}
    	return r;
	}
}
