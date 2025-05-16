package com.evginozan.cargoservice.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "cargos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cargo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String trackingCode;

    @Column(nullable = false)
    private String senderFirstName;

    @Column(nullable = false)
    private String senderLastName;

    @Column(nullable = false)
    private String senderPhone;

    @Column(nullable = false)
    private String receiverFirstName;

    @Column(nullable = false)
    private String receiverLastName;

    @Column(nullable = false)
    private String receiverIdNumber;

    @Column(nullable = false)
    private String receiverPhone;

    @Column(nullable = false)
    private String destinationAddress;

    private BigDecimal weight; // kg
    private Integer width;     // cm
    private Integer height;    // cm
    private Integer length;    // cm

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CargoStatus status;

    private String deliveryCode;

    @ManyToOne
    @JoinColumn(name = "sender_branch_id", nullable = false)
    private Branch senderBranch;

    @ManyToOne
    @JoinColumn(name = "current_branch_id", nullable = false)
    private Branch currentBranch;

    @ManyToOne
    @JoinColumn(name = "destination_branch_id", nullable = false)
    private Branch destinationBranch;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private LocalDateTime deliveredAt;

    @OneToMany(mappedBy = "cargo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CargoHistory> history = new ArrayList<>();

    @PrePersist
    public void prePersist() {
        this.trackingCode = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.status = CargoStatus.AT_SENDER_BRANCH;
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public void generateDeliveryCode() {
        this.deliveryCode = UUID.randomUUID().toString().substring(0, 6).toUpperCase();
    }
}