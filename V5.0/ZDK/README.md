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
Reading file: 4
Reading file: 3
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
2020-11-07 23:54:51:173 nl.zeesoft.zdk.http.HttpServer: Opening server socket on port: 8080 ...
2020-11-07 23:54:51:178 nl.zeesoft.zdk.http.HttpServer: Opened server socket on port: 8080
2020-11-07 23:54:51:198 nl.zeesoft.zdk.http.HttpServer$1: Request/response headers (Port: 8080);
<<<
GET / HTTP/1.1
Content-Type: text/html
Host: 127.0.0.1
Connection: keep-alive
>>>
HTTP/1.1 200 OK
Content-Length: 47
2020-11-07 23:54:51:202 nl.zeesoft.zdk.http.HttpServer$1: Request/response headers (Port: 8080);
<<<
GET /pizza.txt HTTP/1.1
Content-Type: text/html
Host: 127.0.0.1
Connection: keep-alive
>>>
HTTP/1.1 404 File not found: http/pizza.txt
Content-Length: 30
2020-11-07 23:54:51:204 nl.zeesoft.zdk.http.HttpServer$1: Request/response headers (Port: 8080);
<<<
PUT /pizza.txt HTTP/1.1
Content-Type: text/html
Host: 127.0.0.1
Connection: keep-alive
Content-Length: 13
>>>
HTTP/1.1 200 OK
2020-11-07 23:54:51:208 nl.zeesoft.zdk.http.HttpServer$1: Request/response headers (Port: 8080);
<<<
GET /pizza.txt HTTP/1.1
Content-Type: text/html
Host: 127.0.0.1
Connection: keep-alive
>>>
HTTP/1.1 200 OK
Content-Length: 13
2020-11-07 23:54:51:210 nl.zeesoft.zdk.http.HttpServer$1: Request/response headers (Port: 8080);
<<<
DELETE /pizza.txt HTTP/1.1
Content-Type: text/html
Host: 127.0.0.1
Connection: keep-alive
>>>
HTTP/1.1 200 OK
2020-11-07 23:54:51:212 nl.zeesoft.zdk.http.HttpServer: Opening server socket on port: 9090 ...
2020-11-07 23:54:51:213 nl.zeesoft.zdk.http.HttpServer: Opened server socket on port: 9090
2020-11-07 23:54:51:216 nl.zeesoft.zdk.http.HttpServer$1: Request/response headers (Port: 8080);
<<<
GET / HTTP/1.1
Host: 127.0.0.1:8080
Content-Type: text/html
Connection: keep-alive
>>>
HTTP/1.1 200 OK
Content-Length: 47
2020-11-07 23:54:51:217 nl.zeesoft.zdk.http.HttpServer$1: Request/response headers (Port: 9090);
<<<
GET / HTTP/1.1
Host: 127.0.0.1:8080
Content-Type: text/html
Connection: keep-alive
>>>
HTTP/1.1 200 OK
Content-Length: 47
2020-11-07 23:54:51:229 nl.zeesoft.zdk.http.HttpServer: Closing connections ...
2020-11-07 23:54:51:229 nl.zeesoft.zdk.http.HttpServer: Closed connections
2020-11-07 23:54:51:229 nl.zeesoft.zdk.http.HttpServer: Closing server socket on port: 9090 ...
2020-11-07 23:54:51:230 nl.zeesoft.zdk.http.HttpServer: Closed server socket on port: 9090
2020-11-07 23:54:51:241 nl.zeesoft.zdk.http.HttpServer: Closing connections ...
2020-11-07 23:54:51:241 nl.zeesoft.zdk.http.HttpServer: Closed connections
2020-11-07 23:54:51:241 nl.zeesoft.zdk.http.HttpServer: Closing server socket on port: 8080 ...
2020-11-07 23:54:51:241 nl.zeesoft.zdk.http.HttpServer: Closed server socket on port: 8080

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
2020-11-07 23:54:51:410 nl.zeesoft.zdk.neural.processors.ScalarEncoder: Initializing ScalarEncoder ...
2020-11-07 23:54:51:411 nl.zeesoft.zdk.neural.processors.ScalarEncoder: Initialized
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
2020-11-07 23:54:51:457 nl.zeesoft.zdk.neural.processors.SpatialPooler: Initializing SpatialPooler ...
2020-11-07 23:54:51:523 nl.zeesoft.zdk.neural.processors.SpatialPooler: Initialized
2020-11-07 23:54:51:523 nl.zeesoft.zdk.neural.processors.SpatialPooler: Resetting SpatialPooler connections ...
2020-11-07 23:54:51:690 nl.zeesoft.zdk.neural.processors.SpatialPooler: Reset connections

2020-11-07 23:54:51:690 nl.zeesoft.zdk.test.neural.TestSpatialPooler: Initializing spatial pooler (asynchronous) ...
2020-11-07 23:54:52:191 nl.zeesoft.zdk.test.neural.TestSpatialPooler: Initialized spatial pooler (asynchronous)

