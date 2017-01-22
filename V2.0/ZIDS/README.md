Zeesoft Intelligent Dialog Server
=================================
This project was created to use the Zeesoft Artificial Cognition Simulator (ZACS) for dialogs with end users.
It also marks the start of the next phase in development of artificial cognition at Zeesoft; generating self awareness.

Dialogs
-------
The ZACS is very good at language pattern recognition and language generation.
It can learn text input/output combinations for a specific context based on examples.
This would be sufficient for single input/output request but in order to have real meaningful conversations (a related series of input/output requests), a more guided approach is required.
Dialogs are used to define and guide the interaction with the server.
To view the dialogs that are available in the ZIDS, install the application (see instructions below) and navigate to dialogs/dialogs.html.

Sessions
--------
In order to manage and keep track of server interactions, a sessions handling mechanism has been implemented.
These sessions accept external ID's to simplify connections from other software components.
To test dialogs and sessions, install the application (see instructions below) and navigate to dialogs/speaker.html.

Cognitive entities
------------------
ZIDS distinguishes between itself and other cognitive entities it can interact with (humans).
It knows it's own name (which can be overriden) and it can learn the names of humans it interacts with through the 'Handshake' dialogs.  
<img alt="ZIDS Speaker" src="https://raw.githubusercontent.com/DyzLecticus/Zeesoft/master/V2.0/screenshots/ZIDSSpeaker.bmp">

Build
-----
To build this application, configure this project in Eclipse and then execute the 'war' target in the build.xml Ant script.
This will generate the zidd.war file in the root directory of this project.

Installation
------------
To install this application a Java application server like Tomcat or Jetty is needed.
When the application server has been installed, simply deploy the zidd.war file using the standard procedure for the selected application server.

Protocol
--------
The session dialog JSON protocol has been described in the YAML file; <a href="https://raw.githubusercontent.com/DyzLecticus/Zeesoft/master/V2.0/ZIDS/dialog.yaml">dialog.yaml</a>  
