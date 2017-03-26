Zeesoft JSON Machine Orchestration
==================================
Zeesoft JSON Machine Orchestration (ZJMO) is an open source library for Java application development.
The aim of the ZJMO project is to provide a scalable, high availability, JSON based, work distribution architecture.
Imagine an orchestra where the conductor directs the other members to play a certain composition.
All the members speak JSON on two TCP ports; one for control and another one for work.
This library depends on the [Zeesoft Development Kit](https://github.com/DyzLecticus/Zeesoft/tree/master/V3.0/ZDK/).  

**Release downloads**  
Click [here](https://github.com/DyzLecticus/Zeesoft/raw/master/V3.0/ZJMO/releases/zjmo-0.9.11.zip) to download the latest ZJMO release (version 0.9.11).  
All ZJMO releases can be downloaded [here](https://github.com/DyzLecticus/Zeesoft/raw/master/V3.0/ZJMO/releases/).  
*All jar files in the release include source code and build scripts.*  

**Self documenting and self testing**  
The tests used to develop this libary are also used to generate this README file.  
Run the [ZJMO](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZJMO/src/nl/zeesoft/zjmo/test/ZJMO.java) class as a java application to print this documentation to the standard out.  
Click [here](#test-results) to scroll down to the test result summary.  

nl.zeesoft.zjmo.test.TestTestOrchestra
--------------------------------------
This test shows how to create and initialize a *TestOrchestra* instance.

**Example implementation**  
~~~~
// Create orchestra
TestOrchestra orch = new TestOrchestra();
// Initialize the orchestra
orch.initialize();
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
Conductor con = new Conductor(orchestra);
// Start the conductor
boolean started = con.start();
// Create client using conductor control port settings
MemberClient client = new MemberClient("localhost",5433);
// Send stop command
ZStringBuilder response = client.sendCommand(ProtocolControl.STOP_PROGRAM);
~~~~

This test uses the *MockConductor* and the *MockPlayers*.

Class references;  
 * [TestConductor](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZJMO/src/nl/zeesoft/zjmo/test/TestConductor.java)
 * [MockConductor1](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZJMO/src/nl/zeesoft/zjmo/test/mocks/MockConductor1.java)
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
Starting orchestra took 1223 ms

GET_STATE command response: {"state": "ONLINE","workLoad": 0,"memoryUsage": 6107344}

Orchestra state JSON:
{
  "positions": {
    "0": "Conductor",
    "1": "Database X",
    "2": "Database Y",
    "3": "Application server X",
    "4": "Application server Y"
  },
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
      "memoryUsage": 4492168
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
      "memoryUsage": 6626216
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
      "memoryUsage": 6428256
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
      "memoryUsage": 6882424
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
      "memoryUsage": 6125776
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
      "memoryUsage": 6474392
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
      "memoryUsage": 6195912
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
      "memoryUsage": 6845640
    }
  ]
}

Stopping orchestra ...
Sent stop command to: Conductor/0
2017-03-27 01:31:08:176 DBG nl.zeesoft.zdk.thread.WorkerUnion: All workers have been stopped
Sent stop command to: Conductor/1
2017-03-27 01:31:13:168 DBG nl.zeesoft.zdk.thread.WorkerUnion: All workers have been stopped
Stopping players ...
Stopped orchestra
2017-03-27 01:31:14:157 DBG nl.zeesoft.zdk.thread.WorkerUnion: All workers have been stopped
2017-03-27 01:31:14:259 DBG nl.zeesoft.zdk.thread.WorkerUnion: All workers have been stopped
2017-03-27 01:31:14:362 DBG nl.zeesoft.zdk.thread.WorkerUnion: All workers have been stopped
2017-03-27 01:31:14:465 DBG nl.zeesoft.zdk.thread.WorkerUnion: All workers have been stopped
2017-03-27 01:31:14:567 DBG nl.zeesoft.zdk.thread.WorkerUnion: All workers have been stopped
2017-03-27 01:31:14:669 DBG nl.zeesoft.zdk.thread.WorkerUnion: All workers have been stopped
~~~~

nl.zeesoft.zjmo.test.TestMemberOnlineOffline
--------------------------------------------
This test shows how to how a *Conductor* maintains its orchestra member state representation.

**Example implementation**  
~~~~
// Create conductor
Conductor con = new Conductor(orchestra);
// Start the conductor
boolean started = con.start();
// Create client using conductor control port settings
MemberClient client = new MemberClient("localhost",5433);
// Send get member state request
ZStringBuilder response = client.sendCommand(ProtocolControl.GET_MEMBER_STATE,"id","[MEMBERID]");
~~~~

This test uses the *MockConductor* and the *MockPlayers*.

Class references;  
 * [TestMemberOnlineOffline](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZJMO/src/nl/zeesoft/zjmo/test/TestMemberOnlineOffline.java)
 * [MockConductor1](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZJMO/src/nl/zeesoft/zjmo/test/mocks/MockConductor1.java)
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
Starting orchestra took 9188 ms

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
  "memoryUsage": 8179744
}

Stopping backup ...
Stopped backup
2017-03-27 01:31:25:890 DBG nl.zeesoft.zdk.thread.WorkerUnion: All workers have been stopped

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
  "errorTime": 1490571085889,
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
  "memoryUsage": 9828560
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
  "memoryUsage": 10137632
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
  "memoryUsage": 10219104
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
  "memoryUsage": 10366320
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
  "memoryUsage": 10418056
}

