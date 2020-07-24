package life.wpx1997.community.service;

import life.wpx1997.community.cache.MessageTagCache;
import life.wpx1997.community.dto.*;
import life.wpx1997.community.enums.CommentTypeEnum;
import life.wpx1997.community.exception.CustomizeErrorCode;
import life.wpx1997.community.exception.CustomizeException;
import life.wpx1997.community.mapper.QuestionExpandMapper;
import life.wpx1997.community.mapper.QuestionMapper;
import life.wpx1997.community.mapper.UserMapper;
import life.wpx1997.community.model.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author 不会飞的小鹏
 */
@Service
public class QuestionService {

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private QuestionExpandMapper questionExpandMapper;

    @Autowired
    private RedisService redisService;

    @Autowired
    private UserService userService;

    @Autowired
    private CommentService commentService;

    /**
     *
     * selectIndexQuestionList by
     *
     * @author: 不会飞的小鹏
     * @date: 2020/7/20 23:09
     * @param page
     * @return: PaginationDTO<QuestionShowModel>
     */
    public PaginationDTO<QuestionShowModel> selectIndexQuestionList(Integer page) {

        PaginationDTO<QuestionShowModel> paginationDTO = redisService.getIndexQuestionListByPage(page);
        if (paginationDTO == null){
            paginationDTO = new PaginationDTO<>();
            Integer totalCount = questionExpandMapper.countQuestion();
            Integer offset = paginationDTO.setPagination(totalCount, page);
            List<QuestionShowModel> questionShowModelList = questionExpandMapper.selectQuestionShowModelListByOffset(offset);
            questionSetCreatorName(questionShowModelList);
            paginationDTO.setData(questionShowModelList);
            redisService.addQuestionListByIndexPage(paginationDTO,page);
        }


        return paginationDTO;
    }

    /**
     * selectQuestionListBySearchWithPage 查询数据库中所有问题数量并进行分页和返回
     *
     * @param search
     * @param page
     * @author: 不会飞的小鹏
     * @date: 2020/7/1 23:01
     * @return: PaginationDTO<QuestionDTO>
     */
    public PaginationDTO<QuestionShowModel> selectQuestionListBySearchWithPage(String search, Integer page) {


        PaginationDTO paginationDTO = new PaginationDTO();
        QuestionQueryDTO questionQueryDTO = new QuestionQueryDTO();
        // 是否有查询内容
        if (StringUtils.isNotBlank(search)) {
            search = Arrays.stream(search.split(" ")).collect(Collectors.joining("|"));
            questionQueryDTO.setSearch(search);
        }
        // 对问题总数进行统计
        Integer totalCount = questionExpandMapper.countBySearch(questionQueryDTO);

        // 没有符合条件的问题
        if (totalCount == 0){
            return paginationDTO;
        }

        // 进行页码处理
        Integer offset = paginationDTO.setPagination(totalCount,page);
        questionQueryDTO.setOffset(offset);
        // 根据当前页码查询问题的介绍信息
        List<QuestionShowModel> questionShowModelList = questionExpandMapper.selectBySearchWithPage(questionQueryDTO);

        // 对问题进行作者关联
        questionSetCreatorName(questionShowModelList);
        // 进行分页的页码处理
        paginationDTO.setData(questionShowModelList);

        return paginationDTO;
    }

    /**
     * list 根据用户id查询其所有问题并进行分页和返回
     *
     * @param userId
     * @param page
     * @author: 不会飞的小鹏
     * @date: 2020/7/1 23:01
     * @return: PaginationDTO<QuestionDTO>
     */
    public PaginationDTO<QuestionShowModel> selectQuestionListByUserIdWithPage(Long userId, Integer page) {
        PaginationDTO<QuestionShowModel> paginationDTO = new PaginationDTO();
        QuestionExample example = new QuestionExample();
        example.createCriteria().andCreatorEqualTo(userId);
        Integer totalCount = (int) questionMapper.countByExample(example);

        // 没有符合条件的问题
        if (totalCount == 0){
            return paginationDTO;
        }

        Integer offset = paginationDTO.setPagination(totalCount,page);

        QuestionQueryDTO questionQueryDTO = new QuestionQueryDTO();
        questionQueryDTO.setUserId(userId);
        questionQueryDTO.setOffset(offset);
        List<QuestionShowModel> questionShowModelList = questionExpandMapper.selectByUserIdWithPage(questionQueryDTO);
        questionSetCreatorName(questionShowModelList);
        paginationDTO.setData(questionShowModelList);

        return paginationDTO;
    }

