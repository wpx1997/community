package life.wpx1997.community.dto;

import life.wpx1997.community.model.User;
import lombok.Data;

/**
 * created on 2019/8/22 23:33
 * @author 不会飞的小鹏
 */

@Data
public class CommentDTO {
    private Long id;
    private Long parentId;
    private Byte type;
    private Long commentator;
    private Long gmtCreate;
    private Long gmtModified;
    private Long likeCount;
    private String content;
    private Long commentCount;
    private User user;
}
