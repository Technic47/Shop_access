package ru.kuznetsov.shop.business.access.config.web;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.discovery.DiscoveryClient;

public class ProductServiceInstanceSupplierConfiguration extends AbstractServiceInstanceSupplierConfiguration {

    @Value("${service.module-name.product}")
    private String moduleName;

    protected ProductServiceInstanceSupplierConfiguration(DiscoveryClient discoveryClient) {
        super(discoveryClient);
    }

    @Override
    String getServiceName() {
        return moduleName;
    }
}
