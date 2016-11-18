package org.apache.storm.metrics2;


import com.codahale.metrics.*;
import com.codahale.metrics.graphite.Graphite;
import com.codahale.metrics.graphite.GraphiteReporter;
import org.apache.storm.task.WorkerTopologyContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

public class StormMetricRegistry {

    private static final Logger LOG = LoggerFactory.getLogger(StormMetricRegistry.class);

    private static final MetricRegistry REGISTRY = new MetricRegistry();

    private static ScheduledReporter REPORTER;
    static {
//        REPORTER = ConsoleReporter.forRegistry(REGISTRY)
//                .convertRatesTo(TimeUnit.SECONDS)
//                .convertDurationsTo(TimeUnit.MILLISECONDS)
//                .build();


        final Graphite graphite = new Graphite(new InetSocketAddress("graphite", 2003));
        REPORTER = GraphiteReporter.forRegistry(REGISTRY)
                .convertRatesTo(TimeUnit.SECONDS)
                .convertDurationsTo(TimeUnit.MILLISECONDS)
                .filter(MetricFilter.ALL)
                .build(graphite);

        REPORTER.start(15, TimeUnit.SECONDS);
    }


    public static <T> SimpleGauge<T>  gauge(T initialValue, String name, String topologyId, Integer port){
        SimpleGauge<T> gauge = new SimpleGauge<>(initialValue);
        String metricName = String.format("storm.worker.%s.%s-%s", topologyId, port, name);
        return REGISTRY.register(metricName, gauge);
    }

    public static DisruptorMetrics disruptorMetrics(String name, String topologyId, Integer port){
        return new DisruptorMetrics(
                StormMetricRegistry.gauge(0L, name + "-capacity", topologyId, port),
                StormMetricRegistry.gauge(0L, name + "-population", topologyId, port),
                StormMetricRegistry.gauge(0L, name + "-write-position", topologyId, port),
                StormMetricRegistry.gauge(0L, name + "-read-position", topologyId, port),
                StormMetricRegistry.gauge(0.0, name + "-arrival-rate", topologyId, port),
                StormMetricRegistry.gauge(0.0, name + "-sojourn-time-ms", topologyId, port),
                StormMetricRegistry.gauge(0L, name + "-overflow", topologyId, port),
                StormMetricRegistry.gauge(0.0F, name + "-percent-full", topologyId, port)
        );
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
