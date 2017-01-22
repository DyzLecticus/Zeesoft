package nl.zeesoft.zodb.database;

import nl.zeesoft.zodb.Messenger;
import nl.zeesoft.zodb.Worker;

public final class DbServerSessionWorker extends Worker {
	private DbServerSession serverSession = null;
	
	public DbServerSessionWorker(DbServerSession s) {
		serverSession = s;
		setSleep(1);
	}
	
	@Override
	public void start() {
		Messenger.getInstance().debug(this, "Starting server session...");
		if (serverSession.open()) {
			super.start();
			Messenger.getInstance().debug(this, "Started server session: " + serverSession.getSession().getId());
		} else {
			Messenger.getInstance().error(this, "Unable to open server session");
		}
    }

	@Override
    public void stop() {
		Messenger.getInstance().debug(this, "Stopping server session: " + serverSession.getSession().getId());
		super.stop();
		serverSession.close();
		Messenger.getInstance().debug(this, "Stopped server session: " + serverSession.getSession().getId());
    }
	
	@Override
	public void whileWorking() {
		serverSession.readAndProcessInput();
	}

	/**
	 * @return the serverSession
	 */
	public DbServerSession getServerSession() {
		return serverSession;
	}
}
