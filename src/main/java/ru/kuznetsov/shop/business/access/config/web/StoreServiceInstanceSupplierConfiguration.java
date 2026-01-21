package ru.kuznetsov.shop.business.access.config.web;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.discovery.DiscoveryClient;

public class StoreServiceInstanceSupplierConfiguration extends AbstractServiceInstanceSupplierConfiguration {

    @Value("${service.module-name.store}")
    private String moduleName;

    protected StoreServiceInstanceSupplierConfiguration(DiscoveryClient discoveryClient) {
        super(discoveryClient);
    }

    @Override
    String getServiceName() {
        return moduleName;
    }
}
