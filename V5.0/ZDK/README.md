Zeesoft Development Kit
=======================
The Zeesoft Development Kit (ZDK) is an open source library for Java application development.  
It provides support for;  
 * Self documenting and testing libraries  
 * (Mock) File writing and reading  
 * Dynamic class instantiation and reflection  
 * Extended StringBuilder manipulation and validation  
 * Timestamped logging  
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
 * The temporal memory supports optional apical feedback  
 * The classifier can be configured to slowly forget old SDR associations  
 * The implementation allows for SDR processor and network customization via configuration and/or code extension   
 * The global default number of threads used per SDR processor can be controlled via the *ProcessorFactory.THREADS* constant (default = 4) 
  
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
Reading file: 2
Reading file: 3
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
2021-01-16 17:27:56:408 nl.zeesoft.zdk.http.HttpServer: Opening server socket on port: 8080 ...
2021-01-16 17:27:56:424 nl.zeesoft.zdk.http.HttpServer: Opened server socket on port: 8080
2021-01-16 17:27:56:449 nl.zeesoft.zdk.http.HttpServer$1: Request/response headers (Port: 8080);
<<<
GET / HTTP/1.1
Content-Type: text/html
Host: 127.0.0.1
Connection: keep-alive
>>>
HTTP/1.1 200 OK
Content-Length: 47
2021-01-16 17:27:56:650 nl.zeesoft.zdk.http.HttpServer$1: Request/response headers (Port: 8080);
<<<
GET /favicon.ico HTTP/1.1
Content-Type: text/html
Host: 127.0.0.1
Connection: keep-alive
>>>
HTTP/1.1 200 OK
Content-Length: 173
2021-01-16 17:27:56:667 nl.zeesoft.zdk.http.HttpServer$1: Request/response headers (Port: 8080);
<<<
GET /pizza.txt HTTP/1.1
Content-Type: text/html
Host: 127.0.0.1
Connection: keep-alive
>>>
HTTP/1.1 404 File not found: http/pizza.txt
Content-Length: 30
2021-01-16 17:27:56:668 nl.zeesoft.zdk.http.HttpServer$1: Request/response headers (Port: 8080);
<<<
PUT /pizza.txt HTTP/1.1
Content-Type: text/html
Host: 127.0.0.1
Connection: keep-alive
Content-Length: 13
>>>
HTTP/1.1 200 OK
2021-01-16 17:27:56:670 nl.zeesoft.zdk.http.HttpServer$1: Request/response headers (Port: 8080);
<<<
GET /pizza.txt HTTP/1.1
Content-Type: text/html
Host: 127.0.0.1
Connection: keep-alive
>>>
HTTP/1.1 200 OK
Content-Length: 13
2021-01-16 17:27:56:671 nl.zeesoft.zdk.http.HttpServer$1: Request/response headers (Port: 8080);
<<<
DELETE /pizza.txt HTTP/1.1
Content-Type: text/html
Host: 127.0.0.1
Connection: keep-alive
>>>
HTTP/1.1 200 OK
2021-01-16 17:27:56:673 nl.zeesoft.zdk.http.HttpServer: Opening server socket on port: 9090 ...
2021-01-16 17:27:56:673 nl.zeesoft.zdk.http.HttpServer: Opened server socket on port: 9090
2021-01-16 17:27:56:676 nl.zeesoft.zdk.http.HttpServer$1: Request/response headers (Port: 8080);
<<<
GET / HTTP/1.1
Host: 127.0.0.1:8080
Content-Type: text/html
Connection: keep-alive
>>>
HTTP/1.1 200 OK
Content-Length: 47
2021-01-16 17:27:56:679 nl.zeesoft.zdk.http.HttpServer$1: Request/response headers (Port: 9090);
<<<
GET / HTTP/1.1
Host: 127.0.0.1:8080
Content-Type: text/html
Connection: keep-alive
>>>
HTTP/1.1 200 OK
Content-Length: 47
2021-01-16 17:27:56:695 nl.zeesoft.zdk.http.HttpServer: Closing connections ...
2021-01-16 17:27:56:695 nl.zeesoft.zdk.http.HttpServer: Closed connections
2021-01-16 17:27:56:695 nl.zeesoft.zdk.http.HttpServer: Closing server socket on port: 9090 ...
2021-01-16 17:27:56:695 nl.zeesoft.zdk.http.HttpServer: Closed server socket on port: 9090
2021-01-16 17:27:56:711 nl.zeesoft.zdk.http.HttpServer: Closing connections ...
2021-01-16 17:27:56:711 nl.zeesoft.zdk.http.HttpServer: Closed connections
2021-01-16 17:27:56:711 nl.zeesoft.zdk.http.HttpServer: Closing server socket on port: 8080 ...
2021-01-16 17:27:56:711 nl.zeesoft.zdk.http.HttpServer: Closed server socket on port: 8080

Action log;
MKDIRS:http/
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
grid.initialize(4, 4, 4, 0.5F);
// Disable locking (Not needed if column functions do not depend on values on other columns)  
grid.setUseLock(false);
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
1,0,0=P@0,1,0;0.1#0,1,0;0.1!P@1,1,0;0.1#1,1,0;0.1!D@2,1,0;1#2,1,0;1!D@3,1,0;1#3,1,0;1!A@4,1,0;1#4,1,0;1!A@5,1,0;1#5,1,0;1
2,1,0=P@1,1,0;0.1#1,1,0;0.1!P@2,1,0;1#2,1,0;1!D@3,1,0;1#3,1,0;1!D@4,1,0;1#4,1,0;1!A@5,1,0;1#5,1,0;1!A@6,1,0;1#6,1,0;1
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
2021-01-16 17:27:56:913 nl.zeesoft.zdk.neural.processors.ScalarEncoder: Initializing ScalarEncoder ...
2021-01-16 17:27:56:913 nl.zeesoft.zdk.neural.processors.ScalarEncoder: Initialized
Encoded SDR for value 20: 16;16;24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39
Encoded SDR for value 21: 16;16;25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40
~~~~

nl.zeesoft.zdk.test.neural.TestSpatialPooler
--------------------------------------------
This test shows how to use a *SpatialPooler* to create consistent sparse representations of less sparse and/or smaller SDRs.
A *SpatialPoolerConfig* can be used to configure the *SpatialPooler* before initialization.
Please note that this spatial pooler implementation does not support local inhibition.  
  
Configurable properties (**Bolded** properties can be changed after initialization);  
 * *inputSizeX*, *inputSizeY*; Input SDR dimensions.  
 * *outputSizeX*, *outputSizeY*; Output SDR dimensions (mini column dimensions).  
 * *outputOnBits*; Maximum number of on bits in the output.  
 * *potentialConnections*, *potentialRadius*; Number and optional radius of potential connections relative to the input space.  
 * **permanenceThreshold**, **permanenceIncrement**, **permanenceDecrement**; Potential synapse adaptation control.  
 * *activationHistorySize*; Historic column activation buffer size (used to calculate boost factors).  
 * **boostFactorPeriod**; Boost factor recalculation period.  
 * **boostStrength**; Boost strength.  


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
2021-01-16 17:27:56:934 nl.zeesoft.zdk.neural.processors.SpatialPooler: Initializing SpatialPooler ...
2021-01-16 17:27:56:997 nl.zeesoft.zdk.neural.processors.SpatialPooler: Initialized
2021-01-16 17:27:56:997 nl.zeesoft.zdk.neural.processors.SpatialPooler: Resetting SpatialPooler connections ...
2021-01-16 17:27:57:122 nl.zeesoft.zdk.neural.processors.SpatialPooler: Reset connections

