package com.ks.orderservice.mapper;

import org.apache.avro.io.BinaryEncoder;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.io.EncoderFactory;
import org.apache.avro.specific.SpecificDatumWriter;
import org.apache.avro.specific.SpecificRecord;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Component
public class AvroPayloadSerializer {

    public byte[] serialize(SpecificRecord record) {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            DatumWriter<SpecificRecord> writer =
                new SpecificDatumWriter<>(record.getSchema());

            BinaryEncoder encoder =
                EncoderFactory.get().binaryEncoder(out, null);

            writer.write(record, encoder);
            encoder.flush();

            return out.toByteArray();

        } catch (IOException e) {
            throw new IllegalStateException("Failed to serialize Avro", e);
        }
    }
}
