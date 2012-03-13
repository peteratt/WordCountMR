package edu.cs.iit.cs553.hadoop;

import java.io.IOException;
import java.lang.InterruptedException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

/**
 * Class for the Hadoop version of WordCount
 * 
 * @author palvare3
 * @author jherna22
 *
 */
public class WordCountMR {
	/**
	 * The map class of WordCount.
	 */
	public static class WordCountMapper extends
			Mapper<Object, Text, Text, IntWritable> {

		// Fields for output
		private final static IntWritable one = new IntWritable(1);
		private Text word = new Text();
		
		/*
		 * This "homemade" regex handles words like:
		 * 
		 * six-year-old They're you--I doin'
		 */
		String regexWords = "([a-zA-Z]+-{0,2})*([a-zA-Z]+'?)*[a-zA-Z]+'?";
		Pattern wordCountPattern;
		Matcher m;

		public void map(Object key, Text value, Context context)
				throws IOException, InterruptedException {
			
			// Regex pattern to avoid error cases
			wordCountPattern = Pattern.compile(regexWords);
			m = wordCountPattern.matcher(value.toString());

			// Iterate over words found in the line
			while (m.find()) {
				// IMPORTANT: lower case the words so we don't have problems
				word.set(m.group().toLowerCase());
				context.write(word, one);
			}
		}
	}

	/**
	 * The reducer class of WordCount
	 */
	public static class WordCountReducer extends
			Reducer<Text, IntWritable, Text, IntWritable> {
		public void reduce(Text key, Iterable<IntWritable> values,
				Context context) throws IOException, InterruptedException {
			int sum = 0;
			// Sums all the <word, nTimes> pairs
			for (IntWritable value : values) {
				sum += value.get();
			}
			context.write(key, new IntWritable(sum));
		}
	}

	/**
	 * The main entry point.
	 */
	public static void main(String[] args) throws Exception {
		// Initial timestamp
		long initialTime = System.currentTimeMillis();
				
		Configuration conf = new Configuration();
		
		Job job = new Job(conf, "Hadoop WordCount");
		job.setJarByClass(WordCountMR.class);
		
		// Sets mapper and reducer
		job.setMapperClass(WordCountMapper.class);
		job.setReducerClass(WordCountReducer.class);
				
		// Sets output keys and values
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(IntWritable.class);
		
		FileInputFormat.addInputPath(job, new Path("./input"));
		FileOutputFormat.setOutputPath(job, new Path("./output"));
		
		// Issue here: Hadoop returns sorted keys, different from our Java implementation
		// It spends more time
		boolean completed = job.waitForCompletion(true);
		// Final timestamp
		long finalTime = System.currentTimeMillis();
		
		long totalTime = finalTime - initialTime;
		System.out.println("Word Count Hadoop time: " + totalTime + " ms");
		
		System.exit(completed ? 0 : 1);
	}
}