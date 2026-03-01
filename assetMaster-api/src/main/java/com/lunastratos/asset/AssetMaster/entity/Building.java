package com.lunastratos.asset.AssetMaster.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "u_am_building")
public class Building {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "member_id", nullable = false)
    private Long memberId;

    @Column(name = "building_name", nullable = false, length = 100)
    private String buildingName;

    @Column(name = "address", length = 300)
    private String address;

    @Column(name = "admin_name", length = 50)
    private String adminName;

    @Column(name = "admin_phone", length = 20)
    private String adminPhone;

    @Column(name = "description", length = 500)
    private String description;

    @Column(name = "phone", length = 20)
    private String phone;

    @Column(name = "parking_available", nullable = false, columnDefinition = "TINYINT(1) DEFAULT 0")
    private Boolean parkingAvailable = false;

    @Column(name = "structure", columnDefinition = "JSON")
    private String structure;

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
