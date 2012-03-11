import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Word counting thread
 * 
 * @author Pedro Alvarez-Tabio
 * @author Jesus Hernandez
 * 
 */
public class WordCountThread implements Runnable {

	private String filename;
	WordCountMonitor monitor;

	public WordCountThread(String file, WordCountMonitor m) {
		filename = file;
		monitor = m;
	}

	@Override
	public void run() {

		try {

			BufferedReader in = new BufferedReader(new FileReader(filename));

			/*
			 * This "homemade" regex handles words like:
			 * 
			 * six-year-old They're you--I doin'
			 */
			String regexWords = "([a-zA-Z]+-{0,2})*([a-zA-Z]+'?)*[a-zA-Z]+";
			Pattern wordCountPattern = Pattern.compile(regexWords);
			Matcher m;

			// Creates a HashMap with <word, nTimesAppeared>
			Map<String, Integer> words = new HashMap<String, Integer>();
			String line = "";

			while ((line = in.readLine()) != null) {

				m = wordCountPattern.matcher(line);

				// Iterate over words found in the line
				while (m.find()) {
					String foundWord = m.group();

					// IMPORTANT: lower case the words so we don't have problems
					foundWord = foundWord.toLowerCase();

					if (words.containsKey(foundWord)) {
						// Increments the index: <word, nTimesAppeared + 1>
						int result = words.get(foundWord) + 1;
						words.put(foundWord, result);
					} else {
						// Puts a new word
						words.put(foundWord, 1);
					}
				}
			}
			
			StringBuilder sb = new StringBuilder();
			
			if (!(WordCountJ.mode == WordCountJ.MODE_QUIET)) {
				sb.append("Counting words of " + filename + "...\n");
			}

			// Print contents of the table in verbose mode
			if (WordCountJ.mode == WordCountJ.MODE_VERBOSE) {
				Iterator<String> wordsIterator = words.keySet().iterator();

				while (wordsIterator.hasNext()) {
					String key = wordsIterator.next().toString();
					sb.append(key + ": " + words.get(key) + "\n");
				}
			}
			System.out.print(sb);
			monitor.publish(words);
			
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
