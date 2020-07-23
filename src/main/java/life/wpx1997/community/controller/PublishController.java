package life.wpx1997.community.controller;

import life.wpx1997.community.cache.TagCache;
import life.wpx1997.community.dto.QuestionPublishDTO;
import life.wpx1997.community.dto.ResultDTO;
import life.wpx1997.community.exception.CustomizeErrorCode;
import life.wpx1997.community.model.Question;
import life.wpx1997.community.model.QuestionPublishModel;
import life.wpx1997.community.model.User;
import life.wpx1997.community.service.QuestionService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * @author 不会飞的小鹏
 */
@Controller
public class PublishController {

    @Autowired
    private QuestionService questionService;

    @GetMapping("/publish/{id}")
    public String updateQuestion(@PathVariable(name = "id") Long id,
                                 HttpServletRequest request,
                                 Model model){

        User user = (User)request.getSession().getAttribute("user");
        if (user == null){
            return "redirect:/";
        }
        QuestionPublishModel question = questionService.getQuestionPublishModelById(id);
        if (question == null || !user.getId().equals(question.getCreator())){
            return "redirect:/";
        }
        model.addAttribute("question",question);
        model.addAttribute("tags", TagCache.get());

        return "publish";
    }

    @GetMapping("/publish")
    public String publish(Model model){

        QuestionPublishDTO questionPublishDTO = new QuestionPublishDTO();
        model.addAttribute("question",questionPublishDTO);
        model.addAttribute("tags", TagCache.get());

        return "publish";
    }

    @ResponseBody
    @RequestMapping(value = "/publish",method = RequestMethod.POST)
    public Object doPublish(@RequestBody @Valid QuestionPublishDTO questionPublishDTO,
                            HttpServletRequest request){

        String invalid = TagCache.filterInvalid(questionPublishDTO.getTag());
        if (StringUtils.isNotBlank(invalid)){
            return ResultDTO.errorOf(CustomizeErrorCode.ILLEGAL_TAG);
        }

        User user = (User) request.getSession().getAttribute("user");

        if (user == null){
            return ResultDTO.errorOf(CustomizeErrorCode.NOT_LOGIN);
        }

        Boolean update = questionService.createOrUpdate(questionPublishDTO, user.getId());

        if (!update){
            return ResultDTO.errorOf(CustomizeErrorCode.QUESTION_UPDATE_ERROR);
        }

        return ResultDTO.okOf();
    }
}
