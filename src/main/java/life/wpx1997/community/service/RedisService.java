package life.wpx1997.community.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import life.wpx1997.community.dto.PaginationDTO;
import life.wpx1997.community.dto.QuestionShowDTO;
import life.wpx1997.community.model.Comment;
import life.wpx1997.community.model.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.ArrayList;
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
    private StringRedisTemplate stringRedisTemplate;

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

        stringRedisTemplate.opsForValue().set("indexQuestionListByPage=" + page, JSON.toJSONString(paginationDTO),2, TimeUnit.HOURS);

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

        String questionListString = stringRedisTemplate.opsForValue().get("indexQuestionListByPage=" + page);
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
        stringRedisTemplate.opsForList().rightPush("comment-parent=" + comment.getParentId() + "-type=" + comment.getType(),JSON.toJSONString(comment));
    }

    /**
     *
     * getCommentByParentId by 根据parentId和type获取评论列表
     *
     * @author: 不会飞的小鹏
     * @date: 2020/7/29 23:44
     * @param id
     * @param type
     * @return: List<Comment>
     */
    public List<Comment> getCommentByParentId(Long id,Byte type){

        List<String> commentListString = stringRedisTemplate.opsForList().range("comment-parent=" + id + "-type=" + type, 0, -1);
        List<Comment> commentList = stringToCommentList(commentListString);

        return commentList;
    }

    /**
     *
     * getCommentList by 获取缓存中的所有评论并情况缓存
     *
     * @author: 不会飞的小鹏
     * @date: 2020/7/31 1:34
     * @param
     * @return: List<Comment>
     */
    public List<Comment> getCommentList(){

        Set<String> ketSet = stringRedisTemplate.keys("comment-parent=*");
        List<String> commentListString = new ArrayList<>();

        ketSet.stream().forEach(key -> {
            List<String> commentString = stringRedisTemplate.opsForList().range(key, 0, -1);
            commentListString.addAll(commentString);
        });

        List<Comment> commentList = stringToCommentList(commentListString);
        stringRedisTemplate.delete(ketSet);

        return commentList;
    }

    /**
     *
     * stringToCommentList by 将JSON字符串列表转化为评论对象列表
     *
     * @author: 不会飞的小鹏
     * @date: 2020/7/31 1:35
     * @param commentListString
     * @return: List<Comment>
     */
    public List<Comment> stringToCommentList(List<String> commentListString){

        List<Comment> commentList = commentListString.stream().map(commentString -> {
            Comment comment = JSONArray.parseObject(commentString, Comment.class);
            return comment;
        }).collect(Collectors.toList());

        return commentList;
    }

    public void del(){
        Set<String> ketSet = stringRedisTemplate.keys("comment-parent=*");
        stringRedisTemplate.delete(ketSet);
    }

}
