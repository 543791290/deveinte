/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.haothink.utils;

import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.common.Cluster;

import java.util.Map;

/**
 * 用于kafka的分区使用
 */
public class JPartitioner implements Partitioner {

    /**
     * Compute the partition for the given record.
     *
     * @param topic      The topic name
     * @param key        The key to partition on (or null if no key)
     * @param keyBytes   The serialized key to partition on( or null if no key)
     * @param value      The value to partition on or null
     * @param valueBytes The serialized value to partition on or null
     * @param cluster    The current cluster metadata
     */
  
    public int partition(String topic, Object key, byte[] keyBytes, Object value, byte[] valueBytes, Cluster cluster) {
        return (Integer) key % cluster.partitionCountForTopic(topic);
    }

    /**
     * This is called when partitioner is closed.
     */
  
    public void close() {

    }

    /**
     * Configure this class with the given key-value pairs
     *
     * @param configs
     */
  
    public void configure(Map<String, ?> configs) {

    }
}
