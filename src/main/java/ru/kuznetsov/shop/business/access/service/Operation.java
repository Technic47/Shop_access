package ru.kuznetsov.shop.business.access.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import ru.kuznetsov.shop.business.access.enums.OperationType;

@Data
@Getter
@EqualsAndHashCode
@AllArgsConstructor
public class Operation {
    private String id;
    private OperationType type;
    private int result;
}
