package cn.edu.ruc.iir.paraflow.collector;

import cn.edu.ruc.iir.paraflow.collector.utils.CollectorConfig;
import cn.edu.ruc.iir.paraflow.commons.Message;
import cn.edu.ruc.iir.paraflow.commons.ParaflowFiberPartitioner;
import cn.edu.ruc.iir.paraflow.commons.exceptions.ConfigFileNotFoundException;
import com.google.common.util.concurrent.ListenableFuture;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;

import static com.google.common.util.concurrent.Futures.immediateFuture;

/**
 * paraflow
 *
 * @author guodong
 */
public class FlowTask<T>
{
    private final DataFlow<T> dataFlow;
    private final ParaflowKafkaProducer kafkaProducer;
    private ParaflowProducer longProducer;
    private final String topic;
    private long startTime;

    FlowTask(DataFlow<T> dataFlow, ParaflowKafkaProducer kafkaProducer)
    {
        this.dataFlow = dataFlow;
        this.topic = dataFlow.getSink().getDb() + "." + dataFlow.getSink().getTbl();
        this.kafkaProducer = kafkaProducer;
    }

    FlowTask(DataFlow<T> dataFlow, ParaflowKafkaProducer kafkaProducer, ParaflowProducer longProducer)
    {
        this.dataFlow = dataFlow;
        this.topic = dataFlow.getSink().getDb() + "." + dataFlow.getSink().getTbl();
        this.kafkaProducer = kafkaProducer;
        this.longProducer = longProducer;
    }

    ListenableFuture<?> execute()
    {
        System.out.println("Collector Begin Executing.");
        CollectorConfig config = CollectorConfig.INSTANCE();
        try {
            config.init();
        } catch (ConfigFileNotFoundException e) {
            e.printStackTrace();
        }
        startTime = System.currentTimeMillis();
        ParaflowFiberPartitioner partitioner = dataFlow.getPartitioner();
        long count = 1;
        Properties properties = config.getProperties();
        long max_count = 0;
        if (properties.get("index.max") != null) {
            max_count = Long.parseLong(properties.getProperty("index.max"));
        }
        System.out.println("Collector Execute Num is:" + max_count);
        while (!dataFlow.isFinished()) {
            try {
                if (count > max_count) {
                    System.out.println("Collector Exit.");
                    this.close();
                    System.exit(-1);
                }
                Message message = dataFlow.next();
                System.out.println("Collector DataFlow:" + Thread.currentThread().getId() + "#" + message.toString());
                ProducerRecord<byte[], byte[]> record;
                int partition = partitioner.getFiberId(message.getKey());

                record = new ProducerRecord<>(topic, partition, message.getTimestamp(),
                        new byte[0], message.getValue());
                System.out.println("Collector Msg Sending to Topic[" + topic + "]:" + (++count));
                kafkaProducer.sendMsg(record, message.getValue().length);
            }
            catch (Exception e) {
                System.out.println("Producer Sending Msg Error.");
                e.printStackTrace();
            }
        }
        return immediateFuture(null);
    }

    ListenableFuture<?> executeLong()
    {
        System.out.println("Collector Begin Executing.");
        CollectorConfig config = CollectorConfig.INSTANCE();
        try {
            config.init();
        } catch (ConfigFileNotFoundException e) {
            e.printStackTrace();
        }
        startTime = System.currentTimeMillis();
        ParaflowFiberPartitioner partitioner = dataFlow.getPartitioner();
        long count = 1;
        Properties properties = config.getProperties();
        long max_count = 0;
        if (properties.get("index.max") != null) {
            max_count = Long.parseLong(properties.getProperty("index.max"));
        }
        System.out.println("Collector Execute Num is:" + max_count);
        while (!dataFlow.isFinished()) {
            try {
                if (count > max_count) {
                    System.out.println("Collector Exit.");
                    this.close();
                    System.exit(-1);
                }
                Message message = dataFlow.next();
                System.out.println("Collector DataFlow:" + Thread.currentThread().getId() + "#" + message.toString());
                ProducerRecord<Long, byte[]> record;
                int partition = partitioner.getFiberId(message.getKey());

                record = new ProducerRecord<>(topic, partition, message.getTimestamp(),
                        count, message.getValue());
                System.out.println("Collector Msg Sending to Topic[" + topic + "]:" + (++count));
                longProducer.sendMsg(record, message.getValue().length);
            }
            catch (Exception e) {
                System.out.println("Producer Sending Msg Error.");
                e.printStackTrace();
            }
        }
        return immediateFuture(null);
    }

    public double flowSpeed()
    {
        return 1.0 * kafkaProducer.getAckRecords() / (System.currentTimeMillis() - startTime);
    }

    void close()
    {
        kafkaProducer.close();
    }
}
