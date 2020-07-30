package life.wpx1997.community.schedule;

import life.wpx1997.community.cache.CumulativeCache;
import life.wpx1997.community.dto.CommentCumulativeDTO;
import life.wpx1997.community.dto.QuestionCumulativeDTO;
import life.wpx1997.community.model.Comment;
import life.wpx1997.community.service.CommentService;
import life.wpx1997.community.service.QuestionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * @author 不会飞的小鹏
 * create on 2020/7/26 0:00
 * @Description QuestionViewCount is
 */
@Component
@Slf4j
public class CumulativeSchedule {

    @Autowired
    private CumulativeCache cumulativeCache;

    @Autowired
    private QuestionService questionService;

    @Autowired
    private CommentService commentService;

    @Scheduled(cron = "0 0 0/2 * * ?")
    public void cumulativeScheduled(){

        log.info("cumulativeScheduled start {}", new Date());

        // 从缓存中取出数据并清除缓存
        Map<Long, QuestionCumulativeDTO> questionCumulativeMap = cumulativeCache.getQuestionCumulativeMap();
        cumulativeCache.clearCumulativeCache();

        if (questionCumulativeMap.isEmpty()){
            log.info("问题缓存无数据更新", new Date());
        }else {
            List<QuestionCumulativeDTO> questionCumulativeList = new ArrayList<>(questionCumulativeMap.values());
            questionService.questionCumulative(questionCumulativeList);
        }

        Map<Long, CommentCumulativeDTO> commentCumulativeMap = cumulativeCache.getCommentCumulativeMap();
        if (commentCumulativeMap.isEmpty()){
            log.info("评论缓存无数据更新", new Date());
        }else {
            List<CommentCumulativeDTO> commentCumulativeList = new ArrayList<>(commentCumulativeMap.values());
            commentService.commentCumulative(commentCumulativeList);
        }

        log.info("cumulativeScheduled end {}", new Date());
    }

}
