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
2020-11-20 23:28:46:234 nl.zeesoft.zdk.http.HttpServer: Opening server socket on port: 8080 ...
2020-11-20 23:28:46:265 nl.zeesoft.zdk.http.HttpServer: Opened server socket on port: 8080
2020-11-20 23:28:46:293 nl.zeesoft.zdk.http.HttpServer$1: Request/response headers (Port: 8080);
<<<
GET / HTTP/1.1
Content-Type: text/html
Host: 127.0.0.1
Connection: keep-alive
>>>
HTTP/1.1 200 OK
Content-Length: 47
2020-11-20 23:28:46:296 nl.zeesoft.zdk.http.HttpServer$1: Request/response headers (Port: 8080);
<<<
GET /pizza.txt HTTP/1.1
Content-Type: text/html
Host: 127.0.0.1
Connection: keep-alive
>>>
HTTP/1.1 404 File not found: http/pizza.txt
Content-Length: 30
2020-11-20 23:28:46:302 nl.zeesoft.zdk.http.HttpServer$1: Request/response headers (Port: 8080);
<<<
PUT /pizza.txt HTTP/1.1
Content-Type: text/html
Host: 127.0.0.1
Connection: keep-alive
Content-Length: 13
>>>
HTTP/1.1 200 OK
2020-11-20 23:28:46:305 nl.zeesoft.zdk.http.HttpServer$1: Request/response headers (Port: 8080);
<<<
GET /pizza.txt HTTP/1.1
Content-Type: text/html
Host: 127.0.0.1
Connection: keep-alive
>>>
HTTP/1.1 200 OK
Content-Length: 13
2020-11-20 23:28:46:307 nl.zeesoft.zdk.http.HttpServer$1: Request/response headers (Port: 8080);
<<<
DELETE /pizza.txt HTTP/1.1
Content-Type: text/html
Host: 127.0.0.1
Connection: keep-alive
>>>
HTTP/1.1 200 OK
2020-11-20 23:28:46:308 nl.zeesoft.zdk.http.HttpServer: Opening server socket on port: 9090 ...
2020-11-20 23:28:46:309 nl.zeesoft.zdk.http.HttpServer: Opened server socket on port: 9090
2020-11-20 23:28:46:311 nl.zeesoft.zdk.http.HttpServer$1: Request/response headers (Port: 8080);
<<<
GET / HTTP/1.1
Host: 127.0.0.1:8080
Content-Type: text/html
Connection: keep-alive
>>>
HTTP/1.1 200 OK
Content-Length: 47
2020-11-20 23:28:46:313 nl.zeesoft.zdk.http.HttpServer$1: Request/response headers (Port: 9090);
<<<
GET / HTTP/1.1
Host: 127.0.0.1:8080
Content-Type: text/html
Connection: keep-alive
>>>
HTTP/1.1 200 OK
Content-Length: 47
2020-11-20 23:28:46:326 nl.zeesoft.zdk.http.HttpServer: Closing connections ...
2020-11-20 23:28:46:326 nl.zeesoft.zdk.http.HttpServer: Closed connections
2020-11-20 23:28:46:326 nl.zeesoft.zdk.http.HttpServer: Closing server socket on port: 9090 ...
2020-11-20 23:28:46:327 nl.zeesoft.zdk.http.HttpServer: Closed server socket on port: 9090
2020-11-20 23:28:46:337 nl.zeesoft.zdk.http.HttpServer: Closing connections ...
2020-11-20 23:28:46:337 nl.zeesoft.zdk.http.HttpServer: Closed connections
2020-11-20 23:28:46:337 nl.zeesoft.zdk.http.HttpServer: Closing server socket on port: 8080 ...
2020-11-20 23:28:46:337 nl.zeesoft.zdk.http.HttpServer: Closed server socket on port: 8080

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
2020-11-20 23:28:46:459 nl.zeesoft.zdk.neural.processors.ScalarEncoder: Initializing ScalarEncoder ...
2020-11-20 23:28:46:460 nl.zeesoft.zdk.neural.processors.ScalarEncoder: Initialized
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
2020-11-20 23:28:46:480 nl.zeesoft.zdk.neural.processors.SpatialPooler: Initializing SpatialPooler ...
2020-11-20 23:28:46:546 nl.zeesoft.zdk.neural.processors.SpatialPooler: Initialized
2020-11-20 23:28:46:546 nl.zeesoft.zdk.neural.processors.SpatialPooler: Resetting SpatialPooler connections ...
2020-11-20 23:28:46:720 nl.zeesoft.zdk.neural.processors.SpatialPooler: Reset connections

2020-11-20 23:28:46:721 nl.zeesoft.zdk.test.neural.TestSpatialPooler: Initializing spatial pooler (asynchronous) ...
2020-11-20 23:28:47:267 nl.zeesoft.zdk.test.neural.TestSpatialPooler: Initialized spatial pooler (asynchronous)

Input SDR: 16;16;0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15
Output SDR: 48;48;13,16,47,91,168,197,329,414,433,479,501,567,604,611,652,696,765,784,811,872,876,880,943,964,972,1064,1135,1208,1223,1256,1285,1339,1365,1371,1404,1413,1473,1565,1569,1597,1670,1788,1851,1947,1992,2000
Input SDR: 16;16;0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15
Output SDR: 48;48;34,47,91,108,168,340,414,433,449,567,569,648,681,718,741,765,841,872,876,880,906,942,964,980,1064,1094,1135,1256,1285,1299,1371,1413,1565,1569,1577,1618,1642,1661,1766,1788,1847,1851,1892,1953,1992,2000
Input SDR: 16;16;0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15
Output SDR: 48;48;34,47,91,168,197,329,414,433,449,567,611,648,696,765,784,811,872,876,880,906,942,943,964,972,1064,1094,1135,1208,1256,1285,1371,1413,1565,1569,1577,1597,1642,1661,1670,1766,1788,1847,1851,1892,1992,2000
Input SDR: 16;16;0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15
Output SDR: 48;48;34,47,91,168,197,329,414,449,567,611,648,696,741,765,784,811,872,876,880,906,942,943,964,972,1064,1094,1135,1208,1256,1285,1371,1413,1565,1569,1577,1597,1642,1661,1670,1766,1788,1847,1851,1892,1992,2000
Input SDR: 16;16;0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15
Output SDR: 48;48;34,47,91,168,197,329,414,449,567,611,648,696,741,765,784,811,872,876,880,906,942,943,964,972,1064,1094,1135,1208,1256,1285,1371,1413,1565,1569,1577,1597,1642,1661,1670,1766,1788,1847,1851,1892,1992,2000

