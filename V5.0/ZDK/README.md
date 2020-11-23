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
2020-11-23 03:25:08:735 nl.zeesoft.zdk.http.HttpServer: Opening server socket on port: 8080 ...
2020-11-23 03:25:08:735 nl.zeesoft.zdk.http.HttpServer: Opened server socket on port: 8080
2020-11-23 03:25:08:761 nl.zeesoft.zdk.http.HttpServer$1: Request/response headers (Port: 8080);
<<<
GET / HTTP/1.1
Content-Type: text/html
Host: 127.0.0.1
Connection: keep-alive
>>>
HTTP/1.1 200 OK
Content-Length: 47
2020-11-23 03:25:08:763 nl.zeesoft.zdk.http.HttpServer$1: Request/response headers (Port: 8080);
<<<
GET /pizza.txt HTTP/1.1
Content-Type: text/html
Host: 127.0.0.1
Connection: keep-alive
>>>
HTTP/1.1 404 File not found: http/pizza.txt
Content-Length: 30
2020-11-23 03:25:08:767 nl.zeesoft.zdk.http.HttpServer$1: Request/response headers (Port: 8080);
<<<
PUT /pizza.txt HTTP/1.1
Content-Type: text/html
Host: 127.0.0.1
Connection: keep-alive
Content-Length: 13
>>>
HTTP/1.1 200 OK
2020-11-23 03:25:08:770 nl.zeesoft.zdk.http.HttpServer$1: Request/response headers (Port: 8080);
<<<
GET /pizza.txt HTTP/1.1
Content-Type: text/html
Host: 127.0.0.1
Connection: keep-alive
>>>
HTTP/1.1 200 OK
Content-Length: 13
2020-11-23 03:25:08:773 nl.zeesoft.zdk.http.HttpServer$1: Request/response headers (Port: 8080);
<<<
DELETE /pizza.txt HTTP/1.1
Content-Type: text/html
Host: 127.0.0.1
Connection: keep-alive
>>>
HTTP/1.1 200 OK
2020-11-23 03:25:08:774 nl.zeesoft.zdk.http.HttpServer: Opening server socket on port: 9090 ...
2020-11-23 03:25:08:774 nl.zeesoft.zdk.http.HttpServer: Opened server socket on port: 9090
2020-11-23 03:25:08:778 nl.zeesoft.zdk.http.HttpServer$1: Request/response headers (Port: 8080);
<<<
GET / HTTP/1.1
Host: 127.0.0.1:8080
Content-Type: text/html
Connection: keep-alive
>>>
HTTP/1.1 200 OK
Content-Length: 47
2020-11-23 03:25:08:779 nl.zeesoft.zdk.http.HttpServer$1: Request/response headers (Port: 9090);
<<<
GET / HTTP/1.1
Host: 127.0.0.1:8080
Content-Type: text/html
Connection: keep-alive
>>>
HTTP/1.1 200 OK
Content-Length: 47
2020-11-23 03:25:08:817 nl.zeesoft.zdk.http.HttpServer: Closing connections ...
2020-11-23 03:25:08:817 nl.zeesoft.zdk.http.HttpServer: Closed connections
2020-11-23 03:25:08:817 nl.zeesoft.zdk.http.HttpServer: Closing server socket on port: 9090 ...
2020-11-23 03:25:08:817 nl.zeesoft.zdk.http.HttpServer: Closed server socket on port: 9090
2020-11-23 03:25:08:833 nl.zeesoft.zdk.http.HttpServer: Closing connections ...
2020-11-23 03:25:08:833 nl.zeesoft.zdk.http.HttpServer: Closed connections
2020-11-23 03:25:08:833 nl.zeesoft.zdk.http.HttpServer: Closing server socket on port: 8080 ...
2020-11-23 03:25:08:833 nl.zeesoft.zdk.http.HttpServer: Closed server socket on port: 8080

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
2020-11-23 03:25:09:017 nl.zeesoft.zdk.neural.processors.ScalarEncoder: Initializing ScalarEncoder ...
2020-11-23 03:25:09:017 nl.zeesoft.zdk.neural.processors.ScalarEncoder: Initialized
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
2020-11-23 03:25:09:076 nl.zeesoft.zdk.neural.processors.SpatialPooler: Initializing SpatialPooler ...
2020-11-23 03:25:09:154 nl.zeesoft.zdk.neural.processors.SpatialPooler: Initialized
2020-11-23 03:25:09:154 nl.zeesoft.zdk.neural.processors.SpatialPooler: Resetting SpatialPooler connections ...
2020-11-23 03:25:09:295 nl.zeesoft.zdk.neural.processors.SpatialPooler: Reset connections

2020-11-23 03:25:09:295 nl.zeesoft.zdk.test.neural.TestSpatialPooler: Initializing spatial pooler (asynchronous) ...
2020-11-23 03:25:09:717 nl.zeesoft.zdk.test.neural.TestSpatialPooler: Initialized spatial pooler (asynchronous)

Input SDR: 16;16;0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15
Output SDR: 48;48;4,29,40,76,121,196,227,300,347,401,477,522,530,587,593,601,679,770,780,865,872,903,911,985,987,1005,1015,1128,1164,1237,1277,1321,1339,1448,1460,1466,1500,1519,1546,1595,1651,1655,1669,1710,1944,2028
Input SDR: 16;16;0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15
Output SDR: 48;48;4,40,76,121,181,196,227,300,347,401,435,522,530,539,587,679,770,780,815,820,865,903,985,987,989,1015,1128,1164,1237,1277,1315,1321,1339,1448,1498,1500,1519,1546,1595,1645,1651,1669,1710,1713,1944,2028
Input SDR: 16;16;0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15
Output SDR: 48;48;4,29,40,76,121,181,196,227,300,347,401,435,522,530,539,587,593,679,770,780,815,865,903,911,985,987,989,1015,1128,1164,1237,1277,1321,1339,1448,1466,1498,1500,1519,1546,1595,1651,1669,1710,1713,1944
Input SDR: 16;16;0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15
Output SDR: 48;48;4,29,40,76,121,181,196,227,300,347,401,435,522,530,539,587,593,679,770,780,815,865,903,911,985,987,989,1015,1128,1164,1237,1277,1321,1339,1448,1466,1498,1500,1519,1546,1595,1651,1669,1710,1713,1944
Input SDR: 16;16;0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15
Output SDR: 48;48;4,29,40,76,121,181,196,227,300,347,401,435,522,530,539,587,593,679,770,780,815,865,903,911,985,987,989,1015,1128,1164,1237,1277,1321,1339,1448,1466,1498,1500,1519,1546,1595,1651,1669,1710,1713,1944

