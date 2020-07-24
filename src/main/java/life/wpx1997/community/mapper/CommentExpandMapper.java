package life.wpx1997.community.mapper;

import life.wpx1997.community.model.Comment;
import life.wpx1997.community.model.CommentUpdateModel;

/**
 * @author 666
 */
public interface CommentExpandMapper {

    /**
     *
     * cumulativeCommentCount by
     *
     * @author: 不会飞的小鹏
     * @date: 2020/7/4 20:21
     * @param record
     * @return: int
     */
    int cumulativeCommentCount(Comment record);

    /**
     *
     * selectCommentUpdateModelById by 根据评论id获取评论的重要信息
     *
     * @author: 不会飞的小鹏
     * @date: 2020/7/23 11:41
     * @param id
     * @return: CommentCreatorModel
     */
    CommentUpdateModel selectCommentUpdateModelById(Long id);

    /**
     *
     * reduceCommentCount by
     *
     * @author: 不会飞的小鹏
     * @date: 2020/7/24 23:19
     * @param record
     * @return: int
     */
    int reduceCommentCount(Comment record);
}