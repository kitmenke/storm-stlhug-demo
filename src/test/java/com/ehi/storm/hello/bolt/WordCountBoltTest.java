package com.ehi.storm.hello.bolt;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.testng.annotations.Test;

import com.kitmenke.storm.bolt.SplitSentenceBolt;
import com.kitmenke.storm.bolt.WordCountBolt;

import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;

public class WordCountBoltTest {

	private List<Object> newListWithOneStringAndOneInt(String val, int num) {
		List<Object> list = new ArrayList<Object>(1);
		list.add(val);
		list.add(num);
		return list;
	}
	
	@Test
	public void shouldIncrementCountWhenTheSameTupleIsProcessed() {
		// given
		Tuple tuple = mock(Tuple.class);
	    when(tuple.getString(0)).thenReturn("wabbits");
	    
	    Tuple tuple2 = mock(Tuple.class);
	    when(tuple2.getString(0)).thenReturn("carrots");
	    
	    
	    WordCountBolt bolt = new WordCountBolt();
	    BasicOutputCollector collector = mock(BasicOutputCollector.class);
	    
	    // when
	    bolt.execute(tuple, collector);
	    bolt.execute(tuple2, collector); // different one
	    bolt.execute(tuple, collector); // execute again
	    
	    // then
	    verify(collector).emit(newListWithOneStringAndOneInt("wabbits", 1));
	    verify(collector).emit(newListWithOneStringAndOneInt("carrots", 1));
	    verify(collector).emit(newListWithOneStringAndOneInt("wabbits", 2)); // new count
	}
	
	@Test
	public void shouldDeclareOutputFields() {
		// given
		OutputFieldsDeclarer declarer = mock(OutputFieldsDeclarer.class);
		SplitSentenceBolt bolt = new SplitSentenceBolt();

		// when
		bolt.declareOutputFields(declarer);

		// then
		verify(declarer, times(1)).declare(any(Fields.class));
	}
}
