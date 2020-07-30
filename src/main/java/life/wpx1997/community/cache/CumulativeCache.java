package life.wpx1997.community.cache;

import life.wpx1997.community.dto.CommentCumulativeDTO;
import life.wpx1997.community.dto.QuestionCumulativeDTO;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 不会飞的小鹏
 * create on 2020/7/25 23:46
 * @Description ViewCountCache is
 */
@Component
public class CumulativeCache {

    /**
     * @author: 不会飞的小鹏
     * @date: 2020/7/26 21:07
     * @description: CumulativeCache's questionCumulativeMap 问题累计数缓存
     */
    private Map<Long, QuestionCumulativeDTO> questionCumulativeMap = new HashMap<>();

    /**
     * @author: 不会飞的小鹏
     * @date: 2020/7/26 21:07
     * @description: CumulativeCache's commentCumulativeMap 评论累计数缓存
     */
    private Map<Long, CommentCumulativeDTO> commentCumulativeMap = new HashMap<>();

    /**
     * @author: 不会飞的小鹏
     * @date: 2020/7/31 0:27
     * @description: CumulativeCache's 最新评论id
     */
    private Long commentId;

    /**
     *
     * cumulativeQuestionViewCount by 累计问题阅读数
     *
     * @author: 不会飞的小鹏
     * @date: 2020/7/26 21:08
     * @param id
     * @return: void
     */
    public void cumulativeQuestionViewCount(Long id){

        QuestionCumulativeDTO question = questionCumulativeMap.get(id);
        if (question == null){
            question = new QuestionCumulativeDTO(id);
            question.setViewCount(1L);
            this.questionCumulativeMap.put(id,question);
        }else {
            Long questionViewCount = question.getViewCount();
            question.setViewCount(questionViewCount + 1L);
            this.questionCumulativeMap.put(id,question);
        }

    }

    /**
     *
     * cumulativeQuestionCommentCount by 累计问题回复数
     *
     * @author: 不会飞的小鹏
     * @date: 2020/7/26 21:08
     * @param id
     * @param commentCount
     * @return: void
     */
    public void cumulativeQuestionCommentCount(Long id,Long commentCount){

        QuestionCumulativeDTO question = questionCumulativeMap.get(id);
        if (question == null){
            question = new QuestionCumulativeDTO(id);
            question.setCommentCount(commentCount);
            this.questionCumulativeMap.put(id,question);
        }else {
            Long questionCommentCount = question.getCommentCount();
            question.setCommentCount(questionCommentCount + commentCount);
            this.questionCumulativeMap.put(id,question);
        }

    }

    /**
     *
     * cumulativeQuestionLikeCount by 累计问题点赞数
     *
     * @author: 不会飞的小鹏
     * @date: 2020/7/26 21:09
     * @param id
     * @param likeCount
     * @return: void
     */
    public void cumulativeQuestionLikeCount(Long id,Long likeCount){

        QuestionCumulativeDTO question = questionCumulativeMap.get(id);
        if (question == null){
            question = new QuestionCumulativeDTO(id);
            question.setLikeCount(likeCount);
            this.questionCumulativeMap.put(id,question);
        }else {
            Long questionLikeCount = question.getLikeCount();
            question.setLikeCount(questionLikeCount + likeCount);
            this.questionCumulativeMap.put(id,question);
        }

    }

    /**
     *
     * cumulativeCommentCommentCount by 累计评论回复数
     *
     * @author: 不会飞的小鹏
     * @date: 2020/7/26 21:09
     * @param id
     * @param commentCount
     * @return: void
     */
    public void cumulativeCommentCommentCount(Long id,Long commentCount){

        CommentCumulativeDTO comment = commentCumulativeMap.get(id);
        if (comment == null){
            comment = new CommentCumulativeDTO(id);
            comment.setCommentCount(commentCount);
            this.commentCumulativeMap.put(id,comment);
        }else {
            Long commentLikeCount = comment.getLikeCount();
            comment.setLikeCount(commentLikeCount + commentCount);
            this.commentCumulativeMap.put(id,comment);
        }

    }

    /**
     *
     * cumulativeCommentLikeCount by 累计评论点赞数
     *
     * @author: 不会飞的小鹏
     * @date: 2020/7/26 21:09
     * @param id
     * @param likeCount
     * @return: void
     */
    public void cumulativeCommentLikeCount(Long id,Long likeCount){

        CommentCumulativeDTO comment = commentCumulativeMap.get(id);
        if (comment == null){
            comment = new CommentCumulativeDTO(id);
            comment.setLikeCount(likeCount);
            this.commentCumulativeMap.put(id,comment);
        }else {
            Long commentLikeCount = comment.getLikeCount();
            comment.setLikeCount(commentLikeCount + likeCount);
            this.commentCumulativeMap.put(id,comment);
        }

    }

    /**
     *
     * getQuestionCumulativeCacheById by 根据问题id获取未同步到数据库的累计数缓存
     *
     * @author: 不会飞的小鹏
     * @date: 2020/7/26 21:09
     * @param id
     * @return: QuestionCumulativeDTO
     */
    public QuestionCumulativeDTO getQuestionCumulativeCacheById(Long id){

        QuestionCumulativeDTO question = this.questionCumulativeMap.get(id);

        return question;
    }

    /**
     *
     * getQuestionCumulativeMap by 获取问题累计数缓存map
     *
     * @author: 不会飞的小鹏
     * @date: 2020/7/26 21:10
     * @param
     * @return: Map<Long,QuestionCumulativeDTO>
     */
    public Map<Long, QuestionCumulativeDTO> getQuestionCumulativeMap(){
        return this.questionCumulativeMap;
    }

    /**
     *
     * getCommentCumulativeMap by 获取评论累计数缓存map
     *
     * @author: 不会飞的小鹏
     * @date: 2020/7/26 21:11
     * @param
     * @return: Map<Long,CommentCumulativeDTO>
     */
    public Map<Long, CommentCumulativeDTO> getCommentCumulativeMap(){
        return this.commentCumulativeMap;
    }

    /**
     *
     * clearCumulativeCache by 清除缓存
     *
     * @author: 不会飞的小鹏
     * @date: 2020/7/26 21:11
     * @param
     * @return: void
     */
    public void clearCumulativeCache(){
        this.questionCumulativeMap.clear();
        this.commentCumulativeMap.clear();
    }

    /**
     *
     * getCommentId by 获取最新的评论id
     *
     * @author: 不会飞的小鹏
     * @date: 2020/7/31 0:28
     * @param
     * @return: Long
     */
    public Long getCommentId() {
        this.commentId++;
        return this.commentId;
    }

    /**
     *
     * setCommentId by 为评论id设值
     *
     * @author: 不会飞的小鹏
     * @date: 2020/7/31 0:29
     * @param commentId
     * @return: void
     */
    public void setCommentId(Long commentId) {
        this.commentId = commentId;
    }
}
