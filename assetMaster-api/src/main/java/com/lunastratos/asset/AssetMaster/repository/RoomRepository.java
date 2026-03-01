package com.lunastratos.asset.AssetMaster.repository;

import com.lunastratos.asset.AssetMaster.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface RoomRepository extends JpaRepository<Room, Long> {
    List<Room> findByBuildingIdOrderByFloorAscRoomNumberAsc(Long buildingId);
    List<Room> findByBuildingIdAndFloor(Long buildingId, Integer floor);
    List<Room> findByBuildingIdIn(List<Long> buildingIds);
    List<Room> findByBuildingIdInAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
            List<Long> buildingIds, LocalDate date1, LocalDate date2);
    void deleteByBuildingId(Long buildingId);
}
