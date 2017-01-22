package nl.zeesoft.zodb.database.server;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zodb.Messenger;
import nl.zeesoft.zodb.ZODB;
import nl.zeesoft.zodb.database.DbConfig;
import nl.zeesoft.zodb.database.DbDataObject;
import nl.zeesoft.zodb.database.model.MdlClass;
import nl.zeesoft.zodb.database.model.MdlLink;
import nl.zeesoft.zodb.database.model.MdlModel;
import nl.zeesoft.zodb.database.model.MdlPackage;
import nl.zeesoft.zodb.database.model.MdlProperty;
import nl.zeesoft.zodb.database.model.MdlString;
import nl.zeesoft.zodb.database.request.ReqAdd;
import nl.zeesoft.zodb.database.request.ReqDataObject;
import nl.zeesoft.zodb.database.request.ReqGet;
import nl.zeesoft.zodb.database.request.ReqGetFilter;
import nl.zeesoft.zodb.database.request.ReqRemove;
import nl.zeesoft.zodb.database.request.ReqUpdate;
import nl.zeesoft.zodb.file.HTMLFile;
import nl.zeesoft.zodb.file.xml.XMLElem;
import nl.zeesoft.zodb.file.xml.XMLFile;

public class SvrHTTPResourceFactory  {
	private static final int				MAX_SELECT_COLUMNS			= 5;
	private static final int				MAX_INPUT_LENGTH			= 100; // If maxLength > MAX_INPUT_LENGTH then use text area

	public static final String				BACKGROUND_COLOR			= "#F0F0F0";
	public static final String				INDEX_HTML					= "index.html";
	public static final String				INSTALLED_HTML				= "installed.html";
	public static final String				AUTHORIZATION_MANAGER_HTML	= "authorizationManager.html";
	public static final String				AUTHORIZER_HTML				= "authorizer.html";
	public static final String				RESOURCE_NOT_FOUND_HTML		= "404.html";
	
	private SortedMap<String,StringBuilder>	generateFiles				= new TreeMap<String,StringBuilder>();
	private SortedMap<String,BufferedImage>	generateImages				= new TreeMap<String,BufferedImage>();
	
	protected final void addFile(String fileName,StringBuilder content) {
		generateFiles.put(fileName,content);
	}

	protected final void addImage(String fileName,BufferedImage image) {
		generateImages.put(fileName,image);
	}
	
	protected final int getSize() {
		return (generateFiles.size() + generateImages.size());
	}
	
	protected final boolean generateHTTPResources() {
		Messenger.getInstance().debug(this,"Generating " + getSize() + " HTTP resources ...");
		boolean generated = true;
		for (Entry<String,BufferedImage> entry: generateImages.entrySet()) {
			generated = SvrController.getInstance().writeImage(entry.getKey(),entry.getValue());
			if (!generated) {
				break;
			}
		}
		if (generated) {
			for (Entry<String,StringBuilder> entry: generateFiles.entrySet()) {
				generated = SvrController.getInstance().writeFile(entry.getKey(),entry.getValue());
				if (!generated) {
					break;
				}
			}
		}
		if (generated) {
			Messenger.getInstance().debug(this,"Generated " + getSize() + " HTTP resources");
		} else {
			Messenger.getInstance().debug(this,"An error occured while generating HTTP resources to: " + SvrConfig.getInstance().getDataDir());
		}
		return generated;
	}
	
	protected final void reinitializeHTTPResources() {
		generateFiles.clear();
		generateImages.clear();
		initializeHTTPResources();
	}
	
	protected void initializeHTTPResources() {
		// Basic content
		addFile(INDEX_HTML,getIndexHtml().toStringBuilder());
		addFile("home.html",getHomeHtml().toStringBuilder());
		addFile("about.html",getAboutHtml().toStringBuilder());
		addFile("model.html",getModelHtml().toStringBuilder());
		addFile("data.html",getDataHtml().toStringBuilder());
		addFile(INSTALLED_HTML,getInstalledHtml().toStringBuilder());
		addFile(RESOURCE_NOT_FOUND_HTML,get404Html().toStringBuilder());
		addFile("about.html",getAboutHtml().toStringBuilder());
		addImage("favicon.png",generateFavicon());
		
		// Models
		XMLFile model = DbConfig.getInstance().getModel().toXML();
		addFile("abstractModel.xml",model.toStringBuilder());
		model.cleanUp();
		model = DbConfig.getInstance().getModel().toXML(true,true);
		addFile("realModel.xml",model.toStringBuilder());
		model.cleanUp();
		
		// Example POST XML
		XMLFile r = new XMLFile();
		r.setRootElement(new XMLElem("authorize",null,null));
		new XMLElem("password",new StringBuilder("password"),r.getRootElement());
		new XMLElem("updatePassword",new StringBuilder("optionalUpdatePassword"),r.getRootElement());
		addFile("postAuthorize.xml",r.toStringReadFormat());
		r.cleanUp();
		
		ReqGet reqGet = new ReqGet(MdlModel.CLASS_CLASS_FULL_NAME);
		reqGet.getProperties().add(ReqGet.ALL_PROPERTIES);
		reqGet.setLimit(100);
		reqGet.setOrderBy("name");
		reqGet.getChildIndexes().add(MdlModel.STRING_CLASS_FULL_NAME + ":class");
		reqGet.addFilter("name",ReqGetFilter.EQUALS,"Class");
		r = reqGet.toXML();
		addFile("postGet.xml",r.toStringReadFormat());
		r.cleanUp();

		ReqUpdate reqUpdate = new ReqUpdate(MdlModel.CLASS_CLASS_FULL_NAME,1);
		reqUpdate.getUpdateObject().setPropertyValue("name",new StringBuilder("UpdateClassName"));
		reqUpdate.getUpdateObject().setLinkValue("package",2L);
		r = reqUpdate.toXML();
		addFile("postUpdate.xml",r.toStringReadFormat());
		r.cleanUp();

		ReqRemove reqRemove = new ReqRemove(MdlModel.CLASS_CLASS_FULL_NAME,1);
		r = reqRemove.toXML();
		addFile("postRemove.xml",r.toStringReadFormat());
		r.cleanUp();

		ReqAdd reqAdd = new ReqAdd(MdlModel.CLASS_CLASS_FULL_NAME);
		
		DbDataObject addObject = new DbDataObject();
		addObject.setPropertyValue("name",new StringBuilder("ExampleClass"));
		addObject.setPropertyValue("abstract",new StringBuilder("false"));
		List<Long> linkVal = new ArrayList<Long>();
		linkVal.add(2L);
		addObject.setLinkValue("package",linkVal);
		reqAdd.getObjects().add(new ReqDataObject(addObject));

		addObject = new DbDataObject();
		addObject.setPropertyValue("name",new StringBuilder("ExampleAbstractClass"));
		addObject.setPropertyValue("abstract",new StringBuilder("true"));
		linkVal = new ArrayList<Long>();
		linkVal.add(5L);
		addObject.setLinkValue("package",linkVal);
		reqAdd.getObjects().add(new ReqDataObject(addObject));
		
		r = reqAdd.toXML();
		addFile("postAdd.xml",r.toStringReadFormat());
		r.cleanUp();
		
		// ZODB Java script
		StringBuilder ZODB = new StringBuilder();
		ZODB.append(getCookieJavaScript());
		ZODB.append(getURLJavaScript());
		ZODB.append(getDebugJavaScript());
		ZODB.append(getDateJavaScript());
		ZODB.append(getAjaxJavaScript());
		ZODB.append(getModelJavaScript());
		ZODB.append(getDataJavaScript());
		ZODB.append(getDomJavaScript());
		ZODB.append(getAuthorizationJavaScript());
		ZODB.append(getGuiJavaScript());
		addFile("ZODB.js",ZODB);
		
		// ZODB CSS
		addFile("ZODB.css",getCSS());

		// Management content
		addFile(AUTHORIZATION_MANAGER_HTML,getAuthorizationManagerHtml().toStringBuilder());
		addFile(AUTHORIZER_HTML,getAuthorizerHtml().toStringBuilder());
		addFile("packageManager.html",getPackageManagerHtml().toStringBuilder());
		addFile("classManager.html",getClassManagerHtml().toStringBuilder());
		addFile("uniqueConstraintManager.html",getUniqueConstraintManagerHtml().toStringBuilder());
		for (MdlPackage pkg: DbConfig.getInstance().getModel().getPackages()) {
			for (MdlClass cls: pkg.getClasses()) {
				if (!cls.isAbstr()) {
					addFile(cls.getFullName() + ".html",getManagerHtmlForPackageClass(pkg,cls).toStringBuilder());
				}
			}
		}
		addFile("modelManager.html",getModelManagerHtml().toStringBuilder());
	}
	
	public StringBuilder getMenuHtmlForPage(String page) {
		List<String> pages = new ArrayList<String>();
		List<String> titles = new ArrayList<String>();
		
		pages.add("home");
		pages.add("about");
		pages.add("config");
		pages.add("model");
		pages.add("data");

		titles.add("Home");
		titles.add("About");
		titles.add("Configuration");
		titles.add("Model");
		titles.add("Data");
		
		if (getIndexMenuTitle().length()>0) {
			pages.add("index");
			titles.add(getIndexMenuTitle());
		}
		
		StringBuilder html = new StringBuilder();
		int i = 0;
		for (String p: pages) {
			if (html.length()>0) {
				html.append("&nbsp;");
			}
			if (p.equals(page)) {
				html.append("<b>");
			} else {
				html.append("<a href=\"/" + p + ".html\">");
			}
			html.append(titles.get(i));
			if (p.equals(page)) {
				html.append("</b>");
			} else {
				html.append("</a>");
			}
			i++;
		}
		html.insert(0,"<div>");
		html.append("<br/><hr/></div>");
		
		return html;
	}

	protected String getIndexMenuTitle() {
		return "";
	}
	
	protected BufferedImage generateFavicon() {
		return ZODB.getIconImage().getBufferedImage();
	}
	
	protected HTMLFile getInstalledHtml() {
		HTMLFile file = new HTMLFile("ZODB - Installed");
		file.setBodyBgColor(BACKGROUND_COLOR);
		
		StringBuilder body = new StringBuilder();
		body.append(getMenuHtmlForPage(""));
		body.append("<div>");
		body.append("You have succesfully installed the <a href=\"/about.html\">Zeesoft Object Database</a>. ");
		body.append("You can use the menu at the top of the page to explore the default interface. ");
		body.append("<b>Please note</b> that your current database request authorization password is 'admin' and that this database server and its authorization mechanism are <b>not secure enough to be exposed directly to the internet</b>. ");
		body.append("Feel free to use the <a href=\"/" + AUTHORIZATION_MANAGER_HTML + "\">authorization manager</a> to change the password at any time. ");
		body.append("</div>");
		file.getBodyElements().add(body.toString());
		
		return file;
	}
	
	protected HTMLFile getIndexHtml() {
		return getHomeHtml();
	}

	protected HTMLFile getHomeHtml() {
		HTMLFile file = new HTMLFile("ZODB - Home");
		file.setBodyBgColor(BACKGROUND_COLOR);

		StringBuilder body = new StringBuilder();
		body.append(getMenuHtmlForPage("home"));
		body.append("<div>");
		body.append("Welcome to the <a href=\"/about.html\">Zeesoft Object Database</a> default user interface. ");
		body.append("You can use the following programs to manage the database; ");
		body.append("<ul>");
		body.append("<li><a href=\"/" + AUTHORIZATION_MANAGER_HTML + "\">Authorization manager</a></li>");
		body.append("<li><a href=\"/packageManager.html\">Package manager</a></li>");
		body.append("<li><a href=\"/classManager.html\">Class manager</a></li>");
		body.append("<li><a href=\"/uniqueConstraintManager.html\">Unique constraint manager</a></li>");
		body.append("<li><a href=\"/modelManager.html\">Model manager</a></li>");
		body.append("</ul>");
		body.append("</div>");
		file.getBodyElements().add(body.toString());
		
		return file;
	}

	protected HTMLFile getModelHtml() {
		HTMLFile file = new HTMLFile("ZODB - Model");
		file.setBodyBgColor(BACKGROUND_COLOR);
		
		StringBuilder body = new StringBuilder();
		body.append(getMenuHtmlForPage("model"));
		body.append("<div>");
		body.append("The <a href=\"/about.html\">Zeesoft Object Database</a> uses the following data models;");
		body.append("<ul>");
		body.append("<li>The <a href=\"/abstractModel.xml\">abstract data model</a> is used to generate the real data model</li>");
		body.append("<li>The <a href=\"/realModel.xml\">real data model</a> is used to manage database data</li>");
		body.append("<li>The <a href=\"/databaseModel.xml\">database data model</a> is used to update the abstract data model</li>");
		body.append("</ul>");
		body.append("The difference between the abstract data model and the real data model is that in the real data model, extended classes have been resolved and abstract classes are omitted. ");
		body.append("The database data model is stored within in the database itself. ");
		body.append("The following programs can be used to manage the database data model; ");
		body.append("<ul>");
		body.append("<li><a href=\"/packageManager.html\">Package manager</a></li>");
		body.append("<li><a href=\"/classManager.html\">Class manager</a></li>");
		body.append("<li><a href=\"/uniqueConstraintManager.html\">Unique constraint manager</a></li>");
		body.append("</ul>");
		body.append("You can use the <a href=\"/modelManager.html\">model manager</a> to update the abstract data model or revert database data model changes ");
		body.append("</div>");
		file.getBodyElements().add(body.toString());
		
		return file;
	}

	protected HTMLFile getDataHtml() {
		HTMLFile file = new HTMLFile("ZODB - Data");
		file.setBodyBgColor(BACKGROUND_COLOR);
		
		StringBuilder body = new StringBuilder();
		body.append(getMenuHtmlForPage("data"));
		body.append("<div>");
		body.append("The <a href=\"/about.html\">Zeesoft Object Database</a> generates basic managers for all classes; ");
		body.append("<ul>");
		for (MdlClass cls: DbConfig.getInstance().getModel().getClasses()) {
			if (!cls.isAbstr()) {
				body.append("<li><a href=\"/");
				body.append(cls.getFullName());
				body.append(".html\">");
				body.append(cls.getFullName());
				body.append("</a></li>");
			}
		}
		body.append("</ul>");
		body.append("You can access class data directly using the URL syntax; <a href=\"/data/" + MdlModel.CLASS_CLASS_FULL_NAME + ".xml\">http://{server}:{port}/data/{packageName}.{className}.xml</a>. ");
		body.append("Use the following parameter syntax in the URL to manipulate the GET requests;");
		body.append("<ul>");
		body.append("<li><a href=\"/data/" + MdlModel.CLASS_CLASS_FULL_NAME + ".xml?_start=5\">_start={start}</a> (default = 0)</li>");
		body.append("<li><a href=\"/data/" + MdlModel.CLASS_CLASS_FULL_NAME + ".xml?_limit=5\">_limit={limit}</a> (default = <a href=\"/config.html\">{configuredGetRequestLimit}</a>)</li>");
		body.append("<li><a href=\"/data/" + MdlModel.CLASS_CLASS_FULL_NAME + ".xml?_orderBy=name\">_orderBy={propertyName}</a></li>");
		body.append("<li><a href=\"/data/" + MdlModel.CLASS_CLASS_FULL_NAME + ".xml?_orderBy=name&_orderAscending=false\">_orderAscending=true/false</a> (default = true)</li>");
		body.append("<li><a href=\"/data/" + MdlModel.CLASS_CLASS_FULL_NAME + ".xml?_properties=id,abstract\">_properties={propertyName{,propertyName}}/*</a> (default = *)</li>");
		body.append("<li><a href=\"/data/" + MdlModel.CLASS_CLASS_FULL_NAME + ".xml?_childIndexes=" + MdlModel.STRING_CLASS_FULL_NAME + ":class\">_childIndexes={childIndexName{,childIndexName}}/*</a></li>");
		body.append("<li><a href=\"/data/" + MdlModel.CLASS_CLASS_FULL_NAME + ".xml?name=Property\">{propertyName}={propertyValue}</a></li>");
		body.append("<li><a href=\"/data/" + MdlModel.CLASS_CLASS_FULL_NAME + ".xml?name=Property&_filterStrict=true\">_filterStrict=true/false</a> (default = false)</li>");
		body.append("</ul>");
		body.append("Most POST requests require specific XML body content. ");
		body.append("Check the following examples to learn more;");
		body.append("<ul>");
		body.append("<li><a href=\"/postAuthorize.xml\">Authorize</a></li>");
		body.append("<li><a href=\"/postGet.xml\">Get</a></li>");
		body.append("<li><a href=\"/postUpdate.xml\">Update</a></li>");
		body.append("<li><a href=\"/postRemove.xml\">Remove</a></li>");
		body.append("<li><a href=\"/postAdd.xml\">Add</a></li>");
		body.append("</ul>");
		body.append("</div>");
		file.getBodyElements().add(body.toString());
		
		return file;
	}
	
	protected HTMLFile getAboutHtml() {
		HTMLFile file = new HTMLFile("ZODB - About");
		file.setBodyBgColor(BACKGROUND_COLOR);
		
		StringBuilder body = new StringBuilder();
		body.append(getMenuHtmlForPage("about"));
		body.append("<div>");
		body.append("The <b>Zeesoft Object Database</b> is designed to easily store and quickly retrieve object data. ");
		body.append("It allows users to create an object oriented data model that includes the following features; ");
		body.append("<ul>");
		body.append("<li>Abstract classes and multiple inheritance for class properties</li>");
		body.append("<li>Unique constraints that can check property value combinations over multiple classes</li>");
		body.append("<li>Support for property types; string, number and link</li>");
		body.append("<li>Link properties support many to many relationships</li>");
		body.append("</ul>");
		body.append("The database can handle multiple requests simultaniously by dividing the request workload into smaller units. ");
		body.append("A class and object locking mechanism is used to ensure data integrity while requests are handled by multiple threads. ");
		body.append("Indexes are loaded when the database starts. They are combined in get request handling to ensure optimal performance. ");
		body.append("Objects are loaded when they are needed and released from memory when they are no longer needed to ensure optimal memory usage. ");
		body.append("All object property values are optional except the id property. ");
		body.append("Id properties are defined and managed by the database. ");
		body.append("Link property values are always indexed and validated when adding, updating or removing objects. ");
		body.append("The initial model is hard coded but can be extended programmatically to ensure certain mandatory model structures exist. ");
		body.append("The initial class specific database cache configuration is hard coded and can be extended to allow optimization for specific implementations. ");
		body.append("The default database server is a very basic HTTP server that supports GET and POST requests and can be extended or replaced programmatically. ");
		body.append("<b>Please note</b> that this default database server and its authorization mechanism are <b>not secure enough to be exposed directly to the internet</b>. ");
		body.append("</div>");
		file.getBodyElements().add(body.toString());
		return file;
	}
	
