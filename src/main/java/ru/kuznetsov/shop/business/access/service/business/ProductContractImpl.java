package ru.kuznetsov.shop.business.access.service.business;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import ru.kuznetsov.shop.business.access.service.OperationService;
import ru.kuznetsov.shop.represent.contract.business.ProductContract;
import ru.kuznetsov.shop.represent.dto.ProductDto;

import java.util.Collection;
import java.util.HashMap;

import static ru.kuznetsov.shop.business.access.common.ConstValues.PRODUCT_MODULE;

@Service
public class ProductContractImpl extends AbstractContractImpl<ProductDto> implements ProductContract {

    private final OperationService operationService;

    protected ProductContractImpl(@Qualifier("product") WebClient webClient, OperationService operationService1) {
        super(webClient);
        this.operationService = operationService1;
    }

    @Override
    protected String getModuleName() {
        return PRODUCT_MODULE;
    }

    @Override
    public ProductDto create(ProductDto entity) {
        String operationId = connector.sendPostRequest(getModuleName(), new HashMap<>(), entity, String.class).get(0);
        Long entityId = operationService.getEntityIdsByOperationId(operationId).get(0);

        return getById(entityId);
    }

    @Override
    public Collection<ProductDto> createBatch(Collection<ProductDto> entities) {
        String operationId = connector.sendPostRequest(getModuleName() + "/batch", new HashMap<>(), entities, String.class).get(0);
        return operationService.getEntityIdsByOperationId(operationId)
                .stream()
                .map(this::getById)
                .toList();
    }
}
