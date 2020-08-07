package life.wpx1997.community.service;

import life.wpx1997.community.cache.CumulativeCache;
import life.wpx1997.community.cache.HotQuestionCache;
import life.wpx1997.community.cache.HotTagCache;
import life.wpx1997.community.dto.CommentCumulativeDTO;
import life.wpx1997.community.dto.HotTagDTO;
import life.wpx1997.community.dto.QuestionCumulativeDTO;
import life.wpx1997.community.dto.QuestionShowDTO;
import life.wpx1997.community.model.Comment;
import life.wpx1997.community.model.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author 不会飞的小鹏
 * create on 2020/7/29 22:47
 * @Description CacheService is
 */
@Service
public class CacheService {

    @Autowired
    private CumulativeCache cumulativeCache;

    @Autowired
    private HotQuestionCache hotQuestionCache;

    @Autowired
    private HotTagCache hotTagCache;

    /**
     *
     * getHotQuestion by 获取问题热榜
     *
     * @author: 不会飞的小鹏
     * @date: 2020/8/7 12:45
     * @param
     * @return: List<QuestionShowDTO>
     */
    public List<QuestionShowDTO> getHotQuestion(){
        List<QuestionShowDTO> hotQuestionList = hotQuestionCache.getHotQuestionList();
        return hotQuestionList;
    }

    /**
     *
     * getHotTag by 获取标签热榜
     *
     * @author: 不会飞的小鹏
     * @date: 2020/8/7 12:49
     * @param
     * @return: List<HotTagDTO>
     */
    public List<HotTagDTO> getHotTag(){
        List<HotTagDTO> hotTagDTOList = hotTagCache.getHotTagDTOList();
        return hotTagDTOList;
    }

    /**
     *
     * cumulativeQuestionViewCount by 点击问题时累计问题的阅读数
     *
     * @author: 不会飞的小鹏
     * @date: 2020/7/30 22:50
     * @param id
     * @return: void
     */
    public void cumulativeQuestionViewCount(Long id) {
        cumulativeCache.cumulativeQuestionViewCount(id);
    }

    /**
     *
     * cumulativeCommentCount by 累计问题回复数
     *
     * @author: 不会飞的小鹏
     * @date: 2020/7/24 23:42
     * @param question
     * @return: void
     */
    public void cumulativeQuestionCommentCount(Question question) {
        cumulativeCache.cumulativeQuestionCommentCount(question.getId(),question.getCommentCount());
    }

    /**
     *
     * cumulativeCommentCommentCount by 累计评论的评论数
     *
     * @author: 不会飞的小鹏
     * @date: 2020/8/2 0:58
     * @param comment
     * @return: void
     */
    public void cumulativeCommentCommentCount(Comment comment) {
        cumulativeCache.cumulativeCommentCommentCount(comment.getId(),comment.getCommentCount());
    }

    /**
     *
     * getQuestionCumulativeCacheById by 从缓存中读取未更新到数据库的累计数
     *
     * @author: 不会飞的小鹏
     * @date: 2020/7/30 22:52
     * @param id
     * @return: QuestionCumulativeDTO
     */
    public QuestionCumulativeDTO getQuestionCumulativeCacheById(Long id) {
        QuestionCumulativeDTO questionCumulativeDTO = cumulativeCache.getQuestionCumulativeCacheById(id);
        return questionCumulativeDTO;
    }

    /**
     *
     * getCommentCumulativeMap by 为删除状态的评论设值
     *
     * @author: 不会飞的小鹏
     * @date: 2020/7/30 22:53
     * @param
     * @return: Map<Long,CommentCumulativeDTO>
     */
    public Map<Long, CommentCumulativeDTO> getCommentCumulativeMap() {
        Map<Long, CommentCumulativeDTO> commentCumulativeMap = cumulativeCache.getCommentCumulativeMap();
        return commentCumulativeMap;
    }

    /**
     *
     * getCommentId by 获取最新的id
     *
     * @author: 不会飞的小鹏
     * @date: 2020/7/31 0:26
     * @param
     * @return: Long
     */
    public Long getCommentId() {

        Long id = cumulativeCache.getCommentId();

        return id;
    }

}
