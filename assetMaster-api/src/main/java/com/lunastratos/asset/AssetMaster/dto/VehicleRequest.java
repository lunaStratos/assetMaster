package com.lunastratos.asset.AssetMaster.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VehicleRequest {

    @NotBlank
    private String licensePlate;

    private String vehicleType;
    private String description;
}
