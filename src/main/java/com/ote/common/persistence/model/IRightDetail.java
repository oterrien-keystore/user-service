package com.ote.common.persistence.model;

import java.util.Set;

public interface IRightDetail {

    PerimeterEntity getPerimeter();

    Set<PrivilegeEntity> getPrivileges();
}
