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
        logger.info("[Inventory Log] Inventory of medicine '{}' has been updated. New quantity: {}",
                inventory.getMedicine().getName(), inventory.getQuantity());
    }
}