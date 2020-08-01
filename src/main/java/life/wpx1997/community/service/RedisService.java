package life.wpx1997.community.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import life.wpx1997.community.constant.TypeConstant;
import life.wpx1997.community.dto.PaginationDTO;
import life.wpx1997.community.dto.QuestionMessageDTO;
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
import java.util.Optional;
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
        stringRedisTemplate.opsForValue().set("comment-parent=" + comment.getParentId() + "-type=" + comment.getType() + "-id=" + comment.getId(),JSON.toJSONString(comment));
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

        String key = "comment-parent=" + id + "-type=" + type + "*";
        List<Comment> commentList = getCommentListByKey(key);

        return commentList;
    }

    /**
     *
     * getCommentByParentIdList by 根据idList获取评论列表
     *
     * @author: 不会飞的小鹏
     * @date: 2020/8/1 22:54
     * @param idList
     * @param type
     * @return: List<Comment>
     */
    public List<Comment> getCommentByParentIdList(List<Long> idList, Byte type){

        List<Comment> commentList = idList.stream().map(id -> getCommentByParentId(id, type)).flatMap(List::stream).collect(Collectors.toList());

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

        List<Comment> commentList = ketSet.stream().map(key -> getCommentByKey(key)).collect(Collectors.toList());
        stringRedisTemplate.delete(ketSet);

        return commentList;
    }

    /**
     *
     * getCommentByKey by 根据key获取评论列表
     *
     * @author: 不会飞的小鹏
     * @date: 2020/8/1 22:55
     * @param key
     * @return: List<Comment>
     */
    public List<Comment> getCommentListByKey(String key){

        Set<String> keySet = stringRedisTemplate.keys(key);
        List<Comment> commentList = keySet.stream().map(k -> {
            Comment comment = getCommentByKey(k);
            return comment;
        }).collect(Collectors.toList());

        return commentList;
    }

    /**
     *
     * getCommentByKey by 根据key获取redis中的comment
     *
     * @author: 不会飞的小鹏
     * @date: 2020/8/2 0:08
     * @param key
     * @return: Comment
     */
    public Comment getCommentByKey(String key){

        String commentString = stringRedisTemplate.opsForValue().get(key);
        Comment comment = JSONArray.parseObject(commentString, Comment.class);

        return comment;
    }

    /**
     *
     * getCommentById by 根据id获取评论
     *
     * @author: 不会飞的小鹏
     * @date: 2020/8/1 22:55
     * @param id
     * @param parentId
     * @param type
     * @return: Comment
     */
    public Boolean deleteCommentFromRedis(Long id, Long parentId, Byte type,Long userId) {

        String key = "comment-parent=" + parentId + "-type=" + type + "-id=" + id;
        Comment comment = getCommentByKey(key);
        if (comment == null){
            return false;
        }else {
            if (userId.equals(comment.getCommentator())){
                comment.setIsDelete(TypeConstant.IS_DELETE_YES);
                insertComment(comment);
                return true;
            }else {
                return null;
            }

        }
    }

    /**
     *
     * addQuestion by 将question存入redis
     *
     * @author: 不会飞的小鹏
     * @date: 2020/8/1 18:23
     * @param questionMessageDTO
     * @return: void
     */
    public void addQuestion(QuestionMessageDTO questionMessageDTO) {
        stringRedisTemplate.opsForValue().set("questionById=" + questionMessageDTO.getId(),JSON.toJSONString(questionMessageDTO),2,TimeUnit.HOURS);
    }

    /**
     *
     * selectQuestionById by 获取question
     *
     * @author: 不会飞的小鹏
     * @date: 2020/8/1 18:15
     * @param id
     * @return: QuestionMessageDTO
     */
    public QuestionMessageDTO selectQuestionById(Long id) {

        String questionString = stringRedisTemplate.opsForValue().get("questionById=" + id);
        QuestionMessageDTO questionMessageDTO = JSONArray.parseObject(questionString, QuestionMessageDTO.class);

        return questionMessageDTO;
    }

    /**
     *
     * deleteQuestionById by 根据id删除redis中的对应数据
     *
     * @author: 不会飞的小鹏
     * @date: 2020/8/1 22:12
     * @param id
     * @return: void
     */
    public void deleteQuestionById(Long id){
        stringRedisTemplate.delete("questionById=" + id);
    }

}
