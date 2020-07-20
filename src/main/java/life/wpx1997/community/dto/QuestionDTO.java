package life.wpx1997.community.dto;

import life.wpx1997.community.model.User;
import lombok.Data;

/**
 * @author 不会飞的小鹏
 */
@Data
public class QuestionDTO {
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
    private User user;
}
