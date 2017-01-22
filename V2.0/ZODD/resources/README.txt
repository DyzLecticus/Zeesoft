REQUIREMENTS:
- Windows
- Java 1.7+
- Make sure the java executable is directly available through the PATH system variable. 

SECURITY CONCERNS:
PLEASE NOTE that the default database server is NOT SECURE ENOUGH TO BE EXPOSED DIRECTLY TO THE INTERNET. 

INSTALL:
Execute install.bat

The installation will create several folders and files within the main folder;
conf/ - Configuration files
data/ - Database model and data
out/ - Debug and error output
start.bat - Starts the database
stop.bat - Stops the database
control.bat - Shows the database controller (if the database has been started)
conf/DbConfig.xml - The database configuration 
conf/CcConfig.xml - The database cache configuration 
conf/SvrConfig.xml - The server configuration

When the installation is done, the database server will be started.
Starting the database server will also try to start your browser at URL http://localhost:5432/

LICENSE:
This product publisheed under the DBAD license. 
Check http://www.dbad-license.org/ for more information.
