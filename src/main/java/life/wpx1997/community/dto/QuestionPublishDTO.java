package life.wpx1997.community.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author 不会飞的小鹏
 * create on 2020/7/21 17:34
 * @Description QuestionPublishDTO is
 */
@Data
public class QuestionPublishDTO {

    /**
     * @author: 不会飞的小鹏
     * @date: 2020/7/21 17:41
     * @description: QuestionPublishDTO's 问题id
     */
    private Long id;

    /**
     * @author: 不会飞的小鹏
     * @date: 2020/7/21 17:41
     * @description: QuestionPublishDTO's 问题标题
     */
    @NotBlank(message = "问题标题不能为空")
    private String title;

    /**
     * @author: 不会飞的小鹏
     * @date: 2020/7/21 17:43
     * @description: QuestionPublishDTO's 问题内容
     */
    @NotBlank(message = "问题内容不能为空")
    private String description;

    /**
     * @author: 不会飞的小鹏
     * @date: 2020/7/21 17:43
     * @description: QuestionPublishDTO's 问题标签
     */
    @NotBlank(message = "问题标签不能为空")
    private String tag;

}
