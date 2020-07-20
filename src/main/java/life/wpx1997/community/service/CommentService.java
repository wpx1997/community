package life.wpx1997.community.service;

import life.wpx1997.community.dto.CommentCreateDTO;
import life.wpx1997.community.dto.CommentDTO;
import life.wpx1997.community.enums.CommentTypeEnum;
import life.wpx1997.community.enums.NotificationTypeEnum;
import life.wpx1997.community.enums.NotificationStatusEnum;
import life.wpx1997.community.exception.CustomizeErrorCode;
import life.wpx1997.community.exception.CustomizeException;
import life.wpx1997.community.mapper.*;
import life.wpx1997.community.model.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author 小case
 */
@Service
public class CommentService {

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private QuestionExpandMapper questionExpandMapper;

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private CommentExpandMapper commentExpandMapper;

    @Autowired
    private NotificationMapper notificationMapper;

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
            Comment dbComment = commentMapper.selectByPrimaryKey(parentId);

            // 如果回复的comment不存在
            if (dbComment == null){
                throw new CustomizeException(CustomizeErrorCode.COMMENT_NOT_FOUND);
            }
            Question dbQuestion = questionMapper.selectByPrimaryKey(dbComment.getParentId());

            // 如果需要传入的comment的type与COMMENT匹配但comment所在的question不存在
            if (dbQuestion == null){
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }

            // 将comment的comment插入数据库
            commentMapper.insert(comment);

            // 累计comment的commentCount
            dbComment.setCommentCount(1L);
            commentExpandMapper.cumulativeCommentCount(dbComment);

            // 创建通知
            createNotify(comment, dbComment.getCommentator(),dbQuestion.getTitle(), commentator.getName(),dbQuestion.getId(), NotificationTypeEnum.REPLY_COMMENT);
        }
        // 如果需要传入的comment的type与QUESTION匹配
        else {
            Question dbQuestion = questionMapper.selectByPrimaryKey(comment.getParentId());

            // 如果需要传入的comment的type与QUESTION匹配但问题不存在
            if (dbQuestion == null){
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }

            // 需要传入的comment的type与QUESTION匹配且存在
            commentMapper.insert(comment);

            // 累计评论数
            dbQuestion.setCommentCount(1L);
            questionExpandMapper.cumulativeCommentCount(dbQuestion);

            // 创建通知.
            createNotify(comment, dbQuestion.getCreator(),dbQuestion.getTitle(), commentator.getName(), dbQuestion.getId(),NotificationTypeEnum.REPLY_QUESTION);

        }
    }

    /**
     *
     * createNotify by
     *
     * @author: 小case
     * @date: 2020/6/17 13:32
     * @param comment
     * @param receiver
     * @param outerTitle
     * @param notifierName
     * @param outerId
     * @param notificationTypeEnumType
     * @return: void
     */
    private void createNotify(Comment comment, Long receiver, String outerTitle, String notifierName, Long outerId, NotificationTypeEnum notificationTypeEnumType) {
        if (receiver.equals(comment.getCommentator())){
            return;
        }
        Notification notification = new Notification();
        notification.setGmtCreate(System.currentTimeMillis());
        notification.setType(notificationTypeEnumType.getType());
        notification.setOuterId(outerId);
        notification.setNotifier(comment.getCommentator());
        notification.setStatus(NotificationStatusEnum.UNREAD.getStatus());
        notification.setReceiver(receiver);
        notification.setNotifierName(notifierName);
        notification.setOuterTitle(outerTitle);
        notificationMapper.insert(notification);
    }

    /**
     *
     * listByTargetId by
     *
     * @author: 小case
     * @date: 2020/6/17 13:32
     * @param id
     * @param type
     * @return: List<CommentDTO>
     */
    public List<CommentDTO> listByTargetId(Long id, CommentTypeEnum type) {
        CommentExample commentExample = new CommentExample();
        commentExample.createCriteria().andParentIdEqualTo(id).andTypeEqualTo(type.getType());
        commentExample.setOrderByClause("gmt_create desc");
        List<Comment> comments = commentMapper.selectByExample(commentExample);
        if (comments.isEmpty()){
            return new ArrayList<>();
        }

        // 获取去重的评论人
        Set<Long> commentators = comments.stream().map(Comment::getCommentator).collect(Collectors.toSet());
        List<Long> userIds = new ArrayList<>();
        userIds.addAll(commentators);

        // 获取评论人并转换为 Map
        UserExample userExample = new UserExample();
        userExample.createCriteria().andIdIn(userIds);
        List<User> users = userMapper.selectByExample(userExample);
        Map<Long, User> userMap = users.stream().collect(Collectors.toMap(user -> user.getId(), user -> user));

//        转换 comment 为 commentDTO
        List<CommentDTO> commentDTOS = comments.stream().map(comment -> {
            CommentDTO commentDTO = new CommentDTO();
            BeanUtils.copyProperties(comment,commentDTO);
            commentDTO.setUser(userMap.get(comment.getCommentator()));
            return commentDTO;
        }).collect(Collectors.toList());

        return commentDTOS;
    }

    public List<Comment> selectCommentListByQuestionId(Long id,Integer type) {

        CommentExample commentExample = new CommentExample();
        commentExample.createCriteria().andParentIdEqualTo(id).andTypeEqualTo(type);
        List<Comment> commentList = commentMapper.selectByExample(commentExample);

        return commentList;
    }

    public List<Comment> selectCommentListByCommentIdList(List<Long> commentIdList, Integer type) {

        CommentExample commentExample = new CommentExample();
        commentExample.createCriteria().andParentIdIn(commentIdList).andTypeEqualTo(type);
        List<Comment> commentList = commentMapper.selectByExample(commentExample);

        return commentList;
    }

    /**
     *
     * insertComment 向数据库中插入comment
     *
     * @author: 小case
     * @date: 2020/6/16 8:50
     * @param commentCreateDTO
     * @param id
     * @return: void
     */

}
