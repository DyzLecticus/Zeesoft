package nl.zeesoft.zjmo.orchestra;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zjmo.json.JsElem;
import nl.zeesoft.zjmo.json.JsFile;

public class Orchestra {
	public static final String		CONDUCTOR		= "Conductor";
	public static final String		CONDUCTORID		= "Conductor/0";
	
	private List<Position> 			positions		= new ArrayList<Position>();
	private List<OrchestraMember>	members			= new ArrayList<OrchestraMember>();
	
	public Orchestra() {
		addPosition(CONDUCTOR);
		addMember(CONDUCTOR,0,"localhost",5433,5432);
	}
	
	public void initialize() {
		// Override to implement
	}
	
	public List<Position> getPositions() {
		return new ArrayList<Position>(positions);
	}

	public Position getPosition(String name) {
		Position r = null;
		for (Position pos: positions) {
			if (pos.getName().equals(name)) {
				r = pos;
				break;
			}
		}
		return r;
	}

	public Position addPosition(String name) {
		Position r = getPosition(name);
		if (r==null) {
			positions.add(new Position(name));
		}
		return r;
	}

	public OrchestraMember getConductor() {
		return getMemberById(CONDUCTORID);
	}

	public OrchestraMember getMember(String ipAddressOrHostName,int port) {
		OrchestraMember r = null;
		for (OrchestraMember member: members) {
			if (member.getIpAddressOrHostName().equals(ipAddressOrHostName) && 
				(member.getControlPort()==port || member.getWorkPort()==port)
				) {
				r = member;
				break;
			}
		}
		return r;
	
	}

	public OrchestraMember getMemberById(String id) {
		OrchestraMember r = null;
		for (OrchestraMember member: members) {
			if (member.getId().equals(id)) {
				r = member;
				break;
			}
		}
		return r;
	}

	/*
	public OrchestraMember getMemberForPosition(String positionName) {
		return getMemberForPosition(positionName,0);
	}
	
	public OrchestraMember getMemberForPosition(String positionName,int positionBackupNumber) {
		OrchestraMember r = null;
		for (OrchestraMember member: members) {
			if (member.getPosition().getName().equals(positionName) &&
				member.getPositionBackupNumber()==positionBackupNumber
				) {
				r = member;
				break;
			}
		}
		return r;
	}
	*/
	
	public OrchestraMember addMember(String positionName,int positionBackupNumber,String ipAddressOrHostName,int controlPort,int workPort) {
		OrchestraMember r = null;
		if (getPosition(positionName)!=null) {
			r = getMember(ipAddressOrHostName,controlPort);
			if (r==null) {
				if (positionName.equals(CONDUCTOR)) {
					positionBackupNumber = 0;
				}
				r = getMemberById(positionName + "/" + positionBackupNumber);
			}
			if (r==null) {
				r = new OrchestraMember();
				r.setPosition(getPosition(positionName));
				r.setPositionBackupNumber(positionBackupNumber);
				r.setIpAddressOrHostName(ipAddressOrHostName);
				r.setControlPort(controlPort);
				r.setWorkPort(workPort);
				r.setState(MemberState.getState(MemberState.UNKNOWN));
				members.add(r);
			}
		}
		return r;
	}

	public List<OrchestraMember> getMembers() {
		return new ArrayList<OrchestraMember>(members);
	}
	
	public JsFile toJson(boolean includeState) {
		JsFile f = new JsFile();
		f.rootElement = new JsElem();
		JsElem posits = new JsElem("positions");
		f.rootElement.children.add(posits);
		int i = 0;
		for (Position pos: positions) {
			posits.children.add(new JsElem("" + i,pos.getName(),true));
			i++;
		}
		JsElem membs = new JsElem("members",true);
		f.rootElement.children.add(membs);
		for (OrchestraMember member: members) {
			membs.children.add(member.toJsonElem(includeState));
		}
		return f;
	}

	public void fromJson(JsFile file) {
		positions.clear();
		members.clear();
		for (JsElem el: file.rootElement.children) {
			if (el.name.equals("positions")) {
				for (JsElem pos: el.children) {
					if (pos.value!=null && pos.value.length()>0) {
						addPosition(pos.value.toString());
					}
				}
			} else if (el.name.equals("members")) {
				for (JsElem mem: el.children) {
					OrchestraMember member = new OrchestraMember();
					for (JsElem meme: mem.children) {
						if (meme.name.equals("positionName") && meme.value!=null) {
							member.setPosition(getPosition(meme.value.toString()));
						} else if (meme.name.equals("positionBackupNumber") && meme.value!=null) {
							member.setPositionBackupNumber(Integer.parseInt(meme.value.toString()));
						} else if (meme.name.equals("ipAddressOrHostName") && meme.value!=null) {
							member.setIpAddressOrHostName(meme.value.toString());
						} else if (meme.name.equals("controlPort") && meme.value!=null) {
							member.setControlPort(Integer.parseInt(meme.value.toString()));
						} else if (meme.name.equals("workPort") && meme.value!=null) {
							member.setWorkPort(Integer.parseInt(meme.value.toString()));
						}
					}
					if (member.getPosition()!=null) {
						addMember(member.getPosition().getName(),member.getPositionBackupNumber(),member.getIpAddressOrHostName(),member.getControlPort(),member.getWorkPort());
					}
				}
			}
		}
	}
}
