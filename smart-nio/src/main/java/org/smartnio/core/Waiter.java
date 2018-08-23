package org.smartnio.core;

import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;

public interface Waiter {
	void onAccept(ServerSocketChannel cameInChannel);
	ByteBuffer onReceive(final ByteBuffer receiveBuf);
	void onReset();
	void keepWrite(final ByteBuffer sendBuf);
}
