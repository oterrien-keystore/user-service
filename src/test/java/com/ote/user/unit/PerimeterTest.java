package com.ote.user.unit;

import com.ote.common.persistence.model.PerimeterEntity;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class PerimeterTest {

    @Test
    public void testCode(){
        PerimeterEntity root = new PerimeterEntity();
        root.setCode("ROOT");

        PerimeterEntity parent = new PerimeterEntity();
        parent.setCode("PARENT");
        parent.setParent(root);

        PerimeterEntity child = new PerimeterEntity();
        child.setCode("CHILD");
        child.setParent(parent);

        Assertions.assertThat(child.getCode()).isEqualTo("ROOT/PARENT/CHILD");
    }
}
