package nl.zeesoft.zacs.test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import nl.zeesoft.zacs.database.model.StateHistory;
import nl.zeesoft.zacs.image.ImgStateHistory;
import nl.zeesoft.zodb.database.DbConfig;

public class TestImgStateHistory {

	public static void main(String[] args) {
		DbConfig.getInstance().setDebug(true);
		
		// Generate state history
		List<StateHistory> stateHistory = new ArrayList<StateHistory>();
		long dateTimeEnd = (new Date()).getTime();
		long dateTimeStart = dateTimeEnd;
		for (long i = 0; i < 100; i++) {
			StateHistory statHist = new StateHistory();
			dateTimeStart = (dateTimeEnd - (i * 1000));
			statHist.setDateTime(dateTimeStart);
			if (i < 50) {
				statHist.setStatTotalSymLinks((3000 - (i * 10)));
			} else {
				statHist.setStatTotalSymLinks((3000 - (i * 20)));
			}
			stateHistory.add(statHist);
		}
		
		ImgStateHistory img = new ImgStateHistory();
		img.setDateTimeStart(dateTimeStart);
		img.setDateTimeEnd(dateTimeEnd);
		img.setPropertyName(ImgStateHistory.PROPERTY_TOTAL_SYMBOL_LINKS);
		
		img.parseDateTimeValueMapFromStateHistory(stateHistory);
		
		img.render();
		
		img.setVisible(true);
	}

}
