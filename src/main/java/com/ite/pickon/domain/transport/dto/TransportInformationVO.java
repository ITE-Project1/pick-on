package com.ite.pickon.domain.transport.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TransportInformationVO {
    private int fromStoreId;
    private int toStoreId;
    private int transportTime;
}
