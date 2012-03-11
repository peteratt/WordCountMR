if [[ -f wordcount.jar ]]; then
	rm wordcount.jar
fi

if [[ -d hadoopexec ]]; then
	rm -rf hadoopexec
fi

if [[ -d output ]]; then
	rm -rf output
fi

cp src/edu/cs/iit/cs553/hadoop/WordCountMR.java .
mkdir hadoopexec
javac -classpath $HADOOP_HOME/hadoop-core-0.20.203.0.jar:$HADOOP_HOME/lib/commons-cli-1.2.jar -d hadoopexec WordCountMR.java && jar -cvf wordcount.jar -C hadoopexec/ .
hadoop jar wordcount.jar edu.cs.iit.cs553.hadoop.WordCountMR
