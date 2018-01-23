package com.ote.rights.controller;

import lombok.Data;

import java.util.List;

@Data
public class UserRightPayloadSplit {

    private List<UserRightOrganizedPayload> userRights;

    private List<UserRightInheritedPayload> securityGroupRights;

    @Data
    public static class UserRightInheritedPayload {

        private String securityGroup;

        private List<UserRightOrganizedPayload> userRights;
    }
}