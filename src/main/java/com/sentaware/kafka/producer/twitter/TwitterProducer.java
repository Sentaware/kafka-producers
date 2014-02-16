package com.sentaware.kafka.producer.twitter;

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import twitter4j.FilterQuery;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.conf.ConfigurationBuilder;
import twitter4j.json.DataObjectFactory;
import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;

import com.sentaware.kafka.producer.AbstractProducer;

public class TwitterProducer extends AbstractProducer<String, String> {
	private static final Logger LOG = LoggerFactory.getLogger(TwitterProducer.class);
	
	/** The actual Twitter stream. It's set up to collect raw JSON data */
	private TwitterStream twitterStream;
	
	/** The keywords to filter the twitter firehose. */
	private String[] keywords;
	
	/** The name of the topic created for the producer. */
	private String topic;
	
	/**
	 * Creates a TwitterProducer instance.
	 * 
	 * @param topicName The topic associated with this TwitterProducer
	 * @param keywords The keywords to filter from the Twitter API Firehose.
	 */
	public TwitterProducer(String topicName, String[] keywords) {
		this.topic = topicName;
		this.keywords = keywords;
	}

	@Override
	public void configure(Properties props) {
		ProducerConfig config = new ProducerConfig(props);
		producer = new Producer<String, String>(config);
		
		// The necessary authentication keys to connect to twitter API v1.1
	    String consumerKey = props.getProperty(TwitterProducerConstants.CONSUMER_KEY_KEY);
	    String consumerSecret = props.getProperty(TwitterProducerConstants.CONSUMER_SECRET_KEY);
	    String accessToken = props.getProperty(TwitterProducerConstants.ACCESS_TOKEN_KEY);
	    String accessTokenSecret = props.getProperty(TwitterProducerConstants.ACCESS_TOKEN_SECRET_KEY);
		
		ConfigurationBuilder cb = new ConfigurationBuilder();
	    cb.setOAuthConsumerKey(consumerKey);
	    cb.setOAuthConsumerSecret(consumerSecret);
	    cb.setOAuthAccessToken(accessToken);
	    cb.setOAuthAccessTokenSecret(accessTokenSecret);
	    cb.setJSONStoreEnabled(true);
	    cb.setIncludeEntitiesEnabled(true);

	    twitterStream = new TwitterStreamFactory(cb.build()).getInstance();
	}

	@Override
	public void start() {
		
		// The StatusListener is a twitter4j API, which can be added to a Twitter
		// stream, and will execute methods every time a message comes in through
		// the stream.
		StatusListener listener = new StatusListener() {
			// The onStatus method is executed every time a new tweet comes in.
			// We want to produce a new message when this happens.
			public void onStatus(Status status) {
				LOG.debug(status.getUser().getScreenName() + ": "
						+ status.getText());
				
				// Create a key based on the time and the message is the raw json of the tweet.
				String key = String.valueOf(status.getCreatedAt().getTime());
				String msg = DataObjectFactory.getRawJSON(status);
				
	            KeyedMessage<String, String> data = new KeyedMessage<String, String>(topic, key, msg);
	            
	            producer.send(data);
			}

			// This listener will ignore everything except for new tweets
			public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {}
			public void onTrackLimitationNotice(int numberOfLimitedStatuses) {}
			public void onScrubGeo(long userId, long upToStatusId) {}
			public void onException(Exception ex) {
				LOG.error("TwitterStreamException: {}", ex);
			}
			public void onStallWarning(StallWarning warning) {
				LOG.warn("StallWarning: {}", warning);
			}
		};

		LOG.debug("Setting up Twitter filter stream using the following keywords: {}",
				(Object [])keywords);
		
		// Set up the stream's listener (defined above),
		twitterStream.addListener(listener);

		// Create a filtered query and start the stream.
		FilterQuery query = new FilterQuery().track(keywords);
		twitterStream.filter(query);
	}
	
	@Override
	public void shutdown() {
		LOG.info("Shutting down the TwitterProducer.");
		super.shutdown();
		twitterStream.shutdown();
	}
}
