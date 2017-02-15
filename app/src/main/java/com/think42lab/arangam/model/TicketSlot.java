package com.think42lab.arangam.model;

import java.io.Serializable;

/**
 * Created by think42lab on 19/12/16.
 */

public class TicketSlot implements Serializable{
    private int slotValue;
    private boolean isSelected;

    public int getSlotValue() {
        return slotValue;
    }

    public void setSlotValue(int slotValue) {
        this.slotValue = slotValue;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
