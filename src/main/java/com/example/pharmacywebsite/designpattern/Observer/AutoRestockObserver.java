// Concrete observer - Auto restocks if stock is too low
package com.example.pharmacywebsite.designpattern.Observer;

import com.example.pharmacywebsite.domain.Inventory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class AutoRestockObserver implements InventoryObserver {
    private static final Logger logger = LoggerFactory.getLogger(AutoRestockObserver.class);

    @Override
    public void update(Inventory inventory) {
        if (inventory.getQuantity() == 0) {
            logger.info("Medicine is low stock. Need import.", inventory.getMedicine().getName());
        }
    }
}