Input SDR: 16;16;0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15
Output SDR: 48;48;6,23,42,68,71,135,138,177,242,252,262,376,377,424,519,521,526,527,607,630,637,660,673,887,890,938,968,998,1004,1006,1040,1148,1211,1241,1341,1382,1398,1435,1502,1507,1520,1565,1741,1814,1834,2135
Input SDR: 16;16;0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15
Output SDR: 48;48;23,42,68,71,135,138,177,242,252,262,287,376,377,424,519,521,526,527,607,630,637,660,673,696,738,887,938,968,998,1004,1006,1040,1148,1211,1222,1241,1341,1382,1398,1435,1502,1520,1565,1741,1856,2135
Input SDR: 16;16;0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15
Output SDR: 48;48;23,42,68,71,135,138,177,242,252,262,287,376,377,424,519,521,526,527,607,630,637,660,673,738,887,890,938,968,998,1004,1006,1040,1148,1211,1222,1241,1341,1382,1398,1435,1502,1520,1565,1741,1856,2135
Input SDR: 16;16;0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15
Output SDR: 48;48;23,42,68,71,135,138,177,242,252,262,287,376,377,424,519,521,526,527,607,630,637,660,673,738,887,890,938,968,998,1004,1006,1040,1148,1211,1222,1241,1341,1382,1398,1435,1502,1520,1565,1741,1856,2135
Input SDR: 16;16;0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15
Output SDR: 48;48;23,42,68,71,135,138,177,242,252,262,287,376,377,424,519,521,526,527,607,630,637,660,673,738,887,890,938,968,998,1004,1006,1040,1148,1211,1222,1241,1341,1382,1398,1435,1502,1520,1565,1741,1856,2135

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
2020-11-07 23:55:01:043 nl.zeesoft.zdk.neural.processors.TemporalMemory: Initializing TemporalMemory ...
2020-11-07 23:55:01:047 nl.zeesoft.zdk.neural.processors.TemporalMemory: Initialized
2020-11-07 23:55:01:047 nl.zeesoft.zdk.neural.processors.TemporalMemory: Resetting TemporalMemory connections ...
2020-11-07 23:55:01:055 nl.zeesoft.zdk.neural.processors.TemporalMemory: Reset connections
2020-11-07 23:55:01:096 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 1 > bursting: 46, active: 736, winners: 46, predictive: 0
2020-11-07 23:55:01:119 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 2 > bursting: 46, active: 736, winners: 46, predictive: 0
2020-11-07 23:55:01:146 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 3 > bursting: 46, active: 736, winners: 46, predictive: 0
2020-11-07 23:55:01:165 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 4 > bursting: 46, active: 736, winners: 46, predictive: 0
2020-11-07 23:55:01:189 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 5 > bursting: 46, active: 736, winners: 46, predictive: 0
2020-11-07 23:55:01:203 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 6 > bursting: 46, active: 736, winners: 46, predictive: 0
2020-11-07 23:55:01:220 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 7 > bursting: 46, active: 736, winners: 46, predictive: 0
2020-11-07 23:55:01:253 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 8 > bursting: 46, active: 736, winners: 46, predictive: 0
2020-11-07 23:55:01:262 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 9 > bursting: 46, active: 736, winners: 46, predictive: 0
2020-11-07 23:55:01:272 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 10 > bursting: 46, active: 736, winners: 46, predictive: 0
2020-11-07 23:55:01:282 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 11 > bursting: 46, active: 736, winners: 46, predictive: 0
2020-11-07 23:55:01:291 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 12 > bursting: 46, active: 736, winners: 46, predictive: 0
2020-11-07 23:55:01:299 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 13 > bursting: 46, active: 736, winners: 46, predictive: 0
2020-11-07 23:55:01:316 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 14 > bursting: 46, active: 736, winners: 46, predictive: 0
2020-11-07 23:55:01:325 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 15 > bursting: 46, active: 736, winners: 46, predictive: 0
2020-11-07 23:55:01:334 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 16 > bursting: 46, active: 736, winners: 46, predictive: 0
2020-11-07 23:55:01:348 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 17 > bursting: 46, active: 736, winners: 46, predictive: 46
2020-11-07 23:55:01:360 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 18 > bursting: 0, active: 46, winners: 46, predictive: 46
2020-11-07 23:55:01:374 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 19 > bursting: 0, active: 46, winners: 46, predictive: 46
2020-11-07 23:55:01:396 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 20 > bursting: 0, active: 46, winners: 46, predictive: 46
2020-11-07 23:55:01:406 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 21 > bursting: 0, active: 46, winners: 46, predictive: 0
2020-11-07 23:55:01:425 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 22 > bursting: 46, active: 736, winners: 46, predictive: 46
2020-11-07 23:55:01:435 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 23 > bursting: 0, active: 46, winners: 46, predictive: 46
2020-11-07 23:55:01:445 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 24 > bursting: 0, active: 46, winners: 46, predictive: 46
2020-11-07 23:55:01:463 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 25 > bursting: 0, active: 46, winners: 46, predictive: 0
2020-11-07 23:55:01:485 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 26 > bursting: 46, active: 736, winners: 46, predictive: 46
2020-11-07 23:55:01:494 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 27 > bursting: 0, active: 46, winners: 46, predictive: 46
2020-11-07 23:55:01:505 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 28 > bursting: 0, active: 46, winners: 46, predictive: 46
2020-11-07 23:55:01:514 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 29 > bursting: 0, active: 46, winners: 46, predictive: 0
2020-11-07 23:55:01:530 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 30 > bursting: 46, active: 736, winners: 46, predictive: 46
2020-11-07 23:55:01:551 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 31 > bursting: 0, active: 46, winners: 46, predictive: 46
2020-11-07 23:55:01:560 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 32 > bursting: 0, active: 46, winners: 46, predictive: 46
2020-11-07 23:55:01:567 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 33 > bursting: 0, active: 46, winners: 46, predictive: 0
2020-11-07 23:55:01:583 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 34 > bursting: 46, active: 736, winners: 46, predictive: 46
2020-11-07 23:55:01:594 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 35 > bursting: 0, active: 46, winners: 46, predictive: 46
2020-11-07 23:55:01:617 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 36 > bursting: 0, active: 46, winners: 46, predictive: 46
2020-11-07 23:55:01:626 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 37 > bursting: 0, active: 46, winners: 46, predictive: 46
2020-11-07 23:55:01:634 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 38 > bursting: 0, active: 46, winners: 46, predictive: 0
2020-11-07 23:55:01:649 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 39 > bursting: 46, active: 736, winners: 46, predictive: 46
2020-11-07 23:55:01:656 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 40 > bursting: 0, active: 46, winners: 46, predictive: 46
2020-11-07 23:55:01:663 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 41 > bursting: 0, active: 46, winners: 46, predictive: 46
2020-11-07 23:55:01:681 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 42 > bursting: 0, active: 46, winners: 46, predictive: 0
2020-11-07 23:55:01:696 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 43 > bursting: 46, active: 736, winners: 46, predictive: 46
2020-11-07 23:55:01:705 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 44 > bursting: 0, active: 46, winners: 46, predictive: 46
2020-11-07 23:55:01:713 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 45 > bursting: 0, active: 46, winners: 46, predictive: 46
2020-11-07 23:55:01:726 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 46 > bursting: 0, active: 46, winners: 46, predictive: 0
2020-11-07 23:55:01:751 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 47 > bursting: 46, active: 736, winners: 46, predictive: 46
2020-11-07 23:55:01:759 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 48 > bursting: 0, active: 46, winners: 46, predictive: 46
2020-11-07 23:55:01:768 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 49 > bursting: 0, active: 46, winners: 46, predictive: 46
2020-11-07 23:55:01:777 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 50 > bursting: 0, active: 46, winners: 46, predictive: 0
2020-11-07 23:55:02:330 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 100 > bursting: 0, active: 46, winners: 46, predictive: 1
2020-11-07 23:55:02:818 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 150 > bursting: 0, active: 46, winners: 46, predictive: 25
2020-11-07 23:55:03:239 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 200 > bursting: 0, active: 46, winners: 46, predictive: 40
2020-11-07 23:55:03:768 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 250 > bursting: 0, active: 46, winners: 46, predictive: 41
2020-11-07 23:55:04:135 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 300 > bursting: 0, active: 46, winners: 46, predictive: 42
2020-11-07 23:55:04:461 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 350 > bursting: 0, active: 46, winners: 46, predictive: 77
2020-11-07 23:55:04:791 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 400 > bursting: 0, active: 46, winners: 46, predictive: 70
2020-11-07 23:55:05:105 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 450 > bursting: 0, active: 46, winners: 46, predictive: 89
2020-11-07 23:55:05:431 nl.zeesoft.zdk.test.neural.TestTemporalMemory: 500 > bursting: 0, active: 46, winners: 46, predictive: 72
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
2020-11-07 23:55:05:704 nl.zeesoft.zdk.neural.processors.Classifier: Initializing Classifier ...
2020-11-07 23:55:05:705 nl.zeesoft.zdk.neural.processors.Classifier: Initialized

