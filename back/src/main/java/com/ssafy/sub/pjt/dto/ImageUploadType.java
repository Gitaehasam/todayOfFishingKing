package com.ssafy.sub.pjt.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ImageUploadType {
    BOARD("board"),
    PROFILE("profile");

    private final String type;
}
