package com.kip.basketbot.controllers;
import java.awt.EventQueue;

import org.alicebot.ab.Bot;
import org.alicebot.ab.Chat;

import com.kip.basketbot.view.Okno;

public class ChatBot {

	public static void main(String[] args) {
		String botname = "super";
		String path = "./";
		Bot bot = new Bot(botname, path);
		Chat chatSession = new Chat(bot);
		
				try {
					Okno window = new Okno(chatSession);
					window.frmBasketbot.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
	
	}
}
