package com.kitmenke.storm.bolt;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.TimeZone;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.impl.CloudSolrClient;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.common.SolrInputDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Tuple;

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

	@Override
	public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
		_collector = collector;
		
		try {
			LOG.info("Initializing CloudSolrClient");
            String urlString = "http://localhost:8983/solr/word_count_collection";
            _client = new HttpSolrClient.Builder(urlString).build();
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
			doc.addField("id", word);
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
