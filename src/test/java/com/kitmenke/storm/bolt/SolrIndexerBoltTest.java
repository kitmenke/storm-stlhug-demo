package com.kitmenke.storm.bolt;

import org.testng.Assert;
import org.testng.annotations.Test;

public class SolrIndexerBoltTest {

  @Test
  public void getDateNow() {
	  SolrIndexerBolt bolt = new SolrIndexerBolt();
	  String date = bolt.getDateNow();
	  Assert.assertNotNull(date);
  }
}
