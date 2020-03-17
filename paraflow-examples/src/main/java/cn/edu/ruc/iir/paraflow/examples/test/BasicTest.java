package cn.edu.ruc.iir.paraflow.examples.test;

import cn.edu.ruc.iir.paraflow.collector.DefaultCollector;
import cn.edu.ruc.iir.paraflow.collector.ParaflowProducer;
import cn.edu.ruc.iir.paraflow.collector.utils.CollectorConfig;
import cn.edu.ruc.iir.paraflow.commons.exceptions.ConfigFileNotFoundException;
import cn.edu.ruc.iir.paraflow.loader.ParaflowConsumer;
import cn.edu.ruc.iir.paraflow.loader.utils.LoaderConfig;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.TopicPartition;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

/**
 * paraflow
 *
 * @author taoyouxian
 */
public class BasicTest
{
    private BasicTest()
    {
    }

    public static void main(String[] args)
    {
        int count = 5;
        long time = 1000;
        String value = null;
        if (args.length >= 3) {
            int temp = Integer.parseInt(args[0]);
            if (temp > count) {
                count = temp;
            }
            if (args[2].toLowerCase().equals("json")) {
                value = "{\"caption\":\"" + UUID.randomUUID().toString().substring(0, 16) + "\"}";
            }
            else if (args[2].toLowerCase().equals("raw")) {
                value = UUID.randomUUID().toString().substring(0, 16);
            }
            else if (args[2].toLowerCase().equals("csv")) {
                value = UUID.randomUUID().toString().substring(0, 16) + "," + UUID.randomUUID().toString().substring(0, 1);
            }
            if (args.length >= 4) {
                time = Long.parseLong(args[3]);
            }
        }
        else {
            System.out.println("Usage: MsgCount Function[Producer or Consumer] Format[Row,Csv,Json]");
            System.exit(-1);
        }
        String topicName = "test.test";
        if (args[1].toLowerCase().equals("producer") | args[1].equals("1")) {
            System.out.println("Producer begin...");
            try {
                DefaultCollector<String> collector = new DefaultCollector<>();
                boolean isExisted = collector.existsTopic(topicName);
                System.out.println("Topic exist:" + isExisted);
                final CollectorConfig collectorConfig = CollectorConfig.INSTANCE();

                ProducerRecord<Long, byte[]> record = null;
                ParaflowProducer producer = new ParaflowProducer(collectorConfig, time);
                for (long i = 1; i <= count; i++) {
                    record = new ProducerRecord<>(topicName, 0, System.currentTimeMillis(),
                            i, value.getBytes());
                    producer.sendMsg(record, value.length());
                    System.out.println("Msg[" + i + "]：" + value);
                    try {
                        Thread.sleep(2 * 1000);
                    }
                    catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                producer.close();
            }
            catch (ConfigFileNotFoundException e) {
                e.printStackTrace();
            }
        }
        else if (args[1].toLowerCase().equals("consumer") | args[1].equals("2")) {
            System.out.println("Consumer begin...");
            try {
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

                ParaflowConsumer kafkaConsumer = new ParaflowConsumer(topicPartitions, loaderConfig.getProperties());
                Consumer<Long, byte[]> consumer = kafkaConsumer.getConsumer();
                consumer.seekToBeginning(topicPartitions);
                int i = 1;
                while (true) {
                    ConsumerRecords<Long, byte[]> consumerRecords = consumer.poll(time);
                    System.out.println("TopicPartition:" + consumerRecords.partitions().toString());
                    if (!consumerRecords.isEmpty()) {
                        Iterator<ConsumerRecord<Long, byte[]>> it = consumerRecords.iterator();
                        int num = 1;
                        while (it.hasNext()) {
                            ConsumerRecord<Long, byte[]> consumerRecord = it.next();
                            byte[] content = consumerRecord.value();
                            Long key = consumerRecord.key();
                            System.out.println("value[" + num + "]：" + key + "," + new String(content));
                            if (num++ > 30) {
                                System.out.println("Consumer End.");
                                System.exit(1);
                            }
                        }
                    }
                    else {
                        System.out.println("ConsumerRecords is empty.");
                    }
                        if (i > 2) {
                        System.out.println("Test Exit.");
                        break;
                    }
                    System.out.println("Consume Num:" + i);
                    i++;
                    consumer.commitAsync();
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        else {
            System.out.println("Test Functio  Not Found.");
        }
    }
}
