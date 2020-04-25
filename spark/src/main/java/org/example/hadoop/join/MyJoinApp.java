package org.example.hadoop.join;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileUtil;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MyJoinApp {
    public static class MyMaperr extends Mapper<LongWritable, Text, Text, Join> {

        private String filename;
        @Override
        protected void setup(Context context){
            FileSplit fileSplit= (FileSplit) context.getInputSplit();
            filename=fileSplit.getPath().getName();
        }

        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            if (filename.contains("emp")){
                String[] split = value.toString().split("\t");
                if (split.length==8) {
                    Join join = new Join();
                    join.setEmpId(split[0]);
                    join.setEmpName(split[1] + " " + split[2]);
                    join.setDeptId(split[split.length - 1]);
                    join.setDeptName("");
                    join.setFlag(1);
                    context.write(new Text(split[split.length - 1]), join);
                }
            }else {
                String[] split = value.toString().split(",");
                Join join = new Join();
                join.setEmpId("");
                join.setEmpName("");
                join.setDeptId(split[0]);
                join.setDeptName(split[1]);
                join.setFlag(2);
                context.write(new Text(split[0]),join);
            }
        }
    }

    public static class MyReduce extends Reducer<Text, Join, NullWritable, Join> {
        @Override
        protected void reduce(Text key, Iterable<Join> values, Context context) throws IOException, InterruptedException {
            List<Join> joins = new ArrayList<Join>();
            String deptName=null;
            for (Join join:values){
                if (join.getFlag()==1){
                    Join val = new Join();
                    val.setEmpId(join.getEmpId());
                    val.setEmpName(join.getEmpName());
                    val.setDeptId(join.getDeptId());
                    val.setDeptName(join.getDeptName());
                    joins.add(val);
                }else if (join.getFlag()==2){
                    deptName=join.getDeptName();
                }
            }

            for (Join val:joins){
                val.setDeptName(deptName);
                context.write(NullWritable.get(),val);
            }
        }
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        Configuration configuration = new Configuration();
        Job job = Job.getInstance(configuration, "MyJoinApp");

        job.setJarByClass(MyJoinApp.class);

        job.setMapperClass(MyMaperr.class);
        job.setReducerClass(MyReduce.class);

        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(Join.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);

        String input = "data\\input\\join";
        String output = "data\\out";
        FileUtil.fullyDelete(new File(output));
        FileInputFormat.addInputPath(job, new Path(input));
        FileOutputFormat.setOutputPath(job, new Path(output));

        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}
