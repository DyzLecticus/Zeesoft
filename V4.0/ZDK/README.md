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
Key: 8096828812199766241002899163824435515489923816099315288529077846
Input text: Hello, my name is 'Dyz Lecticus'. How are you feeling today? :-) (Don't you know how to: [re;spond]!).

Key encoded text: vhtchj5gfi4crh6hWclcodkjmjAhJf:gLdNdybebfcge#hWiKiVcQgHe2hPdlf7d~dSeef4cdgqe#izjNivcWe0hcdlfvcmjRifeWcUfXdthwiVfVdBiAc8gFg#hdbPaRgxbDi8geiYcHiTgWczdvdkikjJhXgMgwcUeCc1bRaaeLiEiGhNcOgDeqhNdifdf:eVfzfQb1eBe0
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
2019-12-30 11:02:49:993 DBG nl.zeesoft.zdk.test.impl.TestMessengerListener: Test log debug message before Messenger has started
2019-12-30 11:02:50:305 ERR nl.zeesoft.zdk.test.impl.TestMessengerListener: Test log error message while Messenger is working
2019-12-30 11:02:50:305 ERR nl.zeesoft.zdk.test.impl.TestMessengerListener: Test log exception stack trace
java.lang.NumberFormatException: For input string: "A"
	at java.lang.NumberFormatException.forInputString(NumberFormatException.java:65)
	at java.lang.Integer.parseInt(Integer.java:580)
	at java.lang.Integer.parseInt(Integer.java:615)
	at nl.zeesoft.zdk.test.impl.TestMessenger.test(TestMessenger.java:83)
	at nl.zeesoft.zdk.test.Tester.test(Tester.java:71)
	at nl.zeesoft.zdk.test.LibraryObject.describeAndTest(LibraryObject.java:39)
	at nl.zeesoft.zdk.test.impl.ZDK.main(ZDK.java:52)

2019-12-30 11:02:50:633 WRN nl.zeesoft.zdk.test.impl.TestMessengerListener: Test log warning message after Messenger has stopped
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
-01.00 | -00.29 | -00.99
-------+--------+-------
000.27 | -00.11 | 000.49

Randomized multiplied element wise;
-29.89 | -08.70 | -29.60
-------+--------+-------
008.23 | -03.18 | 014.79

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
000.60 | -00.99 | -00.66
-------+--------+-------
-00.18 | -00.59 | -00.07

Randomized matrix transposed;
000.60 | -00.18
-------+-------
-00.99 | -00.59
-------+-------
-00.66 | -00.07
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
Genetic code: 5642475629888796945297547904516763175767513044837139118617832384157725893997663002884654750164007143
Mutated code: 5642475629888296945297527904516763175767513044837139118617632384157725893997661002884654750164007149
                           ^         ^                                  ^                   ^                    ^

Scaled property values;
0: 589
1: 167
2: 577
3: 475
4: 7 <
5: 846
6: 841
7: 164
8: 501 <
9: 861
10: 100 <
11: 713
12: 661 <
13: 7
14: 483
15: 725
16: 16
17: 829 <
18: 562
19: 714 <
20: 837
21: 893
22: 756
23: 846
24: 399
25: 882 <
26: 304 <
27: 617
28: 884
29: 288
30: 576
31: 610 <
32: 475
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
  Input: [1.00|1.00], output: [0.20], expectation: [0.00], error: -0.20, loss: 0.19510187
  Input: [0.00|1.00], output: [0.07], expectation: [1.00], error: 0.93, loss: 0.9304427
  Input: [1.00|0.00], output: [0.10], expectation: [1.00], error: 0.90, loss: 0.90038157
  Average error: 0.51, average loss: 0.51, success: false
Latest test results;
  Input: [1.00|0.00], output: [0.90], expectation: [1.00], error: 0.10, loss: 0.095947504
  Input: [1.00|1.00], output: [0.00], expectation: [0.00], error: -0.00, loss: 0.002313137
  Input: [0.00|1.00], output: [0.92], expectation: [1.00], error: 0.08, loss: 0.083055794
  Input: [0.00|0.00], output: [0.20], expectation: [0.00], error: -0.20, loss: 0.19702782
  Average error: 0.09, average loss: 0.09, success: false
Trained epochs: 10000, total average error: 3109.7378, total average loss: 3109.7378
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
  Input: [1.00|1.00], output: [0.00], expectation: [0.00], error: 0.00, loss: 0.0
  Input: [0.00|1.00], output: [0.00], expectation: [1.00], error: 1.00, loss: 1.0
  Input: [1.00|0.00], output: [1.00], expectation: [1.00], error: 0.00, loss: 0.0
  Average error: 0.25, average loss: 0.25, success: false
Trained epochs: 10000, total average error: 5899.5, total average loss: 5899.5

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
    "2,1,1.0,0.0",
    "2,1,-0.005777098,0.13930686",
    "1,1,1.0"
  ],
  "weights": [
    "1,1,0.0",
    "2,2,-0.022432089,0.53136086,-0.07232627,-0.048318043",
    "1,2,-0.5804305,-0.790617"
  ],
  "biases": [
    "1,1,0.0",
    "2,1,-0.5552777,0.21163313",
    "1,1,0.19478357"
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
Neural net activator: nl.zeesoft.zdk.functions.ZReLU, output activator: nl.zeesoft.zdk.functions.ZLeakyReLU, learning rate: 0.187
Initial test results;
  Input: [0.00|0.00], output: [0.37], expectation: [0.00], error: -0.37, loss: 0.3730788
  Input: [1.00|1.00], output: [-0.00], expectation: [0.00], error: 0.00, loss: 0.001819052
  Input: [0.00|1.00], output: [-0.00], expectation: [1.00], error: 1.00, loss: 1.0005567
  Input: [1.00|0.00], output: [0.25], expectation: [1.00], error: 0.75, loss: 0.7518332
  Average error: 0.53, average loss: 0.53, success: false

00100 ----------------------/ 94%
00200 ----------------------\ 95%
00300 ----------------------/ 94%
00400 ----------------------| 94%
00500 ------------------------\ 100%
00600 ------------------------\ 101%
00700 ------------------------\ 102%
00800 ------------------------\ 103%
00900 ------------------------/ 102%
01000 ------------------------| 102%
01100 ------------------------\ 103%
01200 ------------------------/ 100%
01300 ------------------------| 100%
01400 ------------------------\ 103%
01500 ------------------------/ 101%
01600 ------------------------\ 102%
01700 ------------------------/ 100%
01800 ------------------------\ 102%
01900 ------------------------| 102%
02000 ------------------------\ 103%
02100 -----------------------/ 99%
02200 ------------------------\ 102%
02300 ------------------------| 102%
02400 ------------------------| 102%
02500 ------------------------| 102%
02600 ------------------------| 102%
02700 ------------------------| 102%
02800 -----------------------/ 99%
02900 ------------------------\ 103%
03000 ------------------------/ 102%
03100 ------------------------/ 100%
03200 ------------------------\ 103%
03300 ------------------------/ 102%
03400 ------------------------| 102%
03500 ------------------------| 102%
03600 -----------------------/ 99%
03700 ------------------------\ 102%
03800 ------------------------/ 100%
03900 ------------------------\ 103%
04000 ------------------------/ 102%
04100 ------------------------| 102%
04200 ------------------------\ 103%
04300 ------------------------/ 101%
04400 ------------------------\ 103%
04500 ------------------------/ 101%
04600 -----------------------/ 99%
04700 ------------------------\ 102%
04800 ------------------------\ 103%
04900 ------------------------/ 102%
05000 ------------------------/ 101%
05100 ------------------------\ 102%
05200 ------------------------/ 101%
05300 ------------------------/ 100%
05400 -----------------------/ 99%
05500 ------------------------\ 103%
05600 ------------------------/ 100%
05700 ------------------------\ 103%
05800 ------------------------/ 102%
05900 ------------------------\ 103%
06000 ------------------------| 103%
06100 ------------------------| 103%
06200 ------------------------/ 102%
06300 ------------------------| 102%
06400 ------------------------| 102%
06500 ------------------------/ 101%
06600 ------------------------| 101%
06700 -----------------------/ 99%
06800 ------------------------\ 102%
06900 ------------------------\ 103%
07000 ------------------------/ 100%
07100 ------------------------\ 101%
07200 ------------------------\ 102%
07300 ------------------------\ 103%
07400 ------------------------/ 102%
07500 ------------------------/ 100%
07600 ------------------------\ 101%
07700 ------------------------\ 102%
07800 -----------------------/ 99%
07900 ------------------------\ 102%
08000 ------------------------\ 103%
08100 ------------------------/ 102%
08200 ------------------------| 102%
08300 ------------------------| 102%
08400 -----------------------/ 97%
08500 ------------------------\ 102%
08600 ------------------------| 102%
08700 -----------------------/ 97%
08800 ------------------------\ 102%
08900 -----------------------/ 99%
09000 ------------------------\ 102%
09100 ------------------------/ 100%
09200 ------------------------\ 102%
09300 ------------------------\ 103%
09400 ------------------------/ 102%
09500 -----------------------/ 99%
09600 ------------------------\ 102%
09700 ------------------------/ 100%
09800 -----------------------/ 99%
09900 ------------------------\ 102%
10000 ------------------------/ 101%

Latest test results;
  Input: [1.00|1.00], output: [0.58], expectation: [0.00], error: -0.58, loss: 0.576868
  Input: [0.00|0.00], output: [0.47], expectation: [0.00], error: -0.47, loss: 0.46899366
  Input: [0.00|1.00], output: [0.38], expectation: [1.00], error: 0.62, loss: 0.61870813
  Input: [1.00|0.00], output: [0.50], expectation: [1.00], error: 0.50, loss: 0.50300974
  Average error: 0.54, average loss: 0.54, success: false
Trained epochs: 10000, total average error: 5415.362, total average loss: 5415.362
================================================================================
Neural net activator: nl.zeesoft.zdk.functions.ZLeakyReLU, output activator: nl.zeesoft.zdk.functions.ZReLU, learning rate: 0.194
Initial test results;
  Input: [0.00|0.00], output: [0.97], expectation: [0.00], error: -0.97, loss: 0.97025657
  Input: [1.00|1.00], output: [0.91], expectation: [0.00], error: -0.91, loss: 0.9122372
  Input: [0.00|1.00], output: [1.06], expectation: [1.00], error: -0.06, loss: 0.06350398
  Input: [1.00|0.00], output: [0.44], expectation: [1.00], error: 0.56, loss: 0.55810523
  Average error: 0.63, average loss: 0.63, success: false

00100 ----------/ 44%
00200 ----------\ 47%
00300 ----------/ 45%
00400 ----------/ 44%
00500 ----------| 44%
00600 ----------\ 47%
00700 ----------| 47%
00800 ----------| 47%
00900 ----------| 47%
01000 ----------| 47%
01100 -----------\ 48%
01200 ----------/ 46%
01300 -----------\ 48%
01400 ----------/ 46%
01500 ----------/ 45%
01600 ----------| 45%
01700 ----------/ 44%
01800 ----------\ 45%
01900 ----------| 45%
02000 -----------\ 48%
02100 ----------/ 45%
02200 ----------/ 44%
02300 ----------\ 45%
02400 -----------\ 48%
02500 ----------/ 45%
02600 ----------\ 46%
02700 ----------| 46%
02800 ----------/ 45%
02900 -----------\ 48%
03000 -----------| 48%
03100 ----------/ 46%
03200 -----------\ 48%
03300 -----------| 48%
03400 ----------/ 44%
03500 ----------\ 47%
03600 -----------\ 48%
03700 -----------\ 49%
03800 ----------/ 45%
03900 ---------/ 43%
04000 ----------\ 46%
04100 -----------\ 49%
04200 ----------/ 47%
04300 -----------\ 48%
04400 -----------\ 50%
04500 -----------/ 48%
04600 ----------/ 45%
04700 ----------\ 47%
04800 ----------/ 45%
04900 ----------| 45%
05000 ---------/ 43%
05100 ----------\ 47%
05200 ----------| 47%
05300 -----------\ 49%
05400 -----------| 49%
05500 -----------| 49%
05600 ----------/ 44%
05700 ----------\ 46%
05800 ----------\ 47%
05900 -----------\ 48%
06000 -----------| 48%
06100 -----------\ 50%
06200 -----------/ 49%
06300 ----------/ 44%
06400 ------------\ 53%
06500 ----------/ 44%
06600 ----------\ 46%
06700 --------------\ 61%
06800 -----------/ 51%
06900 ----------/ 47%
07000 -----------\ 50%
07100 ----------/ 45%
07200 ----------| 45%
07300 -----------\ 50%
07400 ----------/ 47%
07500 -----------\ 48%
07600 -----------| 48%
07700 ----------/ 44%
07800 ------------\ 54%
07900 ------------/ 52%
08000 -----------------\ 72%
08100 ----------/ 47%
08200 ----------/ 46%
08300 -----------\ 48%
08400 ------------\ 52%
08500 ----------/ 44%
08600 ----------\ 47%
08700 ----------| 47%
08800 ----------/ 45%
08900 -----------\ 48%
09000 ----------/ 45%
09100 -----------\ 49%
09200 -----------/ 48%
09300 ----------/ 47%
09400 ----------/ 45%
09500 --------------\ 60%
09600 ----------/ 47%
09700 ----------| 47%
09800 -----------\ 48%
09900 -----------\ 49%
10000 -------------------\ 83%

Latest test results;
  Input: [0.00|0.00], output: [0.46], expectation: [0.00], error: -0.46, loss: 0.46224317
  Input: [0.00|1.00], output: [0.00], expectation: [1.00], error: 1.00, loss: 1.0
  Input: [1.00|0.00], output: [0.37], expectation: [1.00], error: 0.63, loss: 0.62960607
  Input: [1.00|1.00], output: [0.00], expectation: [0.00], error: 0.00, loss: 0.0
  Average error: 0.52, average loss: 0.52, success: false
Trained epochs: 10000, total average error: 2998.3262, total average loss: 2998.3262
================================================================================
Neural net activator: nl.zeesoft.zdk.functions.ZTanH, output activator: nl.zeesoft.zdk.functions.ZSigmoid, learning rate: 0.1832
Initial test results;
  Input: [0.00|0.00], output: [0.57], expectation: [0.00], error: -0.57, loss: 0.5722763
  Input: [1.00|1.00], output: [0.66], expectation: [0.00], error: -0.66, loss: 0.65745324
  Input: [0.00|1.00], output: [0.57], expectation: [1.00], error: 0.43, loss: 0.42774928
  Input: [1.00|0.00], output: [0.69], expectation: [1.00], error: 0.31, loss: 0.31050783
  Average error: 0.49, average loss: 0.49, success: false

00100 -----------/ 51%
00200 ------/ 31%
00300 -----/ 24%
00400 ---/ 19%

Latest test results;
  Input: [1.00|0.00], output: [0.90], expectation: [1.00], error: 0.10, loss: 0.099788845
  Input: [0.00|1.00], output: [0.90], expectation: [1.00], error: 0.10, loss: 0.09930712
  Input: [0.00|0.00], output: [0.09], expectation: [0.00], error: -0.09, loss: 0.086732805
  Input: [1.00|1.00], output: [0.09], expectation: [0.00], error: -0.09, loss: 0.088718854
  Average error: 0.09, average loss: 0.09, success: true
Trained epochs: 469, total average error: 86.24257, total average loss: 86.24257
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
2019-12-30 11:02:51:649 DBG nl.zeesoft.zdk.genetic.Evolver: Started
2019-12-30 11:02:51:743 DBG nl.zeesoft.zdk.genetic.Evolver: Selected new best genetic neural net;
- Code: 4407864835724724
- Size: 14
- Initial average loss: 0.75000 (final: 0.00000)
- Total average loss: 31.50000 (epochs: 51)
- Training result: 23.62500
2019-12-30 11:02:51:759 DBG nl.zeesoft.zdk.genetic.Evolver: Selected new best genetic neural net;
- Code: 6784921670378896
- Size: 14
- Initial average loss: 0.43658 (final: 0.07447)
- Total average loss: 16.37382 (epochs: 61)
- Training result: 7.14856
2019-12-30 11:02:52:165 DBG nl.zeesoft.zdk.genetic.Evolver: Selected new best genetic neural net;
- Code: 6784921670378896
- Size: 14
- Initial average loss: 0.42502 (final: 0.06292)
- Total average loss: 10.95426 (epochs: 41)
- Training result: 4.65575
2019-12-30 11:02:52:243 DBG nl.zeesoft.zdk.genetic.Evolver: Selected new best genetic neural net;
- Code: 6784921670378896
- Size: 14
- Initial average loss: 0.42127 (final: 0.07390)
- Total average loss: 8.69794 (epochs: 35)
- Training result: 3.66418
2019-12-30 11:02:52:790 DBG nl.zeesoft.zdk.genetic.Evolver: Selected new best genetic neural net;
- Code: 6784921670378896
- Size: 14
- Initial average loss: 0.45426 (final: 0.05910)
- Total average loss: 6.82626 (epochs: 28)
- Training result: 3.10092
2019-12-30 11:02:55:853 DBG nl.zeesoft.zdk.genetic.Evolver: Selected new best genetic neural net;
- Code: 6784921670378896
- Size: 14
- Initial average loss: 0.43942 (final: 0.06894)
- Total average loss: 6.08306 (epochs: 25)
- Training result: 2.67305
2019-12-30 11:02:56:087 DBG nl.zeesoft.zdk.genetic.Evolver: Selected new best genetic neural net;
- Code: 6784921770378896
- Size: 14
- Initial average loss: 0.38389 (final: 0.05911)
- Total average loss: 3.00958 (epochs: 14)
- Training result: 1.15534
2019-12-30 11:02:56:134 DBG nl.zeesoft.zdk.genetic.Evolver: Selected new best genetic neural net;
- Code: 6754921770378896
- Size: 14
- Initial average loss: 0.22452 (final: 0.03906)
- Total average loss: 2.03649 (epochs: 14)
- Training result: 0.45724
2019-12-30 11:02:56:181 DBG nl.zeesoft.zdk.genetic.Evolver: Selected new best genetic neural net;
- Code: 6754921770378896
- Size: 14
- Initial average loss: 0.22452 (final: 0.03523)
- Total average loss: 1.78502 (epochs: 13)
- Training result: 0.40077
2019-12-30 11:02:56:196 DBG nl.zeesoft.zdk.genetic.Evolver: Selected new best genetic neural net;
- Code: 6754921770378894
- Size: 14
- Initial average loss: 0.13006 (final: 0.06987)
- Total average loss: 0.48043 (epochs: 4)
- Training result: 0.06248
2019-12-30 11:02:56:728 DBG nl.zeesoft.zdk.genetic.Evolver: Selected new best genetic neural net;
- Code: 6754921070378894
- Size: 14
- Initial average loss: 0.10146 (final: 0.04869)
- Total average loss: 0.48160 (epochs: 6)
- Training result: 0.04887
2019-12-30 11:02:56:884 DBG nl.zeesoft.zdk.genetic.Evolver: Selected new best genetic neural net;
- Code: 6754921070378894
- Size: 14
- Initial average loss: 0.05787 (final: 0.04639)
- Total average loss: 0.11291 (epochs: 2)
- Training result: 0.00653
2019-12-30 11:02:57:118 DBG nl.zeesoft.zdk.genetic.Evolver: Selected new best genetic neural net;
- Code: 6754921070378896
- Size: 14
- Initial average loss: 0.04791 (final: 0.03811)
- Total average loss: 0.09276 (epochs: 2)
- Training result: 0.00444
2019-12-30 11:02:57:681 DBG nl.zeesoft.zdk.genetic.Evolver: Selected new best genetic neural net;
- Code: 6752921070378896
- Size: 14
- Initial average loss: 0.04593 (final: 0.04593)
- Total average loss: 0.04593 (epochs: 0)
- Training result: 0.00211

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
    "2019-12-30 11:02:51:743 SEL code: 4407864835724724, size: 14, initial loss: 0.75000 (final: 0.00000), total loss: 31.50000, result: 23.62500 (epochs: 51)",
    "2019-12-30 11:02:51:759 SEL code: 6784921670378896, size: 14, initial loss: 0.43658 (final: 0.07447), total loss: 16.37382, result: 7.14856 (epochs: 61)",
    "2019-12-30 11:02:52:165 SEL code: 6784921670378896, size: 14, initial loss: 0.42502 (final: 0.06292), total loss: 10.95426, result: 4.65575 (epochs: 41)",
    "2019-12-30 11:02:52:243 SEL code: 6784921670378896, size: 14, initial loss: 0.42127 (final: 0.07390), total loss: 8.69794, result: 3.66418 (epochs: 35)",
    "2019-12-30 11:02:52:790 SEL code: 6784921670378896, size: 14, initial loss: 0.45426 (final: 0.05910), total loss: 6.82626, result: 3.10092 (epochs: 28)",
    "2019-12-30 11:02:55:853 SEL code: 6784921670378896, size: 14, initial loss: 0.43942 (final: 0.06894), total loss: 6.08306, result: 2.67305 (epochs: 25)",
    "2019-12-30 11:02:56:087 SEL code: 6784921770378896, size: 14, initial loss: 0.38389 (final: 0.05911), total loss: 3.00958, result: 1.15534 (epochs: 14)",
    "2019-12-30 11:02:56:134 SEL code: 6754921770378896, size: 14, initial loss: 0.22452 (final: 0.03906), total loss: 2.03649, result: 0.45724 (epochs: 14)",
    "2019-12-30 11:02:56:181 SEL code: 6754921770378896, size: 14, initial loss: 0.22452 (final: 0.03523), total loss: 1.78502, result: 0.40077 (epochs: 13)",
    "2019-12-30 11:02:56:196 SEL code: 6754921770378894, size: 14, initial loss: 0.13006 (final: 0.06987), total loss: 0.48043, result: 0.06248 (epochs: 4)",
    "2019-12-30 11:02:56:728 SEL code: 6754921070378894, size: 14, initial loss: 0.10146 (final: 0.04869), total loss: 0.48160, result: 0.04887 (epochs: 6)",
    "2019-12-30 11:02:56:884 SEL code: 6754921070378894, size: 14, initial loss: 0.05787 (final: 0.04639), total loss: 0.11291, result: 0.00653 (epochs: 2)",
    "2019-12-30 11:02:57:118 SEL code: 6754921070378896, size: 14, initial loss: 0.04791 (final: 0.03811), total loss: 0.09276, result: 0.00444 (epochs: 2)",
    "2019-12-30 11:02:57:681 SEL code: 6752921070378896, size: 14, initial loss: 0.04593 (final: 0.04593), total loss: 0.04593, result: 0.00211 (epochs: 0)"
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
          "code": "ZK7EgBbAiNzJuEFPMIbOoFkNAJ3BvO3LpDgJ2NCH#K7OnM7G#AaEuLBNRNQAYGlJJFoK2",
          "neuralNet": [
            {
              "inputNeurons": 2,
              "hiddenLayers": 1,
              "hiddenNeurons": 2,
              "outputNeurons": 1,
              "weightFunction": "nl.zeesoft.zdk.functions.ZWeightXavier",
              "biasFunction": "nl.zeesoft.zdk.functions.ZWeightZero",
              "activator": "nl.zeesoft.zdk.functions.ZReLU",
              "outputActivator": "nl.zeesoft.zdk.functions.ZLeakyReLU",
              "learningRate": 0.1354,
              "values": [
                "2,1,0.0,0.0",
                "2,1,0.0,0.0",
                "1,1,0.0"
              ],
              "weights": [
                "1,1,0.0",
                "2,2,0.85225636,-0.8128203,-0.8610199,0.81501114",
                "1,2,1.0949221,1.1365633"
              ],
              "biases": [
                "1,1,0.0",
                "2,1,0.0,0.0",
                "1,1,0.0"
              ]
            }
          ]
        }
      ],
      "trainingProgram": [
        {
          "stopOnSuccess": true,
          "trainedEpochs": 0,
          "totalAverageError": 0.045928344,
          "totalAverageLoss": 0.045928344,
          "initialResults": [
            {
              "inputNeurons": 2,
              "outputNeurons": 1,
              "lossFunction": "nl.zeesoft.zdk.functions.ZMeanAbsoluteError",
              "errorTolerance": 0.1,
              "averageError": 0.045928344,
              "averageLoss": 0.045928344,
              "success": true,
              "tests": [
                {
                  "inputs": "1.0,1.0",
                  "outputs": "0.043179393",
                  "expectations": "0.0",
                  "errors": "-0.043179393"
                },
                {
                  "inputs": "0.0,1.0",
                  "outputs": "0.92631173",
                  "expectations": "1.0",
                  "errors": "0.07368827"
                },
                {
                  "inputs": "1.0,0.0",
                  "outputs": "0.9331543",
                  "expectations": "1.0",
                  "errors": "0.066845715"
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
              "averageError": 0.045928344,
              "averageLoss": 0.045928344,
              "success": true,
              "tests": [
                {
                  "inputs": "1.0,1.0",
                  "outputs": "0.043179393",
                  "expectations": "0.0",
                  "errors": "-0.043179393"
                },
                {
                  "inputs": "0.0,1.0",
                  "outputs": "0.92631173",
                  "expectations": "1.0",
                  "errors": "0.07368827"
                },
                {
                  "inputs": "1.0,0.0",
                  "outputs": "0.9331543",
                  "expectations": "1.0",
                  "errors": "0.066845715"
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
SDR map: 100,2|90,30|2,11
Number of SDR A matches in SDR map: 1
SDR C: 100,81,88
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
ScalarEncoder length: 52, bits: 2, resolution: 1.0, min: 0.0, max: 50.0, periodic: false
SDR for value 0:  1100000000000000000000000000000000000000000000000000
SDR for value 1:  0110000000000000000000000000000000000000000000000000
SDR for value 24: 0000000000000000000000001100000000000000000000000000
SDR for value 25: 0000000000000000000000000110000000000000000000000000
SDR for value 26: 0000000000000000000000000011000000000000000000000000
SDR for value 49: 0000000000000000000000000000000000000000000000000110
SDR for value 50: 0000000000000000000000000000000000000000000000000011
SDR for value 51: 0000000000000000000000000000000000000000000000000011

ScalarEncoder length: 50, bits: 2, resolution: 1.0, min: 0.0, max: 50.0, periodic: true
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
RDScalarEncoder length: 50, bits: 4, resolution: 1.0, capacity: 230300
SDR for value 0:  00100000000000000000100000000000000000100001000000
SDR for value 1:  00110000000000000000100000000000000000000001000000
SDR for value 24: 00000000000000000000000000000100100000000000100100
SDR for value 25: 00001000000000000000000000000100100000000000000100
SDR for value 75: 00000000000100000000000010000100000000010000000000
SDR for value 76: 00000000100000000000000010000100000000010000000000
SDR for value -1: 00100000010000000000100000000000000000000001000000
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
SDR for 2019-01-01 00:00:00:000, value1: 0, value2: 0; 11111100000000000000000000000000000000000000001111111110000000000000000000000000000000000000000101100101000000010000011000000000010000000000000110100011001001001000000000000000000000111111110000000000
SDR for 2019-02-02 01:00:00:000, value1: 1, value2: 2; 11111111000000000000000000000000000000000000000000001111111100000000000000000000000000000000000001100101000000010000010000000100010000000000000100110011001000001000010000000000111100000000000000001111
SDR for 2019-03-03 02:00:00:000, value1: 2, value2: 4; 00111111110000000000000000000000000000000000000000000000111111110000000000000000000000000000000001100100000010010000010000000100010000000000000100100001001000001010010000000010111111110000000000000000
SDR for 2019-04-04 03:00:00:000, value1: 3, value2: 6; 00111111110000000000000000000000000000000000000000000000000011111111000000000000000000000000000001101000000010010000010000000100010000000000000100100001001000001101010000000000000000000000011111111000
SDR for 2019-05-05 04:00:00:000, value1: 4, value2: 8; 00001111111100000000000000000000000000000000000000000000000000001111111100000000000000000000000001101000100010010000010000000100000000000000000001100001101000001101000000000000111111110000000000000000

Encoder StringBuilder:
VALUE1=0.0,1,33,2,5,21,22,7,15;1.0,1,33,2,5,21,7,29,15;2.0,1,33,2,5,21,12,29,15;3.0,1,33,2,4,21,12,29,15;4.0,1,2,4,21,8,12,29,15|VALUE2=0.0,18,21,7,24,8,10,14,15;1.0,18,7,24,8,10,29,14,15;2.0,18,7,24,10,11,29,14,15;3.0,18,7,24,10,26,11,29,15;4.0,18,38,7,24,10,26,29,15;5.0,18,7,24,25,10,26,29,15;6.0,18,7,24,25,10,27,29,15;7.0,18,7,24,25,9,10,27,15;8.0,16,18,24,25,9,10,27,15
~~~~

nl.zeesoft.zdk.test.impl.htm.TestDateTimeValueEncoder
-----------------------------------------------------
This test shows how to create and scale a *DateTimeValueEncoder*.
A *DateTimeValueEncoder* can be used to customize value to SDR translation for date and time related values.
By default it merely translates values into scalar SDRs.
it can be customized to include periodic date and/or time representations into the encoded SDRs.

**Example implementation**  
~~~~
// Create the encoder
DateTimeValueEncoder enc = new DateTimeValueEncoder();
// Customize the encoder scale
enc.setScale(2);
// Obtain the SDR for a certain value
SDR sdr = enc.getSDRForValue(dateTime,value);
~~~~

Class references;  
 * [TestDateTimeValueEncoder](https://github.com/DyzLecticus/Zeesoft/blob/master/V4.0/ZDK/src/nl/zeesoft/zdk/test/impl/htm/TestDateTimeValueEncoder.java)
 * [DateTimeValueEncoder](https://github.com/DyzLecticus/Zeesoft/blob/master/V4.0/ZDK/src/nl/zeesoft/zdk/htm/enc/DateTimeValueEncoder.java)
 * [SDR](https://github.com/DyzLecticus/Zeesoft/blob/master/V4.0/ZDK/src/nl/zeesoft/zdk/htm/util/SDR.java)

**Test output**  
The output of this test shows;  
 * How scaling changes the output length and bits of the SDRs the encoder will generate.
 * The JSON structure of the encoder.
~~~~
DateTimeValueEncoder length: 256, bits: 32
- VALUE ScalarEncoder length: 256, bits: 32, resolution: 1.0, min: 0.0, max: 100.0, periodic: false

DateTimeValueEncoder length: 512, bits: 64
- VALUE ScalarEncoder length: 512, bits: 64, resolution: 1.0, min: 0.0, max: 100.0, periodic: false

DateTimeValueEncoder length: 1024, bits: 128
- VALUE ScalarEncoder length: 1024, bits: 128, resolution: 1.0, min: 0.0, max: 100.0, periodic: false

Encoder JSON;
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

nl.zeesoft.zdk.test.impl.htm.TestGridEncoder
--------------------------------------------
This test shows how to use a *GridEncoder* to convert a range of multidimensional positions into sparse distributed representations.
The *GridEncoder* class provides static helper methods to create different type of 2D or 3D encoders.

**Example implementation**  
~~~~
// Create the encoder
GridEncoder enc = GridEncoder.getNew2DGridEncoder(length,bits,sizeX,sizeY);
// Obtain the SDR for a certain position
SDR sdr = enc.getSDRForPosition(0,0);
~~~~

Class references;  
 * [TestGridEncoder](https://github.com/DyzLecticus/Zeesoft/blob/master/V4.0/ZDK/src/nl/zeesoft/zdk/test/impl/htm/TestGridEncoder.java)
 * [GridEncoder](https://github.com/DyzLecticus/Zeesoft/blob/master/V4.0/ZDK/src/nl/zeesoft/zdk/htm/enc/GridEncoder.java)
 * [SDR](https://github.com/DyzLecticus/Zeesoft/blob/master/V4.0/ZDK/src/nl/zeesoft/zdk/htm/util/SDR.java)

**Test output**  
The output of this test shows different grid encoders and the SDRs they generate for several positions.
~~~~
GridEncoder length: 128, bits: 16
- D01 GridDimensionEncoder length: 48, bits: 6, resolution: 1.0, capacity: 1664
  - 001 ScalarEncoder length: 16, bits: 2, resolution: 1.0, min: 0.0, max: 16.0, periodic: true
  - 002 ScalarEncoder length: 16, bits: 2, resolution: 14.0, min: 0.0, max: 224.0, periodic: true
  - 003 ScalarEncoder length: 16, bits: 2, resolution: 52.0, min: 0.0, max: 832.0, periodic: true
- D02 GridDimensionEncoder length: 80, bits: 10, resolution: 1.0, capacity: 14336
  - 001 ScalarEncoder length: 16, bits: 2, resolution: 1.0, min: 0.0, max: 16.0, periodic: true
  - 002 ScalarEncoder length: 16, bits: 2, resolution: 14.0, min: 0.0, max: 224.0, periodic: true
  - 003 ScalarEncoder length: 16, bits: 2, resolution: 52.0, min: 0.0, max: 832.0, periodic: true
  - 004 ScalarEncoder length: 16, bits: 2, resolution: 135.0, min: 0.0, max: 2160.0, periodic: true
  - 005 ScalarEncoder length: 16, bits: 2, resolution: 224.0, min: 0.0, max: 3584.0, periodic: true

SDR for position 00,00: 11000000000000001100000000000000110000000000000011000000000000001100000000000000110000000000000011000000000000001100000000000000
SDR for position 01,01: 01100000000000001100000000000000110000000000000001100000000000001100000000000000110000000000000011000000000000001100000000000000
SDR for position 02,02: 00110000000000001100000000000000110000000000000000110000000000001100000000000000110000000000000011000000000000001100000000000000
SDR for position 03,03: 00011000000000001100000000000000110000000000000000011000000000001100000000000000110000000000000011000000000000001100000000000000
SDR for position 04,04: 00001100000000001100000000000000110000000000000000001100000000001100000000000000110000000000000011000000000000001100000000000000
SDR for position 05,05: 00000110000000001100000000000000110000000000000000000110000000001100000000000000110000000000000011000000000000001100000000000000
SDR for position 06,06: 00000011000000001100000000000000110000000000000000000011000000001100000000000000110000000000000011000000000000001100000000000000
SDR for position 07,07: 00000001100000001100000000000000110000000000000000000001100000001100000000000000110000000000000011000000000000001100000000000000
SDR for position 08,08: 00000000110000001100000000000000110000000000000000000000110000001100000000000000110000000000000011000000000000001100000000000000
SDR for position 09,09: 00000000011000001100000000000000110000000000000000000000011000001100000000000000110000000000000011000000000000001100000000000000
SDR for position 10,10: 00000000001100001100000000000000110000000000000000000000001100001100000000000000110000000000000011000000000000001100000000000000
SDR for position 11,11: 00000000000110001100000000000000110000000000000000000000000110001100000000000000110000000000000011000000000000001100000000000000
SDR for position 12,12: 00000000000011001100000000000000110000000000000000000000000011001100000000000000110000000000000011000000000000001100000000000000
SDR for position 13,13: 00000000000001101100000000000000110000000000000000000000000001101100000000000000110000000000000011000000000000001100000000000000
SDR for position 14,14: 00000000000000110110000000000000110000000000000000000000000000110110000000000000110000000000000011000000000000001100000000000000
SDR for position 15,15: 10000000000000010110000000000000110000000000000010000000000000010110000000000000110000000000000011000000000000001100000000000000
SDR for position 16,16: 11000000000000000110000000000000110000000000000011000000000000000110000000000000110000000000000011000000000000001100000000000000
SDR for position 17,17: 01100000000000000110000000000000110000000000000001100000000000000110000000000000110000000000000011000000000000001100000000000000
SDR for position 18,18: 00110000000000000110000000000000110000000000000000110000000000000110000000000000110000000000000011000000000000001100000000000000
SDR for position 19,19: 00011000000000000110000000000000110000000000000000011000000000000110000000000000110000000000000011000000000000001100000000000000

GridEncoder length: 128, bits: 12
- D01 GridDimensionScaledEncoder length: 48, bits: 6, resolution: 1.0, capacity: 360
  - 001 ScalarEncoder length: 24, bits: 2, resolution: 1.0, min: 0.0, max: 24.0, periodic: true
  - 002 ScalarEncoder length: 24, bits: 4, resolution: 15.0, min: 0.0, max: 360.0, periodic: true
- D02 GridDimensionScaledEncoder length: 80, bits: 6, resolution: 1.0, capacity: 720
  - 001 ScalarEncoder length: 32, bits: 2, resolution: 1.0, min: 0.0, max: 32.0, periodic: true
  - 002 ScalarEncoder length: 48, bits: 4, resolution: 15.0, min: 0.0, max: 720.0, periodic: true

SDR for position 00,00: 11000000000000000000000011110000000000000000000011000000000000000000000000000000111100000000000000000000000000000000000000000000
SDR for position 01,01: 01100000000000000000000011110000000000000000000001100000000000000000000000000000111100000000000000000000000000000000000000000000
SDR for position 02,02: 00110000000000000000000011110000000000000000000000110000000000000000000000000000111100000000000000000000000000000000000000000000
SDR for position 03,03: 00011000000000000000000011110000000000000000000000011000000000000000000000000000111100000000000000000000000000000000000000000000
SDR for position 04,04: 00001100000000000000000011110000000000000000000000001100000000000000000000000000111100000000000000000000000000000000000000000000
SDR for position 05,05: 00000110000000000000000011110000000000000000000000000110000000000000000000000000111100000000000000000000000000000000000000000000
SDR for position 06,06: 00000011000000000000000011110000000000000000000000000011000000000000000000000000111100000000000000000000000000000000000000000000
SDR for position 07,07: 00000001100000000000000011110000000000000000000000000001100000000000000000000000111100000000000000000000000000000000000000000000
SDR for position 08,08: 00000000110000000000000011110000000000000000000000000000110000000000000000000000111100000000000000000000000000000000000000000000
SDR for position 09,09: 00000000011000000000000011110000000000000000000000000000011000000000000000000000111100000000000000000000000000000000000000000000
SDR for position 10,10: 00000000001100000000000011110000000000000000000000000000001100000000000000000000111100000000000000000000000000000000000000000000
SDR for position 11,11: 00000000000110000000000011110000000000000000000000000000000110000000000000000000111100000000000000000000000000000000000000000000
SDR for position 12,12: 00000000000011000000000011110000000000000000000000000000000011000000000000000000111100000000000000000000000000000000000000000000
SDR for position 13,13: 00000000000001100000000011110000000000000000000000000000000001100000000000000000111100000000000000000000000000000000000000000000
SDR for position 14,14: 00000000000000110000000011110000000000000000000000000000000000110000000000000000111100000000000000000000000000000000000000000000
SDR for position 15,15: 00000000000000011000000001111000000000000000000000000000000000011000000000000000011110000000000000000000000000000000000000000000
SDR for position 16,16: 00000000000000001100000001111000000000000000000000000000000000001100000000000000011110000000000000000000000000000000000000000000
SDR for position 17,17: 00000000000000000110000001111000000000000000000000000000000000000110000000000000011110000000000000000000000000000000000000000000
SDR for position 18,18: 00000000000000000011000001111000000000000000000000000000000000000011000000000000011110000000000000000000000000000000000000000000
SDR for position 19,19: 00000000000000000001100001111000000000000000000000000000000000000001100000000000011110000000000000000000000000000000000000000000

GridEncoder length: 512, bits: 50
- D01 GridDimensionScaledEncoder length: 224, bits: 20, resolution: 1.0, capacity: 4064
  - 001 ScalarEncoder length: 32, bits: 2, resolution: 1.0, min: 0.0, max: 32.0, periodic: true
  - 002 ScalarEncoder length: 64, bits: 4, resolution: 15.0, min: 0.0, max: 960.0, periodic: true
  - 003 ScalarEncoder length: 96, bits: 6, resolution: 53.0, min: 0.0, max: 5088.0, periodic: true
  - 004 ScalarEncoder length: 32, bits: 8, resolution: 127.0, min: 0.0, max: 4064.0, periodic: true
- D02 GridDimensionScaledEncoder length: 288, bits: 30, resolution: 1.0, capacity: 11952
  - 001 ScalarEncoder length: 24, bits: 2, resolution: 1.0, min: 0.0, max: 24.0, periodic: true
  - 002 ScalarEncoder length: 48, bits: 4, resolution: 15.0, min: 0.0, max: 720.0, periodic: true
  - 003 ScalarEncoder length: 72, bits: 6, resolution: 53.0, min: 0.0, max: 3816.0, periodic: true
  - 004 ScalarEncoder length: 96, bits: 8, resolution: 127.0, min: 0.0, max: 12192.0, periodic: true
  - 005 ScalarEncoder length: 48, bits: 10, resolution: 249.0, min: 0.0, max: 11952.0, periodic: true

SDR for position 00,00: 11000000000000000000000000000000111100000000000000000000000000000000000000000000000000000000000011111100000000000000000000000000000000000000000000000000000000000000000000000000000000000000000011111111000000000000000000000000110000000000000000000000111100000000000000000000000000000000000000000000111111000000000000000000000000000000000000000000000000000000000000000000111111110000000000000000000000000000000000000000000000000000000000000000000000000000000000000000111111111100000000000000000000000000000000000000
SDR for position 01,01: 01100000000000000000000000000000111100000000000000000000000000000000000000000000000000000000000011111100000000000000000000000000000000000000000000000000000000000000000000000000000000000000000011111111000000000000000000000000011000000000000000000000111100000000000000000000000000000000000000000000111111000000000000000000000000000000000000000000000000000000000000000000111111110000000000000000000000000000000000000000000000000000000000000000000000000000000000000000111111111100000000000000000000000000000000000000
SDR for position 02,02: 00110000000000000000000000000000111100000000000000000000000000000000000000000000000000000000000011111100000000000000000000000000000000000000000000000000000000000000000000000000000000000000000011111111000000000000000000000000001100000000000000000000111100000000000000000000000000000000000000000000111111000000000000000000000000000000000000000000000000000000000000000000111111110000000000000000000000000000000000000000000000000000000000000000000000000000000000000000111111111100000000000000000000000000000000000000
SDR for position 03,03: 00011000000000000000000000000000111100000000000000000000000000000000000000000000000000000000000011111100000000000000000000000000000000000000000000000000000000000000000000000000000000000000000011111111000000000000000000000000000110000000000000000000111100000000000000000000000000000000000000000000111111000000000000000000000000000000000000000000000000000000000000000000111111110000000000000000000000000000000000000000000000000000000000000000000000000000000000000000111111111100000000000000000000000000000000000000
SDR for position 04,04: 00001100000000000000000000000000111100000000000000000000000000000000000000000000000000000000000011111100000000000000000000000000000000000000000000000000000000000000000000000000000000000000000011111111000000000000000000000000000011000000000000000000111100000000000000000000000000000000000000000000111111000000000000000000000000000000000000000000000000000000000000000000111111110000000000000000000000000000000000000000000000000000000000000000000000000000000000000000111111111100000000000000000000000000000000000000
SDR for position 05,05: 00000110000000000000000000000000111100000000000000000000000000000000000000000000000000000000000011111100000000000000000000000000000000000000000000000000000000000000000000000000000000000000000011111111000000000000000000000000000001100000000000000000111100000000000000000000000000000000000000000000111111000000000000000000000000000000000000000000000000000000000000000000111111110000000000000000000000000000000000000000000000000000000000000000000000000000000000000000111111111100000000000000000000000000000000000000
SDR for position 06,06: 00000011000000000000000000000000111100000000000000000000000000000000000000000000000000000000000011111100000000000000000000000000000000000000000000000000000000000000000000000000000000000000000011111111000000000000000000000000000000110000000000000000111100000000000000000000000000000000000000000000111111000000000000000000000000000000000000000000000000000000000000000000111111110000000000000000000000000000000000000000000000000000000000000000000000000000000000000000111111111100000000000000000000000000000000000000
SDR for position 07,07: 00000001100000000000000000000000111100000000000000000000000000000000000000000000000000000000000011111100000000000000000000000000000000000000000000000000000000000000000000000000000000000000000011111111000000000000000000000000000000011000000000000000111100000000000000000000000000000000000000000000111111000000000000000000000000000000000000000000000000000000000000000000111111110000000000000000000000000000000000000000000000000000000000000000000000000000000000000000111111111100000000000000000000000000000000000000
SDR for position 08,08: 00000000110000000000000000000000111100000000000000000000000000000000000000000000000000000000000011111100000000000000000000000000000000000000000000000000000000000000000000000000000000000000000011111111000000000000000000000000000000001100000000000000111100000000000000000000000000000000000000000000111111000000000000000000000000000000000000000000000000000000000000000000111111110000000000000000000000000000000000000000000000000000000000000000000000000000000000000000111111111100000000000000000000000000000000000000
SDR for position 09,09: 00000000011000000000000000000000111100000000000000000000000000000000000000000000000000000000000011111100000000000000000000000000000000000000000000000000000000000000000000000000000000000000000011111111000000000000000000000000000000000110000000000000111100000000000000000000000000000000000000000000111111000000000000000000000000000000000000000000000000000000000000000000111111110000000000000000000000000000000000000000000000000000000000000000000000000000000000000000111111111100000000000000000000000000000000000000
SDR for position 10,10: 00000000001100000000000000000000111100000000000000000000000000000000000000000000000000000000000011111100000000000000000000000000000000000000000000000000000000000000000000000000000000000000000011111111000000000000000000000000000000000011000000000000111100000000000000000000000000000000000000000000111111000000000000000000000000000000000000000000000000000000000000000000111111110000000000000000000000000000000000000000000000000000000000000000000000000000000000000000111111111100000000000000000000000000000000000000
SDR for position 11,11: 00000000000110000000000000000000111100000000000000000000000000000000000000000000000000000000000011111100000000000000000000000000000000000000000000000000000000000000000000000000000000000000000011111111000000000000000000000000000000000001100000000000111100000000000000000000000000000000000000000000111111000000000000000000000000000000000000000000000000000000000000000000111111110000000000000000000000000000000000000000000000000000000000000000000000000000000000000000111111111100000000000000000000000000000000000000
SDR for position 12,12: 00000000000011000000000000000000111100000000000000000000000000000000000000000000000000000000000011111100000000000000000000000000000000000000000000000000000000000000000000000000000000000000000011111111000000000000000000000000000000000000110000000000111100000000000000000000000000000000000000000000111111000000000000000000000000000000000000000000000000000000000000000000111111110000000000000000000000000000000000000000000000000000000000000000000000000000000000000000111111111100000000000000000000000000000000000000
SDR for position 13,13: 00000000000001100000000000000000111100000000000000000000000000000000000000000000000000000000000011111100000000000000000000000000000000000000000000000000000000000000000000000000000000000000000011111111000000000000000000000000000000000000011000000000111100000000000000000000000000000000000000000000111111000000000000000000000000000000000000000000000000000000000000000000111111110000000000000000000000000000000000000000000000000000000000000000000000000000000000000000111111111100000000000000000000000000000000000000
SDR for position 14,14: 00000000000000110000000000000000111100000000000000000000000000000000000000000000000000000000000011111100000000000000000000000000000000000000000000000000000000000000000000000000000000000000000011111111000000000000000000000000000000000000001100000000111100000000000000000000000000000000000000000000111111000000000000000000000000000000000000000000000000000000000000000000111111110000000000000000000000000000000000000000000000000000000000000000000000000000000000000000111111111100000000000000000000000000000000000000
SDR for position 15,15: 00000000000000011000000000000000011110000000000000000000000000000000000000000000000000000000000011111100000000000000000000000000000000000000000000000000000000000000000000000000000000000000000011111111000000000000000000000000000000000000000110000000011110000000000000000000000000000000000000000000111111000000000000000000000000000000000000000000000000000000000000000000111111110000000000000000000000000000000000000000000000000000000000000000000000000000000000000000111111111100000000000000000000000000000000000000
SDR for position 16,16: 00000000000000001100000000000000011110000000000000000000000000000000000000000000000000000000000011111100000000000000000000000000000000000000000000000000000000000000000000000000000000000000000011111111000000000000000000000000000000000000000011000000011110000000000000000000000000000000000000000000111111000000000000000000000000000000000000000000000000000000000000000000111111110000000000000000000000000000000000000000000000000000000000000000000000000000000000000000111111111100000000000000000000000000000000000000
SDR for position 17,17: 00000000000000000110000000000000011110000000000000000000000000000000000000000000000000000000000011111100000000000000000000000000000000000000000000000000000000000000000000000000000000000000000011111111000000000000000000000000000000000000000001100000011110000000000000000000000000000000000000000000111111000000000000000000000000000000000000000000000000000000000000000000111111110000000000000000000000000000000000000000000000000000000000000000000000000000000000000000111111111100000000000000000000000000000000000000
SDR for position 18,18: 00000000000000000011000000000000011110000000000000000000000000000000000000000000000000000000000011111100000000000000000000000000000000000000000000000000000000000000000000000000000000000000000011111111000000000000000000000000000000000000000000110000011110000000000000000000000000000000000000000000111111000000000000000000000000000000000000000000000000000000000000000000111111110000000000000000000000000000000000000000000000000000000000000000000000000000000000000000111111111100000000000000000000000000000000000000
SDR for position 19,19: 00000000000000000001100000000000011110000000000000000000000000000000000000000000000000000000000011111100000000000000000000000000000000000000000000000000000000000000000000000000000000000000000011111111000000000000000000000000000000000000000000011000011110000000000000000000000000000000000000000000111111000000000000000000000000000000000000000000000000000000000000000000111111110000000000000000000000000000000000000000000000000000000000000000000000000000000000000000111111111100000000000000000000000000000000000000
~~~~

nl.zeesoft.zdk.test.impl.htm.TestPooler
---------------------------------------
This test shows how to use a *Pooler* to convert encoder output SDRs into consistently sparse representations.

**Example implementation**  
~~~~
// Create the configuration
PoolerConfig config = new PoolerConfig(200,1024,21);
// Create the pooler
Pooler pooler = new Pooler(config);
// Randomize the connections
pooler.randomizeConnections();
// Obtain the output SDR for a certain input SDR
SDR sdr = pooler.getSDRForInput(new SDR(100),true);
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
Initializing pooler took: 16 ms
Randomizing connections took: 78 ms

Processing input SDR map (learning: false) ...
Processing input SDR map took: 11505 ms

Performance statistics;
calculateOverlapScores:        5217.896 ms
selectActiveColumns:            764.408 ms
logActivity:                    718.213 ms
calculateColumnGroupActivity:  3373.059 ms
updateBoostFactors:            1192.225 ms
total:                        11324.810 ms
logSize:                          15330   
avgPerLog:                        0.739 ms

Combined average: 0.3740651, Combined weekly average: 5.264739

Pooler input dimensions: 16*16, output dimensions: 32*32
- Total proximal links: 92160, active: 46146
- Average proximal inputs per column: 90
- Column groups: 144, average columns per group: 441

Processing input SDR map (learning: true) ...
Processing input SDR map took: 11578 ms

Performance statistics;
calculateOverlapScores:        2504.074 ms
selectActiveColumns:            915.218 ms
logActivity:                    862.803 ms
calculateColumnGroupActivity:  2763.834 ms
updateBoostFactors:            1214.250 ms
total:                        11367.914 ms
learnActiveColumnsOnBits:      3050.220 ms
logSize:                          15330   
avgPerLog:                        0.742 ms

Combined average: 0.32842818, Combined weekly average: 7.4676304

Original ratio: 14.074392, learned ratio: 22.737484
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
SDR sdr = memory.getSDRForInput(new SDR(100),true);
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
Memory dimensions: 32*32*4

Processing input SDR map (5000/15330) ...
Processed SDRs: 500, bursting average: 4 (max: 10)
Processed SDRs: 1000, bursting average: 2 (max: 7)
Processed SDRs: 1500, bursting average: 1 (max: 9)
Processed SDRs: 2000, bursting average: 1 (max: 4)
Processed SDRs: 2500, bursting average: 1 (max: 5)
Processed SDRs: 3000, bursting average: 1 (max: 5)
Processed SDRs: 3500, bursting average: 0 (max: 4)
Processed SDRs: 4000, bursting average: 0 (max: 4)
Processed SDRs: 4500, bursting average: 0 (max: 5)
Processed SDRs: 5000, bursting average: 0 (max: 2)
Processing input SDR map took: 13391 ms

Performance statistics;
cycleActiveState:       180.936 ms
activateColumnCells:    194.808 ms
calculateActivity:      878.372 ms
selectPredictiveCells:  387.028 ms
updatePredictions:     4026.171 ms
total:                 5675.563 ms
logSize:                   5000   
avgPerLog:                1.135 ms

Memory dimensions: 32*32*4
- Total distal links: 227344, active: 215892
- Average distal inputs per memory cell: 55 (min: 0, max: 105)
- Average connected distal inputs per memory cell: 52 (min: 0, max: 104)
~~~~

nl.zeesoft.zdk.test.impl.htm.TestMerger
---------------------------------------
This test shows how to use a *Merger* to merge multiple SDRs into a single SDR.

**Example implementation**  
~~~~
// Create the merger
Merger merger = new Merger(new MergerConfig());
// Create an input SDR
SDR sdr = new SDR(100);
sdr.setBit(0,true);
// Create a context SDR list
SDR sdr2 = new SDR(100);
sdr2.setBit(0,true);
List<SDR> context = new ArrayList<SDR>();
context.add(sdr2);
// Obtain the output SDRs for the input SDR and its context SDRs
Lis<SDR> outputSDRs = merger.getSDRsForInput(sdr,context,false);
~~~~

Class references;  
 * [TestMerger](https://github.com/DyzLecticus/Zeesoft/blob/master/V4.0/ZDK/src/nl/zeesoft/zdk/test/impl/htm/TestMerger.java)
 * [Merger](https://github.com/DyzLecticus/Zeesoft/blob/master/V4.0/ZDK/src/nl/zeesoft/zdk/htm/proc/Merger.java)
 * [SDR](https://github.com/DyzLecticus/Zeesoft/blob/master/V4.0/ZDK/src/nl/zeesoft/zdk/htm/util/SDR.java)

**Test output**  
The output of this test shows a description of the merger and the result of several SDR merges.  
~~~~
Merger maximum number of on bits: 4

Input SDR: 50,0,24
Context SDR: 50,0,24
Merged SDR: 100,0,50,24,74

Context SDR 1: 50,0,24
Context SDR 2: 50,0,24
Merged SDR: 100,0,50,24,74
~~~~

nl.zeesoft.zdk.test.impl.htm.TestZGridColumnEncoders
----------------------------------------------------
This test shows the configuration options of several *ZGridColumnEncoder* instances.  
*ZGridColumnEncoder* instances are used to translate *ZGrid* request values into SDRs for further grid processing.  

**Example implementation**  
~~~~
// Create a date/time grid encoder
ZGridEncoderDateTime dateTimeEncoder = new ZGridEncoderDateTime();
// Transform the encoder to JSON
JsFile json = dateTimeEncoder.toJson();
// Configure the encoder using JSON
dateTimeEncoder.fromJson(json);
~~~~

Class references;  
 * [TestZGridColumnEncoders](https://github.com/DyzLecticus/Zeesoft/blob/master/V4.0/ZDK/src/nl/zeesoft/zdk/test/impl/htm/TestZGridColumnEncoders.java)
 * [ZGridColumnEncoder](https://github.com/DyzLecticus/Zeesoft/blob/master/V4.0/ZDK/src/nl/zeesoft/zdk/htm/grid/ZGridColumnEncoder.java)
 * [ZGrid](https://github.com/DyzLecticus/Zeesoft/blob/master/V4.0/ZDK/src/nl/zeesoft/zdk/htm/grid/ZGrid.java)
 * [SDR](https://github.com/DyzLecticus/Zeesoft/blob/master/V4.0/ZDK/src/nl/zeesoft/zdk/htm/util/SDR.java)
 * [ZGridEncoderDateTime](https://github.com/DyzLecticus/Zeesoft/blob/master/V4.0/ZDK/src/nl/zeesoft/zdk/htm/grid/enc/ZGridEncoderDateTime.java)
 * [ZGridEncoderValue](https://github.com/DyzLecticus/Zeesoft/blob/master/V4.0/ZDK/src/nl/zeesoft/zdk/htm/grid/enc/ZGridEncoderValue.java)
 * [ZGridEncoderProperty](https://github.com/DyzLecticus/Zeesoft/blob/master/V4.0/ZDK/src/nl/zeesoft/zdk/htm/grid/enc/ZGridEncoderProperty.java)

**Test output**  
The output of this test shows the JSON representations of several encoders  
~~~~
Date/time encoder JSON:
{
  "scale": 4,
  "bitsPerEncoder": 8,
  "includeMonth": true,
  "includeDayOfWeek": true,
  "includeHourOfDay": true,
  "includeMinute": true,
  "includeSecond": true
}

Value encoder JSON:
{
  "type": "DIMENSIONAL",
  "length": 256,
  "bits": 8,
  "resolution": 1.0,
  "minValue": 0.0,
  "maxValue": 247.0,
  "periodic": false,
  "valueKey": "VALUE"
}

Property encoder JSON:
{
  "length": 256,
  "bits": 8,
  "valueKey": "PROPERTY"
}
~~~~

nl.zeesoft.zdk.test.impl.htm.TestZGrid
--------------------------------------
This test shows how to create and configure a *ZGrid* instance to learn and predict sequences of values.
A *ZGrid* consists of several rows and columns where each column can process a certain input value.
It uses multithreading to maximize the throughput of grid requests.
The first row of a *ZGrid* is reserved for *ZGridColumnEncoder* objects that translate request input values into SDRs.
The remaining rows can be used for *Pooler*, *Memory*, *Classifier*, *Merger* and custom processors.
Context routing can be used to route the output of a column to the context of another column.

**Example implementation**  
~~~~
// Create the grid
ZGrid grid = new ZGrid(4,2);
// Add encoders
grid.setEncoder(0,new ZGridEncoderDateTime());
grid.setEncoder(1,new ZGridEncoderValue(256));
// Add processors
grid.setProcessor(1,0,new PoolerConfig(256,1024,21));
grid.setProcessor(1,1,new PoolerConfig(256,1024,21));
MemoryConfig config = new MemoryConfig(1024,21);
config.addContextDimension(1024);
grid.setProcessor(2,1,config);
grid.setProcessor(3,1,new ClassifierConfig(1));
// Route output from date/time pooler to value memory context
grid.addColumnContext(2,1,1,0);
// Route output from value encoder to value classifier context
grid.addColumnContext(3,1,0,1);
// Add a listener for grid results
grid.addListener(this);
// Randomize grid pooler connections
grid.randomizePoolerConnections();
// Start the grid
grid.start();
// Add requests
ZGridRequest request = grid.getNewRequest();
request.inputValues[0] = request.datetime;
request.inputValues[1] = 1F;
request.inputLabels[1] = "Normal";
long requestId = grid.addRequest(request);
// Remember to stop and destroy the grid after use
grid.stop();
grid.whileActive();
grid.destroy();
~~~~

Class references;  
 * [TestZGrid](https://github.com/DyzLecticus/Zeesoft/blob/master/V4.0/ZDK/src/nl/zeesoft/zdk/test/impl/htm/TestZGrid.java)
 * [ZGrid](https://github.com/DyzLecticus/Zeesoft/blob/master/V4.0/ZDK/src/nl/zeesoft/zdk/htm/grid/ZGrid.java)
 * [ZGridRequest](https://github.com/DyzLecticus/Zeesoft/blob/master/V4.0/ZDK/src/nl/zeesoft/zdk/htm/grid/ZGridRequest.java)
 * [ZGridColumnEncoder](https://github.com/DyzLecticus/Zeesoft/blob/master/V4.0/ZDK/src/nl/zeesoft/zdk/htm/grid/ZGridColumnEncoder.java)

**Test output**  
The output of this test shows;  
 * A description of the grid after initialization  
 * A JSON representation of the grid configuration  
 * The classifier prediction accuracy of the grid while processing requests   
 * A description of the grid after processing requests  
 * The size of the state data for each grid processor  
~~~~
Grid dimensions: 5*5
- Column 00-00 = CombinedEncoder length: 184, bits: 8
  - SECOND ScalarEncoder length: 184, bits: 8, resolution: 1.0, min: 0.0, max: 60.0, periodic: true
- Column 00-01 = CombinedEncoder length: 256, bits: 16
  - VALUE ScalarEncoder length: 256, bits: 16, resolution: 1.0, min: 0.0, max: 30.0, periodic: false
- Column 00-02 = CombinedEncoder length: 64, bits: 8
  - POSX ScalarEncoder length: 64, bits: 8, resolution: 1.0, min: 0.0, max: 20.0, periodic: false
- Column 00-03 = CombinedEncoder length: 64, bits: 8
  - POSY ScalarEncoder length: 64, bits: 8, resolution: 1.0, min: 0.0, max: 20.0, periodic: false
- Column 00-04 = CombinedEncoder length: 64, bits: 8
  - POSZ ScalarEncoder length: 64, bits: 8, resolution: 1.0, min: 0.0, max: 20.0, periodic: false
- Column 01-03 = Merger maximum number of on bits: 0
- Column 02-00 = Pooler input dimensions: 14*14, output dimensions: 32*32
  - Total proximal links: 90304, active: 45062
  - Average proximal inputs per column: 88 (min: 82, max: 90)
  - Column groups: 144, average columns per group: 441
- Column 02-01 = Pooler input dimensions: 16*16, output dimensions: 32*32
  - Total proximal links: 92160, active: 46277
  - Average proximal inputs per column: 90
  - Column groups: 144, average columns per group: 441
- Column 02-03 = Pooler input dimensions: 14*14, output dimensions: 32*32
  - Total proximal links: 91776, active: 45656
  - Average proximal inputs per column: 89 (min: 87, max: 90)
  - Column groups: 144, average columns per group: 441
- Column 03-01 = Memory dimensions: 32*32*4
- Column 04-00 = Detector start: 2500, window long/short: 500/1, threshold: 0.3
- Column 04-01 = Classifier value key: VALUE, predict steps: 1

Grid JSON;
{
  "rows": 5,
  "columns": 5,
  "configurations": [
    {
      "columnId": "00-00",
      "className": "nl.zeesoft.zdk.htm.grid.enc.ZGridEncoderDateTime",
      "scale": 2,
      "bitsPerEncoder": 8,
      "includeMonth": false,
      "includeDayOfWeek": false,
      "includeHourOfDay": false,
      "includeMinute": false,
      "includeSecond": true
    },
    {
      "columnId": "00-01",
      "className": "nl.zeesoft.zdk.htm.grid.enc.ZGridEncoderValue",
      "type": "SCALAR",
      "length": 256,
      "bits": 16,
      "resolution": 1.0,
      "minValue": 0.0,
      "maxValue": 30.0,
      "periodic": false,
      "valueKey": "VALUE"
    },
    {
      "columnId": "00-02",
      "className": "nl.zeesoft.zdk.htm.grid.enc.ZGridEncoderValue",
      "type": "SCALAR",
      "length": 64,
      "bits": 8,
      "resolution": 1.0,
      "minValue": 0.0,
      "maxValue": 20.0,
      "periodic": false,
      "valueKey": "POSX"
    },
    {
      "columnId": "00-03",
      "className": "nl.zeesoft.zdk.htm.grid.enc.ZGridEncoderValue",
      "type": "SCALAR",
      "length": 64,
      "bits": 8,
      "resolution": 1.0,
      "minValue": 0.0,
      "maxValue": 20.0,
      "periodic": false,
      "valueKey": "POSY"
    },
    {
      "columnId": "00-04",
      "className": "nl.zeesoft.zdk.htm.grid.enc.ZGridEncoderValue",
      "type": "SCALAR",
      "length": 64,
      "bits": 8,
      "resolution": 1.0,
      "minValue": 0.0,
      "maxValue": 20.0,
      "periodic": false,
      "valueKey": "POSZ"
    },
    {
      "columnId": "01-03",
      "className": "nl.zeesoft.zdk.htm.proc.MergerConfig",
      "union": false,
      "maxOnBits": 0,
      "contexts": [
        {
          "sourceRow": 0,
          "sourceColumn": 2,
          "sourceIndex": 0
        },
        {
          "sourceRow": 0,
          "sourceColumn": 4,
          "sourceIndex": 0
        }
      ]
    },
    {
      "columnId": "02-00",
      "className": "nl.zeesoft.zdk.htm.proc.PoolerConfig",
      "inputLength": 184,
      "outputLength": 1024,
      "outputBits": 21,
      "inputSizeX": 14,
      "inputSizeY": 14,
      "outputSizeX": 32,
      "outputSizeY": 32,
      "potentialProximalConnections": 0.75,
      "proximalRadius": 5,
      "proximalConnectionThreshold": 0.1,
      "proximalConnectionDecrement": 0.008,
      "proximalConnectionIncrement": 0.05,
      "boostStrength": 10,
      "boostInhibitionRadius": 10,
      "boostActivityLogSize": 100
    },
    {
      "columnId": "02-01",
      "className": "nl.zeesoft.zdk.htm.proc.PoolerConfig",
      "inputLength": 256,
      "outputLength": 1024,
      "outputBits": 21,
      "inputSizeX": 16,
      "inputSizeY": 16,
      "outputSizeX": 32,
      "outputSizeY": 32,
      "potentialProximalConnections": 0.75,
      "proximalRadius": 5,
      "proximalConnectionThreshold": 0.1,
      "proximalConnectionDecrement": 0.008,
      "proximalConnectionIncrement": 0.05,
      "boostStrength": 10,
      "boostInhibitionRadius": 10,
      "boostActivityLogSize": 100
    },
    {
      "columnId": "02-03",
      "className": "nl.zeesoft.zdk.htm.proc.PoolerConfig",
      "inputLength": 192,
      "outputLength": 1024,
      "outputBits": 21,
      "inputSizeX": 14,
      "inputSizeY": 14,
      "outputSizeX": 32,
      "outputSizeY": 32,
      "potentialProximalConnections": 0.75,
      "proximalRadius": 5,
      "proximalConnectionThreshold": 0.1,
      "proximalConnectionDecrement": 0.008,
      "proximalConnectionIncrement": 0.05,
      "boostStrength": 10,
      "boostInhibitionRadius": 10,
      "boostActivityLogSize": 100
    },
    {
      "columnId": "03-01",
      "className": "nl.zeesoft.zdk.htm.proc.MemoryConfig",
      "length": 1024,
      "sizeX": 32,
      "sizeY": 32,
      "bits": 21,
      "depth": 4,
      "maxDistalConnectionsPerCell": 9999,
      "localDistalConnectedRadius": 64,
      "minAlmostActiveDistalConnections": 5,
      "distalConnectionThreshold": 0.2,
      "distalConnectionDecrement": 0.003,
      "distalConnectionIncrement": 0.1,
      "contextDimensions": "1024,1024",
      "contexts": [
        {
          "sourceRow": 2,
          "sourceColumn": 0,
          "sourceIndex": 0
        },
        {
          "sourceRow": 2,
          "sourceColumn": 3,
          "sourceIndex": 0
        }
      ]
    },
    {
      "columnId": "04-00",
      "className": "nl.zeesoft.zdk.htm.proc.DetectorConfig",
      "start": 2500,
      "windowLong": 500,
      "windowShort": 1,
      "threshold": 0.3,
      "contexts": [
        {
          "sourceRow": 2,
          "sourceColumn": 1,
          "sourceIndex": 0
        },
        {
          "sourceRow": 3,
          "sourceColumn": 1,
          "sourceIndex": 1
        }
      ]
    },
    {
      "columnId": "04-01",
      "className": "nl.zeesoft.zdk.htm.proc.ClassifierConfig",
      "predictSteps": "1",
      "valueKey": "VALUE",
      "labelKey": "LABEL",
      "maxCount": 40,
      "contexts": [
        {
          "sourceRow": 0,
          "sourceColumn": 1,
          "sourceIndex": 0
        }
      ]
    }
  ]
}

Added requests
2019-12-30 11:03:41:785 DBG nl.zeesoft.zdk.htm.grid.ZGrid: Starting grid ...
2019-12-30 11:03:41:787 DBG nl.zeesoft.zdk.htm.grid.ZGrid: Started grid
Processed requests: 100, accuracy: 0.480
Processed requests: 200, accuracy: 0.725
Processed requests: 300, accuracy: 0.814
Processed requests: 400, accuracy: 0.861
Processed requests: 500, accuracy: 0.888
Processed requests: 600, accuracy: 0.907
Processed requests: 700, accuracy: 0.920
Processed requests: 800, accuracy: 0.930
Processed requests: 900, accuracy: 0.937
Processed requests: 1000, accuracy: 0.943
Processed requests: 1100, accuracy: 0.994
Processed requests: 1200, accuracy: 0.998
Processed requests: 1300, accuracy: 0.998
Processed requests: 1400, accuracy: 0.998
Processed requests: 1500, accuracy: 0.998
Processed requests: 1600, accuracy: 0.998
Processed requests: 1700, accuracy: 0.998
Processed requests: 1800, accuracy: 0.998
Processed requests: 1900, accuracy: 0.999
Processed requests: 2000, accuracy: 0.999
Processed requests: 2100, accuracy: 0.999 (!)
Processed requests: 2200, accuracy: 0.999
Processed requests: 2300, accuracy: 0.999
Processed requests: 2400, accuracy: 0.999
Processed requests: 2500, accuracy: 1.000
Processed requests: 2600, accuracy: 1.000
Processed requests: 2700, accuracy: 1.000
Detected anomaly at id 2742, detected: 0.47619045, average: 0.9859054, difference: 0.3486194
Detected anomaly at id 2746, detected: 0.4285714, average: 0.9829531, difference: 0.39275384
Detected anomaly at id 2747, detected: 0.52380955, average: 0.98181015, difference: 0.3041941
Processed requests: 2800, accuracy: 0.960
Processed requests: 2900, accuracy: 0.960
Processed requests: 3000, accuracy: 0.960
2019-12-30 11:03:53:464 DBG nl.zeesoft.zdk.htm.grid.ZGrid: Stopping grid ...
2019-12-30 11:03:53:632 DBG nl.zeesoft.zdk.htm.grid.ZGrid: Stopped grid
Processing 3000 requests took 11908 ms

Grid dimensions: 5*5
- Column 00-00 = CombinedEncoder length: 184, bits: 8
  - SECOND ScalarEncoder length: 184, bits: 8, resolution: 1.0, min: 0.0, max: 60.0, periodic: true
- Column 00-01 = CombinedEncoder length: 256, bits: 16
  - VALUE ScalarEncoder length: 256, bits: 16, resolution: 1.0, min: 0.0, max: 30.0, periodic: false
- Column 00-02 = CombinedEncoder length: 64, bits: 8
  - POSX ScalarEncoder length: 64, bits: 8, resolution: 1.0, min: 0.0, max: 20.0, periodic: false
- Column 00-03 = CombinedEncoder length: 64, bits: 8
  - POSY ScalarEncoder length: 64, bits: 8, resolution: 1.0, min: 0.0, max: 20.0, periodic: false
- Column 00-04 = CombinedEncoder length: 64, bits: 8
  - POSZ ScalarEncoder length: 64, bits: 8, resolution: 1.0, min: 0.0, max: 20.0, periodic: false
- Column 01-03 = Merger maximum number of on bits: 0
- Column 02-00 = Pooler input dimensions: 14*14, output dimensions: 32*32
  - Total proximal links: 90304, active: 31219
  - Average proximal inputs per column: 88 (min: 82, max: 90)
  - Column groups: 144, average columns per group: 441
- Column 02-01 = Pooler input dimensions: 16*16, output dimensions: 32*32
  - Total proximal links: 92160, active: 34266
  - Average proximal inputs per column: 90
  - Column groups: 144, average columns per group: 441
- Column 02-03 = Pooler input dimensions: 14*14, output dimensions: 32*32
  - Total proximal links: 91776, active: 34545
  - Average proximal inputs per column: 89 (min: 87, max: 90)
  - Column groups: 144, average columns per group: 441
- Column 03-01 = Memory dimensions: 32*32*4
  - Total distal links: 201874, active: 186550
  - Average distal inputs per memory cell: 49 (min: 0, max: 126)
  - Average connected distal inputs per memory cell: 45 (min: 0, max: 126)
  - Average connected distal context inputs per memory cell: 32 (min: 0, max: 84)
- Column 04-00 = Detector start: 2500, window long/short: 500/1, threshold: 0.3 (ACTIVE)
- Column 04-01 = Classifier value key: VALUE, predict steps: 1

Grid state data;
- 02-00: 1129630
- 02-01: 1155988
- 02-03: 1105278
- 03-01: 4000898
- 04-00: 2974
- 04-01: 121136
- SELF: 25
Loading state data took 2463 ms
~~~~

Test results
------------
All 24 tests have been executed successfully (279287 assertions).  
Total test duration: 66681 ms (total sleep duration: 17800 ms).  

Memory usage per test;  
 * nl.zeesoft.zdk.test.impl.TestZStringEncoder: 757 Kb / 0 Mb
 * nl.zeesoft.zdk.test.impl.TestZStringSymbolParser: 524 Kb / 0 Mb
 * nl.zeesoft.zdk.test.impl.TestCsv: 531 Kb / 0 Mb
 * nl.zeesoft.zdk.test.impl.TestJson: 545 Kb / 0 Mb
 * nl.zeesoft.zdk.test.impl.TestZHttpRequest: 557 Kb / 0 Mb
 * nl.zeesoft.zdk.test.impl.TestMessenger: 763 Kb / 0 Mb
 * nl.zeesoft.zdk.test.impl.TestZMatrix: 829 Kb / 0 Mb
 * nl.zeesoft.zdk.test.impl.TestGeneticCode: 790 Kb / 0 Mb
 * nl.zeesoft.zdk.test.impl.TestNeuralNet: 3769 Kb / 3 Mb
 * nl.zeesoft.zdk.test.impl.TestGeneticNN: 4552 Kb / 4 Mb
 * nl.zeesoft.zdk.test.impl.TestEvolver: 4346 Kb / 4 Mb
 * nl.zeesoft.zdk.test.impl.htm.TestSDR: 916 Kb / 0 Mb
 * nl.zeesoft.zdk.test.impl.htm.TestSDRMap: 933 Kb / 0 Mb
 * nl.zeesoft.zdk.test.impl.htm.TestScalarEncoder: 933 Kb / 0 Mb
 * nl.zeesoft.zdk.test.impl.htm.TestRDScalarEncoder: 957 Kb / 0 Mb
 * nl.zeesoft.zdk.test.impl.htm.TestDateTimeEncoder: 964 Kb / 0 Mb
 * nl.zeesoft.zdk.test.impl.htm.TestDateTimeValuesEncoder: 958 Kb / 0 Mb
 * nl.zeesoft.zdk.test.impl.htm.TestDateTimeValueEncoder: 973 Kb / 0 Mb
 * nl.zeesoft.zdk.test.impl.htm.TestGridEncoder: 979 Kb / 0 Mb
 * nl.zeesoft.zdk.test.impl.htm.TestPooler: 34940 Kb / 34 Mb
 * nl.zeesoft.zdk.test.impl.htm.TestMemory: 34947 Kb / 34 Mb
 * nl.zeesoft.zdk.test.impl.htm.TestMerger: 34959 Kb / 34 Mb
 * nl.zeesoft.zdk.test.impl.htm.TestZGridColumnEncoders: 35024 Kb / 34 Mb
 * nl.zeesoft.zdk.test.impl.htm.TestZGrid: 35221 Kb / 34 Mb
