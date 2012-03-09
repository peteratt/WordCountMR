package edu.cs.iit.cs553;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WordCountThread implements Runnable {
	
	private String filename;
	
	public WordCountThread(String file) {
		filename = file;
	}

	@Override
	public void run() {
		
		try {
			BufferedReader i = new BufferedReader(new FileReader(filename));
			String line, text = "";
			while ((line = i.readLine()) != null) {
				text = text + " " + line;
			}
			String regexWords = "([a-zA-Z]+-{0,2})*([a-zA-Z]+'?)*[a-zA-Z]+";
			text = "�lvarez-Tab�o Cig�e�a CIG�E�A cam-\npana. -98517,5325."; // Prueba sin cargar desde archivo
			text = "�cig�e�as CIG�E�AS! tienen   \n    34662,1123 metros de envergadura";
			System.out.println(text);
			Pattern p = Pattern.compile(regexWords);
			Matcher m = p.matcher(text);
			while (m.find()) {
				m.group();
			}
			i.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
