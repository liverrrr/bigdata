package org.example.hadoop.output;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.FileUtil;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.File;
import java.io.IOException;

public class RuozeOutputFormatApp {
    public static class MyMaperr extends Mapper<LongWritable, Text, Text, NullWritable> {

        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            context.write(value, NullWritable.get());
        }
    }

    public static class MyReduce extends Reducer<Text, NullWritable, Text, NullWritable> {

        @Override
        protected void reduce(Text key, Iterable<NullWritable> values, Context context) throws IOException, InterruptedException {
            for (NullWritable val:values){
                context.write(new Text(key.toString()+"\r\n"),val);
            }
        }
    }

    public static class MyOutputFormat extends FileOutputFormat<Text,NullWritable>{
        public RecordWriter<Text, NullWritable> getRecordWriter(TaskAttemptContext job) throws IOException, InterruptedException {
            return new MyRecordWriter(job);
        }
    }

    public static class MyRecordWriter extends RecordWriter<Text,NullWritable>{

        private FSDataOutputStream output1 = null;
        private FSDataOutputStream output2 = null;
        private FileSystem fileSystem = null;


        public MyRecordWriter(TaskAttemptContext job) {
            try {
                fileSystem = FileSystem.get(job.getConfiguration());
                output1=fileSystem.create(new Path("data\\out\\ruoze.log"));
                output2=fileSystem.create(new Path("data\\out\\other.log"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void write(Text key, NullWritable value) throws IOException {
            String url=key.toString();
            if (url.contains("ruozedata.com")){
                output1.write(url.getBytes());
            }else{
                output2.write(url.getBytes());
            }
        }

        public void close(TaskAttemptContext context) throws IOException {
            IOUtils.closeStream(output1);
            IOUtils.closeStream(output2);
            fileSystem.close();
        }
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        Configuration configuration = new Configuration();
        Job job = Job.getInstance(configuration, "RuozeOutputFormatApp");

        job.setJarByClass(RuozeOutputFormatApp.class);

        job.setMapperClass(MyMaperr.class);
        job.setReducerClass(MyReduce.class);

        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(NullWritable.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(NullWritable.class);

        job.setOutputFormatClass(MyOutputFormat.class);

        String input = "data\\input\\click.log";
        String output = "data\\out";
        FileUtil.fullyDelete(new File(output));
        FileInputFormat.addInputPath(job, new Path(input));
        FileOutputFormat.setOutputPath(job, new Path(output));

        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}
