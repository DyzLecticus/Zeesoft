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
import nl.zeesoft.zjmo.Orchestrator;
import nl.zeesoft.zjmo.orchestra.controller.OrchestraController;
import nl.zeesoft.zjmo.orchestra.members.Conductor;
import nl.zeesoft.zjmo.orchestra.members.Player;

/**
 * Extend this class and implement the initialize method to create a custom orchestra.
 * Orchestra objects are instantiated dynamically by the orchestrator.
 * Do not implement constructors that require arguments.
 */
public abstract class Orchestra {
	public static final String		LOCALHOST			= "localhost";
	public static final String		LOCALHOSTIP			= "127.0.0.1";
	
	public static final String		CONDUCTOR			= "Conductor";
	
	private List<Position> 			positions			= new ArrayList<Position>();
	private List<Channel> 			channels			= new ArrayList<Channel>();
	private List<OrchestraMember>	members				= new ArrayList<OrchestraMember>();
	
	public Orchestra() {
		addPosition(CONDUCTOR);
		addMember(CONDUCTOR,0,LOCALHOST,5433,5432,500,false);
	}
	
	/**
	 * Called by the orchestrator when generating or updating an orchestra.
	 * Override this method and use the methods provided by this class to create a custom orchestra.
	 */
	public abstract void initialize();

	/**
	 * Returns a new orchestra generator for this orchestra.
	 * 
	 * @return A new orchestra generator for this orchestra
	 */
	public OrchestraGenerator getNewGenerator() {
		return new OrchestraGenerator();
	}

	/**
	 * Returns a new conductor for this orchestra.
	 * 
	 * @param msgr The messenger
	 * @param positionBackupNumber The conductor position backup number
	 * @return A new conductor for this orchestra
	 */
	public Conductor getNewConductor(Messenger msgr,int positionBackupNumber) {
		return new Conductor(msgr,this,positionBackupNumber);
	}

	/**
	 * Returns a new player for this orchestra.
	 * 
	 * @param msgr The messenger
	 * @param positionName The player position name
	 * @param positionBackupNumber The player position backup number
	 * @return A new player for this orchestra
	 */
	public Player getNewPlayer(Messenger msgr,String positionName,int positionBackupNumber) {
		return new Player(msgr,this,positionName,positionBackupNumber);
	}

	/**
	 * Returns a new orchestra controller for this orchestra.
	 * 
	 * @param exitOnClose Indicates the controller should call System.exit upon closing
	 * @return A new orchestra controller for this orchestra
	 */
	public OrchestraController getNewController(boolean exitOnClose) {
		return new OrchestraController(this,exitOnClose);
	}

	/**
	 * Returns the duration after which unused work clients in the conductor work client pool are closed.
	 * 
	 * @return The duration after which unused work clients in the conductor work client pool are closed
	 */
	public int closeUnusedWorkClientsMilliseconds() {
		return 60000;
	}

	/**
	 * Returns a copy of this orchestra using the orchestrator for class instantiation and the to and from JSON methods.
	 *  
	 * @param includeState Indicates state information will be included in the copy
	 * @return A copy of this orchestra
	 */
	public Orchestra getCopy(boolean includeState) {
		Orchestra orch = Orchestrator.getOrchestraForClassName(this.getClass().getName());
		orch.fromJson(toJson(includeState));
		return orch;
	}

	/**
	 * Returns true if all orchestra members are hosted locally.
	 * Useful for testing and demonstration purposes.
	 * 
	 * @return True if all orchestra members are hosted locally
	 */
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
	
	/**
	 * Returns a read only list of orchestra positions.
	 * 
	 * @return A read only list of orchestra positions
	 */
	public List<Position> getPositions() {
		return new ArrayList<Position>(positions);
	}

	/**
	 * Returns the position with the specified name or null.
	 * 
	 * @param name The name of the position
	 * @return The position with the specified name or null
	 */
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

	/**
	 * Adds a position to the orchestra.
	 * Ensures the name of the position is unique within the orchestra.
	 * 
	 * @param name The name of the position to add
	 * @return The new position
	 */
	public Position addPosition(String name) {
		Position r = getPosition(name);
		if (r==null) {
			r = new Position(name);
			positions.add(r);
		}
		return r;
	}

	/**
	 * Returns a read only list of orchestra channels.
	 * 
	 * @return A read only list of orchestra channels
	 */
	public List<Channel> getChannels() {
		return new ArrayList<Channel>(channels);
	}

	/**
	 * Returns the channel with the specified name or null.
	 * 
	 * @param name The name of the channel
	 * @return The channel with the specified name or null
	 */
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

	/**
	 * Adds a channel to the orchestra.
	 * Ensures the name of the channel is unique within the orchestra.
	 * 
	 * @param name The name of the channel to add
	 * @param failOnSubscriberError Indicates publish requests should fail when a subscriber returns an error
	 * @return The new channel
	 */
	public Channel addChannel(String name, boolean failOnSubscriberError) {
		Channel r = getChannel(name);
		if (r==null) {
			r = new Channel(name,failOnSubscriberError);
			channels.add(r);
		}
		return r;
	}

	/**
	 * Returns a read only list of orchestra conductors.
	 * 
	 * @return A read only list of orchestra conductors
	 */
	public List<OrchestraMember> getConductors() {
		return getMembersForPosition(CONDUCTOR);
	}

