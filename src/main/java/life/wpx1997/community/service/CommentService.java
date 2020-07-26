package life.wpx1997.community.service;

import life.wpx1997.community.cache.CumulativeCache;
import life.wpx1997.community.dto.CommentCreateDTO;
import life.wpx1997.community.dto.CommentCumulativeDTO;
import life.wpx1997.community.enums.CommentTypeEnum;
import life.wpx1997.community.enums.NotificationTypeEnum;
import life.wpx1997.community.exception.CustomizeErrorCode;
import life.wpx1997.community.exception.CustomizeException;
import life.wpx1997.community.mapper.*;
import life.wpx1997.community.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author 小case
 */
@Service
public class CommentService {

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private CommentExpandMapper commentExpandMapper;

    @Autowired
    private NotificationService notificationService;

    @Autowired QuestionService questionService;

    @Autowired
    private CumulativeCache cumulativeCache;

    /**
     *
     * insertComment by
     *
     * @author: 小case
     * @date: 2020/6/17 13:31
     * @param commentCreateDTO
     * @param commentator
     * @return: void
     */
    @Transactional(rollbackFor = Exception.class)
    public void insertComment(CommentCreateDTO commentCreateDTO, User commentator) {

        Comment comment = new Comment();
        comment.setParentId(commentCreateDTO.getParentId());
        comment.setCommentator(commentator.getId());
        comment.setContent(commentCreateDTO.getContent());
        comment.setType(commentCreateDTO.getType());
        comment.setGmtCreate(System.currentTimeMillis());
        comment.setGmtModified(System.currentTimeMillis());
        comment.setLikeCount(0L);
        comment.setCommentCount(0L);

        Long parentId = comment.getParentId();

        // 如果回复的问题或评论的id为null或0
        if (parentId == null || parentId == 0){
            throw new CustomizeException(CustomizeErrorCode.TARGET_PARAM_NOT_FOUND);
        }

        // 如果需要传入的comment的type不存在或与所有的typeEnum不匹配
        if (comment.getType() == null || !CommentTypeEnum.isExist(comment.getType())){
            throw new CustomizeException(CustomizeErrorCode.TYPE_PARAM_WRONG);
        }

        // 如果需要传入的comment的type与COMMENT匹配
        if (comment.getType().equals(CommentTypeEnum.COMMENT.getType()) ){
            Comment dbComment = commentExpandMapper.selectCommentParentIdById(parentId);

            // 如果回复的comment不存在
            if (dbComment == null){
                throw new CustomizeException(CustomizeErrorCode.COMMENT_NOT_FOUND);
            }
            Question question = questionService.selectQuestionTitleById(dbComment.getParentId());

            // 如果需要传入的comment的type与COMMENT匹配但comment所在的question不存在
            if (question == null){
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }

            // 将comment的comment插入数据库
            commentMapper.insert(comment);

            // 累计comment的commentCount
            dbComment.setCommentCount(1L);
            cumulativeCommentCount(dbComment);

            question.setCommentCount(1L);
            questionService.cumulativeCommentCount(question);

            // 创建通知
            notificationService.createNotify(comment, dbComment.getCommentator(),question.getTitle(), commentator.getName(),question.getId(), NotificationTypeEnum.REPLY_COMMENT);
        }
        // 如果需要传入的comment的type与QUESTION匹配
        else {
            Question question = questionService.selectQuestionTitleById(comment.getParentId());

            // 如果需要传入的comment的type与QUESTION匹配但问题不存在
            if (question == null){
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }

            // 需要传入的comment的type与QUESTION匹配且存在
            commentMapper.insert(comment);

            // 累计评论数
            question.setCommentCount(1L);
            questionService.cumulativeCommentCount(question);

            // 创建通知.
            notificationService.createNotify(comment, question.getCreator(),question.getTitle(), commentator.getName(), question.getId(),NotificationTypeEnum.REPLY_QUESTION);

        }
    }

    /**
     *
     * selectCommentListByQuestionId by 根据问题id和评论类型查询评论
     *
     * @author: 不会飞的小鹏
     * @date: 2020/7/23 11:35
     * @param id
     * @param type
     * @return: List<Comment>
     */
    public List<Comment> selectCommentListByQuestionId(Long id,Integer type) {

        CommentExample commentExample = new CommentExample();
        commentExample.createCriteria().andParentIdEqualTo(id).andTypeEqualTo(type);
        List<Comment> commentList = commentMapper.selectByExample(commentExample);

        return commentList;
    }

    /**
     *
     * selectCommentListByCommentIdList by 根据问题评论id列表查询问题评论的回复
     *
     * @author: 不会飞的小鹏
     * @date: 2020/7/23 11:37
     * @param commentIdList
     * @param type
     * @return: List<Comment>
     */
    public List<Comment> selectCommentListByCommentIdList(List<Long> commentIdList, Integer type) {

        CommentExample commentExample = new CommentExample();
        commentExample.createCriteria().andParentIdIn(commentIdList).andTypeEqualTo(type).andIsDeleteEqualTo((byte) 0);
        List<Comment> commentList = commentMapper.selectByExample(commentExample);

        return commentList;
    }

    /**
     *
     * checkOneself by 检查是否是评论作者本人
     *
     * @author: 不会飞的小鹏
     * @date: 2020/7/23 11:37
     * @param id
     * @return: Boolean
     */
    public Comment selectCommentUpdateModelById(Long id) {

        Comment comment = commentExpandMapper.selectCommentUpdateModelById(id);

        return comment;
    }

    /**
     *
     * deleteCommentById by 将评论状态变更为删除状态
     *
     * @author: 不会飞的小鹏
     * @date: 2020/7/24 1:10
     * @param comment
     * @return: void
     */
    public void deleteCommentById(Comment comment) {

        comment.setIsDelete((byte) 1);
        commentMapper.updateByPrimaryKeySelective(comment);

        // 删除的评论为二级评论
        if (comment.getType().equals(CommentTypeEnum.COMMENT.getType())){
            Comment dbComment = commentExpandMapper.selectCommentParentIdById(comment.getParentId());
            dbComment.setCommentCount((long) -1);
            cumulativeCommentCount(dbComment);
            Question question = questionService.selectQuestionTitleById(dbComment.getParentId());
            question.setCommentCount((long) -1);
            questionService.cumulativeCommentCount(question);
        }else { // 删除的评论为一级评论
            Question question = questionService.selectQuestionTitleById(comment.getParentId());
            question.setCommentCount((long) -1);
            questionService.cumulativeCommentCount(question);
        }
    }

    /**
     *
     * cumulativeCommentCount by 累计评论的回复数
     *
     * @author: 不会飞的小鹏
     * @date: 2020/7/24 23:36
     * @param comment
     * @return: void
     */
    public void cumulativeCommentCount(Comment comment){
        cumulativeCache.cumulativeCommentCommentCount(comment.getId(),comment.getCommentCount());
    }

    /**
     *
     * commentCumulative by 批量更新commentCount
     *
     * @author: 不会飞的小鹏
     * @date: 2020/7/26 15:30
     * @param commentCumulativeList
     * @return: void
     */
    public void commentCumulative(List<CommentCumulativeDTO> commentCumulativeList) {
        commentExpandMapper.cumulativeCommentCount(commentCumulativeList);
    }
}
