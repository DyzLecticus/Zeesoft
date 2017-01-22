package nl.zeesoft.zdsm.database.model;

import nl.zeesoft.zodb.database.DbDataObject;
import nl.zeesoft.zodb.database.model.helpers.HlpObject;

public class Domain extends HlpObject {
	public static final String	SELF					= "Self";
	
	private String 				name 					= "";
	private String 				address 				= "";
	private int					port					= 80;
	private int					checkProcessSeconds		= 900;
	private int					checkServiceSeconds		= 600;
	private int					keepProcessLogDays		= 90;
	private int					keepResponseDays		= 30;
	private boolean				active					= true;
	private boolean				removeMe				= false;

	private boolean				https					= false;
	private boolean				addDomainToPath			= true;
	private boolean				addHeaderHost			= true;
	private boolean				addHeaderHostPort		= true;
	private boolean				addHeaderAuthBasic		= false;
	private String 				userName				= "";
	private String 				userPassword			= "";
	
	@Override
	public void fromDataObject(DbDataObject obj) {
		super.fromDataObject(obj);
		if (obj.hasPropertyValue("name")) {
			setName(obj.getPropertyValue("name").toString());
		}
		if (obj.hasPropertyValue("address")) {
			setAddress(obj.getPropertyValue("address").toString());
		}
		if (obj.hasPropertyValue("port")) {
			setPort(Integer.parseInt(obj.getPropertyValue("port").toString()));
		}
		if (obj.hasPropertyValue("checkProcessSeconds")) {
			setCheckProcessSeconds(Integer.parseInt(obj.getPropertyValue("checkProcessSeconds").toString()));
		}
		if (obj.hasPropertyValue("checkServiceSeconds")) {
			setCheckServiceSeconds(Integer.parseInt(obj.getPropertyValue("checkServiceSeconds").toString()));
		}
		if (obj.hasPropertyValue("keepProcessLogDays")) {
			setKeepProcessLogDays(Integer.parseInt(obj.getPropertyValue("keepProcessLogDays").toString()));
		}
		if (obj.hasPropertyValue("keepResponseDays")) {
			setKeepResponseDays(Integer.parseInt(obj.getPropertyValue("keepResponseDays").toString()));
		}
		if (obj.hasPropertyValue("https")) {
			setHttps(Boolean.parseBoolean(obj.getPropertyValue("https").toString()));
		}
		if (obj.hasPropertyValue("addDomainToPath")) {
			setAddDomainToPath(Boolean.parseBoolean(obj.getPropertyValue("addDomainToPath").toString()));
		}
		if (obj.hasPropertyValue("addHeaderHost")) {
			setAddHeaderHost(Boolean.parseBoolean(obj.getPropertyValue("addHeaderHost").toString()));
		}
		if (obj.hasPropertyValue("addHeaderHostPort")) {
			setAddHeaderHostPort(Boolean.parseBoolean(obj.getPropertyValue("addHeaderHostPort").toString()));
		}
		if (obj.hasPropertyValue("addHeaderAuthBasic")) {
			setAddHeaderAuthBasic(Boolean.parseBoolean(obj.getPropertyValue("addHeaderAuthBasic").toString()));
		}
		if (obj.hasPropertyValue("userName")) {
			setUserName(obj.getPropertyValue("userName").toString());
		}
		if (obj.hasPropertyValue("userPassword")) {
			setUserPassword(obj.getPropertyValue("userPassword").toString());
		}
		if (obj.hasPropertyValue("active")) {
			setActive(Boolean.parseBoolean(obj.getPropertyValue("active").toString()));
		}
		if (obj.hasPropertyValue("removeMe")) {
			setRemoveMe(Boolean.parseBoolean(obj.getPropertyValue("removeMe").toString()));
		}
	}

	@Override
	public DbDataObject toDataObject() {
		DbDataObject r = super.toDataObject();
		r.setPropertyValue("name",new StringBuilder(getName()));
		r.setPropertyValue("address",new StringBuilder(getAddress()));
		r.setPropertyValue("port",new StringBuilder("" + getPort()));
		r.setPropertyValue("checkProcessSeconds",new StringBuilder("" + getCheckProcessSeconds()));
		r.setPropertyValue("checkServiceSeconds",new StringBuilder("" + getCheckServiceSeconds()));
		r.setPropertyValue("keepProcessLogDays",new StringBuilder("" + getKeepProcessLogDays()));
		r.setPropertyValue("keepResponseDays",new StringBuilder("" + getKeepResponseDays()));
		r.setPropertyValue("https",new StringBuilder("" + isHttps()));
		r.setPropertyValue("addDomainToPath",new StringBuilder("" + isAddDomainToPath()));
		r.setPropertyValue("addHeaderHost",new StringBuilder("" + isAddHeaderHost()));
		r.setPropertyValue("addHeaderHostPort",new StringBuilder("" + isAddHeaderHostPort()));
		r.setPropertyValue("addHeaderAuthBasic",new StringBuilder("" + isAddHeaderAuthBasic()));
		r.setPropertyValue("userName",new StringBuilder(getUserName()));
		r.setPropertyValue("userPassword",new StringBuilder(getUserPassword()));
		r.setPropertyValue("active",new StringBuilder("" + isActive()));
		r.setPropertyValue("removeMe",new StringBuilder("" + isRemoveMe()));
		return r;
	}
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * @param address the address to set
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * @return the checkProcessSeconds
	 */
	public int getCheckProcessSeconds() {
		return checkProcessSeconds;
	}

