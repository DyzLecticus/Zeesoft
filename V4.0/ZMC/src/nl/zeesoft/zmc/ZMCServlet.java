package nl.zeesoft.zmc;

import javax.servlet.annotation.WebServlet;

import nl.zeesoft.zodb.Config;
import nl.zeesoft.zodb.ZODBServlet;

@WebServlet("/ZMCServlet")
public class ZMCServlet extends ZODBServlet {
	private static final long			serialVersionUID	= 1L;
	
	@Override
	protected Config getNewConfig() {
		return new ZMCConfig();
	}
}
