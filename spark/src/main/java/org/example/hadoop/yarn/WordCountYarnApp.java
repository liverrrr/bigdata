package org.example.hadoop.yarn;

import org.example.hadoop.util.FileUtil;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;
import java.util.Properties;

public class WordCountYarnApp {

    public static class MyMapper extends Mapper<LongWritable, Text, Text, IntWritable> {

        private final static IntWritable ONE = new IntWritable(1);

        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            String[] split = value.toString().split(",");
            for (String word : split) {
                context.write(new Text(word), ONE);
            }
        }
    }

    public static class MyReduce extends Reducer<Text, IntWritable, Text, IntWritable> {

        @Override
        protected void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
            int sum = 0;
            for (IntWritable val : values) {
                sum += val.get();
            }
            context.write(key, new IntWritable(sum));
        }
    }

    public static void main(String[] args) throws Exception {
        Properties properties = System.getProperties();
        properties.setProperty("HADOOP_USER_NAME", "hadoop");

        Configuration configuration = new Configuration();
        configuration.set("fs.defaultFS", "hdfs://hadoop001:8020");
        configuration.set("dfs.client.use.datanode.hostname", "true");
        configuration.set("mapred.jar", "D:\\若泽大数据\\G7-03\\target\\G7-03-1.0.jar");
        configuration.set("mapreduce.framework.name", "yarn");
        configuration.set("yarn.resourcemanager.hostname", "hadoop001");
        configuration.set("mapreduce.app-submission.cross-platform", "true");

        Job job = Job.getInstance(configuration, "WordCountYarnApp");

        job.setJarByClass(WordCountYarnApp.class);

        job.setMapperClass(MyMapper.class);
        job.setReducerClass(MyReduce.class);

        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(IntWritable.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);

        String input = "hdfs://hadoop001:8020/ruozedata/input/wc.txt";
        String output = "hdfs://hadoop001:8020/ruozedata/out";
//       FileUtil.fullyDelete(new File(output));
        FileUtil.deleteTarget(output);
        FileInputFormat.addInputPath(job, new Path(input));
        FileOutputFormat.setOutputPath(job, new Path(output));

        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}
