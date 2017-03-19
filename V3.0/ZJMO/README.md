Zeesoft JSON Machine Orchestration
==================================
Zeesoft JSON Machine Orchestration (ZJMO) is an open source library for Java application development.
The aim of the ZJMO project is to provide a scalable, high availability, JSON based, work distribution architecture.
Imagine an orchestra where the conductor directs the other members to play a certain composition.
All the members speak JSON on two TCP ports; one for control and another one for work.
This library depends on the [Zeesoft Development Kit](https://github.com/DyzLecticus/Zeesoft/tree/master/V3.0/ZDK/).  

**Release downloads**  
Click [here](https://github.com/DyzLecticus/Zeesoft/raw/master/V3.0/ZJMO/releases/zjmo-0.9.2.zip) to download the latest ZJMO release (version 0.9.2).  
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
 * [MockConductor](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZJMO/src/nl/zeesoft/zjmo/test/mocks/MockConductor.java)
 * [MockPlayers](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZJMO/src/nl/zeesoft/zjmo/test/mocks/MockPlayers.java)
 * [Conductor](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZJMO/src/nl/zeesoft/zjmo/orchestra/members/Conductor.java)
 * [Player](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZJMO/src/nl/zeesoft/zjmo/orchestra/members/Player.java)

**Test output**  
The output of this test shows;  
 * The orchestra initialization duration.  
 * The conductor GET_STATE command response.  
 * The orchestra state JSON.  
~~~~
Starting members ...
Starting Database X/0 (control: 6543, work: 6542) ...
Starting Database Y/0 (control: 7654, work: 7653) ...
Starting Application server X/0 (control: 8765, work: 8764) ...
Starting Application server Y/0 (control: 9876, work: 9875) ...
Starting Database X/1 (control: 6541, work: 6540) ...
Starting Database Y/1 (control: 7652, work: 7651) ...
Starting Conductor/0 (control: 5433, work: 5432) ...
Starting members took 149 ms

GET_STATE command response: {"state": "ONLINE","workLoad": 0,"memoryUsage": 3557536}

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
      "workRequestTimeoutDrain": false,
      "state": "ONLINE",
      "workLoad": 0,
      "memoryUsage": 3124384
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
      "memoryUsage": 4587912
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
      "memoryUsage": 3649464
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
      "memoryUsage": 2661056
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
      "memoryUsage": 4895032
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
      "memoryUsage": 4276232
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
      "memoryUsage": 3939912
    }
  ]
}

Stopping conductor ...
2017-03-19 18:07:41:633 DBG nl.zeesoft.zdk.thread.WorkerUnion: All workers have been stopped

Stopping players ...
2017-03-19 18:07:42:613 DBG nl.zeesoft.zdk.thread.WorkerUnion: All workers have been stopped
2017-03-19 18:07:42:714 DBG nl.zeesoft.zdk.thread.WorkerUnion: All workers have been stopped
2017-03-19 18:07:42:819 DBG nl.zeesoft.zdk.thread.WorkerUnion: All workers have been stopped
2017-03-19 18:07:42:921 DBG nl.zeesoft.zdk.thread.WorkerUnion: All workers have been stopped
2017-03-19 18:07:43:023 DBG nl.zeesoft.zdk.thread.WorkerUnion: All workers have been stopped
2017-03-19 18:07:43:127 DBG nl.zeesoft.zdk.thread.WorkerUnion: All workers have been stopped
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
 * [MockConductor](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZJMO/src/nl/zeesoft/zjmo/test/mocks/MockConductor.java)
 * [MockPlayers](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZJMO/src/nl/zeesoft/zjmo/test/mocks/MockPlayers.java)
 * [Conductor](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZJMO/src/nl/zeesoft/zjmo/orchestra/members/Conductor.java)
 * [Player](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZJMO/src/nl/zeesoft/zjmo/orchestra/members/Player.java)

**Test output**  
The output of this test shows how the orchestra member state representation changes while the state of one of the backup members is manipulated.  
~~~~
Starting members ...
Starting Database X/0 (control: 6543, work: 6542) ...
Starting Database Y/0 (control: 7654, work: 7653) ...
Starting Application server X/0 (control: 8765, work: 8764) ...
Starting Application server Y/0 (control: 9876, work: 9875) ...
Starting Database X/1 (control: 6541, work: 6540) ...
Starting Conductor/0 (control: 5433, work: 5432) ...
Starting members took 2054 ms

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
  "memoryUsage": 3719528
}

Stopping backup ...
Stopped backup
2017-03-19 18:07:47:312 DBG nl.zeesoft.zdk.thread.WorkerUnion: All workers have been stopped

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
  "errorTime": 1489943267211,
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
  "memoryUsage": 4291960
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
  "memoryUsage": 4566632
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
  "memoryUsage": 4669272
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
  "memoryUsage": 4783136
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
  "memoryUsage": 4825512
}

Stopping conductor ...
2017-03-19 18:07:58:393 DBG nl.zeesoft.zdk.thread.WorkerUnion: All workers have been stopped

