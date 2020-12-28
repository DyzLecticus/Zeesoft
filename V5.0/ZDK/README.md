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
2020-12-28 01:12:42:800 nl.zeesoft.zdk.http.HttpServer: Opening server socket on port: 8080 ...
2020-12-28 01:12:42:816 nl.zeesoft.zdk.http.HttpServer: Opened server socket on port: 8080
2020-12-28 01:12:42:841 nl.zeesoft.zdk.http.HttpServer$1: Request/response headers (Port: 8080);
<<<
GET / HTTP/1.1
Content-Type: text/html
Host: 127.0.0.1
Connection: keep-alive
>>>
HTTP/1.1 200 OK
Content-Length: 47
2020-12-28 01:12:42:844 nl.zeesoft.zdk.http.HttpServer$1: Request/response headers (Port: 8080);
<<<
GET /pizza.txt HTTP/1.1
Content-Type: text/html
Host: 127.0.0.1
Connection: keep-alive
>>>
HTTP/1.1 404 File not found: http/pizza.txt
Content-Length: 30
2020-12-28 01:12:42:847 nl.zeesoft.zdk.http.HttpServer$1: Request/response headers (Port: 8080);
<<<
PUT /pizza.txt HTTP/1.1
Content-Type: text/html
Host: 127.0.0.1
Connection: keep-alive
Content-Length: 13
>>>
HTTP/1.1 200 OK
2020-12-28 01:12:42:849 nl.zeesoft.zdk.http.HttpServer$1: Request/response headers (Port: 8080);
<<<
GET /pizza.txt HTTP/1.1
Content-Type: text/html
Host: 127.0.0.1
Connection: keep-alive
>>>
HTTP/1.1 200 OK
Content-Length: 13
2020-12-28 01:12:42:851 nl.zeesoft.zdk.http.HttpServer$1: Request/response headers (Port: 8080);
<<<
DELETE /pizza.txt HTTP/1.1
Content-Type: text/html
Host: 127.0.0.1
Connection: keep-alive
>>>
HTTP/1.1 200 OK
2020-12-28 01:12:42:852 nl.zeesoft.zdk.http.HttpServer: Opening server socket on port: 9090 ...
2020-12-28 01:12:42:852 nl.zeesoft.zdk.http.HttpServer: Opened server socket on port: 9090
2020-12-28 01:12:42:855 nl.zeesoft.zdk.http.HttpServer$1: Request/response headers (Port: 8080);
<<<
GET / HTTP/1.1
Host: 127.0.0.1:8080
Content-Type: text/html
Connection: keep-alive
>>>
HTTP/1.1 200 OK
Content-Length: 47
2020-12-28 01:12:42:857 nl.zeesoft.zdk.http.HttpServer$1: Request/response headers (Port: 9090);
<<<
GET / HTTP/1.1
Host: 127.0.0.1:8080
Content-Type: text/html
Connection: keep-alive
>>>
HTTP/1.1 200 OK
Content-Length: 47
2020-12-28 01:12:42:878 nl.zeesoft.zdk.http.HttpServer: Closing connections ...
2020-12-28 01:12:42:878 nl.zeesoft.zdk.http.HttpServer: Closed connections
2020-12-28 01:12:42:878 nl.zeesoft.zdk.http.HttpServer: Closing server socket on port: 9090 ...
2020-12-28 01:12:42:878 nl.zeesoft.zdk.http.HttpServer: Closed server socket on port: 9090
2020-12-28 01:12:42:894 nl.zeesoft.zdk.http.HttpServer: Closing connections ...
2020-12-28 01:12:42:894 nl.zeesoft.zdk.http.HttpServer: Closed connections
2020-12-28 01:12:42:894 nl.zeesoft.zdk.http.HttpServer: Closing server socket on port: 8080 ...
2020-12-28 01:12:42:894 nl.zeesoft.zdk.http.HttpServer: Closed server socket on port: 8080

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
2020-12-28 01:12:43:073 nl.zeesoft.zdk.neural.processors.ScalarEncoder: Initializing ScalarEncoder ...
2020-12-28 01:12:43:073 nl.zeesoft.zdk.neural.processors.ScalarEncoder: Initialized
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
2020-12-28 01:12:43:094 nl.zeesoft.zdk.neural.processors.SpatialPooler: Initializing SpatialPooler ...
2020-12-28 01:12:43:156 nl.zeesoft.zdk.neural.processors.SpatialPooler: Initialized
2020-12-28 01:12:43:156 nl.zeesoft.zdk.neural.processors.SpatialPooler: Resetting SpatialPooler connections ...
2020-12-28 01:12:43:281 nl.zeesoft.zdk.neural.processors.SpatialPooler: Reset connections

2020-12-28 01:12:43:281 nl.zeesoft.zdk.test.neural.TestSpatialPooler: Initializing spatial pooler (asynchronous) ...
2020-12-28 01:12:43:733 nl.zeesoft.zdk.test.neural.TestSpatialPooler: Initialized spatial pooler (asynchronous)

