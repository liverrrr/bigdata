package org.example.hadoop.sort;

import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapreduce.Partitioner;

public class AccessPartition extends Partitioner<Access, NullWritable> {

    public int getPartition(Access access, NullWritable nullWritable, int numPartitions) {
        String phone = access.getPhone();
        if (phone.startsWith("135")){
            return 0;
        }else if (phone.startsWith("182")){
            return 1;
        }else {
            return 2;
        }
    }
}
