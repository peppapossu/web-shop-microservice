package com.ks.orderservice.infrastructure.outbox.mapper;

import com.ks.orderservice.domain.order.Order;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.io.BinaryEncoder;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.io.EncoderFactory;
import org.apache.avro.specific.SpecificDatumWriter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Slf4j
public class OutboxAvroSerializer {
    public static byte[] serialize(Order order) {

        ByteArrayOutputStream out = new ByteArrayOutputStream();

        BinaryEncoder encoder =
                EncoderFactory.get().binaryEncoder(out, null);

        DatumWriter<Order> writer =
                new SpecificDatumWriter<>(Order.class);

        try {
            writer.write(order, encoder);
            encoder.flush();
        } catch (IOException e) {
            log.error("AvroSerializer Exception : {}", e.getMessage(), e);
            throw new RuntimeException(e);
        }

        return out.toByteArray();
    }
}
