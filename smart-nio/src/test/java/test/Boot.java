package test;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.NetworkChannel;

import org.smartnio.core.$;
import org.smartnio.core.C;
import org.smartnio.core.Waiter;

public class Boot {
	public static void main(String[] args) throws IOException {
		$ nio=$.server("127.0.0.1",9907).setup(new MyWaiter());
		Thread t=new Thread(nio);
		t.start();
		
		C.connect("127.0.0.1", 9907).setup(new Waiter() {
			
			@Override
			public void onReset() {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public ByteBuffer onReceive(ByteBuffer receiveBuf) {
				return null;
			}
			
			@Override
			public void onAccept(NetworkChannel cameInChannel) {
				System.out.println("I connect Server");
				
			}
			
			@Override
			public void keepWrite(ByteBuffer sendBuf) {
				// TODO Auto-generated method stub
				
			}
		});;
		
	}
}
