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
2020-11-08 23:59:31:126 nl.zeesoft.zdk.http.HttpServer: Opening server socket on port: 8080 ...
2020-11-08 23:59:31:136 nl.zeesoft.zdk.http.HttpServer: Opened server socket on port: 8080
2020-11-08 23:59:31:156 nl.zeesoft.zdk.http.HttpServer$1: Request/response headers (Port: 8080);
<<<
GET / HTTP/1.1
Content-Type: text/html
Host: 127.0.0.1
Connection: keep-alive
>>>
HTTP/1.1 200 OK
Content-Length: 47
2020-11-08 23:59:31:160 nl.zeesoft.zdk.http.HttpServer$1: Request/response headers (Port: 8080);
<<<
GET /pizza.txt HTTP/1.1
Content-Type: text/html
Host: 127.0.0.1
Connection: keep-alive
>>>
HTTP/1.1 404 File not found: http/pizza.txt
Content-Length: 30
2020-11-08 23:59:31:162 nl.zeesoft.zdk.http.HttpServer$1: Request/response headers (Port: 8080);
<<<
PUT /pizza.txt HTTP/1.1
Content-Type: text/html
Host: 127.0.0.1
Connection: keep-alive
Content-Length: 13
>>>
HTTP/1.1 200 OK
2020-11-08 23:59:31:164 nl.zeesoft.zdk.http.HttpServer$1: Request/response headers (Port: 8080);
<<<
GET /pizza.txt HTTP/1.1
Content-Type: text/html
Host: 127.0.0.1
Connection: keep-alive
>>>
HTTP/1.1 200 OK
Content-Length: 13
2020-11-08 23:59:31:166 nl.zeesoft.zdk.http.HttpServer$1: Request/response headers (Port: 8080);
<<<
DELETE /pizza.txt HTTP/1.1
Content-Type: text/html
Host: 127.0.0.1
Connection: keep-alive
>>>
HTTP/1.1 200 OK
2020-11-08 23:59:31:167 nl.zeesoft.zdk.http.HttpServer: Opening server socket on port: 9090 ...
2020-11-08 23:59:31:168 nl.zeesoft.zdk.http.HttpServer: Opened server socket on port: 9090
2020-11-08 23:59:31:171 nl.zeesoft.zdk.http.HttpServer$1: Request/response headers (Port: 8080);
<<<
GET / HTTP/1.1
Host: 127.0.0.1:8080
Content-Type: text/html
Connection: keep-alive
>>>
HTTP/1.1 200 OK
Content-Length: 47
2020-11-08 23:59:31:172 nl.zeesoft.zdk.http.HttpServer$1: Request/response headers (Port: 9090);
<<<
GET / HTTP/1.1
Host: 127.0.0.1:8080
Content-Type: text/html
Connection: keep-alive
>>>
HTTP/1.1 200 OK
Content-Length: 47
2020-11-08 23:59:31:184 nl.zeesoft.zdk.http.HttpServer: Closing connections ...
2020-11-08 23:59:31:184 nl.zeesoft.zdk.http.HttpServer: Closed connections
2020-11-08 23:59:31:184 nl.zeesoft.zdk.http.HttpServer: Closing server socket on port: 9090 ...
2020-11-08 23:59:31:185 nl.zeesoft.zdk.http.HttpServer: Closed server socket on port: 9090
2020-11-08 23:59:31:195 nl.zeesoft.zdk.http.HttpServer: Closing connections ...
2020-11-08 23:59:31:195 nl.zeesoft.zdk.http.HttpServer: Closed connections
2020-11-08 23:59:31:195 nl.zeesoft.zdk.http.HttpServer: Closing server socket on port: 8080 ...
2020-11-08 23:59:31:196 nl.zeesoft.zdk.http.HttpServer: Closed server socket on port: 8080

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
2020-11-08 23:59:31:333 nl.zeesoft.zdk.neural.processors.ScalarEncoder: Initializing ScalarEncoder ...
2020-11-08 23:59:31:333 nl.zeesoft.zdk.neural.processors.ScalarEncoder: Initialized
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
2020-11-08 23:59:31:354 nl.zeesoft.zdk.neural.processors.SpatialPooler: Initializing SpatialPooler ...
2020-11-08 23:59:31:416 nl.zeesoft.zdk.neural.processors.SpatialPooler: Initialized
2020-11-08 23:59:31:416 nl.zeesoft.zdk.neural.processors.SpatialPooler: Resetting SpatialPooler connections ...
2020-11-08 23:59:31:610 nl.zeesoft.zdk.neural.processors.SpatialPooler: Reset connections

