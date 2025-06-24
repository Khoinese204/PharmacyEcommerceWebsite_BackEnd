// Concrete observer - Notifies when low stock
package com.example.pharmacywebsite.designpattern.Observer;

import com.example.pharmacywebsite.domain.Inventory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class LowStockNotifier implements InventoryObserver {
    private static final Logger logger = LoggerFactory.getLogger(LowStockNotifier.class);
    private static final int LOW_STOCK_THRESHOLD = 10;

    @Override
    public void update(Inventory inventory) {
        if (inventory.getQuantity() < LOW_STOCK_THRESHOLD) {
            logger.warn("[Warning] Medicine '{}' is nearly out of stock. Remaining: {}",
                    inventory.getMedicine().getName(), inventory.getQuantity());
        }
    }
}
