package life.wpx1997.community.model;

import lombok.Data;

/**
 * @author 不会飞的小鹏
 * create on 2020/7/21 16:32
 * @Description QuestionPublishModel is
 */
@Data
public class QuestionPublishModel {
    private Long id;
    private Long creator;
    private String title;
    private String description;
    private String tag;
}
