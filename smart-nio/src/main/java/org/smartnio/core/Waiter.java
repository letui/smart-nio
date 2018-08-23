package org.smartnio.core;

import java.nio.ByteBuffer;
import java.nio.channels.NetworkChannel;

public interface Waiter {
	void onAccept(NetworkChannel cameInChannel);
	ByteBuffer onReceive(final ByteBuffer receiveBuf);
	void onReset();
	void keepWrite(final ByteBuffer sendBuf);
}
