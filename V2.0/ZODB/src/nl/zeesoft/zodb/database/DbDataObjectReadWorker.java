package nl.zeesoft.zodb.database;

import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;

import nl.zeesoft.zodb.Messenger;
import nl.zeesoft.zodb.Worker;
import nl.zeesoft.zodb.database.model.MdlClass;
import nl.zeesoft.zodb.file.FileIO;
import nl.zeesoft.zodb.file.xml.XMLConfig;
import nl.zeesoft.zodb.file.xml.XMLFile;

public final class DbDataObjectReadWorker extends Worker {
	private String				className		= null;
	private List<Long>			idList			= new ArrayList<Long>();
	private List<Long>			readIdList		= null;
	private List<DbDataObject>	objects			= new ArrayList<DbDataObject>();
	private boolean				done			= false;
	private DocumentBuilder 	builder			= null;
	
	private MdlClass			cls				= null;
	
	protected DbDataObjectReadWorker(String className) {
		setSleep(0);
		this.className = className;
		cls = DbConfig.getInstance().getModel().getClassByFullName(className);
	}
	
	@Override
	public void start() {
		readIdList = null;
		objects.clear();
		if (idList.size()>0) {
			builder = XMLConfig.getInstance().getBuilderForSource(this);
			readIdList = new ArrayList<Long>(idList);
			super.start();
		} else {
			done = true;
		}
    }
	
	@Override
    public void stop() {
		super.stop();
		XMLConfig.getInstance().removeBuilderForSource(this);
		done = true;
    }
	
	@Override
	public void whileWorking() {
		if (readIdList.size()>0) {
			long id = readIdList.remove(0);
			String fileName = DbConfig.getInstance().getDataDir() + className + "/" + id + ".xml";
			DbDataObject object = new DbDataObject();
			if (FileIO.fileExists(fileName)) {
				XMLFile xml = new XMLFile(builder);
				String err = xml.parseFile(fileName,true);
				if (err.length()==0) {
					object.fromXML(xml,cls,true);
				} else {
					Messenger.getInstance().error(this,"Database has been corrupted. See file: " + fileName + ", error:\n" + err);
				}
				xml.cleanUp();
			} else {
				Messenger.getInstance().error(this,"Database has been corrupted. Missing object file: " + fileName);
			}
			if (object.getId()==0) {
				object.setId(id);
			}
			objects.add(object);
		}
		if (readIdList.size()==0) {
			stop();
		}
	}

	/**
	 * @return the idList
	 */
	protected List<Long> getIdList() {
		return idList;
	}
	
	/**
	 * @return the objects
	 */
	protected List<DbDataObject> getObjects() {
		return objects;
	}

	/**
	 * @return the done
	 */
	protected boolean isDone() {
		return done;
	}

}	
