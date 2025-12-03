package ru.kuznetsov.shop.business.access.service.business;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import ru.kuznetsov.shop.business.access.service.OperationService;
import ru.kuznetsov.shop.represent.contract.business.ProductContract;
import ru.kuznetsov.shop.represent.dto.ProductCardDto;
import ru.kuznetsov.shop.represent.dto.ProductDto;
import ru.kuznetsov.shop.represent.dto.util.ProductCardPage;

import java.util.*;

import static ru.kuznetsov.shop.business.access.common.ConstValues.PRODUCT_MODULE;

@Service
public class ProductContractImpl extends AbstractContractImpl<ProductDto> implements ProductContract {

    private final OperationService operationService;

    protected ProductContractImpl(@Qualifier("product") WebClient webClient, OperationService operationService) {
        super(webClient);
        this.operationService = operationService;
    }

    @Override
    protected String getModuleName() {
        return PRODUCT_MODULE;
    }

    @Override
    public Collection<ProductDto> getAllByOwnerId(UUID ownerId) {
        return connector.sendGetRequest(
                getModuleName(),
                Collections.singletonMap("ownerId", ownerId.toString()),
                null,
                ProductDto.class
        );
    }

    @Override
    public Collection<ProductCardDto> getProductCardsByOwnerIdAOrCategoryId(UUID ownerId, Long categoryId) {
        Map<String, Object> params = new HashMap<>();
        if (ownerId != null) params.put("ownerId", ownerId.toString());
        if (categoryId != null) params.put("categoryId", categoryId.toString());

        return connector.sendGetRequest(getModuleName() + "/card", params, null, ProductCardDto.class);
    }

    @Override
    public ProductCardPage getProductCardDtoPageable(int pageNum, int pageSize, String sortBy, String sortDirection) {
        Map<String, Object> params = new HashMap<>();
        params.put("pageNumber", pageNum);
        params.put("pageSize", pageSize);
        if (sortBy != null) params.put("sortBy", sortBy);
        if (sortDirection != null) params.put("order", sortDirection);

        return connector.sendGetRequest(getModuleName() + "/card/page", params, null, ProductCardPage.class).get(0);
    }

    @Override
    public ProductCardPage getProductCardDtoByCategoryOrOwnerIdPageable(UUID ownerId,
                                                                        Long categoryId,
                                                                        int pageNum,
                                                                        int pageSize,
                                                                        String sortBy,
                                                                        String sortDirection) {
        Map<String, Object> params = new HashMap<>();
        params.put("pageNumber", pageNum);
        params.put("pageSize", pageSize);
        if (sortBy != null) params.put("sortBy", sortBy);
        if (sortDirection != null) params.put("order", sortDirection);
        if (ownerId != null) params.put("ownerId", ownerId.toString());
        if (categoryId != null) params.put("categoryId", categoryId);

        return connector.sendGetRequest(getModuleName() + "/card/page", params, null, ProductCardPage.class).get(0);
    }

    @Override
    public Collection<ProductDto> getAllByOwnerIdOrCategoryId(UUID ownerId, Long categoryId) {
        Map<String, Object> params = new HashMap<>();
        if (ownerId != null) params.put("ownerId", ownerId.toString());
        if (categoryId != null) params.put("categoryId", categoryId.toString());

        return connector.sendGetRequest(getModuleName(), params, null, ProductDto.class);
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