	protected HTMLFile get404Html() {
		HTMLFile file = new HTMLFile("ZODB - 404 Not Found");
		file.setBodyBgColor(BACKGROUND_COLOR);
		
		StringBuilder body = new StringBuilder();
		body.append(getMenuHtmlForPage(""));
		body.append("<div>");
		body.append("No resource found for this URL. ");
		body.append("</div>");
		file.getBodyElements().add(body.toString());
		
		return file;
	}
	
	protected StringBuilder getCookieJavaScript() {
		StringBuilder script = new StringBuilder();

		script.append("var ZODB = ZODB || {};\n");
		script.append("ZODB.cookie = ZODB.cookie || {};\n");

		script.append("ZODB.cookie.getCookieValue = function(name) {\n");
		script.append("    var r = null;\n");
		script.append("    var c = window.document.cookie;\n");
		script.append("    if (c!=null && c!=\"\") {\n");
		script.append("        var cs = c.split(\";\");\n");
		script.append("        for (var i = 0; i < cs.length; i++) {\n");
		script.append("            var co = cs[i];\n");
		script.append("            while (co.charAt(0)==\" \") {\n");
		script.append("                co = co.substring(1);\n");
		script.append("            }\n");
		script.append("            if (co.indexOf(name + \"=\")==0) {\n");
		script.append("                r = co.split(\"=\")[1];\n");
		script.append("                break;\n");
		script.append("            }\n");
		script.append("        }\n");
		script.append("    }\n");
		script.append("    return r;\n");
		script.append("};\n");

		return script;
	}

	protected StringBuilder getURLJavaScript() {
		StringBuilder script = new StringBuilder();

		script.append("var ZODB = ZODB || {};\n");
		script.append("ZODB.url = ZODB.url || {};\n");

		script.append("ZODB.url.getURLParameterValue = function(name) {\n");
		script.append("    var r = null;\n");
		script.append("    var q = window.location.search;\n");
		script.append("    if (q!=null && q!=\"\" && q.length>1) {\n");
		script.append("        var qp = q.substring(1).split(\"&\");\n");
		script.append("        for (var i = 0; i < qp.length; i++) {\n");
		script.append("            var qv = qp[i];\n");
		script.append("            if (qv.indexOf(name + \"=\")==0) {\n");
		script.append("                r = qv.split(\"=\")[1];\n");
		script.append("                break;\n");
		script.append("            }\n");
		script.append("        }\n");
		script.append("    }\n");
		script.append("    if (r!=null) {\n");
		script.append("        r = decodeURIComponent(r)\n");
		script.append("    }\n");
		script.append("    return r;\n");
		script.append("};\n");

		return script;
	}
	
	protected StringBuilder getDebugJavaScript() {
		StringBuilder script = new StringBuilder();

		script.append("var ZODB = ZODB || {};\n");
		script.append("ZODB.debug = ZODB.debug || {};\n");
		script.append("ZODB.debug.console = ZODB.debug.console || (ZODB.cookie.getCookieValue(\"debug\")==\"true\") || false;\n");

		script.append("ZODB.debug.log = function(str) {\n");
		script.append("    if (ZODB.debug.console && window.console && window.console.log) {\n");
		script.append("        console.log(str);\n");
		script.append("    }\n");
		script.append("};\n");

		script.append("ZODB.debug.err = function(str) {\n");
		script.append("    ZODB.debug.log(\"ERR:\" + str);\n");
		script.append("};\n");

		return script;
	}

	protected StringBuilder getDateJavaScript() {
		StringBuilder script = new StringBuilder();

		script.append("var ZODB = ZODB || {};\n");
		script.append("ZODB.date = ZODB.date || {};\n");

		script.append("ZODB.date.minStrInt = function(str,len) {\n");
		script.append("    str = \"\" + str;\n");
		script.append("    if (str.length<len) {\n");
		script.append("        for (var i = 0; i < (len - str.length); i++) {\n");
		script.append("            str = \"0\" + str;\n");
		script.append("        }\n");
		script.append("    }\n");
		script.append("    return str;\n");
		script.append("};\n");

		script.append("ZODB.date.toString = function(date) {\n");
		script.append("    var str = \"\";\n");
		script.append("    if (date) {\n");
		script.append("        str += date.getFullYear() + \"-\";\n");
		script.append("        str += ZODB.date.minStrInt((date.getMonth() + 1),2) + \"-\";\n");
		script.append("        str += ZODB.date.minStrInt(date.getDate(),2) + \" \";\n");
		script.append("        str += ZODB.date.minStrInt(date.getHours(),2) + \":\";\n");
		script.append("        str += ZODB.date.minStrInt(date.getMinutes(),2) + \":\";\n");
		script.append("        str += ZODB.date.minStrInt(date.getSeconds(),2);\n");
		script.append("    }\n");
		script.append("    return str;\n");
		script.append("};\n");

		return script;
	}

	protected StringBuilder getAjaxJavaScript() {
		StringBuilder script = new StringBuilder();

		script.append("var ZODB = ZODB || {};\n");
		script.append("ZODB.ajax = {};\n");
		script.append("ZODB.ajax.communicationErrorHandled = false;\n");

		script.append("ZODB.ajax.executeXMLHttpRequest = function(method,url,body,successCallback,errorCallback) {\n");
		script.append("    var xhttp = new XMLHttpRequest();\n");
		script.append("    xhttp.onreadystatechange = function() {\n");
		script.append("        if (this.readyState == 4) {\n");
		script.append("            if (this.status == 200) {\n");
		script.append("                if (xhttp.responseXML==null) {\n");
		script.append("                    ZODB.debug.err(\"ZODB.ajax.executeXMLHttpRequest; responseXML: null, text: \" + xhttp.responseText);\n");
		script.append("                }\n");
		script.append("                successCallback(this);\n");
		script.append("            } else if (errorCallback!=null) {\n");
		script.append("                errorCallback(this);\n");
		script.append("            } else {\n");
		script.append("                ZODB.ajax.defaultErrorCallback(this);\n");
		script.append("            }\n");
		script.append("        }\n");
		script.append("    };\n");
		script.append("    xhttp.open(method,url,true);\n");
		script.append("    if (body) {\n");
		script.append("        xhttp.setRequestHeader(\"Content-type\",\"text/xml\");\n");
		script.append("        xhttp.send(body);\n");
		script.append("    } else {\n");
		script.append("        xhttp.send();\n");
		script.append("    }\n");
		script.append("};\n");

		script.append("ZODB.ajax.defaultErrorCallback = function(xhttp) {\n");
		script.append("    ZODB.debug.err(\"ZODB.ajax.defaultErrorCallback; readyState: \" + xhttp.readyState + \", status: \" + xhttp.status);\n");
		script.append("    ZODB.debug.err(\"ZODB.ajax.defaultErrorCallback; responseText: \" + xhttp.responseText);\n");
		script.append("    if (xhttp.readyState===4 && xhttp.status===0) {\n");
		script.append("        ZODB.ajax.communicationErrorHandler(xhttp);\n");
		script.append("    }\n");
		script.append("};\n");
		
		script.append("ZODB.ajax.communicationErrorHandler = function(xhttp) {\n");
		script.append("    if (!ZODB.ajax.communicationErrorHandled) {\n");
		script.append("        ZODB.ajax.communicationErrorHandled = true;\n");
		script.append("        alert(\"An error occured while communicating with the server\");\n");
		script.append("        window.location.reload();\n");
		script.append("    }\n");
		script.append("};\n");

		return script;
	}

	protected StringBuilder getModelJavaScript() {
		StringBuilder script = new StringBuilder();

		script.append("var ZODB = ZODB || {};\n");
		script.append("ZODB.model = {};\n");

		script.append("ZODB.model.packages = {};\n");
		script.append("ZODB.model.classes = {};\n");
		
		script.append("ZODB.model.getModel = function(successCallback,errorCallback) {\n");
		script.append("    ZODB.ajax.executeXMLHttpRequest(\"GET\",\"/realModel.xml\",null,successCallback,errorCallback);\n");
		script.append("};\n");
		
		script.append("ZODB.model.getModelCallback = function(xhttp) {\n");
		script.append("    var xmlDoc = xhttp.responseXML;\n");
		script.append("    var pElems = xmlDoc.getElementsByTagName(\"package\");\n");
		script.append("    for (var p = 0; p < pElems.length; p++) {\n");
		script.append("        var packageName = pElems[p].childNodes[0].childNodes[0].nodeValue;\n");
		script.append("        ZODB.model.packages[packageName] = {};\n");
		script.append("        for (var pp = 1; pp < pElems[p].childNodes.length; pp++) {\n");
		script.append("            var pvElem = pElems[p].childNodes[pp];\n");
		script.append("            if (pvElem.tagName===\"classes\") {\n");
		script.append("                ZODB.model.parsePackageClassesFromXml(packageName,pvElem);\n");
		script.append("            }\n");
		script.append("        }\n");
		script.append("    }\n");
		script.append("};\n");

		script.append("ZODB.model.parsePackageClassesFromXml = function(packageName,classesElem) {\n");
		script.append("    ZODB.model.packages[packageName].classes = {};\n");
		script.append("    for (var c = 0; c < classesElem.childNodes.length; c++) {\n");
		script.append("        var cElem = classesElem.childNodes[c];\n");
		script.append("        var className = cElem.childNodes[0].childNodes[0].nodeValue;\n");
		script.append("        ZODB.model.packages[packageName].classes[className] = {};\n");
		script.append("        for (var cc = 1; cc < cElem.childNodes.length; cc++) {\n");
		script.append("            var cvElem = cElem.childNodes[cc];\n");
		script.append("            if (cvElem.tagName===\"properties\") {\n");
		script.append("                ZODB.model.parsePackageClassPropertiesFromXml(packageName,className,cvElem);\n");
		script.append("            }\n");
		script.append("        }\n");
		script.append("        ZODB.model.classes[(packageName + \".\" + className)] = ZODB.model.packages[packageName].classes[className];\n");
		script.append("    }\n");
		script.append("};\n");

		script.append("ZODB.model.parsePackageClassPropertiesFromXml = function(packageName,className,propertiesElem) {\n");
		script.append("    var cls = ZODB.model.packages[packageName].classes[className];\n");
		script.append("    cls.properties = {};\n");
		script.append("    cls.selectProperty = null;\n");
		script.append("    for (var p = 0; p < propertiesElem.childNodes.length; p++) {\n");
		script.append("        var pElem = propertiesElem.childNodes[p];\n");
		script.append("        var propName = pElem.childNodes[0].childNodes[0].nodeValue;\n");
		script.append("        var propType = pElem.tagName;\n");
		script.append("        cls.properties[propName] = {};\n");
		script.append("        cls.properties[propName].type = propType;\n");
		script.append("        for (var pp = 1; pp < pElem.childNodes.length; pp++) {\n");
		script.append("            var pvElem = pElem.childNodes[pp];\n");
		script.append("            cls.properties[propName][pvElem.tagName] = pvElem.childNodes[0].nodeValue;\n");
		script.append("        }\n");
		script.append("    }\n");
		script.append("    if (cls.selectProperty==null) {\n");
		script.append("        for (var propName in cls.properties) {\n");
		script.append("            if (cls.properties[propName].type==\"string\" && propName==\"name\") {\n");
		script.append("                cls.selectProperty = propName;\n");
		script.append("                break;\n");
		script.append("            }\n");
		script.append("        }\n");
		script.append("    }\n");
		script.append("    if (cls.selectProperty==null) {\n");
		script.append("        for (var propName in cls.properties) {\n");
		script.append("            if (cls.properties[propName].type==\"string\" && cls.properties[propName].maxLength<=" + MAX_INPUT_LENGTH + ") {\n");
		script.append("                cls.selectProperty = propName;\n");
		script.append("                break;\n");
		script.append("            }\n");
		script.append("        }\n");
		script.append("    }\n");
		script.append("    if (cls.selectProperty==null) {\n");
		script.append("        for (var propName in cls.properties) {\n");
		script.append("            if (cls.properties[propName].type==\"number\" && propName!=\"id\") {\n");
		script.append("                cls.selectProperty = propName;\n");
		script.append("                break;\n");
		script.append("            }\n");
		script.append("        }\n");
		script.append("    }\n");
		script.append("};\n");
			
		return script;
	}

