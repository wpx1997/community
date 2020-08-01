package life.wpx1997.community.service;

import life.wpx1997.community.cache.CumulativeCache;
import life.wpx1997.community.constant.CumulativeConstant;
import life.wpx1997.community.constant.TypeConstant;
import life.wpx1997.community.dto.*;
import life.wpx1997.community.enums.CommentTypeEnum;
import life.wpx1997.community.enums.NotificationTypeEnum;
import life.wpx1997.community.exception.CustomizeErrorCode;
import life.wpx1997.community.exception.CustomizeException;
import life.wpx1997.community.mapper.CommentExpandMapper;
import life.wpx1997.community.mapper.CommentMapper;
import life.wpx1997.community.model.Comment;
import life.wpx1997.community.model.CommentExample;
import life.wpx1997.community.model.Question;
import life.wpx1997.community.model.User;
import org.springframework.beans.BeanUtils;
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
    private CacheService cacheService;

    @Autowired
    private RedisService redisService;

    /**
     *
     * insertComment by 新增comment
     *
     * @author: 小case
     * @date: 2020/6/17 13:31
     * @param commentDTO
     * @param commentator
     * @return: void
     */
    public void insertComment(CommentDTO commentDTO, User commentator) {

        Question question = questionService.selectQuestionTitleById(commentDTO.getQuestionId());

        // 如果需要传入的comment的type与QUESTION匹配但问题不存在
        if (question == null){
            throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
        }

        Long commentId = cacheService.getCommentId();
        Comment comment = new Comment();
        comment.setId(commentId);
        comment.setParentId(commentDTO.getParentId());
        comment.setCommentator(commentator.getId());
        comment.setContent(commentDTO.getContent());
        comment.setType(commentDTO.getType());
        comment.setGmtCreate(System.currentTimeMillis());
        comment.setGmtModified(System.currentTimeMillis());
        comment.setLikeCount(0L);
        comment.setCommentCount(0L);

        // 如果需要传入的comment的type与COMMENT匹配
        if (comment.getType().equals(TypeConstant.COMMENT_TYPE_COMMENT)){

            updateQuestionInRedis(comment,commentDTO.getQuestionId(),commentator);

            // 将comment写入redis
            addCommentToRedis(comment);

            // 累计comment的commentCount
            Comment dbComment = new Comment();
            dbComment.setId(comment.getParentId());
            dbComment.setCommentCount(1L);
            cumulativeCommentCount(dbComment);

            question.setCommentCount(1L);
            questionService.cumulativeCommentCount(question);

            // 创建通知
            notificationService.createNotify(comment, dbComment.getCommentator(),question.getTitle(), commentator.getName(),question.getId(), NotificationTypeEnum.REPLY_COMMENT);
        }
        // 如果需要传入的comment的type与QUESTION匹配
        else {

            updateQuestionInRedis(comment,commentDTO.getQuestionId(),commentator);

            // 需要传入的comment的type与QUESTION匹配且存在
            addCommentToRedis(comment);

            // 累计评论数
            question.setCommentCount(1L);
            questionService.cumulativeCommentCount(question);

            // 创建通知.
            notificationService.createNotify(comment, question.getCreator(),question.getTitle(), commentator.getName(), question.getId(),NotificationTypeEnum.REPLY_QUESTION);

        }
    }

    /**
     *
     * updateQuestionInRedis by 更新redis中的question，将新增的comment添加进去
     *
     * @author: 不会飞的小鹏
     * @date: 2020/8/1 21:52
     * @param comment
     * @param questionId
     * @param commentator
     * @return: void
     */
    private void updateQuestionInRedis(Comment comment, Long questionId, User commentator) {
        QuestionMessageDTO questionMessageDTO = questionService.selectQuestionMessageByRedis(questionId);
        if (questionMessageDTO != null){
            CommentMessageDTO commentMessageDTO = new CommentMessageDTO();
            BeanUtils.copyProperties(comment,commentMessageDTO);
            commentMessageDTO.setCreatorName(commentator.getName());
            commentMessageDTO.setCreatorAvatarUrl(commentator.getAvatarUrl());
            questionMessageDTO.getQuestionCommentList().add(commentMessageDTO);
            questionService.addQuestionToRedis(questionMessageDTO);
        }
    }

    private void addCommentToRedis(Comment comment) {
        redisService.insertComment(comment);
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
    public List<Comment> selectCommentListByQuestionId(Long id,Byte type) {

        // 从数据库中获取评论
        CommentExample commentExample = new CommentExample();
        commentExample.createCriteria().andParentIdEqualTo(id).andTypeEqualTo(type);
        List<Comment> commentList = commentMapper.selectByExample(commentExample);

        // 从redis缓存中获取评论
        commentList.addAll(redisService.getCommentByParentId(id,type));

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
    public List<Comment> selectCommentListByCommentIdList(List<Long> commentIdList, Byte type) {

        // 从数据库中获取二级评论
        CommentExample commentExample = new CommentExample();
        commentExample.createCriteria().andParentIdIn(commentIdList).andTypeEqualTo(type).andIsDeleteEqualTo(TypeConstant.IS_DELETE_NOT);
        List<Comment> commentList = commentMapper.selectByExample(commentExample);

        // 从redis中获取二级评论
        commentList.addAll(redisService.getCommentByParentIdList(commentIdList, type));

        return commentList;
    }

    /**
     *
     * selectCommentUpdateModelById by 查询问题作者
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
     * @param commentDTO
     * @param userId
     * @return: void
     */
    public Boolean deleteComment(CommentDTO commentDTO,Long userId) {

        Question question = questionService.selectQuestionTitleById(commentDTO.getParentId());
        if (question == null){
            throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
        }

        // 从缓存中删除评论
        Boolean delete = deleteCommentFromRedis(commentDTO.getId(),commentDTO.getParentId(),commentDTO.getType(),userId);
        // 非评论作者本人操作
        if (delete == null){
            return null;
        }else {
            if (delete){
                setQuestionWithCommentCount(question,commentDTO.getType(),commentDTO.getParentId());
                return true;
            }else {
                Comment comment = selectCommentUpdateModelById(commentDTO.getId());
                if (comment == null){
                    return false;
                }else {
                    if (userId.equals(comment.getCommentator())){
                        comment.setIsDelete(TypeConstant.IS_DELETE_YES);
                        commentMapper.updateByPrimaryKeySelective(comment);
                        setQuestionWithCommentCount(question,commentDTO.getType(),commentDTO.getParentId());
                        return true;
                    }else {
                        return null;
                    }
                }
            }
        }

    }

    /**
     *
     * setQuestionWithCommentCount by 对问题和评论的回复数进行修改
     *
     * @author: 不会飞的小鹏
     * @date: 2020/8/2 1:14
     * @param question
     * @param type
     * @param userId
     * @return: void
     */
    public void setQuestionWithCommentCount(Question question,Byte type,Long userId){
        question.setCommentCount(CumulativeConstant.CUMULATIVE_CUT);
        questionService.cumulativeCommentCount(question);
        if (TypeConstant.COMMENT_TYPE_COMMENT.equals(type)){
            cumulativeCommentCount(userId);
        }
    }

    public void cumulativeCommentCount(Long parentId){
        Comment comment = new Comment();
        comment.setId(parentId);
        comment.setCommentCount(CumulativeConstant.CUMULATIVE_CUT);
        cumulativeCommentCount(comment);
    }

    /**
     *
     * deleteCommentFromRedis by 从redis中删除评论
     *
     * @author: 不会飞的小鹏
     * @date: 2020/8/1 23:23
     * @param id
     * @param parentId
     * @param type
     * @return: Boolean
     */
    private Boolean deleteCommentFromRedis(Long id, Long parentId, Byte type,Long userId) {
        Boolean delete = redisService.deleteCommentFromRedis(id,parentId,type,userId);
        return delete;
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
        cacheService.cumulativeCommentCommentCount(comment);
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

    /**
     *
     * insertCommentList by 批量插入批量
     *
     * @author: 不会飞的小鹏
     * @date: 2020/7/31 1:29
     * @param commentList
     * @return: void
     */
    public void insertCommentList(List<Comment> commentList) {

        commentExpandMapper.insertCommentList(commentList);

    }
}