Average overlap for similar inputs: 45.0, overall: 2.0
~~~~

nl.zeesoft.zdk.test.neural.TestTemporalMemory
---------------------------------------------
This test shows how to use a *TemporalMemory* to learn SDR sequences.
A *TemporalMemoryConfig* can be used to configure the *TemporalMemory* before initialization.
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
2020-11-23 03:25:19:340 nl.zeesoft.zdk.neural.processors.TemporalMemory: Initializing TemporalMemory ...
2020-11-23 03:25:19:340 nl.zeesoft.zdk.neural.processors.TemporalMemory: Initialized
2020-11-23 03:25:19:340 nl.zeesoft.zdk.neural.processors.TemporalMemory: Resetting TemporalMemory connections ...
2020-11-23 03:25:19:355 nl.zeesoft.zdk.neural.processors.TemporalMemory: Reset connections
2020-11-23 03:25:19:402 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 1 > bursting: 46, active: 736, winners: 46, predictive: 0
2020-11-23 03:25:19:423 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 2 > bursting: 46, active: 736, winners: 46, predictive: 0
2020-11-23 03:25:19:441 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 3 > bursting: 46, active: 736, winners: 46, predictive: 0
2020-11-23 03:25:19:457 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 4 > bursting: 46, active: 736, winners: 46, predictive: 0
2020-11-23 03:25:19:484 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 5 > bursting: 46, active: 736, winners: 46, predictive: 0
2020-11-23 03:25:19:506 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 6 > bursting: 46, active: 736, winners: 46, predictive: 0
2020-11-23 03:25:19:522 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 7 > bursting: 46, active: 736, winners: 46, predictive: 0
2020-11-23 03:25:19:540 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 8 > bursting: 46, active: 736, winners: 46, predictive: 0
2020-11-23 03:25:19:549 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 9 > bursting: 46, active: 736, winners: 46, predictive: 0
2020-11-23 03:25:19:559 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 10 > bursting: 46, active: 736, winners: 46, predictive: 0
2020-11-23 03:25:19:568 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 11 > bursting: 46, active: 736, winners: 46, predictive: 0
2020-11-23 03:25:19:577 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 12 > bursting: 46, active: 736, winners: 46, predictive: 0
2020-11-23 03:25:19:585 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 13 > bursting: 46, active: 736, winners: 46, predictive: 0
2020-11-23 03:25:19:602 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 14 > bursting: 46, active: 736, winners: 46, predictive: 0
2020-11-23 03:25:19:611 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 15 > bursting: 46, active: 736, winners: 46, predictive: 0
2020-11-23 03:25:19:619 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 16 > bursting: 46, active: 736, winners: 46, predictive: 0
2020-11-23 03:25:19:634 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 17 > bursting: 46, active: 736, winners: 46, predictive: 40
2020-11-23 03:25:19:647 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 18 > bursting: 6, active: 136, winners: 46, predictive: 41
2020-11-23 03:25:19:658 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 19 > bursting: 5, active: 121, winners: 46, predictive: 41
2020-11-23 03:25:19:674 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 20 > bursting: 5, active: 121, winners: 46, predictive: 43
2020-11-23 03:25:19:685 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 21 > bursting: 3, active: 91, winners: 46, predictive: 3
2020-11-23 03:25:19:713 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 22 > bursting: 43, active: 691, winners: 46, predictive: 46
2020-11-23 03:25:19:723 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 23 > bursting: 0, active: 46, winners: 46, predictive: 45
2020-11-23 03:25:19:733 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 24 > bursting: 1, active: 61, winners: 46, predictive: 45
2020-11-23 03:25:19:742 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 25 > bursting: 1, active: 61, winners: 46, predictive: 6
2020-11-23 03:25:19:771 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 26 > bursting: 40, active: 646, winners: 46, predictive: 46
2020-11-23 03:25:19:781 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 27 > bursting: 0, active: 46, winners: 46, predictive: 46
2020-11-23 03:25:19:792 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 28 > bursting: 0, active: 46, winners: 46, predictive: 46
2020-11-23 03:25:19:801 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 29 > bursting: 0, active: 46, winners: 46, predictive: 6
2020-11-23 03:25:19:828 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 30 > bursting: 40, active: 646, winners: 46, predictive: 46
2020-11-23 03:25:19:839 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 31 > bursting: 0, active: 46, winners: 46, predictive: 46
2020-11-23 03:25:19:847 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 32 > bursting: 0, active: 46, winners: 46, predictive: 46
2020-11-23 03:25:19:854 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 33 > bursting: 0, active: 46, winners: 46, predictive: 6
2020-11-23 03:25:19:865 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 34 > bursting: 40, active: 646, winners: 46, predictive: 46
2020-11-23 03:25:19:874 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 35 > bursting: 0, active: 46, winners: 46, predictive: 46
2020-11-23 03:25:19:909 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 36 > bursting: 0, active: 46, winners: 46, predictive: 46
2020-11-23 03:25:19:917 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 37 > bursting: 0, active: 46, winners: 46, predictive: 43
2020-11-23 03:25:19:926 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 38 > bursting: 3, active: 91, winners: 46, predictive: 0
2020-11-23 03:25:19:944 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 39 > bursting: 46, active: 736, winners: 46, predictive: 46
2020-11-23 03:25:19:952 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 40 > bursting: 0, active: 46, winners: 46, predictive: 46
2020-11-23 03:25:19:959 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 41 > bursting: 0, active: 46, winners: 46, predictive: 45
2020-11-23 03:25:19:978 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 42 > bursting: 1, active: 61, winners: 46, predictive: 0
2020-11-23 03:25:19:994 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 43 > bursting: 46, active: 736, winners: 46, predictive: 46
2020-11-23 03:25:20:018 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 44 > bursting: 0, active: 46, winners: 46, predictive: 46
2020-11-23 03:25:20:027 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 45 > bursting: 0, active: 46, winners: 46, predictive: 46
2020-11-23 03:25:20:041 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 46 > bursting: 0, active: 46, winners: 46, predictive: 2
2020-11-23 03:25:20:068 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 47 > bursting: 44, active: 706, winners: 46, predictive: 46
2020-11-23 03:25:20:077 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 48 > bursting: 0, active: 46, winners: 46, predictive: 46
2020-11-23 03:25:20:086 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 49 > bursting: 0, active: 46, winners: 46, predictive: 46
2020-11-23 03:25:20:094 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 50 > bursting: 0, active: 46, winners: 46, predictive: 6
2020-11-23 03:25:20:114 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 51 > bursting: 40, active: 646, winners: 46, predictive: 46
2020-11-23 03:25:20:120 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 52 > bursting: 0, active: 46, winners: 46, predictive: 46
2020-11-23 03:25:20:138 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 53 > bursting: 0, active: 46, winners: 46, predictive: 46
2020-11-23 03:25:20:145 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 54 > bursting: 0, active: 46, winners: 46, predictive: 41
2020-11-23 03:25:20:153 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 55 > bursting: 5, active: 121, winners: 46, predictive: 41
2020-11-23 03:25:20:163 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 56 > bursting: 5, active: 121, winners: 46, predictive: 46
2020-11-23 03:25:20:173 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 57 > bursting: 0, active: 46, winners: 46, predictive: 46
2020-11-23 03:25:20:193 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 58 > bursting: 0, active: 46, winners: 46, predictive: 44
2020-11-23 03:25:20:202 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 59 > bursting: 2, active: 76, winners: 46, predictive: 37
2020-11-23 03:25:20:214 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 60 > bursting: 9, active: 181, winners: 46, predictive: 46
2020-11-23 03:25:20:223 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 61 > bursting: 0, active: 46, winners: 46, predictive: 46
2020-11-23 03:25:20:231 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 62 > bursting: 0, active: 46, winners: 46, predictive: 45
2020-11-23 03:25:20:239 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 63 > bursting: 1, active: 61, winners: 46, predictive: 34
2020-11-23 03:25:20:268 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 64 > bursting: 12, active: 226, winners: 46, predictive: 46
2020-11-23 03:25:20:278 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 65 > bursting: 0, active: 46, winners: 46, predictive: 46
2020-11-23 03:25:20:288 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 66 > bursting: 0, active: 46, winners: 46, predictive: 46
2020-11-23 03:25:20:297 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 67 > bursting: 0, active: 46, winners: 46, predictive: 29
2020-11-23 03:25:20:308 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 68 > bursting: 17, active: 301, winners: 46, predictive: 46
2020-11-23 03:25:20:330 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 69 > bursting: 0, active: 46, winners: 46, predictive: 46
2020-11-23 03:25:20:353 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 70 > bursting: 0, active: 46, winners: 46, predictive: 46
2020-11-23 03:25:20:360 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 71 > bursting: 0, active: 46, winners: 46, predictive: 46
2020-11-23 03:25:20:367 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 72 > bursting: 0, active: 46, winners: 46, predictive: 46
2020-11-23 03:25:20:374 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 73 > bursting: 0, active: 46, winners: 46, predictive: 46
2020-11-23 03:25:20:380 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 74 > bursting: 0, active: 46, winners: 46, predictive: 46
2020-11-23 03:25:20:563 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 100 > bursting: 0, active: 46, winners: 46, predictive: 46
2020-11-23 03:25:20:832 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 150 > bursting: 0, active: 46, winners: 46, predictive: 46
2020-11-23 03:25:21:076 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 200 > bursting: 0, active: 46, winners: 46, predictive: 46
2020-11-23 03:25:21:431 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 250 > bursting: 0, active: 46, winners: 46, predictive: 46
2020-11-23 03:25:21:701 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 300 > bursting: 0, active: 46, winners: 46, predictive: 46
2020-11-23 03:25:21:949 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 350 > bursting: 0, active: 46, winners: 46, predictive: 46
2020-11-23 03:25:22:207 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 400 > bursting: 0, active: 46, winners: 46, predictive: 46
2020-11-23 03:25:22:459 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 450 > bursting: 0, active: 46, winners: 46, predictive: 46
2020-11-23 03:25:22:704 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 500 > bursting: 0, active: 46, winners: 46, predictive: 46
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
2020-11-23 03:25:22:806 nl.zeesoft.zdk.neural.processors.Classifier: Initializing Classifier ...
2020-11-23 03:25:22:806 nl.zeesoft.zdk.neural.processors.Classifier: Initialized

