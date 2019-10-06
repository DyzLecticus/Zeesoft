Zeesoft Development Kit
=======================
The Zeesoft Development Kit (ZDK) is an open source library for Java application development.  

It provides support for;  
 * Extended StringBuilder manipulation and validation  
 * Basic file writing and reading  
 * HTTP requests  
 * CSV data  
 * JSON data  
 * Multi threading  
 * Application message handling
 * Basic matrix mathematics  
 * Genetic algorithms  
 * Neural networks  
 * [Hierarchical Temporal Memory](https://numenta.com/)  
   * Sparse distributed representations  
   * Spatial pooling  
   * Temporal memory  
   * SDR streaming  
   * Anomaly detection  
   * Value classification and prediction  
 * Self documenting and testing libraries  

**Release downloads**  
Click [here](https://github.com/DyzLecticus/Zeesoft/raw/master/V4.0/ZDK/releases/zdk-0.9.0.zip) to download the latest ZDK release (version 0.9.0).  
All ZDK releases can be downloaded [here](https://github.com/DyzLecticus/Zeesoft/raw/master/V4.0/ZDK/releases/).  
*All jar files in the release include source code and build scripts.*  

**Self documenting and self testing**  
The tests used to develop this libary are also used to generate this README file.  
Run the [ZDK](https://github.com/DyzLecticus/Zeesoft/blob/master/V4.0/ZDK/src/nl/zeesoft/zdk/test/impl/ZDK.java) class as a java application to print this documentation to the standard out.  
Click [here](#test-results) to scroll down to the test result summary.  

nl.zeesoft.zdk.test.impl.TestZStringEncoder
-------------------------------------------
This test shows how to use the *ZStringEncoder* to generate a key and then use that to encode and decode a text.

**Example implementation**  
~~~~
// Create the ZStringEncoder
ZStringEncoder encoder = new ZStringEncoder("Example text to be encoded.");
// Generate a key
String key = encoder.generateNewKey(1024);
// Use the key to encode the text
encoder.encodeKey(key,0);
// Use the key to decode the encoded text
encoder.decodeKey(key,0);
~~~~

This encoding mechanism can be used to encode and decode passwords and other sensitive data.
The minimum key length is 64. Longer keys provide stronger encoding.

Class references;  
 * [TestZStringEncoder](https://github.com/DyzLecticus/Zeesoft/blob/master/V4.0/ZDK/src/nl/zeesoft/zdk/test/impl/TestZStringEncoder.java)
 * [ZStringEncoder](https://github.com/DyzLecticus/Zeesoft/blob/master/V4.0/ZDK/src/nl/zeesoft/zdk/ZStringEncoder.java)

**Test output**  
The output of this test shows the generated key, the input text, the encoded text, and the decoded text.
~~~~
Key: 9100549626625222306188107799833625458044648450124599516811657012
Input text: Hello, my name is 'Dyz Lecticus'. How are you feeling today? :-) (Don't you know how to: [re;spond]!).

Key encoded text: BCPzdy~zs~1:qB1Ar#7~1AW#d~L#HzT#C:uxR~1z0CBCOyhy4BbBYDFDsBT:5:U~bzc:0:FAyCmxs:P~4AI:RCF~a~7x3zW#~~LALDxD5~jyyBtBWzhzrAR~p~wxjxax1BTy~yCzr~V:GDO~r#kA8AWzb~U#V#8#nz:zYAM#LByC3zZyZA4BWDBDOBR:2:2Aa#f~u:v:lA#y0
Key decoded text: Hello, my name is 'Dyz Lecticus'. How are you feeling today? :-) (Don't you know how to: [re;spond]!).

ASCII encoded text: 42,24,59,48,66,63,2,-28,67,73,-10,50,55,61,59,-28,63,67,-10,-21,26,73,80,-28,34,53,57,56,63,51,75,55,-3,-2,-10,12,69,71,-10,37,72,53,-10,61,69,69,-10,42,59,53,66,45,68,55,-10,56,69,52,55,61,21,-16,16,-15,-1,-16,-2,8,69,62,-3,56,-10,73,69,57,-10,59,68,51,77,-16,62,51,77,-16,74,51,16,-16,49,54,59,11,73,52,69,62,58,33,-9,-7,4
ASCII decoded text: Hello, my name is 'Dyz Lecticus'. How are you feeling today? :-) (Don't you know how to: [re;spond]!).
~~~~

nl.zeesoft.zdk.test.impl.TestZStringSymbolParser
------------------------------------------------
This test shows how to use the *ZStringSymbolParser* to parse symbols (words and punctuation) from a certain text.

**Example implementation**  
~~~~
// Create the ZStringSymbolParser
ZStringSymbolParser parser = new ZStringSymbolParser("Example text.");
// Parse the string
List<String> symbols = parser.toSymbolsPunctuated();
~~~~

Class references;  
 * [TestZStringSymbolParser](https://github.com/DyzLecticus/Zeesoft/blob/master/V4.0/ZDK/src/nl/zeesoft/zdk/test/impl/TestZStringSymbolParser.java)
 * [ZStringSymbolParser](https://github.com/DyzLecticus/Zeesoft/blob/master/V4.0/ZDK/src/nl/zeesoft/zdk/ZStringSymbolParser.java)

**Test output**  
The output of this test shows the input text and the parsed symbols separated by spaces.
~~~~
Input text: Hello, my name is 'Dyz Lecticus'. How are you feeling today? :-) (Don't you know how to: [re;spond]!).
Parsed symbols: Hello , my name is ' Dyz Lecticus ' . How are you feeling today ? :-) ( Don't you know how to : [re;spond] ! ) .
~~~~

nl.zeesoft.zdk.test.impl.TestCsv
--------------------------------
This test shows how to create a *CsvFile* instance from a CSV string.

**Example implementation**  
~~~~
// Create CSV object
CsvFile csv = new CsvFile();
// Parse CSV from string
csv.fromStringBuilder(new ZStringBuilder("test,qwer,123\n"));
~~~~

Class references;  
 * [TestCsv](https://github.com/DyzLecticus/Zeesoft/blob/master/V4.0/ZDK/src/nl/zeesoft/zdk/test/impl/TestCsv.java)
 * [CsvFile](https://github.com/DyzLecticus/Zeesoft/blob/master/V4.0/ZDK/src/nl/zeesoft/zdk/CsvFile.java)

**Test output**  
The output of this test shows the string input and the resulting number of rows and columns.  
~~~~
Input:
test,qwer,123
test,qwer,123,qwer
"t,e\"\nst",qwer,123
"t,e""\nst""",qwer,123

Rows: 4, columns: 3
~~~~

nl.zeesoft.zdk.test.impl.TestJson
---------------------------------
This test shows how to create a *JsFile* instance from a JSON string.

**Example implementation**  
~~~~
// Create JSON object
JsFile json = new JsFile();
// Parse JSON from string
json.fromStringBuilder(new ZStringBuilder("{\"command\":\"doStuff\"}"));
~~~~

Class references;  
 * [TestJson](https://github.com/DyzLecticus/Zeesoft/blob/master/V4.0/ZDK/src/nl/zeesoft/zdk/test/impl/TestJson.java)
 * [JsFile](https://github.com/DyzLecticus/Zeesoft/blob/master/V4.0/ZDK/src/nl/zeesoft/zdk/json/JsFile.java)

**Test output**  
The output of this test shows the string input and the resulting JSON structure.  
~~~~
Input:
{
    "qwerValue" : "qwerqwer",
    "qwerObject1" : {"qwerName":"name1" ,"qwerNumber": 1234},
    "qwerObject2" : {
        "qwerName": "name2",
        "qwerNumber": 12345,
        "qwerArray": [],
        "qwerSubObject1": {qwerqwer:"qwer qwer1",qwertqwert:"qwert qwert1"},
        "qwerSubObject2": {qwerqwer:"qwer qwer2",qwertqwert:"qwert qwert2"},
        "qwerObjectArray": [{asdfasdf:"asdf","qwer":["qwerqwer","qwerqwerqwer","qwerqwerqwerqwer"]},{asdf:"asdfasdf"}]
    }
}

JSON structure:
{
  "qwerValue": "qwerqwer",
  "qwerObject1": {
    "qwerName": "name1",
    "qwerNumber": 1234
  },
  "qwerObject2": {
    "qwerName": "name2",
    "qwerNumber": 12345,
    "qwerArray": [],
    "qwerSubObject1": {
      "qwerqwer": "qwer qwer1",
      "qwertqwert": "qwert qwert1"
    },
    "qwerSubObject2": {
      "qwerqwer": "qwer qwer2",
      "qwertqwert": "qwert qwert2"
    },
    "qwerObjectArray": [
      {
        "asdfasdf": "asdf",
        "qwer": [
          "qwerqwer",
          "qwerqwerqwer",
          "qwerqwerqwerqwer"
        ]
      },
      {
        "asdf": "asdfasdf"
      }
    ]
  }
}
~~~~

nl.zeesoft.zdk.test.impl.TestZHttpRequest
-----------------------------------------
This test shows how to use a *ZHttpRequest* instance to call a JSON API and return the response as a *JsFile*.
A *ZHttpRequest* instance can also be used to make regular GET, POST and PUT requests.

**Example implementation**  
~~~~
// Create ZHttpRequest
ZHttpRequest http = new ZHttpRequest("GET","http://url.domain");
// Send the request
JsFile json = http.sendJsonRequest();
~~~~

This test uses the *MockZHttpRequest*.

Class references;  
 * [TestZHttpRequest](https://github.com/DyzLecticus/Zeesoft/blob/master/V4.0/ZDK/src/nl/zeesoft/zdk/test/impl/TestZHttpRequest.java)
 * [MockZHttpRequest](https://github.com/DyzLecticus/Zeesoft/blob/master/V4.0/ZDK/src/nl/zeesoft/zdk/test/impl/MockZHttpRequest.java)
 * [ZHttpRequest](https://github.com/DyzLecticus/Zeesoft/blob/master/V4.0/ZDK/src/nl/zeesoft/zdk/http/ZHttpRequest.java)
 * [JsFile](https://github.com/DyzLecticus/Zeesoft/blob/master/V4.0/ZDK/src/nl/zeesoft/zdk/json/JsFile.java)

**Test output**  
The output of this test shows the JSON response of the *MockZHttpRequest*.  
~~~~
Response:
{
  "response": "JSON response"
}
~~~~

nl.zeesoft.zdk.test.impl.TestMessenger
--------------------------------------
This test reflects a default implementation of the *Messenger* combined with the *WorkerUnion*.

**Example implementation**  
~~~~
// Create a factory
ZDKFactory factory = new ZDKFactory();
// Get the messenger from the factory
Messenger messenger = factory.getMessenger();
// Add a debug message
messenger.debug(this,"Example debug message");
// Add a warning message
messenger.warn(this,"Example warning message");
// Enable debug message printing
messenger.setPrintDebugMessages(true);
// Start the messenger
messenger.start();
// Add an error message
messenger.error(this,"Example error message");
// Stop the messenger
messenger.stop();
// Ensure all application workers are stopped
factory.getWorkerUnion(messenger).stopWorkers();
// Trigger the messenger to print the remaining messages
messenger.handleMessages();
~~~~

The *Messenger* can be used to log debug, warning and error messages and print them to the standard and/or error out.
It implements the *Worker* to minimize wait time impact for threads that call the *Messenger*.
The *Messenger* is thread safe so it can be shared across the entire application.
Classes that implement the *MessengerListener* interface can subscribe to *Messenger* message printing events.
The *WorkerUnion* can be used to ensure all workers that have been started are stopped when stopping the application.
It will log an error if it fails to stop a worker.

This test uses the *MockMessenger*.

Class references;  
 * [TestMessenger](https://github.com/DyzLecticus/Zeesoft/blob/master/V4.0/ZDK/src/nl/zeesoft/zdk/test/impl/TestMessenger.java)
 * [MockMessenger](https://github.com/DyzLecticus/Zeesoft/blob/master/V4.0/ZDK/src/nl/zeesoft/zdk/test/impl/MockMessenger.java)
 * [Messenger](https://github.com/DyzLecticus/Zeesoft/blob/master/V4.0/ZDK/src/nl/zeesoft/zdk/messenger/Messenger.java)
 * [MessengerListener](https://github.com/DyzLecticus/Zeesoft/blob/master/V4.0/ZDK/src/nl/zeesoft/zdk/messenger/MessengerListener.java)
 * [Worker](https://github.com/DyzLecticus/Zeesoft/blob/master/V4.0/ZDK/src/nl/zeesoft/zdk/thread/Worker.java)
 * [WorkerUnion](https://github.com/DyzLecticus/Zeesoft/blob/master/V4.0/ZDK/src/nl/zeesoft/zdk/thread/WorkerUnion.java)

**Test output**  
The output of this test shows the standard output of the test log messages.
~~~~
2019-10-06 13:22:17:177 DBG nl.zeesoft.zdk.test.impl.TestMessengerListener: Test log debug message before Messenger has started
2019-10-06 13:22:17:478 ERR nl.zeesoft.zdk.test.impl.TestMessengerListener: Test log error message while Messenger is working
2019-10-06 13:22:17:479 ERR nl.zeesoft.zdk.test.impl.TestMessengerListener: Test log exception stack trace
java.lang.NumberFormatException: For input string: "A"
	at java.lang.NumberFormatException.forInputString(NumberFormatException.java:65)
	at java.lang.Integer.parseInt(Integer.java:580)
	at java.lang.Integer.parseInt(Integer.java:615)
	at nl.zeesoft.zdk.test.impl.TestMessenger.test(TestMessenger.java:83)
	at nl.zeesoft.zdk.test.Tester.test(Tester.java:69)
	at nl.zeesoft.zdk.test.LibraryObject.describeAndTest(LibraryObject.java:39)
	at nl.zeesoft.zdk.test.impl.ZDK.main(ZDK.java:50)

2019-10-06 13:22:17:790 WRN nl.zeesoft.zdk.test.impl.TestMessengerListener: Test log warning message after Messenger has stopped
~~~~

nl.zeesoft.zdk.test.impl.TestZMatrix
------------------------------------
This test shows how to use a *ZMatrix* to do matrix calculations and transformations.

**Example implementation**  
~~~~
// Create the matrix
ZMatrix m = new ZMatrix(2,3);
// Randomize the matrix
m.randomize();
// Print the matrix
System.out.println(m.getTable());
~~~~

Class references;  
 * [TestZMatrix](https://github.com/DyzLecticus/Zeesoft/blob/master/V4.0/ZDK/src/nl/zeesoft/zdk/test/impl/TestZMatrix.java)
 * [ZMatrix](https://github.com/DyzLecticus/Zeesoft/blob/master/V4.0/ZDK/src/nl/zeesoft/zdk/ZMatrix.java)

**Test output**  
The output of this test shows the results of several matrix calculations and transformations.  
~~~~
Initial;
010.00 | 010.00 | 010.00
-------+--------+-------
010.00 | 010.00 | 010.00

Scalar multiplied by 3;
030.00 | 030.00 | 030.00
-------+--------+-------
030.00 | 030.00 | 030.00

Randomized;
-00.75 | 000.02 | 000.77
-------+--------+-------
-00.55 | 000.07 | -00.98

Randomized multiplied element wise;
-22.47 | 000.64 | 023.16
-------+--------+-------
-16.41 | 001.98 | -29.29

Matrix 1;
001.00 | 001.00 | 001.00
-------+--------+-------
002.00 | 002.00 | 002.00

Matrix 2;
003.00 | 003.00
-------+-------
004.00 | 004.00
-------+-------
005.00 | 005.00

Matrix multiplication of matrix 1 * matrix 2;
012.00 | 012.00
-------+-------
024.00 | 024.00

New randomized matrix;
-00.35 | -00.61 | 000.87
-------+--------+-------
-00.96 | -00.22 | -01.00

Randomized matrix transposed;
-00.35 | -00.96
-------+-------
-00.61 | -00.22
-------+-------
000.87 | -01.00
~~~~

nl.zeesoft.zdk.test.impl.TestGeneticCode
----------------------------------------
This test shows how to create, mutate and use a *GeneticCode*.

**Example implementation**  
~~~~
// Create the genetic code
GeneticCode genCode = new GeneticCode(100);
// Mutate 5 genes
genCode.mutate(5);
// Get the number of properties
int size = genCode.size();
// Get a property value
float f = genCode.get(4);
// Get a scaled integer property value
int i = genCode.getInteger(4,100);
~~~~

Class references;  
 * [TestGeneticCode](https://github.com/DyzLecticus/Zeesoft/blob/master/V4.0/ZDK/src/nl/zeesoft/zdk/test/impl/TestGeneticCode.java)
 * [GeneticCode](https://github.com/DyzLecticus/Zeesoft/blob/master/V4.0/ZDK/src/nl/zeesoft/zdk/genetic/GeneticCode.java)

**Test output**  
The output of this test shows;  
 * A generated genetic code  
 * The mutated genetic code and the resulting scaled property values  
~~~~
Genetic code: 8402765610946238974795361866824081318200491233166136394913347687059395297134504763377684604341282533
Mutated code: 8402765610946238984795361866824031318200491233166136394913847687159395297134504763377664604341282533
                               ^              ^                         ^     ^                     ^             

Scaled property values;
0: 613
1: 824
2: 159 <
3: 238
4: 297
5: 533
6: 687 <
7: 529
8: 313 <
9: 776
10: 394
11: 682 <
12: 825
13: 847 <
14: 182
15: 136
16: 776
17: 776
18: 504
19: 847 <
20: 402
21: 402 <
22: 536
23: 766 <
24: 656
25: 134
26: 134
27: 939
28: 623
29: 337 <
30: 134
31: 661
32: 593
Mutated property values: 9
~~~~

nl.zeesoft.zdk.test.impl.TestNeuralNet
--------------------------------------
This test shows how to create, train and use a *NeuralNet*.

**Example implementation**  
~~~~
// Create the neural net
NeuralNet nn = new NeuralNet(inputNeurons,hiddenLayers,hiddenNeurons,outputNeurons);
// Initialize the weights
nn.randomizeWeightsAndBiases();
nn.applyWeightFunctions();
// Get a new prediction
Prediction p = nn.getNewPrediction();
// Set the prediction inputs (0.0 - 1.0)
p.inputs[0] = 0.0F;
p.inputs[1] = 1.0F;
// Let the neural net predict the outputs
n.predict(p);
// Get a new test set
TestSet ts = nn.getNewTestSet();
// Get a new test
Test t = ts.addNewTest();
// Set the test inputs (0.0 - 1.0)
t.inputs[0] = 0.0F;
t.inputs[1] = 1.0F;
// Set the test expectations (0.0 - 1.0)
t.expectations[0] = 1.0F;
// Let the neural net predict the test outputs and calculate the error and loss
n.test(ts);
// Randomize the order of the tests
ts.randomizeOrder();
// Use the test set to train the neural net
n.train(ts);
// Repeat randomization and training until the network reaches the desired state
// ... or a maximum number of times because sometimes they fail to converge
~~~~

Class references;  
 * [TestNeuralNet](https://github.com/DyzLecticus/Zeesoft/blob/master/V4.0/ZDK/src/nl/zeesoft/zdk/test/impl/TestNeuralNet.java)
 * [NeuralNet](https://github.com/DyzLecticus/Zeesoft/blob/master/V4.0/ZDK/src/nl/zeesoft/zdk/neural/NeuralNet.java)
 * [Prediction](https://github.com/DyzLecticus/Zeesoft/blob/master/V4.0/ZDK/src/nl/zeesoft/zdk/neural/Prediction.java)
 * [TestSet](https://github.com/DyzLecticus/Zeesoft/blob/master/V4.0/ZDK/src/nl/zeesoft/zdk/neural/TestSet.java)
 * [Test](https://github.com/DyzLecticus/Zeesoft/blob/master/V4.0/ZDK/src/nl/zeesoft/zdk/neural/Test.java)

**Test output**  
The output of this test shows;  
 * The test results for 2 XOR neural net implementations before and after training.  
 * The second neural net JSON structure.  
~~~~
Neural net activator: nl.zeesoft.zdk.functions.ZLeakyReLU, learning rate: 0.1
Initial test results;
  Input: [0.00|0.00], output: [0.00], expectation: [0.00], error: 0.00, loss: 0.0
  Input: [1.00|1.00], output: [0.04], expectation: [0.00], error: -0.04, loss: 0.041709784
  Input: [0.00|1.00], output: [0.07], expectation: [1.00], error: 0.93, loss: 0.925055
  Input: [1.00|0.00], output: [-0.00], expectation: [1.00], error: 1.00, loss: 1.0000378
  Average error: 0.49, average loss: 0.49, success: false
Latest test results;
  Input: [1.00|1.00], output: [0.35], expectation: [0.00], error: -0.35, loss: 0.34907502
  Input: [0.00|1.00], output: [0.97], expectation: [1.00], error: 0.03, loss: 0.028924346
  Input: [0.00|0.00], output: [0.23], expectation: [0.00], error: -0.23, loss: 0.23400281
  Input: [1.00|0.00], output: [0.34], expectation: [1.00], error: 0.66, loss: 0.6551615
  Average error: 0.32, average loss: 0.32, success: false
Trained epochs: 10000, total average error: 3555.1702, total average loss: 3555.1702
================================================================================
Neural net activator: nl.zeesoft.zdk.functions.ZLeakyReLU, output activator: nl.zeesoft.zdk.functions.ZSoftmaxTop, learning rate: 0.1
Initial test results;
  Input: [0.00|0.00], output: [1.00], expectation: [0.00], error: -1.00, loss: 1.0
  Input: [1.00|1.00], output: [1.00], expectation: [0.00], error: -1.00, loss: 1.0
  Input: [0.00|1.00], output: [1.00], expectation: [1.00], error: 0.00, loss: 0.0
  Input: [1.00|0.00], output: [1.00], expectation: [1.00], error: 0.00, loss: 0.0
  Average error: 0.50, average loss: 0.50, success: false
Latest test results;
  Input: [0.00|0.00], output: [0.00], expectation: [0.00], error: 0.00, loss: 0.0
  Input: [1.00|0.00], output: [1.00], expectation: [1.00], error: 0.00, loss: 0.0
  Input: [1.00|1.00], output: [0.00], expectation: [0.00], error: 0.00, loss: 0.0
  Input: [0.00|1.00], output: [1.00], expectation: [1.00], error: 0.00, loss: 0.0
  Average error: 0.00, average loss: 0.00, success: true
Trained epochs: 25, total average error: 11.5, total average loss: 11.5

Neural net JSON;
{
  "inputNeurons": 2,
  "hiddenLayers": 1,
  "hiddenNeurons": 2,
  "outputNeurons": 1,
  "weightFunction": "nl.zeesoft.zdk.functions.ZWeightKaiming",
  "biasFunction": "nl.zeesoft.zdk.functions.ZWeightZero",
  "activator": "nl.zeesoft.zdk.functions.ZLeakyReLU",
  "outputActivator": "nl.zeesoft.zdk.functions.ZSoftmaxTop",
  "learningRate": 0.1,
  "values": [
    "2,1,0.0,1.0",
    "2,1,2.4076316,0.34700137",
    "1,1,1.0"
  ],
  "weights": [
    "1,1,0.0",
    "2,2,-0.22672874,0.22264022,-0.95905256,0.39063674",
    "1,2,-0.4656074,0.7137487"
  ],
  "biases": [
    "1,1,0.0",
    "2,1,2.1849914,-0.043635372",
    "1,1,0.9637339"
  ]
}
~~~~

nl.zeesoft.zdk.test.impl.TestGeneticNN
--------------------------------------
This test shows how to use a *GeneticNN* to generate a *GeneticCode* and corresponding *NeuralNet*.
It uses a *TrainingProgram* to train and test the *NeuralNet*.
It keeps generating neural nets until it finds one that passes all the tests.

**Example implementation**  
~~~~
// Create the genetic neural network
GeneticNN gnn = new GeneticNN();
// Initialize the genetic neural network
gnn.initialize(inputNeurons,maxHiddenLayers,maxHiddenNeurons,outputNeurons,codePropertyStart);
// Generate a new genetic code and corresponding neural network
gnn.generateNewNN();
~~~~

Class references;  
 * [TestGeneticNN](https://github.com/DyzLecticus/Zeesoft/blob/master/V4.0/ZDK/src/nl/zeesoft/zdk/test/impl/TestGeneticNN.java)
 * [GeneticNN](https://github.com/DyzLecticus/Zeesoft/blob/master/V4.0/ZDK/src/nl/zeesoft/zdk/genetic/GeneticNN.java)
 * [GeneticCode](https://github.com/DyzLecticus/Zeesoft/blob/master/V4.0/ZDK/src/nl/zeesoft/zdk/genetic/GeneticCode.java)
 * [NeuralNet](https://github.com/DyzLecticus/Zeesoft/blob/master/V4.0/ZDK/src/nl/zeesoft/zdk/neural/NeuralNet.java)
 * [TrainingProgram](https://github.com/DyzLecticus/Zeesoft/blob/master/V4.0/ZDK/src/nl/zeesoft/zdk/neural/TrainingProgram.java)

**Test output**  
The output of this test shows the training program outputs of one or more generated XOR neural nets.  
~~~~
Neural net activator: nl.zeesoft.zdk.functions.ZReLU, output activator: nl.zeesoft.zdk.functions.ZReLU, learning rate: 0.1872
Initial test results;
  Input: [0.00|0.00], output: [0.96], expectation: [0.00], error: -0.96, loss: 0.95711195
  Input: [1.00|1.00], output: [0.94], expectation: [0.00], error: -0.94, loss: 0.9405454
  Input: [0.00|1.00], output: [1.00], expectation: [1.00], error: 0.00, loss: 0.0039999485
  Input: [1.00|0.00], output: [0.23], expectation: [1.00], error: 0.77, loss: 0.7670843
  Average error: 0.67, average loss: 0.67, success: false

00100 -------------------/ 81%
00200 ------------------/ 79%
00300 -------------------\ 81%
00400 -------------------\ 82%
00500 -------------------/ 81%
00600 ------------------/ 77%
00700 -------------------\ 82%
00800 -------------------| 82%
00900 -------------------| 82%
01000 -------------------| 82%
01100 -------------------/ 80%
01200 -------------------\ 81%
01300 ------------------/ 77%
01400 -------------------\ 81%
01500 -------------------| 81%
01600 -------------------| 81%
01700 ------------------/ 79%
01800 -------------------\ 81%
01900 -------------------| 81%
02000 -------------------/ 80%
02100 ------------------/ 78%
02200 -------------------\ 82%
02300 -------------------/ 81%
02400 -------------------\ 82%
02500 -------------------/ 81%
02600 -------------------\ 82%
02700 ------------------/ 78%
02800 ------------------\ 79%
02900 -------------------\ 82%
03000 -------------------| 82%
03100 -------------------| 82%
03200 -------------------/ 80%
03300 ------------------/ 78%
03400 -------------------\ 81%
03500 -------------------\ 82%
03600 -------------------| 82%
03700 ------------------/ 77%
03800 -------------------\ 81%
03900 -------------------\ 82%
04000 -------------------/ 80%
04100 -------------------\ 81%
04200 -------------------| 81%
04300 ------------------/ 79%
04400 -------------------\ 81%
04500 -------------------| 81%
04600 ------------------/ 78%
04700 -------------------\ 81%
04800 -------------------\ 82%
04900 -------------------/ 80%
05000 -------------------\ 82%
05100 -------------------| 82%
05200 ------------------/ 78%
05300 -------------------\ 80%
05400 -------------------\ 81%
05500 -------------------| 81%
05600 -------------------| 81%
05700 -------------------| 81%
05800 -------------------\ 82%
05900 -------------------| 82%
06000 -------------------/ 80%
06100 ------------------/ 78%
06200 -------------------\ 81%
06300 -------------------\ 82%
06400 -------------------/ 81%
06500 -------------------| 81%
06600 -------------------| 81%
06700 -------------------| 81%
06800 ------------------/ 79%
06900 -------------------\ 80%
07000 -------------------\ 82%
07100 -------------------/ 81%
07200 ------------------/ 79%
07300 -------------------\ 82%
07400 -------------------/ 80%
07500 -------------------\ 81%
07600 -------------------| 81%
07700 -------------------\ 82%
07800 -------------------/ 81%
07900 -------------------| 81%
08000 -------------------\ 82%
08100 -------------------| 82%
08200 ------------------/ 78%
08300 -------------------\ 81%
08400 -------------------| 81%
08500 -------------------\ 82%
08600 -------------------| 82%
08700 -------------------/ 81%
08800 -------------------| 81%
08900 ------------------/ 79%
09000 -------------------\ 81%
09100 ------------------/ 79%
09200 ------------------/ 78%
09300 -------------------\ 82%
09400 ------------------/ 79%
09500 ------------------| 79%
09600 -------------------\ 82%
09700 ------------------/ 78%
09800 -------------------\ 81%
09900 ------------------/ 78%
10000 -------------------\ 82%

Latest test results;
  Input: [1.00|1.00], output: [0.52], expectation: [0.00], error: -0.52, loss: 0.5175048
  Input: [1.00|0.00], output: [0.42], expectation: [1.00], error: 0.58, loss: 0.57937205
  Input: [0.00|0.00], output: [0.53], expectation: [0.00], error: -0.53, loss: 0.52908635
  Input: [0.00|1.00], output: [0.43], expectation: [1.00], error: 0.57, loss: 0.5699586
  Average error: 0.55, average loss: 0.55, success: false
Trained epochs: 10000, total average error: 5425.5864, total average loss: 5425.5864
================================================================================
Neural net activator: nl.zeesoft.zdk.functions.ZTanH, output activator: nl.zeesoft.zdk.functions.ZLeakyReLU, learning rate: 0.112
Initial test results;
  Input: [0.00|0.00], output: [-0.00], expectation: [0.00], error: 0.00, loss: 0.0027463895
  Input: [1.00|1.00], output: [-0.00], expectation: [0.00], error: 0.00, loss: 0.0045988085
  Input: [0.00|1.00], output: [-0.00], expectation: [1.00], error: 1.00, loss: 1.0040946
  Input: [1.00|0.00], output: [-0.00], expectation: [1.00], error: 1.00, loss: 1.0033264
  Average error: 0.50, average loss: 0.50, success: false

00100 ---------------------------\ 113%
00200 ---------------------------/ 112%
00300 --------------------------/ 111%
00400 ------------------------/ 100%
00500 ----------------------------\ 118%
00600 ---------------------------/ 114%
00700 ----------------------------\ 116%
00800 ----------------------------\ 118%
00900 ---------------------------/ 115%
01000 --------------------------/ 108%
01100 --------------------------\ 109%
01200 -------------------------/ 105%
01300 ---------------------------\ 113%
01400 ---------------------------| 113%
01500 -----------------------/ 98%
01600 ---------------------------\ 112%
01700 ------------------------/ 100%
01800 --------------------------\ 108%
01900 ---------------------------\ 113%
02000 ------------------------/ 100%
02100 ----------------------------\ 116%
02200 ---------------------------/ 115%
02300 ----------------------------\ 116%
02400 ---------------------------/ 114%
02500 ----------------------------\ 116%
02600 -------------------------/ 105%
02700 ---------------------------\ 113%
02800 -----------------------------\ 120%
02900 ---------------------------/ 115%
03000 ---------------------------| 115%
03100 ----------------------------\ 116%
03200 ---------------------------/ 115%
03300 ---------------------------/ 114%
03400 ----------------------------\ 119%
03500 ---------------------------/ 113%
03600 -------------------------/ 105%
03700 ---------------------------\ 115%
03800 ----------------------------\ 118%
03900 ---------------------------/ 115%
04000 ---------------------------/ 113%
04100 ------------------------/ 103%
04200 ---------------------------\ 115%
04300 ----------------------------\ 119%
04400 ------------------------/ 101%
04500 --------------------------\ 110%
04600 -----------------------------\ 120%
04700 ---------------------------/ 113%
04800 --------------------------/ 110%
04900 ------------------------/ 102%
05000 ---------------------------\ 115%
05100 ----------------------------\ 116%
05200 ----------------------------\ 117%
05300 -------------------------/ 107%
05400 ---------------------------\ 115%
05500 ---------------------------| 115%
05600 ---------------------------| 115%
05700 ------------------------/ 102%
05800 ---------------------------\ 112%
05900 ------------------------/ 100%
06000 ---------------------------\ 115%
06100 -----------------------------\ 120%
06200 --------------------------/ 110%
06300 ---------------------------\ 115%
06400 ---------------------------| 115%
06500 ----------------------------\ 119%
06600 ------------------------/ 103%
06700 -------------------------\ 105%
06800 ----------------------------\ 119%
06900 ------------------------/ 103%
07000 -------------------------\ 107%
07100 -----------------------------\ 121%
07200 ---------------------------/ 115%
07300 ----------------------------\ 118%
07400 ---------------------------/ 114%
07500 ---------------------------\ 115%
07600 ---------------------------/ 112%
07700 -------------------------/ 104%
07800 -----------------------------\ 121%
07900 ----------------------------/ 117%
08000 -----------------------------\ 120%
08100 ---------------------------/ 115%
08200 -------------------------/ 105%
08300 -----------------------------\ 121%
08400 ---------------------------/ 115%
08500 ----------------------------\ 116%
08600 ----------------------------\ 117%
08700 ----------------------------\ 119%
08800 -------------------------/ 107%
08900 ----------------------------\ 118%
09000 ----------------------------/ 117%
09100 ---------------------------/ 115%
09200 ---------------------------| 115%
09300 --------------------------/ 108%
09400 ------------------------/ 100%
09500 ---------------------------\ 114%
09600 ----------------------------\ 116%
09700 ----------------------------| 116%
09800 ------------------------/ 103%
09900 ----------------------------\ 117%
10000 ---------------------------/ 114%

Latest test results;
  Input: [1.00|0.00], output: [0.60], expectation: [1.00], error: 0.40, loss: 0.40168417
  Input: [1.00|1.00], output: [0.73], expectation: [0.00], error: -0.73, loss: 0.73328173
  Input: [0.00|0.00], output: [0.49], expectation: [0.00], error: -0.49, loss: 0.48689896
  Input: [0.00|1.00], output: [0.32], expectation: [1.00], error: 0.68, loss: 0.6766991
  Average error: 0.57, average loss: 0.57, success: false
Trained epochs: 10000, total average error: 5700.8784, total average loss: 5700.8784
================================================================================
Neural net activator: nl.zeesoft.zdk.functions.ZLeakyReLU, output activator: nl.zeesoft.zdk.functions.ZLeakyReLU, learning rate: 0.0816
Initial test results;
  Input: [0.00|0.00], output: [0.00], expectation: [0.00], error: 0.00, loss: 0.0
  Input: [1.00|1.00], output: [-0.00], expectation: [0.00], error: 0.00, loss: 9.2887995E-4
  Input: [0.00|1.00], output: [-0.01], expectation: [1.00], error: 1.01, loss: 1.0050209
  Input: [1.00|0.00], output: [0.23], expectation: [1.00], error: 0.77, loss: 0.76778543
  Average error: 0.44, average loss: 0.44, success: false

00100 ------------------/ 79%
00200 -------------------\ 80%
00300 -------------------| 80%
00400 -------------------| 80%
00500 ------------------/ 79%
00600 -------------------\ 81%
00700 ------------------/ 78%
00800 -------------------\ 82%
00900 ------------------/ 79%
01000 -------------------\ 80%
01100 ------------------/ 79%
01200 -------------------\ 80%
01300 ------------------/ 79%
01400 -------------------\ 81%
01500 ------------------/ 79%
01600 ------------------/ 78%
01700 ------------------\ 79%
01800 ------------------| 79%
01900 ------------------| 79%
02000 -------------------\ 81%
02100 -------------------| 81%
02200 ------------------/ 79%
02300 ------------------| 79%
02400 ------------------/ 77%
02500 -------------------\ 80%
02600 ------------------/ 77%
02700 -------------------\ 80%
02800 ------------------/ 78%
02900 ------------------\ 79%
03000 ------------------| 79%
03100 -------------------\ 80%
03200 ------------------/ 77%
03300 -------------------\ 80%
03400 ------------------/ 78%
03500 ------------------\ 79%
03600 ------------------/ 78%
03700 -------------------\ 80%
03800 -------------------| 80%
03900 -------------------| 80%
04000 -------------------| 80%
04100 -------------------| 80%
04200 -------------------| 80%
04300 -------------------| 80%
04400 ------------------/ 79%
04500 -------------------\ 80%
04600 ------------------/ 79%
04700 -------------------\ 80%
04800 -------------------\ 81%
04900 -------------------\ 82%
05000 ------------------/ 79%
05100 ------------------/ 78%
05200 -------------------\ 81%
05300 -------------------\ 82%
05400 -------------------/ 80%
05500 -------------------\ 81%
05600 -------------------/ 80%
05700 ------------------/ 79%
05800 -------------------\ 81%
05900 ------------------/ 77%
06000 -------------------\ 82%
06100 ------------------/ 79%
06200 -------------------\ 80%
06300 ------------------/ 79%
06400 ------------------/ 77%
06500 ------------------| 77%
06600 ------------------\ 79%
06700 ------------------| 79%
06800 -------------------\ 81%
06900 -------------------| 81%
07000 -------------------/ 80%
07100 -------------------\ 81%
07200 ------------------/ 79%
07300 ------------------| 79%
07400 -------------------\ 81%
07500 ------------------/ 79%
07600 ------------------| 79%
07700 -------------------\ 80%
07800 -------------------\ 83%
07900 ------------------/ 79%
08000 -------------------\ 80%
08100 ------------------/ 78%
08200 ------------------/ 77%
08300 ------------------\ 79%
08400 -------------------\ 80%
08500 ------------------/ 79%
08600 -------------------\ 83%
08700 ------------------/ 79%
08800 -------------------\ 82%
08900 -------------------/ 81%
09000 ------------------/ 77%
09100 -------------------\ 80%
09200 ------------------/ 79%
09300 -------------------\ 80%
09400 ------------------/ 77%
09500 -------------------\ 80%
09600 --------------------\ 84%
09700 ------------------/ 79%
09800 -------------------\ 80%
09900 ------------------/ 79%
10000 ------------------| 79%

Latest test results;
  Input: [0.00|0.00], output: [0.34], expectation: [0.00], error: -0.34, loss: 0.33791637
  Input: [1.00|1.00], output: [0.31], expectation: [0.00], error: -0.31, loss: 0.30645353
  Input: [1.00|0.00], output: [0.94], expectation: [1.00], error: 0.06, loss: 0.060328424
  Input: [0.00|1.00], output: [0.29], expectation: [1.00], error: 0.71, loss: 0.71191764
  Average error: 0.35, average loss: 0.35, success: false
Trained epochs: 10000, total average error: 3558.896, total average loss: 3558.896
================================================================================
Neural net activator: nl.zeesoft.zdk.functions.ZSigmoid, output activator: nl.zeesoft.zdk.functions.ZTanH, learning rate: 0.0366
Initial test results;
  Input: [0.00|0.00], output: [-0.78], expectation: [0.00], error: 0.78, loss: 0.77633816
  Input: [1.00|1.00], output: [-0.80], expectation: [0.00], error: 0.80, loss: 0.8032298
  Input: [0.00|1.00], output: [-0.81], expectation: [1.00], error: 1.81, loss: 1.8140293
  Input: [1.00|0.00], output: [-0.77], expectation: [1.00], error: 1.77, loss: 1.7705868
  Average error: 1.29, average loss: 1.29, success: false

00100 --------/ 39%
00200 --------| 39%
00300 --------| 39%
00400 --------| 39%
00500 --------| 39%
00600 --------| 39%
00700 --------| 39%
00800 --------| 39%
00900 --------| 39%
01000 --------| 39%
01100 --------| 39%
01200 --------| 39%
01300 --------| 39%
01400 --------| 39%
01500 --------| 39%
01600 --------| 39%
01700 --------/ 38%
01800 --------| 38%
01900 --------| 38%
02000 --------| 38%
02100 --------| 38%
02200 --------/ 37%
02300 --------/ 36%
02400 --------| 36%
02500 -------/ 35%
02600 -------| 35%
02700 -------/ 34%
02800 -------/ 33%
02900 -------/ 32%
03000 ------/ 31%
03100 ------| 31%
03200 ------/ 30%
03300 ------/ 29%
03400 ------| 29%
03500 ------/ 28%
03600 ------| 28%
03700 -----/ 27%
03800 -----/ 26%
03900 -----/ 25%
04000 ----/ 23%
04100 ----/ 21%
04200 ---/ 18%
04300 ---/ 16%
04400 --/ 14%
04500 --/ 12%
04600 -/ 11%
04700 -/ 10%
04800 -/ 8%
04900 / 7%
05000 / 6%
05100 | 6%
05200 / 5%

Latest test results;
  Input: [0.00|1.00], output: [0.90], expectation: [1.00], error: 0.10, loss: 0.099905014
  Input: [1.00|0.00], output: [0.90], expectation: [1.00], error: 0.10, loss: 0.09868097
  Input: [1.00|1.00], output: [0.07], expectation: [0.00], error: -0.07, loss: 0.06757899
  Input: [0.00|0.00], output: [0.03], expectation: [0.00], error: -0.03, loss: 0.027678095
  Average error: 0.07, average loss: 0.07, success: true
Trained epochs: 5206, total average error: 2032.1602, total average loss: 2032.1602
~~~~

nl.zeesoft.zdk.test.impl.TestEvolver
------------------------------------
This test shows how to use an *Evolver* and a *TestSet* to generate, train and select the best *GeneticNN* for a certain task.  
Evolvers use multi threading to use processing power effectively.  
When specifying multiple evolvers, half of them are used to generate completely new neural nets.  
The other half are used to generate mutations of the best-so-far generated neural net.  

**Example implementation**  
~~~~
// Create the TestSet
TestSet tSet = new TestSet(inputs,outputs);
// (Add tests to the test ...)
// Create the Evolver
Evolver evolver = new Evolver(new Messenger(),new WorkerUnion(),maxHiddenLayers,maxHiddenNeurons,codePropertyStart,tSet,evolvers);
// Start the evolver
evolver.start();
// (Give it some time ...)
// Stop the evolver
evolver.stop();
// Get the best-so-far result
EvolverUnit unit = evolver.getBestSoFar();
~~~~

Class references;  
 * [TestEvolver](https://github.com/DyzLecticus/Zeesoft/blob/master/V4.0/ZDK/src/nl/zeesoft/zdk/test/impl/TestEvolver.java)
 * [Evolver](https://github.com/DyzLecticus/Zeesoft/blob/master/V4.0/ZDK/src/nl/zeesoft/zdk/genetic/Evolver.java)
 * [TestSet](https://github.com/DyzLecticus/Zeesoft/blob/master/V4.0/ZDK/src/nl/zeesoft/zdk/neural/TestSet.java)
 * [GeneticNN](https://github.com/DyzLecticus/Zeesoft/blob/master/V4.0/ZDK/src/nl/zeesoft/zdk/genetic/GeneticNN.java)

**Test output**  
The output of this test shows the evolver debug output and the evolver object converted to JSON.  
~~~~
2019-10-06 13:22:18:964 DBG nl.zeesoft.zdk.genetic.Evolver: Started
2019-10-06 13:22:19:046 DBG nl.zeesoft.zdk.genetic.Evolver: Selected new best genetic neural net;
- Code: 4330561257179572
- Size: 14
- Initial average loss: 0.47393 (final: 0.05031)
- Total average loss: 27.51228 (epochs: 83)
- Training result: 13.03895
2019-10-06 13:22:21:783 DBG nl.zeesoft.zdk.genetic.Evolver: Selected new best genetic neural net;
- Code: 7928268014653897
- Size: 14
- Initial average loss: 0.43553 (final: 0.06438)
- Total average loss: 19.64283 (epochs: 78)
- Training result: 8.55507
2019-10-06 13:22:22:180 DBG nl.zeesoft.zdk.genetic.Evolver: Selected new best genetic neural net;
- Code: 7928268014653897
- Size: 14
- Initial average loss: 0.45175 (final: 0.04641)
- Total average loss: 16.77221 (epochs: 58)
- Training result: 7.57690
2019-10-06 13:22:22:790 DBG nl.zeesoft.zdk.genetic.Evolver: Selected new best genetic neural net;
- Code: 7928268014654897
- Size: 14
- Initial average loss: 0.45175 (final: 0.05399)
- Total average loss: 10.26192 (epochs: 40)
- Training result: 4.63586
2019-10-06 13:22:33:221 DBG nl.zeesoft.zdk.genetic.Evolver: Selected new best genetic neural net;
- Code: 7344049677259920
- Size: 14
- Initial average loss: 0.50000 (final: 0.00000)
- Total average loss: 3.25000 (epochs: 9)
- Training result: 1.62500
2019-10-06 13:22:33:525 DBG nl.zeesoft.zdk.genetic.Evolver: Selected new best genetic neural net;
- Code: 7344049675259920
- Size: 14
- Initial average loss: 0.25000 (final: 0.00000)
- Total average loss: 0.75000 (epochs: 4)
- Training result: 0.18750
2019-10-06 13:22:35:423 DBG nl.zeesoft.zdk.genetic.Evolver: Selected new best genetic neural net;
- Code: 7344049675959920
- Size: 14
- Initial average loss: 0.25000 (final: 0.00000)
- Total average loss: 0.50000 (epochs: 2)
- Training result: 0.12500
2019-10-06 13:22:35:496 DBG nl.zeesoft.zdk.genetic.Evolver: Selected new best genetic neural net;
- Code: 7344049675959920
- Size: 14
- Initial average loss: 0.00000 (final: 0.00000)
- Total average loss: 0.00000 (epochs: 0)
- Training result: 0.00000

Evolver JSON;
{
  "mutationRate": 0.05,
  "trainEpochBatches": 1000,
  "trainEpochBatchSize": 10,
  "checkFactorQuarter": 0.75,
  "checkFactorHalf": 0.5,
  "sleepMs": 10,
  "sleepMsFoundBest": 10,
  "maxLogLines": 20,
  "log": [
    "2019-10-06 13:22:19:046 SEL code: 4330561257179572, size: 14, initial loss: 0.47393 (final: 0.05031), total loss: 27.51228, result: 13.03895 (epochs: 83)",
    "2019-10-06 13:22:21:783 SEL code: 7928268014653897, size: 14, initial loss: 0.43553 (final: 0.06438), total loss: 19.64283, result: 8.55507 (epochs: 78)",
    "2019-10-06 13:22:22:180 SEL code: 7928268014653897, size: 14, initial loss: 0.45175 (final: 0.04641), total loss: 16.77221, result: 7.57690 (epochs: 58)",
    "2019-10-06 13:22:22:790 SEL code: 7928268014654897, size: 14, initial loss: 0.45175 (final: 0.05399), total loss: 10.26192, result: 4.63586 (epochs: 40)",
    "2019-10-06 13:22:33:221 SEL code: 7344049677259920, size: 14, initial loss: 0.50000 (final: 0.00000), total loss: 3.25000, result: 1.62500 (epochs: 9)",
    "2019-10-06 13:22:33:525 SEL code: 7344049675259920, size: 14, initial loss: 0.25000 (final: 0.00000), total loss: 0.75000, result: 0.18750 (epochs: 4)",
    "2019-10-06 13:22:35:423 SEL code: 7344049675959920, size: 14, initial loss: 0.25000 (final: 0.00000), total loss: 0.50000, result: 0.12500 (epochs: 2)",
    "2019-10-06 13:22:35:496 SEL code: 7344049675959920, size: 14, initial loss: 0.00000 (final: 0.00000), total loss: 0.00000, result: 0.00000 (epochs: 0)"
  ],
  "bestSoFar": [
    {
      "geneticNN": [
        {
          "inputNeurons": 2,
          "maxHiddenLayers": 1,
          "maxHiddenNeurons": 2,
          "outputNeurons": 1,
          "codePropertyStart": 0,
          "code": "TLOGvOKJRPzAgKDNmGZIVMvKoJ9AbOBHINKI7CRIdEUDEC1G0OGNqLYCKKyL4JgCdCjH2",
          "neuralNet": [
            {
              "inputNeurons": 2,
              "hiddenLayers": 1,
              "hiddenNeurons": 2,
              "outputNeurons": 1,
              "weightFunction": "nl.zeesoft.zdk.functions.ZWeightKaiming",
              "biasFunction": "nl.zeesoft.zdk.functions.ZWeightKaiming",
              "activator": "nl.zeesoft.zdk.functions.ZReLU",
              "outputActivator": "nl.zeesoft.zdk.functions.ZSoftmaxTop",
              "learningRate": 0.10340001,
              "values": [
                "2,1,0.0,0.0",
                "2,1,0.07511769,0.07511769",
                "1,1,0.0"
              ],
              "weights": [
                "1,1,0.0",
                "2,2,-0.53562176,0.3739554,0.31680068,-0.46377003",
                "1,2,0.56828165,0.6858572"
              ],
              "biases": [
                "1,1,0.0",
                "2,1,0.07511769,0.07511769",
                "1,1,-0.2547469"
              ]
            }
          ]
        }
      ],
      "trainingProgram": [
        {
          "stopOnSuccess": true,
          "trainedEpochs": 0,
          "totalAverageError": 0.0,
          "totalAverageLoss": 0.0,
          "initialResults": [
            {
              "inputNeurons": 2,
              "outputNeurons": 1,
              "lossFunction": "nl.zeesoft.zdk.functions.ZMeanAbsoluteError",
              "errorTolerance": 0.1,
              "averageError": 0.0,
              "averageLoss": 0.0,
              "success": true,
              "tests": [
                {
                  "inputs": "1.0,1.0",
                  "outputs": "0.0",
                  "expectations": "0.0",
                  "errors": "0.0"
                },
                {
                  "inputs": "1.0,0.0",
                  "outputs": "1.0",
                  "expectations": "1.0",
                  "errors": "0.0"
                },
                {
                  "inputs": "0.0,1.0",
                  "outputs": "1.0",
                  "expectations": "1.0",
                  "errors": "0.0"
                },
                {
                  "inputs": "0.0,0.0",
                  "outputs": "0.0",
                  "expectations": "0.0",
                  "errors": "0.0"
                }
              ]
            }
          ],
          "latestResults": [
            {
              "inputNeurons": 2,
              "outputNeurons": 1,
              "lossFunction": "nl.zeesoft.zdk.functions.ZMeanAbsoluteError",
              "errorTolerance": 0.1,
              "averageError": 0.0,
              "averageLoss": 0.0,
              "success": true,
              "tests": [
                {
                  "inputs": "1.0,1.0",
                  "outputs": "0.0",
                  "expectations": "0.0",
                  "errors": "0.0"
                },
                {
                  "inputs": "1.0,0.0",
                  "outputs": "1.0",
                  "expectations": "1.0",
                  "errors": "0.0"
                },
                {
                  "inputs": "0.0,1.0",
                  "outputs": "1.0",
                  "expectations": "1.0",
                  "errors": "0.0"
                },
                {
                  "inputs": "0.0,0.0",
                  "outputs": "0.0",
                  "expectations": "0.0",
                  "errors": "0.0"
                }
              ]
            }
          ]
        }
      ]
    }
  ]
}
~~~~

nl.zeesoft.zdk.test.impl.htm.TestSDR
------------------------------------
This test shows how to create and compare sparse distributed representations using *SDR* instances.

**Example implementation**  
~~~~
// Create the SDR
SDR sdrA = new SDR(100);
// Turn on the first and last bits
sdrA.setBit(0,true);
sdrA.setBit(99,true);
// Create another SDR
SDR sdrB = new SDR(100);
// Turn on the first and middle bits
sdrB.setBit(0,true);
sdrB.setBit(50,true);
// Check if the SDRs have one overlapping bit
System.out.println(sdrA.matches(sdrB,1));
~~~~

Class references;  
 * [TestSDR](https://github.com/DyzLecticus/Zeesoft/blob/master/V4.0/ZDK/src/nl/zeesoft/zdk/test/impl/htm/TestSDR.java)
 * [SDR](https://github.com/DyzLecticus/Zeesoft/blob/master/V4.0/ZDK/src/nl/zeesoft/zdk/htm/util/SDR.java)

**Test output**  
The output of this test shows two SDRs and if they match at least one bit.
~~~~
SDR A: 100,0,99
SDR B: 100,0,50
Match: true
~~~~

nl.zeesoft.zdk.test.impl.htm.TestSDRMap
---------------------------------------
This test shows how to use an *SDRMap* to maintain a list of Sparse Distributed Representations.
By default, an *SDRMap* uses an index for each bit of every SDR in the list so it can quickly retrieve matching SDRs.

**Example implementation**  
~~~~
// Create the SDR map
SDRMap sdrMap = new SDRMap(100);
// Create an SDR
SDR sdrA = new SDR(100);
sdrA.randomize(2);
// Create another SDR
SDR sdrB = new SDR(100);
sdrB.randomize(2);
// Add the SDRs to the SDR map
sdrMap.add(sdrA);
sdrMap.add(sdrB);
// Create a third SDR
SDR sdrC = new SDR(100);
sdrC.randomize(2);
// Get matches from the SDR map
SortedMap<Integer,List<SDR>> matchesByOverlapScore = sdrMap.getMatches(sdrC);
~~~~

Class references;  
 * [TestSDRMap](https://github.com/DyzLecticus/Zeesoft/blob/master/V4.0/ZDK/src/nl/zeesoft/zdk/test/impl/htm/TestSDRMap.java)
 * [SDRMap](https://github.com/DyzLecticus/Zeesoft/blob/master/V4.0/ZDK/src/nl/zeesoft/zdk/htm/util/SDRMap.java)
 * [SDR](https://github.com/DyzLecticus/Zeesoft/blob/master/V4.0/ZDK/src/nl/zeesoft/zdk/htm/util/SDR.java)

**Test output**  
The output of this test shows the SDR map, a third SDR and the number of matches for the that SDR in the set.
~~~~
SDR map: 100,2|19,42|21,30
Number of SDR A matches in SDR map: 1
SDR C: 100,7,91
Number of SDR C matches in SDR map: 0
~~~~

nl.zeesoft.zdk.test.impl.htm.TestScalarEncoder
----------------------------------------------
This test shows how to use a *ScalarEncoder* to convert a range of scalar values into sparse distributed representations.

**Example implementation**  
~~~~
// Create the encoder
ScalarEncoder enc = new ScalarEncoder(52,2,0,50);
// Obtain the SDR for a certain value
SDR sdr = enc.getSDRForValue(0);
~~~~

Class references;  
 * [TestScalarEncoder](https://github.com/DyzLecticus/Zeesoft/blob/master/V4.0/ZDK/src/nl/zeesoft/zdk/test/impl/htm/TestScalarEncoder.java)
 * [ScalarEncoder](https://github.com/DyzLecticus/Zeesoft/blob/master/V4.0/ZDK/src/nl/zeesoft/zdk/htm/enc/ScalarEncoder.java)
 * [SDR](https://github.com/DyzLecticus/Zeesoft/blob/master/V4.0/ZDK/src/nl/zeesoft/zdk/htm/util/SDR.java)

**Test output**  
The output of this test shows two scalar encoders and the SDRs they generate for several values.
~~~~
Encoder size: 52, bits: 2, min: 0, max: 50
SDR for value 0:  1100000000000000000000000000000000000000000000000000
SDR for value 1:  0110000000000000000000000000000000000000000000000000
SDR for value 24: 0000000000000000000000001100000000000000000000000000
SDR for value 25: 0000000000000000000000000110000000000000000000000000
SDR for value 26: 0000000000000000000000000011000000000000000000000000
SDR for value 49: 0000000000000000000000000000000000000000000000000110
SDR for value 50: 0000000000000000000000000000000000000000000000000011
SDR for value 51: 0000000000000000000000000000000000000000000000000011

Periodic encoder size: 50, bits: 2, min: 0, max: 50
SDR for value 0:  11000000000000000000000000000000000000000000000000
SDR for value 1:  01100000000000000000000000000000000000000000000000
SDR for value 24: 00000000000000000000000011000000000000000000000000
SDR for value 25: 00000000000000000000000001100000000000000000000000
SDR for value 26: 00000000000000000000000000110000000000000000000000
SDR for value 49: 10000000000000000000000000000000000000000000000001
SDR for value 50: 11000000000000000000000000000000000000000000000000
SDR for value 51: 01100000000000000000000000000000000000000000000000
~~~~

nl.zeesoft.zdk.test.impl.htm.TestRDScalarEncoder
------------------------------------------------
This test shows how to use an *RDScalarEncoder* to convert a range of scalar values into sparse distributed representations.

**Example implementation**  
~~~~
// Create the encoder
RDScalarEncoder enc = new RDScalarEncoder(50,4);
// Obtain the SDR for a certain value
SDR sdr = enc.getSDRForValue(0);
~~~~

Class references;  
 * [TestRDScalarEncoder](https://github.com/DyzLecticus/Zeesoft/blob/master/V4.0/ZDK/src/nl/zeesoft/zdk/test/impl/htm/TestRDScalarEncoder.java)
 * [RDScalarEncoder](https://github.com/DyzLecticus/Zeesoft/blob/master/V4.0/ZDK/src/nl/zeesoft/zdk/htm/enc/RDScalarEncoder.java)
 * [SDR](https://github.com/DyzLecticus/Zeesoft/blob/master/V4.0/ZDK/src/nl/zeesoft/zdk/htm/util/SDR.java)

**Test output**  
The output of this test shows a random distributed scalar encoder and the SDRs it generated for several values.
~~~~
Random distributed encoder size: 50, bits: 4, capacity: 230300
SDR for value 0:  00000000000000011000000000000100000000000001000000
SDR for value 1:  00000000000000011000000000001100000000000000000000
SDR for value 24: 00000000000000000100000000010000000010000010000000
SDR for value 25: 00000100000000000000000000010000000010000010000000
SDR for value 75: 10000000000000000000001000000100000000000000010000
SDR for value 76: 10000000000100000000000000000100000000000000010000
SDR for value -1: 00000000000000011000010000000000000000000001000000
~~~~

nl.zeesoft.zdk.test.impl.htm.TestDateTimeEncoder
------------------------------------------------
This test shows how to use a *DateTimeEncoder* to convert a range of dates/times into combined periodic sparse distributed representations.
The *DateTimeEncoder* is merely an example implementation of a *CombinedEncoder* used to test this library.

**Example implementation**  
~~~~
// Create the encoder
DateTimeEncoder enc = new DateTimeEncoder();
// Obtain the SDR for a certain value
SDR sdr = enc.getSDRForValue(System.currentTimeMillis());
~~~~

Class references;  
 * [TestDateTimeEncoder](https://github.com/DyzLecticus/Zeesoft/blob/master/V4.0/ZDK/src/nl/zeesoft/zdk/test/impl/htm/TestDateTimeEncoder.java)
 * [DateTimeEncoder](https://github.com/DyzLecticus/Zeesoft/blob/master/V4.0/ZDK/src/nl/zeesoft/zdk/htm/enc/DateTimeEncoder.java)
 * [CombinedEncoder](https://github.com/DyzLecticus/Zeesoft/blob/master/V4.0/ZDK/src/nl/zeesoft/zdk/htm/enc/CombinedEncoder.java)
 * [SDR](https://github.com/DyzLecticus/Zeesoft/blob/master/V4.0/ZDK/src/nl/zeesoft/zdk/htm/util/SDR.java)

**Test output**  
The output of this test shows how the generated SDRs represent several date/times.
~~~~
Changing months;
SDR for 2019-01-01 01:00:00:000; 111111110000000000000000000000000000000000000000111111110000000000000000000000000000000000000000000000111111110000000000
SDR for 2019-02-01 01:00:00:000; 111111110000000000000000000000000000000000000000000011111111000000000000000000000000000000000000100000000000000001111111
SDR for 2019-03-01 01:00:00:000; 111111110000000000000000000000000000000000000000000000001111111100000000000000000000000000000000100000000000000001111111
SDR for 2019-04-01 02:00:00:000; 111111110000000000000000000000000000000000000000000000000000111111110000000000000000000000000000000111111110000000000000
SDR for 2019-05-01 02:00:00:000; 111111110000000000000000000000000000000000000000000000000000000011111111000000000000000000000000000000000011111111000000
SDR for 2019-06-01 02:00:00:000; 111111110000000000000000000000000000000000000000000000000000000000001111111100000000000000000000111100000000000000001111
SDR for 2019-07-01 02:00:00:000; 111111110000000000000000000000000000000000000000000000000000000000000000111111110000000000000000000111111110000000000000
SDR for 2019-08-01 02:00:00:000; 111111110000000000000000000000000000000000000000000000000000000000000000000011111111000000000000000000000000011111111000
SDR for 2019-09-01 02:00:00:000; 111111110000000000000000000000000000000000000000000000000000000000000000000000001111111100000000111111110000000000000000
SDR for 2019-10-01 02:00:00:000; 111111110000000000000000000000000000000000000000000000000000000000000000000000000000111111110000000000111111110000000000
SDR for 2019-11-01 01:00:00:000; 111111110000000000000000000000000000000000000000000000000000000000000000000000000000000011111111100000000000000001111111
SDR for 2019-12-01 01:00:00:000; 111111110000000000000000000000000000000000000000111100000000000000000000000000000000000000001111111111110000000000000000

Changing days of week;
SDR for 2020-01-01 01:00:00:000; 111111110000000000000000000000000000000000000000111111110000000000000000000000000000000000000000000000000011111111000000
SDR for 2020-01-02 01:00:00:000; 111111110000000000000000000000000000000000000000111111110000000000000000000000000000000000000000000000000000011111111000
SDR for 2020-01-03 01:00:00:000; 111111110000000000000000000000000000000000000000111111110000000000000000000000000000000000000000100000000000000001111111
SDR for 2020-01-04 01:00:00:000; 111111110000000000000000000000000000000000000000111111110000000000000000000000000000000000000000111100000000000000001111
SDR for 2020-01-05 01:00:00:000; 111111110000000000000000000000000000000000000000111111110000000000000000000000000000000000000000111111110000000000000000
SDR for 2020-01-06 01:00:00:000; 111111110000000000000000000000000000000000000000111111110000000000000000000000000000000000000000000111111110000000000000
SDR for 2020-01-07 01:00:00:000; 111111110000000000000000000000000000000000000000111111110000000000000000000000000000000000000000000000111111110000000000

Changing hours of day;
SDR for 2020-01-08 01:00:00:000; 111111110000000000000000000000000000000000000000011111111000000000000000000000000000000000000000000000000011111111000000
SDR for 2020-01-08 02:00:00:000; 001111111100000000000000000000000000000000000000011111111000000000000000000000000000000000000000000000000011111111000000
SDR for 2020-01-08 03:00:00:000; 000011111111000000000000000000000000000000000000011111111000000000000000000000000000000000000000000000000011111111000000
SDR for 2020-01-08 04:00:00:000; 000000111111110000000000000000000000000000000000011111111000000000000000000000000000000000000000000000000011111111000000
SDR for 2020-01-08 05:00:00:000; 000000001111111100000000000000000000000000000000011111111000000000000000000000000000000000000000000000000011111111000000
SDR for 2020-01-08 06:00:00:000; 000000000011111111000000000000000000000000000000011111111000000000000000000000000000000000000000000000000001111111100000
SDR for 2020-01-08 07:00:00:000; 000000000000111111110000000000000000000000000000011111111000000000000000000000000000000000000000000000000001111111100000
SDR for 2020-01-08 08:00:00:000; 000000000000001111111100000000000000000000000000011111111000000000000000000000000000000000000000000000000001111111100000
SDR for 2020-01-08 09:00:00:000; 000000000000000011111111000000000000000000000000011111111000000000000000000000000000000000000000000000000001111111100000
SDR for 2020-01-08 10:00:00:000; 000000000000000000111111110000000000000000000000011111111000000000000000000000000000000000000000000000000001111111100000
SDR for 2020-01-08 11:00:00:000; 000000000000000000001111111100000000000000000000011111111000000000000000000000000000000000000000000000000001111111100000
SDR for 2020-01-08 12:00:00:000; 000000000000000000000011111111000000000000000000011111111000000000000000000000000000000000000000000000000001111111100000
SDR for 2020-01-08 13:00:00:000; 000000000000000000000000111111110000000000000000011111111000000000000000000000000000000000000000000000000000111111110000
SDR for 2020-01-08 14:00:00:000; 000000000000000000000000001111111100000000000000011111111000000000000000000000000000000000000000000000000000111111110000
SDR for 2020-01-08 15:00:00:000; 000000000000000000000000000011111111000000000000011111111000000000000000000000000000000000000000000000000000111111110000
SDR for 2020-01-08 16:00:00:000; 000000000000000000000000000000111111110000000000011111111000000000000000000000000000000000000000000000000000111111110000
SDR for 2020-01-08 17:00:00:000; 000000000000000000000000000000001111111100000000011111111000000000000000000000000000000000000000000000000000111111110000
SDR for 2020-01-08 18:00:00:000; 000000000000000000000000000000000011111111000000011111111000000000000000000000000000000000000000000000000000111111110000
SDR for 2020-01-08 19:00:00:000; 000000000000000000000000000000000000111111110000011111111000000000000000000000000000000000000000000000000000111111110000
SDR for 2020-01-08 20:00:00:000; 000000000000000000000000000000000000001111111100011111111000000000000000000000000000000000000000000000000000011111111000
SDR for 2020-01-08 21:00:00:000; 000000000000000000000000000000000000000011111111011111111000000000000000000000000000000000000000000000000000011111111000
SDR for 2020-01-08 22:00:00:000; 110000000000000000000000000000000000000000111111011111111000000000000000000000000000000000000000000000000000011111111000
SDR for 2020-01-08 23:00:00:000; 111100000000000000000000000000000000000000001111011111111000000000000000000000000000000000000000000000000000011111111000
SDR for 2020-01-09 00:00:00:000; 111111000000000000000000000000000000000000000011011111111000000000000000000000000000000000000000000000000000011111111000
~~~~

nl.zeesoft.zdk.test.impl.htm.TestDateTimeValuesEncoder
------------------------------------------------------
This test shows how to use a *DateTimeValueEncoder* to convert a range of dates/times and values into combined periodic sparse distributed representations.
The *DateTimeValueEncoder* is merely an example implementation of a *CombinedEncoder* used to test this library.
It uses random distributed scalar encoders to represent the values in order to show how these use state to maintain consistent representations.

**Example implementation**  
~~~~
// Create the encoder
DateTimeValueEncoder enc = new DateTimeEncoder();
// Obtain the SDR for a certain value
SDR sdr = enc.getSDRForValue(System.currentTimeMillis(),2,6);
~~~~

Class references;  
 * [TestDateTimeValuesEncoder](https://github.com/DyzLecticus/Zeesoft/blob/master/V4.0/ZDK/src/nl/zeesoft/zdk/test/impl/htm/TestDateTimeValuesEncoder.java)
 * [DateTimeValuesEncoder](https://github.com/DyzLecticus/Zeesoft/blob/master/V4.0/ZDK/src/nl/zeesoft/zdk/htm/enc/DateTimeValuesEncoder.java)
 * [DateTimeEncoder](https://github.com/DyzLecticus/Zeesoft/blob/master/V4.0/ZDK/src/nl/zeesoft/zdk/htm/enc/DateTimeEncoder.java)
 * [CombinedEncoder](https://github.com/DyzLecticus/Zeesoft/blob/master/V4.0/ZDK/src/nl/zeesoft/zdk/htm/enc/CombinedEncoder.java)
 * [SDR](https://github.com/DyzLecticus/Zeesoft/blob/master/V4.0/ZDK/src/nl/zeesoft/zdk/htm/util/SDR.java)

**Test output**  
The output of this test shows;  
 * How the generated SDRs represent several date/time and value combinations.
 * The StringBuilder representation of the encoder state.
~~~~
SDR for 2019-01-01 00:00:00:000, value1: 0, value2: 0; 11111100000000000000000000000000000000000000001111111110000000000000000000000000000000000000000110000000100010000101001000000000000110000010100000001000110001010000000000010000000000111111110000000000
SDR for 2019-02-02 01:00:00:000, value1: 1, value2: 2; 11111111000000000000000000000000000000000000000000001111111100000000000000000000000000000000000010000000100000100101001000000000000110000010100000001000110001000100000000010000111100000000000000001111
SDR for 2019-03-03 02:00:00:000, value1: 2, value2: 4; 00111111110000000000000000000000000000000000000000000000111111110000000000000000000000000000000010000000100000000101001000000000010110000010100000001000000001000100000100110000111111110000000000000000
SDR for 2019-04-04 03:00:00:000, value1: 3, value2: 6; 00111111110000000000000000000000000000000000000000000000000011111111000000000000000000000000000010100000100000000101001000000000010010000010000000001000001000000100000110110000000000000000011111111000
SDR for 2019-05-05 04:00:00:000, value1: 4, value2: 8; 00001111111100000000000000000000000000000000000000000000000000001111111100000000000000000000000010100000000000000101001001000000010010000010010000000000001000100000000110110000111111110000000000000000

Encoder StringBuilder:
VALUE1=0.0,0,17,19,35,36,22,8,12;1.0,0,17,19,35,36,22,8,14;2.0,0,17,33,19,35,36,22,8;3.0,0,17,33,2,19,36,22,8;4.0,0,17,33,2,19,36,22,25|VALUE2=0.0,16,17,2,35,4,21,23,12;1.0,16,17,2,35,4,21,37,12;2.0,16,17,2,35,4,21,25,12;3.0,17,2,35,4,21,25,12,31;4.0,2,34,35,4,21,25,12,31;5.0,2,34,18,35,21,25,12,31;6.0,32,2,34,18,35,25,12,31;7.0,32,2,34,18,35,22,12,31;8.0,32,2,34,18,35,5,22,31
~~~~

nl.zeesoft.zdk.test.impl.htm.TestPooler
---------------------------------------
This test shows how to use a *Pooler* to convert encoder out SDRs into consistently sparse representations.

**Example implementation**  
~~~~
// Create the configuration
PoolerConfig config = new PoolerConfig(200,1024,21);
// Create the pooler
Pooler pooler = new Pooler(config);
// Randomize the connections
pooler.randomizeConnections();
// Obtain the output SDR for a certain input SDR
SDR sdr = pooler.getSDRForInput(new SDR(),true);
~~~~

This test uses the *MockRegularSDRMap*.

Class references;  
 * [TestPooler](https://github.com/DyzLecticus/Zeesoft/blob/master/V4.0/ZDK/src/nl/zeesoft/zdk/test/impl/htm/TestPooler.java)
 * [SDR](https://github.com/DyzLecticus/Zeesoft/blob/master/V4.0/ZDK/src/nl/zeesoft/zdk/htm/util/SDR.java)
 * [PoolerConfig](https://github.com/DyzLecticus/Zeesoft/blob/master/V4.0/ZDK/src/nl/zeesoft/zdk/htm/proc/PoolerConfig.java)
 * [Pooler](https://github.com/DyzLecticus/Zeesoft/blob/master/V4.0/ZDK/src/nl/zeesoft/zdk/htm/proc/Pooler.java)

**Test output**  
The output of this test shows information about the pooler after passing the SDR test set through it.  
It asserts that learning increases the difference in overlap between the regular weekly recurring values and all the other pooler output SDRs.  
~~~~
Initializing pooler took: 20 ms
Randomizing connections took: 63 ms

Pooler input dimensions: 16x16, output dimensions: 32x32
- Average proximal inputs per column: 90
- Column groups: 144, average columns per group: 441

Processing input SDR map (learning: false) ...
Processing input SDR map took: 7954 ms

Performance statistics;
calculateOverlapScores:       1717.244 ms
selectActiveColumns:           902.740 ms
logActivity:                   818.682 ms
calculateColumnGroupActivity: 3015.087 ms
updateBoostFactors:           1258.038 ms
total:                        7771.785 ms
logSize:                         15330   
avgPerLog:                       0.507 ms

Combined average: 0.37384906, Combined weekly average: 6.4080915

Processing input SDR map (learning: true) ...
Processing input SDR map took: 11554 ms

Performance statistics;
calculateOverlapScores:        2369.927 ms
selectActiveColumns:            923.959 ms
learnActiveColumnsOnBits:      2583.568 ms
logActivity:                   1299.008 ms
calculateColumnGroupActivity:  2904.213 ms
updateBoostFactors:            1241.132 ms
total:                        11382.272 ms
logSize:                          15330   
avgPerLog:                        0.742 ms

Combined average: 0.33009952, Combined weekly average: 8.71098

Original ratio: 17.140852, learned ratio: 26.388952
~~~~

nl.zeesoft.zdk.test.impl.htm.TestMemory
---------------------------------------
This test shows how to use a *Memory* instance to learn temporal sequences of SDRs.
**Please note** that this implementation differs greatly from the Numenta HTM implementation because it does not model dendrites;  
Memory cells are directly connected to each other and dendrite activation is not limited.  
Further more, distal connections do not need to be randomly initialized when the memory is created.  

**Example implementation**  
~~~~
// Create the configuration
MemoryConfig config = new MemoryConfig(1024);
// Create the memory
Memory memory = new Memory(config);
// Obtain the output SDR for a certain input SDR
SDR sdr = memory.getSDRForInput(new SDR(),true);
~~~~

This test uses the *MockRegularSDRMap*.

Class references;  
 * [TestMemory](https://github.com/DyzLecticus/Zeesoft/blob/master/V4.0/ZDK/src/nl/zeesoft/zdk/test/impl/htm/TestMemory.java)
 * [SDR](https://github.com/DyzLecticus/Zeesoft/blob/master/V4.0/ZDK/src/nl/zeesoft/zdk/htm/util/SDR.java)
 * [MemoryConfig](https://github.com/DyzLecticus/Zeesoft/blob/master/V4.0/ZDK/src/nl/zeesoft/zdk/htm/proc/MemoryConfig.java)
 * [Memory](https://github.com/DyzLecticus/Zeesoft/blob/master/V4.0/ZDK/src/nl/zeesoft/zdk/htm/proc/Memory.java)

**Test output**  
The output of this test shows;  
 * How memory column bursting is reduced after leaning several sequences  
 * Information about the memory after passing the SDR test set through it  
~~~~
Memory dimensions: 32x32x4

Processing input SDR map (5000/15330) ...
Processed SDRs: 500, bursting average: 4 (max: 13)
Processed SDRs: 1000, bursting average: 2 (max: 9)
Processed SDRs: 1500, bursting average: 1 (max: 8)
Processed SDRs: 2000, bursting average: 1 (max: 4)
Processed SDRs: 2500, bursting average: 1 (max: 5)
Processed SDRs: 3000, bursting average: 1 (max: 5)
Processed SDRs: 3500, bursting average: 0 (max: 6)
Processed SDRs: 4000, bursting average: 0 (max: 4)
Processed SDRs: 4500, bursting average: 0 (max: 6)
Processed SDRs: 5000, bursting average: 0 (max: 4)
Processing input SDR map took: 13162 ms

Average distal inputs per memory cell: 57 (min: 21, max: 105)
Average connected distal inputs per memory cell: 54 (min: 12, max: 105)

Performance statistics;
cycleActiveState:       213.830 ms
activateColumnCells:    199.577 ms
calculateActivity:      842.702 ms
selectPredictiveCells:  389.478 ms
updatePredictions:     3813.763 ms
total:                 5471.296 ms
logSize:                   5000   
avgPerLog:                1.094 ms

Average distal inputs per memory cell: 57 (min: 21, max: 105)
Average connected distal inputs per memory cell: 54 (min: 12, max: 105)
~~~~

nl.zeesoft.zdk.test.impl.htm.TestStreamEncoder
----------------------------------------------
This test shows how to create and scale a *StreamEncoder*.
A *StreamEncoder* can be used to customize value to SDR translation for stream input.
By default it merely translates values into scalar SDRs.
it can be customized to include periodic date and/or time representations into the encoded SDRs.

**Example implementation**  
~~~~
// Create the encoder
StreamEncoder enc = new StreamEncoder();
// Customize the encoder scale
enc.setScale(2);
// Obtain the SDR for a certain value
SDR sdr = enc.getSDRForValue(dateTime,value);
~~~~

Class references;  
 * [TestStreamEncoder](https://github.com/DyzLecticus/Zeesoft/blob/master/V4.0/ZDK/src/nl/zeesoft/zdk/test/impl/htm/TestStreamEncoder.java)
 * [StreamEncoder](https://github.com/DyzLecticus/Zeesoft/blob/master/V4.0/ZDK/src/nl/zeesoft/zdk/htm/stream/StreamEncoder.java)
 * [SDR](https://github.com/DyzLecticus/Zeesoft/blob/master/V4.0/ZDK/src/nl/zeesoft/zdk/htm/util/SDR.java)

**Test output**  
The output of this test shows;  
 * How scaling changes the output size and bits of the SDRs the encoder will generate.
 * The JSON structure of the encoder.
~~~~
Default encoding length: 256, bits: 32
Scaled (factor 2) encoding length: 512, bits: 64
Scaled (factor 4) encoding length: 1024, bits: 128

Stream encoder JSON;
{
  "scale": 4,
  "includeMonth": false,
  "includeDayOfWeek": false,
  "includeHourOfDay": false,
  "includeMinute": false,
  "includeSecond": false,
  "includeValue": true,
  "valueMin": 0,
  "valueMax": 100,
  "valueResolution": 1.0,
  "valueDistributed": false
}
~~~~

nl.zeesoft.zdk.test.impl.htm.TestAnomalyDetector
------------------------------------------------
This test shows how to use an *AnomalyDetector* to detect anomalies in an SDR *Stream*.
It uses a *StreamFactory* to create a *PredictionStream* and then uses that to create an *AnomalyDetector*.
The *AnomalyDetectorListener* interface can be used to listen for anomaly detections.

**Example implementation**  
~~~~
// Create the stream factory
StreamFactory factory = new StreamFactory(1024,21);
// Create the stream
DefaultStream stream = factory.getNewDefaultStream(true);
// Create the anomaly detector
AnomalyDetector detector = stream.getNewAnomalyDetector();
// Attach a listener (implement the AnomalyDetectorListener interface)
detector.addListener(this);
// Start the stream
stream.start();
stream.waitForStart();
// Add some values to the stream (include an anomaly after 5000 inputs)
stream.addValue(1);
stream.addValue(2);
// Remember to stop and destroy the stream after use
stream.stop();
stream.waitForStop();
stream.destroy();
~~~~

This test uses the *MockAnomalySDRMap*.

Class references;  
 * [TestAnomalyDetector](https://github.com/DyzLecticus/Zeesoft/blob/master/V4.0/ZDK/src/nl/zeesoft/zdk/test/impl/htm/TestAnomalyDetector.java)
 * [StreamFactory](https://github.com/DyzLecticus/Zeesoft/blob/master/V4.0/ZDK/src/nl/zeesoft/zdk/htm/stream/StreamFactory.java)
 * [DefaultStream](https://github.com/DyzLecticus/Zeesoft/blob/master/V4.0/ZDK/src/nl/zeesoft/zdk/htm/stream/DefaultStream.java)
 * [AnomalyDetector](https://github.com/DyzLecticus/Zeesoft/blob/master/V4.0/ZDK/src/nl/zeesoft/zdk/htm/stream/AnomalyDetector.java)
 * [AnomalyDetectorListener](https://github.com/DyzLecticus/Zeesoft/blob/master/V4.0/ZDK/src/nl/zeesoft/zdk/htm/stream/AnomalyDetectorListener.java)

**Test output**  
The output of this test shows;  
 * Information about the stream factory  
 * A JSON export of the stream factory  
 * The average prediction accuracy  
 * The detected anomaly  
 * Information about the stream after passing the SDR test set through it  
~~~~
Test set anomaly detection is expected at: 7666

StreamEncoder length: 256, bits: 32
Pooler input dimensions: 16x16, output dimensions: 32x32
Memory dimensions: 32x32x4

Stream factory JSON;
{
  "encoder": [
    {
      "scale": 1,
      "includeMonth": false,
      "includeDayOfWeek": false,
      "includeHourOfDay": false,
      "includeMinute": false,
      "includeSecond": false,
      "includeValue": true,
      "valueMin": 0,
      "valueMax": 100,
      "valueResolution": 1.0,
      "valueDistributed": false
    }
  ],
  "outputLength": 1024,
  "outputBits": 21,
  "potentialProximalConnections": 0.75,
  "proximalRadius": 5,
  "proximalConnectionThreshold": 0.1,
  "proximalConnectionDecrement": 0.008,
  "proximalConnectionIncrement": 0.05,
  "boostStrength": 10,
  "boostInhibitionRadius": 10,
  "boostActivityLogSize": 100,
  "depth": 4,
  "depth": 4,
  "maxDistalConnectionsPerCell": 9999,
  "localDistalConnectedRadius": 64,
  "minAlmostActiveDistalConnections": 5,
  "distalConnectionThreshold": 0.2,
  "distalConnectionDecrement": 0.003,
  "distalConnectionIncrement": 0.1,
  "predictSteps": "",
  "classifyValueKey": "value",
  "classifyLabelKey": "label",
  "classifyMaxCount": 40
}

Started stream
Processed SDRs: 500, average accuracy: 0.432, latest: 0.857
Processed SDRs: 1000, average accuracy: 0.620, latest: 0.810
Processed SDRs: 1500, average accuracy: 0.842, latest: 0.905
Processed SDRs: 2000, average accuracy: 0.896, latest: 0.952
Processed SDRs: 2500, average accuracy: 0.927, latest: 1.000
Processed SDRs: 3000, average accuracy: 0.947, latest: 1.000
Processed SDRs: 3500, average accuracy: 0.958, latest: 0.952
Processed SDRs: 4000, average accuracy: 0.965, latest: 0.952
Processed SDRs: 4500, average accuracy: 0.970, latest: 0.810
Processed SDRs: 5000, average accuracy: 0.973, latest: 1.000
Processed SDRs: 5500, average accuracy: 0.974, latest: 0.952
Processed SDRs: 6000, average accuracy: 0.975, latest: 0.952
Processed SDRs: 6500, average accuracy: 0.978, latest: 1.000
Processed SDRs: 7000, average accuracy: 0.979, latest: 1.000
Processed SDRs: 7500, average accuracy: 0.979, latest: 0.952
Detected anomaly at: 7666, average accuracy: 0.97895443, latest: 0.23809522, difference: 0.6087338
Stopped stream after 18475 ms

DefaultStream;
total:      62581152.000 ms
logSize:            7667   
avgPerLog:      8162.404 ms

Pooler;
calculateOverlapScores:        5717.881 ms
selectActiveColumns:            966.641 ms
learnActiveColumnsOnBits:      2964.648 ms
logActivity:                   1316.716 ms
calculateColumnGroupActivity:  4379.111 ms
updateBoostFactors:            1342.985 ms
total:                        16754.600 ms
logSize:                          15330   
avgPerLog:                        1.093 ms

Memory;
cycleActiveState:        497.333 ms
activateColumnCells:     341.404 ms
calculateActivity:      1242.959 ms
selectPredictiveCells:   862.674 ms
updatePredictions:     14468.748 ms
total:                 17428.082 ms
logSize:                    7667   
avgPerLog:                 2.273 ms

Total processing time per SDR: 3.366 ms
Total stream time per SDR:     2.410 ms
~~~~

nl.zeesoft.zdk.test.impl.htm.TestValueClassifier
------------------------------------------------
This test shows how to use a *ClassificationStream* and a *ValueClassifier* to classify and/or predict values.
It uses a *StreamFactory* to create the *ClassificationStream* and then uses that to create an *ValueClassifer*.
The *ValueClassiferListener* interface can be used to listen for classifications and/or predictions.
The *ValueClassifer* creates a list of *Classification* objects for each input SDR and then passes that list to its listeners.

**Example implementation**  
~~~~
// Create the stream factory
StreamFactory factory = new StreamFactory(1024,21);
// Specify the number of steps to predict (use 0 to specify classification of the current input)
factory.getPredictSteps().add(1);
// Create the stream
ClassificationStream stream = factory.getNewClassificationStream(true);
// Create the classifier
ValueClassifier classifier = stream.getNewValueClassifier();
// Attach a listener (implement the ValueClassifierListener interface)
classifier.addListener(this);
// Start the stream
stream.start();
stream.waitForStart();
// Add some values to the stream
stream.addValue(1);
stream.addValue(2);
// Remember to stop and destroy the stream after use
stream.stop();
stream.waitForStop();
stream.destroy();
~~~~

This test uses the *MockRegularSDRMap*.

Class references;  
 * [TestValueClassifier](https://github.com/DyzLecticus/Zeesoft/blob/master/V4.0/ZDK/src/nl/zeesoft/zdk/test/impl/htm/TestValueClassifier.java)
 * [StreamFactory](https://github.com/DyzLecticus/Zeesoft/blob/master/V4.0/ZDK/src/nl/zeesoft/zdk/htm/stream/StreamFactory.java)
 * [ClassificationStream](https://github.com/DyzLecticus/Zeesoft/blob/master/V4.0/ZDK/src/nl/zeesoft/zdk/htm/stream/ClassificationStream.java)
 * [ValueClassifier](https://github.com/DyzLecticus/Zeesoft/blob/master/V4.0/ZDK/src/nl/zeesoft/zdk/htm/stream/ValueClassifier.java)
 * [ValueClassifierListener](https://github.com/DyzLecticus/Zeesoft/blob/master/V4.0/ZDK/src/nl/zeesoft/zdk/htm/stream/ValueClassifierListener.java)

**Test output**  
The output of this test shows;  
 * Information about the stream factory  
 * The average prediction accuracy  
 * Information about the stream after passing the SDR test set through it  
~~~~
StreamEncoder length: 256, bits: 32
Pooler input dimensions: 16x16, output dimensions: 32x32
Memory dimensions: 32x32x4
Classifier prediction steps: 1

Started stream
Processed SDRs: 500, accuracy: 0.279
Processed SDRs: 1000, accuracy: 0.507
Processed SDRs: 1500, accuracy: 0.746
Processed SDRs: 2000, accuracy: 0.809
Processed SDRs: 2500, accuracy: 0.886
Processed SDRs: 3000, accuracy: 0.913
Processed SDRs: 3500, accuracy: 0.922
Processed SDRs: 4000, accuracy: 0.929
Processed SDRs: 4500, accuracy: 0.925
Processed SDRs: 5000, accuracy: 0.917
Stopped stream after 10149 ms

ClassificationStream;
total:      23364110.000 ms
logSize:            5004   
avgPerLog:      4669.087 ms

Pooler;
calculateOverlapScores:        4836.172 ms
selectActiveColumns:            439.629 ms
learnActiveColumnsOnBits:      1665.798 ms
logActivity:                    516.332 ms
calculateColumnGroupActivity:  1950.464 ms
updateBoostFactors:             588.320 ms
total:                        10032.046 ms
logSize:                           6887   
avgPerLog:                        1.457 ms

Memory;
cycleActiveState:       298.252 ms
activateColumnCells:    202.019 ms
calculateActivity:      885.853 ms
selectPredictiveCells:  500.365 ms
updatePredictions:     6619.198 ms
total:                 8515.906 ms
logSize:                   5005   
avgPerLog:                1.701 ms

Classifier;
generateClassifications: 669.596 ms
total:                   676.251 ms
logSize:                    5004   
avgPerLog:                 0.135 ms

Total processing time per SDR: 3.293 ms
Total stream time per SDR:     2.028 ms
~~~~

Test results
------------
All 22 tests have been executed successfully (147 assertions).  
Total test duration: 88357 ms (total sleep duration: 45200 ms).  

Memory usage per test;  
 * nl.zeesoft.zdk.test.impl.TestZStringEncoder: 765 Kb / 0 Mb
 * nl.zeesoft.zdk.test.impl.TestZStringSymbolParser: 526 Kb / 0 Mb
 * nl.zeesoft.zdk.test.impl.TestCsv: 533 Kb / 0 Mb
 * nl.zeesoft.zdk.test.impl.TestJson: 547 Kb / 0 Mb
 * nl.zeesoft.zdk.test.impl.TestZHttpRequest: 553 Kb / 0 Mb
 * nl.zeesoft.zdk.test.impl.TestMessenger: 756 Kb / 0 Mb
 * nl.zeesoft.zdk.test.impl.TestZMatrix: 829 Kb / 0 Mb
 * nl.zeesoft.zdk.test.impl.TestGeneticCode: 790 Kb / 0 Mb
 * nl.zeesoft.zdk.test.impl.TestNeuralNet: 2422 Kb / 2 Mb
 * nl.zeesoft.zdk.test.impl.TestGeneticNN: 6778 Kb / 6 Mb
 * nl.zeesoft.zdk.test.impl.TestEvolver: 70069 Kb / 68 Mb
 * nl.zeesoft.zdk.test.impl.htm.TestSDR: 67246 Kb / 65 Mb
 * nl.zeesoft.zdk.test.impl.htm.TestSDRMap: 64369 Kb / 62 Mb
 * nl.zeesoft.zdk.test.impl.htm.TestScalarEncoder: 61363 Kb / 59 Mb
 * nl.zeesoft.zdk.test.impl.htm.TestRDScalarEncoder: 954 Kb / 0 Mb
 * nl.zeesoft.zdk.test.impl.htm.TestDateTimeEncoder: 960 Kb / 0 Mb
 * nl.zeesoft.zdk.test.impl.htm.TestDateTimeValuesEncoder: 956 Kb / 0 Mb
 * nl.zeesoft.zdk.test.impl.htm.TestPooler: 34929 Kb / 34 Mb
 * nl.zeesoft.zdk.test.impl.htm.TestMemory: 34936 Kb / 34 Mb
 * nl.zeesoft.zdk.test.impl.htm.TestStreamEncoder: 34939 Kb / 34 Mb
 * nl.zeesoft.zdk.test.impl.htm.TestAnomalyDetector: 83666 Kb / 81 Mb
 * nl.zeesoft.zdk.test.impl.htm.TestValueClassifier: 93089 Kb / 90 Mb
