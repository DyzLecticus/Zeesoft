package nl.zeesoft.zevt;

import javax.servlet.annotation.WebServlet;

import nl.zeesoft.zodb.Config;
import nl.zeesoft.zodb.ZODBServlet;

@WebServlet("/ZEVTServlet")
public class ZEVTServlet extends ZODBServlet {
	private static final long			serialVersionUID	= 1L;
	
	@Override
	protected Config getNewConfig() {
		return new ZEVTConfig();
	}
}
