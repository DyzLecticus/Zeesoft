package nl.zeesoft.zjmo.orchestra;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zdk.ZDate;
import nl.zeesoft.zdk.json.JsElem;
import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zjmo.orchestra.controller.OrchestraController;
import nl.zeesoft.zjmo.orchestra.members.Conductor;
import nl.zeesoft.zjmo.orchestra.members.Player;

/**
 * Extend this class and implement the initialize method to create a custom orchestra.
 */
public abstract class Orchestra {
	public static final String		LOCALHOST			= "localhost";
	public static final String		LOCALHOSTIP			= "127.0.0.1";
	
	public static final String		CONDUCTOR			= "Conductor";
	public static final String		ORCHESTRA_CRITICAL	= "Orchestra critical";
	public static final String		ORCHESTRA_OPTIONAL	= "Orchestra optional";
	
	private List<Position> 			positions			= new ArrayList<Position>();
	private List<Channel> 			channels			= new ArrayList<Channel>();
	private List<OrchestraMember>	members				= new ArrayList<OrchestraMember>();
	
	// TODO: Java documentation
	
	public Orchestra() {
		addPosition(CONDUCTOR);
		addChannel(ORCHESTRA_CRITICAL,true);
		addChannel(ORCHESTRA_OPTIONAL,false);
		addMember(CONDUCTOR,0,LOCALHOST,5433,5432,500,false);
	}
	
	public abstract void initialize();

	public OrchestraGenerator getNewGenerator() {
		return new OrchestraGenerator();
	}

	public Conductor getNewConductor(Messenger msgr,int positionBackupNumber) {
		return new Conductor(msgr,this,positionBackupNumber);
	}

	public Player getNewPlayer(Messenger msgr,String positionName,int positionBackupNumber) {
		return new Player(msgr,this,positionName,positionBackupNumber);
	}

	public OrchestraController getNewController(boolean exitOnClose) {
		return new OrchestraController(this,exitOnClose);
	}

	public int closeUnusedWorkClientsMilliseconds() {
		return 60000;
	}

