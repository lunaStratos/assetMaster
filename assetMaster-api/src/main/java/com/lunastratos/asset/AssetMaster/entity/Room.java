package com.lunastratos.asset.AssetMaster.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "u_am_room")
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "building_id", nullable = false)
    private Long buildingId;

    @Column(name = "floor", nullable = false)
    private Integer floor;

    @Column(name = "room_number", nullable = false, length = 20)
    private String roomNumber;

    // VACANT, OCCUPIED, MAINTENANCE
    @Column(name = "status", nullable = false, length = 20)
    private String status = "VACANT";

    @Column(name = "tenant_name", length = 50)
    private String tenantName;

    @Column(name = "tenant_phone", length = 20)
    private String tenantPhone;

    @Column(name = "country", length = 50)
    private String country;

    @Column(name = "deposit", precision = 15, scale = 0)
    private BigDecimal deposit;

    @Column(name = "monthly_rent", precision = 15, scale = 0)
    private BigDecimal monthlyRent;

    @Column(name = "account_number", length = 50)
    private String accountNumber;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "description", length = 500)
    private String description;

    @Column(name = "lease_file_path", length = 500)
    private String leaseFilePath;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
