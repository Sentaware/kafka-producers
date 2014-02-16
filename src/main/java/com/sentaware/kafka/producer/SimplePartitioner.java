package com.sentaware.kafka.producer;

import kafka.producer.Partitioner;
import kafka.utils.VerifiableProperties;

/**
 * SimplePartitioner defines the partitioning logic for the {@link SimpleProducer}
 * class. 
 * <p>
 * The logic takes the key, which we expect to be the IP address, finds the last 
 * octet and does a modulo operation on the number of partitions defined within
 * Kafka for the topic. The benefit of this partitioning logic is all web visits 
 * from the same source IP end up in the same Partition. Of course so do other 
 * IPs, but your consumer logic will need to know how to handle that.
 * 
 * @author robertlee
 *
 */
public class SimplePartitioner implements Partitioner<String> {
    public SimplePartitioner (VerifiableProperties props) {
    	// Reacts to settings in props, if any are set.
    }
 
    public int partition(String key, int a_numPartitions) {
        int partition = 0;
        int offset = key.lastIndexOf('.');
        if (offset > 0) {
           partition = Integer.parseInt( key.substring(offset+1)) % a_numPartitions;
        }
       return partition;
  }
 
}