	protected StringBuilder getDataJavaScript() {
		StringBuilder script = new StringBuilder();

		script.append("var ZODB = ZODB || {};\n");
		script.append("ZODB.data = {};\n");

		script.append("ZODB.data.decodeAscii = function(str) {\n");
		script.append("    var r = \"\";\n");
		script.append("    var keyVals = str.split(\",\");\n");
		script.append("    if (keyVals.length>=2) {\n");
		script.append("        var key = parseInt(keyVals[0],10);\n");
		script.append("        var pKey = key;\n");
		script.append("        for (var i = 1; i<keyVals.length; i++) {\n");
		script.append("            var iKey = key + ((i * pKey * 7) % 24);\n");
		script.append("            pKey = iKey;\n");
		script.append("            var iVal = iKey;\n");
		script.append("            var v = parseInt(keyVals[i],10);\n");
		script.append("            if (v>0) {\n");
		script.append("                iVal = iVal + v;\n");							
		script.append("            } else if (v<0) {\n");
		script.append("                iVal = iVal - (v * -1);\n");							
		script.append("            }\n");
		script.append("            r += String.fromCharCode(iVal);\n");
		script.append("        }\n");
		script.append("    }\n");
		script.append("    return r;\n");
		script.append("};\n");
		
		script.append("ZODB.data.DbDataObject = function() {\n");
		script.append("    this.propertyValues = {};\n");
		script.append("    this.linkValues = {};\n");
		script.append("    this.getId = function() {\n");
		script.append("        var id = 0;\n");
		script.append("        if (this.propertyValues[\"id\"]!==null) {\n");
		script.append("            id = this.propertyValues[\"id\"];\n");
		script.append("        }\n");
		script.append("        return id;\n");
		script.append("    };\n");
		script.append("    this.setId = function(id) {\n");
		script.append("        this.propertyValues[\"id\"]=id;\n");
		script.append("    };\n");
		script.append("    this.toXML = function() {\n");
		script.append("        var xml = \"\";\n");
		script.append("        xml += \"<requestObject>\";\n");
		script.append("        xml += \"<object>\";\n");
		script.append("        xml += \"<properties>\";\n");
		script.append("        for (var name in this.propertyValues) {\n");
		script.append("            xml += \"<\" + name + \">\";\n");
		script.append("            if (name!=\"id\") {\n");
		script.append("                xml += \"<![CDATA[\";\n");
		script.append("            }\n");
		script.append("            var value = this.propertyValues[name];\n");
		script.append("            if (value!=undefined) {\n");
		script.append("                value = value.toString();\n");
		script.append("                value = value.replace(/</g,\"&lt;\");\n");
		script.append("                value = value.replace(/>/g,\"&gt;\");\n");
		script.append("                xml += value;\n");
		script.append("            }\n");
		script.append("            if (name!=\"id\") {\n");
		script.append("                xml += \"]]>\";\n");
		script.append("            }\n");
		script.append("            xml += \"</\" + name + \">\";\n");
		script.append("        };\n");
		script.append("        xml += \"</properties>\";\n");
		script.append("        xml += \"<links>\";\n");
		script.append("        for (var name in this.linkValues) {\n");
		script.append("            xml += \"<\" + name + \">\";\n");
		script.append("            if (this.linkValues[name]!=null) {\n");
		script.append("                for (var i=0; i< this.linkValues[name].length; i++) {\n");
		script.append("                    xml += \"<id>\" + this.linkValues[name][i] + \"</id>\";\n");
		script.append("                }\n");
		script.append("            }\n");
		script.append("            xml += \"</\" + name + \">\";\n");
		script.append("        };\n");
		script.append("        xml += \"</links>\";\n");
		script.append("        xml += \"</object>\";\n");
		script.append("        xml += \"</requestObject>\";\n");
		script.append("        return xml;\n");
		script.append("    };\n");
		script.append("};\n");
		
		script.append("ZODB.data.DbDataObjectSet = function(className) {\n");
		script.append("    this.className = className;\n");
		script.append("    this.count = 0;\n");
		script.append("    this.log = 0;\n");
		script.append("    this.objects = {};\n");
		script.append("    this.objectArray = [];\n");
		script.append("    this.propertyNameArray = [];\n");
		script.append("    this.countObjects = 0;\n");
		script.append("    this.toXML = function() {\n");
		script.append("        var xml = \"\";\n");
		script.append("        xml += \"<className>\";\n");
		script.append("        xml += this.className;\n");
		script.append("        xml += \"</className>\";\n");
		script.append("        xml += \"<objects>\";\n");
		script.append("        for (var id in this.objects) {\n");
		script.append("            xml += this.objects[id].toXML();\n");
		script.append("        };\n");
		script.append("        xml += \"</objects>\";\n");
		script.append("        return xml;\n");
		script.append("    };\n");
		script.append("};\n");

		script.append("ZODB.data.Error = function() {\n");
		script.append("    this.code = \"\";\n");
		script.append("    this.message = \"\";\n");
		script.append("    this.properties = {};\n");
		script.append("    this.toString = function() {\n");
		script.append("        return \"ERROR: \" + this.code + \": \" + this.message;\n");
		script.append("    };\n");
		script.append("};\n");
		
		script.append("ZODB.data.GetRequest = function(className) {\n");
		script.append("    this.dataObjectSet = new ZODB.data.DbDataObjectSet(className);\n");
		script.append("    this.properties = [];\n");
		script.append("    this.orderBy = \"\";\n");
		script.append("    this.orderAscending = true;\n");
		script.append("    this.start = 0;\n");
		script.append("    this.limit = 0;\n");
		script.append("    this.childIndexes = [];\n");
		script.append("    this.filters = {};\n");
		script.append("    this.filterOperators = {};\n");
		script.append("    this.getClassName = function() {\n");
		script.append("        return this.dataObjectSet.className;\n");
		script.append("    };\n");
		script.append("    this.toXML = function() {\n");
		script.append("        var xml = \"\";\n");
		script.append("        xml += \"<get>\";\n");
		script.append("        xml += \"<className>\";\n");
		script.append("        xml += this.getClassName();\n");
		script.append("        xml += \"</className>\";\n");
		script.append("        if (this.properties && this.properties.length>0) {\n");
		script.append("            xml += \"<properties>\";\n");
		script.append("            for (var i = 0; i < this.properties.length; i++) {\n");
		script.append("                xml += \"<name>\";\n");
		script.append("                xml += this.properties[i];\n");
		script.append("                xml += \"</name>\";\n");
		script.append("            }\n");
		script.append("            xml += \"</properties>\";\n");
		script.append("        }\n");
		script.append("        if (this.orderBy.length>0) {\n");
		script.append("            xml += \"<orderBy>\";\n");
		script.append("            xml += this.orderBy;\n");
		script.append("            xml += \"</orderBy>\";\n");
		script.append("            xml += \"<orderAscending>\";\n");
		script.append("            xml += this.orderAscending;\n");
		script.append("            xml += \"</orderAscending>\";\n");
		script.append("        }\n");
		script.append("        if (this.start>0 || this.limit>0) {\n");
		script.append("            xml += \"<start>\";\n");
		script.append("            xml += this.start;\n");
		script.append("            xml += \"</start>\";\n");
		script.append("            xml += \"<limit>\";\n");
		script.append("            xml += this.limit;\n");
		script.append("            xml += \"</limit>\";\n");
		script.append("        }\n");
		script.append("        xml += \"<filters>\";\n");
		script.append("        for (var name in this.filters) {\n");
		script.append("            var operator = \"equals\";\n");
		script.append("            if (this.filterOperators[name]) {\n");
		script.append("                operator = this.filterOperators[name];\n");
		script.append("            }\n");
		script.append("            if (ZODB.model.classes[this.getClassName()]!=null &&\n");
		script.append("                ZODB.model.classes[this.getClassName()].properties[name]!=null &&\n");
		script.append("                ZODB.model.classes[this.getClassName()].properties[name].type==\"link\"\n");
		script.append("                ) {\n");
		script.append("                operator = \"contains\";\n");
		script.append("            }\n");
		script.append("            xml += \"<filter>\";\n");
		script.append("            xml += \"<property>\";\n");
		script.append("            xml += name;\n");
		script.append("            xml += \"</property>\";\n");
		script.append("            xml += \"<operator>\";\n");
		script.append("            xml += operator;\n");
		script.append("            xml += \"</operator>\";\n");
		script.append("            xml += \"<value><![CDATA[\";\n");
		script.append("            xml += this.filters[name];\n");
		script.append("            xml += \"]]></value>\";\n");
		script.append("            xml += \"</filter>\";\n");
		script.append("        }\n");
		script.append("        xml += \"</filters>\";\n");
		script.append("        xml += \"</get>\";\n");
		script.append("        return xml;\n");
		script.append("    };\n");
		script.append("};\n");

		script.append("ZODB.data.AddRequest = function(className) {\n");
		script.append("    this.dataObjectSet = new ZODB.data.DbDataObjectSet(className);\n");
		script.append("    this.getClassName = function() {\n");
		script.append("        return this.dataObjectSet.className;\n");
		script.append("    };\n");
		script.append("    this.toXML = function() {\n");
		script.append("        var xml = \"\";\n");
		script.append("        xml += \"<add>\";\n");
		script.append("        xml += this.dataObjectSet.toXML();\n");
		script.append("        xml += \"</add>\";\n");
		script.append("        return xml;\n");
		script.append("    };\n");
		script.append("};\n");

		script.append("ZODB.data.UpdateRequest = function(className) {\n");
		script.append("    this.dataObjectSet = new ZODB.data.DbDataObjectSet(className);\n");
		script.append("    this.getRequest = new ZODB.data.GetRequest(className);\n");
		script.append("    this.getClassName = function() {\n");
		script.append("        return this.dataObjectSet.className;\n");
		script.append("    };\n");
		script.append("    this.toXML = function() {\n");
		script.append("        var xml = \"\";\n");
		script.append("        xml += \"<update>\";\n");
		script.append("        xml += this.getRequest.toXML();\n");
		script.append("        xml += this.dataObjectSet.toXML();\n");
		script.append("        xml += \"</update>\";\n");
		script.append("        return xml;\n");
		script.append("    };\n");
		script.append("};\n");

		script.append("ZODB.data.RemoveRequest = function(className) {\n");
		script.append("    this.dataObjectSet = new ZODB.data.DbDataObjectSet(className);\n");
		script.append("    this.getRequest = new ZODB.data.GetRequest(className);\n");
		script.append("    this.getClassName = function() {\n");
		script.append("        return this.dataObjectSet.className;\n");
		script.append("    };\n");
		script.append("    this.toXML = function() {\n");
		script.append("        var xml = \"\";\n");
		script.append("        xml += \"<remove>\";\n");
		script.append("        xml += this.getRequest.toXML();\n");
		script.append("        xml += this.dataObjectSet.toXML();\n");
		script.append("        xml += \"</remove>\";\n");
		script.append("        return xml;\n");
		script.append("    };\n");
		script.append("};\n");
		
		script.append("ZODB.data.getDataForClass = function(className,successCallback,errorCallback) {\n");
		script.append("    ZODB.ajax.executeXMLHttpRequest(\"GET\",\"/data/\" + className + \".xml\",null,successCallback,errorCallback);\n");
		script.append("};\n");
		
		script.append("ZODB.data.getDataForClassCallback = function(xhttp) {\n");
		script.append("    return ZODB.data.parseDbDataObjectSetFromXml(xhttp.responseXML);\n");
		script.append("};\n");

		script.append("ZODB.data.parseDbDataObjectSetFromXml = function(xmlDoc) {\n");
		script.append("    var className = xmlDoc.getElementsByTagName(\"className\")[0].childNodes[0].nodeValue;\n");
		script.append("    var logElem = xmlDoc.getElementsByTagName(\"log\");\n");
		script.append("    var log = \"\";\n");
		script.append("    if (logElem!=null && logElem.length>0) {\n");
		script.append("        log = logElem[0].childNodes[0].nodeValue;\n");
		script.append("    }\n");
		script.append("    var countElem = xmlDoc.getElementsByTagName(\"count\");\n");
		script.append("    var count = 0;\n");
		script.append("    if (countElem!=null && countElem.length>0) {\n");
		script.append("        count = countElem[(countElem.length - 1)].childNodes[0].nodeValue;\n");
		script.append("    }\n");
		script.append("    var dataObjectSet = new ZODB.data.DbDataObjectSet(className);\n");
		script.append("    dataObjectSet.count = count;\n");
		script.append("    dataObjectSet.log = log;\n");
		script.append("    var objectElems = xmlDoc.getElementsByTagName(\"object\");\n");
		script.append("    ZODB.data.parseDbDataSetFromXml(dataObjectSet,objectElems);\n");
		script.append("    return dataObjectSet;\n");
		script.append("};\n");

		script.append("ZODB.data.parseDbDataSetFromXml = function(dataObjectSet,objectElems) {\n");
		script.append("    var first = true;\n");
		script.append("    for (var i = 0; i < objectElems.length; i++) {\n");
		script.append("        var objElem = objectElems[i];\n");
		script.append("        var dataObject = new ZODB.data.DbDataObject();\n");
		script.append("        for (var c = 0; c < objElem.childNodes.length; c++) {\n");
		script.append("            var olElem = objElem.childNodes[c];\n");
		script.append("            if (olElem.tagName===\"properties\") {\n");
		script.append("                var pNum=0;\n");
		script.append("                for (var p = 0; p < olElem.childNodes.length; p++) {\n");
		script.append("                    var pElem = olElem.childNodes[p];\n");
		script.append("                    if (pElem.tagName && pElem.tagName.length>0) {\n");
		script.append("                        if (pElem.childNodes.length>0) {\n");
		script.append("                            dataObject.propertyValues[pElem.tagName] = pElem.childNodes[0].nodeValue;\n");
		script.append("                        } else {\n");
		script.append("                            dataObject.propertyValues[pElem.tagName] = \"\";\n");
		script.append("                        }\n");
		script.append("                        if (first) {\n");
		script.append("                            dataObjectSet.propertyNameArray[pNum] = pElem.tagName;\n");
		script.append("                            pNum++;\n");
		script.append("                        }\n");
		script.append("                    }\n");
		script.append("                }\n");
		script.append("            }\n");
		script.append("            if (olElem.tagName===\"links\") {\n");
		script.append("                for (var l = 0; l < olElem.childNodes.length; l++) {\n");
		script.append("                    var lElem = olElem.childNodes[l];\n");
		script.append("                    dataObject.linkValues[lElem.tagName] = [];\n");
		script.append("                    if (lElem.tagName && lElem.tagName.length>0) {\n");
		script.append("                        for (var li = 0; li < lElem.childNodes.length; li++) {\n");
		script.append("                            var liElem = lElem.childNodes[li];\n");
		script.append("                            dataObject.linkValues[lElem.tagName][li] = liElem.childNodes[0].nodeValue;\n");
		script.append("                        }\n");
		script.append("                    }\n");
		script.append("                }\n");
		script.append("            }\n");
		script.append("            first = false;\n");
		script.append("        }\n");
		script.append("        dataObjectSet.objects[dataObject.getId()] = dataObject;\n");
		script.append("        dataObjectSet.objectArray[i] = dataObject;\n");
		script.append("        dataObjectSet.countObjects++;\n");
		script.append("    }\n");
		script.append("    var newNameArray = [dataObjectSet.propertyNameArray.length];\n");
		script.append("    var pNum = 0;\n");
		script.append("    for (var i = 0; i < dataObjectSet.propertyNameArray.length; i++) {\n");
		script.append("        if (dataObjectSet.propertyNameArray[i]===\"id\" || dataObjectSet.propertyNameArray[i]===\"name\") {\n");
		script.append("            newNameArray[pNum] = dataObjectSet.propertyNameArray[i];\n");
		script.append("            pNum++;\n");
		script.append("        }\n");
		script.append("    }\n");
		script.append("    for (var i = 0; i < dataObjectSet.propertyNameArray.length; i++) {\n");
		script.append("        if (dataObjectSet.propertyNameArray[i]!==\"id\" && dataObjectSet.propertyNameArray[i]!==\"name\") {\n");
		script.append("            newNameArray[pNum] = dataObjectSet.propertyNameArray[i];\n");
		script.append("            pNum++;\n");
		script.append("        }\n");
		script.append("    }\n");
		script.append("    dataObjectSet.propertyNameArray = newNameArray;\n");
		script.append("};\n");

		script.append("ZODB.data.getErrorsCallback = function(xhttp) {\n");
		script.append("    var xmlDoc = xhttp.responseXML;\n");
		script.append("    var errorElems = xmlDoc.getElementsByTagName(\"error\");\n");
		script.append("    var errors = ZODB.data.parseErrorsFromXml(errorElems);\n");
		script.append("    return errors;\n");
		script.append("};\n");

		script.append("ZODB.data.parseErrorsFromXml = function(errorElems) {\n");
		script.append("    var errors = [];\n");
		script.append("    for (var i = 0; i < errorElems.length; i++) {\n");
		script.append("        var errElem = errorElems[i];\n");
		script.append("        var error = new ZODB.data.Error();\n");
		script.append("        for (var c = 0; c < errElem.childNodes.length; c++) {\n");
		script.append("            var evElem = errElem.childNodes[c];\n");
		script.append("            if (evElem.tagName===\"code\") {\n");
		script.append("                error.code = evElem.childNodes[0].nodeValue;\n");
		script.append("            }\n");
		script.append("            if (evElem.tagName===\"message\") {\n");
		script.append("                error.message = evElem.childNodes[0].nodeValue;\n");
		script.append("            }\n");
		script.append("            if (evElem.tagName===\"properties\") {\n");
		script.append("                for (var p = 0; p < evElem.childNodes.length; p++) {\n");
		script.append("                    var pElem = evElem.childNodes[p];\n");
		script.append("                    error.properties[p] = pElem.childNodes[0].nodeValue;\n");
		script.append("                }\n");
		script.append("            }\n");
		script.append("        }\n");
		script.append("        errors[i] = error;\n");
		script.append("    }\n");
		script.append("    return errors;\n");
		script.append("};\n");

		script.append("ZODB.data.getImpactedIdsCallback = function(xhttp) {\n");
		script.append("    var xmlDoc = xhttp.responseXML;\n");
		script.append("    var impactedIdElem = xmlDoc.getElementsByTagName(\"impactedIds\");\n");
		script.append("    var ids = ZODB.data.parseImpactedIdsFromXml(impactedIdElem);\n");
		script.append("    return ids;\n");
		script.append("};\n");

		script.append("ZODB.data.parseImpactedIdsFromXml = function(impactedIdElem) {\n");
		script.append("    var ids = [];\n");
		script.append("    if (impactedIdElem && impactedIdElem.length==1) {\n");
		script.append("        for (var i = 0; i < impactedIdElem[0].childNodes.length; i++) {\n");
		script.append("            var idElem = impactedIdElem[0].childNodes[i];\n");
		script.append("            var id = 0;\n");
		script.append("            if (idElem.tagName===\"id\") {\n");
		script.append("                id = idElem.childNodes[0].nodeValue;\n");
		script.append("            }\n");
		script.append("            ids[i] = id;\n");
		script.append("        }\n");
		script.append("    }\n");
		script.append("    return ids;\n");
		script.append("};\n");
		
		script.append("ZODB.data.executeGetRequest = function(getRequest,successCallback,errorCallback) {\n");
		script.append("    var url = \"/data/\" + getRequest.getClassName() + \".xml?\";\n");
		script.append("    url += \"_start=\" + getRequest.start\n");
		script.append("    url += \"&_limit=\" + getRequest.limit\n");
		script.append("    if (getRequest.orderBy!==\"\") {\n");
		script.append("        url += \"&_orderBy=\" + getRequest.orderBy\n");
		script.append("        url += \"&_orderAscending=\" + getRequest.orderAscending\n");
		script.append("    }\n");
		script.append("    if (getRequest.properties.length>0) {\n");
		script.append("        var props=\"\";\n");
		script.append("        for (var i = 0; i <getRequest.properties.length; i++) {\n");
		script.append("            if (props!==\"\") {\n");
		script.append("                props += \",\";\n");
		script.append("            }\n");
		script.append("            props += getRequest.properties[i];\n");
		script.append("        }\n");
		script.append("        url += \"&_properties=\" + props;\n");
		script.append("    }\n");
		script.append("    if (getRequest.childIndexes.length>0) {\n");
		script.append("        var cIndexes=\"\";\n");
		script.append("        for (var i = 0; i <getRequest.childIndexes.length; i++) {\n");
		script.append("            if (cIndexes!==\"\") {\n");
		script.append("                cIndexes += \",\";\n");
		script.append("            }\n");
		script.append("            cIndexes += getRequest.childIndexes[i];\n");
		script.append("        }\n");
		script.append("        url += \"&_childIndexes=\" + cIndexes;\n");
		script.append("    }\n");
		script.append("    for (var name in getRequest.filters) {\n");
		script.append("        url += \"&\" + name + \"=\" + getRequest.filters[name];\n");
		script.append("    }\n");
		script.append("    ZODB.ajax.executeXMLHttpRequest(\"GET\",url,null,successCallback,errorCallback);\n");
		script.append("};\n");

		script.append("ZODB.data.executePostRequest = function(request,successCallback,errorCallback) {\n");
		script.append("    var url = \"/data/\" + request.getClassName() + \".xml\";\n");
		script.append("    ZODB.ajax.executeXMLHttpRequest(\"POST\",url,request.toXML(),successCallback,errorCallback);\n");
		script.append("};\n");
		
		return script;
	}

