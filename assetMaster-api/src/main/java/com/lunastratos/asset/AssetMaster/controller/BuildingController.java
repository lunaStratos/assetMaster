package com.lunastratos.asset.AssetMaster.controller;

import com.lunastratos.asset.AssetMaster.common.ApiResponse;
import com.lunastratos.asset.AssetMaster.dto.BuildingRequest;
import com.lunastratos.asset.AssetMaster.entity.Building;
import com.lunastratos.asset.AssetMaster.service.BuildingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "건물", description = "건물 CRUD")
@RestController
@RequestMapping("/api/buildings")
public class BuildingController {

    private final BuildingService buildingService;

    public BuildingController(BuildingService buildingService) {
        this.buildingService = buildingService;
    }

    @Operation(summary = "건물 생성", description = "층/호실 구조와 함께 건물 생성")
    @PostMapping
    public ResponseEntity<ApiResponse<Building>> create(Authentication auth,
                                                        @Valid @RequestBody BuildingRequest request) {
        Long memberId = (Long) auth.getPrincipal();
        Building building = buildingService.createBuilding(memberId, request);
        return ResponseEntity.ok(ApiResponse.success("건물이 생성되었습니다.", building));
    }

    @Operation(summary = "건물 목록 조회")
    @GetMapping
    public ResponseEntity<ApiResponse<List<Building>>> list(Authentication auth) {
        Long memberId = (Long) auth.getPrincipal();
        return ResponseEntity.ok(ApiResponse.success(buildingService.getBuildings(memberId)));
    }

    @Operation(summary = "건물 상세 조회")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Building>> detail(Authentication auth, @PathVariable Long id) {
        Long memberId = (Long) auth.getPrincipal();
        return buildingService.getBuilding(id, memberId)
                .map(b -> ResponseEntity.ok(ApiResponse.success(b)))
                .orElse(ResponseEntity.status(404).body(ApiResponse.error(404, "건물을 찾을 수 없습니다.")));
    }

    @Operation(summary = "건물 수정")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Building>> update(Authentication auth,
                                                        @PathVariable Long id,
                                                        @Valid @RequestBody BuildingRequest request) {
        Long memberId = (Long) auth.getPrincipal();
        return buildingService.updateBuilding(id, memberId, request)
                .map(b -> ResponseEntity.ok(ApiResponse.success("건물이 수정되었습니다.", b)))
                .orElse(ResponseEntity.status(404).body(ApiResponse.error(404, "건물을 찾을 수 없습니다.")));
    }

    @Operation(summary = "건물 삭제", description = "건물 및 소속 방 모두 삭제")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> delete(Authentication auth, @PathVariable Long id) {
        Long memberId = (Long) auth.getPrincipal();
        if (buildingService.deleteBuilding(id, memberId)) {
            return ResponseEntity.ok(ApiResponse.success("건물이 삭제되었습니다.", null));
        }
        return ResponseEntity.status(404).body(ApiResponse.error(404, "건물을 찾을 수 없습니다."));
    }
}
