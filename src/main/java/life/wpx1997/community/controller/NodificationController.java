package life.wpx1997.community.controller;

import life.wpx1997.community.dto.NotificationDTO;
import life.wpx1997.community.enums.NotificationTypeEnum;
import life.wpx1997.community.model.User;
import life.wpx1997.community.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;

/**
 * created by 小case on 2019/8/27 0:14
 */
@Controller
public class NodificationController {

    @Autowired
    private NotificationService notificationService;

    @GetMapping("/notification/{id}")
    public String notification(HttpServletRequest request,
                               @PathVariable(name = "id") Long id){

        User user = (User)request.getSession().getAttribute("user");
        if (user == null){
            return "redirect:/";
        }

        NotificationDTO notificationDTO = notificationService.read(id,user);

        if (NotificationTypeEnum.REPLY_COMMENT.getType() == notificationDTO.getType() || NotificationTypeEnum.REPLY_QUESTION.getType() == notificationDTO.getType()){
            return "redirect:/question/" + notificationDTO.getOuterId();
        }else {
            return "redirect:/";
        }

    }
}
