package nl.zeesoft.zid.dialog;

public class DialogVariableExample {
	private StringBuilder	question	= null; 
	private StringBuilder	answer		= null;
	
	protected DialogVariableExample(StringBuilder question, StringBuilder answer) {
		this.question = question;
		this.answer = answer;
	}
	
	public StringBuilder getQuestion() {
		return question;
	}

	public StringBuilder getAnswer() {
		return answer;
	}
}
