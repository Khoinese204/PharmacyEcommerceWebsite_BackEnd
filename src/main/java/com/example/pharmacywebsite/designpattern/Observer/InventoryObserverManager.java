// InventoryObserverManager.java
package com.example.pharmacywebsite.designpattern.Observer;

import com.example.pharmacywebsite.domain.Inventory;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InventoryObserverManager {
    private final LowStockNotifier lowStockNotifier;
    private final AutoRestockObserver autoRestockObserver;
    private final InventoryDashboardUpdater inventoryDashboardUpdater;
    private final InventoryLoggerObserver inventoryLoggerObserver;

    private final InventorySubject subject = new InventorySubject();

    @PostConstruct
    public void initObservers() {
        subject.addObserver(lowStockNotifier);
        subject.addObserver(autoRestockObserver);
        subject.addObserver(inventoryDashboardUpdater);
        subject.addObserver(inventoryLoggerObserver);
    }

    public void notifyAll(Inventory inventory) {
        subject.notifyObservers(inventory);
    }
}
