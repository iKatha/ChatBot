package com.kip.basketbot.controllers;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class ChatController {

	static public String[] parseGet(String get) {
		String[] values = get.split(";");
		return values;
	}

	static public String checkIfEmpty(String[] values) {
		String response = "ok";
		for (int i = 2; i < values.length; i++) {
			if (values[i].equals("unknown")) {
				switch (i) {
				case 2:
					response = "A jak się nazywa?";
					break;
				case 3:
					response = "A ile miał asyst?";
					break;
				case 4:
					response = "Powiedz jeszcze ile zdobył punktów.";
					break;
				case 5:
					response = "Muszę wiedzieć ile miał zbiórek.";
					break;
				}
				break;
			}
		}
		return response;
	}

	static public void learn(String pattern, String template) throws IOException {
		Path path = Paths.get(".\\bots\\super\\aiml\\baza.aiml");
		List<String> lines;
		lines = Files.readAllLines(path, StandardCharsets.UTF_8);
		int position = 2;
		String extraLine = "<category>\n\t<pattern>" + pattern + "</pattern>\n\t<template>" + template+ "</template>\n</category>";
		lines.add(position, extraLine);
		Files.write(path, lines, StandardCharsets.UTF_8);

	}
	
	static public String fuzzy(String assists, String rebounds, String points) {
		FuzzyController fc = new FuzzyController(assists,rebounds,points);
		String op=fc.playerStats();
		StringBuilder sb= new StringBuilder();
		for(int i=0;i<op.length();i++) {
			if(op.charAt(i)==',')
				sb.append(".");
			else
				sb.append(op.charAt(i));
		}
		double opinion=Double.parseDouble(sb.toString());
		String response="";
		if(opinion<3.5)
			response="bad";
		else if(opinion>=3.5 && opinion <6)
			response="average";
		else
			response="good";
		return response;
	}
	
	static public String checkPoints(String get) {
		String response="isOk";
		String [] values = parseGet(get);
		int points=Integer.parseInt(values[4]);
		if(points>50 || points<0)
			response="To niemożliwe.";
		return response;
	}
	
	static public String checkRebounds(String get) {
		String response="isOk";
		String [] values = parseGet(get);
		int rebounds=Integer.parseInt(values[5]);	
		if(rebounds<0 || rebounds>20)
			response="To niemożliwe.";
		return response;
	}

	static public String checkAssists(String get) {
		String response="isOk";
		String [] values = parseGet(get);
		int assists=Integer.parseInt(values[3]);
		if(assists>15 || assists<0)
			response="To niemożliwe.";
		return response;
	}
}
