package life.wpx1997.community.service;

import life.wpx1997.community.dto.PaginationDTO;
import life.wpx1997.community.dto.QuestionDTO;
import life.wpx1997.community.exception.CustomizeErrorCode;
import life.wpx1997.community.exception.CustomizeException;
import life.wpx1997.community.mapper.QuestionExpandMapper;
import life.wpx1997.community.mapper.QuestionMapper;
import life.wpx1997.community.mapper.UserMapper;
import life.wpx1997.community.model.Question;
import life.wpx1997.community.model.QuestionExample;
import life.wpx1997.community.model.User;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuestionService {

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private QuestionExpandMapper questionExpandMapper;

//    查询数据库中所有问题并进行分页和返回
    public PaginationDTO list(Integer page, Integer size) {

        PaginationDTO paginationDTO = new PaginationDTO();
        Integer totalCount = (int)questionMapper.countByExample(new QuestionExample());
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

        QuestionExample questionExample = new QuestionExample();
        questionExample.setOrderByClause("gmt_create desc");
        List<Question> questions = questionMapper.selectByExampleWithBLOBsWithRowbounds(questionExample,new RowBounds(offset,size));
        List<QuestionDTO> questionDTOList = new ArrayList<>();

        for (Question question : questions){
            User user = userMapper.selectByPrimaryKey(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question,questionDTO);
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }

        paginationDTO.setQuestions(questionDTOList);

        return paginationDTO;
    }

//    根据用户id查询其所有问题并进行分页和返回
    public PaginationDTO list(Long userId, Integer page, Integer size) {
        PaginationDTO paginationprofileDTO = new PaginationDTO();
        QuestionExample example = new QuestionExample();
        example.createCriteria().andCreatorEqualTo(userId);
        Integer totalCount = (int)questionMapper.countByExample(example);
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

        QuestionExample questionExample = new QuestionExample();
        questionExample.createCriteria().andCreatorEqualTo(userId);
        questionExample.setOrderByClause("gmt_create desc");
        List<Question> questions = questionMapper.selectByExampleWithBLOBsWithRowbounds(questionExample,new RowBounds(offset,size));
        List<QuestionDTO> questionDTOList = new ArrayList<>();

        for (Question question : questions){
            User user = userMapper.selectByPrimaryKey(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question,questionDTO);
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }

        paginationprofileDTO.setQuestions(questionDTOList);

        return paginationprofileDTO;
    }

//    根据页面传递tag查询相关问题并进行分页和返回
    public PaginationDTO listByTag(String tag, Integer page, Integer size) {
        PaginationDTO paginationmorelikeDTO = new PaginationDTO();

        String[] tags = StringUtils.split(tag,"，");
        String regexpTag = Arrays.stream(tags).collect(Collectors.joining("|"));
        Question countQuestion = new Question();
        countQuestion.setTag(regexpTag);
        Integer totalCount = (int)questionExpandMapper.countByTag(countQuestion);
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

        paginationmorelikeDTO.setPaination(totalPage,page);
        Integer offset = size*(page-1);

        PaginationDTO paginationDTO = new PaginationDTO();
        paginationDTO.setTag(regexpTag);
        paginationDTO.setOffset(offset);
        paginationDTO.setSize(size);

        List<Question> questions = questionExpandMapper.selectByTagWithPage(paginationDTO);
        List<QuestionDTO> questionDTOList = new ArrayList<>();

        for (Question question : questions){
            User user = userMapper.selectByPrimaryKey(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question,questionDTO);
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }

        paginationmorelikeDTO.setQuestions(questionDTOList);

        return paginationmorelikeDTO;
    }


    //    根据页面传递id查询问题的内容
    public QuestionDTO getById(Long id) {
        Question question = questionMapper.selectByPrimaryKey(id);
        if (question == null){
            throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
        }
        QuestionDTO questionDTO = new QuestionDTO();
        BeanUtils.copyProperties(question,questionDTO);
        User user = userMapper.selectByPrimaryKey(question.getCreator());
        questionDTO.setUser(user);
        return questionDTO;
    }


//    编辑问题点击发布后判断是更新已有问题还是直接发布新问题
    public void createOrUpdate(Question question) {
        if (question.getId() == null){
//            创建新问题
            question.setCommentCount(0L);
            question.setViewCount(0L);
            question.setLikeCount(0L);
            question.setGmtCreate(System.currentTimeMillis());
            question.setGmtModified(question.getGmtCreate());
            questionMapper.insert(question);
        }else {
//            更新已有问题

            Question updateQuestion = new Question();
            updateQuestion.setGmtModified(System.currentTimeMillis());
            updateQuestion.setTitle(question.getTitle());
            updateQuestion.setDescription(question.getDescription());
            updateQuestion.setTag(question.getTag());

            QuestionExample example = new QuestionExample();
            example.createCriteria().andIdEqualTo(question.getId());
            int update = questionMapper.updateByExampleSelective(updateQuestion,example);
            if (update != 1){
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
        }
    }

//    若当前用户为展示问题的作者，则根据问题creator查询并返回其前十条问题
    public PaginationDTO getByCreator(Long creator) {

        PaginationDTO likeQuestions = new PaginationDTO();

        QuestionExample questionExample = new QuestionExample();
        questionExample.createCriteria().andCreatorEqualTo(creator);
        List<Question> questions = questionMapper.selectByExample(questionExample);

        List<QuestionDTO> questionDTOList = new ArrayList<>();
        if(questions.size() >= 10){
            for (int i=0;i<10;i++){
                QuestionDTO questionDTO = new QuestionDTO();
                BeanUtils.copyProperties(questions.get(i),questionDTO);
                questionDTOList.add(questionDTO);
            }
        }else {
            for (Question question : questions){
                QuestionDTO questionDTO = new QuestionDTO();
                BeanUtils.copyProperties(question,questionDTO);
                questionDTOList.add(questionDTO);
            }
        }

        likeQuestions.setQuestions(questionDTOList);

        return likeQuestions;

    }

//    点击问题时累计问题的阅读数
    public void cumulativeView(Long id) {
        Question question = new Question();
        question.setId(id);
        question.setViewCount(1L);
        questionExpandMapper.cumulativeView(question);
    }
//    根据页面展示问题的tag查询相关问题并返回前十条问题
    public List<QuestionDTO> getByTag(QuestionDTO tagDTO) {
        String[] tags = StringUtils.split(tagDTO.getTag(),"，");
        String regexpTag = Arrays.stream(tags).collect(Collectors.joining("|"));
        Question question = new Question();
        question.setId(tagDTO.getId());
        question.setTag(regexpTag);
        List<Question> questions = questionExpandMapper.selectByTag(question);

        List<QuestionDTO> questionDTOS = questions.stream().map(q -> {
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(q,questionDTO);
            return questionDTO;
        }).collect(Collectors.toList());

        return questionDTOS;
    }
}
