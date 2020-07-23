package life.wpx1997.community.mapper;

import life.wpx1997.community.model.Comment;
import life.wpx1997.community.model.CommentCreatorModel;

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
     * selectCommentCreatorModelById by 根据评论id过去作者id
     *
     * @author: 不会飞的小鹏
     * @date: 2020/7/23 11:41
     * @param id
     * @return: CommentCreatorModel
     */
    CommentCreatorModel selectCommentCreatorModelById(Long id);
}