Average overlap for similar inputs: 45.0, overall: 2.0
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
2020-11-20 23:28:56:217 nl.zeesoft.zdk.neural.processors.TemporalMemory: Initializing TemporalMemory ...
2020-11-20 23:28:56:220 nl.zeesoft.zdk.neural.processors.TemporalMemory: Initialized
2020-11-20 23:28:56:221 nl.zeesoft.zdk.neural.processors.TemporalMemory: Resetting TemporalMemory connections ...
2020-11-20 23:28:56:231 nl.zeesoft.zdk.neural.processors.TemporalMemory: Reset connections
2020-11-20 23:28:56:279 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 1 > bursting: 46, active: 736, winners: 46, predictive: 0
2020-11-20 23:28:56:305 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 2 > bursting: 46, active: 736, winners: 46, predictive: 0
2020-11-20 23:28:56:332 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 3 > bursting: 46, active: 736, winners: 46, predictive: 0
2020-11-20 23:28:56:349 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 4 > bursting: 46, active: 736, winners: 46, predictive: 0
2020-11-20 23:28:56:375 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 5 > bursting: 46, active: 736, winners: 46, predictive: 0
2020-11-20 23:28:56:389 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 6 > bursting: 46, active: 736, winners: 46, predictive: 0
2020-11-20 23:28:56:404 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 7 > bursting: 46, active: 736, winners: 46, predictive: 0
2020-11-20 23:28:56:428 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 8 > bursting: 46, active: 736, winners: 46, predictive: 0
2020-11-20 23:28:56:436 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 9 > bursting: 46, active: 736, winners: 46, predictive: 0
2020-11-20 23:28:56:445 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 10 > bursting: 46, active: 736, winners: 46, predictive: 0
2020-11-20 23:28:56:453 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 11 > bursting: 46, active: 736, winners: 46, predictive: 0
2020-11-20 23:28:56:462 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 12 > bursting: 46, active: 736, winners: 46, predictive: 0
2020-11-20 23:28:56:470 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 13 > bursting: 46, active: 736, winners: 46, predictive: 0
2020-11-20 23:28:56:487 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 14 > bursting: 46, active: 736, winners: 46, predictive: 0
2020-11-20 23:28:56:495 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 15 > bursting: 46, active: 736, winners: 46, predictive: 0
2020-11-20 23:28:56:503 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 16 > bursting: 46, active: 736, winners: 46, predictive: 0
2020-11-20 23:28:56:518 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 17 > bursting: 46, active: 736, winners: 46, predictive: 40
2020-11-20 23:28:56:530 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 18 > bursting: 6, active: 136, winners: 46, predictive: 41
2020-11-20 23:28:56:541 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 19 > bursting: 5, active: 121, winners: 46, predictive: 42
2020-11-20 23:28:56:554 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 20 > bursting: 4, active: 106, winners: 46, predictive: 43
2020-11-20 23:28:56:564 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 21 > bursting: 3, active: 91, winners: 46, predictive: 4
2020-11-20 23:28:56:595 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 22 > bursting: 42, active: 676, winners: 46, predictive: 46
2020-11-20 23:28:56:605 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 23 > bursting: 0, active: 46, winners: 46, predictive: 45
2020-11-20 23:28:56:615 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 24 > bursting: 1, active: 61, winners: 46, predictive: 46
2020-11-20 23:28:56:625 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 25 > bursting: 0, active: 46, winners: 46, predictive: 6
2020-11-20 23:28:56:640 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 26 > bursting: 40, active: 646, winners: 46, predictive: 46
2020-11-20 23:28:56:650 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 27 > bursting: 0, active: 46, winners: 46, predictive: 46
2020-11-20 23:28:56:662 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 28 > bursting: 0, active: 46, winners: 46, predictive: 46
2020-11-20 23:28:56:674 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 29 > bursting: 0, active: 46, winners: 46, predictive: 6
2020-11-20 23:28:56:700 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 30 > bursting: 40, active: 646, winners: 46, predictive: 46
2020-11-20 23:28:56:709 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 31 > bursting: 0, active: 46, winners: 46, predictive: 46
2020-11-20 23:28:56:716 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 32 > bursting: 0, active: 46, winners: 46, predictive: 46
2020-11-20 23:28:56:721 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 33 > bursting: 0, active: 46, winners: 46, predictive: 6
2020-11-20 23:28:56:735 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 34 > bursting: 40, active: 646, winners: 46, predictive: 46
2020-11-20 23:28:56:744 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 35 > bursting: 0, active: 46, winners: 46, predictive: 46
2020-11-20 23:28:56:779 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 36 > bursting: 0, active: 46, winners: 46, predictive: 46
2020-11-20 23:28:56:791 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 37 > bursting: 0, active: 46, winners: 46, predictive: 45
2020-11-20 23:28:56:800 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 38 > bursting: 1, active: 61, winners: 46, predictive: 0
2020-11-20 23:28:56:817 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 39 > bursting: 46, active: 736, winners: 46, predictive: 46
2020-11-20 23:28:56:826 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 40 > bursting: 0, active: 46, winners: 46, predictive: 46
2020-11-20 23:28:56:834 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 41 > bursting: 0, active: 46, winners: 46, predictive: 45
2020-11-20 23:28:56:859 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 42 > bursting: 1, active: 61, winners: 46, predictive: 0
2020-11-20 23:28:56:872 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 43 > bursting: 46, active: 736, winners: 46, predictive: 46
2020-11-20 23:28:56:881 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 44 > bursting: 0, active: 46, winners: 46, predictive: 46
2020-11-20 23:28:56:888 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 45 > bursting: 0, active: 46, winners: 46, predictive: 46
2020-11-20 23:28:56:902 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 46 > bursting: 0, active: 46, winners: 46, predictive: 1
2020-11-20 23:28:56:926 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 47 > bursting: 45, active: 721, winners: 46, predictive: 46
2020-11-20 23:28:56:934 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 48 > bursting: 0, active: 46, winners: 46, predictive: 46
2020-11-20 23:28:56:942 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 49 > bursting: 0, active: 46, winners: 46, predictive: 46
2020-11-20 23:28:56:950 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 50 > bursting: 0, active: 46, winners: 46, predictive: 6
2020-11-20 23:28:56:962 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 51 > bursting: 40, active: 646, winners: 46, predictive: 46
2020-11-20 23:28:56:969 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 52 > bursting: 0, active: 46, winners: 46, predictive: 46
2020-11-20 23:28:56:984 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 53 > bursting: 0, active: 46, winners: 46, predictive: 46
2020-11-20 23:28:56:991 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 54 > bursting: 0, active: 46, winners: 46, predictive: 45
2020-11-20 23:28:56:999 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 55 > bursting: 1, active: 61, winners: 46, predictive: 9
2020-11-20 23:28:57:014 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 56 > bursting: 37, active: 601, winners: 46, predictive: 46
2020-11-20 23:28:57:022 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 57 > bursting: 0, active: 46, winners: 46, predictive: 46
2020-11-20 23:28:57:029 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 58 > bursting: 0, active: 46, winners: 46, predictive: 46
2020-11-20 23:28:57:036 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 59 > bursting: 0, active: 46, winners: 46, predictive: 6
2020-11-20 23:28:57:048 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 60 > bursting: 40, active: 646, winners: 46, predictive: 46
2020-11-20 23:28:57:068 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 61 > bursting: 0, active: 46, winners: 46, predictive: 46
2020-11-20 23:28:57:076 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 62 > bursting: 0, active: 46, winners: 46, predictive: 46
2020-11-20 23:28:57:084 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 63 > bursting: 0, active: 46, winners: 46, predictive: 6
2020-11-20 23:28:57:099 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 64 > bursting: 40, active: 646, winners: 46, predictive: 46
2020-11-20 23:28:57:105 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 65 > bursting: 0, active: 46, winners: 46, predictive: 46
2020-11-20 23:28:57:111 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 66 > bursting: 0, active: 46, winners: 46, predictive: 46
2020-11-20 23:28:57:116 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 67 > bursting: 0, active: 46, winners: 46, predictive: 6
2020-11-20 23:28:57:125 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 68 > bursting: 40, active: 646, winners: 46, predictive: 46
2020-11-20 23:28:57:139 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 69 > bursting: 0, active: 46, winners: 46, predictive: 46
2020-11-20 23:28:57:145 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 70 > bursting: 0, active: 46, winners: 46, predictive: 46
2020-11-20 23:28:57:150 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 71 > bursting: 0, active: 46, winners: 46, predictive: 44
2020-11-20 23:28:57:157 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 72 > bursting: 2, active: 76, winners: 46, predictive: 44
2020-11-20 23:28:57:164 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 73 > bursting: 2, active: 76, winners: 46, predictive: 46
2020-11-20 23:28:57:171 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 74 > bursting: 0, active: 46, winners: 46, predictive: 46
2020-11-20 23:28:57:383 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 100 > bursting: 0, active: 46, winners: 46, predictive: 46
2020-11-20 23:28:57:664 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 150 > bursting: 0, active: 46, winners: 46, predictive: 46
2020-11-20 23:28:57:942 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 200 > bursting: 0, active: 46, winners: 46, predictive: 46
2020-11-20 23:28:58:313 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 250 > bursting: 0, active: 46, winners: 46, predictive: 46
2020-11-20 23:28:58:577 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 300 > bursting: 0, active: 46, winners: 46, predictive: 46
2020-11-20 23:28:58:849 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 350 > bursting: 0, active: 46, winners: 46, predictive: 46
2020-11-20 23:28:59:131 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 400 > bursting: 0, active: 46, winners: 46, predictive: 46
2020-11-20 23:28:59:415 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 450 > bursting: 0, active: 46, winners: 46, predictive: 46
2020-11-20 23:28:59:682 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 500 > bursting: 0, active: 46, winners: 46, predictive: 46
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
2020-11-20 23:28:59:851 nl.zeesoft.zdk.neural.processors.Classifier: Initializing Classifier ...
2020-11-20 23:28:59:851 nl.zeesoft.zdk.neural.processors.Classifier: Initialized

