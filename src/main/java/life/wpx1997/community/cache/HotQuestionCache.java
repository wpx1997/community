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

    private List<QuestionShowDTO> hotQuestionList;

    public void updateHotQuestionList(List<QuestionShowDTO> hotQuestionList){
        this.hotQuestionList = hotQuestionList;
    }

    public List<QuestionShowDTO> getHotQuestionList(){
        return this.hotQuestionList;
    }

}
