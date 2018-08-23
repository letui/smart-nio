package org.smartnio.core;
 
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
 
public class C {
	final int bufferSize=2048;
	SocketChannel channel = null;
	public static C connect(String ip,int port) {
		C c=new C();
		try {
			c.channel = SocketChannel.open();
			c.channel.configureBlocking(false);
			c.channel.connect(new InetSocketAddress(ip, port));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return c;
	}
	
	public void setup(Waiter w) throws IOException {
		Selector selector = Selector.open();
		channel.register(selector, SelectionKey.OP_CONNECT);
		while (true) {
			selector.select(); // 阻塞 直到有就绪事件为止
			Set<SelectionKey> readySelectionKey = selector.selectedKeys();
			Iterator<SelectionKey> it = readySelectionKey.iterator();
			while (it.hasNext()) {
				SelectionKey selectionKey = it.next();
				if (selectionKey.isConnectable()) {
					if (channel.isConnectionPending()) {
						if (channel.finishConnect()) {
							// 只有当连接成功后才能注册OP_READ事件
							channel.register(selector,SelectionKey.OP_READ | SelectionKey.OP_WRITE);
							w.onAccept(channel);
						} else {
							selectionKey.cancel();
						}
					}
				}
				if (selectionKey.isReadable()) {// 读数据
					SocketChannel clientChannel = (SocketChannel) selectionKey.channel();
					ByteBuffer receiveBuf = ByteBuffer.allocate(bufferSize);
					clientChannel.read(receiveBuf);
					ByteBuffer sendBuf=w.onReceive(receiveBuf);
					if(sendBuf!=null) {
						sendBuf.flip();
						clientChannel.write(sendBuf);
					}
				}
				if (selectionKey.isWritable()) {// 写数据
					SocketChannel clientChannel = (SocketChannel) selectionKey.channel();
					ByteBuffer sendBuf = ByteBuffer.allocate(bufferSize);
					w.keepWrite(sendBuf);
					sendBuf.flip(); 
					if(clientChannel.isOpen()) {
						clientChannel.write(sendBuf);
					}else {
						clientChannel.close();
					}
				}
				it.remove();
			}
		}
	}
	
}

