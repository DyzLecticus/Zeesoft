package nl.zeesoft.zacs.database.model;

import nl.zeesoft.zodb.Locker;
import nl.zeesoft.zodb.Messenger;
import nl.zeesoft.zodb.database.DbController;
import nl.zeesoft.zodb.database.DbDataObject;
import nl.zeesoft.zodb.database.DbRequestQueue;
import nl.zeesoft.zodb.database.gui.GuiController;
import nl.zeesoft.zodb.database.request.ReqAdd;
import nl.zeesoft.zodb.database.request.ReqDataObject;
import nl.zeesoft.zodb.database.request.ReqObject;
import nl.zeesoft.zodb.event.EvtEvent;
import nl.zeesoft.zodb.event.EvtEventExceptionSubscriber;

public class ZACSDataGenerator extends Locker implements EvtEventExceptionSubscriber {

	private static final String	CONTEXT_KNOWLEDGE				= "knowledge";
	private static final String	CONTEXT_ARTIFICIAL				= "artificial";
	private static final String	CONTEXT_COGNITION				= "cognition";

	private static final String	CONTEXT_SELF					= "I self me myself Dyz Lecticus";
	private static final String	CONTEXT_TYPE					= CONTEXT_ARTIFICIAL + " " + CONTEXT_COGNITION + " intelligence AI software hardware machine";
	private static final String	CONTEXT_GOAL					= "goal goals purpose intent " + CONTEXT_KNOWLEDGE;
	private static final String	CONTEXT_CREATOR					= "creator maker builder designer programmer";

	private static final String	NAME							= "Dyz Lecticus";
	private static final String	MY_NAME_IS						= "My name is " + NAME + ".";
	private static final String	I_AM_AN							= "I am an artificial cognition.";
	private static final String	I_WAS_CREATED_BY				= "I was created by Andre van der Zee.";
	private static final String	MY_GOAL_IS						= "My goal is to learn and generate new knowledge in order to help humanity.";

	private static final String	I_AM							= "I am " + NAME + ".";
	private static final String	INTRODUCTION					= I_AM + " " + I_AM_AN + " " + I_WAS_CREATED_BY + " " + MY_GOAL_IS;

	private static final String WHAT_IS_YOUR_NAME				= "What is your name?";
	private static final String WHO_ARE_YOU						= "Who are you?";
	private static final String WHAT_ARE_YOU					= "What are you?";
	
	private static final String WHO_CREATED_YOU					= "Who created you?";
	private static final String WHO_MADE_YOU					= "Who made you?";
	private static final String WHO_DESIGNED_YOU				= "Who designed you?";
	private static final String WHO_BUILT_YOU					= "Who built you?";
	private static final String WHO_PROGRAMMED_YOU				= "Who programmed you?";
	
	private static final String WHY_DO_YOU_EXIST				= "Why do you exist?";
	private static final String WHY_WERE_YOU_CREATED			= "Why were you created?";
	private static final String WHAT_IS_YOUR_PURPOSE			= "What is your purpose?";
	private static final String WHAT_ARE_YOUR_INTENTIONS		= "What are your intentions?";
	private static final String WHAT_ARE_YOUR_GOALS				= "What are your goals?";
	private static final String WHAT_IS_YOUR_GOAL				= "What is your goal?";
	
	private static final String WHAT_IS_ARTIFICIAL				= "What is artificial?";
	private static final String	ARTIFICIAL_IS					= "A product of intentional human manufacture.";

	private static final String WHAT_IS_COGNITION				= "What is cognition?";
	private static final String	COGNITION_IS					= "The set of all mental abilities and processes related to knowledge, attention, memory and working memory, judgment and evaluation, reasoning and computation, problem solving and decision making, comprehension and production of language.";

	private static final String WHAT_IS_KNOWLEDGE				= "What is knowledge?";
	private static final String	KNOWLEDGE_IS					= "A familiarity, awareness or understanding of someone or something, such as facts, information, descriptions, or skills, which is acquired through experience or education by perceiving, discovering, or learning.";

