package life.wpx1997.community.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author 不会飞的小鹏
 * create on 2020/7/24 22:46
 * @Description CommentDeleteDTO is
 */
@Data
public class CommentDeleteDTO {

    /**
     * @author: 不会飞的小鹏
     * @date: 2020/7/24 22:49
     * @description: CommentDeleteDTO's 问题id
     */
    @NotNull(message = "问题id不能为空")
    private Long questionId;

    /**
     * @author: 不会飞的小鹏
     * @date: 2020/7/24 22:51
     * @description: CommentDeleteDTO's 评论id
     */
    @NotNull(message = "评论id不能为空")
    private Long commentId;

}