2020-11-20 23:28:59:895 nl.zeesoft.zdk.neural.processors.ClassifierStep: Dividing step 1 classifier value counts by two ...
2020-11-20 23:28:59:931 nl.zeesoft.zdk.neural.processors.ClassifierStep: Dividing step 1 classifier value counts by two ...
2020-11-20 23:28:59:970 nl.zeesoft.zdk.neural.processors.ClassifierStep: Dividing step 1 classifier value counts by two ...

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
2020-11-20 23:29:00:017 nl.zeesoft.zdk.neural.processors.Merger: Initializing Merger ...
2020-11-20 23:29:00:017 nl.zeesoft.zdk.neural.processors.Merger: Initialized

Merged and distorted;
10101100
00000010
00011011
01000000
00000000
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
2020-11-20 23:29:00:110 nl.zeesoft.zdk.neural.processors.SpatialPooler: Initializing SpatialPooler ...
2020-11-20 23:29:00:160 nl.zeesoft.zdk.neural.processors.SpatialPooler: Initialized
2020-11-20 23:29:00:160 nl.zeesoft.zdk.neural.processors.SpatialPooler: Resetting SpatialPooler connections ...
2020-11-20 23:29:00:222 nl.zeesoft.zdk.neural.processors.SpatialPooler: Reset connections
2020-11-20 23:29:00:222 nl.zeesoft.zdk.neural.processors.TemporalMemory: Initializing TemporalMemory ...
2020-11-20 23:29:00:225 nl.zeesoft.zdk.neural.processors.TemporalMemory: Initialized
2020-11-20 23:29:00:225 nl.zeesoft.zdk.neural.processors.Classifier: Initializing Classifier ...
2020-11-20 23:29:00:226 nl.zeesoft.zdk.neural.processors.Classifier: Initialized

