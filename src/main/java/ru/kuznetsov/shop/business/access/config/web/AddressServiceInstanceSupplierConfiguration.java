package ru.kuznetsov.shop.business.access.config.web;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.discovery.DiscoveryClient;

public class AddressServiceInstanceSupplierConfiguration extends AbstractServiceInstanceSupplierConfiguration {

    @Value("${service.module-name.address}")
    private String moduleName;

    protected AddressServiceInstanceSupplierConfiguration(DiscoveryClient discoveryClient) {
        super(discoveryClient);
    }

    @Override
    String getServiceName() {
        return moduleName;
    }
}
