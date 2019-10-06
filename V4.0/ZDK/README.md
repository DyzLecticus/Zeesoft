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
Key: 2179359262169116468992046052774815546101345937125238952825860084
Input text: Hello, my name is 'Dyz Lecticus'. How are you feeling today? :-) (Don't you know how to: [re;spond]!).

Key encoded text: EVOVD1J2nX9Yx1~WyZ4VSVGZ:2mVDUvZhYFYq1:2P2mWJTAX7ZOUIYCWo1t1nYKZMUDXdY0YrZaUvUFV7XUXkZN2zXRYrVpWuYpWwXu2N2tXQWW2rWTZo2vYtTsUfU#T4VSU31M2mX4YN2qVyZhWZWGYz2vVRVGZZXMZx2n3u1gWXVlX3YGUGY:WKZr1kYR1LVGYyYWXeYoU0
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
2019-10-06 23:56:32:983 DBG nl.zeesoft.zdk.test.impl.TestMessengerListener: Test log debug message before Messenger has started
2019-10-06 23:56:33:284 ERR nl.zeesoft.zdk.test.impl.TestMessengerListener: Test log error message while Messenger is working
2019-10-06 23:56:33:284 ERR nl.zeesoft.zdk.test.impl.TestMessengerListener: Test log exception stack trace
java.lang.NumberFormatException: For input string: "A"
	at java.lang.NumberFormatException.forInputString(NumberFormatException.java:65)
	at java.lang.Integer.parseInt(Integer.java:580)
	at java.lang.Integer.parseInt(Integer.java:615)
	at nl.zeesoft.zdk.test.impl.TestMessenger.test(TestMessenger.java:83)
	at nl.zeesoft.zdk.test.Tester.test(Tester.java:69)
	at nl.zeesoft.zdk.test.LibraryObject.describeAndTest(LibraryObject.java:39)
	at nl.zeesoft.zdk.test.impl.ZDK.main(ZDK.java:50)

2019-10-06 23:56:33:596 WRN nl.zeesoft.zdk.test.impl.TestMessengerListener: Test log warning message after Messenger has stopped
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
-00.15 | 000.87 | -00.25
-------+--------+-------
-00.38 | -00.66 | 000.23

Randomized multiplied element wise;
-04.42 | 026.24 | -07.63
-------+--------+-------
-11.35 | -19.81 | 007.01

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
000.24 | -00.02 | -00.83
-------+--------+-------
000.18 | -00.99 | -00.14

Randomized matrix transposed;
000.24 | 000.18
-------+-------
-00.02 | -00.99
-------+-------
-00.83 | -00.14
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
Genetic code: 2435395882769080924291769691290494520904847678165596370991439521543592842045988659446129031452077951
Mutated code: 2437395882769080924291769691290494540904847678165593370991439521543595842045988659746129031452077951
                 ^                               ^               ^                 ^            ^                 

Scaled property values;
0: 816
1: 886 <
2: 584 <
3: 48
4: 437 <
5: 769
6: 933 <
7: 454 <
8: 690
9: 49
10: 696
11: 435
12: 691 <
13: 521
14: 77
15: 395
16: 886
17: 395 <
18: 540 <
19: 767
20: 291
21: 48
22: 521
23: 145 <
24: 559
25: 31
26: 176
27: 49 <
28: 207
29: 769
30: 655
31: 914
32: 291
Mutated property values: 10
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
  Input: [1.00|1.00], output: [-0.01], expectation: [0.00], error: 0.01, loss: 0.009638871
  Input: [0.00|1.00], output: [-0.01], expectation: [1.00], error: 1.01, loss: 1.006593
  Input: [1.00|0.00], output: [-0.00], expectation: [1.00], error: 1.00, loss: 1.0030459
  Average error: 0.50, average loss: 0.50, success: false
Latest test results;
  Input: [1.00|1.00], output: [0.01], expectation: [0.00], error: -0.01, loss: 0.008854799
  Input: [0.00|1.00], output: [0.95], expectation: [1.00], error: 0.05, loss: 0.051279604
  Input: [1.00|0.00], output: [0.95], expectation: [1.00], error: 0.05, loss: 0.048368335
  Input: [0.00|0.00], output: [0.12], expectation: [0.00], error: -0.12, loss: 0.12358746
  Average error: 0.06, average loss: 0.06, success: false
Trained epochs: 10000, total average error: 2899.5056, total average loss: 2899.5056
================================================================================
Neural net activator: nl.zeesoft.zdk.functions.ZLeakyReLU, output activator: nl.zeesoft.zdk.functions.ZSoftmaxTop, learning rate: 0.1
Initial test results;
  Input: [0.00|0.00], output: [0.00], expectation: [0.00], error: 0.00, loss: 0.0
  Input: [1.00|1.00], output: [0.00], expectation: [0.00], error: 0.00, loss: 0.0
  Input: [0.00|1.00], output: [0.00], expectation: [1.00], error: 1.00, loss: 1.0
  Input: [1.00|0.00], output: [0.00], expectation: [1.00], error: 1.00, loss: 1.0
  Average error: 0.50, average loss: 0.50, success: false
Latest test results;
  Input: [1.00|1.00], output: [1.00], expectation: [0.00], error: -1.00, loss: 1.0
  Input: [1.00|0.00], output: [0.00], expectation: [1.00], error: 1.00, loss: 1.0
  Input: [0.00|0.00], output: [1.00], expectation: [0.00], error: -1.00, loss: 1.0
  Input: [0.00|1.00], output: [0.00], expectation: [1.00], error: 1.00, loss: 1.0
  Average error: 1.00, average loss: 1.00, success: false
Trained epochs: 10000, total average error: 7109.5, total average loss: 7109.5

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
    "2,1,1.1232579,-0.029934349",
    "1,1,0.0"
  ],
  "weights": [
    "1,1,0.0",
    "2,2,-0.042926986,-0.009389047,-0.667572,-0.8089906",
    "1,2,0.5977514,-0.8952775"
  ],
  "biases": [
    "1,1,0.0",
    "2,1,1.2521971,-2.1862347",
    "1,1,-0.7099886"
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
Neural net activator: nl.zeesoft.zdk.functions.ZSigmoid, output activator: nl.zeesoft.zdk.functions.ZReLU, learning rate: 0.0408
Initial test results;
  Input: [0.00|0.00], output: [1.63], expectation: [0.00], error: -1.63, loss: 1.6339092
  Input: [1.00|1.00], output: [1.51], expectation: [0.00], error: -1.51, loss: 1.5062909
  Input: [0.00|1.00], output: [1.58], expectation: [1.00], error: -0.58, loss: 0.58254707
  Input: [1.00|0.00], output: [1.55], expectation: [1.00], error: -0.55, loss: 0.55242884
  Average error: 1.07, average loss: 1.07, success: false

00100 -----------/ 48%
00200 -----------| 48%
00300 -----------| 48%
00400 -----------| 48%
00500 -----------| 48%
00600 ----------/ 47%
00700 ----------| 47%
00800 ----------| 47%
00900 ----------| 47%
01000 ----------| 47%
01100 ----------| 47%
01200 ----------| 47%
01300 ----------/ 46%
01400 ----------| 46%
01500 ----------/ 45%
01600 ----------/ 44%
01700 ---------/ 43%
01800 ---------/ 42%
01900 ---------/ 40%
02000 --------/ 37%
02100 -------/ 34%
02200 ------/ 29%
02300 -----/ 25%
02400 ----/ 21%
02500 ---/ 16%
02600 --/ 12%
02700 -/ 8%

Latest test results;
  Input: [1.00|1.00], output: [0.10], expectation: [0.00], error: -0.10, loss: 0.099095404
  Input: [1.00|0.00], output: [0.93], expectation: [1.00], error: 0.07, loss: 0.07317859
  Input: [0.00|0.00], output: [0.04], expectation: [0.00], error: -0.04, loss: 0.041377723
  Input: [0.00|1.00], output: [0.93], expectation: [1.00], error: 0.07, loss: 0.07015109
  Average error: 0.07, average loss: 0.07, success: true
Trained epochs: 2787, total average error: 1171.3406, total average loss: 1171.3406
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
2019-10-06 23:56:34:296 DBG nl.zeesoft.zdk.genetic.Evolver: Started
2019-10-06 23:56:34:376 DBG nl.zeesoft.zdk.genetic.Evolver: Selected new best genetic neural net;
- Code: 8094276811121005
- Size: 14
- Initial average loss: 0.51633 (final: 0.05599)
- Total average loss: 21.21823 (epochs: 72)
- Training result: 10.95551
2019-10-06 23:56:34:768 DBG nl.zeesoft.zdk.genetic.Evolver: Selected new best genetic neural net;
- Code: 8094276111121005
- Size: 14
- Initial average loss: 0.54678 (final: 0.07705)
- Total average loss: 17.04869 (epochs: 62)
- Training result: 9.32194
2019-10-06 23:56:38:280 DBG nl.zeesoft.zdk.genetic.Evolver: Selected new best genetic neural net;
- Code: 8094276111125005
- Size: 14
- Initial average loss: 0.55534 (final: 0.06481)
- Total average loss: 7.61667 (epochs: 25)
- Training result: 4.22981
2019-10-06 23:56:38:373 DBG nl.zeesoft.zdk.genetic.Evolver: Selected new best genetic neural net;
- Code: 8094276111125055
- Size: 14
- Initial average loss: 0.44761 (final: 0.07418)
- Total average loss: 6.81544 (epochs: 24)
- Training result: 3.05069
2019-10-06 23:56:38:477 DBG nl.zeesoft.zdk.genetic.Evolver: Selected new best genetic neural net;
- Code: 8094273111125055
- Size: 14
- Initial average loss: 0.35884 (final: 0.05790)
- Total average loss: 6.83307 (epochs: 27)
- Training result: 2.45199
2019-10-06 23:56:39:757 DBG nl.zeesoft.zdk.genetic.Evolver: Selected new best genetic neural net;
- Code: 8094273111125055
- Size: 14
- Initial average loss: 0.30640 (final: 0.07115)
- Total average loss: 2.82585 (epochs: 16)
- Training result: 0.86583
2019-10-06 23:56:39:792 DBG nl.zeesoft.zdk.genetic.Evolver: Selected new best genetic neural net;
- Code: 8094273111125052
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
    "2019-10-06 23:56:34:376 SEL code: 8094276811121005, size: 14, initial loss: 0.51633 (final: 0.05599), total loss: 21.21823, result: 10.95551 (epochs: 72)",
    "2019-10-06 23:56:34:768 SEL code: 8094276111121005, size: 14, initial loss: 0.54678 (final: 0.07705), total loss: 17.04869, result: 9.32194 (epochs: 62)",
    "2019-10-06 23:56:38:280 SEL code: 8094276111125005, size: 14, initial loss: 0.55534 (final: 0.06481), total loss: 7.61667, result: 4.22981 (epochs: 25)",
    "2019-10-06 23:56:38:373 SEL code: 8094276111125055, size: 14, initial loss: 0.44761 (final: 0.07418), total loss: 6.81544, result: 3.05069 (epochs: 24)",
    "2019-10-06 23:56:38:477 SEL code: 8094273111125055, size: 14, initial loss: 0.35884 (final: 0.05790), total loss: 6.83307, result: 2.45199 (epochs: 27)",
    "2019-10-06 23:56:39:757 SEL code: 8094273111125055, size: 14, initial loss: 0.30640 (final: 0.07115), total loss: 2.82585, result: 0.86583 (epochs: 16)",
    "2019-10-06 23:56:39:792 SEL code: 8094273111125052, size: 14, initial loss: 0.00000 (final: 0.00000), total loss: 0.00000, result: 0.00000 (epochs: 0)"
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
          "code": "4MbGpElBoHQENBWORNwK4HqIDGPEJObLfJQNGA4DhBbGDMPGdOjO3DfOlEbCkCPOwCPJ2",
          "neuralNet": [
            {
              "inputNeurons": 2,
              "hiddenLayers": 1,
              "hiddenNeurons": 2,
              "outputNeurons": 1,
              "weightFunction": "nl.zeesoft.zdk.functions.ZWeightXavier",
              "biasFunction": "nl.zeesoft.zdk.functions.ZWeightKaiming",
              "activator": "nl.zeesoft.zdk.functions.ZReLU",
              "outputActivator": "nl.zeesoft.zdk.functions.ZSoftmaxTop",
              "learningRate": 0.1578,
              "values": [
                "2,1,1.0,0.0",
                "2,1,0.0,0.33489418",
                "1,1,1.0"
              ],
              "weights": [
                "1,1,0.0",
                "2,2,-0.85006547,-0.6835578,0.98809147,0.62002194",
                "1,2,-0.7421954,-0.61237246"
              ],
              "biases": [
                "1,1,0.0",
                "2,1,0.73321396,-0.6531973",
                "1,1,0.5045949"
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
SDR map: 100,2|77,79|3,59
Number of SDR A matches in SDR map: 1
SDR C: 100,64,42
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
SDR for value 0:  00000000100000000010000000000000011000000000000000
SDR for value 1:  00000100100000000010000000000000001000000000000000
SDR for value 24: 01000000000100000000000000010001000000000000000000
SDR for value 25: 01100000000100000000000000010000000000000000000000
SDR for value 75: 00001000000000000000010000000001000000000000010000
SDR for value 76: 00001000000000000000000000000001000000000000010100
SDR for value -1: 00000000000000000010000000000000011100000000000000
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
SDR for 2019-01-01 00:00:00:000, value1: 0, value2: 0; 11111100000000000000000000000000000000000000001111111110000000000000000000000000000000000000000100101000010000000100100000000010000110000010000000000010010001001000001000010100000000111111110000000000
SDR for 2019-02-02 01:00:00:000, value1: 1, value2: 2; 11111111000000000000000000000000000000000000000000001111111100000000000000000000000000000000000000101000010000000100101000000010000010000010000000000010010001001000100000110000111100000000000000001111
SDR for 2019-03-03 02:00:00:000, value1: 2, value2: 4; 00111111110000000000000000000000000000000000000000000000111111110000000000000000000000000000000000101000000000000100101000000010000010100011000000000010010000001000100000110000111111110000000000000000
SDR for 2019-04-04 03:00:00:000, value1: 3, value2: 6; 00111111110000000000000000000000000000000000000000000000000011111111000000000000000000000000000000101000000000000100101000000011000000100011000100000010010000011000100000000000000000000000011111111000
SDR for 2019-05-05 04:00:00:000, value1: 4, value2: 8; 00001111111100000000000000000000000000000000000000000000000000001111111100000000000000000000000000111000000000000100101000000010000000100010000001000110010000011000100000000000111111110000000000000000

Encoder StringBuilder:
VALUE1=0.0,17,2,35,4,20,36,9,30;1.0,17,2,4,20,36,22,9,30;2.0,17,2,4,20,36,22,38,30;3.0,17,2,4,20,22,38,30,31;4.0,17,2,3,4,20,22,38,30|VALUE2=0.0,17,2,35,21,37,24,30,14;1.0,17,2,35,21,37,24,28,14;2.0,17,2,34,35,21,24,28,14;3.0,17,2,34,35,24,27,28,14;4.0,17,2,34,35,3,24,28,14;5.0,17,2,34,3,7,24,28,14;6.0,17,2,3,7,23,24,28,14;7.0,17,2,3,23,24,9,28,14;8.0,17,2,23,24,9,28,13,14
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
Initializing pooler took: 18 ms
Randomizing connections took: 76 ms

Pooler input dimensions: 16x16, output dimensions: 32x32
- Average proximal inputs per column: 90
- Column groups: 144, average columns per group: 441

Processing input SDR map (learning: false) ...
Processing input SDR map took: 8019 ms

Performance statistics;
calculateOverlapScores:       1718.086 ms
selectActiveColumns:           870.309 ms
logActivity:                  1147.552 ms
calculateColumnGroupActivity: 2808.459 ms
updateBoostFactors:           1242.457 ms
total:                        7847.295 ms
logSize:                         15330   
avgPerLog:                       0.512 ms

Combined average: 0.37596637, Combined weekly average: 4.9757214

Processing input SDR map (learning: true) ...
Processing input SDR map took: 11386 ms

Performance statistics;
calculateOverlapScores:        2681.246 ms
selectActiveColumns:            900.980 ms
learnActiveColumnsOnBits:      2544.721 ms
logActivity:                    961.656 ms
calculateColumnGroupActivity:  2816.548 ms
updateBoostFactors:            1227.795 ms
total:                        11194.338 ms
logSize:                          15330   
avgPerLog:                        0.730 ms

Combined average: 0.3209485, Combined weekly average: 7.3554926

Original ratio: 13.234486, learned ratio: 22.917984
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
Processed SDRs: 500, bursting average: 4 (max: 9)
Processed SDRs: 1000, bursting average: 2 (max: 7)
Processed SDRs: 1500, bursting average: 1 (max: 8)
Processed SDRs: 2000, bursting average: 1 (max: 4)
Processed SDRs: 2500, bursting average: 1 (max: 7)
Processed SDRs: 3000, bursting average: 1 (max: 4)
Processed SDRs: 3500, bursting average: 0 (max: 4)
Processed SDRs: 4000, bursting average: 0 (max: 4)
Processed SDRs: 4500, bursting average: 0 (max: 3)
Processed SDRs: 5000, bursting average: 0 (max: 5)
Processing input SDR map took: 13352 ms

Performance statistics;
cycleActiveState:       190.622 ms
activateColumnCells:    210.260 ms
calculateActivity:      894.152 ms
selectPredictiveCells:  424.895 ms
updatePredictions:     3922.325 ms
total:                 5653.498 ms
logSize:                   5000   
avgPerLog:                1.131 ms

Average distal inputs per memory cell: 56 (min: 21, max: 105)
Average connected distal inputs per memory cell: 53 (min: 11, max: 104)
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
// Add some values to the stream (include an anomaly after 5000 inputs)
stream.addValue(1);
stream.addValue(2);
// Remember to stop and destroy the stream after use
stream.stop();
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
Processed SDRs: 500, average accuracy: 0.452, latest: 0.810
Processed SDRs: 1000, average accuracy: 0.633, latest: 1.000
Processed SDRs: 1500, average accuracy: 0.843, latest: 1.000
Processed SDRs: 2000, average accuracy: 0.902, latest: 0.952
Processed SDRs: 2500, average accuracy: 0.930, latest: 1.000
Processed SDRs: 3000, average accuracy: 0.946, latest: 1.000
Processed SDRs: 3500, average accuracy: 0.959, latest: 0.905
Processed SDRs: 4000, average accuracy: 0.968, latest: 1.000
Processed SDRs: 4500, average accuracy: 0.972, latest: 0.952
Processed SDRs: 5000, average accuracy: 0.972, latest: 0.810
Processed SDRs: 5500, average accuracy: 0.974, latest: 0.952
Processed SDRs: 6000, average accuracy: 0.977, latest: 0.905
Processed SDRs: 6500, average accuracy: 0.978, latest: 0.952
Processed SDRs: 7000, average accuracy: 0.978, latest: 0.952
Processed SDRs: 7500, average accuracy: 0.976, latest: 1.000
Detected anomaly at: 7666, average accuracy: 0.9769547, latest: 0.047619045, difference: 0.9070461
Stopped stream after 16947 ms

DefaultStream;
total:      57581540.000 ms
logSize:            7668   
avgPerLog:      7509.330 ms

Pooler;
calculateOverlapScores:        5208.748 ms
selectActiveColumns:            844.452 ms
learnActiveColumnsOnBits:      2653.069 ms
logActivity:                   1378.459 ms
calculateColumnGroupActivity:  5433.421 ms
updateBoostFactors:            1228.095 ms
total:                        16809.283 ms
logSize:                          13894   
avgPerLog:                        1.210 ms

Memory;
cycleActiveState:        501.786 ms
activateColumnCells:     305.309 ms
calculateActivity:      1191.905 ms
selectPredictiveCells:  1016.190 ms
updatePredictions:     12757.426 ms
total:                 15787.575 ms
logSize:                    7668   
avgPerLog:                 2.059 ms

Total processing time per SDR: 3.269 ms
Total stream time per SDR:     2.210 ms
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
// Add some values to the stream
stream.addValue(1);
stream.addValue(2);
// Remember to stop and destroy the stream after use
stream.stop();
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
Processed SDRs: 500, accuracy: 0.297
Processed SDRs: 1000, accuracy: 0.529
Processed SDRs: 1500, accuracy: 0.753
Processed SDRs: 2000, accuracy: 0.800
Processed SDRs: 2500, accuracy: 0.878
Processed SDRs: 3000, accuracy: 0.894
Processed SDRs: 3500, accuracy: 0.919
Processed SDRs: 4000, accuracy: 0.945
Processed SDRs: 4500, accuracy: 0.929
Processed SDRs: 5000, accuracy: 0.933
Stopped stream after 10015 ms

ClassificationStream;
total:      23252246.000 ms
logSize:            5005   
avgPerLog:      4645.804 ms

Pooler;
calculateOverlapScores:       3756.707 ms
selectActiveColumns:           482.117 ms
learnActiveColumnsOnBits:     1556.507 ms
logActivity:                   749.814 ms
calculateColumnGroupActivity: 2646.004 ms
updateBoostFactors:            659.198 ms
total:                        9888.814 ms
logSize:                          7454   
avgPerLog:                       1.327 ms

Memory;
cycleActiveState:       334.774 ms
activateColumnCells:    202.487 ms
calculateActivity:      771.703 ms
selectPredictiveCells:  602.947 ms
updatePredictions:     6609.973 ms
total:                 8531.671 ms
logSize:                   5006   
avgPerLog:                1.704 ms

Classifier;
generateClassifications: 626.017 ms
total:                   632.476 ms
logSize:                    5005   
avgPerLog:                 0.126 ms

Total processing time per SDR: 3.157 ms
Total stream time per SDR:     2.001 ms

Stream state JSON;
{
  "streamClassName": "nl.zeesoft.zdk.htm.stream.ClassificationStream",
  "encoderClassName": "nl.zeesoft.zdk.htm.stream.StreamEncoder",
  "encoderData": "",
  "uid": "15330",
  "processors": [
    {
      "processorClassName": "nl.zeesoft.zdk.htm.proc.Pooler",
      "processorData": "0.0,153;0.98399997,68;0.78399956,58;0.0,164;0.1679994,87;0.0,96;0.98399997,113;0 ..."
    },
    {
      "processorClassName": "nl.zeesoft.zdk.htm.proc.Memory",
      "processorData": "0.38518602,0-1-0;0.28071848,21-3-1;0.29650417,17-4-2;0.22381736,22-4-3;0.3897513 ..."
    },
    {
      "processorClassName": "nl.zeesoft.zdk.htm.proc.Classifier",
      "processorData": "1#0;Float;10.0,5;7.0,1;14.0,2;25.0,2;17.0,1;21.0,1;35.0,1;11.0,3;13.0,1;22.0,7;9 ..."
    }
  ]
}
~~~~

Test results
------------
All 22 tests have been executed successfully (149 assertions).  
Total test duration: 76757 ms (total sleep duration: 32800 ms).  

Memory usage per test;  
 * nl.zeesoft.zdk.test.impl.TestZStringEncoder: 766 Kb / 0 Mb
 * nl.zeesoft.zdk.test.impl.TestZStringSymbolParser: 526 Kb / 0 Mb
 * nl.zeesoft.zdk.test.impl.TestCsv: 533 Kb / 0 Mb
 * nl.zeesoft.zdk.test.impl.TestJson: 547 Kb / 0 Mb
 * nl.zeesoft.zdk.test.impl.TestZHttpRequest: 554 Kb / 0 Mb
 * nl.zeesoft.zdk.test.impl.TestMessenger: 756 Kb / 0 Mb
 * nl.zeesoft.zdk.test.impl.TestZMatrix: 830 Kb / 0 Mb
 * nl.zeesoft.zdk.test.impl.TestGeneticCode: 790 Kb / 0 Mb
 * nl.zeesoft.zdk.test.impl.TestNeuralNet: 3754 Kb / 3 Mb
 * nl.zeesoft.zdk.test.impl.TestGeneticNN: 6464 Kb / 6 Mb
 * nl.zeesoft.zdk.test.impl.TestEvolver: 9073 Kb / 8 Mb
 * nl.zeesoft.zdk.test.impl.htm.TestSDR: 884 Kb / 0 Mb
 * nl.zeesoft.zdk.test.impl.htm.TestSDRMap: 892 Kb / 0 Mb
 * nl.zeesoft.zdk.test.impl.htm.TestScalarEncoder: 890 Kb / 0 Mb
 * nl.zeesoft.zdk.test.impl.htm.TestRDScalarEncoder: 915 Kb / 0 Mb
 * nl.zeesoft.zdk.test.impl.htm.TestDateTimeEncoder: 921 Kb / 0 Mb
 * nl.zeesoft.zdk.test.impl.htm.TestDateTimeValuesEncoder: 918 Kb / 0 Mb
 * nl.zeesoft.zdk.test.impl.htm.TestPooler: 34957 Kb / 34 Mb
 * nl.zeesoft.zdk.test.impl.htm.TestMemory: 34935 Kb / 34 Mb
 * nl.zeesoft.zdk.test.impl.htm.TestStreamEncoder: 34938 Kb / 34 Mb
 * nl.zeesoft.zdk.test.impl.htm.TestAnomalyDetector: 72895 Kb / 71 Mb
 * nl.zeesoft.zdk.test.impl.htm.TestValueClassifier: 79385 Kb / 77 Mb
