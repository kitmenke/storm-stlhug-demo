package com.kitmenke.storm.spout;

import java.util.Map;
import java.util.Random;

import org.apache.storm.spout.SpoutOutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichSpout;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;
import org.apache.storm.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
				"And you ate the whole wheel of cheese?", 
				"Open the pod bay doors, HAL.",
				"Space... The final frontier.",
				"These are the voyages of the starship Enterprise.",
				"It's continuing mission, to explore strange new worlds.",
				"To seek out new life and new civilizations.",
				"To boldly go where no one has gone before.",
				"Tea, Earl Grey, hot."};
		String sentence = sentences[_rand.nextInt(sentences.length)];
		LOG.debug("RandomSentenceSpout generated this sentence: {}", sentence);
		_collector.emit(new Values(sentence));
		Utils.sleep(1000);
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