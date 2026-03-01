package com.lunastratos.asset.AssetMaster.repository;

import com.lunastratos.asset.AssetMaster.entity.Building;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BuildingRepository extends JpaRepository<Building, Long> {
    List<Building> findByMemberIdOrderByCreatedAtDesc(Long memberId);
    Optional<Building> findByIdAndMemberId(Long id, Long memberId);
}
