package com.flc.domain;

public record Member(String id, String name) {
    public Member {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("Member id must not be blank.");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Member name must not be blank.");
        }
    }
}