Input SDR: 16;16;0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15
Output SDR: 48;48;17,40,64,71,174,189,244,268,317,366,374,396,459,498,500,525,581,603,648,649,665,790,812,850,859,872,906,948,986,992,997,1056,1071,1123,1203,1211,1229,1312,1514,1604,1699,1700,1806,1896,1981,1983
Input SDR: 16;16;0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15
Output SDR: 48;48;32,40,64,71,174,178,189,244,268,366,374,396,459,498,500,525,581,603,648,649,665,790,812,820,850,859,872,906,948,986,992,997,1071,1123,1203,1211,1229,1312,1514,1604,1699,1700,1896,1981,1983,2138
Input SDR: 16;16;0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15
Output SDR: 48;48;17,40,64,71,174,178,189,244,268,366,374,396,459,498,500,525,581,603,648,649,665,790,812,850,859,872,906,948,986,992,997,1056,1071,1123,1203,1211,1229,1312,1514,1604,1699,1700,1896,1981,1983,2138
Input SDR: 16;16;0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15
Output SDR: 48;48;17,40,64,71,174,178,189,244,268,366,374,396,459,498,500,525,581,603,648,649,665,790,812,850,859,872,906,948,986,992,997,1056,1071,1123,1203,1211,1229,1312,1514,1604,1699,1700,1896,1981,1983,2138
Input SDR: 16;16;0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15
Output SDR: 48;48;17,40,64,71,174,178,189,244,268,366,374,396,459,498,500,525,581,603,648,649,665,790,812,850,859,872,906,948,986,992,997,1056,1071,1123,1203,1211,1229,1312,1514,1604,1699,1700,1896,1981,1983,2138

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
2020-12-28 01:12:51:852 nl.zeesoft.zdk.neural.processors.TemporalMemory: Initializing TemporalMemory ...
2020-12-28 01:12:51:852 nl.zeesoft.zdk.neural.processors.TemporalMemory: Initialized
2020-12-28 01:12:51:852 nl.zeesoft.zdk.neural.processors.TemporalMemory: Resetting TemporalMemory connections ...
2020-12-28 01:12:51:867 nl.zeesoft.zdk.neural.processors.TemporalMemory: Reset connections
2020-12-28 01:12:51:908 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 1 > bursting: 46, active: 736, winners: 46, predictive: 0
2020-12-28 01:12:51:932 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 2 > bursting: 46, active: 736, winners: 46, predictive: 0
2020-12-28 01:12:51:961 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 3 > bursting: 46, active: 736, winners: 46, predictive: 0
2020-12-28 01:12:51:977 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 4 > bursting: 46, active: 736, winners: 46, predictive: 0
2020-12-28 01:12:52:009 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 5 > bursting: 46, active: 736, winners: 46, predictive: 41
2020-12-28 01:12:52:019 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 6 > bursting: 5, active: 121, winners: 46, predictive: 44
2020-12-28 01:12:52:028 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 7 > bursting: 2, active: 76, winners: 46, predictive: 39
2020-12-28 01:12:52:049 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 8 > bursting: 7, active: 151, winners: 46, predictive: 39
2020-12-28 01:12:52:058 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 9 > bursting: 7, active: 151, winners: 46, predictive: 5
2020-12-28 01:12:52:075 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 10 > bursting: 41, active: 661, winners: 46, predictive: 45
2020-12-28 01:12:52:079 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 11 > bursting: 1, active: 61, winners: 46, predictive: 44
2020-12-28 01:12:52:085 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 12 > bursting: 2, active: 76, winners: 46, predictive: 46
2020-12-28 01:12:52:092 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 13 > bursting: 0, active: 46, winners: 46, predictive: 41
2020-12-28 01:12:52:107 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 14 > bursting: 5, active: 121, winners: 46, predictive: 0
2020-12-28 01:12:52:119 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 15 > bursting: 46, active: 736, winners: 46, predictive: 45
2020-12-28 01:12:52:125 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 16 > bursting: 1, active: 61, winners: 46, predictive: 46
2020-12-28 01:12:52:132 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 17 > bursting: 0, active: 46, winners: 46, predictive: 46
2020-12-28 01:12:52:138 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 18 > bursting: 0, active: 46, winners: 46, predictive: 39
2020-12-28 01:12:52:149 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 19 > bursting: 7, active: 151, winners: 46, predictive: 0
2020-12-28 01:12:52:173 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 20 > bursting: 46, active: 736, winners: 46, predictive: 46
2020-12-28 01:12:52:177 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 21 > bursting: 0, active: 46, winners: 46, predictive: 46
2020-12-28 01:12:52:183 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 22 > bursting: 0, active: 46, winners: 46, predictive: 44
2020-12-28 01:12:52:190 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 23 > bursting: 2, active: 76, winners: 46, predictive: 43
2020-12-28 01:12:52:201 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 24 > bursting: 3, active: 91, winners: 46, predictive: 0
2020-12-28 01:12:52:232 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 25 > bursting: 46, active: 736, winners: 46, predictive: 87
2020-12-28 01:12:52:241 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 26 > bursting: 0, active: 46, winners: 46, predictive: 15
2020-12-28 01:12:52:259 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 27 > bursting: 32, active: 526, winners: 46, predictive: 86
2020-12-28 01:12:52:267 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 28 > bursting: 0, active: 46, winners: 46, predictive: 9
2020-12-28 01:12:52:280 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 29 > bursting: 37, active: 601, winners: 46, predictive: 87
2020-12-28 01:12:52:287 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 30 > bursting: 0, active: 46, winners: 46, predictive: 51
2020-12-28 01:12:52:305 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 31 > bursting: 2, active: 76, winners: 46, predictive: 29
2020-12-28 01:12:52:318 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 32 > bursting: 23, active: 391, winners: 46, predictive: 91
2020-12-28 01:12:52:326 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 33 > bursting: 0, active: 46, winners: 46, predictive: 0
2020-12-28 01:12:52:349 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 34 > bursting: 46, active: 736, winners: 46, predictive: 93
2020-12-28 01:12:52:359 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 35 > bursting: 0, active: 46, winners: 46, predictive: 39
2020-12-28 01:12:52:378 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 36 > bursting: 11, active: 211, winners: 46, predictive: 58
2020-12-28 01:12:52:385 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 37 > bursting: 0, active: 46, winners: 46, predictive: 47
2020-12-28 01:12:52:391 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 38 > bursting: 0, active: 46, winners: 46, predictive: 47
2020-12-28 01:12:52:397 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 39 > bursting: 0, active: 46, winners: 46, predictive: 59
2020-12-28 01:12:52:404 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 40 > bursting: 0, active: 46, winners: 46, predictive: 45
2020-12-28 01:12:52:412 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 41 > bursting: 3, active: 91, winners: 46, predictive: 7
2020-12-28 01:12:52:440 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 42 > bursting: 39, active: 631, winners: 46, predictive: 93
2020-12-28 01:12:52:449 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 43 > bursting: 0, active: 46, winners: 46, predictive: 64
2020-12-28 01:12:52:457 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 44 > bursting: 1, active: 61, winners: 46, predictive: 80
2020-12-28 01:12:52:466 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 45 > bursting: 0, active: 46, winners: 46, predictive: 36
2020-12-28 01:12:52:480 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 46 > bursting: 12, active: 226, winners: 46, predictive: 53
2020-12-28 01:12:52:500 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 47 > bursting: 0, active: 46, winners: 46, predictive: 64
2020-12-28 01:12:52:507 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 48 > bursting: 0, active: 46, winners: 46, predictive: 50
2020-12-28 01:12:52:514 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 49 > bursting: 0, active: 46, winners: 46, predictive: 46
2020-12-28 01:12:52:522 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 50 > bursting: 1, active: 61, winners: 46, predictive: 1
2020-12-28 01:12:52:542 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 51 > bursting: 45, active: 721, winners: 46, predictive: 93
2020-12-28 01:12:52:550 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 52 > bursting: 0, active: 46, winners: 46, predictive: 76
2020-12-28 01:12:52:568 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 53 > bursting: 2, active: 76, winners: 46, predictive: 58
2020-12-28 01:12:52:575 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 54 > bursting: 1, active: 61, winners: 46, predictive: 68
2020-12-28 01:12:52:580 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 55 > bursting: 2, active: 76, winners: 46, predictive: 64
2020-12-28 01:12:52:585 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 56 > bursting: 0, active: 46, winners: 46, predictive: 75
2020-12-28 01:12:52:590 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 57 > bursting: 0, active: 46, winners: 46, predictive: 67
2020-12-28 01:12:52:594 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 58 > bursting: 0, active: 46, winners: 46, predictive: 53
2020-12-28 01:12:52:615 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 59 > bursting: 3, active: 91, winners: 46, predictive: 1
2020-12-28 01:12:52:637 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 60 > bursting: 45, active: 721, winners: 46, predictive: 93
2020-12-28 01:12:52:645 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 61 > bursting: 0, active: 46, winners: 46, predictive: 66
2020-12-28 01:12:52:651 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 62 > bursting: 0, active: 46, winners: 46, predictive: 78
2020-12-28 01:12:52:655 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 63 > bursting: 0, active: 46, winners: 46, predictive: 68
2020-12-28 01:12:52:670 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 64 > bursting: 0, active: 46, winners: 46, predictive: 84
2020-12-28 01:12:52:680 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 65 > bursting: 0, active: 46, winners: 46, predictive: 73
2020-12-28 01:12:52:686 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 66 > bursting: 0, active: 46, winners: 46, predictive: 86
2020-12-28 01:12:52:690 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 67 > bursting: 0, active: 46, winners: 46, predictive: 70
2020-12-28 01:12:52:695 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 68 > bursting: 0, active: 46, winners: 46, predictive: 85
2020-12-28 01:12:52:700 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 69 > bursting: 0, active: 46, winners: 46, predictive: 78
2020-12-28 01:12:52:705 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 70 > bursting: 0, active: 46, winners: 46, predictive: 88
2020-12-28 01:12:52:709 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 71 > bursting: 0, active: 46, winners: 46, predictive: 68
2020-12-28 01:12:52:724 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 72 > bursting: 1, active: 61, winners: 46, predictive: 88
2020-12-28 01:12:52:729 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 73 > bursting: 0, active: 46, winners: 46, predictive: 81
2020-12-28 01:12:52:735 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 74 > bursting: 0, active: 46, winners: 46, predictive: 89
2020-12-28 01:12:52:910 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 100 > bursting: 0, active: 46, winners: 46, predictive: 88
2020-12-28 01:12:53:195 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 150 > bursting: 0, active: 46, winners: 46, predictive: 89
2020-12-28 01:12:53:482 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 200 > bursting: 0, active: 46, winners: 46, predictive: 89
2020-12-28 01:12:53:973 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 250 > bursting: 0, active: 46, winners: 46, predictive: 89
2020-12-28 01:12:54:299 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 300 > bursting: 0, active: 46, winners: 46, predictive: 89
2020-12-28 01:12:54:562 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 350 > bursting: 0, active: 46, winners: 46, predictive: 90
2020-12-28 01:12:54:813 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 400 > bursting: 0, active: 46, winners: 46, predictive: 89
2020-12-28 01:12:55:065 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 450 > bursting: 0, active: 46, winners: 46, predictive: 90
2020-12-28 01:12:55:312 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 500 > bursting: 0, active: 46, winners: 46, predictive: 89
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
2020-12-28 01:12:55:545 nl.zeesoft.zdk.neural.processors.Classifier: Initializing Classifier ...
2020-12-28 01:12:55:545 nl.zeesoft.zdk.neural.processors.Classifier: Initialized

