package life.wpx1997.community.mapper;

import life.wpx1997.community.model.Question;

public interface QuestionExpandMapper {
    int cumulativeView(Question record);
}