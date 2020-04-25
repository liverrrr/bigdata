package org.example.hadoop.top;

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
import java.util.ArrayList;
import java.util.Collections;


public class TopNApp {

    private static int TOPN = 1;

    public static class MyMapper extends Mapper<LongWritable, Text, Text, Order> {
        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            String[] splits = value.toString().split(",");
            Order order = new Order();
            order.setUserId(splits[0]);
            order.setName(splits[1]);
            order.setPrice(Integer.parseInt(splits[2]));
            context.write(new Text(splits[0]), order);
        }
    }

    public static class MyReduce extends Reducer<Text, Order, NullWritable, Order> {
        private ArrayList<Order> orders = new ArrayList<Order>();

        @Override
        protected void reduce(Text key, Iterable<Order> values, Context context) {
            for (Order val : values) {
                Order order = new Order();
                order.setUserId(val.getUserId());
                order.setName(val.getName());
                order.setPrice(val.getPrice());
                orders.add(order);
            }
        }

        @Override
        protected void cleanup(Context context) throws IOException, InterruptedException {
            Collections.sort(orders);
            for (int i = 0; i < TOPN; i++) {
                context.write(NullWritable.get(), orders.get(i));
            }
        }
    }

    public static void main(String[] args) throws Exception {
        Configuration configuration = new Configuration();
        Job job = Job.getInstance(configuration, "TopNApp");

        job.setJarByClass(TopNApp.class);

        job.setMapperClass(MyMapper.class);
        job.setReducerClass(MyReduce.class);

        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(Order.class);
        job.setOutputKeyClass(NullWritable.class);
        job.setOutputValueClass(Order.class);

        String input = "data\\input\\top.txt";
        String output = "data\\out";
        FileUtil.fullyDelete(new File(output));

        job.setPartitionerClass(OrderPartition.class);
        job.setNumReduceTasks(3);

        FileInputFormat.addInputPath(job, new Path(input));
        FileOutputFormat.setOutputPath(job, new Path(output));

        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}