2021-01-16 17:27:57:122 nl.zeesoft.zdk.test.neural.TestSpatialPooler: Initializing spatial pooler (asynchronous) ...
2021-01-16 17:27:57:584 nl.zeesoft.zdk.test.neural.TestSpatialPooler: Initialized spatial pooler (asynchronous)

Input SDR: 16;16;0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15
Output SDR: 48;48;35,69,215,245,276,291,357,383,428,441,443,476,494,554,564,582,635,653,736,746,764,793,863,922,927,930,931,943,992,1073,1119,1128,1161,1208,1228,1239,1271,1316,1337,1365,1433,1450,1525,1717,1938,2080
Input SDR: 16;16;0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15
Output SDR: 48;48;35,69,215,245,276,291,357,383,428,441,476,494,540,554,556,564,582,635,653,736,746,764,863,922,930,931,935,943,975,992,1119,1128,1161,1208,1228,1239,1271,1316,1337,1365,1402,1450,1717,1892,1938,2038
Input SDR: 16;16;0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15
Output SDR: 48;48;35,69,215,245,276,291,357,383,428,441,476,494,554,556,564,582,635,653,736,746,764,793,863,922,930,931,935,943,975,992,1119,1128,1161,1208,1228,1239,1271,1316,1337,1365,1402,1450,1717,1892,1938,2038
Input SDR: 16;16;0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15
Output SDR: 48;48;35,69,215,245,276,291,357,383,428,441,476,494,554,556,564,582,635,653,736,746,764,793,863,922,930,931,935,943,975,992,1119,1128,1161,1208,1228,1239,1271,1316,1337,1365,1402,1450,1717,1892,1938,2038
Input SDR: 16;16;0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15
Output SDR: 48;48;35,69,215,245,276,291,357,383,428,441,476,494,554,556,564,582,635,653,736,746,764,793,863,922,930,931,935,943,975,992,1119,1128,1161,1208,1228,1239,1271,1316,1337,1365,1402,1450,1717,1892,1938,2038

Average overlap for similar inputs: 45.0, overall: 2.0
~~~~

nl.zeesoft.zdk.test.neural.TestTemporalMemory
---------------------------------------------
This test shows how to use a *TemporalMemory* to learn SDR sequences.
A *TemporalMemoryConfig* can be used to configure the *TemporalMemory* before initialization.
Configurable properties (**Bolded** properties can be changed after initialization);  
 * *sizeX*, *sizeY*, *sizeZ*; Cell grid dimensions (sizeX, sizeY specify input SDR dimensions).  
 * *maxSegmentsPerCell*; Maximum number of segments per cell.  
 * *maxSynapsesPerSegment*; Maximum number of synapses per segment.  
 * **initialPermanence**, **permanenceThreshold**, **permanenceIncrement**, **permanenceDecrement**; Distal and apical synapse adaptation control.  
 * **segmentCreationSubsample**; Limits the creation of new segments to the specified percentage.  
 * **distalSegmentDecrement**, **apicalSegmentDecrement**; Optional segment decrement for distal/apical segments.  
 * **distalPotentialRadius**; Optional potential radius for distal segments.  
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
2021-01-16 17:28:06:655 nl.zeesoft.zdk.neural.processors.TemporalMemory: Initializing TemporalMemory ...
2021-01-16 17:28:06:671 nl.zeesoft.zdk.neural.processors.TemporalMemory: Initialized
2021-01-16 17:28:06:671 nl.zeesoft.zdk.neural.processors.TemporalMemory: Resetting TemporalMemory connections ...
2021-01-16 17:28:06:671 nl.zeesoft.zdk.neural.processors.TemporalMemory: Reset connections
2021-01-16 17:28:06:719 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 1 > bursting: 46, active: 736, winners: 46, predictive: 0
2021-01-16 17:28:06:743 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 2 > bursting: 46, active: 736, winners: 46, predictive: 0
2021-01-16 17:28:06:768 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 3 > bursting: 46, active: 736, winners: 46, predictive: 0
2021-01-16 17:28:06:784 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 4 > bursting: 46, active: 736, winners: 46, predictive: 0
2021-01-16 17:28:06:806 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 5 > bursting: 46, active: 736, winners: 46, predictive: 44
2021-01-16 17:28:06:815 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 6 > bursting: 2, active: 76, winners: 46, predictive: 42
2021-01-16 17:28:06:825 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 7 > bursting: 4, active: 106, winners: 46, predictive: 41
2021-01-16 17:28:06:844 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 8 > bursting: 5, active: 121, winners: 46, predictive: 38
2021-01-16 17:28:06:858 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 9 > bursting: 8, active: 166, winners: 46, predictive: 2
2021-01-16 17:28:06:874 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 10 > bursting: 44, active: 706, winners: 46, predictive: 46
2021-01-16 17:28:06:882 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 11 > bursting: 0, active: 46, winners: 46, predictive: 46
2021-01-16 17:28:06:892 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 12 > bursting: 0, active: 46, winners: 46, predictive: 46
2021-01-16 17:28:06:898 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 13 > bursting: 0, active: 46, winners: 46, predictive: 41
2021-01-16 17:28:06:913 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 14 > bursting: 5, active: 121, winners: 46, predictive: 0
2021-01-16 17:28:06:927 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 15 > bursting: 46, active: 736, winners: 46, predictive: 46
2021-01-16 17:28:06:934 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 16 > bursting: 0, active: 46, winners: 46, predictive: 46
2021-01-16 17:28:06:947 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 17 > bursting: 0, active: 46, winners: 46, predictive: 46
2021-01-16 17:28:06:955 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 18 > bursting: 0, active: 46, winners: 46, predictive: 45
2021-01-16 17:28:06:972 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 19 > bursting: 1, active: 61, winners: 46, predictive: 0
2021-01-16 17:28:06:985 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 20 > bursting: 46, active: 736, winners: 46, predictive: 46
2021-01-16 17:28:06:991 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 21 > bursting: 0, active: 46, winners: 46, predictive: 46
2021-01-16 17:28:06:997 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 22 > bursting: 0, active: 46, winners: 46, predictive: 46
2021-01-16 17:28:07:004 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 23 > bursting: 0, active: 46, winners: 46, predictive: 41
2021-01-16 17:28:07:013 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 24 > bursting: 5, active: 121, winners: 46, predictive: 0
2021-01-16 17:28:07:039 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 25 > bursting: 46, active: 736, winners: 46, predictive: 90
2021-01-16 17:28:07:047 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 26 > bursting: 0, active: 46, winners: 46, predictive: 17
2021-01-16 17:28:07:060 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 27 > bursting: 30, active: 496, winners: 46, predictive: 72
2021-01-16 17:28:07:067 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 28 > bursting: 0, active: 46, winners: 46, predictive: 44
2021-01-16 17:28:07:073 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 29 > bursting: 2, active: 76, winners: 46, predictive: 46
2021-01-16 17:28:07:077 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 30 > bursting: 0, active: 46, winners: 46, predictive: 46
2021-01-16 17:28:07:091 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 31 > bursting: 0, active: 46, winners: 46, predictive: 44
2021-01-16 17:28:07:095 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 32 > bursting: 2, active: 76, winners: 46, predictive: 39
2021-01-16 17:28:07:101 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 33 > bursting: 7, active: 151, winners: 46, predictive: 0
2021-01-16 17:28:07:115 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 34 > bursting: 46, active: 736, winners: 46, predictive: 92
2021-01-16 17:28:07:122 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 35 > bursting: 0, active: 46, winners: 46, predictive: 24
2021-01-16 17:28:07:144 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 36 > bursting: 23, active: 391, winners: 46, predictive: 89
2021-01-16 17:28:07:151 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 37 > bursting: 0, active: 46, winners: 46, predictive: 37
2021-01-16 17:28:07:161 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 38 > bursting: 9, active: 181, winners: 46, predictive: 49
2021-01-16 17:28:07:169 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 39 > bursting: 0, active: 46, winners: 46, predictive: 46
2021-01-16 17:28:07:174 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 40 > bursting: 0, active: 46, winners: 46, predictive: 45
2021-01-16 17:28:07:181 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 41 > bursting: 1, active: 61, winners: 46, predictive: 40
2021-01-16 17:28:07:198 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 42 > bursting: 6, active: 136, winners: 46, predictive: 0
2021-01-16 17:28:07:215 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 43 > bursting: 46, active: 736, winners: 46, predictive: 95
2021-01-16 17:28:07:222 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 44 > bursting: 0, active: 46, winners: 46, predictive: 37
2021-01-16 17:28:07:238 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 45 > bursting: 16, active: 286, winners: 46, predictive: 63
2021-01-16 17:28:07:247 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 46 > bursting: 0, active: 46, winners: 46, predictive: 44
2021-01-16 17:28:07:284 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 47 > bursting: 2, active: 76, winners: 46, predictive: 46
2021-01-16 17:28:07:290 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 48 > bursting: 0, active: 46, winners: 46, predictive: 51
2021-01-16 17:28:07:297 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 49 > bursting: 0, active: 46, winners: 46, predictive: 46
2021-01-16 17:28:07:305 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 50 > bursting: 0, active: 46, winners: 46, predictive: 41
2021-01-16 17:28:07:314 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 51 > bursting: 5, active: 121, winners: 46, predictive: 0
2021-01-16 17:28:07:335 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 52 > bursting: 46, active: 736, winners: 46, predictive: 92
2021-01-16 17:28:07:352 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 53 > bursting: 0, active: 46, winners: 46, predictive: 41
2021-01-16 17:28:07:378 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 54 > bursting: 15, active: 271, winners: 46, predictive: 70
2021-01-16 17:28:07:385 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 55 > bursting: 0, active: 46, winners: 46, predictive: 46
2021-01-16 17:28:07:391 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 56 > bursting: 0, active: 46, winners: 46, predictive: 51
2021-01-16 17:28:07:398 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 57 > bursting: 0, active: 46, winners: 46, predictive: 46
2021-01-16 17:28:07:404 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 58 > bursting: 0, active: 46, winners: 46, predictive: 45
2021-01-16 17:28:07:424 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 59 > bursting: 1, active: 61, winners: 46, predictive: 43
2021-01-16 17:28:07:430 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 60 > bursting: 3, active: 91, winners: 46, predictive: 0
2021-01-16 17:28:07:450 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 61 > bursting: 46, active: 736, winners: 46, predictive: 137
2021-01-16 17:28:07:457 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 62 > bursting: 0, active: 46, winners: 46, predictive: 17
2021-01-16 17:28:07:467 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 63 > bursting: 29, active: 481, winners: 46, predictive: 113
2021-01-16 17:28:07:482 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 64 > bursting: 0, active: 46, winners: 46, predictive: 46
2021-01-16 17:28:07:489 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 65 > bursting: 7, active: 151, winners: 46, predictive: 69
2021-01-16 17:28:07:494 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 66 > bursting: 0, active: 46, winners: 46, predictive: 51
2021-01-16 17:28:07:499 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 67 > bursting: 0, active: 46, winners: 46, predictive: 46
2021-01-16 17:28:07:504 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 68 > bursting: 0, active: 46, winners: 46, predictive: 61
2021-01-16 17:28:07:509 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 69 > bursting: 0, active: 46, winners: 46, predictive: 46
2021-01-16 17:28:07:513 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 70 > bursting: 0, active: 46, winners: 46, predictive: 46
2021-01-16 17:28:07:518 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 71 > bursting: 0, active: 46, winners: 46, predictive: 45
2021-01-16 17:28:07:532 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 72 > bursting: 1, active: 61, winners: 46, predictive: 36
2021-01-16 17:28:07:539 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 73 > bursting: 10, active: 196, winners: 46, predictive: 1
2021-01-16 17:28:07:552 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 74 > bursting: 45, active: 721, winners: 46, predictive: 142
2021-01-16 17:28:07:835 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 100 > bursting: 0, active: 46, winners: 46, predictive: 46
2021-01-16 17:28:08:159 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 150 > bursting: 0, active: 46, winners: 46, predictive: 99
2021-01-16 17:28:08:470 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 200 > bursting: 0, active: 46, winners: 46, predictive: 122
2021-01-16 17:28:08:902 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 250 > bursting: 0, active: 46, winners: 46, predictive: 102
2021-01-16 17:28:09:215 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 300 > bursting: 0, active: 46, winners: 46, predictive: 123
2021-01-16 17:28:09:511 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 350 > bursting: 0, active: 46, winners: 46, predictive: 103
2021-01-16 17:28:09:814 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 400 > bursting: 0, active: 46, winners: 46, predictive: 123
2021-01-16 17:28:10:116 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 450 > bursting: 0, active: 46, winners: 46, predictive: 104
2021-01-16 17:28:10:417 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 500 > bursting: 0, active: 46, winners: 46, predictive: 124
~~~~

