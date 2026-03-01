package com.lunastratos.asset.AssetMaster.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class BuildingRequest {

    @NotBlank
    private String buildingName;

    private String address;
    private String adminName;
    private String adminPhone;
    private String description;
    private String phone;
    private Boolean parkingAvailable = false;

    @NotNull
    private List<FloorInfo> floors;

    @Getter
    @Setter
    public static class FloorInfo {
        private int floor;
        private int roomCount;
    }
}
