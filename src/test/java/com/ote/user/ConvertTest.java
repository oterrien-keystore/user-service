package com.ote.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ote.user.persistence.model.*;
import com.ote.user.rights.api.Perimeter;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;

public class ConvertTest {

    @Test
    public void testConvert() {

        ApplicationEntity application = new ApplicationEntity();
        application.setCode("SLA");

        UserEntity user = new UserEntity();
        user.setLogin("olivier.terrien");

        UserRightEntity userRightEntity = new UserRightEntity();
        userRightEntity.setApplication(application);
        userRightEntity.setUser(user);
        userRightEntity.setPerimeters(new ArrayList<>());

        PerimeterEntity dealPerimeter = new PerimeterEntity();
        dealPerimeter.setCode("DEAL");
        dealPerimeter.setPerimeters(new ArrayList<>());

        PerimeterEntity glePerimeter = new PerimeterEntity();
        glePerimeter.setCode("GLE");
        glePerimeter.setPerimeters(new ArrayList<>());

        PrivilegeEntity readOnlyPrivilege = new PrivilegeEntity();
        readOnlyPrivilege.setCode("READ_ONLY");

        PrivilegeEntity readWritePrivilege = new PrivilegeEntity();
        readWritePrivilege.setCode("READ_WRITE");

        dealPerimeter.getPrivileges().add(readOnlyPrivilege);
        dealPerimeter.getPerimeters().add(glePerimeter);
        glePerimeter.getPrivileges().add(readWritePrivilege);

        userRightEntity.getPerimeters().add(dealPerimeter);

        List<Perimeter> perimeters = new ArrayList<>();
        perimeters.addAll(userRightEntity.getPerimeters().stream().map(p -> convert(p)).collect(Collectors.toList()));

        System.out.println(userRightEntity.getPerimeters());
        System.out.println(perimeters);

    }

    private Perimeter convert(PerimeterEntity entity) {
        Perimeter perimeter = new Perimeter(entity.getCode());
        perimeter.getPrivileges().addAll(entity.getPrivileges().stream().map(p -> p.getCode()).collect(Collectors.toList()));
        perimeter.getPerimeters().addAll(entity.getPerimeters().stream().map(p -> convert(p)).collect(Collectors.toList()));
        return perimeter;
    }
}
