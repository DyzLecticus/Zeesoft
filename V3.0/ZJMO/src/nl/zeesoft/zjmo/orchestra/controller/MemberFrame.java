package nl.zeesoft.zjmo.orchestra.controller;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.text.NumberFormat;
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import nl.zeesoft.zjmo.orchestra.Orchestra;
import nl.zeesoft.zjmo.orchestra.OrchestraMember;
import nl.zeesoft.zjmo.orchestra.Position;

public class MemberFrame {
	private	Orchestra				orchestra				= null;
	private OrchestraMember			member					= null;
	
	private JFrame 					frame					= null;
	private JComboBox<String>		position				= null;
	private JFormattedTextField		positionBackupNumber	= null;
	private JFormattedTextField		ipAddressOrHostName		= null;
	private JFormattedTextField		workPort				= null;
	private JFormattedTextField		controlPort				= null;
	private JFormattedTextField		workRequestTimeout		= null;
	private JCheckBox				workRequestTimeoutDrain	= null;
	
	private JButton					save					= new JButton("Save");
	
	public MemberFrame(OrchestraController controller, Orchestra orchestra) {
		this.orchestra = orchestra;
		frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.addWindowListener(controller.getNewAdapter());
		frame.setTitle("Member details");
		frame.setSize(400,400);
		frame.setIconImage(new ImageIconLabel("z",32,Color.WHITE).getBufferedImage());
		
		JPanel details = new JPanel();
		details.setLayout(new GridBagLayout());
		details.setPreferredSize(new Dimension(300,300));
		details.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
		
		int row = 0;
		
		position = new JComboBox<String>();
		for (Position pos: orchestra.getPositions()) {
			position.addItem(pos.getName());
		}
		addLabel(details,row,"Position");
		addProperty(details,row,position);
		
		row++;
		NumberFormat fmt = NumberFormat.getIntegerInstance(Locale.US);
		fmt.setGroupingUsed(false);
		fmt.setMaximumIntegerDigits(2);
		positionBackupNumber = new JFormattedTextField(fmt);
		addLabel(details,row,"Backup number");
		addProperty(details,row,positionBackupNumber);

		row++;
		ipAddressOrHostName = new JFormattedTextField();
		addLabel(details,row,"IP or host name");
		addProperty(details,row,ipAddressOrHostName);
		
		row++;
		fmt = NumberFormat.getIntegerInstance(Locale.US);
		fmt.setGroupingUsed(false);
		fmt.setMaximumIntegerDigits(5);
		controlPort = new JFormattedTextField(fmt);
		addLabel(details,row,"Control port");
		addProperty(details,row,controlPort);
		
		row++;
		fmt = NumberFormat.getIntegerInstance(Locale.US);
		fmt.setGroupingUsed(false);
		fmt.setMaximumIntegerDigits(5);
		workPort = new JFormattedTextField(fmt);
		addLabel(details,row,"Work port");
		addProperty(details,row,workPort);

		row++;
		fmt = NumberFormat.getIntegerInstance(Locale.US);
		fmt.setGroupingUsed(false);
		fmt.setMaximumIntegerDigits(5);
		workRequestTimeout = new JFormattedTextField(fmt);
		addLabel(details,row,"Work request timeout");
		addProperty(details,row,workRequestTimeout);

		row++;
		workRequestTimeoutDrain = new JCheckBox();
		addLabel(details,row,"Work request timeout drain");
		addProperty(details,row,workRequestTimeoutDrain);

		row++;
		save.addActionListener(controller);
		save.setActionCommand(OrchestraController.SAVE_MEMBER);
		addButton(details,row,save);

		row++;
		JPanel filler = new JPanel();
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.anchor = GridBagConstraints.PAGE_END;
		gbc.weightx = 1.0;
		gbc.weighty = 0.99;
		gbc.gridwidth = 2;
		gbc.gridx = 0;
		gbc.gridy = row;
		details.add(filler,gbc);

		JScrollPane scroller = new JScrollPane(details);
		frame.setContentPane(scroller);
	}
	
	protected OrchestraMember getMember() {
		return member;
	}

