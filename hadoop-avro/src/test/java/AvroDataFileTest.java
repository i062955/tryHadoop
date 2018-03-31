import static org.junit.Assert.assertEquals;

import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.avro.Schema;
import org.apache.avro.SchemaBuilder;
import org.apache.avro.generic.*;

import com.songwy.hadoop.avro.AvroDataFile;

public class AvroDataFileTest {

    protected ArrayList<GenericRecord> createUsers(Schema schema) {
        ArrayList<GenericRecord> users = new ArrayList<GenericRecord>();

        GenericRecord user1 = new GenericData.Record(schema);
        user1.put("name", "Alyssa");
        user1.put("favorite_number", 256);
        // Leave favorite color null
        users.add(user1);

        GenericRecord user2 = new GenericData.Record(schema);
        user2.put("name", "Ben");
        user2.put("favorite_number", 7);
        user2.put("favorite_color", "red");
        users.add(user2);
        return users;
    }

    protected Schema createSchema() {
        String[] names = {"name", "favorite_number", "favorite_color"};
        String[] types = {"string", "int", "string"};

        SchemaBuilder.FieldAssembler<Schema> fields = SchemaBuilder.record("SDSAvroAdapter").fields();

        for (int i=0; i < types.length; ++i) {
            if (types[i].equalsIgnoreCase("string")) {
                fields = fields.name(names[i]).type().nullable().stringType().noDefault();
            }else if (types[i].equalsIgnoreCase("int")){
                fields = fields.name(names[i]).type().nullable().intType().noDefault();
            }
        }
        Schema schema = fields.endRecord();
        return schema;
    }

    @Test
    public void writeReadAvroFileTest() {
        AvroDataFile tester = new AvroDataFile();
//        Schema writerSchema = tester.createSchema("user.avsc");
        Schema writerSchema = createSchema();

        String outputFile = "user.avso";
        try {
            tester.serialize(writerSchema, createUsers(writerSchema), outputFile);
            System.out.println("Serialized AVRO data into file: " + outputFile);
            System.out.println();

            Schema readerSchema = tester.getReadSchema(outputFile);
            System.out.println("Fetch Schema from AVRO data file: " + outputFile);
            assertEquals(readerSchema.toString(), writerSchema.toString());
            System.out.println();

            System.out.println("Deserialize AVRO data file: " + outputFile);
            tester.deserialize(outputFile);
        }catch (Exception e) {
            System.out.print(e);
        }
    }
}