2020-11-23 03:25:22:846 nl.zeesoft.zdk.neural.processors.ClassifierStep: Dividing step 1 classifier value counts by two ...
2020-11-23 03:25:22:878 nl.zeesoft.zdk.neural.processors.ClassifierStep: Dividing step 1 classifier value counts by two ...
2020-11-23 03:25:22:908 nl.zeesoft.zdk.neural.processors.ClassifierStep: Dividing step 1 classifier value counts by two ...

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
2020-11-23 03:25:22:939 nl.zeesoft.zdk.neural.processors.Merger: Initializing Merger ...
2020-11-23 03:25:22:939 nl.zeesoft.zdk.neural.processors.Merger: Initialized

Merged and distorted;
10011100
00000000
00000110
11000000
00010000
00010000
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
2020-11-23 03:25:23:049 nl.zeesoft.zdk.neural.processors.SpatialPooler: Initializing SpatialPooler ...
2020-11-23 03:25:23:096 nl.zeesoft.zdk.neural.processors.SpatialPooler: Initialized
2020-11-23 03:25:23:096 nl.zeesoft.zdk.neural.processors.SpatialPooler: Resetting SpatialPooler connections ...
2020-11-23 03:25:23:158 nl.zeesoft.zdk.neural.processors.SpatialPooler: Reset connections
2020-11-23 03:25:23:158 nl.zeesoft.zdk.neural.processors.TemporalMemory: Initializing TemporalMemory ...
2020-11-23 03:25:23:158 nl.zeesoft.zdk.neural.processors.TemporalMemory: Initialized
2020-11-23 03:25:23:158 nl.zeesoft.zdk.neural.processors.Classifier: Initializing Classifier ...
2020-11-23 03:25:23:158 nl.zeesoft.zdk.neural.processors.Classifier: Initialized

