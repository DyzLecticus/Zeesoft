package nl.zeesoft.zjmo.orchestra.protocol;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zjmo.orchestra.Channel;
import nl.zeesoft.zjmo.orchestra.MemberObject;
import nl.zeesoft.zjmo.orchestra.OrchestraMember;
import nl.zeesoft.zjmo.orchestra.ProtocolObject;
import nl.zeesoft.zjmo.orchestra.members.Conductor;
import nl.zeesoft.zjmo.orchestra.members.WorkClient;

public class ProtocolWorkConductor extends ProtocolWork {
	@Override
	protected ZStringBuilder handleInput(MemberObject member,ZStringBuilder input) {
		ZStringBuilder output = null;
		if (isCommandJson(input)) {
			output = super.handleInput(member, input);
		} else if (member instanceof Conductor) {
			//System.out.println(this + ": Handle work: " + input);
			input.trim();
			if (input.startsWith("{")) {
				Conductor con = (Conductor) member;
				WorkRequest wr = new WorkRequest();
				wr.fromStringBuilder(input);
				PublishRequest pr = new PublishRequest();
				pr.fromStringBuilder(input);
				if (wr.getPositionName().length()==0 && pr.getChannelName().length()==0) {
					output = handleUnknownJsonRequest(con,input);
					if (output==null) {
						wr.setError("Unrecognized input");
						output = wr.toJson().toStringBuilder();
					}
				} else {
					if (wr.getPositionName().length()>0) {
						output = handleWorkRequest(con,wr);
					} else if (pr.getChannelName().length()>0) {
						output = handlePublishRequest(con,pr);
						System.out.println("-------------------> " + output);
					}
				}
			} else {
				output = getErrorJson("Unrecognized input");
			}
		}
		return output;
	}

	protected ZStringBuilder handleUnknownJsonRequest(Conductor con,ZStringBuilder input) {
		return null;
	}

	protected ZStringBuilder handleWorkRequest(Conductor con,WorkRequest wr) {
		ZStringBuilder output = null;
		if (wr.getRequest()==null) {
			wr.setError("Work request is empty");
			output = wr.toJson().toStringBuilder();
		}
		if (output==null && con.getOrchestra().getPosition(wr.getPositionName())==null) {
			wr.setError("Work request position does not exist: " + wr.getPositionName());
			output = wr.toJson().toStringBuilder();
		}
		if (output==null) {
			WorkClient client = null;
			boolean retry = true;
			while (retry) {
				client = con.getClient(this,wr.getPositionName());
				if (client==null) {
					if (wr.getError().length()==0) {
						wr.setError("No players online for position: " + wr.getPositionName());
					}
					output = wr.toJson().toStringBuilder();
					retry = false;
				} else {
					wr.setResponse(null);
					//System.out.println(this + ": Sending request: " + wr.getRequest().toStringBuilder());
					ZStringBuilder response = client.sendWorkRequestRequest(wr);
					con.returnClient(client);
					if (response==null || response.equals(getCommandJson(ProtocolObject.CLOSE_SESSION,null))) {
						wr.setError("Work request timed out on: " + client.getMemberId());
						con.workRequestTimedOut(client);
					} else if (client.isOpen()) {
						JsFile resp = new JsFile();
						resp.fromStringBuilder(response);
						if (resp.rootElement==null) {
							wr.setError("Player did not return valid JSON: " + client.getMemberId());
						} else {
							wr.setResponse(resp);
							wr.setError("");
						}
						output = wr.toJson().toStringBuilder();
						retry = false;
					}
				}
			}
		}
		return output;
	}

	protected ZStringBuilder handlePublishRequest(Conductor con,PublishRequest pr) {
		ZStringBuilder output = null;
		Channel channel = null;
		if (pr.getRequest()==null) {
			pr.setError("Publish request is empty");
			output = pr.toJson().toStringBuilder();
		}
		channel = con.getOrchestra().getChannel(pr.getChannelName());
		if (output==null && channel==null) {
			pr.setError("Publish request channel does not exist: " + pr.getChannelName());
			output = pr.toJson().toStringBuilder();
		}
		List<PublishRequestWorker> workers = new ArrayList<PublishRequestWorker>();
		if (output==null) {
			List<OrchestraMember> members = con.getOrchestra().getMembersForChannel(pr.getChannelName());
			if (members.size()==0) {
				pr.setError("No subscribers for channel: " + pr.getChannelName());
				output = pr.toJson().toStringBuilder();
			} else {
				for (OrchestraMember member: members) {
					WorkClient client = con.getClientForMember(this,member.getId());
					WorkRequest wr = new WorkRequest();
					wr.setRequest(pr.getRequest());
					if (client!=null) {
						PublishRequestWorker worker = new PublishRequestWorker(con.getMessenger(),con.getUnion(),wr,client);
						workers.add(worker);
					} else if (channel.isFailOnSubscriberError()) {
						pr.setError("Channel subscriber is not online: " + member.getId());
						output = pr.toJson().toStringBuilder();
						break;
					}
				}
			}
		}
		if (output==null) {
			for (PublishRequestWorker worker: workers) {
				worker.start();
			}
			for (PublishRequestWorker worker: workers) {
				int i = 0;
				while (!worker.isDone()) {
					i++;
					try {
						if (i>10) {
							Thread.sleep(10);
						} else {
							Thread.sleep(1);
						}
					} catch (InterruptedException e) {
						con.getMessenger().error(this,"Request publishing was interrupted");
					}
				}
			}
			ZStringBuilder errors = new ZStringBuilder();
			for (PublishRequestWorker worker: workers) {
				con.returnClient(worker.getClient());
			}
			for (PublishRequestWorker worker: workers) {
				if (worker.getResponse()==null || worker.getResponse().equals(getCommandJson(ProtocolObject.CLOSE_SESSION,null))) {
					if (errors.length()>0) {
						errors.append("\n");
					}
					errors.append("Work request timed out on: " + worker.getClient().getMemberId());
					con.workRequestTimedOut(worker.getClient());
				} else if (worker.getClient().isOpen()) {
					JsFile resp = new JsFile();
					resp.fromStringBuilder(worker.getResponse());
					if (resp.rootElement==null) {
						if (errors.length()>0) {
							errors.append("\n");
						}
						errors.append("Member did not return valid JSON: " + worker.getClient().getMemberId());
					}
				}
			}
			if (errors.length()>0) {
				JsFile resp = new JsFile();
				resp.fromStringBuilder(getErrorJson("Error(s) occured while publishing the request"));
				pr.setResponse(resp);
				pr.setError(errors.toString());
			} else {
				JsFile resp = new JsFile();
				resp.fromStringBuilder(getExecutedCommandResponse());
				pr.setResponse(resp);
			}
			output = pr.toJson().toStringBuilder();
		}
		return output;
	}
}
