package life.wpx1997.community.dto;

import life.wpx1997.community.model.Question;
import lombok.Data;

import java.util.List;

/**
 * @author 不会飞的小鹏
 */
@Data
public class QuestionMessageDTO {
    private Long id;
    private String accountId;
    private String title;
    private String description;
    private String tag;
    private Long gmtCreate;
    private Long gmtModified;
    private Long creator;
    private Long viewCount;
    private Long commentCount;
    private Long likeCount;
    private String creatorName;
    private String creatorAvatarUrl;
    private Boolean isLike;
    private List<Question> dependentQuestionTitleList;
    private List<Question> thisCreatorQuestionTitleList;
    private List<CommentMessageDTO> questionCommentList;
}
