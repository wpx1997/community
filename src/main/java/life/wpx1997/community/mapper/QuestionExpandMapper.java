package life.wpx1997.community.mapper;

import life.wpx1997.community.dto.PaginationDTO;
import life.wpx1997.community.model.Question;
import java.util.List;

public interface QuestionExpandMapper {
    int cumulativeView(Question record);
    int cumulativeCommentCount(Question record);
    int countByTag(Question question);
    List<Question> selectByTag(Question question);
    List<Question> selectByTagWithPage(PaginationDTO paginationDTO);
}