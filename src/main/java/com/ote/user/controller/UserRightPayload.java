package com.ote.user.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserRightPayload {

    private String user;

    private String application;

    private String perimeter;

    private String privilege;

    private boolean isGranted;

}
