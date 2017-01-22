package nl.zeesoft.zadf.controller.impl;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.util.Calendar;
import java.util.Date;

import javax.swing.SwingUtilities;

import nl.zeesoft.zadf.controller.GuiWindowController;
import nl.zeesoft.zadf.gui.GuiButton;
import nl.zeesoft.zadf.gui.GuiDialog;
import nl.zeesoft.zadf.gui.property.PrpComboBox;
import nl.zeesoft.zadf.gui.property.PrpDateTime;
import nl.zeesoft.zadf.gui.property.PrpInteger;
import nl.zeesoft.zadf.gui.property.PrpString;
import nl.zeesoft.zodb.Generic;
import nl.zeesoft.zodb.event.EvtEvent;
import nl.zeesoft.zodb.event.EvtEventSubscriber;
import nl.zeesoft.zodb.model.datatypes.DtDateTime;
import nl.zeesoft.zodb.model.datatypes.DtInteger;
import nl.zeesoft.zodb.model.datatypes.DtObject;
import nl.zeesoft.zodb.model.datatypes.DtString;

public class PrpDateTimeDialogController extends GuiWindowController implements EvtEventSubscriber {
	private PrpDateTime 		property 			= null;
	
	private Date 				dateValue 			= null;
	private int[][]				weekdays			= new int[6][7];
	
	private boolean				attachedListeners	= false;

	private boolean				refreshing			= false;
	
