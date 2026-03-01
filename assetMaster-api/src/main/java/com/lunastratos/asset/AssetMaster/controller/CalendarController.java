package com.lunastratos.asset.AssetMaster.controller;

import com.lunastratos.asset.AssetMaster.common.ApiResponse;
import com.lunastratos.asset.AssetMaster.entity.Room;
import com.lunastratos.asset.AssetMaster.service.RoomService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "달력", description = "달력용 방/계약 데이터 조회")
@RestController
@RequestMapping("/api/calendar")
public class CalendarController {

    private final RoomService roomService;

    public CalendarController(RoomService roomService) {
        this.roomService = roomService;
    }

    @Operation(summary = "달력용 방 목록 조회", description = "건물별 계약 시작/종료일, 월세 정보 포함")
    @GetMapping("/building/{buildingId}")
    public ResponseEntity<ApiResponse<List<Room>>> getRoomsForCalendar(@PathVariable Long buildingId) {
        List<Room> rooms = roomService.getRoomsByBuilding(buildingId);
        return ResponseEntity.ok(ApiResponse.success(rooms));
    }
}