2020-11-07 23:55:05:758 nl.zeesoft.zdk.neural.processors.ClassifierStep: Dividing step 1 classifier value counts by two ...
2020-11-07 23:55:05:801 nl.zeesoft.zdk.neural.processors.ClassifierStep: Dividing step 1 classifier value counts by two ...
2020-11-07 23:55:05:834 nl.zeesoft.zdk.neural.processors.ClassifierStep: Dividing step 1 classifier value counts by two ...

Input SDR: 10;10;63,64,65,66,67,68,69,70,71,72,73,74,75,76,77,78
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
2020-11-07 23:55:05:883 nl.zeesoft.zdk.neural.processors.Merger: Initializing Merger ...
2020-11-07 23:55:05:883 nl.zeesoft.zdk.neural.processors.Merger: Initialized

Merged and distorted;
10101010
00100000
00001101
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
2020-11-07 23:55:05:934 nl.zeesoft.zdk.neural.processors.SpatialPooler: Initializing SpatialPooler ...
2020-11-07 23:55:05:955 nl.zeesoft.zdk.neural.processors.SpatialPooler: Initialized
2020-11-07 23:55:05:956 nl.zeesoft.zdk.neural.processors.SpatialPooler: Resetting SpatialPooler connections ...
2020-11-07 23:55:06:019 nl.zeesoft.zdk.neural.processors.SpatialPooler: Reset connections
2020-11-07 23:55:06:019 nl.zeesoft.zdk.neural.processors.TemporalMemory: Initializing TemporalMemory ...
2020-11-07 23:55:06:021 nl.zeesoft.zdk.neural.processors.TemporalMemory: Initialized
2020-11-07 23:55:06:022 nl.zeesoft.zdk.neural.processors.Classifier: Initializing Classifier ...
2020-11-07 23:55:06:023 nl.zeesoft.zdk.neural.processors.Classifier: Initialized

