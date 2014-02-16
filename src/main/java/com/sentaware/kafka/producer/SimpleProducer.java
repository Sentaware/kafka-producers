package com.sentaware.kafka.producer;

import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;
 
/**
 * SimpleProducer is a sample producer you can run locally on your machine to
 * ensure that your multi-broker pseudo-cluster is working locally on your
 * machine before you move on to more complicated producers. 
 * <p>
 * Prior to running the code, be sure you have thoroughly read the README in the
 * root directory of this repository. You will need to create the "page_visits" 
 * topic in order to properly produce the sample data.
 * <p>
 * The code is heavily borrowed from the Apache Kafka start up guide located here:
 * https://cwiki.apache.org/confluence/display/KAFKA/0.8.0+Producer+Example
 * 
 * @see {@link TestSimpleProducer} to run the SimpleProducer.
 * @author robertlee
 */
public class SimpleProducer extends AbstractProducer<String, String> {
	private static Logger LOG = LoggerFactory.getLogger(SimpleProducer.class);
	private long nEvents;
	
	/**
	 * Create a SimpleProducer with the number of events to sample.
	 * @param nEvents The number of events to produce and send to a Kafka broker.
	 */
	public SimpleProducer(long nEvents) {
		this.nEvents = nEvents;
	}


	@Override
	public void configure(Properties props) {
        // Note that the Producer is a Java Generic and you need to tell it the type of two parameters. The first
        // is the type of the Partition key, the second the type of the message. In this example they are both 
        // Strings, which also matches to what we defined in the Properties above. 
		ProducerConfig config = new ProducerConfig(props);
		producer = new Producer<String, String>(config);		
	}

	@Override
	public void start() {
		Random rnd = new Random();
		 
        for (long event = 0; event < nEvents; event++) { 
               long runtime = new Date().getTime();  
               String ip = "192.168.2." + rnd.nextInt(255); 
               String msg = runtime + ",www.example.com," + ip; 
               
               // In this example we are faking a message for a website visit by IP address. First part of the 
               // comma-separated message is the timestamp of the event, the second is the website and the third 
               // is the IP address of the requester. We use the Java Random class here to make the last octet 
               // of the IP vary so we can see how Partitioning works.
               KeyedMessage<String, String> data = new KeyedMessage<String, String>("page_visits", ip, msg);
               LOG.info("Sending: {}", data.toString());
               
               // Finally, we sent the message to the broker.
               producer.send(data);
        }
	}
}
