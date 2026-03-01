package com.lunastratos.asset.AssetMaster.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lunastratos.asset.AssetMaster.dto.BuildingRequest;
import com.lunastratos.asset.AssetMaster.entity.Building;
import com.lunastratos.asset.AssetMaster.entity.Room;
import com.lunastratos.asset.AssetMaster.repository.BuildingRepository;
import com.lunastratos.asset.AssetMaster.repository.RoomRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BuildingService {

    private final BuildingRepository buildingRepository;
    private final RoomRepository roomRepository;
    private final ObjectMapper objectMapper;

    public BuildingService(BuildingRepository buildingRepository, RoomRepository roomRepository, ObjectMapper objectMapper) {
        this.buildingRepository = buildingRepository;
        this.roomRepository = roomRepository;
        this.objectMapper = objectMapper;
    }

    @Transactional
    public Building createBuilding(Long memberId, BuildingRequest request) {
        Building building = new Building();
        building.setMemberId(memberId);
        building.setBuildingName(request.getBuildingName());
        building.setAddress(request.getAddress());
        building.setAdminName(request.getAdminName());
        building.setAdminPhone(request.getAdminPhone());
        building.setDescription(request.getDescription());
        building.setPhone(request.getPhone());
        building.setParkingAvailable(request.getParkingAvailable() != null && request.getParkingAvailable());

        try {
            building.setStructure(objectMapper.writeValueAsString(request.getFloors()));
        } catch (JsonProcessingException e) {
            throw new RuntimeException("구조 JSON 변환 실패", e);
        }

        Building saved = buildingRepository.save(building);

        // 층/방 자동 생성
        List<Room> rooms = new ArrayList<>();
        for (BuildingRequest.FloorInfo floorInfo : request.getFloors()) {
            for (int r = 1; r <= floorInfo.getRoomCount(); r++) {
                Room room = new Room();
                room.setBuildingId(saved.getId());
                room.setFloor(floorInfo.getFloor());
                room.setRoomNumber(floorInfo.getFloor() + "0" + r);
                room.setStatus("VACANT");
                rooms.add(room);
            }
        }
        roomRepository.saveAll(rooms);

        return saved;
    }

    public List<Building> getBuildings(Long memberId) {
        return buildingRepository.findByMemberIdOrderByCreatedAtDesc(memberId);
    }

    public Optional<Building> getBuilding(Long buildingId, Long memberId) {
        return buildingRepository.findByIdAndMemberId(buildingId, memberId);
    }

    @Transactional
    public Optional<Building> updateBuilding(Long buildingId, Long memberId, BuildingRequest request) {
        return buildingRepository.findByIdAndMemberId(buildingId, memberId).map(building -> {
            building.setBuildingName(request.getBuildingName());
            building.setAddress(request.getAddress());
            building.setAdminName(request.getAdminName());
            building.setAdminPhone(request.getAdminPhone());
            building.setDescription(request.getDescription());
            building.setPhone(request.getPhone());
            building.setParkingAvailable(request.getParkingAvailable() != null && request.getParkingAvailable());
            try {
                building.setStructure(objectMapper.writeValueAsString(request.getFloors()));
            } catch (JsonProcessingException e) {
                throw new RuntimeException("구조 JSON 변환 실패", e);
            }
            return buildingRepository.save(building);
        });
    }

    @Transactional
    public boolean deleteBuilding(Long buildingId, Long memberId) {
        return buildingRepository.findByIdAndMemberId(buildingId, memberId).map(building -> {
            roomRepository.deleteByBuildingId(buildingId);
            buildingRepository.delete(building);
            return true;
        }).orElse(false);
    }
}
