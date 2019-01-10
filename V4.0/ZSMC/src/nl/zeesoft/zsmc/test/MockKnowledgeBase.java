package nl.zeesoft.zsmc.test;

import nl.zeesoft.zdk.test.MockObject;
import nl.zeesoft.zodb.Config;
import nl.zeesoft.zsmc.kb.KnowledgeBase;

public class MockKnowledgeBase extends MockObject {
	private static final String[] mockData = {
		"Confabulation theory proposes that cognition is a phylogenetic outgrowth of movement and that cognition utilizes the same neural circuitry that was originally developed for movement.", 
		"Movement relies on the deliberate, smooth, properly sequenced and coordinated, graded, contractions of selected ensembles of discrete muscles.",
		"Therefore, the neural circuitry of movement was specialized for this purpose.",
		"Soon, a new design possibility emerged: the elaborate neuronal machinery of movement control could be applied to brain tissue itself.",
		"In particular, discrete brain structures, modules, emerged that could be controlled exactly like individual muscles.",
		"By manipulating these modules in properly coordinated movements, valuable information processing could be carried out, thereby further enhancing animal competitive success and diversity.",
		"Confabulation theory postulates that the gray matter of human cerebral cortex is comprised of roughly 4000 localized, largely mutually disjoint, modules.",
		"Genetically selected pairs of these modules are connected by knowledge bases; of which humans have roughly 40000.",
		"Specific parameter values cited in this article are crude estimates of means intended to fix ideas.",
		"These values surely vary significantly within each human brain and between human brains.",
		"Each individual module and knowledge base also includes a small, uniquely dedicated, zone of thalamus.",
		"Modules and knowledge bases constitute the hardware of thought.",
		"Modules and knowledge bases are postulated by confabulation theory to implement four key information processing functional elements that together make up the mechanism of thought."
		};
	
	@Override
	protected void describe() {
		System.out.println("This test uses the *MockKnowledgeBase*.  ");
	}

	@Override
	protected Object initialzeMock() {
		KnowledgeBase conf = new KnowledgeBase((new Config()).getMessenger(),4);
		for (int i = 0; i < mockData.length; i++) {
			conf.learnSequence(mockData[i],"Confabulation");
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
