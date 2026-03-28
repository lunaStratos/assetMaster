package com.lunastratos.asset.AssetMaster.service;

import com.lunastratos.asset.AssetMaster.entity.Room;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Service
public class RoomExcelService {

    private static final String[] HEADERS = {
            "층", "호수", "상태", "세입자명", "전화번호", "국가",
            "보증금", "월세", "계좌번호", "계약시작일", "계약종료일", "설명"
    };

    public byte[] exportRooms(List<Room> rooms) throws IOException {
        try (XSSFWorkbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            Sheet sheet = workbook.createSheet("방 현황");

            // 헤더 스타일
            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);
            headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            headerStyle.setBorderBottom(BorderStyle.THIN);

            // 헤더 행
            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < HEADERS.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(HEADERS[i]);
                cell.setCellStyle(headerStyle);
            }

            // 데이터 행
            CellStyle numberStyle = workbook.createCellStyle();
            numberStyle.setDataFormat(workbook.createDataFormat().getFormat("#,##0"));

            for (int i = 0; i < rooms.size(); i++) {
                Room room = rooms.get(i);
                Row row = sheet.createRow(i + 1);

                row.createCell(0).setCellValue(room.getFloor());
                row.createCell(1).setCellValue(room.getRoomNumber());
                row.createCell(2).setCellValue(statusLabel(room.getStatus()));
                row.createCell(3).setCellValue(nullSafe(room.getTenantName()));
                row.createCell(4).setCellValue(nullSafe(room.getTenantPhone()));
                row.createCell(5).setCellValue(nullSafe(room.getCountry()));

                Cell depositCell = row.createCell(6);
                if (room.getDeposit() != null) {
                    depositCell.setCellValue(room.getDeposit().doubleValue());
                    depositCell.setCellStyle(numberStyle);
                }

                Cell rentCell = row.createCell(7);
                if (room.getMonthlyRent() != null) {
                    rentCell.setCellValue(room.getMonthlyRent().doubleValue());
                    rentCell.setCellStyle(numberStyle);
                }

                row.createCell(8).setCellValue(nullSafe(room.getAccountNumber()));
                row.createCell(9).setCellValue(room.getStartDate() != null ? room.getStartDate().toString() : "");
                row.createCell(10).setCellValue(room.getEndDate() != null ? room.getEndDate().toString() : "");
                row.createCell(11).setCellValue(nullSafe(room.getDescription()));
            }

            // 열 너비 설정 (256 = 1 character width)
            int[] minWidths = {
                    8,   // 층
                    10,  // 호수
                    10,  // 상태
                    14,  // 세입자명
                    16,  // 전화번호
                    10,  // 국가
                    16,  // 보증금
                    16,  // 월세
                    20,  // 계좌번호
                    14,  // 계약시작일
                    14,  // 계약종료일
                    30,  // 설명
            };
            for (int i = 0; i < HEADERS.length; i++) {
                sheet.setColumnWidth(i, minWidths[i] * 256);
            }

            workbook.write(out);
            return out.toByteArray();
        }
    }

    private String statusLabel(String status) {
        if (status == null) return "";
        return switch (status) {
            case "OCCUPIED" -> "점유";
            case "MAINTENANCE" -> "수리중";
            default -> "공실";
        };
    }

    private String nullSafe(String value) {
        return value != null ? value : "";
    }
}