2020-12-28 01:12:55:594 nl.zeesoft.zdk.neural.processors.ClassifierStep: Dividing step 1 classifier value counts by two ...
2020-12-28 01:12:55:628 nl.zeesoft.zdk.neural.processors.ClassifierStep: Dividing step 1 classifier value counts by two ...
2020-12-28 01:12:55:657 nl.zeesoft.zdk.neural.processors.ClassifierStep: Dividing step 1 classifier value counts by two ...

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
2020-12-28 01:12:55:691 nl.zeesoft.zdk.neural.processors.Merger: Initializing Merger ...
2020-12-28 01:12:55:691 nl.zeesoft.zdk.neural.processors.Merger: Initialized

Merged and distorted;
10101100
00000000
00000101
11000000
10001000
00000000
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
2020-12-28 01:12:55:708 nl.zeesoft.zdk.neural.processors.SpatialPooler: Initializing SpatialPooler ...
2020-12-28 01:12:55:740 nl.zeesoft.zdk.neural.processors.SpatialPooler: Initialized
2020-12-28 01:12:55:740 nl.zeesoft.zdk.neural.processors.SpatialPooler: Resetting SpatialPooler connections ...
2020-12-28 01:12:55:771 nl.zeesoft.zdk.neural.processors.SpatialPooler: Reset connections
2020-12-28 01:12:55:771 nl.zeesoft.zdk.neural.processors.TemporalMemory: Initializing TemporalMemory ...
2020-12-28 01:12:55:771 nl.zeesoft.zdk.neural.processors.TemporalMemory: Initialized
2020-12-28 01:12:55:771 nl.zeesoft.zdk.neural.processors.Classifier: Initializing Classifier ...
2020-12-28 01:12:55:771 nl.zeesoft.zdk.neural.processors.Classifier: Initialized

