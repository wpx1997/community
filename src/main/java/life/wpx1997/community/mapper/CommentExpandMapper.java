package life.wpx1997.community.mapper;

import life.wpx1997.community.dto.CommentCumulativeDTO;
import life.wpx1997.community.model.Comment;

import java.util.List;

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
     * @param commentCumulativeList
     * @return: int
     */
    int cumulativeCommentCount(List<CommentCumulativeDTO> commentCumulativeList);

    /**
     *
     * selectCommentUpdateModelById by 根据评论id获取评论的重要信息
     *
     * @author: 不会飞的小鹏
     * @date: 2020/7/23 11:41
     * @param id
     * @return: CommentCreatorModel
     */
    Comment selectCommentUpdateModelById(Long id);

    /**
     *
     * selectCommentParentIdById by 查询父评论
     *
     * @author: 不会飞的小鹏
     * @date: 2020/7/26 13:50
     * @param id
     * @return: Comment
     */
    Comment selectCommentParentIdById(Long id);

    /**
     *
     * insertCommentList by 批量插入评论
     *
     * @author: 不会飞的小鹏
     * @date: 2020/7/31 1:30
     * @param commentList
     * @return: int
     */
    int insertCommentList(List<Comment> commentList);
}