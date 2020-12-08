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
2020-12-08 08:14:53:861 nl.zeesoft.zdk.http.HttpServer: Opening server socket on port: 8080 ...
2020-12-08 08:14:53:866 nl.zeesoft.zdk.http.HttpServer: Opened server socket on port: 8080
2020-12-08 08:14:53:885 nl.zeesoft.zdk.http.HttpServer$1: Request/response headers (Port: 8080);
<<<
GET / HTTP/1.1
Content-Type: text/html
Host: 127.0.0.1
Connection: keep-alive
>>>
HTTP/1.1 200 OK
Content-Length: 47
2020-12-08 08:14:53:888 nl.zeesoft.zdk.http.HttpServer$1: Request/response headers (Port: 8080);
<<<
GET /pizza.txt HTTP/1.1
Content-Type: text/html
Host: 127.0.0.1
Connection: keep-alive
>>>
HTTP/1.1 404 File not found: http/pizza.txt
Content-Length: 30
2020-12-08 08:14:53:890 nl.zeesoft.zdk.http.HttpServer$1: Request/response headers (Port: 8080);
<<<
PUT /pizza.txt HTTP/1.1
Content-Type: text/html
Host: 127.0.0.1
Connection: keep-alive
Content-Length: 13
>>>
HTTP/1.1 200 OK
2020-12-08 08:14:53:892 nl.zeesoft.zdk.http.HttpServer$1: Request/response headers (Port: 8080);
<<<
GET /pizza.txt HTTP/1.1
Content-Type: text/html
Host: 127.0.0.1
Connection: keep-alive
>>>
HTTP/1.1 200 OK
Content-Length: 13
2020-12-08 08:14:53:894 nl.zeesoft.zdk.http.HttpServer$1: Request/response headers (Port: 8080);
<<<
DELETE /pizza.txt HTTP/1.1
Content-Type: text/html
Host: 127.0.0.1
Connection: keep-alive
>>>
HTTP/1.1 200 OK
2020-12-08 08:14:53:895 nl.zeesoft.zdk.http.HttpServer: Opening server socket on port: 9090 ...
2020-12-08 08:14:53:896 nl.zeesoft.zdk.http.HttpServer: Opened server socket on port: 9090
2020-12-08 08:14:53:898 nl.zeesoft.zdk.http.HttpServer$1: Request/response headers (Port: 8080);
<<<
GET / HTTP/1.1
Host: 127.0.0.1:8080
Content-Type: text/html
Connection: keep-alive
>>>
HTTP/1.1 200 OK
Content-Length: 47
2020-12-08 08:14:53:900 nl.zeesoft.zdk.http.HttpServer$1: Request/response headers (Port: 9090);
<<<
GET / HTTP/1.1
Host: 127.0.0.1:8080
Content-Type: text/html
Connection: keep-alive
>>>
HTTP/1.1 200 OK
Content-Length: 47
2020-12-08 08:14:53:911 nl.zeesoft.zdk.http.HttpServer: Closing connections ...
2020-12-08 08:14:53:911 nl.zeesoft.zdk.http.HttpServer: Closed connections
2020-12-08 08:14:53:911 nl.zeesoft.zdk.http.HttpServer: Closing server socket on port: 9090 ...
2020-12-08 08:14:53:912 nl.zeesoft.zdk.http.HttpServer: Closed server socket on port: 9090
2020-12-08 08:14:53:922 nl.zeesoft.zdk.http.HttpServer: Closing connections ...
2020-12-08 08:14:53:922 nl.zeesoft.zdk.http.HttpServer: Closed connections
2020-12-08 08:14:53:922 nl.zeesoft.zdk.http.HttpServer: Closing server socket on port: 8080 ...
2020-12-08 08:14:53:922 nl.zeesoft.zdk.http.HttpServer: Closed server socket on port: 8080

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
2020-12-08 08:14:54:108 nl.zeesoft.zdk.neural.processors.ScalarEncoder: Initializing ScalarEncoder ...
2020-12-08 08:14:54:109 nl.zeesoft.zdk.neural.processors.ScalarEncoder: Initialized
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
2020-12-08 08:14:54:164 nl.zeesoft.zdk.neural.processors.SpatialPooler: Initializing SpatialPooler ...
2020-12-08 08:14:54:239 nl.zeesoft.zdk.neural.processors.SpatialPooler: Initialized
2020-12-08 08:14:54:239 nl.zeesoft.zdk.neural.processors.SpatialPooler: Resetting SpatialPooler connections ...
2020-12-08 08:14:54:358 nl.zeesoft.zdk.neural.processors.SpatialPooler: Reset connections

2020-12-08 08:14:54:359 nl.zeesoft.zdk.test.neural.TestSpatialPooler: Initializing spatial pooler (asynchronous) ...
2020-12-08 08:14:54:806 nl.zeesoft.zdk.test.neural.TestSpatialPooler: Initialized spatial pooler (asynchronous)

