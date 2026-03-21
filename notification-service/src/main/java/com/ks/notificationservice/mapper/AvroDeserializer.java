package com.ks.notificationservice.mapper;

import com.ks.avro.order.OrderCreatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.io.BinaryDecoder;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.DecoderFactory;
import org.apache.avro.specific.SpecificDatumReader;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class AvroDeserializer {
    public OrderCreatedEvent deserialize(byte[] message) {

        DatumReader<OrderCreatedEvent> reader =
                new SpecificDatumReader<>(OrderCreatedEvent.class);

        BinaryDecoder decoder =
                DecoderFactory.get().binaryDecoder(message, null);

        OrderCreatedEvent result;
        try {
            result = reader.read(null, decoder);
        } catch (IOException e) {
            log.error("Error while deserialize message", e);
            throw new RuntimeException(e);
        }
        return result;
    }
}
