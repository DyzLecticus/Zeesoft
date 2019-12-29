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
Key: 5830536036153008784265282413980485466741055927366004892449573208
Input text: Hello, my name is 'Dyz Lecticus'. How are you feeling today? :-) (Don't you know how to: [re;spond]!).

Key encoded text: #4U6Y331N4r274VZA3c4P1z4X2~Z9Zm7L6W5Q242Q5U4C1v6o2E3O1D338X681j3g6E3E3N5Z6a5B3y1WZK3p5N7R295n3A505EZXZr4r7:6C294k4O7f5:5V1u1IYEYW3Y6n261M4m2p5JYA3q5W1z3V2IZm1#7w547X3P2v4O4Q2d6k1z3M1~3r7V651q4f7H4Z4A4M4o50
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
2019-12-29 15:25:31:439 DBG nl.zeesoft.zdk.test.impl.TestMessengerListener: Test log debug message before Messenger has started
2019-12-29 15:25:31:739 ERR nl.zeesoft.zdk.test.impl.TestMessengerListener: Test log error message while Messenger is working
2019-12-29 15:25:31:740 ERR nl.zeesoft.zdk.test.impl.TestMessengerListener: Test log exception stack trace
java.lang.NumberFormatException: For input string: "A"
	at java.lang.NumberFormatException.forInputString(NumberFormatException.java:65)
	at java.lang.Integer.parseInt(Integer.java:580)
	at java.lang.Integer.parseInt(Integer.java:615)
	at nl.zeesoft.zdk.test.impl.TestMessenger.test(TestMessenger.java:83)
	at nl.zeesoft.zdk.test.Tester.test(Tester.java:71)
	at nl.zeesoft.zdk.test.LibraryObject.describeAndTest(LibraryObject.java:39)
	at nl.zeesoft.zdk.test.impl.ZDK.main(ZDK.java:52)

2019-12-29 15:25:32:051 WRN nl.zeesoft.zdk.test.impl.TestMessengerListener: Test log warning message after Messenger has stopped
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
000.63 | -00.02 | 000.15
-------+--------+-------
-00.79 | -00.73 | 000.02

Randomized multiplied element wise;
018.93 | -00.57 | 004.38
-------+--------+-------
-23.69 | -21.99 | 000.47

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
000.42 | -00.76 | 000.35
-------+--------+-------
-00.88 | -00.16 | -00.55

Randomized matrix transposed;
000.42 | -00.88
-------+-------
-00.76 | -00.16
-------+-------
000.35 | -00.55
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
Genetic code: 4364834432544115795869968672884594331291221640380858964488877254201244503747528615669629883712722920
Mutated code: 4764834432544115795869968672884594331297221640380852964488877258201244503747528615669629883712722020
               ^                                     ^           ^           ^                                 ^  

Scaled property values;
0: 156 <
1: 488
2: 852 <
3: 594
4: 852 <
5: 820 <
6: 747
7: 115
8: 764 <
9: 403
10: 820 <
11: 820 <
12: 288
13: 722 <
14: 943
15: 968
16: 943
17: 296 <
18: 445
19: 795
20: 476 <
21: 254 <
22: 331
23: 374
24: 258 <
25: 344
26: 579
27: 962
28: 594
29: 459
30: 752
31: 772
32: 448 <
Mutated property values: 13
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
  Input: [1.00|1.00], output: [0.06], expectation: [0.00], error: -0.06, loss: 0.06181397
  Input: [0.00|1.00], output: [-0.00], expectation: [1.00], error: 1.00, loss: 1.0001428
  Input: [1.00|0.00], output: [0.00], expectation: [1.00], error: 1.00, loss: 0.9969296
  Average error: 0.51, average loss: 0.51, success: false
Latest test results;
  Input: [1.00|1.00], output: [-0.00], expectation: [0.00], error: 0.00, loss: 3.3855064E-5
  Input: [0.00|0.00], output: [0.10], expectation: [0.00], error: -0.10, loss: 0.09856344
  Input: [0.00|1.00], output: [0.94], expectation: [1.00], error: 0.06, loss: 0.06323278
  Input: [1.00|0.00], output: [0.95], expectation: [1.00], error: 0.05, loss: 0.05289644
  Average error: 0.05, average loss: 0.05, success: true
Trained epochs: 9141, total average error: 2729.6636, total average loss: 2729.6636
================================================================================
Neural net activator: nl.zeesoft.zdk.functions.ZLeakyReLU, output activator: nl.zeesoft.zdk.functions.ZSoftmaxTop, learning rate: 0.1
Initial test results;
  Input: [0.00|0.00], output: [1.00], expectation: [0.00], error: -1.00, loss: 1.0
  Input: [1.00|1.00], output: [1.00], expectation: [0.00], error: -1.00, loss: 1.0
  Input: [0.00|1.00], output: [1.00], expectation: [1.00], error: 0.00, loss: 0.0
  Input: [1.00|0.00], output: [1.00], expectation: [1.00], error: 0.00, loss: 0.0
  Average error: 0.50, average loss: 0.50, success: false
Latest test results;
  Input: [1.00|1.00], output: [0.00], expectation: [0.00], error: 0.00, loss: 0.0
  Input: [1.00|0.00], output: [1.00], expectation: [1.00], error: 0.00, loss: 0.0
  Input: [0.00|0.00], output: [0.00], expectation: [0.00], error: 0.00, loss: 0.0
  Input: [0.00|1.00], output: [1.00], expectation: [1.00], error: 0.00, loss: 0.0
  Average error: 0.00, average loss: 0.00, success: true
Trained epochs: 2556, total average error: 1573.75, total average loss: 1573.75

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
    "2,1,0.7695092,0.0036942959",
    "1,1,1.0"
  ],
  "weights": [
    "1,1,0.0",
    "2,2,-0.092187464,-0.10283512,2.77069,2.7713196",
    "1,2,-0.78332007,-0.042211056"
  ],
  "biases": [
    "1,1,0.0",
    "2,1,0.8723443,-2.7676253",
    "1,1,0.64761233"
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
Neural net activator: nl.zeesoft.zdk.functions.ZReLU, output activator: nl.zeesoft.zdk.functions.ZSigmoid, learning rate: 0.1992
Initial test results;
  Input: [0.00|0.00], output: [0.28], expectation: [0.00], error: -0.28, loss: 0.28414357
  Input: [1.00|1.00], output: [0.28], expectation: [0.00], error: -0.28, loss: 0.28414357
  Input: [0.00|1.00], output: [0.28], expectation: [1.00], error: 0.72, loss: 0.71585643
  Input: [1.00|0.00], output: [0.34], expectation: [1.00], error: 0.66, loss: 0.66196716
  Average error: 0.49, average loss: 0.49, success: false

00100 -----------------/ 72%
00200 ----------------/ 70%
00300 ----------------/ 69%
00400 ----------------| 69%
00500 ----------------| 69%
00600 ----------------| 69%
00700 ----------------| 69%
00800 ----------------| 69%
00900 ----------------| 69%
01000 ----------------/ 68%
01100 ----------------| 68%
01200 ----------------\ 69%
01300 ----------------/ 68%
01400 ----------------| 68%
01500 ----------------\ 69%
01600 ----------------| 69%
01700 ----------------/ 68%
01800 ----------------| 68%
01900 ----------------| 68%
02000 ----------------| 68%
02100 ----------------| 68%
02200 ----------------| 68%
02300 ----------------| 68%
02400 ----------------| 68%
02500 ----------------| 68%
02600 ----------------| 68%
02700 ----------------| 68%
02800 ----------------| 68%
02900 ----------------| 68%
03000 ----------------| 68%
03100 ----------------| 68%
03200 ----------------| 68%
03300 ----------------| 68%
03400 ----------------| 68%
03500 ----------------| 68%
03600 ----------------| 68%
03700 ----------------| 68%
03800 ----------------| 68%
03900 ----------------| 68%
04000 ----------------| 68%
04100 ----------------| 68%
04200 ----------------| 68%
04300 ----------------| 68%
04400 ----------------| 68%
04500 ----------------| 68%
04600 ----------------| 68%
04700 ----------------| 68%
04800 ----------------| 68%
04900 ----------------| 68%
05000 ----------------| 68%
05100 ----------------| 68%
05200 ----------------| 68%
05300 ----------------\ 69%
05400 ----------------/ 68%
05500 ----------------| 68%
05600 ----------------| 68%
05700 ----------------| 68%
05800 ----------------| 68%
05900 ----------------| 68%
06000 ----------------| 68%
06100 ----------------| 68%
06200 ----------------\ 69%
06300 ----------------| 69%
06400 ----------------/ 68%
06500 ----------------| 68%
06600 ----------------| 68%
06700 ----------------| 68%
06800 ----------------| 68%
06900 ----------------| 68%
07000 ----------------| 68%
07100 ----------------| 68%
07200 ----------------| 68%
07300 ----------------| 68%
07400 ----------------| 68%
07500 ----------------| 68%
07600 ----------------| 68%
07700 ----------------| 68%
07800 ----------------| 68%
07900 ----------------| 68%
08000 ----------------| 68%
08100 ----------------| 68%
08200 ----------------| 68%
08300 ----------------| 68%
08400 ----------------| 68%
08500 ----------------| 68%
08600 ----------------| 68%
08700 ----------------| 68%
08800 ----------------| 68%
08900 ----------------| 68%
09000 ----------------\ 69%
09100 ----------------/ 68%
09200 ----------------| 68%
09300 ----------------| 68%
09400 ----------------| 68%
09500 ----------------| 68%
09600 ----------------| 68%
09700 ----------------| 68%
09800 ----------------| 68%
09900 ----------------\ 69%
10000 ----------------/ 68%

Latest test results;
  Input: [0.00|1.00], output: [0.33], expectation: [1.00], error: 0.67, loss: 0.6676099
  Input: [1.00|1.00], output: [0.34], expectation: [0.00], error: -0.34, loss: 0.33897087
  Input: [0.00|0.00], output: [0.34], expectation: [0.00], error: -0.34, loss: 0.33558905
  Input: [1.00|0.00], output: [1.00], expectation: [1.00], error: 0.00, loss: 4.490018E-4
  Average error: 0.34, average loss: 0.34, success: false
Trained epochs: 10000, total average error: 3356.719, total average loss: 3356.719
================================================================================
Neural net activator: nl.zeesoft.zdk.functions.ZSigmoid, output activator: nl.zeesoft.zdk.functions.ZReLU, learning rate: 0.057
Initial test results;
  Input: [0.00|0.00], output: [0.00], expectation: [0.00], error: 0.00, loss: 0.0
  Input: [1.00|1.00], output: [0.00], expectation: [0.00], error: 0.00, loss: 0.0
  Input: [0.00|1.00], output: [0.00], expectation: [1.00], error: 1.00, loss: 1.0
  Input: [1.00|0.00], output: [0.00], expectation: [1.00], error: 1.00, loss: 1.0
  Average error: 0.50, average loss: 0.50, success: false

00100 -------------------------\ 104%
00200 -------------------------| 104%
00300 ------------------------/ 103%
00400 -------------------------\ 104%
00500 -------------------------| 104%
00600 -------------------------| 104%
00700 -------------------------| 104%
00800 -------------------------| 104%
00900 -------------------------| 104%
01000 -------------------------| 104%
01100 ------------------------/ 103%
01200 -------------------------\ 104%
01300 -------------------------| 104%
01400 -------------------------| 104%
01500 -------------------------| 104%
01600 -------------------------| 104%
01700 -------------------------| 104%
01800 ------------------------/ 103%
01900 -------------------------\ 104%
02000 -------------------------| 104%
02100 ------------------------/ 103%
02200 ------------------------| 103%
02300 -----------------------/ 99%
02400 ------------------------\ 101%
02500 -----------------------/ 99%
02600 ----------------------/ 95%
02700 ---------------------/ 91%
02800 --------------------/ 86%
02900 -------------------/ 82%
03000 ------------------/ 78%
03100 -----------------/ 74%
03200 ----------------/ 71%
03300 ----------------/ 68%
03400 --------------/ 62%
03500 ---------------\ 64%
03600 ---------------| 64%
03700 --------------/ 62%
03800 --------------| 62%
03900 -------------/ 59%
04000 -------------| 59%
04100 --------------\ 63%
04200 -------------/ 59%
04300 --------------\ 61%
04400 -------------/ 58%
04500 -------------/ 57%
04600 -------------\ 58%
04700 -------------/ 57%
04800 -------------| 57%
04900 -------------/ 56%
05000 -------------| 56%
05100 -------------| 56%
05200 -------------\ 59%
05300 -------------/ 58%
05400 -------------/ 57%
05500 -------------\ 58%
05600 -------------/ 56%
05700 -------------| 56%
05800 -------------\ 58%
05900 --------------\ 60%
06000 -------------/ 58%
06100 -------------/ 56%
06200 -------------\ 58%
06300 -------------| 58%
06400 ------------/ 55%
06500 -------------\ 58%
06600 -------------| 58%
06700 ------------/ 55%
06800 -------------\ 58%
06900 -------------/ 57%
07000 -------------| 57%
07100 -------------\ 58%
07200 -------------/ 56%
07300 -------------\ 58%
07400 ------------/ 55%
07500 -------------\ 57%
07600 -------------\ 59%
07700 -------------/ 58%
07800 -------------/ 57%
07900 -------------| 57%
08000 ------------/ 55%
08100 -------------\ 59%
08200 -------------/ 57%
08300 -------------\ 58%
08400 -------------/ 57%
08500 -------------/ 56%
08600 -------------| 56%
08700 ------------/ 54%
08800 -------------\ 58%
08900 ------------/ 55%
09000 -------------\ 56%
09100 ------------/ 55%
09200 ------------| 55%
09300 -------------\ 58%
09400 -------------/ 57%
09500 -------------\ 59%
09600 -------------/ 57%
09700 -------------/ 56%
09800 -------------\ 57%
09900 --------------\ 60%
10000 -------------/ 57%

Latest test results;
  Input: [1.00|1.00], output: [0.55], expectation: [0.00], error: -0.55, loss: 0.55303276
  Input: [0.00|1.00], output: [0.45], expectation: [1.00], error: 0.55, loss: 0.5498462
  Input: [1.00|0.00], output: [1.04], expectation: [1.00], error: -0.04, loss: 0.035036683
  Input: [0.00|0.00], output: [0.01], expectation: [0.00], error: -0.01, loss: 0.009394228
  Average error: 0.29, average loss: 0.29, success: false
Trained epochs: 10000, total average error: 3582.0789, total average loss: 3582.0789
================================================================================
Neural net activator: nl.zeesoft.zdk.functions.ZReLU, output activator: nl.zeesoft.zdk.functions.ZLeakyReLU, learning rate: 0.12560001
Initial test results;
  Input: [0.00|0.00], output: [0.00], expectation: [0.00], error: 0.00, loss: 0.0
  Input: [1.00|1.00], output: [0.26], expectation: [0.00], error: -0.26, loss: 0.25811732
  Input: [0.00|1.00], output: [0.00], expectation: [1.00], error: 1.00, loss: 1.0
  Input: [1.00|0.00], output: [0.21], expectation: [1.00], error: 0.79, loss: 0.7888427
  Average error: 0.51, average loss: 0.51, success: false
Latest test results;
  Input: [1.00|1.00], output: [0.04], expectation: [0.00], error: -0.04, loss: 0.039714754
  Input: [1.00|0.00], output: [1.01], expectation: [1.00], error: -0.01, loss: 0.007480502
  Input: [0.00|1.00], output: [0.90], expectation: [1.00], error: 0.10, loss: 0.09658772
  Input: [0.00|0.00], output: [0.07], expectation: [0.00], error: -0.07, loss: 0.07497662
  Average error: 0.05, average loss: 0.05, success: true
Trained epochs: 96, total average error: 24.877333, total average loss: 24.877333
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
2019-12-29 15:25:33:120 DBG nl.zeesoft.zdk.genetic.Evolver: Started
2019-12-29 15:25:33:456 DBG nl.zeesoft.zdk.genetic.Evolver: Selected new best genetic neural net;
- Code: 6973866451036884
- Size: 14
- Initial average loss: 0.50051 (final: 0.07216)
- Total average loss: 146.39893 (epochs: 339)
- Training result: 73.27374
2019-12-29 15:25:34:767 DBG nl.zeesoft.zdk.genetic.Evolver: Selected new best genetic neural net;
- Code: 8989876836588739
- Size: 14
- Initial average loss: 0.49681 (final: 0.05554)
- Total average loss: 18.35313 (epochs: 64)
- Training result: 9.11803
2019-12-29 15:25:36:226 DBG nl.zeesoft.zdk.genetic.Evolver: Selected new best genetic neural net;
- Code: 8989876836588739
- Size: 14
- Initial average loss: 0.50000 (final: 0.00000)
- Total average loss: 4.75000 (epochs: 15)
- Training result: 2.37500
2019-12-29 15:25:36:278 DBG nl.zeesoft.zdk.genetic.Evolver: Selected new best genetic neural net;
- Code: 8989878836588739
- Size: 14
- Initial average loss: 0.50000 (final: 0.00000)
- Total average loss: 2.25000 (epochs: 5)
- Training result: 1.12500
2019-12-29 15:25:36:520 DBG nl.zeesoft.zdk.genetic.Evolver: Selected new best genetic neural net;
- Code: 8989878836588739
- Size: 14
- Initial average loss: 0.50000 (final: 0.00000)
- Total average loss: 1.50000 (epochs: 4)
- Training result: 0.75000
2019-12-29 15:25:38:492 DBG nl.zeesoft.zdk.genetic.Evolver: Selected new best genetic neural net;
- Code: 8989878836188739
- Size: 14
- Initial average loss: 0.25000 (final: 0.00000)
- Total average loss: 0.25000 (epochs: 2)
- Training result: 0.06250
2019-12-29 15:25:39:367 DBG nl.zeesoft.zdk.genetic.Evolver: Selected new best genetic neural net;
- Code: 8989878836188539
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
    "2019-12-29 15:25:33:456 SEL code: 6973866451036884, size: 14, initial loss: 0.50051 (final: 0.07216), total loss: 146.39893, result: 73.27374 (epochs: 339)",
    "2019-12-29 15:25:34:767 SEL code: 8989876836588739, size: 14, initial loss: 0.49681 (final: 0.05554), total loss: 18.35313, result: 9.11803 (epochs: 64)",
    "2019-12-29 15:25:36:226 SEL code: 8989876836588739, size: 14, initial loss: 0.50000 (final: 0.00000), total loss: 4.75000, result: 2.37500 (epochs: 15)",
    "2019-12-29 15:25:36:278 SEL code: 8989878836588739, size: 14, initial loss: 0.50000 (final: 0.00000), total loss: 2.25000, result: 1.12500 (epochs: 5)",
    "2019-12-29 15:25:36:520 SEL code: 8989878836588739, size: 14, initial loss: 0.50000 (final: 0.00000), total loss: 1.50000, result: 0.75000 (epochs: 4)",
    "2019-12-29 15:25:38:492 SEL code: 8989878836188739, size: 14, initial loss: 0.25000 (final: 0.00000), total loss: 0.25000, result: 0.06250 (epochs: 2)",
    "2019-12-29 15:25:39:367 SEL code: 8989878836188539, size: 14, initial loss: 0.00000 (final: 0.00000), total loss: 0.00000, result: 0.00000 (epochs: 0)"
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
          "code": "rNMPcN8JINZOmAnEjJFO5ExJkDPCAMiIfDJPQOCLDI7CBPVN6DsO1JvLQOHHjDaFcKAA2",
          "neuralNet": [
            {
              "inputNeurons": 2,
              "hiddenLayers": 1,
              "hiddenNeurons": 2,
              "outputNeurons": 1,
              "weightFunction": "nl.zeesoft.zdk.functions.ZWeightXavier",
              "biasFunction": "nl.zeesoft.zdk.functions.ZWeightXavier",
              "activator": "nl.zeesoft.zdk.functions.ZLeakyReLU",
              "outputActivator": "nl.zeesoft.zdk.functions.ZSoftmaxTop",
              "learningRate": 0.187,
              "values": [
                "2,1,1.0,0.0",
                "2,1,1.15679,0.97713715",
                "1,1,1.0"
              ],
              "weights": [
                "1,1,0.0",
                "2,2,0.140217,-0.7076576,1.0713454,-0.9727553",
                "1,2,-0.8524224,0.9308061"
              ],
              "biases": [
                "1,1,0.0",
                "2,1,1.0165731,-0.09420831",
                "1,1,0.29883772"
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
                },
                {
                  "inputs": "1.0,0.0",
                  "outputs": "1.0",
                  "expectations": "1.0",
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
                },
                {
                  "inputs": "1.0,0.0",
                  "outputs": "1.0",
                  "expectations": "1.0",
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
SDR map: 100,2|85,29|0,33
Number of SDR A matches in SDR map: 1
SDR C: 100,64,94
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
SDR for value 0:  00001000000000000010000000000000010000000000100000
SDR for value 1:  00000000000100000010000000000000010000000000100000
SDR for value 24: 00000001000001000000000000000010001000000000000000
SDR for value 25: 00000001000000000000000000000010001000000100000000
SDR for value 75: 01000000000001000000000000000000000000000100000100
SDR for value 76: 01010000000000000000000000000000000000000100000100
SDR for value -1: 00001000000000100000000000000000010000000000100000
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
SDR for 2019-01-01 00:00:00:000, value1: 0, value2: 0; 11111100000000000000000000000000000000000000001111111110000000000000000000000000000000000000000110110000000000000100101000000100000010000000011100000010000001001010000010000000000000111111110000000000
SDR for 2019-02-02 01:00:00:000, value1: 1, value2: 2; 11111111000000000000000000000000000000000000000000001111111100000000000000000000000000000000000010100000000010000100101000000100000010000000011110000010000000000010000010100000111100000000000000001111
SDR for 2019-03-03 02:00:00:000, value1: 2, value2: 4; 00111111110000000000000000000000000000000000000000000000111111110000000000000000000000000000000011100000000010000000101000000100000010000000011010000010100000010000000010100000111111110000000000000000
SDR for 2019-04-04 03:00:00:000, value1: 3, value2: 6; 00111111110000000000000000000000000000000000000000000000000011111111000000000000000000000000000011100000000011000000101000000100000000000100011000000000100000010000000010101000000000000000011111111000
SDR for 2019-05-05 04:00:00:000, value1: 4, value2: 8; 00001111111100000000000000000000000000000000000000000000000000001111111100000000000000000000000011100000000011000000101000000010000000000100101000000000100000010000000010100010111111110000000000000000

Encoder StringBuilder:
VALUE1=0.0,0,17,2,3,20,36,22,29;1.0,0,17,2,20,36,22,12,29;2.0,0,1,2,20,36,22,12,29;3.0,0,1,2,20,22,12,29,13;4.0,0,1,2,20,22,12,13,30|VALUE2=0.0,32,21,5,6,7,24,26,14;1.0,32,34,21,5,6,7,26,14;2.0,32,34,5,6,7,8,26,14;3.0,32,34,5,6,7,23,8,14;4.0,32,16,34,5,6,23,8,14;5.0,32,16,1,34,5,6,23,8;6.0,32,16,1,34,36,5,6,23;7.0,32,16,1,34,36,4,6,23;8.0,32,16,1,34,4,6,38,23
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
Initializing pooler took: 23 ms
Randomizing connections took: 69 ms

Processing input SDR map (learning: false) ...
Processing input SDR map took: 14237 ms

Performance statistics;
calculateOverlapScores:        6580.272 ms
selectActiveColumns:            895.489 ms
logActivity:                    916.740 ms
calculateColumnGroupActivity:  4249.421 ms
updateBoostFactors:            1282.863 ms
total:                        14002.346 ms
logSize:                          15330   
avgPerLog:                        0.913 ms

Combined average: 0.3938923, Combined weekly average: 3.9976878

Pooler input dimensions: 16*16, output dimensions: 32*32
- Total proximal links: 92160, active: 46057
- Average proximal inputs per column: 90
- Column groups: 144, average columns per group: 441

Processing input SDR map (learning: true) ...
Processing input SDR map took: 12422 ms

Performance statistics;
calculateOverlapScores:        3229.364 ms
selectActiveColumns:           1003.481 ms
logActivity:                   1016.967 ms
calculateColumnGroupActivity:  2984.491 ms
updateBoostFactors:            1300.894 ms
total:                        12202.754 ms
learnActiveColumnsOnBits:      2597.821 ms
logSize:                          15330   
avgPerLog:                        0.796 ms

Combined average: 0.31766137, Combined weekly average: 6.3658957

Original ratio: 10.14919, learned ratio: 20.039879
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
Processed SDRs: 500, bursting average: 4 (max: 12)
Processed SDRs: 1000, bursting average: 2 (max: 6)
Processed SDRs: 1500, bursting average: 2 (max: 9)
Processed SDRs: 2000, bursting average: 0 (max: 4)
Processed SDRs: 2500, bursting average: 1 (max: 5)
Processed SDRs: 3000, bursting average: 1 (max: 6)
Processed SDRs: 3500, bursting average: 0 (max: 5)
Processed SDRs: 4000, bursting average: 1 (max: 5)
Processed SDRs: 4500, bursting average: 0 (max: 3)
Processed SDRs: 5000, bursting average: 0 (max: 2)
Processing input SDR map took: 14941 ms

Performance statistics;
cycleActiveState:       259.653 ms
activateColumnCells:    234.413 ms
calculateActivity:     1008.471 ms
selectPredictiveCells:  415.005 ms
updatePredictions:     4412.125 ms
total:                 6346.324 ms
logSize:                   5000   
avgPerLog:                1.269 ms

Memory dimensions: 32*32*4
- Total distal links: 232752, active: 221059
- Average distal inputs per memory cell: 56 (min: 0, max: 105)
- Average connected distal inputs per memory cell: 53 (min: 0, max: 105)
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
  - Total proximal links: 90304, active: 44976
  - Average proximal inputs per column: 88 (min: 82, max: 90)
  - Column groups: 144, average columns per group: 441
- Column 02-01 = Pooler input dimensions: 16*16, output dimensions: 32*32
  - Total proximal links: 92160, active: 46138
  - Average proximal inputs per column: 90
  - Column groups: 144, average columns per group: 441
- Column 02-03 = Pooler input dimensions: 14*14, output dimensions: 32*32
  - Total proximal links: 91776, active: 45761
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
2019-12-29 15:26:29:835 DBG nl.zeesoft.zdk.htm.grid.ZGrid: Starting grid ...
2019-12-29 15:26:29:837 DBG nl.zeesoft.zdk.htm.grid.ZGrid: Started grid
Processed requests: 100, accuracy: 0.490
Processed requests: 200, accuracy: 0.712
Processed requests: 300, accuracy: 0.806
Processed requests: 400, accuracy: 0.852
Processed requests: 500, accuracy: 0.880
Processed requests: 600, accuracy: 0.898
Processed requests: 700, accuracy: 0.913
Processed requests: 800, accuracy: 0.924
Processed requests: 900, accuracy: 0.932
Processed requests: 1000, accuracy: 0.939
Processed requests: 1100, accuracy: 0.987
Processed requests: 1200, accuracy: 0.996
Processed requests: 1300, accuracy: 0.997
Processed requests: 1400, accuracy: 0.998
Processed requests: 1500, accuracy: 0.999
Processed requests: 1600, accuracy: 1.000
Processed requests: 1700, accuracy: 1.000
Processed requests: 1800, accuracy: 1.000
Processed requests: 1900, accuracy: 1.000
Processed requests: 2000, accuracy: 1.000
Processed requests: 2100, accuracy: 1.000 (!)
Processed requests: 2200, accuracy: 1.000
Processed requests: 2300, accuracy: 1.000
Processed requests: 2400, accuracy: 1.000
Processed requests: 2500, accuracy: 1.000
Processed requests: 2600, accuracy: 0.999
Processed requests: 2700, accuracy: 0.999
Detected anomaly at id 2743, detected: 0.3333333, average: 0.9860956, difference: 0.49473095
Detected anomaly at id 2745, detected: 0.52380955, average: 0.9840956, difference: 0.30524862
Detected anomaly at id 2746, detected: 0.28571427, average: 0.9831432, difference: 0.54965115
Processed requests: 2800, accuracy: 0.959
Processed requests: 2900, accuracy: 0.958
Processed requests: 3000, accuracy: 0.958
2019-12-29 15:26:41:697 DBG nl.zeesoft.zdk.htm.grid.ZGrid: Stopping grid ...
Processing 3000 requests took 12059 ms
2019-12-29 15:26:41:871 DBG nl.zeesoft.zdk.htm.grid.ZGrid: Stopped grid

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
  - Total proximal links: 90304, active: 31441
  - Average proximal inputs per column: 88 (min: 82, max: 90)
  - Column groups: 144, average columns per group: 441
- Column 02-01 = Pooler input dimensions: 16*16, output dimensions: 32*32
  - Total proximal links: 92160, active: 34033
  - Average proximal inputs per column: 90
  - Column groups: 144, average columns per group: 441
- Column 02-03 = Pooler input dimensions: 14*14, output dimensions: 32*32
  - Total proximal links: 91776, active: 34801
  - Average proximal inputs per column: 89 (min: 87, max: 90)
  - Column groups: 144, average columns per group: 441
- Column 03-01 = Memory dimensions: 32*32*4
  - Total distal links: 196925, active: 182343
  - Average distal inputs per memory cell: 48 (min: 0, max: 126)
  - Average connected distal inputs per memory cell: 44 (min: 0, max: 126)
  - Average connected distal context inputs per memory cell: 31 (min: 0, max: 84)
- Column 04-00 = Detector start: 2500, window long/short: 500/1, threshold: 0.3 (ACTIVE)
- Column 04-01 = Classifier value key: VALUE, predict steps: 1

Grid state data;
- 02-00: 1128225
- 02-01: 1156446
- 02-03: 1106606
- 03-01: 3902885
- 04-00: 2971
- 04-01: 119559
- SELF: 25
Loading state data took 2825 ms
~~~~

Test results
------------
All 24 tests have been executed successfully (279286 assertions).  
Total test duration: 73826 ms (total sleep duration: 18800 ms).  

Memory usage per test;  
 * nl.zeesoft.zdk.test.impl.TestZStringEncoder: 757 Kb / 0 Mb
 * nl.zeesoft.zdk.test.impl.TestZStringSymbolParser: 523 Kb / 0 Mb
 * nl.zeesoft.zdk.test.impl.TestCsv: 531 Kb / 0 Mb
 * nl.zeesoft.zdk.test.impl.TestJson: 545 Kb / 0 Mb
 * nl.zeesoft.zdk.test.impl.TestZHttpRequest: 557 Kb / 0 Mb
 * nl.zeesoft.zdk.test.impl.TestMessenger: 763 Kb / 0 Mb
 * nl.zeesoft.zdk.test.impl.TestZMatrix: 829 Kb / 0 Mb
 * nl.zeesoft.zdk.test.impl.TestGeneticCode: 790 Kb / 0 Mb
 * nl.zeesoft.zdk.test.impl.TestNeuralNet: 4227 Kb / 4 Mb
 * nl.zeesoft.zdk.test.impl.TestGeneticNN: 1317 Kb / 1 Mb
 * nl.zeesoft.zdk.test.impl.TestEvolver: 6599 Kb / 6 Mb
 * nl.zeesoft.zdk.test.impl.htm.TestSDR: 3010 Kb / 2 Mb
 * nl.zeesoft.zdk.test.impl.htm.TestSDRMap: 934 Kb / 0 Mb
 * nl.zeesoft.zdk.test.impl.htm.TestScalarEncoder: 934 Kb / 0 Mb
 * nl.zeesoft.zdk.test.impl.htm.TestRDScalarEncoder: 957 Kb / 0 Mb
 * nl.zeesoft.zdk.test.impl.htm.TestDateTimeEncoder: 964 Kb / 0 Mb
 * nl.zeesoft.zdk.test.impl.htm.TestDateTimeValuesEncoder: 959 Kb / 0 Mb
 * nl.zeesoft.zdk.test.impl.htm.TestDateTimeValueEncoder: 975 Kb / 0 Mb
 * nl.zeesoft.zdk.test.impl.htm.TestGridEncoder: 979 Kb / 0 Mb
 * nl.zeesoft.zdk.test.impl.htm.TestPooler: 34940 Kb / 34 Mb
 * nl.zeesoft.zdk.test.impl.htm.TestMemory: 34947 Kb / 34 Mb
 * nl.zeesoft.zdk.test.impl.htm.TestMerger: 34958 Kb / 34 Mb
 * nl.zeesoft.zdk.test.impl.htm.TestZGridColumnEncoders: 35024 Kb / 34 Mb
 * nl.zeesoft.zdk.test.impl.htm.TestZGrid: 35206 Kb / 34 Mb