	private static final String WHAT_IS_ARTIFICIAL_COGNITION	= "What is artificial cognition?";
	private static final String	ARTIFICIAL_COGNITION_IS			= "A product of intentional human manufacture that simulates mental abilities and processes related to knowledge, attention, memory and working memory, judgment and evaluation, reasoning and computation, problem solving and decision making, comprehension and production of language.";
	
	private ReqAdd 				addCommandRequest				= new ReqAdd(ZACSModel.COMMAND_CLASS_FULL_NAME);
	private ReqAdd 				addControlRequest				= new ReqAdd(ZACSModel.CONTROL_CLASS_FULL_NAME);
	private ReqAdd 				addModuleRequest				= new ReqAdd(ZACSModel.MODULE_CLASS_FULL_NAME);
	private ReqAdd 				addExampleRequest				= new ReqAdd(ZACSModel.EXAMPLE_CLASS_FULL_NAME);
	private ReqAdd 				addAssignmentRequest			= new ReqAdd(ZACSModel.ASSIGNMENT_CLASS_FULL_NAME);
	private ReqAdd 				addCrawlerRequest				= new ReqAdd(ZACSModel.CRAWLER_CLASS_FULL_NAME);

	private boolean 			done	 						= false;
	
	private boolean				installExampsAndAssigns			= true;
	private boolean				installCrawlers					= true;

	public void confirmInstallExamplesAndAssignments() {
		installExampsAndAssigns = GuiController.getInstance().showConfirmMsg("Do you want to install the default examples and assignments?","Install default examples and assignments?", installExampsAndAssigns);
	}

	public void confirmInstallCrawlers() {
		installCrawlers = GuiController.getInstance().showConfirmMsg("Do you want to install the default crawlers?","Install default crawlers?", installCrawlers);
	}
	
	@Override
	public void handleEventException(EvtEvent evt, Exception ex) {
		setDone(true);
	}
	
	@Override
	public final void handleEvent(EvtEvent evt) {
		if (evt.getType().equals(DbController.DB_INITIALIZED_MODEL) && evt.getValue().toString().equals("true")) {
			generateInitialData();
		} else if (evt.getValue()==addCommandRequest) {
			if (!addCommandRequest.hasError()) {
				addControlRequest.addSubscriber(this);
				addControlRequest.getObjects().add(new ReqDataObject(createControlObject().toDataObject()));
				DbRequestQueue.getInstance().addRequest(addControlRequest,this);
			} else {
				Messenger.getInstance().error(this,"Error executing add request: " + addCommandRequest.getErrors().get(0).getMessage());
				setDone(true);
			}
		} else if (evt.getValue()==addControlRequest) {
			if (!addControlRequest.hasError()) {
				addModuleRequest.addSubscriber(this);
				for (int i = 1; i <= ZACSModel.getNumberOfModules(); i++) {
					addModuleRequest.getObjects().add(new ReqDataObject(createModuleObject(i)));
				}
				DbRequestQueue.getInstance().addRequest(addModuleRequest,this);
			} else {
				Messenger.getInstance().error(this,"Error executing add request: " + addControlRequest.getErrors().get(0).getMessage());
				setDone(true);
			}
		} else if (evt.getValue()==addModuleRequest) {
			if (!addModuleRequest.hasError()) {
				if (!installExamplesAndAssignments()) {
					// Skip to crawler
					executeAddCrawlerRequest();
				} else {
					addExampleRequest.addSubscriber(this);
					addExampleObjectsToAddRequest(addExampleRequest);
					DbRequestQueue.getInstance().addRequest(addExampleRequest,this);
				}
			} else {
				Messenger.getInstance().error(this,"Error executing add request: " + addModuleRequest.getErrors().get(0).getMessage());
				setDone(true);
			}
		} else if (evt.getValue()==addExampleRequest) {
			if (!addExampleRequest.hasError()) {
				addAssignmentRequest.addSubscriber(this);
				addAssignmentObjectsToAddRequest(addAssignmentRequest);
				DbRequestQueue.getInstance().addRequest(addAssignmentRequest,this);
			} else {
				Messenger.getInstance().error(this,"Error executing add request: " + addExampleRequest.getErrors().get(0).getMessage());
				setDone(true);
			}
		} else if (evt.getValue()==addAssignmentRequest) {
			if (!addExampleRequest.hasError()) {
				executeAddCrawlerRequest();
			} else {
				Messenger.getInstance().error(this,"Error executing add request: " + addAssignmentRequest.getErrors().get(0).getMessage());
				setDone(true);
			}
		} else if (evt.getValue()==addCrawlerRequest) {
			setDone(true);
		} else if (evt.getValue()!=null && evt.getValue() instanceof ReqObject) {
			handleRequestEvent((ReqObject) evt.getValue(),evt);
		}
	}
	
