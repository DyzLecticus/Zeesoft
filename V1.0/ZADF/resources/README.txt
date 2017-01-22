REQUIREMENTS:
- Windows
- Java 1.7+

DATABASE MODEL:
model.txt

INSTALL:
bin/install.bat

The installation will create several folders and files within the main folder;
data/ - Configuration and database data
out/ - Debug and error output
startServer.bat - Starts the server
startControl.bat - Starts the server control
startGUI.bat - Starts the GUI
stopServer.bat - Stops the server
bin/restore.bat - Restores the database backup
bin/update.bat - Updates the current installation based on another installations backup (older version) 
data/DbConfig.xml - The database configuration (use server control to modify!) 
data/ClConfig.xml - The client configuration (use GUI to modify!)
data/GuiConfig.xml - The GUI configuration (use GUI to modify!)

DEFAULT USERS (name/password - description):
admin/1superAdmin! - The database administrator (ID:1, Active:true, Level:1 and Administrator:true)
userAdmin/1userAdmin! - The user administrator (Level:10)
batchAdmin/1batchAdmin! - The batch administrator (Level:20)