	protected StringBuilder getDomJavaScript() {
		StringBuilder script = new StringBuilder();

		script.append("var ZODB = ZODB || {};\n");
		script.append("ZODB.dom = {};\n");
		script.append("ZODB.dom.enterFunctions = {};\n");
		
		script.append("ZODB.dom.setElementVisibility = function(element,visibility) {\n");
		script.append("    element.style.visibility=visibility;\n");
		script.append("    if (visibility==\"hidden\") {\n");
		script.append("        element.style.display=\"none\";\n");
		script.append("    } else if (visibility==\"inherit\") {\n");
		script.append("        element.style.display=\"inherit\";\n");
		script.append("    }\n");
		script.append("};\n");

		script.append("ZODB.dom.setElementsVisibility = function(elements,visibility) {\n");
		script.append("    for (var i = 0; i < elements.length; i++) {\n");
		script.append("        ZODB.dom.setElementVisibility(elements[i],visibility);\n");
		script.append("    }\n");
		script.append("};\n");

		script.append("ZODB.dom.hideElementById = function(id) {\n");
		script.append("    var elem = window.document.getElementById(id);\n");
		script.append("    if (elem!=null) {\n");
		script.append("        ZODB.dom.setElementVisibility(elem,\"hidden\");\n");
		script.append("    }\n");
		script.append("};\n");

		script.append("ZODB.dom.showElementById = function(id) {\n");
		script.append("    var elem = window.document.getElementById(id);\n");
		script.append("    if (elem!=null) {\n");
		script.append("        ZODB.dom.setElementVisibility(elem,\"inherit\");\n");
		script.append("    }\n");
		script.append("};\n");

		script.append("ZODB.dom.getInputValueByElementId = function(id) {\n");
		script.append("    var value=null;\n");
		script.append("    var elem = window.document.getElementById(id);\n");
		script.append("    if (elem!=null) {\n");
		script.append("        value = elem.value;\n");
		script.append("    }\n");
		script.append("    return value;\n");
		script.append("};\n");

		script.append("ZODB.dom.getInputValueByElementName = function(name) {\n");
		script.append("    var value=null;\n");
		script.append("    var elems = window.document.getElementsByName(name);\n");
		script.append("    if (elems!=null && elems.length>0) {\n");
		script.append("        value = elems[0].value;\n");
		script.append("    }\n");
		script.append("    return value;\n");
		script.append("};\n");

		script.append("ZODB.dom.getInputCheckedByElementId = function(id) {\n");
		script.append("    var value=null;\n");
		script.append("    var elem = window.document.getElementById(id);\n");
		script.append("    if (elem!=null) {\n");
		script.append("        value = elem.checked;\n");
		script.append("    }\n");
		script.append("    return value;\n");
		script.append("};\n");
		
		script.append("ZODB.dom.getNumberInputValueByElementId = function(id) {\n");
		script.append("    var value = ZODB.dom.getInputValueByElementId(id);\n");
		script.append("    if (value==null || value==\"\" || isNaN(value)) {\n");
		script.append("        value = 0;\n");
		script.append("    } else {\n");
		script.append("        value = parseInt(\"\" + value,10);\n");
		script.append("    }\n");
		script.append("    return value;\n");
		script.append("};\n");

		script.append("ZODB.dom.getSelectValueByElementId = function(id) {\n");
		script.append("    var value=null;\n");
		script.append("    var elem = window.document.getElementById(id);\n");
		script.append("    if (elem!=null) {\n");
		script.append("        value = elem.options[elem.selectedIndex].value;\n");
		script.append("    }\n");
		script.append("    return value;\n");
		script.append("};\n");
		
		script.append("ZODB.dom.setInputValueByElementId = function(id,value) {\n");
		script.append("    var elem = window.document.getElementById(id);\n");
		script.append("    if (elem!=null) {\n");
		script.append("        elem.value = value;\n");
		script.append("    }\n");
		script.append("};\n");

		script.append("ZODB.dom.setInputValueByElementName = function(name,value) {\n");
		script.append("    var elems = window.document.getElementsByName(name);\n");
		script.append("    if (elems!=null && elems.length>0) {\n");
		script.append("        for (var i = 0; i < elems.length; i++) {\n");
		script.append("            elems[i].value = value;\n");
		script.append("        }\n");
		script.append("    }\n");
		script.append("};\n");

		script.append("ZODB.dom.selectRadioByElementName = function(id,value) {\n");
		script.append("    var elems = window.document.getElementsByName(id);\n");
		script.append("    if (elems!=null && elems.length>0) {\n");
		script.append("        for (var i = 0; i < elems.length; i++) {\n");
		script.append("            if (elems[i].value==value) {\n");
		script.append("                elems[i].checked = true;\n");
		script.append("            } else {\n");
		script.append("                elems[i].checked = false;\n");
		script.append("            }\n");
		script.append("        }\n");
		script.append("    }\n");
		script.append("};\n");
		
		script.append("ZODB.dom.unselectRadioByElementName = function(id) {\n");
		script.append("    var elems = window.document.getElementsByName(id);\n");
		script.append("    if (elems!=null && elems.length>0) {\n");
		script.append("        for (var i = 0; i < elems.length; i++) {\n");
		script.append("            elems[i].checked = false;\n");
		script.append("        }\n");
		script.append("    }\n");
		script.append("};\n");

		script.append("ZODB.dom.getSelectedRadioByElementName = function(id) {\n");
		script.append("    var value = null;\n");
		script.append("    var elems = window.document.getElementsByName(id);\n");
		script.append("    if (elems!=null && elems.length>0) {\n");
		script.append("        for (var i = 0; i < elems.length; i++) {\n");
		script.append("            if (elems[i].checked) {\n");
		script.append("                value = elems[i].value;\n");
		script.append("            }\n");
		script.append("        }\n");
		script.append("    }\n");
		script.append("    return value;\n");
		script.append("};\n");
		
		script.append("ZODB.dom.setInputDisabledByElementId = function(id,disabled) {\n");
		script.append("    var elem = window.document.getElementById(id);\n");
		script.append("    if (elem!=null) {\n");
		script.append("        elem.disabled = disabled;\n");
		script.append("    }\n");
		script.append("};\n");

		script.append("ZODB.dom.getTableHTMLForDbDataObjectSet = function(dataObjectSet,selectName,onclick) {\n");
		script.append("    var html = \"\";\n");
		script.append("    html += \"<table>\";\n");
		script.append("    var columns = 0;\n");
		script.append("    if (ZODB.model.classes[dataObjectSet.className]!=null) {\n");
		script.append("        var cls = ZODB.model.classes[dataObjectSet.className];\n");
		script.append("        for (var i = 0 ; i < dataObjectSet.objectArray.length; i++) {\n");
		script.append("            var object = dataObjectSet.objectArray[i];\n");
		script.append("            html += \"<tr>\";\n");
		script.append("            if (selectName || onclick) {\n");
		script.append("                html += \"<th width=''1%''>&nbsp;</th>\";\n");
		script.append("            }\n");
		script.append("            for (var p = 0; p < dataObjectSet.propertyNameArray.length; p++) {\n");
		script.append("                var propertyName = dataObjectSet.propertyNameArray[p];\n");
		script.append("                var property = cls.properties[propertyName];\n");
		script.append("                if (property && property.type!=\"link\" &&\n");
		script.append("                    (property.type!=\"string\" || (property.maxLength<=" + MAX_INPUT_LENGTH + ") && property.encode!=\"true\")) {\n");
		script.append("                    if (propertyName!=\"id\" && propertyName!=cls.selectProperty) {\n");
		script.append("                        if (columns==0) {\n");
		script.append("                            html += \"<th>\";\n");
		script.append("                        } else if (columns==1) {\n");
		script.append("                            html += \"<th class=''notVital''>\";\n");
		script.append("                        } else {\n");
		script.append("                            html += \"<th class=''notImportant''>\";\n");
		script.append("                        }\n");
		script.append("                    } else {\n");
		script.append("                        html += \"<th>\";\n");
		script.append("                    }\n");
		script.append("                    html += propertyName;\n");
		script.append("                    html += \"</th>\";\n");
		script.append("                    columns++;\n");
		script.append("                    if (columns>=" + MAX_SELECT_COLUMNS + ") {\n");
		script.append("                        break;\n");
		script.append("                    }\n");
		script.append("                }\n");
		script.append("            }\n");
		script.append("            break;\n");
		script.append("        }\n");
		script.append("        html += \"</tr>\";\n");
		script.append("    } else {\n");
		script.append("        for (var i = 0 ; i < dataObjectSet.objectArray.length; i++) {\n");
		script.append("            var object = dataObjectSet.objectArray[i];\n");
		script.append("            html += \"<tr>\";\n");
		script.append("            if (selectName || onclick) {\n");
		script.append("                html += \"<th width=''1%''>&nbsp;</th>\";\n");
		script.append("            }\n");
		script.append("            for (var p = 0; p < dataObjectSet.propertyNameArray.length; p++) {\n");
		script.append("                var name = dataObjectSet.propertyNameArray[p];\n");
		script.append("                if (name!=\"id\" && name!=\"name\") {\n");
		script.append("                    if (columns==0) {\n");
		script.append("                        html += \"<th>\";\n");
		script.append("                    } else if (columns==1) {\n");
		script.append("                        html += \"<th class=''notVital''>\";\n");
		script.append("                    } else {\n");
		script.append("                        html += \"<th class=''notImportant''>\";\n");
		script.append("                    }\n");
		script.append("                } else {\n");
		script.append("                    html += \"<th>\";\n");
		script.append("                }\n");
		script.append("                html += name;\n");
		script.append("                html += \"</th>\";\n");
		script.append("                columns++;\n");
		script.append("                if (columns>=" + MAX_SELECT_COLUMNS + ") {\n");
		script.append("                    break;\n");
		script.append("                }\n");
		script.append("            }\n");
		script.append("            html += \"</tr>\";\n");
		script.append("            break;\n");
		script.append("        }\n");
		script.append("    }\n");
		script.append("    for (var i = 0 ; i < dataObjectSet.objectArray.length; i++) {\n");
		script.append("        var object = dataObjectSet.objectArray[i];\n");
		script.append("        var id = object.getId();\n");
		script.append("        columns = 0;\n");
		script.append("        html += \"<tr>\";\n");
		script.append("        if (onclick) {\n");
		script.append("            html += \"<td width=''1%''>\";\n");
		script.append("            html += \"<input type=''radio'' name=''\" + selectName + \"'' value=''\" + id + \"'' onclick=''\" + onclick + \"''/>\";\n");
		script.append("            html += \"</td>\";\n");
		script.append("        } else if (selectName) {\n");
		script.append("            html += \"<td width=''1%''>\";\n");
		script.append("            html += \"<input type=''radio'' name=''\" + selectName + \"'' value=''\" + id + \"''/>\";\n");
		script.append("            html += \"</td>\";\n");
		script.append("        }\n");
		script.append("        if (ZODB.model.classes[dataObjectSet.className]!=null) {\n");
		script.append("            for (var p = 0; p < dataObjectSet.propertyNameArray.length; p++) {\n");
		script.append("                var propertyName = dataObjectSet.propertyNameArray[p];\n");
		script.append("                var property = cls.properties[propertyName];\n");
		script.append("                if (property && property.type!=\"link\" &&\n");
		script.append("                    (property.type!=\"string\" || (property.maxLength<=" + MAX_INPUT_LENGTH + ") && property.encode!=\"true\")) {\n");
		script.append("                    var align = \"\";\n");
		script.append("                    if (property.type==\"number\") {\n");
		script.append("                        align = \" align=''right''\"\n");
		script.append("                    }\n");
		script.append("                    if (propertyName!=\"id\" && propertyName!=cls.selectProperty) {\n");
		script.append("                        if (columns==0) {\n");
		script.append("                            html += \"<td\" + align +\">\";\n");
		script.append("                        } else if (columns==1) {\n");
		script.append("                            html += \"<td class=''notVital''\" + align +\">\";\n");
		script.append("                        } else {\n");
		script.append("                            html += \"<td class=''notImportant''\" + align +\">\";\n");
		script.append("                        }\n");
		script.append("                    } else {\n");
		script.append("                        html += \"<td\" + align +\">\";\n");
		script.append("                    }\n");
		script.append("                    if (object.propertyValues[propertyName]) {\n");
		script.append("                        var value = object.propertyValues[propertyName];\n");
		script.append("                        if (value.length>32) {\n");
		script.append("                            value = value.substring(0,32) + \" ...\"\n");
		script.append("                        }\n");
		script.append("                        if (value.length>0 && propertyName.indexOf(\"dateTime\")==0) {\n");
		script.append("                            value = new Date(parseInt(value,10));\n");
		script.append("                            value = ZODB.date.toString(value);\n");
		script.append("                        }\n");
		script.append("                        html += value;\n");
		script.append("                    }\n");
		script.append("                    html += \"</td>\";\n");
		script.append("                    columns++;\n");
		script.append("                    if (columns>=" + MAX_SELECT_COLUMNS + ") {\n");
		script.append("                        break;\n");
		script.append("                    }\n");
		script.append("                }\n");
		script.append("            }\n");
		script.append("        } else {\n");
		script.append("            for (var p = 0; p < dataObjectSet.propertyNameArray.length; p++) {\n");
		script.append("                var name = dataObjectSet.propertyNameArray[p];\n");
		script.append("                if (name!=\"id\" && name!=\"name\") {\n");
		script.append("                    if (columns==0) {\n");
		script.append("                        html += \"<td class=''notVital''>\";\n");
		script.append("                    } else {\n");
		script.append("                        html += \"<td class=''notImportant''>\";\n");
		script.append("                    }\n");
		script.append("                } else {\n");
		script.append("                    html += \"<td>\";\n");
		script.append("                }\n");
		script.append("                if (object.propertyValues[name]) {\n");
		script.append("                    var value = object.propertyValues[name];\n");
		script.append("                    if (value.length>32) {\n");
		script.append("                        value = value.substring(0,32) + \" ...\"\n");
		script.append("                    }\n");
		script.append("                    if (value.length>0 && name.indexOf(\"dateTime\")==0) {\n");
		script.append("                        value = new Date(parseInt(value,10));\n");
		script.append("                        value = ZODB.date.toString(value);\n");
		script.append("                    }\n");
		script.append("                    html += value;\n");
		script.append("                }\n");
		script.append("                html += \"</td>\";\n");
		script.append("                columns++;\n");
		script.append("                if (columns>=" + MAX_SELECT_COLUMNS + ") {\n");
		script.append("                    break;\n");
		script.append("                }\n");
		script.append("            }\n");
		script.append("        }\n");
		script.append("        html += \"</tr>\";\n");
		script.append("    }\n");
		script.append("    html += \"</table>\";\n");
		script.append("    html = html.replace(/''/g,'\"');\n");
		script.append("    html = html.replace(/@@/g,\"'\");\n");
		script.append("    return html;\n");
		script.append("};\n");

		script.append("ZODB.dom.getTableHTMLForDbDataObject = function(dataObject,className,prefixName,selectCallbackPrefix,disabledPropertyNameArray) {\n");
		script.append("    var html = \"\";\n");
		script.append("    html += \"<table>\";\n");
		script.append("    if (ZODB.model.classes[className]!=null) {\n");
		script.append("        var cls = ZODB.model.classes[className];\n");
		script.append("        for (var propertyName in cls.properties) {\n");
		script.append("            html += \"<tr>\";\n");
		script.append("            var property = cls.properties[propertyName];\n");
		script.append("            var value = \"\";\n");
		script.append("            if (dataObject!=null) {\n");
		script.append("                if (property.type == \"link\" && dataObject.linkValues[propertyName]) {\n");
		script.append("                    for (var i = 0; i < dataObject.linkValues[propertyName].length; i++) {\n");
		script.append("                        if (value!=\"\") {\n");
		script.append("                            value+=\",\";\n");
		script.append("                        }\n");
		script.append("                        value += dataObject.linkValues[propertyName][i];\n");
		script.append("                    }\n");
		script.append("                } else if (dataObject.propertyValues[propertyName]) {\n");
		script.append("                    value = dataObject.propertyValues[propertyName];\n");
		script.append("                }\n");
		script.append("            } else if (propertyName==\"id\") {\n");
		script.append("                value = 0;\n");
		script.append("            }\n");
		script.append("            html += \"<td width=''10%''>\";\n");
		script.append("            html += propertyName;\n");
		script.append("            html += \"</td>\";\n");
		script.append("            html += \"<td>\";\n");
		script.append("            var disabled = \"\";\n");
		script.append("            for (var di = 0 ; di < disabledPropertyNameArray.length; di++) {\n");
		script.append("                if (disabledPropertyNameArray[di]==propertyName) {\n");
		script.append("                    disabled = \" disabled\";\n");
		script.append("                    break;\n");
		script.append("                }\n");
		script.append("            }\n");
		script.append("            if (propertyName==\"id\") {\n");
		script.append("                html += \"<input type=''number'' id=''\" + prefixName + propertyName + \"'' value=''\" + value + \"'' disabled/>\";\n");
		script.append("            } else if (property.type == \"string\") {\n");
		script.append("                if (property.maxLength > " + MAX_INPUT_LENGTH + " && property.encode!=\"true\") {\n");
		script.append("                    html += \"<textarea id=''\" + prefixName + propertyName + \"''\" + disabled + \">\" + value + \"</textarea>\";\n");
		script.append("                } else {\n");
		script.append("                    var inType = \"text\";\n");
		script.append("                    if (property.encode==\"true\") {\n");
		script.append("                        var inType = \"password\";\n");
		script.append("                        value = ZODB.data.decodeAscii(value);\n");
		script.append("                    }\n");
		script.append("                    html += \"<input type=''\" + inType + \"'' id=''\" + prefixName + propertyName + \"'' value=''\" + value + \"'' class=''textInput''\" + disabled + \"/>\";\n");
		script.append("                }\n");
		script.append("            } else if (property.type == \"number\") {\n");
		script.append("                html += \"<input type=''number'' id=''\" + prefixName + propertyName + \"'' value=''\" + value + \"''\" + disabled + \"/>\";\n");
		script.append("                if (value.length>0 && propertyName.indexOf(\"dateTime\")==0) {\n");
		script.append("                    value = new Date(parseInt(value,10));\n");
		script.append("                    value = ZODB.date.toString(value);\n");
		script.append("                    html += \" \" + value;\n");
		script.append("                }\n");
		script.append("            } else if (property.type == \"link\") {\n");
		script.append("                html += \"<input type=''hidden'' id=''\" + prefixName + propertyName + \"'' value=''\" + value + \"''/>\";\n");
		script.append("                if (property.maxSize > 1) {\n");
		script.append("                    html += \"<select id=''OPTIONS_\" + prefixName + propertyName + \"'' onchange=''\" + selectCallbackPrefix + \"[@@\" + prefixName + propertyName + \"@@].changedSelectedValuesCallback();'' multiple\" + disabled + \"/>\";\n");
		script.append("                } else {\n");
		script.append("                    html += \"<select id=''OPTIONS_\" + prefixName + propertyName + \"'' onchange=''\" + selectCallbackPrefix + \"[@@\" + prefixName + propertyName + \"@@].changedSelectedValuesCallback();''\" + disabled + \"/>\";\n");
		script.append("                }\n");
		script.append("            }\n");
		script.append("            html += \"</td>\";\n");
		script.append("            html += \"</tr>\";\n");
		script.append("        }\n");
		script.append("    } else {\n");
		script.append("        ZODB.debug.err(\"getTableHTMLForDbDataObject: This function requires the model is loaded and contains the specified class\");\n");
		script.append("    }\n");
		script.append("    html += \"</table>\";\n");
		script.append("    html = html.replace(/''/g,'\"');\n");
		script.append("    html = html.replace(/@@/g,\"'\");\n");
		script.append("    return html;\n");
		script.append("};\n");
		
		script.append("ZODB.dom.setDivInnerHTMLByElementId = function(id,value) {\n");
		script.append("    var elem = window.document.getElementById(id);\n");
		script.append("    if (elem!=null) {\n");
		script.append("        elem.innerHTML = value;\n");
		script.append("    }\n");
		script.append("};\n");

		script.append("ZODB.dom.clearSelectOptionsByElementId = function(id) {\n");
		script.append("    var elem = window.document.getElementById(id);\n");
		script.append("    if (elem!=null) {\n");
		script.append("        elem.innerHTML = \"\";\n");
		script.append("    }\n");
		script.append("};\n");

		script.append("ZODB.dom.focusInputByElementId = function(id) {\n");
		script.append("    var elem = window.document.getElementById(id);\n");
		script.append("    if (elem!=null) {\n");
		script.append("        elem.blur();\n");
		script.append("        elem.focus();\n");
		script.append("    }\n");
		script.append("};\n");

		script.append("ZODB.dom.addSelectOptionByElementId = function(id,value,text) {\n");
		script.append("    var elem = window.document.getElementById(id);\n");
		script.append("    if (elem!=null) {\n");
		script.append("        var option = window.document.createElement(\"option\");\n");
		script.append("        option.text = text;\n");
		script.append("        option.value = value;\n");
		script.append("        elem.add(option);\n");
		script.append("    }\n");
		script.append("};\n");

		script.append("ZODB.dom.selectOptionByElementIdByOptionValues = function(id,values) {\n");
		script.append("    var elem = window.document.getElementById(id);\n");
		script.append("    if (elem!=null) {\n");
		script.append("        for (var v = 0; v < values.length; v++) {\n");
		script.append("            var value = values[v];\n");
		script.append("            for (var i = 0; i < elem.options.length; i++) {\n");
		script.append("                if (elem.options[i].value==value) {\n");
		script.append("                    elem.options[i].selected = true;\n");
		script.append("                }\n");
		script.append("            }\n");
		script.append("        }\n");
		script.append("    }\n");
		script.append("};\n");

		script.append("ZODB.dom.getSelectedValuesByElementId = function(id) {\n");
		script.append("    values = [];\n");
		script.append("    var elem = window.document.getElementById(id);\n");
		script.append("    if (elem!=null) {\n");
		script.append("        for (var i = 0; i < elem.options.length; i++) {\n");
		script.append("            if (elem.options[i].selected && elem.options[i].value!=\"\") {\n");
		script.append("                values[values.length] = elem.options[i].value;\n");
		script.append("            }\n");
		script.append("        }\n");
		script.append("    }\n");
		script.append("    return values;\n");
		script.append("};\n");
		
		script.append("ZODB.dom.bindEnterFunctionToElementId = function(id,enterFunction) {\n");
		script.append("    var elem = window.document.getElementById(id);\n");
		script.append("    if (elem!=null) {\n");
		script.append("        ZODB.dom.enterFunctions[id] = enterFunction;\n");
		script.append("    }\n");
		script.append("};\n");

		script.append("ZODB.dom.handleKeyPress = function(event) {\n");
		script.append("    var elem = event.target || event.srcElement;\n");
		script.append("    if (elem!=null && elem.id!=null && event.keyCode==13 && ZODB.dom.enterFunctions[elem.id]!=null) {\n");
		script.append("        ZODB.dom.enterFunctions[elem.id]();\n");
		script.append("    }\n");
		script.append("};\n");
		script.append("window.document.onkeypress = ZODB.dom.handleKeyPress;\n");

		return script;
	}

