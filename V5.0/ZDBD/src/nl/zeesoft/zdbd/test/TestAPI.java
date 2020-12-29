package nl.zeesoft.zdbd.test;

import nl.zeesoft.zdbd.ThemeController;
import nl.zeesoft.zdbd.ThemeControllerSettings;
import nl.zeesoft.zdbd.api.ServerConfig;
import nl.zeesoft.zdk.FileIO;
import nl.zeesoft.zdk.Logger;
import nl.zeesoft.zdk.Str;
import nl.zeesoft.zdk.http.HttpClient;
import nl.zeesoft.zdk.http.HttpServer;
import nl.zeesoft.zdk.test.util.TestObject;
import nl.zeesoft.zdk.test.util.Tester;
import nl.zeesoft.zdk.thread.CodeRunnerChain;
import nl.zeesoft.zdk.thread.Waiter;

public class TestAPI extends TestObject {
	public TestAPI(Tester tester) {
		super(tester);
	}

	public static void main(String[] args) {
		(new TestAPI(new Tester())).runTest(args);
	}

	@Override
	protected void describe() {
		/* TODO: Describe
		System.out.println("This test shows how a *Str* instance can be used to split a comma separated string into a list of *Str* instances. ");
		System.out.println("The *Str* class is designed to add features of the Java String to a Java StringBuilder. ");
		System.out.println("It also contains methods for file writing and reading. ");
		System.out.println();
		System.out.println("**Example implementation**  ");
		System.out.println("~~~~");
		System.out.println("// Create the Str");
		System.out.println("Str str = new Str(\"qwer,asdf,zxcv\");");
		System.out.println("// Split the Str");
		System.out.println("List<Str> strs = str.split(\",\");");
		System.out.println("~~~~");
		System.out.println();
		getTester().describeMock(MockStr.class.getName());
		System.out.println();
		System.out.println("Class references;  ");
		System.out.println(" * " + getTester().getLinkForClass(TestController.class));
		System.out.println(" * " + getTester().getLinkForClass(Str.class));
		System.out.println();
		System.out.println("**Test output**  ");
		System.out.println("The output of this test shows the string input and lists the *Str* objects.  ");
		*/
	}

	@Override
	protected void test(String[] args) {
		Logger.setLoggerDebug(true);
		
		// Create the controller
		ThemeControllerSettings settings = new ThemeControllerSettings();
		settings.soundBankDir = "../../V3.0/ZeeTracker/resources/";
		if (FileIO.checkDirectory(settings.workDir).length()>0) {
			FileIO.mkDirs(settings.workDir);
		}
		ThemeController controller = new ThemeController();
		
		// Create the configuration
		ServerConfig config = new ServerConfig(controller);
		config.setLogger(Logger.logger);
		config.setFilePath(settings.workDir);
		
		// Create the server
		HttpServer server = new HttpServer(config);
		
		// Open the server
		Str error = server.open();
		if (assertEqual(error,new Str(),"Open server does not match expectation")) {

			// Initialize the controller
			CodeRunnerChain chain = controller.initialize(settings);
			Waiter.startAndWaitFor(chain, 10000);
	
			// Create the client
			HttpClient client = new HttpClient("GET","http://127.0.0.1:8080/");
			// Send the request and get the response
			client.sendRequest();
			
			int responseCode = client.getResponseCode();
			assertEqual(responseCode,200,"Response code does not match expectation");
			
			Str responseBody = client.getResponseBody();
			assertEqual(responseBody.startsWith("<html>\n<head>"),true,"Response body start does not match expectation");
			assertEqual(responseBody.endsWith("</body>\n</html>"),true,"Response body end does not match expectation");
	
			sleep(100000);
			
			// Close the server
			error = server.close();
			assertEqual(error,new Str(),"Close server does not match expectation");
			
			// Destroy the controller
			chain = controller.destroy();
			Waiter.startAndWaitFor(chain,5000);
			
			// Wait for connections to close if needed
			Waiter.waitForRunners(server.getActiveRunners(),1000);
		}
	}
}
