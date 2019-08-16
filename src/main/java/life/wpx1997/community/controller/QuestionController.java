package life.wpx1997.community.controller;

import life.wpx1997.community.dto.PaginationDTO;
import life.wpx1997.community.dto.QuestionDTO;
import life.wpx1997.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @GetMapping("/question/tag/{tag}")
    public String questionByTag(@PathVariable(name = "tag") String tag,
                                @RequestParam(name = "page",defaultValue = "1") Integer page,
                                @RequestParam(name = "size", defaultValue = "5") Integer size,
                                Model model){

        PaginationDTO paginationMoreQuestion = questionService.listByTag(tag,page,size);

        model.addAttribute("paginationMoreQuestion",paginationMoreQuestion);
        model.addAttribute("judge","yes");

        return "question";
    }



    @GetMapping("/question/{id}")
    public String questionById(@PathVariable(name = "id") Integer id,
                               Model model){

        QuestionDTO thisQuestion = questionService.getById(id);
        PaginationDTO likeQuestions = questionService.getByTag(thisQuestion.getTag());

        model.addAttribute("question",thisQuestion);
        model.addAttribute("label",likeQuestions);
        model.addAttribute("judge","no");
        return "question";
    }
}
