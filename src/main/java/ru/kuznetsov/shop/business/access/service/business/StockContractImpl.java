package ru.kuznetsov.shop.business.access.service.business;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import ru.kuznetsov.shop.business.access.service.OperationService;
import ru.kuznetsov.shop.represent.contract.business.StockContract;
import ru.kuznetsov.shop.represent.dto.StockDto;

import java.util.Collection;
import java.util.HashMap;

import static ru.kuznetsov.shop.business.access.common.ConstValues.STOCK_MODULE;


@Service
public class StockContractImpl extends AbstractContractImpl<StockDto> implements StockContract {

    private final OperationService operationService;

    protected StockContractImpl(@Qualifier("stock") WebClient webClient, OperationService operationService) {
        super(webClient);
        this.operationService = operationService;
    }

    @Override
    protected String getModuleName() {
        return STOCK_MODULE;
    }

    @Override
    public Collection<StockDto> getAllByStoreId(Long storeId) {
        return connector.sendGetRequest(getModuleName() + "/store", new HashMap<>(), null, StockDto.class);
    }

    @Override
    public Collection<StockDto> getAllByProductId(Long productId) {
        return connector.sendGetRequest(getModuleName() + "/product", new HashMap<>(), null, StockDto.class);
    }


    @Override
    public StockDto create(StockDto entity) {
        String operationId = connector.sendPostRequest(getModuleName(), new HashMap<>(), entity, String.class).get(0);
        Long entityId = operationService.getEntityIdsByOperationId(operationId).get(0);

        return getById(entityId);
    }

    @Override
    public Collection<StockDto> createBatch(Collection<StockDto> entities) {
        String operationId = connector.sendPostRequest(getModuleName() + "/batch", new HashMap<>(), entities, String.class).get(0);
        return operationService.getEntityIdsByOperationId(operationId)
                .stream()
                .map(this::getById)
                .toList();
    }
}
