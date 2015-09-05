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
import backtype.storm.topology.TopologyBuilder;

/**
 * WordCountTopology
 */
public class WordCountTopology {
	private static final Logger LOG = LoggerFactory.getLogger(WordCountTopology.class);

	public static void main(String[] args) throws Exception {
		LOG.info("Setting up WordCountTopology");
		TopologyBuilder builder = new TopologyBuilder();
		builder.setSpout("1-spout", new RandomSentenceSpout(), 1);
		builder.setBolt("2-split-sentence", new SplitSentenceBolt(), 1).shuffleGrouping("1-spout");
		builder.setBolt("3-count-words", new WordCountBolt(), 1).shuffleGrouping("2-split-sentence");
		builder.setBolt("4-output", new OutputBolt(), 1).shuffleGrouping("3-count-words");
		builder.setBolt("5-solr", new SolrIndexerBolt(), 1).shuffleGrouping("3-count-words");

		Config conf = new Config();
		

		if (args != null && args.length > 0) {
			LOG.info("Submitting topology with name {}", args[0]);
			conf.setNumWorkers(3);

			StormSubmitter.submitTopology(args[0], conf, builder.createTopology());
		} else {
			// turning debug on prints out when tuples are emitted 
			//conf.setDebug(true);
			
			conf.setMaxTaskParallelism(3);

			LocalCluster cluster = new LocalCluster();
			LOG.info("Submitting local topology, will be shutdown in 60s");
			cluster.submitTopology("CountingTopology", conf, builder.createTopology());

			Thread.sleep(60000);

			cluster.shutdown();
		}
	}
}
