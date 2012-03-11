WordCount: Java vs MapReduce

To run this application in a single node:

1. Install Hadoop, stable version recommended: http://www.apache.org/dyn/closer.cgi/hadoop/common/

2. Install Ant. In Ubuntu:

$ sudo apt-get install ant

3. Set the environmental variable HADOOP_HOME pointing to the root Hadoop installation directory, e.g.:

$ export HADOOP_HOME=~/hadoop-0.20.203.0

4. Edit the <hadoop install directory>/conf/hadoop-env.sh by uncommenting the following line. In Mac OS X it is:

# The java implementation to use.  Required.
export JAVA_HOME=/Library/Java/Home

And setting your JAVA_HOME correctly.

5. Insert the plain text files you want to count words in the input/ directory

6. Run javaexec.sh and hadoopexec.sh and enjoy!