2020-11-08 23:59:31:610 nl.zeesoft.zdk.test.neural.TestSpatialPooler: Initializing spatial pooler (asynchronous) ...
2020-11-08 23:59:32:128 nl.zeesoft.zdk.test.neural.TestSpatialPooler: Initialized spatial pooler (asynchronous)

Input SDR: 16;16;0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15
Output SDR: 48;48;35,59,125,185,202,275,304,356,371,378,512,518,551,585,588,602,614,699,716,730,747,750,757,794,821,827,969,1143,1220,1243,1267,1271,1285,1311,1371,1373,1382,1394,1458,1464,1630,1650,1662,1885,1941,2041
Input SDR: 16;16;0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15
Output SDR: 48;48;35,36,59,125,185,202,245,304,356,371,378,386,512,518,563,585,588,602,614,632,699,716,750,757,794,821,827,969,1220,1241,1243,1267,1268,1271,1283,1285,1311,1371,1373,1382,1458,1599,1650,1662,1941,2041
Input SDR: 16;16;0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15
Output SDR: 48;48;35,36,59,125,185,202,245,304,356,371,378,386,512,518,563,585,588,602,614,699,716,730,750,757,794,821,827,969,1220,1241,1243,1267,1268,1271,1283,1285,1311,1371,1373,1382,1394,1458,1599,1650,1662,1941
Input SDR: 16;16;0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15
Output SDR: 48;48;35,36,59,125,185,202,304,356,371,378,386,512,518,563,585,588,602,614,699,716,730,750,757,794,821,827,969,1220,1241,1243,1267,1268,1271,1283,1285,1311,1371,1373,1382,1394,1458,1599,1650,1662,1885,1941
Input SDR: 16;16;0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15
Output SDR: 48;48;35,36,59,125,185,202,304,356,371,378,386,512,518,563,585,588,602,614,699,716,730,750,757,794,821,827,969,1220,1241,1243,1267,1268,1271,1283,1285,1311,1371,1373,1382,1394,1458,1599,1650,1662,1885,1941

