package life.wpx1997.community.controller;

import life.wpx1997.community.cache.HotTagCache;
import life.wpx1997.community.dto.HotTagDTO;
import life.wpx1997.community.dto.PaginationDTO;
import life.wpx1997.community.model.QuestionShowModel;
import life.wpx1997.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author 不会飞的小鹏
 */
@Controller
public class IndexController {

    @Autowired
    private QuestionService questionService;

    @Autowired
    private HotTagCache hotTagCache;

    @GetMapping("/")
    public String index(Model model,
                        @RequestParam(name = "page",defaultValue = "1") Integer page){

        PaginationDTO<QuestionShowModel> paginationDTO = questionService.selectIndexQuestionList(page);
        List<HotTagDTO> hotTags = hotTagCache.getHots();
        model.addAttribute("indexPagination",paginationDTO);
        model.addAttribute("hotTags",hotTags);

        return "index";
    }

    @GetMapping("/search")
    public String searchIndex(Model model,
                              @RequestParam(name = "page",defaultValue = "1") Integer page,
                              @RequestParam(name = "search",required = false)String search){

        PaginationDTO<QuestionShowModel> paginationDTO = questionService.selectQuestionListBySearchWithPage(search,page);
        if (paginationDTO == null){
            model.addAttribute("tips","搜索内容不存在");
        }
        List<HotTagDTO> hotTags = hotTagCache.getHots();

        model.addAttribute("searchPagination",paginationDTO);
        model.addAttribute("hotTags",hotTags);
        model.addAttribute("search",search);

        return "search";
    }

}
