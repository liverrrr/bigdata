package org.example.hadoop.sort;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileUtil;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;


import java.io.File;
import java.io.IOException;


public class AccessApp {
    public static class MyMapper extends Mapper<LongWritable, Text, Access, NullWritable> {
        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            String[] splits = value.toString().split("\t");
            String phone=splits[1];
            long up=Long.parseLong(splits[splits.length-3]);
            long down=Long.parseLong(splits[splits.length-2]);

            context.write(new Access(phone,up,down),NullWritable.get());
        }
    }

    public static class MyReduce extends Reducer<Access, NullWritable, Access, NullWritable> {
        @Override
        protected void reduce(Access key, Iterable<NullWritable> values, Context context) throws IOException, InterruptedException {
            for (NullWritable val:values) {
                context.write(key,NullWritable.get());
            }
        }
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        Configuration configuration = new Configuration();
        Job job = Job.getInstance(configuration, "AccessApp");

        job.setJarByClass(AccessApp.class);

        job.setMapperClass(MyMapper.class);
        job.setReducerClass(MyReduce.class);

        job.setMapOutputKeyClass(Access.class);
        job.setMapOutputValueClass(NullWritable.class);
        job.setOutputKeyClass(Access.class);
        job.setOutputValueClass(NullWritable.class);

        job.setPartitionerClass(AccessPartition.class);
        job.setNumReduceTasks(3);

        String input = "data\\input\\access.log";
        String output = "data\\out";
        FileUtil.fullyDelete(new File(output));
        FileInputFormat.addInputPath(job, new Path(input));
        FileOutputFormat.setOutputPath(job, new Path(output));

        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}