Average overlap for similar inputs: 46.0, overall: 1.0
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
2020-11-08 23:59:41:173 nl.zeesoft.zdk.neural.processors.TemporalMemory: Initializing TemporalMemory ...
2020-11-08 23:59:41:175 nl.zeesoft.zdk.neural.processors.TemporalMemory: Initialized
2020-11-08 23:59:41:176 nl.zeesoft.zdk.neural.processors.TemporalMemory: Resetting TemporalMemory connections ...
2020-11-08 23:59:41:186 nl.zeesoft.zdk.neural.processors.TemporalMemory: Reset connections
2020-11-08 23:59:41:225 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 1 > bursting: 46, active: 736, winners: 46, predictive: 0
2020-11-08 23:59:41:247 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 2 > bursting: 46, active: 736, winners: 46, predictive: 0
2020-11-08 23:59:41:278 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 3 > bursting: 46, active: 736, winners: 46, predictive: 0
2020-11-08 23:59:41:295 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 4 > bursting: 46, active: 736, winners: 46, predictive: 0
2020-11-08 23:59:41:320 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 5 > bursting: 46, active: 736, winners: 46, predictive: 0
2020-11-08 23:59:41:332 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 6 > bursting: 46, active: 736, winners: 46, predictive: 0
2020-11-08 23:59:41:349 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 7 > bursting: 46, active: 736, winners: 46, predictive: 0
2020-11-08 23:59:41:375 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 8 > bursting: 46, active: 736, winners: 46, predictive: 0
2020-11-08 23:59:41:385 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 9 > bursting: 46, active: 736, winners: 46, predictive: 0
2020-11-08 23:59:41:395 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 10 > bursting: 46, active: 736, winners: 46, predictive: 0
2020-11-08 23:59:41:405 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 11 > bursting: 46, active: 736, winners: 46, predictive: 0
2020-11-08 23:59:41:425 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 12 > bursting: 46, active: 736, winners: 46, predictive: 0
2020-11-08 23:59:41:449 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 13 > bursting: 46, active: 736, winners: 46, predictive: 0
2020-11-08 23:59:41:473 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 14 > bursting: 46, active: 736, winners: 46, predictive: 0
2020-11-08 23:59:41:511 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 15 > bursting: 46, active: 736, winners: 46, predictive: 0
2020-11-08 23:59:41:570 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 16 > bursting: 46, active: 736, winners: 46, predictive: 0
2020-11-08 23:59:41:643 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 17 > bursting: 46, active: 736, winners: 46, predictive: 40
2020-11-08 23:59:41:730 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 18 > bursting: 6, active: 136, winners: 46, predictive: 39
2020-11-08 23:59:41:759 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 19 > bursting: 7, active: 151, winners: 46, predictive: 38
2020-11-08 23:59:41:805 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 20 > bursting: 8, active: 166, winners: 46, predictive: 43
2020-11-08 23:59:41:830 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 21 > bursting: 3, active: 91, winners: 46, predictive: 5
2020-11-08 23:59:41:858 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 22 > bursting: 41, active: 661, winners: 46, predictive: 45
2020-11-08 23:59:41:869 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 23 > bursting: 1, active: 61, winners: 46, predictive: 46
2020-11-08 23:59:41:882 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 24 > bursting: 0, active: 46, winners: 46, predictive: 46
2020-11-08 23:59:41:910 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 25 > bursting: 0, active: 46, winners: 46, predictive: 6
2020-11-08 23:59:41:929 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 26 > bursting: 40, active: 646, winners: 46, predictive: 46
2020-11-08 23:59:41:947 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 27 > bursting: 0, active: 46, winners: 46, predictive: 46
2020-11-08 23:59:41:991 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 28 > bursting: 0, active: 46, winners: 46, predictive: 46
2020-11-08 23:59:42:005 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 29 > bursting: 0, active: 46, winners: 46, predictive: 6
2020-11-08 23:59:42:023 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 30 > bursting: 40, active: 646, winners: 46, predictive: 46
2020-11-08 23:59:42:044 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 31 > bursting: 0, active: 46, winners: 46, predictive: 46
2020-11-08 23:59:42:060 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 32 > bursting: 0, active: 46, winners: 46, predictive: 46
2020-11-08 23:59:42:074 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 33 > bursting: 0, active: 46, winners: 46, predictive: 6
2020-11-08 23:59:42:103 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 34 > bursting: 40, active: 646, winners: 46, predictive: 46
2020-11-08 23:59:42:122 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 35 > bursting: 0, active: 46, winners: 46, predictive: 46
2020-11-08 23:59:42:169 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 36 > bursting: 0, active: 46, winners: 46, predictive: 46
2020-11-08 23:59:42:179 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 37 > bursting: 0, active: 46, winners: 46, predictive: 39
2020-11-08 23:59:42:185 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 38 > bursting: 7, active: 151, winners: 46, predictive: 0
2020-11-08 23:59:42:203 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 39 > bursting: 46, active: 736, winners: 46, predictive: 46
2020-11-08 23:59:42:214 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 40 > bursting: 0, active: 46, winners: 46, predictive: 46
2020-11-08 23:59:42:222 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 41 > bursting: 0, active: 46, winners: 46, predictive: 43
2020-11-08 23:59:42:232 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 42 > bursting: 3, active: 91, winners: 46, predictive: 0
2020-11-08 23:59:42:252 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 43 > bursting: 46, active: 736, winners: 46, predictive: 46
2020-11-08 23:59:42:280 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 44 > bursting: 0, active: 46, winners: 46, predictive: 46
2020-11-08 23:59:42:295 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 45 > bursting: 0, active: 46, winners: 46, predictive: 45
2020-11-08 23:59:42:327 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 46 > bursting: 1, active: 61, winners: 46, predictive: 3
2020-11-08 23:59:42:367 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 47 > bursting: 43, active: 691, winners: 46, predictive: 46
2020-11-08 23:59:42:382 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 48 > bursting: 0, active: 46, winners: 46, predictive: 46
2020-11-08 23:59:42:403 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 49 > bursting: 0, active: 46, winners: 46, predictive: 46
2020-11-08 23:59:42:417 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 50 > bursting: 0, active: 46, winners: 46, predictive: 7
2020-11-08 23:59:42:447 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 51 > bursting: 39, active: 631, winners: 46, predictive: 46
2020-11-08 23:59:42:458 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 52 > bursting: 0, active: 46, winners: 46, predictive: 46
2020-11-08 23:59:42:475 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 53 > bursting: 0, active: 46, winners: 46, predictive: 46
2020-11-08 23:59:42:482 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 54 > bursting: 0, active: 46, winners: 46, predictive: 44
2020-11-08 23:59:42:495 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 55 > bursting: 2, active: 76, winners: 46, predictive: 43
2020-11-08 23:59:42:504 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 56 > bursting: 3, active: 91, winners: 46, predictive: 46
2020-11-08 23:59:42:513 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 57 > bursting: 0, active: 46, winners: 46, predictive: 46
2020-11-08 23:59:42:540 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 58 > bursting: 0, active: 46, winners: 46, predictive: 46
2020-11-08 23:59:42:584 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 59 > bursting: 0, active: 46, winners: 46, predictive: 41
2020-11-08 23:59:42:604 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 60 > bursting: 5, active: 121, winners: 46, predictive: 46
2020-11-08 23:59:42:629 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 61 > bursting: 0, active: 46, winners: 46, predictive: 46
2020-11-08 23:59:42:642 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 62 > bursting: 0, active: 46, winners: 46, predictive: 46
2020-11-08 23:59:42:662 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 63 > bursting: 0, active: 46, winners: 46, predictive: 41
2020-11-08 23:59:42:686 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 64 > bursting: 5, active: 121, winners: 46, predictive: 46
2020-11-08 23:59:42:699 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 65 > bursting: 0, active: 46, winners: 46, predictive: 46
2020-11-08 23:59:42:709 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 66 > bursting: 0, active: 46, winners: 46, predictive: 46
2020-11-08 23:59:42:723 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 67 > bursting: 0, active: 46, winners: 46, predictive: 41
2020-11-08 23:59:42:733 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 68 > bursting: 5, active: 121, winners: 46, predictive: 46
2020-11-08 23:59:42:764 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 69 > bursting: 0, active: 46, winners: 46, predictive: 46
2020-11-08 23:59:42:775 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 70 > bursting: 0, active: 46, winners: 46, predictive: 46
2020-11-08 23:59:42:784 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 71 > bursting: 0, active: 46, winners: 46, predictive: 46
2020-11-08 23:59:42:800 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 72 > bursting: 0, active: 46, winners: 46, predictive: 46
2020-11-08 23:59:42:810 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 73 > bursting: 0, active: 46, winners: 46, predictive: 46
2020-11-08 23:59:42:819 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 74 > bursting: 0, active: 46, winners: 46, predictive: 46
2020-11-08 23:59:43:117 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 100 > bursting: 0, active: 46, winners: 46, predictive: 46
2020-11-08 23:59:43:444 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 150 > bursting: 0, active: 46, winners: 46, predictive: 46
2020-11-08 23:59:43:834 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 200 > bursting: 0, active: 46, winners: 46, predictive: 46
2020-11-08 23:59:44:384 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 250 > bursting: 0, active: 46, winners: 46, predictive: 46
2020-11-08 23:59:44:718 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 300 > bursting: 0, active: 46, winners: 46, predictive: 46
2020-11-08 23:59:45:048 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 350 > bursting: 0, active: 46, winners: 46, predictive: 46
2020-11-08 23:59:45:350 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 400 > bursting: 0, active: 46, winners: 46, predictive: 46
2020-11-08 23:59:45:683 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 450 > bursting: 0, active: 46, winners: 46, predictive: 46
2020-11-08 23:59:45:986 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 500 > bursting: 0, active: 46, winners: 46, predictive: 46
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
 * *predictSteps*; List of steps to classify/predict;  
   Step 0 will classify the current input.  
   Steps greater than 0 will predict future input.  
   By default the next step will be predicted (predictSteps equals [1]).  
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
2020-11-08 23:59:46:098 nl.zeesoft.zdk.neural.processors.Classifier: Initializing Classifier ...
2020-11-08 23:59:46:099 nl.zeesoft.zdk.neural.processors.Classifier: Initialized

