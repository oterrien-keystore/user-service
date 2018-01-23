package com.ote.rights.controller;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@EqualsAndHashCode(of = "application")
public class UserRightOrganizedPayload {

    private String application;

    private Set<Perimeter> perimeters;

    @Getter
    @Setter
    @EqualsAndHashCode(of = "code")
    public static class Perimeter {

        private String code;

        private Set<String> privileges;
    }
}

