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
Key: 7533480853735704884732056964892131487199105428834591652592569283
Input text: Hello, my name is 'Dyz Lecticus'. How are you feeling today? :-) (Don't you know how to: [re;spond]!).

Key encoded text: vFfEODVDZEvFIAOH:EWCVGODDF#Ft~XEaH~GODhFUD3Cx~9EwFyHnFXEYHuHZCcAHCeAsDZHWGABGImHWBw~BF~EADRG:GMDEEMFgHzBcF8DnCQFfH4COFSFeGDCUAf~RFjDdCYDYEqF2BCG:EaD3GOCBFGGEBbESGGHVE4GzBWCIBQEsEqHlFTEnGsHWCjBGDhBKEMGJFOB0
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
2020-01-04 03:20:50:957 DBG nl.zeesoft.zdk.test.impl.TestMessengerListener: Test log debug message before Messenger has started
2020-01-04 03:20:51:258 ERR nl.zeesoft.zdk.test.impl.TestMessengerListener: Test log error message while Messenger is working
2020-01-04 03:20:51:258 ERR nl.zeesoft.zdk.test.impl.TestMessengerListener: Test log exception stack trace
java.lang.NumberFormatException: For input string: "A"
	at java.lang.NumberFormatException.forInputString(NumberFormatException.java:65)
	at java.lang.Integer.parseInt(Integer.java:580)
	at java.lang.Integer.parseInt(Integer.java:615)
	at nl.zeesoft.zdk.test.impl.TestMessenger.test(TestMessenger.java:83)
	at nl.zeesoft.zdk.test.Tester.test(Tester.java:71)
	at nl.zeesoft.zdk.test.LibraryObject.describeAndTest(LibraryObject.java:39)
	at nl.zeesoft.zdk.test.impl.ZDK.main(ZDK.java:52)

2020-01-04 03:20:51:568 WRN nl.zeesoft.zdk.test.impl.TestMessengerListener: Test log warning message after Messenger has stopped
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
000.34 | -00.46 | -00.08
-------+--------+-------
000.06 | -00.16 | 000.70

Randomized multiplied element wise;
010.07 | -13.82 | -02.54
-------+--------+-------
001.76 | -04.82 | 021.09

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
-00.06 | -00.59 | 000.57
-------+--------+-------
000.97 | 000.40 | 000.83

Randomized matrix transposed;
-00.06 | 000.97
-------+-------
-00.59 | 000.40
-------+-------
000.57 | 000.83
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
Genetic code: 5848850892750744993526402434001661082909208954425110810988543602172380619914710158971048341684185344
Mutated code: 5848850882750744993536402434001661082909238954425110810988543602172380619918710158971048340684185344
                      ^           ^                    ^                                 ^              ^         

Scaled property values;
0: 406 <
1: 418
2: 499
3: 92 <
4: 993
5: 543
6: 108
7: 187 <
8: 610
9: 810
10: 340
11: 400
12: 110
13: 88 <
14: 882 <
15: 172
16: 61
17: 340
18: 1
19: 744
20: 360
21: 895
22: 810
23: 909
24: 434
25: 449 <
26: 1
27: 402
28: 442
29: 935
30: 238 <
31: 589
32: 418
Mutated property values: 7
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
  Input: [1.00|1.00], output: [0.01], expectation: [0.00], error: -0.01, loss: 0.005574427
  Input: [0.00|1.00], output: [0.00], expectation: [1.00], error: 1.00, loss: 0.99799806
  Input: [1.00|0.00], output: [-0.00], expectation: [1.00], error: 1.00, loss: 1.0002172
  Average error: 0.50, average loss: 0.50, success: false
Latest test results;
  Input: [1.00|1.00], output: [0.53], expectation: [0.00], error: -0.53, loss: 0.5335018
  Input: [0.00|1.00], output: [0.48], expectation: [1.00], error: 0.52, loss: 0.52339476
  Input: [1.00|0.00], output: [0.53], expectation: [1.00], error: 0.47, loss: 0.4678905
  Input: [0.00|0.00], output: [0.58], expectation: [0.00], error: -0.58, loss: 0.5753633
  Average error: 0.53, average loss: 0.53, success: false
Trained epochs: 10000, total average error: 5237.212, total average loss: 5237.212
================================================================================
Neural net activator: nl.zeesoft.zdk.functions.ZLeakyReLU, output activator: nl.zeesoft.zdk.functions.ZSoftmaxTop, learning rate: 0.1
Initial test results;
  Input: [0.00|0.00], output: [0.00], expectation: [0.00], error: 0.00, loss: 0.0
  Input: [1.00|1.00], output: [1.00], expectation: [0.00], error: -1.00, loss: 1.0
  Input: [0.00|1.00], output: [1.00], expectation: [1.00], error: 0.00, loss: 0.0
  Input: [1.00|0.00], output: [0.00], expectation: [1.00], error: 1.00, loss: 1.0
  Average error: 0.50, average loss: 0.50, success: false
Latest test results;
  Input: [1.00|1.00], output: [1.00], expectation: [0.00], error: -1.00, loss: 1.0
  Input: [0.00|0.00], output: [0.00], expectation: [0.00], error: 0.00, loss: 0.0
  Input: [0.00|1.00], output: [0.00], expectation: [1.00], error: 1.00, loss: 1.0
  Input: [1.00|0.00], output: [0.00], expectation: [1.00], error: 1.00, loss: 1.0
  Average error: 0.75, average loss: 0.75, success: false
Trained epochs: 10000, total average error: 7241.5, total average loss: 7241.5

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
    "2,1,0.62782454,0.5483193",
    "1,1,0.0"
  ],
  "weights": [
    "1,1,0.0",
    "2,2,0.10148236,0.17904721,0.19767171,0.18052569",
    "1,2,0.96530807,-0.37742066"
  ],
  "biases": [
    "1,1,0.0",
    "2,1,0.7194038,0.2751634",
    "1,1,-0.4205867"
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
Neural net activator: nl.zeesoft.zdk.functions.ZTanH, output activator: nl.zeesoft.zdk.functions.ZReLU, learning rate: 0.0228
Initial test results;
  Input: [0.00|0.00], output: [0.00], expectation: [0.00], error: 0.00, loss: 0.0
  Input: [1.00|1.00], output: [0.00], expectation: [0.00], error: 0.00, loss: 0.0
  Input: [0.00|1.00], output: [0.00], expectation: [1.00], error: 1.00, loss: 1.0
  Input: [1.00|0.00], output: [0.00], expectation: [1.00], error: 1.00, loss: 1.0
  Average error: 0.50, average loss: 0.50, success: false

00100 ------------------------\ 102%
00200 ------------------------| 102%
00300 ------------------------| 102%
00400 ------------------------| 102%
00500 ------------------------| 102%
00600 ------------------------| 102%
00700 ------------------------| 102%
00800 ------------------------| 102%
00900 ------------------------| 102%
01000 ------------------------| 102%
01100 ------------------------| 102%
01200 ------------------------| 102%
01300 ------------------------| 102%
01400 ------------------------/ 101%
01500 ------------------------| 101%
01600 ------------------------| 101%
01700 ------------------------| 101%
01800 ------------------------| 101%
01900 ------------------------| 101%
02000 ------------------------| 101%
02100 ------------------------| 101%
02200 ------------------------/ 100%
02300 ------------------------\ 101%
02400 ------------------------| 101%
02500 ------------------------/ 100%
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
03600 ------------------------\ 101%
03700 ------------------------/ 100%
03800 ------------------------| 100%
03900 ------------------------| 100%
04000 ------------------------\ 101%
04100 ------------------------| 101%
04200 ------------------------/ 100%
04300 ------------------------\ 101%
04400 ------------------------/ 100%
04500 ------------------------\ 101%
04600 ------------------------| 101%
04700 ------------------------| 101%
04800 ------------------------| 101%
04900 ------------------------/ 100%
05000 ------------------------\ 101%
05100 ------------------------| 101%
05200 ------------------------| 101%
05300 ------------------------| 101%
05400 ------------------------| 101%
05500 ------------------------| 101%
05600 ------------------------| 101%
05700 ------------------------| 101%
05800 ------------------------| 101%
05900 ------------------------| 101%
06000 ------------------------| 101%
06100 ------------------------| 101%
06200 ------------------------| 101%
06300 ------------------------| 101%
06400 ------------------------| 101%
06500 ------------------------| 101%
06600 ------------------------| 101%
06700 ------------------------/ 100%
06800 ------------------------\ 101%
06900 ------------------------/ 100%
07000 ------------------------\ 101%
07100 ------------------------| 101%
07200 ------------------------| 101%
07300 ------------------------| 101%
07400 ------------------------| 101%
07500 ------------------------| 101%
07600 ------------------------/ 100%
07700 ------------------------\ 101%
07800 ------------------------| 101%
07900 ------------------------| 101%
08000 ------------------------| 101%
08100 ------------------------| 101%
08200 ------------------------| 101%
08300 ------------------------| 101%
08400 ------------------------| 101%
08500 ------------------------\ 102%
08600 ------------------------/ 101%
08700 ------------------------| 101%
08800 ------------------------\ 102%
08900 ------------------------| 102%
09000 ------------------------| 102%
09100 ------------------------| 102%
09200 ------------------------| 102%
09300 ------------------------| 102%
09400 ------------------------| 102%
09500 ------------------------| 102%
09600 ------------------------| 102%
09700 ------------------------| 102%
09800 ------------------------| 102%
09900 ------------------------/ 101%
10000 ------------------------\ 102%

Latest test results;
  Input: [1.00|1.00], output: [0.53], expectation: [0.00], error: -0.53, loss: 0.5344305
  Input: [1.00|0.00], output: [0.48], expectation: [1.00], error: 0.52, loss: 0.52491456
  Input: [0.00|1.00], output: [0.51], expectation: [1.00], error: 0.49, loss: 0.49415076
  Input: [0.00|0.00], output: [0.50], expectation: [0.00], error: -0.50, loss: 0.49973783
  Average error: 0.51, average loss: 0.51, success: false
Trained epochs: 10000, total average error: 5077.446, total average loss: 5077.446
================================================================================
Neural net activator: nl.zeesoft.zdk.functions.ZReLU, output activator: nl.zeesoft.zdk.functions.ZLeakyReLU, learning rate: 0.1714
Initial test results;
  Input: [0.00|0.00], output: [1.56], expectation: [0.00], error: -1.56, loss: 1.5593113
  Input: [1.00|1.00], output: [2.06], expectation: [0.00], error: -2.06, loss: 2.0600762
  Input: [0.00|1.00], output: [1.99], expectation: [1.00], error: -0.99, loss: 0.9896561
  Input: [1.00|0.00], output: [1.63], expectation: [1.00], error: -0.63, loss: 0.6297314
  Average error: 1.31, average loss: 1.31, success: false

00100 ---------/ 41%
00200 -------------\ 59%
00300 ----------/ 46%
00400 ------/ 29%
00500 -----/ 26%
00600 ------\ 28%
00700 ------| 28%
00800 -----/ 26%
00900 ------\ 28%
01000 -----/ 26%
01100 -----\ 27%
01200 -----/ 26%
01300 ------\ 28%
01400 ------| 28%
01500 -----/ 25%
01600 ------\ 28%
01700 -----/ 26%
01800 -----| 26%
01900 ------\ 28%
02000 -----/ 26%
02100 -----\ 27%
02200 -----/ 26%
02300 ------\ 28%
02400 -----/ 27%
02500 -----| 27%
02600 -----/ 26%
02700 ------\ 28%
02800 -----/ 27%
02900 -----/ 26%
03000 -----\ 27%
03100 -----/ 26%
03200 -----| 26%
03300 ------\ 28%
03400 -----/ 26%
03500 ------\ 28%
03600 ------| 28%
03700 ------| 28%
03800 -----/ 27%
03900 -----/ 25%
04000 ------\ 28%
04100 -----/ 27%
04200 -----/ 26%
04300 -----\ 27%
04400 -----/ 26%
04500 -----| 26%
04600 ------\ 28%
04700 ------| 28%
04800 -----/ 25%
04900 ------\ 28%
05000 ------| 28%
05100 -----/ 27%
05200 ------\ 28%
05300 -----/ 25%
05400 -----\ 27%
05500 ------\ 28%
05600 ------| 28%
05700 -----/ 25%
05800 ------\ 28%
05900 -----/ 27%
06000 ------\ 28%
06100 -----/ 27%
06200 -----| 27%
06300 -----/ 26%
06400 ------\ 28%
06500 -----/ 27%
06600 ------\ 28%
06700 -----/ 26%
06800 -----\ 27%
06900 -----| 27%
07000 ------\ 28%
07100 -----/ 26%
07200 ------\ 28%
07300 -----/ 25%
07400 -----| 25%
07500 -----\ 27%
07600 -----| 27%
07700 -----| 27%
07800 ------\ 28%
07900 ------| 28%
08000 ------| 28%
08100 -----/ 25%
08200 -----| 25%
08300 ------\ 28%
08400 -----/ 27%
08500 ------\ 28%
08600 ------| 28%
08700 ------| 28%
08800 -----/ 27%
08900 -----| 27%
09000 -----| 27%
09100 ------\ 28%
09200 ------| 28%
09300 -----/ 26%
09400 ------\ 28%
09500 -----/ 27%
09600 -----/ 26%
09700 -----| 26%
09800 ------\ 28%
09900 ------| 28%
10000 -----/ 26%

Latest test results;
  Input: [1.00|0.00], output: [0.68], expectation: [1.00], error: 0.32, loss: 0.32410252
  Input: [1.00|1.00], output: [-0.00], expectation: [0.00], error: 0.00, loss: 7.6177536E-4
  Input: [0.00|1.00], output: [0.73], expectation: [1.00], error: 0.27, loss: 0.26855004
  Input: [0.00|0.00], output: [0.78], expectation: [0.00], error: -0.78, loss: 0.7774794
  Average error: 0.34, average loss: 0.34, success: false
Trained epochs: 10000, total average error: 3688.8093, total average loss: 3688.8093
================================================================================
Neural net activator: nl.zeesoft.zdk.functions.ZTanH, output activator: nl.zeesoft.zdk.functions.ZLeakyReLU, learning rate: 0.1168
Initial test results;
  Input: [0.00|0.00], output: [0.35], expectation: [0.00], error: -0.35, loss: 0.353401
  Input: [1.00|1.00], output: [0.45], expectation: [0.00], error: -0.45, loss: 0.445907
  Input: [0.00|1.00], output: [0.74], expectation: [1.00], error: 0.26, loss: 0.2569304
  Input: [1.00|0.00], output: [0.08], expectation: [1.00], error: 0.92, loss: 0.9230876
  Average error: 0.49, average loss: 0.49, success: false

00100 -------------------------\ 106%
00200 --------------------------\ 108%
00300 ---------------------------\ 115%
00400 ----------------------------\ 117%
00500 ----------------------------| 117%
00600 ------------------------/ 100%
00700 ------------------------| 100%
00800 -----------------------------\ 123%
00900 -------------------------/ 105%
01000 ----------------------------\ 116%
01100 ----------------------------\ 118%
01200 -----------------------------\ 121%
01300 ----------------------------/ 116%
01400 ---------------------------/ 114%
01500 ----------------------------\ 117%
01600 -----------------------------\ 120%
01700 --------------------------/ 110%
01800 ----------------------------\ 118%
01900 ----------------------------\ 119%
02000 ----------------------------/ 116%
02100 ----------------------------\ 118%
02200 ---------------------------/ 115%
02300 -------------------------/ 107%
02400 ----------------------------\ 118%
02500 ----------------------------| 118%
02600 ---------------------------/ 115%
02700 --------------------------/ 110%
02800 ----------------------------\ 119%
02900 ---------------------------/ 115%
03000 ----------------------------\ 117%
03100 -----------------------------\ 122%
03200 -----------------------------/ 121%
03300 -----------------------------| 121%
03400 ----------------------------/ 117%
03500 -------------------------/ 104%
03600 ---------------------------\ 114%
03700 ----------------------------\ 116%
03800 -----------------------/ 97%
03900 ---------------------------\ 115%
04000 -----------------------/ 99%
04100 ---------------------------\ 112%
04200 ----------------------------\ 116%
04300 ----------------------------\ 118%
04400 --------------------------/ 110%
04500 ---------------------------\ 114%
04600 ---------------------------\ 115%
04700 ---------------------------| 115%
04800 ---------------------------/ 113%
04900 -----------------------------\ 123%
05000 --------------------------/ 111%
05100 -----------------------------\ 122%
05200 ----------------------------/ 116%
05300 ----------------------------\ 117%
05400 --------------------------/ 110%
05500 ----------------------------\ 119%
05600 ---------------------------/ 115%
05700 ----------------------------\ 119%
05800 ----------------------------/ 118%
05900 -----------------------------\ 122%
06000 ----------------------------/ 119%
06100 --------------------------/ 111%
06200 -----------------------------\ 121%
06300 ----------------------------/ 117%
06400 -----------------------/ 98%
06500 -----------------------| 98%
06600 ---------------------------\ 114%
06700 ---------------------------\ 115%
06800 ----------------------------\ 119%
06900 -----------------------------\ 120%
07000 ----------------------------/ 118%
07100 ---------------------------/ 115%
07200 ----------------------------\ 119%
07300 ---------------------------/ 115%
07400 ----------------------------\ 116%
07500 ----------------------------| 116%
07600 ---------------------------/ 115%
07700 ----------------------------\ 117%
07800 ----------------------------/ 116%
07900 ---------------------------/ 115%
08000 ------------------------/ 102%
08100 ----------------------------\ 118%
08200 ----------------------------/ 116%
08300 --------------------------/ 111%
08400 --------------------------/ 110%
08500 ----------------------------\ 116%
08600 -----------------------/ 97%
08700 -----------------------------\ 121%
08800 -----------------------------| 121%
08900 ---------------------------/ 115%
09000 ---------------------------/ 112%
09100 -----------------------------\ 121%
09200 ------------------------------\ 124%
09300 ----------------------------/ 117%
09400 ---------------------------/ 115%
09500 ----------------------------\ 119%
09600 ---------------------------/ 115%
09700 ------------------------/ 102%
09800 ---------------------------\ 115%
09900 ---------------------------| 115%
10000 ----------------------------\ 116%

Latest test results;
  Input: [0.00|1.00], output: [0.65], expectation: [1.00], error: 0.35, loss: 0.3502466
  Input: [1.00|1.00], output: [0.77], expectation: [0.00], error: -0.77, loss: 0.7724798
  Input: [0.00|0.00], output: [0.50], expectation: [0.00], error: -0.50, loss: 0.5018029
  Input: [1.00|0.00], output: [0.33], expectation: [1.00], error: 0.67, loss: 0.67402875
  Average error: 0.57, average loss: 0.57, success: false
Trained epochs: 10000, total average error: 5703.27, total average loss: 5703.27
================================================================================
Neural net activator: nl.zeesoft.zdk.functions.ZReLU, output activator: nl.zeesoft.zdk.functions.ZSigmoid, learning rate: 0.1196
Initial test results;
  Input: [0.00|0.00], output: [0.68], expectation: [0.00], error: -0.68, loss: 0.6843939
  Input: [1.00|1.00], output: [0.68], expectation: [0.00], error: -0.68, loss: 0.6843939
  Input: [0.00|1.00], output: [0.68], expectation: [1.00], error: 0.32, loss: 0.31560612
  Input: [1.00|0.00], output: [0.68], expectation: [1.00], error: 0.32, loss: 0.31560612
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
  Input: [0.00|1.00], output: [0.50], expectation: [1.00], error: 0.50, loss: 0.49930155
  Input: [0.00|0.00], output: [0.50], expectation: [0.00], error: -0.50, loss: 0.5044306
  Input: [1.00|1.00], output: [0.50], expectation: [0.00], error: -0.50, loss: 0.5006604
  Input: [1.00|0.00], output: [0.50], expectation: [1.00], error: 0.50, loss: 0.50308204
  Average error: 0.50, average loss: 0.50, success: false
Trained epochs: 10000, total average error: 5019.097, total average loss: 5019.097
================================================================================
Neural net activator: nl.zeesoft.zdk.functions.ZSigmoid, output activator: nl.zeesoft.zdk.functions.ZSoftmaxTop, learning rate: 0.0828
Initial test results;
  Input: [0.00|0.00], output: [1.00], expectation: [0.00], error: -1.00, loss: 1.0
  Input: [1.00|1.00], output: [1.00], expectation: [0.00], error: -1.00, loss: 1.0
  Input: [0.00|1.00], output: [1.00], expectation: [1.00], error: 0.00, loss: 0.0
  Input: [1.00|0.00], output: [1.00], expectation: [1.00], error: 0.00, loss: 0.0
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
  Input: [1.00|1.00], output: [1.00], expectation: [0.00], error: -1.00, loss: 1.0
  Input: [0.00|1.00], output: [1.00], expectation: [1.00], error: 0.00, loss: 0.0
  Input: [1.00|0.00], output: [1.00], expectation: [1.00], error: 0.00, loss: 0.0
  Input: [0.00|0.00], output: [1.00], expectation: [0.00], error: -1.00, loss: 1.0
  Average error: 0.50, average loss: 0.50, success: false
Trained epochs: 10000, total average error: 5000.0, total average loss: 5000.0
================================================================================
Neural net activator: nl.zeesoft.zdk.functions.ZReLU, output activator: nl.zeesoft.zdk.functions.ZTanH, learning rate: 0.013600001
Initial test results;
  Input: [0.00|0.00], output: [0.00], expectation: [0.00], error: 0.00, loss: 0.0
  Input: [1.00|1.00], output: [-0.70], expectation: [0.00], error: 0.70, loss: 0.6963401
  Input: [0.00|1.00], output: [-0.37], expectation: [1.00], error: 1.37, loss: 1.3688748
  Input: [1.00|0.00], output: [-0.44], expectation: [1.00], error: 1.44, loss: 1.4361933
  Average error: 0.88, average loss: 0.88, success: false

00100 -------------/ 56%
00200 ------------/ 54%
00300 -----------/ 51%
00400 ----------/ 46%
00500 ---------/ 41%
00600 --------/ 36%
00700 -------/ 33%
00800 -------/ 32%
00900 ------/ 31%
01000 ------/ 30%
01100 ------| 30%
01200 ------| 30%
01300 ------/ 29%
01400 ------| 29%
01500 ------| 29%
01600 ------| 29%
01700 ------| 29%
01800 ------| 29%
01900 ------| 29%
02000 ------| 29%
02100 ------| 29%
02200 ------| 29%
02300 ------| 29%
02400 ------| 29%
02500 ------| 29%
02600 ------/ 28%
02700 ------\ 29%
02800 ------| 29%
02900 ------| 29%
03000 ------| 29%
03100 ------| 29%
03200 ------| 29%
03300 ------| 29%
03400 ------/ 28%
03500 ------\ 29%
03600 ------/ 28%
03700 ------| 28%
03800 ------| 28%
03900 ------| 28%
04000 ------| 28%
04100 ------| 28%
04200 ------| 28%
04300 ------| 28%
04400 ------| 28%
04500 ------| 28%
04600 ------| 28%
04700 ------| 28%
04800 ------| 28%
04900 ------| 28%
05000 ------| 28%
05100 ------| 28%
05200 ------| 28%
05300 ------| 28%
05400 ------| 28%
05500 ------| 28%
05600 ------| 28%
05700 ------| 28%
05800 ------| 28%
05900 ------| 28%
06000 ------| 28%
06100 ------| 28%
06200 ------| 28%
06300 ------| 28%
06400 ------| 28%
06500 ------| 28%
06600 ------| 28%
06700 ------| 28%
06800 ------| 28%
06900 ------| 28%
07000 ------| 28%
07100 ------| 28%
07200 ------| 28%
07300 ------| 28%
07400 ------\ 29%
07500 ------/ 28%
07600 ------| 28%
07700 ------| 28%
07800 ------| 28%
07900 ------| 28%
08000 ------| 28%
08100 ------| 28%
08200 ------| 28%
08300 ------| 28%
08400 ------| 28%
08500 ------| 28%
08600 ------| 28%
08700 ------| 28%
08800 ------| 28%
08900 ------| 28%
09000 ------| 28%
09100 ------| 28%
09200 ------| 28%
09300 ------| 28%
09400 ------| 28%
09500 ------| 28%
09600 ------| 28%
09700 ------| 28%
09800 ------| 28%
09900 ------| 28%
10000 ------| 28%

Latest test results;
  Input: [0.00|1.00], output: [1.00], expectation: [1.00], error: 0.00, loss: 0.0017025471
  Input: [1.00|1.00], output: [0.00], expectation: [0.00], error: -0.00, loss: 0.0010064837
  Input: [0.00|0.00], output: [0.50], expectation: [0.00], error: -0.50, loss: 0.50082374
  Input: [1.00|0.00], output: [0.50], expectation: [1.00], error: 0.50, loss: 0.5031971
  Average error: 0.25, average loss: 0.25, success: false
Trained epochs: 10000, total average error: 2660.8186, total average loss: 2660.8186
================================================================================
Neural net activator: nl.zeesoft.zdk.functions.ZLeakyReLU, output activator: nl.zeesoft.zdk.functions.ZLeakyReLU, learning rate: 0.15100001
Initial test results;
  Input: [0.00|0.00], output: [0.07], expectation: [0.00], error: -0.07, loss: 0.07027438
  Input: [1.00|1.00], output: [0.14], expectation: [0.00], error: -0.14, loss: 0.1380584
  Input: [0.00|1.00], output: [-0.00], expectation: [1.00], error: 1.00, loss: 1.000039
  Input: [1.00|0.00], output: [0.18], expectation: [1.00], error: 0.82, loss: 0.81672907
  Average error: 0.51, average loss: 0.51, success: false

00100 --------/ 36%

Latest test results;
  Input: [0.00|1.00], output: [0.97], expectation: [1.00], error: 0.03, loss: 0.025948405
  Input: [0.00|0.00], output: [0.03], expectation: [0.00], error: -0.03, loss: 0.028365135
  Input: [1.00|1.00], output: [0.10], expectation: [0.00], error: -0.10, loss: 0.09843969
  Input: [1.00|0.00], output: [0.91], expectation: [1.00], error: 0.09, loss: 0.092502
  Average error: 0.06, average loss: 0.06, success: true
Trained epochs: 127, total average error: 40.892704, total average loss: 40.892704
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
2020-01-04 03:20:53:510 DBG nl.zeesoft.zdk.genetic.Evolver: Started
2020-01-04 03:20:54:129 DBG nl.zeesoft.zdk.genetic.Evolver: Selected new best genetic neural net;
- Code: 3978096847942984
- Size: 14
- Initial average loss: 1.06706 (final: 0.05724)
- Total average loss: 176.90158 (epochs: 609)
- Training result: 188.76456
2020-01-04 03:20:54:194 DBG nl.zeesoft.zdk.genetic.Evolver: Selected new best genetic neural net;
- Code: 7637846459123288
- Size: 14
- Initial average loss: 0.50299 (final: 0.03968)
- Total average loss: 100.49574 (epochs: 680)
- Training result: 50.54885
2020-01-04 03:20:56:218 DBG nl.zeesoft.zdk.genetic.Evolver: Selected new best genetic neural net;
- Code: 2273649095134928
- Size: 14
- Initial average loss: 0.49307 (final: 0.07139)
- Total average loss: 18.56124 (epochs: 56)
- Training result: 9.15203
2020-01-04 03:20:58:866 DBG nl.zeesoft.zdk.genetic.Evolver: Selected new best genetic neural net;
- Code: 1406216017187738
- Size: 14
- Initial average loss: 0.35252 (final: 0.08255)
- Total average loss: 5.84917 (epochs: 28)
- Training result: 2.06194
2020-01-04 03:20:59:522 DBG nl.zeesoft.zdk.genetic.Evolver: Selected new best genetic neural net;
- Code: 1406216017186738
- Size: 14
- Initial average loss: 0.27326 (final: 0.07276)
- Total average loss: 3.12699 (epochs: 19)
- Training result: 0.85448
2020-01-04 03:20:59:635 DBG nl.zeesoft.zdk.genetic.Evolver: Selected new best genetic neural net;
- Code: 1406216017186735
- Size: 14
- Initial average loss: 0.26654 (final: 0.07772)
- Total average loss: 2.89196 (epochs: 17)
- Training result: 0.77082
2020-01-04 03:20:59:668 DBG nl.zeesoft.zdk.genetic.Evolver: Selected new best genetic neural net;
- Code: 1406216017186745
- Size: 14
- Initial average loss: 0.25283 (final: 0.07802)
- Total average loss: 2.64202 (epochs: 16)
- Training result: 0.66798
2020-01-04 03:20:59:928 DBG nl.zeesoft.zdk.genetic.Evolver: Selected new best genetic neural net;
- Code: 1406216017186745
- Size: 14
- Initial average loss: 0.25000 (final: 0.00000)
- Total average loss: 0.50000 (epochs: 3)
- Training result: 0.12500
2020-01-04 03:20:59:992 DBG nl.zeesoft.zdk.genetic.Evolver: Selected new best genetic neural net;
- Code: 1406216017187745
- Size: 14
- Initial average loss: 0.25000 (final: 0.00000)
- Total average loss: 0.25000 (epochs: 2)
- Training result: 0.06250
2020-01-04 03:21:00:554 DBG nl.zeesoft.zdk.genetic.Evolver: Selected new best genetic neural net;
- Code: 1406216015187745
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
    "2020-01-04 03:20:54:129 SEL code: 3978096847942984, size: 14, initial loss: 1.06706 (final: 0.05724), total loss: 176.90158, result: 188.76456 (epochs: 609)",
    "2020-01-04 03:20:54:194 SEL code: 7637846459123288, size: 14, initial loss: 0.50299 (final: 0.03968), total loss: 100.49574, result: 50.54885 (epochs: 680)",
    "2020-01-04 03:20:56:218 SEL code: 2273649095134928, size: 14, initial loss: 0.49307 (final: 0.07139), total loss: 18.56124, result: 9.15203 (epochs: 56)",
    "2020-01-04 03:20:58:866 SEL code: 1406216017187738, size: 14, initial loss: 0.35252 (final: 0.08255), total loss: 5.84917, result: 2.06194 (epochs: 28)",
    "2020-01-04 03:20:59:522 SEL code: 1406216017186738, size: 14, initial loss: 0.27326 (final: 0.07276), total loss: 3.12699, result: 0.85448 (epochs: 19)",
    "2020-01-04 03:20:59:635 SEL code: 1406216017186735, size: 14, initial loss: 0.26654 (final: 0.07772), total loss: 2.89196, result: 0.77082 (epochs: 17)",
    "2020-01-04 03:20:59:668 SEL code: 1406216017186745, size: 14, initial loss: 0.25283 (final: 0.07802), total loss: 2.64202, result: 0.66798 (epochs: 16)",
    "2020-01-04 03:20:59:929 SEL code: 1406216017186745, size: 14, initial loss: 0.25000 (final: 0.00000), total loss: 0.50000, result: 0.12500 (epochs: 3)",
    "2020-01-04 03:20:59:992 SEL code: 1406216017187745, size: 14, initial loss: 0.25000 (final: 0.00000), total loss: 0.25000, result: 0.06250 (epochs: 2)",
    "2020-01-04 03:21:00:554 SEL code: 1406216015187745, size: 14, initial loss: 0.00000 (final: 0.00000), total loss: 0.00000, result: 0.00000 (epochs: 0)"
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
          "code": "KCaJQJ:HxL3IVC7G#DGGiBUKmF#D2IOEhDdLTO~AaLqNnBBL8LTNUP7AJL3KwJaAMHUM2",
          "neuralNet": [
            {
              "inputNeurons": 2,
              "hiddenLayers": 1,
              "hiddenNeurons": 2,
              "outputNeurons": 1,
              "weightFunction": "nl.zeesoft.zdk.functions.ZWeightXavier",
              "biasFunction": "nl.zeesoft.zdk.functions.ZWeightKaiming",
              "activator": "nl.zeesoft.zdk.functions.ZLeakyReLU",
              "outputActivator": "nl.zeesoft.zdk.functions.ZSoftmaxTop",
              "learningRate": 0.122,
              "values": [
                "2,1,1.0,1.0",
                "2,1,0.09787437,-0.001209924",
                "1,1,0.0"
              ],
              "weights": [
                "1,1,0.0",
                "2,2,0.82158387,-1.0143822,-0.46008697,0.5236228",
                "1,2,0.62217045,0.8916144"
              ],
              "biases": [
                "1,1,0.0",
                "2,1,0.29067275,-0.18452825",
                "1,1,-0.18452825"
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
                  "inputs": "0.0,1.0",
                  "outputs": "1.0",
                  "expectations": "1.0",
                  "errors": "0.0"
                },
                {
                  "inputs": "1.0,0.0",
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
                  "inputs": "0.0,1.0",
                  "outputs": "1.0",
                  "expectations": "1.0",
                  "errors": "0.0"
                },
                {
                  "inputs": "1.0,0.0",
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
SDR map: 100,2|82,77|83,38
Number of SDR A matches in SDR map: 1
SDR C: 100,86,73
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
SDR for value 0:  00000000000000000000001010000000000000001000000001
SDR for value 1:  00000000000000000000001010000000000000000100000001
SDR for value 24: 00000000000100001000000000000000000000010001000000
SDR for value 25: 00000000000100000000000000000100000000010001000000
SDR for value 75: 00000000000000000000001010100000000100000000000000
SDR for value 76: 00000000000000000000001000100000001100000000000000
SDR for value -1: 00000000000010000000000010000000000000001000000001
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
SDR for 2019-01-01 00:00:00:000, value1: 0, value2: 0; 11111100000000000000000000000000000000000000001111111110000000000000000000000000000000000000000100111000000010000000000110000000000000110000100000010000100001000100000001100100000000111111110000000000
SDR for 2019-02-02 01:00:00:000, value1: 1, value2: 2; 11111111000000000000000000000000000000000000000000001111111100000000000000000000000000000000000000111000000010000100000010000000000000110000000000010000100001000101000001100100111100000000000000001111
SDR for 2019-03-03 02:00:00:000, value1: 2, value2: 4; 00111111110000000000000000000000000000000000000000000000111111110000000000000000000000000000000000111000000000000100000010000001000000110000000001010001100001000100000001000100111111110000000000000000
SDR for 2019-04-04 03:00:00:000, value1: 3, value2: 6; 00111111110000000000000000000000000000000000000000000000000011111111000000000000000000000000000000011000000000000100000010001001000000110010000000010001100000000100000011000100000000000000011111111000
SDR for 2019-05-05 04:00:00:000, value1: 4, value2: 8; 00001111111100000000000000000000000000000000000000000000000000001111111100000000000000000000000000011000000000000100000010000001100000110000000000010001100000000000001111000100111111110000000000000000

Encoder StringBuilder:
VALUE1=0.0,2,3,4,38,23,39,24,12;1.0,17,2,3,4,38,39,24,12;2.0,17,2,3,4,38,39,24,31;3.0,17,3,4,38,39,24,28,31;4.0,32,17,3,4,38,39,24,31|VALUE2=0.0,16,33,34,4,21,37,25,11;1.0,16,33,1,34,21,37,25,11;2.0,16,33,34,21,37,25,11,27;3.0,16,33,34,21,37,25,9,11;4.0,16,33,21,37,25,9,11,15;5.0,16,32,33,37,25,9,11,15;6.0,16,32,33,2,37,25,11,15;7.0,16,32,33,37,25,11,15,31;8.0,16,32,33,37,11,30,15,31
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
Initializing pooler took: 28 ms
Randomizing connections took: 86 ms

Processing input SDR map (learning: false) ...
Processing input SDR map took: 13975 ms

Performance statistics;
calculateOverlapScores:        6458.362 ms
selectActiveColumns:            968.230 ms
logActivity:                   1037.347 ms
calculateColumnGroupActivity:  3888.961 ms
updateBoostFactors:            1311.465 ms
total:                        13740.384 ms
logSize:                          15330   
avgPerLog:                        0.896 ms

Combined average: 0.36313802, Combined weekly average: 4.220232

Pooler input dimensions: 16*16, output dimensions: 32*32
- Total proximal links: 92160, active: 46113
- Average proximal inputs per column: 90
- Column groups: 144, average columns per group: 441

Processing input SDR map (learning: true) ...
Processing input SDR map took: 13074 ms

Performance statistics;
calculateOverlapScores:        3309.185 ms
selectActiveColumns:           1010.311 ms
logActivity:                   1097.162 ms
calculateColumnGroupActivity:  3152.580 ms
updateBoostFactors:            1296.933 ms
total:                        12865.558 ms
learnActiveColumnsOnBits:      2927.622 ms
logSize:                          15330   
avgPerLog:                        0.839 ms

Combined average: 0.2937948, Combined weekly average: 7.1040454

Original ratio: 11.621565, learned ratio: 24.180296
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
Processed SDRs: 500, bursting average: 4 (max: 11)
Processed SDRs: 1000, bursting average: 2 (max: 10)
Processed SDRs: 1500, bursting average: 1 (max: 6)
Processed SDRs: 2000, bursting average: 1 (max: 4)
Processed SDRs: 2500, bursting average: 1 (max: 5)
Processed SDRs: 3000, bursting average: 1 (max: 4)
Processed SDRs: 3500, bursting average: 0 (max: 3)
Processed SDRs: 4000, bursting average: 0 (max: 6)
Processed SDRs: 4500, bursting average: 0 (max: 4)
Processed SDRs: 5000, bursting average: 0 (max: 2)
Processing input SDR map took: 14987 ms

Performance statistics;
cycleActiveState:       219.259 ms
activateColumnCells:    230.554 ms
calculateActivity:     1008.599 ms
selectPredictiveCells:  444.251 ms
updatePredictions:     4546.754 ms
total:                 6459.825 ms
logSize:                   5000   
avgPerLog:                1.292 ms

Memory dimensions: 32*32*4
- Total distal links: 232673, active: 220175
- Average distal inputs per memory cell: 56 (min: 21, max: 105)
- Average connected distal inputs per memory cell: 53 (min: 12, max: 105)
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
  - Total proximal links: 90304, active: 45141
  - Average proximal inputs per column: 88 (min: 82, max: 90)
  - Column groups: 144, average columns per group: 441
- Column 02-01 = Pooler input dimensions: 16*16, output dimensions: 32*32
  - Total proximal links: 92160, active: 46006
  - Average proximal inputs per column: 90
  - Column groups: 144, average columns per group: 441
- Column 02-03 = Pooler input dimensions: 14*14, output dimensions: 32*32
  - Total proximal links: 91776, active: 45685
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
      "includeSecond": true,
      "valueKey": "DATETIME"
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

2020-01-04 03:21:51:519 DBG nl.zeesoft.zdk.htm.grid.ZGrid: Starting grid ...
2020-01-04 03:21:51:521 DBG nl.zeesoft.zdk.htm.grid.ZGrid: Started grid
Added requests
Processed requests: 100, accuracy: 0.495
Processed requests: 200, accuracy: 0.712
Processed requests: 300, accuracy: 0.799
Processed requests: 400, accuracy: 0.849
Processed requests: 500, accuracy: 0.880
Processed requests: 600, accuracy: 0.900
Processed requests: 700, accuracy: 0.914
Processed requests: 800, accuracy: 0.925
Processed requests: 900, accuracy: 0.933
Processed requests: 1000, accuracy: 0.940
Processed requests: 1100, accuracy: 0.989
Processed requests: 1200, accuracy: 0.997
Processed requests: 1300, accuracy: 1.000
Processed requests: 1400, accuracy: 1.000
Processed requests: 1500, accuracy: 1.000
Processed requests: 1600, accuracy: 1.000
Processed requests: 1700, accuracy: 1.000
Processed requests: 1800, accuracy: 1.000
Processed requests: 1900, accuracy: 1.000
Processed requests: 2000, accuracy: 1.000
Processed requests: 2100, accuracy: 1.000 (!)
Processed requests: 2200, accuracy: 1.000
Processed requests: 2300, accuracy: 0.999
Processed requests: 2400, accuracy: 0.999
Processed requests: 2500, accuracy: 0.999
Processed requests: 2600, accuracy: 0.999
Processed requests: 2700, accuracy: 0.999
Detected anomaly at id 2742, detected: 0.4285714, average: 0.9851436, difference: 0.39369476
Detected anomaly at id 2743, detected: 0.52380955, average: 0.98400074, difference: 0.305205
Detected anomaly at id 2746, detected: 0.4285714, average: 0.9815246, difference: 0.39213872
Processed requests: 2800, accuracy: 0.959
Processed requests: 2900, accuracy: 0.959
Processed requests: 3000, accuracy: 0.959
2020-01-04 03:22:04:654 DBG nl.zeesoft.zdk.htm.grid.ZGrid: Stopping grid ...
2020-01-04 03:22:04:826 DBG nl.zeesoft.zdk.htm.grid.ZGrid: Stopped grid
Processing 3000 requests took 13401 ms

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
  - Total proximal links: 90304, active: 31160
  - Average proximal inputs per column: 88 (min: 82, max: 90)
  - Column groups: 144, average columns per group: 441
- Column 02-01 = Pooler input dimensions: 16*16, output dimensions: 32*32
  - Total proximal links: 92160, active: 34239
  - Average proximal inputs per column: 90
  - Column groups: 144, average columns per group: 441
- Column 02-03 = Pooler input dimensions: 14*14, output dimensions: 32*32
  - Total proximal links: 91776, active: 34569
  - Average proximal inputs per column: 89 (min: 87, max: 90)
  - Column groups: 144, average columns per group: 441
- Column 03-01 = Memory dimensions: 32*32*4
  - Total distal links: 204308, active: 190722
  - Average distal inputs per memory cell: 49 (min: 0, max: 126)
  - Average connected distal inputs per memory cell: 46 (min: 0, max: 126)
  - Average connected distal context inputs per memory cell: 33 (min: 0, max: 84)
- Column 04-00 = Detector start: 2500, window long/short: 500/1, threshold: 0.3 (ACTIVE)
- Column 04-01 = Classifier value key: VALUE, predict steps: 1

Grid state data;
- 02-00: 1125522
- 02-01: 1157385
- 02-03: 1104811
- 03-01: 4049938
- 04-00: 3099
- 04-01: 122444
- SELF: 25
Loading state data took 2520 ms
~~~~

Test results
------------
All 24 tests have been executed successfully (279287 assertions).  
Total test duration: 77296 ms (total sleep duration: 20800 ms).  

Memory usage per test;  
 * nl.zeesoft.zdk.test.impl.TestZStringEncoder: 757 Kb / 0 Mb
 * nl.zeesoft.zdk.test.impl.TestZStringSymbolParser: 524 Kb / 0 Mb
 * nl.zeesoft.zdk.test.impl.TestCsv: 531 Kb / 0 Mb
 * nl.zeesoft.zdk.test.impl.TestJson: 545 Kb / 0 Mb
 * nl.zeesoft.zdk.test.impl.TestZHttpRequest: 557 Kb / 0 Mb
 * nl.zeesoft.zdk.test.impl.TestMessenger: 763 Kb / 0 Mb
 * nl.zeesoft.zdk.test.impl.TestZMatrix: 829 Kb / 0 Mb
 * nl.zeesoft.zdk.test.impl.TestGeneticCode: 790 Kb / 0 Mb
 * nl.zeesoft.zdk.test.impl.TestNeuralNet: 2566 Kb / 2 Mb
 * nl.zeesoft.zdk.test.impl.TestGeneticNN: 22863 Kb / 22 Mb
 * nl.zeesoft.zdk.test.impl.TestEvolver: 25997 Kb / 25 Mb
 * nl.zeesoft.zdk.test.impl.htm.TestSDR: 23733 Kb / 23 Mb
 * nl.zeesoft.zdk.test.impl.htm.TestSDRMap: 18766 Kb / 18 Mb
 * nl.zeesoft.zdk.test.impl.htm.TestScalarEncoder: 946 Kb / 0 Mb
 * nl.zeesoft.zdk.test.impl.htm.TestRDScalarEncoder: 970 Kb / 0 Mb
 * nl.zeesoft.zdk.test.impl.htm.TestDateTimeEncoder: 977 Kb / 0 Mb
 * nl.zeesoft.zdk.test.impl.htm.TestDateTimeValuesEncoder: 972 Kb / 0 Mb
 * nl.zeesoft.zdk.test.impl.htm.TestDateTimeValueEncoder: 987 Kb / 0 Mb
 * nl.zeesoft.zdk.test.impl.htm.TestGridEncoder: 992 Kb / 0 Mb
 * nl.zeesoft.zdk.test.impl.htm.TestPooler: 34941 Kb / 34 Mb
 * nl.zeesoft.zdk.test.impl.htm.TestMemory: 34948 Kb / 34 Mb
 * nl.zeesoft.zdk.test.impl.htm.TestMerger: 34960 Kb / 34 Mb
 * nl.zeesoft.zdk.test.impl.htm.TestZGridColumnEncoders: 35025 Kb / 34 Mb
 * nl.zeesoft.zdk.test.impl.htm.TestZGrid: 35220 Kb / 34 Mb