2020-11-08 23:59:46:147 nl.zeesoft.zdk.neural.processors.ClassifierStep: Dividing step 1 classifier value counts by two ...
2020-11-08 23:59:46:184 nl.zeesoft.zdk.neural.processors.ClassifierStep: Dividing step 1 classifier value counts by two ...
2020-11-08 23:59:46:236 nl.zeesoft.zdk.neural.processors.ClassifierStep: Dividing step 1 classifier value counts by two ...

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
2020-11-08 23:59:46:288 nl.zeesoft.zdk.neural.processors.Merger: Initializing Merger ...
2020-11-08 23:59:46:288 nl.zeesoft.zdk.neural.processors.Merger: Initialized

Merged and distorted;
11010100
00000010
00010101
11000000
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
2020-11-08 23:59:46:304 nl.zeesoft.zdk.neural.processors.SpatialPooler: Initializing SpatialPooler ...
2020-11-08 23:59:46:330 nl.zeesoft.zdk.neural.processors.SpatialPooler: Initialized
2020-11-08 23:59:46:330 nl.zeesoft.zdk.neural.processors.SpatialPooler: Resetting SpatialPooler connections ...
2020-11-08 23:59:46:395 nl.zeesoft.zdk.neural.processors.SpatialPooler: Reset connections
2020-11-08 23:59:46:395 nl.zeesoft.zdk.neural.processors.TemporalMemory: Initializing TemporalMemory ...
2020-11-08 23:59:46:398 nl.zeesoft.zdk.neural.processors.TemporalMemory: Initialized
2020-11-08 23:59:46:399 nl.zeesoft.zdk.neural.processors.Classifier: Initializing Classifier ...
2020-11-08 23:59:46:399 nl.zeesoft.zdk.neural.processors.Classifier: Initialized