Processing ...
2020-11-20 23:29:02:526 nl.zeesoft.zdk.test.neural.TestProcessorFactory: 768;48;112,113,114,115,116,117,118,119,120,121,122,123,124,125,126,127,320,321,322,323,324,325,326,327,328,329,330,331,332,333,334,335,464,465,466,467,468,469,470,471,472,473,474,475,476,477,478,479,1072,1073,1074,1075,1076,1077,1078,1079,1080,1081,1082,1083,1084,1085,1086,1087,2016,2017,2018,2019,2020,2021,2022,2023,2024,2025,2026,2027,2028,2029,2030,2031,2688,2689,2690,2691,2692,2693,2694,2695,2696,2697,2698,2699,2700,2701,2702,2703,2768,2769,2770,2771,2772,2773,2774,2775,2776,2777,2778,2779,2780,2781,2782,2783,2848,2849,2850,2851,2852,2853,2854,2855,2856,2857,2858,2859,2860,2861,2862,2863,2944,2945,2946,2947,2948,2949,2950,2951,2952,2953,2954,2955,2956,2957,2958,2959,3680,3681,3682,3683,3684,3685,3686,3687,3688,3689,3690,3691,3692,3693,3694,3695,4784,4785,4786,4787,4788,4789,4790,4791,4792,4793,4794,4795,4796,4797,4798,4799,5840,5841,5842,5843,5844,5845,5846,5847,5848,5849,5850,5851,5852,5853,5854,5855,6800,6801,6802,6803,6804,6805,6806,6807,6808,6809,6810,6811,6812,6813,6814,6815,6880,6881,6882,6883,6884,6885,6886,6887,6888,6889,6890,6891,6892,6893,6894,6895,7008,7009,7010,7011,7012,7013,7014,7015,7016,7017,7018,7019,7020,7021,7022,7023,7344,7345,7346,7347,7348,7349,7350,7351,7352,7353,7354,7355,7356,7357,7358,7359,7808,7809,7810,7811,7812,7813,7814,7815,7816,7817,7818,7819,7820,7821,7822,7823,8864,8865,8866,8867,8868,8869,8870,8871,8872,8873,8874,8875,8876,8877,8878,8879,9872,9873,9874,9875,9876,9877,9878,9879,9880,9881,9882,9883,9884,9885,9886,9887,11184,11185,11186,11187,11188,11189,11190,11191,11192,11193,11194,11195,11196,11197,11198,11199,12160,12161,12162,12163,12164,12165,12166,12167,12168,12169,12170,12171,12172,12173,12174,12175,12272,12273,12274,12275,12276,12277,12278,12279,12280,12281,12282,12283,12284,12285,12286,12287,13776,13777,13778,13779,13780,13781,13782,13783,13784,13785,13786,13787,13788,13789,13790,13791,14045,14240,14241,14242,14243,14244,14245,14246,14247,14248,14249,14250,14251,14252,14253,14254,14255,14816,14817,14818,14819,14820,14821,14822,14823,14824,14825,14826,14827,14828,14829,14830,14831,14896,14897,14898,14899,14900,14901,14902,14903,14904,14905,14906,14907,14908,14909,14910,14911,16224,16225,16226,16227,16228,16229,16230,16231,16232,16233,16234,16235,16236,16237,16238,16239,16288,16289,16290,16291,16292,16293,16294,16295,16296,16297,16298,16299,16300,16301,16302,16303,16336,16337,16338,16339,16340,16341,16342,16343,16344,16345,16346,16347,16348,16349,16350,16351,19541,19728,19729,19730,19731,19732,19733,19734,19735,19736,19737,19738,19739,19740,19741,19742,19743,20784,20785,20786,20787,20788,20789,20790,20791,20792,20793,20794,20795,20796,20797,20798,20799,21264,21265,21266,21267,21268,21269,21270,21271,21272,21273,21274,21275,21276,21277,21278,21279,21984,21985,21986,21987,21988,21989,21990,21991,21992,21993,21994,21995,21996,21997,21998,21999,22000,22001,22002,22003,22004,22005,22006,22007,22008,22009,22010,22011,22012,22013,22014,22015,22528,22529,22530,22531,22532,22533,22534,22535,22536,22537,22538,22539,22540,22541,22542,22543,23472,23473,23474,23475,23476,23477,23478,23479,23480,23481,23482,23483,23484,23485,23486,23487,24320,24321,24322,24323,24324,24325,24326,24327,24328,24329,24330,24331,24332,24333,24334,24335,24784,24785,24786,24787,24788,24789,24790,24791,24792,24793,24794,24795,24796,24797,24798,24799,25584,25585,25586,25587,25588,25589,25590,25591,25592,25593,25594,25595,25596,25597,25598,25599,25856,25857,25858,25859,25860,25861,25862,25863,25864,25865,25866,25867,25868,25869,25870,25871,27920,27921,27922,27923,27924,27925,27926,27927,27928,27929,27930,27931,27932,27933,27934,27935,29456,29457,29458,29459,29460,29461,29462,29463,29464,29465,29466,29467,29468,29469,29470,29471,31136,31137,31138,31139,31140,31141,31142,31143,31144,31145,31146,31147,31148,31149,31150,31151,31808,31809,31810,31811,31812,31813,31814,31815,31816,31817,31818,31819,31820,31821,31822,31823
2020-11-20 23:29:05:645 nl.zeesoft.zdk.test.neural.TestProcessorFactory: 768;48;1082,1808,1809,1810,1811,1812,1813,1814,1815,1816,1817,1818,1819,1820,1821,1822,1823,2689,2848,2849,2850,2851,2852,2853,2854,2855,2856,2857,2858,2859,2860,2861,2862,2863,3136,3137,3138,3139,3140,3141,3142,3143,3144,3145,3146,3147,3148,3149,3150,3151,4160,4161,4162,4163,4164,4165,4166,4167,4168,4169,4170,4171,4172,4173,4174,4175,4416,4417,4418,4419,4420,4421,4422,4423,4424,4425,4426,4427,4428,4429,4430,4431,6815,6892,7502,8380,9872,9873,9874,9875,9876,9877,9878,9879,9880,9881,9882,9883,9884,9885,9886,9887,10016,10017,10018,10019,10020,10021,10022,10023,10024,10025,10026,10027,10028,10029,10030,10031,10336,10337,10338,10339,10340,10341,10342,10343,10344,10345,10346,10347,10348,10349,10350,10351,11197,11232,11233,11234,11235,11236,11237,11238,11239,11240,11241,11242,11243,11244,11245,11246,11247,12224,12225,12226,12227,12228,12229,12230,12231,12232,12233,12234,12235,12236,12237,12238,12239,12277,12880,12881,12882,12883,12884,12885,12886,12887,12888,12889,12890,12891,12892,12893,12894,12895,13788,14624,14625,14626,14627,14628,14629,14630,14631,14632,14633,14634,14635,14636,14637,14638,14639,14679,14819,14902,15040,15041,15042,15043,15044,15045,15046,15047,15048,15049,15050,15051,15052,15053,15054,15055,16236,16290,16960,16961,16962,16963,16964,16965,16966,16967,16968,16969,16970,16971,16972,16973,16974,16975,17120,17121,17122,17123,17124,17125,17126,17127,17128,17129,17130,17131,17132,17133,17134,17135,18352,18353,18354,18355,18356,18357,18358,18359,18360,18361,18362,18363,18364,18365,18366,18367,18992,18993,18994,18995,18996,18997,18998,18999,19000,19001,19002,19003,19004,19005,19006,19007,19536,19537,19538,19539,19540,19541,19542,19543,19544,19545,19546,19547,19548,19549,19550,19551,19818,20592,20593,20594,20595,20596,20597,20598,20599,20600,20601,20602,20603,20604,20605,20606,20607,22112,22113,22114,22115,22116,22117,22118,22119,22120,22121,22122,22123,22124,22125,22126,22127,22697,23474,24322,25512,25869,26433,27248,27249,27250,27251,27252,27253,27254,27255,27256,27257,27258,27259,27260,27261,27262,27263,27923,28144,28145,28146,28147,28148,28149,28150,28151,28152,28153,28154,28155,28156,28157,28158,28159,29460,31920,31921,31922,31923,31924,31925,31926,31927,31928,31929,31930,31931,31932,31933,31934,31935
2020-11-20 23:29:09:036 nl.zeesoft.zdk.test.neural.TestProcessorFactory: 768;48;116,330,704,705,706,707,708,709,710,711,712,713,714,715,716,717,718,719,1082,2016,2689,2730,2950,3688,4096,4097,4098,4099,4100,4101,4102,4103,4104,4105,4106,4107,4108,4109,4110,4111,4160,4161,4162,4163,4164,4165,4166,4167,4168,4169,4170,4171,4172,4173,4174,4175,4224,4225,4226,4227,4228,4229,4230,4231,4232,4233,4234,4235,4236,4237,4238,4239,5728,5729,5730,5731,5732,5733,5734,5735,5736,5737,5738,5739,5740,5741,5742,5743,6224,6225,6226,6227,6228,6229,6230,6231,6232,6233,6234,6235,6236,6237,6238,6239,6815,6892,7011,8380,8868,9879,11197,11232,11233,11234,11235,11236,11237,11238,11239,11240,11241,11242,11243,11244,11245,11246,11247,13008,13009,13010,13011,13012,13013,13014,13015,13016,13017,13018,13019,13020,13021,13022,13023,13788,14045,14255,14819,14902,16236,16290,16345,17392,17393,17394,17395,17396,17397,17398,17399,17400,17401,17402,17403,17404,17405,17406,17407,19888,19889,19890,19891,19892,19893,19894,19895,19896,19897,19898,19899,19900,19901,19902,19903,20224,20225,20226,20227,20228,20229,20230,20231,20232,20233,20234,20235,20236,20237,20238,20239,20464,20465,20466,20467,20468,20469,20470,20471,20472,20473,20474,20475,20476,20477,20478,20479,20592,20593,20594,20595,20596,20597,20598,20599,20600,20601,20602,20603,20604,20605,20606,20607,20768,20769,20770,20771,20772,20773,20774,20775,20776,20777,20778,20779,20780,20781,20782,20783,20784,20785,20786,20787,20788,20789,20790,20791,20792,20793,20794,20795,20796,20797,20798,20799,20960,20961,20962,20963,20964,20965,20966,20967,20968,20969,20970,20971,20972,20973,20974,20975,21273,21984,21985,21986,21987,21988,21989,21990,21991,21992,21993,21994,21995,21996,21997,21998,21999,22528,22529,22530,22531,22532,22533,22534,22535,22536,22537,22538,22539,22540,22541,22542,22543,23474,24795,29460,31139
2020-11-20 23:29:12:462 nl.zeesoft.zdk.test.neural.TestProcessorFactory: 768;48;290,330,711,1082,1200,1201,1202,1203,1204,1205,1206,1207,1208,1209,1210,1211,1212,1213,1214,1215,2016,2699,3688,4102,4425,6815,6892,7104,7105,7106,7107,7108,7109,7110,7111,7112,7113,7114,7115,7116,7117,7118,7119,8380,8868,9879,11197,11776,11777,11778,11779,11780,11781,11782,11783,11784,11785,11786,11787,11788,11789,11790,11791,12277,13788,14045,14255,14631,14902,15048,15520,15521,15522,15523,15524,15525,15526,15527,15528,15529,15530,15531,15532,15533,15534,15535,16236,16290,16968,18358,19863,19893,21273,22368,22369,22370,22371,22372,22373,22374,22375,22376,22377,22378,22379,22380,22381,22382,22383,22543,23474,24064,24065,24066,24067,24068,24069,24070,24071,24072,24073,24074,24075,24076,24077,24078,24079,24322,24795,25869,26433,27252,28147,29460,31808,31930
2020-11-20 23:29:15:939 nl.zeesoft.zdk.test.neural.TestProcessorFactory: 768;48;116,330,696,1082,1190,2016,2689,2848,2849,2850,2851,2852,2853,2854,2855,2856,2857,2858,2859,2860,2861,2862,2863,3040,3041,3042,3043,3044,3045,3046,3047,3048,3049,3050,3051,3052,3053,3054,3055,3136,3137,3138,3139,3140,3141,3142,3143,3144,3145,3146,3147,3148,3149,3150,3151,3688,4102,4175,4794,6815,6892,8380,9879,11197,11232,11233,11234,11235,11236,11237,11238,11239,11240,11241,11242,11243,11244,11245,11246,11247,13136,13137,13138,13139,13140,13141,13142,13143,13144,13145,13146,13147,13148,13149,13150,13151,13788,14045,14255,14902,15680,15681,15682,15683,15684,15685,15686,15687,15688,15689,15690,15691,15692,15693,15694,15695,16236,16290,17120,17121,17122,17123,17124,17125,17126,17127,17128,17129,17130,17131,17132,17133,17134,17135,19536,19537,19538,19539,19540,19541,19542,19543,19544,19545,19546,19547,19548,19549,19550,19551,19818,20474,20592,20792,21273,22112,22113,22114,22115,22116,22117,22118,22119,22120,22121,22122,22123,22124,22125,22126,22127,22543,22697,23474,24322,24795,25504,25505,25506,25507,25508,25509,25510,25511,25512,25513,25514,25515,25516,25517,25518,25519,25869,27216,27217,27218,27219,27220,27221,27222,27223,27224,27225,27226,27227,27228,27229,27230,27231,29460,31139
2020-11-20 23:29:19:342 nl.zeesoft.zdk.test.neural.TestProcessorFactory: 768;48;330,472,1082,1101,2016,2689,2768,2769,2770,2771,2772,2773,2774,2775,2776,2777,2778,2779,2780,2781,2782,2783,3680,3681,3682,3683,3684,3685,3686,3687,3688,3689,3690,3691,3692,3693,3694,3695,4102,4359,4416,4417,4418,4419,4420,4421,4422,4423,4424,4425,4426,4427,4428,4429,4430,4431,6267,6815,6892,7104,7105,7106,7107,7108,7109,7110,7111,7112,7113,7114,7115,7116,7117,7118,7119,7344,7345,7346,7347,7348,7349,7350,7351,7352,7353,7354,7355,7356,7357,7358,7359,8380,8868,9088,9089,9090,9091,9092,9093,9094,9095,9096,9097,9098,9099,9100,9101,9102,9103,9879,10336,10337,10338,10339,10340,10341,10342,10343,10344,10345,10346,10347,10348,10349,10350,10351,11197,12224,12225,12226,12227,12228,12229,12230,12231,12232,12233,12234,12235,12236,12237,12238,12239,12277,12427,13788,14045,14255,14902,15990,16236,16290,18352,18353,18354,18355,18356,18357,18358,18359,18360,18361,18362,18363,18364,18365,18366,18367,20464,20465,20466,20467,20468,20469,20470,20471,20472,20473,20474,20475,20476,20477,20478,20479,20782,20784,20785,20786,20787,20788,20789,20790,20791,20792,20793,20794,20795,20796,20797,20798,20799,21273,22000,22001,22002,22003,22004,22005,22006,22007,22008,22009,22010,22011,22012,22013,22014,22015,22543,23474,24040,24322,24795,25869,28147,29467
2020-11-20 23:29:22:784 nl.zeesoft.zdk.test.neural.TestProcessorFactory: 768;48;330,1082,2016,2689,2950,3137,3688,4102,4416,4417,4418,4419,4420,4421,4422,4423,4424,4425,4426,4427,4428,4429,4430,4431,4794,6333,6815,6892,8206,8380,8868,9879,11197,12251,12277,13176,13788,14045,14255,14631,14902,15048,15689,16236,16290,16656,16657,16658,16659,16660,16661,16662,16663,16664,16665,16666,16667,16668,16669,16670,16671,16960,16961,16962,16963,16964,16965,16966,16967,16968,16969,16970,16971,16972,16973,16974,16975,20592,21273,22543,23474,24032,24033,24034,24035,24036,24037,24038,24039,24040,24041,24042,24043,24044,24045,24046,24047,24795,25869,26433,27216,27217,27218,27219,27220,27221,27222,27223,27224,27225,27226,27227,27228,27229,27230,27231,27252,29460,31139,31808,31930
2020-11-20 23:29:26:257 nl.zeesoft.zdk.test.neural.TestProcessorFactory: 768;48;116,330,729,1082,1190,1201,2016,2699,3052,3688,4101,6815,6892,7502,7809,8380,8868,11197,12224,12225,12226,12227,12228,12229,12230,12231,12232,12233,12234,12235,12236,12237,12238,12239,12260,12277,13788,14045,14255,14902,15520,15521,15522,15523,15524,15525,15526,15527,15528,15529,15530,15531,15532,15533,15534,15535,16236,16290,16671,19412,20782,20792,21273,22000,22543,22697,23474,24076,24322,24795,25512,25584,25585,25586,25587,25588,25589,25590,25591,25592,25593,25594,25595,25596,25597,25598,25599,25869,26352,26353,26354,26355,26356,26357,26358,26359,26360,26361,26362,26363,26364,26365,26366,26367,27923,29460
2020-11-20 23:29:29:689 nl.zeesoft.zdk.test.neural.TestProcessorFactory: 768;48;330,696,704,705,706,707,708,709,710,711,712,713,714,715,716,717,718,719,729,1082,1088,1089,1090,1091,1092,1093,1094,1095,1096,1097,1098,1099,1100,1101,1102,1103,1808,1809,1810,1811,1812,1813,1814,1815,1816,1817,1818,1819,1820,1821,1822,1823,2016,2699,2773,3688,4101,4416,4417,4418,4419,4420,4421,4422,4423,4424,4425,4426,4427,4428,4429,4430,4431,5795,6815,6892,8206,8380,8868,10928,10929,10930,10931,10932,10933,10934,10935,10936,10937,10938,10939,10940,10941,10942,10943,11197,12251,12277,12890,13176,13680,13681,13682,13683,13684,13685,13686,13687,13688,13689,13690,13691,13692,13693,13694,13695,13788,14045,14631,14902,15048,15984,15985,15986,15987,15988,15989,15990,15991,15992,15993,15994,15995,15996,15997,15998,15999,16290,16968,17122,20474,21984,21985,21986,21987,21988,21989,21990,21991,21992,21993,21994,21995,21996,21997,21998,21999,23456,23457,23458,23459,23460,23461,23462,23463,23464,23465,23466,23467,23468,23469,23470,23471,23474,24032,24033,24034,24035,24036,24037,24038,24039,24040,24041,24042,24043,24044,24045,24046,24047,24322,25595,25869,26433,29460,31808
Processing 1000 SDRs took: 33007 ms (33 ms/SDR)
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

