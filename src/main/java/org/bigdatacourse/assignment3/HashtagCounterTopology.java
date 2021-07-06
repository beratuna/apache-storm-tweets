package org.bigdatacourse.assignment3;

import java.util.concurrent.TimeUnit;
import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.StormSubmitter;
import org.apache.storm.topology.TopologyBuilder;
import org.apache.storm.topology.base.BaseWindowedBolt.Duration;
import org.apache.storm.tuple.Fields;

public class HashtagCounterTopology {

	public static void main(String[] args) throws Exception {
		TopologyBuilder builder = new TopologyBuilder();
		
		//Task 1a: Complete the topology.
		builder.setSpout( "TweetGeneratorSpout" , new TweetGeneratorSpout() );
		//builder.set...
		// shuffle grouping for HashtagFinderBolt
		builder.setBolt("HashtagFinderBolt", new HashtagFinderBolt() )
			.shuffleGrouping("TweetGeneratorSpout");

		// fields grouping for HashtagCounterBolt
//		builder.setBolt("HashtagCounterBolt", new HashtagCounterBolt() )
//			.fieldsGrouping("HashtagFinderBolt", new Fields("hashtag"));
		builder.setBolt("SlidingWindowHashtagCounterBolt", new SlidingWindowHashtagCounterBolt()
//				.withWindow(Duration.seconds(30)))
				  .withWindow(new Duration(10, TimeUnit.SECONDS)))
				  		.fieldsGrouping("HashtagFinderBolt", new Fields("hashtag"));
		
		Config config = new Config();
		config.setDebug(false);
		
//		StormSubmitter.submitTopology("myTopology", config, builder.createTopology());
		LocalCluster cluster = new LocalCluster();
		
		try {
			cluster.submitTopology( "HashtagCounterTopology", config, builder.createTopology() );
			Thread.sleep(100000);
		} catch ( Exception e ) {
			e.printStackTrace();
		} finally {
			//cluster.shutdown();
		}
	}
}
