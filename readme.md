Storm Demo - St. Louis Hadoop User Group
----------------------------------------

A word count topology originally forked from the [storm-starter](https://github.com/apache/storm/tree/master/examples/storm-starter) project and modified for a St. Louis Hadoop User Group presentation.

Outline:

1. RandomSentenceSpout: emits random sentence tuples
1. SplitSentenceBolt: splits each sentence into word tuples
1. WordCountBolt: keeps track of counts for each word and emits (word, count) tuples
1. OutputBolt: LOG the current word and the count

Running the topology in local mode
----------------------------------

Assuming you're using Eclipse and you're able to open the project without errors. 

1. Open com.kitmenke.storm.WordCountTopology
1. Right click on the class, Debug As -> Java Application

Running the topology on a cluster
---------------------------------

For testing I'm using the Hortonworks HDP 2.3 Sandbox.

1. Build the project using `mvn clean package`
1. Upload the jar to a node in the cluster which has the storm client
1. Submit the topology: `storm jar storm-stlhug-demo-0.0.1-SNAPSHOT.jar com.kitmenke.storm.WordCountTopology WordCountTopology`

Environment
-----------

I developed this topology on Windows 10 using the following:

- Eclipse Mars IDE for Java Developers (includes maven and git)
- [TestNG plugin](http://testng.org/doc/download.html for Eclipse)
- Oracle Virtual Box + [HortonWorks HDP 2.3 Sandbox](Eclipse IDE for Java Developers) (not required for running storm in local mode)