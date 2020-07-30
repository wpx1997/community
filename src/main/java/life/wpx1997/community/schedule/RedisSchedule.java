package life.wpx1997.community.schedule;

import life.wpx1997.community.model.Comment;
import life.wpx1997.community.service.CommentService;
import life.wpx1997.community.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;

/**
 * @author 不会飞的小鹏
 * create on 2020/7/31 0:35
 * @Description RedisSchedule is
 */
public class RedisSchedule {

    @Autowired
    private RedisService redisService;

    @Autowired
    private CommentService commentService;

    @Scheduled(cron = "0 1 0/2 * * ?")
    public void insertCommentList(){

        List<Comment> commentList = redisService.getCommentList();
        commentService.insertCommentList(commentList);

    }

}
