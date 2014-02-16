package com.sentaware.kafka.producer;

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestSimpleProducer {
	private static Logger LOG = LoggerFactory.getLogger(TestSimpleProducer.class);

    public static void main(String[] args) {
    	
    	// Number of test events we want to produce.
        long events = 25;
        
        // We are now going to produce the properties file that dictates how we want our producer to run.
        Properties props = new Properties();
        
        // Defines where the Producer can find a one or more Brokers to determine the Leader for each topic. 
        // This does not need to be the full set of Brokers in your cluster but should include at least two 
        // in case the first Broker is not available. No need to worry about figuring out which Broker is the 
        // leader for the topic (and partition), the Producer knows how to connect to the Broker and ask for 
        // the meta data then connect to the correct Broker. Here, we are listing the three ports we created
        // that are listening as kafka brokers in a pseudo-clustered manner.
        props.put("metadata.broker.list", "localhost:9092,localhost:9093,localhost:9094");
        
        // Defines what Serializer to use when preparing the message for transmission to the Broker. In our 
        // example we use a simple String encoder provided as part of Kafka. Note that the encoder must accept 
        // the same type as defined in the KeyedMessage object in the next step. 
        props.put("serializer.class", "kafka.serializer.StringEncoder");
        
        // Defines what class to use to determine which Partition in the Topic the message is to be sent to. 
        // This is optional, but for any non-trivial implementation you are going to want to implement a 
        // partitioning scheme.
        props.put("partitioner.class", "com.sentaware.kafka.producer.SimplePartitioner");
        
        // Tells Kafka that you want your Producer to require an acknowledgment from the Broker that the message 
        // was received. Without this setting the Producer will 'fire and forget' possibly leading to data loss.
        props.put("request.required.acks", "1");
        
        // Create an instance of the simple producer and configure the Producer with the properties above.
        SimpleProducer simpleProducer = new SimpleProducer(events);
        simpleProducer.configure(props);
        
        // We now run the producer and create the events
        LOG.info("Running SimpleProducer for {} sample events.", events);
        simpleProducer.start();
        LOG.info("Completed producing {} sample events.", events);
        
        // Close the producer to terminate the connection to the brokers and free memory. 
        simpleProducer.shutdown();
    }
}