Processing ...
2020-11-07 23:55:08:073 nl.zeesoft.zdk.test.neural.TestProcessorFactory: 768;48;160,161,162,163,164,165,166,167,168,169,170,171,172,173,174,175,528,529,530,531,532,533,534,535,536,537,538,539,540,541,542,543,784,785,786,787,788,789,790,791,792,793,794,795,796,797,798,799,950,1408,1409,1410,1411,1412,1413,1414,1415,1416,1417,1418,1419,1420,1421,1422,1423,1504,1505,1506,1507,1508,1509,1510,1511,1512,1513,1514,1515,1516,1517,1518,1519,1728,1729,1730,1731,1732,1733,1734,1735,1736,1737,1738,1739,1740,1741,1742,1743,2432,2433,2434,2435,2436,2437,2438,2439,2440,2441,2442,2443,2444,2445,2446,2447,2944,2945,2946,2947,2948,2949,2950,2951,2952,2953,2954,2955,2956,2957,2958,2959,3936,3937,3938,3939,3940,3941,3942,3943,3944,3945,3946,3947,3948,3949,3950,3951,4528,4529,4530,4531,4532,4533,4534,4535,4536,4537,4538,4539,4540,4541,4542,4543,5392,5393,5394,5395,5396,5397,5398,5399,5400,5401,5402,5403,5404,5405,5406,5407,5520,5521,5522,5523,5524,5525,5526,5527,5528,5529,5530,5531,5532,5533,5534,5535,6016,6017,6018,6019,6020,6021,6022,6023,6024,6025,6026,6027,6028,6029,6030,6031,7344,7345,7346,7347,7348,7349,7350,7351,7352,7353,7354,7355,7356,7357,7358,7359,7568,7569,7570,7571,7572,7573,7574,7575,7576,7577,7578,7579,7580,7581,7582,7583,9705,9824,9825,9826,9827,9828,9829,9830,9831,9832,9833,9834,9835,9836,9837,9838,9839,10432,10433,10434,10435,10436,10437,10438,10439,10440,10441,10442,10443,10444,10445,10446,10447,11792,11793,11794,11795,11796,11797,11798,11799,11800,11801,11802,11803,11804,11805,11806,11807,11840,11841,11842,11843,11844,11845,11846,11847,11848,11849,11850,11851,11852,11853,11854,11855,12640,12641,12642,12643,12644,12645,12646,12647,12648,12649,12650,12651,12652,12653,12654,12655,12976,12977,12978,12979,12980,12981,12982,12983,12984,12985,12986,12987,12988,12989,12990,12991,14390,14432,14433,14434,14435,14436,14437,14438,14439,14440,14441,14442,14443,14444,14445,14446,14447,15568,15569,15570,15571,15572,15573,15574,15575,15576,15577,15578,15579,15580,15581,15582,15583,15936,15937,15938,15939,15940,15941,15942,15943,15944,15945,15946,15947,15948,15949,15950,15951,16220,16288,16289,16290,16291,16292,16293,16294,16295,16296,16297,16298,16299,16300,16301,16302,16303,17504,17505,17506,17507,17508,17509,17510,17511,17512,17513,17514,17515,17516,17517,17518,17519,18160,18161,18162,18163,18164,18165,18166,18167,18168,18169,18170,18171,18172,18173,18174,18175,18512,18513,18514,18515,18516,18517,18518,18519,18520,18521,18522,18523,18524,18525,18526,18527,18784,18785,18786,18787,18788,18789,18790,18791,18792,18793,18794,18795,18796,18797,18798,18799,18882,21248,21249,21250,21251,21252,21253,21254,21255,21256,21257,21258,21259,21260,21261,21262,21263,23504,23505,23506,23507,23508,23509,23510,23511,23512,23513,23514,23515,23516,23517,23518,23519,23616,23617,23618,23619,23620,23621,23622,23623,23624,23625,23626,23627,23628,23629,23630,23631,23968,23969,23970,23971,23972,23973,23974,23975,23976,23977,23978,23979,23980,23981,23982,23983,24816,24817,24818,24819,24820,24821,24822,24823,24824,24825,24826,24827,24828,24829,24830,24831,24896,24897,24898,24899,24900,24901,24902,24903,24904,24905,24906,24907,24908,24909,24910,24911,25184,25185,25186,25187,25188,25189,25190,25191,25192,25193,25194,25195,25196,25197,25198,25199,27952,27953,27954,27955,27956,27957,27958,27959,27960,27961,27962,27963,27964,27965,27966,27967,30272,30273,30274,30275,30276,30277,30278,30279,30280,30281,30282,30283,30284,30285,30286,30287,30480,30481,30482,30483,30484,30485,30486,30487,30488,30489,30490,30491,30492,30493,30494,30495,31136,31137,31138,31139,31140,31141,31142,31143,31144,31145,31146,31147,31148,31149,31150,31151,31747
2020-11-07 23:55:10:486 nl.zeesoft.zdk.test.neural.TestProcessorFactory: 768;48;172,257,528,529,530,531,532,533,534,535,536,537,538,539,540,541,542,543,795,810,1663,1795,2447,2951,3988,4539,5329,5393,5536,5803,5851,6488,6643,8610,9705,9830,10937,11792,11793,11794,11795,11796,11797,11798,11799,11800,11801,11802,11803,11804,11805,11806,11807,12000,12001,12002,12003,12004,12005,12006,12007,12008,12009,12010,12011,12012,12013,12014,12015,12978,14390,15467,15577,15872,15873,15874,15875,15876,15877,15878,15879,15880,15881,15882,15883,15884,15885,15886,15887,15948,16220,17507,18882,20080,20081,20082,20083,20084,20085,20086,20087,20088,20089,20090,20091,20092,20093,20094,20095,21254,22192,22193,22194,22195,22196,22197,22198,22199,22200,22201,22202,22203,22204,22205,22206,22207,23253,23616,23617,23618,23619,23620,23621,23622,23623,23624,23625,23626,23627,23628,23629,23630,23631,23824,23825,23826,23827,23828,23829,23830,23831,23832,23833,23834,23835,23836,23837,23838,23839,24909,25187,25216,25217,25218,25219,25220,25221,25222,25223,25224,25225,25226,25227,25228,25229,25230,25231,27897,27952,27953,27954,27955,27956,27957,27958,27959,27960,27961,27962,27963,27964,27965,27966,27967,31136,31137,31138,31139,31140,31141,31142,31143,31144,31145,31146,31147,31148,31149,31150,31151,31747
2020-11-07 23:55:12:395 nl.zeesoft.zdk.test.neural.TestProcessorFactory: 768;48;541,810,950,1513,1663,1733,1795,2944,2945,2946,2947,2948,2949,2950,2951,2952,2953,2954,2955,2956,2957,2958,2959,3936,3937,3938,3939,3940,3941,3942,3943,3944,3945,3946,3947,3948,3949,3950,3951,5329,5393,5536,5537,5538,5539,5540,5541,5542,5543,5544,5545,5546,5547,5548,5549,5550,5551,5803,5851,6018,6480,6481,6482,6483,6484,6485,6486,6487,6488,6489,6490,6491,6492,6493,6494,6495,6643,7580,8608,8609,8610,8611,8612,8613,8614,8615,8616,8617,8618,8619,8620,8621,8622,8623,9705,9830,10436,11841,12647,12978,14390,14439,15440,15441,15442,15443,15444,15445,15446,15447,15448,15449,15450,15451,15452,15453,15454,15455,15577,15948,17512,18525,18882,20081,21254,23253,23625,23977,24825,24896,24897,24898,24899,24900,24901,24902,24903,24904,24905,24906,24907,24908,24909,24910,24911,25187,27897,27955,30272,30273,30274,30275,30276,30277,30278,30279,30280,30281,30282,30283,30284,30285,30286,30287,31144,31744,31745,31746,31747,31748,31749,31750,31751,31752,31753,31754,31755,31756,31757,31758,31759
2020-11-07 23:55:14:172 nl.zeesoft.zdk.test.neural.TestProcessorFactory: 768;48;795,1408,1409,1410,1411,1412,1413,1414,1415,1416,1417,1418,1419,1420,1421,1422,1423,1504,1505,1506,1507,1508,1509,1510,1511,1512,1513,1514,1515,1516,1517,1518,1519,1648,1649,1650,1651,1652,1653,1654,1655,1656,1657,1658,1659,1660,1661,1662,1663,1728,1729,1730,1731,1732,1733,1734,1735,1736,1737,1738,1739,1740,1741,1742,1743,1795,1830,2447,2944,2945,2946,2947,2948,2949,2950,2951,2952,2953,2954,2955,2956,2957,2958,2959,3988,4528,4529,4530,4531,4532,4533,4534,4535,4536,4537,4538,4539,4540,4541,4542,4543,5329,5392,5393,5394,5395,5396,5397,5398,5399,5400,5401,5402,5403,5404,5405,5406,5407,5536,5537,5538,5539,5540,5541,5542,5543,5544,5545,5546,5547,5548,5549,5550,5551,5803,5840,5841,5842,5843,5844,5845,5846,5847,5848,5849,5850,5851,5852,5853,5854,5855,6016,6017,6018,6019,6020,6021,6022,6023,6024,6025,6026,6027,6028,6029,6030,6031,6480,6481,6482,6483,6484,6485,6486,6487,6488,6489,6490,6491,6492,6493,6494,6495,6640,6641,6642,6643,6644,6645,6646,6647,6648,6649,6650,6651,6652,6653,6654,6655,7580,8608,8609,8610,8611,8612,8613,8614,8615,8616,8617,8618,8619,8620,8621,8622,8623,9705,10432,10433,10434,10435,10436,10437,10438,10439,10440,10441,10442,10443,10444,10445,10446,10447,10937,11204,12976,12977,12978,12979,12980,12981,12982,12983,12984,12985,12986,12987,12988,12989,12990,12991,14390,14432,14433,14434,14435,14436,14437,14438,14439,14440,14441,14442,14443,14444,14445,14446,14447,15440,15441,15442,15443,15444,15445,15446,15447,15448,15449,15450,15451,15452,15453,15454,15455,15568,15569,15570,15571,15572,15573,15574,15575,15576,15577,15578,15579,15580,15581,15582,15583,15872,15873,15874,15875,15876,15877,15878,15879,15880,15881,15882,15883,15884,15885,15886,15887,15936,15937,15938,15939,15940,15941,15942,15943,15944,15945,15946,15947,15948,15949,15950,15951,17504,17505,17506,17507,17508,17509,17510,17511,17512,17513,17514,17515,17516,17517,17518,17519,18160,18161,18162,18163,18164,18165,18166,18167,18168,18169,18170,18171,18172,18173,18174,18175,19539,20080,20081,20082,20083,20084,20085,20086,20087,20088,20089,20090,20091,20092,20093,20094,20095,22204,23248,23249,23250,23251,23252,23253,23254,23255,23256,23257,23258,23259,23260,23261,23262,23263,23832,24825,24896,24897,24898,24899,24900,24901,24902,24903,24904,24905,24906,24907,24908,24909,24910,24911,27056,27057,27058,27059,27060,27061,27062,27063,27064,27065,27066,27067,27068,27069,27070,27071,27888,27889,27890,27891,27892,27893,27894,27895,27896,27897,27898,27899,27900,27901,27902,27903,31136,31137,31138,31139,31140,31141,31142,31143,31144,31145,31146,31147,31148,31149,31150,31151,31865,31904,31905,31906,31907,31908,31909,31910,31911,31912,31913,31914,31915,31916,31917,31918,31919
2020-11-07 23:55:15:844 nl.zeesoft.zdk.test.neural.TestProcessorFactory: 768;48;172,257,541,795,810,1422,1513,1663,1733,2447,3948,5329,5393,5526,5536,5803,5851,6018,6488,6643,7580,8610,9705,9830,10436,11804,11841,12647,12978,14390,14439,15948,17512,18525,18882,20081,21254,23253,23625,23977,24825,24909,25187,27897,31144,31865
2020-11-07 23:55:17:522 nl.zeesoft.zdk.test.neural.TestProcessorFactory: 768;48;172,257,541,795,810,1390,1513,1663,1795,2447,2951,3948,3988,4539,5329,5393,5536,5803,5851,6488,6643,7349,7580,8610,9705,11841,12009,12647,12978,14390,14439,15441,15948,16220,17507,18882,20081,21254,23253,23625,24909,27897,30280,31144,31747,31865
2020-11-07 23:55:19:190 nl.zeesoft.zdk.test.neural.TestProcessorFactory: 768;48;257,541,795,810,950,1422,1513,1663,1733,1795,2447,3948,3988,4539,5329,5393,5536,5803,6018,6488,6643,7580,9705,9830,10436,11841,12647,14439,15441,15577,15948,16220,18168,18525,18882,21254,23253,23625,23977,24825,24909,25187,27955,30280,31144,31865
2020-11-07 23:55:20:810 nl.zeesoft.zdk.test.neural.TestProcessorFactory: 768;48;257,541,795,810,1663,1733,1795,2447,2951,3988,4539,5329,5536,5803,5851,6018,6488,6643,7580,8610,9705,9830,11804,12978,14390,15441,15577,15948,16220,17507,18168,18525,18793,20081,21254,23253,23625,23977,24825,24909,25187,25220,27897,27955,31747,31865
2020-11-07 23:55:22:456 nl.zeesoft.zdk.test.neural.TestProcessorFactory: 768;48;172,257,541,795,810,1663,1795,1830,2447,2951,3009,3988,4539,5329,5393,5536,5803,5851,6488,6643,7349,8610,9705,10937,11204,12647,12978,14390,15441,15467,15948,16220,16292,17512,19539,20081,22204,23253,23832,24909,27058,27897,30280,31747,31865,31918
Processing 1000 SDRs took: 18152 ms (18 ms/SDR)
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