	protected StringBuilder getGuiJavaScript() {
		StringBuilder script = new StringBuilder();

		script.append("var ZODB = ZODB || {};\n");
		script.append("ZODB.gui = {};\n");
		script.append("ZODB.gui.selects = {};\n");
		script.append("ZODB.gui.details = {};\n");
		script.append("ZODB.gui.managers = {};\n");
		script.append("ZODB.gui.genericResponseCallbackRedirect = false;\n");
		
		script.append("ZODB.gui.genericResponseCallback = function(xhttp) {\n");
		script.append("    var xmlDoc = xhttp.responseXML;\n");
		script.append("    if (xmlDoc!=null) {\n");
		script.append("        var rElems = xmlDoc.getElementsByTagName(\"redirect\");\n");
		script.append("        var redir = \"\";\n");
		script.append("        var warn = true;\n");
		script.append("        if (!ZODB.gui.genericResponseCallbackRedirect && rElems.length>0) {;\n");
		script.append("            ZODB.gui.genericResponseCallbackRedirect = true;\n");
		script.append("            redir = rElems[0].childNodes[0].nodeValue;\n");
		script.append("        } else {\n");
		script.append("            warn = false;\n");
		script.append("        }\n");
		script.append("        var mElems = xmlDoc.getElementsByTagName(\"message\");\n");
		script.append("        if (warn && mElems.length>0) {\n");
		script.append("            alert(mElems[0].childNodes[0].nodeValue);\n");
		script.append("        }\n");
		script.append("        if (redir.length>0) {\n");
		script.append("            window.location.href = redir;\n");
		script.append("        }\n");
		script.append("    } else {\n");
		script.append("        ZODB.ajax.defaultErrorCallback(xhttp);\n");
		script.append("    }\n");
		script.append("};\n");

		script.append("ZODB.gui.Select = function(id,packageName,className) {\n");
		script.append("    var that = this;\n");
		script.append("    this.id = id;\n");
		script.append("    this.packageName = packageName;\n");
		script.append("    this.className = className;\n");
		script.append("    this.dataObjectSet = null;\n");
		script.append("    this.selectedId = null;\n");
		script.append("    this.selectedIdChangedCallback = null;\n");
		script.append("    this.filterProperty = null;\n");
		script.append("    this.filterValue = null;\n");
		script.append("    this.getFullClassName = function() {\n");
		script.append("        return this.packageName + \".\" + this.className;\n");
		script.append("    };\n");
		script.append("    this.executeGetRequestCallback = function(xhttp) {\n");
		script.append("        if (that.selectedId!=null) {\n");
		script.append("            that.setSelectedId(null);\n");
		script.append("        }\n");
		script.append("        that.dataObjectSet = ZODB.data.getDataForClassCallback(xhttp);\n");
		script.append("        if (that.propertyNameArray!=null) {\n");
		script.append("            that.dataObjectSet.propertyNameArray=that.propertyNameArray;\n");
		script.append("        }\n");
		script.append("        that.refresh();\n");
		script.append("    };\n");
		script.append("    this.clear = function() {\n");
		script.append("        if (that.selectedId!=null) {\n");
		script.append("            that.setSelectedId(null);\n");
		script.append("        }\n");
		script.append("        that.dataObjectSet = null;\n");
		script.append("        that.refresh();\n");
		script.append("    };\n");
		script.append("    this.refresh = function() {\n");
		script.append("        var refreshDataObjectSet = null;\n");
		script.append("        if (that.dataObjectSet==null) {\n");
		script.append("            refreshDataObjectSet = new ZODB.data.DbDataObjectSet(that.getFullClassName());\n");
		script.append("        } else {\n");
		script.append("            refreshDataObjectSet = that.dataObjectSet;\n");
		script.append("        }\n");
		script.append("        if (that.dataObjectSet!=null) {\n");
		script.append("            var html = \"<b>Showing&nbsp;\" + that.dataObjectSet.countObjects + \"&nbsp;/&nbsp;\" + that.dataObjectSet.count + \"&nbsp;objects</b>\";\n");
		script.append("            ZODB.dom.setDivInnerHTMLByElementId(\"RESULTS\" + that.id,html);\n");
		script.append("            ZODB.dom.showElementById(\"RESULTS\" + that.id);\n");
		script.append("        } else {\n");
		script.append("            ZODB.dom.setDivInnerHTMLByElementId(\"RESULTS\" + that.id,\"\");\n");
		script.append("            ZODB.dom.hideElementById(\"RESULTS\" + that.id);\n");
		script.append("        }\n");
		script.append("        var html = ZODB.dom.getTableHTMLForDbDataObjectSet(refreshDataObjectSet,\"IDS\" + that.id,\"ZODB.gui.selects[@@\" + that.id + \"@@].setSelectedId(this.value);\");\n");
		script.append("        ZODB.dom.setDivInnerHTMLByElementId(\"SELECT\" + that.id,html);\n");
		script.append("        ZODB.dom.showElementById(\"SELECT\" + that.id);\n");
		script.append("    };\n");
		script.append("    this.executeGetRequest = function(start) {\n");
		script.append("        if (start==null || start==\"undefined\") {;\n");
		script.append("            start = ZODB.dom.getNumberInputValueByElementId(\"START\" + that.id);\n");
		script.append("        }\n");
		script.append("        if (start<0) {\n");
		script.append("            start=0;\n");
		script.append("        }\n");
		script.append("        ZODB.dom.setInputValueByElementId(\"START\" + that.id,start);\n");
		script.append("        var getRequest = new ZODB.data.GetRequest(that.getFullClassName());\n");
		script.append("        getRequest.start = start;\n");
		script.append("        getRequest.limit = 10;\n");
		script.append("        getRequest.start = start;\n");
		script.append("        if (that.propertyNameArray!=null) {\n");
		script.append("            getRequest.properties=that.propertyNameArray;\n");
		script.append("        }\n");
		script.append("        if (that.filterProperty!=null && that.filterValue!=null) {\n");
		script.append("            getRequest.filters[that.filterProperty]=that.filterValue;\n");
		script.append("        }\n");
		script.append("        ZODB.data.executeGetRequest(getRequest,that.executeGetRequestCallback,ZODB.gui.genericResponseCallback);\n");	
		script.append("    };\n");
		script.append("    this.executePrevGetRequest = function() {\n");
		script.append("        var start = ZODB.dom.getNumberInputValueByElementId(\"START\" + this.id);\n");
		script.append("        start = start - 10;\n");
		script.append("        this.executeGetRequest(start);\n");	
		script.append("    };\n");
		script.append("    this.executeNextGetRequest = function() {\n");
		script.append("        var start = ZODB.dom.getNumberInputValueByElementId(\"START\" + this.id);\n");
		script.append("        start = start + 10;\n");
		script.append("        this.executeGetRequest(start);\n");	
		script.append("    };\n");
		script.append("    this.setSelectedId = function(selId) {\n");
		script.append("        this.selectedId = selId;\n");
		script.append("        if (this.selectedIdChangedCallback) {\n");
		script.append("            this.selectedIdChangedCallback(selId);\n");
		script.append("        }\n");
		script.append("    };\n");
		script.append("    this.unsetSelectedId = function(doCallback) {\n");
		script.append("        this.selectedId = null;\n");
		script.append("        ZODB.dom.unselectRadioByElementName(\"IDS\" + that.id);\n");
		script.append("        if (doCallback && this.selectedIdChangedCallback) {\n");
		script.append("            this.selectedIdChangedCallback(selId);\n");
		script.append("        }\n");
		script.append("    };\n");
		script.append("    this.getSelectedDataObject = function() {\n");
		script.append("        var dataObject = null;\n");
		script.append("        if (this.selectedId!=null) {\n");
		script.append("            dataObject = this.dataObjectSet.objects[this.selectedId];\n");
		script.append("        }\n");
		script.append("        return dataObject;\n");
		script.append("    };\n");
		script.append("    this.initializeHTML = function() {\n");
		script.append("        var html = \"\";\n");
		script.append("        html += \"<div id='GET\" + this.id + \"'>\";\n");
		script.append("        html += \"<input type='button' value='Get' onclick='ZODB.gui.selects[@@\" + this.id + \"@@].executeGetRequest()'/>\";\n");
		script.append("        html += \"<span class='notVital'>&nbsp;Start:&nbsp;</span><input id='START\" + this.id + \"' type='number' value='0'/>\";\n");
		script.append("        html += \"<input type='button' value='<' onclick='ZODB.gui.selects[@@\" + this.id + \"@@].executePrevGetRequest()'/>\";\n");
		script.append("        html += \"<input type='button' value='>' onclick='ZODB.gui.selects[@@\" + this.id + \"@@].executeNextGetRequest()'/>\";\n");
		script.append("        html += \"</div>\";\n");
		script.append("        html += \"<div id='RESULTS\" + this.id + \"' style='visibility: hidden;'>\";\n");
		script.append("        html += \"</div>\";\n");
		script.append("        html += \"<div id='SELECT\" + this.id + \"' style='visibility: hidden;'>\";\n");
		script.append("        html += \"</div>\";\n");
		script.append("        html = html.replace(/'/g,'\"');\n");
		script.append("        html = html.replace(/@@/g,\"'\");\n");
		script.append("        ZODB.dom.setDivInnerHTMLByElementId(this.id,html);\n");
		script.append("        ZODB.dom.showElementById(this.id);\n");
		script.append("        ZODB.dom.bindEnterFunctionToElementId(\"START\" + this.id,ZODB.gui.selects[this.id].executeGetRequest);\n");
		script.append("    };\n");
		script.append("    this.initialize = function(executeRequest) {\n");
		script.append("        this.initializeHTML();\n");
		script.append("        if (executeRequest) {\n");
		script.append("            this.executeGetRequest(0);\n");
		script.append("        }\n");
		script.append("    };\n");
		script.append("    ZODB.gui.selects[id] = this;\n");
		script.append("};\n");

		script.append("ZODB.gui.Detail = function(id,packageName,className) {\n");
		script.append("    var that = this;\n");
		script.append("    this.id = id;\n");
		script.append("    this.packageName = packageName;\n");
		script.append("    this.className = className;\n");
		script.append("    this.dataObject = null;\n");
		script.append("    this.initialDataObject = null;\n");
		script.append("    this.impactedIds = [];\n");
		script.append("    this.propertyNameArray = null;\n");
		script.append("    this.disabledPropertyNameArray = [];\n");
		script.append("    this.selectedObjectChangedCallback = null;\n");
		script.append("    this.impactedObjectsCallback = null;\n");
		script.append("    this.getSelectCallbacks = {};\n");
		script.append("    this.lastSelectRefreshDate = null;\n");
		script.append("    this.filterProperty = null;\n");
		script.append("    this.filterValue = null;\n");
		script.append("    this.getFullClassName = function() {\n");
		script.append("        return this.packageName + \".\" + this.className;\n");
		script.append("    };\n");
		script.append("    this.setDataObject = function(dataObject) {\n");
		script.append("        this.dataObject = dataObject;\n");
		script.append("        var html = \"\";\n");
		script.append("        if (dataObject==null) {\n");
		script.append("            ZODB.dom.setInputDisabledByElementId(\"DETAIL_SAVE\" + this.id,false);\n");
		script.append("            ZODB.dom.setInputDisabledByElementId(\"DETAIL_TITLE_REMOVE\" + this.id,false);\n");
		script.append("            html += \"<b>New object</b>\";\n");
		script.append("        } else {\n");
		script.append("            html += \"<b>Selected object</b><br/>\";\n");
		script.append("            html += \"<input id='DETAIL_TITLE_NEW\" + this.id + \"' type='button' value='New' onclick='ZODB.gui.details[@@\" + this.id + \"@@].setDataObject(null)'/>\";\n");
		script.append("            html += \"<input id='DETAIL_TITLE_REMOVE\" + this.id + \"' type='button' value='Remove' onclick='ZODB.gui.details[@@\" + this.id + \"@@].removeDataObject()'/>\";\n");
		script.append("            html += \"<input id='DETAIL_TITLE_REMOVE_CONFIRM\" + this.id + \"' type='checkbox' value='Confirm'/>\";\n");
		script.append("        }\n");
		script.append("        html = html.replace(/'/g,'\"');\n");
		script.append("        html = html.replace(/@@/g,\"'\");\n");
		script.append("        ZODB.dom.setDivInnerHTMLByElementId(\"DETAIL_TITLE\" + this.id,html);\n");
		script.append("        ZODB.dom.showElementById(\"DETAIL_TITLE\" + this.id);\n");
		script.append("        var selectCallbackPrefix = \"ZODB.gui.details[@@\" + this.id + \"@@].getSelectCallbacks\";\n");
		script.append("        var tableDataObject = this.dataObject;\n");
		script.append("        if (tableDataObject==null) {\n");
		script.append("            tableDataObject = this.initialDataObject;\n");
		script.append("        }\n");
		script.append("        html = ZODB.dom.getTableHTMLForDbDataObject(tableDataObject,this.getFullClassName(),\"PROPS\" + this.id,selectCallbackPrefix,this.disabledPropertyNameArray);\n");
		script.append("        ZODB.dom.setDivInnerHTMLByElementId(\"DETAILS\" + this.id,html);\n");
		script.append("        ZODB.dom.showElementById(\"DETAILS\" + this.id);\n");
		script.append("        if (this.selectedObjectChangedCallback!=null) {\n");
		script.append("            this.selectedObjectChangedCallback(this.dataObject);\n");
		script.append("        }\n");
		script.append("        if (this.dataObject!=null) {\n");
		script.append("            this.refreshSelects();\n");
		script.append("        } else {\n");
		script.append("            if (this.filterProperty!=null && this.filterValue!=null) {\n");
		script.append("                var cls = ZODB.model.classes[this.getFullClassName()];\n");
		script.append("                var inputId = \"PROPS\" + this.id + this.filterProperty;\n");
		script.append("                ZODB.dom.setInputValueByElementId(inputId,this.filterValue);\n");
		script.append("            }\n");
		script.append("            this.initializeSelects();\n");
		script.append("        }\n");
		script.append("    };\n");
		script.append("    this.executePostRequestErrorCallback = function(xhttp) {\n");
		script.append("        ZODB.gui.genericResponseCallback(xhttp);\n");
		script.append("        ZODB.dom.setInputDisabledByElementId(\"DETAIL_SAVE\" + that.id,false);\n");
		script.append("        ZODB.dom.setInputDisabledByElementId(\"DETAIL_TITLE_REMOVE\" + that.id,false);\n");
		script.append("    };\n");
		script.append("    this.executePostRequestCallback = function(xhttp) {\n");
		script.append("        var errors = ZODB.data.getErrorsCallback(xhttp);\n");
		script.append("        if (errors.length>0) {\n");
		script.append("            alert(errors[0].message);\n");
		script.append("        }\n");
		script.append("        ZODB.dom.setInputDisabledByElementId(\"DETAIL_SAVE\" + that.id,false);\n");
		script.append("        ZODB.dom.setInputDisabledByElementId(\"DETAIL_TITLE_REMOVE\" + that.id,false);\n");
		script.append("        var ids = ZODB.data.getImpactedIdsCallback(xhttp);\n");
		script.append("        if (ids.length>0) {\n");
		script.append("            that.lastSelectRefreshDate = null;\n");
		script.append("            that.setDataObject(null);\n");
		script.append("            if (that.impactedObjectsCallback!=null) {\n");
		script.append("                that.impactedObjectsCallback(ids);\n");
		script.append("            }\n");
		script.append("        }\n");
		script.append("    };\n");
		script.append("    this.removeDataObject = function() {\n");
		script.append("        var remove = false;\n");
		script.append("        if (this.dataObject == null) {\n");
		script.append("            ZODB.debug.err(\"No object selected, nothing to remove\");\n");
		script.append("        } else {\n");
		script.append("            remove = ZODB.dom.getInputCheckedByElementId(\"DETAIL_TITLE_REMOVE_CONFIRM\" + this.id);\n");
		script.append("            if (!remove) {\n");
		script.append("                remove = confirm(\"Are you sure you want to remove the selected object?\");\n");
		script.append("            }\n");
		script.append("        }\n");
		script.append("        if (remove) {\n");
		script.append("            ZODB.dom.setInputDisabledByElementId(\"DETAIL_SAVE\" + this.id,true);\n");
		script.append("            ZODB.dom.setInputDisabledByElementId(\"DETAIL_TITLE_REMOVE\" + this.id,true);\n");
		script.append("            var request = new ZODB.data.RemoveRequest(this.getFullClassName());\n");
		script.append("            request.getRequest.filters[\"id\"] = this.dataObject.getId();\n");
		script.append("            ZODB.data.executePostRequest(request,this.executePostRequestCallback,this.executePostRequestErrorCallback);\n");
		script.append("        }\n");
		script.append("    };\n");
		script.append("    this.saveDataObject = function() {\n");
		script.append("        ZODB.dom.setInputDisabledByElementId(\"DETAIL_SAVE\" + this.id,true);\n");
		script.append("        ZODB.dom.setInputDisabledByElementId(\"DETAIL_TITLE_REMOVE\" + this.id,true);\n");
		script.append("        var request = new ZODB.data.UpdateRequest(this.getFullClassName());\n");
		script.append("        var requestObject = this.dataObject;\n");
		script.append("        if (requestObject == null) {\n");
		script.append("            requestObject = new ZODB.data.DbDataObject();\n");
		script.append("            request = new ZODB.data.AddRequest(this.getFullClassName());\n");
		script.append("        } else {\n");
		script.append("            requestObject = new ZODB.data.DbDataObject();\n");
		script.append("            request.getRequest.filters[\"id\"] = this.dataObject.getId();\n");
		script.append("        }\n");
		script.append("        if (ZODB.model.classes[this.getFullClassName()]!=null) {\n");
		script.append("            var cls = ZODB.model.classes[this.getFullClassName()];\n");
		script.append("            for (var propertyName in cls.properties) {\n");
		script.append("                var disabled = false;\n");
		script.append("                for (var di = 0 ; di < this.disabledPropertyNameArray.length; di++) {\n");
		script.append("                    if (this.disabledPropertyNameArray[di]==propertyName) {\n");
		script.append("                        disabled = true;\n");
		script.append("                        break;\n");
		script.append("                    }\n");
		script.append("                }\n");
		script.append("                if (!disabled) {\n");
		script.append("                    var inputId = \"PROPS\" + this.id + propertyName;\n");
		script.append("                    if (cls.properties[propertyName].type==\"link\") {\n");
		script.append("                        var value = ZODB.dom.getInputValueByElementId(inputId);\n");
		script.append("                        if (value!=null && value!=\"\") {\n");
		script.append("                            requestObject.linkValues[propertyName]=value.split(\",\");\n");
		script.append("                        } else {\n");
		script.append("                            requestObject.linkValues[propertyName]=[];\n");
		script.append("                        }\n");
		script.append("                    } else if (cls.properties[propertyName].type==\"number\") {\n");
		script.append("                        var value = ZODB.dom.getNumberInputValueByElementId(inputId);\n");
		script.append("                        requestObject.propertyValues[propertyName]=value;\n");
		script.append("                    } else if (cls.properties[propertyName].type==\"string\") {\n");
		script.append("                        var value = ZODB.dom.getInputValueByElementId(inputId);\n");
		script.append("                        if (value!=null) {\n");
		script.append("                            requestObject.propertyValues[propertyName]=value;\n");
		script.append("                        }\n");
		script.append("                    }\n");
		script.append("                }\n");
		script.append("            }\n");
		script.append("        }\n");
		script.append("        request.dataObjectSet.objects[requestObject.getId()] = requestObject;\n");
		script.append("        ZODB.data.executePostRequest(request,this.executePostRequestCallback,this.executePostRequestErrorCallback);\n");
		script.append("    };\n");
		script.append("    this.initializeHTML = function() {\n");
		script.append("        var html = \"\";\n");
		script.append("        html += \"<div id='DETAIL_TITLE\" + this.id + \"' style='visibility: hidden;'>\";\n");
		script.append("        html += \"</div>\";\n");
		script.append("        html += \"<div id='DETAILS\" + this.id + \"' style='visibility: hidden;'>\";\n");
		script.append("        html += \"</div>\";\n");
		script.append("        html += \"<div>\";\n");
		script.append("        html += \"<input id='DETAIL_SAVE\" + this.id + \"' type='button' value='Save' onclick='ZODB.gui.details[@@\" + this.id + \"@@].saveDataObject()'/>\";\n");
		script.append("        html += \"</div>\";\n");
		script.append("        html = html.replace(/'/g,'\"');\n");
		script.append("        html = html.replace(/@@/g,\"'\");\n");
		script.append("        ZODB.dom.setDivInnerHTMLByElementId(this.id,html);\n");
		script.append("        ZODB.dom.showElementById(this.id);\n");
		script.append("    };\n");
		script.append("    this.GetSelectCallback = function(elemId,elemValue) {\n");
		script.append("        var that = this;\n");
		script.append("        this.elementId = elemId;\n");
		script.append("        this.elementOptionsId = \"OPTIONS_\" + elemId;\n");
		script.append("        this.elementValue = elemValue;\n");
		script.append("        this.dataObjectSet = null;\n");
		script.append("        this.getRequestCallback = function(xhttp) {\n");
		script.append("            that.dataObjectSet = ZODB.data.getDataForClassCallback(xhttp);\n");
		script.append("            that.refresh();\n");
		script.append("        };\n");
		script.append("        this.refresh = function() {\n");
		script.append("            ZODB.dom.clearSelectOptionsByElementId(that.elementOptionsId);\n");
		script.append("            ZODB.dom.addSelectOptionByElementId(that.elementOptionsId,\"\",\"\");\n");
		script.append("            if (that.dataObjectSet!=null) {\n");
		script.append("                for (var id in that.dataObjectSet.objects) {\n");
		script.append("                    var cls = ZODB.model.classes[that.dataObjectSet.className];\n");
		script.append("                    var name = \"\";\n");
		script.append("                    if (cls.selectProperty!=null) {\n");
		script.append("                        name = that.dataObjectSet.objects[id].propertyValues[cls.selectProperty];\n");
		script.append("                    } else {\n");
		script.append("                        name = \"\" + id;\n");
		script.append("                    }\n");
		script.append("                    ZODB.dom.addSelectOptionByElementId(that.elementOptionsId,id,name);\n");
		script.append("                }\n");
		script.append("            }\n");
		script.append("            if (that.elementValue!=null && that.elementValue!=\"\") {\n");
		script.append("                ZODB.dom.selectOptionByElementIdByOptionValues(that.elementOptionsId,that.elementValue.split(\",\"));\n");
		script.append("            } else {\n");
		script.append("                ZODB.dom.selectOptionByElementIdByOptionValues(that.elementOptionsId,[]);\n");
		script.append("            }\n");
		script.append("        };\n");
		script.append("        this.changedSelectedValuesCallback = function() {\n");
		script.append("            var values = ZODB.dom.getSelectedValuesByElementId(this.elementOptionsId);\n");
		script.append("            var value = \"\";\n");
		script.append("            if (values.length>0) {\n");
		script.append("                for (var i = 0; i < values.length; i++) {\n");
		script.append("                    if (value!=\"\") {\n");
		script.append("                        value+=\",\";\n");
		script.append("                    }\n");
		script.append("                    value += values[i];\n");
		script.append("                }\n");
		script.append("            }\n");
		script.append("            ZODB.dom.setInputValueByElementId(this.elementId,value);\n");
		script.append("        };\n");
		script.append("    };\n");
		script.append("    this.initializeSelects = function() {\n");
		script.append("        if (this.lastSelectRefreshDate!=null) {\n");
		script.append("            var now = new Date();\n");
		script.append("            if ((now.getTime() - this.lastSelectRefreshDate.getTime()) < 60000) {\n");
		script.append("                this.refreshSelects();\n");
		script.append("                return;\n");
		script.append("            }\n");
		script.append("        }\n");
		script.append("        if (ZODB.model.classes[this.getFullClassName()]!=null) {\n");
		script.append("            var cls = ZODB.model.classes[this.getFullClassName()];\n");
		script.append("            for (var propertyName in cls.properties) {\n");
		script.append("                 if (cls.properties[propertyName].type==\"link\") {\n");
		script.append("                     var elemId = \"PROPS\" + this.id + propertyName;\n");
		script.append("                     var elemValue = \"\";\n");
		script.append("                     if (this.dataObject!=null && this.dataObject.linkValues[propertyName]) {;\n");
		script.append("                         for (var i = 0; i < this.dataObject.linkValues[propertyName].length; i++) {\n");
		script.append("                             if (elemValue.length>0) {\n");
		script.append("                                 elemValue += \",\"\n");
		script.append("                             }\n");
		script.append("                             elemValue += this.dataObject.linkValues[propertyName][i];\n");
		script.append("                         }\n");
		script.append("                     } else if (ZODB.dom.getInputValueByElementId(elemId)!=null) {;\n");
		script.append("                         elemValue = ZODB.dom.getInputValueByElementId(elemId);\n");
		script.append("                     }\n");
		script.append("                     this.getSelectCallbacks[elemId] = new this.GetSelectCallback(elemId,elemValue);\n");
		script.append("                     var getRequest = new ZODB.data.GetRequest(cls.properties[propertyName].classTo);\n");
		script.append("                     var clsTo = ZODB.model.classes[cls.properties[propertyName].classTo];\n");
		script.append("                     getRequest.properties[0] = \"id\";\n");
		script.append("                     if (clsTo.selectProperty!=null) {\n");
		script.append("                         getRequest.properties[1] = clsTo.selectProperty;\n");
		script.append("                     }\n");
		script.append("                     ZODB.data.executeGetRequest(getRequest,this.getSelectCallbacks[elemId].getRequestCallback,ZODB.gui.genericResponseCallback);\n");
		script.append("                 }\n");
		script.append("            }\n");
		script.append("            this.lastSelectRefreshDate = new Date();\n");
		script.append("        }\n");
		script.append("    };\n");
		script.append("    this.refreshSelects = function() {\n");
		script.append("        if (ZODB.model.classes[this.getFullClassName()]!=null) {\n");
		script.append("            var cls = ZODB.model.classes[this.getFullClassName()];\n");
		script.append("            for (var propertyName in cls.properties) {\n");
		script.append("                 if (cls.properties[propertyName].type==\"link\") {\n");
		script.append("                     var elemId = \"PROPS\" + this.id + propertyName;\n");
		script.append("                     var elemValue = \"\";\n");
		script.append("                     if (this.dataObject!=null && this.dataObject.linkValues[propertyName]) {;\n");
		script.append("                         for (var i = 0; i < this.dataObject.linkValues[propertyName].length; i++) {\n");
		script.append("                             if (elemValue.length>0) {\n");
		script.append("                                 elemValue += \",\"\n");
		script.append("                             }\n");
		script.append("                             elemValue += this.dataObject.linkValues[propertyName][i];\n");
		script.append("                         }\n");
		script.append("                     } else if (ZODB.dom.getInputValueByElementId(elemId)!=null) {;\n");
		script.append("                         elemValue = ZODB.dom.getInputValueByElementId(elemId);\n");
		script.append("                     }\n");
		script.append("                     this.getSelectCallbacks[elemId].elementValue = elemValue;\n");
		script.append("                     this.getSelectCallbacks[elemId].refresh();\n");
		script.append("                 }\n");
		script.append("            }\n");
		script.append("        }\n");
		script.append("    };\n");
		script.append("    this.initialize = function() {\n");
		script.append("        this.initializeHTML();\n");
		script.append("        this.setDataObject(null);\n");
		script.append("    };\n");
		script.append("    ZODB.gui.details[id] = this;\n");
		script.append("};\n");

		script.append("ZODB.gui.Manager = function(id,packageName,className) {\n");
		script.append("    var that = this;\n");
		script.append("    this.id = id;\n");
		script.append("    this.packageName = packageName;\n");
		script.append("    this.className = className;\n");
		script.append("    this.childManagers = [];\n");
		script.append("    this.selectedObjectChangedCallback = null;\n");
		script.append("    this.isVisible = false;\n");
		script.append("    this.select = new ZODB.gui.Select(\"SELECT_\" + this.id,this.packageName,this.className);\n");
		script.append("    this.detail = new ZODB.gui.Detail(\"DETAIL_\" + this.id,this.packageName,this.className);\n");
		script.append("    this.getFullClassName = function() {\n");
		script.append("        return this.packageName + \".\" + this.className;\n");
		script.append("    };\n");
		script.append("    this.initializeHTML = function() {\n");
		script.append("        var html = \"\";\n");
		script.append("        html += \"<div id='SELECT_\" + this.id + \"' style='visibility: hidden;'>\";\n");
		script.append("        html += \"</div>\";\n");
		script.append("        html += \"<div id='DETAIL_\" + this.id + \"' style='visibility: hidden;'>\";\n");
		script.append("        html += \"</div>\";\n");
		script.append("        html = html.replace(/'/g,'\"');\n");
		script.append("        html = html.replace(/@@/g,\"'\");\n");
		script.append("        ZODB.dom.setDivInnerHTMLByElementId(this.id,html);\n");
		script.append("    };\n");
		script.append("    this.show = function() {\n");
		script.append("        ZODB.dom.showElementById(this.id);\n");
		script.append("        this.isVisible = true;\n");
		script.append("    };\n");
		script.append("    this.hide = function() {\n");
		script.append("        ZODB.dom.hideElementById(this.id);\n");
		script.append("        this.isVisible = false;\n");
		script.append("    };\n");
		script.append("    this.toggleVisibility = function(source) {\n");
		script.append("        if (this.isVisible) {\n");
		script.append("            this.hide();\n");
		script.append("            if (source && source.value) {\n");
		script.append("                source.value=\"+\"\n");
		script.append("            }\n");
		script.append("        } else {\n");
		script.append("            this.show();\n");
		script.append("            if (source && source.value) {\n");
		script.append("                source.value=\"-\"\n");
		script.append("            }\n");
		script.append("        }\n");
		script.append("    };\n");
		script.append("    this.select.selectedIdChangedCallback = function(selId) {\n");
		script.append("        that.detail.setDataObject(that.select.getSelectedDataObject());\n");
		script.append("    };\n");
		script.append("    this.detail.selectedObjectChangedCallback = function(dataObject) {\n");
		script.append("        if (dataObject==null) {\n");
		script.append("            that.select.unsetSelectedId(false);\n");
		script.append("        }\n");
		script.append("        if (that.selectedObjectChangedCallback!=null) {\n");
		script.append("            that.selectedObjectChangedCallback(dataObject);\n");
		script.append("        }\n");
		script.append("        if (dataObject!=null) {\n");
		script.append("            for (var i = 0; i < that.childManagers.length; i++) {\n");
		script.append("                that.childManagers[i].select.executeGetRequest();\n");
		script.append("            }\n");
		script.append("        } else {\n");
		script.append("            for (var i = 0; i < that.childManagers.length; i++) {\n");
		script.append("                that.childManagers[i].select.clear();\n");
		script.append("            }\n");
		script.append("        }\n");
		script.append("    };\n");
		script.append("    this.detail.impactedObjectsCallback = function(ids) {\n");
		script.append("        that.select.executeGetRequest();\n");
		script.append("    };\n");
		script.append("    this.initialize = function(show) {\n");
		script.append("        this.initializeHTML();\n");
		script.append("        this.select.initialize();\n");
		script.append("        this.detail.initialize();\n");
		script.append("        if (show) {;\n");
		script.append("            this.select.executeGetRequest();\n");
		script.append("            this.show();\n");
		script.append("        } else {\n");
		script.append("            this.hide();\n");
		script.append("        }\n");
		script.append("    };\n");
		script.append("    ZODB.gui.managers[id] = this;\n");
		script.append("};\n");
				
		return script;
	}
	
