package org.apache.storm.metrics2;


import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricRegistry;
import org.apache.storm.task.WorkerTopologyContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class StormMetricRegistry {

    private static final Logger LOG = LoggerFactory.getLogger(StormMetricRegistry.class);

    private static final MetricRegistry REGISTRY = new MetricRegistry();

    private static ConsoleReporter REPORTER;
    static {
        REPORTER = ConsoleReporter.forRegistry(REGISTRY)
                .convertRatesTo(TimeUnit.SECONDS)
                .convertDurationsTo(TimeUnit.MILLISECONDS)
                .build();
        REPORTER.start(15, TimeUnit.SECONDS);
    }


    public static Meter meter(String name, WorkerTopologyContext context, String componentId){
        // storm.worker.{topology}.{host}.{port}
        // TODO: hostname
        String metricName = String.format("storm.worker.%s.%s.%s-%s", context.getStormId(), componentId, context.getThisWorkerPort(), name);

        return REGISTRY.meter(metricName);
    }

    public static void shutdown(){
        REPORTER.stop();
    }
}
