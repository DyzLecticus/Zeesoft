package nl.zeesoft.zdk.test.http;

import java.util.List;

import nl.zeesoft.zdk.FileIO;
import nl.zeesoft.zdk.Logger;
import nl.zeesoft.zdk.Str;
import nl.zeesoft.zdk.http.HttpClient;
import nl.zeesoft.zdk.http.HttpServer;
import nl.zeesoft.zdk.http.HttpServerConfig;
import nl.zeesoft.zdk.http.ProxyServerConfig;
import nl.zeesoft.zdk.test.util.TestObject;
import nl.zeesoft.zdk.test.util.Tester;
import nl.zeesoft.zdk.thread.CodeRunner;
import nl.zeesoft.zdk.thread.CodeRunnerManager;
import nl.zeesoft.zdk.thread.Waiter;

public class TestHttpServer extends TestObject {
	public TestHttpServer(Tester tester) {
		super(tester);
	}

	public static void main(String[] args) {
		(new TestHttpServer(new Tester())).runTest(args);
	}

	@Override
	protected void describe() {
		/*
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
		System.out.println(" * " + getTester().getLinkForClass(TestStr.class));
		System.out.println(" * " + getTester().getLinkForClass(Str.class));
		System.out.println();
		System.out.println("**Test output**  ");
		System.out.println("The output of this test shows the string input and lists the *Str* objects.  ");
		*/
	}

	@Override
	protected void test(String[] args) {
		Logger logger = new Logger(true);
		HttpServerConfig config = new HttpServerConfig(logger);
		config.setAllowAll();
		HttpServer server = new HttpServer(config);
		
		Str indexHtml = new Str("<html><head><head><body>Index.html<body></html>");
		FileIO.writeFile(indexHtml,config.getFilePath() + config.getIndexFileName());
		
		Str error = server.open();
		assertEqual(error,new Str(),"Opening HTTP server returned an unexpected error");
		if (error.length()==0) {
			sleep(10);
			
			HttpClient request = new HttpClient(logger,"GET","http://127.0.0.1:8080/");
			request.sendRequest();
			assertEqual(request.getResponseCode(),200,"Response code does not match expectation");
			assertEqual(request.getResponseBody(),indexHtml,"Response body does not match expectation");
			
			request = new HttpClient(logger,"GET","http://127.0.0.1:8080/pizza.txt");
			request.sendRequest();
			assertEqual(request.getResponseCode(),404,"Response code does not match expectation");
			assertEqual(request.getResponseBody(),new Str("File not found: http/pizza.txt"),"Response body does not match expectation");
			
			request = new HttpClient(logger,"PUT","http://127.0.0.1:8080/pizza.txt");
			request.sendRequest(new Str("I like pizza!"));
			assertEqual(request.getResponseCode(),200,"Response code does not match expectation");
			assertEqual(request.getResponseBody(),new Str(),"Response body does not match expectation");
			
			request = new HttpClient(logger,"GET","http://127.0.0.1:8080/pizza.txt");
			request.sendRequest();
			assertEqual(request.getResponseCode(),200,"Response code does not match expectation");
			assertEqual(request.getResponseBody(),new Str("I like pizza!"),"Response body does not match expectation");
			
			request = new HttpClient(logger,"DELETE","http://127.0.0.1:8080/pizza.txt");
			request.sendRequest();
			assertEqual(request.getResponseCode(),200,"Response code does not match expectation");
			assertEqual(request.getResponseBody(),new Str(),"Response body does not match expectation");
			
			System.out.println();
			System.out.println("Action log;");
			System.out.println(FileIO.getActionLogStr());
			System.out.println();
			
			ProxyServerConfig proxyConfig = new ProxyServerConfig(logger);
			HttpServer proxy = new HttpServer(proxyConfig);
			error = proxy.open();
			assertEqual(error,new Str(),"Opening proxy server returned an unexpected error");
			if (error.length()==0) {
				
				HttpClient client2 = new HttpClient(logger,"GET","http://127.0.0.1:9090/");
				client2.getHeaders().addHostHeader("127.0.0.1:8080");
				client2.sendRequest();
				assertEqual(client2.getResponseCode(),200,"Proxy response code does not match expectation");
				assertEqual(client2.getResponseBody(),indexHtml,"Proxy response body does not match expectation");
				
				sleep(10);
				error = proxy.close();
				List<CodeRunner> runners = proxy.getActiveRunners();
				Waiter.waitTillRunnersDone(runners,1000);
				assertEqual(error,new Str(),"Closing proxy server returned an unexpected error");
			}
			sleep(10);
			error = server.close();
			List<CodeRunner> runners = server.getActiveRunners();
			Waiter.waitTillRunnersDone(runners,1000);
			assertEqual(error,new Str(),"Closing HTTP server returned an unexpected error");
		}
		
		assertEqual(CodeRunnerManager.getActiverRunners().size(),0,"Number of active code runners does not match expectation");
	}
}
