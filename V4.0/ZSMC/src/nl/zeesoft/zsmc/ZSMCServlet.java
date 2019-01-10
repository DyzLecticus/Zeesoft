package nl.zeesoft.zsmc;

import javax.servlet.annotation.WebServlet;

import nl.zeesoft.zodb.Config;
import nl.zeesoft.zodb.ZODBServlet;

@WebServlet("/ZSMCServlet")
public class ZSMCServlet extends ZODBServlet {
	private static final long			serialVersionUID	= 1L;
	
	@Override
	protected Config getNewConfig() {
		return new ZSMCConfig();
	}
}
