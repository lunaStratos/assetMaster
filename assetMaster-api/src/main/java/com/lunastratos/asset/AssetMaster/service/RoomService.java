package com.lunastratos.asset.AssetMaster.service;

import com.lunastratos.asset.AssetMaster.dto.RoomUpdateRequest;
import com.lunastratos.asset.AssetMaster.entity.Room;
import com.lunastratos.asset.AssetMaster.repository.RoomRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class RoomService {

    private final RoomRepository roomRepository;

    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    public List<Room> getRoomsByBuilding(Long buildingId) {
        return roomRepository.findByBuildingIdOrderByFloorAscRoomNumberAsc(buildingId);
    }

    public Optional<Room> getRoom(Long roomId) {
        return roomRepository.findById(roomId);
    }

    @Transactional
    public Optional<Room> updateRoom(Long roomId, RoomUpdateRequest request) {
        return roomRepository.findById(roomId).map(room -> {
            if (request.getRoomNumber() != null) room.setRoomNumber(request.getRoomNumber());
            if (request.getStatus() != null) room.setStatus(request.getStatus());
            room.setTenantName(request.getTenantName());
            room.setTenantPhone(request.getTenantPhone());
            room.setCountry(request.getCountry());
            room.setDeposit(request.getDeposit());
            room.setMonthlyRent(request.getMonthlyRent());
            room.setAccountNumber(request.getAccountNumber());
            room.setStartDate(request.getStartDate());
            room.setEndDate(request.getEndDate());
            room.setDescription(request.getDescription());
            return roomRepository.save(room);
        });
    }

    @Transactional
    public Optional<Room> updateLeaseFile(Long roomId, String filePath) {
        return roomRepository.findById(roomId).map(room -> {
            room.setLeaseFilePath(filePath);
            return roomRepository.save(room);
        });
    }

    public List<Room> getRoomsByBuildingIds(List<Long> buildingIds) {
        return roomRepository.findByBuildingIdIn(buildingIds);
    }
}
