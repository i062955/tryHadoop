package com.songwy.hadoop.avro;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.avro.Schema;
import org.apache.avro.SchemaBuilder;
import org.apache.avro.file.DataFileReader;
import org.apache.avro.file.DataFileWriter;
import org.apache.avro.generic.*;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.DatumWriter;

public class AvroDataFile {

    /**
     *  Based on a given AVRO schema, write the records into file
     *  @param schema is a given AVRO schema
     *  @param records is the data to be written, with the type of Avro GenericRecord
     *  @param filename is the target file name
     */
    public void serialize(Schema schema, ArrayList<GenericRecord> records, String filename) throws Exception {
        File file = new File(filename);
        DatumWriter<GenericRecord> datumWriter = new GenericDatumWriter<GenericRecord>(schema);

        DataFileWriter<GenericRecord> dataFileWriter = new DataFileWriter<GenericRecord>(datumWriter);
        dataFileWriter.create(schema, file);

        for (GenericRecord record: records) {
            dataFileWriter.append(record);
            System.out.println("Append Record: " + record);
        }

        dataFileWriter.close();
    }

    /**
     *  Read out the content from an AVRO file and print out
     *  @param filename is the AVRO file to be read out
     */
    public void deserialize(String filename) throws Exception {
        File file = new File(filename);
        Schema schema = getReadSchema(filename);
        if (schema != null) {
            DatumReader<GenericRecord> datumReader = new GenericDatumReader<GenericRecord>(schema);
            DataFileReader<GenericRecord> dataFileReader = new DataFileReader<GenericRecord>(file, datumReader);

            GenericRecord user = null;
            while (dataFileReader.hasNext()) {
                user = dataFileReader.next(user);
                System.out.println(user);
            }
            dataFileReader.close();
        }
    }

    public Schema getReadSchema (String avroFileName) throws Exception {
        DataFileReader<Void> reader = null;
        Schema schema = null;
        try {
             reader = new DataFileReader<Void>(new File(avroFileName),
                                               new GenericDatumReader<Void>());
             schema = reader.getSchema();
        }catch (Exception e) {
            System.out.print(e);
        }finally {
            if (reader != null)
                reader.close();
        }
        return schema;
    }

    public Schema getWriteSchema (String avscFileName){
        File schema = new File(avscFileName);
        try {
            return new Schema.Parser().parse(schema);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
