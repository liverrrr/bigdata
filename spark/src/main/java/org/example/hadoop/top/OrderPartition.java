package org.example.hadoop.top;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Partitioner;

public class OrderPartition extends Partitioner<Text, Order> {

    @Override
    public int getPartition(Text text, Order order, int numPartitions) {
        return (text.toString().hashCode() & Integer.MAX_VALUE) % numPartitions;
    }
}
