package life.wpx1997.community.dto;

import life.wpx1997.community.model.User;
import lombok.Data;

/**
 * created by 小case on 2019/8/22 23:33
 */

@Data
public class CommentDTO {
    private Long id;
    private Long parentId;
    private Integer type;
    private Long commentator;
    private Long gmtCreate;
    private Long gmtModified;
    private Long likeCount;
    private String content;
    private Long commentCount;
    private User user;
}
