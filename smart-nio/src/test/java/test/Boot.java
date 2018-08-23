package test;

import org.smartnio.core.$;

public class Boot {
	public static void main(String[] args) {
		$ nio=$.server("127.0.0.1",9907).setup(new MyWaiter());
		Thread t=new Thread(nio);
		t.start();
		
	}
}