Processing ...
2020-12-28 01:12:57:468 nl.zeesoft.zdk.test.neural.TestProcessorFactory: 768;48;32,33,34,35,36,37,38,39,40,41,42,43,44,45,46,47,368,369,370,371,372,373,374,375,376,377,378,379,380,381,382,383,512,513,514,515,516,517,518,519,520,521,522,523,524,525,526,527,800,801,802,803,804,805,806,807,808,809,810,811,812,813,814,815,816,817,818,819,820,821,822,823,824,825,826,827,828,829,830,831,1254,1784,2304,2305,2306,2307,2308,2309,2310,2311,2312,2313,2314,2315,2316,2317,2318,2319,4800,4801,4802,4803,4804,4805,4806,4807,4808,4809,4810,4811,4812,4813,4814,4815,6592,6593,6594,6595,6596,6597,6598,6599,6600,6601,6602,6603,6604,6605,6606,6607,6912,6913,6914,6915,6916,6917,6918,6919,6920,6921,6922,6923,6924,6925,6926,6927,6960,6961,6962,6963,6964,6965,6966,6967,6968,6969,6970,6971,6972,6973,6974,6975,7856,7857,7858,7859,7860,7861,7862,7863,7864,7865,7866,7867,7868,7869,7870,7871,8336,8337,8338,8339,8340,8341,8342,8343,8344,8345,8346,8347,8348,8349,8350,8351,8480,8481,8482,8483,8484,8485,8486,8487,8488,8489,8490,8491,8492,8493,8494,8495,8750,10176,10177,10178,10179,10180,10181,10182,10183,10184,10185,10186,10187,10188,10189,10190,10191,11168,11169,11170,11171,11172,11173,11174,11175,11176,11177,11178,11179,11180,11181,11182,11183,11184,11185,11186,11187,11188,11189,11190,11191,11192,11193,11194,11195,11196,11197,11198,11199,11818,11872,11873,11874,11875,11876,11877,11878,11879,11880,11881,11882,11883,11884,11885,11886,11887,12560,12561,12562,12563,12564,12565,12566,12567,12568,12569,12570,12571,12572,12573,12574,12575,12768,12769,12770,12771,12772,12773,12774,12775,12776,12777,12778,12779,12780,12781,12782,12783,13328,13329,13330,13331,13332,13333,13334,13335,13336,13337,13338,13339,13340,13341,13342,13343,13664,13665,13666,13667,13668,13669,13670,13671,13672,13673,13674,13675,13676,13677,13678,13679,13776,13777,13778,13779,13780,13781,13782,13783,13784,13785,13786,13787,13788,13789,13790,13791,14848,14849,14850,14851,14852,14853,14854,14855,14856,14857,14858,14859,14860,14861,14862,14863,15104,15105,15106,15107,15108,15109,15110,15111,15112,15113,15114,15115,15116,15117,15118,15119,15731,15888,15889,15890,15891,15892,15893,15894,15895,15896,15897,15898,15899,15900,15901,15902,15903,16832,16833,16834,16835,16836,16837,16838,16839,16840,16841,16842,16843,16844,16845,16846,16847,18176,18177,18178,18179,18180,18181,18182,18183,18184,18185,18186,18187,18188,18189,18190,18191,18976,18977,18978,18979,18980,18981,18982,18983,18984,18985,18986,18987,18988,18989,18990,18991,18992,18993,18994,18995,18996,18997,18998,18999,19000,19001,19002,19003,19004,19005,19006,19007,19616,19617,19618,19619,19620,19621,19622,19623,19624,19625,19626,19627,19628,19629,19630,19631,19776,19777,19778,19779,19780,19781,19782,19783,19784,19785,19786,19787,19788,19789,19790,19791,20032,20033,20034,20035,20036,20037,20038,20039,20040,20041,20042,20043,20044,20045,20046,20047,20256,20257,20258,20259,20260,20261,20262,20263,20264,20265,20266,20267,20268,20269,20270,20271,21296,21297,21298,21299,21300,21301,21302,21303,21304,21305,21306,21307,21308,21309,21310,21311,22048,22049,22050,22051,22052,22053,22054,22055,22056,22057,22058,22059,22060,22061,22062,22063,22944,22945,22946,22947,22948,22949,22950,22951,22952,22953,22954,22955,22956,22957,22958,22959,24368,24369,24370,24371,24372,24373,24374,24375,24376,24377,24378,24379,24380,24381,24382,24383,27424,27425,27426,27427,27428,27429,27430,27431,27432,27433,27434,27435,27436,27437,27438,27439,29552,29553,29554,29555,29556,29557,29558,29559,29560,29561,29562,29563,29564,29565,29566,29567,30277,33408,33409,33410,33411,33412,33413,33414,33415,33416,33417,33418,33419,33420,33421,33422,33423
2020-12-28 01:13:00:039 nl.zeesoft.zdk.test.neural.TestProcessorFactory: 768;48;47,380,517,809,816,817,818,819,820,821,822,823,824,825,826,827,828,829,830,831,2686,2952,4804,5312,5313,5314,5315,5316,5317,5318,5319,5320,5321,5322,5323,5324,5325,5326,5327,5534,6592,6593,6594,6595,6596,6597,6598,6599,6600,6601,6602,6603,6604,6605,6606,6607,6913,6972,8348,9120,9121,9122,9123,9124,9125,9126,9127,9128,9129,9130,9131,9132,9133,9134,9135,9322,12132,12285,12564,12777,13024,13025,13026,13027,13028,13029,13030,13031,13032,13033,13034,13035,13036,13037,13038,13039,13664,13665,13666,13667,13668,13669,13670,13671,13672,13673,13674,13675,13676,13677,13678,13679,14308,15025,15728,15729,15730,15731,15732,15733,15734,15735,15736,15737,15738,15739,15740,15741,15742,15743,15888,16835,17173,17895,17952,17953,17954,17955,17956,17957,17958,17959,17960,17961,17962,17963,17964,17965,17966,17967,18635,19621,20270,20400,20401,20402,20403,20404,20405,20406,20407,20408,20409,20410,20411,20412,20413,20414,20415,21301,22956,24381,27174,28613,29488,29489,29490,29491,29492,29493,29494,29495,29496,29497,29498,29499,29500,29501,29502,29503,29566,30277,30292,30464,30465,30466,30467,30468,30469,30470,30471,30472,30473,30474,30475,30476,30477,30478,30479,31184,31185,31186,31187,31188,31189,31190,31191,31192,31193,31194,31195,31196,31197,31198,31199,33408,33409,33410,33411,33412,33413,33414,33415,33416,33417,33418,33419,33420,33421,33422,33423
2020-12-28 01:13:02:734 nl.zeesoft.zdk.test.neural.TestProcessorFactory: 768;48;517,809,1248,1249,1250,1251,1252,1253,1254,1255,1256,1257,1258,1259,1260,1261,1262,1263,1784,2307,2686,3884,5534,6913,6972,8348,8736,8737,8738,8739,8740,8741,8742,8743,8744,8745,8746,8747,8748,8749,8750,8751,11194,11818,11878,12132,12564,12777,13386,13776,13777,13778,13779,13780,13781,13782,13783,13784,13785,13786,13787,13788,13789,13790,13791,14856,15117,15888,15959,16000,16001,16002,16003,16004,16005,16006,16007,16008,16009,16010,16011,16012,16013,16014,16015,16835,17895,17963,18990,18996,19788,19853,20270,21989,22053,22419,24381,25152,25616,25617,25618,25619,25620,25621,25622,25623,25624,25625,25626,25627,25628,25629,25630,25631,27174,29440,29441,29442,29443,29444,29445,29446,29447,29448,29449,29450,29451,29452,29453,29454,29455,30277,30292,30464,30465,30466,30467,30468,30469,30470,30471,30472,30473,30474,30475,30476,30477,30478,30479,31075,33423
2020-12-28 01:13:05:416 nl.zeesoft.zdk.test.neural.TestProcessorFactory: 768;48;47,517,822,2307,2944,2945,2946,2947,2948,2949,2950,2951,2952,2953,2954,2955,2956,2957,2958,2959,5534,6913,8348,8486,8736,8737,8738,8739,8740,8741,8742,8743,8744,8745,8746,8747,8748,8749,8750,8751,9322,12132,12155,12285,12564,12777,13568,14308,14455,14856,15025,15732,15888,15959,16835,17173,18176,18177,18178,18179,18180,18181,18182,18183,18184,18185,18186,18187,18188,18189,18190,18191,18635,19788,20270,20608,20609,20610,20611,20612,20613,20614,20615,20616,20617,20618,20619,20620,20621,20622,20623,21301,22053,22956,24381,24912,24913,24914,24915,24916,24917,24918,24919,24920,24921,24922,24923,24924,24925,24926,24927,25152,25630,26528,27174,27952,27953,27954,27955,27956,27957,27958,27959,27960,27961,27962,27963,27964,27965,27966,27967,28613,29566,30277,31184,31185,31186,31187,31188,31189,31190,31191,31192,31193,31194,31195,31196,31197,31198,31199,33423
2020-12-28 01:13:08:118 nl.zeesoft.zdk.test.neural.TestProcessorFactory: 768;48;380,517,822,1248,1249,1250,1251,1252,1253,1254,1255,1256,1257,1258,1259,1260,1261,1262,1263,1784,2307,4804,5534,6599,6913,6972,7621,8348,8464,8465,8466,8467,8468,8469,8470,8471,8472,8473,8474,8475,8476,8477,8478,8479,11808,11809,11810,11811,11812,11813,11814,11815,11816,11817,11818,11819,11820,11821,11822,11823,11872,11873,11874,11875,11876,11877,11878,11879,11880,11881,11882,11883,11884,11885,11886,11887,12132,12155,12564,12777,13568,13670,13776,13777,13778,13779,13780,13781,13782,13783,13784,13785,13786,13787,13788,13789,13790,13791,15025,15104,15105,15106,15107,15108,15109,15110,15111,15112,15113,15114,15115,15116,15117,15118,15119,15779,15888,16835,18635,18990,18992,18993,18994,18995,18996,18997,18998,18999,19000,19001,19002,19003,19004,19005,19006,19007,19621,19788,19853,20040,20608,20609,20610,20611,20612,20613,20614,20615,20616,20617,20618,20619,20620,20621,20622,20623,21301,22956,24381,27952,27953,27954,27955,27956,27957,27958,27959,27960,27961,27962,27963,27964,27965,27966,27967,28613,29489,29566,30277,30352,30353,30354,30355,30356,30357,30358,30359,30360,30361,30362,30363,30364,30365,30366,30367,33423
2020-12-28 01:13:10:707 nl.zeesoft.zdk.test.neural.TestProcessorFactory: 768;48;194,380,517,809,1019,1206,2307,2952,3093,3884,4804,5534,6913,8348,11072,11073,11074,11075,11076,11077,11078,11079,11080,11081,11082,11083,11084,11085,11086,11087,11939,12132,12155,12285,12564,12777,13328,13329,13330,13331,13332,13333,13334,13335,13336,13337,13338,13339,13340,13341,13342,13343,13386,13670,14056,14308,14455,14856,15025,15888,16835,17173,18635,19788,20270,20400,20401,20402,20403,20404,20405,20406,20407,20408,20409,20410,20411,20412,20413,20414,20415,20608,20609,20610,20611,20612,20613,20614,20615,20616,20617,20618,20619,20620,20621,20622,20623,21301,24381,27964,28613,29566,30277,30466,31075,33423
2020-12-28 01:13:13:218 nl.zeesoft.zdk.test.neural.TestProcessorFactory: 768;48;47,194,517,809,1019,1254,1784,2307,5534,8348,8486,8552,9750,10177,11808,11809,11810,11811,11812,11813,11814,11815,11816,11817,11818,11819,11820,11821,11822,11823,11878,12132,12564,12777,13032,13568,13776,13777,13778,13779,13780,13781,13782,13783,13784,13785,13786,13787,13788,13789,13790,13791,14856,15025,15117,15483,15776,15777,15778,15779,15780,15781,15782,15783,15784,15785,15786,15787,15788,15789,15790,15791,15888,16012,16835,18054,18635,18990,18992,18993,18994,18995,18996,18997,18998,18999,19000,19001,19002,19003,19004,19005,19006,19007,20270,20594,21301,24381,24905,24918,25137,26528,28613,30277,30292,30464,30465,30466,30467,30468,30469,30470,30471,30472,30473,30474,30475,30476,30477,30478,30479
2020-12-28 01:13:15:832 nl.zeesoft.zdk.test.neural.TestProcessorFactory: 768;48;156,380,517,809,867,2307,2952,5534,6599,6913,6972,7860,8348,8544,8545,8546,8547,8548,8549,8550,8551,8552,8553,8554,8555,8556,8557,8558,8559,9133,10174,11939,12132,12285,12564,12777,13670,14308,14856,15776,15777,15778,15779,15780,15781,15782,15783,15784,15785,15786,15787,15788,15789,15790,15791,15888,15959,16012,16835,16981,17895,18635,18782,19621,19788,20270,20613,21301,22053,22685,22956,24381,28613,29566,30277,30352,30353,30354,30355,30356,30357,30358,30359,30360,30361,30362,30363,30364,30365,30366,30367
2020-12-28 01:13:18:366 nl.zeesoft.zdk.test.neural.TestProcessorFactory: 768;48;47,517,809,1206,1254,1784,2686,3884,4209,5317,5534,5862,6635,8348,8552,11180,11872,11873,11874,11875,11876,11877,11878,11879,11880,11881,11882,11883,11884,11885,11886,11887,11939,12132,12564,12777,14056,15025,15117,15888,16012,16835,16981,18054,18635,18990,18996,19480,19853,20270,21301,22956,24381,26494,27964,28613,29489,29566,30277,31075,33423
Processing 1000 SDRs took: 25238 ms (25 ms/SDR)
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

