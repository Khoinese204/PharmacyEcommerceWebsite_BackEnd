// Observer interface
package com.example.pharmacywebsite.designpattern.Observer;

import com.example.pharmacywebsite.domain.Inventory;

public interface InventoryObserver {
    void update(Inventory inventory);
}
