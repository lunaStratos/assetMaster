package com.lunastratos.asset.AssetMaster.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class BuildingRequest {

    @NotBlank
    private String buildingName;

    private String address;
    private String adminName;
    private String adminPhone;
    private String description;
    private String phone;

    @NotNull
    private List<FloorInfo> floors;

    public static class FloorInfo {
        private int floor;
        private int roomCount;

        public int getFloor() { return floor; }
        public void setFloor(int floor) { this.floor = floor; }

        public int getRoomCount() { return roomCount; }
        public void setRoomCount(int roomCount) { this.roomCount = roomCount; }
    }

    public String getBuildingName() { return buildingName; }
    public void setBuildingName(String buildingName) { this.buildingName = buildingName; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getAdminName() { return adminName; }
    public void setAdminName(String adminName) { this.adminName = adminName; }

    public String getAdminPhone() { return adminPhone; }
    public void setAdminPhone(String adminPhone) { this.adminPhone = adminPhone; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public List<FloorInfo> getFloors() { return floors; }
    public void setFloors(List<FloorInfo> floors) { this.floors = floors; }
}
