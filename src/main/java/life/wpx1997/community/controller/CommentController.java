package life.wpx1997.community.controller;

import life.wpx1997.community.dto.CommentDTO;
import life.wpx1997.community.dto.ResultDTO;
import life.wpx1997.community.exception.CustomizeErrorCode;
import life.wpx1997.community.mapper.CommentMapper;
import life.wpx1997.community.model.Comment;
import life.wpx1997.community.model.User;
import life.wpx1997.community.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Controller
public class CommentController {

    @Autowired
    private CommentService commentService;

    @ResponseBody
    @RequestMapping(value = "/comment",method = RequestMethod.POST)
    public Object post(@RequestBody CommentDTO commentDTO,
                       HttpServletRequest request){

        User user = (User) request.getSession().getAttribute("user");
//        如果用户未登录
        if (user == null){
            return ResultDTO.errorOf(CustomizeErrorCode.NOT_LOGIN);
        }

        Comment comment = new Comment();
        comment.setQuestionId(commentDTO.getQuestionId());
        comment.setCommentator(user.getId());
        comment.setContent(commentDTO.getContent());
        comment.setType(commentDTO.getType());
        comment.setGmtCreate(System.currentTimeMillis());
        comment.setGmtModified(System.currentTimeMillis());
        comment.setLikeCount(0L);
//        进行问题回复或评论或反复
        commentService.insert(comment);
        return ResultDTO.okOf();
    }

}
