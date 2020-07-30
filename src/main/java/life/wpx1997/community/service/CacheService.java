package life.wpx1997.community.service;

import life.wpx1997.community.cache.CumulativeCache;
import life.wpx1997.community.dto.CommentCumulativeDTO;
import life.wpx1997.community.dto.QuestionCumulativeDTO;
import life.wpx1997.community.model.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    /**
     *
     * cumulativeCommentCount by 累计问题回复数
     *
     * @author: 不会飞的小鹏
     * @date: 2020/7/24 23:42
     * @param question
     * @return: void
     */
    public void cumulativeCommentCount(Question question) {
        cumulativeCache.cumulativeQuestionCommentCount(question.getId(),question.getCommentCount());
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
