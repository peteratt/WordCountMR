import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * WordCount Java version main
 * 
 * @author Pedro Alvarez-Tabio
 * @author Jesus Hernandez
 * 
 */
public class WordCountJ {
	
	public static final int MODE_VERBOSE = 1;
	public static final int MODE_QUIET = 2;
	
	public static int mode = 0;

	// main execution: $ java WordCountJ [-t nThreads] [-v]
	public static void main(String[] args) {
		
		// Initial timestamp
		long initialTime = System.currentTimeMillis();

		int nThreads = 1;
		
		for (int i = 0; i < args.length; i++) {
			if (args[i].equals("-v")) {
				mode = MODE_VERBOSE;
			} else if (i < args.length - 1 && args[i].equals("-t")) {
				try {
					nThreads = Integer.parseInt(args[i + 1]);
				} catch (Exception e) {
					System.out.println("nThreads not valid, using 1 thread");
				}
			} else if (args[i].equals("-q")) {
				mode = MODE_QUIET;
			}
		}
		
		// Makes a fixed thread pool of nThreads
		ExecutorService threadPool = Executors.newFixedThreadPool(nThreads);
		
		WordCountMonitor monitor = new WordCountMonitor();
		
		// Counts files in input folder
		File f = new File("./input");
		int nFiles = f.listFiles().length;
		
		// Counts words with the pool of nThreads threads in N_FILES files
		// and reduces each results with a not-yet developed monitor
		// TODO: code the monitor that handles the reduce DONE, TEST NEXT
		/*
		 * The monitor must have an array of N_FILES HashMaps and reduce in a 2-to-1
		 * fashion in each step, that is, each 2 HashMaps submitted by WordCountThreads, 
		 * reduce it to another one and erase those 2.
		 */
		// TODO: get rid of fixed N_FILES
		for (int i = 0; i < nFiles; i++) {
			String file;
			
			// input file sanitization
			if (i < 10) {
				file = "./input/input_0" + i + ".txt";
			} else {
				file = "./input/input_" + i + ".txt";
			}
			
			// Execution of the thread pool
			threadPool.execute(new WordCountThread(file, monitor));
		}
		
		threadPool.shutdown();
		try {
			threadPool.awaitTermination(Integer.MAX_VALUE, TimeUnit.MINUTES);
		
			PrintWriter out = new PrintWriter("./output/results.txt");
			
			Map<String, Integer> lastMap = new TreeMap<String, Integer>(monitor.getLastMap());
			Iterator<String> lastMapIterator = lastMap.keySet().iterator();
			
			while (lastMapIterator.hasNext()) {
				String key = lastMapIterator.next().toString();
				out.println(key + " " + lastMap.get(key));
			}
			
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		
		long finalTime = System.currentTimeMillis();
		
		long totalTime = finalTime - initialTime;
		System.out.println("Word Count Java time: " + totalTime + " ms");
	}

}