Processing ...
2020-11-08 23:59:49:192 nl.zeesoft.zdk.test.neural.TestProcessorFactory: 768;48;231,1355,3160,3191,4160,4161,4162,4163,4164,4165,4166,4167,4168,4169,4170,4171,4172,4173,4174,4175,4437,4915,5688,5706,6272,6273,6274,6275,6276,6277,6278,6279,6280,6281,6282,6283,6284,6285,6286,6287,6480,6481,6482,6483,6484,6485,6486,6487,6488,6489,6490,6491,6492,6493,6494,6495,6777,8577,8813,9991,10329,10713,11600,11601,11602,11603,11604,11605,11606,11607,11608,11609,11610,11611,11612,11613,11614,11615,11728,11729,11730,11731,11732,11733,11734,11735,11736,11737,11738,11739,11740,11741,11742,11743,12615,12812,12833,12848,12849,12850,12851,12852,12853,12854,12855,12856,12857,12858,12859,12860,12861,12862,12863,14144,14145,14146,14147,14148,14149,14150,14151,14152,14153,14154,14155,14156,14157,14158,14159,15397,15795,15929,17320,17380,17806,18014,18192,18193,18194,18195,18196,18197,18198,18199,18200,18201,18202,18203,18204,18205,18206,18207,18208,18209,18210,18211,18212,18213,18214,18215,18216,18217,18218,18219,18220,18221,18222,18223,18677,18864,18865,18866,18867,18868,18869,18870,18871,18872,18873,18874,18875,18876,18877,18878,18879,20441,20604,21127,21370,21759,22722,24899,25641,27066,29381,30384,30385,30386,30387,30388,30389,30390,30391,30392,30393,30394,30395,30396,30397,30398,30399
2020-11-08 23:59:51:865 nl.zeesoft.zdk.test.neural.TestProcessorFactory: 768;48;231,1355,3191,4167,4248,4437,5502,5706,6285,6777,7417,8813,9991,10329,10713,11306,12568,14151,14752,14753,14754,14755,14756,14757,14758,14759,14760,14761,14762,14763,14764,14765,14766,14767,15232,15397,15795,15929,15984,15985,15986,15987,15988,15989,15990,15991,15992,15993,15994,15995,15996,15997,15998,15999,17320,17380,17602,17738,17904,17905,17906,17907,17908,17909,17910,17911,17912,17913,17914,17915,17916,17917,17918,17919,18014,18198,18516,18677,20441,20604,21127,21370,21759,22135,22722,25085,25641,29381,30391,31232,31233,31234,31235,31236,31237,31238,31239,31240,31241,31242,31243,31244,31245,31246,31247,34173
2020-11-08 23:59:53:863 nl.zeesoft.zdk.test.neural.TestProcessorFactory: 768;48;231,1355,3160,3191,4167,4248,4915,5688,6285,6486,6777,7996,8577,8813,9384,9618,10329,10713,11391,11603,12615,12812,12861,15397,15929,17320,17806,18014,18198,18217,18677,19048,20441,20604,21127,21370,21759,22077,22135,22722,24899,25085,25641,27066,29381,34173
2020-11-08 23:59:55:645 nl.zeesoft.zdk.test.neural.TestProcessorFactory: 768;48;231,3160,3191,3317,4248,4915,5688,5706,6285,6486,6777,7417,7996,8577,8813,9384,9991,10329,10713,11391,11603,12615,12812,12861,14151,15232,15397,15795,17380,18014,18198,18217,18677,19048,20441,21127,21370,21759,22077,22135,22722,24899,25641,27066,29381,34173
2020-11-08 23:59:57:371 nl.zeesoft.zdk.test.neural.TestProcessorFactory: 768;48;1355,3160,4915,5688,5706,6777,7417,7996,8577,8912,8913,8914,8915,8916,8917,8918,8919,8920,8921,8922,8923,8924,8925,8926,8927,9384,9991,10329,10713,11391,12568,12615,12812,14752,14753,14754,14755,14756,14757,14758,14759,14760,14761,14762,14763,14764,14765,14766,14767,15397,15929,15984,15985,15986,15987,15988,15989,15990,15991,15992,15993,15994,15995,15996,15997,15998,15999,17320,17602,17738,17806,17904,17905,17906,17907,17908,17909,17910,17911,17912,17913,17914,17915,17916,17917,17918,17919,18014,18516,18677,19048,20441,20604,21127,21759,22135,22786,23136,23137,23138,23139,23140,23141,23142,23143,23144,23145,23146,23147,23148,23149,23150,23151,24899,25085,25641,27066,29381,30304,30305,30306,30307,30308,30309,30310,30311,30312,30313,30314,30315,30316,30317,30318,30319,31232,31233,31234,31235,31236,31237,31238,31239,31240,31241,31242,31243,31244,31245,31246,31247,34173
2020-11-08 23:59:59:151 nl.zeesoft.zdk.test.neural.TestProcessorFactory: 768;48;1355,3160,3191,4167,4248,4437,4915,6285,6486,6777,7996,8813,9618,10713,11391,11740,12568,12615,12812,12861,14151,15232,15795,15929,16779,17320,17602,17738,17806,18198,18217,18516,18677,19048,20441,20604,21127,21370,21759,22135,22722,24899,25085,25641,29381,34173
2020-11-09 00:00:00:979 nl.zeesoft.zdk.test.neural.TestProcessorFactory: 768;48;2839,3160,3191,4167,4248,4437,4915,5502,5688,5706,6486,6777,7417,7996,8679,9384,9618,9991,11603,12568,12615,12812,12833,14151,15232,15795,15929,16779,17320,17380,17602,17806,18014,18677,18878,19048,20441,20604,21370,21759,22077,22722,25085,25641,29381,30528,30529,30530,30531,30532,30533,30534,30535,30536,30537,30538,30539,30540,30541,30542,30543
2020-11-09 00:00:02:757 nl.zeesoft.zdk.test.neural.TestProcessorFactory: 768;48;231,1355,4167,4248,4437,4915,5502,5706,6228,6486,6777,7417,7996,8577,8813,9384,9618,9991,10329,10713,11391,11603,12615,12812,12833,14151,15232,15397,15795,17380,17806,18014,18677,18878,20441,20604,21127,21370,21759,22077,22135,22722,24899,25641,27066,29381
2020-11-09 00:00:04:499 nl.zeesoft.zdk.test.neural.TestProcessorFactory: 768;48;231,3191,3317,4167,4248,4437,4915,5688,5706,6285,6777,7417,7996,8813,9384,9991,10329,10713,11740,12615,12812,12861,14151,15232,15397,15795,15929,17320,17806,18014,18198,18217,18677,19048,20441,20604,21127,21370,21759,22135,22722,24899,25641,29381,30391,34173
Processing 1000 SDRs took: 19981 ms (19 ms/SDR)
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