	/**
	 * @param checkProcessSeconds the checkProcessSeconds to set
	 */
	public void setCheckProcessSeconds(int checkProcessSeconds) {
		this.checkProcessSeconds = checkProcessSeconds;
	}

	/**
	 * @return the active
	 */
	public boolean isActive() {
		return active;
	}

	/**
	 * @param active the active to set
	 */
	public void setActive(boolean active) {
		this.active = active;
	}

	/**
	 * @return the removeMe
	 */
	public boolean isRemoveMe() {
		return removeMe;
	}

	/**
	 * @param removeMe the removeMe to set
	 */
	public void setRemoveMe(boolean removeMe) {
		this.removeMe = removeMe;
	}

	/**
	 * @return the port
	 */
	public int getPort() {
		return port;
	}

	/**
	 * @param port the port to set
	 */
	public void setPort(int port) {
		this.port = port;
	}

	/**
	 * @return the checkServiceSeconds
	 */
	public int getCheckServiceSeconds() {
		return checkServiceSeconds;
	}

	/**
	 * @param checkServiceSeconds the checkServiceSeconds to set
	 */
	public void setCheckServiceSeconds(int checkServiceSeconds) {
		this.checkServiceSeconds = checkServiceSeconds;
	}

	/**
	 * @return the keepProcessLogDays
	 */
	public int getKeepProcessLogDays() {
		return keepProcessLogDays;
	}

	/**
	 * @param keepProcessLogDays the keepProcessLogDays to set
	 */
	public void setKeepProcessLogDays(int keepProcessLogDays) {
		this.keepProcessLogDays = keepProcessLogDays;
	}

	/**
	 * @return the keepResponseDays
	 */
	public int getKeepResponseDays() {
		return keepResponseDays;
	}

	/**
	 * @param keepResponseDays the keepResponseDays to set
	 */
	public void setKeepResponseDays(int keepResponseDays) {
		this.keepResponseDays = keepResponseDays;
	}

	/**
	 * @return the https
	 */
	public boolean isHttps() {
		return https;
	}

	/**
	 * @param https the https to set
	 */
	public void setHttps(boolean https) {
		this.https = https;
	}

	/**
	 * @return the addHeaderAuthBasic
	 */
	public boolean isAddHeaderAuthBasic() {
		return addHeaderAuthBasic;
	}

	/**
	 * @param addHeaderAuthBasic the addHeaderAuthBasic to set
	 */
	public void setAddHeaderAuthBasic(boolean addHeaderAuthBasic) {
		this.addHeaderAuthBasic = addHeaderAuthBasic;
	}

	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * @param userName the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * @return the userPassword
	 */
	public String getUserPassword() {
		return userPassword;
	}

	/**
	 * @param userPassword the userPassword to set
	 */
	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}

	/**
	 * @return the addDomainToPath
	 */
	public boolean isAddDomainToPath() {
		return addDomainToPath;
	}

	/**
	 * @param addDomainToPath the addDomainToPath to set
	 */
	public void setAddDomainToPath(boolean addDomainToPath) {
		this.addDomainToPath = addDomainToPath;
	}

	/**
	 * @return the addHeaderHost
	 */
	public boolean isAddHeaderHost() {
		return addHeaderHost;
	}

	/**
	 * @param addHeaderHost the addHeaderHost to set
	 */
	public void setAddHeaderHost(boolean addHeaderHost) {
		this.addHeaderHost = addHeaderHost;
	}

	/**
	 * @return the addHeaderHostPort
	 */
	public boolean isAddHeaderHostPort() {
		return addHeaderHostPort;
	}

	/**
	 * @param addHeaderHostPort the addHeaderHostPort to set
	 */
	public void setAddHeaderHostPort(boolean addHeaderHostPort) {
		this.addHeaderHostPort = addHeaderHostPort;
	}
}