2020-12-28 01:13:21:025 nl.zeesoft.zdk.neural.network.Network: Initializing network ...
2020-12-28 01:13:21:028 nl.zeesoft.zdk.neural.processors.SpatialPooler: Initializing SpatialPooler ...
2020-12-28 01:13:21:028 nl.zeesoft.zdk.neural.processors.ScalarEncoder: Initializing ScalarEncoder ...
2020-12-28 01:13:21:028 nl.zeesoft.zdk.neural.processors.TemporalMemory: Initializing TemporalMemory ...
2020-12-28 01:13:21:028 nl.zeesoft.zdk.neural.processors.ScalarEncoder: Initialized
2020-12-28 01:13:21:030 nl.zeesoft.zdk.neural.processors.Classifier: Initializing Classifier ...
2020-12-28 01:13:21:031 nl.zeesoft.zdk.neural.processors.Classifier: Initialized
2020-12-28 01:13:21:032 nl.zeesoft.zdk.neural.processors.TemporalMemory: Initialized
2020-12-28 01:13:21:033 nl.zeesoft.zdk.neural.processors.TemporalMemory: Resetting TemporalMemory connections ...
2020-12-28 01:13:21:034 nl.zeesoft.zdk.neural.processors.TemporalMemory: Reset connections
2020-12-28 01:13:21:055 nl.zeesoft.zdk.neural.processors.SpatialPooler: Initialized
2020-12-28 01:13:21:055 nl.zeesoft.zdk.neural.processors.SpatialPooler: Resetting SpatialPooler connections ...
2020-12-28 01:13:21:085 nl.zeesoft.zdk.neural.processors.SpatialPooler: Reset connections
2020-12-28 01:13:21:086 nl.zeesoft.zdk.neural.network.Network: Initialized network