Processing ...
2020-11-23 03:25:25:391 nl.zeesoft.zdk.test.neural.TestProcessorFactory: 768;48;112,113,114,115,116,117,118,119,120,121,122,123,124,125,126,127,2000,2001,2002,2003,2004,2005,2006,2007,2008,2009,2010,2011,2012,2013,2014,2015,2192,2193,2194,2195,2196,2197,2198,2199,2200,2201,2202,2203,2204,2205,2206,2207,3136,3137,3138,3139,3140,3141,3142,3143,3144,3145,3146,3147,3148,3149,3150,3151,3376,3377,3378,3379,3380,3381,3382,3383,3384,3385,3386,3387,3388,3389,3390,3391,3712,3713,3714,3715,3716,3717,3718,3719,3720,3721,3722,3723,3724,3725,3726,3727,3824,3825,3826,3827,3828,3829,3830,3831,3832,3833,3834,3835,3836,3837,3838,3839,4368,4369,4370,4371,4372,4373,4374,4375,4376,4377,4378,4379,4380,4381,4382,4383,6208,6209,6210,6211,6212,6213,6214,6215,6216,6217,6218,6219,6220,6221,6222,6223,6576,6577,6578,6579,6580,6581,6582,6583,6584,6585,6586,6587,6588,6589,6590,6591,6992,6993,6994,6995,6996,6997,6998,6999,7000,7001,7002,7003,7004,7005,7006,7007,7707,7728,7729,7730,7731,7732,7733,7734,7735,7736,7737,7738,7739,7740,7741,7742,7743,7824,7825,7826,7827,7828,7829,7830,7831,7832,7833,7834,7835,7836,7837,7838,7839,7872,7873,7874,7875,7876,7877,7878,7879,7880,7881,7882,7883,7884,7885,7886,7887,8624,8625,8626,8627,8628,8629,8630,8631,8632,8633,8634,8635,8636,8637,8638,8639,8784,8785,8786,8787,8788,8789,8790,8791,8792,8793,8794,8795,8796,8797,8798,8799,9168,9169,9170,9171,9172,9173,9174,9175,9176,9177,9178,9179,9180,9181,9182,9183,10496,10497,10498,10499,10500,10501,10502,10503,10504,10505,10506,10507,10508,10509,10510,10511,11744,11745,11746,11747,11748,11749,11750,11751,11752,11753,11754,11755,11756,11757,11758,11759,12016,12017,12018,12019,12020,12021,12022,12023,12024,12025,12026,12027,12028,12029,12030,12031,13360,13361,13362,13363,13364,13365,13366,13367,13368,13369,13370,13371,13372,13373,13374,13375,13792,13793,13794,13795,13796,13797,13798,13799,13800,13801,13802,13803,13804,13805,13806,13807,13992,14165,14384,14385,14386,14387,14388,14389,14390,14391,14392,14393,14394,14395,14396,14397,14398,14399,15168,15169,15170,15171,15172,15173,15174,15175,15176,15177,15178,15179,15180,15181,15182,15183,15205,17024,17025,17026,17027,17028,17029,17030,17031,17032,17033,17034,17035,17036,17037,17038,17039,17456,17457,17458,17459,17460,17461,17462,17463,17464,17465,17466,17467,17468,17469,17470,17471,19600,19601,19602,19603,19604,19605,19606,19607,19608,19609,19610,19611,19612,19613,19614,19615,20128,20129,20130,20131,20132,20133,20134,20135,20136,20137,20138,20139,20140,20141,20142,20143,20976,20977,20978,20979,20980,20981,20982,20983,20984,20985,20986,20987,20988,20989,20990,20991,21024,21025,21026,21027,21028,21029,21030,21031,21032,21033,21034,21035,21036,21037,21038,21039,21040,21041,21042,21043,21044,21045,21046,21047,21048,21049,21050,21051,21052,21053,21054,21055,21376,21377,21378,21379,21380,21381,21382,21383,21384,21385,21386,21387,21388,21389,21390,21391,21952,21953,21954,21955,21956,21957,21958,21959,21960,21961,21962,21963,21964,21965,21966,21967,23296,23297,23298,23299,23300,23301,23302,23303,23304,23305,23306,23307,23308,23309,23310,23311,23360,23361,23362,23363,23364,23365,23366,23367,23368,23369,23370,23371,23372,23373,23374,23375,24784,24785,24786,24787,24788,24789,24790,24791,24792,24793,24794,24795,24796,24797,24798,24799,24864,24865,24866,24867,24868,24869,24870,24871,24872,24873,24874,24875,24876,24877,24878,24879,25904,25905,25906,25907,25908,25909,25910,25911,25912,25913,25914,25915,25916,25917,25918,25919,27152,27153,27154,27155,27156,27157,27158,27159,27160,27161,27162,27163,27164,27165,27166,27167,28624,28625,28626,28627,28628,28629,28630,28631,28632,28633,28634,28635,28636,28637,28638,28639,28720,28721,28722,28723,28724,28725,28726,28727,28728,28729,28730,28731,28732,28733,28734,28735,29328,29329,29330,29331,29332,29333,29334,29335,29336,29337,29338,29339,29340,29341,29342,29343
2020-11-23 03:25:28:585 nl.zeesoft.zdk.test.neural.TestProcessorFactory: 768;48;192,193,194,195,196,197,198,199,200,201,202,203,204,205,206,207,2001,3424,3425,3426,3427,3428,3429,3430,3431,3432,3433,3434,3435,3436,3437,3438,3439,3506,4208,4209,4210,4211,4212,4213,4214,4215,4216,4217,4218,4219,4220,4221,4222,4223,5120,5121,5122,5123,5124,5125,5126,5127,5128,5129,5130,5131,5132,5133,5134,5135,5600,5601,5602,5603,5604,5605,5606,5607,5608,5609,5610,5611,5612,5613,5614,5615,6209,6585,7004,7318,7707,8416,8417,8418,8419,8420,8421,8422,8423,8424,8425,8426,8427,8428,8429,8430,8431,8496,8497,8498,8499,8500,8501,8502,8503,8504,8505,8506,8507,8508,8509,8510,8511,8704,8705,8706,8707,8708,8709,8710,8711,8712,8713,8714,8715,8716,8717,8718,8719,10592,10593,10594,10595,10596,10597,10598,10599,10600,10601,10602,10603,10604,10605,10606,10607,10992,11257,11754,12029,13019,13232,13233,13234,13235,13236,13237,13238,13239,13240,13241,13242,13243,13244,13245,13246,13247,13360,13361,13362,13363,13364,13365,13366,13367,13368,13369,13370,13371,13372,13373,13374,13375,13793,14432,14433,14434,14435,14436,14437,14438,14439,14440,14441,14442,14443,14444,14445,14446,14447,15177,15248,15249,15250,15251,15252,15253,15254,15255,15256,15257,15258,15259,15260,15261,15262,15263,16034,16912,16913,16914,16915,16916,16917,16918,16919,16920,16921,16922,16923,16924,16925,16926,16927,17469,17824,17825,17826,17827,17828,17829,17830,17831,17832,17833,17834,17835,17836,17837,17838,17839,18008,19612,20128,20129,20130,20131,20132,20133,20134,20135,20136,20137,20138,20139,20140,20141,20142,20143,20224,20225,20226,20227,20228,20229,20230,20231,20232,20233,20234,20235,20236,20237,20238,20239,20439,21027,21776,21777,21778,21779,21780,21781,21782,21783,21784,21785,21786,21787,21788,21789,21790,21791,21959,23424,23425,23426,23427,23428,23429,23430,23431,23432,23433,23434,23435,23436,23437,23438,23439,24796,24869,25559,25893,27153,28726
2020-11-23 03:25:32:120 nl.zeesoft.zdk.test.neural.TestProcessorFactory: 768;48;2001,2864,2865,2866,2867,2868,2869,2870,2871,2872,2873,2874,2875,2876,2877,2878,2879,3248,3249,3250,3251,3252,3253,3254,3255,3256,3257,3258,3259,3260,3261,3262,3263,3376,3377,3378,3379,3380,3381,3382,3383,3384,3385,3386,3387,3388,3389,3390,3391,3424,3425,3426,3427,3428,3429,3430,3431,3432,3433,3434,3435,3436,3437,3438,3439,4371,6209,7004,7536,7537,7538,7539,7540,7541,7542,7543,7544,7545,7546,7547,7548,7549,7550,7551,7707,7824,7825,7826,7827,7828,7829,7830,7831,7832,7833,7834,7835,7836,7837,7838,7839,7872,7873,7874,7875,7876,7877,7878,7879,7880,7881,7882,7883,7884,7885,7886,7887,8416,8417,8418,8419,8420,8421,8422,8423,8424,8425,8426,8427,8428,8429,8430,8431,8635,10240,10241,10242,10243,10244,10245,10246,10247,10248,10249,10250,10251,10252,10253,10254,10255,10992,11745,12029,12256,12257,12258,12259,12260,12261,12262,12263,12264,12265,12266,12267,12268,12269,12270,12271,13019,13373,13793,15168,15169,15170,15171,15172,15173,15174,15175,15176,15177,15178,15179,15180,15181,15182,15183,17232,17233,17234,17235,17236,17237,17238,17239,17240,17241,17242,17243,17244,17245,17246,17247,17469,18528,18529,18530,18531,18532,18533,18534,18535,18536,18537,18538,18539,18540,18541,18542,18543,18992,18993,18994,18995,18996,18997,18998,18999,19000,19001,19002,19003,19004,19005,19006,19007,19296,19297,19298,19299,19300,19301,19302,19303,19304,19305,19306,19307,19308,19309,19310,19311,19612,19664,19665,19666,19667,19668,19669,19670,19671,19672,19673,19674,19675,19676,19677,19678,19679,20128,20129,20130,20131,20132,20133,20134,20135,20136,20137,20138,20139,20140,20141,20142,20143,20439,21003,21027,21040,21041,21042,21043,21044,21045,21046,21047,21048,21049,21050,21051,21052,21053,21054,21055,21179,21776,21777,21778,21779,21780,21781,21782,21783,21784,21785,21786,21787,21788,21789,21790,21791,21959,24384,24385,24386,24387,24388,24389,24390,24391,24392,24393,24394,24395,24396,24397,24398,24399,24796,24869,25559,25893,28032,28033,28034,28035,28036,28037,28038,28039,28040,28041,28042,28043,28044,28045,28046,28047,28726,30960,30961,30962,30963,30964,30965,30966,30967,30968,30969,30970,30971,30972,30973,30974,30975
2020-11-23 03:25:35:686 nl.zeesoft.zdk.test.neural.TestProcessorFactory: 768;48;1230,2432,2433,2434,2435,2436,2437,2438,2439,2440,2441,2442,2443,2444,2445,2446,2447,3506,4507,5568,5569,5570,5571,5572,5573,5574,5575,5576,5577,5578,5579,5580,5581,5582,5583,5614,6209,6585,7004,7536,7537,7538,7539,7540,7541,7542,7543,7544,7545,7546,7547,7548,7549,7550,7551,7707,8508,10240,10241,10242,10243,10244,10245,10246,10247,10248,10249,10250,10251,10252,10253,10254,10255,10508,10592,10593,10594,10595,10596,10597,10598,10599,10600,10601,10602,10603,10604,10605,10606,10607,10992,11193,11754,12029,12448,12449,12450,12451,12452,12453,12454,12455,12456,12457,12458,12459,12460,12461,12462,12463,13019,13373,13793,14042,14394,15177,15259,16034,17232,17233,17234,17235,17236,17237,17238,17239,17240,17241,17242,17243,17244,17245,17246,17247,17469,18008,18528,19296,19297,19298,19299,19300,19301,19302,19303,19304,19305,19306,19307,19308,19309,19310,19311,19612,20128,20129,20130,20131,20132,20133,20134,20135,20136,20137,20138,20139,20140,20141,20142,20143,20439,21003,21027,21376,21776,21777,21778,21779,21780,21781,21782,21783,21784,21785,21786,21787,21788,21789,21790,21791,24796,24869,25559,25893,25904,25905,25906,25907,25908,25909,25910,25911,25912,25913,25914,25915,25916,25917,25918,25919,28726
2020-11-23 03:25:39:267 nl.zeesoft.zdk.test.neural.TestProcessorFactory: 768;48;2001,2192,2193,2194,2195,2196,2197,2198,2199,2200,2201,2202,2203,2204,2205,2206,2207,2641,3252,3431,3872,3873,3874,3875,3876,3877,3878,3879,3880,3881,3882,3883,3884,3885,3886,3887,4222,4496,4497,4498,4499,4500,4501,4502,4503,4504,4505,4506,4507,4508,4509,4510,4511,6209,6585,7004,7707,7885,10240,10241,10242,10243,10244,10245,10246,10247,10248,10249,10250,10251,10252,10253,10254,10255,10992,11257,11754,12029,13019,13364,13793,14433,15177,16034,17469,17824,17825,17826,17827,17828,17829,17830,17831,17832,17833,17834,17835,17836,17837,17838,17839,18528,19006,19612,20139,20439,21003,21027,21959,23299,23361,24796,24869,25559,25893,27153,27277,28639,28726,29336,29456,29457,29458,29459,29460,29461,29462,29463,29464,29465,29466,29467,29468,29469,29470,29471
2020-11-23 03:25:42:747 nl.zeesoft.zdk.test.neural.TestProcessorFactory: 768;48;2001,2128,2129,2130,2131,2132,2133,2134,2135,2136,2137,2138,2139,2140,2141,2142,2143,2874,3151,4291,4371,5120,5121,5122,5123,5124,5125,5126,5127,5128,5129,5130,5131,5132,5133,5134,5135,5392,5393,5394,5395,5396,5397,5398,5399,5400,5401,5402,5403,5404,5405,5406,5407,5614,6209,6544,6545,6546,6547,6548,6549,6550,6551,6552,6553,6554,6555,6556,6557,6558,6559,7152,7153,7154,7155,7156,7157,7158,7159,7160,7161,7162,7163,7164,7165,7166,7167,7707,7728,7729,7730,7731,7732,7733,7734,7735,7736,7737,7738,7739,7740,7741,7742,7743,7830,8786,9168,9169,9170,9171,9172,9173,9174,9175,9176,9177,9178,9179,9180,9181,9182,9183,10337,10508,11745,12029,13364,13793,14394,15177,17026,17469,17824,17825,17826,17827,17828,17829,17830,17831,17832,17833,17834,17835,17836,17837,17838,17839,19612,21027,21179,21376,22224,22225,22226,22227,22228,22229,22230,22231,22232,22233,22234,22235,22236,22237,22238,22239,23361,24388,24796,24869,25893,27277,27440,27441,27442,27443,27444,27445,27446,27447,27448,27449,27450,27451,27452,27453,27454,27455,28032,28033,28034,28035,28036,28037,28038,28039,28040,28041,28042,28043,28044,28045,28046,28047,28639,28726,29336,29440,29441,29442,29443,29444,29445,29446,29447,29448,29449,29450,29451,29452,29453,29454,29455,29456,29457,29458,29459,29460,29461,29462,29463,29464,29465,29466,29467,29468,29469,29470,29471
2020-11-23 03:25:46:290 nl.zeesoft.zdk.test.neural.TestProcessorFactory: 768;48;203,2001,2128,2129,2130,2131,2132,2133,2134,2135,2136,2137,2138,2139,2140,2141,2142,2143,3431,4222,5120,5121,5122,5123,5124,5125,5126,5127,5128,5129,5130,5131,5132,5133,5134,5135,5392,5393,5394,5395,5396,5397,5398,5399,5400,5401,5402,5403,5404,5405,5406,5407,6209,6585,7004,7152,7153,7154,7155,7156,7157,7158,7159,7160,7161,7162,7163,7164,7165,7166,7167,7536,7537,7538,7539,7540,7541,7542,7543,7544,7545,7546,7547,7548,7549,7550,7551,7707,7830,8496,8497,8498,8499,8500,8501,8502,8503,8504,8505,8506,8507,8508,8509,8510,8511,8635,9344,9345,9346,9347,9348,9349,9350,9351,9352,9353,9354,9355,9356,9357,9358,9359,10337,10992,11745,12029,12452,13373,13793,15177,16913,17469,18528,18529,18530,18531,18532,18533,18534,18535,18536,18537,18538,18539,18540,18541,18542,18543,18992,18993,18994,18995,18996,18997,18998,18999,19000,19001,19002,19003,19004,19005,19006,19007,19612,20224,20225,20226,20227,20228,20229,20230,20231,20232,20233,20234,20235,20236,20237,20238,20239,20439,21003,21024,21025,21026,21027,21028,21029,21030,21031,21032,21033,21034,21035,21036,21037,21038,21039,21959,22224,22225,22226,22227,22228,22229,22230,22231,22232,22233,22234,22235,22236,22237,22238,22239,23361,24796,24864,24865,24866,24867,24868,24869,24870,24871,24872,24873,24874,24875,24876,24877,24878,24879,25893,25904,25905,25906,25907,25908,25909,25910,25911,25912,25913,25914,25915,25916,25917,25918,25919,26496,26497,26498,26499,26500,26501,26502,26503,26504,26505,26506,26507,26508,26509,26510,26511,27277,28726,29336,29456,29457,29458,29459,29460,29461,29462,29463,29464,29465,29466,29467,29468,29469,29470,29471
2020-11-23 03:25:49:879 nl.zeesoft.zdk.test.neural.TestProcessorFactory: 768;48;2001,2192,2193,2194,2195,2196,2197,2198,2199,2200,2201,2202,2203,2204,2205,2206,2207,3151,3431,3506,4222,5392,5393,5394,5395,5396,5397,5398,5399,5400,5401,5402,5403,5404,5405,5406,5407,6209,6585,7004,7707,8425,8704,8705,8706,8707,8708,8709,8710,8711,8712,8713,8714,8715,8716,8717,8718,8719,9347,9776,9777,9778,9779,9780,9781,9782,9783,9784,9785,9786,9787,9788,9789,9790,9791,10254,10592,10593,10594,10595,10596,10597,10598,10599,10600,10601,10602,10603,10604,10605,10606,10607,10992,11745,12029,13019,13364,13793,14160,14161,14162,14163,14164,14165,14166,14167,14168,14169,14170,14171,14172,14173,14174,14175,15177,16913,17469,19006,19612,19664,19665,19666,19667,19668,19669,19670,19671,19672,19673,19674,19675,19676,19677,19678,19679,20139,20224,20225,20226,20227,20228,20229,20230,20231,20232,20233,20234,20235,20236,20237,20238,20239,20439,20976,20977,20978,20979,20980,20981,20982,20983,20984,20985,20986,20987,20988,20989,20990,20991,21027,21040,21041,21042,21043,21044,21045,21046,21047,21048,21049,21050,21051,21052,21053,21054,21055,21959,23296,23297,23298,23299,23300,23301,23302,23303,23304,23305,23306,23307,23308,23309,23310,23311,24796,24869,25559,25893,27277,27440,27441,27442,27443,27444,27445,27446,27447,27448,27449,27450,27451,27452,27453,27454,27455,28726,29460
2020-11-23 03:25:53:599 nl.zeesoft.zdk.test.neural.TestProcessorFactory: 768;48;2001,2192,2193,2194,2195,2196,2197,2198,2199,2200,2201,2202,2203,2204,2205,2206,2207,2874,3829,4222,4371,5614,6209,6556,6585,7707,7738,7885,8508,8635,9174,10508,11178,11234,11745,11824,11825,11826,11827,11828,11829,11830,11831,11832,11833,11834,11835,11836,11837,11838,11839,12029,13373,13793,14042,15177,15205,17469,19006,19296,19297,19298,19299,19300,19301,19302,19303,19304,19305,19306,19307,19308,19309,19310,19311,19612,20139,20989,21027,21376,21959,22238,23361,24796,24869,25559,25893,26505,28726,29336,30970
Processing 1000 SDRs took: 34159 ms (34 ms/SDR)
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
// Create the network
Network network = new Network();
network.configure(config);
network.initialize(true);
// Turn on step 1 accuracy logging in the classifier
network.setProcessorProperty("CL", "logPredictionAccuracy", true);
// Use the network
NetworkIO io = new NetworkIO();
io.setValue("input1", 1);
network.processIO(io);
// Get the average classifier accuracy
float accuracy = getAverageClassifierAccuracy(false);
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

