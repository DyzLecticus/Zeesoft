Zeesoft Intelligent Dialog Demo
===============================
This project contains the complete source code for the Zeesoft Intelligent Dialog Demo (ZIDD) application.
This application extends the Zeesoft Intelligent Dialog Server (ZIDS).
It adds some dialogs to the ZIDS in order to demonstrate a 'room booking' implementation.

It is assumed that readers of this documentation have a basic understanding of Java and Java EE application servers.

Build
-----
To build this application, configure this project in Eclipse and then execute the 'war' target in the build.xml Ant script.
This will generate the zidd.war file in the root directory of this project.

Installation
------------
To install this application a Java application server like Tomcat or Jetty is needed.
When the application server has been installed, simply deploy the zidd.war file using the standard procedure for the selected application server.
A manifest.yml file has been added to this project for easy IBM Bluemix deployement; <a href="https://raw.githubusercontent.com/DyzLecticus/Zeesoft/master/V2.0/ZIDD/manifest.yml">manifest.yml</a>  
