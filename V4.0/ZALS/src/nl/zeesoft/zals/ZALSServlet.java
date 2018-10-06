package nl.zeesoft.zals;

import javax.servlet.annotation.WebServlet;

import nl.zeesoft.zodb.Config;
import nl.zeesoft.zodb.ZODBServlet;

@WebServlet("/ZALSServlet")
public class ZALSServlet extends ZODBServlet {
	private static final long			serialVersionUID	= 1L;
	
	@Override
	protected Config getNewConfig() {
		return new ZALSConfig();
	}
}
