package ru.kuznetsov.shop.business.access.config.web;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.discovery.DiscoveryClient;

public class OperationServiceInstanceSupplierConfiguration extends AbstractServiceInstanceSupplierConfiguration {

    @Value("${service.module-name.operation}")
    private String moduleName;

    protected OperationServiceInstanceSupplierConfiguration(DiscoveryClient discoveryClient) {
        super(discoveryClient);
    }

    @Override
    String getServiceName() {
        return moduleName;
    }
}
