# smart-nio
精悍小巧
<h3>换一种方式写Socket吧？</h3>
<pre>
    $ nio=$.server("127.0.0.1",9907).setup(new MyWaiter());
		Thread t=new Thread(nio);
		t.start();
</pre>
<h3>MyWaiter.class</h3>
<pre>
package test;

import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;

import org.smartnio.core.Waiter;

public class MyWaiter implements Waiter {

	@Override
	public void onAccept(ServerSocketChannel cameInChannel) {
		
	}

	@Override
	public ByteBuffer onReceive(ByteBuffer receiveBuf) {
		return receiveBuf;
	}

	@Override
	public void onReset() {
		System.out.println("BYE");
	}

	@Override
	public void keepWrite(ByteBuffer sendBuf) {
		try {
			Thread.sleep(1000l);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		//sendBuf.put("HelloMyBaby".getBytes());
	}

}

</pre>