    /**
     * listByTag 根据页面传递tag查询相关问题并进行分页和返回
     *
     * @param tag
     * @param page
     * @author: 不会飞的小鹏
     * @date: 2020/7/1 23:01
     * @return: PaginationDTO<QuestionDTO>
     */
    public PaginationDTO<QuestionShowModel> selectQuestionListByTag(String tag, Integer page) {

        PaginationDTO<QuestionShowModel> paginationDTO = new PaginationDTO();

        String regexpTag = Arrays.stream(tag.split("，")).collect(Collectors.joining("|"));
        QuestionQueryDTO questionQueryDTO = new QuestionQueryDTO();
        questionQueryDTO.setSearch(regexpTag);
        Integer totalCount = questionExpandMapper.countByTag(questionQueryDTO);

        // 没有符合条件的问题
        if (totalCount == 0){
            return paginationDTO;
        }

        Integer offset = paginationDTO.setPagination(totalCount,page);

        questionQueryDTO.setOffset(offset);

        List<QuestionShowModel> questionShowModelList = questionExpandMapper.selectByTagWithPage(questionQueryDTO);

        questionSetCreatorName(questionShowModelList);
        paginationDTO.setData(questionShowModelList);

        return paginationDTO;
    }

    /**
     * questionSetData 封装返回问题结果集的方法
     *
     * @param questionShowModelList
     * @author: 不会飞的小鹏
     * @date: 2020/7/1 22:59
     * @return: void
     */
    private void questionSetCreatorName(List<QuestionShowModel> questionShowModelList) {

        // 获取去重的评论人
        Set<Long> creatorSet = questionShowModelList.stream().map(QuestionShowModel::getCreator).collect(Collectors.toSet());
        List<UserMessageModel> creatorMessageList = userService.selectUserMessageModelListByCreatorSet(creatorSet);
        Map<Long, UserMessageModel> userMessageModelMap = creatorMessageList.stream().collect(Collectors.toMap(userMessageModel -> userMessageModel.getId(), userMessageModel -> userMessageModel));
        questionShowModelList.stream().forEach(questionShowModel -> {
            questionShowModel.setCreatorName(userMessageModelMap.get(questionShowModel.getCreator()).getName());
            questionShowModel.setCreatorAvatarUrl(userMessageModelMap.get(questionShowModel.getCreator()).getAvatarUrl());
        });

    }

    /**
     * createOrUpdate 编辑问题点击发布后判断是更新已有问题还是直接发布新问题
     *
     * @param questionPublishDTO
     * @author: 不会飞的小鹏
     * @date: 2020/7/3 12:21
     * @return: void
     */
    public Boolean createOrUpdate(QuestionPublishDTO questionPublishDTO, Long userId) {

        if (questionPublishDTO.getId() == null) {

            Question question = new Question();
            question.setTitle(questionPublishDTO.getTitle());
            question.setDescription(questionPublishDTO.getDescription());
            question.setTag(questionPublishDTO.getTag());
            question.setCommentCount(0L);
            question.setViewCount(0L);
            question.setLikeCount(0L);
            question.setGmtCreate(System.currentTimeMillis());
            question.setGmtModified(System.currentTimeMillis());

            questionMapper.insert(question);
        } else {

            // 更新已有问题
            Question updateQuestion = questionMapper.selectByPrimaryKey(questionPublishDTO.getId());
            if (updateQuestion == null){
                return false;
            }
            if (!userId.equals(updateQuestion.getCreator())){
                return false;
            }
            updateQuestion.setTitle(questionPublishDTO.getTitle());
            updateQuestion.setDescription(questionPublishDTO.getDescription());
            updateQuestion.setTag(questionPublishDTO.getTag());
            updateQuestion.setGmtModified(System.currentTimeMillis());

            questionMapper.updateByPrimaryKeySelective(updateQuestion);
        }
        return true;
    }

    /**
     * getByCreator 若当前用户为展示问题的作者，则根据问题creator查询并返回其前十条问题
     *
     * @param creator
     * @author: 不会飞的小鹏
     * @date: 2020/7/3 12:22
     * @return: List<QuestionDTO>
     */
    public List<QuestionTitleModel> listTitleByCreator(Long creator) {

        List<QuestionTitleModel> questionTitleDaoList = questionExpandMapper.selectByCreatorWithTen(creator);

        return questionTitleDaoList;
    }

