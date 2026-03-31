package com.flc.domain;

public enum BookingStatus {
    BOOKED,
    CHANGED,
    ATTENDED,
    CANCELLED;

    public boolean countsTowardsCapacity() {
        return this != CANCELLED;
    }

    public boolean canBeUpdated() {
        return this == BOOKED || this == CHANGED;
    }
}