2020-11-20 23:29:33:255 nl.zeesoft.zdk.neural.network.Network: Initializing network ...
2020-11-20 23:29:33:255 nl.zeesoft.zdk.neural.processors.SpatialPooler: Initializing SpatialPooler ...
2020-11-20 23:29:33:255 nl.zeesoft.zdk.neural.processors.ScalarEncoder: Initializing ScalarEncoder ...
2020-11-20 23:29:33:256 nl.zeesoft.zdk.neural.processors.ScalarEncoder: Initialized
2020-11-20 23:29:33:258 nl.zeesoft.zdk.neural.processors.Classifier: Initializing Classifier ...
2020-11-20 23:29:33:259 nl.zeesoft.zdk.neural.processors.Classifier: Initialized
2020-11-20 23:29:33:261 nl.zeesoft.zdk.neural.processors.TemporalMemory: Initializing TemporalMemory ...
2020-11-20 23:29:33:266 nl.zeesoft.zdk.neural.processors.TemporalMemory: Initialized
2020-11-20 23:29:33:267 nl.zeesoft.zdk.neural.processors.TemporalMemory: Resetting TemporalMemory connections ...
2020-11-20 23:29:33:279 nl.zeesoft.zdk.neural.processors.TemporalMemory: Reset connections
2020-11-20 23:29:33:299 nl.zeesoft.zdk.neural.processors.SpatialPooler: Initialized
2020-11-20 23:29:33:299 nl.zeesoft.zdk.neural.processors.SpatialPooler: Resetting SpatialPooler connections ...
2020-11-20 23:29:33:361 nl.zeesoft.zdk.neural.processors.SpatialPooler: Reset connections
2020-11-20 23:29:33:362 nl.zeesoft.zdk.neural.network.Network: Initialized network

