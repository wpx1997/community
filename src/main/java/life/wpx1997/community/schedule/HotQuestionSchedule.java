package life.wpx1997.community.schedule;

import life.wpx1997.community.cache.HotQuestionCache;
import life.wpx1997.community.dto.QuestionShowDTO;
import life.wpx1997.community.model.Question;
import life.wpx1997.community.service.QuestionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author 不会飞的小鹏
 * create on 2020/7/25 16:20
 * @Description HotQuestion is
 */
@Component
@Slf4j
public class HotQuestionSchedule {

    @Autowired
    private QuestionService questionService;

    @Autowired
    private HotQuestionCache hotQuestionCache;

    @Scheduled(cron = "0 3 0/2 * * ?")
    public void hotQuestionSchedule(){

        log.info("hotQuestionSchedule start {}", new Date());
        // 从数据中获取所有问题（有待改进）
        List<Question> questionList = questionService.selectAllQuestionShowModelList();
        // 根据权重获取热度最高的十条问题
        List<Question> hotQuestionList = questionList.stream()
                .sorted(Comparator.comparingLong((Question question) -> {
                    Long exponent =  (question.getCommentCount() * 20) + (question.getLikeCount() * 10) + question.getViewCount();
                    return exponent;
                }).reversed()).limit(10).collect(Collectors.toList());

        List<QuestionShowDTO> questionShowDTOList = questionService.questionSetCreatorName(hotQuestionList);
        // 获取热榜问题id
        List<Long> idList = hotQuestionList.stream().map(Question::getId).collect(Collectors.toList());
        // 将热榜问题更新到redis
        questionService.updateHotQuestionToRedis(idList);

        hotQuestionCache.updateHotQuestionList(questionShowDTOList);
        log.info("hotQuestionSchedule stop {}", new Date());
    }

}
