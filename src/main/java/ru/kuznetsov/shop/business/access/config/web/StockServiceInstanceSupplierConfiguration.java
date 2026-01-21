package ru.kuznetsov.shop.business.access.config.web;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.discovery.DiscoveryClient;

public class StockServiceInstanceSupplierConfiguration extends AbstractServiceInstanceSupplierConfiguration {

    @Value("${service.module-name.stock}")
    private String moduleName;

    protected StockServiceInstanceSupplierConfiguration(DiscoveryClient discoveryClient) {
        super(discoveryClient);
    }

    @Override
    String getServiceName() {
        return moduleName;
    }
}