Input SDR: 16;16;0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15
Output SDR: 48;48;28,81,178,225,233,274,332,361,390,422,428,461,462,465,554,558,603,613,664,731,746,828,940,1008,1020,1025,1051,1070,1084,1095,1118,1128,1144,1188,1189,1260,1280,1459,1526,1617,1658,1807,1897,1941,1944,2129
Input SDR: 16;16;0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15
Output SDR: 48;48;28,178,233,274,332,361,390,461,462,554,558,603,613,664,731,746,828,895,927,1008,1020,1025,1051,1070,1084,1095,1118,1128,1134,1144,1188,1189,1260,1268,1283,1459,1478,1526,1617,1658,1807,1897,1898,1904,1941,1944
Input SDR: 16;16;0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15
Output SDR: 48;48;28,178,233,274,332,361,390,422,461,462,554,558,603,613,664,731,746,828,895,927,1008,1020,1025,1051,1070,1084,1095,1118,1128,1134,1144,1188,1189,1260,1459,1478,1526,1617,1658,1807,1897,1898,1904,1941,1944,2129
Input SDR: 16;16;0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15
Output SDR: 48;48;28,178,233,274,332,361,390,422,461,462,554,558,603,613,664,731,746,828,895,927,1008,1020,1025,1051,1070,1084,1095,1118,1128,1134,1144,1188,1189,1260,1268,1459,1478,1526,1617,1658,1807,1897,1898,1904,1941,1944
Input SDR: 16;16;0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15
Output SDR: 48;48;28,233,274,332,361,390,422,461,462,554,558,603,613,664,731,746,828,895,927,940,1008,1025,1051,1070,1084,1095,1118,1128,1134,1144,1188,1189,1260,1268,1280,1459,1478,1526,1617,1658,1807,1897,1898,1904,1941,1944

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
2020-12-08 08:15:03:944 nl.zeesoft.zdk.neural.processors.TemporalMemory: Initializing TemporalMemory ...
2020-12-08 08:15:03:947 nl.zeesoft.zdk.neural.processors.TemporalMemory: Initialized
2020-12-08 08:15:03:947 nl.zeesoft.zdk.neural.processors.TemporalMemory: Resetting TemporalMemory connections ...
2020-12-08 08:15:03:956 nl.zeesoft.zdk.neural.processors.TemporalMemory: Reset connections
2020-12-08 08:15:03:994 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 1 > bursting: 46, active: 736, winners: 46, predictive: 0
2020-12-08 08:15:04:024 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 2 > bursting: 46, active: 736, winners: 46, predictive: 0
2020-12-08 08:15:04:050 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 3 > bursting: 46, active: 736, winners: 46, predictive: 0
2020-12-08 08:15:04:064 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 4 > bursting: 46, active: 736, winners: 46, predictive: 0
2020-12-08 08:15:04:089 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 5 > bursting: 46, active: 736, winners: 46, predictive: 41
2020-12-08 08:15:04:099 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 6 > bursting: 5, active: 121, winners: 46, predictive: 39
2020-12-08 08:15:04:108 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 7 > bursting: 7, active: 151, winners: 46, predictive: 44
2020-12-08 08:15:04:127 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 8 > bursting: 2, active: 76, winners: 46, predictive: 42
2020-12-08 08:15:04:135 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 9 > bursting: 4, active: 106, winners: 46, predictive: 5
2020-12-08 08:15:04:151 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 10 > bursting: 41, active: 661, winners: 46, predictive: 46
2020-12-08 08:15:04:158 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 11 > bursting: 0, active: 46, winners: 46, predictive: 46
2020-12-08 08:15:04:166 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 12 > bursting: 0, active: 46, winners: 46, predictive: 45
2020-12-08 08:15:04:172 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 13 > bursting: 1, active: 61, winners: 46, predictive: 42
2020-12-08 08:15:04:187 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 14 > bursting: 4, active: 106, winners: 46, predictive: 0
2020-12-08 08:15:04:201 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 15 > bursting: 46, active: 736, winners: 46, predictive: 46
2020-12-08 08:15:04:208 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 16 > bursting: 0, active: 46, winners: 46, predictive: 46
2020-12-08 08:15:04:215 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 17 > bursting: 0, active: 46, winners: 46, predictive: 46
2020-12-08 08:15:04:223 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 18 > bursting: 0, active: 46, winners: 46, predictive: 42
2020-12-08 08:15:04:231 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 19 > bursting: 4, active: 106, winners: 46, predictive: 0
2020-12-08 08:15:04:242 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 20 > bursting: 46, active: 736, winners: 46, predictive: 46
2020-12-08 08:15:04:248 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 21 > bursting: 0, active: 46, winners: 46, predictive: 46
2020-12-08 08:15:04:265 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 22 > bursting: 0, active: 46, winners: 46, predictive: 46
2020-12-08 08:15:04:272 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 23 > bursting: 0, active: 46, winners: 46, predictive: 38
2020-12-08 08:15:04:280 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 24 > bursting: 8, active: 166, winners: 46, predictive: 0
2020-12-08 08:15:04:296 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 25 > bursting: 46, active: 736, winners: 46, predictive: 87
2020-12-08 08:15:04:306 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 26 > bursting: 0, active: 46, winners: 46, predictive: 29
2020-12-08 08:15:04:315 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 27 > bursting: 17, active: 301, winners: 46, predictive: 46
2020-12-08 08:15:04:321 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 28 > bursting: 0, active: 46, winners: 46, predictive: 46
2020-12-08 08:15:04:327 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 29 > bursting: 0, active: 46, winners: 46, predictive: 46
2020-12-08 08:15:04:341 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 30 > bursting: 0, active: 46, winners: 46, predictive: 46
2020-12-08 08:15:04:346 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 31 > bursting: 0, active: 46, winners: 46, predictive: 45
2020-12-08 08:15:04:351 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 32 > bursting: 1, active: 61, winners: 46, predictive: 39
2020-12-08 08:15:04:356 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 33 > bursting: 7, active: 151, winners: 46, predictive: 0
2020-12-08 08:15:04:370 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 34 > bursting: 46, active: 736, winners: 46, predictive: 92
2020-12-08 08:15:04:376 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 35 > bursting: 0, active: 46, winners: 46, predictive: 3
2020-12-08 08:15:04:390 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 36 > bursting: 43, active: 691, winners: 46, predictive: 91
2020-12-08 08:15:04:396 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 37 > bursting: 0, active: 46, winners: 46, predictive: 19
2020-12-08 08:15:04:408 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 38 > bursting: 27, active: 451, winners: 46, predictive: 57
2020-12-08 08:15:04:424 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 39 > bursting: 2, active: 76, winners: 46, predictive: 47
2020-12-08 08:15:04:431 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 40 > bursting: 0, active: 46, winners: 46, predictive: 46
2020-12-08 08:15:04:438 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 41 > bursting: 0, active: 46, winners: 46, predictive: 47
2020-12-08 08:15:04:444 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 42 > bursting: 0, active: 46, winners: 46, predictive: 46
2020-12-08 08:15:04:450 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 43 > bursting: 0, active: 46, winners: 46, predictive: 46
2020-12-08 08:15:04:455 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 44 > bursting: 0, active: 46, winners: 46, predictive: 45
2020-12-08 08:15:04:461 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 45 > bursting: 1, active: 61, winners: 46, predictive: 45
2020-12-08 08:15:04:473 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 46 > bursting: 3, active: 91, winners: 46, predictive: 0
2020-12-08 08:15:04:506 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 47 > bursting: 46, active: 736, winners: 46, predictive: 98
2020-12-08 08:15:04:514 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 48 > bursting: 0, active: 46, winners: 46, predictive: 7
2020-12-08 08:15:04:533 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 49 > bursting: 39, active: 631, winners: 46, predictive: 136
2020-12-08 08:15:04:542 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 50 > bursting: 0, active: 46, winners: 46, predictive: 6
2020-12-08 08:15:04:562 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 51 > bursting: 40, active: 646, winners: 46, predictive: 98
2020-12-08 08:15:04:572 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 52 > bursting: 0, active: 46, winners: 46, predictive: 35
2020-12-08 08:15:04:583 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 53 > bursting: 12, active: 226, winners: 46, predictive: 83
2020-12-08 08:15:04:592 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 54 > bursting: 0, active: 46, winners: 46, predictive: 30
2020-12-08 08:15:04:616 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 55 > bursting: 16, active: 286, winners: 46, predictive: 75
2020-12-08 08:15:04:623 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 56 > bursting: 0, active: 46, winners: 46, predictive: 57
2020-12-08 08:15:04:629 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 57 > bursting: 1, active: 61, winners: 46, predictive: 48
2020-12-08 08:15:04:635 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 58 > bursting: 0, active: 46, winners: 46, predictive: 47
2020-12-08 08:15:04:643 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 59 > bursting: 1, active: 61, winners: 46, predictive: 0
2020-12-08 08:15:04:662 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 60 > bursting: 46, active: 736, winners: 46, predictive: 94
2020-12-08 08:15:04:667 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 61 > bursting: 0, active: 46, winners: 46, predictive: 71
2020-12-08 08:15:04:673 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 62 > bursting: 3, active: 91, winners: 46, predictive: 62
2020-12-08 08:15:04:679 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 63 > bursting: 5, active: 121, winners: 46, predictive: 51
2020-12-08 08:15:04:695 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 64 > bursting: 8, active: 166, winners: 46, predictive: 38
2020-12-08 08:15:04:703 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 65 > bursting: 14, active: 256, winners: 46, predictive: 92
2020-12-08 08:15:04:710 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 66 > bursting: 0, active: 46, winners: 46, predictive: 65
2020-12-08 08:15:04:716 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 67 > bursting: 5, active: 121, winners: 46, predictive: 72
2020-12-08 08:15:04:721 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 68 > bursting: 0, active: 46, winners: 46, predictive: 38
2020-12-08 08:15:04:728 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 69 > bursting: 11, active: 211, winners: 46, predictive: 95
2020-12-08 08:15:04:733 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 70 > bursting: 0, active: 46, winners: 46, predictive: 71
2020-12-08 08:15:04:738 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 71 > bursting: 0, active: 46, winners: 46, predictive: 73
2020-12-08 08:15:04:752 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 72 > bursting: 0, active: 46, winners: 46, predictive: 57
2020-12-08 08:15:04:757 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 73 > bursting: 0, active: 46, winners: 46, predictive: 66
2020-12-08 08:15:04:762 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 74 > bursting: 0, active: 46, winners: 46, predictive: 78
2020-12-08 08:15:04:944 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 100 > bursting: 0, active: 46, winners: 46, predictive: 63
2020-12-08 08:15:05:250 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 150 > bursting: 0, active: 46, winners: 46, predictive: 100
2020-12-08 08:15:05:537 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 200 > bursting: 0, active: 46, winners: 46, predictive: 63
2020-12-08 08:15:05:938 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 250 > bursting: 0, active: 46, winners: 46, predictive: 104
2020-12-08 08:15:06:231 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 300 > bursting: 0, active: 46, winners: 46, predictive: 63
2020-12-08 08:15:06:501 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 350 > bursting: 0, active: 46, winners: 46, predictive: 106
2020-12-08 08:15:06:777 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 400 > bursting: 0, active: 46, winners: 46, predictive: 63
2020-12-08 08:15:07:045 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 450 > bursting: 0, active: 46, winners: 46, predictive: 107
2020-12-08 08:15:07:306 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 500 > bursting: 0, active: 46, winners: 46, predictive: 63
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
2020-12-08 08:15:07:559 nl.zeesoft.zdk.neural.processors.Classifier: Initializing Classifier ...
2020-12-08 08:15:07:560 nl.zeesoft.zdk.neural.processors.Classifier: Initialized

