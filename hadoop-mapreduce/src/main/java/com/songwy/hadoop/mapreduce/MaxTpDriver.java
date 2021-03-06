package com.songwy.hadoop.mapreduce;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.util.*;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class MaxTpDriver extends Configured implements Tool {

    @Override
    public int run(String[] args) throws Exception {
        if (args.length != 2){
            System.err.printf("Usage: %s [generic options] <input path> <output path>",
                getClass().getSimpleName());
            ToolRunner.printGenericCommandUsage(System.err);
            String defau[] = new String[2];
            defau[0] = "hadoop-mapreduce/data/1901";
            defau[1] = "hadoop-mapreduce/output";
            return this.run(defau);
        }

        Job job = new Job(getConf(), "Max temperature");
        job.setJarByClass(MaxTpDriver.class);

        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));

        job.setMapperClass   (MaxTpMapper.class);
        job.setCombinerClass (MaxTpReducer.class);
        job.setReducerClass  (MaxTpReducer.class);

        //job.setMapOutputKeyClass(Text.class);
        //job.setMapOutputValueClass(IntWritable.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);

        return (job.waitForCompletion(true) ? 0: 1);
    }

    public static void main(String[] args) throws Exception {
        int exitCode = ToolRunner.run (new MaxTpDriver(), args);
        System.exit(exitCode);
    }
}

