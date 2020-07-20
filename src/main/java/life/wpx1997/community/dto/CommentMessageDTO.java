package life.wpx1997.community.dto;

import life.wpx1997.community.model.User;
import lombok.Data;

import java.util.List;

/**
 * created on 2019/8/22 23:33
 * @author 不会飞的小鹏
 */

@Data
public class CommentMessageDTO {
    private Long id;
    private Long parentId;
    private Integer type;
    private Long commentator;
    private Long gmtCreate;
    private Long gmtModified;
    private Long likeCount;
    private String content;
    private Long commentCount;
    private String creatorName;
    private String creatorAvatarUrl;
    private List<CommentMessageDTO> commentCommentList;
}
