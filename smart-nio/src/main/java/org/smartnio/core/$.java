package org.smartnio.core;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class $ implements Runnable {
	public int id = 100000;
	public int bufferSize = 2048;
	
	private String ip;
	private int port;
	private Waiter w;

	@Override
	public void run() {
		init();
	}

	public static $ server(String ip, int port) {
		$ _ = new $();
		_.ip=ip;
		_.port=port;
		return _;
	}
	
	public $ setup(Waiter w) {
		this.w=w;
		return this;
	}

	private void init() {
		try {
			ServerSocketChannel socketChannel = ServerSocketChannel.open();
			Selector selector = Selector.open();
			InetSocketAddress inetSocketAddress = new InetSocketAddress(ip, port);
			socketChannel.socket().bind(inetSocketAddress);
			socketChannel.configureBlocking(false);
			socketChannel.register(selector, SelectionKey.OP_ACCEPT).attach(id++);
			listener(selector);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void listener(Selector in_selector) {
		try {
			while (true) {
				in_selector.select(); // 阻塞 直到有就绪事件为止
				Set<SelectionKey> readySelectionKey = in_selector.selectedKeys();
				Iterator<SelectionKey> it = readySelectionKey.iterator();
				while (it.hasNext()) {
					SelectionKey selectionKey = it.next();
					if (selectionKey.isAcceptable()) {// 客户请求连接
						ServerSocketChannel serverSocketChannel = (ServerSocketChannel) selectionKey.channel();
						serverSocketChannel.accept().configureBlocking(false)
								.register(in_selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE).attach(id++);
						w.onAccept(serverSocketChannel);
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
		} catch (Exception e) {
			if("Connection reset by peer" .equals(e.getMessage())) {
				w.onReset();
			}else {
				e.printStackTrace();
			}
		}

	}
}