Stopping players ...
2017-03-19 18:07:59:458 DBG nl.zeesoft.zdk.thread.WorkerUnion: All workers have been stopped
2017-03-19 18:07:59:560 DBG nl.zeesoft.zdk.thread.WorkerUnion: All workers have been stopped
2017-03-19 18:07:59:661 DBG nl.zeesoft.zdk.thread.WorkerUnion: All workers have been stopped
2017-03-19 18:07:59:764 DBG nl.zeesoft.zdk.thread.WorkerUnion: All workers have been stopped
2017-03-19 18:07:59:865 DBG nl.zeesoft.zdk.thread.WorkerUnion: All workers have been stopped
2017-03-19 18:07:59:967 DBG nl.zeesoft.zdk.thread.WorkerUnion: All workers have been stopped
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
 * [MockConductor](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZJMO/src/nl/zeesoft/zjmo/test/mocks/MockConductor.java)
 * [MockPlayers](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZJMO/src/nl/zeesoft/zjmo/test/mocks/MockPlayers.java)
 * [WorkRequest](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZJMO/src/nl/zeesoft/zjmo/orchestra/protocol/WorkRequest.java)

**Test output**  
The output of this test shows how one of the players handles a work request while draining offline, allowing a backup player to take over the position for the next work requests.  
~~~~
Starting members ...
Starting Database X/0 (control: 6543, work: 6542) ...
Starting Database Y/0 (control: 7654, work: 7653) ...
Starting Application server X/0 (control: 8765, work: 8764) ...
Starting Application server Y/0 (control: 9876, work: 9875) ...
Starting Database X/1 (control: 6541, work: 6540) ...
Starting Database Y/1 (control: 7652, work: 7651) ...
Starting Conductor/0 (control: 5433, work: 5432) ...
Starting members took 88 ms

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
  "memoryUsage": 3267008
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
  "memoryUsage": 3464744
}

Sending work request to backup: {"positionName": "Database X","request": {"echoMe": "Echo me this"}}
Work request response from backup: {"positionName": "Database X","request": {"echoMe": "Echo me this"},"response": {"echoMe": "Echo me this"}}
First work request to backup took 8 ms

Sending work request to backup: {"positionName": "Database X","request": {"echoMe": "Echo me this"}}
Work request response from backup: {"positionName": "Database X","request": {"echoMe": "Echo me this"},"response": {"echoMe": "Echo me this"}}
Second work request to backup took 5 ms

Sending work request to backup: {"positionName": "Database X","request": {"echoMe": "Echo me this","sleep": 3000}}
Work request response from backup: {"positionName": "Database X","error": "Work request timed out on: Database X/1","request": {"echoMe": "Echo me this","sleep": 3000}}
Second work request to backup took 2017 ms

Player state JSON:
{
  "positionName": "Database X",
  "positionBackupNumber": 1,
  "ipAddressOrHostName": "localhost",
  "controlPort": 6541,
  "workPort": 6540,
  "workRequestTimeout": 2000,
  "workRequestTimeoutDrain": true,
  "state": "DRAINING_OFFLINE",
  "workLoad": 1,
  "memoryUsage": 3864904
}
2017-03-19 18:08:07:162 ERR nl.zeesoft.zjmo.orchestra.members.ConductorMemberController: No players online for: Database X (members: 2)

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
  "workLoad": 0,
  "memoryUsage": 3999880
}

Stopping conductor ...
2017-03-19 18:08:10:220 DBG nl.zeesoft.zdk.thread.WorkerUnion: All workers have been stopped

Stopping players ...
2017-03-19 18:08:11:284 DBG nl.zeesoft.zdk.thread.WorkerUnion: All workers have been stopped
2017-03-19 18:08:11:386 DBG nl.zeesoft.zdk.thread.WorkerUnion: All workers have been stopped
2017-03-19 18:08:11:489 DBG nl.zeesoft.zdk.thread.WorkerUnion: All workers have been stopped
2017-03-19 18:08:11:591 DBG nl.zeesoft.zdk.thread.WorkerUnion: All workers have been stopped
2017-03-19 18:08:11:693 DBG nl.zeesoft.zdk.thread.WorkerUnion: All workers have been stopped
2017-03-19 18:08:11:795 DBG nl.zeesoft.zdk.thread.WorkerUnion: All workers have been stopped
~~~~

Test results
------------
All 4 tests have been executed successfully (64 assertions).  
Total test duration: 32575 ms (total sleep duration: 25000 ms).  

Memory usage per test;  
 * nl.zeesoft.zjmo.test.TestTestOrchestra: 300 Kb / 0 Mb
 * nl.zeesoft.zjmo.test.TestConductor: 940 Kb / 0 Mb
 * nl.zeesoft.zjmo.test.TestMemberOnlineOffline: 781 Kb / 0 Mb
 * nl.zeesoft.zjmo.test.TestWorkRequest: 306 Kb / 0 Mb
