package nl.zeesoft.zsds;

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.zeesoft.zdk.ZDKFactory;
import nl.zeesoft.zdk.ZStringEncoder;
import nl.zeesoft.zdk.json.JsElem;
import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.WorkerUnion;

/**
 * Servlet implementation class ZIDSServlet
 */
@WebServlet("/ZSDSServlet")
public class ZSDSServlet extends HttpServlet {
	private static final long		serialVersionUID	= 1L;
    
	private String					installDir			= "";
	private boolean					debug				= false;
	private String					key					= "";
	
	private Messenger				messenger			= null;
	private WorkerUnion				union				= null;
	
	@Override
	public void init(ServletConfig config) throws ServletException {
		installDir = config.getInitParameter("installDir");
		if (installDir==null || installDir.length()==0) {
			installDir = config.getServletContext().getRealPath("");
		}
		String dbg = config.getInitParameter("debug");
		if (dbg!=null && dbg.length()>0) {
			debug = Boolean.parseBoolean(dbg);
		}
		
		initializeConfiguration();
		System.out.println("installDir: " + installDir);
		System.out.println("debug: " + debug);
		System.out.println("key: " + key);
		
		// Messenger
		ZDKFactory factory = new ZDKFactory();
		messenger = factory.getMessenger();
		messenger.setPrintDebugMessages(debug);
		messenger.start();

		// Union
		union = factory.getWorkerUnion(messenger);
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}
	
	@Override
	public void destroy() {
		super.destroy();
		messenger.stop();
		union.stopWorkers();
		messenger.whileWorking();
	}

	public String getInstallDir() {
		return installDir;
	}

	public boolean isDebug() {
		return debug;
	}

	public String getKey() {
		return key;
	}

	public Messenger getMessenger() {
		return messenger;
	}

	public WorkerUnion getUnion() {
		return union;
	}
	
	private void initializeConfiguration() {
		String fileName = installDir + "config.json";
		File config = new File(fileName);
		JsFile file = new JsFile();
		ZStringEncoder encode = new ZStringEncoder();
		if (!config.exists()) {
			key = encode.generateNewKey(1024);
			encode.append(key);
			file.rootElement = new JsElem("configuration");
			file.rootElement.children.add(new JsElem("debug","" + debug));
			file.rootElement.children.add(new JsElem("key",encode.compress().toString(),true));
			file.toFile(fileName,true);
		} else {
			file.fromFile(fileName);
			debug = Boolean.parseBoolean(file.rootElement.getChildByName("debug").value.toString());
			encode.append(file.rootElement.getChildByName("key").value.toString());
			key = encode.decompress().toString();
		}
	}
}
