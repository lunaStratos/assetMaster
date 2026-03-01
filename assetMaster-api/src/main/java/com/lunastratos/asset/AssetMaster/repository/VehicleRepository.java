package com.lunastratos.asset.AssetMaster.repository;

import com.lunastratos.asset.AssetMaster.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
    List<Vehicle> findByRoomIdOrderByCreatedAtAsc(Long roomId);
    List<Vehicle> findByRoomIdIn(List<Long> roomIds);
    void deleteByRoomId(Long roomId);
}
