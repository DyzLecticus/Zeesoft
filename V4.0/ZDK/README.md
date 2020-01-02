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
Key: 4429276438621880245503967199698420242446991881763220064623496372
Input text: Hello, my name is 'Dyz Lecticus'. How are you feeling today? :-) (Don't you know how to: [re;spond]!).

Key encoded text: 7DsDwCoHrC0F2ECErDHG4FKCHCUHgFpAfC:CrDwDJBxDxGUFEGCC3IDIvFXIPHrCQBh~#B#DjCxCFEHGKI0GTC5HEHsA2G1FBDWCJClAEB4EVE6FVCKDhEXHYEmCPAf~vCwCSCrHqC5FkFtCrDVHaFKBFC4HuGABXBFEyEiEo~rDIICFAFxB1I~IRFVIMHyDPCkASCnC7BID0
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
2020-01-02 15:06:43:529 DBG nl.zeesoft.zdk.test.impl.TestMessengerListener: Test log debug message before Messenger has started
2020-01-02 15:06:43:838 ERR nl.zeesoft.zdk.test.impl.TestMessengerListener: Test log error message while Messenger is working
2020-01-02 15:06:43:838 ERR nl.zeesoft.zdk.test.impl.TestMessengerListener: Test log exception stack trace
java.lang.NumberFormatException: For input string: "A"
	at java.lang.NumberFormatException.forInputString(NumberFormatException.java:65)
	at java.lang.Integer.parseInt(Integer.java:580)
	at java.lang.Integer.parseInt(Integer.java:615)
	at nl.zeesoft.zdk.test.impl.TestMessenger.test(TestMessenger.java:83)
	at nl.zeesoft.zdk.test.Tester.test(Tester.java:71)
	at nl.zeesoft.zdk.test.LibraryObject.describeAndTest(LibraryObject.java:39)
	at nl.zeesoft.zdk.test.impl.ZDK.main(ZDK.java:52)

2020-01-02 15:06:44:172 WRN nl.zeesoft.zdk.test.impl.TestMessengerListener: Test log warning message after Messenger has stopped
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
000.24 | 000.04 | -00.62
-------+--------+-------
000.81 | -00.04 | -00.91

Randomized multiplied element wise;
007.18 | 001.17 | -18.62
-------+--------+-------
024.41 | -01.07 | -27.34

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
000.44 | 000.91 | 000.26
-------+--------+-------
-00.23 | -00.09 | 000.68

Randomized matrix transposed;
000.44 | -00.23
-------+-------
000.91 | -00.09
-------+-------
000.26 | 000.68
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
Genetic code: 4526916895395712882829283807926969801121358771395952048926157443250388637595864917586165034332421354
Mutated code: 4126916895395712882829243807926969801121358771395952048929157443250388637595814917586165034332411354
               ^                     ^                                 ^                   ^                 ^    

Scaled property values;
0: 882 <
1: 759
2: 375
3: 503
4: 637
5: 801
6: 877
7: 269
8: 520 <
9: 34
10: 758
11: 882
12: 438 <
13: 926
14: 895
15: 11
16: 616
17: 877
18: 571
19: 712 <
20: 412 <
21: 915 <
22: 696
23: 282
24: 691
25: 324
26: 814 <
27: 916
28: 354
29: 165
30: 814 <
31: 157
32: 395 <
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
  Input: [1.00|1.00], output: [0.17], expectation: [0.00], error: -0.17, loss: 0.16847189
  Input: [0.00|1.00], output: [-0.00], expectation: [1.00], error: 1.00, loss: 1.0000069
  Input: [1.00|0.00], output: [0.30], expectation: [1.00], error: 0.70, loss: 0.6956122
  Average error: 0.47, average loss: 0.47, success: false
Latest test results;
  Input: [1.00|1.00], output: [0.29], expectation: [0.00], error: -0.29, loss: 0.29079437
  Input: [1.00|0.00], output: [0.98], expectation: [1.00], error: 0.02, loss: 0.020113587
  Input: [0.00|0.00], output: [0.07], expectation: [0.00], error: -0.07, loss: 0.074795455
  Input: [0.00|1.00], output: [0.62], expectation: [1.00], error: 0.38, loss: 0.38407743
  Average error: 0.19, average loss: 0.19, success: false
Trained epochs: 10000, total average error: 3316.738, total average loss: 3316.738
================================================================================
Neural net activator: nl.zeesoft.zdk.functions.ZLeakyReLU, output activator: nl.zeesoft.zdk.functions.ZSoftmaxTop, learning rate: 0.1
Initial test results;
  Input: [0.00|0.00], output: [1.00], expectation: [0.00], error: -1.00, loss: 1.0
  Input: [1.00|1.00], output: [1.00], expectation: [0.00], error: -1.00, loss: 1.0
  Input: [0.00|1.00], output: [1.00], expectation: [1.00], error: 0.00, loss: 0.0
  Input: [1.00|0.00], output: [1.00], expectation: [1.00], error: 0.00, loss: 0.0
  Average error: 0.50, average loss: 0.50, success: false
Latest test results;
  Input: [1.00|0.00], output: [1.00], expectation: [1.00], error: 0.00, loss: 0.0
  Input: [1.00|1.00], output: [1.00], expectation: [0.00], error: -1.00, loss: 1.0
  Input: [0.00|0.00], output: [0.00], expectation: [0.00], error: 0.00, loss: 0.0
  Input: [0.00|1.00], output: [0.00], expectation: [1.00], error: 1.00, loss: 1.0
  Average error: 0.50, average loss: 0.50, success: false
Trained epochs: 10000, total average error: 5117.25, total average loss: 5117.25

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
    "2,1,-0.05778248,0.47807392",
    "1,1,0.0"
  ],
  "weights": [
    "1,1,0.0",
    "2,2,-0.33326536,-1.1008916,-0.008438516,-0.03850928",
    "1,2,0.6701484,-0.18756711"
  ],
  "biases": [
    "1,1,0.0",
    "2,1,-4.676016,0.47906977",
    "1,1,0.1220597"
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
Neural net activator: nl.zeesoft.zdk.functions.ZReLU, output activator: nl.zeesoft.zdk.functions.ZSoftmaxTop, learning rate: 0.0354
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
  Input: [1.00|0.00], output: [1.00], expectation: [1.00], error: 0.00, loss: 0.0
  Input: [0.00|1.00], output: [1.00], expectation: [1.00], error: 0.00, loss: 0.0
  Input: [0.00|0.00], output: [1.00], expectation: [0.00], error: -1.00, loss: 1.0
  Average error: 0.50, average loss: 0.50, success: false
Trained epochs: 10000, total average error: 5000.0, total average loss: 5000.0
================================================================================
Neural net activator: nl.zeesoft.zdk.functions.ZReLU, output activator: nl.zeesoft.zdk.functions.ZSoftmaxTop, learning rate: 0.08180001
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
  Input: [1.00|0.00], output: [1.00], expectation: [1.00], error: 0.00, loss: 0.0
  Input: [0.00|1.00], output: [1.00], expectation: [1.00], error: 0.00, loss: 0.0
  Input: [0.00|0.00], output: [1.00], expectation: [0.00], error: -1.00, loss: 1.0
  Input: [1.00|1.00], output: [1.00], expectation: [0.00], error: -1.00, loss: 1.0
  Average error: 0.50, average loss: 0.50, success: false
Trained epochs: 10000, total average error: 5000.0, total average loss: 5000.0
================================================================================
Neural net activator: nl.zeesoft.zdk.functions.ZReLU, output activator: nl.zeesoft.zdk.functions.ZReLU, learning rate: 0.1446
Initial test results;
  Input: [0.00|0.00], output: [1.64], expectation: [0.00], error: -1.64, loss: 1.6396787
  Input: [1.00|1.00], output: [2.72], expectation: [0.00], error: -2.72, loss: 2.7247658
  Input: [0.00|1.00], output: [2.68], expectation: [1.00], error: -1.68, loss: 1.6770945
  Input: [1.00|0.00], output: [1.29], expectation: [1.00], error: -0.29, loss: 0.2855966
  Average error: 1.58, average loss: 1.58, success: false

00100 -------/ 33%
00200 ------------\ 53%
00300 -------/ 34%
00400 ---------\ 42%
00500 -------/ 34%
00600 -------| 34%
00700 -------/ 32%
00800 --------\ 38%
00900 ------/ 31%
01000 ----/ 23%
01100 ----/ 22%
01200 ----| 22%
01300 ----| 22%
01400 ----/ 21%
01500 ----\ 23%
01600 ----| 23%
01700 ----/ 22%
01800 ----| 22%
01900 ----\ 23%
02000 ----/ 22%
02100 ----/ 21%
02200 ----\ 22%
02300 ----/ 21%
02400 ----\ 23%
02500 ----/ 22%
02600 ----| 22%
02700 ----\ 23%
02800 ----/ 21%
02900 ----\ 23%
03000 ----| 23%
03100 ----/ 22%
03200 ----\ 23%
03300 ----/ 22%
03400 ----| 22%
03500 ----| 22%
03600 ----/ 21%
03700 ----\ 22%
03800 ----\ 23%
03900 ----| 23%
04000 ----/ 21%
04100 ----\ 22%
04200 ----| 22%
04300 ----\ 23%
04400 ----| 23%
04500 ----/ 21%
04600 ----| 21%
04700 ----\ 23%
04800 ----/ 21%
04900 ----\ 23%
05000 ----/ 21%
05100 ----\ 22%
05200 ----\ 23%
05300 ----/ 22%
05400 ----| 22%
05500 ----| 22%
05600 ----/ 21%
05700 ----| 21%
05800 ----\ 23%
05900 ----| 23%
06000 ----/ 21%
06100 ----\ 22%
06200 ----| 22%
06300 ----| 22%
06400 ----| 22%
06500 ----/ 21%
06600 ----\ 22%
06700 ----| 22%
06800 ----| 22%
06900 ----| 22%
07000 ----\ 23%
07100 ----/ 22%
07200 ----/ 21%
07300 ----\ 23%
07400 ----/ 22%
07500 ----| 22%
07600 ----\ 23%
07700 ----/ 21%
07800 ----\ 22%
07900 ----| 22%
08000 ----/ 21%
08100 ----\ 22%
08200 ----\ 23%
08300 ----| 23%
08400 ----/ 22%
08500 ----\ 23%
08600 ----| 23%
08700 ----/ 22%
08800 ----/ 21%
08900 ----\ 22%
09000 ----\ 23%
09100 ----/ 22%
09200 ----| 22%
09300 ----/ 21%
09400 ----\ 23%
09500 ----/ 22%
09600 ----\ 23%
09700 ----/ 22%
09800 ----\ 23%
09900 ----/ 22%
10000 ----| 22%

Latest test results;
  Input: [1.00|1.00], output: [0.00], expectation: [0.00], error: 0.00, loss: 0.0
  Input: [0.00|0.00], output: [0.69], expectation: [0.00], error: -0.69, loss: 0.6883124
  Input: [1.00|0.00], output: [0.59], expectation: [1.00], error: 0.41, loss: 0.41121757
  Input: [0.00|1.00], output: [0.65], expectation: [1.00], error: 0.35, loss: 0.3517555
  Average error: 0.36, average loss: 0.36, success: false
Trained epochs: 10000, total average error: 3797.572, total average loss: 3797.572
================================================================================
Neural net activator: nl.zeesoft.zdk.functions.ZLeakyReLU, output activator: nl.zeesoft.zdk.functions.ZTanH, learning rate: 0.0866
Initial test results;
  Input: [0.00|0.00], output: [-0.49], expectation: [0.00], error: 0.49, loss: 0.49048072
  Input: [1.00|1.00], output: [-0.15], expectation: [0.00], error: 0.15, loss: 0.14759442
  Input: [0.00|1.00], output: [-0.39], expectation: [1.00], error: 1.39, loss: 1.3869997
  Input: [1.00|0.00], output: [-0.13], expectation: [1.00], error: 1.13, loss: 1.1296941
  Average error: 0.79, average loss: 0.79, success: false

00100 ----------/ 44%
00200 ---------/ 43%
00300 ---------| 43%
00400 ---------| 43%
00500 ---------| 43%
00600 --/ 13%

Latest test results;
  Input: [0.00|1.00], output: [0.91], expectation: [1.00], error: 0.09, loss: 0.09477824
  Input: [0.00|0.00], output: [0.04], expectation: [0.00], error: -0.04, loss: 0.04206415
  Input: [1.00|0.00], output: [0.90], expectation: [1.00], error: 0.10, loss: 0.09808242
  Input: [1.00|1.00], output: [0.02], expectation: [0.00], error: -0.02, loss: 0.024209656
  Average error: 0.06, average loss: 0.06, success: true
Trained epochs: 652, total average error: 202.79807, total average loss: 202.79807
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
2020-01-02 15:06:45:401 DBG nl.zeesoft.zdk.genetic.Evolver: Started
2020-01-02 15:06:45:560 DBG nl.zeesoft.zdk.genetic.Evolver: Selected new best genetic neural net;
- Code: 1964892260841816
- Size: 14
- Initial average loss: 0.50000 (final: 0.00000)
- Total average loss: 64.50000 (epochs: 114)
- Training result: 32.25000
2020-01-02 15:06:45:591 DBG nl.zeesoft.zdk.genetic.Evolver: Selected new best genetic neural net;
- Code: 5840821225150472
- Size: 14
- Initial average loss: 0.48158 (final: 0.05188)
- Total average loss: 47.14985 (epochs: 127)
- Training result: 22.70665
2020-01-02 15:06:45:669 DBG nl.zeesoft.zdk.genetic.Evolver: Selected new best genetic neural net;
- Code: 2919966610749441
- Size: 14
- Initial average loss: 0.43439 (final: 0.07711)
- Total average loss: 42.55477 (epochs: 181)
- Training result: 18.48545
2020-01-02 15:06:45:797 DBG nl.zeesoft.zdk.genetic.Evolver: Selected new best genetic neural net;
- Code: 3186889898145520
- Size: 14
- Initial average loss: 0.25000 (final: 0.00000)
- Total average loss: 31.50000 (epochs: 51)
- Training result: 7.87500
2020-01-02 15:06:49:030 DBG nl.zeesoft.zdk.genetic.Evolver: Selected new best genetic neural net;
- Code: 3686889898145520
- Size: 14
- Initial average loss: 0.25000 (final: 0.00000)
- Total average loss: 18.25000 (epochs: 30)
- Training result: 4.56250
2020-01-02 15:06:49:061 DBG nl.zeesoft.zdk.genetic.Evolver: Selected new best genetic neural net;
- Code: 1643070671140336
- Size: 14
- Initial average loss: 0.25000 (final: 0.00000)
- Total average loss: 5.25000 (epochs: 17)
- Training result: 1.31250
2020-01-02 15:06:49:336 DBG nl.zeesoft.zdk.genetic.Evolver: Selected new best genetic neural net;
- Code: 1643070670140336
- Size: 14
- Initial average loss: 0.25000 (final: 0.00000)
- Total average loss: 4.00000 (epochs: 13)
- Training result: 1.00000
2020-01-02 15:06:50:051 DBG nl.zeesoft.zdk.genetic.Evolver: Selected new best genetic neural net;
- Code: 1643070670140336
- Size: 14
- Initial average loss: 0.25000 (final: 0.00000)
- Total average loss: 2.50000 (epochs: 6)
- Training result: 0.62500
2020-01-02 15:06:50:306 DBG nl.zeesoft.zdk.genetic.Evolver: Selected new best genetic neural net;
- Code: 1643010670140336
- Size: 14
- Initial average loss: 0.25000 (final: 0.00000)
- Total average loss: 2.00000 (epochs: 7)
- Training result: 0.50000
2020-01-02 15:06:50:642 DBG nl.zeesoft.zdk.genetic.Evolver: Selected new best genetic neural net;
- Code: 1643017670140336
- Size: 14
- Initial average loss: 0.25000 (final: 0.00000)
- Total average loss: 1.00000 (epochs: 4)
- Training result: 0.25000
2020-01-02 15:06:51:074 DBG nl.zeesoft.zdk.genetic.Evolver: Selected new best genetic neural net;
- Code: 1643010670140336
- Size: 14
- Initial average loss: 0.25000 (final: 0.00000)
- Total average loss: 0.50000 (epochs: 2)
- Training result: 0.12500
2020-01-02 15:06:51:095 DBG nl.zeesoft.zdk.genetic.Evolver: Selected new best genetic neural net;
- Code: 1243010670140336
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
    "2020-01-02 15:06:45:560 SEL code: 1964892260841816, size: 14, initial loss: 0.50000 (final: 0.00000), total loss: 64.50000, result: 32.25000 (epochs: 114)",
    "2020-01-02 15:06:45:591 SEL code: 5840821225150472, size: 14, initial loss: 0.48158 (final: 0.05188), total loss: 47.14985, result: 22.70665 (epochs: 127)",
    "2020-01-02 15:06:45:669 SEL code: 2919966610749441, size: 14, initial loss: 0.43439 (final: 0.07711), total loss: 42.55477, result: 18.48545 (epochs: 181)",
    "2020-01-02 15:06:45:797 SEL code: 3186889898145520, size: 14, initial loss: 0.25000 (final: 0.00000), total loss: 31.50000, result: 7.87500 (epochs: 51)",
    "2020-01-02 15:06:49:030 SEL code: 3686889898145520, size: 14, initial loss: 0.25000 (final: 0.00000), total loss: 18.25000, result: 4.56250 (epochs: 30)",
    "2020-01-02 15:06:49:061 SEL code: 1643070671140336, size: 14, initial loss: 0.25000 (final: 0.00000), total loss: 5.25000, result: 1.31250 (epochs: 17)",
    "2020-01-02 15:06:49:336 SEL code: 1643070670140336, size: 14, initial loss: 0.25000 (final: 0.00000), total loss: 4.00000, result: 1.00000 (epochs: 13)",
    "2020-01-02 15:06:50:051 SEL code: 1643070670140336, size: 14, initial loss: 0.25000 (final: 0.00000), total loss: 2.50000, result: 0.62500 (epochs: 6)",
    "2020-01-02 15:06:50:306 SEL code: 1643010670140336, size: 14, initial loss: 0.25000 (final: 0.00000), total loss: 2.00000, result: 0.50000 (epochs: 7)",
    "2020-01-02 15:06:50:642 SEL code: 1643017670140336, size: 14, initial loss: 0.25000 (final: 0.00000), total loss: 1.00000, result: 0.25000 (epochs: 4)",
    "2020-01-02 15:06:51:074 SEL code: 1643010670140336, size: 14, initial loss: 0.25000 (final: 0.00000), total loss: 0.50000, result: 0.12500 (epochs: 2)",
    "2020-01-02 15:06:51:095 SEL code: 1243010670140336, size: 14, initial loss: 0.00000 (final: 0.00000), total loss: 0.00000, result: 0.00000 (epochs: 0)"
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
          "code": "xBfECBOA8AjJiCuA#BoBLLULSB7E0CEE6AAKgDfBKHlGeGwOKNOJlAgE8HwMvJLN:I0B2",
          "neuralNet": [
            {
              "inputNeurons": 2,
              "hiddenLayers": 1,
              "hiddenNeurons": 2,
              "outputNeurons": 1,
              "weightFunction": "nl.zeesoft.zdk.functions.ZWeightDefault",
              "biasFunction": "nl.zeesoft.zdk.functions.ZWeightKaiming",
              "activator": "nl.zeesoft.zdk.functions.ZTanH",
              "outputActivator": "nl.zeesoft.zdk.functions.ZSoftmaxTop",
              "learningRate": 0.1224,
              "values": [
                "2,1,1.0,0.0",
                "2,1,-0.16460367,-0.8157417",
                "1,1,1.0"
              ],
              "weights": [
                "1,1,0.0",
                "2,2,-0.398,0.42200005,-0.77,0.676",
                "1,2,-0.472,0.34599996"
              ],
              "biases": [
                "1,1,0.0",
                "2,1,0.23188508,-0.37395546",
                "1,1,0.22861902"
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
                  "inputs": "0.0,0.0",
                  "outputs": "0.0",
                  "expectations": "0.0",
                  "errors": "0.0"
                },
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
SDR map: 100,2|66,14|97,20
Number of SDR A matches in SDR map: 1
SDR C: 100,5,92
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
SDR for value 0:  00000000001101000000000000000000000000010000000000
SDR for value 1:  00000000000101000000000000000000100000010000000000
SDR for value 24: 00000000100000000000000000000000000000000000001110
SDR for value 25: 00000000100000100000000000000000000000000000001010
SDR for value 75: 01000000000000100000000000000000000000000011000000
SDR for value 76: 01000000000000100100000000000000000000000001000000
SDR for value -1: 00000000000101000000000000000001000000010000000000
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
SDR for 2019-01-01 00:00:00:000, value1: 0, value2: 0; 11111100000000000000000000000000000000000000001111111110000000000000000000000000000000000000000110000010000001000000100010000000101000010000000000001010010100000010010100000100000000111111110000000000
SDR for 2019-02-02 01:00:00:000, value1: 1, value2: 2; 11111111000000000000000000000000000000000000000000001111111100000000000000000000000000000000000010000010000001000000100100000000101000010000000000001010010000100010000101000100111100000000000000001111
SDR for 2019-03-03 02:00:00:000, value1: 2, value2: 4; 00111111110000000000000000000000000000000000000000000000111111110000000000000000000000000000000010000010000001000010100100000000100000010000000000000010010001110010000101000000111111110000000000000000
SDR for 2019-04-04 03:00:00:000, value1: 3, value2: 6; 00111111110000000000000000000000000000000000000000000000000011111111000000000000000000000000000010000010000001000010110100000000000000010000001000000010010001110010000001000000000000000000011111111000
SDR for 2019-05-05 04:00:00:000, value1: 4, value2: 8; 00001111111100000000000000000000000000000000000000000000000000001111111100000000000000000000000010000010000001000000110100000001000000010000001000000010010001010110000001000000111111110000000000000000

Encoder StringBuilder:
VALUE1=0.0,32,0,34,20,6,39,24,13;1.0,32,0,34,20,6,39,23,13;2.0,32,0,18,20,6,39,23,13;3.0,0,18,20,21,6,39,23,13;4.0,0,20,21,6,39,23,13,31|VALUE2=0.0,17,19,37,26,12,29,14,31;1.0,17,33,19,37,26,12,14,31;2.0,17,33,37,22,26,12,14,31;3.0,17,33,22,23,26,12,14,31;4.0,17,33,21,22,23,26,14,31;5.0,17,33,20,21,22,23,26,14;6.0,17,33,21,22,6,23,26,14;7.0,17,33,21,6,23,24,26,14;8.0,17,33,21,6,23,25,26,14
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
Randomizing connections took: 83 ms

Processing input SDR map (learning: false) ...
Processing input SDR map took: 13168 ms

Performance statistics;
calculateOverlapScores:        6535.107 ms
selectActiveColumns:            871.838 ms
logActivity:                    794.852 ms
calculateColumnGroupActivity:  3472.421 ms
updateBoostFactors:            1220.585 ms
total:                        12956.576 ms
logSize:                          15330   
avgPerLog:                        0.845 ms

Combined average: 0.37003273, Combined weekly average: 5.1300564

Pooler input dimensions: 16*16, output dimensions: 32*32
- Total proximal links: 92160, active: 45711
- Average proximal inputs per column: 90
- Column groups: 144, average columns per group: 441

Processing input SDR map (learning: true) ...
Processing input SDR map took: 11394 ms

Performance statistics;
calculateOverlapScores:        2408.680 ms
selectActiveColumns:            882.723 ms
logActivity:                    873.097 ms
calculateColumnGroupActivity:  2709.418 ms
updateBoostFactors:            1221.860 ms
total:                        11198.509 ms
learnActiveColumnsOnBits:      3043.772 ms
logSize:                          15330   
avgPerLog:                        0.730 ms

Combined average: 0.29946375, Combined weekly average: 8.174567

Original ratio: 13.8637905, learned ratio: 27.297352
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
Processed SDRs: 500, bursting average: 4 (max: 15)
Processed SDRs: 1000, bursting average: 2 (max: 9)
Processed SDRs: 1500, bursting average: 1 (max: 7)
Processed SDRs: 2000, bursting average: 1 (max: 4)
Processed SDRs: 2500, bursting average: 1 (max: 7)
Processed SDRs: 3000, bursting average: 1 (max: 5)
Processed SDRs: 3500, bursting average: 0 (max: 5)
Processed SDRs: 4000, bursting average: 0 (max: 4)
Processed SDRs: 4500, bursting average: 0 (max: 3)
Processed SDRs: 5000, bursting average: 0 (max: 2)
Processing input SDR map took: 13754 ms

Performance statistics;
cycleActiveState:       201.751 ms
activateColumnCells:    207.614 ms
calculateActivity:      918.182 ms
selectPredictiveCells:  429.715 ms
updatePredictions:     4116.357 ms
total:                 5882.292 ms
logSize:                   5000   
avgPerLog:                1.176 ms

Memory dimensions: 32*32*4
- Total distal links: 229499, active: 216996
- Average distal inputs per memory cell: 56 (min: 0, max: 126)
- Average connected distal inputs per memory cell: 52 (min: 0, max: 113)
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
  - Total proximal links: 90304, active: 44998
  - Average proximal inputs per column: 88 (min: 82, max: 90)
  - Column groups: 144, average columns per group: 441
- Column 02-01 = Pooler input dimensions: 16*16, output dimensions: 32*32
  - Total proximal links: 92160, active: 46200
  - Average proximal inputs per column: 90
  - Column groups: 144, average columns per group: 441
- Column 02-03 = Pooler input dimensions: 14*14, output dimensions: 32*32
  - Total proximal links: 91776, active: 46296
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
2020-01-02 15:07:37:278 DBG nl.zeesoft.zdk.htm.grid.ZGrid: Starting grid ...
2020-01-02 15:07:37:280 DBG nl.zeesoft.zdk.htm.grid.ZGrid: Started grid
Processed requests: 100, accuracy: 0.463
Processed requests: 200, accuracy: 0.706
Processed requests: 300, accuracy: 0.793
Processed requests: 400, accuracy: 0.840
Processed requests: 500, accuracy: 0.870
Processed requests: 600, accuracy: 0.892
Processed requests: 700, accuracy: 0.907
Processed requests: 800, accuracy: 0.919
Processed requests: 900, accuracy: 0.928
Processed requests: 1000, accuracy: 0.933
Processed requests: 1100, accuracy: 0.985
Processed requests: 1200, accuracy: 0.990
Processed requests: 1300, accuracy: 0.992
Processed requests: 1400, accuracy: 0.986
Processed requests: 1500, accuracy: 0.986
Processed requests: 1600, accuracy: 0.985
Processed requests: 1700, accuracy: 0.981
Processed requests: 1800, accuracy: 0.975
Processed requests: 1900, accuracy: 0.970
Processed requests: 2000, accuracy: 0.968
Processed requests: 2100, accuracy: 0.968 (!)
Processed requests: 2200, accuracy: 0.960
Processed requests: 2300, accuracy: 0.959
Processed requests: 2400, accuracy: 0.960
Processed requests: 2500, accuracy: 0.962
Processed requests: 2600, accuracy: 0.966
Processed requests: 2700, accuracy: 0.966
Detected anomaly at id 2742, detected: 0.52380955, average: 0.9850483, difference: 0.3056873
Detected anomaly at id 2744, detected: 0.38095236, average: 0.9834292, difference: 0.44157505
Detected anomaly at id 2746, detected: 0.47619045, average: 0.9814292, difference: 0.34661907
Detected anomaly at id 2747, detected: 0.52380955, average: 0.9803816, difference: 0.30353326
Processed requests: 2800, accuracy: 0.929
Processed requests: 2900, accuracy: 0.930
Processed requests: 3000, accuracy: 0.928
2020-01-02 15:07:49:259 DBG nl.zeesoft.zdk.htm.grid.ZGrid: Stopping grid ...
2020-01-02 15:07:49:437 DBG nl.zeesoft.zdk.htm.grid.ZGrid: Stopped grid
Processing 3000 requests took 12178 ms

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
  - Total proximal links: 90304, active: 31580
  - Average proximal inputs per column: 88 (min: 82, max: 90)
  - Column groups: 144, average columns per group: 441
- Column 02-01 = Pooler input dimensions: 16*16, output dimensions: 32*32
  - Total proximal links: 92160, active: 34472
  - Average proximal inputs per column: 90
  - Column groups: 144, average columns per group: 441
- Column 02-03 = Pooler input dimensions: 14*14, output dimensions: 32*32
  - Total proximal links: 91776, active: 34822
  - Average proximal inputs per column: 89 (min: 87, max: 90)
  - Column groups: 144, average columns per group: 441
- Column 03-01 = Memory dimensions: 32*32*4
  - Total distal links: 204022, active: 190055
  - Average distal inputs per memory cell: 49 (min: 0, max: 126)
  - Average connected distal inputs per memory cell: 46 (min: 0, max: 126)
  - Average connected distal context inputs per memory cell: 32 (min: 0, max: 84)
- Column 04-00 = Detector start: 2500, window long/short: 500/1, threshold: 0.3 (ACTIVE)
- Column 04-01 = Classifier value key: VALUE, predict steps: 1

Grid state data;
- 02-00: 1130016
- 02-01: 1158797
- 02-03: 1109326
- 03-01: 4039069
- 04-00: 2997
- 04-01: 122408
- SELF: 25
Loading state data took 2167 ms
~~~~

Test results
------------
All 24 tests have been executed successfully (279287 assertions).  
Total test duration: 68874 ms (total sleep duration: 18100 ms).  

Memory usage per test;  
 * nl.zeesoft.zdk.test.impl.TestZStringEncoder: 757 Kb / 0 Mb
 * nl.zeesoft.zdk.test.impl.TestZStringSymbolParser: 524 Kb / 0 Mb
 * nl.zeesoft.zdk.test.impl.TestCsv: 531 Kb / 0 Mb
 * nl.zeesoft.zdk.test.impl.TestJson: 545 Kb / 0 Mb
 * nl.zeesoft.zdk.test.impl.TestZHttpRequest: 557 Kb / 0 Mb
 * nl.zeesoft.zdk.test.impl.TestMessenger: 763 Kb / 0 Mb
 * nl.zeesoft.zdk.test.impl.TestZMatrix: 829 Kb / 0 Mb
 * nl.zeesoft.zdk.test.impl.TestGeneticCode: 790 Kb / 0 Mb
 * nl.zeesoft.zdk.test.impl.TestNeuralNet: 1107 Kb / 1 Mb
 * nl.zeesoft.zdk.test.impl.TestGeneticNN: 12792 Kb / 12 Mb
 * nl.zeesoft.zdk.test.impl.TestEvolver: 8240 Kb / 8 Mb
 * nl.zeesoft.zdk.test.impl.htm.TestSDR: 6149 Kb / 6 Mb
 * nl.zeesoft.zdk.test.impl.htm.TestSDRMap: 932 Kb / 0 Mb
 * nl.zeesoft.zdk.test.impl.htm.TestScalarEncoder: 932 Kb / 0 Mb
 * nl.zeesoft.zdk.test.impl.htm.TestRDScalarEncoder: 956 Kb / 0 Mb
 * nl.zeesoft.zdk.test.impl.htm.TestDateTimeEncoder: 962 Kb / 0 Mb
 * nl.zeesoft.zdk.test.impl.htm.TestDateTimeValuesEncoder: 958 Kb / 0 Mb
 * nl.zeesoft.zdk.test.impl.htm.TestDateTimeValueEncoder: 973 Kb / 0 Mb
 * nl.zeesoft.zdk.test.impl.htm.TestGridEncoder: 978 Kb / 0 Mb
 * nl.zeesoft.zdk.test.impl.htm.TestPooler: 34940 Kb / 34 Mb
 * nl.zeesoft.zdk.test.impl.htm.TestMemory: 34947 Kb / 34 Mb
 * nl.zeesoft.zdk.test.impl.htm.TestMerger: 34959 Kb / 34 Mb
 * nl.zeesoft.zdk.test.impl.htm.TestZGridColumnEncoders: 35024 Kb / 34 Mb
 * nl.zeesoft.zdk.test.impl.htm.TestZGrid: 35220 Kb / 34 Mb
