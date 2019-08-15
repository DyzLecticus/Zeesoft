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
Key: 7372340814726588866234910787234883488927569764204302490246584138
Input text: Hello, my name is 'Dyz Lecticus'. How are you feeling today? :-) (Don't you know how to: [re;spond]!).

Key encoded text: EZkX81nWeXHW1UP1SV2Xb1rWCZYZe1l2l2vYiYJVsXzYp1SU4Vu1p251eWoXrYi1215WKXs2~2x1wWu1OYJYh3v1CZBW3WLUKXSWGUwWgYl1RUwW1YtZgZl1#WQUST~T4ZoWwZqWdXCWjVDZSVfYi1rVAZ8Zs2z241~ZpZyW5WtYA2AUZUm1n211AVmXoYp2128X6Yf1p1I10
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
2019-08-16 01:51:40:091 DBG nl.zeesoft.zdk.test.impl.TestMessengerListener: Test log debug message before Messenger has started
2019-08-16 01:51:40:393 ERR nl.zeesoft.zdk.test.impl.TestMessengerListener: Test log error message while Messenger is working
2019-08-16 01:51:40:394 ERR nl.zeesoft.zdk.test.impl.TestMessengerListener: Test log exception stack trace
java.lang.NumberFormatException: For input string: "A"
	at java.lang.NumberFormatException.forInputString(NumberFormatException.java:65)
	at java.lang.Integer.parseInt(Integer.java:580)
	at java.lang.Integer.parseInt(Integer.java:615)
	at nl.zeesoft.zdk.test.impl.TestMessenger.test(TestMessenger.java:83)
	at nl.zeesoft.zdk.test.Tester.test(Tester.java:69)
	at nl.zeesoft.zdk.test.LibraryObject.describeAndTest(LibraryObject.java:39)
	at nl.zeesoft.zdk.test.impl.ZDK.main(ZDK.java:39)

2019-08-16 01:51:40:761 WRN nl.zeesoft.zdk.test.impl.TestMessengerListener: Test log warning message after Messenger has stopped
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
000.41 | 000.73 | 000.19
-------+--------+-------
-00.75 | 000.16 | -00.65

Randomized multiplied element wise;
012.15 | 021.98 | 005.76
-------+--------+-------
-22.48 | 004.85 | -19.63

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
-00.35 | 000.00 | 000.70
-------+--------+-------
000.96 | 000.12 | 000.24

Randomized matrix transposed;
-00.35 | 000.96
-------+-------
000.00 | 000.12
-------+-------
000.70 | 000.24
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
Genetic code: 7221721612859112146794029189612021594292865641608740731218645758190598028052303291414872655935849651
Mutated code: 7221731612859112146794029189612021594892865641608740731818645358190598028052303291414672655935849651
                   ^                               ^                 ^     ^                       ^              

Scaled property values;
0: 120
1: 940
2: 402 <
3: 215
4: 221
5: 961
6: 32
7: 186
8: 217
9: 121
10: 21
11: 612
12: 802 <
13: 407
14: 303
15: 948 <
16: 731 <
17: 612
18: 286 <
19: 612
20: 32 <
21: 128
22: 489 <
23: 112
24: 612
25: 318 <
26: 645
27: 407
28: 230 <
29: 581
30: 849
31: 358
32: 159
Mutated property values: 9
~~~~

nl.zeesoft.zdk.test.impl.TestNeuralNet
--------------------------------------
This test shows how to create, train and use a *NeuralNet*.

**Example implementation**  
~~~~
// Create the neural net
NeuralNet nn = new NeuralNet(inputNeurons,hiddenLayers,hiddenNeurons,outputNeurons);
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
  Input: [0.00|0.00], output: [-0.01], expectation: [0.00], error: 0.01, loss: 0.006566665
  Input: [1.00|1.00], output: [-0.01], expectation: [0.00], error: 0.01, loss: 0.010461977
  Input: [0.00|1.00], output: [-0.01], expectation: [1.00], error: 1.01, loss: 1.0056201
  Input: [1.00|0.00], output: [-0.01], expectation: [1.00], error: 1.01, loss: 1.0076443
  Average error: 0.51, average loss: 0.51, success: false
Latest test results;
  Input: [1.00|0.00], output: [-0.00], expectation: [1.00], error: 1.00, loss: 1.0001864
  Input: [0.00|1.00], output: [1.01], expectation: [1.00], error: -0.01, loss: 0.009772778
  Input: [1.00|1.00], output: [-0.00], expectation: [0.00], error: 0.00, loss: 9.560436E-5
  Input: [0.00|0.00], output: [0.01], expectation: [0.00], error: -0.01, loss: 0.014287002
  Average error: 0.26, average loss: 0.26, success: false
Trained epochs: 5000, error change rate: 5.029757E-5, loss change rate: 5.029757E-5
================================================================================
Neural net activator: nl.zeesoft.zdk.functions.ZLeakyReLU, output activator: nl.zeesoft.zdk.functions.ZSoftmaxTop, learning rate: 0.1
Initial test results;
  Input: [0.00|0.00], output: [1.00], expectation: [0.00], error: -1.00, loss: 1.0
  Input: [1.00|1.00], output: [1.00], expectation: [0.00], error: -1.00, loss: 1.0
  Input: [0.00|1.00], output: [1.00], expectation: [1.00], error: 0.00, loss: 0.0
  Input: [1.00|0.00], output: [1.00], expectation: [1.00], error: 0.00, loss: 0.0
  Average error: 0.50, average loss: 0.50, success: false
Latest test results;
  Input: [0.00|1.00], output: [1.00], expectation: [1.00], error: 0.00, loss: 0.0
  Input: [1.00|0.00], output: [1.00], expectation: [1.00], error: 0.00, loss: 0.0
  Input: [1.00|1.00], output: [0.00], expectation: [0.00], error: 0.00, loss: 0.0
  Input: [0.00|0.00], output: [0.00], expectation: [0.00], error: 0.00, loss: 0.0
  Average error: 0.00, average loss: 0.00, success: true
Trained epochs: 2008, error change rate: 2.49004E-4, loss change rate: 2.49004E-4

Neural net JSON;
{
  "inputNeurons": 2,
  "hiddenLayers": 1,
  "hiddenNeurons": 2,
  "outputNeurons": 1,
  "activator": "nl.zeesoft.zdk.functions.ZLeakyReLU",
  "outputActivator": "nl.zeesoft.zdk.functions.ZSoftmaxTop",
  "learningRate": 0.1,
  "values": [
    "2,1,0.0,0.0",
    "2,1,0.5369457,-0.017742906",
    "1,1,0.0"
  ],
  "weights": [
    "1,1,0.0",
    "2,2,-0.29537186,-0.77666014,-0.8777653,-2.0815716",
    "1,2,-0.06183946,0.76430285"
  ],
  "biases": [
    "1,1,0.0",
    "2,1,0.5369457,-1.7742907",
    "1,1,0.03584087"
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
Neural net activator: nl.zeesoft.zdk.functions.ZTanH, output activator: nl.zeesoft.zdk.functions.ZReLU, learning rate: 0.0726
Initial test results;
  Input: [0.00|0.00], output: [1.42], expectation: [0.00], error: -1.42, loss: 1.4248079
  Input: [1.00|1.00], output: [1.55], expectation: [0.00], error: -1.55, loss: 1.5489203
  Input: [0.00|1.00], output: [1.44], expectation: [1.00], error: -0.44, loss: 0.441648
  Input: [1.00|0.00], output: [1.41], expectation: [1.00], error: -0.41, loss: 0.4112078
  Average error: 0.96, average loss: 0.96, success: false
Latest test results;
  Input: [0.00|1.00], output: [0.52], expectation: [1.00], error: 0.48, loss: 0.47828138
  Input: [1.00|1.00], output: [0.63], expectation: [0.00], error: -0.63, loss: 0.6258883
  Input: [1.00|0.00], output: [0.49], expectation: [1.00], error: 0.51, loss: 0.5104301
  Input: [0.00|0.00], output: [0.60], expectation: [0.00], error: -0.60, loss: 0.6007438
  Average error: 0.55, average loss: 0.55, success: false
Trained epochs: 5000, error change rate: 8.056202E-5, loss change rate: 8.056202E-5
================================================================================
Neural net activator: nl.zeesoft.zdk.functions.ZTanH, output activator: nl.zeesoft.zdk.functions.ZLeakyReLU, learning rate: 0.0677
Initial test results;
  Input: [0.00|0.00], output: [0.28], expectation: [0.00], error: -0.28, loss: 0.2792269
  Input: [1.00|1.00], output: [0.75], expectation: [0.00], error: -0.75, loss: 0.7505909
  Input: [0.00|1.00], output: [0.68], expectation: [1.00], error: 0.32, loss: 0.31851816
  Input: [1.00|0.00], output: [0.56], expectation: [1.00], error: 0.44, loss: 0.44433308
  Average error: 0.45, average loss: 0.45, success: false
Latest test results;
  Input: [1.00|0.00], output: [0.91], expectation: [1.00], error: 0.09, loss: 0.08732468
  Input: [1.00|1.00], output: [0.10], expectation: [0.00], error: -0.10, loss: 0.095746994
  Input: [0.00|1.00], output: [0.90], expectation: [1.00], error: 0.10, loss: 0.098106444
  Input: [0.00|0.00], output: [0.10], expectation: [0.00], error: -0.10, loss: 0.09621692
  Average error: 0.09, average loss: 0.09, success: true
Trained epochs: 587, error change rate: 6.0275727E-4, loss change rate: 6.0275727E-4

~~~~

Test results
------------
All 10 tests have been executed successfully (54 assertions).  
Total test duration: 1909 ms (total sleep duration: 600 ms).  

Memory usage per test;  
 * nl.zeesoft.zdk.test.impl.TestZStringEncoder: 521 Kb / 0 Mb
 * nl.zeesoft.zdk.test.impl.TestZStringSymbolParser: 404 Kb / 0 Mb
 * nl.zeesoft.zdk.test.impl.TestCsv: 411 Kb / 0 Mb
 * nl.zeesoft.zdk.test.impl.TestJson: 424 Kb / 0 Mb
 * nl.zeesoft.zdk.test.impl.TestZHttpRequest: 431 Kb / 0 Mb
 * nl.zeesoft.zdk.test.impl.TestMessenger: 689 Kb / 0 Mb
 * nl.zeesoft.zdk.test.impl.TestZMatrix: 805 Kb / 0 Mb
 * nl.zeesoft.zdk.test.impl.TestGeneticCode: 775 Kb / 0 Mb
 * nl.zeesoft.zdk.test.impl.TestNeuralNet: 3136 Kb / 3 Mb
 * nl.zeesoft.zdk.test.impl.TestGeneticNN: 6776 Kb / 6 Mb