nl.zeesoft.zdk.test.neural.TestClassifier
-----------------------------------------
This test shows how to use a *Classifier* to classify and/or predict future input values based on SDR sequences.
A *ClassifierConfig* can be used to configure the *Classifier* before initialization.
A *Classifier* will combine all input SDRs that match the specified dimension into a single footprint to associate with a certain input value.
The input value can be provided by using a *KeyValueSDR* with a key/value pair with a configurable key.
The classifications and/or predictions will be attached to the output *KeyValueSDR* using *Classification* objects.
Please note that this classifier implementation will 'forget' old classifications by default (see maxCount).  
  
Configurable properties (**Bolded** properties can be changed after initialization);  
 * *sizeX*, *sizeY*; Merged output SDR dimensions (input SDRs that do not match are ignored for merge).  
 * **maxOnBits**; Optional maximum number of on bits in the merged input (uses sub sampling).  
 * *valueKey*; Value key to look for in the input KeyValueSDRs.  
 * *predictSteps*; Array of steps to classify/predict;  
   Step 0 will classify the current input.  
   Steps greater than 0 will predict future input.  
   By default the next step will be predicted (predictSteps[0] equals 1).  
 * *maxCount*; Maximum count of a step bit value count (a minimum of 8 is enforced);  
   If a step bit reaches this maximum, all bit value counts of the step are divided by two.  
   When when a value has only a single count it will be removed.  
   When a step bit has no value counts it will be removed.  
 * **logPredictionAccuracy**; Indicates average next step prediction accuracy and trend should be logged (requires predictSteps to contain step 1).  
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
2021-01-16 17:28:10:775 nl.zeesoft.zdk.neural.processors.Classifier: Initializing Classifier ...
2021-01-16 17:28:10:775 nl.zeesoft.zdk.neural.processors.Classifier: Initialized

2021-01-16 17:28:10:813 nl.zeesoft.zdk.neural.processors.ClassifierStep: Dividing step 1 classifier value counts by two ...
2021-01-16 17:28:10:842 nl.zeesoft.zdk.neural.processors.ClassifierStep: Dividing step 1 classifier value counts by two ...
2021-01-16 17:28:10:870 nl.zeesoft.zdk.neural.processors.ClassifierStep: Dividing step 1 classifier value counts by two ...

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
2021-01-16 17:28:10:917 nl.zeesoft.zdk.neural.processors.Merger: Initializing Merger ...
2021-01-16 17:28:10:917 nl.zeesoft.zdk.neural.processors.Merger: Initialized