2020-11-23 03:25:57:332 nl.zeesoft.zdk.neural.network.Network: Initializing network ...
2020-11-23 03:25:57:336 nl.zeesoft.zdk.neural.processors.Classifier: Initializing Classifier ...
2020-11-23 03:25:57:336 nl.zeesoft.zdk.neural.processors.ScalarEncoder: Initializing ScalarEncoder ...
2020-11-23 03:25:57:336 nl.zeesoft.zdk.neural.processors.SpatialPooler: Initializing SpatialPooler ...
2020-11-23 03:25:57:337 nl.zeesoft.zdk.neural.processors.Classifier: Initialized
2020-11-23 03:25:57:336 nl.zeesoft.zdk.neural.processors.ScalarEncoder: Initialized
2020-11-23 03:25:57:361 nl.zeesoft.zdk.neural.processors.SpatialPooler: Initialized
2020-11-23 03:25:57:338 nl.zeesoft.zdk.neural.processors.TemporalMemory: Initializing TemporalMemory ...
2020-11-23 03:25:57:378 nl.zeesoft.zdk.neural.processors.SpatialPooler: Resetting SpatialPooler connections ...
2020-11-23 03:25:57:381 nl.zeesoft.zdk.neural.processors.TemporalMemory: Initialized
2020-11-23 03:25:57:382 nl.zeesoft.zdk.neural.processors.TemporalMemory: Resetting TemporalMemory connections ...
2020-11-23 03:25:57:384 nl.zeesoft.zdk.neural.processors.TemporalMemory: Reset connections
2020-11-23 03:25:57:440 nl.zeesoft.zdk.neural.processors.SpatialPooler: Reset connections
2020-11-23 03:25:57:455 nl.zeesoft.zdk.neural.network.Network: Initialized network

