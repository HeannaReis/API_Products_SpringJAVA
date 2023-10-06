package me.dio.controller.dto;

public class DepartmentDto {
    private Long id;
    private String name;

    public DepartmentDto() {
    }

    public DepartmentDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    // Getters e setters
}
