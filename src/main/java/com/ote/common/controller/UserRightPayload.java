package com.ote.common.controller;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class UserRightPayload {

    private String user;

    private String application;

    private List<Detail> details;

    @NoArgsConstructor
    public static class Detail {
        private String perimeter;
        private List<String> privileges;
    }
}