    /**
     * getByTag 根据页面展示问题的tag查询相关问题标题并返回前十条问题
     *
     * @param id
     * @param tag
     * @author: 不会飞的小鹏
     * @date: 2020/7/3 12:23
     * @return: List<QuestionDTO>
     */
    public List<QuestionTitleModel> listTitleByTag(Long id, String tag) {

        // 将问题标签转化为正则匹配
        String regexpTag = Arrays.stream(tag.split("，")).collect(Collectors.joining("|"));
        Question conditionQuestion = new Question();
        conditionQuestion.setId(id);
        conditionQuestion.setTag(regexpTag);
        List<QuestionTitleModel> questionTitleDaoList = questionExpandMapper.selectByTagWithTen(conditionQuestion);

        return questionTitleDaoList;
    }

    /**
     * cumulativeView 点击问题时累计问题的阅读数
     *
     * @param id
     * @author: 不会飞的小鹏
     * @date: 2020/7/4 17:53
     * @return: void
     */
    public void cumulativeView(Long id) {
        Question question = new Question();
        question.setId(id);
        question.setViewCount(1L);
        questionExpandMapper.cumulativeView(question);
    }

    /**
     *
     * userTag by 查询用户发布问题的所有标签并统计
     *
     * @author: 不会飞的小鹏
     * @date: 2020/7/13 23:40
     * @param id
     * @return: List<MessageTagDTO>
     */
    public List<MessageTagDTO> userTag(Long id) {

        List<String> tagList = questionExpandMapper.selectTagListByCreator(id);
        Map<String, Long> priorities = tagList.stream().map(tag -> tag.split("，"))
                .flatMap(Arrays::stream)
                .collect(Collectors.groupingBy(tag -> tag, Collectors.counting()));

        MessageTagCache messageTagCache = new MessageTagCache();
        List<MessageTagDTO> messageTagDTOS = messageTagCache.cacheTags(priorities);

        return messageTagDTOS;

    }

    public QuestionMessageDTO selectQuestionByQuestionId(Long id) {

        // 根据questionId获取问题
        Question question = questionMapper.selectByPrimaryKey(id);

        Byte isDelete = 1;

        if (question == null || isDelete.equals(question.getIsDelete())){
            return null;
        }

        // 根据creator获取作者信息
        Long creatorId = question.getCreator();
        UserMessageModel userMessageModel = userService.selectUserMessageDaoByUserId(creatorId);
        QuestionMessageDTO questionMessageDTO = new QuestionMessageDTO();
        BeanUtils.copyProperties(question,questionMessageDTO);
        questionMessageDTO.setCreatorName(userMessageModel.getName());
        questionMessageDTO.setCreatorAvatarUrl(userMessageModel.getAvatarUrl());

        // 获取十条相同标签问题的标题
        List<QuestionTitleModel> dependentQuestionTitleDaoList = listTitleByTag(questionMessageDTO.getId(), questionMessageDTO.getTag());
        questionMessageDTO.setDependentQuestionTitleList(dependentQuestionTitleDaoList);

        // 获取作者的十条问题标题
        List<QuestionTitleModel> myQuestionTitleDaoList = listTitleByCreator(creatorId);
        questionMessageDTO.setThisCreatorQuestionTitleList(myQuestionTitleDaoList);

        // 若评论不为空
        if (question.getCommentCount() != 0) {

            // 获取此问题的评论
            List<Comment> questionCommentList = commentService.selectCommentListByQuestionId(id, CommentTypeEnum.QUESTION.getType());
            setDeleteTypeComment(questionCommentList);
            List<Long> commentIdList = questionCommentList.stream().map(Comment::getId).collect(Collectors.toList());
            // 获取此问题评论的回复（二级评论）
            List<Comment> commentCommentList = commentService.selectCommentListByCommentIdList(commentIdList,CommentTypeEnum.COMMENT.getType());

            // 获取去重的评论人
            Set<Long> creatorSet = questionCommentList.stream().map(Comment::getCommentator).collect(Collectors.toSet());
            Set<Long> commentCommentCreatorSet = commentCommentList.stream().map(Comment::getCommentator).collect(Collectors.toSet());

            creatorSet.addAll(commentCommentCreatorSet);
            List<UserMessageModel> userMessageModelList = userService.selectUserMessageModelListByCreatorSet(creatorSet);

            // 一级评论
            List<CommentMessageDTO> questionCommentMessageDTOList = setCommentCreatorMessage(questionCommentList, userMessageModelList);

            // 二级评论
            List<CommentMessageDTO> commentCommentMessageDTOList = setCommentCreatorMessage(commentCommentList, userMessageModelList);

            // 将问题评论的回复列表转为map（根据二级评论的parentId分类）
            Map<Long, List<CommentMessageDTO>> commentCommentMap = commentCommentMessageDTOList.stream().collect(Collectors.groupingBy(CommentMessageDTO::getParentId));
            // 将分类好的二级评论根据parentId赋值给一级评论
            Set<Long> parentIdSet = commentCommentMap.keySet();
            questionCommentMessageDTOList.stream()
                    .filter(commentMessageDTO -> parentIdSet.contains(commentMessageDTO.getId()))
                    .forEach(commentMessageDTO -> commentMessageDTO.setCommentCommentList(commentCommentMap.get(commentMessageDTO.getId())));

            questionMessageDTO.setQuestionCommentList(questionCommentMessageDTOList);
        }

        return questionMessageDTO;
    }

