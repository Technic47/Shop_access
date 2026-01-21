package ru.kuznetsov.shop.business.access.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import ru.kuznetsov.shop.business.access.connector.WebClientConnector;
import ru.kuznetsov.shop.represent.contract.OperationContract;
import ru.kuznetsov.shop.represent.dto.OperationDto;
import ru.kuznetsov.shop.represent.dto.OperationPayloadDto;
import ru.kuznetsov.shop.represent.enums.OperationType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ru.kuznetsov.shop.business.access.common.ConstValues.OPERATION_MODULE;

@Service
public class OperationService implements OperationContract {

    protected final WebClientConnector connector;

    public OperationService(@Qualifier("operation") WebClient.Builder webClientBuilder) {
        this.connector = new WebClientConnector(webClientBuilder);
    }

    @Override
    public boolean containsOperation(String operationId) {
        return connector.sendGetRequest(OPERATION_MODULE + "/" + operationId + "/contains",
                null,
                null,
                Boolean.class).get(0);
    }

    @Override
    public OperationDto getOperation(String operationId) {
        return connector.sendGetRequest(OPERATION_MODULE + "/" + operationId,
                null,
                null,
                OperationDto.class).get(0);
    }

    @Override
    public List<OperationPayloadDto> getOperationData(String operationId) {
        return connector.sendGetRequest(OPERATION_MODULE + "/payload/" + operationId,
                null,
                null,
                OperationPayloadDto.class);
    }

    @Override
    public List<OperationPayloadDto> getOperationData(OperationDto operation) {
        return connector.sendPostRequest(OPERATION_MODULE + "/payload",
                null,
                operation,
                OperationPayloadDto.class);
    }

    @Override
    public void removeOperations(List<OperationDto> operationIds) {
        connector.sendDeleteRequest(OPERATION_MODULE + "/batch",
                null,
                operationIds,
                void.class);
    }

    @Override
    public void removeOperation(OperationDto operation) {
        connector.sendDeleteRequest(OPERATION_MODULE,
                null,
                operation,
                void.class);
    }

    @Override
    public void addOperations(String objectJson, String operationId, OperationType operationType, int result) {
        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("objectJson", objectJson);
        requestMap.put("operationId", operationId);
        requestMap.put("operationType", operationType);
        requestMap.put("result", result);

        connector.sendPostRequest(OPERATION_MODULE + "/payload",
                null,
                requestMap,
                OperationPayloadDto.class);
    }

    @Override
    public List<Long> getEntityIdsByOperationId(String operationId) {
        return connector.sendGetRequest(OPERATION_MODULE + "/payload/" + operationId + "/wait",
                null,
                null,
                Long.class);
    }
}
