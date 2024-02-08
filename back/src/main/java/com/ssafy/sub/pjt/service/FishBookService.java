package com.ssafy.sub.pjt.service;

import static com.ssafy.sub.pjt.common.CustomExceptionStatus.NOT_FOUND_FISH;
import static com.ssafy.sub.pjt.dto.FishType.FRESH_WATER;
import static com.ssafy.sub.pjt.dto.FishType.SEA;

import com.ssafy.sub.pjt.domain.FishBook;
import com.ssafy.sub.pjt.domain.repository.FishBookRepository;
import com.ssafy.sub.pjt.dto.*;
import com.ssafy.sub.pjt.exception.BadRequestException;
import com.ssafy.sub.pjt.util.RedisUtil;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FishBookService {
    private final RedisUtil redisUtil;
    private final FishBookRepository fishBookRepository;

    @Transactional(readOnly = true)
    public FishBookListResponse getFishBooksByPage(final Pageable pageable) {

        if (redisUtil.getFishBook("fishBook") != null) {
            return (FishBookListResponse) redisUtil.getFishBook("fishBook");
        }

        final Slice<FishBook> fishBooks =
                fishBookRepository.findSliceBy(pageable.previousOrFirst());
        final List<FishBookResponse> fishBookResponse =
                fishBooks.stream()
                        .map(fishBook -> FishBookResponse.of(fishBook))
                        .collect(Collectors.toList());
        redisUtil.setFishBook(
                "fishBook", new FishBookListResponse(fishBookResponse, fishBooks.hasNext()));

        return new FishBookListResponse(fishBookResponse, fishBooks.hasNext());
    }

    public FishBookDetailResponse searchById(Integer fishBookId) {
        final FishBook fishBook = findByFishBookById(fishBookId);
        return FishBookDetailResponse.of(fishBook);
    }

    private FishBook findByFishBookById(Integer id) {
        return fishBookRepository
                .findById(id)
                .orElseThrow(() -> new BadRequestException(NOT_FOUND_FISH));
    }

    public FishBookFilterListResponse getFIshBooksByFishType() {
        List<FishBook> seaFishResponse = fishBookRepository.findByFishType(SEA);
        List<FishBook> freshWaterFishResponse = fishBookRepository.findByFishType(FRESH_WATER);

        final List<FishBookFilterResponse> seaFishListResponse =
                seaFishResponse.stream()
                        .map(seaFishes -> FishBookFilterResponse.of(seaFishes))
                        .collect(Collectors.toList());

        final List<FishBookFilterResponse> freshWaterFishListResponse =
                seaFishResponse.stream()
                        .map(freshFishes -> FishBookFilterResponse.of(freshFishes))
                        .collect(Collectors.toList());

        return new FishBookFilterListResponse(seaFishListResponse, freshWaterFishListResponse);
    }

    public List<FishBookAutoCompleteResponse> findAutoCompleteName(String searchWord) {
        List<FishBook> fishBooks =
                fishBookRepository.findTop3ByNameStartsWithOrderByNameAsc(searchWord);

        final List<FishBookAutoCompleteResponse> fishBookAutoCompleteResponse =
                fishBooks.stream()
                        .map(fishBook -> FishBookAutoCompleteResponse.of(fishBook))
                        .collect(Collectors.toList());
        return fishBookAutoCompleteResponse;
    }
}
