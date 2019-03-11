package com.songwy.hadoop.mapreduce;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class MaxTpMapper extends Mapper<LongWritable, Text, Text, IntWritable> {
    private static final int HIGHEST_TEMPERATURE = 1000;

    enum Temperature {
        OVER_100
    }

    private NcdcRecordParser parser = new NcdcRecordParser();

    @Override
    public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        parser.parse(value);
        if (parser.isValidTp()){
            int Tp = parser.getAirTp();
            if (Tp > HIGHEST_TEMPERATURE){
                System.err.println("Temperature over 100 degress for input: " + value);
                context.setStatus("Detected possibly corrupt record, see log.");
                context.getCounter(Temperature.OVER_100).increment(1);
            }
            context.write(new Text(parser.getYear()), new IntWritable(Tp));
        }
    }
}

