package com.lunastratos.asset.AssetMaster.controller;

import com.lunastratos.asset.AssetMaster.common.ApiResponse;
import com.lunastratos.asset.AssetMaster.dto.RoomUpdateRequest;
import com.lunastratos.asset.AssetMaster.entity.Room;
import com.lunastratos.asset.AssetMaster.service.RoomExcelService;
import com.lunastratos.asset.AssetMaster.service.RoomService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Tag(name = "방/호실", description = "방 조회, 수정, 계약서 파일 업로드")
@RestController
@RequestMapping("/api/rooms")
public class RoomController {

    private final RoomService roomService;
    private final RoomExcelService roomExcelService;

    @Value("${file.upload-dir}")
    private String uploadDir;

    public RoomController(RoomService roomService, RoomExcelService roomExcelService) {
        this.roomService = roomService;
        this.roomExcelService = roomExcelService;
    }

    @Operation(summary = "건물별 방 목록 조회")
    @GetMapping("/building/{buildingId}")
    public ResponseEntity<ApiResponse<List<Room>>> listByBuilding(@PathVariable Long buildingId) {
        return ResponseEntity.ok(ApiResponse.success(roomService.getRoomsByBuilding(buildingId)));
    }

    @Operation(summary = "방 상세 조회")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Room>> detail(@PathVariable Long id) {
        return roomService.getRoom(id)
                .map(r -> ResponseEntity.ok(ApiResponse.success(r)))
                .orElse(ResponseEntity.status(404).body(ApiResponse.error(404, "방을 찾을 수 없습니다.")));
    }

    @Operation(summary = "방 정보 수정")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Room>> update(@PathVariable Long id,
                                                    @RequestBody RoomUpdateRequest request) {
        return roomService.updateRoom(id, request)
                .map(r -> ResponseEntity.ok(ApiResponse.success("방 정보가 수정되었습니다.", r)))
                .orElse(ResponseEntity.status(404).body(ApiResponse.error(404, "방을 찾을 수 없습니다.")));
    }

    @Operation(summary = "건물별 방 현황 엑셀 다운로드")
    @GetMapping("/building/{buildingId}/excel")
    public ResponseEntity<byte[]> downloadExcel(@PathVariable Long buildingId) throws IOException {
        List<Room> rooms = roomService.getRoomsByBuilding(buildingId);
        byte[] excelData = roomExcelService.exportRooms(rooms);

        String fileName = URLEncoder.encode("방현황.xlsx", StandardCharsets.UTF_8);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename*=UTF-8''" + fileName)
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(excelData);
    }

    @Operation(summary = "계약서 파일 업로드", description = "PDF/이미지 파일 업로드 (최대 20MB)")
    @PostMapping("/{id}/lease-file")
    public ResponseEntity<ApiResponse<Room>> uploadLeaseFile(@PathVariable Long id,
                                                             @RequestParam("file") MultipartFile file) throws IOException {
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        String originalName = file.getOriginalFilename();
        String ext = (originalName != null && originalName.contains("."))
                ? originalName.substring(originalName.lastIndexOf("."))
                : "";
        String storedName = UUID.randomUUID() + ext;
        Path filePath = uploadPath.resolve(storedName);
        file.transferTo(filePath.toFile());

        return roomService.updateLeaseFile(id, storedName)
                .map(r -> ResponseEntity.ok(ApiResponse.success("파일이 업로드되었습니다.", r)))
                .orElse(ResponseEntity.status(404).body(ApiResponse.error(404, "방을 찾을 수 없습니다.")));
    }
}
