package com.evginozan.cargoservice.model;

public enum CargoStatus {
    AT_SENDER_BRANCH,      // Gönderici Şubede
    IN_TRANSIT,            // Taşımada
    AT_TRANSFER_CENTER,    // Aktarma Merkezinde
    AT_DELIVERY_BRANCH,    // Teslimat Şubesinde
    OUT_FOR_DELIVERY,      // Dağıtımda
    DELIVERED              // Teslim Edildi
}