    /**
     *
     * setDeleteTypeComment by 为删除状态的评论设值
     *
     * @author: 不会飞的小鹏
     * @date: 2020/7/24 1:07
     * @param questionCommentList
     * @return: void
     */
    private void setDeleteTypeComment(List<Comment> questionCommentList) {

        Byte deleteType = 1;
        questionCommentList.stream().filter(comment -> deleteType.equals(comment.getIsDelete())).forEach(comment -> comment.setContent("该评论已删除"));

    }


    /**
     *
     * setCommentCreatorMessage by 为评论添加作者信息
     *
     * @author: 不会飞的小鹏
     * @date: 2020/7/23 11:16
     * @param commentList
     * @param userMessageDaoList
     * @return: List<CommentMessageDTO>
     */
    private List<CommentMessageDTO> setCommentCreatorMessage(List<Comment> commentList, List<UserMessageModel> userMessageDaoList) {

        Map<Long, UserMessageModel> userMessageDaoMap = userMessageDaoList.stream().collect(Collectors.toMap((UserMessageModel::getId), userMessageModel -> userMessageModel));

        List<CommentMessageDTO> commentMessageDTOList = commentList.stream().map(comment -> {
            CommentMessageDTO commentMessageDTO = new CommentMessageDTO();
            BeanUtils.copyProperties(comment, commentMessageDTO);
            UserMessageModel userMessageModel = userMessageDaoMap.get(comment.getCommentator());
            commentMessageDTO.setCreatorName(userMessageModel.getName());
            commentMessageDTO.setCreatorAvatarUrl(userMessageModel.getAvatarUrl());
            return commentMessageDTO;
        }).collect(Collectors.toList());

        return commentMessageDTOList;
    }

    /**
     *
     * getQuestionPublishModelById by 根据问题id返回问题修改内容
     *
     * @author: 不会飞的小鹏
     * @date: 2020/7/23 11:15
     * @param id
     * @return: QuestionPublishModel
     */
    public QuestionPublishModel getQuestionPublishModelById(Long id) {

        QuestionPublishModel questionPublishModel = questionExpandMapper.selectQuestionPublishModelById(id);

        return questionPublishModel;
    }

    /**
     *
     * checkOneself by 检查是否为作者本人
     *
     * @author: 不会飞的小鹏
     * @date: 2020/7/23 11:15
     * @param id
     * @param userId
     * @return: Boolean
     */
    public Boolean checkOneself(Long id, Long userId) {

        QuestionCreatorModel questionCreatorModel = questionExpandMapper.selectQuestionCreatorModelById(id);

        if (questionCreatorModel == null){
            return null;
        }

        if (userId.equals(questionCreatorModel.getCreator())){
            return true;
        }

        return false;
    }

    /**
     *
     * deleteQuestionById by 根据问题id删除问题
     *
     * @author: 不会飞的小鹏
     * @date: 2020/7/23 11:14
     * @param id
     * @return: void
     */
    public void deleteQuestionById(Long id) {
        Question question = new Question();
        question.setId(id);
        question.setIsDelete((byte) 1);
        questionMapper.updateByPrimaryKeySelective(question);
    }


    /**
     *
     * cumulativeCommentCount by 累计问题回复数
     *
     * @author: 不会飞的小鹏
     * @date: 2020/7/24 23:42
     * @param dbQuestion
     * @return: void
     */
    public void cumulativeCommentCount(Question dbQuestion) {
        questionExpandMapper.cumulativeCommentCount(dbQuestion);
    }

}