2020-12-08 08:15:07:595 nl.zeesoft.zdk.neural.processors.ClassifierStep: Dividing step 1 classifier value counts by two ...
2020-12-08 08:15:07:743 nl.zeesoft.zdk.neural.processors.ClassifierStep: Dividing step 1 classifier value counts by two ...
2020-12-08 08:15:07:808 nl.zeesoft.zdk.neural.processors.ClassifierStep: Dividing step 1 classifier value counts by two ...

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
2020-12-08 08:15:07:886 nl.zeesoft.zdk.neural.processors.Merger: Initializing Merger ...
2020-12-08 08:15:07:886 nl.zeesoft.zdk.neural.processors.Merger: Initialized

Merged and distorted;
11111100
00000000
00001000
01000000
00000000
00010000
00000100
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
2020-12-08 08:15:07:963 nl.zeesoft.zdk.neural.processors.SpatialPooler: Initializing SpatialPooler ...
2020-12-08 08:15:08:009 nl.zeesoft.zdk.neural.processors.SpatialPooler: Initialized
2020-12-08 08:15:08:009 nl.zeesoft.zdk.neural.processors.SpatialPooler: Resetting SpatialPooler connections ...
2020-12-08 08:15:08:040 nl.zeesoft.zdk.neural.processors.SpatialPooler: Reset connections
2020-12-08 08:15:08:041 nl.zeesoft.zdk.neural.processors.TemporalMemory: Initializing TemporalMemory ...
2020-12-08 08:15:08:043 nl.zeesoft.zdk.neural.processors.TemporalMemory: Initialized
2020-12-08 08:15:08:043 nl.zeesoft.zdk.neural.processors.Classifier: Initializing Classifier ...
2020-12-08 08:15:08:044 nl.zeesoft.zdk.neural.processors.Classifier: Initialized

