Zeesoft products
================
Have you ever tried to create a serious multi user client server Java application that requires object persistence?
Pretty soon you'll be looking at Maven, Spring, Hibernate, SQL databases and a long list of Apache libraries.
You spend months learning all that stuff, knitting it all together to suit your purpose and you have not even started on the GUI.
All you wanted to do is write some cool Java code but now when you try to build your project, 
Maven starts downloading the internet and then breaks your build.
Zeesoft products are created to avoid all that noise and let you focus on writing that cool Java code.
 
Contents
--------
This folder contains the complete source code for the following products;
 * ZODB Zeesoft Object Database
 * ZODD Zeesoft Object Database Demo
 * ZACS Zeesoft Artificial Cognition Simulator
 * ZIDS Zeesoft Intelligent Dialog Server
 * ZIDD Zeesoft Intelligent Dialog server Demo
 * ZIDL Zeesoft Intelligent Dialog Lambda
 * ZDSM Zeesoft Domain Service Monitor
 
ZODB is a fast and lightweight object oriented database written in Java. 
ZODD is an implementation of the ZODB that generates 250000 objects upon installation in order to demonstrate the capabilities of the ZODB.
ZACS is an artificial cognition simulator that learns to produce text output based on examples and then uses that knowledge to complete assignments.
ZIDS is a JSON speaking implementation of the ZACS that functions as a chat bot and can be deployed in any servlet container.
ZIDD is an implementation of the ZIDS that adds a room booking dialog and some features for compatibility with Amazon's Alexa.
ZIDL is an Amazon Speechlet that can be used to interface between Amazon's Alexa and instances of the ZIDS (like the ZIDD).
ZDSM is a program that allows users to to monitor and analyze trends for a set of processes and services across several domains.

If you are running Windows, you can install the latest release of any of the Zeesoft core products right now.
Make sure you have Java 1.7 installed (you can download and install Java SE from <a href="http://www.oracle.com/technetwork/java/javase/downloads/java-archive-downloads-javase7-521261.html">Oracle</a>).
Download and extract the corresponding zip file and follow the instructions in the README.txt file;
 * <a href="https://github.com/DyzLecticus/Zeesoft/raw/master/V2.0/ZODB/ZODB.zip">ZODB.zip</a>
 * <a href="https://github.com/DyzLecticus/Zeesoft/raw/master/V2.0/ZODD/ZODD.zip">ZODD.zip</a>
 * <a href="https://github.com/DyzLecticus/Zeesoft/raw/master/V2.0/ZACS/ZACS.zip">ZACS.zip</a>
 * <a href="https://github.com/DyzLecticus/Zeesoft/raw/master/V2.0/ZDSM/ZDSM.zip">ZDSM.zip</a>
 
To install the ZIDS/ZIDD you need a servlet container like <a href="https://tomcat.apache.org/whichversion.html">Tomcat</a>.
When installed, you can download the <a href="https://github.com/DyzLecticus/Zeesoft/raw/master/V2.0/ZIDS/ZIDS.war">ZIDS.war</a> file and then deploy it following the standard procedure.

Configuration and installation instructions for the ZIDL can be found in the corresponding folder.

Improvements
------------
A major improvement in the Zeesoft version 2.0 architecture is that the database can handle requests faster due to;
 * Improved task separation; all requests are divided into smaller tasks.
 * Improved multi threading; each request gets its own thread.
 * Improved locking; collection and object locking in stead of database and collection locking.
 * Improved indexing; get requests will auto select up to five indexes and combine them to quickly filter results.

Furthermore, the version 2.0 database data model is now dynamic in stead of hard coded.
It allows users to create and change an object oriented data model that includes the following features;
 * Abstract classes and multiple inheritance for class properties
 * Unique constraints that can check property value combinations over multiple classes
 * Support for property types; string, number and link
 * Link properties support many to many relationships

Another significant improvement is the separation of the database server from the database itself.
The default database server is a very basic HTTP server that supports GET and POST requests and can be extended or replaced.
Please note that this default database server and its authorization mechanism are not secure enough to be exposed directly to the internet.
The default database server also provides the GUI to update the database data model or revert changes.

ZODB Screenshots
----------------
**Controller**  
If the ZODB is installed on a server that supports a graphic environment, a controller will be available for the database server.  
<img alt="ZODB Controller" src="https://raw.githubusercontent.com/DyzLecticus/Zeesoft/master/V2.0/screenshots/ZODBController.bmp">

**Controller tray icon**  
If the graphic environment supports tray icons, the controller will minimize to a tray icon.  
<img alt="ZODB Controller tray icon" src="https://raw.githubusercontent.com/DyzLecticus/Zeesoft/master/V2.0/screenshots/ZODBTrayIcon.bmp">

**Browser interface**  
The database server provides a browser interface that can be used to update the database data model and more.  
<img alt="ZODB Browser interface" src="https://raw.githubusercontent.com/DyzLecticus/Zeesoft/master/V2.0/screenshots/ZODBBrowserInterface.bmp">

ZACS Screenshots
----------------
**Home**  
The home page summarizes the ideas behind this project.  
<img alt="ZACS Home" src="https://raw.githubusercontent.com/DyzLecticus/Zeesoft/master/V2.0/screenshots/ZACSHome.bmp">

**Monitor**  
The monitor provides a nice summary of the state history of the simulation.  
<img alt="ZACS Monitor" src="https://raw.githubusercontent.com/DyzLecticus/Zeesoft/master/V2.0/screenshots/ZACSMonitor.bmp">

**Control**  
The control page can be used to configure the simulation parameters and issue commands.  
<img alt="ZACS Control" src="https://raw.githubusercontent.com/DyzLecticus/Zeesoft/master/V2.0/screenshots/ZACSControl.bmp">

**Assignments**  
Assignments are used to challenge, test and improve the simulation.  
<img alt="ZACS Assignments" src="https://raw.githubusercontent.com/DyzLecticus/Zeesoft/master/V2.0/screenshots/ZACSAssignments.bmp">

ZIDS Screenshots
----------------
**Dialogs**  
The dialogs overview shows the dialog details and can be expanded to show the corresponding examples.  
<img alt="ZIDS Dialogs" src="https://raw.githubusercontent.com/DyzLecticus/Zeesoft/master/V2.0/screenshots/ZIDSDialogs.bmp">

**Speaker**  
The speaker implements the dialog JSON POST interface.  
<img alt="ZIDS Speaker" src="https://raw.githubusercontent.com/DyzLecticus/Zeesoft/master/V2.0/screenshots/ZIDSSpeaker.bmp">

**Poster**  
The poster implements the database data JSON POST interface.  
<img alt="ZIDS Poster" src="https://raw.githubusercontent.com/DyzLecticus/Zeesoft/master/V2.0/screenshots/ZIDSPoster.bmp">
