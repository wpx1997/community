package life.wpx1997.community.controller;

import life.wpx1997.community.dto.MessageTagDTO;
import life.wpx1997.community.mapper.QuestionMapper;
import life.wpx1997.community.mapper.UserMapper;
import life.wpx1997.community.model.QuestionExample;
import life.wpx1997.community.model.User;
import life.wpx1997.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * created by 小case on 2019/9/7 14:15
 */
@Controller
public class UserMessageController {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private QuestionService questionService;

    @GetMapping("/message/{id}")
    public String userMessage(@PathVariable(name = "id")Long id,
                              HttpServletRequest request,
                              Model model){

//        如果当前用户未登录或已登录但id不匹配
        User currentUser = (User)request.getSession().getAttribute("user");
        if (currentUser == null || currentUser.getId() != id){
            model.addAttribute("whoIs","isOthers");
        }

        if (currentUser != null && currentUser.getId() == id){
            model.addAttribute("whoIs","isMe");
        }

        User user = userMapper.selectByPrimaryKey(id);
        QuestionExample example = new QuestionExample();
        example.createCriteria().andCreatorEqualTo(user.getId());
        long countQuestion = questionMapper.countByExample(example);
        List<MessageTagDTO> messageTagDTOS = questionService.userTag(user.getId());
        model.addAttribute("userMessage",user);
        model.addAttribute("countQuestion",countQuestion);
        model.addAttribute("messageTagDTOS",messageTagDTOS);

        return "message";
    }

}
