package com.zy.spring.mildware.mq.kafka.utils;

import org.apache.avro.Schema;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.*;
import org.apache.avro.specific.SpecificDatumReader;
import org.apache.avro.specific.SpecificDatumWriter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class SchemaUtils {

    private static final Map<String, Schema> SCHEMA_MAP = new ConcurrentHashMap<>();

    public static Schema getSchema(String filePath) {
        Schema schema = SCHEMA_MAP.get(filePath);
        if (Objects.nonNull(schema)) {
            return schema;
        }

        // InputStream inputStream = SchemaUtils.class.getResourceAsStream(filePath);
        // BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))
        try (InputStream inputStream = ClassLoader.getSystemResourceAsStream(filePath);
             ) {
            schema = new Schema.Parser().parse(inputStream);
            SCHEMA_MAP.put(filePath, schema);
            return schema;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] record2Bytes(GenericRecord record, Schema schema) {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            DatumWriter<GenericRecord> writer = new SpecificDatumWriter<>(schema);
            Encoder encoder = EncoderFactory.get().binaryEncoder(out, null);
            writer.write(record, encoder);
            encoder.flush();
            return out.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static GenericRecord bytes2Record(byte[] record, Schema schema) {
        try {
            BinaryDecoder decoder = DecoderFactory.get().binaryDecoder(record, null);
            DatumReader<GenericRecord> reader = new SpecificDatumReader<>();
            reader.setSchema(schema);
            return reader.read(null, decoder);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
