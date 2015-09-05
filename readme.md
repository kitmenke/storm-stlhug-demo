Storm Demo - St. Louis Hadoop User Group
----------------------------------------

A word count topology originally forked from the [storm-starter](https://github.com/apache/storm/tree/master/examples/storm-starter) project and modified for a St. Louis Hadoop User Group presentation.

Outline:

1. RandomSentenceSpout: emits random sentence tuples
1. SplitSentenceBolt: splits each sentence into word tuples
1. WordCountBolt: keeps track of counts for each word and emits (word, count) tuples
1. OutputBolt: LOG the current word and the count

Running the topology in local mode
----------------------------------

Assuming you're using Eclipse and you're able to open the project without errors. 

1. Open com.kitmenke.storm.WordCountTopology
1. Right click on the class, Debug As -> Java Application

Running the topology on a cluster
---------------------------------

For testing I'm using the Hortonworks HDP 2.3 Sandbox.

1. Build the project using `mvn clean package`
1. Upload the jar to a node in the cluster which has the storm client
1. Submit the topology: `storm jar storm-stlhug-demo-0.0.1-SNAPSHOT.jar com.kitmenke.storm.WordCountTopology WordCountTopology`

Environment
-----------

I developed this topology on Windows 10 using the following:

- Eclipse Mars IDE for Java Developers (includes maven and git)
- [TestNG plugin](http://testng.org/doc/download.html for Eclipse)
- Oracle Virtual Box
- [HortonWorks HDP 2.3 Sandbox](http://hortonworks.com/products/hortonworks-sandbox/)
- Solr 5.2.1 (already installed on the HDP 2.3 sandbox)
- Storm 0.10.0.2.3.0.0-2557 (already installed on the HDP 2.3 sandbox)
- Banana 1.5.0

Solr and Banana
---------------

As part of the demo, we'll show indexing data in Solr. The HDP 2.3 Sandbox comes with solr installed in /opt/lucidworks-hdpsearch/solr but doesn't include banana.

```
su - solr
cd /opt/lucidworks-hdpsearch/solr
```

Install Banana version 1.5.0:
```
wget https://github.com/LucidWorks/banana/archive/v1.5.0.zip
mkdir /opt/lucidworks-hdpsearch/solr/server/solr-webapp/webapp/banana/
unzip v1.5.0.zip -d /opt/lucidworks-hdpsearch/solr/server/solr-webapp/webapp/banana/
```

Start Solr in "cloud mode" using the local zookeeper instance at port 2181:
```
bin/solr start -c -z localhost:2181
```

Browse to the Solr UI: http://127.0.0.1:8983/solr/#/

Using your favorite SCP tool (like WinSCP) copy the *banana-int* and *word_count_collection* folders (in multilang) to */opt/lucidworks-hdpsearch/solr* on the server. Then, run the [zkcli.sh script](https://cwiki.apache.org/confluence/display/solr/Using+ZooKeeper+to+Manage+Configuration+Files) upload the configs to zookeeper:
```
server/scripts/cloud-scripts/zkcli.sh -zkhost localhost:2181 -cmd upconfig -confname banana-int -confdir banana-int
server/scripts/cloud-scripts/zkcli.sh -zkhost localhost:2181 -cmd upconfig -confname word_count_collection -confdir word_count_collection
```

Confirm uploaded correctly using tree view: http://127.0.0.1:8983/solr/#/~cloud?view=tree. You should see two new folders under the */configs* directory.

Create the Banana Dashboards collection in SOLR:
http://127.0.0.1:8983/solr/admin/collections?action=CREATE&name=banana-int&numShards=1&maxShardsPerNode=1&replicationFactor=1&collection.configName=banana-int

Note: The solrconfig.xml included with banana was broken. I used the schema.xml from banana-int-solr-4.5/banana-int/conf and the default solrconfig.xml.

Create the Word Count collection in SOLR:
http://127.0.0.1:8983/solr/admin/collections?action=CREATE&name=word_count_collection&numShards=3&maxShardsPerNode=6&replicationFactor=2&collection.configName=word_count_collection

Browse to Banana: http://127.0.0.1:8983/solr/banana/index.html#/dashboard

Import *Word Dashboard.json* into Banana to create the dashboard.

Other useful commands / notes
----------------------------

Try creating an example document using the Solr admin UI: http://127.0.0.1:8983/solr/#/word_count_collection_shard1_replica1/documents
```
{
"word": "zombie", 
"count": 24,
"updated": "2015-09-05T21:28:00Z"
}
```

In case you change configs or what to start over, use the clear command:
server/scripts/cloud-scripts/zkcli.sh -zkhost localhost:2181 -cmd clear /configs/word_count_collection

Delete a collection (and all the data in it!):
http://127.0.0.1:8983/solr/admin/collections?action=DELETE&name=word_count_collection

