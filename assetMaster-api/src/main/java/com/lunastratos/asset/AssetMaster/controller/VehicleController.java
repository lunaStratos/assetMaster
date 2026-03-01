package com.lunastratos.asset.AssetMaster.controller;

import com.lunastratos.asset.AssetMaster.common.ApiResponse;
import com.lunastratos.asset.AssetMaster.dto.VehicleRequest;
import com.lunastratos.asset.AssetMaster.entity.Vehicle;
import com.lunastratos.asset.AssetMaster.service.VehicleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "차량", description = "세입자 차량 등록/수정/삭제")
@RestController
@RequestMapping("/api/vehicles")
public class VehicleController {

    private final VehicleService vehicleService;

    public VehicleController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    @Operation(summary = "방별 차량 목록 조회")
    @GetMapping("/room/{roomId}")
    public ResponseEntity<ApiResponse<List<Vehicle>>> listByRoom(@PathVariable Long roomId) {
        return ResponseEntity.ok(ApiResponse.success(vehicleService.getVehiclesByRoom(roomId)));
    }

    @Operation(summary = "차량 등록")
    @PostMapping("/room/{roomId}")
    public ResponseEntity<ApiResponse<Vehicle>> add(@PathVariable Long roomId,
                                                     @Valid @RequestBody VehicleRequest request) {
        Vehicle vehicle = vehicleService.addVehicle(roomId, request);
        return ResponseEntity.ok(ApiResponse.success("차량이 등록되었습니다.", vehicle));
    }

    @Operation(summary = "차량 정보 수정")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Vehicle>> update(@PathVariable Long id,
                                                        @Valid @RequestBody VehicleRequest request) {
        return vehicleService.updateVehicle(id, request)
                .map(v -> ResponseEntity.ok(ApiResponse.success("차량 정보가 수정되었습니다.", v)))
                .orElse(ResponseEntity.status(404).body(ApiResponse.error(404, "차량을 찾을 수 없습니다.")));
    }

    @Operation(summary = "차량 삭제")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> delete(@PathVariable Long id) {
        if (vehicleService.deleteVehicle(id)) {
            return ResponseEntity.ok(ApiResponse.success("차량이 삭제되었습니다.", null));
        }
        return ResponseEntity.status(404).body(ApiResponse.error(404, "차량을 찾을 수 없습니다."));
    }
}
