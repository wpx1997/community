package life.wpx1997.community.dto;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

/**
 * @author 不会飞的小鹏
 */
@Data
public class CommentCreateDTO {

    @Min(value = 0,message = "未选中任何问题和回复，请重新选择")
    private Long parentId;

    @NotBlank(message = "回复内容为空")
    private String content;
    private Byte type;
}
