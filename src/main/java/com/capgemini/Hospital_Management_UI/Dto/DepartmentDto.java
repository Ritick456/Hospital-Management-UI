package com.capgemini.Hospital_Management_UI.dto;


public class DepartmentDto {
    private Integer departmentId;
    private String name;
    private PhysicianDepartmentDto physicianDetail;

    public Integer getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Integer departmentId) {
        this.departmentId = departmentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PhysicianDepartmentDto getPhysicianDetail() {
        return physicianDetail;
    }

    public void setPhysicianDetail(PhysicianDepartmentDto physicianDetail) {
        this.physicianDetail = physicianDetail;
    }
}