2020-11-23 03:25:57:455 nl.zeesoft.zdk.test.neural.TestNetwork: Processing 100 SDRs ...
2020-11-23 03:26:01:227 nl.zeesoft.zdk.test.neural.TestNetwork: Processed 100 SDRs

2020-11-23 03:26:01:430 nl.zeesoft.zdk.neural.network.Network: Saving network ...
2020-11-23 03:26:01:447 nl.zeesoft.zdk.neural.network.Network$7: Writing dist/CL.txt ...
2020-11-23 03:26:01:447 nl.zeesoft.zdk.neural.network.Network$7: Writing dist/SP.txt ...
2020-11-23 03:26:01:447 nl.zeesoft.zdk.neural.network.Network$8: Writing dist/PreviousIO.txt ...
2020-11-23 03:26:01:447 nl.zeesoft.zdk.neural.network.Network$6: Writing dist/Configuration.txt ...
2020-11-23 03:26:01:447 nl.zeesoft.zdk.neural.network.Network$7: Writing dist/TM.txt ...
2020-11-23 03:26:01:447 nl.zeesoft.zdk.neural.network.Network$7: Writing dist/EN.txt ...
2020-11-23 03:26:02:169 nl.zeesoft.zdk.neural.network.Network: Saved network

Processor: EN
-> 16;16;1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16
-> SDR##1;1;@value#java.lang.Integer#1
Processor: SP
-> 48;48;727,1098,927,418,232,1118,202,1763,73,498,1576,1412,1355,354,770,430,1380,337,746,142,296,1506,1619,9,801,293,992,377,857,1000,1328,766,136,561,494,502,1514,1716,543,579,412,528,928,94,1450,676
Processor: TM
-> 768;48;2176,2177,2178,2179,2180,2181,2182,2183,2184,2185,2186,2187,2188,2189,2190,2191,3712,3713,3714,3715,3716,3717,3718,3719,3720,3721,3722,3723,3724,3725,3726,3727,4736,4737,4738,4739,4740,4741,4742,4743,4744,4745,4746,4747,4748,4749,4750,4751,6592,6593,6594,6595,6596,6597,6598,6599,6600,6601,6602,6603,6604,6605,6606,6607,8448,8449,8450,8451,8452,8453,8454,8455,8456,8457,8458,8459,8460,8461,8462,8463,10816,10817,10818,10819,10820,10821,10822,10823,10824,10825,10826,10827,10828,10829,10830,10831,1504,1505,1506,1507,1508,1509,1510,1511,1512,1513,1514,1515,1516,1517,1518,1519,14848,14849,14850,14851,14852,14853,14854,14855,14856,14857,14858,14859,14860,14861,14862,14863,144,145,146,147,148,149,150,151,152,153,154,155,156,157,158,159,2272,2273,2274,2275,2276,2277,2278,2279,2280,2281,2282,2283,2284,2285,2286,2287,15872,15873,15874,15875,15876,15877,15878,15879,15880,15881,15882,15883,15884,15885,15886,15887,1168,1169,1170,1171,1172,1173,1174,1175,1176,1177,1178,1179,1180,1181,1182,1183,16000,16001,16002,16003,16004,16005,16006,16007,16008,16009,16010,16011,16012,16013,16014,16015,3232,3233,3234,3235,3236,3237,3238,3239,3240,3241,3242,3243,3244,3245,3246,3247,8688,8689,8690,8691,8692,8693,8694,8695,8696,8697,8698,8699,8700,8701,8702,8703,9264,9265,9266,9267,9268,9269,9270,9271,9272,9273,9274,9275,9276,9277,9278,9279,4688,4689,4690,4691,4692,4693,4694,4695,4696,4697,4698,4699,4700,4701,4702,4703,5664,5665,5666,5667,5668,5669,5670,5671,5672,5673,5674,5675,5676,5677,5678,5679,5405,6693,6032,6033,6034,6035,6036,6037,6038,6039,6040,6041,6042,6043,6044,6045,6046,6047,6880,6881,6882,6883,6884,6885,6886,6887,6888,6889,6890,6891,6892,6893,6894,6895,21248,21249,21250,21251,21252,21253,21254,21255,21256,21257,21258,21259,21260,21261,21262,21263,11632,11633,11634,11635,11636,11637,11638,11639,11640,11641,11642,11643,11644,11645,11646,11647,7904,7905,7906,7907,7908,7909,7910,7911,7912,7913,7914,7915,7916,7917,7918,7919,22085,7969,8032,8033,8034,8035,8036,8037,8038,8039,8040,8041,8042,8043,8044,8045,8046,8047,22592,22593,22594,22595,22596,22597,22598,22599,22600,22601,22602,22603,22604,22605,22606,22607,8976,8977,8978,8979,8980,8981,8982,8983,8984,8985,8986,8987,8988,8989,8990,8991,14832,14833,14834,14835,14836,14837,14838,14839,14840,14841,14842,14843,14844,14845,14846,14847,25216,25217,25218,25219,25220,25221,25222,25223,25224,25225,25226,25227,25228,25229,25230,25231,12816,12817,12818,12819,12820,12821,12822,12823,12824,12825,12826,12827,12828,12829,12830,12831,11936,11937,11938,11939,11940,11941,11942,11943,11944,11945,11946,11947,11948,11949,11950,11951,12256,12257,12258,12259,12260,12261,12262,12263,12264,12265,12266,12267,12268,12269,12270,12271,13712,13713,13714,13715,13716,13717,13718,13719,13720,13721,13722,13723,13724,13725,13726,13727,27456,27457,27458,27459,27460,27461,27462,27463,27464,27465,27466,27467,27468,27469,27470,27471,12326,21688,17577,17888,25904,25905,25906,25907,25908,25909,25910,25911,25912,25913,25914,25915,25916,25917,25918,25919,28217,23200,23201,23202,23203,23204,23205,23206,23207,23208,23209,23210,23211,23212,23213,23214,23215,24096,24097,24098,24099,24100,24101,24102,24103,24104,24105,24106,24107,24108,24109,24110,24111,24224,24225,24226,24227,24228,24229,24230,24231,24232,24233,24234,24235,24236,24237,24238,24239
-> 48;48;9,73,94,136,142,202,232,293,296,354,377,412,430,494,502,528,543,561,579,676,727,746,766,801,857,927,928,992,1000,1328,1412,1450,1506,1514,1576,1619,1716
-> 768;48;3582,3179,4868,4498,6213,10648,26531,28013
-> 768;48;2179,3722,4738,6592,8451,10829,1506,14862,148,2282,15881,1171,16008,3239,8688,9267,4692,5675,5405,6693,6042,6882,21255,11636,7910,22085,7969,8036,8984,22597,14843,25230,12817,11944,13721,12256,12326,27459,21688,17577,17888,25913,28217,23205,24100,24231
Processor: CL
-> SDR##1;1;@accuracy#java.lang.Float#0.97894734@accuracyTrend#java.lang.Float#1.0@classification:1#nl.zeesoft.zdk.neural.processors.Classification#1;value;java.lang.Integer;0,2447%1,1016
~~~~

