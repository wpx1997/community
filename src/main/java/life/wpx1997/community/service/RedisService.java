package life.wpx1997.community.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import life.wpx1997.community.dto.PaginationDTO;
import life.wpx1997.community.dto.QuestionShowDTO;
import life.wpx1997.community.model.Comment;
import life.wpx1997.community.model.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author 不会飞的小鹏
 * create on 2020/7/1 23:09
 * @Description RedisService is
 */
@Service
public class RedisService {

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     *
     * addQuestionListByIndexPage by 将分页处理的问题列表存入redis
     *
     * @author: 不会飞的小鹏
     * @date: 2020/7/29 23:09
     * @param paginationDTO
     * @param page
     * @return: void
     */
    public void addQuestionListByIndexPage(PaginationDTO<QuestionShowDTO> paginationDTO, Integer page) {

        redisTemplate.opsForValue().set("indexQuestionListByPage=" + page, JSON.toJSONString(paginationDTO),2, TimeUnit.HOURS);

    }

    /**
     *
     * getIndexQuestionListByPage by 从redis中取出经过分页处理的问题列表
     *
     * @author: 不会飞的小鹏
     * @date: 2020/7/29 23:09
     * @param page
     * @return: PaginationDTO<QuestionShowDTO>
     */
    public PaginationDTO<QuestionShowDTO> getIndexQuestionListByPage(Integer page) {

        String questionListString = (String) redisTemplate.opsForValue().get("indexQuestionListByPage=" + page);
        PaginationDTO<QuestionShowDTO> paginationDTO = JSONArray.parseObject(questionListString,PaginationDTO.class);

        return paginationDTO;
    }

    /**
     *
     * insertComment by 将新增的评论放入redis
     *
     * @author: 不会飞的小鹏
     * @date: 2020/7/29 23:10
     * @param comment
     * @return: void
     */
    public void insertComment(Comment comment) {
        redisTemplate.opsForValue().set("comment-parent=" + comment.getParentId() + "-type=" + comment.getType() + "-id=" + comment.getId(),JSON.toJSONString(comment),3,TimeUnit.HOURS);
    }

    /**
     *
     * getCommentByParentId by 根据parentId和type获取批量列表
     *
     * @author: 不会飞的小鹏
     * @date: 2020/7/29 23:44
     * @param id
     * @param type
     * @return: List<Comment>
     */
    public List<Comment> getCommentByParentId(Long id,Byte type){

        Set<String> keySet = redisTemplate.keys("comment-parent=" + id + "-type=" + type + "*");
        List<Comment> commentList = keySet.stream().map(key -> {
            String commentString = (String) redisTemplate.opsForValue().get(key);
            Comment comment = JSONArray.parseObject(commentString, Comment.class);
            return comment;
        }).collect(Collectors.toList());

        return commentList;
    }

}
