package life.wpx1997.community.controller;

import life.wpx1997.community.dto.PaginationDTO;
import life.wpx1997.community.model.User;
import life.wpx1997.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import javax.servlet.http.HttpServletRequest;

@Controller
public class ProfileController {

    @Autowired
    private QuestionService questionService;

    @GetMapping("/profile/{action}")
    public String profile(@PathVariable(name = "action") String action,
                          HttpServletRequest request,
                          Model model,
                          @RequestParam(name = "page",defaultValue = "1") Integer page,
                          @RequestParam(name = "size", defaultValue = "5") Integer size){

        User user = (User) request.getSession().getAttribute("user");

        if (user == null){
            return "redirect:/";
        }

        if ("questions".equals(action)){
            model.addAttribute("section","questions");
            model.addAttribute("name","我的提问");
        }else if ("replies".equals(action)){
            model.addAttribute("section","replies");
            model.addAttribute("name","最新回复");
        }

        PaginationDTO paginationprofile = questionService.list(user.getAccountId(), page, size);
        model.addAttribute("paginationprofile",paginationprofile);
        return "profile";
    }
}
