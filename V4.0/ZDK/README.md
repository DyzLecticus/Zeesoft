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
Key: 9489372029662496230509587089056812695599710643444488023631887328
Input text: Hello, my name is 'Dyz Lecticus'. How are you feeling today? :-) (Don't you know how to: [re;spond]!).

Key encoded text: k3eZs3L3vY91XWNV4YP2G1d1wXiZN2k1tXJWTURYwWa4UZL2U28Wz3v3RV61U2K1~VuWn1X4311Z5494V2eVhW#14Z4XTYSYUZlZj3L2EVeWvYu1TXQW~3~231lXOUAUG2iYO2O3uY41gXBU4Y44N1dZuXrZ24y1bWQX1WDZ9V5491w2QZZWx3r3gV41R1R2:WxXF1K2PYeZ0
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
2019-09-04 12:50:15:370 DBG nl.zeesoft.zdk.test.impl.TestMessengerListener: Test log debug message before Messenger has started
2019-09-04 12:50:15:672 ERR nl.zeesoft.zdk.test.impl.TestMessengerListener: Test log error message while Messenger is working
2019-09-04 12:50:15:672 ERR nl.zeesoft.zdk.test.impl.TestMessengerListener: Test log exception stack trace
java.lang.NumberFormatException: For input string: "A"
	at java.lang.NumberFormatException.forInputString(NumberFormatException.java:65)
	at java.lang.Integer.parseInt(Integer.java:580)
	at java.lang.Integer.parseInt(Integer.java:615)
	at nl.zeesoft.zdk.test.impl.TestMessenger.test(TestMessenger.java:83)
	at nl.zeesoft.zdk.test.Tester.test(Tester.java:69)
	at nl.zeesoft.zdk.test.LibraryObject.describeAndTest(LibraryObject.java:39)
	at nl.zeesoft.zdk.test.impl.ZDK.main(ZDK.java:39)

2019-09-04 12:50:15:984 WRN nl.zeesoft.zdk.test.impl.TestMessengerListener: Test log warning message after Messenger has stopped
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
000.64 | -00.46 | 000.10
-------+--------+-------
-00.15 | 000.03 | -00.96

Randomized multiplied element wise;
019.35 | -13.83 | 003.14
-------+--------+-------
-04.58 | 001.00 | -28.87

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
000.84 | -00.52 | -00.07
-------+--------+-------
000.44 | -00.47 | 000.65

Randomized matrix transposed;
000.84 | 000.44
-------+-------
-00.52 | -00.47
-------+-------
-00.07 | 000.65
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
Genetic code: 6116098562773367263876042292428591771929170005626602686889884887844452817764818445594563087126628649
Mutated code: 0116098562773367263876042292423591771929170005626612686889884887844452817764818403594563087126628649
              ^                             ^                   ^                             ^^                  

Scaled property values;
0: 733 <
1: 878
2: 864
3: 192
4: 56
5: 945
6: 126 <
7: 648
8: 771
9: 0
10: 266 <
11: 929
12: 291
13: 562
14: 604
15: 177
16: 456
17: 56 <
18: 628
19: 924
20: 818
21: 627
22: 817
23: 898
24: 609
25: 403 <
26: 336
27: 878 <
28: 594
29: 444
30: 773
31: 11 <
32: 604
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
  Input: [1.00|1.00], output: [-0.00], expectation: [0.00], error: 0.00, loss: 5.116637E-4
  Input: [0.00|1.00], output: [0.00], expectation: [1.00], error: 1.00, loss: 0.9992315
  Input: [1.00|0.00], output: [-0.00], expectation: [1.00], error: 1.00, loss: 1.0010036
  Average error: 0.50, average loss: 0.50, success: false
Latest test results;
  Input: [1.00|0.00], output: [0.51], expectation: [1.00], error: 0.49, loss: 0.49049532
  Input: [0.00|1.00], output: [0.56], expectation: [1.00], error: 0.44, loss: 0.4449101
  Input: [1.00|1.00], output: [0.60], expectation: [0.00], error: -0.60, loss: 0.6016034
  Input: [0.00|0.00], output: [0.54], expectation: [0.00], error: -0.54, loss: 0.54088175
  Average error: 0.52, average loss: 0.52, success: false
Trained epochs: 10000, total average error: 5235.109, total average loss: 5235.109
================================================================================
Neural net activator: nl.zeesoft.zdk.functions.ZLeakyReLU, output activator: nl.zeesoft.zdk.functions.ZSoftmaxTop, learning rate: 0.1
Initial test results;
  Input: [0.00|0.00], output: [1.00], expectation: [0.00], error: -1.00, loss: 1.0
  Input: [1.00|1.00], output: [1.00], expectation: [0.00], error: -1.00, loss: 1.0
  Input: [0.00|1.00], output: [1.00], expectation: [1.00], error: 0.00, loss: 0.0
  Input: [1.00|0.00], output: [0.00], expectation: [1.00], error: 1.00, loss: 1.0
  Average error: 0.75, average loss: 0.75, success: false
Latest test results;
  Input: [1.00|1.00], output: [0.00], expectation: [0.00], error: 0.00, loss: 0.0
  Input: [0.00|1.00], output: [1.00], expectation: [1.00], error: 0.00, loss: 0.0
  Input: [0.00|0.00], output: [0.00], expectation: [0.00], error: 0.00, loss: 0.0
  Input: [1.00|0.00], output: [1.00], expectation: [1.00], error: 0.00, loss: 0.0
  Average error: 0.00, average loss: 0.00, success: true
Trained epochs: 33, total average error: 14.5, total average loss: 14.5

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
    "2,1,0.8300457,-0.0097241225",
    "1,1,1.0"
  ],
  "weights": [
    "1,1,0.0",
    "2,2,0.4685011,-0.080415726,-1.0670729,0.13275132",
    "1,2,0.3728485,0.969226"
  ],
  "biases": [
    "1,1,0.0",
    "2,1,0.3615446,0.09466059",
    "1,1,-0.27281678"
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
Neural net activator: nl.zeesoft.zdk.functions.ZTanH, output activator: nl.zeesoft.zdk.functions.ZTanH, learning rate: 0.0024
Initial test results;
  Input: [0.00|0.00], output: [-0.47], expectation: [0.00], error: 0.47, loss: 0.46937037
  Input: [1.00|1.00], output: [-0.03], expectation: [0.00], error: 0.03, loss: 0.034987185
  Input: [0.00|1.00], output: [0.17], expectation: [1.00], error: 0.83, loss: 0.83166164
  Input: [1.00|0.00], output: [-0.59], expectation: [1.00], error: 1.59, loss: 1.5856783
  Average error: 0.73, average loss: 0.73, success: false

00100 ----------------/ 69%
00200 ----------------| 69%
00300 ----------------/ 68%
00400 ----------------| 68%
00500 ----------------| 68%
00600 ----------------| 68%
00700 ----------------| 68%
00800 ----------------| 68%
00900 ----------------| 68%
01000 ----------------| 68%
01100 ----------------| 68%
01200 ----------------| 68%
01300 ----------------| 68%
01400 ----------------| 68%
01500 ----------------| 68%
01600 ----------------| 68%
01700 ---------------/ 67%
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
03500 ---------------| 67%
03600 ---------------| 67%
03700 ---------------| 67%
03800 ---------------/ 66%
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
04900 ---------------/ 65%
05000 ---------------| 65%
05100 ---------------| 65%
05200 ---------------| 65%
05300 ---------------| 65%
05400 ---------------| 65%
05500 ---------------| 65%
05600 ---------------| 65%
05700 ---------------/ 64%
05800 ---------------| 64%
05900 ---------------| 64%
06000 ---------------| 64%
06100 ---------------| 64%
06200 ---------------| 64%
06300 ---------------| 64%
06400 --------------/ 63%
06500 --------------| 63%
06600 --------------| 63%
06700 --------------| 63%
06800 --------------| 63%
06900 --------------| 63%
07000 --------------| 63%
07100 --------------| 63%
07200 --------------/ 62%
07300 --------------| 62%
07400 --------------| 62%
07500 --------------| 62%
07600 --------------| 62%
07700 --------------| 62%
07800 --------------| 62%
07900 --------------| 62%
08000 --------------/ 61%
08100 --------------| 61%
08200 --------------| 61%
08300 --------------| 61%
08400 --------------| 61%
08500 --------------| 61%
08600 --------------| 61%
08700 --------------| 61%
08800 --------------| 61%
08900 --------------| 61%
09000 --------------/ 60%
09100 --------------| 60%
09200 --------------| 60%
09300 --------------| 60%
09400 --------------| 60%
09500 --------------| 60%
09600 --------------| 60%
09700 --------------| 60%
09800 --------------| 60%
09900 --------------| 60%
10000 --------------| 60%

Latest test results;
  Input: [0.00|0.00], output: [0.28], expectation: [0.00], error: -0.28, loss: 0.28110623
  Input: [1.00|1.00], output: [0.60], expectation: [0.00], error: -0.60, loss: 0.59508735
  Input: [1.00|0.00], output: [0.62], expectation: [1.00], error: 0.38, loss: 0.37950283
  Input: [0.00|1.00], output: [0.49], expectation: [1.00], error: 0.51, loss: 0.5086038
  Average error: 0.44, average loss: 0.44, success: false
Trained epochs: 10000, total average error: 4766.8794, total average loss: 4766.8794
================================================================================
Neural net activator: nl.zeesoft.zdk.functions.ZReLU, output activator: nl.zeesoft.zdk.functions.ZReLU, learning rate: 0.126
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
  Input: [1.00|0.00], output: [0.00], expectation: [1.00], error: 1.00, loss: 1.0
  Input: [0.00|0.00], output: [0.00], expectation: [0.00], error: 0.00, loss: 0.0
  Input: [1.00|1.00], output: [0.00], expectation: [0.00], error: 0.00, loss: 0.0
  Input: [0.00|1.00], output: [0.00], expectation: [1.00], error: 1.00, loss: 1.0
  Average error: 0.50, average loss: 0.50, success: false
Trained epochs: 10000, total average error: 5000.01, total average loss: 5000.01
================================================================================
Neural net activator: nl.zeesoft.zdk.functions.ZTanH, output activator: nl.zeesoft.zdk.functions.ZReLU, learning rate: 0.089600004
Initial test results;
  Input: [0.00|0.00], output: [0.58], expectation: [0.00], error: -0.58, loss: 0.5841926
  Input: [1.00|1.00], output: [0.62], expectation: [0.00], error: -0.62, loss: 0.62437373
  Input: [0.00|1.00], output: [0.64], expectation: [1.00], error: 0.36, loss: 0.36366034
  Input: [1.00|0.00], output: [0.56], expectation: [1.00], error: 0.44, loss: 0.44047946
  Average error: 0.50, average loss: 0.50, success: false

00100 ---------------------/ 90%

Latest test results;
  Input: [0.00|0.00], output: [0.09], expectation: [0.00], error: -0.09, loss: 0.093170166
  Input: [1.00|0.00], output: [0.92], expectation: [1.00], error: 0.08, loss: 0.07650137
  Input: [1.00|1.00], output: [0.08], expectation: [0.00], error: -0.08, loss: 0.083233476
  Input: [0.00|1.00], output: [0.90], expectation: [1.00], error: 0.10, loss: 0.099842906
  Average error: 0.09, average loss: 0.09, success: true
Trained epochs: 197, total average error: 77.57227, total average loss: 77.57227
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
2019-09-04 12:50:16:929 DBG nl.zeesoft.zdk.genetic.Evolver: Started
2019-09-04 12:50:16:989 DBG nl.zeesoft.zdk.genetic.Evolver: Selected new best genetic neural net;
- Code: 8399542413662136
- Size: 14
- Initial average loss: 0.50000 (final: 0.00000)
- Total average loss: 40.75000 (epochs: 66)
- Training result: 20.37500
2019-09-04 12:50:17:073 DBG nl.zeesoft.zdk.genetic.Evolver: Selected new best genetic neural net;
- Code: 6964821507716406
- Size: 14
- Initial average loss: 0.49647 (final: 0.05428)
- Total average loss: 22.49310 (epochs: 78)
- Training result: 11.16715
2019-09-04 12:50:19:631 DBG nl.zeesoft.zdk.genetic.Evolver: Selected new best genetic neural net;
- Code: 6964821507716406
- Size: 14
- Initial average loss: 0.49647 (final: 0.04954)
- Total average loss: 18.76678 (epochs: 68)
- Training result: 9.31714
2019-09-04 12:50:23:197 DBG nl.zeesoft.zdk.genetic.Evolver: Selected new best genetic neural net;
- Code: 6924821507716406
- Size: 14
- Initial average loss: 0.49142 (final: 0.04904)
- Total average loss: 16.32451 (epochs: 62)
- Training result: 8.02219
2019-09-04 12:50:24:954 DBG nl.zeesoft.zdk.genetic.Evolver: Selected new best genetic neural net;
- Code: 2602763215061766
- Size: 14
- Initial average loss: 0.50000 (final: 0.00000)
- Total average loss: 12.25000 (epochs: 20)
- Training result: 6.12500
2019-09-04 12:50:25:848 DBG nl.zeesoft.zdk.genetic.Evolver: Selected new best genetic neural net;
- Code: 2602763215031766
- Size: 14
- Initial average loss: 0.50000 (final: 0.00000)
- Total average loss: 5.25000 (epochs: 11)
- Training result: 2.62500
2019-09-04 12:50:27:322 DBG nl.zeesoft.zdk.genetic.Evolver: Selected new best genetic neural net;
- Code: 2702763215031766
- Size: 14
- Initial average loss: 0.50000 (final: 0.00000)
- Total average loss: 3.00000 (epochs: 9)
- Training result: 1.50000
2019-09-04 12:50:28:012 DBG nl.zeesoft.zdk.genetic.Evolver: Selected new best genetic neural net;
- Code: 1702763215031766
- Size: 14
- Initial average loss: 0.25000 (final: 0.00000)
- Total average loss: 4.75000 (epochs: 10)
- Training result: 1.18750
2019-09-04 12:50:28:032 DBG nl.zeesoft.zdk.genetic.Evolver: Selected new best genetic neural net;
- Code: 1702764215031766
- Size: 14
- Initial average loss: 0.25000 (final: 0.00000)
- Total average loss: 2.75000 (epochs: 8)
- Training result: 0.68750
2019-09-04 12:50:28:134 DBG nl.zeesoft.zdk.genetic.Evolver: Selected new best genetic neural net;
- Code: 1702764315031766
- Size: 14
- Initial average loss: 0.25000 (final: 0.00000)
- Total average loss: 2.25000 (epochs: 7)
- Training result: 0.56250
2019-09-04 12:50:28:143 DBG nl.zeesoft.zdk.genetic.Evolver: Selected new best genetic neural net;
- Code: 6702764315031766
- Size: 14
- Initial average loss: 0.25000 (final: 0.00000)
- Total average loss: 1.00000 (epochs: 4)
- Training result: 0.25000
2019-09-04 12:50:28:184 DBG nl.zeesoft.zdk.genetic.Evolver: Selected new best genetic neural net;
- Code: 6705764315031766
- Size: 14
- Initial average loss: 0.25000 (final: 0.00000)
- Total average loss: 0.75000 (epochs: 4)
- Training result: 0.18750
2019-09-04 12:50:28:761 DBG nl.zeesoft.zdk.genetic.Evolver: Selected new best genetic neural net;
- Code: 6705764315031763
- Size: 14
- Initial average loss: 0.25000 (final: 0.00000)
- Total average loss: 0.50000 (epochs: 3)
- Training result: 0.12500
2019-09-04 12:50:29:015 DBG nl.zeesoft.zdk.genetic.Evolver: Selected new best genetic neural net;
- Code: 6705764315031763
- Size: 14
- Initial average loss: 0.25000 (final: 0.00000)
- Total average loss: 0.25000 (epochs: 2)
- Training result: 0.06250
2019-09-04 12:50:30:085 DBG nl.zeesoft.zdk.genetic.Evolver: Selected new best genetic neural net;
- Code: 6705724315031763
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
    "2019-09-04 12:50:16:989 SEL code: 8399542413662136, size: 14, initial loss: 0.50000 (final: 0.00000), total loss: 40.75000, result: 20.37500 (epochs: 66)",
    "2019-09-04 12:50:17:073 SEL code: 6964821507716406, size: 14, initial loss: 0.49647 (final: 0.05428), total loss: 22.49310, result: 11.16715 (epochs: 78)",
    "2019-09-04 12:50:19:631 SEL code: 6964821507716406, size: 14, initial loss: 0.49647 (final: 0.04954), total loss: 18.76678, result: 9.31714 (epochs: 68)",
    "2019-09-04 12:50:23:197 SEL code: 6924821507716406, size: 14, initial loss: 0.49142 (final: 0.04904), total loss: 16.32451, result: 8.02219 (epochs: 62)",
    "2019-09-04 12:50:24:954 SEL code: 2602763215061766, size: 14, initial loss: 0.50000 (final: 0.00000), total loss: 12.25000, result: 6.12500 (epochs: 20)",
    "2019-09-04 12:50:25:848 SEL code: 2602763215031766, size: 14, initial loss: 0.50000 (final: 0.00000), total loss: 5.25000, result: 2.62500 (epochs: 11)",
    "2019-09-04 12:50:27:322 SEL code: 2702763215031766, size: 14, initial loss: 0.50000 (final: 0.00000), total loss: 3.00000, result: 1.50000 (epochs: 9)",
    "2019-09-04 12:50:28:013 SEL code: 1702763215031766, size: 14, initial loss: 0.25000 (final: 0.00000), total loss: 4.75000, result: 1.18750 (epochs: 10)",
    "2019-09-04 12:50:28:032 SEL code: 1702764215031766, size: 14, initial loss: 0.25000 (final: 0.00000), total loss: 2.75000, result: 0.68750 (epochs: 8)",
    "2019-09-04 12:50:28:134 SEL code: 1702764315031766, size: 14, initial loss: 0.25000 (final: 0.00000), total loss: 2.25000, result: 0.56250 (epochs: 7)",
    "2019-09-04 12:50:28:143 SEL code: 6702764315031766, size: 14, initial loss: 0.25000 (final: 0.00000), total loss: 1.00000, result: 0.25000 (epochs: 4)",
    "2019-09-04 12:50:28:184 SEL code: 6705764315031766, size: 14, initial loss: 0.25000 (final: 0.00000), total loss: 0.75000, result: 0.18750 (epochs: 4)",
    "2019-09-04 12:50:28:762 SEL code: 6705764315031763, size: 14, initial loss: 0.25000 (final: 0.00000), total loss: 0.50000, result: 0.12500 (epochs: 3)",
    "2019-09-04 12:50:29:015 SEL code: 6705764315031763, size: 14, initial loss: 0.25000 (final: 0.00000), total loss: 0.25000, result: 0.06250 (epochs: 2)",
    "2019-09-04 12:50:30:086 SEL code: 6705724315031763, size: 14, initial loss: 0.00000 (final: 0.00000), total loss: 0.00000, result: 0.00000 (epochs: 0)"
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
          "code": "UKqIfGmHkCBF3E6BrEGGaL4HtBeHiKuMOOmOLLSAVEyJ4EsILJhFJHdAEKBLBABFYJ0B2",
          "neuralNet": [
            {
              "inputNeurons": 2,
              "hiddenLayers": 1,
              "hiddenNeurons": 2,
              "outputNeurons": 1,
              "weightFunction": "nl.zeesoft.zdk.functions.ZWeightKaiming",
              "biasFunction": "nl.zeesoft.zdk.functions.ZWeightXavier",
              "activator": "nl.zeesoft.zdk.functions.ZTanH",
              "outputActivator": "nl.zeesoft.zdk.functions.ZSoftmaxTop",
              "learningRate": 0.1912,
              "values": [
                "2,1,0.0,0.0",
                "2,1,-0.44608742,0.77114475",
                "1,1,0.0"
              ],
              "weights": [
                "1,1,0.0",
                "2,2,-0.30536973,0.5486858,0.7479109,-0.7952677",
                "1,2,-0.5486857,-0.5552177"
              ],
              "biases": [
                "1,1,0.0",
                "2,1,-0.479805,1.0231458",
                "1,1,0.17636333"
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
                  "inputs": "1.0,0.0",
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

Test results
------------
All 11 tests have been executed successfully (57 assertions).  
Total test duration: 15557 ms (total sleep duration: 13600 ms).  

Memory usage per test;  
 * nl.zeesoft.zdk.test.impl.TestZStringEncoder: 531 Kb / 0 Mb
 * nl.zeesoft.zdk.test.impl.TestZStringSymbolParser: 404 Kb / 0 Mb
 * nl.zeesoft.zdk.test.impl.TestCsv: 412 Kb / 0 Mb
 * nl.zeesoft.zdk.test.impl.TestJson: 426 Kb / 0 Mb
 * nl.zeesoft.zdk.test.impl.TestZHttpRequest: 432 Kb / 0 Mb
 * nl.zeesoft.zdk.test.impl.TestMessenger: 690 Kb / 0 Mb
 * nl.zeesoft.zdk.test.impl.TestZMatrix: 816 Kb / 0 Mb
 * nl.zeesoft.zdk.test.impl.TestGeneticCode: 777 Kb / 0 Mb
 * nl.zeesoft.zdk.test.impl.TestNeuralNet: 1871 Kb / 1 Mb
 * nl.zeesoft.zdk.test.impl.TestGeneticNN: 1327 Kb / 1 Mb
 * nl.zeesoft.zdk.test.impl.TestEvolver: 53493 Kb / 52 Mb
