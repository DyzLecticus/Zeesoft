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
Key: 9877408868662200375004484150036017767876829785366336928875083432
Input text: Hello, my name is 'Dyz Lecticus'. How are you feeling today? :-) (Don't you know how to: [re;spond]!).

Key encoded text: NfqfIeueXbu9reufgeheTdpdvadaB819Kbod#bc9k0sclbJeQbM0JcJ9U9FbwdP8b0:d8e3dZemexe8e3eS0wgQeufIbwbKdydnb#b9ePfO0HfzfrexcA0wefalb48K8cfue8exeWbp9Hfiegevf1dpctamaP9e0vaveEcY9W8mczcueMaE0HcF9j9DbtdW9aaBesePcMd#e0
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
2019-12-22 07:59:47:868 DBG nl.zeesoft.zdk.test.impl.TestMessengerListener: Test log debug message before Messenger has started
2019-12-22 07:59:48:183 ERR nl.zeesoft.zdk.test.impl.TestMessengerListener: Test log error message while Messenger is working
2019-12-22 07:59:48:183 ERR nl.zeesoft.zdk.test.impl.TestMessengerListener: Test log exception stack trace
java.lang.NumberFormatException: For input string: "A"
	at java.lang.NumberFormatException.forInputString(NumberFormatException.java:65)
	at java.lang.Integer.parseInt(Integer.java:580)
	at java.lang.Integer.parseInt(Integer.java:615)
	at nl.zeesoft.zdk.test.impl.TestMessenger.test(TestMessenger.java:83)
	at nl.zeesoft.zdk.test.Tester.test(Tester.java:71)
	at nl.zeesoft.zdk.test.LibraryObject.describeAndTest(LibraryObject.java:39)
	at nl.zeesoft.zdk.test.impl.ZDK.main(ZDK.java:52)

2019-12-22 07:59:48:534 WRN nl.zeesoft.zdk.test.impl.TestMessengerListener: Test log warning message after Messenger has stopped
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
000.33 | -00.07 | -00.45
-------+--------+-------
000.37 | -00.36 | 000.66

Randomized multiplied element wise;
009.88 | -02.10 | -13.54
-------+--------+-------
011.09 | -10.87 | 019.92

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
000.81 | 000.20 | -00.68
-------+--------+-------
000.81 | 000.85 | -00.95

Randomized matrix transposed;
000.81 | 000.81
-------+-------
000.20 | 000.85
-------+-------
-00.68 | -00.95
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
Genetic code: 8519913682757773032939471666560381178246733900034371508713024817016888489323242900383468699952403972
Mutated code: 8519913682757773732939471666560381178245733900034071508713024817016888489323232900303468699952403972
                              ^                      ^         ^                           ^     ^                

Scaled property values;
0: 248
1: 199
2: 573 <
3: 457 <
4: 686
5: 71 <
6: 457 <
7: 136
8: 381
9: 3
10: 939
11: 38
12: 199
13: 232 <
14: 130
15: 373 <
16: 248 <
17: 573 <
18: 368
19: 715
20: 489
21: 524
22: 952
23: 732 <
24: 665
25: 248
26: 24 <
27: 38
28: 170 <
29: 471
30: 573 <
31: 481
32: 893
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
  Input: [1.00|1.00], output: [-0.00], expectation: [0.00], error: 0.00, loss: 0.0026027488
  Input: [0.00|1.00], output: [-0.00], expectation: [1.00], error: 1.00, loss: 1.0015384
  Input: [1.00|0.00], output: [-0.00], expectation: [1.00], error: 1.00, loss: 1.0010643
  Average error: 0.50, average loss: 0.50, success: false
Latest test results;
  Input: [1.00|0.00], output: [0.67], expectation: [1.00], error: 0.33, loss: 0.3263278
  Input: [0.00|0.00], output: [0.71], expectation: [0.00], error: -0.71, loss: 0.7070837
  Input: [0.00|1.00], output: [0.64], expectation: [1.00], error: 0.36, loss: 0.363881
  Input: [1.00|1.00], output: [-0.00], expectation: [0.00], error: 0.00, loss: 3.1791627E-4
  Average error: 0.35, average loss: 0.35, success: false
Trained epochs: 10000, total average error: 3551.554, total average loss: 3551.554
================================================================================
Neural net activator: nl.zeesoft.zdk.functions.ZLeakyReLU, output activator: nl.zeesoft.zdk.functions.ZSoftmaxTop, learning rate: 0.1
Initial test results;
  Input: [0.00|0.00], output: [1.00], expectation: [0.00], error: -1.00, loss: 1.0
  Input: [1.00|1.00], output: [1.00], expectation: [0.00], error: -1.00, loss: 1.0
  Input: [0.00|1.00], output: [1.00], expectation: [1.00], error: 0.00, loss: 0.0
  Input: [1.00|0.00], output: [1.00], expectation: [1.00], error: 0.00, loss: 0.0
  Average error: 0.50, average loss: 0.50, success: false
Latest test results;
  Input: [1.00|1.00], output: [1.00], expectation: [0.00], error: -1.00, loss: 1.0
  Input: [1.00|0.00], output: [0.00], expectation: [1.00], error: 1.00, loss: 1.0
  Input: [0.00|1.00], output: [0.00], expectation: [1.00], error: 1.00, loss: 1.0
  Input: [0.00|0.00], output: [1.00], expectation: [0.00], error: -1.00, loss: 1.0
  Average error: 1.00, average loss: 1.00, success: false
Trained epochs: 10000, total average error: 7055.75, total average loss: 7055.75

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
    "2,1,-0.00584996,0.3512661",
    "1,1,1.0"
  ],
  "weights": [
    "1,1,0.0",
    "2,2,0.3880834,0.19139282,0.026913956,-0.01964973",
    "1,2,0.10876596,0.8024111"
  ],
  "biases": [
    "1,1,0.0",
    "2,1,-0.58510476,0.27102497",
    "1,1,-0.19330776"
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
Neural net activator: nl.zeesoft.zdk.functions.ZReLU, output activator: nl.zeesoft.zdk.functions.ZSoftmaxTop, learning rate: 0.19500001
Initial test results;
  Input: [0.00|0.00], output: [0.00], expectation: [0.00], error: 0.00, loss: 0.0
  Input: [1.00|1.00], output: [0.00], expectation: [0.00], error: 0.00, loss: 0.0
  Input: [0.00|1.00], output: [0.00], expectation: [1.00], error: 1.00, loss: 1.0
  Input: [1.00|0.00], output: [0.00], expectation: [1.00], error: 1.00, loss: 1.0
  Average error: 0.50, average loss: 0.50, success: false

00100 ------------------------------------\ 150%
00200 -------------------------------------------------\ 200%
00300 -------------------------------------------------| 200%
00400 -------------------------------------------------| 200%
00500 ------------------------------------/ 150%
00600 -------------------------------------------------\ 200%
00700 -------------------------------------------------| 200%
00800 ------------------------------------/ 150%
00900 -------------------------------------------------\ 200%
01000 ------------------------------------/ 150%
01100 -------------------------------------------------\ 200%
01200 ------------------------------------/ 150%
01300 ------------------------------------| 150%
01400 ------------------------/ 100%
01500 ------------------------| 100%
01600 ------------------------------------\ 150%
01700 ------------------------------------| 150%
01800 ------------------------------------| 150%
01900 -------------------------------------------------\ 200%
02000 ------------------------/ 100%
02100 -------------------------------------------------\ 200%
02200 ------------------------------------/ 150%
02300 ------------------------------------| 150%
02400 ------------------------------------| 150%
02500 ------------------------/ 100%
02600 ------------------------------------\ 150%
02700 -------------------------------------------------\ 200%
02800 ------------------------------------/ 150%
02900 ------------------------/ 100%
03000 -----------/ 50%
03100 -------------------------------------------------\ 200%
03200 ------------------------------------/ 150%
03300 ------------------------/ 100%
03400 -------------------------------------------------\ 200%
03500 -------------------------------------------------| 200%
03600 ------------------------/ 100%
03700 -------------------------------------------------\ 200%
03800 -------------------------------------------------| 200%
03900 ------------------------------------/ 150%
04000 ------------------------------------| 150%
04100 -------------------------------------------------\ 200%
04200 ------------------------------------/ 150%
04300 ------------------------------------| 150%
04400 ------------------------------------| 150%
04500 ------------------------/ 100%
04600 ------------------------------------\ 150%
04700 -------------------------------------------------\ 200%
04800 -------------------------------------------------| 200%
04900 ------------------------/ 100%
05000 ------------------------------------\ 150%
05100 ------------------------------------| 150%
05200 -------------------------------------------------\ 200%
05300 -------------------------------------------------| 200%
05400 -------------------------------------------------| 200%
05500 -------------------------------------------------| 200%
05600 -------------------------------------------------| 200%
05700 ------------------------/ 100%
05800 ------------------------------------\ 150%
05900 ------------------------/ 100%
06000 ------------------------------------\ 150%
06100 ------------------------/ 100%
06200 -------------------------------------------------\ 200%
06300 -------------------------------------------------| 200%
06400 -------------------------------------------------| 200%
06500 ------------------------/ 100%
06600 ------------------------------------\ 150%
06700 ------------------------------------| 150%
06800 ------------------------------------| 150%
06900 ------------------------------------| 150%
07000 ------------------------------------| 150%
07100 ------------------------/ 100%
07200 ------------------------------------\ 150%
07300 ------------------------/ 100%
07400 ------------------------------------\ 150%
07500 ------------------------/ 100%
07600 ------------------------| 100%
07700 -----------/ 50%
07800 -------------------------------------------------\ 200%
07900 -------------------------------------------------| 200%
08000 ------------------------/ 100%
08100 ------------------------------------\ 150%
08200 ------------------------/ 100%
08300 ------------------------------------\ 150%
08400 -------------------------------------------------\ 200%
08500 ------------------------------------/ 150%
08600 -----------/ 50%
08700 ------------------------------------\ 150%
08800 ------------------------------------| 150%
08900 ------------------------------------| 150%
09000 ------------------------------------| 150%
09100 -------------------------------------------------\ 200%
09200 ------------------------------------/ 150%
09300 -----------/ 50%
09400 -------------------------------------------------\ 200%
09500 ------------------------------------/ 150%
09600 -------------------------------------------------\ 200%
09700 ------------------------/ 100%
09800 -----------/ 50%
09900 -------------------------------------------------\ 200%
10000 ------------------------------------/ 150%

Latest test results;
  Input: [0.00|0.00], output: [0.00], expectation: [0.00], error: 0.00, loss: 0.0
  Input: [0.00|1.00], output: [0.00], expectation: [1.00], error: 1.00, loss: 1.0
  Input: [1.00|0.00], output: [0.00], expectation: [1.00], error: 1.00, loss: 1.0
  Input: [1.00|1.00], output: [1.00], expectation: [0.00], error: -1.00, loss: 1.0
  Average error: 0.75, average loss: 0.75, success: false
Trained epochs: 10000, total average error: 7276.0, total average loss: 7276.0
================================================================================
Neural net activator: nl.zeesoft.zdk.functions.ZReLU, output activator: nl.zeesoft.zdk.functions.ZSigmoid, learning rate: 0.046
Initial test results;
  Input: [0.00|0.00], output: [0.42], expectation: [0.00], error: -0.42, loss: 0.41548613
  Input: [1.00|1.00], output: [0.46], expectation: [0.00], error: -0.46, loss: 0.464503
  Input: [0.00|1.00], output: [0.40], expectation: [1.00], error: 0.60, loss: 0.5963528
  Input: [1.00|0.00], output: [0.48], expectation: [1.00], error: 0.52, loss: 0.52329993
  Average error: 0.50, average loss: 0.50, success: false

00100 ---------------------/ 88%
00200 ------------------/ 79%
00300 -----------------/ 74%
00400 ----------------/ 71%
00500 ----------------/ 70%
00600 ----------------/ 69%
00700 ----------------/ 68%
00800 ----------------| 68%
00900 ----------------| 68%
01000 ---------------/ 67%
01100 ---------------| 67%
01200 ---------------| 67%
01300 ---------------| 67%
01400 ---------------| 67%
01500 ---------------| 67%
01600 ---------------| 67%
01700 ---------------| 67%
01800 ---------------| 67%
01900 ---------------| 67%
02000 ---------------| 67%
02100 ---------------| 67%
02200 ---------------| 67%
02300 ---------------| 67%
02400 ---------------| 67%
02500 ---------------| 67%
02600 ---------------| 67%
02700 ---------------| 67%
02800 ---------------| 67%
02900 ---------------| 67%
03000 ---------------| 67%
03100 ---------------| 67%
03200 ---------------| 67%
03300 ---------------| 67%
03400 ---------------| 67%
03500 ---------------/ 66%
03600 ---------------\ 67%
03700 ---------------/ 66%
03800 ---------------| 66%
03900 ---------------| 66%
04000 ---------------| 66%
04100 ---------------| 66%
04200 ---------------| 66%
04300 ---------------| 66%
04400 ---------------| 66%
04500 ---------------| 66%
04600 ---------------| 66%
04700 ---------------| 66%
04800 ---------------| 66%
04900 ---------------| 66%
05000 ---------------| 66%
05100 ---------------| 66%
05200 ---------------| 66%
05300 ---------------| 66%
05400 ---------------| 66%
05500 ---------------| 66%
05600 ---------------| 66%
05700 ---------------| 66%
05800 ---------------| 66%
05900 ---------------| 66%
06000 ---------------| 66%
06100 ---------------| 66%
06200 ---------------| 66%
06300 ---------------| 66%
06400 ---------------| 66%
06500 ---------------| 66%
06600 ---------------| 66%
06700 ---------------| 66%
06800 ---------------| 66%
06900 ---------------| 66%
07000 ---------------| 66%
07100 ---------------| 66%
07200 ---------------| 66%
07300 ---------------| 66%
07400 ---------------| 66%
07500 ---------------| 66%
07600 ---------------| 66%
07700 ---------------| 66%
07800 ---------------| 66%
07900 ---------------| 66%
08000 ---------------| 66%
08100 ---------------| 66%
08200 ---------------| 66%
08300 ---------------| 66%
08400 ---------------| 66%
08500 ---------------| 66%
08600 ---------------| 66%
08700 ---------------| 66%
08800 ---------------| 66%
08900 ---------------| 66%
09000 ---------------| 66%
09100 ---------------| 66%
09200 ---------------| 66%
09300 ---------------| 66%
09400 ---------------| 66%
09500 ---------------| 66%
09600 ---------------| 66%
09700 ---------------| 66%
09800 ---------------| 66%
09900 ---------------| 66%
10000 ---------------| 66%

Latest test results;
  Input: [1.00|1.00], output: [0.33], expectation: [0.00], error: -0.33, loss: 0.3331091
  Input: [0.00|1.00], output: [0.33], expectation: [1.00], error: 0.67, loss: 0.66764665
  Input: [1.00|0.00], output: [1.00], expectation: [1.00], error: 0.00, loss: 0.001758039
  Input: [0.00|0.00], output: [0.33], expectation: [0.00], error: -0.33, loss: 0.33386725
  Average error: 0.33, average loss: 0.33, success: false
Trained epochs: 10000, total average error: 3384.5186, total average loss: 3384.5186
================================================================================
Neural net activator: nl.zeesoft.zdk.functions.ZSigmoid, output activator: nl.zeesoft.zdk.functions.ZTanH, learning rate: 0.102400005
Initial test results;
  Input: [0.00|0.00], output: [-0.38], expectation: [0.00], error: 0.38, loss: 0.38246262
  Input: [1.00|1.00], output: [-0.60], expectation: [0.00], error: 0.60, loss: 0.60165876
  Input: [0.00|1.00], output: [-0.53], expectation: [1.00], error: 1.53, loss: 1.5322073
  Input: [1.00|0.00], output: [-0.51], expectation: [1.00], error: 1.51, loss: 1.5058352
  Average error: 1.01, average loss: 1.01, success: false

00100 ------------/ 52%
00200 -----------/ 51%
00300 -----------| 51%
00400 -----------/ 49%
00500 ----------/ 46%
00600 ---------/ 42%
00700 --------/ 37%
00800 ------/ 31%
00900 ----/ 22%
01000 --/ 14%
01100 -/ 9%

Latest test results;
  Input: [0.00|0.00], output: [0.03], expectation: [0.00], error: -0.03, loss: 0.033615198
  Input: [1.00|0.00], output: [0.90], expectation: [1.00], error: 0.10, loss: 0.0999639
  Input: [0.00|1.00], output: [0.90], expectation: [1.00], error: 0.10, loss: 0.09764218
  Input: [1.00|1.00], output: [0.09], expectation: [0.00], error: -0.09, loss: 0.08611067
  Average error: 0.08, average loss: 0.08, success: true
Trained epochs: 1182, total average error: 440.637, total average loss: 440.637
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
2019-12-22 07:59:49:695 DBG nl.zeesoft.zdk.genetic.Evolver: Started
2019-12-22 07:59:49:804 DBG nl.zeesoft.zdk.genetic.Evolver: Selected new best genetic neural net;
- Code: 2542712689547456
- Size: 14
- Initial average loss: 0.57275 (final: 0.07393)
- Total average loss: 25.77682 (epochs: 80)
- Training result: 14.76378
2019-12-22 07:59:58:173 DBG nl.zeesoft.zdk.genetic.Evolver: Selected new best genetic neural net;
- Code: 2541712689547456
- Size: 14
- Initial average loss: 0.55378 (final: 0.07879)
- Total average loss: 8.67549 (epochs: 35)
- Training result: 4.80431
2019-12-22 07:59:58:757 DBG nl.zeesoft.zdk.genetic.Evolver: Selected new best genetic neural net;
- Code: 2541312689547456
- Size: 14
- Initial average loss: 0.42753 (final: 0.06998)
- Total average loss: 6.22500 (epochs: 31)
- Training result: 2.66140
2019-12-22 07:59:58:921 DBG nl.zeesoft.zdk.genetic.Evolver: Selected new best genetic neural net;
- Code: 2541312689547156
- Size: 14
- Initial average loss: 0.35830 (final: 0.06177)
- Total average loss: 5.57194 (epochs: 32)
- Training result: 1.99641
2019-12-22 07:59:58:984 DBG nl.zeesoft.zdk.genetic.Evolver: Selected new best genetic neural net;
- Code: 2541312688547156
- Size: 14
- Initial average loss: 0.25265 (final: 0.07102)
- Total average loss: 1.92891 (epochs: 13)
- Training result: 0.48734
2019-12-22 07:59:59:046 DBG nl.zeesoft.zdk.genetic.Evolver: Selected new best genetic neural net;
- Code: 2541372688547156
- Size: 14
- Initial average loss: 0.16810 (final: 0.06192)
- Total average loss: 0.96269 (epochs: 9)
- Training result: 0.16183
2019-12-22 07:59:59:210 DBG nl.zeesoft.zdk.genetic.Evolver: Selected new best genetic neural net;
- Code: 2541372688547156
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
    "2019-12-22 07:59:49:804 SEL code: 2542712689547456, size: 14, initial loss: 0.57275 (final: 0.07393), total loss: 25.77682, result: 14.76378 (epochs: 80)",
    "2019-12-22 07:59:58:173 SEL code: 2541712689547456, size: 14, initial loss: 0.55378 (final: 0.07879), total loss: 8.67549, result: 4.80431 (epochs: 35)",
    "2019-12-22 07:59:58:757 SEL code: 2541312689547456, size: 14, initial loss: 0.42753 (final: 0.06998), total loss: 6.22500, result: 2.66140 (epochs: 31)",
    "2019-12-22 07:59:58:921 SEL code: 2541312689547156, size: 14, initial loss: 0.35830 (final: 0.06177), total loss: 5.57194, result: 1.99641 (epochs: 32)",
    "2019-12-22 07:59:58:984 SEL code: 2541312688547156, size: 14, initial loss: 0.25265 (final: 0.07102), total loss: 1.92891, result: 0.48734 (epochs: 13)",
    "2019-12-22 07:59:59:046 SEL code: 2541372688547156, size: 14, initial loss: 0.16810 (final: 0.06192), total loss: 0.96269, result: 0.16183 (epochs: 9)",
    "2019-12-22 07:59:59:210 SEL code: 2541372688547156, size: 14, initial loss: 0.00000 (final: 0.00000), total loss: 0.00000, result: 0.00000 (epochs: 0)"
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
          "code": "xDHCIEJNALZKmMEMlGCMGADB2HUAfFmMdELBSG#KuDgKZBzAoCnMHGMKFNoGML:NCOjH2",
          "neuralNet": [
            {
              "inputNeurons": 2,
              "hiddenLayers": 1,
              "hiddenNeurons": 2,
              "outputNeurons": 1,
              "weightFunction": "nl.zeesoft.zdk.functions.ZWeightXavier",
              "biasFunction": "nl.zeesoft.zdk.functions.ZWeightZero",
              "activator": "nl.zeesoft.zdk.functions.ZLeakyReLU",
              "outputActivator": "nl.zeesoft.zdk.functions.ZSoftmaxTop",
              "learningRate": 0.1816,
              "values": [
                "2,1,0.0,1.0",
                "2,1,-0.009048376,0.47104135",
                "1,1,1.0"
              ],
              "weights": [
                "1,1,0.0",
                "2,2,0.8938832,-0.90483767,-0.90045595,0.47104135",
                "1,2,0.2008582,0.9969424"
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
SDR map: 100,2|48,57|20,42
Number of SDR A matches in SDR map: 1
SDR C: 100,17,46
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
SDR for value 0:  01000100000000000000000000000000000000001000100000
SDR for value 1:  00000100000000000000000000000100000000001000100000
SDR for value 24: 00000000000000000100000000000000001000000001000100
SDR for value 25: 00000000000100000100000000000000000000000001000100
SDR for value 75: 00000000000000000000000000000000000001000010000110
SDR for value 76: 00000000001000000000000000000000000001000000000110
SDR for value -1: 01000000000000000001000000000000000000001000100000
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
SDR for 2019-01-01 00:00:00:000, value1: 0, value2: 0; 11111100000000000000000000000000000000000000001111111110000000000000000000000000000000000000000100110100000110000100000000000000010000100011000010010100001100000000000100000000000000111111110000000000
SDR for 2019-02-02 01:00:00:000, value1: 1, value2: 2; 11111111000000000000000000000000000000000000000000001111111100000000000000000000000000000000000000111100000110000000000000000000010000100000000010010100001100000100000100000010111100000000000000001111
SDR for 2019-03-03 02:00:00:000, value1: 2, value2: 4; 00111111110000000000000000000000000000000000000000000000111111110000000000000000000000000000000000111100001110000000000000000000000000100000000010010100100101000100000100000000111111110000000000000000
SDR for 2019-04-04 03:00:00:000, value1: 3, value2: 6; 00111111110000000000000000000000000000000000000000000000000011111111000000000000000000000000000000111000001110000000000000001000000000100000000010010000110001000100100100000000000000000000011111111000
SDR for 2019-05-05 04:00:00:000, value1: 4, value2: 8; 00001111111100000000000000000000000000000000000000000000000000001111111100000000000000000000000000111000001110000000000000001100000000000100100000010000100001000100100100000000111111110000000000000000

Encoder StringBuilder:
VALUE1=0.0,33,17,2,3,5,38,11,12;1.0,33,2,3,4,5,38,11,12;2.0,2,3,4,5,38,10,11,12;3.0,2,3,4,38,10,11,12,28;4.0,2,3,4,10,11,12,28,29|VALUE2=0.0,18,2,19,3,8,11,13,31;1.0,18,2,19,8,25,11,13,31;2.0,18,19,38,8,25,11,13,31;3.0,16,19,38,8,25,11,13,31;4.0,16,19,21,8,25,11,13,31;5.0,16,19,21,8,25,11,28,31;6.0,16,17,21,8,25,11,28,31;7.0,16,17,1,21,25,11,28,31;8.0,16,1,4,21,25,11,28,31
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

Pooler input dimensions: 16*16, output dimensions: 32*32
- Total proximal links: 92160, active: 45824
- Average proximal inputs per column: 90
- Column groups: 144, average columns per group: 441

Processing input SDR map (learning: false) ...
Processing input SDR map took: 9907 ms

Performance statistics;
calculateOverlapScores:       1873.586 ms
selectActiveColumns:          1740.874 ms
logActivity:                   872.343 ms
calculateColumnGroupActivity: 3980.894 ms
updateBoostFactors:           1228.998 ms
total:                        9755.092 ms
logSize:                         15330   
avgPerLog:                       0.636 ms

Combined average: 0.37086153, Combined weekly average: 5.495956

Processing input SDR map (learning: true) ...
Processing input SDR map took: 11018 ms

Performance statistics;
calculateOverlapScores:        2476.677 ms
selectActiveColumns:            917.431 ms
logActivity:                    898.785 ms
calculateColumnGroupActivity:  2794.707 ms
updateBoostFactors:            1211.649 ms
total:                        10826.523 ms
learnActiveColumnsOnBits:      2445.443 ms
logSize:                          15330   
avgPerLog:                        0.706 ms

Combined average: 0.30949232, Combined weekly average: 7.7953753

Original ratio: 14.819428, learned ratio: 25.187622
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
Processed SDRs: 1000, bursting average: 2 (max: 9)
Processed SDRs: 1500, bursting average: 1 (max: 8)
Processed SDRs: 2000, bursting average: 0 (max: 6)
Processed SDRs: 2500, bursting average: 1 (max: 5)
Processed SDRs: 3000, bursting average: 1 (max: 5)
Processed SDRs: 3500, bursting average: 0 (max: 3)
Processed SDRs: 4000, bursting average: 0 (max: 6)
Processed SDRs: 4500, bursting average: 0 (max: 4)
Processed SDRs: 5000, bursting average: 0 (max: 3)
Processing input SDR map took: 14225 ms

Performance statistics;
cycleActiveState:       206.726 ms
activateColumnCells:    217.794 ms
calculateActivity:      963.538 ms
selectPredictiveCells:  441.816 ms
updatePredictions:     4300.863 ms
total:                 6144.405 ms
logSize:                   5000   
avgPerLog:                1.229 ms

Memory dimensions: 32*32*4
- Total distal links: 230669, active: 217737
- Average distal inputs per memory cell: 56 (min: 0, max: 105)
- Average connected distal inputs per memory cell: 53 (min: 0, max: 104)
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
  "bitsPerEncoder": 24,
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
- Column 00-00 = CombinedEncoder length: 128, bits: 32
  - SECOND ScalarEncoder length: 128, bits: 32, resolution: 1.0, min: 0.0, max: 60.0, periodic: true
- Column 00-01 = CombinedEncoder length: 256, bits: 16
  - VALUE ScalarEncoder length: 256, bits: 16, resolution: 1.0, min: 0.0, max: 30.0, periodic: false
- Column 00-02 = CombinedEncoder length: 64, bits: 8
  - POSX ScalarEncoder length: 64, bits: 8, resolution: 1.0, min: 0.0, max: 20.0, periodic: false
- Column 00-03 = CombinedEncoder length: 64, bits: 8
  - POSY ScalarEncoder length: 64, bits: 8, resolution: 1.0, min: 0.0, max: 20.0, periodic: false
- Column 00-04 = CombinedEncoder length: 64, bits: 8
  - POSZ ScalarEncoder length: 64, bits: 8, resolution: 1.0, min: 0.0, max: 20.0, periodic: false
- Column 01-03 = Merger maximum number of on bits: 0
- Column 02-00 = Pooler input dimensions: 12*11, output dimensions: 32*32
  - Total proximal links: 89600, active: 44782
  - Average proximal inputs per column: 87 (min: 87, max: 88)
  - Column groups: 144, average columns per group: 441
- Column 02-01 = Pooler input dimensions: 16*16, output dimensions: 32*32
  - Total proximal links: 92160, active: 46153
  - Average proximal inputs per column: 90
  - Column groups: 144, average columns per group: 441
- Column 02-03 = Pooler input dimensions: 14*14, output dimensions: 32*32
  - Total proximal links: 91776, active: 45736
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
      "bitsPerEncoder": 32,
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
      "inputLength": 128,
      "outputLength": 1024,
      "outputBits": 21,
      "inputSizeX": 12,
      "inputSizeY": 11,
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
2019-12-22 08:00:43:669 DBG nl.zeesoft.zdk.htm.grid.ZGrid: Starting grid ...
2019-12-22 08:00:43:671 DBG nl.zeesoft.zdk.htm.grid.ZGrid: Started grid
Processed requests: 100, accuracy: 0.470
Processed requests: 200, accuracy: 0.702
Processed requests: 300, accuracy: 0.802
Processed requests: 400, accuracy: 0.852
Processed requests: 500, accuracy: 0.882
Processed requests: 600, accuracy: 0.901
Processed requests: 700, accuracy: 0.916
Processed requests: 800, accuracy: 0.926
Processed requests: 900, accuracy: 0.934
Processed requests: 1000, accuracy: 0.938
Processed requests: 1100, accuracy: 0.989
Processed requests: 1200, accuracy: 0.996
Processed requests: 1300, accuracy: 0.995
Processed requests: 1400, accuracy: 0.994
Processed requests: 1500, accuracy: 0.994
Processed requests: 1600, accuracy: 0.994
Processed requests: 1700, accuracy: 0.994
Processed requests: 1800, accuracy: 0.994
Processed requests: 1900, accuracy: 0.993
Processed requests: 2000, accuracy: 0.995
Processed requests: 2100, accuracy: 0.995 (!)
Processed requests: 2200, accuracy: 0.993
Processed requests: 2300, accuracy: 0.994 (!)
Processed requests: 2400, accuracy: 0.993
Processed requests: 2500, accuracy: 0.993
Processed requests: 2600, accuracy: 0.993
Processed requests: 2700, accuracy: 0.993
Detected anomaly at id 2742, detected: 0.38095236, average: 0.9880006, difference: 0.44343978
Detected anomaly at id 2743, detected: 0.52380955, average: 0.98685765, difference: 0.3065189
Detected anomaly at id 2744, detected: 0.52380955, average: 0.98590535, difference: 0.30608153
Detected anomaly at id 2745, detected: 0.47619045, average: 0.9849529, difference: 0.34819478
Detected anomaly at id 2747, detected: 0.4285714, average: 0.98323864, difference: 0.39287668
Detected anomaly at id 2756, detected: 0.4285714, average: 0.97838134, difference: 0.3907807
Processed requests: 2800, accuracy: 0.953
Processed requests: 2900, accuracy: 0.953
Processed requests: 3000, accuracy: 0.954
2019-12-22 08:00:56:622 DBG nl.zeesoft.zdk.htm.grid.ZGrid: Stopping grid ...
2019-12-22 08:00:56:622 DBG nl.zeesoft.zdk.htm.grid.ZGrid: Stopped grid
Processing 3000 requests took 12992 ms

Grid dimensions: 5*5
- Column 00-00 = CombinedEncoder length: 128, bits: 32
  - SECOND ScalarEncoder length: 128, bits: 32, resolution: 1.0, min: 0.0, max: 60.0, periodic: true
- Column 00-01 = CombinedEncoder length: 256, bits: 16
  - VALUE ScalarEncoder length: 256, bits: 16, resolution: 1.0, min: 0.0, max: 30.0, periodic: false
- Column 00-02 = CombinedEncoder length: 64, bits: 8
  - POSX ScalarEncoder length: 64, bits: 8, resolution: 1.0, min: 0.0, max: 20.0, periodic: false
- Column 00-03 = CombinedEncoder length: 64, bits: 8
  - POSY ScalarEncoder length: 64, bits: 8, resolution: 1.0, min: 0.0, max: 20.0, periodic: false
- Column 00-04 = CombinedEncoder length: 64, bits: 8
  - POSZ ScalarEncoder length: 64, bits: 8, resolution: 1.0, min: 0.0, max: 20.0, periodic: false
- Column 01-03 = Merger maximum number of on bits: 0
- Column 02-00 = Pooler input dimensions: 12*11, output dimensions: 32*32
  - Total proximal links: 89600, active: 50177
  - Average proximal inputs per column: 87 (min: 87, max: 88)
  - Column groups: 144, average columns per group: 441
- Column 02-01 = Pooler input dimensions: 16*16, output dimensions: 32*32
  - Total proximal links: 92160, active: 34356
  - Average proximal inputs per column: 90
  - Column groups: 144, average columns per group: 441
- Column 02-03 = Pooler input dimensions: 14*14, output dimensions: 32*32
  - Total proximal links: 91776, active: 34460
  - Average proximal inputs per column: 89 (min: 87, max: 90)
  - Column groups: 144, average columns per group: 441
- Column 03-01 = Memory dimensions: 32*32*4
  - Total distal links: 204482, active: 191188
  - Average distal inputs per memory cell: 49 (min: 0, max: 126)
  - Average connected distal inputs per memory cell: 46 (min: 0, max: 126)
  - Average connected distal context inputs per memory cell: 33 (min: 0, max: 84)
- Column 04-00 = Detector start: 2500, window long/short: 500/1, threshold: 0.3 (ACTIVE)
- Column 04-01 = Classifier value key: VALUE, predict steps: 1

Grid column state data;
- 02-00: 833225
- 02-01: 947095
- 02-03: 891921
- 03-01: 4015141
- 04-00: 2929
- 04-01: 122086
Loading state data took 1657 ms
~~~~

Test results
------------
All 24 tests have been executed successfully (279288 assertions).  
Total test duration: 71359 ms (total sleep duration: 22300 ms).  

Memory usage per test;  
 * nl.zeesoft.zdk.test.impl.TestZStringEncoder: 810 Kb / 0 Mb
 * nl.zeesoft.zdk.test.impl.TestZStringSymbolParser: 528 Kb / 0 Mb
 * nl.zeesoft.zdk.test.impl.TestCsv: 535 Kb / 0 Mb
 * nl.zeesoft.zdk.test.impl.TestJson: 550 Kb / 0 Mb
 * nl.zeesoft.zdk.test.impl.TestZHttpRequest: 561 Kb / 0 Mb
 * nl.zeesoft.zdk.test.impl.TestMessenger: 767 Kb / 0 Mb
 * nl.zeesoft.zdk.test.impl.TestZMatrix: 833 Kb / 0 Mb
 * nl.zeesoft.zdk.test.impl.TestGeneticCode: 794 Kb / 0 Mb
 * nl.zeesoft.zdk.test.impl.TestNeuralNet: 1119 Kb / 1 Mb
 * nl.zeesoft.zdk.test.impl.TestGeneticNN: 2749 Kb / 2 Mb
 * nl.zeesoft.zdk.test.impl.TestEvolver: 56936 Kb / 55 Mb
 * nl.zeesoft.zdk.test.impl.htm.TestSDR: 56384 Kb / 55 Mb
 * nl.zeesoft.zdk.test.impl.htm.TestSDRMap: 55554 Kb / 54 Mb
 * nl.zeesoft.zdk.test.impl.htm.TestScalarEncoder: 53992 Kb / 52 Mb
 * nl.zeesoft.zdk.test.impl.htm.TestRDScalarEncoder: 972 Kb / 0 Mb
 * nl.zeesoft.zdk.test.impl.htm.TestDateTimeEncoder: 978 Kb / 0 Mb
 * nl.zeesoft.zdk.test.impl.htm.TestDateTimeValuesEncoder: 974 Kb / 0 Mb
 * nl.zeesoft.zdk.test.impl.htm.TestDateTimeValueEncoder: 989 Kb / 0 Mb
 * nl.zeesoft.zdk.test.impl.htm.TestGridEncoder: 994 Kb / 0 Mb
 * nl.zeesoft.zdk.test.impl.htm.TestPooler: 34943 Kb / 34 Mb
 * nl.zeesoft.zdk.test.impl.htm.TestMemory: 34950 Kb / 34 Mb
 * nl.zeesoft.zdk.test.impl.htm.TestMerger: 34958 Kb / 34 Mb
 * nl.zeesoft.zdk.test.impl.htm.TestZGridColumnEncoders: 35002 Kb / 34 Mb
 * nl.zeesoft.zdk.test.impl.htm.TestZGrid: 35198 Kb / 34 Mb
