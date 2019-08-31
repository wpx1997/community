package life.wpx1997.community.dto;

import lombok.Data;

/**
 * created by 小case on 2019/8/31 3:53
 */
@Data
public class QuestionQueryDTO {
    private String search;
    private Integer offset;
    private Integer size;
}
