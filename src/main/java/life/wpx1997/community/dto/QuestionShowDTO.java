package life.wpx1997.community.dto;

import lombok.Data;

/**
 * @author 不会飞的小鹏
 * create on 2020/7/25 18:58
 * @Description QuestionShowDTO is
 */
@Data
public class QuestionShowDTO {
    private Long id;
    private Long creator;
    private String creatorAvatarUrl;
    private String title;
    private String creatorName;
    private Long commentCount;
    private Long viewCount;
    private Long gmtCreate;
}
