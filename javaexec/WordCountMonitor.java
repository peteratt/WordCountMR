import java.util.Iterator;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * The WordCount monitor. It contains a queue of HashMaps and triggers the 
 * reduce threads to merge the HashMaps.
 * 
 * @author palvare3
 * @author jherna22
 *
 */
public class WordCountMonitor {
	
	private Queue<Map<String, Integer>> reduceQueue =
			new ConcurrentLinkedQueue<Map<String,Integer>>();
	
	private volatile boolean finished = false;

	public static int nReduceThreads = 0;
	
	public void publish(Map<String, Integer> partialResult) {
		reduceQueue.add(partialResult);
		
		synchronized (this) {
			if (reduceQueue.size() > 1) {
				// Takes the first two hash tables and merges them
				Map<String, Integer> map1 = reduceQueue.poll();
				Map<String, Integer> map2 = reduceQueue.poll();
				
				// With the use of a ReduceThread
				Thread reducer = new Thread(new ReduceThread(map1, map2));
				reducer.start();
			}
			// Notifies the rest of the threads. Useful when the main
			// thread is waiting.
			notifyAll();
		}
	}
	
	public Map<String, Integer> getLastMap() throws InterruptedException {
		synchronized (this) {
			// The main thread enters here and waits until all the threads
			// have finished.
			while(!finished) wait();
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
			synchronized (this) {
				nReduceThreads++;
			}
			
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
			
			// Publish the resulting map in the queue. When there's only
			// one reduce remaining and the queue size is 0, and the main 
			// thread has already entered the getLastMap() and is waiting,
			// finished = true and the main thread can continue.
			if (reduceQueue.size() == 0 && nReduceThreads == 1) {
				finished = true;
			}
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
			
			synchronized (this) {
				nReduceThreads--;
			}
		}
		
	}

}
