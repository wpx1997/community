package life.wpx1997.community.controller;

import life.wpx1997.community.dto.CommentCreateDTO;
import life.wpx1997.community.dto.ResultDTO;
import life.wpx1997.community.exception.CustomizeErrorCode;
import life.wpx1997.community.model.Comment;
import life.wpx1997.community.model.User;
import life.wpx1997.community.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * @author 小case
 */
@Controller
public class CommentController {

    @Autowired
    private CommentService commentService;

    /**
     *
     * postComment 对用户回复进行插入操作
     *
     * @author: 小case
     * @date: 2020/6/16 8:47
     * @param commentCreateDTO
     * @param request
     * @return: Object
     */
    @ResponseBody
    @RequestMapping(value = "/comment",method = RequestMethod.POST)
    public Object postComment(@RequestBody @Valid CommentCreateDTO commentCreateDTO,
                       HttpServletRequest request){

        User user = (User) request.getSession().getAttribute("user");
//        如果用户未登录
        if (user == null){
            return ResultDTO.errorOf(CustomizeErrorCode.NOT_LOGIN);
        }

        if (commentCreateDTO == null){
            return ResultDTO.errorOf(CustomizeErrorCode.CONTENT_IS_EMPTY);
        }

        commentService.insertComment(commentCreateDTO,user);

        return ResultDTO.okOf();
    }

    @ResponseBody
    @PostMapping("/comment/delete")
    public Object deleteComment(@RequestBody Long id,
                                HttpServletRequest request){

        User user = (User) request.getSession().getAttribute("user");

        // 未登录
        if (user == null){
            return ResultDTO.errorOf(CustomizeErrorCode.NOT_LOGIN);
        }else {
            // 从数据库中查询评论
            Comment comment = commentService.selectCommentUpdateModelById(id);

            // 评论不存在
            if (comment == null){
                return ResultDTO.errorOf(CustomizeErrorCode.COMMENT_NOT_FOUND);
            }

            // 是否为评论作者
            if (user.getId().equals(comment.getCommentator())){
                commentService.deleteCommentById(comment);
                return ResultDTO.okOf();
            }else {
                return ResultDTO.errorOf(CustomizeErrorCode.COMMENT_CREATOR_NOT_YOU);
            }
        }

    }

}
