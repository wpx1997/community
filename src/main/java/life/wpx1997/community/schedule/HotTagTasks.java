package life.wpx1997.community.schedule;

import life.wpx1997.community.cache.HotTagCache;
import life.wpx1997.community.mapper.QuestionMapper;
import life.wpx1997.community.model.Question;
import life.wpx1997.community.model.QuestionExample;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * created by 小case on 2019/9/1 22:00
 */
@Component
@Slf4j
public class HotTagTasks {

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private HotTagCache hotTagCache;

    @Scheduled(fixedRate = 10800000)
    public void hotTagSchedule() {

        log.info("hotTagSchedule start {}", new Date());
        List<Question> questions;
        Map<String, Integer> priorities = new HashMap<>();
        questions = questionMapper.selectByExample(new QuestionExample());

        for (Question question : questions) {
            String[] tags = StringUtils.split(question.getTag(), "，");
            for (String tag : tags) {
                Integer priority = priorities.get(tag);
                if (priority != null){
                    priorities.put(tag, (int) (priority + 5 +  question.getCommentCount() + question.getLikeCount()*5 + question.getViewCount()/100));
                }else {
                    priorities.put(tag, (int) (5 + question.getCommentCount() + question.getLikeCount()*5 + question.getViewCount()/100));
                }
            }
        }

        hotTagCache.updateTags(priorities);
        log.info("hotTagSchedule stop {}", new Date());
    }

}
