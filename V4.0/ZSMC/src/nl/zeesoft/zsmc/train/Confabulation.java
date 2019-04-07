package nl.zeesoft.zsmc.train;

import nl.zeesoft.zsmc.db.TrainingSet;

public class Confabulation {
	private static final String[] sequences = {
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
		"Modules and knowledge bases are postulated by confabulation theory to implement four key information processing functional elements that together make up the mechanism of thought.",
		"A longstanding hypothesis regarding the function of cerebral cortex is that many cortical neurons function as feature detectors.",
		"This hypothesis has been confirmed widely across cortex.",
		"The ubiquitous presence of feature detector neurons across cortex suggests that an important function of cortex is to have neurons which selectively respond to certain aspects of objects in the mental world.",
		"Confabulation theory takes this much further by hypothesizing that each module is specialized for describing one and only one fixed attribute that a mental world object may possess.",
		"Symbols are hypothesized to be mostly formed in childhood and then remain stable throughout life; they are the stable terms of reference that must exist if knowledge is to be accumulated across decades.",
		"Thalamocortical module symbol sets are the first of the four key functional elements of confabulation theory.",
		"Although the vague concept of human cognitive knowledge (something which is acquired, stored, and then used) has been in widespread use since at least the time of (Aristotle c.350 BC); until confabulation theory, there was no detailed hypothesis regarding the nature of knowledge or the neuronal mechanisms involved in its acquisition, storage, and use (other than the persistent vague suspicion that Hebbian co-occurrence-based synaptic modification might somehow be involved).",
		"Confabulation theory specifies precisely what cognitive knowledge is, how it is acquired, how it is stored, and how it is used in thinking.",
		"Pairs of symbols which meaningfully co-occur in this manner are postulated by confabulation theory to have unidirectional axonal links, termed knowledge links (each considered a single item of knowledge), established between them via synaptic strengthening (assuming that the required axons are actually present; which is determined by genetics).",
		"A single knowledge link, therefore, is a connection between two populations(or portions thereof) of neurons.",
		"The concept that the co-occurrence of pairs of items is the fundamental driver for the creation of knowledge was presciently envisioned by Aristotle.",
		"This associationist notion of the nature of animal cognitive knowledge was later progressively refined by many others, including Locke, Hume, Hartley, Mills, Bain, James, Pavlov, and Hebb.",
		"That all cognitive processing can be based entirely upon pairwise knowledge links between meaningfully co-occurring symbols seems preposterous.",
		"But that is exactly what confabulation theory hypothesizes.",
		"Confabulation theory postulates that the average adult human has billions of knowledge links; most of which are established in childhood.",
		"The sustained net average rate of human knowledge acquisition can probably sometimes exceed ten links per second for periods of days.",
		"Each knowledge link is hypothesized to be implemented via a two-stage synfire chain  connecting (via the fascicles of the cortical white matter) the collection of neurons that represent the source symbol of the link with a subset of the neuron collection which represents the target symbol of the link.",
		"Formation of knowledge links involves a complex process of instantaneous, but temporary, knowledge link establishment at time of first co-occurrence; followed by repeated evaluations and strengthenings (if the evaluation is favorable) of each such individual provisional knowledge link over the following hundred hours or so.",
		"Sejnowski and Destexhe propose that cortical activity during sleep is suited for consolidating information in neural assemblies.",
		"Thus, confabulation theory proposes that knowledge evaluation and solidification processes that strengthen knowledge links are largely carried out during sleep and involve entorhinal cortex, hippocampus, and many other brain nuclei.",
		"Given that many tens of thousands of provisional knowledge links are often formed daily, it is no wonder that we must sleep a third of the time.",
		"The set of all knowledge links joining symbols belonging to one specific source module to symbols belonging to one specific target module is termed a knowledge base.",
		"In the human brain, knowledge bases are hypothesized by confabulation theory to essentially take the form of the huge bundles of axons (the classical fascicles), which together make up a large portion of each cerebral hemisphere's ipsilateral white matter.",
		"Each module also typically has a knowledge base to its contralateral twin module (and perhaps to a few others near its twin); which together constitute the corpus callosum fascicle coherently linking the two cerebral hemispheres topographically.",
		"The vague notion that cognition employs some sort of information processing also dates to at least the time of Aristotle.",
		"Yet, until confabulation theory, no detailed specification has been offered of what this information processing might be.",
		"Confabulation theory hypothesizes that cognition involves one, and only one, information processing operation: confabulation, a specialized type of winners-take-all competition between the symbols of a module on the basis of the total input excitation each symbol is receiving from knowledge links.",
		"Confabulation takes place only when the module receives a specific, deliberate, thought command signal axonal input (analogous to motor neuron input to a muscle).",
		"Each module has a separate and independent thought command input.",
		"A confabulation in this sense can be thought of as a neural contraction, where the state of the contraction determines how many symbols are competing and the completion of the contraction results in a single symbol being expressed." 
		};
	
	public static TrainingSet getTrainingSet() {
		TrainingSet r = new TrainingSet();
		for (int i = 0; i < sequences.length; i++) {
			r.addSequence(sequences[i],"Confabulation");
		}
		return r;
	}
}
