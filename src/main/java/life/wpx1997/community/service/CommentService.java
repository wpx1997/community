package life.wpx1997.community.service;

import life.wpx1997.community.enums.CommentTypeEnum;
import life.wpx1997.community.exception.CustomizeErrorCode;
import life.wpx1997.community.exception.CustomizeException;
import life.wpx1997.community.mapper.CommentMapper;
import life.wpx1997.community.mapper.QuestionExpandMapper;
import life.wpx1997.community.mapper.QuestionMapper;
import life.wpx1997.community.model.Comment;
import life.wpx1997.community.model.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentService {

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private QuestionExpandMapper questionExpandMapper;

    public void insert(Comment comment) {

//        如果回复的问题或评论的id为null或0
        if (comment.getQuestionId() == null || comment.getQuestionId() == 0){
            throw new CustomizeException(CustomizeErrorCode.TAGET_PARAM_NOT_FOUND);
        }

//        如果需要传入的comment的type不存在或与所有的typeEnum不匹配
        if (comment.getType() == null || !CommentTypeEnum.isExist(comment.getType())){
            throw new CustomizeException(CustomizeErrorCode.TYPE_PARAM_WRONG);
        }

//        如果需要传入的comment的type与COMMENT匹配
        if (comment.getType() == CommentTypeEnum.COMMENT.getType()){
            Comment dbComment = commentMapper.selectByPrimaryKey(comment.getQuestionId());
            if (dbComment == null){
                throw new CustomizeException(CustomizeErrorCode.COMMENT_NOT_FOUND);
            }
            commentMapper.insert(comment);
        }
        else {
            Question question = new Question();
//            如果需要传入的comment的type与QUESTION匹配但问题不存在
            if (question == null){
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }

//            需要传入的comment的type与QUESTION匹配且存在
            commentMapper.insert(comment);

//            累计评论数
            question.setCommentCount(1L);
            questionExpandMapper.cumulativeCommentCount(question);

        }
    }
}
