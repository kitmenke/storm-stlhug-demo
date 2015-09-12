package com.kitmenke.storm.bolt;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.TimeZone;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.impl.CloudSolrClient;
import org.apache.solr.common.SolrInputDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Tuple;

/**
 * Bolt which indexes a word and the current count in Solr Cloud
 * @author Kit Menke
 *
 */
public class SolrIndexerBolt extends BaseRichBolt {
	private static final long serialVersionUID = 1L;
	private static Logger LOG = LoggerFactory.getLogger(SolrIndexerBolt.class);
	OutputCollector _collector;
	SolrClient _client;
	
	@SuppressWarnings("rawtypes")
	@Override
	public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
		_collector = collector;
		
		try {
			LOG.info("Initializing CloudSolrClient");
			String zkHosts = "localhost:2181";
			CloudSolrClient client = new CloudSolrClient(zkHosts);
			client.setDefaultCollection("word_count_collection");
			client.setIdField("word");
			_client = client;
		} catch (Exception e) {
			LOG.error("SolrIndexerBolt prepare error", e);
			_collector.reportError(e);
		}
	}

	@Override
	public void execute(Tuple input) {
		try {
			String word = input.getString(0);
			int count = input.getInteger(1);
			
			SolrInputDocument doc = new SolrInputDocument();
			doc.addField("word", word);
			doc.addField("count", count);
			doc.addField("updated", getDateNow());
			_client.add(doc, 5000);
			
			LOG.info("Indexed {} = {}", word, count);
		} catch (Exception e) {
			LOG.error("SolrIndexerBolt error", e);
			_collector.reportError(e);
		}	
	}
	
	public String getDateNow() {
		SimpleDateFormat dateFormatGmt = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
		dateFormatGmt.setTimeZone(TimeZone.getTimeZone("GMT"));
		return dateFormatGmt.format(new Date());
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		// TODO Auto-generated method stub
	}
}
