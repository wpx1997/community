package life.wpx1997.community.controller;

import life.wpx1997.community.dto.PaginationDTO;
import life.wpx1997.community.dto.QuestionMessageDTO;
import life.wpx1997.community.dto.ResultDTO;
import life.wpx1997.community.exception.CustomizeErrorCode;
import life.wpx1997.community.model.QuestionShowModel;
import life.wpx1997.community.model.User;
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
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @GetMapping("/question/{id}")
    public String questionById(@PathVariable(name = "id") Long id,
                               HttpServletRequest request,
                               Model model) {

        // 获取当前登录用户
        User user = (User) request.getSession().getAttribute("user");
        Long userId;
        if (user == null){
            userId = 0L;
        }else {
            userId = user.getId();
        }

        // 根据页面传递的id查询问题的内容
        QuestionMessageDTO thisQuestion = questionService.selectQuestionByQuestionId(id);

        if (thisQuestion == null){

            model.addAttribute("message","问题不存在或已被删除");

            return "error";
        }

        // 累计问题阅读数
        questionService.cumulativeView(id);

        // 如果用户未登录或不是此问题的作者，则状态state为disLogin
        if (user == null || userId.equals(thisQuestion.getCreator())) {
            model.addAttribute("state", "notMine");
        }else {
            model.addAttribute("state", "mine");
        }

        // 增加此问题内容
        model.addAttribute("question", thisQuestion);

        return "question";
    }

    @GetMapping("/tag/{tag}")
    public String questionByTag(@PathVariable(name = "tag") String tag,
                                @RequestParam(name = "page", defaultValue = "1") Integer page,
                                Model model) {

        // 根据标签返回此页面问题的相似问题
        PaginationDTO<QuestionShowModel> paginationDTO = questionService.selectQuestionListByTag(tag, page);
        model.addAttribute("tagPagination", paginationDTO);
        model.addAttribute("tag",tag);

        return "tag";
    }

    @GetMapping("/question/delete/{id}")
    public Object deleteQuestionById(@PathVariable(name = "id")Long id,
                                     HttpServletRequest request){

        User user = (User) request.getSession().getAttribute("user");
        if (user == null){
            return ResultDTO.errorOf(CustomizeErrorCode.NOT_LOGIN);
        }else {
            Boolean isOneself = questionService.checkOneself(id,user.getId());
            if (isOneself == null){
                return ResultDTO.errorOf(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
            if (isOneself){
                questionService.deleteQuestionById(id);
                return ResultDTO.okOf();
            }else {
                return ResultDTO.errorOf(CustomizeErrorCode.QUESTION_CREATOR_NOT_YOU);
            }
        }

    }


}
