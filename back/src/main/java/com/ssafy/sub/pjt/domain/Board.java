package com.ssafy.sub.pjt.domain;

import static com.ssafy.sub.pjt.common.CustomExceptionStatus.CANNOT_ADD_HASHTAG_EXCEPTION;

import com.ssafy.sub.pjt.dto.BoardRequest;
import com.ssafy.sub.pjt.dto.BoardUpdateRequest;
import com.ssafy.sub.pjt.exception.BadRequestException;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;
import lombok.*;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

@Entity
@Builder
@Getter
@AllArgsConstructor
@TypeDef(name = "json", typeClass = JsonType.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Board extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    private Integer id;

    private String content;

    @Type(type = "json")
    @Column(columnDefinition = "json")
    private Image image;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fish_book_id")
    private FishBook fishBook;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fishing_spot_id")
    private FishingSpot fishingSpot;

    @OneToMany(
            mappedBy = "board",
            fetch = FetchType.LAZY,
            cascade = CascadeType.PERSIST,
            orphanRemoval = true)
    private List<BoardHashTag> boardHashTags;

    @OneToMany(mappedBy = "board", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<Comment> comments;

    @OneToMany(
            mappedBy = "board",
            fetch = FetchType.LAZY,
            cascade = CascadeType.PERSIST,
            orphanRemoval = true)
    private List<Like> likes;

    public void update(
            final BoardUpdateRequest updateRequest,
            final Category category,
            final FishBook fishBook,
            final FishingSpot fishingSpot) {
        this.content = updateRequest.getContent();
        this.image =
                Image.of(
                        updateRequest.getImageUrl(),
                        updateRequest.getLongitude(),
                        updateRequest.getLatitude(),
                        updateRequest.getCreatedAt());
        this.category = category;
        this.fishBook = fishBook;
        this.fishingSpot = fishingSpot;
    }

    public void updateHashTags(List<HashTag> hashTags) {
        boardHashTags.clear();
        addHashTags(hashTags);
    }

    public void addHashTags(List<HashTag> hashTags) {
        this.addAll(this, hashTags);
    }

    public void addAll(Board board, List<HashTag> hashTags) {
        validateDuplicateHashTag(hashTags);
        hashTags.forEach(this::validateDuplicateHashTagAlreadyExistsInBoard);

        hashTags.stream().map(tag -> new BoardHashTag(board, tag)).forEach(boardHashTags::add);
    }

    private void validateDuplicateHashTag(List<HashTag> hashTags) {
        long distinctCountOfNewTags = hashTags.stream().map(HashTag::getName).distinct().count();

        if (distinctCountOfNewTags != hashTags.size()) {
            throw new BadRequestException(CANNOT_ADD_HASHTAG_EXCEPTION);
        }
    }

    private void validateDuplicateHashTagAlreadyExistsInBoard(HashTag hashTag) {
        boolean isDuplicate =
                boardHashTags.stream().anyMatch(postTag -> postTag.hasSameTag(hashTag));

        if (isDuplicate) {
            throw new BadRequestException(CANNOT_ADD_HASHTAG_EXCEPTION);
        }
    }

    public boolean isNotWrittenBy(User user) {
        return !this.user.equals(user);
    }

    public void like(User user) {
        Like like = Like.builder().user(user).board(this).build();
        likes.add(like);
    }

    public void unlike(User user) {
        Like like = Like.builder().user(user).board(this).build();
        likes.remove(like);
    }

    public Boolean isLiked(String socialId) {
        for (Like like : likes) {
            if (like.getUser().getSocialId().equals(socialId)) {
                return true;
            }
        }
        return false;
    }

    public int getLikeCounts() {
        return likes.size();
    }

    public static Board of(
            final User user,
            final Category category,
            final FishBook fishBook,
            final FishingSpot fishingSpot,
            final BoardRequest boardRequest) {
        return new Board(
                null,
                boardRequest.getContent(),
                Image.of(
                        boardRequest.getImageUrl(),
                        boardRequest.getLongitude(),
                        boardRequest.getLatitude(),
                        boardRequest.getCreatedAt()),
                category,
                user,
                fishBook,
                fishingSpot,
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>());
    }
}