Stopping orchestra ...
Sent stop command to: Conductor/0
2017-03-27 01:31:35:966 DBG nl.zeesoft.zdk.thread.WorkerUnion: All workers have been stopped
Sent stop command to: Conductor/1
2017-03-27 01:31:40:967 DBG nl.zeesoft.zdk.thread.WorkerUnion: All workers have been stopped
Stopping players ...
Stopped orchestra
~~~~

nl.zeesoft.zjmo.test.TestWorkRequest
------------------------------------
This test shows how to how a *Conductor* handles work requests.

**Example implementation**  
~~~~
// Create conductor
Conductor con = new Conductor(orchestra);
// Start the conductor
boolean started = con.start();
// Create client using conductor work port settings
MemberClient client = new MemberClient("localhost",5432);
// Create work request
WorkRequest wr = new WorkRequest("Database X","{\"something\":\"json\"}");
// Send work request to conductor
ZStringBuilder response = client.writeOutputReadInput(wr.toJson().toStringBuilder());
~~~~

This test uses the *MockConductor* and the *MockPlayers*.

Class references;  
 * [TestWorkRequest](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZJMO/src/nl/zeesoft/zjmo/test/TestWorkRequest.java)
 * [MockConductor1](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZJMO/src/nl/zeesoft/zjmo/test/mocks/MockConductor1.java)
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
Starting orchestra took 3182 ms

Sending work request: {"positionName": "Database X","request": {"echoMe": "Echo me this","sleep": 1000}}

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
  "memoryUsage": 7413448
}

Work request response: {"positionName": "Database X","request": {"echoMe": "Echo me this","sleep": 1000},"response": {"echoMe": "Echo me this","sleep": 1000}}

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
  "memoryUsage": 7584928
}

Sending work request to backup: {"positionName": "Database X","request": {"echoMe": "Echo me this"}}
Work request response from backup: {"positionName": "Database X","request": {"echoMe": "Echo me this"},"response": {"echoMe": "Echo me this"}}
First work request to backup took 4 ms

Sending work request to backup: {"positionName": "Database X","request": {"echoMe": "Echo me this"}}
Work request response from backup: {"positionName": "Database X","request": {"echoMe": "Echo me this"},"response": {"echoMe": "Echo me this"}}
Second work request to backup took 4 ms

Sending work request to backup: {"positionName": "Database X","request": {"echoMe": "Echo me this","sleep": 3000}}
Work request response from backup: {"positionName": "Database X","error": "Work request timed out on: Database X/1","request": {"echoMe": "Echo me this","sleep": 3000}}
time out work request to backup took 2015 ms

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
  "memoryUsage": 7976088
}
2017-03-27 01:31:52:207 ERR nl.zeesoft.zjmo.orchestra.members.ConductorMemberController: No players online for: Database X (members: 2)

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
  "memoryUsage": 7976088
}

Stopping orchestra ...
Sent stop command to: Conductor/0
2017-03-27 01:31:56:354 WRN nl.zeesoft.zdk.thread.WorkerUnion: Stopping worker: nl.zeesoft.zjmo.orchestra.ConductorConnectorWorker
Sent stop command to: Conductor/1
2017-03-27 01:32:01:246 DBG nl.zeesoft.zdk.thread.WorkerUnion: All workers have been stopped
Stopping players ...
Stopped orchestra
~~~~

Test results
------------
All 4 tests have been executed successfully (24 assertions).  
Total test duration: 57551 ms (total sleep duration: 22000 ms).  

Memory usage per test;  
 * nl.zeesoft.zjmo.test.TestTestOrchestra: 320 Kb / 0 Mb
 * nl.zeesoft.zjmo.test.TestConductor: 989 Kb / 0 Mb
 * nl.zeesoft.zjmo.test.TestMemberOnlineOffline: 879 Kb / 0 Mb
 * nl.zeesoft.zjmo.test.TestWorkRequest: 374 Kb / 0 Mb
