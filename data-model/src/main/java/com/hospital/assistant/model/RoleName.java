package com.hospital.assistant.model;

public enum RoleName {
    ROLE_SANITARY("Санитарка"),
    ROLE_NURSE("Сестра"),
    ROLE_DOCTOR("Доктор"),
    ROLE_PATIENT("Пациент"),
    ROLE_ADMIN("Админ");

    private String role;

    RoleName(String role) {
        this.role = role;
    }

    public static RoleName from(String role) {
        for (RoleName r : RoleName.values()) {
            if (r.role.equalsIgnoreCase(role)) {
                return r;
            }
        }

        throw new IllegalArgumentException("Role '" + role + "' doesn't exist");
    }

    @Override
    public String toString() {
        return role;
    }
}
