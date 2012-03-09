package edu.cs.iit.cs553;

/**
 * WordCount Java version main
 *
 */
public class WordCountJ {
	
	private static final int N_FILES = 0;
	private static int busyThreads = 0;
	
	public static void main (String args[]) throws Exception {
		
		int nThreads = Integer.parseInt(args[0]);
		
		for (int i = 0; i < N_FILES; i++) {
			
			while (getBusyThreads() == nThreads) {
				Thread.wait();
			}
			String filename = "../input_" + i + ".txt";
			WordCountThread wc = new WordCountThread(filename);
			Thread tWc = new Thread(wc);
			tWc.start();
			incrementBusyThreads();
		}
		
	}
	
	private static synchronized int getBusyThreads() {
		return busyThreads;
	}
	
	private static synchronized void incrementBusyThreads() {
		busyThreads++;
	}
	
	public static synchronized void decrementBusyThreads() {
		busyThreads--;
	}
	
}

