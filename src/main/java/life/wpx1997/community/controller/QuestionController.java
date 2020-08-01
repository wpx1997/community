package life.wpx1997.community.controller;

import life.wpx1997.community.cache.HotTagCache;
import life.wpx1997.community.dto.*;
import life.wpx1997.community.exception.CustomizeErrorCode;
import life.wpx1997.community.model.User;
import life.wpx1997.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author 不会飞的小鹏
 */
@Controller
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @Autowired
    private HotTagCache hotTagCache;

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
        QuestionMessageDTO thisQuestion = questionService.selectQuestion(id,userId);

        if (thisQuestion == null){

            model.addAttribute("message","问题不存在或已被删除");

            return "error";
        }

        // 累计问题阅读数
        questionService.cumulativeView(id);

        // 如果用户未登录或不是此问题的作者，则状态state为notMine
        if (userId.equals(thisQuestion.getCreator())) {
            model.addAttribute("state", "mine");
        }else {
            model.addAttribute("state", "notMine");
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
        PaginationDTO<QuestionShowDTO> paginationDTO = questionService.selectQuestionListByTag(tag, page);
        List<HotTagDTO> hotTagList = hotTagCache.getHotTagDTOList();
        model.addAttribute("tagPagination", paginationDTO);
        model.addAttribute("hotTagList",hotTagList);
        model.addAttribute("tag",tag);

        return "tag";
    }

    @ResponseBody
    @PostMapping("/question/delete/")
    public Object deleteQuestionById(@RequestBody Long id,
                                     HttpServletRequest request){

        User user = (User) request.getSession().getAttribute("user");

        // 未登录
        if (user == null){
            return ResultDTO.errorOf(CustomizeErrorCode.NOT_LOGIN);
        }else {
            Boolean isOneself = questionService.checkOneself(id,user.getId());

            // 问题已不存在
            if (isOneself == null){
                return ResultDTO.errorOf(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
            // 作者本人
            if (isOneself){
                questionService.deleteQuestionById(id);
                return ResultDTO.okOf();
            }else { // 非作者本人
                return ResultDTO.errorOf(CustomizeErrorCode.QUESTION_CREATOR_NOT_YOU);
            }
        }

    }


}
