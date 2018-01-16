package com.ote.common.persistence.model;

import java.util.Set;

public interface IRightDetail extends Comparable<IRightDetail> {

    PerimeterEntity getPerimeter();

    Set<PrivilegeEntity> getPrivileges();

    default int compareTo(IRightDetail o) {
        return this.getPerimeter().compareTo(o.getPerimeter());
    }

}
