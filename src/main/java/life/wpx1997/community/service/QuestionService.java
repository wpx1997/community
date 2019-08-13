package life.wpx1997.community.service;

import life.wpx1997.community.dto.PaginationDTO;
import life.wpx1997.community.dto.QuestionDTO;
import life.wpx1997.community.mapper.QuestionMapper;
import life.wpx1997.community.mapper.UserMapper;
import life.wpx1997.community.model.Question;
import life.wpx1997.community.model.User;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class QuestionService {

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private UserMapper userMapper;

    public PaginationDTO list(Integer page, Integer size) {

        PaginationDTO paginationDTO = new PaginationDTO();
        Integer totalCount = questionMapper.count();
        Integer totalPage;

        if (totalCount % size == 0){
            totalPage = totalCount / size;
        }else {
            totalPage = totalCount / size + 1;
        }

        if (page<1){
            page = 1;
        }
        if (page>totalPage){
            page = totalPage;
        }

        paginationDTO.setPaination(totalPage,page);
        Integer offset = size*(page-1);

        List<Question> questions = questionMapper.list(offset,size);
        List<QuestionDTO> questionDTOList = new ArrayList<>();

        for (Question question : questions){
            User user = userMapper.fingById(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question,questionDTO);
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }

        paginationDTO.setQuestions(questionDTOList);

        return paginationDTO;
    }

    public PaginationDTO list(String userAccountId, Integer page, Integer size) {
        PaginationDTO paginationprofileDTO = new PaginationDTO();
        Integer totalCount = questionMapper.countByUserAccountId(userAccountId);
        Integer totalPage;

        if (totalCount % size == 0){
            totalPage = totalCount / size;
        }else {
            totalPage = totalCount / size + 1;
        }

        if (page<1){
            page = 1;
        }
        if (page>totalPage){
            page = totalPage;
        }

        paginationprofileDTO.setPaination(totalPage,page);
        Integer offset = size*(page-1);

        List<Question> questions = questionMapper.listByUserAccountId(userAccountId,offset,size);
        List<QuestionDTO> questionDTOList = new ArrayList<>();

        for (Question question : questions){
            User user = userMapper.fingById(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question,questionDTO);
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }

        paginationprofileDTO.setQuestions(questionDTOList);

        return paginationprofileDTO;
    }
}
