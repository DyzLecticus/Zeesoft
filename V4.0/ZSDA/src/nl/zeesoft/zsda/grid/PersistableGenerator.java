package nl.zeesoft.zsda.grid;

import java.util.Calendar;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.functions.ZRandomize;
import nl.zeesoft.zdk.htm.grid.ZGrid;
import nl.zeesoft.zdk.htm.grid.ZGridRequest;
import nl.zeesoft.zdk.json.JsElem;
import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.StateWorker;
import nl.zeesoft.zdk.thread.WorkerUnion;
import nl.zeesoft.zodb.db.init.Persistable;

public class PersistableGenerator extends StateWorker implements Persistable {
	private static int				GENERATE		= 3000;
	private static int				MAX_VALUE		= 11;
	private static int				INTERVAL_MS		= 5000;
	
	private ZGrid					grid			= null;

	private long					dateTime		= 0;
	private int 					value			= MAX_VALUE;
	
	private int						randomize		= 0;
	
	public PersistableGenerator(Messenger msgr,WorkerUnion uni,ZGrid grid) {
		super(msgr,uni);
		this.grid = grid;
		setSleep(INTERVAL_MS);
	}

	@Override
	public JsFile toJson() {
		JsFile json = new JsFile();
		json.rootElement = new JsElem();
		json.rootElement.children.add(new JsElem("dateTime","" + dateTime));
		json.rootElement.children.add(new JsElem("value","" + value));
		json.rootElement.children.add(new JsElem("randomize","" + randomize));
		return json;
	}

	@Override
	public void fromJson(JsFile json) {
		if (json.rootElement!=null) {
			dateTime = json.rootElement.getChildLong("dateTime",dateTime);
			value = json.rootElement.getChildInt("value",value);
			randomize = json.rootElement.getChildInt("randomize",randomize);
		}
	}

	@Override
	public ZStringBuilder getObjectName() {
		return new ZStringBuilder("Generator");
	}

	public int getRandomize() {
		lockMe(this);
		int r = randomize;
		unlockMe(this);
		return r;
	}

	public void setRandomize(int randomize) {
		lockMe(this);
		this.randomize = randomize;
		unlockMe(this);
	}

	@Override
	protected void whileWorking() {
		initializeDateTime();
		catchUp();
		addRequest();
	}
	
	protected void initializeDateTime() {
		if (dateTime==0) {
			Calendar cal = Calendar.getInstance();
			cal.set(Calendar.MILLISECOND,0);
			dateTime = cal.getTimeInMillis() - (GENERATE * INTERVAL_MS);
		}
	}
	
	protected void catchUp() {
		long ms = System.currentTimeMillis() - dateTime;
		if (ms>INTERVAL_MS * 1.5) {
			ms = ms - (ms % INTERVAL_MS);
			int generate = (int) (ms / INTERVAL_MS);
			for (int i = 0; i < generate; i++) {
				addRequest();
			}
		}
	}
	
	protected void addRequest() {
		grid.addRequest(generateRequest());
	}
	
	protected ZGridRequest generateRequest() {
		int val = value;
		lockMe(this);
		if (randomize>0) {
			val += ZRandomize.getRandomInt(0,randomize);
			randomize--;
		}
		unlockMe(this);
		ZGridRequest r = getNewRequest(dateTime,val);
		dateTime += INTERVAL_MS;
		value++;
		if (value>MAX_VALUE) {
			value = 0;
		}
		return r;
	}
	
	protected ZGridRequest getNewRequest(long dateTime,int value) {
		ZGridRequest r = null;
		r = grid.getNewRequest();
		r.dateTime = dateTime;
		r.inputValues[0] = dateTime;
		r.inputValues[1] = value;
		return r;
	}
}
