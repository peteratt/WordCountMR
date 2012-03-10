package edu.cs.iit.cs553;

import java.util.Iterator;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class WordCountMonitor {
	
	private Queue<Map<String, Integer>> reduceQueue =
			new ConcurrentLinkedQueue<Map<String,Integer>>();
	
	public void publish(Map<String, Integer> partialResult) {
		reduceQueue.add(partialResult);
		
		synchronized (this) {
			if (reduceQueue.size() > 1) {
				Map<String, Integer> map1 = reduceQueue.poll();
				Map<String, Integer> map2 = reduceQueue.poll();
				
				Thread reducer = new Thread(new ReduceThread(map1, map2));
				reducer.start();
			}
			notifyAll();
		}
	}
	
	public Map<String, Integer> getLastMap() throws InterruptedException {
		synchronized (this) {
			while(reduceQueue.size() < 1) wait();
		}
		return reduceQueue.poll();
	}
	
	class ReduceThread implements Runnable {

		private Map<String, Integer> map1;
		private Map<String, Integer> map2;

		public ReduceThread(Map<String, Integer> source, Map<String, Integer> dest) {
			map1 = source;
			map2 = dest;
		}

		@Override
		public void run() {
			
			Iterator<String> i = map1.keySet().iterator();
			
			while (i.hasNext()) {
				String key = i.next();
				if (map2.containsKey(key)) {
					Integer prevVal = map2.get(key);
					map2.put(key, prevVal + map1.get(key));
				} else {
					map2.put(key, map1.get(key));
				}
			}
			
			// Publish the resulting map in the queue
			publish(map2);
			
			// Creates a StringBuilder to return the result all at a time
			// in a single println call
			StringBuilder sb = new StringBuilder();
			if (!(WordCountJ.mode == WordCountJ.MODE_QUIET)) {
				sb.append("Reducing 2 maps...\n");
			}

			// Print contents of the table in verbose mode
			if (WordCountJ.mode == WordCountJ.MODE_VERBOSE) {
				Iterator<String> wordsIterator = map2.keySet().iterator();

				while (wordsIterator.hasNext()) {
					String key = wordsIterator.next().toString();
					sb.append(key + ": " + map2.get(key) + "\n");
				}
			}
			System.out.print(sb);
		}
		
	}

}
