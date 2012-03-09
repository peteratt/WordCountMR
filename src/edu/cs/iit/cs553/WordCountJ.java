package edu.cs.iit.cs553;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class WordCountJ {
	/// JESUS
	/**
	 * @param args
	 */
	public static void main (String args[]) throws Exception {
		
		if (2 < args.length)
			throw (new Exception("Syntax: Count <dest> <src>"));
		
		BufferedReader i = new BufferedReader(new InputStreamReader(System.in));
		if (args.length > 0)
			i = new BufferedReader(new FileReader(args[0]));
		
		PrintWriter o = new PrintWriter(System.out);
		if (args.length == 2)
			o = new PrintWriter(new FileWriter(args[1]));
		
		String line, text = "";
		while ((line = i.readLine()) != null) {
			text = text + " " + line;
		}

		if (text.matches("[^a-zA-Z]*\\Z"))
			text = ""; // elimina ficheros sin palabras
		
		// Versi�n 1.0: Reconoce palabras sin guion del espa�ol
		
		// Versi�n 1.1: Reconoce palabras con guion del espa�ol (no salto de l�nea)
		// "[^a-zA-Z���-���]*[a-zA-Z���-���]+-?[a-zA-Z���-���]*[^a-zA-Z���-���]*"
		// NOTA: la palabra 9Hola56 la reconoce como palabra, no deber�a
		
		// Versi�n 1.2: Reconoce n�meros con decimal y signo
		// "[^a-zA-Z���-���]*(([a-zA-Z���-���]+-?[\na-zA-Z���-���])|([0-9]+?,?[0-9]*))*[^a-zA-Z���-���]*"
		
		// Versi�n 1.3: Reconoce ya con salto de l�nea
		// "[^a-zA-Z���-���]*(([a-zA-Z���-���]+-?[\\\na-zA-Z���-���])|([0-9]+?,?[0-9]*))*[^a-zA-Z���-���]*"
		String regexPalabras = 
			"[^a-zA-Z���-���]*(([a-zA-Z���-���]+-?[\\\na-zA-Z���-���])|([0-9]+?,?[0-9]*))*[^a-zA-Z���-���]*";
				
		// No me funciona la regex que aparece en las transparencias: "[^a-zA-Z]*([a-zA-Z]+[^a-zA-Z]+)+"
		// pero hago split y s� que va
		String regexFrases = "[.?!]+";
				
		text = "�lvarez-Tab�o Cig�e�a CIG�E�A cam-\npana. -98517,5325."; // Prueba sin cargar desde archivo

		text = "�cig�e�as CIG�E�AS! tienen   \n    34662,1123 metros de envergadura";
		
		System.out.println(text);
		
		int numPalabras = text.replaceAll(regexPalabras, "w").length();
				
		// Aqu�, en vez de replaceAll, con split y mi regex va bien
		int numFrases = text.split(regexFrases).length;
		
		int numCaracteres = text.length();
		
		// �Por qu� si cargo desde archivo me cuenta siempre una palabra de m�s?
		
		o.println("Caracteres: " + numCaracteres + ", " + 
				"Palabras: " + numPalabras + ", " + "Frases: " + numFrases);
		
		o.close();
		i.close();
	}

}
