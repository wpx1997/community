package life.wpx1997.community.model;

import lombok.Data;

/**
 * @author 不会飞的小鹏
 * create on 2020/7/20 22:11
 * @Description QuestionShowModel is
 */
@Data
public class QuestionShowModel {
    private Long id;
    private Long creator;
    private String creatorAvatarUrl;
    private String title;
    private String creatorName;
    private Long commentCount;
    private Long viewCount;
    private Long gmtCreate;
}
