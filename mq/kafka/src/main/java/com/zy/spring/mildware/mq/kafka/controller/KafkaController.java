package com.zy.spring.mildware.mq.kafka.controller;

import com.zy.spring.mildware.mq.kafka.entity.Stu;
import com.zy.spring.mildware.mq.kafka.utils.SchemaUtils;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.ByteArraySerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/kafka/")
public class KafkaController {

    @Autowired
    private KafkaTemplate kafkaTemplate;

    private Producer<String, byte[]> producer;

    @RequestMapping("send")
    public void send(@RequestBody Stu stu) {
        Schema schema = SchemaUtils.getSchema("avro/Stu.avsc");
        GenericRecord record = new GenericData.Record(schema);
        record.put("id", stu.getId());
        record.put("name", stu.getName());
        record.put("age", stu.getAge());
        byte[] bytes = SchemaUtils.record2Bytes(record, schema);

        // 方法1: 用 KafkaTemplate 实现
        // kafkaTemplate.send("kafka-avro-topic", bytes);

        // 方法2: 用 Producer 发送消息
        ProducerRecord<String, byte[]> producerRecord = new ProducerRecord<>("kafka-avro-topic", bytes);
        producer.send(producerRecord);
    }

    @PostConstruct
    public void init() {
        Map<String, Object> configs = new HashMap<>(1);
        configs.put("bootstrap.servers", "192.168.0.156:9092");
        ProducerFactory<String, byte[]> producerFactory = new DefaultKafkaProducerFactory<>(configs, new StringSerializer(), new ByteArraySerializer());
        this.producer = producerFactory.createProducer();
    }

}
