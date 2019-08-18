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
Key: 1381171890499689881923488888055950760755882563692881699487424096
Input text: Hello, my name is 'Dyz Lecticus'. How are you feeling today? :-) (Don't you know how to: [re;spond]!).

Key encoded text: KB5DvG0BACMFyA8HqHt~8EJIHIsFZGNI9HoFEBIHgC0DPD~HYHWH0HpGyAFFVFwGcDEAzFXFZBSFoERFVHrFrCBFiFUCvFiHnC2HvG:BzFIHEI3EQHMGHEqBeCeA2Ah~0A9CRGcB~CHFOCVGqHEBeEJHFI:FdH2IQGvGLCxHSB4D4EkGUGOH8HlGUADFSFAIbEHBRGKEMA7F0
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
2019-08-18 22:38:52:783 DBG nl.zeesoft.zdk.test.impl.TestMessengerListener: Test log debug message before Messenger has started
2019-08-18 22:38:53:084 ERR nl.zeesoft.zdk.test.impl.TestMessengerListener: Test log error message while Messenger is working
2019-08-18 22:38:53:084 ERR nl.zeesoft.zdk.test.impl.TestMessengerListener: Test log exception stack trace
java.lang.NumberFormatException: For input string: "A"
	at java.lang.NumberFormatException.forInputString(NumberFormatException.java:65)
	at java.lang.Integer.parseInt(Integer.java:580)
	at java.lang.Integer.parseInt(Integer.java:615)
	at nl.zeesoft.zdk.test.impl.TestMessenger.test(TestMessenger.java:83)
	at nl.zeesoft.zdk.test.Tester.test(Tester.java:69)
	at nl.zeesoft.zdk.test.LibraryObject.describeAndTest(LibraryObject.java:39)
	at nl.zeesoft.zdk.test.impl.ZDK.main(ZDK.java:39)

2019-08-18 22:38:53:394 WRN nl.zeesoft.zdk.test.impl.TestMessengerListener: Test log warning message after Messenger has stopped
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
-00.92 | 000.67 | 000.40
-------+--------+-------
-00.60 | -00.66 | -00.28

Randomized multiplied element wise;
-27.69 | 020.24 | 011.88
-------+--------+-------
-17.93 | -19.74 | -08.29

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
-00.61 | -00.52 | 000.15
-------+--------+-------
000.85 | -00.79 | -00.45

Randomized matrix transposed;
-00.61 | 000.85
-------+-------
-00.52 | -00.79
-------+-------
000.15 | -00.45
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
Genetic code: 5356994814201587920992401376088359848248179989528782084034193468132431695786501211808455170784723899
Mutated code: 5351994814201587920996401376088359848248179689528782084034193468132431495786501221808455170784723899
                 ^                 ^                     ^                          ^         ^                   

Scaled property values;
0: 179
1: 376 <
2: 934
3: 528
4: 996 <
5: 847
6: 209
7: 218 <
8: 640 <
9: 501
10: 519 <
11: 792
12: 899
13: 899
14: 99 <
15: 996 <
16: 481
17: 952
18: 243
19: 813
20: 813
21: 420
22: 782
23: 179 <
24: 78
25: 899
26: 481
27: 287 <
28: 587
29: 455
30: 895
31: 551
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
  Input: [1.00|1.00], output: [0.03], expectation: [0.00], error: -0.03, loss: 0.02672286
  Input: [0.00|1.00], output: [-0.00], expectation: [1.00], error: 1.00, loss: 1.0000165
  Input: [1.00|0.00], output: [0.19], expectation: [1.00], error: 0.81, loss: 0.8083849
  Average error: 0.46, average loss: 0.46, success: false
Latest test results;
  Input: [1.00|1.00], output: [0.36], expectation: [0.00], error: -0.36, loss: 0.3620716
  Input: [0.00|0.00], output: [0.32], expectation: [0.00], error: -0.32, loss: 0.32434228
  Input: [1.00|0.00], output: [0.95], expectation: [1.00], error: 0.05, loss: 0.04895699
  Input: [0.00|1.00], output: [0.29], expectation: [1.00], error: 0.71, loss: 0.70689553
  Average error: 0.36, average loss: 0.36, success: false
Trained epochs: 5000, error change rate: 1.964289E-5, loss change rate: 1.964289E-5
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
  Input: [0.00|1.00], output: [1.00], expectation: [1.00], error: 0.00, loss: 0.0
  Input: [1.00|0.00], output: [0.00], expectation: [1.00], error: 1.00, loss: 1.0
  Input: [1.00|1.00], output: [1.00], expectation: [0.00], error: -1.00, loss: 1.0
  Average error: 0.50, average loss: 0.50, success: false
Trained epochs: 5000, error change rate: 0.0, loss change rate: 0.0

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
    "2,1,1.0,1.0",
    "2,1,-3.7425637E-4,4.72255",
    "1,1,1.0"
  ],
  "weights": [
    "1,1,0.0",
    "2,2,0.45185083,0.634225,-0.015598714,-0.014894724",
    "1,2,0.19187021,0.07922113"
  ],
  "biases": [
    "1,1,0.0",
    "2,1,-1.1240772,4.729277",
    "1,1,-0.37267017"
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
GeneticNN gnn = new GeneticNN(inputNeurons,maxHiddenLayers,maxHiddenNeurons,outputNeurons,codePropertyStart);
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
Neural net activator: nl.zeesoft.zdk.functions.ZReLU, output activator: nl.zeesoft.zdk.functions.ZReLU, learning rate: 0.0999
Initial test results;
  Input: [0.00|0.00], output: [0.00], expectation: [0.00], error: 0.00, loss: 0.0
  Input: [1.00|1.00], output: [0.09], expectation: [0.00], error: -0.09, loss: 0.09215298
  Input: [0.00|1.00], output: [0.13], expectation: [1.00], error: 0.87, loss: 0.866663
  Input: [1.00|0.00], output: [0.00], expectation: [1.00], error: 1.00, loss: 1.0
  Average error: 0.49, average loss: 0.49, success: false
Latest test results;
  Input: [1.00|1.00], output: [0.68], expectation: [0.00], error: -0.68, loss: 0.6821595
  Input: [0.00|1.00], output: [0.61], expectation: [1.00], error: 0.39, loss: 0.38598824
  Input: [0.00|0.00], output: [0.00], expectation: [0.00], error: 0.00, loss: 0.0
  Input: [1.00|0.00], output: [0.65], expectation: [1.00], error: 0.35, loss: 0.34742802
  Average error: 0.35, average loss: 0.35, success: false
Trained epochs: 5000, error change rate: 2.716201E-5, loss change rate: 2.716201E-5
================================================================================
Neural net activator: nl.zeesoft.zdk.functions.ZReLU, output activator: nl.zeesoft.zdk.functions.ZLeakyReLU, learning rate: 0.0925
Initial test results;
  Input: [0.00|0.00], output: [1.51], expectation: [0.00], error: -1.51, loss: 1.5072069
  Input: [1.00|1.00], output: [1.01], expectation: [0.00], error: -1.01, loss: 1.0091898
  Input: [0.00|1.00], output: [1.12], expectation: [1.00], error: -0.12, loss: 0.115662456
  Input: [1.00|0.00], output: [1.01], expectation: [1.00], error: -0.01, loss: 0.009189844
  Average error: 0.66, average loss: 0.66, success: false
Latest test results;
  Input: [1.00|1.00], output: [0.51], expectation: [0.00], error: -0.51, loss: 0.5144323
  Input: [1.00|0.00], output: [0.47], expectation: [1.00], error: 0.53, loss: 0.5331527
  Input: [0.00|1.00], output: [0.52], expectation: [1.00], error: 0.48, loss: 0.48383605
  Input: [0.00|0.00], output: [0.56], expectation: [0.00], error: -0.56, loss: 0.5609188
  Average error: 0.52, average loss: 0.52, success: false
Trained epochs: 5000, error change rate: 2.744546E-5, loss change rate: 2.744546E-5
================================================================================
Neural net activator: nl.zeesoft.zdk.functions.ZSigmoid, output activator: nl.zeesoft.zdk.functions.ZSigmoid, learning rate: 0.0184
Initial test results;
  Input: [0.00|0.00], output: [0.51], expectation: [0.00], error: -0.51, loss: 0.51098174
  Input: [1.00|1.00], output: [0.50], expectation: [0.00], error: -0.50, loss: 0.5014555
  Input: [0.00|1.00], output: [0.45], expectation: [1.00], error: 0.55, loss: 0.5532032
  Input: [1.00|0.00], output: [0.56], expectation: [1.00], error: 0.44, loss: 0.4427663
  Average error: 0.50, average loss: 0.50, success: false
Latest test results;
  Input: [0.00|0.00], output: [0.51], expectation: [0.00], error: -0.51, loss: 0.51206577
  Input: [1.00|1.00], output: [0.48], expectation: [0.00], error: -0.48, loss: 0.4845396
  Input: [0.00|1.00], output: [0.48], expectation: [1.00], error: 0.52, loss: 0.5209882
  Input: [1.00|0.00], output: [0.53], expectation: [1.00], error: 0.47, loss: 0.47456813
  Average error: 0.50, average loss: 0.50, success: false
Trained epochs: 5000, error change rate: 8.122444E-7, loss change rate: 8.122444E-7
================================================================================
Neural net activator: nl.zeesoft.zdk.functions.ZLeakyReLU, output activator: nl.zeesoft.zdk.functions.ZLeakyReLU, learning rate: 0.08360001
Initial test results;
  Input: [0.00|0.00], output: [0.66], expectation: [0.00], error: -0.66, loss: 0.6599233
  Input: [1.00|1.00], output: [0.23], expectation: [0.00], error: -0.23, loss: 0.22900876
  Input: [0.00|1.00], output: [0.29], expectation: [1.00], error: 0.71, loss: 0.71378106
  Input: [1.00|0.00], output: [0.62], expectation: [1.00], error: 0.38, loss: 0.37962425
  Average error: 0.50, average loss: 0.50, success: false
Latest test results;
  Input: [1.00|1.00], output: [-0.00], expectation: [0.00], error: 0.00, loss: 1.2794911E-4
  Input: [0.00|1.00], output: [0.98], expectation: [1.00], error: 0.02, loss: 0.016874015
  Input: [1.00|0.00], output: [0.50], expectation: [1.00], error: 0.50, loss: 0.50363624
  Input: [0.00|0.00], output: [0.55], expectation: [0.00], error: -0.55, loss: 0.54844195
  Average error: 0.27, average loss: 0.27, success: false
Trained epochs: 5000, error change rate: 4.566287E-5, loss change rate: 4.566287E-5
================================================================================
Neural net activator: nl.zeesoft.zdk.functions.ZSigmoid, output activator: nl.zeesoft.zdk.functions.ZLeakyReLU, learning rate: 0.022
Initial test results;
  Input: [0.00|0.00], output: [-0.01], expectation: [0.00], error: 0.01, loss: 0.0067308764
  Input: [1.00|1.00], output: [-0.01], expectation: [0.00], error: 0.01, loss: 0.007871529
  Input: [0.00|1.00], output: [-0.01], expectation: [1.00], error: 1.01, loss: 1.0074476
  Input: [1.00|0.00], output: [-0.01], expectation: [1.00], error: 1.01, loss: 1.0073026
  Average error: 0.51, average loss: 0.51, success: false
Latest test results;
  Input: [1.00|1.00], output: [0.65], expectation: [0.00], error: -0.65, loss: 0.64872813
  Input: [1.00|0.00], output: [0.56], expectation: [1.00], error: 0.44, loss: 0.44123
  Input: [0.00|0.00], output: [0.21], expectation: [0.00], error: -0.21, loss: 0.20949647
  Input: [0.00|1.00], output: [0.57], expectation: [1.00], error: 0.43, loss: 0.43239105
  Average error: 0.43, average loss: 0.43, success: false
Trained epochs: 5000, error change rate: 1.4875346E-5, loss change rate: 1.4875346E-5
================================================================================
Neural net activator: nl.zeesoft.zdk.functions.ZTanH, output activator: nl.zeesoft.zdk.functions.ZTanH, learning rate: 0.0505
Initial test results;
  Input: [0.00|0.00], output: [0.61], expectation: [0.00], error: -0.61, loss: 0.6117998
  Input: [1.00|1.00], output: [0.27], expectation: [0.00], error: -0.27, loss: 0.2715471
  Input: [0.00|1.00], output: [0.14], expectation: [1.00], error: 0.86, loss: 0.8649677
  Input: [1.00|0.00], output: [0.68], expectation: [1.00], error: 0.32, loss: 0.3208993
  Average error: 0.52, average loss: 0.52, success: false
Latest test results;
  Input: [0.00|0.00], output: [0.05], expectation: [0.00], error: -0.05, loss: 0.052049167
  Input: [0.00|1.00], output: [0.90], expectation: [1.00], error: 0.10, loss: 0.09938574
  Input: [1.00|0.00], output: [0.90], expectation: [1.00], error: 0.10, loss: 0.09765953
  Input: [1.00|1.00], output: [0.10], expectation: [0.00], error: -0.10, loss: 0.09593763
  Average error: 0.09, average loss: 0.09, success: true
Trained epochs: 768, error change rate: 5.6125707E-4, loss change rate: 5.6125707E-4

~~~~

Test results
------------
All 10 tests have been executed successfully (54 assertions).  
Total test duration: 1820 ms (total sleep duration: 600 ms).  

Memory usage per test;  
 * nl.zeesoft.zdk.test.impl.TestZStringEncoder: 522 Kb / 0 Mb
 * nl.zeesoft.zdk.test.impl.TestZStringSymbolParser: 404 Kb / 0 Mb
 * nl.zeesoft.zdk.test.impl.TestCsv: 411 Kb / 0 Mb
 * nl.zeesoft.zdk.test.impl.TestJson: 425 Kb / 0 Mb
 * nl.zeesoft.zdk.test.impl.TestZHttpRequest: 431 Kb / 0 Mb
 * nl.zeesoft.zdk.test.impl.TestMessenger: 689 Kb / 0 Mb
 * nl.zeesoft.zdk.test.impl.TestZMatrix: 815 Kb / 0 Mb
 * nl.zeesoft.zdk.test.impl.TestGeneticCode: 776 Kb / 0 Mb
 * nl.zeesoft.zdk.test.impl.TestNeuralNet: 2401 Kb / 2 Mb
 * nl.zeesoft.zdk.test.impl.TestGeneticNN: 7358 Kb / 7 Mb
