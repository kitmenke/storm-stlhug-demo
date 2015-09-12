package com.kitmenke.storm.spout;

import java.util.Map;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;
import backtype.storm.utils.Utils;

/**
 * Spout which generates random sentences.
 * @author Kit Menke
 *
 */
public class RandomSentenceSpout extends BaseRichSpout {
	private static final long serialVersionUID = 1L;

	private static final Logger LOG = LoggerFactory.getLogger(RandomSentenceSpout.class);

	SpoutOutputCollector _collector;
	Random _rand;

	@SuppressWarnings("rawtypes")
	@Override
	public void open(Map conf, TopologyContext context,
			SpoutOutputCollector collector) {
		_collector = collector;
		_rand = new Random();
	}

	@Override
	public void nextTuple() {
		String[] sentences = new String[] { 
				"Don't act like you're not impressed.",
				"Boy, that escalated quickly... I mean, that really got out of hand fast.",
				"I immediately regret this decision.",
				"I'm in a glass case of emotion!",
				"I love lamp.", 
				"They've done studies, you know. 60% of the time, it works every time.",
				"You know I don’t speak Spanish.",
				"And you ate the whole wheel of cheese?"};
		String sentence = sentences[_rand.nextInt(sentences.length)];
		LOG.debug("RandomSentenceSpout generated this sentence: {}", sentence);
		_collector.emit(new Values(sentence));
		Utils.sleep(10000);
	}
	
	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("word"));
	}

	@Override
	public void ack(Object id) {
	}

	@Override
	public void fail(Object id) {
	}
}