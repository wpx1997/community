package life.wpx1997.community.controller;

import life.wpx1997.community.cache.HotQuestionCache;
import life.wpx1997.community.cache.HotTagCache;
import life.wpx1997.community.dto.HotTagDTO;
import life.wpx1997.community.dto.PaginationDTO;
import life.wpx1997.community.dto.QuestionShowDTO;
import life.wpx1997.community.service.CacheService;
import life.wpx1997.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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

    @Autowired
    private CacheService cacheService;

    @GetMapping("/")
    public String index(Model model){

        List<QuestionShowDTO> hotQuestionList = cacheService.getHotQuestion();
        List<HotTagDTO> hotTagList = hotTagCache.getHotTagDTOList();
        model.addAttribute("hotQuestionList",hotQuestionList);
        model.addAttribute("hotTagList",hotTagList);

        return "index";
    }

    @GetMapping("/search")
    public String searchIndex(Model model,
                              @RequestParam(name = "page",defaultValue = "1") Integer page,
                              @RequestParam(name = "search",required = false)String search){

        PaginationDTO<QuestionShowDTO> paginationDTO = questionService.selectQuestionListBySearchWithPage(search,page);
        if (paginationDTO == null){
            model.addAttribute("tips","搜索内容不存在");
            List<QuestionShowDTO> hotQuestionList = cacheService.getHotQuestion();
            model.addAttribute("hotQuestionList",hotQuestionList);
        }else {
            model.addAttribute("tips",null);
            model.addAttribute("searchPagination",paginationDTO);
            model.addAttribute("search",search);
        }
        List<HotTagDTO> hotTagList = hotTagCache.getHotTagDTOList();
        model.addAttribute("hotTagList",hotTagList);

        return "search";
    }

}