	public boolean isLocalHost() {
		boolean r = true;
		for (OrchestraMember member: members) {
			if (!member.getIpAddressOrHostName().equals(LOCALHOST) && !member.getIpAddressOrHostName().equals(LOCALHOSTIP)) {
				r = false;
				break;
			}
		}
		return r;
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

	public List<Channel> getChannels() {
		return new ArrayList<Channel>(channels);
	}

	public Channel getChannel(String name) {
		Channel r = null;
		for (Channel chan: channels) {
			if (chan.getName().equals(name)) {
				r = chan;
				break;
			}
		}
		return r;
	}

	public Channel addChannel(String name, boolean failOnSubscriberError) {
		Channel r = getChannel(name);
		if (r==null) {
			channels.add(new Channel(name,failOnSubscriberError));
		}
		return r;
	}

	public List<OrchestraMember> getConductors() {
		return getMembersForPosition(CONDUCTOR);
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

	public List<OrchestraMember> getMembersForPosition(String positionName) {
		List<OrchestraMember> r = new ArrayList<OrchestraMember>();
		SortedMap<Integer,OrchestraMember> sorter = new TreeMap<Integer,OrchestraMember>();
		for (OrchestraMember member: members) {
			if (member.getPosition().getName().equals(positionName)) {
				sorter.put(member.getPositionBackupNumber(),member);
			}
		}
		for (Entry<Integer,OrchestraMember> entry: sorter.entrySet()) {
			r.add(entry.getValue());
		}
		return r;
	}

	public OrchestraMember getMemberForPosition(String positionName,int positionBackupNumber) {
		OrchestraMember r = null;
		for (OrchestraMember member: members) {
			if (member.getPosition().getName().equals(positionName) && member.getPositionBackupNumber()==positionBackupNumber) {
				r = member;
				break;
			}
		}
		return r;
	}

	public List<OrchestraMember> getMembersForChannel(String channelName) {
		List<OrchestraMember> r = new ArrayList<OrchestraMember>();
		for (OrchestraMember member: members) {
			for (Channel chan: member.getChannels()) {
				if (chan.getName().equals(channelName)) {
					r.add(member);
					break;
				}
			}
		}
		return r;
	}
	
	public OrchestraMember addMember(String positionName,int positionBackupNumber,String ipAddressOrHostName,int controlPort,int workPort) {
		return addMember(positionName,positionBackupNumber,ipAddressOrHostName,controlPort,workPort,500,false);
	}

	public OrchestraMember addMember(String positionName,int positionBackupNumber,String ipAddressOrHostName,int controlPort,int workPort,int workRequestTimeout,boolean workRequestTimeoutDrain) {
		OrchestraMember r = null;
		if (getPosition(positionName)!=null) {
			r = getMember(ipAddressOrHostName,controlPort);
			if (r==null) {
				r = getMember(ipAddressOrHostName,workPort);
			}
			if (r==null) {
				r = getMemberById(positionName + "/" + positionBackupNumber);
			}
			if (r==null) {
				r = new OrchestraMember();
				r.setPosition(getPosition(positionName));
				r.setPositionBackupNumber(positionBackupNumber);
				r.setIpAddressOrHostName(ipAddressOrHostName);
				r.setControlPort(controlPort);
				r.setWorkPort(workPort);
				r.setWorkRequestTimeout(workRequestTimeout);
				r.setWorkRequestTimeoutDrain(workRequestTimeoutDrain);
				r.setState(MemberState.getState(MemberState.UNKNOWN));
				Channel crit = getChannel(ORCHESTRA_CRITICAL);
				if (crit!=null) {
					r.getChannels().add(crit);
				}
				Channel opti = getChannel(ORCHESTRA_OPTIONAL);
				if (opti!=null) {
					r.getChannels().add(opti);
				}
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
		JsElem chans = new JsElem("channels",true);
		f.rootElement.children.add(chans);
		for (Channel chan: channels) {
			JsElem ch = new JsElem();
			chans.children.add(ch);
			ch.children.add(new JsElem("name",chan.getName(),true));
			ch.children.add(new JsElem("failOnSubscriberError","" + chan.isFailOnSubscriberError()));
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
		channels.clear();
		members.clear();
		for (JsElem el: file.rootElement.children) {
			if (el.name.equals("positions")) {
				for (JsElem pos: el.children) {
					if (pos.value!=null && pos.value.length()>0) {
						addPosition(pos.value.toString());
					}
				}
			} else if (el.name.equals("channels")) {
				if (el.children.size()>0) {
					for (JsElem ch: el.children) {
						String name = "";
						boolean fail = false;
						for (JsElem chan: ch.children) {
							if (chan.name.equals("name") && chan.value!=null && chan.value.length()>0) {
								name = chan.value.toString();
							} else if (chan.name.equals("failOnSubscriberError") && chan.value!=null && chan.value.length()>0) {
								fail = Boolean.parseBoolean(chan.value.toString());
							}
						}
						addChannel(name,fail);
					}
				}
			}
		}
		for (JsElem el: file.rootElement.children) {
			if (el.name.equals("members")) {
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
						} else if (meme.name.equals("workRequestTimeout") && meme.value!=null) {
							member.setWorkRequestTimeout(Integer.parseInt(meme.value.toString()));
						} else if (meme.name.equals("workRequestTimeoutDrain") && meme.value!=null) {
							member.setWorkRequestTimeoutDrain(Boolean.parseBoolean(meme.value.toString()));
						} else if (meme.name.equals("channels") && meme.children.size()>0) {
							for (JsElem chan: meme.children) {
								if (chan.value!=null && chan.value.length()>0) {
									Channel channel = getChannel(chan.value.toString());
									if (channel!=null) {
										member.getChannels().add(channel);
									}
								}
							}
						} else if (meme.name.equals("state") && meme.value!=null) {
							member.setState(MemberState.getState(meme.value.toString()));
						} else if (meme.name.equals("workLoad") && meme.value!=null) {
							member.setWorkLoad(Integer.parseInt(meme.value.toString()));
						} else if (meme.name.equals("memoryUsage") && meme.value!=null) {
							member.setMemoryUsage(Long.parseLong(meme.value.toString()));
						} else if (meme.name.equals("errorDate") && meme.value!=null) {
							Date d = new Date();
							d.setTime(Long.parseLong(meme.value.toString()));
							ZDate zd = new ZDate();
							zd.setDate(d);
							member.setErrorDate(zd);
						} else if (meme.name.equals("errorMessage") && meme.value!=null) {
							member.setErrorMessage(meme.value.toString());
						}
					}
					if (member.getPosition()!=null) {
						OrchestraMember existing = getMember(member.getIpAddressOrHostName(),member.getControlPort());
						if (existing==null) {
							existing = getMember(member.getIpAddressOrHostName(),member.getWorkPort());
						}
						if (existing==null) {
							existing = getMemberById(member.getId());
						}
						if (existing==null) {
							members.add(member);
						}
					}
				}
			}
		}
	}
}
