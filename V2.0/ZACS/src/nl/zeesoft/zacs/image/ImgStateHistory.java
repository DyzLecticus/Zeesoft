package nl.zeesoft.zacs.image;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.swing.JPanel;

import nl.zeesoft.zacs.database.model.StateHistory;
import nl.zeesoft.zacs.database.model.ZACSModel;
import nl.zeesoft.zodb.Messenger;
import nl.zeesoft.zodb.file.image.ImgObject;

public class ImgStateHistory extends ImgObject {
	public final static String			PROPERTY_AVERAGE_SYMBOL_LINK_COUNT		= "statAvgSymLinkCount"; 
	public final static String			PROPERTY_AVERAGE_CONTEXT_LINK_COUNT		= "statAvgConLinkCount"; 
	public final static String			PROPERTY_AVERAGE_MODULE_SYMBOL_LEVEL	= "statTotalSymbolLevel"; 
	public final static String			PROPERTY_TOTAL_SYMBOLS					= "statTotalSymbols"; 
	public final static String			PROPERTY_TOTAL_SYMBOL_LINKS				= "statTotalSymLinks"; 
	public final static String			PROPERTY_TOTAL_CONTEXT_LINKS			= "statTotalConLinks"; 

	public final static String[]		PROPERTIES								= {
		PROPERTY_AVERAGE_SYMBOL_LINK_COUNT,
		PROPERTY_AVERAGE_CONTEXT_LINK_COUNT,
		PROPERTY_AVERAGE_MODULE_SYMBOL_LEVEL,
		PROPERTY_TOTAL_SYMBOLS,
		PROPERTY_TOTAL_SYMBOL_LINKS,
		PROPERTY_TOTAL_CONTEXT_LINKS
		}; 

	private int							height									= ZACSModel.STATE_HISTORY_IMAGE_HEIGHT;
	private int							width									= ZACSModel.STATE_HISTORY_IMAGE_WIDTH;
	private long						dateTimeStart							= 0;
	private long						dateTimeEnd								= Long.MAX_VALUE;
	private String 						propertyName 							= PROPERTY_AVERAGE_SYMBOL_LINK_COUNT;

	private SortedMap<Long,BigDecimal>	dateTimeValueMap						= new TreeMap<Long,BigDecimal>();

	private BigDecimal					lowestValue								= null;
	private BigDecimal					highestValue							= null;

	public void parseDateTimeValueMapFromStateHistory(List<StateHistory> stateHistory) {
		dateTimeValueMap.clear();
		lowestValue = null;
		highestValue = null;
		StateHistory first = null;
		for (StateHistory statHist: stateHistory) {
			if (statHist.getDateTime()>=dateTimeStart && statHist.getDateTime()<=dateTimeEnd) {
				addStateHistoryToDateTimeValueMap(statHist);
			} else if (statHist.getDateTime()<dateTimeStart && (first==null || first.getDateTime()<statHist.getDateTime())) {
				first = statHist;
			}
		}
		if (first!=null) {
			addStateHistoryToDateTimeValueMap(first);
		}
	}

	/**
	 * @param height the height to set
	 */
	public void setHeight(int height) {
		if (height < 50) {
			height = 50;
		}
		this.height = height;
	}

	/**
	 * @param width the width to set
	 */
	public void setWidth(int width) {
		if (width < 100) {
			width = 100;
		}
		this.width = width;
	}

	/**
	 * @param dateTimeStart the dateTimeStart to set
	 */
	public void setDateTimeStart(long dateTimeStart) {
		this.dateTimeStart = dateTimeStart;
	}

	/**
	 * @param dateTimeEnd the dateTimeEnd to set
	 */
	public void setDateTimeEnd(long dateTimeEnd) {
		this.dateTimeEnd = dateTimeEnd;
	}

	/**
	 * @param propertyName the propertyName to set
	 */
	public void setPropertyName(String propertyName) {
		if (
			propertyName.equals(PROPERTY_AVERAGE_SYMBOL_LINK_COUNT) ||
			propertyName.equals(PROPERTY_AVERAGE_CONTEXT_LINK_COUNT) ||
			propertyName.equals(PROPERTY_AVERAGE_MODULE_SYMBOL_LEVEL) ||
			propertyName.equals(PROPERTY_TOTAL_SYMBOLS) ||
			propertyName.equals(PROPERTY_TOTAL_SYMBOL_LINKS) ||
			propertyName.equals(PROPERTY_TOTAL_CONTEXT_LINKS)
			) {
			this.propertyName = propertyName;
		} else {
			Messenger.getInstance().error(this,"Property not supported: " + propertyName);
		}
	}
	