2020-11-07 23:55:24:193 nl.zeesoft.zdk.neural.network.Network: Initializing network ...
2020-11-07 23:55:24:194 nl.zeesoft.zdk.neural.processors.TemporalMemory: Initializing TemporalMemory ...
2020-11-07 23:55:24:194 nl.zeesoft.zdk.neural.processors.Classifier: Initializing Classifier ...
2020-11-07 23:55:24:195 nl.zeesoft.zdk.neural.processors.Classifier: Initialized
2020-11-07 23:55:24:195 nl.zeesoft.zdk.neural.processors.ScalarEncoder: Initializing ScalarEncoder ...
2020-11-07 23:55:24:195 nl.zeesoft.zdk.neural.processors.SpatialPooler: Initializing SpatialPooler ...
2020-11-07 23:55:24:195 nl.zeesoft.zdk.neural.processors.ScalarEncoder: Initialized
2020-11-07 23:55:24:200 nl.zeesoft.zdk.neural.processors.TemporalMemory: Initialized
2020-11-07 23:55:24:200 nl.zeesoft.zdk.neural.processors.TemporalMemory: Resetting TemporalMemory connections ...
2020-11-07 23:55:24:203 nl.zeesoft.zdk.neural.processors.TemporalMemory: Reset connections
2020-11-07 23:55:24:226 nl.zeesoft.zdk.neural.processors.SpatialPooler: Initialized
2020-11-07 23:55:24:226 nl.zeesoft.zdk.neural.processors.SpatialPooler: Resetting SpatialPooler connections ...
2020-11-07 23:55:24:289 nl.zeesoft.zdk.neural.processors.SpatialPooler: Reset connections
2020-11-07 23:55:24:290 nl.zeesoft.zdk.neural.network.Network: Initialized network