Processing ...
2020-12-08 08:15:09:745 nl.zeesoft.zdk.test.neural.TestProcessorFactory: 768;48;64,65,66,67,68,69,70,71,72,73,74,75,76,77,78,79,1024,1025,1026,1027,1028,1029,1030,1031,1032,1033,1034,1035,1036,1037,1038,1039,1488,1489,1490,1491,1492,1493,1494,1495,1496,1497,1498,1499,1500,1501,1502,1503,1776,1777,1778,1779,1780,1781,1782,1783,1784,1785,1786,1787,1788,1789,1790,1791,2432,2433,2434,2435,2436,2437,2438,2439,2440,2441,2442,2443,2444,2445,2446,2447,2704,2705,2706,2707,2708,2709,2710,2711,2712,2713,2714,2715,2716,2717,2718,2719,3168,3169,3170,3171,3172,3173,3174,3175,3176,3177,3178,3179,3180,3181,3182,3183,4144,4145,4146,4147,4148,4149,4150,4151,4152,4153,4154,4155,4156,4157,4158,4159,4432,4433,4434,4435,4436,4437,4438,4439,4440,4441,4442,4443,4444,4445,4446,4447,4480,4481,4482,4483,4484,4485,4486,4487,4488,4489,4490,4491,4492,4493,4494,4495,4924,6016,6017,6018,6019,6020,6021,6022,6023,6024,6025,6026,6027,6028,6029,6030,6031,6128,6129,6130,6131,6132,6133,6134,6135,6136,6137,6138,6139,6140,6141,6142,6143,7920,7921,7922,7923,7924,7925,7926,7927,7928,7929,7930,7931,7932,7933,7934,7935,8688,8689,8690,8691,8692,8693,8694,8695,8696,8697,8698,8699,8700,8701,8702,8703,8704,8705,8706,8707,8708,8709,8710,8711,8712,8713,8714,8715,8716,8717,8718,8719,8816,8817,8818,8819,8820,8821,8822,8823,8824,8825,8826,8827,8828,8829,8830,8831,9280,9281,9282,9283,9284,9285,9286,9287,9288,9289,9290,9291,9292,9293,9294,9295,9600,9601,9602,9603,9604,9605,9606,9607,9608,9609,9610,9611,9612,9613,9614,9615,10016,10017,10018,10019,10020,10021,10022,10023,10024,10025,10026,10027,10028,10029,10030,10031,10064,10065,10066,10067,10068,10069,10070,10071,10072,10073,10074,10075,10076,10077,10078,10079,11936,11937,11938,11939,11940,11941,11942,11943,11944,11945,11946,11947,11948,11949,11950,11951,12864,12865,12866,12867,12868,12869,12870,12871,12872,12873,12874,12875,12876,12877,12878,12879,12976,12977,12978,12979,12980,12981,12982,12983,12984,12985,12986,12987,12988,12989,12990,12991,14720,14721,14722,14723,14724,14725,14726,14727,14728,14729,14730,14731,14732,14733,14734,14735,16014,16048,16049,16050,16051,16052,16053,16054,16055,16056,16057,16058,16059,16060,16061,16062,16063,16457,16912,16913,16914,16915,16916,16917,16918,16919,16920,16921,16922,16923,16924,16925,16926,16927,17056,17057,17058,17059,17060,17061,17062,17063,17064,17065,17066,17067,17068,17069,17070,17071,18800,18801,18802,18803,18804,18805,18806,18807,18808,18809,18810,18811,18812,18813,18814,18815,19552,19553,19554,19555,19556,19557,19558,19559,19560,19561,19562,19563,19564,19565,19566,19567,20634,22112,22113,22114,22115,22116,22117,22118,22119,22120,22121,22122,22123,22124,22125,22126,22127,22588,22651,23520,23521,23522,23523,23524,23525,23526,23527,23528,23529,23530,23531,23532,23533,23534,23535,23632,23633,23634,23635,23636,23637,23638,23639,23640,23641,23642,23643,23644,23645,23646,23647,24864,24865,24866,24867,24868,24869,24870,24871,24872,24873,24874,24875,24876,24877,24878,24879,25712,25713,25714,25715,25716,25717,25718,25719,25720,25721,25722,25723,25724,25725,25726,25727,25872,25873,25874,25875,25876,25877,25878,25879,25880,25881,25882,25883,25884,25885,25886,25887,27328,27329,27330,27331,27332,27333,27334,27335,27336,27337,27338,27339,27340,27341,27342,27343,28768,28769,28770,28771,28772,28773,28774,28775,28776,28777,28778,28779,28780,28781,28782,28783,28911,29536,29537,29538,29539,29540,29541,29542,29543,29544,29545,29546,29547,29548,29549,29550,29551,29606
2020-12-08 08:15:12:210 nl.zeesoft.zdk.test.neural.TestProcessorFactory: 768;48;77,538,1588,1783,1811,2445,3392,3393,3394,3395,3396,3397,3398,3399,3400,3401,3402,3403,3404,3405,3406,3407,3680,3681,3682,3683,3684,3685,3686,3687,3688,3689,3690,3691,3692,3693,3694,3695,4443,4485,4924,5120,5121,5122,5123,5124,5125,5126,5127,5128,5129,5130,5131,5132,5133,5134,5135,5632,5633,5634,5635,5636,5637,5638,5639,5640,5641,5642,5643,5644,5645,5646,5647,6016,6017,6018,6019,6020,6021,6022,6023,6024,6025,6026,6027,6028,6029,6030,6031,6133,7008,7009,7010,7011,7012,7013,7014,7015,7016,7017,7018,7019,7020,7021,7022,7023,7929,8694,8816,8817,8818,8819,8820,8821,8822,8823,8824,8825,8826,8827,8828,8829,8830,8831,9610,11949,12874,12983,13591,14725,17168,17169,17170,17171,17172,17173,17174,17175,17176,17177,17178,17179,17180,17181,17182,17183,17963,18094,18768,18769,18770,18771,18772,18773,18774,18775,18776,18777,18778,18779,18780,18781,18782,18783,19386,19562,22112,22113,22114,22115,22116,22117,22118,22119,22120,22121,22122,22123,22124,22125,22126,22127,23524,23551,23642,24001,24800,24801,24802,24803,24804,24805,24806,24807,24808,24809,24810,24811,24812,24813,24814,24815,25536,25537,25538,25539,25540,25541,25542,25543,25544,25545,25546,25547,25548,25549,25550,25551,25600,25601,25602,25603,25604,25605,25606,25607,25608,25609,25610,25611,25612,25613,25614,25615,25872,25873,25874,25875,25876,25877,25878,25879,25880,25881,25882,25883,25884,25885,25886,25887,27104,27105,27106,27107,27108,27109,27110,27111,27112,27113,27114,27115,27116,27117,27118,27119,27330,28115,28672,28673,28674,28675,28676,28677,28678,28679,28680,28681,28682,28683,28684,28685,28686,28687,28777,34832,34833,34834,34835,34836,34837,34838,34839,34840,34841,34842,34843,34844,34845,34846,34847
2020-12-08 08:15:15:100 nl.zeesoft.zdk.test.neural.TestProcessorFactory: 768;48;77,528,529,530,531,532,533,534,535,536,537,538,539,540,541,542,543,961,1783,2445,2704,2705,2706,2707,2708,2709,2710,2711,2712,2713,2714,2715,2716,2717,2718,2719,3120,3121,3122,3123,3124,3125,3126,3127,3128,3129,3130,3131,3132,3133,3134,3135,4114,4443,4544,4924,5644,6133,7016,7929,8032,8033,8034,8035,8036,8037,8038,8039,8040,8041,8042,8043,8044,8045,8046,8047,8704,8705,8706,8707,8708,8709,8710,8711,8712,8713,8714,8715,8716,8717,8718,8719,9293,10068,11949,12874,12983,14725,16449,16999,17024,17025,17026,17027,17028,17029,17030,17031,17032,17033,17034,17035,17036,17037,17038,17039,17176,17952,17953,17954,17955,17956,17957,17958,17959,17960,17961,17962,17963,17964,17965,17966,17967,18080,18081,18082,18083,18084,18085,18086,18087,18088,18089,18090,18091,18092,18093,18094,18095,19432,19494,19562,22114,22588,22651,22921,23524,25872,25873,25874,25875,25876,25877,25878,25879,25880,25881,25882,25883,25884,25885,25886,25887,27330,28082,28112,28113,28114,28115,28116,28117,28118,28119,28120,28121,28122,28123,28124,28125,28126,28127,28687,28804,29536,29537,29538,29539,29540,29541,29542,29543,29544,29545,29546,29547,29548,29549,29550,29551,29606,31807
2020-12-08 08:15:17:938 nl.zeesoft.zdk.test.neural.TestProcessorFactory: 768;48;77,1811,2445,3100,3123,3392,3393,3394,3395,3396,3397,3398,3399,3400,3401,3402,3403,3404,3405,3406,3407,4443,4485,4924,5564,6022,6133,6880,6881,6882,6883,6884,6885,6886,6887,6888,6889,6890,6891,6892,6893,6894,6895,8096,8097,8098,8099,8100,8101,8102,8103,8104,8105,8106,8107,8108,8109,8110,8111,9293,9610,10021,10068,11330,11949,12983,13591,14725,17024,17025,17026,17027,17028,17029,17030,17031,17032,17033,17034,17035,17036,17037,17038,17039,17168,17169,17170,17171,17172,17173,17174,17175,17176,17177,17178,17179,17180,17181,17182,17183,17552,17553,17554,17555,17556,17557,17558,17559,17560,17561,17562,17563,17564,17565,17566,17567,17945,18341,19432,19562,22114,22651,22784,22785,22786,22787,22788,22789,22790,22791,22792,22793,22794,22795,22796,22797,22798,22799,23371,23524,23551,23642,24001,24800,25545,25608,27106,27188,27330,33424,33425,33426,33427,33428,33429,33430,33431,33432,33433,33434,33435,33436,33437,33438,33439,34128,34129,34130,34131,34132,34133,34134,34135,34136,34137,34138,34139,34140,34141,34142,34143
2020-12-08 08:15:21:166 nl.zeesoft.zdk.test.neural.TestProcessorFactory: 768;48;77,352,353,354,355,356,357,358,359,360,361,362,363,364,365,366,367,512,513,514,515,516,517,518,519,520,521,522,523,524,525,526,527,704,705,706,707,708,709,710,711,712,713,714,715,716,717,718,719,1811,2445,3392,3393,3394,3395,3396,3397,3398,3399,3400,3401,3402,3403,3404,3405,3406,3407,3680,3681,3682,3683,3684,3685,3686,3687,3688,3689,3690,3691,3692,3693,3694,3695,3808,3809,3810,3811,3812,3813,3814,3815,3816,3817,3818,3819,3820,3821,3822,3823,4485,4544,4913,4972,6022,6617,7632,7633,7634,7635,7636,7637,7638,7639,7640,7641,7642,7643,7644,7645,7646,7647,8111,8749,8816,8817,8818,8819,8820,8821,8822,8823,8824,8825,8826,8827,8828,8829,8830,8831,9293,9610,9961,10021,11949,12256,12257,12258,12259,12260,12261,12262,12263,12264,12265,12266,12267,12268,12269,12270,12271,12874,12983,13974,14160,14161,14162,14163,14164,14165,14166,14167,14168,14169,14170,14171,14172,14173,14174,14175,15584,15585,15586,15587,15588,15589,15590,15591,15592,15593,15594,15595,15596,15597,15598,15599,19386,19562,20634,22651,23371,23524,23642,24001,24784,24785,24786,24787,24788,24789,24790,24791,24792,24793,24794,24795,24796,24797,24798,24799,25608,25824,25825,25826,25827,25828,25829,25830,25831,25832,25833,25834,25835,25836,25837,25838,25839,25881,26352,26353,26354,26355,26356,26357,26358,26359,26360,26361,26362,26363,26364,26365,26366,26367,28082,28646,28777
2020-12-08 08:15:24:366 nl.zeesoft.zdk.test.neural.TestProcessorFactory: 768;48;195,512,513,514,515,516,517,518,519,520,521,522,523,524,525,526,527,528,529,530,531,532,533,534,535,536,537,538,539,540,541,542,543,1783,2445,3168,3169,3170,3171,3172,3173,3174,3175,3176,3177,3178,3179,3180,3181,3182,3183,3405,3808,3809,3810,3811,3812,3813,3814,3815,3816,3817,3818,3819,3820,3821,3822,3823,4443,4924,4972,6022,6133,8111,8297,8712,8785,9293,9610,10021,10068,11330,11949,12874,12983,13591,17176,18979,19386,19562,22588,22651,22921,23524,23551,23642,24795,24800,24864,24865,24866,24867,24868,24869,24870,24871,24872,24873,24874,24875,24876,24877,24878,24879,25608,25881,26359,27104,27105,27106,27107,27108,27109,27110,27111,27112,27113,27114,27115,27116,27117,27118,27119,27188,27330,29600,29601,29602,29603,29604,29605,29606,29607,29608,29609,29610,29611,29612,29613,29614,29615
2020-12-08 08:15:27:677 nl.zeesoft.zdk.test.neural.TestProcessorFactory: 768;48;519,528,529,530,531,532,533,534,535,536,537,538,539,540,541,542,543,1783,1811,2445,3100,3169,3392,3393,3394,3395,3396,3397,3398,3399,3400,3401,3402,3403,3404,3405,3406,3407,3815,4443,4485,4913,4972,6022,6133,7016,7929,8111,8297,8694,8785,11330,11949,12874,12983,14725,15113,16999,17176,17945,18672,18673,18674,18675,18676,18677,18678,18679,18680,18681,18682,18683,18684,18685,18686,18687,18979,19386,19562,22114,22651,22798,22921,23524,23642,24001,24795,25545,26359,27330,28112,28113,28114,28115,28116,28117,28118,28119,28120,28121,28122,28123,28124,28125,28126,28127
2020-12-08 08:15:30:805 nl.zeesoft.zdk.test.neural.TestProcessorFactory: 768;48;77,299,356,512,513,514,515,516,517,518,519,520,521,522,523,524,525,526,527,528,529,530,531,532,533,534,535,536,537,538,539,540,541,542,543,706,798,1811,2445,3169,3405,3812,4913,4972,6617,6893,7016,7929,8111,8749,8785,9293,9610,9961,10021,10068,11330,11949,12983,13974,15113,15592,19562,22114,22651,22921,23371,23524,23642,24001,24795,25608,26359,28082,28115,28804
2020-12-08 08:15:34:105 nl.zeesoft.zdk.test.neural.TestProcessorFactory: 768;48;77,961,1588,1811,2445,3405,3812,4144,4145,4146,4147,4148,4149,4150,4151,4152,4153,4154,4155,4156,4157,4158,4159,4443,4485,4913,6022,6133,8297,8785,9293,9610,10021,10068,11330,11536,11537,11538,11539,11540,11541,11542,11543,11544,11545,11546,11547,11548,11549,11550,11551,11949,12983,13591,14725,17033,17176,17952,17953,17954,17955,17956,17957,17958,17959,17960,17961,17962,17963,17964,17965,17966,17967,18687,19432,19562,22114,22588,22651,23371,23524,23551,23642,24001,24795,25545,26359,27188,27330,28804,29606
Processing 1000 SDRs took: 29622 ms (29 ms/SDR)
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

