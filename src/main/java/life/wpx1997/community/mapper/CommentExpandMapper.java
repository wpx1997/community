package life.wpx1997.community.mapper;

import life.wpx1997.community.model.Comment;

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

}