package life.wpx1997.community.controller;

import life.wpx1997.community.dto.CommentCreateDTO;
import life.wpx1997.community.dto.CommentDTO;
import life.wpx1997.community.dto.ResultDTO;
import life.wpx1997.community.enums.CommentTypeEnum;
import life.wpx1997.community.exception.CustomizeErrorCode;
import life.wpx1997.community.model.User;
import life.wpx1997.community.service.CommentService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

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
    @GetMapping("/comment/delete/{id}")
    public Object deleteComment(@PathVariable(name = "id")Long id,
                                HttpServletRequest request){

        User user = (User) request.getSession().getAttribute("user");

        if (user == null){
            return ResultDTO.errorOf(CustomizeErrorCode.NOT_LOGIN);
        }else {
            Boolean isOneself = commentService.checkOneself(id,user.getId());
            if (isOneself == null){
                return ResultDTO.errorOf(CustomizeErrorCode.COMMENT_NOT_FOUND);
            }
            if (isOneself){
                commentService.deleteCommentById(id);
                return ResultDTO.okOf();
            }else {
                return ResultDTO.errorOf(CustomizeErrorCode.COMMENT_CREATOR_NOT_YOU);
            }
        }

    }

}
