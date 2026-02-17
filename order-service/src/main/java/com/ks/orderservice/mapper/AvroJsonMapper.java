package com.ks.orderservice.mapper;

import org.apache.avro.io.Encoder;
import org.apache.avro.io.EncoderFactory;
import org.apache.avro.specific.SpecificDatumWriter;
import org.apache.avro.specific.SpecificRecord;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class AvroJsonMapper {

    public static String toJson(SpecificRecord record) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

            Encoder encoder = EncoderFactory.get()
                    .jsonEncoder(record.getSchema(), outputStream);

            SpecificDatumWriter<SpecificRecord> writer =
                    new SpecificDatumWriter<>(record.getSchema());

            writer.write(record, encoder);
            encoder.flush();

            return outputStream.toString(StandardCharsets.UTF_8);

        } catch (IOException e) {
            throw new RuntimeException("Failed to convert Avro to JSON", e);
        }
    }
}
