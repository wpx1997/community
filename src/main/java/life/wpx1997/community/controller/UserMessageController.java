package life.wpx1997.community.controller;

import life.wpx1997.community.dto.MessageTagDTO;
import life.wpx1997.community.mapper.QuestionMapper;
import life.wpx1997.community.mapper.UserMapper;
import life.wpx1997.community.model.QuestionExample;
import life.wpx1997.community.model.User;
import life.wpx1997.community.service.QuestionService;
import life.wpx1997.community.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * created by 小case on 2019/9/7 14:15
 * @author 不会飞的小鹏
 */
@Controller
public class UserMessageController {

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private QuestionService questionService;

    @Autowired
    private UserService userService;

    @GetMapping("/message/{id}")
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

        return "message";
    }

}
