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
		if (reduceQueue.size() > 1) {
			Thread reducer = new Thread(new ReduceThread());
			reducer.start();
		}
		synchronized (this) {
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

		@Override
		public void run() {
			
			Map<String, Integer> map1 = reduceQueue.poll();
			Map<String, Integer> map2 = reduceQueue.poll();
			
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
			
			publish(map2);
			
			StringBuilder sb = new StringBuilder();
			sb.append("Reducing 2 maps... Result:\n");

			// Print contents of the table in verbose mode
			if (WordCountJ.mode == WordCountJ.MODE_VERBOSE) {
				Iterator<String> wordsIterator = map2.keySet().iterator();


				sb.append("Reducing 2 maps... Result:\n");
				while (wordsIterator.hasNext()) {
					String key = wordsIterator.next().toString();
					sb.append(key + ": " + map2.get(key) + "\n");
				}
			}
			System.out.print(sb);
		}
		
	}

}
