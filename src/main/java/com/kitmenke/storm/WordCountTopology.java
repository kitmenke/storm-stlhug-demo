package com.kitmenke.storm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kitmenke.storm.bolt.OutputBolt;
import com.kitmenke.storm.bolt.SolrIndexerBolt;
import com.kitmenke.storm.bolt.SplitSentenceBolt;
import com.kitmenke.storm.bolt.WordCountBolt;
import com.kitmenke.storm.spout.RandomSentenceSpout;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.StormSubmitter;
import backtype.storm.generated.AlreadyAliveException;
import backtype.storm.generated.AuthorizationException;
import backtype.storm.generated.InvalidTopologyException;
import backtype.storm.topology.TopologyBuilder;

/**
 * A Word Counting Storm topology.
 * 
 * To submit the topology to a cluster:
 * storm jar storm-stlhug-demo-0.0.1-SNAPSHOT.jar com.kitmenke.storm.WordCountTopology WordCountTopology
 * @author Kit Menke
 *
 */
public class WordCountTopology {
	
	private static final Logger LOG = LoggerFactory.getLogger(WordCountTopology.class);
	private static final String SPOUT = "1-spout";
	private static final String SPLIT_BOLT = "2-split-sentence";
	private static final String COUNT_BOLT = "3-count-words";
	private static final String LOG_BOLT = "4-output";
	private static final String SOLR_BOLT = "5-solr";
	private static final int NUM_TASKS = 1;

	public static void main(String[] args) throws Exception {
		LOG.info("Setting up WordCountTopology");
		TopologyBuilder builder = new TopologyBuilder();
		// spout generates random sentences
		builder.setSpout(SPOUT, new RandomSentenceSpout(), NUM_TASKS);
		// split the sentences into words
		builder.setBolt(SPLIT_BOLT, new SplitSentenceBolt(), NUM_TASKS).shuffleGrouping(SPOUT);
		// count the words
		builder.setBolt(COUNT_BOLT, new WordCountBolt(), NUM_TASKS).shuffleGrouping(SPLIT_BOLT);
		// log each word and the current count
		builder.setBolt(LOG_BOLT, new OutputBolt(), NUM_TASKS).shuffleGrouping(COUNT_BOLT);
		
		Config conf = new Config();
		
		if (args != null && args.length > 0) {
			// index each word and the current count in solr
			builder.setBolt(SOLR_BOLT, new SolrIndexerBolt(), NUM_TASKS).shuffleGrouping(COUNT_BOLT);			
			runClusterTopology(args, builder, conf);
		} else {
			
			runLocalTopology(builder, conf);
		}
	}

	/**
	 * Submit the topology to a cluster
	 * @param args
	 * @param builder
	 * @param conf
	 * @throws AlreadyAliveException
	 * @throws InvalidTopologyException
	 * @throws AuthorizationException
	 */
	private static void runClusterTopology(String[] args, TopologyBuilder builder, Config conf)
			throws AlreadyAliveException, InvalidTopologyException, AuthorizationException {
		LOG.info("Submitting topology with name {}", args[0]);
		conf.setNumWorkers(3);
		StormSubmitter.submitTopology(args[0], conf, builder.createTopology());
	}

	/**
	 * Run the cluster locally (inside eclipse on your desktop for example)
	 * @param builder
	 * @param conf
	 * @throws InterruptedException
	 */
	private static void runLocalTopology(TopologyBuilder builder, Config conf) throws InterruptedException {
		conf.setMaxTaskParallelism(3);
		// turning debug on prints out when tuples are emitted 
		//conf.setDebug(true);
		
		LocalCluster cluster = new LocalCluster();
		LOG.info("Submitting local topology, will be shutdown in 60s");
		cluster.submitTopology("CountingTopology", conf, builder.createTopology());

		Thread.sleep(60000);

		cluster.shutdown();
	}
}