2020-11-09 00:00:06:402 nl.zeesoft.zdk.neural.network.Network: Initializing network ...
2020-11-09 00:00:06:402 nl.zeesoft.zdk.neural.processors.SpatialPooler: Initializing SpatialPooler ...
2020-11-09 00:00:06:402 nl.zeesoft.zdk.neural.processors.Classifier: Initializing Classifier ...
2020-11-09 00:00:06:402 nl.zeesoft.zdk.neural.processors.TemporalMemory: Initializing TemporalMemory ...
2020-11-09 00:00:06:404 nl.zeesoft.zdk.neural.processors.Classifier: Initialized
2020-11-09 00:00:06:402 nl.zeesoft.zdk.neural.processors.ScalarEncoder: Initializing ScalarEncoder ...
2020-11-09 00:00:06:407 nl.zeesoft.zdk.neural.processors.ScalarEncoder: Initialized
2020-11-09 00:00:06:409 nl.zeesoft.zdk.neural.processors.TemporalMemory: Initialized
2020-11-09 00:00:06:409 nl.zeesoft.zdk.neural.processors.TemporalMemory: Resetting TemporalMemory connections ...
2020-11-09 00:00:06:411 nl.zeesoft.zdk.neural.processors.TemporalMemory: Reset connections
2020-11-09 00:00:06:432 nl.zeesoft.zdk.neural.processors.SpatialPooler: Initialized
2020-11-09 00:00:06:432 nl.zeesoft.zdk.neural.processors.SpatialPooler: Resetting SpatialPooler connections ...
2020-11-09 00:00:06:497 nl.zeesoft.zdk.neural.processors.SpatialPooler: Reset connections
2020-11-09 00:00:06:498 nl.zeesoft.zdk.neural.network.Network: Initialized network

