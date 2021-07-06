package org.bigdatacourse.assignment3;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

import org.apache.storm.spout.SpoutOutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichSpout;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;

public class TweetGeneratorSpout extends BaseRichSpout{
	SpoutOutputCollector collector;
	String fileName = new String("/home/labuser/eclipse-workspace/assignment3/tweets.txt");
	ArrayList<String> tweets = new ArrayList<String>();
	Random rand = new Random();
	
	@Override
	public void open(Map<String, Object> conf, TopologyContext context, SpoutOutputCollector collector) {
		this.collector = collector;
		
		BufferedReader reader;
		try {
			reader = new BufferedReader( new FileReader( fileName ) );
			String line = reader.readLine();
			
			while (line != null) {
				tweets.add(line);
				line = reader.readLine();
			}
			
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void nextTuple() {
		String randomTweet = tweets.get( rand.nextInt( tweets.size() ) ) ;
		collector.emit( new Values( randomTweet ) );
		
		try {
			Thread.sleep(50);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare( new Fields( "tweet" ) );
	}

}