2020-11-20 23:29:33:362 nl.zeesoft.zdk.test.neural.TestNetwork: Processing 100 SDRs ...
2020-11-20 23:29:37:319 nl.zeesoft.zdk.test.neural.TestNetwork: Processed 100 SDRs

2020-11-20 23:29:37:326 nl.zeesoft.zdk.neural.network.Network: Saving network ...
2020-11-20 23:29:37:894 nl.zeesoft.zdk.neural.network.Network: Saved network
2020-11-20 23:29:37:894 nl.zeesoft.zdk.neural.network.Network: Loading network ...
2020-11-20 23:29:41:912 nl.zeesoft.zdk.neural.network.Network: Loaded network

Processor: EN
-> 16;16;1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16
-> SDR##1;1;@value#java.lang.Integer#1
Processor: SP
-> 48;48;937,202,655,535,205,1617,446,447,270,1195,752,1506,544,296,805,2136,1613,1559,9,213,954,1703,163,2088,1998,672,1396,124,498,1855,636,1951,1560,1063,766,1366,1502,516,693,1292,855,797,125,638,760,38
Processor: TM
-> 768;48;1984,1985,1986,1987,1988,1989,1990,1991,1992,1993,1994,1995,1996,1997,1998,1999,4744,8256,8257,8258,8259,8260,8261,8262,8263,8264,8265,8266,8267,8268,8269,8270,8271,8715,10176,10177,10178,10179,10180,10181,10182,10183,10184,10185,10186,10187,10188,10189,10190,10191,10752,10753,10754,10755,10756,10757,10758,10759,10760,10761,10762,10763,10764,10765,10766,10767,12045,12160,12161,12162,12163,12164,12165,12166,12167,12168,12169,12170,12171,12172,12173,12174,12175,144,145,146,147,148,149,150,151,152,153,154,155,156,157,158,159,608,609,610,611,612,613,614,615,616,617,618,619,620,621,622,623,2000,2001,2002,2003,2004,2005,2006,2007,2008,2009,2010,2011,2012,2013,2014,2015,20672,20673,20674,20675,20676,20677,20678,20679,20680,20681,20682,20683,20684,20685,20686,20687,3240,3282,3408,3409,3410,3411,3412,3413,3414,3415,3416,3417,3418,3419,3420,3421,3422,3423,4325,2608,2609,2610,2611,2612,2613,2614,2615,2616,2617,2618,2619,2620,2621,2622,2623,22336,22337,22338,22339,22340,22341,22342,22343,22344,22345,22346,22347,22348,22349,22350,22351,7136,7968,7969,7970,7971,7972,7973,7974,7975,7976,7977,7978,7979,7980,7981,7982,7983,24960,24961,24962,24963,24964,24965,24966,24967,24968,24969,24970,24971,24972,24973,24974,24975,7157,10208,10209,10210,10211,10212,10213,10214,10215,10216,10217,10218,10219,10220,10221,10222,10223,8560,8561,8562,8563,8564,8565,8566,8567,8568,8569,8570,8571,8572,8573,8574,8575,11088,11089,11090,11091,11092,11093,11094,11095,11096,11097,11098,11099,11100,11101,11102,11103,10483,12256,12257,12258,12259,12260,12261,12262,12263,12264,12265,12266,12267,12268,12269,12270,12271,12752,12753,12754,12755,12756,12757,12758,12759,12760,12761,12762,12763,12764,12765,12766,12767,12887,13680,13681,13682,13683,13684,13685,13686,13687,13688,13689,13690,13691,13692,13693,13694,13695,33408,33409,33410,33411,33412,33413,33414,33415,33416,33417,33418,33419,33420,33421,33422,33423,14997,15264,15265,15266,15267,15268,15269,15270,15271,15272,15273,15274,15275,15276,15277,15278,15279,34176,34177,34178,34179,34180,34181,34182,34183,34184,34185,34186,34187,34188,34189,34190,34191,17008,17009,17010,17011,17012,17013,17014,17015,17016,17017,17018,17019,17020,17021,17022,17023,19122,21856,21857,21858,21859,21860,21861,21862,21863,21864,21865,21866,21867,21868,21869,21870,21871,24032,24101,24944,24945,24946,24947,24948,24949,24950,24951,24952,24953,24954,24955,24956,24957,24958,24959,25808,25809,25810,25811,25812,25813,25814,25815,25816,25817,25818,25819,25820,25821,25822,25823,25886,31972,27248,27249,27250,27251,27252,27253,27254,27255,27256,27257,27258,27259,27260,27261,27262,27263,29680,29681,29682,29683,29684,29685,29686,29687,29688,29689,29690,29691,29692,29693,29694,29695,31216,31217,31218,31219,31220,31221,31222,31223,31224,31225,31226,31227,31228,31229,31230,31231
-> 48;48;9,38,124,125,163,213,498,516,535,636,638,672,693,760,766,797,855,954,1063,1292,1366,1396,1559,1560,1613,1703,1855,1951,2088,2136
-> 768;48;
-> 768;48;1987,4744,8268,8715,10190,10766,12045,12166,148,616,2000,3240,3282,20673,4325,3412,2616,22340,7136,7981,24964,7157,10217,8566,11102,10483,12258,12758,12887,14997,13694,33418,15266,34181,17014,19122,21870,24032,24101,24945,25810,25886,31972,27256,29688,31220
Processor: CL
-> SDR##1;1;@accuracy#java.lang.Float#1.0@accuracyTrend#java.lang.Float#1.0@classification:1#nl.zeesoft.zdk.neural.processors.Classification#1;value;java.lang.Integer;0,2306%1,689
~~~~

