package test;

import java.nio.ByteBuffer;
import java.nio.channels.NetworkChannel;

import org.smartnio.core.Waiter;

public class MyWaiter implements Waiter {

	@Override
	public void onAccept(NetworkChannel cameInChannel) {
		
	}

	@Override
	public ByteBuffer onReceive(ByteBuffer receiveBuf) {
		System.out.println(receiveBuf.getChar());
		return receiveBuf;
	}

	@Override
	public void onReset() {
		System.out.println("BYE");
	}

	@Override
	public void keepWrite(ByteBuffer sendBuf) {
		sendBuf.put("HelloMyBaby".getBytes());
	}

}
