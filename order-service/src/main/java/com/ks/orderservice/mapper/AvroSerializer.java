package com.ks.orderservice.mapper;

import com.ks.avro.order.OrderCreatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.io.BinaryEncoder;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.io.EncoderFactory;
import org.apache.avro.specific.SpecificDatumWriter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Slf4j
public class AvroSerializer {
    public static byte[] serialize(OrderCreatedEvent orderCreatedEvent) {

        ByteArrayOutputStream out = new ByteArrayOutputStream();

        BinaryEncoder encoder =
                EncoderFactory.get().binaryEncoder(out, null);

        DatumWriter<OrderCreatedEvent> writer =
                new SpecificDatumWriter<>(OrderCreatedEvent.class);

        try {
            writer.write(orderCreatedEvent, encoder);
            encoder.flush();
        } catch (IOException e) {
            log.error("AvroSerializer Exception : {}", e.getMessage(), e);
            throw new RuntimeException(e);
        }

        return out.toByteArray();
    }
}
