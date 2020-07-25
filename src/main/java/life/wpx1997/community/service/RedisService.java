package life.wpx1997.community.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import life.wpx1997.community.dto.PaginationDTO;
import life.wpx1997.community.dto.QuestionShowDTO;
import life.wpx1997.community.model.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * @author 不会飞的小鹏
 * create on 2020/7/1 23:09
 * @Description RedisService is
 */
@Service
public class RedisService {

    @Autowired
    private RedisTemplate redisTemplate;

    public void addQuestionListByIndexPage(PaginationDTO<QuestionShowDTO> paginationDTO, Integer page) {

        redisTemplate.opsForValue().set("indexQuestionListByPage=" + page, JSON.toJSONString(paginationDTO),2, TimeUnit.HOURS);

    }

    public PaginationDTO<QuestionShowDTO> getIndexQuestionListByPage(Integer page) {

        String questionListString = (String) redisTemplate.opsForValue().get("indexQuestionListByPage=" + page);
        PaginationDTO<QuestionShowDTO> paginationDTO = (PaginationDTO<QuestionShowDTO>) JSONArray.parseObject(questionListString,PaginationDTO.class);

        return paginationDTO;
    }

}
