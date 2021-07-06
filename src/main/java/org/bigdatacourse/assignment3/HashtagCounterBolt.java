package org.bigdatacourse.assignment3;

import java.util.HashMap;
import java.util.Map;

import org.apache.storm.topology.BasicOutputCollector;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseBasicBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;

public class HashtagCounterBolt extends BaseBasicBolt{
	Map<String, Integer> counts = new HashMap<String, Integer>();
	
//	public void cleanup() {
//		System.out.println("Hashtag Counts:");
//		for(Map.Entry<String, Integer> entry: counts.entrySet()) {
//			System.out.println(entry.getKey() + " hashtag amount is " + entry.getValue());
//		}
//	}

	
	@Override
	public void execute(Tuple input, BasicOutputCollector collector) {
		//Task 2b: Implement this method
		//...
		String hashtag = input.getString(0);
		Integer count = counts.get(hashtag);
		if (count == null)
			count = 0;
		count++;
		counts.put(hashtag, count);
		System.out.println(hashtag + ": " + count);
		collector.emit(new Values(hashtag, count));
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("hashtag", "count"));
	}

}