2020-12-08 08:15:37:687 nl.zeesoft.zdk.neural.network.Network: Initializing network ...
2020-12-08 08:15:37:688 nl.zeesoft.zdk.neural.processors.SpatialPooler: Initializing SpatialPooler ...
2020-12-08 08:15:37:688 nl.zeesoft.zdk.neural.processors.TemporalMemory: Initializing TemporalMemory ...
2020-12-08 08:15:37:688 nl.zeesoft.zdk.neural.processors.ScalarEncoder: Initializing ScalarEncoder ...
2020-12-08 08:15:37:714 nl.zeesoft.zdk.neural.processors.SpatialPooler: Initialized
2020-12-08 08:15:37:693 nl.zeesoft.zdk.neural.processors.TemporalMemory: Initialized
2020-12-08 08:15:37:688 nl.zeesoft.zdk.neural.processors.Classifier: Initializing Classifier ...
2020-12-08 08:15:37:736 nl.zeesoft.zdk.neural.processors.ScalarEncoder: Initialized
2020-12-08 08:15:37:736 nl.zeesoft.zdk.neural.processors.TemporalMemory: Resetting TemporalMemory connections ...
2020-12-08 08:15:37:736 nl.zeesoft.zdk.neural.processors.SpatialPooler: Resetting SpatialPooler connections ...
2020-12-08 08:15:37:737 nl.zeesoft.zdk.neural.processors.Classifier: Initialized
2020-12-08 08:15:37:738 nl.zeesoft.zdk.neural.processors.TemporalMemory: Reset connections
2020-12-08 08:15:37:767 nl.zeesoft.zdk.neural.processors.SpatialPooler: Reset connections
2020-12-08 08:15:37:768 nl.zeesoft.zdk.neural.network.Network: Initialized network

