package life.wpx1997.community.dto;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author 不会飞的小鹏
 */
@Data
public class CommentDTO {

    private Long id;

    @NotNull(message = "未选中任何问题和回复，请重新选择")
    @Min(value = 1,message = "选中的问题或评论id为错误id")
    private Long parentId;

    @NotNull(message = "评论类型为空")
    @Min(value = 1,message = "评论类型错误")
    @Max(value = 2,message = "评论类型错误")
    private Byte type;

    @NotNull(message = "问题id不能为空")
    @Min(value = 0,message = "问题id错误")
    private Long questionId;

    private String content;

}