2020-12-28 01:13:21:086 nl.zeesoft.zdk.test.neural.TestNetwork: Processing 100 SDRs ...
2020-12-28 01:13:24:554 nl.zeesoft.zdk.test.neural.TestNetwork: Processed 100 SDRs

Statistics;
- Cells             : 36864
- Proximal segments : 2304
- Proximal synapses : 463735 (active: 241269)
- Distal segments   : 1468
- Distal synapses   : 38218 (active: 3031)

2020-12-28 01:13:24:805 nl.zeesoft.zdk.neural.network.Network: Saving network ...
2020-12-28 01:13:24:819 nl.zeesoft.zdk.neural.network.Network$8: Writing dist/CL.txt ...
2020-12-28 01:13:24:819 nl.zeesoft.zdk.neural.network.Network$7: Writing dist/Configuration.txt ...
2020-12-28 01:13:24:819 nl.zeesoft.zdk.neural.network.Network$8: Writing dist/EN.txt ...
2020-12-28 01:13:24:819 nl.zeesoft.zdk.neural.network.Network$8: Writing dist/SP.txt ...
2020-12-28 01:13:24:826 nl.zeesoft.zdk.neural.network.Network$8: Writing dist/TM.txt ...
2020-12-28 01:13:24:884 nl.zeesoft.zdk.neural.network.Network$9: Writing dist/PreviousIO.txt ...
2020-12-28 01:13:25:467 nl.zeesoft.zdk.neural.network.Network: Saved network

