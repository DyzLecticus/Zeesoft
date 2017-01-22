Zeesoft Artificial Cognition Simulator
======================================
This project was created to research artificial cognition.

Inspiration
-----------
This software was inspired by ideas put forth in the following books;
 * 'Langauge and Mind' by Noam Chomsky
 * 'Godel Esher Bach' by Douglas R. Hofstadter
 * 'I Am a Strange Loop' by Douglas R. Hofstadter
 * 'The Mind's I' by Douglas R. Hofstadter and Daniel C. Dennet
 * 'Consciousness Explained' by Daniel C. Dennet
 * 'Confabulation Theory' by Robert Hecht-Nielsen

Cognition
---------
Cognition is a term used to describe all mental processes related to thought like memory management, learning, pattern recognition and production of language.
When we think about something, like a table, our brain represents that object by exciting a distinct group of neurons.
What also happens is that thinking about something triggers all kinds of associations with other mental objects, like the colour of the table and related objects like chairs.
As you may notice while reading this, we can find or create words for every thought we want to express.
Children do not have to learn grammar in order to be able to form correct sentences, they learn by example.
They can do this because language production is a direct result of the way cognition works.
This makes words and punctuation the ideal candidates to represent thoughts in software.

Artificial cognition
--------------------
Language learning and production is good starting point for artificial cognition.
This software can learn context sensitive examples of text input/output combinations and then use that knowledge to complete assignments.
To explain this implementation we will use the following example sequences;  
<img alt="Candy and pizza" src="https://raw.githubusercontent.com/DyzLecticus/Zeesoft/master/V2.0/ZACS/resources/doc/trim/01%20Candy%20and%20pizza.bmp">

Knowledge links
---------------
Knowledge is learned by separating all symbols in an input sequence and then creating links for each unique association.
Included in these links is the distance between the symbols within the learned sequence.
One can imagine that creating these links for long sequences can take up a lot of memory.
To prevent memory issues, the maximum link distance in this software is limited to 8.  
<img alt="Knowledge links" src="https://raw.githubusercontent.com/DyzLecticus/Zeesoft/master/V2.0/ZACS/resources/doc/trim/02%20Knowledge%20links.bmp">

Knowledge link counters
-----------------------
In order to maintain the stochastic properties while making only unique symbol distance combinations, a counter is incremented on the link whenever it is encountered by the sequence learning software.
When one of these link counters reaches a configurable maximum, all link counters are divided by 2.
Links with counter value 1 are removed in the process.
The result of this mechanism is that links that are not incremented by regular exposure to the example set will deteriorate, allowing the software to forget knowledge that is no longer relevant.  
<img alt="Knowledge link counters" src="https://raw.githubusercontent.com/DyzLecticus/Zeesoft/master/V2.0/ZACS/resources/doc/trim/03%20Knowledge%20link%20counters.bmp">

Context links
-------------
Context links are used to associate and restrict cognition to a context specific set of symbols.
There are no limits to how many context links a symbol can have but example learning and assignment making is restricted to a configurable limit.  
<img alt="Context links" src="https://raw.githubusercontent.com/DyzLecticus/Zeesoft/master/V2.0/ZACS/resources/doc/trim/04%20Context%20links.bmp">

Context link counters
---------------------
Context links use the same counter mechanism as knowledge links to learn, represent and forget context associations.  
<img alt="Context link counters" src="https://raw.githubusercontent.com/DyzLecticus/Zeesoft/master/V2.0/ZACS/resources/doc/trim/05%20Context%20link%20counters.bmp">

Modules and symbols
-------------------
Modules are virtual thinking muscles.
In order to use the knowledge links with distance 8 to their full potential we need 8 modules.
Each module has a link to each symbol which is able to hold the excitation level for that module symbol.
Based on the input symbol sequence, knowledge links are used to excite the associated symbols in the first module.  
<img alt="Modules and symbols" src="https://raw.githubusercontent.com/DyzLecticus/Zeesoft/master/V2.0/ZACS/resources/doc/06%20Modules%20and%20symbols.bmp">

Forward link firing
-------------------
Based on the input symbol sequence, knowledge links are used to excite the associated symbols in the second module.  
The excited symbols in the first module use knowledge links to fire forward into the second module.  
<img alt="Forward link firing" src="https://raw.githubusercontent.com/DyzLecticus/Zeesoft/master/V2.0/ZACS/resources/doc/07%20Forward%20link%20firing.bmp">

Backward link firing
--------------------
If the input sequence contains some symbols before the symbols visualized in this image, it is possible that one of the input symbols in the second module is the conclusion for that module.
This conclusion can then be used to support activated symbols in the first module by firing the associated links backward, in this case leading to a conclusion.  
<img alt="Backward link firing" src="https://raw.githubusercontent.com/DyzLecticus/Zeesoft/master/V2.0/ZACS/resources/doc/08%20Backward%20link%20firing.bmp">

Context
-------
If a specific context is given, or when the assignment requires dynamic context, the only module symbols enabled are the ones associated with that context.  
<img alt="Context" src="https://raw.githubusercontent.com/DyzLecticus/Zeesoft/master/V2.0/ZACS/resources/doc/09%20Context.bmp">

Firing function
---------------
The code that implements artificial cognition to complete assignments can be found in the class nl.zeesoft.zacs.simulator.SimAssignmentHandler.
The function below highlights just one of the many details needed to get correct, coherent results.
In order to limit the influence of symbols that are linked to a lot (i.e. periods), the number of links that link to the target symbol is used in the equation that increases the excitement level.  
<img alt="Context" src="https://raw.githubusercontent.com/DyzLecticus/Zeesoft/master/V2.0/ZACS/resources/doc/10%20Firing%20function.bmp">