Merged and distorted;
10111100
00000000
00000001
11000000
00000000
00000000
00000001
00100000
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
2021-01-16 17:28:11:002 nl.zeesoft.zdk.neural.processors.SpatialPooler: Initializing SpatialPooler ...
2021-01-16 17:28:11:017 nl.zeesoft.zdk.neural.processors.SpatialPooler: Initialized
2021-01-16 17:28:11:017 nl.zeesoft.zdk.neural.processors.SpatialPooler: Resetting SpatialPooler connections ...
2021-01-16 17:28:11:048 nl.zeesoft.zdk.neural.processors.SpatialPooler: Reset connections
2021-01-16 17:28:11:048 nl.zeesoft.zdk.neural.processors.TemporalMemory: Initializing TemporalMemory ...
2021-01-16 17:28:11:048 nl.zeesoft.zdk.neural.processors.TemporalMemory: Initialized
2021-01-16 17:28:11:048 nl.zeesoft.zdk.neural.processors.Classifier: Initializing Classifier ...
2021-01-16 17:28:11:048 nl.zeesoft.zdk.neural.processors.Classifier: Initialized

Processing ...
2021-01-16 17:28:12:744 nl.zeesoft.zdk.test.neural.TestProcessorFactory: 768;48;112,113,114,115,116,117,118,119,120,121,122,123,124,125,126,127,528,529,530,531,532,533,534,535,536,537,538,539,540,541,542,543,1632,1633,1634,1635,1636,1637,1638,1639,1640,1641,1642,1643,1644,1645,1646,1647,1958,2912,2913,2914,2915,2916,2917,2918,2919,2920,2921,2922,2923,2924,2925,2926,2927,2992,2993,2994,2995,2996,2997,2998,2999,3000,3001,3002,3003,3004,3005,3006,3007,3088,3089,3090,3091,3092,3093,3094,3095,3096,3097,3098,3099,3100,3101,3102,3103,3246,4620,4704,4705,4706,4707,4708,4709,4710,4711,4712,4713,4714,4715,4716,4717,4718,4719,5536,5537,5538,5539,5540,5541,5542,5543,5544,5545,5546,5547,5548,5549,5550,5551,5600,5601,5602,5603,5604,5605,5606,5607,5608,5609,5610,5611,5612,5613,5614,5615,6690,7728,7729,7730,7731,7732,7733,7734,7735,7736,7737,7738,7739,7740,7741,7742,7743,8896,8897,8898,8899,8900,8901,8902,8903,8904,8905,8906,8907,8908,8909,8910,8911,9040,9041,9042,9043,9044,9045,9046,9047,9048,9049,9050,9051,9052,9053,9054,9055,9056,9057,9058,9059,9060,9061,9062,9063,9064,9065,9066,9067,9068,9069,9070,9071,10320,10321,10322,10323,10324,10325,10326,10327,10328,10329,10330,10331,10332,10333,10334,10335,10448,10449,10450,10451,10452,10453,10454,10455,10456,10457,10458,10459,10460,10461,10462,10463,11824,11825,11826,11827,11828,11829,11830,11831,11832,11833,11834,11835,11836,11837,11838,11839,12592,12593,12594,12595,12596,12597,12598,12599,12600,12601,12602,12603,12604,12605,12606,12607,13143,14016,14017,14018,14019,14020,14021,14022,14023,14024,14025,14026,14027,14028,14029,14030,14031,14336,14337,14338,14339,14340,14341,14342,14343,14344,14345,14346,14347,14348,14349,14350,14351,15056,15057,15058,15059,15060,15061,15062,15063,15064,15065,15066,15067,15068,15069,15070,15071,15808,15809,15810,15811,15812,15813,15814,15815,15816,15817,15818,15819,15820,15821,15822,15823,15856,15857,15858,15859,15860,15861,15862,15863,15864,15865,15866,15867,15868,15869,15870,15871,16224,16225,16226,16227,16228,16229,16230,16231,16232,16233,16234,16235,16236,16237,16238,16239,17168,17169,17170,17171,17172,17173,17174,17175,17176,17177,17178,17179,17180,17181,17182,17183,19330,20912,20913,20914,20915,20916,20917,20918,20919,20920,20921,20922,20923,20924,20925,20926,20927,21264,21265,21266,21267,21268,21269,21270,21271,21272,21273,21274,21275,21276,21277,21278,21279,21776,21777,21778,21779,21780,21781,21782,21783,21784,21785,21786,21787,21788,21789,21790,21791,22048,22049,22050,22051,22052,22053,22054,22055,22056,22057,22058,22059,22060,22061,22062,22063,22096,22097,22098,22099,22100,22101,22102,22103,22104,22105,22106,22107,22108,22109,22110,22111,22544,22545,22546,22547,22548,22549,22550,22551,22552,22553,22554,22555,22556,22557,22558,22559,22832,22833,22834,22835,22836,22837,22838,22839,22840,22841,22842,22843,22844,22845,22846,22847,24272,24273,24274,24275,24276,24277,24278,24279,24280,24281,24282,24283,24284,24285,24286,24287,24288,24289,24290,24291,24292,24293,24294,24295,24296,24297,24298,24299,24300,24301,24302,24303,24800,24801,24802,24803,24804,24805,24806,24807,24808,24809,24810,24811,24812,24813,24814,24815,26384,26385,26386,26387,26388,26389,26390,26391,26392,26393,26394,26395,26396,26397,26398,26399,26624,26625,26626,26627,26628,26629,26630,26631,26632,26633,26634,26635,26636,26637,26638,26639,28752,28753,28754,28755,28756,28757,28758,28759,28760,28761,28762,28763,28764,28765,28766,28767,30208,30209,30210,30211,30212,30213,30214,30215,30216,30217,30218,30219,30220,30221,30222,30223,30245,31040,31041,31042,31043,31044,31045,31046,31047,31048,31049,31050,31051,31052,31053,31054,31055
2021-01-16 17:28:15:394 nl.zeesoft.zdk.test.neural.TestProcessorFactory: 768;48;359,474,608,1500,1632,1633,1634,1635,1636,1637,1638,1639,1640,1641,1642,1643,1644,1645,1646,1647,2912,2913,2914,2915,2916,2917,2918,2919,2920,2921,2922,2923,2924,2925,2926,2927,2992,2993,2994,2995,2996,2997,2998,2999,3000,3001,3002,3003,3004,3005,3006,3007,3099,4202,4608,4609,4610,4611,4612,4613,4614,4615,4616,4617,4618,4619,4620,4621,4622,4623,4704,5264,5265,5266,5267,5268,5269,5270,5271,5272,5273,5274,5275,5276,5277,5278,5279,5504,5505,5506,5507,5508,5509,5510,5511,5512,5513,5514,5515,5516,5517,5518,5519,5536,5613,6672,6673,6674,6675,6676,6677,6678,6679,6680,6681,6682,6683,6684,6685,6686,6687,6758,7056,7057,7058,7059,7060,7061,7062,7063,7064,7065,7066,7067,7068,7069,7070,7071,7361,8148,8904,9043,9056,9057,9058,9059,9060,9061,9062,9063,9064,9065,9066,9067,9068,9069,9070,9071,10406,11930,12079,12592,12593,12594,12595,12596,12597,12598,12599,12600,12601,12602,12603,12604,12605,12606,12607,13116,15557,15568,15569,15570,15571,15572,15573,15574,15575,15576,15577,15578,15579,15580,15581,15582,15583,15808,15809,15810,15811,15812,15813,15814,15815,15816,15817,15818,15819,15820,15821,15822,15823,16237,17056,17057,17058,17059,17060,17061,17062,17063,17064,17065,17066,17067,17068,17069,17070,17071,18016,18017,18018,18019,18020,18021,18022,18023,18024,18025,18026,18027,18028,18029,18030,18031,19968,19969,19970,19971,19972,19973,19974,19975,19976,19977,19978,19979,19980,19981,19982,19983,20912,20913,20914,20915,20916,20917,20918,20919,20920,20921,20922,20923,20924,20925,20926,20927,21266,22110,24303,24801,25611,25872,25873,25874,25875,25876,25877,25878,25879,25880,25881,25882,25883,25884,25885,25886,25887,27184,27185,27186,27187,27188,27189,27190,27191,27192,27193,27194,27195,27196,27197,27198,27199,29632,29633,29634,29635,29636,29637,29638,29639,29640,29641,29642,29643,29644,29645,29646,29647,30240,30241,30242,30243,30244,30245,30246,30247,30248,30249,30250,30251,30252,30253,30254,30255,31792,31793,31794,31795,31796,31797,31798,31799,31800,31801,31802,31803,31804,31805,31806,31807
2021-01-16 17:28:18:442 nl.zeesoft.zdk.test.neural.TestProcessorFactory: 768;48;359,1536,1537,1538,1539,1540,1541,1542,1543,1544,1545,1546,1547,1548,1549,1550,1551,1952,1953,1954,1955,1956,1957,1958,1959,1960,1961,1962,1963,1964,1965,1966,1967,2187,2400,2401,2402,2403,2404,2405,2406,2407,2408,2409,2410,2411,2412,2413,2414,2415,2513,2992,2993,2994,2995,2996,2997,2998,2999,3000,3001,3002,3003,3004,3005,3006,3007,3099,3358,4256,4257,4258,4259,4260,4261,4262,4263,4264,4265,4266,4267,4268,4269,4270,4271,4704,5376,5377,5378,5379,5380,5381,5382,5383,5384,5385,5386,5387,5388,5389,5390,5391,5514,5536,5613,7856,7857,7858,7859,7860,7861,7862,7863,7864,7865,7866,7867,7868,7869,7870,7871,7875,8320,8321,8322,8323,8324,8325,8326,8327,8328,8329,8330,8331,8332,8333,8334,8335,8904,9043,9297,11824,11825,11826,11827,11828,11829,11830,11831,11832,11833,11834,11835,11836,11837,11838,11839,12176,12177,12178,12179,12180,12181,12182,12183,12184,12185,12186,12187,12188,12189,12190,12191,12390,12599,15056,15057,15058,15059,15060,15061,15062,15063,15064,15065,15066,15067,15068,15069,15070,15071,15573,15840,15841,15842,15843,15844,15845,15846,15847,15848,15849,15850,15851,15852,15853,15854,15855,15856,15857,15858,15859,15860,15861,15862,15863,15864,15865,15866,15867,15868,15869,15870,15871,16237,17173,20912,20913,20914,20915,20916,20917,20918,20919,20920,20921,20922,20923,20924,20925,20926,20927,22110,22592,22593,22594,22595,22596,22597,22598,22599,22600,22601,22602,22603,22604,22605,22606,22607,22836,22960,22961,22962,22963,22964,22965,22966,22967,22968,22969,22970,22971,22972,22973,22974,22975,24303,24801,25536,25537,25538,25539,25540,25541,25542,25543,25544,25545,25546,25547,25548,25549,25550,25551,25856,25857,25858,25859,25860,25861,25862,25863,25864,25865,25866,25867,25868,25869,25870,25871,26387,26624,26625,26626,26627,26628,26629,26630,26631,26632,26633,26634,26635,26636,26637,26638,26639,27184,27185,27186,27187,27188,27189,27190,27191,27192,27193,27194,27195,27196,27197,27198,27199,29636,30208,30209,30210,30211,30212,30213,30214,30215,30216,30217,30218,30219,30220,30221,30222,30223,30250
2021-01-16 17:28:21:593 nl.zeesoft.zdk.test.neural.TestProcessorFactory: 768;48;359,608,1500,1548,2912,2913,2914,2915,2916,2917,2918,2919,2920,2921,2922,2923,2924,2925,2926,2927,4256,4257,4258,4259,4260,4261,4262,4263,4264,4265,4266,4267,4268,4269,4270,4271,4602,4610,4704,5536,6758,7168,7169,7170,7171,7172,7173,7174,7175,7176,7177,7178,7179,7180,7181,7182,7183,7361,7728,7729,7730,7731,7732,7733,7734,7735,7736,7737,7738,7739,7740,7741,7742,7743,7870,7875,8148,8904,9043,9296,9297,9298,9299,9300,9301,9302,9303,9304,9305,9306,9307,9308,9309,9310,9311,10406,11824,11825,11826,11827,11828,11829,11830,11831,11832,11833,11834,11835,11836,11837,11838,11839,11920,11921,11922,11923,11924,11925,11926,11927,11928,11929,11930,11931,11932,11933,11934,11935,12176,12177,12178,12179,12180,12181,12182,12183,12184,12185,12186,12187,12188,12189,12190,12191,12390,12599,13116,15808,15809,15810,15811,15812,15813,15814,15815,15816,15817,15818,15819,15820,15821,15822,15823,16237,20926,22060,22836,22970,23597,24303,24801,25102,25611,25867,25872,26624,26625,26626,26627,26628,26629,26630,26631,26632,26633,26634,26635,26636,26637,26638,26639,27184,27185,27186,27187,27188,27189,27190,27191,27192,27193,27194,27195,27196,27197,27198,27199,29421,29632,29633,29634,29635,29636,29637,29638,29639,29640,29641,29642,29643,29644,29645,29646,29647,30250,31040,31041,31042,31043,31044,31045,31046,31047,31048,31049,31050,31051,31052,31053,31054,31055
2021-01-16 17:28:24:790 nl.zeesoft.zdk.test.neural.TestProcessorFactory: 768;48;128,129,130,131,132,133,134,135,136,137,138,139,140,141,142,143,359,608,1500,1953,2187,2512,2513,2514,2515,2516,2517,2518,2519,2520,2521,2522,2523,2524,2525,2526,2527,2912,2913,2914,2915,2916,2917,2918,2919,2920,2921,2922,2923,2924,2925,2926,2927,3344,3345,3346,3347,3348,3349,3350,3351,3352,3353,3354,3355,3356,3357,3358,3359,4704,5536,5613,6683,7063,8148,8224,8225,8226,8227,8228,8229,8230,8231,8232,8233,8234,8235,8236,8237,8238,8239,8335,8785,8904,9043,9062,10112,10113,10114,10115,10116,10117,10118,10119,10120,10121,10122,10123,10124,10125,10126,10127,10451,12206,12599,13092,14288,14289,14290,14291,14292,14293,14294,14295,14296,14297,14298,14299,14300,14301,14302,14303,15557,15864,16237,17065,17120,17173,21266,22836,23296,23297,23298,23299,23300,23301,23302,23303,23304,23305,23306,23307,23308,23309,23310,23311,24303,24801,25102,25611,25872,25873,25874,25875,25876,25877,25878,25879,25880,25881,25882,25883,25884,25885,25886,25887,26387,27187,28767,29408,29409,29410,29411,29412,29413,29414,29415,29416,29417,29418,29419,29420,29421,29422,29423,30250
2021-01-16 17:28:27:982 nl.zeesoft.zdk.test.neural.TestProcessorFactory: 768;48;134,359,474,608,1500,2416,2417,2418,2419,2420,2421,2422,2423,2424,2425,2426,2427,2428,2429,2430,2431,2912,2913,2914,2915,2916,2917,2918,2919,2920,2921,2922,2923,2924,2925,2926,2927,3232,3233,3234,3235,3236,3237,3238,3239,3240,3241,3242,3243,3244,3245,3246,3247,4202,4592,4593,4594,4595,4596,4597,4598,4599,4600,4601,4602,4603,4604,4605,4606,4607,4704,5264,5265,5266,5267,5268,5269,5270,5271,5272,5273,5274,5275,5276,5277,5278,5279,5536,5613,8148,8903,9043,9062,9761,10333,10406,11928,12079,12599,14024,15056,15057,15058,15059,15060,15061,15062,15063,15064,15065,15066,15067,15068,15069,15070,15071,15557,15823,16237,17136,17137,17138,17139,17140,17141,17142,17143,17144,17145,17146,17147,17148,17149,17150,17151,19968,19969,19970,19971,19972,19973,19974,19975,19976,19977,19978,19979,19980,19981,19982,19983,20926,21284,22110,22592,22593,22594,22595,22596,22597,22598,22599,22600,22601,22602,22603,22604,22605,22606,22607,22960,22961,22962,22963,22964,22965,22966,22967,22968,22969,22970,22971,22972,22973,22974,22975,23597,24203,24303,24801,25611,25920,25921,25922,25923,25924,25925,25926,25927,25928,25929,25930,25931,25932,25933,25934,25935,26627,27187,29575,30250
2021-01-16 17:28:31:227 nl.zeesoft.zdk.test.neural.TestProcessorFactory: 768;48;352,353,354,355,356,357,358,359,360,361,362,363,364,365,366,367,528,529,530,531,532,533,534,535,536,537,538,539,540,541,542,543,608,1488,1489,1490,1491,1492,1493,1494,1495,1496,1497,1498,1499,1500,1501,1502,1503,1640,1953,2187,2416,2417,2418,2419,2420,2421,2422,2423,2424,2425,2426,2427,2428,2429,2430,2431,3099,3358,4704,4705,4706,4707,4708,4709,4710,4711,4712,4713,4714,4715,4716,4717,4718,4719,5264,5265,5266,5267,5268,5269,5270,5271,5272,5273,5274,5275,5276,5277,5278,5279,5514,5536,6080,6081,6082,6083,6084,6085,6086,6087,6088,6089,6090,6091,6092,6093,6094,6095,7824,7825,7826,7827,7828,7829,7830,7831,7832,7833,7834,7835,7836,7837,7838,7839,8148,8896,8897,8898,8899,8900,8901,8902,8903,8904,8905,8906,8907,8908,8909,8910,8911,9760,9761,9762,9763,9764,9765,9766,9767,9768,9769,9770,9771,9772,9773,9774,9775,10112,10113,10114,10115,10116,10117,10118,10119,10120,10121,10122,10123,10124,10125,10126,10127,11824,11825,11826,11827,11828,11829,11830,11831,11832,11833,11834,11835,11836,11837,11838,11839,12390,12599,14016,14017,14018,14019,14020,14021,14022,14023,14024,14025,14026,14027,14028,14029,14030,14031,14291,15557,15568,15569,15570,15571,15572,15573,15574,15575,15576,15577,15578,15579,15580,15581,15582,15583,15823,16237,17065,17120,17136,17137,17138,17139,17140,17141,17142,17143,17144,17145,17146,17147,17148,17149,17150,17151,17173,19968,19969,19970,19971,19972,19973,19974,19975,19976,19977,19978,19979,19980,19981,19982,19983,20912,20913,20914,20915,20916,20917,20918,20919,20920,20921,20922,20923,20924,20925,20926,20927,22110,22832,22833,22834,22835,22836,22837,22838,22839,22840,22841,22842,22843,22844,22845,22846,22847,23597,24303,24336,24337,24338,24339,24340,24341,24342,24343,24344,24345,24346,24347,24348,24349,24350,24351,24801,25611,25856,25857,25858,25859,25860,25861,25862,25863,25864,25865,25866,25867,25868,25869,25870,25871,25920,25921,25922,25923,25924,25925,25926,25927,25928,25929,25930,25931,25932,25933,25934,25935,28767,30250
2021-01-16 17:28:34:556 nl.zeesoft.zdk.test.neural.TestProcessorFactory: 768;48;128,129,130,131,132,133,134,135,136,137,138,139,140,141,142,143,530,608,1500,1548,2416,2417,2418,2419,2420,2421,2422,2423,2424,2425,2426,2427,2428,2429,2430,2431,2513,3099,3358,4202,5264,5265,5266,5267,5268,5269,5270,5271,5272,5273,5274,5275,5276,5277,5278,5279,5386,5514,5536,5613,6672,6673,6674,6675,6676,6677,6678,6679,6680,6681,6682,6683,6684,6685,6686,6687,7170,7824,7825,7826,7827,7828,7829,7830,7831,7832,7833,7834,7835,7836,7837,7838,7839,8148,8335,8904,9043,10406,10448,10449,10450,10451,10452,10453,10454,10455,10456,10457,10458,10459,10460,10461,10462,10463,12390,12599,14024,15573,16237,17120,20926,21264,21265,21266,21267,21268,21269,21270,21271,21272,21273,21274,21275,21276,21277,21278,21279,21787,22048,22049,22050,22051,22052,22053,22054,22055,22056,22057,22058,22059,22060,22061,22062,22063,22110,22557,23597,23968,23969,23970,23971,23972,23973,23974,23975,23976,23977,23978,23979,23980,23981,23982,23983,24272,24273,24274,24275,24276,24277,24278,24279,24280,24281,24282,24283,24284,24285,24286,24287,24303,24336,24337,24338,24339,24340,24341,24342,24343,24344,24345,24346,24347,24348,24349,24350,24351,24801,27187,27482,28882,30250
2021-01-16 17:28:37:850 nl.zeesoft.zdk.test.neural.TestProcessorFactory: 768;48;112,113,114,115,116,117,118,119,120,121,122,123,124,125,126,127,359,474,608,609,610,611,612,613,614,615,616,617,618,619,620,621,622,623,1488,1489,1490,1491,1492,1493,1494,1495,1496,1497,1498,1499,1500,1501,1502,1503,1640,1953,2176,2177,2178,2179,2180,2181,2182,2183,2184,2185,2186,2187,2188,2189,2190,2191,2416,2417,2418,2419,2420,2421,2422,2423,2424,2425,2426,2427,2428,2429,2430,2431,2912,2913,2914,2915,2916,2917,2918,2919,2920,2921,2922,2923,2924,2925,2926,2927,2992,2993,2994,2995,2996,2997,2998,2999,3000,3001,3002,3003,3004,3005,3006,3007,4704,5514,5536,5792,5793,5794,5795,5796,5797,5798,5799,5800,5801,5802,5803,5804,5805,5806,5807,6082,6752,6753,6754,6755,6756,6757,6758,6759,6760,6761,6762,6763,6764,6765,6766,6767,7856,7857,7858,7859,7860,7861,7862,7863,7864,7865,7866,7867,7868,7869,7870,7871,7872,7873,7874,7875,7876,7877,7878,7879,7880,7881,7882,7883,7884,7885,7886,7887,8148,8904,9040,9041,9042,9043,9044,9045,9046,9047,9048,9049,9050,9051,9052,9053,9054,9055,10112,10113,10114,10115,10116,10117,10118,10119,10120,10121,10122,10123,10124,10125,10126,10127,12176,12177,12178,12179,12180,12181,12182,12183,12184,12185,12186,12187,12188,12189,12190,12191,12192,12193,12194,12195,12196,12197,12198,12199,12200,12201,12202,12203,12204,12205,12206,12207,12599,15557,15578,15823,15840,15841,15842,15843,15844,15845,15846,15847,15848,15849,15850,15851,15852,15853,15854,15855,16237,17173,19968,19969,19970,19971,19972,19973,19974,19975,19976,19977,19978,19979,19980,19981,19982,19983,20912,20913,20914,20915,20916,20917,20918,20919,20920,20921,20922,20923,20924,20925,20926,20927,21280,21281,21282,21283,21284,21285,21286,21287,21288,21289,21290,21291,21292,21293,21294,21295,23296,23297,23298,23299,23300,23301,23302,23303,23304,23305,23306,23307,23308,23309,23310,23311,24192,24193,24194,24195,24196,24197,24198,24199,24200,24201,24202,24203,24204,24205,24206,24207,24303,24801,24816,24817,24818,24819,24820,24821,24822,24823,24824,24825,24826,24827,24828,24829,24830,24831,25600,25601,25602,25603,25604,25605,25606,25607,25608,25609,25610,25611,25612,25613,25614,25615,25856,25857,25858,25859,25860,25861,25862,25863,25864,25865,25866,25867,25868,25869,25870,25871,27187,28752,28753,28754,28755,28756,28757,28758,28759,28760,28761,28762,28763,28764,28765,28766,28767,30250,31792,31793,31794,31795,31796,31797,31798,31799,31800,31801,31802,31803,31804,31805,31806,31807
Processing 1000 SDRs took: 30199 ms (30 ms/SDR)
~~~~

