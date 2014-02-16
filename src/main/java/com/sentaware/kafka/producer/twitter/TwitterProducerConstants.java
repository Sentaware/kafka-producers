package com.sentaware.kafka.producer.twitter;

/**
 * The constants used for configuring a {@link TwitterProducer}. The
 * {@link Properties} file passed to {@link TwitterProducer#configure(java.util.Properties)}
 * should contain these authentication constants so that a TwitterProducer can
 * be created and authenticated to the Twitter API.
 * 
 * @author robertlee
 */
public class TwitterProducerConstants {
	  public static final String CONSUMER_KEY_KEY = "consumerKey";
	  public static final String CONSUMER_SECRET_KEY = "consumerSecret";
	  public static final String ACCESS_TOKEN_KEY = "accessToken";
	  public static final String ACCESS_TOKEN_SECRET_KEY = "accessTokenSecret";
	  
	  public static final String BATCH_SIZE_KEY = "batchSize";
	  public static final long DEFAULT_BATCH_SIZE = 1000L;
}
