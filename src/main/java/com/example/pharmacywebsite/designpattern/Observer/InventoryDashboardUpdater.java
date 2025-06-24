// Concrete observer - Updates dashboard metrics
package com.example.pharmacywebsite.designpattern.Observer;

import com.example.pharmacywebsite.domain.Inventory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class InventoryDashboardUpdater implements InventoryObserver {
    private static final Logger logger = LoggerFactory.getLogger(InventoryDashboardUpdater.class);

    @Override
    public void update(Inventory inventory) {
        logger.info("[Dashboard] Update data storage dashboard for medicine '{}'.",
                inventory.getMedicine().getName());
    }
}
