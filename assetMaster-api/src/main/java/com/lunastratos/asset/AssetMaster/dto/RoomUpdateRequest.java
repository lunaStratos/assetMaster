package com.lunastratos.asset.AssetMaster.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class RoomUpdateRequest {

    private String roomNumber;
    private String status;
    private String tenantName;
    private String tenantPhone;
    private String country;
    private BigDecimal deposit;
    private BigDecimal monthlyRent;
    private String accountNumber;
    private LocalDate startDate;
    private LocalDate endDate;
    private String description;
}