Processor: EN
-> 16;16;1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16
-> SDR##1;1;@value#java.lang.Integer#1
Processor: SP
-> 48;48;535,359,522,853,648,47,861,1807,1275,322,968,326,958,1119,121,1904,713,1223,1407,98,1950,43,1216,311,460,996,640,1102,1018,1894,1810,1622,1267,1406,815,986,386,1147,1903,1349,19,1718,1899,847,385,1132
Processor: TM
-> 768;48;7360,7361,7362,7363,7364,7365,7366,7367,7368,7369,7370,7371,7372,7373,7374,7375,10240,10241,10242,10243,10244,10245,10246,10247,10248,10249,10250,10251,10252,10253,10254,10255,10380,15502,15936,15937,15938,15939,15940,15941,15942,15943,15944,15945,15946,15947,15948,15949,15950,15951,1936,1937,1938,1939,1940,1941,1942,1943,1944,1945,1946,1947,1948,1949,1950,1951,18112,18113,18114,18115,18116,18117,18118,18119,18120,18121,18122,18123,18124,18125,18126,18127,1568,1569,1570,1571,1572,1573,1574,1575,1576,1577,1578,1579,1580,1581,1582,1583,19456,19457,19458,19459,19460,19461,19462,19463,19464,19465,19466,19467,19468,19469,19470,19471,6160,6161,6162,6163,6164,6165,6166,6167,6168,6169,6170,6171,6172,6173,6174,6175,304,305,306,307,308,309,310,311,312,313,314,315,316,317,318,319,5157,5216,5217,5218,5219,5220,5221,5222,5223,5224,5225,5226,5227,5228,5229,5230,5231,688,689,690,691,692,693,694,695,696,697,698,699,700,701,702,703,752,753,754,755,756,757,758,759,760,761,762,763,764,765,766,767,11408,11409,11410,11411,11412,11413,11414,11415,11416,11417,11418,11419,11420,11421,11422,11423,6176,6177,6178,6179,6180,6181,6182,6183,6184,6185,6186,6187,6188,6189,6190,6191,4976,4977,4978,4979,4980,4981,4982,4983,4984,4985,4986,4987,4988,4989,4990,4991,8352,8353,8354,8355,8356,8357,8358,8359,8360,8361,8362,8363,8364,8365,8366,8367,30476,13648,13649,13650,13651,13652,13653,13654,13655,13656,13657,13658,13659,13660,13661,13662,13663,5744,5745,5746,5747,5748,5749,5750,5751,5752,5753,5754,5755,5756,5757,5758,5759,13776,13777,13778,13779,13780,13781,13782,13783,13784,13785,13786,13787,13788,13789,13790,13791,15332,8560,8561,8562,8563,8564,8565,8566,8567,8568,8569,8570,8571,8572,8573,8574,8575,15776,15777,15778,15779,15780,15781,15782,15783,15784,15785,15786,15787,15788,15789,15790,15791,16288,16289,16290,16291,16292,16293,16294,16295,16296,16297,16298,16299,16300,16301,16302,16303,13040,13041,13042,13043,13044,13045,13046,13047,13048,13049,13050,13051,13052,13053,13054,13055,17632,17633,17634,17635,17636,17637,17638,17639,17640,17641,17642,17643,17644,17645,17646,17647,21584,21585,21586,21587,21588,21589,21590,21591,21592,21593,21594,21595,21596,21597,21598,21599,13552,13553,13554,13555,13556,13557,13558,13559,13560,13561,13562,13563,13564,13565,13566,13567,22496,22497,22498,22499,22500,22501,22502,22503,22504,22505,22506,22507,22508,22509,22510,22511,17910,18352,18353,18354,18355,18356,18357,18358,18359,18360,18361,18362,18363,18364,18365,18366,18367,19568,19569,19570,19571,19572,19573,19574,19575,19576,19577,19578,19579,19580,19581,19582,19583,25952,25953,25954,25955,25956,25957,25958,25959,25960,25961,25962,25963,25964,25965,25966,25967,20272,20273,20274,20275,20276,20277,20278,20279,20280,20281,20282,20283,20284,20285,20286,20287,27488,27489,27490,27491,27492,27493,27494,27495,27496,27497,27498,27499,27500,27501,27502,27503,20415,28960,28961,28962,28963,28964,28965,28966,28967,28968,28969,28970,28971,28972,28973,28974,28975,22521,30304,30305,30306,30307,30308,30309,30310,30311,30312,30313,30314,30315,30316,30317,30318,30319,31200,31201,31202,31203,31204,31205,31206,31207,31208,31209,31210,31211,31212,31213,31214,31215,28912,28913,28914,28915,28916,28917,28918,28919,28920,28921,28922,28923,28924,28925,28926,28927,30384,30385,30386,30387,30388,30389,30390,30391,30392,30393,30394,30395,30396,30397,30398,30399,30448,30449,30450,30451,30452,30453,30454,30455,30456,30457,30458,30459,30460,30461,30462,30463
-> 48;48;19,43,47,98,121,311,326,359,385,386,460,522,535,640,713,815,847,853,861,986,996,1018,1102,1132,1147,1216,1223,1267,1349,1406,1622,1718,1807,1810,1894,1899,1903,1950
-> 768;48;785,31901
-> 768;48;7367,10253,10380,15502,15949,18124,1938,1582,19471,313,6166,5157,5220,693,758,11413,6182,30476,8362,4983,13649,5756,13783,15332,8569,15778,16299,13050,21599,17646,13556,17910,22505,18365,19574,25964,20274,27489,20415,28963,22521,30307,31214,28919,30386,30462
Processor: CL
-> SDR##1;1;@accuracy#java.lang.Float#0.9894737@accuracyTrend#java.lang.Float#1.0@classification:1#nl.zeesoft.zdk.neural.processors.Classification#1;value;java.lang.Integer;0,2514%1,914
~~~~

