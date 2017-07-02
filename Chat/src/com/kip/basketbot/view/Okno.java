package com.kip.basketbot.view;

import java.awt.EventQueue;

import javax.swing.JFrame;
import java.awt.GridLayout;

import javax.swing.AbstractAction;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.LayoutStyle.ComponentPlacement;

import org.alicebot.ab.Bot;
import org.alicebot.ab.Chat;

import com.kip.basketbot.controllers.ChatController;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JTextArea;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.awt.event.ActionEvent;
import java.awt.Color;
import java.awt.Font;
import javax.swing.border.LineBorder;
import java.awt.Window.Type;
import java.awt.Toolkit;
import javax.swing.JLabel;
import javax.swing.ImageIcon;

public class Okno {

	public JFrame frmBasketbot;
	private JTextField textField;
	private JButton btnWyslij;
	private JTextArea textArea;
	private Chat chatSession;
	private boolean flag;
	private String template;
	private String pattern;
	private JLabel lblNewLabel;

	/**
	 * Create the application.
	 */
	public Okno(Chat session) {
		chatSession = session;
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmBasketbot = new JFrame();
		frmBasketbot.setIconImage(Toolkit.getDefaultToolkit()
				.getImage(Okno.class.getResource("/com/kip/basketbot/resources/basketball32.png")));
		frmBasketbot.setTitle("BasketBot");
		frmBasketbot.getContentPane().setBackground(new Color(255, 153, 51));
		frmBasketbot.setBounds(100, 100, 439, 414);
		frmBasketbot.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JScrollPane scrollPane = new JScrollPane();

		textField = new JTextField();
		textField.setBorder(new LineBorder(new Color(0, 0, 0)));
		textField.setColumns(10);

		btnWyslij = new JButton("");
		btnWyslij.setIcon(new ImageIcon(Okno.class.getResource("/com/kip/basketbot/resources/button.png")));
		btnWyslij.setFont(new Font("Segoe Print", Font.BOLD, 14));
		btnWyslij.setForeground(new Color(0, 0, 0));
		btnWyslij.setBorder(new LineBorder(new Color(0, 0, 0)));
		btnWyslij.setBackground(new Color(248, 182,94));
		btnWyslij.setFocusPainted(false);
		
		
		btnWyslij.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String request = textField.getText();
				String chat = textArea.getText();
				//jezeli request pusty to nic nie rob
				if (!request.equals("")) {
					if (!chat.equals(""))
						textArea.setText(chat + "\nJa: " + request);
					else
						textArea.setText(chat + "Ja: " + request);
					textField.setText("");
					chat = textArea.getText();
					//jezeli podano template ktory bot ma sie nauczyc
					if (flag) {
						template = request;
						if(!template.equals("to była pomyłka"))
						try {
							ChatController.learn(pattern, template);
							// pobranie danych
							String get = "get";
							get = chatSession.multisentenceRespond(get);
							String [] values = ChatController.parseGet(get);
							String botname = "super";
							String p = "./";
							Bot bot = new Bot(botname, p);
							chatSession = new Chat(bot);
							chatSession.multisentenceRespond("setImie" + values[0]);
							chatSession.multisentenceRespond("setKImie" + values[1]);
							chatSession.multisentenceRespond("setKNazwisko" + values[2]);
							chatSession.multisentenceRespond("setAsysty" + values[3]);
							chatSession.multisentenceRespond("setPunkty" + values[4]);
							chatSession.multisentenceRespond("setZbiorki" + values[5]);
							chatSession.multisentenceRespond("setTemat" + values[6]);

							textArea.setText(chat + "\nBot: " + "Dziękuję!");
							flag = false;
						} catch (IOException e) {
							e.printStackTrace();
						}
						else {
							flag=false;
							textArea.setText(chat + "\nBot: " + "Ok.");
						}
					}
					//jezeli ma to byc zwykla odpowiedz
					else {

						String response = chatSession.multisentenceRespond(request);
						//w przypadku gdyby bot nie znal odpowiedzi
						if (response.equals("I have no answer for that.")) {
							textArea.setText(chat + "\nBot: " + "Nie znam odpowiedzi, naucz mnie!");
							flag = true;
							StringBuffer sb = new StringBuffer();
							//przefiltrowanie requesta w celu usuniecia znakow specjalnych
							for (int i = 0; i < request.length(); i++) {
								if (request.charAt(i) != '?' && request.charAt(i) != '!' && request.charAt(i) != '.'&& request.charAt(i) != ';' && request.charAt(i) != '*'&& request.charAt(i) != '@' && request.charAt(i) != '%'&& request.charAt(i) != '"')
									sb.append(request.charAt(i));
							}
						pattern = sb.toString();
						
						
						//jezeli ma uzyc logiki rozmytej w celu udzielenia odpowiedzi
						}else if(response.equals("ocena koszykarza")) {
							String get=chatSession.multisentenceRespond("get");
							String [] values = ChatController.parseGet(get);
							response=ChatController.checkIfEmpty(values);
							//jezeli wszystkie dane nie zostaly podane
							if(!response.equals("ok")) {
								if(response.equals("A jak się nazywa?")) {
									response=chatSession.multisentenceRespond("zapytaj o imię");
								}
								
								textArea.setText(chat + "\nBot: " + response);
							}
							//gdy bot posiada wszystkie dane
							else {
								response=ChatController.fuzzy(values[3], values[4], values[5]);
								response=chatSession.multisentenceRespond(response);
								textArea.setText(chat + "\nBot: " + response);
							}
						}
						else if(response.equals("sprawdz punkty")) {
							String get=chatSession.multisentenceRespond("get");
							response=ChatController.checkPoints(get);
							if(response.equals("isOk"))
								response=chatSession.multisentenceRespond(response);
							else
								chatSession.multisentenceRespond("setPunkty unknown");
							textArea.setText(chat + "\nBot: " + response);
						}
						else if(response.equals("sprawdz asysty")) {
							String get=chatSession.multisentenceRespond("get");
							response=ChatController.checkAssists(get);
							if(response.equals("isOk"))
								response=chatSession.multisentenceRespond(response);
							else
								chatSession.multisentenceRespond("setAsysty unknown");
							textArea.setText(chat + "\nBot: " + response);
						}
						else if(response.equals("sprawdz zbiorki")) {
							String get=chatSession.multisentenceRespond("get");
							response=ChatController.checkRebounds(get);
							if(response.equals("isOk"))
								response=chatSession.multisentenceRespond(response);
							else
								chatSession.multisentenceRespond("setZbiorki unknown");
							textArea.setText(chat + "\nBot: " + response);
						}
						//jezeli jest to zwykla odpowiedz
						else
							textArea.setText(chat + "\nBot: " + response);
					}
				}
			}
		});
		
		lblNewLabel = new JLabel("");
		lblNewLabel.setIcon(new ImageIcon(Okno.class.getResource("/com/kip/basketbot/resources/BasketBot.png")));
		
		frmBasketbot.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER,0),"clickButton");
		frmBasketbot.getRootPane().getActionMap().put("clickButton",new AbstractAction(){
				        public void actionPerformed(ActionEvent ae)
				        {
				    btnWyslij.doClick();
				    System.out.println("button clicked");
				        }
				    });
				    
		
		GroupLayout groupLayout = new GroupLayout(frmBasketbot.getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(textField, GroupLayout.DEFAULT_SIZE, 297, Short.MAX_VALUE)
							.addGap(18)
							.addComponent(btnWyslij, GroupLayout.PREFERRED_SIZE, 77, GroupLayout.PREFERRED_SIZE)
							.addGap(21))
						.addGroup(groupLayout.createSequentialGroup()
							.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
								.addComponent(lblNewLabel, Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 403, Short.MAX_VALUE)
								.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 403, Short.MAX_VALUE))
							.addContainerGap())))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblNewLabel, GroupLayout.PREFERRED_SIZE, 83, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 180, Short.MAX_VALUE)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(18)
							.addComponent(btnWyslij))
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(29)
							.addComponent(textField, GroupLayout.PREFERRED_SIZE, 32, GroupLayout.PREFERRED_SIZE)))
					.addGap(26))
		);

		textArea = new JTextArea();
		textArea.setBorder(new LineBorder(new Color(0, 0, 0)));
		textArea.setForeground(new Color(0, 0, 0));
		textArea.setFont(new Font("Segoe Print", Font.BOLD, 14));
		textArea.setEditable(false);
		scrollPane.setViewportView(textArea);
		frmBasketbot.getContentPane().setLayout(groupLayout);
	}
}