	protected HTMLFile getPackageManagerHtml() {
		HTMLFile file = new HTMLFile("ZODB - Package manager");
		file.setBodyBgColor(BACKGROUND_COLOR);
		
		file.getScriptFiles().add("ZODB.js");
		file.getStyleFiles().add("ZODB.css");

		StringBuilder header = new StringBuilder();
		header.append("<script type=\"text/javascript\">\n");

		header.append("var ZODB = ZODB || {};\n");
		header.append("ZODB.manager = {};\n");
		header.append("ZODB.manager.parent = new ZODB.gui.Manager(\"packages\",\"" + MdlModel.class.getPackage().getName() + "\",\"" + MdlModel.PACKAGE_CLASS_NAME + "\");\n");
		header.append("ZODB.manager.child = new ZODB.gui.Manager(\"classes\",\"" + MdlModel.class.getPackage().getName() + "\",\"" + MdlModel.CLASS_CLASS_NAME + "\");\n");

		header.append("ZODB.manager.parent.childManagers[0] = ZODB.manager.child;\n");

		header.append("var initialObject = new ZODB.data.DbDataObject();\n");
		header.append("initialObject.propertyValues[\"abstract\"] = \"false\";\n");
		header.append("ZODB.manager.child.detail.initialDataObject = initialObject;\n");
		header.append("ZODB.manager.child.detail.disabledPropertyNameArray = [\"package\"];");
		
		header.append("ZODB.manager.parent.selectedObjectChangedCallback = function(dataObject) {\n");
		header.append("    if (dataObject==null) {\n");
		header.append("        ZODB.dom.hideElementById(\"packageClasses\");\n");
		header.append("    } else {\n");
		header.append("        ZODB.manager.child.select.filterProperty=\"package\";\n");
		header.append("        ZODB.manager.child.select.filterValue=dataObject.getId();\n");
		header.append("        ZODB.manager.child.detail.filterProperty=\"package\";\n");
		header.append("        ZODB.manager.child.detail.filterValue=dataObject.getId();\n");
		header.append("        ZODB.manager.child.detail.setDataObject(null);\n");
		header.append("        ZODB.dom.showElementById(\"packageClasses\");\n");
		header.append("    }\n");
		header.append("};\n");

		header.append("ZODB.manager.parent.detail.impactedObjectsCallback = function(ids) {\n");
		header.append("    ZODB.manager.child.detail.lastSelectRefreshDate = null;\n");
		header.append("    ZODB.manager.parent.select.executeGetRequest();\n");
		header.append("};\n");

		header.append("ZODB.manager.initialize = function(xhttp) {\n");
		header.append("    ZODB.model.getModelCallback(xhttp);\n");
		header.append("    ZODB.manager.parent.initialize(true);\n");
		header.append("    ZODB.manager.child.initialize();\n");
		header.append("};\n");

		header.append("</script>\n");
		file.getHeadElements().add(header.toString());
		
		file.setOnload("ZODB.model.getModel(ZODB.manager.initialize);");

		StringBuilder body = new StringBuilder();
		body.append(getMenuHtmlForPage(""));
		body.append("<div>");
		body.append("\n");
		
		body.append("<div><b>Package manager</b></div>");
		body.append("<div id=\"packages\" style=\"visibility: hidden;\"></div>\n");
		body.append("<br/>");
		body.append("<div id=\"packageClasses\" class=\"boxedDiv\" style=\"visibility: hidden;\">\n");
		body.append("<div><input type=\"button\" value=\"+\" onclick=\"ZODB.manager.child.toggleVisibility(this);\"/>&nbsp;<b>Selected package classes</b></div>");
		body.append("<div id=\"classes\" style=\"visibility: hidden;\"></div>\n");
		body.append("</div>\n");

		body.append("</div>");
		body.append("\n");
		file.getBodyElements().add(body.toString());
		
		return file;
	}