Test results
------------
All 15 tests have been executed successfully (285 assertions).  
Total test duration: 47835 ms (total sleep duration: 176 ms).  

Memory usage and duration per test;  
 * nl.zeesoft.zdk.test.TestStr: 684 Kb / 0 Mb, 0 ms
 * nl.zeesoft.zdk.test.thread.TestRunCode: 438 Kb / 0 Mb, 30 ms
 * nl.zeesoft.zdk.test.thread.TestCodeRunnerChain: 439 Kb / 0 Mb, 137 ms
 * nl.zeesoft.zdk.test.collection.TestCollections: 466 Kb / 0 Mb, 172 ms
 * nl.zeesoft.zdk.test.http.TestHttpServer: 811 Kb / 0 Mb, 109 ms
 * nl.zeesoft.zdk.test.grid.TestGrid: 801 Kb / 0 Mb, 23 ms
 * nl.zeesoft.zdk.test.neural.TestSDR: 820 Kb / 0 Mb, 0 ms
 * nl.zeesoft.zdk.test.neural.TestCellGrid: 880 Kb / 0 Mb, 15 ms
 * nl.zeesoft.zdk.test.neural.TestScalarEncoder: 882 Kb / 0 Mb, 5 ms
 * nl.zeesoft.zdk.test.neural.TestSpatialPooler: 883 Kb / 0 Mb, 8742 ms
 * nl.zeesoft.zdk.test.neural.TestTemporalMemory: 896 Kb / 0 Mb, 3693 ms
 * nl.zeesoft.zdk.test.neural.TestClassifier: 908 Kb / 0 Mb, 146 ms
 * nl.zeesoft.zdk.test.neural.TestMerger: 918 Kb / 0 Mb, 17 ms
 * nl.zeesoft.zdk.test.neural.TestProcessorFactory: 919 Kb / 0 Mb, 25301 ms
 * nl.zeesoft.zdk.test.neural.TestNetwork: 956 Kb / 0 Mb, 9163 ms
