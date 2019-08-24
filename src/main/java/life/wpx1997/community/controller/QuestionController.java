package life.wpx1997.community.controller;

import life.wpx1997.community.dto.CommentDTO;
import life.wpx1997.community.dto.PaginationDTO;
import life.wpx1997.community.dto.QuestionDTO;
import life.wpx1997.community.enums.CommentTypeEnum;
import life.wpx1997.community.model.User;
import life.wpx1997.community.service.CommentService;
import life.wpx1997.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @Autowired
    private CommentService commentService;

    @GetMapping("/question/tag/{tag}")
    public String questionByTag(@PathVariable(name = "tag") String tag,
                                @RequestParam(name = "page",defaultValue = "1") Integer page,
                                @RequestParam(name = "size", defaultValue = "5") Integer size,
                                Model model){

//        根据标签返回此页面问题的相似问题
        PaginationDTO paginationMoreQuestion = questionService.listByTag(tag,page,size);

        model.addAttribute("paginationMoreQuestion",paginationMoreQuestion);
//        问题显示形式judge为tag
        model.addAttribute("judge","tag");

        return "question";
    }

    @GetMapping("/question/{id}")
    public String questionById(@PathVariable(name = "id") Long id,
                               HttpServletRequest request,
                               Model model){

//        获取当前登录用户
        User user = (User)request.getSession().getAttribute("user");
//        根据页面传递的id查询问题的内容
        QuestionDTO thisQuestion = questionService.getById(id);
//        根据此问题id查询评论内容
        List<CommentDTO> comments = commentService.listByTargetId(id, CommentTypeEnum.QUESTION);
//        累计问题阅读数
        questionService.cumulativeView(id);
//        根据问题的标签查询相似的问题
        PaginationDTO likeQuestions = questionService.getByTag(thisQuestion.getTag());

//        如果用户未登录或不是此问题的作者，则状态state为disLogin
        if (user == null || user.getId() != thisQuestion.getCreator()){
            model.addAttribute("state","disLogin");
        }

//        如果用户已登录且是此问题的作者，则状态state为login且根据此问题作者id搜索并返回其最多十条问题的标题
        if (user != null && user.getId() == thisQuestion.getCreator()){
            PaginationDTO myQuestions = questionService.getByCreator(thisQuestion.getCreator());
            model.addAttribute("state","login");
            model.addAttribute("profile",myQuestions);
        }

//        增加此问题内容
        model.addAttribute("question",thisQuestion);
//        增加此问题的评论
        model.addAttribute("comments",comments);
//        增加此问题相似tag的问题的标题
        model.addAttribute("label",likeQuestions);

//        问题的显示显示judge为id
        model.addAttribute("judge","id");
        return "question";
    }
}