2020-11-07 23:55:24:290 nl.zeesoft.zdk.test.neural.TestNetwork: Processing 100 SDRs ...
2020-11-07 23:55:28:046 nl.zeesoft.zdk.test.neural.TestNetwork: Processed 100 SDRs

2020-11-07 23:55:28:046 nl.zeesoft.zdk.neural.network.Network: Saving network ...
2020-11-07 23:55:28:716 nl.zeesoft.zdk.neural.network.Network: Saved network
2020-11-07 23:55:28:716 nl.zeesoft.zdk.neural.network.Network: Loading network ...
2020-11-07 23:55:32:634 nl.zeesoft.zdk.neural.network.Network: Loaded network

Processor: EN
-> 16;16;1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16
-> SDR##1;1;@value#java.lang.Integer#1
Processor: SP
-> 48;48;1524,759,984,83,1755,207,408,341,1711,904,844,1520,1189,1190,248,430,1310,1194,1811,407,428,29,732,42,360,901,107,535,867,1503,1252,762,1457,1697,1425,575,1413,1097,1234,1500,1564,1474,308,1040,688,986
Processor: TM
-> 768;48;464,465,466,467,468,469,470,471,472,473,474,475,476,477,478,479,3968,3969,3970,3971,3972,3973,3974,3975,3976,3977,3978,3979,3980,3981,3982,3983,672,673,674,675,676,677,678,679,680,681,682,683,684,685,686,687,1328,1329,1330,1331,1332,1333,1334,1335,1336,1337,1338,1339,1340,1341,1342,1343,4928,4929,4930,4931,4932,4933,4934,4935,4936,4937,4938,4939,4940,4941,4942,4943,1712,1713,1714,1715,1716,1717,1718,1719,1720,1721,1722,1723,1724,1725,1726,1727,5760,5761,5762,5763,5764,5765,5766,5767,5768,5769,5770,5771,5772,5773,5774,5775,3312,3313,3314,3315,3316,3317,3318,3319,3320,3321,3322,3323,3324,3325,3326,3327,6528,6529,6530,6531,6532,6533,6534,6535,6536,6537,6538,6539,6540,6541,6542,6543,6848,6849,6850,6851,6852,6853,6854,6855,6856,6857,6858,6859,6860,6861,6862,6863,6880,6881,6882,6883,6884,6885,6886,6887,6888,6889,6890,6891,6892,6893,6894,6895,5456,5457,5458,5459,5460,5461,5462,5463,5464,5465,5466,5467,5468,5469,5470,5471,6512,6513,6514,6515,6516,6517,6518,6519,6520,6521,6522,6523,6524,6525,6526,6527,8560,8561,8562,8563,8564,8565,8566,8567,8568,8569,8570,8571,8572,8573,8574,8575,11008,11009,11010,11011,11012,11013,11014,11015,11016,11017,11018,11019,11020,11021,11022,11023,9200,9201,9202,9203,9204,9205,9206,9207,9208,9209,9210,9211,9212,9213,9214,9215,11712,11713,11714,11715,11716,11717,11718,11719,11720,11721,11722,11723,11724,11725,11726,11727,12192,12193,12194,12195,12196,12197,12198,12199,12200,12201,12202,12203,12204,12205,12206,12207,13504,13505,13506,13507,13508,13509,13510,13511,13512,13513,13514,13515,13516,13517,13518,13519,12144,12145,12146,12147,12148,12149,12150,12151,12152,12153,12154,12155,12156,12157,12158,12159,14464,14465,14466,14467,14468,14469,14470,14471,14472,14473,14474,14475,14476,14477,14478,14479,15776,15777,15778,15779,15780,15781,15782,15783,15784,15785,15786,15787,15788,15789,15790,15791,15744,15745,15746,15747,15748,15749,15750,15751,15752,15753,15754,15755,15756,15757,15758,15759,13872,13873,13874,13875,13876,13877,13878,13879,13880,13881,13882,13883,13884,13885,13886,13887,14416,14417,14418,14419,14420,14421,14422,14423,14424,14425,14426,14427,14428,14429,14430,14431,16640,16641,16642,16643,16644,16645,16646,16647,16648,16649,16650,16651,16652,16653,16654,16655,19040,19041,19042,19043,19044,19045,19046,19047,19048,19049,19050,19051,19052,19053,19054,19055,19104,19105,19106,19107,19108,19109,19110,19111,19112,19113,19114,19115,19116,19117,19118,19119,19744,19745,19746,19747,19748,19749,19750,19751,19752,19753,19754,19755,19756,19757,19758,19759,17552,17553,17554,17555,17556,17557,17558,17559,17560,17561,17562,17563,17564,17565,17566,17567,20032,20033,20034,20035,20036,20037,20038,20039,20040,20041,20042,20043,20044,20045,20046,20047,20960,20961,20962,20963,20964,20965,20966,20967,20968,20969,20970,20971,20972,20973,20974,20975,19024,19025,19026,19027,19028,19029,19030,19031,19032,19033,19034,19035,19036,19037,19038,19039,23584,23585,23586,23587,23588,23589,23590,23591,23592,23593,23594,23595,23596,23597,23598,23599,24048,24049,24050,24051,24052,24053,24054,24055,24056,24057,24058,24059,24060,24061,24062,24063,24000,24001,24002,24003,24004,24005,24006,24007,24008,24009,24010,24011,24012,24013,24014,24015,24320,24321,24322,24323,24324,24325,24326,24327,24328,24329,24330,24331,24332,24333,24334,24335,22608,22609,22610,22611,22612,22613,22614,22615,22616,22617,22618,22619,22620,22621,22622,22623,24384,24385,24386,24387,24388,24389,24390,24391,24392,24393,24394,24395,24396,24397,24398,24399,22800,22801,22802,22803,22804,22805,22806,22807,22808,22809,22810,22811,22812,22813,22814,22815,25024,25025,25026,25027,25028,25029,25030,25031,25032,25033,25034,25035,25036,25037,25038,25039,27380,23312,23313,23314,23315,23316,23317,23318,23319,23320,23321,23322,23323,23324,23325,23326,23327,28080,28081,28082,28083,28084,28085,28086,28087,28088,28089,28090,28091,28092,28093,28094,28095,28976,28977,28978,28979,28980,28981,28982,28983,28984,28985,28986,28987,28988,28989,28990,28991,27152,27153,27154,27155,27156,27157,27158,27159,27160,27161,27162,27163,27164,27165,27166,27167
-> 48;48;29,42,83,107,207,248,308,341,360,407,408,428,430,535,575,688,732,759,762,844,867,901,904,984,986,1040,1097,1189,1190,1194,1234,1252,1310,1413,1425,1457,1474,1500,1503,1520,1524,1564,1697,1755,1811
-> 768;48;
-> 768;48;469,683,3972,1341,4937,1724,5762,3321,6532,6849,6884,5458,6512,8568,11012,9210,11723,12199,13507,12152,14465,15786,15747,13884,14424,16650,19048,19109,19744,17557,20042,20964,19025,23597,24007,24051,24327,22610,24389,22813,25031,27380,23314,28082,28987,27155
Processor: CL
-> SDR##1;1;@accuracy#java.lang.Float#1.0@accuracyTrend#java.lang.Float#1.0@classification:1#nl.zeesoft.zdk.neural.processors.Classification#1;value;java.lang.Integer;0,6538%1,1965
~~~~