Test results
------------
All 15 tests have been executed successfully (278 assertions).  
Total test duration: 56175 ms (total sleep duration: 176 ms).  

Memory usage per test;  
 * nl.zeesoft.zdk.test.TestStr: 674 Kb / 0 Mb
 * nl.zeesoft.zdk.test.thread.TestRunCode: 435 Kb / 0 Mb
 * nl.zeesoft.zdk.test.thread.TestCodeRunnerChain: 437 Kb / 0 Mb
 * nl.zeesoft.zdk.test.collection.TestCollections: 465 Kb / 0 Mb
 * nl.zeesoft.zdk.test.http.TestHttpServer: 809 Kb / 0 Mb
 * nl.zeesoft.zdk.test.grid.TestGrid: 798 Kb / 0 Mb
 * nl.zeesoft.zdk.test.neural.TestSDR: 814 Kb / 0 Mb
 * nl.zeesoft.zdk.test.neural.TestCellGrid: 875 Kb / 0 Mb
 * nl.zeesoft.zdk.test.neural.TestScalarEncoder: 878 Kb / 0 Mb
 * nl.zeesoft.zdk.test.neural.TestSpatialPooler: 879 Kb / 0 Mb
 * nl.zeesoft.zdk.test.neural.TestTemporalMemory: 893 Kb / 0 Mb
 * nl.zeesoft.zdk.test.neural.TestClassifier: 904 Kb / 0 Mb
 * nl.zeesoft.zdk.test.neural.TestMerger: 914 Kb / 0 Mb
 * nl.zeesoft.zdk.test.neural.TestProcessorFactory: 915 Kb / 0 Mb
 * nl.zeesoft.zdk.test.neural.TestNetwork: 946 Kb / 0 Mb
