import java.awt.EventQueue;

import org.alicebot.ab.Bot;
import org.alicebot.ab.Chat;

public class ChatBot {

	public static void main(String[] args) {
		String botname = "super";
		String path = "./";
		Bot bot = new Bot(botname, path);
		Chat chatSession = new Chat(bot);
		
				try {
					Okno window = new Okno(chatSession);
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
		
		
	}
}