	@Override
	public void render() {
		@SuppressWarnings("serial")
		JPanel graphPanel = new JPanel() {
			@Override
			public Dimension getPreferredSize() {
		        return new Dimension(width,height);
		    }
			@Override
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				Graphics2D g2 = (Graphics2D) g;
				g2.setColor(Color.WHITE);
				g2.fillRect(0,0,width,height);
				if (dateTimeValueMap.size()>0) {
					//Messenger.getInstance().debug(this,"Lowest value: " + lowestValue + ", highest value: " + highestValue);
					BigDecimal low = lowestValue.setScale(2);
					BigDecimal high = highestValue.setScale(2);
					BigDecimal divider = high.subtract(low);
					SortedMap<Long,BigDecimal> dateTimeValuePercentageMap = new TreeMap<Long,BigDecimal>();
					for (Entry<Long,BigDecimal> entry: dateTimeValueMap.entrySet()) {
						BigDecimal percentage = entry.getValue().subtract(low);
						percentage = percentage.setScale(2);
						if (divider.compareTo(BigDecimal.ZERO)>0 && percentage.compareTo(BigDecimal.ZERO)>0) {
							percentage = percentage.divide(divider,BigDecimal.ROUND_HALF_UP);
						} else {
							percentage = BigDecimal.ZERO;
						}
						dateTimeValuePercentageMap.put(entry.getKey(),percentage);
					}
					long timePerPixel = (dateTimeEnd - dateTimeStart) / width;
					long time = dateTimeStart;
					long drawTime = 0;
					for (int i = 0; i < width; i++) {
						SortedMap<Long,BigDecimal> subMap = dateTimeValuePercentageMap.subMap(-1L,(time + 1L));
						int pixels = 0;
						if (subMap.size()>0) {
							drawTime = subMap.lastKey();
							BigDecimal drawPercentage = dateTimeValuePercentageMap.get(drawTime);
							BigDecimal drawPixels = drawPercentage.multiply(new BigDecimal("" + height));
							drawPixels = drawPixels.setScale(0);
							pixels = Integer.parseInt(drawPixels.toString());
						} else {
							pixels = 0;
						}
						int start = height - pixels;
						g2.setColor(Color.GREEN);
						g2.drawLine(i, start, i, height);
						time = time + timePerPixel;
					}
					g2.setColor(Color.BLACK);
					g2.drawString(highestValue.toString(),10,20);
					g2.drawString(lowestValue.toString(),10,height - 10);
				}
			}
		};
		graphPanel.setSize(width, height);
		setPanel(graphPanel);
		super.render();
	}
	
	private void addStateHistoryToDateTimeValueMap(StateHistory statHist) {
		BigDecimal value = null;
		if (propertyName.equals(PROPERTY_AVERAGE_SYMBOL_LINK_COUNT)) {
			value = new BigDecimal("" + statHist.getStatAvgSymLinkCount());
		} else if (propertyName.equals(PROPERTY_AVERAGE_CONTEXT_LINK_COUNT)) {
			value = new BigDecimal("" + statHist.getStatAvgConLinkCount());
		} else if (propertyName.equals(PROPERTY_AVERAGE_MODULE_SYMBOL_LEVEL)) {
			value = new BigDecimal("" + statHist.getStatTotalSymbolLevel());
		} else if (propertyName.equals(PROPERTY_TOTAL_SYMBOLS)) {
			value = new BigDecimal("" + statHist.getStatTotalSymbols());
		} else if (propertyName.equals(PROPERTY_TOTAL_SYMBOL_LINKS)) {
			value = new BigDecimal("" + statHist.getStatTotalSymLinks());
		} else if (propertyName.equals(PROPERTY_TOTAL_CONTEXT_LINKS)) {
			value = new BigDecimal("" + statHist.getStatTotalConLinks());
		}
		if (value!=null) {
			if (lowestValue==null || lowestValue.compareTo(value)>0) {
				lowestValue = value;
			}
			if (highestValue==null || highestValue.compareTo(value)<0) {
				highestValue = value;
			}
			dateTimeValueMap.put(statHist.getDateTime(),value);
		}
	}
}
