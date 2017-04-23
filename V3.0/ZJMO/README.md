Zeesoft JSON Machine Orchestration
==================================
Zeesoft JSON Machine Orchestration (ZJMO) is an open source library for Java application development.
The aim of the ZJMO project is to provide a scalable, high availability, JSON based, work distribution architecture.
Imagine an orchestra where the conductor directs the other members to play a certain composition.
All the members speak JSON on two TCP ports; one for control and another one for work.
One or more backup members can be created for conductors and players alike.
This library depends on the [Zeesoft Development Kit](https://github.com/DyzLecticus/Zeesoft/tree/master/V3.0/ZDK/).  

**Release downloads**  
Click [here](https://github.com/DyzLecticus/Zeesoft/raw/master/V3.0/ZJMO/releases/zjmo-0.9.18.zip) to download the latest ZJMO release (version 0.9.18).  
All ZJMO releases can be downloaded [here](https://github.com/DyzLecticus/Zeesoft/raw/master/V3.0/ZJMO/releases/).  
*All jar files in the release include source code and build scripts.*  

**Self documenting and self testing**  
The tests used to develop this libary are also used to generate this README file.  
Run the [ZJMO](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZJMO/src/nl/zeesoft/zjmo/test/ZJMO.java) class as a java application to print this documentation to the standard out.  
Click [here](#test-results) to scroll down to the test result summary.  

nl.zeesoft.zjmo.test.TestTestOrchestra
--------------------------------------
This test shows how to create and initialize a *TestOrchestra* instance.
The *TestOrchestra* extends the *Orchestra* class which functions as a factory for other objects like conductors and players.

**Example implementation**  
~~~~
// Create orchestra
TestOrchestra orchestra = new TestOrchestra();
// Initialize the orchestra
orchestra.initialize();
~~~~

This test uses the *MockTestOrchestra*.

Class references;  
 * [TestTestOrchestra](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZJMO/src/nl/zeesoft/zjmo/test/TestTestOrchestra.java)
 * [MockTestOrchestra](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZJMO/src/nl/zeesoft/zjmo/test/mocks/MockTestOrchestra.java)
 * [TestOrchestra](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZJMO/src/nl/zeesoft/zjmo/test/mocks/TestOrchestra.java)
 * [Orchestra](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZJMO/src/nl/zeesoft/zjmo/orchestra/Orchestra.java)

**Test output**  
The output of this test shows the JSON structure of the *TestOrchestra*.  
~~~~
{
  "positions": {
    "0": "Conductor",
    "1": "Database X",
    "2": "Database Y",
    "3": "Application server X",
    "4": "Application server Y"
  },
  "channels": [
    {
      "name": "Orchestra critical",
      "failOnSubscriberError": true,
      "subscriberIdList": {
        "0": "Conductor/0",
        "1": "Conductor/1",
        "2": "Database X/0",
        "3": "Database Y/0",
        "4": "Application server X/0",
        "5": "Application server Y/0",
        "6": "Database X/1",
        "7": "Database Y/1"
      }
    },
    {
      "name": "Orchestra optional",
      "failOnSubscriberError": false,
      "subscriberIdList": {
        "0": "Conductor/0",
        "1": "Conductor/1",
        "2": "Database X/0",
        "3": "Database Y/0",
        "4": "Application server X/0",
        "5": "Application server Y/0",
        "6": "Database X/1",
        "7": "Database Y/1"
      }
    }
  ],
  "members": [
    {
      "positionName": "Conductor",
      "positionBackupNumber": 0,
      "ipAddressOrHostName": "localhost",
      "controlPort": 5433,
      "workPort": 5432,
      "workRequestTimeout": 500,
      "workRequestTimeoutDrain": true
    },
    {
      "positionName": "Conductor",
      "positionBackupNumber": 1,
      "ipAddressOrHostName": "localhost",
      "controlPort": 5431,
      "workPort": 5430,
      "workRequestTimeout": 500,
      "workRequestTimeoutDrain": false
    },
    {
      "positionName": "Database X",
      "positionBackupNumber": 0,
      "ipAddressOrHostName": "localhost",
      "controlPort": 6543,
      "workPort": 6542,
      "workRequestTimeout": 2000,
      "workRequestTimeoutDrain": true
    },
    {
      "positionName": "Database Y",
      "positionBackupNumber": 0,
      "ipAddressOrHostName": "localhost",
      "controlPort": 7654,
      "workPort": 7653,
      "workRequestTimeout": 2000,
      "workRequestTimeoutDrain": true
    },
    {
      "positionName": "Application server X",
      "positionBackupNumber": 0,
      "ipAddressOrHostName": "localhost",
      "controlPort": 8765,
      "workPort": 8764,
      "workRequestTimeout": 500,
      "workRequestTimeoutDrain": false
    },
    {
      "positionName": "Application server Y",
      "positionBackupNumber": 0,
      "ipAddressOrHostName": "localhost",
      "controlPort": 9876,
      "workPort": 9875,
      "workRequestTimeout": 500,
      "workRequestTimeoutDrain": false
    },
    {
      "positionName": "Database X",
      "positionBackupNumber": 1,
      "ipAddressOrHostName": "localhost",
      "controlPort": 6541,
      "workPort": 6540,
      "workRequestTimeout": 2000,
      "workRequestTimeoutDrain": true
    },
    {
      "positionName": "Database Y",
      "positionBackupNumber": 1,
      "ipAddressOrHostName": "localhost",
      "controlPort": 7652,
      "workPort": 7651,
      "workRequestTimeout": 2000,
      "workRequestTimeoutDrain": true
    }
  ]
}
~~~~

nl.zeesoft.zjmo.test.TestConductor
----------------------------------
This test shows how to start and stop *Conductor* and *Player* instances.

**Example implementation**  
~~~~
// Create conductor
Conductor con = new Conductor(null,orchestra,0);
// Start the conductor
boolean started = con.start();
// Create client using conductor control port settings
MemberClient client = new MemberClient("localhost",5433);
// Send stop command
ZStringBuilder response = client.sendCommand(ProtocolControl.STOP_PROGRAM);
~~~~

This test uses *MockConductor1*, *MockConductor2* and the *MockPlayers*.

Class references;  
 * [TestConductor](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZJMO/src/nl/zeesoft/zjmo/test/TestConductor.java)
 * [MockConductor1](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZJMO/src/nl/zeesoft/zjmo/test/mocks/MockConductor1.java)
 * [MockConductor2](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZJMO/src/nl/zeesoft/zjmo/test/mocks/MockConductor2.java)
 * [MockPlayers](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZJMO/src/nl/zeesoft/zjmo/test/mocks/MockPlayers.java)
 * [Conductor](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZJMO/src/nl/zeesoft/zjmo/orchestra/members/Conductor.java)
 * [Player](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZJMO/src/nl/zeesoft/zjmo/orchestra/members/Player.java)

**Test output**  
The output of this test shows;  
 * The orchestra initialization duration.  
 * The conductor GET_STATE command response.  
 * The orchestra state JSON.  
~~~~
Starting orchestra ...
Starting Database X/0 (control: 6543, work: 6542) ...
Starting Database Y/0 (control: 7654, work: 7653) ...
Starting Application server X/0 (control: 8765, work: 8764) ...
Starting Application server Y/0 (control: 9876, work: 9875) ...
Starting Database X/1 (control: 6541, work: 6540) ...
Starting Database Y/1 (control: 7652, work: 7651) ...
Starting Conductor/1 (control: 5431, work: 5430) ...
Starting Conductor/0 (control: 5433, work: 5432) ...
Starting orchestra took 47 ms

GET_STATE command response: {"state": "ONLINE","workLoad": 0,"memoryUsage": 11155360,"restartRequired": false}

Orchestra state JSON:
{
  "positions": {
    "0": "Conductor",
    "1": "Database X",
    "2": "Database Y",
    "3": "Application server X",
    "4": "Application server Y"
  },
  "channels": [
    {
      "name": "Orchestra critical",
      "failOnSubscriberError": true,
      "subscriberIdList": {
        "0": "Conductor/0",
        "1": "Conductor/1",
        "2": "Database X/0",
        "3": "Database Y/0",
        "4": "Application server X/0",
        "5": "Application server Y/0",
        "6": "Database X/1",
        "7": "Database Y/1"
      }
    },
    {
      "name": "Orchestra optional",
      "failOnSubscriberError": false,
      "subscriberIdList": {
        "0": "Conductor/0",
        "1": "Conductor/1",
        "2": "Database X/0",
        "3": "Database Y/0",
        "4": "Application server X/0",
        "5": "Application server Y/0",
        "6": "Database X/1",
        "7": "Database Y/1"
      }
    }
  ],
  "members": [
    {
      "positionName": "Conductor",
      "positionBackupNumber": 0,
      "ipAddressOrHostName": "localhost",
      "controlPort": 5433,
      "workPort": 5432,
      "workRequestTimeout": 500,
      "workRequestTimeoutDrain": true,
      "state": "ONLINE",
      "workLoad": 0,
      "memoryUsage": 10161160,
      "restartRequired": false
    },
    {
      "positionName": "Conductor",
      "positionBackupNumber": 1,
      "ipAddressOrHostName": "localhost",
      "controlPort": 5431,
      "workPort": 5430,
      "workRequestTimeout": 500,
      "workRequestTimeoutDrain": false,
      "state": "ONLINE",
      "workLoad": 0,
      "memoryUsage": 10536104,
      "restartRequired": false
    },
    {
      "positionName": "Database X",
      "positionBackupNumber": 0,
      "ipAddressOrHostName": "localhost",
      "controlPort": 6543,
      "workPort": 6542,
      "workRequestTimeout": 2000,
      "workRequestTimeoutDrain": true,
      "state": "ONLINE",
      "workLoad": 0,
      "memoryUsage": 10738568,
      "restartRequired": false
    },
    {
      "positionName": "Database Y",
      "positionBackupNumber": 0,
      "ipAddressOrHostName": "localhost",
      "controlPort": 7654,
      "workPort": 7653,
      "workRequestTimeout": 2000,
      "workRequestTimeoutDrain": true,
      "state": "ONLINE",
      "workLoad": 0,
      "memoryUsage": 10872168,
      "restartRequired": false
    },
    {
      "positionName": "Application server X",
      "positionBackupNumber": 0,
      "ipAddressOrHostName": "localhost",
      "controlPort": 8765,
      "workPort": 8764,
      "workRequestTimeout": 500,
      "workRequestTimeoutDrain": false,
      "state": "ONLINE",
      "workLoad": 0,
      "memoryUsage": 10920368,
      "restartRequired": false
    },
    {
      "positionName": "Application server Y",
      "positionBackupNumber": 0,
      "ipAddressOrHostName": "localhost",
      "controlPort": 9876,
      "workPort": 9875,
      "workRequestTimeout": 500,
      "workRequestTimeoutDrain": false,
      "state": "ONLINE",
      "workLoad": 0,
      "memoryUsage": 10980368,
      "restartRequired": false
    },
    {
      "positionName": "Database X",
      "positionBackupNumber": 1,
      "ipAddressOrHostName": "localhost",
      "controlPort": 6541,
      "workPort": 6540,
      "workRequestTimeout": 2000,
      "workRequestTimeoutDrain": true,
      "state": "ONLINE",
      "workLoad": 0,
      "memoryUsage": 11008096,
      "restartRequired": false
    },
    {
      "positionName": "Database Y",
      "positionBackupNumber": 1,
      "ipAddressOrHostName": "localhost",
      "controlPort": 7652,
      "workPort": 7651,
      "workRequestTimeout": 2000,
      "workRequestTimeoutDrain": true,
      "state": "ONLINE",
      "workLoad": 0,
      "memoryUsage": 11020440,
      "restartRequired": false
    }
  ]
}

Stopping orchestra ...
Sent stop command to: Conductor/0
2017-04-23 11:30:17:329 DBG nl.zeesoft.zdk.thread.WorkerUnion: All workers have been stopped
Sent stop command to: Conductor/1
2017-04-23 11:30:21:220 DBG nl.zeesoft.zdk.thread.WorkerUnion: All workers have been stopped
Stopping players ...
Stopped orchestra
2017-04-23 11:30:24:290 DBG nl.zeesoft.zdk.thread.WorkerUnion: All workers have been stopped
2017-04-23 11:30:25:202 DBG nl.zeesoft.zdk.thread.WorkerUnion: All workers have been stopped
2017-04-23 11:30:25:310 DBG nl.zeesoft.zdk.thread.WorkerUnion: All workers have been stopped
2017-04-23 11:30:26:313 DBG nl.zeesoft.zdk.thread.WorkerUnion: All workers have been stopped
2017-04-23 11:30:27:328 DBG nl.zeesoft.zdk.thread.WorkerUnion: All workers have been stopped
2017-04-23 11:30:29:227 DBG nl.zeesoft.zdk.thread.WorkerUnion: All workers have been stopped
~~~~

nl.zeesoft.zjmo.test.TestMemberOnlineOffline
--------------------------------------------
This test shows how a *Conductor* maintains its orchestra member state representation.

**Example implementation**  
~~~~
// Create conductor
Conductor con = new Conductor(null,orchestra,0);
// Start the conductor
boolean started = con.start();
// Create client using conductor control port settings
MemberClient client = new MemberClient("localhost",5433);
// Send get member state request
ZStringBuilder response = client.sendCommand(ProtocolControl.GET_MEMBER_STATE,"id","[MEMBERID]");
~~~~

This test uses *MockConductor1*, *MockConductor2* and the *MockPlayers*.

Class references;  
 * [TestMemberOnlineOffline](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZJMO/src/nl/zeesoft/zjmo/test/TestMemberOnlineOffline.java)
 * [MockConductor1](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZJMO/src/nl/zeesoft/zjmo/test/mocks/MockConductor1.java)
 * [MockConductor2](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZJMO/src/nl/zeesoft/zjmo/test/mocks/MockConductor2.java)
 * [MockPlayers](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZJMO/src/nl/zeesoft/zjmo/test/mocks/MockPlayers.java)
 * [Conductor](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZJMO/src/nl/zeesoft/zjmo/orchestra/members/Conductor.java)
 * [Player](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZJMO/src/nl/zeesoft/zjmo/orchestra/members/Player.java)

**Test output**  
The output of this test shows how the orchestra member state representation changes while the state of one of the backup members is manipulated.  
~~~~
Starting orchestra ...
Starting Database X/0 (control: 6543, work: 6542) ...
Starting Database Y/0 (control: 7654, work: 7653) ...
Starting Application server X/0 (control: 8765, work: 8764) ...
Starting Application server Y/0 (control: 9876, work: 9875) ...
Starting Database X/1 (control: 6541, work: 6540) ...
Starting Conductor/1 (control: 5431, work: 5430) ...
Starting Conductor/0 (control: 5433, work: 5432) ...
Starting orchestra took 120 ms

Starting backup ...
Starting Database Y/1 (control: 7652, work: 7651) ...

Backup member state JSON:
{
  "positionName": "Database Y",
  "positionBackupNumber": 1,
  "ipAddressOrHostName": "localhost",
  "controlPort": 7652,
  "workPort": 7651,
  "workRequestTimeout": 2000,
  "workRequestTimeoutDrain": true,
  "state": "ONLINE",
  "workLoad": 0,
  "memoryUsage": 10501960,
  "restartRequired": false
}

Stopping backup ...
Stopped backup
2017-04-23 11:30:31:400 DBG nl.zeesoft.zdk.thread.WorkerUnion: All workers have been stopped

Backup member state JSON:
{
  "positionName": "Database Y",
  "positionBackupNumber": 1,
  "ipAddressOrHostName": "localhost",
  "controlPort": 7652,
  "workPort": 7651,
  "workRequestTimeout": 2000,
  "workRequestTimeoutDrain": true,
  "state": "UNKNOWN",
  "errorTime": 1492939831398,
  "errorMessage": "Lost connection"
}

Starting backup ...
Starting Database Y/1 (control: 7652, work: 7651) ...
Started backup

Backup member state JSON:
{
  "positionName": "Database Y",
  "positionBackupNumber": 1,
  "ipAddressOrHostName": "localhost",
  "controlPort": 7652,
  "workPort": 7651,
  "workRequestTimeout": 2000,
  "workRequestTimeoutDrain": true,
  "state": "ONLINE",
  "workLoad": 0,
  "memoryUsage": 11521776,
  "restartRequired": false
}

Taking backup offline ...
Take backup offline response: {"response":"Executed command"}

Backup member state JSON:
{
  "positionName": "Database Y",
  "positionBackupNumber": 1,
  "ipAddressOrHostName": "localhost",
  "controlPort": 7652,
  "workPort": 7651,
  "workRequestTimeout": 2000,
  "workRequestTimeoutDrain": true,
  "state": "OFFLINE",
  "workLoad": 0,
  "memoryUsage": 12483744,
  "restartRequired": false
}

Bringing backup online ...
Bring backup online response: {"response":"Executed command"}

Backup member state JSON:
{
  "positionName": "Database Y",
  "positionBackupNumber": 1,
  "ipAddressOrHostName": "localhost",
  "controlPort": 7652,
  "workPort": 7651,
  "workRequestTimeout": 2000,
  "workRequestTimeoutDrain": true,
  "state": "ONLINE",
  "workLoad": 0,
  "memoryUsage": 13018816,
  "restartRequired": false
}

Draining backup offline ...
Drain backup offline response: {"response":"Executed command"}

Backup member state JSON:
{
  "positionName": "Database Y",
  "positionBackupNumber": 1,
  "ipAddressOrHostName": "localhost",
  "controlPort": 7652,
  "workPort": 7651,
  "workRequestTimeout": 2000,
  "workRequestTimeoutDrain": true,
  "state": "DRAINING_OFFLINE",
  "workLoad": 0,
  "memoryUsage": 13182416,
  "restartRequired": false
}

Backup member state JSON:
{
  "positionName": "Database Y",
  "positionBackupNumber": 1,
  "ipAddressOrHostName": "localhost",
  "controlPort": 7652,
  "workPort": 7651,
  "workRequestTimeout": 2000,
  "workRequestTimeoutDrain": true,
  "state": "OFFLINE",
  "workLoad": 0,
  "memoryUsage": 10745256,
  "restartRequired": false
}

Stopping orchestra ...
Sent stop command to: Conductor/0
2017-04-23 11:30:41:524 DBG nl.zeesoft.zdk.thread.WorkerUnion: All workers have been stopped
Sent stop command to: Conductor/1
2017-04-23 11:30:45:369 DBG nl.zeesoft.zdk.thread.WorkerUnion: All workers have been stopped
Stopping players ...
Stopped orchestra
~~~~

nl.zeesoft.zjmo.test.TestWorkRequest
------------------------------------
This test shows how a *Conductor* handles work requests.

**Example implementation**  
~~~~
// Create conductor
Conductor con = new Conductor(null,orchestra,0);
// Start the conductor
boolean started = con.start();
// Create client using conductor work port settings
MemberClient client = new MemberClient("localhost",5432);
// Create work request for a certain position
WorkRequest wr = new WorkRequest("Database X","{\"something\":\"json\"}");
// Send work request to conductor
ZStringBuilder response = client.writeOutputReadInput(wr.toJson().toStringBuilder());
~~~~

This test uses *MockConductor1*, *MockConductor2* and the *MockPlayers*.

Class references;  
 * [TestWorkRequest](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZJMO/src/nl/zeesoft/zjmo/test/TestWorkRequest.java)
 * [MockConductor1](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZJMO/src/nl/zeesoft/zjmo/test/mocks/MockConductor1.java)
 * [MockConductor2](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZJMO/src/nl/zeesoft/zjmo/test/mocks/MockConductor2.java)
 * [MockPlayers](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZJMO/src/nl/zeesoft/zjmo/test/mocks/MockPlayers.java)
 * [WorkRequest](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZJMO/src/nl/zeesoft/zjmo/orchestra/protocol/WorkRequest.java)

**Test output**  
The output of this test shows how one of the players handles a work request while draining offline, allowing a backup player to take over the position for the next work requests.  
~~~~
Starting orchestra ...
Starting Database X/0 (control: 6543, work: 6542) ...
Starting Database Y/0 (control: 7654, work: 7653) ...
Starting Application server X/0 (control: 8765, work: 8764) ...
Starting Application server Y/0 (control: 9876, work: 9875) ...
Starting Database X/1 (control: 6541, work: 6540) ...
Starting Database Y/1 (control: 7652, work: 7651) ...
Starting Conductor/1 (control: 5431, work: 5430) ...
Starting Conductor/0 (control: 5433, work: 5432) ...
Starting orchestra took 34 ms

Sending work request: {"request": {"echoMe": "Echo me this","sleep": 1000},"positionName": "Database X"}

Player state JSON:
{
  "positionName": "Database X",
  "positionBackupNumber": 0,
  "ipAddressOrHostName": "localhost",
  "controlPort": 6543,
  "workPort": 6542,
  "workRequestTimeout": 2000,
  "workRequestTimeoutDrain": true,
  "state": "DRAINING_OFFLINE",
  "workLoad": 1,
  "memoryUsage": 11520160,
  "restartRequired": false
}

Work request response: {"request": {"echoMe": "Echo me this","sleep": 1000},"response": {"echoMe": "Echo me this","sleep": 1000},"positionName": "Database X"}

Player state JSON:
{
  "positionName": "Database X",
  "positionBackupNumber": 0,
  "ipAddressOrHostName": "localhost",
  "controlPort": 6543,
  "workPort": 6542,
  "workRequestTimeout": 2000,
  "workRequestTimeoutDrain": true,
  "state": "OFFLINE",
  "workLoad": 0,
  "memoryUsage": 11775328,
  "restartRequired": false
}

Sending work request to backup: {"request": {"echoMe": "Echo me this"},"positionName": "Database X"}
Work request response from backup: {"request": {"echoMe": "Echo me this"},"response": {"echoMe": "Echo me this"},"positionName": "Database X"}
First work request to backup took 4 ms

Sending work request to backup: {"request": {"echoMe": "Echo me this"},"positionName": "Database X"}
Work request response from backup: {"request": {"echoMe": "Echo me this"},"response": {"echoMe": "Echo me this"},"positionName": "Database X"}
Second work request to backup took 3 ms

Sending work request to backup: {"request": {"echoMe": "Echo me this","sleep": 3000},"positionName": "Database X"}
Work request response from backup: {"error": "Work request timed out on: Database X/1","request": {"echoMe": "Echo me this","sleep": 3000},"positionName": "Database X"}
Time out work request to backup took 2016 ms

Player state JSON:
{
  "positionName": "Database X",
  "positionBackupNumber": 1,
  "ipAddressOrHostName": "localhost",
  "controlPort": 6541,
  "workPort": 6540,
  "workRequestTimeout": 2000,
  "workRequestTimeoutDrain": true,
  "state": "ONLINE",
  "workLoad": 1,
  "memoryUsage": 12223192,
  "restartRequired": false
}

Stopping orchestra ...
Sent stop command to: Conductor/0
2017-04-23 11:30:56:638 DBG nl.zeesoft.zdk.thread.WorkerUnion: All workers have been stopped
Sent stop command to: Conductor/1
2017-04-23 11:31:00:632 DBG nl.zeesoft.zdk.thread.WorkerUnion: All workers have been stopped
Stopping players ...
Stopped orchestra
2017-04-23 11:31:03:543 DBG nl.zeesoft.zdk.thread.WorkerUnion: All workers have been stopped
2017-04-23 11:31:03:544 DBG nl.zeesoft.zdk.thread.WorkerUnion: All workers have been stopped
2017-04-23 11:31:03:566 DBG nl.zeesoft.zdk.thread.WorkerUnion: All workers have been stopped
2017-04-23 11:31:04:569 DBG nl.zeesoft.zdk.thread.WorkerUnion: All workers have been stopped
2017-04-23 11:31:04:571 DBG nl.zeesoft.zdk.thread.WorkerUnion: All workers have been stopped
2017-04-23 11:31:04:574 DBG nl.zeesoft.zdk.thread.WorkerUnion: All workers have been stopped
~~~~

nl.zeesoft.zjmo.test.TestConductorConnector
-------------------------------------------
This test shows how to use a *ConductorConnector* to create reliable (external) connections to a working orchestra.

**Example implementation**  
~~~~
// Create conductor
Conductor con = new Conductor(null,orchestra,0);
// Start the conductor
boolean started = con.start();
// Create conductor connector
ConductorConnector connector = new ConductorConnector(null,null,false);
// Initialize the connector
connector.initialize(con.getOrchestra(),null);
// Open the connector
connector.open();
// Create work request
WorkRequest wr = new WorkRequest("Database X","{\"something\":\"json\"}");
// Send work request to a conductor
wr = connector.sendWorkRequest(wr);
// Close the connector
connector.close();
~~~~

This test uses *MockConductor1*, *MockConductor2* and the *MockPlayers*.

Class references;  
 * [TestConductorConnector](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZJMO/src/nl/zeesoft/zjmo/test/TestConductorConnector.java)
 * [MockConductor1](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZJMO/src/nl/zeesoft/zjmo/test/mocks/MockConductor1.java)
 * [MockConductor2](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZJMO/src/nl/zeesoft/zjmo/test/mocks/MockConductor2.java)
 * [MockPlayers](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZJMO/src/nl/zeesoft/zjmo/test/mocks/MockPlayers.java)
 * [ConductorConnector](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZJMO/src/nl/zeesoft/zjmo/orchestra/client/ConductorConnector.java)

**Test output**  
The output of this test shows how the *ConductorConnector* always maintains open connections to working conductors.  
~~~~
Starting orchestra ...
Starting Database X/0 (control: 6543, work: 6542) ...
Starting Database Y/0 (control: 7654, work: 7653) ...
Starting Application server X/0 (control: 8765, work: 8764) ...
Starting Application server Y/0 (control: 9876, work: 9875) ...
Starting Database X/1 (control: 6541, work: 6540) ...
Starting Database Y/1 (control: 7652, work: 7651) ...
Starting Conductor/1 (control: 5431, work: 5430) ...
Starting Conductor/0 (control: 5433, work: 5432) ...
Starting orchestra took 44 ms

Open clients: 2

Open clients after taking Conductor/0 offline: 1

Got open client to conductor: Conductor/1

Open clients after bringing Conductor/0 online: 2

Got open client to conductor: Conductor/0

Stopping orchestra ...
Sent stop command to: Conductor/0
2017-04-23 11:31:09:696 DBG nl.zeesoft.zdk.thread.WorkerUnion: All workers have been stopped
Sent stop command to: Conductor/1
2017-04-23 11:31:13:701 DBG nl.zeesoft.zdk.thread.WorkerUnion: All workers have been stopped
Stopping players ...
Stopped orchestra
~~~~

nl.zeesoft.zjmo.test.TestPublishRequest
---------------------------------------
This test shows how a *PublishRequest* is executed by a *Conductor* instance.

**Example implementation**  
~~~~
// Create conductor
Conductor con = new Conductor(null,orchestra,0);
// Start the conductor
boolean started = con.start();
// Create conductor connector
ConductorConnector connector = new ConductorConnector(null,null,false);
// Initialize the connector
connector.initialize(orchestra,null);
// Open the connector
connector.open();
// Create publish request for a certain channel
PublishRequest pr = new PublishRequest("Orchestra optional","{\"something\":\"json\"}");
// Publish request on the request channel through a conductor
pr = connector.publishRequest(pr);
// Close the connector
connector.close();
~~~~

This test uses *MockConductor1*, *MockConductor2* and the *MockPlayers*.

Class references;  
 * [TestPublishRequest](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZJMO/src/nl/zeesoft/zjmo/test/TestPublishRequest.java)
 * [MockConductor1](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZJMO/src/nl/zeesoft/zjmo/test/mocks/MockConductor1.java)
 * [MockConductor2](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZJMO/src/nl/zeesoft/zjmo/test/mocks/MockConductor2.java)
 * [MockPlayers](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZJMO/src/nl/zeesoft/zjmo/test/mocks/MockPlayers.java)
 * [PublishRequest](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZJMO/src/nl/zeesoft/zjmo/orchestra/protocol/PublishRequest.java)

**Test output**  
The output of this test shows how the request is handled differently depending on the channel that the request is published on.  
~~~~
Starting orchestra ...
Starting Database X/0 (control: 6543, work: 6542) ...
Starting Database Y/0 (control: 7654, work: 7653) ...
Starting Application server X/0 (control: 8765, work: 8764) ...
Starting Application server Y/0 (control: 9876, work: 9875) ...
Starting Database X/1 (control: 6541, work: 6540) ...
Starting Database Y/1 (control: 7652, work: 7651) ...
Starting Conductor/1 (control: 5431, work: 5430) ...
Starting Conductor/0 (control: 5433, work: 5432) ...
Starting orchestra took 22 ms

Open clients: 2

Got open client to conductor: Conductor/0
Publish request response: {"request": {"echoMe": "Echo me this"},"response": {"response": "Executed command"},"channelName": "Orchestra critical"}

Open clients after taking Conductor/0 offline: 1

Got open client to conductor: Conductor/1
Publish request response: {"error": "Channel subscriber is not online: Conductor/0","request": {"echoMe": "Echo me this"},"channelName": "Orchestra critical"}
Publish request response: {"request": {"echoMe": "Echo me this"},"response": {"response": "Executed command"},"channelName": "Orchestra optional"}

Stopping orchestra ...
Sent stop command to: Conductor/0
2017-04-23 11:31:20:891 DBG nl.zeesoft.zdk.thread.WorkerUnion: All workers have been stopped
Sent stop command to: Conductor/1
2017-04-23 11:31:23:862 DBG nl.zeesoft.zdk.thread.WorkerUnion: All workers have been stopped
Stopping players ...
Stopped orchestra
~~~~

Test results
------------
All 6 tests have been executed successfully (37 assertions).  
Total test duration: 76770 ms (total sleep duration: 29000 ms).  

Memory usage per test;  
 * nl.zeesoft.zjmo.test.TestTestOrchestra: 342 Kb / 0 Mb
 * nl.zeesoft.zjmo.test.TestConductor: 575 Kb / 0 Mb
 * nl.zeesoft.zjmo.test.TestMemberOnlineOffline: 929 Kb / 0 Mb
 * nl.zeesoft.zjmo.test.TestWorkRequest: 732 Kb / 0 Mb
 * nl.zeesoft.zjmo.test.TestConductorConnector: 860 Kb / 0 Mb
 * nl.zeesoft.zjmo.test.TestPublishRequest: 1364 Kb / 1 Mb
