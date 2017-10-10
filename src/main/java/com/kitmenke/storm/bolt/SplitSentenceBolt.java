package com.kitmenke.storm.bolt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.storm.topology.BasicOutputCollector;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseBasicBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;

/**
 * Bolt which splits an incoming sentences into words using whitespace as a delimiter. 
 * @author Kit Menke
 *
 */
public class SplitSentenceBolt extends BaseBasicBolt {
	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LoggerFactory.getLogger(SplitSentenceBolt.class);

	@Override
	public void execute(Tuple tuple, BasicOutputCollector collector) {
		try {
			String sentence = tuple.getString(0);
			// split the sentence on each space into words
			String[] words = sentence.split(" ");
			for (String word : words) {
				// strip punctuation, make lowercase, etc..
				word = cleanWord(word);
				collector.emit(new Values(word));
			}
		} catch (Exception e) {
			LOG.error("SplitSentenceBolt error", e);
			collector.reportError(e);
		}	
	}
	
	private String cleanWord(String word) {
		if (word == null) {
			return null;
		}
		return word.toLowerCase().replaceAll("[^a-z]", "");
	}

	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("word"));
	}
}