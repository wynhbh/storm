package org.apache.storm.metrics2;

import org.apache.storm.utils.DisruptorQueue;

public class DisruptorMetrics {
    private SimpleGauge<Long> capacity;
    private SimpleGauge<Long> population;
    private SimpleGauge<Long> writePosition;
    private SimpleGauge<Long> readPosition;
    private SimpleGauge<Double> arrivalRate; // TODO: Change to meter
    private SimpleGauge<Double> sojournTime;
    private SimpleGauge<Long> overflow;
    private SimpleGauge<Float> pctFull;


    DisruptorMetrics(SimpleGauge<Long> capacity,
                    SimpleGauge<Long> population,
                    SimpleGauge<Long> writePosition,
                    SimpleGauge<Long> readPosition,
                    SimpleGauge<Double> arrivalRate,
                    SimpleGauge<Double> sojournTime,
                    SimpleGauge<Long> overflow,
                    SimpleGauge<Float> pctFull) {
        this.capacity = capacity;
        this.population = population;
        this.writePosition = writePosition;
        this.readPosition = readPosition;
        this.arrivalRate = arrivalRate;
        this.sojournTime = sojournTime;
        this.overflow = overflow;
        this.pctFull = pctFull;
    }

    public void setCapacity(Long capacity) {
        this.capacity.set(capacity);
    }

    public void setPopulation(Long population) {
        this.population.set(population);
    }

    public void setWritePosition(Long writePosition) {
        this.writePosition.set(writePosition);
    }

    public void setReadPosition(Long readPosition) {
        this.readPosition.set(readPosition);
    }

    public void setArrivalRate(Double arrivalRate) {
        this.arrivalRate.set(arrivalRate);
    }

    public void setSojournTime(Double soujournTime) {
        this.sojournTime.set(soujournTime);
    }

    public void setOverflow(Long overflow) {
        this.overflow.set(overflow);
    }

    public void setPercentFull(Float pctFull){
        this.pctFull.set(pctFull);
    }

    public void set(DisruptorQueue.QueueMetrics metrics){
        this.capacity.set(metrics.capacity());
        this.population.set(metrics.population());
        this.writePosition.set(metrics.writePos());
        this.readPosition.set(metrics.readPos());
        this.arrivalRate.set(metrics.arrivalRate());
        this.sojournTime.set(metrics.sojournTime());
        this.overflow.set(metrics.overflow());
        this.pctFull.set(metrics.pctFull());
    }
}
