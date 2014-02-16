package com.sentaware.kafka.producer;

import java.util.Properties;

import kafka.javaapi.producer.Producer;

/**
 * A basic template class to build specialized producers from. 
 * 
 * @author robertlee
 * @param <P> the partitioning class that groups messages into partitions 
 * 		within the Kafka brokers.
 * @param <M> the message class that is serialized and sent by the producer.
 */
public abstract class AbstractProducer<P, M> {
	

	/** The Producer responsible for creating messages and 
	 * 	sending the messages to the brokers.  */
	protected Producer<P, M> producer;
	
	/**
	 * Configures the producer in accordance to the properties 
	 * given in <code>props</code> and then instantiates the
	 * {@link producer} 
	 * 
	 * @param props The properties to create the Producer with.
	 */
	public abstract void configure(Properties props);
	
	/**
	 * The operational logic for producing and sending messages to
	 * the {@link #producer is contained within this method. 
	 */
	public abstract void start();
	
	/**
	 * Stops the producer and closes the connection to all
	 * brokers.
	 */
	public void shutdown() {
		producer.close();
	}
}
