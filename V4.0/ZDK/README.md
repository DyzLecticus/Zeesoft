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
Key: 8067848024710238915028245607482012199318792970561997519269736000
Input text: Hello, my name is 'Dyz Lecticus'. How are you feeling today? :-) (Don't you know how to: [re;spond]!).

Key encoded text: tgdb2fPgohxdNf1aTcgdjgrbZaycBcAhWhMaAdqabdnhxbOd1elfwbxgoeghtcP0LakbLbvi9h6b5bDhMgLgidking502etfecuigiAgHe8a4hUc3f8hEgScge30P0L0PfhaqfSgnhsd7gO0TcueqgraXaEcPdOhEgTbHecbNbhhIczdWddfubtgKdehqcWaKbnc7bihVgjc0
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
2019-09-01 22:14:59:155 DBG nl.zeesoft.zdk.test.impl.TestMessengerListener: Test log debug message before Messenger has started
2019-09-01 22:14:59:455 ERR nl.zeesoft.zdk.test.impl.TestMessengerListener: Test log error message while Messenger is working
2019-09-01 22:14:59:455 ERR nl.zeesoft.zdk.test.impl.TestMessengerListener: Test log exception stack trace
java.lang.NumberFormatException: For input string: "A"
	at java.lang.NumberFormatException.forInputString(NumberFormatException.java:65)
	at java.lang.Integer.parseInt(Integer.java:580)
	at java.lang.Integer.parseInt(Integer.java:615)
	at nl.zeesoft.zdk.test.impl.TestMessenger.test(TestMessenger.java:83)
	at nl.zeesoft.zdk.test.Tester.test(Tester.java:69)
	at nl.zeesoft.zdk.test.LibraryObject.describeAndTest(LibraryObject.java:39)
	at nl.zeesoft.zdk.test.impl.ZDK.main(ZDK.java:39)

2019-09-01 22:14:59:765 WRN nl.zeesoft.zdk.test.impl.TestMessengerListener: Test log warning message after Messenger has stopped
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
000.52 | -00.19 | 000.20
-------+--------+-------
000.14 | -00.78 | -00.42

Randomized multiplied element wise;
015.49 | -05.68 | 006.01
-------+--------+-------
004.18 | -23.38 | -12.54

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
-00.64 | 000.47 | -00.05
-------+--------+-------
-00.36 | -00.91 | -00.07

Randomized matrix transposed;
-00.64 | -00.36
-------+-------
000.47 | -00.91
-------+-------
-00.05 | -00.07
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
Genetic code: 1403675726900632242791302717710214145356832399523775739625644696681936722260000857890512421834284092
Mutated code: 1403675726400632242791302717710234145356832399543775739625644696681937722260000857890512421834684092
                        ^                     ^              ^                     ^                        ^     

Scaled property values;
0: 323
1: 356
2: 193
3: 772 <
4: 400 <
5: 23 <
6: 356
7: 102
8: 453
9: 242
10: 632
11: 625 <
12: 772 <
13: 124
14: 832
15: 913
16: 564 <
17: 789
18: 279
19: 8
20: 722
21: 409
22: 775
23: 675 <
24: 377
25: 234 <
26: 145
27: 6
28: 239
29: 962
30: 757
31: 578 <
32: 578
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
  Input: [1.00|1.00], output: [0.00], expectation: [0.00], error: -0.00, loss: 0.0012911228
  Input: [0.00|1.00], output: [-0.00], expectation: [1.00], error: 1.00, loss: 1.0001248
  Input: [1.00|0.00], output: [-0.00], expectation: [1.00], error: 1.00, loss: 1.0002359
  Average error: 0.50, average loss: 0.50, success: false
Latest test results;
  Input: [1.00|1.00], output: [0.52], expectation: [0.00], error: -0.52, loss: 0.52097183
  Input: [1.00|0.00], output: [0.47], expectation: [1.00], error: 0.53, loss: 0.53225756
  Input: [0.00|1.00], output: [0.52], expectation: [1.00], error: 0.48, loss: 0.47514468
  Input: [0.00|0.00], output: [0.57], expectation: [0.00], error: -0.57, loss: 0.57125884
  Average error: 0.52, average loss: 0.52, success: false
Trained epochs: 50000, total average error: 26187.57, total average loss: 26187.57
================================================================================
Neural net activator: nl.zeesoft.zdk.functions.ZLeakyReLU, output activator: nl.zeesoft.zdk.functions.ZSoftmaxTop, learning rate: 0.1
Initial test results;
  Input: [0.00|0.00], output: [0.00], expectation: [0.00], error: 0.00, loss: 0.0
  Input: [1.00|1.00], output: [0.00], expectation: [0.00], error: 0.00, loss: 0.0
  Input: [0.00|1.00], output: [0.00], expectation: [1.00], error: 1.00, loss: 1.0
  Input: [1.00|0.00], output: [0.00], expectation: [1.00], error: 1.00, loss: 1.0
  Average error: 0.50, average loss: 0.50, success: false
Latest test results;
  Input: [0.00|1.00], output: [1.00], expectation: [1.00], error: 0.00, loss: 0.0
  Input: [1.00|0.00], output: [1.00], expectation: [1.00], error: 0.00, loss: 0.0
  Input: [1.00|1.00], output: [1.00], expectation: [0.00], error: -1.00, loss: 1.0
  Input: [0.00|0.00], output: [0.00], expectation: [0.00], error: 0.00, loss: 0.0
  Average error: 0.25, average loss: 0.25, success: false
Trained epochs: 50000, total average error: 36157.25, total average loss: 36157.25

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
    "2,1,0.0,0.0",
    "2,1,-0.016499173,0.2229825",
    "1,1,0.0"
  ],
  "weights": [
    "1,1,0.0",
    "2,2,-0.90942776,-0.91948354,-0.055630192,-1.6126037E-4",
    "1,2,-0.35797298,0.35356164"
  ],
  "biases": [
    "1,1,0.0",
    "2,1,-1.6499172,0.2229825",
    "1,1,-0.09046388"
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
Neural net activator: nl.zeesoft.zdk.functions.ZLeakyReLU, output activator: nl.zeesoft.zdk.functions.ZSigmoid, learning rate: 0.1634
Initial test results;
  Input: [0.00|0.00], output: [0.50], expectation: [0.00], error: -0.50, loss: 0.5
  Input: [1.00|1.00], output: [0.44], expectation: [0.00], error: -0.44, loss: 0.4445699
  Input: [0.00|1.00], output: [0.46], expectation: [1.00], error: 0.54, loss: 0.54274595
  Input: [1.00|0.00], output: [0.49], expectation: [1.00], error: 0.51, loss: 0.5128056
  Average error: 0.50, average loss: 0.50, success: false
Latest test results;
  Input: [1.00|1.00], output: [0.03], expectation: [0.00], error: -0.03, loss: 0.028489502
  Input: [0.00|1.00], output: [0.90], expectation: [1.00], error: 0.10, loss: 0.099713564
  Input: [1.00|0.00], output: [0.90], expectation: [1.00], error: 0.10, loss: 0.099747896
  Input: [0.00|0.00], output: [0.03], expectation: [0.00], error: -0.03, loss: 0.02998577
  Average error: 0.06, average loss: 0.06, success: true
Trained epochs: 885, total average error: 267.9154, total average loss: 267.9154
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
2019-09-01 22:15:02:083 DBG nl.zeesoft.zdk.genetic.Evolver: Started
2019-09-01 22:15:02:088 DBG nl.zeesoft.zdk.genetic.Evolver: Selected new best genetic neural net;
- Code: 3154584262920720
- Size: 14
- Initial average loss: 0.50000 (final: 0.00000)
- Total average loss: 4.25000 (epochs: 9)
- Training result: 2.12500
2019-09-01 22:15:02:097 DBG nl.zeesoft.zdk.genetic.Evolver: Selected new best genetic neural net;
- Code: 3154984262920720
- Size: 14
- Initial average loss: 0.50000 (final: 0.00000)
- Total average loss: 3.50000 (epochs: 8)
- Training result: 1.75000
2019-09-01 22:15:02:147 DBG nl.zeesoft.zdk.genetic.Evolver: Selected new best genetic neural net;
- Code: 3154984262920720
- Size: 14
- Initial average loss: 0.50000 (final: 0.00000)
- Total average loss: 1.50000 (epochs: 5)
- Training result: 0.75000
2019-09-01 22:15:02:649 DBG nl.zeesoft.zdk.genetic.Evolver: Selected new best genetic neural net;
- Code: 3154986262920720
- Size: 14
- Initial average loss: 0.50000 (final: 0.00000)
- Total average loss: 1.25000 (epochs: 4)
- Training result: 0.62500
2019-09-01 22:15:05:368 DBG nl.zeesoft.zdk.genetic.Evolver: Selected new best genetic neural net;
- Code: 3154986262520720
- Size: 14
- Initial average loss: 0.50000 (final: 0.00000)
- Total average loss: 1.00000 (epochs: 3)
- Training result: 0.50000
2019-09-01 22:15:09:740 DBG nl.zeesoft.zdk.genetic.Evolver: Selected new best genetic neural net;
- Code: 3154286262520720
- Size: 14
- Initial average loss: 0.50000 (final: 0.00000)
- Total average loss: 0.75000 (epochs: 3)
- Training result: 0.37500
2019-09-01 22:15:31:013 DBG nl.zeesoft.zdk.genetic.Evolver: Selected new best genetic neural net;
- Code: 8154286262520720
- Size: 14
- Initial average loss: 0.50000 (final: 0.00000)
- Total average loss: 0.50000 (epochs: 2)
- Training result: 0.25000
2019-09-01 22:15:31:415 DBG nl.zeesoft.zdk.genetic.Evolver: Selected new best genetic neural net;
- Code: 8154286762520720
- Size: 14
- Initial average loss: 0.25000 (final: 0.00000)
- Total average loss: 0.25000 (epochs: 2)
- Training result: 0.06250
2019-09-01 22:15:31:614 DBG nl.zeesoft.zdk.genetic.Evolver: Selected new best genetic neural net;
- Code: 8154286762820720
- Size: 14
- Initial average loss: 0.00000 (final: 0.00000)
- Total average loss: 0.00000 (epochs: 0)
- Training result: 0.00000

Evolver JSON;
{
  "mutationRate": 0.05,
  "trainEpochBatches": 1000,
  "trainEpochBatchSize": 10,
  "checkFactorQuarter": 0.66,
  "checkFactorHalf": 0.33,
  "sleepMs": 10,
  "sleepMsFoundBest": 50,
  "log": [
    "2019-09-01 22:15:02:088 SEL code: 3154584262920720, size: 14, initial loss: 0.50000 (final: 0.00000), total loss: 4.25000, result: 2.12500 (epochs: 9)",
    "2019-09-01 22:15:02:097 SEL code: 3154984262920720, size: 14, initial loss: 0.50000 (final: 0.00000), total loss: 3.50000, result: 1.75000 (epochs: 8)",
    "2019-09-01 22:15:02:147 SEL code: 3154984262920720, size: 14, initial loss: 0.50000 (final: 0.00000), total loss: 1.50000, result: 0.75000 (epochs: 5)",
    "2019-09-01 22:15:02:649 SEL code: 3154986262920720, size: 14, initial loss: 0.50000 (final: 0.00000), total loss: 1.25000, result: 0.62500 (epochs: 4)",
    "2019-09-01 22:15:05:368 SEL code: 3154986262520720, size: 14, initial loss: 0.50000 (final: 0.00000), total loss: 1.00000, result: 0.50000 (epochs: 3)",
    "2019-09-01 22:15:09:740 SEL code: 3154286262520720, size: 14, initial loss: 0.50000 (final: 0.00000), total loss: 0.75000, result: 0.37500 (epochs: 3)",
    "2019-09-01 22:15:31:013 SEL code: 8154286262520720, size: 14, initial loss: 0.50000 (final: 0.00000), total loss: 0.50000, result: 0.25000 (epochs: 2)",
    "2019-09-01 22:15:31:415 SEL code: 8154286762520720, size: 14, initial loss: 0.25000 (final: 0.00000), total loss: 0.25000, result: 0.06250 (epochs: 2)",
    "2019-09-01 22:15:31:614 SEL code: 8154286762820720, size: 14, initial loss: 0.00000 (final: 0.00000), total loss: 0.00000, result: 0.00000 (epochs: 0)"
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
          "code": "0McG1KWEHB4BPOkO#KmHSFBFnDiIUIuG#F3BgAYMfDuHLBZEyLrLoDfOfDcJLFzELPeE2",
          "neuralNet": [
            {
              "inputNeurons": 2,
              "hiddenLayers": 1,
              "hiddenNeurons": 2,
              "outputNeurons": 1,
              "weightFunction": "nl.zeesoft.zdk.functions.ZWeightKaiming",
              "biasFunction": "nl.zeesoft.zdk.functions.ZWeightZero",
              "activator": "nl.zeesoft.zdk.functions.ZReLU",
              "outputActivator": "nl.zeesoft.zdk.functions.ZSoftmaxTop",
              "learningRate": 0.0856,
              "values": [
                "2,1,1.0,0.0",
                "2,1,0.22001326,0.0",
                "1,1,1.0"
              ],
              "weights": [
                "1,1,0.0",
                "2,2,0.280434,-0.2935793,-0.29577017,0.3023429",
                "1,2,0.629519,0.6736097"
              ],
              "biases": [
                "1,1,0.0",
                "2,1,-0.06042075,0.06531976",
                "1,1,-0.12574047"
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

Test results
------------
All 11 tests have been executed successfully (57 assertions).  
Total test duration: 33809 ms (total sleep duration: 30600 ms).  

Memory usage per test;  
 * nl.zeesoft.zdk.test.impl.TestZStringEncoder: 530 Kb / 0 Mb
 * nl.zeesoft.zdk.test.impl.TestZStringSymbolParser: 405 Kb / 0 Mb
 * nl.zeesoft.zdk.test.impl.TestCsv: 413 Kb / 0 Mb
 * nl.zeesoft.zdk.test.impl.TestJson: 426 Kb / 0 Mb
 * nl.zeesoft.zdk.test.impl.TestZHttpRequest: 432 Kb / 0 Mb
 * nl.zeesoft.zdk.test.impl.TestMessenger: 690 Kb / 0 Mb
 * nl.zeesoft.zdk.test.impl.TestZMatrix: 816 Kb / 0 Mb
 * nl.zeesoft.zdk.test.impl.TestGeneticCode: 777 Kb / 0 Mb
 * nl.zeesoft.zdk.test.impl.TestNeuralNet: 33180 Kb / 32 Mb
 * nl.zeesoft.zdk.test.impl.TestGeneticNN: 3480 Kb / 3 Mb
 * nl.zeesoft.zdk.test.impl.TestEvolver: 56349 Kb / 55 Mb