	protected HTMLFile getClassManagerHtml() {
		HTMLFile file = new HTMLFile("ZODB - Class manager");
		file.setBodyBgColor(BACKGROUND_COLOR);
		
		file.getScriptFiles().add("ZODB.js");
		file.getStyleFiles().add("ZODB.css");

		StringBuilder header = new StringBuilder();
		header.append("<script type=\"text/javascript\">\n");

		header.append("var ZODB = ZODB || {};\n");
		header.append("ZODB.manager = {};\n");
		header.append("ZODB.manager.parent = new ZODB.gui.Manager(\"classes\",\"" + MdlModel.class.getPackage().getName() + "\",\"" + MdlModel.CLASS_CLASS_NAME + "\");\n");
		header.append("ZODB.manager.child1 = new ZODB.gui.Manager(\"strings\",\"" + MdlModel.class.getPackage().getName() + "\",\"" + MdlModel.STRING_CLASS_NAME + "\");\n");
		header.append("ZODB.manager.child2 = new ZODB.gui.Manager(\"numbers\",\"" + MdlModel.class.getPackage().getName() + "\",\"" + MdlModel.NUMBER_CLASS_NAME + "\");\n");
		header.append("ZODB.manager.child3 = new ZODB.gui.Manager(\"links\",\"" + MdlModel.class.getPackage().getName() + "\",\"" + MdlModel.LINK_CLASS_NAME + "\");\n");

		header.append("ZODB.manager.parent.childManagers[0] = ZODB.manager.child1;\n");
		header.append("ZODB.manager.parent.childManagers[1] = ZODB.manager.child2;\n");
		header.append("ZODB.manager.parent.childManagers[2] = ZODB.manager.child3;\n");

		header.append("var initialObject = new ZODB.data.DbDataObject();\n");
		header.append("initialObject.propertyValues[\"abstract\"] = \"false\";\n");
		header.append("ZODB.manager.parent.detail.initialDataObject = initialObject;\n");

		header.append("var initialObject = new ZODB.data.DbDataObject();\n");
		header.append("initialObject.propertyValues[\"maxLength\"] = 64;\n");
		header.append("initialObject.propertyValues[\"encode\"] = \"false\";\n");
		header.append("initialObject.propertyValues[\"index\"] = \"false\";\n");
		header.append("ZODB.manager.child1.detail.initialDataObject = initialObject;\n");
		header.append("ZODB.manager.child1.detail.disabledPropertyNameArray = [\"class\"];");
		
		header.append("var initialObject = new ZODB.data.DbDataObject();\n");
		header.append("initialObject.propertyValues[\"minValue\"] = 0;\n");
		header.append("initialObject.propertyValues[\"maxValue\"] = " + Long.MAX_VALUE +";\n");
		header.append("initialObject.propertyValues[\"index\"] = \"false\";\n");
		header.append("ZODB.manager.child2.detail.initialDataObject = initialObject;\n");
		header.append("ZODB.manager.child2.detail.disabledPropertyNameArray = [\"class\"];");
		
		header.append("var initialObject = new ZODB.data.DbDataObject();\n");
		header.append("initialObject.propertyValues[\"maxSize\"] = 1;\n");
		header.append("initialObject.propertyValues[\"index\"] = \"true\";\n");
		header.append("ZODB.manager.child3.detail.initialDataObject = initialObject;\n");
		header.append("ZODB.manager.child3.detail.disabledPropertyNameArray = [\"class\",\"index\"];");

		header.append("ZODB.manager.parent.selectedObjectChangedCallback = function(dataObject) {\n");
		header.append("    if (dataObject==null) {\n");
		header.append("        ZODB.dom.hideElementById(\"classProperties\");\n");
		header.append("    } else {\n");
		header.append("        for (var i = 0; i < this.childManagers.length; i++) {\n");
		header.append("            this.childManagers[i].select.filterProperty=\"class\";\n");
		header.append("            this.childManagers[i].select.filterValue=dataObject.getId();\n");
		header.append("            this.childManagers[i].detail.filterProperty=\"class\";\n");
		header.append("            this.childManagers[i].detail.filterValue=dataObject.getId();\n");
		header.append("            this.childManagers[i].detail.setDataObject(null);\n");
		header.append("        }\n");
		header.append("        ZODB.dom.showElementById(\"classProperties\");\n");
		header.append("    }\n");
		header.append("};\n");

		header.append("ZODB.manager.parent.detail.impactedObjectsCallback = function(ids) {\n");
		header.append("    ZODB.manager.child1.detail.lastSelectRefreshDate = null;\n");
		header.append("    ZODB.manager.child2.detail.lastSelectRefreshDate = null;\n");
		header.append("    ZODB.manager.child3.detail.lastSelectRefreshDate = null;\n");
		header.append("    ZODB.manager.parent.select.executeGetRequest();\n");
		header.append("};\n");
		
		header.append("ZODB.manager.initialize = function(xhttp) {\n");
		header.append("    ZODB.model.getModelCallback(xhttp);\n");
		header.append("    ZODB.manager.parent.initialize(true);\n");
		header.append("    ZODB.manager.child1.initialize();\n");
		header.append("    ZODB.manager.child2.initialize();\n");
		header.append("    ZODB.manager.child3.initialize();\n");
		header.append("};\n");

		header.append("</script>\n");
		file.getHeadElements().add(header.toString());
		
		file.setOnload("ZODB.model.getModel(ZODB.manager.initialize);");

		StringBuilder body = new StringBuilder();
		body.append(getMenuHtmlForPage(""));
		body.append("<div>");
		body.append("\n");
		
		body.append("<div><b>Class manager</b></div>");
		body.append("<div id=\"classes\" style=\"visibility: hidden;\"></div>\n");
		body.append("<br/>");
		body.append("<div id=\"classProperties\" class=\"boxedDiv\" style=\"visibility: hidden;\">\n");
		body.append("<div><input type=\"button\" value=\"+\" onclick=\"ZODB.manager.child1.toggleVisibility(this);\"/>&nbsp;<b>Selected class strings</b></div>");
		body.append("<div id=\"strings\" style=\"visibility: hidden;\"></div>\n");
		body.append("<div><input type=\"button\" value=\"+\" onclick=\"ZODB.manager.child2.toggleVisibility(this);\"/>&nbsp;<b>Selected class numbers</b></div>");
		body.append("<div id=\"numbers\" style=\"visibility: hidden;\"></div>\n");
		body.append("<div><input type=\"button\" value=\"+\" onclick=\"ZODB.manager.child3.toggleVisibility(this);\"/>&nbsp;<b>Selected class links</b></div>");
		body.append("<div id=\"links\" style=\"visibility: hidden;\"></div>\n");
		body.append("</div>\n");

		body.append("</div>");
		body.append("\n");
		file.getBodyElements().add(body.toString());
		
		return file;
	}

	protected HTMLFile getUniqueConstraintManagerHtml() {
		SortedMap<String,String> initialPropertyValues = new TreeMap<String,String>();
		initialPropertyValues.put("caseSensitive","false");
		return getManagerHtmlForPackageClass(
			MdlModel.class.getPackage().getName(),
			MdlModel.UNIQUE_CONSTRAINT_CLASS_NAME,
			"ZODB - Unique constraint manager",
			"Unique constraint manager",
			getMenuHtmlForPage(""),
			null,
			initialPropertyValues,
			null
			);
	}
	
	protected HTMLFile getManagerHtmlForPackageClass(MdlPackage pkg,MdlClass cls) {
		return getManagerHtmlForPackageClass(
			pkg.getName(),
			cls.getName(),
			"ZODB - " + cls.getName(),
			cls.getName(),
			getMenuHtmlForPage("")
			);
	}

	protected HTMLFile getManagerHtmlForPackageClass(String packageName,String className,String pageTitle,String title,StringBuilder menuHtml) {
		return getManagerHtmlForPackageClass(packageName,className,pageTitle,title,menuHtml,null,null,null);
	}

	protected HTMLFile getManagerHtmlForPackageClass(String packageName,String className,String pageTitle,String title,StringBuilder menuHtml, String[] selectPropertyNameArray) {
		return getManagerHtmlForPackageClass(packageName,className,pageTitle,title,menuHtml,selectPropertyNameArray,null,null);
	}

	protected HTMLFile getManagerHtmlForPackageClass(String packageName,String className,String pageTitle,String title,StringBuilder menuHtml, String[] selectPropertyNameArray, SortedMap<String,String> initialPropertyValues, String[] disabledPropertyNameArray) {
		HTMLFile file = new HTMLFile(pageTitle);
		file.setBodyBgColor(BACKGROUND_COLOR);
		
		file.getScriptFiles().add("ZODB.js");
		file.getStyleFiles().add("ZODB.css");

		StringBuilder header = new StringBuilder();
		header.append("<script type=\"text/javascript\">\n");

		header.append("var ZODB = ZODB || {};\n");
		header.append("ZODB.manager = {};\n");
		header.append("ZODB.manager.main = new ZODB.gui.Manager(\"manager\",\"" + packageName + "\",\"" + className + "\");\n");
		
		if (selectPropertyNameArray!=null && selectPropertyNameArray.length>0) {
			StringBuilder props = getPropertyNameArrayAsJavaScriptString(packageName,className,selectPropertyNameArray);
			header.append("ZODB.manager.main.select.propertyNameArray = [");
			header.append(props);
			header.append("];\n");
		}

		if (initialPropertyValues!=null && initialPropertyValues.size()>0) {
			StringBuilder obj = new StringBuilder("var initialObject = new ZODB.data.DbDataObject();\n");
			for (MdlProperty prop: DbConfig.getInstance().getModel().getClassByFullName(packageName + "." + className).getPropertiesExtended()) {
				if (!(prop instanceof MdlLink)) {
					for (Entry<String,String> entry: initialPropertyValues.entrySet()) {
						if (entry.getKey().equals(prop.getName())) {
							obj.append("initialObject.propertyValues[\"");
							obj.append(prop.getName());
							obj.append("\"] = ");
							if (prop instanceof MdlString) {
								obj.append("\"");
							}
							obj.append(entry.getValue());
							if (prop instanceof MdlString) {
								obj.append("\"");
							}
							obj.append(";\n");
						}
					}
				}
			}
			header.append(obj);
			header.append("ZODB.manager.main.detail.initialDataObject = initialObject;\n");
		}

		if (disabledPropertyNameArray!=null && disabledPropertyNameArray.length>0) {
			StringBuilder props = getArrayAsJavaScriptString(disabledPropertyNameArray);
			header.append("ZODB.manager.main.detail.disabledPropertyNameArray = [");
			header.append(props);
			header.append("];\n");
		}
		
		header.append("ZODB.manager.initialize = function(xhttp) {\n");
		header.append("    ZODB.model.getModelCallback(xhttp);\n");
		header.append("    ZODB.manager.main.initialize(true);\n");
		header.append("};\n");

		header.append("</script>\n");
		file.getHeadElements().add(header.toString());
		
		file.setOnload("ZODB.model.getModel(ZODB.manager.initialize);");

		StringBuilder body = new StringBuilder();
		body.append(menuHtml);
		body.append("<div>");
		body.append("\n");
		
		body.append("<div><b>");
		body.append(title);
		body.append("</b></div>");

		body.append("<div id=\"manager\" style=\"visibility: hidden;\"></div>\n");

		body.append("</div>");
		body.append("\n");
		file.getBodyElements().add(body.toString());
		
		return file;
	}

	protected StringBuilder getPropertyNameArrayAsJavaScriptString(String packageName, String className,String[] propertyNameArray) {
		StringBuilder props = new StringBuilder();
		props.append(getArrayAsJavaScriptString(propertyNameArray));
		for (MdlProperty prop: DbConfig.getInstance().getModel().getClassByFullName(packageName + "." + className).getPropertiesExtended()) {
			boolean found = false;
			for (String propName: propertyNameArray) {
				if (propName.equals(prop.getName())) {
					found = true;
					break;
				}
			}
			if (!found) {
				if (props.length()>0) {
					props.append(",");
				}
				props.append("\"");
				props.append(prop.getName());
				props.append("\"");
			}
		}
		return props;
	}

	protected StringBuilder getArrayAsJavaScriptString(String[] array) {
		StringBuilder r = new StringBuilder();
		for (String propName: array) {
			if (r.length()>0) {
				r.append(",");
			}
			r.append("\"");
			r.append(propName);
			r.append("\"");
		}
		return r;
	}
	