2020-12-08 08:15:37:768 nl.zeesoft.zdk.test.neural.TestNetwork: Processing 100 SDRs ...
2020-12-08 08:15:40:859 nl.zeesoft.zdk.test.neural.TestNetwork: Processed 100 SDRs

Statistics;
- Cells             : 36864
- Proximal segments : 2304
- Proximal synapses : 464044 (active: 241265)
- Distal segments   : 1357
- Distal synapses   : 36396 (active: 3988)

2020-12-08 08:15:41:100 nl.zeesoft.zdk.neural.network.Network: Saving network ...
2020-12-08 08:15:41:100 nl.zeesoft.zdk.neural.network.Network$7: Writing dist/Configuration.txt ...
2020-12-08 08:15:41:100 nl.zeesoft.zdk.neural.network.Network$8: Writing dist/EN.txt ...
2020-12-08 08:15:41:100 nl.zeesoft.zdk.neural.network.Network$8: Writing dist/CL.txt ...
2020-12-08 08:15:41:101 nl.zeesoft.zdk.neural.network.Network$8: Writing dist/TM.txt ...
2020-12-08 08:15:41:129 nl.zeesoft.zdk.neural.network.Network$9: Writing dist/PreviousIO.txt ...
2020-12-08 08:15:41:133 nl.zeesoft.zdk.neural.network.Network$8: Writing dist/SP.txt ...
2020-12-08 08:15:41:747 nl.zeesoft.zdk.neural.network.Network: Saved network

