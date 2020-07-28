package life.wpx1997.community.controller;

import life.wpx1997.community.cache.HotQuestionCache;
import life.wpx1997.community.cache.HotTagCache;
import life.wpx1997.community.dto.HotTagDTO;
import life.wpx1997.community.dto.MessageTagDTO;
import life.wpx1997.community.dto.PaginationDTO;
import life.wpx1997.community.dto.QuestionShowDTO;
import life.wpx1997.community.mapper.QuestionMapper;
import life.wpx1997.community.model.User;
import life.wpx1997.community.service.QuestionService;
import life.wpx1997.community.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.websocket.server.PathParam;
import java.util.List;

/**
 * created by 小case on 2019/9/7 14:15
 * @author 不会飞的小鹏
 */
@Controller
public class UserMessageController {

    @Autowired
    private QuestionService questionService;

    @Autowired
    private UserService userService;

    @Autowired
    private HotQuestionCache hotQuestionCache;

    @Autowired
    private HotTagCache hotTagCache;

    @GetMapping("/message/user/{id}")
    public String userMessage(@PathVariable(name = "id")Long id,
                              HttpServletRequest request,
                              Model model){

//        如果当前用户未登录或已登录但id不匹配
        User currentUser = (User)request.getSession().getAttribute("user");
        if (currentUser == null || !currentUser.getId().equals(id)){
            model.addAttribute("whoIs","isOthers");
        }

        if (currentUser != null && currentUser.getId().equals(id)){
            model.addAttribute("whoIs","isMe");
        }

        User user = userService.selectUseById(id);
        Long questionCount = questionService.countByCreator(id);
        List<MessageTagDTO> messageTagDTOS = questionService.userTag(user.getId());
        model.addAttribute("userMessage",user);
        model.addAttribute("questionCount",questionCount);
        model.addAttribute("messageTagDTOS",messageTagDTOS);
        model.addAttribute("message","user");
        model.addAttribute("id",id);

        return "message";
    }

    @GetMapping("/message/questions/{id}")
    public String questionMessage(@PathVariable(name = "id")Long id,
                                  @RequestParam(name = "page",defaultValue = "1")Integer page,
                                  Model model){

        PaginationDTO<QuestionShowDTO> paginationDTO = questionService.selectQuestionListByUserIdWithPage(id, page);
        if (paginationDTO == null){
            model.addAttribute("tips","该作者未有创作");
            List<QuestionShowDTO> hotQuestionList = hotQuestionCache.getHotQuestionList();
            model.addAttribute("hotQuestionList",hotQuestionList);
        }else {
            model.addAttribute("tips",null);
            model.addAttribute("searchPagination",paginationDTO);
        }
        List<HotTagDTO> hotTagList = hotTagCache.getHotTagDTOList();
        model.addAttribute("hotTagList",hotTagList);
        model.addAttribute("message","questions");
        model.addAttribute("id",id);

        return "message";
    }

}
