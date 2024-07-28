package telran.bullscows.net;

import java.util.List;

import telran.bulls.cows.net.BullsCowsApplItems;
import telran.net.TcpClient;
import telran.view.Item;
import telran.view.Menu;
import telran.view.SystemInputOutput;

public class BullsCowsClientAppl {
	private static final int PORT = 5000;
	public static void main(String[] args) {
		TcpClient tcpClient = new TcpClient("localhost", PORT);
		BullsCowsProxy bullsCowsProxy = new BullsCowsProxy(tcpClient);
		List<Item> items = BullsCowsApplItems.getItems(bullsCowsProxy);
		items.add(Item.of("Exit & connection close", io -> {
			try {
				tcpClient.close();
			} catch (Exception e) {
				
			}
		}, true));
		Menu menu = new Menu("Bulls and Cows Game",  items.toArray(Item[]::new));
		menu.perform(new SystemInputOutput());


	}

}


