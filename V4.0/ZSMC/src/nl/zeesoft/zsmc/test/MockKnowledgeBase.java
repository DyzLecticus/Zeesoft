package nl.zeesoft.zsmc.test;

import nl.zeesoft.zdk.test.MockObject;
import nl.zeesoft.zodb.Config;
import nl.zeesoft.zsmc.db.TrainingSequence;
import nl.zeesoft.zsmc.db.TrainingSet;
import nl.zeesoft.zsmc.kb.KnowledgeBase;
import nl.zeesoft.zsmc.train.Confabulation;

public class MockKnowledgeBase extends MockObject {
	@Override
	protected void describe() {
		System.out.println("This test uses the *MockKnowledgeBase*.  ");
	}

	@Override
	protected Object initialzeMock() {
		KnowledgeBase conf = new KnowledgeBase((new Config()).getMessenger(),4,false);
		TrainingSet ts = Confabulation.getTrainingSet();
		for (TrainingSequence seq: ts.getSequences()) {
			conf.learnSequence(seq.sequence,seq.context);
		}
		conf.learnSequence("My name is Dyz Lecticus.","Self");
		conf.learnSequence("I am an artifically intelligent virtual agent.","Self");
		conf.learnSequence("I can learn context sensitive symbol sequences and use that knowledge to do things like correct symbols, classify context and more.","Self");
		conf.learnSequence("My goal is to understand and help people.","Self");
		conf.learnSequence("I like helping people.","Self");
		conf.calculateProbabilities();
		return conf;
	}

}
