Zeesoft Development Kit
=======================
The Zeesoft Development Kit (ZDK) is an open source library for Java application development.  
It provides support for;  
 * Self documenting and testing libraries  
 * (Mock) File writing and reading  
 * Dynamic class instantiation and reflection  
 * Extended StringBuilder manipulation and validation  
 * TimeStamped logging  
 * Multi threading  
 * Collection queries and persistence  
 * HTTP servers and requests  
 * Advanced MIDI instrument and sequence pattern design  
 * High performance manipulation of large multi dimensional data structures  
 * [Hierarchical Temporal Memory](https://numenta.com/)  
   * Sparse distributed representations  
   * Detailed neural cell modeling  
   * Spatial pooling  
   * Temporal memory  
   * Value classification and prediction  
   * Neural networks  
  
HTM implementation notes;  
 * SDRs have two dimensions by default in order to retain topographical properties throughout networks  
 * Individual SDR processors and networks use multi threading to maximize performance  
 * The spatial pooler does not support local inhibition  
 * The temporal memory does not generate any initial segments/synapses  
 * The temporal memory supports optional apical feedback  
 * The classifier can be configured to slowly forget old SDR associations  
 * The implementation allows for SDR processor and network customization via configuration and/or code extension   
  
**Release downloads**  
Click [here](https://github.com/DyzLecticus/Zeesoft/raw/master/V5.0/ZDK/releases/zdk-0.9.0.zip) to download the latest ZDK release (version 0.9.0).  
All ZDK releases can be downloaded [here](https://github.com/DyzLecticus/Zeesoft/raw/master/V5.0/ZDK/releases/).  
*All jar files in the release include source code and build scripts.*  

**Self documenting and self testing**  
The tests used to develop this libary are also used to generate this README file.  
Run the [ZDK](https://github.com/DyzLecticus/Zeesoft/blob/master/V5.0/ZDK/src/nl/zeesoft/zdk/test/ZDK.java) class as a java application to print this documentation to the standard out.  
Click [here](#test-results) to scroll down to the test result summary.  

nl.zeesoft.zdk.test.TestStr
---------------------------
This test shows how a *Str* instance can be used to split a comma separated string into a list of *Str* instances. 
The *Str* class is designed to add features of the Java String to a Java StringBuilder. 
It also contains methods for file writing and reading. 

**Example implementation**  
~~~~
// Create the Str
Str str = new Str("qwer,asdf,zxcv");
// Split the Str
List<Str> strs = str.split(",");
~~~~

This test uses the *MockStr*.

Class references;  
 * [TestStr](https://github.com/DyzLecticus/Zeesoft/blob/master/V5.0/ZDK/src/nl/zeesoft/zdk/test/TestStr.java)
 * [Str](https://github.com/DyzLecticus/Zeesoft/blob/master/V5.0/ZDK/src/nl/zeesoft/zdk/Str.java)

**Test output**  
The output of this test shows the string input and lists the *Str* objects.  
~~~~
Input: qwer,asdf,zxcv
Split; 
- qwer
- asdf
- zxcv
~~~~

nl.zeesoft.zdk.test.thread.TestRunCode
--------------------------------------
This test shows how to use a *RunCode* instance combined with a *CodeRunner* instance to run code in a separate thread. 
It also shows how to use a *CodeRunnerList* instance to run different code in multiple threads simultaneously. 
*Lock* instances are used to prevent concurrent modification exceptions. 

A *CodeRunner* will repeat calling the *RunCode*.run() method until the method returns true or the method throws an exception. 
It can also be forced to stop by calling the *CodeRunner*.stop() method. 
The *CodeRunnerList* implements similar logic; it stops when all its *CodeRunner* instances have stopped or one of them throws an exception. 
The *CodeRunnerList* can also be forced to stop by calling the *CodeRunnerList*.stop() method. 

Exceptions are caught and supressed by default but they are available through getException() methods. 

**Example implementation**  
~~~~
// Create the run code
RunCode code = new RunCode() {
    private int counter = 0;
    @Override
    protected boolean run() {
        counter++;
        System.out.println("Code run: " + counter);
        boolean r = counter >= 10;
        if (r) {
            counter = 0;
        }
        return r;
    }
};
// Create the code runner
CodeRunner runner = new CodeRunner(code);
// Specify the number of milliseconds to sleep between runs
runner.setSleepMs(1);
// Start the runner
runner.start();
~~~~

Class references;  
 * [TestRunCode](https://github.com/DyzLecticus/Zeesoft/blob/master/V5.0/ZDK/src/nl/zeesoft/zdk/test/thread/TestRunCode.java)
 * [RunCode](https://github.com/DyzLecticus/Zeesoft/blob/master/V5.0/ZDK/src/nl/zeesoft/zdk/thread/RunCode.java)
 * [CodeRunner](https://github.com/DyzLecticus/Zeesoft/blob/master/V5.0/ZDK/src/nl/zeesoft/zdk/thread/CodeRunner.java)
 * [CodeRunnerList](https://github.com/DyzLecticus/Zeesoft/blob/master/V5.0/ZDK/src/nl/zeesoft/zdk/thread/CodeRunnerList.java)
 * [Lock](https://github.com/DyzLecticus/Zeesoft/blob/master/V5.0/ZDK/src/nl/zeesoft/zdk/thread/Lock.java)

**Test output**  
The output of this test shows the output of several different code runs.  
~~~~
Test 10 runs;
Code run: 1
Code run: 2
Code run: 3
Code run: 4
Code run: 5
Code run: 6
Code run: 7
Code run: 8
Code run: 9
Code run: 10

Test exception handling;
Caught exception; java.lang.NumberFormatException: For input string: "X"

Test runner list;
Code run: 1
Code run: 1
Code run: 2
Code run: 2
Code run: 3
Code run: 3
Code run: 4
Code run: 4
Code run: 5
Code run: 5
Code run: 6
Code run: 6
Code run: 7
Code run: 7
Code run: 8
Code run: 8
Code run: 9
Code run: 9
Code run: 10
Code run: 10

Test runner list exception handling;
Code run: 1
Code run: 1
Caught exception; java.lang.NumberFormatException: For input string: "X"
~~~~

nl.zeesoft.zdk.test.thread.TestCodeRunnerChain
----------------------------------------------
This test shows how to use a *CodeRunnerChain* instance to run a sequence of *CodeRunnerList* instances in a separate threads. 
Classes that implement *ProgressListener* can be used to listen to the progress of all *RunCode* instances in the entire chain. 
An example of this is a *ProgressBar* that logs the chain progress to the console standard output. 

**Example implementation**  
~~~~
// Create the chain
CodeRunnerChain chain = new CodeRunnerChain();
// Add a list of codes
chain.addAll(new ArrayList<RunCode>());
// Add a single code
chain.add(new RunCode());
// Add a progress listener to the chain
chain.addProgressListener(new ProgressBar("Process description"));
// Start the chain
chain.start();
~~~~

Class references;  
 * [TestCodeRunnerChain](https://github.com/DyzLecticus/Zeesoft/blob/master/V5.0/ZDK/src/nl/zeesoft/zdk/test/thread/TestCodeRunnerChain.java)
 * [CodeRunnerChain](https://github.com/DyzLecticus/Zeesoft/blob/master/V5.0/ZDK/src/nl/zeesoft/zdk/thread/CodeRunnerChain.java)
 * [CodeRunnerList](https://github.com/DyzLecticus/Zeesoft/blob/master/V5.0/ZDK/src/nl/zeesoft/zdk/thread/CodeRunnerList.java)
 * [ProgressListener](https://github.com/DyzLecticus/Zeesoft/blob/master/V5.0/ZDK/src/nl/zeesoft/zdk/thread/ProgressListener.java)
 * [ProgressBar](https://github.com/DyzLecticus/Zeesoft/blob/master/V5.0/ZDK/src/nl/zeesoft/zdk/thread/ProgressBar.java)

**Test output**  
The output of this test shows;  
 * An example chain that imports multiple files simultaneously and then processes the results.  
 * An example of multiple chains using progress bars to show their progress simultaneously.  
~~~~
File import;
Reading file: 1
Reading file: 3
Reading file: 2
Reading file: 4
Imported 4 files

Progress bars;
Primary tasks [==                  ] 10%Primary tasks [====                ] 20%Primary tasks [======              ] 30%Primary tasks [========            ] 40%Primary tasks [==========          ] 50%Primary tasks [============        ] 60%Primary tasks [==============      ] 70%Primary tasks [================    ] 80%Primary tasks [==================  ] 90%Primary tasks [====================] 100%
Secondary tasks [==================  ] 90%Secondary tasks [====================] 100%
~~~~

nl.zeesoft.zdk.test.collection.TestCollections
----------------------------------------------
This test shows how to use different collections provided by this library. 
These collections are a mix between Java List and LinkedList style collections. 
They use Java reflection to provide features like queries and persistence with minimal programming. 
The following collections are provided by this library;  
 * *QueryableCollection* provides support for queries.  
 * *CompleteCollection* extends *QueryableCollection* and automatically adds referenced objects. 
 * *PersistableCollection* extends *CompleteCollection* and adds persistence. 
 * *CompressedCollection* extends *PersistableCollection* and adds compression to persistence. 
 * *PartitionedCollection* extends *CompressedCollection* and adds multithreading for saving/loading large collections to/from directories. 

Persistence for most standard property types is supported including arrays and lists for non primitives. 

**Example implementation**  
~~~~
// Create the PersistableCollection
PersistableCollection collection = new PersistableCollection();
// Add objects to the collection
Str id1 = collection.add(new PersistableParent());
Str id2 = collection.add(new PersistableChild());
// Write the data to a file
collection.toPath("data/fileName.txt");
// Load the collection from a file
collection.fromPath("data/fileName.txt");
// Query the collection
SortedMap<Str,Object> results = collection.query(
    Query.create(PersistableParent.class)
    .equals("getTestString","Parent")
    .notContains("getTestIntArray",42)
).results;
~~~~

Class references;  
 * [TestCollections](https://github.com/DyzLecticus/Zeesoft/blob/master/V5.0/ZDK/src/nl/zeesoft/zdk/test/collection/TestCollections.java)
 * [QueryableCollection](https://github.com/DyzLecticus/Zeesoft/blob/master/V5.0/ZDK/src/nl/zeesoft/zdk/collection/QueryableCollection.java)
 * [CompleteCollection](https://github.com/DyzLecticus/Zeesoft/blob/master/V5.0/ZDK/src/nl/zeesoft/zdk/collection/CompleteCollection.java)
 * [PersistableCollection](https://github.com/DyzLecticus/Zeesoft/blob/master/V5.0/ZDK/src/nl/zeesoft/zdk/collection/PersistableCollection.java)
 * [CompressedCollection](https://github.com/DyzLecticus/Zeesoft/blob/master/V5.0/ZDK/src/nl/zeesoft/zdk/collection/CompressedCollection.java)
 * [PartitionedCollection](https://github.com/DyzLecticus/Zeesoft/blob/master/V5.0/ZDK/src/nl/zeesoft/zdk/collection/PartitionedCollection.java)

**Test output**  
The output of this test shows the file structure of an example persistable collection.  
~~~~
Regular;
@NI|5
@SO|
@PO|nl.zeesoft.zdk.test.collection.CollectionTestChild@2
@PP|testInt|EQ=111
@PP|testBoolean|EQ=true
@PP|testParents|EQ=nl.zeesoft.zdk.test.collection.CollectionTestParent@LS|nl.zeesoft.zdk.test.collection.CollectionTestParent@1|LC|nl.zeesoft.zdk.test.collection.CollectionTestParent@3|LE@
@PP|testStrs|EQ=nl.zeesoft.zdk.Str@LS|null|LE@
@PP|testStr|EQ=I like pizza!
@PP|testString|EQ=TestChild1
@NO|
@PO|nl.zeesoft.zdk.test.collection.CollectionTestChild@4
@PP|testInt|EQ=222
@PP|testBoolean|EQ=false
@PP|testParents|EQ=nl.zeesoft.zdk.test.collection.CollectionTestParent@LS|nl.zeesoft.zdk.test.collection.CollectionTestParent@3|LC|null|LE@
@PP|testStrs|EQ=nl.zeesoft.zdk.Str@LS|null|LE@
@PP|testStr|EQ=I like ice cream!
@PP|testString|EQ=TestChild2
@NO|
@PO|nl.zeesoft.zdk.test.collection.CollectionTestParent@1
@PP|testStringList|EQ=java.util.List@LS||LE@
@PP|testFloatList|EQ=java.util.List@LS||LE@
@PP|testStringArray|EQ=java.lang.String@LS|TestElement1|LC|TestElement2|LE@
@PP|testIntArray|EQ=int@LS|0|LC|1|LC|2|LC|3|LE@
@PP|testChildren|EQ=java.util.List@LS|nl.zeesoft.zdk.test.collection.CollectionTestChild@2|LC|nl.zeesoft.zdk.test.collection.CollectionTestChild@4|LE@
@PP|testPartner|EQ=nl.zeesoft.zdk.test.collection.CollectionTestParent@3
@PP|testString|EQ=TestParent1
@NO|
@PO|nl.zeesoft.zdk.test.collection.CollectionTestParent@3
@PP|testStringList|EQ=java.util.List@LS||LE@
@PP|testFloatList|EQ=java.util.List@LS||LE@
@PP|testStringArray|EQ=null
@PP|testIntArray|EQ=null
@PP|testChildren|EQ=java.util.List@LS|nl.zeesoft.zdk.test.collection.CollectionTestChild@4|LE@
@PP|testPartner|EQ=nl.zeesoft.zdk.test.collection.CollectionTestParent@1
@PP|testString|EQ=TestParent2

Compressed;
@CN|000@|EQ=nl.zeesoft.zdk.test.collection.CollectionTestParent@
@CN|001@|EQ=nl.zeesoft.zdk.test.collection.CollectionTestChild@
@CN|002@|EQ=java.lang.Object@
@CN|003@|EQ=java.lang.StringBuilder@
@CN|004@|EQ=java.lang.String@
@CN|005@|EQ=java.lang.Integer@
@CN|006@|EQ=java.lang.Long@
@CN|007@|EQ=java.lang.Float@
@CN|008@|EQ=java.lang.Double@
@CN|009@|EQ=java.lang.Boolean@
@CN|010@|EQ=java.lang.Byte@
@CN|011@|EQ=java.lang.Short@
@CN|012@|EQ=java.math.BigDecimal@
@CN|013@|EQ=java.util.List@
@CN|014@|EQ=nl.zeesoft.zdk.Str@
@PN|000|EQ|EQ=@PP|testStringList|EQ
@PN|001|EQ|EQ=@PP|testFloatList|EQ
@PN|002|EQ|EQ=@PP|testStringArray|EQ
@PN|003|EQ|EQ=@PP|testIntArray|EQ
@PN|004|EQ|EQ=@PP|testChildren|EQ
@PN|005|EQ|EQ=@PP|testPartner|EQ
@PN|006|EQ|EQ=@PP|testString|EQ
@PN|007|EQ|EQ=@PP|testInt|EQ
@PN|008|EQ|EQ=@PP|testBoolean|EQ
@PN|009|EQ|EQ=@PP|testParents|EQ
@PN|010|EQ|EQ=@PP|testStrs|EQ
@PN|011|EQ|EQ=@PP|testStr|EQ
@NI|5
@SO|
@PO|@CN|001@2
@PN|007|EQ=111
@PN|008|EQ=true
@PN|009|EQ=@CN|000@LS|@CN|000@1|LC|@CN|000@3|LE@
@PN|010|EQ=@CN|014@LS|null|LE@
@PN|011|EQ=I like pizza!
@PN|006|EQ=TestChild1
@NO|
@PO|@CN|001@4
@PN|007|EQ=222
@PN|008|EQ=false
@PN|009|EQ=@CN|000@LS|@CN|000@3|LC|null|LE@
@PN|010|EQ=@CN|014@LS|null|LE@
@PN|011|EQ=I like ice cream!
@PN|006|EQ=TestChild2
@NO|
@PO|@CN|000@1
@PN|000|EQ=@CN|013@LS||LE@
@PN|001|EQ=@CN|013@LS||LE@
@PN|002|EQ=@CN|004@LS|TestElement1|LC|TestElement2|LE@
@PN|003|EQ=int@LS|0|LC|1|LC|2|LC|3|LE@
@PN|004|EQ=@CN|013@LS|@CN|001@2|LC|@CN|001@4|LE@
@PN|005|EQ=@CN|000@3
@PN|006|EQ=TestParent1
@NO|
@PO|@CN|000@3
@PN|000|EQ=@CN|013@LS||LE@
@PN|001|EQ=@CN|013@LS||LE@
@PN|002|EQ=null
@PN|003|EQ=null
@PN|004|EQ=@CN|013@LS|@CN|001@4|LE@
@PN|005|EQ=@CN|000@1
@PN|006|EQ=TestParent2

Original: 1692, compressed: 1725, compressed excluding header: 798
~~~~

nl.zeesoft.zdk.test.http.TestHttpServer
---------------------------------------
This test shows how to configure, start and stop an *HttpServer*. 
It also shows how an *HttpClient* can be used to send HTTP requests and process the responses. 
The *HttpServer* provided by this library does not support HTTPS. 
The behavior of the *HttpServer* can be customized by overriding and/or extending the *HttpServerConfig* and *HttpRequestHandler*. 
An example of such customization is the *ProxyServerConfig* and *ProxyRequestHandler* which will make the *HttpServer* function as a proxy server. 

**Example implementation**  
~~~~
// Create the configuration
HttpServerConfig config = new HttpServerConfig(logger);
// Create the server
HttpServer server = new HttpServer(config);
// Open the server
Str error = server.open();
// Create the client
HttpClient client = new HttpClient("GET","http://127.0.0.1:8080/");
// Send the request and get the response
client.sendRequest();
int responseCode = client.getResponseCode();
Str responseBody = client.getResponseBody();
// Close the server
error = server.close();
// Wait for connections to close if needed
Waiter.waitForRunners(server.getActiveRunners(),1000);
~~~~

Class references;  
 * [TestHttpServer](https://github.com/DyzLecticus/Zeesoft/blob/master/V5.0/ZDK/src/nl/zeesoft/zdk/test/http/TestHttpServer.java)
 * [HttpServer](https://github.com/DyzLecticus/Zeesoft/blob/master/V5.0/ZDK/src/nl/zeesoft/zdk/http/HttpServer.java)
 * [HttpServerConfig](https://github.com/DyzLecticus/Zeesoft/blob/master/V5.0/ZDK/src/nl/zeesoft/zdk/http/HttpServerConfig.java)
 * [HttpRequestHandler](https://github.com/DyzLecticus/Zeesoft/blob/master/V5.0/ZDK/src/nl/zeesoft/zdk/http/HttpRequestHandler.java)
 * [ProxyServerConfig](https://github.com/DyzLecticus/Zeesoft/blob/master/V5.0/ZDK/src/nl/zeesoft/zdk/http/ProxyServerConfig.java)
 * [ProxyRequestHandler](https://github.com/DyzLecticus/Zeesoft/blob/master/V5.0/ZDK/src/nl/zeesoft/zdk/http/ProxyRequestHandler.java)

**Test output**  
The output of this test shows the full debug logging of a regular HTTP server and a proxy server handling some basic requests.  
It also shows the mocked OS file actions that were performed.  
~~~~
2020-11-21 22:20:06:034 nl.zeesoft.zdk.http.HttpServer: Opening server socket on port: 8080 ...
2020-11-21 22:20:06:059 nl.zeesoft.zdk.http.HttpServer: Opened server socket on port: 8080
2020-11-21 22:20:06:081 nl.zeesoft.zdk.http.HttpServer$1: Request/response headers (Port: 8080);
<<<
GET / HTTP/1.1
Content-Type: text/html
Host: 127.0.0.1
Connection: keep-alive
>>>
HTTP/1.1 200 OK
Content-Length: 47
2020-11-21 22:20:06:084 nl.zeesoft.zdk.http.HttpServer$1: Request/response headers (Port: 8080);
<<<
GET /pizza.txt HTTP/1.1
Content-Type: text/html
Host: 127.0.0.1
Connection: keep-alive
>>>
HTTP/1.1 404 File not found: http/pizza.txt
Content-Length: 30
2020-11-21 22:20:06:087 nl.zeesoft.zdk.http.HttpServer$1: Request/response headers (Port: 8080);
<<<
PUT /pizza.txt HTTP/1.1
Content-Type: text/html
Host: 127.0.0.1
Connection: keep-alive
Content-Length: 13
>>>
HTTP/1.1 200 OK
2020-11-21 22:20:06:089 nl.zeesoft.zdk.http.HttpServer$1: Request/response headers (Port: 8080);
<<<
GET /pizza.txt HTTP/1.1
Content-Type: text/html
Host: 127.0.0.1
Connection: keep-alive
>>>
HTTP/1.1 200 OK
Content-Length: 13
2020-11-21 22:20:06:092 nl.zeesoft.zdk.http.HttpServer$1: Request/response headers (Port: 8080);
<<<
DELETE /pizza.txt HTTP/1.1
Content-Type: text/html
Host: 127.0.0.1
Connection: keep-alive
>>>
HTTP/1.1 200 OK
2020-11-21 22:20:06:093 nl.zeesoft.zdk.http.HttpServer: Opening server socket on port: 9090 ...
2020-11-21 22:20:06:094 nl.zeesoft.zdk.http.HttpServer: Opened server socket on port: 9090
2020-11-21 22:20:06:097 nl.zeesoft.zdk.http.HttpServer$1: Request/response headers (Port: 8080);
<<<
GET / HTTP/1.1
Host: 127.0.0.1:8080
Content-Type: text/html
Connection: keep-alive
>>>
HTTP/1.1 200 OK
Content-Length: 47
2020-11-21 22:20:06:098 nl.zeesoft.zdk.http.HttpServer$1: Request/response headers (Port: 9090);
<<<
GET / HTTP/1.1
Host: 127.0.0.1:8080
Content-Type: text/html
Connection: keep-alive
>>>
HTTP/1.1 200 OK
Content-Length: 47
2020-11-21 22:20:06:110 nl.zeesoft.zdk.http.HttpServer: Closing connections ...
2020-11-21 22:20:06:110 nl.zeesoft.zdk.http.HttpServer: Closed connections
2020-11-21 22:20:06:110 nl.zeesoft.zdk.http.HttpServer: Closing server socket on port: 9090 ...
2020-11-21 22:20:06:111 nl.zeesoft.zdk.http.HttpServer: Closed server socket on port: 9090
2020-11-21 22:20:06:121 nl.zeesoft.zdk.http.HttpServer: Closing connections ...
2020-11-21 22:20:06:121 nl.zeesoft.zdk.http.HttpServer: Closed connections
2020-11-21 22:20:06:122 nl.zeesoft.zdk.http.HttpServer: Closing server socket on port: 8080 ...
2020-11-21 22:20:06:122 nl.zeesoft.zdk.http.HttpServer: Closed server socket on port: 8080

Action log;
WRITE:http/index.html:47
READ:http/index.html:47
WRITE:http/pizza.txt:13
READ:http/pizza.txt:13
DELETE:http/pizza.txt:13
READ:http/index.html:47
~~~~

nl.zeesoft.zdk.test.grid.TestGrid
---------------------------------
This test shows how to use *Grid* instances to store and manipulate multi dimensional data structures. 
A *Grid* is a Matrix with an optional 3rd dimension. 
It was designed to represent 2 or 3 dimensional data structures where the location of the value has signifigance. 
*ColumnFunctions* can be used to manipulate XY value arrays in a functional and optionally multi threaded manner. 

**Example implementation**  
~~~~
// Create the grid
Grid grid = new Grid();
// Initialize the grid
Grid grid = new Grid(4, 4, 4, 0.5F);
// Set a value
grid.setValue(1, 2, 3, 0.5F);
// Create a column function
ColumnFunction increment1 = new ColumnFunction() {
    @Override
    public Object applyFunction(GridColumn column, int posZ, Object value) {
        return (float)value + 1F;
    }
};
// Apply the column function (specify a *CodeRunnerList* for multi threaded application)
grid.applyFunction(increment1);
// Get a value
float value = (float) grid.getValue(1, 2, 3);
~~~~

Class references;  
 * [TestGrid](https://github.com/DyzLecticus/Zeesoft/blob/master/V5.0/ZDK/src/nl/zeesoft/zdk/test/grid/TestGrid.java)
 * [Grid](https://github.com/DyzLecticus/Zeesoft/blob/master/V5.0/ZDK/src/nl/zeesoft/zdk/grid/Grid.java)
 * [ColumnFunction](https://github.com/DyzLecticus/Zeesoft/blob/master/V5.0/ZDK/src/nl/zeesoft/zdk/grid/ColumnFunction.java)
 * [CodeRunnerList](https://github.com/DyzLecticus/Zeesoft/blob/master/V5.0/ZDK/src/nl/zeesoft/zdk/thread/CodeRunnerList.java)

**Test output**  
The output of this test shows the number of columns for a 48 by 48 by 16 grid and some column value manipulations.  
~~~~
Columns: 2304
Value at (10, 15, 4): 0.0
New value at (10, 15, 4): 0.5
New value at (10, 15, 4): 1.5
New value at (10, 15, 4): 2.5
New value at (10, 15, 4): 3.5
~~~~

nl.zeesoft.zdk.test.neural.TestSDR
----------------------------------
This test shows how to create and compare sparse distributed representations using *SDR* instances.
It also shows how to use a *BasicScalarEncoder* to encode integer (or float) values into *SDR* instances.
This library also provides other interesting encoders like the *BasicFeatureEncoder* and the *BasicFeatureArrayEncoder*.

**Example implementation**  
~~~~
// Create the SDR
SDR sdrA = new SDR(10, 10);
// Turn on the first and last bits
sdrA.setBit(0,true);
sdrA.setBit(99,true);
// Create another SDR
SDR sdrB = new SDR(10, 10);
// Turn on the first and middle bits
sdrB.setBit(0,true);
sdrB.setBit(50,true);
// Get the number of overlapping bits
int overlap = sdrA.getOverlap(sdrB);

// Create the scalar encoder
BasicScalarEncoder encoder = new BasicScalarEncoder();
SDR sdr = encoder.getEncodedValue(0)
~~~~

Class references;  
 * [TestSDR](https://github.com/DyzLecticus/Zeesoft/blob/master/V5.0/ZDK/src/nl/zeesoft/zdk/test/neural/TestSDR.java)
 * [SDR](https://github.com/DyzLecticus/Zeesoft/blob/master/V5.0/ZDK/src/nl/zeesoft/zdk/neural/SDR.java)
 * [BasicScalarEncoder](https://github.com/DyzLecticus/Zeesoft/blob/master/V5.0/ZDK/src/nl/zeesoft/zdk/neural/BasicScalarEncoder.java)
 * [BasicFeatureEncoder](https://github.com/DyzLecticus/Zeesoft/blob/master/V5.0/ZDK/src/nl/zeesoft/zdk/neural/BasicFeatureEncoder.java)
 * [BasicFeatureArrayEncoder](https://github.com/DyzLecticus/Zeesoft/blob/master/V5.0/ZDK/src/nl/zeesoft/zdk/neural/BasicFeatureArrayEncoder.java)

**Test output**  
The output of this test shows an example SDR in its compressed and visualized form.
~~~~
SDR: 4;4;0,7,8,15

Visualized;
1000
0001
1000
0001
~~~~

nl.zeesoft.zdk.test.neural.TestCellGrid
---------------------------------------
This test shows how to use a *CellGrid* to model a multi dimensional set of neural cells. 
The *Cell* instances in the grid are designed to support the Numenta HTM cell model. 
This allows the Numenta HTM algorithms to be implemented on these grids. 

**Example implementation**  
~~~~
// Create the grid
CellGrid grid = new CellGrid();
// Initialize the grid
grid.initialize(8, 2);
// Get a cell
Cell cell = (Cell)grid.getValue(1, 0, 0);
// Create a segment (proximal/distal/apical)
DistalSegment segment = new DistalSegment();
// Attach the segment to the cell
cell.distalSegments.add(segment);
// Create a synapse
Synapse syn = new Synapse();
syn.connectTo.x = 1;
syn.connectTo.y = 1;
syn.connectTo.z = 1;
syn.permanence = 0.1F;
// Attach the synapse to the segment
segment.synapses.add(syn);
~~~~

Class references;  
 * [TestCellGrid](https://github.com/DyzLecticus/Zeesoft/blob/master/V5.0/ZDK/src/nl/zeesoft/zdk/test/neural/TestCellGrid.java)
 * [CellGrid](https://github.com/DyzLecticus/Zeesoft/blob/master/V5.0/ZDK/src/nl/zeesoft/zdk/neural/model/CellGrid.java)
 * [Cell](https://github.com/DyzLecticus/Zeesoft/blob/master/V5.0/ZDK/src/nl/zeesoft/zdk/neural/model/Cell.java)
 * [ProximalSegment](https://github.com/DyzLecticus/Zeesoft/blob/master/V5.0/ZDK/src/nl/zeesoft/zdk/neural/model/ProximalSegment.java)
 * [DistalSegment](https://github.com/DyzLecticus/Zeesoft/blob/master/V5.0/ZDK/src/nl/zeesoft/zdk/neural/model/DistalSegment.java)
 * [ApicalSegment](https://github.com/DyzLecticus/Zeesoft/blob/master/V5.0/ZDK/src/nl/zeesoft/zdk/neural/model/ApicalSegment.java)
 * [Synapse](https://github.com/DyzLecticus/Zeesoft/blob/master/V5.0/ZDK/src/nl/zeesoft/zdk/neural/model/Synapse.java)

**Test output**  
The output of this test shows the *Str* form of an example 8 by 2 by 1 cell grid.  
~~~~
8*2*1
1,0,0=P@0,1,0;0.100#0,1,0;0.100!P@1,1,0;0.100#1,1,0;0.100!D@2,1,0;0.100#2,1,0;0.100!D@3,1,0;0.100#3,1,0;0.100!A@4,1,0;0.100#4,1,0;0.100!A@5,1,0;0.100#5,1,0;0.100
2,1,0=P@1,1,0;0.100#1,1,0;0.100!P@2,1,0;0.100#2,1,0;0.100!D@3,1,0;0.100#3,1,0;0.100!D@4,1,0;0.100#4,1,0;0.100!A@5,1,0;0.100#5,1,0;0.100!A@6,1,0;0.100#6,1,0;0.100
~~~~

nl.zeesoft.zdk.test.neural.TestScalarEncoder
--------------------------------------------
This test shows how to use a *ScalarEncoder* to generate scalar SDRs.
A *ScalarEncoderConfig* can be used to configure the *ScalarEncoder* before initialization.

Configurable properties;  
 * *sizeX*, *sizeY*; Encoded output SDR dimensions.  
 * *onBits*; The number of on bits in the encoded output.  
 * *minValue*, *maxValue*; The value range this encoder will encode.  
 * *resolution*; The value resolution this encoder will encode.  
 * *periodic*; Indicates the encoder should wrap the on bits over the value range.  

All *SDRProcessor* instances implement the same pattern/life cycle to ensure the processing is done using multiple threads. 
Bare *SDRProcessor* instances are not thread safe beyond the specified life cycle. 
Use the *ProcessorFactory* for thread safe *Processor* instantiation and interaction. 

**Example implementation**  
~~~~
// Create the encoder
ScalarEncoder en = new ScalarEncoder();
// Initialize the encoder
en.initialize();
// Create and build the processor chain
CodeRunnerChain processorChain = new CodeRunnerChain();
en.buildProcessorChain(processorChain);
// Set the input
en.setValue(20);
// Run the processor chain
if (Waiter.startAndWaitFor(processorChain, 1000)) {
    // Get the output
    SDR output = en.getOutput();
}
~~~~

Class references;  
 * [TestScalarEncoder](https://github.com/DyzLecticus/Zeesoft/blob/master/V5.0/ZDK/src/nl/zeesoft/zdk/test/neural/TestScalarEncoder.java)
 * [ScalarEncoder](https://github.com/DyzLecticus/Zeesoft/blob/master/V5.0/ZDK/src/nl/zeesoft/zdk/neural/processors/ScalarEncoder.java)
 * [ScalarEncoderConfig](https://github.com/DyzLecticus/Zeesoft/blob/master/V5.0/ZDK/src/nl/zeesoft/zdk/neural/processors/ScalarEncoderConfig.java)

**Test output**  
The output of this test shows a scalar encoder beeing used to encode 2 example values into SDRs.  
~~~~
2020-11-21 22:20:06:305 nl.zeesoft.zdk.neural.processors.ScalarEncoder: Initializing ScalarEncoder ...
2020-11-21 22:20:06:306 nl.zeesoft.zdk.neural.processors.ScalarEncoder: Initialized
Encoded SDR for value 20: 16;16;24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39
Encoded SDR for value 21: 16;16;25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40
~~~~

nl.zeesoft.zdk.test.neural.TestSpatialPooler
--------------------------------------------
This test shows how to use a *SpatialPooler* to create consistent sparse representations of less sparse and/or smaller SDRs.
A *SpatialPoolerConfig* can be used to configure the *SpatialPooler* before initialization.
Please note that this spatial pooler implementation does not support local inhibition.  
  
Configurable properties;  
 * *inputSizeX*, *inputSizeY*; Input SDR dimensions.  
 * *outputSizeX*, *outputSizeY*; Output SDR dimensions (mini column dimensions).  
 * *outputOnBits*; Maximum number of on bits in the output.  
 * *potentialConnections*, *potentialRadius*; Number and optional radius of potential connections relative to the input space.  
 * *permanenceThreshold*, *permanenceIncrement*, *permanenceDecrement*; Potential synapse adaptation control.  
 * *activationHistorySize*; Historic column activation buffer size (used to calculate boost factors).  
 * *boostFactorPeriod*; Boost factor recalculation period.  
 * *boostStrength*; Boost strength.  


All *SDRProcessor* instances implement the same pattern/life cycle to ensure the processing is done using multiple threads. 
Bare *SDRProcessor* instances are not thread safe beyond the specified life cycle. 
Use the *ProcessorFactory* for thread safe *Processor* instantiation and interaction. 

**Example implementation**  
~~~~
// Create the spatial pooler
SpatialPooler sp = new SpatialPooler();
// Initialize the spatial pooler
sp.initialize();
// Initialize the spatial pooler connections
sp.resetConnections();
// Create and build the processor chain
CodeRunnerChain processorChain = new CodeRunnerChain();
sp.buildProcessorChain(processorChain);
// Set the input (dimensions should match configured input dimensions)
sp.setInput(new SDR());
// Run the processor chain
if (Waiter.startAndWaitFor(processorChain, 1000)) {
    // Get the output
    SDR output = sp.getOutput();
}
// Get a Str containing the data
Str data = sp.toStr();
~~~~

Class references;  
 * [TestSpatialPooler](https://github.com/DyzLecticus/Zeesoft/blob/master/V5.0/ZDK/src/nl/zeesoft/zdk/test/neural/TestSpatialPooler.java)
 * [SpatialPooler](https://github.com/DyzLecticus/Zeesoft/blob/master/V5.0/ZDK/src/nl/zeesoft/zdk/neural/processors/SpatialPooler.java)
 * [SpatialPoolerConfig](https://github.com/DyzLecticus/Zeesoft/blob/master/V5.0/ZDK/src/nl/zeesoft/zdk/neural/processors/SpatialPoolerConfig.java)

**Test output**  
The output of this test shows an example spatial pooler and the input/output for similar inputs among a certain input variation.  
It also shows the average overlap for similar inputs and the average overall overlap.  
~~~~
2020-11-21 22:20:06:393 nl.zeesoft.zdk.neural.processors.SpatialPooler: Initializing SpatialPooler ...
2020-11-21 22:20:06:488 nl.zeesoft.zdk.neural.processors.SpatialPooler: Initialized
2020-11-21 22:20:06:488 nl.zeesoft.zdk.neural.processors.SpatialPooler: Resetting SpatialPooler connections ...
2020-11-21 22:20:06:627 nl.zeesoft.zdk.neural.processors.SpatialPooler: Reset connections

2020-11-21 22:20:06:627 nl.zeesoft.zdk.test.neural.TestSpatialPooler: Initializing spatial pooler (asynchronous) ...
2020-11-21 22:20:07:051 nl.zeesoft.zdk.test.neural.TestSpatialPooler: Initialized spatial pooler (asynchronous)

Input SDR: 16;16;0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15
Output SDR: 48;48;4,6,207,273,322,328,361,385,393,447,450,476,510,521,564,565,626,655,659,804,810,867,872,888,938,1022,1032,1082,1171,1211,1225,1256,1261,1279,1313,1333,1417,1436,1551,1557,1653,1845,1856,1861,1934,1983
Input SDR: 16;16;0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15
Output SDR: 48;48;4,6,24,27,44,207,273,322,361,385,393,447,450,476,521,564,565,626,653,655,804,810,867,872,888,1022,1032,1082,1098,1171,1211,1225,1256,1261,1279,1417,1434,1436,1551,1557,1653,1770,1845,1856,1934,1983
Input SDR: 16;16;0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15
Output SDR: 48;48;4,6,24,44,207,273,322,361,385,393,447,450,476,510,521,564,565,626,653,655,804,810,867,872,888,938,1022,1032,1082,1098,1171,1211,1225,1256,1261,1279,1417,1436,1551,1557,1653,1845,1856,1861,1934,1983
Input SDR: 16;16;0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15
Output SDR: 48;48;4,6,24,27,44,207,273,322,361,385,393,447,450,476,510,521,564,565,626,653,655,804,810,867,872,888,938,1022,1032,1082,1098,1171,1211,1225,1256,1261,1279,1417,1436,1551,1557,1653,1845,1856,1861,1983
Input SDR: 16;16;0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15
Output SDR: 48;48;4,6,24,27,44,273,322,361,385,393,447,450,476,510,521,564,565,626,653,804,810,867,872,888,938,1022,1032,1082,1098,1171,1211,1225,1256,1261,1279,1313,1417,1436,1551,1557,1653,1845,1856,1861,1934,1983

Average overlap for similar inputs: 46.0, overall: 2.0
~~~~

nl.zeesoft.zdk.test.neural.TestTemporalMemory
---------------------------------------------
This test shows how to use a *TemporalMemory* to learn SDR sequences.
A *TemporalMemoryConfig* can be used to configure the *TemporalMemory* before initialization.
Please note that this temporal memory implementation does not generate any initial segments/synapses.  
  
Configurable properties;  
 * *sizeX*, *sizeY*, *sizeZ*; Cell grid dimensions (sizeX, sizeY specify input SDR dimensions).  
 * *maxSegmentsPerCell*; Maximum number of segments per cell.  
 * *maxSynapsesPerSegment*; Maximum number of synapses per segment.  
 * *initialPermanence*, *permanenceThreshold*, *permanenceIncrement*, *permanenceDecrement*; Distal and apical synapse adaptation control.  
 * *segmentCreationSubsample*; Limits the creation of new segments to the specified percentage.  
 * *distalSegmentDecrement*, *apicalSegmentDecrement*; Optional segment decrement for distal/apical segments.  
 * *distalPotentialRadius*; Optional potential radius for distal segments.  
 * *apicalPotentialRadius*; Optional potential radius for apical segments (assumes apical input XY dimensions match model XY dimensions).  
 * *activationThreshold*; Number of active synapses on a segment for it to be considered active.  
 * *matchingThreshold*; Number of potential synapses on a segment for it to be considered matching.  
 * *maxNewSynapseCount*; Maximum number of synapses to create when creating/adapting segments.  


All *SDRProcessor* instances implement the same pattern/life cycle to ensure the processing is done using multiple threads. 
Bare *SDRProcessor* instances are not thread safe beyond the specified life cycle. 
Use the *ProcessorFactory* for thread safe *Processor* instantiation and interaction. 

**Example implementation**  
~~~~
// Create the temporal memory
TemporalMemory tm = new TemporalMemory();
// Initialize the temporal memory
tm.initialize();
// Initialize the temporal memory connections (optional)
tm.resetConnections();
// Create and build the processor chain
CodeRunnerChain processorChain = new CodeRunnerChain();
tm.buildProcessorChain(processorChain);
// Set the input (dimensions should match configured X/Y dimensions)
tm.setInput(new SDR());
// Run the processor chain
if (Waiter.startAndWaitFor(processorChain, 1000)) {
    // Get the output
    SDR output = tm.getOutput();
}
// Get a Str containing the data
Str data = tm.toStr();
~~~~

Class references;  
 * [TestTemporalMemory](https://github.com/DyzLecticus/Zeesoft/blob/master/V5.0/ZDK/src/nl/zeesoft/zdk/test/neural/TestTemporalMemory.java)
 * [TemporalMemory](https://github.com/DyzLecticus/Zeesoft/blob/master/V5.0/ZDK/src/nl/zeesoft/zdk/neural/processors/TemporalMemory.java)
 * [TemporalMemoryConfig](https://github.com/DyzLecticus/Zeesoft/blob/master/V5.0/ZDK/src/nl/zeesoft/zdk/neural/processors/TemporalMemoryConfig.java)

**Test output**  
The output of this test shows an example temporal memory learning a sequence of SDRs.  
~~~~
2020-11-21 22:20:16:915 nl.zeesoft.zdk.neural.processors.TemporalMemory: Initializing TemporalMemory ...
2020-11-21 22:20:16:918 nl.zeesoft.zdk.neural.processors.TemporalMemory: Initialized
2020-11-21 22:20:16:918 nl.zeesoft.zdk.neural.processors.TemporalMemory: Resetting TemporalMemory connections ...
2020-11-21 22:20:16:928 nl.zeesoft.zdk.neural.processors.TemporalMemory: Reset connections
2020-11-21 22:20:16:960 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 1 > bursting: 46, active: 736, winners: 46, predictive: 0
2020-11-21 22:20:16:983 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 2 > bursting: 46, active: 736, winners: 46, predictive: 0
2020-11-21 22:20:17:010 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 3 > bursting: 46, active: 736, winners: 46, predictive: 0
2020-11-21 22:20:17:030 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 4 > bursting: 46, active: 736, winners: 46, predictive: 0
2020-11-21 22:20:17:054 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 5 > bursting: 46, active: 736, winners: 46, predictive: 0
2020-11-21 22:20:17:068 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 6 > bursting: 46, active: 736, winners: 46, predictive: 0
2020-11-21 22:20:17:083 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 7 > bursting: 46, active: 736, winners: 46, predictive: 0
2020-11-21 22:20:17:107 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 8 > bursting: 46, active: 736, winners: 46, predictive: 0
2020-11-21 22:20:17:116 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 9 > bursting: 46, active: 736, winners: 46, predictive: 0
2020-11-21 22:20:17:130 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 10 > bursting: 46, active: 736, winners: 46, predictive: 0
2020-11-21 22:20:17:139 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 11 > bursting: 46, active: 736, winners: 46, predictive: 0
2020-11-21 22:20:17:148 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 12 > bursting: 46, active: 736, winners: 46, predictive: 0
2020-11-21 22:20:17:157 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 13 > bursting: 46, active: 736, winners: 46, predictive: 0
2020-11-21 22:20:17:174 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 14 > bursting: 46, active: 736, winners: 46, predictive: 0
2020-11-21 22:20:17:182 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 15 > bursting: 46, active: 736, winners: 46, predictive: 0
2020-11-21 22:20:17:190 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 16 > bursting: 46, active: 736, winners: 46, predictive: 0
2020-11-21 22:20:17:207 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 17 > bursting: 46, active: 736, winners: 46, predictive: 42
2020-11-21 22:20:17:223 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 18 > bursting: 4, active: 106, winners: 46, predictive: 42
2020-11-21 22:20:17:238 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 19 > bursting: 4, active: 106, winners: 46, predictive: 41
2020-11-21 22:20:17:250 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 20 > bursting: 5, active: 121, winners: 46, predictive: 43
2020-11-21 22:20:17:261 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 21 > bursting: 3, active: 91, winners: 46, predictive: 4
2020-11-21 22:20:17:293 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 22 > bursting: 42, active: 676, winners: 46, predictive: 45
2020-11-21 22:20:17:303 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 23 > bursting: 1, active: 61, winners: 46, predictive: 46
2020-11-21 22:20:17:313 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 24 > bursting: 0, active: 46, winners: 46, predictive: 46
2020-11-21 22:20:17:323 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 25 > bursting: 0, active: 46, winners: 46, predictive: 4
2020-11-21 22:20:17:337 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 26 > bursting: 42, active: 676, winners: 46, predictive: 46
2020-11-21 22:20:17:346 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 27 > bursting: 0, active: 46, winners: 46, predictive: 46
2020-11-21 22:20:17:359 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 28 > bursting: 0, active: 46, winners: 46, predictive: 46
2020-11-21 22:20:17:369 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 29 > bursting: 0, active: 46, winners: 46, predictive: 4
2020-11-21 22:20:17:405 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 30 > bursting: 42, active: 676, winners: 46, predictive: 46
2020-11-21 22:20:17:416 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 31 > bursting: 0, active: 46, winners: 46, predictive: 46
2020-11-21 22:20:17:425 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 32 > bursting: 0, active: 46, winners: 46, predictive: 46
2020-11-21 22:20:17:433 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 33 > bursting: 0, active: 46, winners: 46, predictive: 4
2020-11-21 22:20:17:444 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 34 > bursting: 42, active: 676, winners: 46, predictive: 46
2020-11-21 22:20:17:454 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 35 > bursting: 0, active: 46, winners: 46, predictive: 46
2020-11-21 22:20:17:463 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 36 > bursting: 0, active: 46, winners: 46, predictive: 46
2020-11-21 22:20:17:472 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 37 > bursting: 0, active: 46, winners: 46, predictive: 41
2020-11-21 22:20:17:482 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 38 > bursting: 5, active: 121, winners: 46, predictive: 0
2020-11-21 22:20:17:510 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 39 > bursting: 46, active: 736, winners: 46, predictive: 46
2020-11-21 22:20:17:519 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 40 > bursting: 0, active: 46, winners: 46, predictive: 46
2020-11-21 22:20:17:528 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 41 > bursting: 0, active: 46, winners: 46, predictive: 46
2020-11-21 22:20:17:542 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 42 > bursting: 0, active: 46, winners: 46, predictive: 0
2020-11-21 22:20:17:559 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 43 > bursting: 46, active: 736, winners: 46, predictive: 46
2020-11-21 22:20:17:581 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 44 > bursting: 0, active: 46, winners: 46, predictive: 46
2020-11-21 22:20:17:590 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 45 > bursting: 0, active: 46, winners: 46, predictive: 46
2020-11-21 22:20:17:602 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 46 > bursting: 0, active: 46, winners: 46, predictive: 0
2020-11-21 22:20:17:619 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 47 > bursting: 46, active: 736, winners: 46, predictive: 46
2020-11-21 22:20:17:628 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 48 > bursting: 0, active: 46, winners: 46, predictive: 46
2020-11-21 22:20:17:637 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 49 > bursting: 0, active: 46, winners: 46, predictive: 46
2020-11-21 22:20:17:659 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 50 > bursting: 0, active: 46, winners: 46, predictive: 3
2020-11-21 22:20:17:676 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 51 > bursting: 43, active: 691, winners: 46, predictive: 46
2020-11-21 22:20:17:683 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 52 > bursting: 0, active: 46, winners: 46, predictive: 46
2020-11-21 22:20:17:690 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 53 > bursting: 0, active: 46, winners: 46, predictive: 46
2020-11-21 22:20:17:698 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 54 > bursting: 0, active: 46, winners: 46, predictive: 42
2020-11-21 22:20:17:708 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 55 > bursting: 4, active: 106, winners: 46, predictive: 46
2020-11-21 22:20:17:727 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 56 > bursting: 0, active: 46, winners: 46, predictive: 46
2020-11-21 22:20:17:736 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 57 > bursting: 0, active: 46, winners: 46, predictive: 46
2020-11-21 22:20:17:745 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 58 > bursting: 0, active: 46, winners: 46, predictive: 46
2020-11-21 22:20:17:754 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 59 > bursting: 0, active: 46, winners: 46, predictive: 43
2020-11-21 22:20:17:763 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 60 > bursting: 3, active: 91, winners: 46, predictive: 46
2020-11-21 22:20:17:789 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 61 > bursting: 0, active: 46, winners: 46, predictive: 46
2020-11-21 22:20:17:799 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 62 > bursting: 0, active: 46, winners: 46, predictive: 46
2020-11-21 22:20:17:808 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 63 > bursting: 0, active: 46, winners: 46, predictive: 43
2020-11-21 22:20:17:819 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 64 > bursting: 3, active: 91, winners: 46, predictive: 46
2020-11-21 22:20:17:836 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 65 > bursting: 0, active: 46, winners: 46, predictive: 46
2020-11-21 22:20:17:846 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 66 > bursting: 0, active: 46, winners: 46, predictive: 46
2020-11-21 22:20:17:865 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 67 > bursting: 0, active: 46, winners: 46, predictive: 43
2020-11-21 22:20:17:871 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 68 > bursting: 3, active: 91, winners: 46, predictive: 46
2020-11-21 22:20:17:876 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 69 > bursting: 0, active: 46, winners: 46, predictive: 46
2020-11-21 22:20:17:882 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 70 > bursting: 0, active: 46, winners: 46, predictive: 46
2020-11-21 22:20:17:887 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 71 > bursting: 0, active: 46, winners: 46, predictive: 46
2020-11-21 22:20:17:901 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 72 > bursting: 0, active: 46, winners: 46, predictive: 46
2020-11-21 22:20:17:906 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 73 > bursting: 0, active: 46, winners: 46, predictive: 46
2020-11-21 22:20:17:911 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 74 > bursting: 0, active: 46, winners: 46, predictive: 46
2020-11-21 22:20:18:119 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 100 > bursting: 0, active: 46, winners: 46, predictive: 46
2020-11-21 22:20:18:399 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 150 > bursting: 0, active: 46, winners: 46, predictive: 46
2020-11-21 22:20:18:663 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 200 > bursting: 0, active: 46, winners: 46, predictive: 46
2020-11-21 22:20:19:038 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 250 > bursting: 0, active: 46, winners: 46, predictive: 46
2020-11-21 22:20:19:317 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 300 > bursting: 0, active: 46, winners: 46, predictive: 46
2020-11-21 22:20:19:593 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 350 > bursting: 0, active: 46, winners: 46, predictive: 46
2020-11-21 22:20:19:877 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 400 > bursting: 0, active: 46, winners: 46, predictive: 46
2020-11-21 22:20:20:149 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 450 > bursting: 0, active: 46, winners: 46, predictive: 46
2020-11-21 22:20:20:418 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 500 > bursting: 0, active: 46, winners: 46, predictive: 46
~~~~

nl.zeesoft.zdk.test.neural.TestClassifier
-----------------------------------------
This test shows how to use a *Classifier* to classify and/or predict future input values based on SDR sequences.
A *ClassifierConfig* can be used to configure the *Classifier* before initialization.
A *Classifier* will combine all input SDRs that match the specified dimension into a single footprint to associate with a certain input value.
The input value can be provided by using a *KeyValueSDR* with a key/value pair with a configurable key.
The classifications and/or predictions will be attached to the output *KeyValueSDR* using *Classification* objects.
Please note that this classifier implementation will 'forget' old classifications by default (see maxCount).  
  
Configurable properties;  
 * *sizeX*, *sizeY*; Merged output SDR dimensions (input SDRs that do not match are ignored for merge).  
 * *maxOnBits*; Optional maximum number of on bits in the merged input (uses sub sampling).  
 * *valueKey*; Value key to look for in the input KeyValueSDRs.  
 * *predictSteps*; Array of steps to classify/predict;  
   Step 0 will classify the current input.  
   Steps greater than 0 will predict future input.  
   By default the next step will be predicted (predictSteps[0] equals 1).  
 * *maxCount*; Maximum count of a step bit value count (a minimum of 8 is enforced);  
   If a step bit reaches this maximum, all bit value counts of the step are divided by two.  
   When when a value has only a single count it will be removed.  
   When a step bit has no value counts it will be removed.  
 * *logPredictionAccuracy*; Indicates average next step prediction accuracy and trend should be logged (requires predictSteps to contain step 1).  
 * *accuracyHistorySize*; The size of the historical accuracy buffer used to calculate average next step prediction accuracy and trend.  
 * *accuracyTrendSize*; The subsample of the historical accuracy used to calculate the trend.  


All *SDRProcessor* instances implement the same pattern/life cycle to ensure the processing is done using multiple threads. 
Bare *SDRProcessor* instances are not thread safe beyond the specified life cycle. 
Use the *ProcessorFactory* for thread safe *Processor* instantiation and interaction. 

**Example implementation**  
~~~~
// Create the classifier
Classifier cl = new Classifier();
// Initialize the classifier
cl.initialize();
// Create and build the processor chain
CodeRunnerChain processorChain = new CodeRunnerChain();
cl.buildProcessorChain(processorChain);
// Set the input (dimensions should match configured X/Y dimensions)
cl.setInput(new SDR());
// Run the processor chain
if (Waiter.startAndWaitFor(processorChain, 1000)) {
    // Get the output
    SDR output = cl.getOutput();
}
// Get a Str containing the data
Str data = cl.toStr();
~~~~

Class references;  
 * [TestClassifier](https://github.com/DyzLecticus/Zeesoft/blob/master/V5.0/ZDK/src/nl/zeesoft/zdk/test/neural/TestClassifier.java)
 * [Classifier](https://github.com/DyzLecticus/Zeesoft/blob/master/V5.0/ZDK/src/nl/zeesoft/zdk/neural/processors/Classifier.java)
 * [ClassifierConfig](https://github.com/DyzLecticus/Zeesoft/blob/master/V5.0/ZDK/src/nl/zeesoft/zdk/neural/processors/ClassifierConfig.java)
 * [KeyValueSDR](https://github.com/DyzLecticus/Zeesoft/blob/master/V5.0/ZDK/src/nl/zeesoft/zdk/neural/KeyValueSDR.java)
 * [Classification](https://github.com/DyzLecticus/Zeesoft/blob/master/V5.0/ZDK/src/nl/zeesoft/zdk/neural/processors/Classification.java)

**Test output**  
The output of this test shows an example classifier input and output SDR.  
In this case the value is passed to the classifier using a separate key value SDR which is not shown here.  
~~~~
2020-11-21 22:20:20:547 nl.zeesoft.zdk.neural.processors.Classifier: Initializing Classifier ...
2020-11-21 22:20:20:547 nl.zeesoft.zdk.neural.processors.Classifier: Initialized

2020-11-21 22:20:20:585 nl.zeesoft.zdk.neural.processors.ClassifierStep: Dividing step 1 classifier value counts by two ...
2020-11-21 22:20:20:617 nl.zeesoft.zdk.neural.processors.ClassifierStep: Dividing step 1 classifier value counts by two ...
2020-11-21 22:20:20:656 nl.zeesoft.zdk.neural.processors.ClassifierStep: Dividing step 1 classifier value counts by two ...

Input SDR: 10;10;84,85,86,87,88,89,90,91,92,93,94,95,96,97,98,99
Output SDR: SDR##1;1;@accuracy#java.lang.Float#1.0@accuracyTrend#java.lang.Float#1.0@classification:1#nl.zeesoft.zdk.neural.processors.Classification#1;value;java.lang.Integer;0,160
~~~~

nl.zeesoft.zdk.test.neural.TestMerger
-------------------------------------
This test shows how to use a *Merger* to merge, concatenate, subsample and/or distort SDRs.
A *MergerConfig* can be used to configure the *Merger* before initialization.

Configurable properties;  
 * *sizeX*, *sizeY*; Merged output SDR dimensions (input SDR dimensions are not restricted).  
 * *concatenate*; Indicates the input SDRs should be concatenated.  
 * *maxOnBits*; Optional maximum number of on bits in the merged output (uses sub sampling).  
 * *distortion*; Optional on bit distortion to the output SDR.  

All *SDRProcessor* instances implement the same pattern/life cycle to ensure the processing is done using multiple threads. 
Bare *SDRProcessor* instances are not thread safe beyond the specified life cycle. 
Use the *ProcessorFactory* for thread safe *Processor* instantiation and interaction. 

**Example implementation**  
~~~~
// Create the merger
Merger mr = new Merger();
// Initialize the merger
mr.initialize();
// Create and build the processor chain
CodeRunnerChain processorChain = new CodeRunnerChain();
mr.buildProcessorChain(processorChain);
// Set the input
mr.setInput(new SDR(), new SDR());
// Run the processor chain
if (Waiter.startAndWaitFor(processorChain, 1000)) {
    // Get the output
    SDR output = mr.getOutput();
}
~~~~

Class references;  
 * [TestMerger](https://github.com/DyzLecticus/Zeesoft/blob/master/V5.0/ZDK/src/nl/zeesoft/zdk/test/neural/TestMerger.java)
 * [Merger](https://github.com/DyzLecticus/Zeesoft/blob/master/V5.0/ZDK/src/nl/zeesoft/zdk/neural/processors/Merger.java)
 * [MergerConfig](https://github.com/DyzLecticus/Zeesoft/blob/master/V5.0/ZDK/src/nl/zeesoft/zdk/neural/processors/MergerConfig.java)

**Test output**  
The output of this test shows a *Merger* and an example of a merged and distorted output SDR.  
~~~~
2020-11-21 22:20:20:708 nl.zeesoft.zdk.neural.processors.Merger: Initializing Merger ...
2020-11-21 22:20:20:710 nl.zeesoft.zdk.neural.processors.Merger: Initialized

Merged and distorted;
10111100
00000001
00001000
11000000
00000000
00000001
00000000
00000000
~~~~

nl.zeesoft.zdk.test.neural.TestProcessorFactory
-----------------------------------------------
This test shows how to use a *ProcessorFactory* to create a basic chain of default *Processor* instances that can make predictions.
*Processor* instances are thread safe wrappers around *SDRProcessor* instances.
*ProcessorIO* instances are used to specify *Processor* input/output and more.

**Example implementation**  
~~~~
// Create the processors
Processor sp = ProcessorFactory.getNewProcessor("SP", new SpatialPoolerConfig(), true);
Processor tm = ProcessorFactory.getNewProcessor("TM", new TemporalMemoryConfig());
Processor cl = ProcessorFactory.getNewProcessor("CL", new ClassifierConfig());
// Use the spatial pooler
ProcessorIO io1 = new ProcessorIO();
io1.inputs.add(new SDR());
sp.processIO(io1);
// Use the temporal memory
ProcessorIO io2 = new ProcessorIO();
io2.inputs.add(io1.outputs.get(SpatialPooler.ACTIVE_COLUMNS_OUTPUT);
tm.processIO(io2);
// Use the classifier
KeyValueSDR kvSdr = new KeyValueSDR(io2.outputs.get(TemporalMemory.ACTIVE_CELLS_OUTPUT));
kvSdr.put(Classifier.DEFAULT_VALUE_KEY, i % 4);
ProcessorIO io3 = new ProcessorIO();
io3.inputs.add(kvSdr);
cl.processIO(io3);
// Get the classification
KeyValueSDR kvSdr = (KeyValueSDR) outputList.get(outputList.size() - 1);
Classification cls = (Classification) kvSdr.get(Classifier.CLASSIFICATION_VALUE_KEY + ":1");
// Save the processor data to files
sp.save("data/sp.txt");
tm.save("data/tm.txt");
cl.save("data/cl.txt");
~~~~

Class references;  
 * [TestProcessorFactory](https://github.com/DyzLecticus/Zeesoft/blob/master/V5.0/ZDK/src/nl/zeesoft/zdk/test/neural/TestProcessorFactory.java)
 * [ProcessorFactory](https://github.com/DyzLecticus/Zeesoft/blob/master/V5.0/ZDK/src/nl/zeesoft/zdk/neural/processors/ProcessorFactory.java)
 * [Processor](https://github.com/DyzLecticus/Zeesoft/blob/master/V5.0/ZDK/src/nl/zeesoft/zdk/neural/processors/Processor.java)
 * [ProcessorIO](https://github.com/DyzLecticus/Zeesoft/blob/master/V5.0/ZDK/src/nl/zeesoft/zdk/neural/processors/ProcessorIO.java)

**Test output**  
The output of this test shows an example SDR processing chain and a subsample of some of its temporal memory outputs.  
~~~~
2020-11-21 22:20:20:806 nl.zeesoft.zdk.neural.processors.SpatialPooler: Initializing SpatialPooler ...
2020-11-21 22:20:20:851 nl.zeesoft.zdk.neural.processors.SpatialPooler: Initialized
2020-11-21 22:20:20:851 nl.zeesoft.zdk.neural.processors.SpatialPooler: Resetting SpatialPooler connections ...
2020-11-21 22:20:20:914 nl.zeesoft.zdk.neural.processors.SpatialPooler: Reset connections
2020-11-21 22:20:20:915 nl.zeesoft.zdk.neural.processors.TemporalMemory: Initializing TemporalMemory ...
2020-11-21 22:20:20:917 nl.zeesoft.zdk.neural.processors.TemporalMemory: Initialized
2020-11-21 22:20:20:917 nl.zeesoft.zdk.neural.processors.Classifier: Initializing Classifier ...
2020-11-21 22:20:20:918 nl.zeesoft.zdk.neural.processors.Classifier: Initialized

Processing ...
2020-11-21 22:20:23:217 nl.zeesoft.zdk.test.neural.TestProcessorFactory: 768;48;201,752,753,754,755,756,757,758,759,760,761,762,763,764,765,766,767,880,881,882,883,884,885,886,887,888,889,890,891,892,893,894,895,1088,1089,1090,1091,1092,1093,1094,1095,1096,1097,1098,1099,1100,1101,1102,1103,1664,1665,1666,1667,1668,1669,1670,1671,1672,1673,1674,1675,1676,1677,1678,1679,1744,1745,1746,1747,1748,1749,1750,1751,1752,1753,1754,1755,1756,1757,1758,1759,3489,4896,4897,4898,4899,4900,4901,4902,4903,4904,4905,4906,4907,4908,4909,4910,4911,5344,5345,5346,5347,5348,5349,5350,5351,5352,5353,5354,5355,5356,5357,5358,5359,5573,5744,5745,5746,5747,5748,5749,5750,5751,5752,5753,5754,5755,5756,5757,5758,5759,7680,7681,7682,7683,7684,7685,7686,7687,7688,7689,7690,7691,7692,7693,7694,7695,8176,8177,8178,8179,8180,8181,8182,8183,8184,8185,8186,8187,8188,8189,8190,8191,8960,8961,8962,8963,8964,8965,8966,8967,8968,8969,8970,8971,8972,8973,8974,8975,9360,9361,9362,9363,9364,9365,9366,9367,9368,9369,9370,9371,9372,9373,9374,9375,9648,9649,9650,9651,9652,9653,9654,9655,9656,9657,9658,9659,9660,9661,9662,9663,10304,10305,10306,10307,10308,10309,10310,10311,10312,10313,10314,10315,10316,10317,10318,10319,10448,10449,10450,10451,10452,10453,10454,10455,10456,10457,10458,10459,10460,10461,10462,10463,10614,10624,10625,10626,10627,10628,10629,10630,10631,10632,10633,10634,10635,10636,10637,10638,10639,10648,11760,11761,11762,11763,11764,11765,11766,11767,11768,11769,11770,11771,11772,11773,11774,11775,12400,12401,12402,12403,12404,12405,12406,12407,12408,12409,12410,12411,12412,12413,12414,12415,13774,14175,14512,14513,14514,14515,14516,14517,14518,14519,14520,14521,14522,14523,14524,14525,14526,14527,15088,15089,15090,15091,15092,15093,15094,15095,15096,15097,15098,15099,15100,15101,15102,15103,15296,15297,15298,15299,15300,15301,15302,15303,15304,15305,15306,15307,15308,15309,15310,15311,15696,15697,15698,15699,15700,15701,15702,15703,15704,15705,15706,15707,15708,15709,15710,15711,15712,15713,15714,15715,15716,15717,15718,15719,15720,15721,15722,15723,15724,15725,15726,15727,16304,16305,16306,16307,16308,16309,16310,16311,16312,16313,16314,16315,16316,16317,16318,16319,16448,16449,16450,16451,16452,16453,16454,16455,16456,16457,16458,16459,16460,16461,16462,16463,16946,18496,18497,18498,18499,18500,18501,18502,18503,18504,18505,18506,18507,18508,18509,18510,18511,19707,20048,20049,20050,20051,20052,20053,20054,20055,20056,20057,20058,20059,20060,20061,20062,20063,20224,20225,20226,20227,20228,20229,20230,20231,20232,20233,20234,20235,20236,20237,20238,20239,24192,24193,24194,24195,24196,24197,24198,24199,24200,24201,24202,24203,24204,24205,24206,24207,25072,25073,25074,25075,25076,25077,25078,25079,25080,25081,25082,25083,25084,25085,25086,25087,25888,25889,25890,25891,25892,25893,25894,25895,25896,25897,25898,25899,25900,25901,25902,25903,26333,27200,27201,27202,27203,27204,27205,27206,27207,27208,27209,27210,27211,27212,27213,27214,27215,27280,27281,27282,27283,27284,27285,27286,27287,27288,27289,27290,27291,27292,27293,27294,27295,27984,27985,27986,27987,27988,27989,27990,27991,27992,27993,27994,27995,27996,27997,27998,27999,28048,28049,28050,28051,28052,28053,28054,28055,28056,28057,28058,28059,28060,28061,28062,28063,30416,30417,30418,30419,30420,30421,30422,30423,30424,30425,30426,30427,30428,30429,30430,30431
2020-11-21 22:20:26:384 nl.zeesoft.zdk.test.neural.TestProcessorFactory: 768;48;598,1662,1664,1665,1666,1667,1668,1669,1670,1671,1672,1673,1674,1675,1676,1677,1678,1679,1745,2793,3693,4904,5757,5889,6782,6892,7088,7089,7090,7091,7092,7093,7094,7095,7096,7097,7098,7099,7100,7101,7102,7103,8182,8973,9648,9649,9650,9651,9652,9653,9654,9655,9656,9657,9658,9659,9660,9661,9662,9663,10311,10458,11064,11167,11504,11505,11506,11507,11508,11509,11510,11511,11512,11513,11514,11515,11516,11517,11518,11519,11526,11675,11712,11713,11714,11715,11716,11717,11718,11719,11720,11721,11722,11723,11724,11725,11726,11727,12174,12403,12448,12449,12450,12451,12452,12453,12454,12455,12456,12457,12458,12459,12460,12461,12462,12463,12912,12913,12914,12915,12916,12917,12918,12919,12920,12921,12922,12923,12924,12925,12926,12927,13603,13888,13889,13890,13891,13892,13893,13894,13895,13896,13897,13898,13899,13900,13901,13902,13903,14011,14352,14353,14354,14355,14356,14357,14358,14359,14360,14361,14362,14363,14364,14365,14366,14367,14514,14704,14705,14706,14707,14708,14709,14710,14711,14712,14713,14714,14715,14716,14717,14718,14719,15099,15713,19488,19489,19490,19491,19492,19493,19494,19495,19496,19497,19498,19499,19500,19501,19502,19503,19984,19985,19986,19987,19988,19989,19990,19991,19992,19993,19994,19995,19996,19997,19998,19999,20933,21968,21969,21970,21971,21972,21973,21974,21975,21976,21977,21978,21979,21980,21981,21982,21983,22796,24368,24369,24370,24371,24372,24373,24374,24375,24376,24377,24378,24379,24380,24381,24382,24383,24944,24945,24946,24947,24948,24949,24950,24951,24952,24953,24954,24955,24956,24957,24958,24959,25024,25025,25026,25027,25028,25029,25030,25031,25032,25033,25034,25035,25036,25037,25038,25039,27285,28800,28801,28802,28803,28804,28805,28806,28807,28808,28809,28810,28811,28812,28813,28814,28815,31181
2020-11-21 22:20:29:718 nl.zeesoft.zdk.test.neural.TestProcessorFactory: 768;48;192,193,194,195,196,197,198,199,200,201,202,203,204,205,206,207,542,758,1088,1089,1090,1091,1092,1093,1094,1095,1096,1097,1098,1099,1100,1101,1102,1103,1745,2793,3096,4358,4904,5344,5345,5346,5347,5348,5349,5350,5351,5352,5353,5354,5355,5356,5357,5358,5359,5568,5569,5570,5571,5572,5573,5574,5575,5576,5577,5578,5579,5580,5581,5582,5583,5858,7681,8182,8769,10311,10458,11103,11167,11497,11760,11761,11762,11763,11764,11765,11766,11767,11768,11769,11770,11771,11772,11773,11774,11775,12174,12403,12915,13897,14157,14514,15099,15711,16311,16461,16944,16945,16946,16947,16948,16949,16950,16951,16952,16953,16954,16955,16956,16957,16958,16959,18727,18992,18993,18994,18995,18996,18997,18998,18999,19000,19001,19002,19003,19004,19005,19006,19007,19488,19489,19490,19491,19492,19493,19494,19495,19496,19497,19498,19499,19500,19501,19502,19503,23440,23441,23442,23443,23444,23445,23446,23447,23448,23449,23450,23451,23452,23453,23454,23455,23872,23873,23874,23875,23876,23877,23878,23879,23880,23881,23882,23883,23884,23885,23886,23887,24206,24368,24369,24370,24371,24372,24373,24374,24375,24376,24377,24378,24379,24380,24381,24382,24383,25900,26333,28048,28049,28050,28051,28052,28053,28054,28055,28056,28057,28058,28059,28060,28061,28062,28063,28814,31136,31137,31138,31139,31140,31141,31142,31143,31144,31145,31146,31147,31148,31149,31150,31151,31728,31729,31730,31731,31732,31733,31734,31735,31736,31737,31738,31739,31740,31741,31742,31743,32112,32113,32114,32115,32116,32117,32118,32119,32120,32121,32122,32123,32124,32125,32126,32127
2020-11-21 22:20:32:952 nl.zeesoft.zdk.test.neural.TestProcessorFactory: 768;48;542,598,758,1662,1664,1665,1666,1667,1668,1669,1670,1671,1672,1673,1674,1675,1676,1677,1678,1679,1745,3693,4904,5757,5858,5889,6892,8182,8769,9648,9649,9650,9651,9652,9653,9654,9655,9656,9657,9658,9659,9660,9661,9662,9663,10430,10614,10624,10625,10626,10627,10628,10629,10630,10631,10632,10633,10634,10635,10636,10637,10638,10639,11064,11103,11167,11526,11675,11712,11713,11714,11715,11716,11717,11718,11719,11720,11721,11722,11723,11724,11725,11726,11727,11774,12174,12320,12321,12322,12323,12324,12325,12326,12327,12328,12329,12330,12331,12332,12333,12334,12335,12403,12512,12513,12514,12515,12516,12517,12518,12519,12520,12521,12522,12523,12524,12525,12526,12527,12920,13600,13601,13602,13603,13604,13605,13606,13607,13608,13609,13610,13611,13612,13613,13614,13615,14011,14365,14514,15099,15654,16461,19490,20933,21968,21969,21970,21971,21972,21973,21974,21975,21976,21977,21978,21979,21980,21981,21982,21983,22796,23440,23441,23442,23443,23444,23445,23446,23447,23448,23449,23450,23451,23452,23453,23454,23455,23872,23873,23874,23875,23876,23877,23878,23879,23880,23881,23882,23883,23884,23885,23886,23887,25072,25073,25074,25075,25076,25077,25078,25079,25080,25081,25082,25083,25084,25085,25086,25087,27200,27201,27202,27203,27204,27205,27206,27207,27208,27209,27210,27211,27212,27213,27214,27215,27984,27985,27986,27987,27988,27989,27990,27991,27992,27993,27994,27995,27996,27997,27998,27999
2020-11-21 22:20:36:245 nl.zeesoft.zdk.test.neural.TestProcessorFactory: 768;48;542,598,758,881,1094,1664,1665,1666,1667,1668,1669,1670,1671,1672,1673,1674,1675,1676,1677,1678,1679,1745,2793,3096,3499,3693,4133,5757,5763,5858,5889,6720,6721,6722,6723,6724,6725,6726,6727,6728,6729,6730,6731,6732,6733,6734,6735,6782,6892,7091,7681,8182,10311,10458,11064,11497,11504,11505,11506,11507,11508,11509,11510,11511,11512,11513,11514,11515,11516,11517,11518,11519,11526,11774,12174,12403,13603,14011,14514,15099,15711,15713,18992,18993,18994,18995,18996,18997,18998,18999,19000,19001,19002,19003,19004,19005,19006,19007,20933,22796,22868,25035,25900,29700,30144,30145,30146,30147,30148,30149,30150,30151,30152,30153,30154,30155,30156,30157,30158,30159,32112,32113,32114,32115,32116,32117,32118,32119,32120,32121,32122,32123,32124,32125,32126,32127
2020-11-21 22:20:39:469 nl.zeesoft.zdk.test.neural.TestProcessorFactory: 768;48;502,598,758,3499,3693,4358,4904,5889,6720,6721,6722,6723,6724,6725,6726,6727,6728,6729,6730,6731,6732,6733,6734,6735,6782,6892,8182,9370,10311,10458,10614,11064,11167,11488,11489,11490,11491,11492,11493,11494,11495,11496,11497,11498,11499,11500,11501,11502,11503,11505,11526,11675,11714,12174,12403,12462,12518,13603,14011,14157,14365,14719,15099,15301,15711,16461,18735,19328,19329,19330,19331,19332,19333,19334,19335,19336,19337,19338,19339,19340,19341,19342,19343,19696,19697,19698,19699,19700,19701,19702,19703,19704,19705,19706,19707,19708,19709,19710,19711,20933,21974,22796,23452,24993,25078,28814
2020-11-21 22:20:42:633 nl.zeesoft.zdk.test.neural.TestProcessorFactory: 768;48;502,542,758,881,1094,1745,3693,4904,5757,6224,6225,6226,6227,6228,6229,6230,6231,6232,6233,6234,6235,6236,6237,6238,6239,6612,6726,6782,7072,7073,7074,7075,7076,7077,7078,7079,7080,7081,7082,7083,7084,7085,7086,7087,7681,8182,8769,8973,9370,10311,10458,10624,10625,10626,10627,10628,10629,10630,10631,10632,10633,10634,10635,10636,10637,10638,10639,11064,11167,11497,12174,12320,12321,12322,12323,12324,12325,12326,12327,12328,12329,12330,12331,12332,12333,12334,12335,12403,14514,15099,15711,15712,15713,15714,15715,15716,15717,15718,15719,15720,15721,15722,15723,15724,15725,15726,15727,19993,20051,20896,20897,20898,20899,20900,20901,20902,20903,20904,20905,20906,20907,20908,20909,20910,20911,20933,22796,22868,24192,24193,24194,24195,24196,24197,24198,24199,24200,24201,24202,24203,24204,24205,24206,24207,24371,27984,27985,27986,27987,27988,27989,27990,27991,27992,27993,27994,27995,27996,27997,27998,27999,28052,30419,31147,31168,31169,31170,31171,31172,31173,31174,31175,31176,31177,31178,31179,31180,31181,31182,31183,31948
2020-11-21 22:20:45:697 nl.zeesoft.zdk.test.neural.TestProcessorFactory: 768;48;542,598,758,881,1662,1667,1745,3499,4133,4904,5757,5889,6612,6782,8182,8769,8973,9370,10311,10430,10458,11064,11505,11526,12174,12403,12912,12913,12914,12915,12916,12917,12918,12919,12920,12921,12922,12923,12924,12925,12926,12927,14514,14719,15099,15711,16461,19490,20051,20896,20897,20898,20899,20900,20901,20902,20903,20904,20905,20906,20907,20908,20909,20910,20911,21968,21969,21970,21971,21972,21973,21974,21975,21976,21977,21978,21979,21980,21981,21982,21983,23876,24206,25900,27201,28052,28814,29700,30419,31181,31948
2020-11-21 22:20:48:754 nl.zeesoft.zdk.test.neural.TestProcessorFactory: 768;48;598,758,881,1667,1745,3499,3693,4358,4904,5757,5763,5889,6224,6225,6226,6227,6228,6229,6230,6231,6232,6233,6234,6235,6236,6237,6238,6239,6612,6892,7091,8182,8973,9651,10311,10430,10458,11064,11497,11505,11526,11774,12174,12403,12920,14011,14157,14514,14719,15099,15711,16311,18735,19328,19329,19330,19331,19332,19333,19334,19335,19336,19337,19338,19339,19340,19341,19342,19343,20897,20933,22796,25035,25078,28052,31948
Processing 1000 SDRs took: 30916 ms (30 ms/SDR)
~~~~

nl.zeesoft.zdk.test.neural.TestNetwork
--------------------------------------
This test shows how to use a *Network* to link *Processor* instances together. 
A *NetworkConfig* instance can be used to configure the *Network* before initialization. 
A *NetworkIO* instance can be used to feed input to the network and gather all the outputs of all processors in the network. 
Non-SDR network input values will be wrapped in a *KeyValueSDR* before being passed to a processor. 

**Example implementation**  
~~~~
// Create the configuration
NetworkConfig config = new NetworkConfig();
// Define a network input
config.inputNames.add("input1");
// Add some processors
config.addScalarEncoder("EN");
config.addSpatialPooler("SP");
config.addTemporalMemory("TM");
config.addClassifier("CL");
// Link the network inputs and processors
config.addLink("input1", 0, "EN", 0);
config.addLink("EN", 0, "SP", 0);
config.addLink("SP", 0, "TM", 0);
config.addLink("TM", 0, "CL", 0);
config.addLink("input1", 0, "CL", 1);
// Create the network
Network network = new Network();
network.configure(config);
network.initialize(true);
// Use the network
NetworkIO io = new NetworkIO();
io.setValue("input1", 1);
network.processIO(io);
// Save the network
network.save("data/");
~~~~

Class references;  
 * [TestNetwork](https://github.com/DyzLecticus/Zeesoft/blob/master/V5.0/ZDK/src/nl/zeesoft/zdk/test/neural/TestNetwork.java)
 * [Network](https://github.com/DyzLecticus/Zeesoft/blob/master/V5.0/ZDK/src/nl/zeesoft/zdk/neural/network/Network.java)
 * [Processor](https://github.com/DyzLecticus/Zeesoft/blob/master/V5.0/ZDK/src/nl/zeesoft/zdk/neural/processors/Processor.java)
 * [NetworkConfig](https://github.com/DyzLecticus/Zeesoft/blob/master/V5.0/ZDK/src/nl/zeesoft/zdk/neural/network/NetworkConfig.java)
 * [NetworkIO](https://github.com/DyzLecticus/Zeesoft/blob/master/V5.0/ZDK/src/nl/zeesoft/zdk/neural/network/NetworkIO.java)

**Test output**  
The output of this test shows;  
 * The network configuration description  
 * Some network debug logging  
 * An example output  
~~~~
Inputs: 
<- value
Layer: 0
  EN: ScalarEncoderConfig:
  <- 0 = value
  -> 0 = SDR: 16*16, on bits: 16
  -> 1 = value: 1*1
Layer: 1
  SP: SpatialPoolerConfig: 16*16*1
  <- 0 = EN/0
  -> 0 = Active columns: 48*48, on bits: 46
Layer: 2
  TM: TemporalMemoryConfig: 48*48*16
  <- 0 = SP/0
  -> 0 = Active cells: 768*48
  -> 1 = Bursting columns: 48*48
  -> 2 = Predictive cells: 768*48
  -> 3 = Winner cells: 768*48
Layer: 3
  CL: ClassifierConfig: 768*48 (value)
  <- 0 = TM/0
  <- 1 = value
  -> 0 = Classifications: 1*1

2020-11-21 22:20:51:855 nl.zeesoft.zdk.neural.network.Network: Initializing network ...
2020-11-21 22:20:51:855 nl.zeesoft.zdk.neural.processors.SpatialPooler: Initializing SpatialPooler ...
2020-11-21 22:20:51:855 nl.zeesoft.zdk.neural.processors.TemporalMemory: Initializing TemporalMemory ...
2020-11-21 22:20:51:856 nl.zeesoft.zdk.neural.processors.Classifier: Initializing Classifier ...
2020-11-21 22:20:51:855 nl.zeesoft.zdk.neural.processors.ScalarEncoder: Initializing ScalarEncoder ...
2020-11-21 22:20:51:857 nl.zeesoft.zdk.neural.processors.ScalarEncoder: Initialized
2020-11-21 22:20:51:858 nl.zeesoft.zdk.neural.processors.Classifier: Initialized
2020-11-21 22:20:51:860 nl.zeesoft.zdk.neural.processors.TemporalMemory: Initialized
2020-11-21 22:20:51:860 nl.zeesoft.zdk.neural.processors.TemporalMemory: Resetting TemporalMemory connections ...
2020-11-21 22:20:51:862 nl.zeesoft.zdk.neural.processors.TemporalMemory: Reset connections
2020-11-21 22:20:51:885 nl.zeesoft.zdk.neural.processors.SpatialPooler: Initialized
2020-11-21 22:20:51:885 nl.zeesoft.zdk.neural.processors.SpatialPooler: Resetting SpatialPooler connections ...
2020-11-21 22:20:51:948 nl.zeesoft.zdk.neural.processors.SpatialPooler: Reset connections
2020-11-21 22:20:51:949 nl.zeesoft.zdk.neural.network.Network: Initialized network

2020-11-21 22:20:51:949 nl.zeesoft.zdk.test.neural.TestNetwork: Processing 100 SDRs ...
2020-11-21 22:20:55:888 nl.zeesoft.zdk.test.neural.TestNetwork: Processed 100 SDRs

2020-11-21 22:20:55:895 nl.zeesoft.zdk.neural.network.Network: Saving network ...
2020-11-21 22:20:55:896 nl.zeesoft.zdk.neural.network.Network$3: Writing dist/CL.txt ...
2020-11-21 22:20:55:896 nl.zeesoft.zdk.neural.network.Network$3: Writing dist/EN.txt ...
2020-11-21 22:20:55:896 nl.zeesoft.zdk.neural.network.Network$3: Writing dist/TM.txt ...
2020-11-21 22:20:55:921 nl.zeesoft.zdk.neural.network.Network$3: Writing dist/SP.txt ...
2020-11-21 22:20:55:994 nl.zeesoft.zdk.neural.network.Network$4: Writing dist/PreviousIO.txt ...
2020-11-21 22:20:56:574 nl.zeesoft.zdk.neural.network.Network: Saved network
2020-11-21 22:20:56:574 nl.zeesoft.zdk.neural.network.Network: Initializing network ...
2020-11-21 22:20:56:574 nl.zeesoft.zdk.neural.processors.ScalarEncoder: Initializing ScalarEncoder ...
2020-11-21 22:20:56:575 nl.zeesoft.zdk.neural.processors.SpatialPooler: Initializing SpatialPooler ...
2020-11-21 22:20:56:575 nl.zeesoft.zdk.neural.processors.ScalarEncoder: Initialized
2020-11-21 22:20:56:575 nl.zeesoft.zdk.neural.processors.Classifier: Initializing Classifier ...
2020-11-21 22:20:56:575 nl.zeesoft.zdk.neural.processors.TemporalMemory: Initializing TemporalMemory ...
2020-11-21 22:20:56:576 nl.zeesoft.zdk.neural.processors.Classifier: Initialized
2020-11-21 22:20:56:579 nl.zeesoft.zdk.neural.processors.TemporalMemory: Initialized
2020-11-21 22:20:56:600 nl.zeesoft.zdk.neural.processors.SpatialPooler: Initialized
2020-11-21 22:20:56:602 nl.zeesoft.zdk.neural.network.Network: Initialized network
2020-11-21 22:20:56:602 nl.zeesoft.zdk.neural.network.Network: Loading network ...
2020-11-21 22:20:56:602 nl.zeesoft.zdk.neural.network.Network$3: Reading dist/EN.txt ...
2020-11-21 22:20:56:602 nl.zeesoft.zdk.neural.network.Network$3: Reading dist/CL.txt ...
2020-11-21 22:20:56:602 nl.zeesoft.zdk.neural.network.Network$3: Reading dist/TM.txt ...
2020-11-21 22:20:56:603 nl.zeesoft.zdk.neural.network.Network$3: Reading dist/SP.txt ...
2020-11-21 22:20:56:710 nl.zeesoft.zdk.neural.network.Network$4: Reading dist/PreviousIO.txt ...
2020-11-21 22:21:00:703 nl.zeesoft.zdk.neural.network.Network: Loaded network

Processor: EN
-> 16;16;1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16
-> SDR##1;1;@value#java.lang.Integer#1
Processor: SP
-> 48;48;1762,1904,23,925,789,234,1028,1407,1316,982,863,266,1378,993,1292,415,1257,1748,335,570,918,383,681,527,1165,856,1267,186,597,670,531,1233,354,803,184,502,2000,1564,811,920,979,303,595,1664,231,130
Processor: TM
-> 768;48;2080,2081,2082,2083,2084,2085,2086,2087,2088,2089,2090,2091,2092,2093,2094,2095,2944,2945,2946,2947,2948,2949,2950,2951,2952,2953,2954,2955,2956,2957,2958,2959,2976,2977,2978,2979,2980,2981,2982,2983,2984,2985,2986,2987,2988,2989,2990,2991,3744,3745,3746,3747,3748,3749,3750,3751,3752,3753,3754,3755,3756,3757,3758,3759,4256,4257,4258,4259,4260,4261,4262,4263,4264,4265,4266,4267,4268,4269,4270,4271,368,369,370,371,372,373,374,375,376,377,378,379,380,381,382,383,5664,5665,5666,5667,5668,5669,5670,5671,5672,5673,5674,5675,5676,5677,5678,5679,8032,8033,8034,8035,8036,8037,8038,8039,8040,8041,8042,8043,8044,8045,8046,8047,3696,3697,3698,3699,3700,3701,3702,3703,3704,3705,3706,3707,3708,3709,3710,3711,9120,9121,9122,9123,9124,9125,9126,9127,9128,9129,9130,9131,9132,9133,9134,9135,13696,13697,13698,13699,13700,13701,13702,13703,13704,13705,13706,13707,13708,13709,13710,13711,10720,10721,10722,10723,10724,10725,10726,10727,10728,10729,10730,10731,10732,10733,10734,10735,9552,9553,9554,9555,9556,9557,9558,9559,9560,9561,9562,9563,9564,9565,9566,9567,4848,4849,4850,4851,4852,4853,4854,4855,4856,4857,4858,4859,4860,4861,4862,4863,5360,5361,5362,5363,5364,5365,5366,5367,5368,5369,5370,5371,5372,5373,5374,5375,14720,14721,14722,14723,14724,14725,14726,14727,14728,14729,14730,14731,14732,14733,14734,14735,10896,10897,10898,10899,10900,10901,10902,10903,10904,10905,10906,10907,10908,10909,10910,10911,6128,6129,6130,6131,6132,6133,6134,6135,6136,6137,6138,6139,6140,6141,6142,6143,14688,14689,14690,14691,14692,14693,14694,14695,14696,14697,14698,14699,14700,14701,14702,14703,16448,16449,16450,16451,16452,16453,16454,16455,16456,16457,16458,16459,16460,16461,16462,16463,12624,12625,12626,12627,12628,12629,12630,12631,12632,12633,12634,12635,12636,12637,12638,12639,6654,15712,15713,15714,15715,15716,15717,15718,15719,15720,15721,15722,15723,15724,15725,15726,15727,8432,8433,8434,8435,8436,8437,8438,8439,8440,8441,8442,8443,8444,8445,8446,8447,14800,14801,14802,14803,14804,14805,14806,14807,14808,14809,14810,14811,14812,14813,14814,14815,8496,8497,8498,8499,8500,8501,8502,8503,8504,8505,8506,8507,8508,8509,8510,8511,15888,15889,15890,15891,15892,15893,15894,15895,15896,15897,15898,15899,15900,15901,15902,15903,20672,20673,20674,20675,20676,20677,20678,20679,20680,20681,20682,20683,20684,20685,20686,20687,9520,9521,9522,9523,9524,9525,9526,9527,9528,9529,9530,9531,9532,9533,9534,9535,21056,21057,21058,21059,21060,21061,21062,21063,21064,21065,21066,21067,21068,21069,21070,21071,22048,22049,22050,22051,22052,22053,22054,22055,22056,22057,22058,22059,22060,22061,22062,22063,18640,18641,18642,18643,18644,18645,18646,18647,18648,18649,18650,18651,18652,18653,18654,18655,12848,12849,12850,12851,12852,12853,12854,12855,12856,12857,12858,12859,12860,12861,12862,12863,19728,19729,19730,19731,19732,19733,19734,19735,19736,19737,19738,19739,19740,19741,19742,19743,12976,12977,12978,12979,12980,12981,12982,12983,12984,12985,12986,12987,12988,12989,12990,12991,25024,25025,25026,25027,25028,25029,25030,25031,25032,25033,25034,25035,25036,25037,25038,25039,20112,20113,20114,20115,20116,20117,20118,20119,20120,20121,20122,20123,20124,20125,20126,20127,13818,26624,26625,26626,26627,26628,26629,26630,26631,26632,26633,26634,26635,26636,26637,26638,26639,15664,15665,15666,15667,15668,15669,15670,15671,15672,15673,15674,15675,15676,15677,15678,15679,28192,28193,28194,28195,28196,28197,28198,28199,28200,28201,28202,28203,28204,28205,28206,28207,27968,27969,27970,27971,27972,27973,27974,27975,27976,27977,27978,27979,27980,27981,27982,27983,30464,30465,30466,30467,30468,30469,30470,30471,30472,30473,30474,30475,30476,30477,30478,30479,20272,20273,20274,20275,20276,20277,20278,20279,20280,20281,20282,20283,20284,20285,20286,20287,32000,32001,32002,32003,32004,32005,32006,32007,32008,32009,32010,32011,32012,32013,32014,32015,22512,22513,22514,22515,22516,22517,22518,22519,22520,22521,22522,22523,22524,22525,22526,22527
-> 48;48;23,130,184,186,231,234,266,303,335,354,383,502,527,531,570,595,597,670,681,789,803,811,856,918,920,925,979,982,993,1028,1165,1233,1257,1267,1292,1316,1378,1407,1564,1664,1748,1762,1904,2000
-> 768;48;12619,6455,6967,13804,10109,24915
-> 768;48;2088,2951,2976,3755,4261,371,5666,8032,3705,9125,10728,9555,13703,4858,5370,14730,10898,6143,14702,6654,12634,16463,15723,8436,14801,8498,15898,20681,9532,21058,22048,18649,12852,19739,12983,25038,13818,20122,26632,15668,28204,27980,30476,20276,32000,22516
Processor: CL
-> SDR##1;1;@accuracy#java.lang.Float#1.0@accuracyTrend#java.lang.Float#1.0@classification:1#nl.zeesoft.zdk.neural.processors.Classification#1;value;java.lang.Integer;0,2468%1,723
~~~~

Test results
------------
All 15 tests have been executed successfully (283 assertions).  
Total test duration: 55212 ms (total sleep duration: 176 ms).  

Memory usage per test;  
 * nl.zeesoft.zdk.test.TestStr: 676 Kb / 0 Mb
 * nl.zeesoft.zdk.test.thread.TestRunCode: 435 Kb / 0 Mb
 * nl.zeesoft.zdk.test.thread.TestCodeRunnerChain: 438 Kb / 0 Mb
 * nl.zeesoft.zdk.test.collection.TestCollections: 465 Kb / 0 Mb
 * nl.zeesoft.zdk.test.http.TestHttpServer: 810 Kb / 0 Mb
 * nl.zeesoft.zdk.test.grid.TestGrid: 799 Kb / 0 Mb
 * nl.zeesoft.zdk.test.neural.TestSDR: 818 Kb / 0 Mb
 * nl.zeesoft.zdk.test.neural.TestCellGrid: 877 Kb / 0 Mb
 * nl.zeesoft.zdk.test.neural.TestScalarEncoder: 879 Kb / 0 Mb
 * nl.zeesoft.zdk.test.neural.TestSpatialPooler: 881 Kb / 0 Mb
 * nl.zeesoft.zdk.test.neural.TestTemporalMemory: 894 Kb / 0 Mb
 * nl.zeesoft.zdk.test.neural.TestClassifier: 905 Kb / 0 Mb
 * nl.zeesoft.zdk.test.neural.TestMerger: 915 Kb / 0 Mb
 * nl.zeesoft.zdk.test.neural.TestProcessorFactory: 916 Kb / 0 Mb
 * nl.zeesoft.zdk.test.neural.TestNetwork: 949 Kb / 0 Mb
