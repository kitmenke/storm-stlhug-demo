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

import org.apache.storm.topology.BasicOutputCollector;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;

public class SplitSentenceBoltTest {

	private List<Object> newListWithOneString(String val) {
		List<Object> list = new ArrayList<Object>(1);
		list.add(val);
		return list;
	}
	
	@Test
	public void legalSentenceShouldEmitFiveWords() {
		// given
		Tuple tuple = mock(Tuple.class);
	    when(tuple.getString(0)).thenReturn("milk was a bad choice");
	    
	    SplitSentenceBolt bolt = new SplitSentenceBolt();
	    BasicOutputCollector collector = mock(BasicOutputCollector.class);
	    
	    // when
	    bolt.execute(tuple, collector);
	    
	    // then
	    verify(collector).emit(newListWithOneString("milk"));
	    verify(collector).emit(newListWithOneString("was"));
	    verify(collector).emit(newListWithOneString("a"));
	    verify(collector).emit(newListWithOneString("bad"));
	    verify(collector).emit(newListWithOneString("choice"));
	}
	
	@Test
	public void shouldMakeWordsLowercaseAndRemovePunctuation() {
		// given
		Tuple tuple = mock(Tuple.class);
	    when(tuple.getString(0)).thenReturn("Boy, that escalated QUICKLY...");
	    
	    SplitSentenceBolt bolt = new SplitSentenceBolt();
	    BasicOutputCollector collector = mock(BasicOutputCollector.class);
	    
	    // when
	    bolt.execute(tuple, collector);
	    
	    // then
	    verify(collector).emit(newListWithOneString("boy"));
	    verify(collector).emit(newListWithOneString("that"));
	    verify(collector).emit(newListWithOneString("escalated"));
	    verify(collector).emit(newListWithOneString("quickly"));
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