	protected void handleRequestEvent(ReqObject request,EvtEvent evt) {
		// Override to implement
	}
	
	protected void generateInitialData() {
		Messenger.getInstance().debug(this,"Generating initial ZACS data ...");
		setDone(false);
		
		addCommandRequest.addSubscriber(this);
		addCommandRequest.getObjects().add(new ReqDataObject(createCommandObject(Command.ACTIVATE)));
		addCommandRequest.getObjects().add(new ReqDataObject(createCommandObject(Command.DEACTIVATE)));
		addCommandRequest.getObjects().add(new ReqDataObject(createCommandObject(Command.REACTIVATE)));
		DbRequestQueue.getInstance().addRequest(addCommandRequest,this);
		
		waitTillDone("Generating initial data was interrupted");
		Messenger.getInstance().debug(this,"Generated initial ZACS data");
	}
	
	protected boolean installExamplesAndAssignments() {
		return installExampsAndAssigns;
	}

	protected boolean installCrawlers() {
		return installCrawlers;
	}
	
	protected void addExampleObjectsToAddRequest(ReqAdd addExampleRequest) {
		DbDataObject obj = null;
		
		obj = createExampleObject(WHO_ARE_YOU,INTRODUCTION,CONTEXT_SELF + " introduction");
		addExampleRequest.getObjects().add(new ReqDataObject(obj));
		obj = createExampleObject(WHAT_IS_YOUR_NAME,MY_NAME_IS,CONTEXT_SELF);
		addExampleRequest.getObjects().add(new ReqDataObject(obj));
		obj = createExampleObject(WHAT_ARE_YOU,I_AM_AN,CONTEXT_SELF + " " + CONTEXT_TYPE);
		addExampleRequest.getObjects().add(new ReqDataObject(obj));
		obj = createExampleObject(WHO_CREATED_YOU,I_WAS_CREATED_BY,CONTEXT_SELF + " " + CONTEXT_CREATOR);
		addExampleRequest.getObjects().add(new ReqDataObject(obj));
		obj = createExampleObject(WHO_MADE_YOU,I_WAS_CREATED_BY,CONTEXT_SELF + " " + CONTEXT_CREATOR);
		addExampleRequest.getObjects().add(new ReqDataObject(obj));
		obj = createExampleObject(WHO_DESIGNED_YOU,I_WAS_CREATED_BY,CONTEXT_SELF + " " + CONTEXT_CREATOR);
		addExampleRequest.getObjects().add(new ReqDataObject(obj));
		obj = createExampleObject(WHO_BUILT_YOU,I_WAS_CREATED_BY,CONTEXT_SELF + " " + CONTEXT_CREATOR);
		addExampleRequest.getObjects().add(new ReqDataObject(obj));
		obj = createExampleObject(WHO_PROGRAMMED_YOU,I_WAS_CREATED_BY,CONTEXT_SELF + " " + CONTEXT_CREATOR);
		addExampleRequest.getObjects().add(new ReqDataObject(obj));
		obj = createExampleObject(WHY_DO_YOU_EXIST,MY_GOAL_IS,CONTEXT_SELF + " " + CONTEXT_GOAL);
		addExampleRequest.getObjects().add(new ReqDataObject(obj));
		obj = createExampleObject(WHY_WERE_YOU_CREATED,MY_GOAL_IS,CONTEXT_SELF + " " + CONTEXT_GOAL);
		addExampleRequest.getObjects().add(new ReqDataObject(obj));
		obj = createExampleObject(WHAT_IS_YOUR_PURPOSE,MY_GOAL_IS,CONTEXT_SELF + " " + CONTEXT_GOAL);
		addExampleRequest.getObjects().add(new ReqDataObject(obj));
		obj = createExampleObject(WHAT_ARE_YOUR_INTENTIONS,MY_GOAL_IS,CONTEXT_SELF + " " + CONTEXT_GOAL);
		addExampleRequest.getObjects().add(new ReqDataObject(obj));
		obj = createExampleObject(WHAT_ARE_YOUR_GOALS,MY_GOAL_IS,CONTEXT_SELF + " " + CONTEXT_GOAL);
		addExampleRequest.getObjects().add(new ReqDataObject(obj));
		obj = createExampleObject(WHAT_IS_YOUR_GOAL,MY_GOAL_IS,CONTEXT_SELF + " " + CONTEXT_GOAL);
		addExampleRequest.getObjects().add(new ReqDataObject(obj));
		obj = createExampleObject(WHAT_IS_ARTIFICIAL,ARTIFICIAL_IS,CONTEXT_ARTIFICIAL);
		addExampleRequest.getObjects().add(new ReqDataObject(obj));
		obj = createExampleObject(WHAT_IS_COGNITION,COGNITION_IS,CONTEXT_COGNITION);
		addExampleRequest.getObjects().add(new ReqDataObject(obj));
		obj = createExampleObject(WHAT_IS_KNOWLEDGE,KNOWLEDGE_IS,CONTEXT_KNOWLEDGE + " science");
		addExampleRequest.getObjects().add(new ReqDataObject(obj));
		obj = createExampleObject(WHAT_IS_ARTIFICIAL_COGNITION,ARTIFICIAL_COGNITION_IS,CONTEXT_ARTIFICIAL + " " + CONTEXT_COGNITION);
		addExampleRequest.getObjects().add(new ReqDataObject(obj));
	}