2020-11-09 00:00:06:498 nl.zeesoft.zdk.test.neural.TestNetwork: Processing 100 SDRs ...
2020-11-09 00:00:10:714 nl.zeesoft.zdk.test.neural.TestNetwork: Processed 100 SDRs

2020-11-09 00:00:10:714 nl.zeesoft.zdk.neural.network.Network: Saving network ...
2020-11-09 00:00:11:433 nl.zeesoft.zdk.neural.network.Network: Saved network
2020-11-09 00:00:11:433 nl.zeesoft.zdk.neural.network.Network: Loading network ...
2020-11-09 00:00:15:745 nl.zeesoft.zdk.neural.network.Network: Loaded network

Processor: EN
-> 16;16;1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16
-> SDR##1;1;@value#java.lang.Integer#1
Processor: SP
-> 48;48;162,1001,1416,938,1806,56,1859,1945,1370,124,1319,1663,299,1141,226,1427,944,410,873,905,138,1367,1128,1415,1094,1187,1269,1070,1114,957,715,1896,1854,328,1710,1946,1041,643,4,768,1983,265,350,420,718,335
Processor: TM
-> 768;48;64,65,66,67,68,69,70,71,72,73,74,75,76,77,78,79,903,1984,1985,1986,1987,1988,1989,1990,1991,1992,1993,1994,1995,1996,1997,1998,1999,5248,5249,5250,5251,5252,5253,5254,5255,5256,5257,5258,5259,5260,5261,5262,5263,6720,6721,6722,6723,6724,6725,6726,6727,6728,6729,6730,6731,6732,6733,6734,6735,12288,12289,12290,12291,12292,12293,12294,12295,12296,12297,12298,12299,12300,12301,12302,12303,15104,15105,15106,15107,15108,15109,15110,15111,15112,15113,15114,15115,15116,15117,15118,15119,18048,18049,18050,18051,18052,18053,18054,18055,18056,18057,18058,18059,18060,18061,18062,18063,2208,2209,2210,2211,2212,2213,2214,2215,2216,2217,2218,2219,2220,2221,2222,2223,4240,4241,4242,4243,4244,4245,4246,4247,4248,4249,4250,4251,4252,4253,4254,4255,2592,2593,2594,2595,2596,2597,2598,2599,2600,2601,2602,2603,2604,2605,2606,2607,22656,22657,22658,22659,22660,22661,22662,22663,22664,22665,22666,22667,22668,22669,22670,22671,3616,3617,3618,3619,3620,3621,3622,3623,3624,3625,3626,3627,3628,3629,3630,3631,5600,5601,5602,5603,5604,5605,5606,5607,5608,5609,5610,5611,5612,5613,5614,5615,6560,6561,6562,6563,6564,6565,6566,6567,6568,6569,6570,6571,6572,6573,6574,6575,30336,30337,30338,30339,30340,30341,30342,30343,30344,30345,30346,30347,30348,30349,30350,30351,13968,13969,13970,13971,13972,13973,13974,13975,13976,13977,13978,13979,13980,13981,13982,13983,11488,11489,11490,11491,11492,11493,11494,11495,11496,11497,11498,11499,11500,11501,11502,11503,14480,14481,14482,14483,14484,14485,14486,14487,14488,14489,14490,14491,14492,14493,14494,14495,15312,15313,15314,15315,15316,15317,15318,15319,15320,15321,15322,15323,15324,15325,15326,15327,16016,16017,16018,16019,16020,16021,16022,16023,16024,16025,16026,16027,16028,16029,16030,16031,15008,15009,15010,15011,15012,15013,15014,15015,15016,15017,15018,15019,15020,15021,15022,15023,16656,16657,16658,16659,16660,16661,16662,16663,16664,16665,16666,16667,16668,16669,16670,16671,17120,17121,17122,17123,17124,17125,17126,17127,17128,17129,17130,17131,17132,17133,17134,17135,18256,18257,18258,18259,18260,18261,18262,18263,18264,18265,18266,18267,18268,18269,18270,18271,17504,17505,17506,17507,17508,17509,17510,17511,17512,17513,17514,17515,17516,17517,17518,17519,17824,17825,17826,17827,17828,17829,17830,17831,17832,17833,17834,17835,17836,17837,17838,17839,20304,20305,20306,20307,20308,20309,20310,20311,20312,20313,20314,20315,20316,20317,20318,20319,21920,21921,21922,21923,21924,21925,21926,21927,21928,21929,21930,21931,21932,21933,21934,21935,27360,27361,27362,27363,27364,27365,27366,27367,27368,27369,27370,27371,27372,27373,27374,27375,31120,31121,31122,31123,31124,31125,31126,31127,31128,31129,31130,31131,31132,31133,31134,31135,28896,28897,28898,28899,28900,28901,28902,28903,28904,28905,28906,28907,28908,28909,28910,28911,29664,29665,29666,29667,29668,29669,29670,29671,29672,29673,29674,29675,29676,29677,29678,29679,31136,31137,31138,31139,31140,31141,31142,31143,31144,31145,31146,31147,31148,31149,31150,31151,4784,4785,4786,4787,4788,4789,4790,4791,4792,4793,4794,4795,4796,4797,4798,4799,5360,5361,5362,5363,5364,5365,5366,5367,5368,5369,5370,5371,5372,5373,5374,5375,10288,10289,10290,10291,10292,10293,10294,10295,10296,10297,10298,10299,10300,10301,10302,10303,11440,11441,11442,11443,11444,11445,11446,11447,11448,11449,11450,11451,11452,11453,11454,11455,18992,18993,18994,18995,18996,18997,18998,18999,19000,19001,19002,19003,19004,19005,19006,19007,21104,21105,21106,21107,21108,21109,21110,21111,21112,21113,21114,21115,21116,21117,21118,21119,21872,21873,21874,21875,21876,21877,21878,21879,21880,21881,21882,21883,21884,21885,21886,21887,22640,22641,22642,22643,22644,22645,22646,22647,22648,22649,22650,22651,22652,22653,22654,22655,22832,22833,22834,22835,22836,22837,22838,22839,22840,22841,22842,22843,22844,22845,22846,22847,26608,26609,26610,26611,26612,26613,26614,26615,26616,26617,26618,26619,26620,26621,26622,26623,29744,29745,29746,29747,29748,29749,29750,29751,29752,29753,29754,29755,29756,29757,29758,29759,31728,31729,31730,31731,31732,31733,31734,31735,31736,31737,31738,31739,31740,31741,31742,31743
-> 48;48;4,124,138,162,226,265,299,328,335,350,410,420,643,715,718,768,873,905,938,944,957,1001,1041,1070,1094,1114,1128,1141,1187,1269,1319,1367,1370,1415,1416,1427,1663,1710,1806,1854,1859,1896,1945,1946,1983
-> 768;48;3172,3485,11160,9801,25822,5234,8504,9981,17015,24762
-> 768;48;65,903,1993,5258,6721,12299,15111,18060,2221,2594,4253,22663,3628,5603,6571,30340,13969,11499,14487,15319,16027,15016,16661,17131,18268,17508,17830,20312,21931,27361,31120,28904,29678,31139,4792,5370,10289,11454,19000,21105,21872,22648,22834,26609,29744,31740
Processor: CL
-> SDR##1;1;@accuracy#java.lang.Float#0.9894737@accuracyTrend#java.lang.Float#1.0@classification:1#nl.zeesoft.zdk.neural.processors.Classification#1;value;java.lang.Integer;0,2574%1,740
~~~~