Processor: EN
-> 16;16;1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16
-> SDR##1;1;@value#java.lang.Integer#1
Processor: SP
-> 48;48;1591,451,1801,688,1576,354,1665,1268,2089,150,1284,1652,1649,156,1350,1747,61,1550,1701,844,1695,81,911,1157,169,1370,18,444,204,1715,948,743,149,329,1433,382,1451,363,524,398,289,1173,1995,381,1850,989
Processor: TM
-> 768;48;2496,2497,2498,2499,2500,2501,2502,2503,2504,2505,2506,2507,2508,2509,2510,2511,976,977,978,979,980,981,982,983,984,985,986,987,988,989,990,991,3264,3265,3266,3267,3268,3269,3270,3271,3272,3273,3274,3275,3276,3277,3278,3279,1296,1297,1298,1299,1300,1301,1302,1303,1304,1305,1306,1307,1308,1309,1310,1311,2384,2385,2386,2387,2388,2389,2390,2391,2392,2393,2394,2395,2396,2397,2398,2399,7104,7105,7106,7107,7108,7109,7110,7111,7112,7113,7114,7115,7116,7117,7118,7119,2704,2705,2706,2707,2708,2709,2710,2711,2712,2713,2714,2715,2716,2717,2718,2719,8384,8385,8386,8387,8388,8389,8390,8391,8392,8393,8394,8395,8396,8397,8398,8399,4624,4625,4626,4627,4628,4629,4630,4631,4632,4633,4634,4635,4636,4637,4638,4639,5264,5265,5266,5267,5268,5269,5270,5271,5272,5273,5274,5275,5276,5277,5278,5279,11008,11009,11010,11011,11012,11013,11014,11015,11016,11017,11018,11019,11020,11021,11022,11023,6096,6097,6098,6099,6100,6101,6102,6103,6104,6105,6106,6107,6108,6109,6110,6111,13504,13505,13506,13507,13508,13509,13510,13511,13512,13513,13514,13515,13516,13517,13518,13519,15168,15169,15170,15171,15172,15173,15174,15175,15176,15177,15178,15179,15180,15181,15182,15183,20288,20289,20290,20291,20292,20293,20294,20295,20296,20297,20298,20299,20300,20301,20302,20303,15824,15825,15826,15827,15828,15829,15830,15831,15832,15833,15834,15835,15836,15837,15838,15839,20544,20545,20546,20547,20548,20549,20550,20551,20552,20553,20554,20555,20556,20557,20558,20559,18512,18513,18514,18515,18516,18517,18518,18519,18520,18521,18522,18523,18524,18525,18526,18527,18768,18769,18770,18771,18772,18773,18774,18775,18776,18777,18778,18779,18780,18781,18782,18783,25216,25217,25218,25219,25220,25221,25222,25223,25224,25225,25226,25227,25228,25229,25230,25231,288,289,290,291,292,293,294,295,296,297,298,299,300,301,302,303,26432,26433,26434,26435,26436,26437,26438,26439,26440,26441,26442,26443,26444,26445,26446,26447,22928,22929,22930,22931,22932,22933,22934,22935,22936,22937,22938,22939,22940,22941,22942,22943,5808,5809,5810,5811,5812,5813,5814,5815,5816,5817,5818,5819,5820,5821,5822,5823,26384,26385,26386,26387,26388,26389,26390,26391,26392,26393,26394,26395,26396,26397,26398,26399,7216,7217,7218,7219,7220,7221,7222,7223,7224,7225,7226,7227,7228,7229,7230,7231,2400,2401,2402,2403,2404,2405,2406,2407,2408,2409,2410,2411,2412,2413,2414,2415,26640,26641,26642,26643,26644,26645,26646,26647,26648,26649,26650,26651,26652,26653,26654,26655,27216,27217,27218,27219,27220,27221,27222,27223,27224,27225,27226,27227,27228,27229,27230,27231,28816,28817,28818,28819,28820,28821,28822,28823,28824,28825,28826,28827,28828,28829,28830,28831,11888,11889,11890,11891,11892,11893,11894,11895,11896,11897,11898,11899,11900,11901,11902,11903,5664,5665,5666,5667,5668,5669,5670,5671,5672,5673,5674,5675,5676,5677,5678,5679,6112,6113,6114,6115,6116,6117,6118,6119,6120,6121,6122,6123,6124,6125,6126,6127,14576,14577,14578,14579,14580,14581,14582,14583,14584,14585,14586,14587,14588,14589,14590,14591,33424,33425,33426,33427,33428,33429,33430,33431,33432,33433,33434,33435,33436,33437,33438,33439,6368,6369,6370,6371,6372,6373,6374,6375,6376,6377,6378,6379,6380,6381,6382,6383,23216,23217,23218,23219,23220,23221,23222,23223,23224,23225,23226,23227,23228,23229,23230,23231,25456,25457,25458,25459,25460,25461,25462,25463,25464,25465,25466,25467,25468,25469,25470,25471,21600,21601,21602,21603,21604,21605,21606,21607,21608,21609,21610,21611,21612,21613,21614,21615,27120,27121,27122,27123,27124,27125,27126,27127,27128,27129,27130,27131,27132,27133,27134,27135,21920,21921,21922,21923,21924,21925,21926,21927,21928,21929,21930,21931,21932,21933,21934,21935,27440,27441,27442,27443,27444,27445,27446,27447,27448,27449,27450,27451,27452,27453,27454,27455,27952,27953,27954,27955,27956,27957,27958,27959,27960,27961,27962,27963,27964,27965,27966,27967,24800,24801,24802,24803,24804,24805,24806,24807,24808,24809,24810,24811,24812,24813,24814,24815,31920,31921,31922,31923,31924,31925,31926,31927,31928,31929,31930,31931,31932,31933,31934,31935,29600,29601,29602,29603,29604,29605,29606,29607,29608,29609,29610,29611,29612,29613,29614,29615
-> 48;48;18,61,81,149,150,156,169,204,289,329,354,363,381,382,398,444,451,524,688,743,844,911,948,989,1157,1173,1268,1284,1350,1370,1433,1451,1550,1576,1591,1649,1652,1665,1695,1701,1715,1747,1801,1850,1995,2089
-> 768;48;3033,7169,6757,7071,11830,15308,15529,14997,19416,19548,26470,28092
-> 768;48;2511,3277,982,1306,2398,7115,2712,8397,4625,11009,5273,6107,13511,15172,15836,20295,20552,18517,18768,25230,290,22934,26433,5821,26399,7217,2410,26646,27218,28821,11898,5670,14586,6118,33437,6369,23218,25461,21601,27127,27442,21935,27957,24813,31927,29609
Processor: CL
-> SDR##1;1;@accuracy#java.lang.Float#1.0@accuracyTrend#java.lang.Float#1.0@classification:1#nl.zeesoft.zdk.neural.processors.Classification#1;value;java.lang.Integer;0,2366%1,638
~~~~

