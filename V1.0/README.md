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
This folder contains the complete source code for the following Zeesoft products:
  * ZODB Zeesoft Object Database
  * ZADF Zeesoft Application Development Framework
  * ZPO	 Zeesoft Personal Organizer
  * ZCRM Zeesoft Customer Relationship Manager

Build and run
-------------
Zeesoft products require Java 1.7 to build and run.
All Zeesoft product folders contain simple Ant build files.
Use the 'dist_install' target to create and install the application for development testing purposes.
Use the 'release' target to create separate application and source code zip files.
The application zip files will contain README.txt files with application specific installation and usage instructions.
Zeesoft products have been designed to be installed on Windows operating systems but the installation procedure can be extended to support other operating systems like Unix and Linux.

Extend to implement
-------------------
Zeesoft products are designed to be extended in order to be implemented.
If you want to create your own application, extending one of the Zeesoft products, the best place to start is the Zeesoft Personal Organizer.
It is a relatively simple extension of the Zeesoft Application Development Framework.
If you only want to use the Zeesoft Object Database, you can extend it like the Zeesoft Application Development Framework does.

Icons
-----
Zeesoft does not deserve any credit for the icons in this repository.
If you know who created these icons, please share the information.

ZODB
----
The Zeesoft Object Database is a pure Java object persistence database.
It was created to provide an easy to install, client server data persistence framework for Java applications.
Its collection model is static because it is entirely derived from the custom annotations of the objects it persists.
Object IDs, unique property combinations and links between objects are indexed and loaded in memory when the database server starts.
The actual objects themselves are actively loaded in a separate background process or lazy loaded when needed.
When objects are added, updated or removed a separate background process saves all the changes.
Database configuration changes made using the client control protocol are applied immediately.

Features:
  * User management
  * Session logging
  * White list access control
  * Batch programs
  * Static data request response cashing
  * Separate server control port and protocol
  * Encrypted communication protocols
  * Client protocol software support
  * Database backup (batch program) and restore
  * Generic data update functionality

ZADF
----
The Zeesoft Application Development Framework extends the Zeesoft Object Database.
Together, they provide a fast, secure and reliable framework for application development and management.

Features:
  * Remote server control GUI
  * Database content management GUI
  * Collection model visualization
  * Debugging GUI
  * Collection and property access management
  * Configurable data report generation

ZPO
---
The Zeesoft Personal Organizer extends the Zeesoft Application Development Framework.

Features:
  * Personal task administration and reporting.
  * Personal note administration.

The following screenshots were taken from a fresh installation of the Zeesoft Personal Organizer, using the administrator account in order to show all the features.

**ZPO Login (Control & GUI)**  
<img alt="ZPO Login (Control & GUI)" src="https://raw.githubusercontent.com/DyzLecticus/Zeesoft/master/V1.0/screenshots/ZPO_Login.bmp">

**ZPO Control**  
<img alt="ZPO Control" src="https://raw.githubusercontent.com/DyzLecticus/Zeesoft/master/V1.0/screenshots/ZPO_Control.bmp">

**ZPO Control Configuration**  
<img alt="ZPO Control Configuration" src="https://raw.githubusercontent.com/DyzLecticus/Zeesoft/master/V1.0/screenshots/ZPO_Control_Configuration.bmp">

**ZPO GUI**  
<img alt="ZPO GUI" src="https://raw.githubusercontent.com/DyzLecticus/Zeesoft/master/V1.0/screenshots/ZPO_GUI_Main.bmp">

**ZPO GUI Configuration**  
<img alt="ZPO GUI Configuration" src="https://raw.githubusercontent.com/DyzLecticus/Zeesoft/master/V1.0/screenshots/ZPO_GUI_Configuration.bmp">

**ZPO GUI Model**  
<img alt="ZPO GUI Model" src="https://raw.githubusercontent.com/DyzLecticus/Zeesoft/master/V1.0/screenshots/ZPO_GUI_Model.bmp">

**ZPO GUI Reporter**  
<img alt="ZPO GUI" src="https://raw.githubusercontent.com/DyzLecticus/Zeesoft/master/V1.0/screenshots/ZPO_GUI_Reporter.bmp">

**ZPO Debugger (Control & GUI)**  
<img alt="ZPO Debugger (Control & GUI)" src="https://raw.githubusercontent.com/DyzLecticus/Zeesoft/master/V1.0/screenshots/ZPO_Debug.bmp">

If you are running Windows, you can install the latest release of the Zeesoft Personal Organizer right now.
Make sure you have Java 1.7 installed (you can download and install Java SE from <a href="http://www.oracle.com/technetwork/java/javase/downloads/index.html">Oracle</a>).
Download and extract the <a href="https://github.com/DyzLecticus/Zeesoft/raw/master/V1.0/ZPO/release/ZPO.zip">ZPO.zip</a> file and follow the instructions in the README.txt file.

ZCRM
----
The Zeesoft Customer Relationship Manager extends the Zeesoft Personal Organizer.

Features:
  * Country and value added tax administration.
  * Service and/or product price administration.
  * People and organization contact, address, contract and delivery administration.
  * Invoice generation by batch procedure.

If you are running Windows, you can install the latest release of the Zeesoft Customer Relationship Manager right now.
Make sure you have Java 1.7 installed (you can download and install Java SE from <a href="http://www.oracle.com/technetwork/java/javase/downloads/index.html">Oracle</a>).
Download and extract the <a href="https://github.com/DyzLecticus/Zeesoft/raw/master/V1.0/ZCRM/release/ZCRM.zip">ZCRM.zip</a> file and follow the instructions in the README.txt file.

License
-------
Zeesoft is published under the <a href="http://www.dbad-license.org/">DBAD</a> license.
