package nl.zeesoft.zdk.test.http;

import java.util.List;

import nl.zeesoft.zdk.FileIO;
import nl.zeesoft.zdk.Logger;
import nl.zeesoft.zdk.Str;
import nl.zeesoft.zdk.http.HttpClient;
import nl.zeesoft.zdk.http.HttpClientManager;
import nl.zeesoft.zdk.http.HttpHeaderList;
import nl.zeesoft.zdk.http.HttpRequestHandler;
import nl.zeesoft.zdk.http.HttpServer;
import nl.zeesoft.zdk.http.HttpServerConfig;
import nl.zeesoft.zdk.http.ProxyRequestHandler;
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
		System.out.println("This test shows how to configure, start and stop an *HttpServer*. ");
		System.out.println("It also shows how an *HttpClient* can be used to send HTTP requests and process the responses. ");
		System.out.println("The *HttpServer* provided by this library does not support HTTPS. ");
		System.out.println("The behavior of the *HttpServer* can be customized by overriding and/or extending the *HttpServerConfig* and *HttpRequestHandler*. ");
		System.out.println("An example of such customization is the *ProxyServerConfig* and *ProxyRequestHandler* which will make the *HttpServer* function as a proxy server. ");
		System.out.println();
		System.out.println("**Example implementation**  ");
		System.out.println("~~~~");
		System.out.println("// Create the configuration");
		System.out.println("HttpServerConfig config = new HttpServerConfig(logger);");
		System.out.println("// Create the server");
		System.out.println("HttpServer server = new HttpServer(config);");
		System.out.println("// Open the server");
		System.out.println("Str error = server.open();");
		System.out.println("// Create the client");
		System.out.println("HttpClient client = new HttpClient(\"GET\",\"http://127.0.0.1:8080/\");");
		System.out.println("// Send the request and get the response");
		System.out.println("client.sendRequest();");
		System.out.println("int responseCode = client.getResponseCode();");
		System.out.println("Str responseBody = client.getResponseBody();");
		System.out.println("// Close the server");
		System.out.println("error = server.close();");
		System.out.println("// Wait for connections to close if needed");
		System.out.println("Waiter.waitForRunners(server.getActiveRunners(),1000);");
		System.out.println("~~~~");
		System.out.println();
		System.out.println("Class references;  ");
		System.out.println(" * " + getTester().getLinkForClass(TestHttpServer.class));
		System.out.println(" * " + getTester().getLinkForClass(HttpServer.class));
		System.out.println(" * " + getTester().getLinkForClass(HttpServerConfig.class));
		System.out.println(" * " + getTester().getLinkForClass(HttpRequestHandler.class));
		System.out.println(" * " + getTester().getLinkForClass(ProxyServerConfig.class));
		System.out.println(" * " + getTester().getLinkForClass(ProxyRequestHandler.class));
		System.out.println();
		System.out.println("**Test output**  ");
		System.out.println("The output of this test shows the full debug logging of a regular HTTP server and a proxy server handling some basic requests.  ");
		System.out.println("It also shows the mocked OS file actions that were performed.  ");
	}

	@Override
	protected void test(String[] args) {
		Logger logger = new Logger(true);
		
		FileIO.mkDirs("http");
		
		HttpServerConfig config = new HttpServerConfig(logger);
		config.setAllowAll();
		config.setDebugLogHeaders(true);
		HttpServer server = new HttpServer(config);
		
		Str indexHtml = new Str("<html><head><head><body>Index.html<body></html>");
		FileIO.writeFile(indexHtml,config.getFilePath() + config.getIndexFileName());
		
		Str error = server.open();
		assertEqual(error,new Str(),"Opening HTTP server returned an unexpected error");
		if (error.length()==0) {
			sleep(10);
			
			HttpClient request = new HttpClient(logger,"GET","http://127.0.0.1:8080/");
			request.sendRequest();
			assertEqual(request.getResponseCode(),200,"Response code does not match expectation (1)");
			assertEqual(request.getResponseBody(),indexHtml,"Response body does not match expectation (1)");
			
			request = new HttpClient(logger,"GET","http://127.0.0.1:8080/pizza.txt");
			request.sendRequest();
			assertEqual(request.getResponseCode(),404,"Response code does not match expectation");
			assertEqual(request.getResponseBody(),new Str("File not found: http/pizza.txt"),"Response body does not match expectation");
			
			request = new HttpClient(logger,"PUT","http://127.0.0.1:8080/pizza.txt");
			request.sendRequest(new Str("I like pizza!"));
			assertEqual(request.getResponseCode(),200,"Response code does not match expectation (2)");
			assertEqual(request.getResponseBody(),new Str(),"Response body does not match expectation (2)");
			
			request = new HttpClient(logger,"GET","http://127.0.0.1:8080/pizza.txt");
			request.sendRequest();
			assertEqual(request.getResponseCode(),200,"Response code does not match expectation (3)");
			assertEqual(request.getResponseBody(),new Str("I like pizza!"),"Response body does not match expectation (3)");
			
			request = new HttpClient(logger,"DELETE","http://127.0.0.1:8080/pizza.txt");
			request.sendRequest();
			assertEqual(request.getResponseCode(),200,"Response code does not match expectation (4)");
			assertEqual(request.getResponseBody(),new Str(),"Response body does not match expectation (4)");
			
			ProxyServerConfig proxyConfig = new ProxyServerConfig(logger);
			proxyConfig.setDebugLogHeaders(true);
			HttpServer proxy = new HttpServer(proxyConfig);
			error = proxy.open();
			assertEqual(error,new Str(),"Opening proxy server returned an unexpected error");
			if (error.length()==0) {
				
				HttpClient client2 = new HttpClient(logger,"GET","http://127.0.0.1:9090/");
				HttpHeaderList headers = new HttpHeaderList();
				headers.addHostHeader("127.0.0.1:8080");
				client2.setHeaders(headers);
				client2.sendRequest();
				assertEqual(client2.getResponseCode(),200,"Proxy response code does not match expectation (5)");
				assertEqual(client2.getResponseBody(),indexHtml,"Proxy response body does not match expectation (5)");
				
				sleep(10);
				error = proxy.close();
				List<CodeRunner> runners = proxy.getActiveRunners();
				Waiter.waitForRunners(runners,1000);
				assertEqual(error,new Str(),"Closing proxy server returned an unexpected error");
			}
			sleep(10);
			error = server.close();
			Waiter.waitForRunners(server.getActiveRunners(),1000);
			assertEqual(error,new Str(),"Closing HTTP server returned an unexpected error");
		}
		
		System.out.println();
		System.out.println("Action log;");
		System.out.println(FileIO.getActionLogStr());
		
		FileIO.renameDir("http", "http_old");
		assertEqual(FileIO.checkFile("http_old/index.html"),new Str(),"Directory file rename failed");
		assertEqual(FileIO.listFiles("http_old").size(),1,"List files result does not match expectation");
		
		assertEqual(HttpClientManager.getConnectedClients().size(),0,"Number of connected clients does not match expectation");		
		assertEqual(CodeRunnerManager.getActiverRunners().size(),0,"Number of active code runners does not match expectation");
	}
}
