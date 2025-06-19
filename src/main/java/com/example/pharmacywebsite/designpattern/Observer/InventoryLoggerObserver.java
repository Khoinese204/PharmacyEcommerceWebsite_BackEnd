// Concrete observer - Logs inventory change
package com.example.pharmacywebsite.designpattern.Observer;

import com.example.pharmacywebsite.domain.Inventory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class InventoryLoggerObserver implements InventoryObserver {
    private static final Logger logger = LoggerFactory.getLogger(InventoryLoggerObserver.class);

    @Override
    public void update(Inventory inventory) {
        logger.info("[Inventory Log] Tồn kho thuốc '{}' đã thay đổi. Số lượng mới: {}",
                inventory.getMedicine().getName(), inventory.getQuantity());
    }
}