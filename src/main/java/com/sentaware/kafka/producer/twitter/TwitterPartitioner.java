package com.sentaware.kafka.producer.twitter;

import kafka.producer.Partitioner;
import kafka.utils.VerifiableProperties;

/**
 * A simple Twitter partitioner that partitions based on time of
 * the tweet.
 * <p>
 * If tweets are sporadic and no specific partitioning of metadata
 * is needed by the consumer, one should not create a partition
 * and allow the Producer to randomly partition such as to
 * maximize the efficiency of read/writes by distributing the
 * load.
 * 
 * @author robertlee
 *
 */
public class TwitterPartitioner implements Partitioner<String> {
    public TwitterPartitioner (VerifiableProperties props) {
    }
 
    /**
     * We partition by the time the tweet comes in over the number
     * of partitions. In this way, we can share the time load of 
     * tweets over partitions. 
     * 
     * Custom partitions can partition by some other metric (such
     * as entity mentioned, URL's, etc.).
     */
    public int partition(String key, int a_numPartitions) {
       return Integer.parseInt(key) % a_numPartitions;
  }
 
}
