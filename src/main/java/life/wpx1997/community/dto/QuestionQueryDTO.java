package life.wpx1997.community.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author 不会飞的小鹏
 * created on 2019/8/31 3:53
 */
@Data
public class QuestionQueryDTO {
    private Long userId;
    private String search;
    private Integer offset;
}