nl.zeesoft.zdk.test.neural.TestNetwork
--------------------------------------
This test shows how to use a *Network* to link *Processor* instances together. 
A *NetworkConfig* instance can be used to configure the *Network* before initialization. 
A *NetworkIO* instance can be used to feed input to the network and gather all the outputs of all processors in the network. 
Network links that link from processors in higher layers are interpreted as feedback links. 
Feedback links will receive the specified processor output from the previously processed *NetworkIO*. 
Non-SDR network input values will be wrapped in a *KeyValueSDR* before being passed to a processor. 
Processor thread usage numbers can be specified for each individual processor via the configuration. 
Processor learning and certain processor specific properties can be changed after initialization. 
This includes the option to switch between parallel (default) and sequential processing for the entire network and/or individual processors. 

**Example implementation**  
~~~~
// Create the configuration
NetworkConfig config = new NetworkConfig();
config.directory = "directoryName";
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
// Check the configuration
Str err = config.check();
// Create the network
Network network = new Network();
network.initialize(config,true);
// Turn on step 1 accuracy logging in the classifier
network.setProcessorProperty("CL", "logPredictionAccuracy", true);
// Use the network
NetworkIO io = new NetworkIO();
io.setValue("input1", 1);
network.processIO(io);
// Get the average classifier accuracy
float accuracy = io.getAverageClassifierAccuracy(false);
// Turn of learning for all processors
network.setProcessorLearn("*", false);
// Save the network data to the configured directory
network.save();
~~~~

