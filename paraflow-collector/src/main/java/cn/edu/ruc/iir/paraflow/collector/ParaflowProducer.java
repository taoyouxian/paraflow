package cn.edu.ruc.iir.paraflow.collector;

import cn.edu.ruc.iir.paraflow.collector.utils.CollectorConfig;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.ByteArraySerializer;
import org.apache.kafka.common.serialization.IntegerSerializer;

import java.nio.ByteBuffer;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicLong;
import kafka.serializer.Encoder;
import kafka.utils.VerifiableProperties;
import org.apache.kafka.common.serialization.LongSerializer;

/**
 * paraflow
 *
 * @author taoyouxian
 */
public class ParaflowProducer
{
    private final KafkaProducer<Long, byte[]> kafkaProducer;
    private final AtomicLong ackRecords = new AtomicLong();
    private final ThroughputStats throughputStats;

    public ParaflowProducer(CollectorConfig conf, long statsInterval)
    {
        Properties config = conf.getProperties();
        // set the producer configuration properties for kafka record key and value serializers
        System.out.println("ParaflowProducer Init");
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, LongSerializer.class.getName());
        if (!config.containsKey(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG)) {
            System.out.println("VALUE SERIALIZER");
            config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, ByteArraySerializer.class.getName());
        }
        if (!config.containsKey(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG)) {
            throw new IllegalArgumentException(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG + " must be specified in the config");
        }
        kafkaProducer = new KafkaProducer<>(config);
        this.throughputStats = new ThroughputStats(statsInterval, conf.isMetricEnabled(), conf.getPushGateWayUrl(),
                conf.getCollectorId());
    }

    public void sendMsg(ProducerRecord<Long, byte[]> record, int length)
    {
        try {
            kafkaProducer.send(record, new ProducerCallback(length, throughputStats));
        }
        catch (Exception e) {
            System.out.println("Producer Sending Msg Exception.");
            e.printStackTrace();
        }
    }

    public long getAckRecords()
    {
        return ackRecords.get();
    }

    public void close()
    {
        kafkaProducer.flush();
        kafkaProducer.close();
    }

    private final class ProducerCallback
            implements Callback
    {
        private final int bytes;
        private final ThroughputStats throughputStats;

        ProducerCallback(int bytes, ThroughputStats throughputStats)
        {
            this.bytes = bytes;
            this.throughputStats = throughputStats;
        }

        /**
         * A callback method the user can implement to provide asynchronous handling of request completion. This method will
         * be called when the record sent to the server has been acknowledged. Exactly one of the arguments will be
         * non-null.
         *
         * @param metadata  The metadata for the record that was sent (i.e. the partition and offset). Null if an error
         *                  occurred.
         * @param exception The exception thrown during processing of this record. Null if no error occurred.
         *                  Possible thrown exceptions include:
         *                  <p>
         *                  Non-Retriable exceptions (fatal, the message will never be sent):
         *                  <p>
         *                  InvalidTopicException
         *                  OffsetMetadataTooLargeException
         *                  RecordBatchTooLargeException
         *                  RecordTooLargeException
         *                  UnknownServerException
         *                  <p>
         *                  Retriable exceptions (transient, may be covered by increasing #.retries):
         *                  <p>
         *                  CorruptRecordException
         *                  InvalidMetadataException
         *                  NotEnoughReplicasAfterAppendException
         *                  NotEnoughReplicasException
         *                  OffsetOutOfRangeException
         *                  TimeoutException
         */
        @Override
        public void onCompletion(RecordMetadata metadata, Exception exception)
        {
            if (exception == null) {
                this.throughputStats.record(bytes, 1);
            }
        }
    }

    private class NumberEncoder
        implements Encoder<Number>
{

    public NumberEncoder()
    {
    }

    @SuppressWarnings("UnusedParameters")
    public NumberEncoder(VerifiableProperties properties)
    {
        // constructor required by Kafka
    }

    @Override
    public byte[] toBytes(Number value)
    {
        ByteBuffer buf = ByteBuffer.allocate(8);
        buf.putLong(value == null ? 0L : value.longValue());
        return buf.array();
    }
}
}
