package nl.zeesoft.zjmo.test.mocks;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.thread.Worker;
import nl.zeesoft.zjmo.orchestra.MemberClient;
import nl.zeesoft.zjmo.orchestra.members.Conductor;
import nl.zeesoft.zjmo.orchestra.members.Player;

public class PlayerCommandWorker extends Worker {
	private Conductor 		conductor	= null;
	private Player 			player 		= null;
	private String			command		= null;
	private int				delayStart	= 500;
	private ZStringBuilder	response	= null;

	public PlayerCommandWorker(Conductor conductor,Player player,String command) {
		super(conductor.getMessenger(),conductor.getUnion());
		setSleep(1000);
		this.conductor = conductor;
		this.player = player;
		this.command = command;
	}

	@Override
	public void whileWorking() {
		try {
			Thread.sleep(delayStart);
		} catch (InterruptedException e) {
			getMessenger().error(this,"Player command worker was interrupted (1)");
		}
		MemberClient client = conductor.getNewControlClient(conductor.getMessenger(),conductor.getUnion());
		if (client.open()) {
			response = client.sendCommand(command,"id",player.getId());
			
			System.out.println();
			System.out.println("Player state JSON:");
			System.out.println(conductor.getMemberState(player.getId()).toStringBuilderReadFormat());
			
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				getMessenger().error(this,"Player command worker was interrupted (2)");
			}
			
			System.out.println();
			System.out.println("Player state JSON:");
			System.out.println(conductor.getMemberState(player.getId()).toStringBuilderReadFormat());
		}
		stop();
	}
	
	public ZStringBuilder getResponse() {
		return response;
	}
}
