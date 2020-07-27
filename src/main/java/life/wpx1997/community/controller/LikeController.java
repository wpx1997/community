package life.wpx1997.community.controller;

import life.wpx1997.community.dto.LikeDTO;
import life.wpx1997.community.dto.ResultDTO;
import life.wpx1997.community.exception.CustomizeErrorCode;
import life.wpx1997.community.model.User;
import life.wpx1997.community.service.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * @author 不会飞的小鹏
 * create on 2020/7/27 21:33
 * @Description LikeController is
 */
@Controller
public class LikeController {

    @Autowired
    private LikeService likeService;

    @ResponseBody
    @PostMapping("/like/addLike")
    public Object addLike(HttpServletRequest request,
                          @RequestBody @Valid LikeDTO likeDTO){

        User user = (User) request.getSession().getAttribute("user");
        if (user == null){
            return ResultDTO.errorOf(CustomizeErrorCode.NOT_LOGIN);
        }

        likeService.createOrUpdateLikeByParentIdWithUserId(likeDTO,user.getId());

        return ResultDTO.okOf();
    }

    @ResponseBody
    @PostMapping("/like/deleteLike")
    public Object deleteLike(HttpServletRequest request,
                             @RequestBody @Valid LikeDTO likeDTO){

        User user = (User) request.getSession().getAttribute("user");
        if (user == null){
            return ResultDTO.errorOf(CustomizeErrorCode.NOT_LOGIN);
        }

        Boolean delete = likeService.deleteLikeByParentIdWithUserId(likeDTO,user.getId());
        if (delete){
            return ResultDTO.okOf();
        }else {
            return ResultDTO.errorOf(CustomizeErrorCode.HAVE_NOT_LIKE);
        }

    }

}
