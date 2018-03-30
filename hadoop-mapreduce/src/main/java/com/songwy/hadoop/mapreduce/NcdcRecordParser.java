package com.songwy.hadoop.mapreduce;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;

public class NcdcRecordParser {
    private static final int MISSING_TEMPERATURE = 9999;

    private int    airTp;
    private String year;
    private String quality;

    public void parse(String record) {
        year = record.substring(15, 19);

        int airTpPos = 87;
        if (record.charAt(airTpPos) == '+') { // leading plus signs
            airTpPos++;
        }
        airTp = Integer.parseInt(record.substring(airTpPos, 92));
        quality = record.substring(92, 93);
    }

    public void parse(Text record) {
        parse(record.toString());
    }

    public boolean isValidTp() {
        return (airTp != MISSING_TEMPERATURE && quality.matches("[01459]"));
    }

    public String getYear() {
        return year;
    }

    public int getAirTp() {
        return airTp;
    }
}