	protected OrchestraMember getSaveMember() {
		OrchestraMember member = null;
		if (getMember()==null) {
			member = new OrchestraMember();
			member.setPosition(orchestra.getPosition(position.getSelectedItem().toString()));
			member.setPositionBackupNumber(Integer.parseInt(positionBackupNumber.getValue().toString()));
			member.setIpAddressOrHostName(ipAddressOrHostName.getValue().toString());
		} else {
			member = getMember().getCopy();
		}
		member.setControlPort(Integer.parseInt(controlPort.getValue().toString()));
		member.setWorkPort(Integer.parseInt(workPort.getValue().toString()));
		member.setWorkRequestTimeout(Integer.parseInt(workRequestTimeout.getValue().toString()));
		member.setWorkRequestTimeoutDrain(workRequestTimeoutDrain.isSelected());
		return member;
	}

	protected void setMember(OrchestraMember member) {
		this.member = member;

		boolean enabled = false;
		if (member==null) {
			enabled = true;
		}
		position.setEnabled(enabled);
		positionBackupNumber.setEnabled(enabled);
		ipAddressOrHostName.setEnabled(enabled);
		
		if (member==null) {
			frame.setTitle("Add member");
			position.setSelectedIndex(0);
			positionBackupNumber.setValue(0);
			ipAddressOrHostName.setValue(Orchestra.LOCALHOST);
			controlPort.setValue(4322);
			workPort.setValue(4321);
			workRequestTimeout.setValue(500);
			workRequestTimeoutDrain.setSelected(false);
		} else {
			frame.setTitle("Update " + member.getId());
			Position pos = orchestra.getPosition(member.getPosition().getName());
			position.setSelectedIndex(orchestra.getPositions().indexOf(pos));
			positionBackupNumber.setValue(member.getPositionBackupNumber());
			ipAddressOrHostName.setValue(member.getIpAddressOrHostName());
			controlPort.setValue(member.getControlPort());
			workPort.setValue(member.getWorkPort());
			workRequestTimeout.setValue(member.getWorkRequestTimeout());
			workRequestTimeoutDrain.setSelected(member.isWorkRequestTimeoutDrain());
		}
	}

	protected boolean checkSave() {
		String err = "";
		if (!checkIntegerPropertyValue(positionBackupNumber,0)) {
			err = "Position backup number must be greater or equal to zero";
		} else if (!checkStringPropertyValue(ipAddressOrHostName)) {
			err = "IP address or host name is mandatory";
		} else if (!checkIntegerPropertyValue(controlPort,99)) {
			err = "Control port must be greater or equal to 99";
		} else if (!checkIntegerPropertyValue(workPort,99)) {
			err = "Work port must be greater or equal to 99";
		} else if (!checkIntegerPropertyValue(workRequestTimeout,0)) {
			err = "Work request timeout must be greater or equal to zero";
		}
		if (err.length()>0) {
			JOptionPane.showMessageDialog(frame, err, "Error",JOptionPane.ERROR_MESSAGE);
		}
		return err.length() == 0;
	}
	
	protected JFrame getFrame() {
		return frame;
	}
	
	protected void addLabel(JPanel panel, int row,String text) {
		JLabel lbl = new JLabel(text);
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.anchor = GridBagConstraints.FIRST_LINE_START;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 0.3;
		gbc.gridx = 0;
		gbc.gridy = row;
		panel.add(lbl,gbc);
	}

	protected void addProperty(JPanel panel, int row,Component c) {
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.anchor = GridBagConstraints.PAGE_START;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 0.7;
		gbc.gridx = 1;
		gbc.gridy = row;
		panel.add(c,gbc);
	}

	protected void addButton(JPanel panel, int row,Component c) {
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.anchor = GridBagConstraints.FIRST_LINE_START;
		gbc.fill = GridBagConstraints.NONE;
		gbc.weightx = 0.7;
		gbc.gridx = 1;
		gbc.gridy = row;
		panel.add(c,gbc);
	}
	
	protected boolean checkIntegerPropertyValue(JFormattedTextField property,int min) {
		boolean valid = true;
		if (property.getValue()==null || Integer.parseInt(property.getValue().toString())<min) {
			valid = false;
		}
		return valid;
	}

	protected boolean checkStringPropertyValue(JFormattedTextField property) {
		boolean valid = true;
		if (property.getValue()==null || property.getValue().toString().length()==0) {
			valid = false;
		}
		return valid;
	}
}
