// Subject class
package com.example.pharmacywebsite.designpattern.Observer;

import com.example.pharmacywebsite.domain.Inventory;
import java.util.ArrayList;
import java.util.List;

public class InventorySubject {
    private final List<InventoryObserver> observers = new ArrayList<>();

    public void addObserver(InventoryObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(InventoryObserver observer) {
        observers.remove(observer);
    }

    public void notifyObservers(Inventory inventory) {
        for (InventoryObserver observer : observers) {
            observer.update(inventory);
        }
    }
}