package life.wpx1997.community.controller;

import life.wpx1997.community.dto.NotificationDTO;
import life.wpx1997.community.dto.PaginationDTO;
import life.wpx1997.community.enums.ActionTypeEnum;
import life.wpx1997.community.model.QuestionShowModel;
import life.wpx1997.community.model.User;
import life.wpx1997.community.service.NotificationService;
import life.wpx1997.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import javax.servlet.http.HttpServletRequest;

/**
 * @author 不会飞的小鹏
 */
@Controller
public class ProfileController {

    @Autowired
    private QuestionService questionService;

    @Autowired
    private NotificationService notificationService;

    @GetMapping("/profile/{action}")
    public String profile(@PathVariable(name = "action") String action,
                          HttpServletRequest request,
                          Model model,
                          @RequestParam(name = "page",defaultValue = "1") Integer page){

        User user = (User) request.getSession().getAttribute("user");

        if (user == null){
            return "redirect:/";
        }

        if (ActionTypeEnum.QUESTIONS.getType().equals(action)){

            model.addAttribute("section","questions");
            model.addAttribute("name","我的提问");
            PaginationDTO<QuestionShowModel> paginationDTO = questionService.selectQuestionListByUserIdWithPage(user.getId(), page);
            model.addAttribute("questionPagination",paginationDTO);
        }else if (ActionTypeEnum.REPLIES.getType().equals(action)){
            PaginationDTO<NotificationDTO> paginationDTO = notificationService.selectNotificationList(user.getId(),page);
            model.addAttribute("notificationPagination",paginationDTO);
            model.addAttribute("section","replies");
            model.addAttribute("name","最新回复");
        }

        return "profile";
    }
}
