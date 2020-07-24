package life.wpx1997.community.model;

import lombok.Data;

/**
 * @author 不会飞的小鹏
 * create on 2020/7/23 11:39
 * @Description CommentCreatorModel is
 */
@Data
public class CommentUpdateModel {

    /**
     * @author: 不会飞的小鹏
     * @date: 2020/7/24 23:10
     * @description: CommentUpdateModel's 评论的id
     */
    private Long id;

    /**
     * @author: 不会飞的小鹏
     * @date: 2020/7/24 23:10
     * @description: CommentUpdateModel's 评论的父级id
     */
    private Long parentId;

    /**
     * @author: 不会飞的小鹏
     * @date: 2020/7/24 23:10
     * @description: CommentUpdateModel's 评论人id
     */
    private Long commentator;

    /**
     * @author: 不会飞的小鹏
     * @date: 2020/7/24 23:11
     * @description: CommentUpdateModel's 评论类型
     */
    private Integer type;

}