	protected void addAssignmentObjectsToAddRequest(ReqAdd addAssignmentRequest) {
		Assignment as = null;

		as = createAssignmentObject(WHO_ARE_YOU,CONTEXT_SELF,WHO_ARE_YOU,128);
		as.setLogExtended(true);
		addAssignmentRequest.getObjects().add(new ReqDataObject(as.toDataObject()));
		
		as = createAssignmentObject(WHAT_IS_YOUR_NAME,CONTEXT_SELF,WHAT_IS_YOUR_NAME,128);
		as.setLogExtended(true);
		addAssignmentRequest.getObjects().add(new ReqDataObject(as.toDataObject()));

		as = createAssignmentObject(WHAT_IS_ARTIFICIAL_COGNITION,CONTEXT_ARTIFICIAL + " " + CONTEXT_COGNITION,WHAT_IS_ARTIFICIAL_COGNITION,256);
		as.setLogExtended(true);
		addAssignmentRequest.getObjects().add(new ReqDataObject(as.toDataObject()));

		as = createAssignmentObject("Free form (1)","",".",128,false);
		as.setLogExtended(true);
		as.setContextDynamic(true);
		addAssignmentRequest.getObjects().add(new ReqDataObject(as.toDataObject()));
		
		as = createAssignmentObject("Free form (2)","",".",128,true);
		as.setLogExtended(true);
		as.setContextDynamic(true);
		addAssignmentRequest.getObjects().add(new ReqDataObject(as.toDataObject()));

		as = createAssignmentObject("Free form (3)","",".",128,true);
		as.setThinkWidth(10);
		as.setLogExtended(true);
		as.setContextDynamic(true);
		addAssignmentRequest.getObjects().add(new ReqDataObject(as.toDataObject()));
		
		// Input correction tests
		as = createAssignmentObject("Who are e you?",CONTEXT_SELF,"Who are e you?",32);
		as.setCorrectInput(true);
		as.setLogExtended(true);
		addAssignmentRequest.getObjects().add(new ReqDataObject(as.toDataObject()));

		as = createAssignmentObject("What is qwer name?",CONTEXT_SELF,"What is qwer name?",32);
		as.setCorrectInput(true);
		as.setLogExtended(true);
		addAssignmentRequest.getObjects().add(new ReqDataObject(as.toDataObject()));

		as = createAssignmentObject("What is artificial qwer?",CONTEXT_TYPE,"What is artificial qwer?",32);
		as.setCorrectInput(true);
		as.setLogExtended(true);
		addAssignmentRequest.getObjects().add(new ReqDataObject(as.toDataObject()));

		as = createAssignmentObject("What is qwer asdf",CONTEXT_TYPE,"What is qwer asdf",32);
		as.setCorrectInput(true);
		as.setCorrectLineEnd(true);
		as.setLogExtended(true);
		addAssignmentRequest.getObjects().add(new ReqDataObject(as.toDataObject()));
	}
	