Test results
------------
All 15 tests have been executed successfully (255 assertions).  
Total test duration: 42018 ms (total sleep duration: 166 ms).  

Memory usage per test;  
 * nl.zeesoft.zdk.test.TestStr: 671 Kb / 0 Mb
 * nl.zeesoft.zdk.test.thread.TestRunCode: 435 Kb / 0 Mb
 * nl.zeesoft.zdk.test.thread.TestCodeRunnerChain: 437 Kb / 0 Mb
 * nl.zeesoft.zdk.test.collection.TestCollections: 464 Kb / 0 Mb
 * nl.zeesoft.zdk.test.http.TestHttpServer: 805 Kb / 0 Mb
 * nl.zeesoft.zdk.test.grid.TestGrid: 797 Kb / 0 Mb
 * nl.zeesoft.zdk.test.neural.TestSDR: 803 Kb / 0 Mb
 * nl.zeesoft.zdk.test.neural.TestCellGrid: 873 Kb / 0 Mb
 * nl.zeesoft.zdk.test.neural.TestScalarEncoder: 875 Kb / 0 Mb
 * nl.zeesoft.zdk.test.neural.TestSpatialPooler: 876 Kb / 0 Mb
 * nl.zeesoft.zdk.test.neural.TestTemporalMemory: 890 Kb / 0 Mb
 * nl.zeesoft.zdk.test.neural.TestClassifier: 901 Kb / 0 Mb
 * nl.zeesoft.zdk.test.neural.TestMerger: 911 Kb / 0 Mb
 * nl.zeesoft.zdk.test.neural.TestProcessorFactory: 911 Kb / 0 Mb
 * nl.zeesoft.zdk.test.neural.TestNetwork: 923 Kb / 0 Mb
