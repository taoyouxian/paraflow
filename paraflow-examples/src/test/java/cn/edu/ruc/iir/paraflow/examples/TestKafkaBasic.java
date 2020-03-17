package cn.edu.ruc.iir.paraflow.examples;

import cn.edu.ruc.iir.paraflow.collector.DefaultCollector;
import cn.edu.ruc.iir.paraflow.commons.exceptions.ConfigFileNotFoundException;
import cn.edu.ruc.iir.paraflow.loader.ParaflowKafkaConsumer;
import cn.edu.ruc.iir.paraflow.loader.utils.LoaderConfig;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.TopicPartition;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * paraflow
 *
 * @author taoyouxian
 */
public class TestKafkaBasic
{
    @Test
    public void createTopic()
    {
        try {
            DefaultCollector<String> collector = new DefaultCollector<>();
            collector.createTopic("exampledb-exampletbl", 10, (short) 1);
        }
        catch (ConfigFileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void listTopic()
    {
        try {
            String topicName = "test.tpch";
            DefaultCollector<String> collector = new DefaultCollector<>();
            boolean isExisted = collector.existsTopic(topicName);
            System.out.println("Topic exist:" + isExisted);
            if (isExisted) {
                collector.deleteTopic(topicName);
                System.out.println("Topic deleted:" + !collector.existsTopic(topicName));
            }
        }
        catch (ConfigFileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testSendingMsg()
    {
        try {
            String topicName = "test.test";
            DefaultCollector<String> collector = new DefaultCollector<>();
            boolean isExisted = collector.existsTopic(topicName);
            System.out.println("Topic exist:" + isExisted);
            byte[] value = "abcdefg".getBytes();
            ProducerRecord<byte[], byte[]> record = new ProducerRecord<>(topicName, 0, System.currentTimeMillis(),
                    new byte[0], value);
            int length = value.length;
            collector.sendMsgToTopic(topicName, record, length);
        }
        catch (ConfigFileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGettingMsg()
    {
        try {
            String topicName = "test.test";
            List<TopicPartition> topicPartitions = new ArrayList<>();
            TopicPartition topicPartition = new TopicPartition(topicName, 0);
            topicPartitions.add(topicPartition);
            final LoaderConfig loaderConfig = LoaderConfig.INSTANCE();
            try {
                loaderConfig.init();
            }
            catch (ConfigFileNotFoundException e) {
                e.printStackTrace();
            }

            ParaflowKafkaConsumer kafkaConsumer = new ParaflowKafkaConsumer(topicPartitions, loaderConfig.getProperties());
            Consumer<byte[], byte[]> consumer = kafkaConsumer.getConsumer();
            consumer.seekToBeginning(topicPartitions);
            while (true) {
                ConsumerRecords<byte[], byte[]> consumerRecords = consumer.poll(1000);
                if (!consumerRecords.isEmpty()) {
                    ConsumerRecord<byte[], byte[]> consumerRecord = consumerRecords.iterator().next();
                    byte[] content = consumerRecord.value();
                    System.out.println("value is:" + new String(content));
                }
                consumer.commitAsync();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
