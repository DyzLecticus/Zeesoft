package nl.zeesoft.zbe;

import javax.servlet.annotation.WebServlet;

import nl.zeesoft.zodb.Config;
import nl.zeesoft.zodb.ZODBServlet;

@WebServlet("/ZBEServlet")
public class ZBEServlet extends ZODBServlet {
	private static final long			serialVersionUID	= 1L;
	
	@Override
	protected Config getNewConfig() {
		return new ZBEConfig();
	}
}
