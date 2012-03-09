package edu.cs.iit.cs553;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * WordCount Java version main
 * 
 * @author Pedro Alvarez-Tabio
 * @author Jesus Hernandez
 * 
 */
public class WordCountJ {
	
	private static final int N_FILES = 0;

	// main execution: $ java WordCountJ [nThreads]
	public static void main(String args[]) throws Exception {

		int nThreads = Integer.parseInt(args[0]);
		
		// Makes a fixed thread pool of nThreads
		ExecutorService threadPool = Executors.newFixedThreadPool(nThreads);
		
		// Counts words with the pool of nThreads threads in N_FILES files
		// and reduces each results with a not-yet developed monitor
		// TODO: code the monitor that handles the reduce
		/*
		 * The monitor must have an array of N_FILES HashMaps and reduce in a 2-to-1
		 * fashion in each step, that is, each 2 HashMaps submitted by WordCountThreads, 
		 * reduce it to another one and erase those 2.
		 */
		// TODO: get rid of fixed N_FILES
		for (int i = 0; i < N_FILES; i++) {
			String file;
			
			// input file sanitization
			if (i < 10) {
				file = "../input_0" + i + ".txt";
			} else {
				file = "../input_" + i + ".txt";
			}
			
			// Execution of the thread pool
			threadPool.execute(new WordCountThread(file));
		}

	}

}
