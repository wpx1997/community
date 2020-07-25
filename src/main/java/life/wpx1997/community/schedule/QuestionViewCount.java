package life.wpx1997.community.schedule;

import life.wpx1997.community.cache.ViewCountCache;
import life.wpx1997.community.model.Question;
import life.wpx1997.community.service.QuestionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author 不会飞的小鹏
 * create on 2020/7/26 0:00
 * @Description QuestionViewCount is
 */
@Component
@Slf4j
public class QuestionViewCount {

    @Autowired
    private ViewCountCache viewCountCache;

    @Autowired
    private QuestionService questionService;

    @Scheduled(cron = "0 0 0/2 * * ?")
    public void cumulativeCommentCount(){

        log.info("QuestionViewCountSchedule start {}", new Date());
        Map<Long, Long> viewCountMap = viewCountCache.getViewCountMap();
        List<Question> questionList = new ArrayList<>();

        viewCountMap.forEach((id,viewCount) -> {
            Question question = new Question();
            question.setId(id);
            question.setViewCount(viewCount);
            questionList.add(question);
        });

        questionService.cumulativeViewCount(questionList);
        viewCountCache.clearViewCountMap();
        log.info("QuestionViewCountSchedule end {}", new Date());
    }

}
