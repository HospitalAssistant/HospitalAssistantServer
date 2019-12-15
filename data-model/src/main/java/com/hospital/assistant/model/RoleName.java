package com.hospital.assistant.model;

public enum RoleName {
    ROLE_SANITARY("Санитарка", "Sanitary"),
    ROLE_NURSE("Сестра", "Nurse"),
    ROLE_DOCTOR("Доктор", "Doctor"),
    ROLE_PATIENT("Пациент", "Patient"),
    ROLE_ADMIN("Админ", "Admin");

    private String roleBg;

    private String roleEn;

    RoleName(String roleBg, String roleEn) {
        this.roleBg = roleBg;
        this.roleEn = roleEn;
    }

    public static RoleName from(String role) {
        for (RoleName r : RoleName.values()) {
            if (r.roleBg.equalsIgnoreCase(role)
                    || r.roleEn.equalsIgnoreCase(role)) {
                return r;
            }
        }

        throw new IllegalArgumentException("Role '" + role + "' doesn't exist");
    }

    public String getRoleEn () {
        return roleEn;
    }

    @Override
    public String toString() {
        return roleBg;
    }
}