Test results
------------
All 15 tests have been executed successfully (285 assertions).  
Total test duration: 52575 ms (total sleep duration: 176 ms).  

Memory usage and duration per test;  
 * nl.zeesoft.zdk.test.TestStr: 683 Kb / 0 Mb, 15 ms
 * nl.zeesoft.zdk.test.thread.TestRunCode: 437 Kb / 0 Mb, 39 ms
 * nl.zeesoft.zdk.test.thread.TestCodeRunnerChain: 439 Kb / 0 Mb, 156 ms
 * nl.zeesoft.zdk.test.collection.TestCollections: 466 Kb / 0 Mb, 235 ms
 * nl.zeesoft.zdk.test.http.TestHttpServer: 810 Kb / 0 Mb, 81 ms
 * nl.zeesoft.zdk.test.grid.TestGrid: 800 Kb / 0 Mb, 15 ms
 * nl.zeesoft.zdk.test.neural.TestSDR: 819 Kb / 0 Mb, 4 ms
 * nl.zeesoft.zdk.test.neural.TestCellGrid: 880 Kb / 0 Mb, 8 ms
 * nl.zeesoft.zdk.test.neural.TestScalarEncoder: 881 Kb / 0 Mb, 9 ms
 * nl.zeesoft.zdk.test.neural.TestSpatialPooler: 884 Kb / 0 Mb, 9754 ms
 * nl.zeesoft.zdk.test.neural.TestTemporalMemory: 896 Kb / 0 Mb, 3602 ms
 * nl.zeesoft.zdk.test.neural.TestClassifier: 907 Kb / 0 Mb, 308 ms
 * nl.zeesoft.zdk.test.neural.TestMerger: 917 Kb / 0 Mb, 4 ms
 * nl.zeesoft.zdk.test.neural.TestProcessorFactory: 918 Kb / 0 Mb, 29707 ms
 * nl.zeesoft.zdk.test.neural.TestNetwork: 956 Kb / 0 Mb, 8148 ms
