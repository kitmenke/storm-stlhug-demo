package com.kitmenke.storm.bolt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.storm.topology.BasicOutputCollector;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseBasicBolt;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;

/**
 * Bolt which logs each word and the current count. 
 * @author Kit Menke
 *
 */
public class OutputBolt extends BaseBasicBolt {
	private static final long serialVersionUID = 1L;
	private static Logger LOG = LoggerFactory.getLogger(OutputBolt.class);

	@Override
	public void execute(Tuple tuple, BasicOutputCollector collector) {
		try {
			String word = tuple.getString(0);
			int count = tuple.getInteger(1);
			LOG.info("{} = {}", word, count);
			collector.emit(new Values(word, count));
		} catch (Exception e) {
			LOG.error("OutputBolt error", e);
			collector.reportError(e);
		}
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		// TODO Auto-generated method stub

	}

}
