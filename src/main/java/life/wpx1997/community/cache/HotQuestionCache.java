package life.wpx1997.community.cache;

import life.wpx1997.community.dto.QuestionShowDTO;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author 不会飞的小鹏
 * create on 2020/7/25 22:03
 * @Description HotQuestionCache is
 */
@Component
public class HotQuestionCache {

    /**
     * @author: 不会飞的小鹏
     * @date: 2020/8/7 12:51
     * @description: HotQuestionCache's 热榜问题列表
     */
    private List<QuestionShowDTO> hotQuestionList;

    /**
     *
     * updateHotQuestionList by 更新热榜问题
     *
     * @author: 不会飞的小鹏
     * @date: 2020/8/7 12:51
     * @param hotQuestionList
     * @return: void
     */
    public void updateHotQuestionList(List<QuestionShowDTO> hotQuestionList){
        this.hotQuestionList = hotQuestionList;
    }

    /**
     *
     * getHotQuestionList by 获取热榜问题
     *
     * @author: 不会飞的小鹏
     * @date: 2020/8/7 12:52
     * @param
     * @return: List<QuestionShowDTO>
     */
    public List<QuestionShowDTO> getHotQuestionList(){
        return this.hotQuestionList;
    }

}
