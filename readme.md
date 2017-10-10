Storm Demo - St. Louis Hadoop User Group
----------------------------------------

Last updated October 2017.

A word count topology originally forked from the [storm-starter](https://github.com/apache/storm/tree/master/examples/storm-starter) project and modified for a St. Louis Hadoop User Group presentation.

Outline:

1. RandomSentenceSpout: emits random sentence tuples
1. SplitSentenceBolt: splits each sentence into word tuples
1. WordCountBolt: keeps track of counts for each word and emits (word, count) tuples
1. OutputBolt: LOG the current word and the count
1. SolrIndexerBolt: Index the current word and the count in Solr

Environment
-----------

I developed this topology on Windows 10 using the following:

- IntelliJ Community Edition 2017.2
- Storm 1.0.5 (local mode)
- Solr 7.0.1 + Banana ?.?.? (installed separately)

Demo Hints
----------

1. Start solr with .\bin\solr.cmd start
1. Bring up solr admin http://localhost:8983/solr/#/
1. Bring up banana http://localhost:8983/solr/banana/#/dashboard/solr/Word%20Dashboard?server=%2Fsolr%2F
1. Set auto-refresh to 3s and past 5 min
1. Clean up word_count_collection: .\bin\solr.cmd delete -c word_count_collection
1. Recreate word_count_collection: .\bin\solr.cmd create -c word_count_collection -d word_count_configs
1. Open up project in IntelliJ and debug WordCountTopology
1. Demo!
1. Stop solr .\bin\solr.cmd stop -all

Running the topology in local mode
----------------------------------

Clone the project and open it in Eclipse. Make sure you're able to execute a maven build without errors. 

1. Open com.kitmenke.storm.WordCountTopology
1. Right click on the class, Debug As -> Java Application

Running the topology on a cluster
---------------------------------

1. Build the project using `mvn clean package`
1. Upload the jar to the cluster
1. Submit the topology: `storm jar storm-stlhug-demo-0.0.1-SNAPSHOT.jar com.kitmenke.storm.WordCountTopology WordCountTopology`

Solr and Banana
---------------

Download solr from https://lucene.apache.org/solr/ and install https://lucene.apache.org/solr/guide/7_0/installing-solr.html#installing-solr

```
.\bin\solr.cmd start
```

Validate you can get to the Solr UI: http://localhost:8983/solr/

Copy word_count_configs to solr-7.0.1\server\solr\configsets example https://github.com/apache/lucene-solr/blob/releases/lucene-solr/6.0.0/solr/server/solr/configsets/basic_configs/conf/managed-schema
```
.\bin\solr.cmd create -c banana-int
.\bin\solr.cmd create -c word_count_collection -d word_count_configs
```

Install Banana from https://github.com/LucidWorks/banana/

Browse to Banana: http://localhost:8983/solr/banana/#/dashboard

Import *Word Dashboard.json* into Banana to create the dashboard.

Other useful commands / notes
----------------------------

Try creating an example document using the Solr admin UI: http://127.0.0.1:8983/solr/#/word_count_collection_shard1_replica1/documents
```
{
"id": "zombie", 
"count": 24,
"updated": "2015-09-05T21:28:00Z"
}
```

Delete a collection and all data in it
```
.\bin\solr.cmd delete -c word_count_collection
```