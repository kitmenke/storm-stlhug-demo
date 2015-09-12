package com.kitmenke.storm.bolt;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

/**
 * Bolt which counts words.
 * @author Kit Menke
 *
 */
public class WordCountBolt extends BaseBasicBolt {
	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LoggerFactory.getLogger(WordCountBolt.class);
	
	Map<String, Integer> counts = new HashMap<String, Integer>();

	@Override
	public void execute(Tuple tuple, BasicOutputCollector collector) {
		try {
			String word = tuple.getString(0);
			// check to see what the current count of this word is
			Integer count = counts.get(word);
			if (count == null)
				count = 0;
			count++;
			counts.put(word, count);
			collector.emit(new Values(word, count));
		} catch (Exception e) {
			LOG.error("WordCountBolt error", e);
			collector.reportError(e);
		}		
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("word", "count"));
	}
}