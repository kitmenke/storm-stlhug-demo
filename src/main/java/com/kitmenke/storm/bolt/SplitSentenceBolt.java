package com.kitmenke.storm.bolt;

import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

public class SplitSentenceBolt extends BaseBasicBolt {
	private static final long serialVersionUID = 1L;

	@Override
	public void execute(Tuple tuple, BasicOutputCollector collector) {
		String sentence = tuple.getString(0);
		// split the sentence on each space into words
		String[] words = sentence.split(" ");
		for (String word : words) {
			// strip punctuation, make lowercase, etc..
			word = cleanWord(word);
			collector.emit(new Values(word));
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