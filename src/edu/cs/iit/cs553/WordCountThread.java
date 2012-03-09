package edu.cs.iit.cs553;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
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
			text = "Álvarez-Tabío Cigüeña CIGÜEÑA cam-\npana. -98517,5325."; // Prueba sin cargar desde archivo
			text = "¿cigüeñas CIGÜEÑAS! tienen   \n    34662,1123 metros de envergadura";
			System.out.println(text);
			Pattern p = Pattern.compile(regexWords);
			Matcher m = p.matcher(text);
			
			Map<String, Integer> words = new HashMap<String, Integer>();
			
			while (m.find()) {
				String testWord = m.group();
				if (words.containsKey(testWord)) {
					int result = words.get(testWord) + 1;
					words.put(testWord, result);
				} else {
					words.put(testWord, 1);
				}
			}
			
			i.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
