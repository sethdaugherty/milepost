package io.sethdaugherty.milepost.configuration;

import java.util.List;

import io.prometheus.client.Collector;

public class ExporterRegister {
    private List<Collector> collectors; 

    public ExporterRegister(List<Collector> collectors) {
         for (Collector collector : collectors) {
             collector.register();
         }
         this.collectors = collectors;
    }

    public List<Collector> getCollectors() {
         return collectors;
    }

}