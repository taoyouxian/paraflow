package cn.edu.ruc.iir.paraflow.collector;

import cn.edu.ruc.iir.paraflow.collector.utils.CollectorConfig;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * collector runtime
 *
 * @author guodong
 */
class CollectorRuntime
{
    private static final ExecutorService executorService = Executors.newCachedThreadPool();
    private static final Map<String, FlowTask> flowTasks = new HashMap<>();
    private static final long statsInterval = 3000L;
    private static ParaflowKafkaProducer kafkaProducer;
    private static ParaflowProducer longProducer;

    CollectorRuntime(CollectorConfig conf)
    {
        kafkaProducer = new ParaflowKafkaProducer(conf, statsInterval);
        longProducer = new ParaflowProducer(conf, statsInterval);
    }

    static void destroy()
    {
        for (FlowTask task : flowTasks.values()) {
            task.close();
        }
        executorService.shutdownNow();
    }

    <T> void run(DataFlow<T> dataFlow, boolean flag)
    {
        FlowTask<T> task = new FlowTask<>(dataFlow, kafkaProducer, longProducer);
        flowTasks.put(dataFlow.getName(), task);
        executorService.submit((Runnable) task::executeLong);
    }

    <T> void run(DataFlow<T> dataFlow)
    {
        FlowTask<T> task = new FlowTask<>(dataFlow, kafkaProducer);
        flowTasks.put(dataFlow.getName(), task);
        executorService.submit((Runnable) task::execute);
    }

    public void sendMsgToTopic(String topicName, ProducerRecord<byte[], byte[]> record, int length)
    {
        try {
            kafkaProducer.sendMsg(record, length);
        }
        catch (Exception e) {
            System.out.println("Producer Sending Msg To Topic[" + topicName + "] Error.");
            e.printStackTrace();
        }
    }
}