Test results
------------
All 15 tests have been executed successfully (285 assertions).  
Total test duration: 58506 ms (total sleep duration: 176 ms).  

Memory usage and duration per test;  
 * nl.zeesoft.zdk.test.TestStr: 681 Kb / 0 Mb, 15 ms
 * nl.zeesoft.zdk.test.thread.TestRunCode: 435 Kb / 0 Mb, 42 ms
 * nl.zeesoft.zdk.test.thread.TestCodeRunnerChain: 438 Kb / 0 Mb, 139 ms
 * nl.zeesoft.zdk.test.collection.TestCollections: 465 Kb / 0 Mb, 229 ms
 * nl.zeesoft.zdk.test.http.TestHttpServer: 810 Kb / 0 Mb, 114 ms
 * nl.zeesoft.zdk.test.grid.TestGrid: 799 Kb / 0 Mb, 43 ms
 * nl.zeesoft.zdk.test.neural.TestSDR: 819 Kb / 0 Mb, 0 ms
 * nl.zeesoft.zdk.test.neural.TestCellGrid: 878 Kb / 0 Mb, 15 ms
 * nl.zeesoft.zdk.test.neural.TestScalarEncoder: 880 Kb / 0 Mb, 12 ms
 * nl.zeesoft.zdk.test.neural.TestSpatialPooler: 882 Kb / 0 Mb, 10248 ms
 * nl.zeesoft.zdk.test.neural.TestTemporalMemory: 895 Kb / 0 Mb, 3450 ms
 * nl.zeesoft.zdk.test.neural.TestClassifier: 906 Kb / 0 Mb, 133 ms
 * nl.zeesoft.zdk.test.neural.TestMerger: 916 Kb / 0 Mb, 16 ms
 * nl.zeesoft.zdk.test.neural.TestProcessorFactory: 917 Kb / 0 Mb, 34268 ms
 * nl.zeesoft.zdk.test.neural.TestNetwork: 954 Kb / 0 Mb, 9309 ms