	public PrpDateTimeDialogController(GuiDialog dateTimeDialog) {
		super(dateTimeDialog);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String action = "";
		if (e.getActionCommand()!=null) {
			action = e.getActionCommand();
		}

		if (action.equals(GuiWindowController.ACTION_CLOSE_FRAME)) {
			super.actionPerformed(e);
			return;
		}
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(dateValue);
		
		boolean refresh = false;
		boolean focus = false;
		
		if (action.startsWith(ZADFFactory.BUTTON_DATETIME_DATE_PREFIX)) {
			String[] wd = Generic.getValuesFromString(action);
			int week = Integer.parseInt(wd[1]);
			int weekday = Integer.parseInt(wd[2]);
			if (weekdays[week][weekday]>0) {
				if (cal.get(Calendar.DATE)!=weekdays[week][weekday]) {
					cal.set(Calendar.DATE,weekdays[week][weekday]);
					dateValue = cal.getTime();
					refresh = true;
					focus = true;
				}
			}
		} else if (action.startsWith(ZADFFactory.BUTTON_DATETIME_NEXT_YEAR)) {
			// Increment year
			int year = getYear();
			if (year<9999) {
				cal.set(Calendar.YEAR,(year + 1));
				dateValue = cal.getTime();
			}
			refresh = true;
			focus = true;
		} else if (action.startsWith(ZADFFactory.BUTTON_DATETIME_PREV_YEAR)) {
			// Decrement year
			int year = getYear();
			if (year>0) {
				cal.set(Calendar.YEAR,(year - 1));
				dateValue = cal.getTime();
			}
			refresh = true;
			focus = true;
		} else if (action.startsWith(ZADFFactory.BUTTON_DATETIME_NEXT_MONTH)) {
			int month = getMonth();
			if (month==11) {
				month = 0;
				// Increment year
				int year = getYear();
				if (year<9999) {
					cal.set(Calendar.YEAR,(year + 1));
					dateValue = cal.getTime();
				}
			} else {
				month = month + 1;
			}
			cal.set(Calendar.MONTH,month);
			dateValue = cal.getTime();
			refresh = true;
			focus = true;
		} else if (action.startsWith(ZADFFactory.BUTTON_DATETIME_PREV_MONTH)) {
			int month = getMonth();
			if (month==0) {
				month = 11;
				// Decrement year
				int year = getYear();
				if (year>0) {
					cal.set(Calendar.YEAR,(year - 1));
					dateValue = cal.getTime();
				}
			} else {
				month = month - 1;
			}
			cal.set(Calendar.MONTH,month);
			dateValue = cal.getTime();
			refresh = true;
			focus = true;
		} else if (action.startsWith(ZADFFactory.BUTTON_DATETIME_SET)) {
			DtObject val = property.getValueObject();
			if (val instanceof DtDateTime) {
				updateDateValueBasedOnInput(cal);
				DtDateTime dateRef = (DtDateTime) val;
				dateRef.setValue(dateValue);
				if (dateValue!=null) {
					property.setStringValue(dateValue.toString());
				} else {
					property.setStringValue("");
				}
				property.updateComponentValue();
			}
			this.publishEvent(new EvtEvent(GuiWindowController.ACTION_CLOSE_FRAME, this, GuiWindowController.ACTION_CLOSE_FRAME));
		} else if (action.startsWith(ZADFFactory.BUTTON_DATETIME_TODAY)) {
			setToday(cal);
			refresh = true;
			focus = true;
		} else if (action.startsWith(ZADFFactory.BUTTON_DATETIME_CLEAR)) {
			DtDateTime val = new DtDateTime();
			property.setValueObject(val);
			property.setStringValue("");
			property.updateComponentValue();
			this.publishEvent(new EvtEvent(GuiWindowController.ACTION_CLOSE_FRAME, this, GuiWindowController.ACTION_CLOSE_FRAME));
		} else {
			if (!refreshing) {
				if (updateDateValueBasedOnInput(cal)) {
					refresh = true;
					focus = true;
				}
			}
		}
		if (refresh) {
			final boolean requestFocus = focus;
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					refresh(requestFocus);
			  	}
			});	
		}
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (!refreshing) {
			if (dateValue==null) {
				setToday(Calendar.getInstance());
			}
			Calendar cal = Calendar.getInstance();
			cal.setTime(dateValue);
			if (updateDateValueBasedOnInput(cal)) {
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						refresh(false);
				  	}
				});
			}
		}
	}	
	
	private void refresh(boolean requestFocus) {
		refreshing = true;
		
		Calendar cal = Calendar.getInstance();
		if (dateValue==null) {
			setToday(cal);
		}
		cal.setTime(dateValue);
		
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH);
		int date = cal.get(Calendar.DATE);

		String hour = Generic.minStrInt(cal.get(Calendar.HOUR_OF_DAY),2);
		String minute = Generic.minStrInt(cal.get(Calendar.MINUTE),2);
		String second = Generic.minStrInt(cal.get(Calendar.SECOND),2);
		
		PrpInteger propYear = (PrpInteger) getGuiObjectByName(ZADFFactory.PROPERTY_DATETIME_YEAR);
		PrpComboBox propMonth = (PrpComboBox) getGuiObjectByName(ZADFFactory.PROPERTY_DATETIME_MONTH);
		PrpString propHour = (PrpString) getGuiObjectByName(ZADFFactory.PROPERTY_DATETIME_HOUR);
		PrpString propMinute = (PrpString) getGuiObjectByName(ZADFFactory.PROPERTY_DATETIME_MINUTE);
		PrpString propSecond = (PrpString) getGuiObjectByName(ZADFFactory.PROPERTY_DATETIME_SECOND);

		propYear.getValueObject().setValue(year);
		propMonth.getValueObject().setValue(ZADFFactory.MONTHS[month]);
		propHour.getValueObject().setValue(hour);
		propMinute.getValueObject().setValue(minute);
		propSecond.getValueObject().setValue(second);
		
		propYear.updateComponentValue();
		propMonth.updateComponentValue();
		propHour.updateComponentValue();
		propMinute.updateComponentValue();
		propSecond.updateComponentValue();

		cal.set(Calendar.DATE,1);
		int startDayOfWeek = cal.get(Calendar.DAY_OF_WEEK); 

		boolean doVal = false;
		int d = 1;
		
		GuiButton focus = null;
		for (int week = 0; week < 6; week++) {
			for (int weekday = 0; weekday < 7; weekday++) {
				String btnName = ZADFFactory.BUTTON_DATETIME_DATE_PREFIX + Generic.SEP_STR + week + Generic.SEP_STR + weekday;
				GuiButton guiBtn = (GuiButton) getGuiObjectByName(btnName);
				String btnVal = "";
				guiBtn.getButton().setForeground(Color.BLACK);
				guiBtn.getButton().setEnabled(false);
				if ((week==0) && ((weekday + 1)==startDayOfWeek)) {
					doVal = true;
				}
				if (doVal) {
					weekdays[week][weekday] = d;
					btnVal = "" + d;
					if (d==date) {
						guiBtn.getButton().setForeground(Color.BLUE);
						if (requestFocus) {
							focus = guiBtn;
						}
					}
					guiBtn.getButton().setEnabled(true);
					
					d++;
					cal.set(Calendar.DATE, d);
					if (cal.get(Calendar.MONTH)!=month) {
						doVal = false;
					}
				} else {
					weekdays[week][weekday] = 0;
				}
				guiBtn.getButton().setText(btnVal);
			}
		}
		if (focus!=null) {
			focus.getButton().requestFocusInWindow();
		}
		
		if (!attachedListeners) {
			propYear.addSubscriber(this);
			propMonth.addSubscriber(this);
			propHour.addSubscriber(this);
			propMinute.addSubscriber(this);
			propSecond.addSubscriber(this);
			propYear.attachListeners();
			propMonth.attachListeners();
			propHour.attachListeners();
			propMinute.attachListeners();
			propSecond.attachListeners();
			
			attachedListeners = true;
		}
		refreshing = false;
	}

	private boolean updateDateValueBasedOnInput(Calendar cal) {
		boolean changed = false;
		if (!refreshing) {
			int year = getYear();
			int month = getMonth();
			int hour = getHour();
			int minute = getMinute();
			int second = getSecond();
	
			if (year<0) {
				year = 0;
				changed = true;
			} else if (year>9999) {
				year = 9999;
				changed = true;
			}
			if (month<0) {
				month = 0;
				changed = true;
			} else if (month>11) {
				month = 11;
				changed = true;
			}
			if (hour<0) {
				hour = 0;
				changed = true;
			} else if (hour>23) {
				hour = 23;
				changed = true;
			}
			if (minute<0) {
				minute = 0;
				changed = true;
			} else if (minute>59) {
				minute = 59;
				changed = true;
			}
			if (second<0) {
				second = 0;
				changed = true;
			} else if (second>59) {
				second = 59;
				changed = true;
			}
			
			cal.set(Calendar.YEAR,year);
			cal.set(Calendar.MONTH,month);
			cal.set(Calendar.HOUR_OF_DAY,hour);
			cal.set(Calendar.MINUTE,minute);
			cal.set(Calendar.SECOND,second);
			
			if (!changed) {
				changed = (dateValue.getTime() != cal.getTime().getTime());
			}
			
			dateValue = cal.getTime();
		}
		return changed;
	}
	
	/**
	 * @param property the property to set
	 */
	public void setProperty(PrpDateTime property) {
		this.property = property;

		DtObject val = property.getValueObject();
		if ((val instanceof DtDateTime) && (val.getValue()!=null)) {
			if (dateValue==null || ((Date) val.getValue()).getTime()!=dateValue.getTime()) {
				dateValue = (Date) val.getValue();
			} else {
				dateValue = null;
			}
		} else {
			dateValue = null;
		}

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				refresh(true);
		  	}
		});
	}

	private PrpInteger getYearProperty() {
		return (PrpInteger) getGuiObjectByName(ZADFFactory.PROPERTY_DATETIME_YEAR);
	}
	private PrpComboBox getMonthProperty() {
		return (PrpComboBox) getGuiObjectByName(ZADFFactory.PROPERTY_DATETIME_MONTH);
	}
	private PrpString getHourProperty() {
		return (PrpString) getGuiObjectByName(ZADFFactory.PROPERTY_DATETIME_HOUR);
	}
	private PrpString getMinuteProperty() {
		return (PrpString) getGuiObjectByName(ZADFFactory.PROPERTY_DATETIME_MINUTE);
	}
	private PrpString getSecondProperty() {
		return (PrpString) getGuiObjectByName(ZADFFactory.PROPERTY_DATETIME_SECOND);
	}

	private int getYear() {
		DtInteger value = (DtInteger) getYearProperty().getNewValueObjectFromComponentValue();
		return value.getValue();
	}

	private int getMonth() {
		int month = 0;
		DtString value = (DtString) getMonthProperty().getNewValueObjectFromComponentValue();
		for (int m = 0; m < ZADFFactory.MONTHS.length; m++) {
			if (ZADFFactory.MONTHS[m].equals(value.getValue())) {
				month = m;
				break;
			}
		}
		return month;
	}

	private int getHour() {
		DtString value = (DtString) getHourProperty().getNewValueObjectFromComponentValue();
		return tryParseInteger(value.getValue());
	}
	
	private int getMinute() {
		DtString value = (DtString) getMinuteProperty().getNewValueObjectFromComponentValue();
		return tryParseInteger(value.getValue());
	}
	
	private int getSecond() {
		DtString value = (DtString) getSecondProperty().getNewValueObjectFromComponentValue();
		return tryParseInteger(value.getValue());
	}

	private int tryParseInteger(String s) {
		int i = -1;
		try {
			i = Integer.parseInt(s);
		} catch (NumberFormatException e) {
			// Ignore
		}
		return i;
	}
	
	private void setToday(Calendar cal) {
		cal.setTime(new Date());
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		dateValue = cal.getTime();
	}

	@Override
	public void handleEvent(EvtEvent e) {
		if (e.getValue() instanceof PropertyChangeEvent) {
			if (!refreshing) {
				if (dateValue==null) {
					setToday(Calendar.getInstance());
				}
				Calendar cal = Calendar.getInstance();
				cal.setTime(dateValue);
				if (updateDateValueBasedOnInput(cal)) {
					SwingUtilities.invokeLater(new Runnable() {
						public void run() {
							refresh(true);
					  	}
					});
				}
			}
		} else if (e.getValue() instanceof ActionEvent) {
			if (!refreshing) {
				if (dateValue==null) {
					setToday(Calendar.getInstance());
				}
				Calendar cal = Calendar.getInstance();
				cal.setTime(dateValue);
				if (updateDateValueBasedOnInput(cal)) {
					SwingUtilities.invokeLater(new Runnable() {
						public void run() {
							refresh(true);
					  	}
					});
				}
			}			
		}
	}
}
