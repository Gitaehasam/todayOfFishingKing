package com.ssafy.sub.pjt.service;

import static com.ssafy.sub.pjt.common.CustomExceptionStatus.*;
import static com.ssafy.sub.pjt.util.AuthenticationUtil.getCurrentUserSocialId;

import com.ssafy.sub.pjt.domain.*;
import com.ssafy.sub.pjt.domain.repository.*;
import com.ssafy.sub.pjt.dto.*;
import com.ssafy.sub.pjt.exception.AuthException;
import com.ssafy.sub.pjt.exception.BadRequestException;
import com.ssafy.sub.pjt.util.RedisUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final BoardQueryRepository boardQueryRepository;
    private final HashTagService hashTagService;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final FishBookRepository fishBookRepository;
    private final FishingSpotRepository fishingSpotRepository;
    private final ApplicationEventPublisher publisher;
    private final RedisUtil redisUtil;

    public Integer write(BoardRequest boardRequest, String socialId) {
        if (boardRequest.getCategoryId() == 1 && boardRequest.getFishBookId() == null) {
            throw new BadRequestException(REQUIRED_FISHBOOKID);
        }

        Board savedBoard = boardRepository.save(createBoard(boardRequest, socialId));

        final String key = "boards:*";
        redisUtil.deleteDataList(redisUtil.getKeys(key));

        return savedBoard.getId();
    }

    private Board createBoard(BoardRequest boardRequest, String socialId) {
        final List<HashTag> hashTags = getHashTagList(boardRequest.getHashTags());

        final User user = findUserBySocialId(socialId);

        final Category category =
                categoryRepository
                        .findById(boardRequest.getCategoryId())
                        .orElseThrow(() -> new BadRequestException(NOT_FOUND_CATEGORY));

        final FishBook fishBook = findByFishBookId(boardRequest.getFishBookId());
        final FishingSpot fishingSpot = findByFishingSpotId(boardRequest.getFishingSpotId());
        final Board board = Board.of(user, category, fishBook, fishingSpot, boardRequest);
        board.addHashTags(hashTags);

        return board;
    }

    private User findUserBySocialId(String socialId) {
        return userRepository
                .findBySocialId(socialId)
                .orElseThrow(() -> new AuthException(NOT_FOUND_MEMBER_ID));
    }

    public BoardDetailResponse searchById(Integer boardId) {

        final String key = "boards:" + boardId;

        if (redisUtil.getObject(key) != null) {
            return (BoardDetailResponse) redisUtil.getObject(key);
        }

        final Board board = findBoardById(boardId);
        final BoardDetailResponse boardDetailResponse =
                BoardDetailResponse.of(board, getCurrentUserSocialId());

        redisUtil.setObject(key, boardDetailResponse);

        return boardDetailResponse;
    }

    @Transactional(readOnly = true)
    public BoardListResponse getBoardsByPage(
            final Pageable pageable,
            final String sortType,
            final Integer fishBookId,
            final Integer hashTagId,
            final Integer categoryId,
            final String socialId) {

        final String key =
                "boards:"
                        + pageable.getPageNumber()
                        + "c:"
                        + categoryId
                        + "s:"
                        + sortType
                        + "f:"
                        + fishBookId
                        + "h:"
                        + hashTagId;

        if (redisUtil.getObject(key) != null) {
            return (BoardListResponse) redisUtil.getObject(key);
        }

        final Slice<BoardData> boardData =
                boardQueryRepository.searchBy(
                        BoardSearchCondition.builder()
                                .fishBookId(fishBookId)
                                .hashTagId(hashTagId)
                                .sortType(sortType)
                                .categoryId(categoryId)
                                .build(),
                        pageable,
                        socialId);

        final List<BoardResponse> boardResponses =
                boardData.stream()
                        .map(
                                board ->
                                        BoardResponse.of(
                                                board, board.getCommentCnt(), board.getLikeCnt()))
                        .collect(Collectors.toList());

        BoardListResponse boardListResponse =
                new BoardListResponse(boardResponses, boardData.hasNext());

        redisUtil.setObject(key, new BoardListResponse(boardResponses, boardData.hasNext()));

        return boardListResponse;
    }

    public void validateBoardByUser(final String socialId, final Integer boardId) {
        if (!boardRepository.existsByUserSocialIdAndId(socialId, boardId)) {
            throw new AuthException(INVALID_BOARD_WITH_USER);
        }
    }

    @Transactional
    public void update(final Integer boardId, final BoardUpdateRequest boardUpdateRequest) {
        if (boardUpdateRequest.getCategoryId() == 1 && boardUpdateRequest.getFishBookId() == null) {
            throw new BadRequestException(REQUIRED_FISHBOOKID);
        }

        final Board board =
                boardRepository
                        .findById(boardId)
                        .orElseThrow(() -> new BadRequestException(NOT_FOUND_BOARD_ID));

        final Category category =
                categoryRepository
                        .findById(boardUpdateRequest.getCategoryId())
                        .orElseThrow(() -> new BadRequestException(NOT_FOUND_CATEGORY));

        if (!category.getId().equals(boardUpdateRequest.getCategoryId())) {
            throw new BadRequestException(NOT_EQUAL_CATEGORY);
        }

        final FishBook fishBook = findByFishBookId(boardUpdateRequest.getFishBookId());

        final List<HashTag> hashTags = getHashTagList(boardUpdateRequest.getHashTags());

        final FishingSpot fishingSpot = findByFishingSpotId(boardUpdateRequest.getFishingSpotId());

        board.updateHashTags(hashTags);
        updateImage(board.getImage().getUrl(), boardUpdateRequest.getImageUrl());
        board.update(boardUpdateRequest, category, fishBook, fishingSpot);

        final String key = "boards:*";
        redisUtil.deleteDataList(redisUtil.getKeys(key));
    }

    private FishBook findByFishBookId(final Integer fishBookId) {
        if (fishBookId == null) {
            return null;
        }

        final FishBook fishBook =
                fishBookRepository
                        .findById(fishBookId)
                        .orElseThrow(() -> new BadRequestException(NOT_FOUND_FISH));

        return fishBook;
    }

    private FishingSpot findByFishingSpotId(final Integer fishingSpotId) {
        if (fishingSpotId == null) {
            return null;
        }

        final FishingSpot fishingSpot =
                fishingSpotRepository
                        .findById(fishingSpotId)
                        .orElseThrow(() -> new BadRequestException(NOT_FOUND_FISHING_SPOT));

        return fishingSpot;
    }

    private List<HashTag> getHashTagList(List<String> hashTagsRequest) {
        if (hashTagsRequest == null) {
            return new ArrayList<>();
        }

        final List<HashTag> hashTags =
                hashTagService.findOrCreateHashTags(new HashTagsRequest(hashTagsRequest));

        return hashTags;
    }

    private void updateImage(final String originalImageName, final String updateImageName) {
        if (originalImageName.equals(updateImageName)) {
            return;
        }
        publisher.publishEvent(new S3ImageEvent(originalImageName));
    }

    @Transactional
    public void delete(String socialId, Integer boardId) {
        User user = findUserBySocialId(socialId);
        Board board = findBoardById(boardId);

        user.delete(board);
        boardRepository.delete(board);

        final String key = "boards:*";
        redisUtil.deleteDataList(redisUtil.getKeys(key));
    }

    @Transactional
    public LikeResponse like(String socialId, Integer boardId) {
        User source = findUserBySocialId(socialId);
        Board target = findBoardById(boardId);

        target.like(source);

        final String key = "boards:*";
        redisUtil.deleteDataList(redisUtil.getKeys(key));

        return LikeResponse.of(target.getLikeCounts(), true);
    }

    @Transactional
    public LikeResponse unlike(String socialId, Integer boardId) {
        User source = findUserBySocialId(socialId);
        Board target = findBoardById(boardId);

        target.unlike(source);

        final String key = "boards:*";
        redisUtil.deleteDataList(redisUtil.getKeys(key));

        return LikeResponse.of(target.getLikeCounts(), false);
    }

    private Board findBoardById(Integer id) {
        return boardRepository
                .findById(id)
                .orElseThrow(() -> new BadRequestException(NOT_FOUND_BOARD_ID));
    }

    public MyBoardListResponse getMyBoards(final String socialId, final Integer categoryId) {
        List<Board> boards = boardRepository.findByUserSocialIdAndCategoryId(socialId, categoryId);
        return new MyBoardListResponse(
                boards.stream()
                        .map(board -> MyBoardResponse.of(board))
                        .collect(Collectors.toList()));
    }
}
