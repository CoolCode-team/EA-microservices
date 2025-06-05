package com.ea.academic_sapaces.entity;

public enum SpaceStatus {
    AVAILABLE("AVAILABLE"),
    UNAVAILABLE("UNAVAILABLE");

    private  final String status;


    SpaceStatus(String status) {
        this.status = status;
    }

}
