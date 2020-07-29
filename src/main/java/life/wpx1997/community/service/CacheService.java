package life.wpx1997.community.service;

import life.wpx1997.community.cache.CumulativeCache;
import life.wpx1997.community.model.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

}