Test results
------------
All 15 tests have been executed successfully (265 assertions).  
Total test duration: 45166 ms (total sleep duration: 176 ms).  

Memory usage per test;  
 * nl.zeesoft.zdk.test.TestStr: 671 Kb / 0 Mb
 * nl.zeesoft.zdk.test.thread.TestRunCode: 435 Kb / 0 Mb
 * nl.zeesoft.zdk.test.thread.TestCodeRunnerChain: 437 Kb / 0 Mb
 * nl.zeesoft.zdk.test.collection.TestCollections: 464 Kb / 0 Mb
 * nl.zeesoft.zdk.test.http.TestHttpServer: 805 Kb / 0 Mb
 * nl.zeesoft.zdk.test.grid.TestGrid: 797 Kb / 0 Mb
 * nl.zeesoft.zdk.test.neural.TestSDR: 802 Kb / 0 Mb
 * nl.zeesoft.zdk.test.neural.TestCellGrid: 872 Kb / 0 Mb
 * nl.zeesoft.zdk.test.neural.TestScalarEncoder: 874 Kb / 0 Mb
 * nl.zeesoft.zdk.test.neural.TestSpatialPooler: 876 Kb / 0 Mb
 * nl.zeesoft.zdk.test.neural.TestTemporalMemory: 890 Kb / 0 Mb
 * nl.zeesoft.zdk.test.neural.TestClassifier: 901 Kb / 0 Mb
 * nl.zeesoft.zdk.test.neural.TestMerger: 911 Kb / 0 Mb
 * nl.zeesoft.zdk.test.neural.TestProcessorFactory: 912 Kb / 0 Mb
 * nl.zeesoft.zdk.test.neural.TestNetwork: 923 Kb / 0 Mb
