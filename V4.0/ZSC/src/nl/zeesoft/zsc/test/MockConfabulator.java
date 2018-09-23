package nl.zeesoft.zsc.test;

import nl.zeesoft.zdk.test.MockObject;
import nl.zeesoft.zodb.Config;
import nl.zeesoft.zsc.confab.Confabulator;

public class MockConfabulator extends MockObject {

	@Override
	protected void describe() {
		System.out.println("This test uses the *MockConfabulator*.  ");
	}

	@Override
	protected Object initialzeMock() {
		Confabulator conf = new Confabulator(new Config(),"MockConfabulator",4);
		conf.learnSequence("My name is Dyz Lecticus.","Self Name");
		conf.learnSequence("I am an artifically intelligent virtual agent.","Self Description");
		conf.learnSequence("I can learn context sensitive symbol sequences and use that knowledge to do things like correct symbols, classify context and more.","Self Description");
		conf.learnSequence("My goal is to understand and help people.","Self Goal");
		conf.learnSequence("What is very good?","Question");
		conf.calculateProbabilities();
		return conf;
	}

}
