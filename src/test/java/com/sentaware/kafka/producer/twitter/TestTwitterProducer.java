package com.sentaware.kafka.producer.twitter;

import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestTwitterProducer {
	private static Logger LOG = LoggerFactory.getLogger(TestTwitterProducer.class);

	private static final String TWITTER_TOPIC_NAME = "twitter.live";
	private static final String[] FILTER_KEYWORDS = {"Google", "Apple", "IBM", "Apache"};
	private static final long RUN_TIME_IN_SECONDS = 60;
	
	// Twitter API consumer keys for testing if you don't have your own
	public static final String CONSUMER_KEY = "to15zikqFu3KFvRGr2fYCQ";
	public static final String CONSUMER_SECRET = "pZ64dkSQNqeax5ddkZI8qS4Ut8wIEzyFglMot6YVqw8";
	public static final String ACCESS_TOKEN = "302091857-c2k1RXdZW5kvwObIHu91rEqlVpKT64GAgvwAJVCJ";
	public static final String ACCESS_TOKEN_SECRET = "eVc8vQ6vgXh7IzWh7W7jjdgTEf9kSTcL4EVVP3qvqck31";
	
	public static void main(String[] args) throws InterruptedException {
		
		String consumerKey, consumerSecret, access, accessSecret;
		
		// If you want to provide your own keys, provide each via args or hardcode them in.
		if ( args.length == 4 ) {
			consumerKey = args[0];
			consumerSecret = args[1];
			access = args[2];
			accessSecret = args[3];
		} else {
			consumerKey = CONSUMER_KEY;
			consumerSecret = CONSUMER_SECRET;
			access = ACCESS_TOKEN;
			accessSecret = ACCESS_TOKEN_SECRET;
		}
		
		TwitterProducer twitterProducer = 
			new TwitterProducer(TWITTER_TOPIC_NAME, FILTER_KEYWORDS);
		
		Properties props = new Properties();
        props.put("metadata.broker.list", "localhost:9092,localhost:9093,localhost:9094");
        props.put("serializer.class", "kafka.serializer.StringEncoder");
        props.put("partitioner.class", "com.sentaware.kafka.producer.SimplePartitioner");
        props.put("request.required.acks", "1");
        
        // These are the keys given by signing up for a free developer account at http://dev.twitter.com
        // Shared keys are provided for quick testing -- may or may not work. 
        props.put(TwitterProducerConstants.CONSUMER_KEY_KEY, consumerKey);
        props.put(TwitterProducerConstants.CONSUMER_SECRET_KEY, consumerSecret);        
        props.put(TwitterProducerConstants.ACCESS_TOKEN_KEY, access);
        props.put(TwitterProducerConstants.ACCESS_TOKEN_SECRET_KEY, accessSecret);
        
        LOG.info("Starting the TwitterProducer...");
        
        twitterProducer.configure(props);
        twitterProducer.start();
        Thread.sleep(TimeUnit.SECONDS.toMillis(RUN_TIME_IN_SECONDS));
        twitterProducer.shutdown();
        
        LOG.info("Shutdown the TwitterProducer after {} seconds.", RUN_TIME_IN_SECONDS);
	}
}
