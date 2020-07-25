package life.wpx1997.community.schedule;

import com.sun.xml.internal.fastinfoset.util.StringArray;
import life.wpx1997.community.cache.HotTagCache;
import life.wpx1997.community.model.Question;
import life.wpx1997.community.model.QuestionExample;
import life.wpx1997.community.service.QuestionService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * created on 2019/9/1 22:00
 * @author 不会飞的小鹏
 */
@Component
@Slf4j
public class HotTag {

    @Autowired
    private QuestionService questionService;

    @Autowired
    private HotTagCache hotTagCache;

    @Scheduled(cron = "0 1 0/2 * * ?")
    public void hotTagSchedule() {

        log.info("hotTagSchedule start {}", new Date());
        List<Question> questionList = questionService.selectAllQuestionTagList();

        Map<String,Long> priorities = new HashMap<>();
        questionList.stream().forEach(question -> {
            Arrays.stream(question.getTag().split("，")).forEach(tag -> {
                Long exponent = (question.getCommentCount() * 20) + (question.getLikeCount() * 10) + question.getViewCount();
                Long priority = priorities.get(tag);
                if (priority == null){
                    priorities.put(tag,exponent);
                }else {
                    priorities.put(tag,priority + exponent);
                }
            });
        });

        hotTagCache.updateTagList(priorities);
        log.info("hotTagSchedule stop {}", new Date());
    }

}
