package org.bigdatacourse.assignment3;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.ArrayList;

import org.apache.storm.topology.BasicOutputCollector;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseBasicBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;

public class HashtagFinderBolt extends BaseBasicBolt{
	@Override
	public void execute(Tuple input, BasicOutputCollector collector) {
		//Task 2a: Implement this method
		//..
		// get tweet
		String twt = input.getString(0);	
//		System.out.println(twt);
		
		// split words of tweet
		String[] word_arr = twt.split(" ");
		
		// find hashtags in words array
		for(int i = 0; i< word_arr.length;i++) {
			// check if a word contains '#' character or not
			if(word_arr[i].charAt(0) == '#'){
				String hashtag = word_arr[i];

				collector.emit(new Values(hashtag));
//				System.out.println(hashtag);
			} 
		}
		
		
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare( new Fields("hashtag") );
	}
}
