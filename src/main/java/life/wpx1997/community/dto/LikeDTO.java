package life.wpx1997.community.dto;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @author 不会飞的小鹏
 * create on 2020/7/27 21:36
 * @Description LikeDTO is
 */
@Data
public class LikeDTO {

    /**
     * @author: 不会飞的小鹏
     * @date: 2020/7/27 21:43
     * @description: LikeDTO's parentId 点赞指向的id
     */
    @NotNull(message = "点赞的指向id不能为空")
    private Long parentId;

    /**
     * @author: 不会飞的小鹏
     * @date: 2020/7/27 21:40
     * @description: LikeDTO's type 点赞类型（1为问题点赞，2为一级评论点赞）
     */
    @Max(value = 2,message = "类别范围超出")
    @Min(value = 1,message = "类别范围超出")
    private Byte type;
}