	protected HTMLFile getModelManagerHtml() {
		HTMLFile file = new HTMLFile("ZODB - Model manager");
		file.setBodyBgColor(BACKGROUND_COLOR);
		
		file.getScriptFiles().add("ZODB.js");
		file.getStyleFiles().add("ZODB.css");

		StringBuilder header = new StringBuilder();
		header.append("<script type=\"text/javascript\">\n");

		header.append("var ZODB = ZODB || {};\n");
		header.append("ZODB.database = {};\n");

		header.append("ZODB.database.initializeCallback = function(xhttp) {\n");
		header.append("    ZODB.dom.hideElementById(\"refreshingPanel\");\n");
		header.append("    var xmlDoc = xhttp.responseXML;\n");
		header.append("    var vElems = xmlDoc.getElementsByTagName(\"value\");\n");
		header.append("    for (var v = 0; v < vElems.length; v++) {\n");
		header.append("        if (vElems[v].childNodes[0].nodeValue==\"true\") {\n");
		header.append("            ZODB.ajax.executeXMLHttpRequest(\"GET\",\"/modelChanges.xml\",\"\",ZODB.database.modelChangesCallback,ZODB.gui.genericResponseCallback);\n");
		header.append("            ZODB.dom.showElementById(\"updatePanel\");\n");
		header.append("            ZODB.dom.hideElementById(\"refreshPanel\");\n");
		header.append("        } else {\n");
		header.append("            ZODB.dom.hideElementById(\"updatePanel\");\n");
		header.append("            ZODB.dom.showElementById(\"refreshPanel\");\n");
		header.append("        }\n");
		header.append("        break;\n");
		header.append("    }\n");
		header.append("};\n");

		header.append("ZODB.database.modelChangesCallback = function(xhttp) {\n");
		header.append("    var xmlDoc = xhttp.responseXML;\n");
		header.append("    var vElems = xmlDoc.getElementsByTagName(\"value\");\n");
		header.append("    for (var v = 0; v < vElems.length; v++) {\n");
		header.append("        if (vElems[v].childNodes[0].nodeValue.length>0) {\n");
		header.append("            ZODB.dom.showElementById(\"changesPanel\");\n");
		header.append("            ZODB.dom.showElementById(\"changes\");\n");
		header.append("            ZODB.dom.setDivInnerHTMLByElementId(\"changes\",vElems[v].childNodes[0].nodeValue);\n");
		header.append("        } else {\n");
		header.append("            ZODB.dom.hideElementById(\"changesPanel\");\n");
		header.append("            ZODB.dom.hideElementById(\"changes\");\n");
		header.append("        }\n");
		header.append("        break;\n");
		header.append("    }\n");
		header.append("};\n");
		
		header.append("ZODB.database.actionCallback = function(xhttp) {\n");
		header.append("    ZODB.gui.genericResponseCallback(xhttp);\n");
		header.append("    ZODB.dom.setInputDisabledByElementId(\"updateButton\",false);\n");
		header.append("    ZODB.dom.setInputDisabledByElementId(\"revertButton\",false);\n");
		header.append("    ZODB.database.initialize();\n");
		header.append("};\n");
		
		header.append("ZODB.database.update = function() {\n");
		header.append("    update = ZODB.dom.getInputCheckedByElementId(\"updateconfirm\");\n");
		header.append("    if (!update) {\n");
		header.append("        update = confirm(\"Are you sure you want to update abstract model?\");\n");
		header.append("    }\n");
		header.append("    if (update) {\n");
		header.append("        ZODB.dom.setInputDisabledByElementId(\"updateButton\",true);\n");
		header.append("        ZODB.dom.setInputDisabledByElementId(\"revertButton\",true);\n");
		header.append("        ZODB.ajax.executeXMLHttpRequest(\"POST\",\"/updateModel.xml\",\"\",ZODB.database.actionCallback,ZODB.gui.genericResponseCallback);\n");
		header.append("    }\n");
		header.append("};\n");

		header.append("ZODB.database.revert = function() {\n");
		header.append("    revert = ZODB.dom.getInputCheckedByElementId(\"revertconfirm\");\n");
		header.append("    if (!revert) {\n");
		header.append("        revert = confirm(\"Are you sure you want to revert changes to the database model?\");\n");
		header.append("    }\n");
		header.append("    if (revert) {\n");
		header.append("        ZODB.dom.setInputDisabledByElementId(\"updateButton\",true);\n");
		header.append("        ZODB.dom.setInputDisabledByElementId(\"revertButton\",true);\n");
		header.append("        ZODB.ajax.executeXMLHttpRequest(\"POST\",\"/revertModel.xml\",\"\",ZODB.database.actionCallback,ZODB.gui.genericResponseCallback);\n");
		header.append("    }\n");
		header.append("};\n");

		header.append("ZODB.database.initialize = function() {\n");
		header.append("    ZODB.dom.hideElementById(\"updatePanel\");\n");
		header.append("    ZODB.dom.hideElementById(\"refreshPanel\");\n");
		header.append("    ZODB.dom.showElementById(\"refreshingPanel\");\n");
		header.append("    ZODB.ajax.executeXMLHttpRequest(\"GET\",\"/modelChanged.xml\",\"\",ZODB.database.initializeCallback,ZODB.gui.genericResponseCallback);\n");
		header.append("};\n");
		
		header.append("</script>\n");
		file.getHeadElements().add(header.toString());
		
		file.setOnload("ZODB.database.initialize();");
		
		StringBuilder body = new StringBuilder();
		body.append(getMenuHtmlForPage(""));
		body.append("<div>");
		body.append("\n");

		body.append("<div><b>Model manager</b></div>");
		body.append("\n");

		body.append("<div id=\"updatePanel\" style=\"visibility: hidden;\">");
		body.append("\n");
		body.append("Click on 'Update' to update the abstract model.<br/>");
		body.append("\n");
		body.append("<input id=\"updateButton\" type=\"button\" value=\"Update\" onclick=\"ZODB.database.update();\"/><input id=\"updateconfirm\" type=\"checkbox\"/><br/>");
		body.append("\n");
		body.append("Click on 'Revert' to revert the database model changes.<br/>");
		body.append("\n");
		body.append("<input id=\"revertButton\" type=\"button\" value=\"Revert\" onclick=\"ZODB.database.revert();\"/><input id=\"revertconfirm\" type=\"checkbox\"/><br/>");
		body.append("\n");
		body.append("<br/>");
		body.append("\n");
		body.append("<div id=\"changesPanel\" style=\"visibility: hidden;\"></div>");
		body.append("\n");
		body.append("<b>Changes</b>");
		body.append("\n");
		body.append("<div id=\"changes\" style=\"visibility: hidden;\"></div>");
		body.append("\n");
		body.append("</div>");
		body.append("\n");

		body.append("<div id=\"refreshPanel\" style=\"visibility: hidden;\">");
		body.append("\n");
		body.append("The abstract data model matches the database data model.<br/>");
		body.append("\n");
		body.append("<input type=\"button\" value=\"Refresh\" onclick=\"ZODB.database.initialize();\"/><br/>");
		body.append("\n");
		body.append("</div>");
		body.append("\n");

		body.append("<div id=\"refreshingPanel\">");
		body.append("\n");
		body.append("Busy checking if the abstract data model matches the database data model ...<br/>");
		body.append("\n");
		body.append("</div>");
		body.append("\n");
		
		body.append("</div>");
		body.append("\n");
		file.getBodyElements().add(body.toString());
		
		return file;
	}
	
	protected StringBuilder getAuthorizationJavaScript() {
		StringBuilder script = new StringBuilder();
		
		script.append("var ZODB = ZODB || {};\n");
		script.append("ZODB.authorization = {};\n");
		
		script.append("ZODB.authorization.initializeCallback = function(xhttp) {\n");
		script.append("    ZODB.dom.hideElementById(\"refreshingPanel\");\n");
		script.append("    var xmlDoc = xhttp.responseXML;\n");
		script.append("    var vElems = xmlDoc.getElementsByTagName(\"value\");\n");
		script.append("    for (var v = 0; v < vElems.length; v++) {\n");
		script.append("        if (vElems[v].childNodes[0].nodeValue==\"true\") {\n");
		script.append("            ZODB.dom.showElementById(\"authorizedPanel\");\n");
		script.append("            ZODB.dom.hideElementById(\"notAuthorizedPanel\");\n");
		script.append("        } else {\n");
		script.append("            ZODB.dom.hideElementById(\"authorizedPanel\");\n");
		script.append("            ZODB.dom.showElementById(\"notAuthorizedPanel\");\n");
		script.append("        }\n");
		script.append("        break;\n");
		script.append("    }\n");
		script.append("    ZODB.dom.focusInputByElementId(\"authorizePassword\");\n");
		script.append("};\n");
		
		script.append("ZODB.authorization.getAuthXML = function(pwd,upwd) {\n");
		script.append("    var auth = \"\";\n");
		script.append("    auth += \"<authorize>\";\n");
		script.append("    auth += \"<password>\";\n");
		script.append("    auth += pwd;\n");
		script.append("    auth += \"</password>\";\n");
		script.append("    if (upwd) {\n");
		script.append("        auth += \"<updatePassword>\";\n");
		script.append("        auth += upwd;\n");
		script.append("        auth += \"</updatePassword>\";\n");
		script.append("    };\n");
		script.append("    auth += \"</authorize>\";\n");
		script.append("    return auth;\n");
		script.append("};\n");

		script.append("ZODB.authorization.authorizeCallback = function(xhttp) {\n");
		script.append("    ZODB.gui.genericResponseCallback(xhttp);\n");
		script.append("    ZODB.dom.setInputDisabledByElementId(\"authorizeButton\",false);\n");
		script.append("    ZODB.dom.setInputDisabledByElementId(\"authorizeUpdateButton\",false);\n");
		script.append("    ZODB.authorization.initialize();\n");
		script.append("};\n");

		script.append("ZODB.authorization.authorize = function() {\n");
		script.append("    ZODB.dom.setInputDisabledByElementId(\"authorizeButton\",true);\n");
		script.append("    ZODB.dom.setInputDisabledByElementId(\"authorizeUpdateButton\",true);\n");
		script.append("    var pwd = ZODB.dom.getInputValueByElementId(\"authorizePassword\");\n");
		script.append("    ZODB.ajax.executeXMLHttpRequest(\"POST\",\"/authorizationManager.xml\",ZODB.authorization.getAuthXML(pwd),ZODB.authorization.authorizeCallback);\n");
		script.append("};\n");

		script.append("ZODB.authorization.update = function() {\n");
		script.append("    var upwd = ZODB.dom.getInputValueByElementId(\"authorizeUpdatePassword\");\n");
		script.append("    var upwdr = ZODB.dom.getInputValueByElementId(\"authorizeUpdatePasswordRepeat\");\n");
		script.append("    if (upwd!=upwdr) {\n");
		script.append("        alert(\"The new password repeat does not match the new password\");\n");
		script.append("        return;\n");
		script.append("    }\n");
		script.append("    update = ZODB.dom.getInputCheckedByElementId(\"updateconfirm\");\n");
		script.append("    if (!update) {\n");
		script.append("        update = confirm(\"Are you sure you want to update the authorization password?\");\n");
		script.append("    }\n");
		script.append("    if (update) {\n");
		script.append("        ZODB.dom.setInputDisabledByElementId(\"authorizeButton\",true);\n");
		script.append("        ZODB.dom.setInputDisabledByElementId(\"authorizeUpdateButton\",true);\n");
		script.append("        var pwd = ZODB.dom.getInputValueByElementId(\"authorizePassword\");\n");
		script.append("        ZODB.ajax.executeXMLHttpRequest(\"POST\",\"/authorizationManager.xml\",ZODB.authorization.getAuthXML(pwd,upwd),ZODB.authorization.authorizeCallback);\n");
		script.append("    }\n");
		script.append("};\n");

		script.append("ZODB.authorization.initialize = function() {\n");
		script.append("    ZODB.dom.hideElementById(\"authorizedPanel\");\n");
		script.append("    ZODB.dom.hideElementById(\"notAuthorizedPanel\");\n");
		script.append("    ZODB.dom.showElementById(\"refreshingPanel\");\n");
		script.append("    var sessionid = ZODB.cookie.getCookieValue(\"sessionid\");\n");
		script.append("    if (sessionid==null || sessionid==\"\") {\n");
		script.append("        ZODB.dom.hideElementById(\"authorizePanel\");\n");
		script.append("        ZODB.dom.showElementById(\"nosessionPanel\");\n");
		script.append("    } else {\n");
		script.append("        ZODB.ajax.executeXMLHttpRequest(\"GET\",\"/sessionAuthorized.xml\",\"\",ZODB.authorization.initializeCallback);\n");
		script.append("        ZODB.dom.showElementById(\"authorizePanel\");\n");
		script.append("        ZODB.dom.hideElementById(\"nosessionPanel\");\n");
		script.append("        ZODB.dom.focusInputByElementId(\"authorizePassword\");\n");
		script.append("    }\n");
		script.append("    ZODB.dom.bindEnterFunctionToElementId(\"authorizePassword\",ZODB.authorization.authorize);\n");
		script.append("    ZODB.dom.bindEnterFunctionToElementId(\"authorizeUpdatePasswordRepeat\",ZODB.authorization.update);\n");
		script.append("};\n");
		
		return script;
	}
	
	protected HTMLFile getAuthorizationManagerHtml() {
		HTMLFile file = new HTMLFile("ZODB - Authorization manager");
		file.setBodyBgColor(BACKGROUND_COLOR);

		file.getScriptFiles().add("ZODB.js");
		file.getStyleFiles().add("ZODB.css");

		file.setOnload("ZODB.authorization.initialize();");

		StringBuilder body = new StringBuilder();
		body.append(getMenuHtmlForPage(""));
		body.append("<div>");
		body.append("\n");
		
		body.append("<div><b>Authorization manager</b></div>\n");
		body.append("<div id=\"authorizePanel\" style=\"visibility: hidden;\">\n");
		body.append("<div id=\"refreshingPanel\" style=\"visibility: hidden;\">\n");
		body.append("Busy checking if current session is authorized ...<br/>\n");
		body.append("</div>\n");
		body.append("<div id=\"authorizedPanel\" style=\"visibility: hidden;\">\n");
		body.append("Your current session is <b>authorized</b>.<br/>\n");
		body.append("</div>\n");
		body.append("<div id=\"notAuthorizedPanel\" style=\"visibility: hidden;\">\n");
		body.append("Your current session is <b>not authorized</b>.<br/>\n");
		body.append("</div>\n");
		body.append("Enter the authorization password and click 'Authorize' to authorize your session for database data access.<br/>\n");
		body.append("<input type=\"password\" id=\"authorizePassword\" autocomplete=\"off\" tabindex=1 autofocus/>&nbsp;(The default password is 'admin')<br/>");
		body.append("<input type=\"button\" id=\"authorizeButton\" value=\"Authorize\" tabindex=2 onclick=\"ZODB.authorization.authorize();\"/>");
		body.append("<br/>\n");
		body.append("Enter the authorization password and a new authorization password (twice) and click 'Update' to update the current password.<br/>\n");
		body.append("<input type=\"password\" id=\"authorizeUpdatePassword\" autocomplete=\"off\" tabindex=3/><br/>");
		body.append("<input type=\"password\" id=\"authorizeUpdatePasswordRepeat\" autocomplete=\"off\" tabindex=4/>&nbsp;(repeat)<br/>");
		body.append("<input type=\"button\" id=\"authorizeUpdateButton\" value=\"Update\" onclick=\"ZODB.authorization.update();\" tabindex=5/><input id=\"updateconfirm\" type=\"checkbox\" tabindex=6/>");
		body.append("</div>\n");
		body.append("<div id=\"nosessionPanel\" style=\"visibility: hidden;\">\n");
		body.append("No session to authorize.");
		body.append("</div>\n");

		body.append("</div>");
		body.append("\n");
		file.getBodyElements().add(body.toString());
		
		return file;
	}

	protected String getAuthorizerTitle() {
		return "ZODB - Authorizer";
	}

	protected StringBuilder getAuthorizerMenuHtml() {
		return getMenuHtmlForPage("");
	}

	protected HTMLFile getAuthorizerHtml() {
		HTMLFile file = new HTMLFile(getAuthorizerTitle());
		file.setBodyBgColor(BACKGROUND_COLOR);
		
		file.getScriptFiles().add("ZODB.js");
		file.getStyleFiles().add("ZODB.css");
		
		file.setOnload("ZODB.authorization.initialize();");

		StringBuilder body = new StringBuilder();
		body.append(getAuthorizerMenuHtml());
		body.append("<div>");
		body.append("\n");
		
		body.append("<div><b>Authorizer</b></div>\n");
		body.append("<div id=\"authorizePanel\" style=\"visibility: hidden;\">\n");
		body.append("<div id=\"refreshingPanel\" style=\"visibility: hidden;\">\n");
		body.append("Busy checking if current session is authorized ...<br/>\n");
		body.append("</div>\n");
		body.append("<div id=\"authorizedPanel\" style=\"visibility: hidden;\">\n");
		body.append("Your current session is <b>authorized</b>.<br/>\n");
		body.append("</div>\n");
		body.append("<div id=\"notAuthorizedPanel\" style=\"visibility: hidden;\">\n");
		body.append("Your current session is <b>not authorized</b>.<br/>\n");
		body.append("Enter the authorization password and click 'Authorize' to authorize your session for database data access.<br/>\n");
		body.append("<input type=\"password\" id=\"authorizePassword\" autocomplete=\"off\" tabindex=1 autofocus/>&nbsp;(The default password is 'admin')<br/>");
		body.append("<input type=\"button\" id=\"authorizeButton\" value=\"Authorize\" onclick=\"ZODB.authorization.authorize();\" tabindex=2/>");
		body.append("</div>\n");
		body.append("</div>\n");
		body.append("<div id=\"nosessionPanel\" style=\"visibility: hidden;\">\n");
		body.append("No session to authorize.");
		body.append("</div>\n");

		body.append("</div>");
		body.append("\n");
		file.getBodyElements().add(body.toString());
		
		return file;
	}
	
	protected StringBuilder getCSS() {
		StringBuilder css = new StringBuilder();

		css.append("table, th, td {\n");
		css.append("    border: 1px solid black;\n");
		css.append("    border-collapse: collapse;\n");
		css.append("}\n");
		css.append("table {\n");
		css.append("    width: 100%;\n");
		css.append("}\n");
		css.append("th, td {\n");
		css.append("    padding: 1px;\n");
		css.append("}\n");
		css.append("textarea {\n");
		css.append("    width: 95%;\n");
		css.append("    height: 100px;\n");
		css.append("}\n");
		css.append(".textInput {\n");
		css.append("    width: 95%;\n");
		css.append("}\n");
		css.append(".boxedDiv {\n");
		css.append("    border: 1px solid black;\n");
		css.append("    border-collapse: collapse;\n");
		css.append("    padding: 10px;\n");
		css.append("}\n");

		css.append("@media (max-width: 320px) {\n");
		css.append("    .notVital {\n");
		css.append("        visibility: hidden;\n");
		css.append("        display: none;\n");
		css.append("    }\n");
		css.append("}\n");

		css.append("@media (max-width: 640px) {\n");
		css.append("    .notImportant {\n");
		css.append("        visibility: hidden;\n");
		css.append("        display: none;\n");
		css.append("    }\n");
		css.append("}\n");
				
		return css;
	}
}
