package com.zy.spring.mildware.mq.kafka.consumer;

import com.zy.spring.mildware.mq.kafka.utils.SchemaUtils;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class KafkaConsumer {
    @KafkaListener(topics = "kafka-avro-topic",
            groupId = "kafka-avro-topic-group01",
            properties = {"key.deserializer:org.apache.kafka.common.serialization.StringDeserializer",
                    "value.deserializer:org.apache.kafka.common.serialization.ByteArrayDeserializer"})
    public void kafkaAvro(ConsumerRecord<?, byte[]> record) {
        Optional<byte[]> optional = Optional.ofNullable(record.value());
        if (optional.isPresent()) {
            byte[] bytes = optional.get();
            Schema schema = SchemaUtils.getSchema("avro/Stu.avsc");
            GenericRecord genericRecord = SchemaUtils.bytes2Record(bytes, schema);
            System.out.println("--------------------------------------");
            System.out.println(genericRecord);
            System.out.println("--------------------------------------");
        }
    }

}
