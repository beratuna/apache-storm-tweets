# apache-storm-tweets
This repository uses a Big Data framework Apache Storm to generate Stream processing of continuously produced tweets.

There are 4 main tasks:

- Task 1: Complete the topology
- Task 2: Implement the two bolts of the topology
- Task 3: Replace HashtagCounterBolt
- Task 4: Submit the topology to production


# User&#39;s Manual

## Task 1.

Shuffle grouping randomly emits tuples so that every bolt will obtain same amount of tuples using round robin sequence. These types of groupings should be used for atomic operations such as math operations.

In fields grouping, the stream data is divided into indicated fields. If stream is partitioned based on &quot;word&quot; field, tuples will be directed to same field (task) of bolt. Basically, fields grouping is used to partition the stream by fields.

If we increase the number of threads in for HashtagCounterBolt the topology will not produce correct results. Since, the number of threads increased, hashtags might be counted several times in threads respectively.

- Now, open up the eclipse workspace and open the project. In the project open HashtagCounterTopology.java to implement shuffle grouping and fields grouping in the topology.

- Edit the HashtagCounterTopology.java to complete the topology by building one bolt for &quot;HashtagFinderBolt&quot; with shuffle grouping which gets stream data from &quot;TweetGeneratorSpout&quot; and emits stream to &quot;HashtagCounterBolt&quot;. Then build second bolt named &quot;HashtagCounterBolt&quot; which gets stream data from &quot;HashtagFinderBolt&quot; with fields grouping, set fields by &quot;hashtag&quot;


## Task 2.

- Open HashtagFinderBolt.java and implement the bolt function in the execute() function;

  - First get tweet as input.
  - Split (by space &quot; &quot;) tweets into array of words.
  - Then find words which has &quot;#&quot; at the beginning of words.
  - Finally emit the hashtags.

- Open HashtagCounterBolt.java and implement the bolt function in the execute() function;

  - First get hashtag as input.
  - Get count of hashtag from the hashmap.
  - Then check whether the word exists in hashmap (by checking whether count value is null or not). If count did not exist, set count to 0.
  - Increment count by 1.
  - Update the count value in the hashmap.
  - Then, emit the hashtag values and their count.

- Run the topology locally on eclipse by right clicking on HashtagCounterTopology.java -> Run as -> 1 Java Application;

The results are printed on console as shown below;

![image](https://user-images.githubusercontent.com/29654044/124648805-5c52dc80-dea0-11eb-9d47-57e80d9bfc39.png)

## Task 3.

- Initially create a class by right click org.bigdatacourse.assignment3 -> New -> Class in package explorer. After a new window opens write SlidingWindowHashtaCounter to Name field and click "finish" to create the class;

- In HashtagCounterTopology.java comment out the &quot;HashtagCounterBolt&quot; bolt in topology and add the following lines to set builder for &quot;SlidingWindowHashtagCounterBolt&quot; bolt into topology (give 10 seconds of window duration with .withWindow(Duration windowLength))

- Complete the SlidingWindowHashtagCounterBolt class:

  - Initially import necessary classes that are used in HashtagCounterBolt and additionally import;
    - &quot;java.util.List&quot; to use tuple lists,
    - &quot;org.apache.storm.topology.base.BaseWindowedBolt&quot; for windowing bolt,
    - &quot;org.apache.storm.windowing.TupleWindow&quot; to use as input parameter of execute() function.

  - Then, complete execute() and declareOutputFields() functions.

    - Different than HashtagCounterBolt, set TupleWindow type as input parameter.
    - Get input window, find new tuples and expired tuples, set them to into list of tuples as follows;
    - Add new hashtags to &#39;counts&#39; hashmap by traversing newTuples List in a for loop.
    - Remove expired hashtags of window in &#39;counts&#39; hashmap by traversing expired Tuples List in a for loop.
    - Print complete &#39;counts&#39; hashmap at the end of execute() function.
    - declareOutputFields() function could be same with HashtagCounterBolt.

- In conclusion, I used sliding windowing for the bolt. Since we were only given that the window duration is 10 seconds, I thought we could simply be able to observe last 10 seconds&#39; data stream using sliding windowing. The following is the console output when the new topology is run;

Console output of new topology

![image](https://user-images.githubusercontent.com/29654044/124649340-0599d280-dea1-11eb-9302-acd1db406715.png)


## Task 4.

- Change the script in HashtagCounterTopology.java as follows;
  - Comment out the LocalCluster and following try catch part in the code.
  - Write the following line to submit topology using StormSubmitter.

- Generate Jar file. In Package Explorer, right click assignment3 \&gt;Run as Maven install This will generate assignment3-0.0.1-SNAPSHOT.jar in target folder.

- Open a terminal (Applications \&gt; Favorites \&gt; Terminal) and type:
  - sudo zkServer.sh start
- Wait for Zookeper to start. Then type:
  - sudo storm nimbus
- Make sure it stays open. Open another terminal and type:
  - sudo storm supervisor
- Open another terminal and type:
  - sudo storm ui
- Open Firefox and go to:
  - http://localhost:8081/ for storm ui
- Open a terminal. Navigate to the directory which contains the jar file, and then:
  - sudo storm jar assignment3-0.0.1-SNAPSHOT.jar org.bigdatacourse.assignment3.HashtagCounterTopology

  - After a while the topology &#39;myTopology&#39; will appear on storm ui as shown;

  - You can see topology Stats, Spouts and Bolts if you click &#39;myTopology&#39;. You can also view executor amount, task amount, emitted or transferred data stream for bolts and spouts on myTopology screen as seen;

Topology Stats – Bolts – Spouts information UI

![image](https://user-images.githubusercontent.com/29654044/124649685-7b9e3980-dea1-11eb-92d5-5d59db9b84c7.png)

  - You can view visualization of the topology by clicking show topology or open visualization buttons

Topology Visualization

![image](https://user-images.githubusercontent.com/29654044/124649703-81941a80-dea1-11eb-8a92-990cc1f94ac8.png)

- Finally kill the topology by opening terminal and type:
  - sudo storm kill myTopology
