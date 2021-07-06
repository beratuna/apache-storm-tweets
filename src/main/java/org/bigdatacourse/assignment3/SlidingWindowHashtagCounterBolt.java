package org.bigdatacourse.assignment3;

import java.util.HashMap;
import java.util.Map;
import java.util.List;

import org.apache.storm.topology.BasicOutputCollector;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseWindowedBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;
import org.apache.storm.windowing.TupleWindow;

public class SlidingWindowHashtagCounterBolt extends BaseWindowedBolt{
	Map<String, Integer> counts = new HashMap<String, Integer>();
//	
	@Override
	public void execute(TupleWindow input) {
		//Task 2b: Implement this method
		//...
		
        List<Tuple> tuplesInWindow = input.get();
        List<Tuple> newTuples = input.getNew();
        List<Tuple> expiredTuples = input.getExpired();

        for(Tuple tpl: newTuples) {
    		String hashtag = (String) tpl.getValue(0);
    		Integer count = counts.get(hashtag);
    		if (count == null)
    			count = 0;
    		count++;
    		counts.put(hashtag, count);
        }
        for(Tuple tpl: expiredTuples) {
    		String hashtag = (String) tpl.getValue(0);
    		Integer count = counts.get(hashtag);
    		if (count == null)
    			count = 0;
    		else
    			count--;
    		counts.put(hashtag, count);
//    		collector.emit(new Values(hashtag, count));        	
        }
        
		System.out.println(counts);
	}
//
	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("hashtag", "count"));
	}
}
