package com.ote.rights.controller;

import lombok.Data;

@Data
public class UserRightPayload {

    private String application;

    private String perimeter;

    private String privilege;
}