	protected void executeAddCrawlerRequest() {
		if (installCrawlers()) {
			addCrawlerToRequest("https://www.edge.org","science knowledge philosophy");
			addCrawlerToRequest("https://en.wikipedia.org/wiki/Artificial_intelligence","artificial intelligence AI cognition");
			addCrawlerToRequest("https://en.wikipedia.org/wiki/Cognition","cognition");
			addCrawlerToRequest("http://www.scholarpedia.org/article/Confabulation_theory_(computational_intelligence)","artificial intelligence AI cognition");
			addCrawlerRequest.addSubscriber(this);
			DbRequestQueue.getInstance().addRequest(addCrawlerRequest,this);
		} else {
			setDone(true);
		}
	}

	protected void addCrawlerToRequest(String url,String context) {
		Crawler c = new Crawler();
		if (url!=null && url.length()>0) {
			c.setCrawlUrl(url);
		}
		if (context!=null && context.length()>0) {
			c.setConvertContext(new StringBuilder(context));
		}
		addCrawlerRequest.getObjects().add(new ReqDataObject(c.toDataObject()));
	}
	
	protected Control createControlObject() {
		return new Control();
	}

	private DbDataObject createCommandObject(String code) {
		DbDataObject obj = (new Command()).toDataObject();
		obj.setPropertyValue("code",new StringBuilder(code));
		return obj;
	}

	private DbDataObject createModuleObject(int num) {
		DbDataObject obj = (new Module()).toDataObject();
		obj.setPropertyValue("num",new StringBuilder("" + num));
		return obj;
	}

	private DbDataObject createExampleObject(String input, String output, String context) {
		DbDataObject obj = (new Example()).toDataObject();
		obj.setPropertyValue("context",new StringBuilder(context));
		obj.setPropertyValue("input",new StringBuilder(input));
		obj.setPropertyValue("output",new StringBuilder(output));
		return obj;
	}

	private Assignment createAssignmentObject(String name, String context, String input, int maxSymbols) {
		return createAssignmentObject(name,context,input,maxSymbols,false);
	}
	
	private Assignment createAssignmentObject(String name, String context, String input,int maxSymbols,boolean thinkFast) {
		Assignment as = new Assignment();
		as.setName(name);
		as.setContext(new StringBuilder(context));
		as.setInput(new StringBuilder(input));
		as.setMaxSymbols(maxSymbols);
		as.setThinkFast(thinkFast);
		return as;
	}
	
	protected void setDone(boolean done) {
		lockMe(this);
		this.done = done;
		unlockMe(this);
	}

	private boolean isDone() {
		boolean r = false;
		lockMe(this);
		r = done;
		unlockMe(this);
		return r;
	}
	
	protected void waitTillDone(String interruptedMessage) {
		while(!isDone()) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				Messenger.getInstance().error(this,interruptedMessage);
			}
		}
	}
}