Class references;  
 * [TestNetwork](https://github.com/DyzLecticus/Zeesoft/blob/master/V5.0/ZDK/src/nl/zeesoft/zdk/test/neural/TestNetwork.java)
 * [Network](https://github.com/DyzLecticus/Zeesoft/blob/master/V5.0/ZDK/src/nl/zeesoft/zdk/neural/network/Network.java)
 * [Processor](https://github.com/DyzLecticus/Zeesoft/blob/master/V5.0/ZDK/src/nl/zeesoft/zdk/neural/processors/Processor.java)
 * [NetworkConfig](https://github.com/DyzLecticus/Zeesoft/blob/master/V5.0/ZDK/src/nl/zeesoft/zdk/neural/network/NetworkConfig.java)
 * [NetworkIO](https://github.com/DyzLecticus/Zeesoft/blob/master/V5.0/ZDK/src/nl/zeesoft/zdk/neural/network/NetworkIO.java)
 * [KeyValueSDR](https://github.com/DyzLecticus/Zeesoft/blob/master/V5.0/ZDK/src/nl/zeesoft/zdk/neural/KeyValueSDR.java)

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

2021-01-16 17:28:41:262 nl.zeesoft.zdk.neural.network.Network: Initializing network ...
2021-01-16 17:28:41:272 nl.zeesoft.zdk.neural.processors.TemporalMemory: Initializing TemporalMemory ...
2021-01-16 17:28:41:272 nl.zeesoft.zdk.neural.processors.Classifier: Initializing Classifier ...
2021-01-16 17:28:41:272 nl.zeesoft.zdk.neural.processors.ScalarEncoder: Initializing ScalarEncoder ...
2021-01-16 17:28:41:275 nl.zeesoft.zdk.neural.processors.TemporalMemory: Initialized
2021-01-16 17:28:41:273 nl.zeesoft.zdk.neural.processors.SpatialPooler: Initializing SpatialPooler ...
2021-01-16 17:28:41:273 nl.zeesoft.zdk.neural.processors.Classifier: Initialized
2021-01-16 17:28:41:311 nl.zeesoft.zdk.neural.processors.ScalarEncoder: Initialized
2021-01-16 17:28:41:311 nl.zeesoft.zdk.neural.processors.TemporalMemory: Resetting TemporalMemory connections ...
2021-01-16 17:28:41:313 nl.zeesoft.zdk.neural.processors.TemporalMemory: Reset connections
2021-01-16 17:28:41:335 nl.zeesoft.zdk.neural.processors.SpatialPooler: Initialized
2021-01-16 17:28:41:335 nl.zeesoft.zdk.neural.processors.SpatialPooler: Resetting SpatialPooler connections ...
2021-01-16 17:28:41:366 nl.zeesoft.zdk.neural.processors.SpatialPooler: Reset connections
2021-01-16 17:28:41:367 nl.zeesoft.zdk.neural.network.Network: Initialized network

2021-01-16 17:28:41:367 nl.zeesoft.zdk.test.neural.TestNetwork: Processing 100 SDRs ...
2021-01-16 17:28:44:479 nl.zeesoft.zdk.test.neural.TestNetwork: Processed 100 SDRs

Statistics;
- Cells             : 36864
- Proximal segments : 2304
- Proximal synapses : 464088 (active: 241989)
- Distal segments   : 1462
- Distal synapses   : 38495 (active: 3375)

2021-01-16 17:28:44:713 nl.zeesoft.zdk.neural.network.Network: Saving network ...
2021-01-16 17:28:44:715 nl.zeesoft.zdk.neural.network.Network$7: Writing dist/Configuration.txt ...
2021-01-16 17:28:44:715 nl.zeesoft.zdk.neural.network.Network$8: Writing dist/EN.txt ...
2021-01-16 17:28:44:715 nl.zeesoft.zdk.neural.network.Network$8: Writing dist/CL.txt ...
2021-01-16 17:28:44:721 nl.zeesoft.zdk.neural.network.Network$8: Writing dist/TM.txt ...
2021-01-16 17:28:44:747 nl.zeesoft.zdk.neural.network.Network$8: Writing dist/SP.txt ...
2021-01-16 17:28:44:763 nl.zeesoft.zdk.neural.network.Network$9: Writing dist/PreviousIO.txt ...
2021-01-16 17:28:45:295 nl.zeesoft.zdk.neural.network.Network: Saved network

Processor: EN
-> 16;16;1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16
-> SDR##1;1;@value#java.lang.Integer#1
Processor: SP
-> 48;48;1337,962,1383,570,12,2041,390,1088,809,818,695,788,751,60,1451,146,824,974,1703,1948,1187,1718,404,79,172,1619,934,1032,480,160,1143,903,1939,658,309,547,388,986,523,117,621,1176,545,831,1129,1162
Processor: TM
-> 768;48;1872,1873,1874,1875,1876,1877,1878,1879,1880,1881,1882,1883,1884,1885,1886,1887,4944,4945,4946,4947,4948,4949,4950,4951,4952,4953,4954,4955,4956,4957,4958,4959,8720,8721,8722,8723,8724,8725,8726,8727,8728,8729,8730,8731,8732,8733,8734,8735,9936,9937,9938,9939,9940,9941,9942,9943,9944,9945,9946,9947,9948,9949,9950,9951,12944,12945,12946,12947,12948,12949,12950,12951,12952,12953,12954,12955,12956,12957,12958,12959,2348,1264,1265,1266,1267,1268,1269,1270,1271,1272,1273,1274,1275,1276,1277,1278,1279,6249,18064,18065,18066,18067,18068,18069,18070,18071,18072,18073,18074,18075,18076,18077,18078,18079,9120,9121,9122,9123,9124,9125,9126,9127,9128,9129,9130,9131,9132,9133,9134,9135,10528,10529,10530,10531,10532,10533,10534,10535,10536,10537,10538,10539,10540,10541,10542,10543,21392,21393,21394,21395,21396,21397,21398,21399,21400,21401,21402,21403,21404,21405,21406,21407,13094,14944,14945,14946,14947,14948,14949,14950,14951,14952,14953,14954,14955,14956,14957,14958,14959,8368,8369,8370,8371,8372,8373,8374,8375,8376,8377,8378,8379,8380,8381,8382,8383,15392,15393,15394,15395,15396,15397,15398,15399,15400,15401,15402,15403,15404,15405,15406,15407,8752,8753,8754,8755,8756,8757,8758,8759,8760,8761,8762,8763,8764,8765,8766,8767,15584,15585,15586,15587,15588,15589,15590,15591,15592,15593,15594,15595,15596,15597,15598,15599,192,193,194,195,196,197,198,199,200,201,202,203,204,205,206,207,11125,15776,15777,15778,15779,15780,15781,15782,15783,15784,15785,15786,15787,15788,15789,15790,15791,12016,12017,12018,12019,12020,12021,12022,12023,12024,12025,12026,12027,12028,12029,12030,12031,960,961,962,963,964,965,966,967,968,969,970,971,972,973,974,975,13296,13297,13298,13299,13300,13301,13302,13303,13304,13305,13306,13307,13308,13309,13310,13311,2560,2561,2562,2563,2564,2565,2566,2567,2568,2569,2570,2571,2572,2573,2574,2575,32656,32657,32658,32659,32660,32661,32662,32663,32664,32665,32666,32667,32668,32669,32670,32671,2752,2753,2754,2755,2756,2757,2758,2759,2760,2761,2762,2763,2764,2765,2766,2767,18592,18593,18594,18595,18596,18597,18598,18599,18600,18601,18602,18603,18604,18605,18606,18607,14448,14449,14450,14451,14452,14453,14454,14455,14456,14457,14458,14459,14460,14461,14462,14463,6208,6209,6210,6211,6212,6213,6214,6215,6216,6217,6218,6219,6220,6221,6222,6223,6464,6465,6466,6467,6468,6469,6470,6471,6472,6473,6474,6475,6476,6477,6478,6479,18288,18289,18290,18291,18292,18293,18294,18295,18296,18297,18298,18299,18300,18301,18302,18303,7680,7681,7682,7683,7684,7685,7686,7687,7688,7689,7690,7691,7692,7693,7694,7695,18992,18993,18994,18995,18996,18997,18998,18999,19000,19001,19002,19003,19004,19005,19006,19007,27488,27489,27490,27491,27492,27493,27494,27495,27496,27497,27498,27499,27500,27501,27502,27503,12620,22128,22129,22130,22131,22132,22133,22134,22135,22136,22137,22138,22139,22140,22141,22142,22143,13197,23224,16512,16513,16514,16515,16516,16517,16518,16519,16520,16521,16522,16523,16524,16525,16526,16527,25904,25905,25906,25907,25908,25909,25910,25911,25912,25913,25914,25915,25916,25917,25918,25919,27248,27249,27250,27251,27252,27253,27254,27255,27256,27257,27258,27259,27260,27261,27262,27263,17408,17409,17410,17411,17412,17413,17414,17415,17416,17417,17418,17419,17420,17421,17422,17423,18816,18817,18818,18819,18820,18821,18822,18823,18824,18825,18826,18827,18828,18829,18830,18831,31024,31025,31026,31027,31028,31029,31030,31031,31032,31033,31034,31035,31036,31037,31038,31039,31168,31169,31170,31171,31172,31173,31174,31175,31176,31177,31178,31179,31180,31181,31182,31183
-> 48;48;12,60,79,117,160,172,309,388,404,480,523,545,547,570,621,658,751,809,831,903,934,962,974,986,1032,1088,1129,1143,1162,1176,1187,1337,1383,1619,1703,1718,1939,1948,2041
-> 768;48;843,3993,4237,6561,5490,6921,7177,7459,7864,8520,10114,10169,13847,15763,15937,16272,13183,13628,18833,15098,21037,24196,23588,28118,27239,29550
-> 768;48;1887,4949,8730,9936,12957,2348,6249,18065,1277,9132,10535,21402,13094,14951,8379,15406,8754,15589,11125,195,15791,12029,965,13308,2572,32666,18598,2752,14460,6223,6479,18297,7686,18999,27499,12620,13197,22130,23224,25912,16518,27254,17422,18825,31027,31180
Processor: CL
-> SDR##1;1;@accuracy#java.lang.Float#1.0@accuracyTrend#java.lang.Float#1.0@classification:1#nl.zeesoft.zdk.neural.processors.Classification#1;value;java.lang.Integer;0,2664%1,514
~~~~

Test results
------------
All 15 tests have been executed successfully (287 assertions).  
Total test duration: 53615 ms (total sleep duration: 176 ms).  

Memory usage and duration per test;  
 * nl.zeesoft.zdk.test.TestStr: 685 Kb / 0 Mb, 0 ms
 * nl.zeesoft.zdk.test.thread.TestRunCode: 437 Kb / 0 Mb, 30 ms
 * nl.zeesoft.zdk.test.thread.TestCodeRunnerChain: 439 Kb / 0 Mb, 137 ms
 * nl.zeesoft.zdk.test.collection.TestCollections: 466 Kb / 0 Mb, 165 ms
 * nl.zeesoft.zdk.test.http.TestHttpServer: 1680 Kb / 1 Mb, 318 ms
 * nl.zeesoft.zdk.test.grid.TestGrid: 1614 Kb / 1 Mb, 15 ms
 * nl.zeesoft.zdk.test.neural.TestSDR: 1633 Kb / 1 Mb, 0 ms
 * nl.zeesoft.zdk.test.neural.TestCellGrid: 1693 Kb / 1 Mb, 16 ms
 * nl.zeesoft.zdk.test.neural.TestScalarEncoder: 1693 Kb / 1 Mb, 6 ms
 * nl.zeesoft.zdk.test.neural.TestSpatialPooler: 1691 Kb / 1 Mb, 9690 ms
 * nl.zeesoft.zdk.test.neural.TestTemporalMemory: 1702 Kb / 1 Mb, 4104 ms
 * nl.zeesoft.zdk.test.neural.TestClassifier: 1714 Kb / 1 Mb, 127 ms
 * nl.zeesoft.zdk.test.neural.TestMerger: 1724 Kb / 1 Mb, 6 ms
 * nl.zeesoft.zdk.test.neural.TestProcessorFactory: 1725 Kb / 1 Mb, 30245 ms
 * nl.zeesoft.zdk.test.neural.TestNetwork: 1762 Kb / 1 Mb, 8273 ms
