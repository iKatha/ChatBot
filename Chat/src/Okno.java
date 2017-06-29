import java.awt.EventQueue;

import javax.swing.JFrame;
import java.awt.GridLayout;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;

import org.alicebot.ab.Bot;
import org.alicebot.ab.Chat;

import javax.swing.JButton;
import javax.swing.JTextArea;
import java.awt.event.ActionListener;
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

public class Okno {

	public JFrame frame;
	private JTextField textField;
	private JButton btnWyslij;
	private JTextArea textArea;
	private Chat chatSession;
	private boolean flag;
	private String template;
	private String pattern;


	/**
	 * Create the application.
	 */
	public Okno(Chat session) {
		chatSession=session;
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.getContentPane().setBackground(new Color(102, 153, 255));
		frame.setBounds(100, 100, 439, 414);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JScrollPane scrollPane = new JScrollPane();
		
		textField = new JTextField();
		textField.setColumns(10);
		
		btnWyslij = new JButton("Wy\u015Blij");
		btnWyslij.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String request=textField.getText();
				String chat=textArea.getText();
				if(!request.equals("")) {
					if(!chat.equals(""))
						textArea.setText(chat+"\nMe: "+ request);
					else
						textArea.setText(chat+"Me: "+ request);
					textField.setText("");
					chat=textArea.getText();
					if(flag) {
						template=request;
						Path path = Paths.get("Chat/bots/super/aiml/baza.aiml");
						List<String> lines;
						try {
							lines = Files.readAllLines(path, StandardCharsets.UTF_8);
							int position = 2;
							String extraLine = "<category>\n\t<pattern>"+pattern+"</pattern>\n\t<template>"+template+"</template>\n</category>";  
							lines.add(position, extraLine);
							Files.write(path, lines, StandardCharsets.UTF_8);
							
							String botname = "super";
							String p = "./";
							Bot bot = new Bot(botname, p);
							chatSession = new Chat(bot);
							textArea.setText(chat+"\nBot: "+"Dziękuję!");
							flag=false;
						} catch (IOException e) {
							e.printStackTrace();
						}

						
					}
					
					else {
						
						String response=chatSession.multisentenceRespond(request);
						
						if(response.equals("I have no answer for that.")) {
							textArea.setText(chat+"\nBot: "+"Nie znam odpowiedzi, naucz mnie!");
							flag=true;
							StringBuffer sb = new StringBuffer();
							for(int i=0;i<request.length();i++) {
								if(request.charAt(i)!='?')
									sb.append(request.charAt(i));
							}
							pattern=sb.toString();
						}
						else
							textArea.setText(chat+"\nBot: "+response);
					}			
				}
			}
		});
		GroupLayout groupLayout = new GroupLayout(frame.getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 403, Short.MAX_VALUE)
							.addContainerGap())
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(textField, GroupLayout.DEFAULT_SIZE, 297, Short.MAX_VALUE)
							.addGap(18)
							.addComponent(btnWyslij, GroupLayout.PREFERRED_SIZE, 72, GroupLayout.PREFERRED_SIZE)
							.addGap(26))))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(65)
					.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 223, Short.MAX_VALUE)
					.addGap(18)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(textField, GroupLayout.PREFERRED_SIZE, 43, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnWyslij))
					.addGap(26))
		);
		
		textArea = new JTextArea();
		textArea.setForeground(new Color(0, 0, 153));
		textArea.setFont(new Font("Meiryo UI", Font.PLAIN, 12));
		textArea.setEditable(false);
		scrollPane.setViewportView(textArea);
		frame.getContentPane().setLayout(groupLayout);
	}
}
