package test;

import org.smartnio.core.$;

public class Boot {
	public static void main(String[] args) throws Exception {
		$ nio=$.server("127.0.0.1",9907).setup(new MyWaiter());
		nio.start();
		
	}
}
