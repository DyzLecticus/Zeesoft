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
Key: 2171390044467960530778525673986022108942778743122471052420298814
Input text: Hello, my name is 'Dyz Lecticus'. How are you feeling today? :-) (Don't you know how to: [re;spond]!).

Key encoded text: P7V7dbM7L9Eb~567m0Z8m0LaRb1cd067U0U7g67bXbwc~9d8Y0KaybJ9bdvcuaP5u7b716t7LcNbP9W8AbAaDcvbb0X7:7m8x8m00b#746~9H819a8L6e9CclbebR5I5e7Z6~aP7K9~bS6T5m0d0t0L0Pb0draj7C928n7Sb~aqcN0V7U9CawbF9#ctcraW6t8e8l7g6:b2b0
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
2019-10-06 17:47:21:537 DBG nl.zeesoft.zdk.test.impl.TestMessengerListener: Test log debug message before Messenger has started
2019-10-06 17:47:21:838 ERR nl.zeesoft.zdk.test.impl.TestMessengerListener: Test log error message while Messenger is working
2019-10-06 17:47:21:839 ERR nl.zeesoft.zdk.test.impl.TestMessengerListener: Test log exception stack trace
java.lang.NumberFormatException: For input string: "A"
	at java.lang.NumberFormatException.forInputString(NumberFormatException.java:65)
	at java.lang.Integer.parseInt(Integer.java:580)
	at java.lang.Integer.parseInt(Integer.java:615)
	at nl.zeesoft.zdk.test.impl.TestMessenger.test(TestMessenger.java:83)
	at nl.zeesoft.zdk.test.Tester.test(Tester.java:69)
	at nl.zeesoft.zdk.test.LibraryObject.describeAndTest(LibraryObject.java:39)
	at nl.zeesoft.zdk.test.impl.ZDK.main(ZDK.java:50)

2019-10-06 17:47:22:151 WRN nl.zeesoft.zdk.test.impl.TestMessengerListener: Test log warning message after Messenger has stopped
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
000.62 | 000.23 | -00.63
-------+--------+-------
-00.38 | -00.35 | -01.00

Randomized multiplied element wise;
018.61 | 006.93 | -18.98
-------+--------+-------
-11.28 | -10.50 | -29.96

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
000.88 | -00.97 | 000.03
-------+--------+-------
-00.86 | 000.26 | -00.72

Randomized matrix transposed;
000.88 | -00.86
-------+-------
-00.97 | 000.26
-------+-------
000.03 | -00.72
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
Genetic code: 7299990451686932893073365082904075231392671564726191780097701648640264874538832723479842123092288684
Mutated code: 7299990451686932793073365086904075231392671364726191780097701618640264874538832723579842123092288684
                              ^          ^               ^                  ^                   ^                 

Scaled property values;
0: 139
1: 686
2: 279 <
3: 508
4: 357 <
5: 313 <
6: 472
7: 261
8: 640
9: 421 <
10: 917
11: 874
12: 904
13: 619
14: 231 <
15: 648
16: 723
17: 364 <
18: 869 <
19: 45
20: 139
21: 168 <
22: 904
23: 327
24: 832
25: 930
26: 977
27: 640 <
28: 231
29: 9
30: 186 <
31: 186 <
32: 690 <
Mutated property values: 12
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
  Input: [1.00|1.00], output: [-0.00], expectation: [0.00], error: 0.00, loss: 0.0032400384
  Input: [0.00|1.00], output: [-0.00], expectation: [1.00], error: 1.00, loss: 1.0018582
  Input: [1.00|0.00], output: [-0.00], expectation: [1.00], error: 1.00, loss: 1.0013819
  Average error: 0.50, average loss: 0.50, success: false
Latest test results;
  Input: [1.00|1.00], output: [0.01], expectation: [0.00], error: -0.01, loss: 0.011711657
  Input: [0.00|1.00], output: [0.68], expectation: [1.00], error: 0.32, loss: 0.31935704
  Input: [1.00|0.00], output: [0.71], expectation: [1.00], error: 0.29, loss: 0.28751212
  Input: [0.00|0.00], output: [0.74], expectation: [0.00], error: -0.74, loss: 0.73823655
  Average error: 0.34, average loss: 0.34, success: false
Trained epochs: 10000, total average error: 3550.0266, total average loss: 3550.0266
================================================================================
Neural net activator: nl.zeesoft.zdk.functions.ZLeakyReLU, output activator: nl.zeesoft.zdk.functions.ZSoftmaxTop, learning rate: 0.1
Initial test results;
  Input: [0.00|0.00], output: [0.00], expectation: [0.00], error: 0.00, loss: 0.0
  Input: [1.00|1.00], output: [0.00], expectation: [0.00], error: 0.00, loss: 0.0
  Input: [0.00|1.00], output: [0.00], expectation: [1.00], error: 1.00, loss: 1.0
  Input: [1.00|0.00], output: [0.00], expectation: [1.00], error: 1.00, loss: 1.0
  Average error: 0.50, average loss: 0.50, success: false
Latest test results;
  Input: [0.00|0.00], output: [0.00], expectation: [0.00], error: 0.00, loss: 0.0
  Input: [1.00|1.00], output: [0.00], expectation: [0.00], error: 0.00, loss: 0.0
  Input: [0.00|1.00], output: [0.00], expectation: [1.00], error: 1.00, loss: 1.0
  Input: [1.00|0.00], output: [0.00], expectation: [1.00], error: 1.00, loss: 1.0
  Average error: 0.50, average loss: 0.50, success: false
Trained epochs: 10000, total average error: 5000.0, total average loss: 5000.0

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
    "2,1,-0.037282247,-0.080730624",
    "1,1,0.0"
  ],
  "weights": [
    "1,1,0.0",
    "2,2,-1.0733123,-0.6580581,-2.8773637,-3.4812655",
    "1,2,-0.1103344,-0.25459325"
  ],
  "biases": [
    "1,1,0.0",
    "2,1,-2.6551332,-5.196209",
    "1,1,-0.6946341"
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
Neural net activator: nl.zeesoft.zdk.functions.ZReLU, output activator: nl.zeesoft.zdk.functions.ZReLU, learning rate: 0.001
Initial test results;
  Input: [0.00|0.00], output: [0.00], expectation: [0.00], error: 0.00, loss: 0.0
  Input: [1.00|1.00], output: [0.00], expectation: [0.00], error: 0.00, loss: 0.0
  Input: [0.00|1.00], output: [0.00], expectation: [1.00], error: 1.00, loss: 1.0
  Input: [1.00|0.00], output: [0.00], expectation: [1.00], error: 1.00, loss: 1.0
  Average error: 0.50, average loss: 0.50, success: false

00100 ------------------------| 100%
00200 ------------------------| 100%
00300 ------------------------| 100%
00400 ------------------------| 100%
00500 ------------------------| 100%
00600 ------------------------| 100%
00700 ------------------------| 100%
00800 ------------------------| 100%
00900 ------------------------| 100%
01000 ------------------------| 100%
01100 ------------------------| 100%
01200 ------------------------| 100%
01300 ------------------------| 100%
01400 ------------------------| 100%
01500 ------------------------| 100%
01600 ------------------------| 100%
01700 ------------------------| 100%
01800 ------------------------| 100%
01900 ------------------------| 100%
02000 ------------------------| 100%
02100 ------------------------| 100%
02200 ------------------------| 100%
02300 ------------------------| 100%
02400 ------------------------| 100%
02500 ------------------------| 100%
02600 ------------------------| 100%
02700 ------------------------| 100%
02800 ------------------------| 100%
02900 ------------------------| 100%
03000 ------------------------| 100%
03100 ------------------------| 100%
03200 ------------------------| 100%
03300 ------------------------| 100%
03400 ------------------------| 100%
03500 ------------------------| 100%
03600 ------------------------| 100%
03700 ------------------------| 100%
03800 ------------------------| 100%
03900 ------------------------| 100%
04000 ------------------------| 100%
04100 ------------------------| 100%
04200 ------------------------| 100%
04300 ------------------------| 100%
04400 ------------------------| 100%
04500 ------------------------| 100%
04600 ------------------------| 100%
04700 ------------------------| 100%
04800 ------------------------| 100%
04900 ------------------------| 100%
05000 ------------------------| 100%
05100 ------------------------| 100%
05200 ------------------------| 100%
05300 ------------------------| 100%
05400 ------------------------| 100%
05500 ------------------------| 100%
05600 ------------------------| 100%
05700 ------------------------| 100%
05800 ------------------------| 100%
05900 ------------------------| 100%
06000 ------------------------| 100%
06100 ------------------------| 100%
06200 ------------------------| 100%
06300 ------------------------| 100%
06400 ------------------------| 100%
06500 ------------------------| 100%
06600 ------------------------| 100%
06700 ------------------------| 100%
06800 ------------------------| 100%
06900 ------------------------| 100%
07000 ------------------------| 100%
07100 ------------------------| 100%
07200 ------------------------| 100%
07300 ------------------------| 100%
07400 ------------------------| 100%
07500 ------------------------| 100%
07600 ------------------------| 100%
07700 ------------------------| 100%
07800 ------------------------| 100%
07900 ------------------------| 100%
08000 ------------------------| 100%
08100 ------------------------| 100%
08200 ------------------------| 100%
08300 ------------------------| 100%
08400 ------------------------| 100%
08500 ------------------------| 100%
08600 ------------------------| 100%
08700 ------------------------| 100%
08800 ------------------------| 100%
08900 ------------------------| 100%
09000 ------------------------| 100%
09100 ------------------------| 100%
09200 ------------------------| 100%
09300 ------------------------| 100%
09400 ------------------------| 100%
09500 ------------------------| 100%
09600 ------------------------| 100%
09700 ------------------------| 100%
09800 ------------------------| 100%
09900 ------------------------| 100%
10000 ------------------------| 100%

Latest test results;
  Input: [1.00|1.00], output: [0.00], expectation: [0.00], error: 0.00, loss: 0.0
  Input: [1.00|0.00], output: [0.00], expectation: [1.00], error: 1.00, loss: 1.0
  Input: [0.00|1.00], output: [0.00], expectation: [1.00], error: 1.00, loss: 1.0
  Input: [0.00|0.00], output: [0.00], expectation: [0.00], error: 0.00, loss: 0.0
  Average error: 0.50, average loss: 0.50, success: false
Trained epochs: 10000, total average error: 5000.0, total average loss: 5000.0
================================================================================
Neural net activator: nl.zeesoft.zdk.functions.ZReLU, output activator: nl.zeesoft.zdk.functions.ZReLU, learning rate: 0.0858
Initial test results;
  Input: [0.00|0.00], output: [0.00], expectation: [0.00], error: 0.00, loss: 0.0
  Input: [1.00|1.00], output: [0.00], expectation: [0.00], error: 0.00, loss: 0.0
  Input: [0.00|1.00], output: [0.00], expectation: [1.00], error: 1.00, loss: 1.0
  Input: [1.00|0.00], output: [0.06], expectation: [1.00], error: 0.94, loss: 0.93702155
  Average error: 0.48, average loss: 0.48, success: false

00100 -------------------------\ 107%
00200 ----------------------------------------\ 165%
00300 ---------------------/ 90%
00400 ---/ 18%

Latest test results;
  Input: [1.00|1.00], output: [0.00], expectation: [0.00], error: 0.00, loss: 0.0
  Input: [1.00|0.00], output: [0.91], expectation: [1.00], error: 0.09, loss: 0.08971989
  Input: [0.00|0.00], output: [0.03], expectation: [0.00], error: -0.03, loss: 0.033923864
  Input: [0.00|1.00], output: [1.01], expectation: [1.00], error: -0.01, loss: 0.0096588135
  Average error: 0.03, average loss: 0.03, success: true
Trained epochs: 420, total average error: 208.9504, total average loss: 208.9504
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
2019-10-06 17:47:23:082 DBG nl.zeesoft.zdk.genetic.Evolver: Started
2019-10-06 17:47:23:306 DBG nl.zeesoft.zdk.genetic.Evolver: Selected new best genetic neural net;
- Code: 9249428880084296
- Size: 14
- Initial average loss: 0.48417 (final: 0.07853)
- Total average loss: 99.23747 (epochs: 226)
- Training result: 48.04776
2019-10-06 17:47:23:563 DBG nl.zeesoft.zdk.genetic.Evolver: Selected new best genetic neural net;
- Code: 9249428880084295
- Size: 14
- Initial average loss: 0.49651 (final: 0.06942)
- Total average loss: 64.59866 (epochs: 159)
- Training result: 32.07360
2019-10-06 17:47:28:669 DBG nl.zeesoft.zdk.genetic.Evolver: Selected new best genetic neural net;
- Code: 9249428880784295
- Size: 14
- Initial average loss: 0.46113 (final: 0.07095)
- Total average loss: 30.65082 (epochs: 89)
- Training result: 14.13404
2019-10-06 17:47:28:954 DBG nl.zeesoft.zdk.genetic.Evolver: Selected new best genetic neural net;
- Code: 9249428880784495
- Size: 14
- Initial average loss: 0.25000 (final: 0.00000)
- Total average loss: 1.50000 (epochs: 5)
- Training result: 0.37500
2019-10-06 17:47:29:030 DBG nl.zeesoft.zdk.genetic.Evolver: Selected new best genetic neural net;
- Code: 9249488880784495
- Size: 14
- Initial average loss: 0.25000 (final: 0.00000)
- Total average loss: 0.25000 (epochs: 2)
- Training result: 0.06250
2019-10-06 17:47:29:082 DBG nl.zeesoft.zdk.genetic.Evolver: Selected new best genetic neural net;
- Code: 9249488880784495
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
    "2019-10-06 17:47:23:306 SEL code: 9249428880084296, size: 14, initial loss: 0.48417 (final: 0.07853), total loss: 99.23747, result: 48.04776 (epochs: 226)",
    "2019-10-06 17:47:23:563 SEL code: 9249428880084295, size: 14, initial loss: 0.49651 (final: 0.06942), total loss: 64.59866, result: 32.07360 (epochs: 159)",
    "2019-10-06 17:47:28:669 SEL code: 9249428880784295, size: 14, initial loss: 0.46113 (final: 0.07095), total loss: 30.65082, result: 14.13404 (epochs: 89)",
    "2019-10-06 17:47:28:954 SEL code: 9249428880784495, size: 14, initial loss: 0.25000 (final: 0.00000), total loss: 1.50000, result: 0.37500 (epochs: 5)",
    "2019-10-06 17:47:29:030 SEL code: 9249488880784495, size: 14, initial loss: 0.25000 (final: 0.00000), total loss: 0.25000, result: 0.06250 (epochs: 2)",
    "2019-10-06 17:47:29:082 SEL code: 9249488880784495, size: 14, initial loss: 0.00000 (final: 0.00000), total loss: 0.00000, result: 0.00000 (epochs: 0)"
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
          "code": "OOcOhNNBxG3IVBvLnOSFrLHCeJLK6LrAPCoKmFvMREmCTOMM#OIO:JcCWAnGaAsDGDKG2",
          "neuralNet": [
            {
              "inputNeurons": 2,
              "hiddenLayers": 1,
              "hiddenNeurons": 2,
              "outputNeurons": 1,
              "weightFunction": "nl.zeesoft.zdk.functions.ZWeightDefault",
              "biasFunction": "nl.zeesoft.zdk.functions.ZWeightDefault",
              "activator": "nl.zeesoft.zdk.functions.ZTanH",
              "outputActivator": "nl.zeesoft.zdk.functions.ZSoftmaxTop",
              "learningRate": 0.1458,
              "values": [
                "2,1,1.0,1.0",
                "2,1,0.44142565,-0.6921129",
                "1,1,0.0"
              ],
              "weights": [
                "1,1,0.0",
                "2,2,-0.928,0.776,-0.994,0.806",
                "1,2,-0.956,0.806"
              ],
              "biases": [
                "1,1,0.0",
                "2,1,0.62600005,-0.66400003",
                "1,1,0.86800003"
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
                  "inputs": "0.0,0.0",
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
                  "inputs": "1.0,1.0",
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
                  "inputs": "0.0,0.0",
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
                  "inputs": "1.0,1.0",
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
SDR map: 100,2|17,98|55,13
Number of SDR A matches in SDR map: 1
SDR C: 100,33,95
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
SDR for value 0:  00000000100101000000000000000100000000000000000000
SDR for value 1:  00000000100100000000000000000100000000000100000000
SDR for value 24: 00001100010000000000000000000000000000000000000100
SDR for value 25: 00001100010000000000000000000000000000000001000000
SDR for value 75: 00000100000100000000000010000010000000000000000000
SDR for value 76: 00000100000100000000000000000010000000100000000000
SDR for value -1: 00010000100001000000000000000100000000000000000000
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
SDR for 2019-01-01 00:00:00:000, value1: 0, value2: 0; 11111100000000000000000000000000000000000000001111111110000000000000000000000000000000000000000101000011000000100000000000100010100001000000100000000110010000100000100000010001000000111111110000000000
SDR for 2019-02-02 01:00:00:000, value1: 1, value2: 2; 11111111000000000000000000000000000000000000000000001111111100000000000000000000000000000000000001100010000000100000000000100010100001001000100000001010010000000000100000010001111100000000000000001111
SDR for 2019-03-03 02:00:00:000, value1: 2, value2: 4; 00111111110000000000000000000000000000000000000000000000111111110000000000000000000000000000000001100010000000100000000010100000100001001010100000001000000000000000100100010001111111110000000000000000
SDR for 2019-04-04 03:00:00:000, value1: 3, value2: 6; 00111111110000000000000000000000000000000000000000000000000011111111000000000000000000000000000001100010000000100000000001100000100001001010100010001000000000000000000100010001000000000000011111111000
SDR for 2019-05-05 04:00:00:000, value1: 4, value2: 8; 00001111111100000000000000000000000000000000000000000000000000001111111100000000000000000000000001100010000000100010000001100000100000001010000010001000001001000000000100000001111111110000000000000000

Encoder StringBuilder:
VALUE1=0.0,32,1,37,6,7,26,30,14;1.0,32,1,2,37,6,26,30,14;2.0,32,1,2,37,6,24,26,14;3.0,32,1,2,37,6,25,26,14;4.0,32,1,2,18,6,25,26,14|VALUE2=0.0,17,35,4,22,39,28,13,14;1.0,0,17,35,4,22,39,28,14;2.0,0,17,35,4,39,28,12,14;3.0,0,35,4,39,28,12,14,31;4.0,0,2,35,4,39,28,12,31;5.0,0,2,35,4,39,11,12,31;6.0,0,2,35,4,39,8,12,31;7.0,0,2,18,4,39,8,12,31;8.0,0,2,18,21,39,8,12,31
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
Initializing pooler took: 17 ms
Randomizing connections took: 67 ms

Pooler input dimensions: 16x16, output dimensions: 32x32
- Average proximal inputs per column: 90
- Column groups: 144, average columns per group: 441

Processing input SDR map (learning: false) ...
Processing input SDR map took: 8443 ms

Performance statistics;
calculateOverlapScores:       1792.074 ms
selectActiveColumns:           967.614 ms
logActivity:                  1196.124 ms
calculateColumnGroupActivity: 2963.468 ms
updateBoostFactors:           1271.496 ms
total:                        8252.693 ms
logSize:                         15330   
avgPerLog:                       0.538 ms

Combined average: 0.35673797, Combined weekly average: 5.701158

Processing input SDR map (learning: true) ...
Processing input SDR map took: 11123 ms

Performance statistics;
calculateOverlapScores:        2506.502 ms
selectActiveColumns:            963.551 ms
learnActiveColumnsOnBits:      2413.979 ms
logActivity:                    899.617 ms
calculateColumnGroupActivity:  2835.869 ms
updateBoostFactors:            1228.878 ms
total:                        10934.268 ms
logSize:                          15330   
avgPerLog:                        0.713 ms

Combined average: 0.30563408, Combined weekly average: 8.153758

Original ratio: 15.98136, learned ratio: 26.678171
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
Processed SDRs: 500, bursting average: 4 (max: 11)
Processed SDRs: 1000, bursting average: 2 (max: 7)
Processed SDRs: 1500, bursting average: 1 (max: 7)
Processed SDRs: 2000, bursting average: 0 (max: 3)
Processed SDRs: 2500, bursting average: 1 (max: 5)
Processed SDRs: 3000, bursting average: 1 (max: 6)
Processed SDRs: 3500, bursting average: 0 (max: 3)
Processed SDRs: 4000, bursting average: 1 (max: 5)
Processed SDRs: 4500, bursting average: 0 (max: 4)
Processed SDRs: 5000, bursting average: 0 (max: 4)
Processing input SDR map took: 13071 ms

Performance statistics;
cycleActiveState:       200.014 ms
activateColumnCells:    194.114 ms
calculateActivity:      908.292 ms
selectPredictiveCells:  421.672 ms
updatePredictions:     3650.672 ms
total:                 5385.933 ms
logSize:                   5000   
avgPerLog:                1.077 ms

Average distal inputs per memory cell: 56 (min: 0, max: 105)
Average connected distal inputs per memory cell: 53 (min: 0, max: 105)
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
 * How scaling changes the output length and bits of the SDRs the encoder will generate.
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
It uses a *StreamFactory* to create a *DefaultStream* and then uses that to create an *AnomalyDetector*.
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
Processed SDRs: 500, average accuracy: 0.438, latest: 0.952
Processed SDRs: 1000, average accuracy: 0.626, latest: 1.000
Processed SDRs: 1500, average accuracy: 0.850, latest: 1.000
Processed SDRs: 2000, average accuracy: 0.899, latest: 0.905
Processed SDRs: 2500, average accuracy: 0.927, latest: 0.810
Processed SDRs: 3000, average accuracy: 0.947, latest: 0.952
Processed SDRs: 3500, average accuracy: 0.957, latest: 1.000
Processed SDRs: 4000, average accuracy: 0.967, latest: 0.952
Processed SDRs: 4500, average accuracy: 0.971, latest: 0.952
Processed SDRs: 5000, average accuracy: 0.975, latest: 1.000
Processed SDRs: 5500, average accuracy: 0.977, latest: 0.952
Processed SDRs: 6000, average accuracy: 0.977, latest: 1.000
Processed SDRs: 6500, average accuracy: 0.978, latest: 1.000
Processed SDRs: 7000, average accuracy: 0.978, latest: 1.000
Processed SDRs: 7500, average accuracy: 0.979, latest: 1.000
Detected anomaly at: 7666, average accuracy: 0.9790976, latest: 0.14285713, difference: 0.7453425
Stopped stream after 15256 ms

DefaultStream;
total:      53622648.000 ms
logSize:            7673   
avgPerLog:      6988.486 ms

Pooler;
calculateOverlapScores:        5171.839 ms
selectActiveColumns:            895.851 ms
learnActiveColumnsOnBits:      2291.027 ms
logActivity:                   1247.448 ms
calculateColumnGroupActivity:  4386.075 ms
updateBoostFactors:            1113.022 ms
total:                        15161.901 ms
logSize:                          12730   
avgPerLog:                        1.191 ms

Memory;
cycleActiveState:        475.102 ms
activateColumnCells:     305.744 ms
calculateActivity:      1308.008 ms
selectPredictiveCells:   867.240 ms
updatePredictions:     10848.721 ms
total:                 13820.984 ms
logSize:                    7673   
avgPerLog:                 1.801 ms

Total processing time per SDR: 2.992 ms
Total stream time per SDR:     1.988 ms
~~~~

nl.zeesoft.zdk.test.impl.htm.TestValueClassifier
------------------------------------------------
This test shows how to use a *ClassificationStream* and a *ValueClassifier* to classify and/or predict values.
It uses a *StreamFactory* to create a *ClassificationStream* and then uses that to create a *ValueClassifer*.
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
 * A trimmed JSON export of stream encoder and processor state information  
~~~~
StreamEncoder length: 256, bits: 32
Pooler input dimensions: 16x16, output dimensions: 32x32
Memory dimensions: 32x32x4
Classifier prediction steps: 1

Started stream
Processed SDRs: 500, accuracy: 0.311
Processed SDRs: 1000, accuracy: 0.528
Processed SDRs: 1500, accuracy: 0.761
Processed SDRs: 2000, accuracy: 0.814
Processed SDRs: 2500, accuracy: 0.891
Processed SDRs: 3000, accuracy: 0.926
Processed SDRs: 3500, accuracy: 0.930
Processed SDRs: 4000, accuracy: 0.928
Processed SDRs: 4500, accuracy: 0.914
Processed SDRs: 5000, accuracy: 0.934
Stopped stream after 8842 ms

ClassificationStream;
total:      21720920.000 ms
logSize:            5006   
avgPerLog:      4338.977 ms

Pooler;
calculateOverlapScores:       3865.427 ms
selectActiveColumns:           403.502 ms
learnActiveColumnsOnBits:     1273.698 ms
logActivity:                   562.399 ms
calculateColumnGroupActivity: 2097.631 ms
updateBoostFactors:            524.057 ms
total:                        8757.896 ms
logSize:                          6022   
avgPerLog:                       1.454 ms

Memory;
cycleActiveState:       308.464 ms
activateColumnCells:    201.286 ms
calculateActivity:      809.547 ms
selectPredictiveCells:  509.216 ms
updatePredictions:     5052.402 ms
total:                 6890.663 ms
logSize:                   5007   
avgPerLog:                1.376 ms

Classifier;
generateClassifications: 644.670 ms
total:                   652.057 ms
logSize:                    5006   
avgPerLog:                 0.130 ms

Total processing time per SDR: 2.961 ms
Total stream time per SDR:     1.766 ms

Stream state JSON;
{
  "streamClassName": "nl.zeesoft.zdk.htm.stream.ClassificationStream",
  "encoderClassName": "nl.zeesoft.zdk.htm.stream.StreamEncoder",
  "encoderData": "",
  "uid": "15330",
  "processors": [
    {
      "processorClassName": "nl.zeesoft.zdk.htm.proc.Pooler",
      "processorData": "0.0,36;0.0,169;0.0,128;0.992,113;0.0,97;0.0,120;1.0,148;0.0,166;0.0,98;0.0,163;0 ..."
    },
    {
      "processorClassName": "nl.zeesoft.zdk.htm.proc.Memory",
      "processorData": "0.43872043,0-0-2;0.41301697,1-0-1;0.34989405,24-0-0;0.35397014,3-1-1;0.36996734, ..."
    },
    {
      "processorClassName": "nl.zeesoft.zdk.htm.proc.Classifier",
      "processorData": "1#0;Float;14.0,1;10.0,4;9.0,4;5.0,1;32.0,1;15.0,1;38.0,1;29.0,1;27.0,1;34.0,1;39 ..."
    }
  ]
}
~~~~

Test results
------------
All 22 tests have been executed successfully (149 assertions).  
Total test duration: 75267 ms (total sleep duration: 30500 ms).  

Memory usage per test;  
 * nl.zeesoft.zdk.test.impl.TestZStringEncoder: 766 Kb / 0 Mb
 * nl.zeesoft.zdk.test.impl.TestZStringSymbolParser: 526 Kb / 0 Mb
 * nl.zeesoft.zdk.test.impl.TestCsv: 533 Kb / 0 Mb
 * nl.zeesoft.zdk.test.impl.TestJson: 547 Kb / 0 Mb
 * nl.zeesoft.zdk.test.impl.TestZHttpRequest: 554 Kb / 0 Mb
 * nl.zeesoft.zdk.test.impl.TestMessenger: 756 Kb / 0 Mb
 * nl.zeesoft.zdk.test.impl.TestZMatrix: 830 Kb / 0 Mb
 * nl.zeesoft.zdk.test.impl.TestGeneticCode: 790 Kb / 0 Mb
 * nl.zeesoft.zdk.test.impl.TestNeuralNet: 3720 Kb / 3 Mb
 * nl.zeesoft.zdk.test.impl.TestGeneticNN: 11973 Kb / 11 Mb
 * nl.zeesoft.zdk.test.impl.TestEvolver: 18633 Kb / 18 Mb
 * nl.zeesoft.zdk.test.impl.htm.TestSDR: 21510 Kb / 21 Mb
 * nl.zeesoft.zdk.test.impl.htm.TestSDRMap: 19149 Kb / 18 Mb
 * nl.zeesoft.zdk.test.impl.htm.TestScalarEncoder: 10016 Kb / 9 Mb
 * nl.zeesoft.zdk.test.impl.htm.TestRDScalarEncoder: 956 Kb / 0 Mb
 * nl.zeesoft.zdk.test.impl.htm.TestDateTimeEncoder: 962 Kb / 0 Mb
 * nl.zeesoft.zdk.test.impl.htm.TestDateTimeValuesEncoder: 959 Kb / 0 Mb
 * nl.zeesoft.zdk.test.impl.htm.TestPooler: 34983 Kb / 34 Mb
 * nl.zeesoft.zdk.test.impl.htm.TestMemory: 34936 Kb / 34 Mb
 * nl.zeesoft.zdk.test.impl.htm.TestStreamEncoder: 34938 Kb / 34 Mb
 * nl.zeesoft.zdk.test.impl.htm.TestAnomalyDetector: 79458 Kb / 77 Mb
 * nl.zeesoft.zdk.test.impl.htm.TestValueClassifier: 87547 Kb / 85 Mb
