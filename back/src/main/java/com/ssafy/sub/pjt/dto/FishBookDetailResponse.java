package com.ssafy.sub.pjt.dto;

import com.ssafy.sub.pjt.domain.FishBook;
import java.io.Serializable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class FishBookDetailResponse implements Serializable {

    private final Integer fishBookId;
    private final String name;
    private final String scientificName;
    private final FishType fishType;
    private final String size;
    private final String habitat;
    private final String bait;
    private final String interview;
    private final String imageUrl;

    public static FishBookDetailResponse of(FishBook fishBook) {
        return new FishBookDetailResponse(
                fishBook.getId(),
                fishBook.getName(),
                fishBook.getScientificName(),
                fishBook.getFishType(),
                fishBook.getSize(),
                fishBook.getHabitat(),
                fishBook.getBait(),
                fishBook.getInterview(),
                fishBook.getImageUrl());
    }
}
