package nl.zeesoft.zdk.test.http;

import java.util.List;

import nl.zeesoft.zdk.Logger;
import nl.zeesoft.zdk.Str;
import nl.zeesoft.zdk.http.HttpServer;
import nl.zeesoft.zdk.http.ProxyServerConfig;
import nl.zeesoft.zdk.test.util.TestObject;
import nl.zeesoft.zdk.test.util.Tester;
import nl.zeesoft.zdk.thread.CodeRunner;
import nl.zeesoft.zdk.thread.CodeRunnerManager;
import nl.zeesoft.zdk.thread.Waiter;

public class TestProxyServer extends TestObject {
	public TestProxyServer(Tester tester) {
		super(tester);
	}

	public static void main(String[] args) {
		(new TestProxyServer(new Tester())).runTest(args);
	}

	@Override
	protected void describe() {
		System.out.println("This test is not included in the ZDK test set");
	}

	@Override
	protected void test(String[] args) {
		Logger logger = new Logger(true);
		ProxyServerConfig config = new ProxyServerConfig(logger);
		config.setAllowAll();
		config.setDebugLogHeaders(true);
		HttpServer server = new HttpServer(config);
		
		Str error = server.open();
		assertEqual(error,new Str(),"Opening proxy server returned an unexpected error");
		if (error.length()==0) {
			
			sleep(60000);
			
			error = server.close();
			List<CodeRunner> runners = server.getActiveRunners();
			Waiter.waitTillRunnersDone(runners,1000);
			assertEqual(error,new Str(),"Closing proxy server returned an unexpected error");
		}
		
		assertEqual(CodeRunnerManager.getActiverRunners().size(),0,"Number of active code runners does not match expectation");
	}
}
