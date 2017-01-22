Zeesoft Intelligent Dialog Lambda
=================================
This project contains the complete source code for the Zeesoft Intelligent Dialog Lambda (ZIDL).
This project was created to be able to connect Alexa to the Zeesoft Intelligent Dialog server Demo (ZIDD).

It is assumed that readers of this documentation have a basic understanding of Alexa Speechlets, Java and Maven.

Intents
-------
The ZIDL handles the following intents;
 * DialogIntent  
   Handles all user dialog input
 * AMAZON.HelpIntent  
   Returns a help message for this Lambda
 * AMAZON.StopIntent  
   Stops the dialog
 * AMAZON.CancelIntent  
   Cancels the dialog

The intent schema for this project can be found in; src/main/java/zidl/speechAssets/IntentSchema.json.
   
Dialog intent
-------------
The main feature of this Lambda is dialog intent request handling.
The idea is simple; all user speech input is posted directly to the dialog server (ZIDD).
The dialog server responds to these post requests with a speech output response which is then returned to Alexa.  
<img alt="Architecture" src="https://raw.githubusercontent.com/DyzLecticus/Zeesoft/master/V2.0/ZIDL/src/main/resources/doc/Architecture.bmp">

Utterances
----------
In order to prepare Alexa for usage of this intent, Alexa requires a sample utterances file.
The utterances file is included in this project; src/main/java/zidl/speechAssets/SampleUtterances.txt.
ZIDD can be used to generate the contents of this file by requesting the URL; alexa/utterances.html.

Configuration
-------------
This lambda requires a URL that specifies the location of the ZIDD.
This URL is currently hard coded in the file; src/main/java/zidl/Config.java (See 'zidsPostDialogUrl').

Build
-----
This project requires Maven and Java 1.7. Make sure they are installed and configured correctly.
To build this project on a windows system, simply execute package.bat in the root of the project folder.
On a unix system, navigate to the root folder and execute the Maven command; 'mvn assembly:assembly -DdescriptorId=jar-with-dependencies package'.
This will create a jar file that can be uploaded to Amazon; target/ZIDL-1.0-jar-with-dependencies.jar.

Installation
------------
To install this Lambda;
 1. Sign in to the Amazon Developer Console at; https://console.aws.amazon.com/console/home?region=us-east-1#
 2. Click on 'Lambda'
 3. Click on the 'Create a Lambda function' button
 4. Click on the 'Skip' button (we will not be using a blueprint)
 5. Enter a name for the function (i.e. 'ZIDL')
 6. Enter a description for the function (i.e. 'Zeesoft Intelligent Dialog Lambda')
 6. Select 'Java 8' as runtime
 8. Under 'Lambda function code' Select 'Upload a ZIP file'
 9. Click on 'Upload' and select the jar file (target/ZIDL-1.0-jar-with-dependencies.jar)
 10. Enter 'zidl.ZIDLSpeechletRequestStreamHandler' as a handler
 11. Select 'Basic execution role' as the role
 12. Increase the timeout to about 20 seconds
 13. Click on the 'Next' button
 14. Click on the 'Create function' button 
 15. Note the ARN that has been assigned to the function, this is needed to identify it.
 
To configure this Lambda as an Alexa skill;
 1. Sign in to the Alexa Developer Portal at; https://developer.amazon.com/home.html
 2. Click on 'Alexa' in the menu bar
 3. Click on the 'Get started' button under 'Alexa Skills Kit'
 4. Click on the 'Add a New Skill' button
 5. Select skill type 'Custom Interaction Model'
 6. Enter a name for the skill (i.e. 'Zeesoft Intelligent Dialog Lambda')
 7. Enter an invocation name for the skill (i.e. 'book')
 8. Click on the 'Next' button
 9. Open the intent schema file (src/main/java/zidl/speechAssets/IntentSchema.json)
 10. Copy the content of the file into the intent schema text area.
 11. Open the sample utterances file (src/main/java/zidl/speechAssets/SampleUtterances.txt)
 12. Copy the content of the file intto the sample utterances text area.  
 13. Click on the 'Next' button
 14. Select 'Lambda ARN' as an endpoint
 15. Enter the ARN assigned to the AWS function that runs the ZIDL
 16. Click on the 'Next' button
 17. Test the skill to see if everything is configured correctly