	/**
	 * Returns a member for a certain host and port or null.
	 * Uses both control and work ports for the search.
	 * 
	 * @param ipAddressOrHostName The IP address or host name of the member
	 * @param port The control or work port of the member
	 * @return The member or null
	 */
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

	/**
	 * Returns the member with the specified member id or null.
	 * 
	 * @param id The id of the member
	 * @return The member or null
	 */
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

	/**
	 * Returns a read only list of members for a certain position.
	 * Members are ordered by position backup number, ascending.
	 * 
	 * @param positionName The name of the position
	 * @return A read only list of members for a certain position
	 */
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

	/**
	 * Returns a member for a certain position and backup number or null.
	 * 
	 * @param positionName The name of the position
	 * @param positionBackupNumber The position backup number
	 * @return The member or null
	 */
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

	/**
	 * Returns a read only list of members (subscribers) for a certain channel.
	 * 
	 * @param channelName The name of the channel
	 * @return A read only list of members (subscribers) for a certain channel
	 */
	public List<OrchestraMember> getMembersForChannel(String channelName) {
		List<OrchestraMember> r = new ArrayList<OrchestraMember>();
		Channel chan = getChannel(channelName);
		if (chan!=null) {
			for (String id: chan.getSubscriberIdList()) {
				OrchestraMember member = getMemberById(id);
				if (member!=null) {
					r.add(member);
				}
			}
		}
		return r;
	}

	/**
	 * Removes the member with the specified id from the orchestra
	 * 
	 * @param id The member id
	 */
	public void removeMember(String id) {
		OrchestraMember member = getMemberById(id);
		if (member!=null && !(member.getPosition().getName().equals(CONDUCTOR) && member.getPositionBackupNumber()==0)) {
			members.remove(member);
			for (Channel chan: channels) {
				chan.getSubscriberIdList().remove(member.getId());
			}
		}
	}

	/**
	 * Adds a member to the orchestra with the specified parameters.
	 * Ensures member id's are unique.
	 * 
	 * @param positionName The member position name
	 * @param positionBackupNumber The member position backup number
	 * @param ipAddressOrHostName The IP address or host name of the member
	 * @param controlPort The control port
	 * @param workPort The work port
	 * @return A new orchestra member
	 */
	public OrchestraMember addMember(String positionName,int positionBackupNumber,String ipAddressOrHostName,int controlPort,int workPort) {
		return addMember(positionName,positionBackupNumber,ipAddressOrHostName,controlPort,workPort,500,false);
	}

	/**
	 * Adds a member to the orchestra with the specified parameters.
	 * Ensures member id's are unique.
	 * 
	 * @param positionName The member position name
	 * @param positionBackupNumber The member position backup number
	 * @param ipAddressOrHostName The IP address or host name of the member
	 * @param controlPort The control port
	 * @param workPort The work port
	 * @param workRequestTimeout The work request time out in milliseconds
	 * @param workRequestTimeoutDrain Indicates the member should drain work upon time out so that conductors will divert to backups
	 * @return A new orchestra member
	 */
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
				members.add(r);
			}
		}
		return r;
	}

	/**
	 * Returns a read only list of orchestra members.
	 * 
	 * @return A read only list of orchestra members
	 */
	public List<OrchestraMember> getMembers() {
		return new ArrayList<OrchestraMember>(members);
	}
	
	/**
	 * Converts the orchestra to a JSON file.
	 * 
	 * @param includeState Indicates member state information should be included in the JSON
	 * @return The orchestra as a JSON file
	 */
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
			if (chan.getSubscriberIdList().size()>0) {
				JsElem subs = new JsElem("subscriberIdList");
				ch.children.add(subs);
				i = 0;
				for (String id: chan.getSubscriberIdList()) {
					subs.children.add(new JsElem(""+ i,id,true));
					i++;
				}
			}
		}
		JsElem membs = new JsElem("members",true);
		f.rootElement.children.add(membs);
		for (OrchestraMember member: members) {
			membs.children.add(member.toJsonElem(includeState));
		}
		return f;
	}

	/**
	 * Initializes the orchestra using a JSON file.
	 * 
	 * @param file The orchestra as a JSON file
	 */
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
						List<String> subs = new ArrayList<String>();
						for (JsElem chan: ch.children) {
							if (chan.name.equals("name") && chan.value!=null && chan.value.length()>0) {
								name = chan.value.toString();
							} else if (chan.name.equals("failOnSubscriberError") && chan.value!=null && chan.value.length()>0) {
								fail = Boolean.parseBoolean(chan.value.toString());
							} else if (chan.name.equals("subscriberIdList") && chan.children.size()>0) {
								for (JsElem sub: chan.children) {
									if (sub.value!=null && sub.value.toString().length()>0) {
										subs.add(sub.value.toString());
									}
								}
							}
						}
						Channel chan = addChannel(name,fail);
						if (subs.size()>0) {
							for (String id: subs) {
								chan.getSubscriberIdList().add(id);
							}
						}
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
						} else if (meme.name.equals("restartRequired") && meme.value!=null) {
							member.setRestartRequired(Boolean.parseBoolean(meme.value.toString()));
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
		Position tp = getPosition(CONDUCTOR);
		if (tp==null) {
			addPosition(CONDUCTOR);
		}
		OrchestraMember tc = getMemberForPosition(CONDUCTOR,0);
		if (tc==null) {
			addMember(CONDUCTOR,0,LOCALHOST,5433,5432,500,false);
		}
	}
}
