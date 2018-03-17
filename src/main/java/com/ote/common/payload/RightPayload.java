package com.ote.common.payload;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
public class RightPayload {

    @NonNull
    public String application;

    @NonNull
    private String perimeter;

    @NonNull
    private String privilege;
}
