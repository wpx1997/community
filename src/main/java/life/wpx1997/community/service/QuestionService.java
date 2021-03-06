package life.wpx1997.community.service;

import life.wpx1997.community.cache.MessageTagCache;
import life.wpx1997.community.constant.TypeConstant;
import life.wpx1997.community.dto.*;
import life.wpx1997.community.enums.CommentTypeEnum;
import life.wpx1997.community.mapper.QuestionExpandMapper;
import life.wpx1997.community.mapper.QuestionMapper;
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

    @Autowired
    private LikeService likeService;

    @Autowired
    private CacheService cacheService;

    /**
     *
     * updateHotQuestionToRedis by 将热榜问题信息更新到redis中
     *
     * @author: 不会飞的小鹏
     * @date: 2020/8/7 13:04
     * @param idList
     * @return: void
     */
    public void updateHotQuestionToRedis(List<Long> idList) {
        idList.stream().forEach(id -> {
            QuestionMessageDTO questionMessageDTO = selectQuestionByRedis(id);
            addQuestionToRedis(questionMessageDTO);
        });
    }

    /**
     *
     * updateHotTagToRedis by 将热榜标签信息更新到redis
     *
     * @author: 不会飞的小鹏
     * @date: 2020/8/7 13:36
     * @param hotTagDTOList
     * @return: void
     */
    public void updateHotTagToRedis(List<HotTagDTO> hotTagDTOList) {
        Integer page = 1;
        List<String> tagList = hotTagDTOList.stream().map(HotTagDTO::getName).collect(Collectors.toList());
        tagList.stream().forEach(tag -> {
            PaginationDTO<QuestionShowDTO> paginationDTO = selectQuestionListByTag(tag, page);
            redisService.addPaginationByTagWithPage(paginationDTO,tag,page);
        });
    }

    /**
     *
     * selectIndexQuestionList by
     *
     * @author: 不会飞的小鹏
     * @date: 2020/7/20 23:09
     * @param page
     * @return: PaginationDTO<QuestionShowModel>
     */
    public PaginationDTO<QuestionShowDTO> selectIndexQuestionList(Integer page) {

        PaginationDTO<QuestionShowDTO> paginationDTO = redisService.getIndexQuestionListByPage(page);
        if (paginationDTO == null){
            paginationDTO = new PaginationDTO<>();
            Integer totalCount = questionExpandMapper.countQuestion();
            Integer offset = paginationDTO.setPagination(totalCount, page);
            List<Question> questionList = questionExpandMapper.selectQuestionShowModelListByOffset(offset);
            List<QuestionShowDTO> questionShowDTOList = questionSetCreatorName(questionList);
            paginationDTO.setData(questionShowDTOList);
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
    public PaginationDTO<QuestionShowDTO> selectQuestionListBySearchWithPage(String search, Integer page) {

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
            return null;
        }

        // 进行页码处理
        Integer offset = paginationDTO.setPagination(totalCount,page);
        questionQueryDTO.setOffset(offset);
        // 根据当前页码查询问题的介绍信息
        List<Question> questionList = questionExpandMapper.selectBySearchWithPage(questionQueryDTO);

        // 对问题进行作者关联
        List<QuestionShowDTO> questionShowDTOList = questionSetCreatorName(questionList);
        // 进行分页的页码处理
        paginationDTO.setData(questionShowDTOList);

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
    public PaginationDTO<QuestionShowDTO> selectQuestionListByUserIdWithPage(Long userId, Integer page) {

        PaginationDTO<QuestionShowDTO> paginationDTO = new PaginationDTO();
        QuestionExample example = new QuestionExample();
        example.createCriteria().andCreatorEqualTo(userId);
        Integer totalCount = (int) questionMapper.countByExample(example);

        // 没有符合条件的问题
        if (totalCount == 0){
            return null;
        }

        Integer offset = paginationDTO.setPagination(totalCount,page);

        QuestionQueryDTO questionQueryDTO = new QuestionQueryDTO();
        questionQueryDTO.setUserId(userId);
        questionQueryDTO.setOffset(offset);
        List<Question> questionList = questionExpandMapper.selectByUserIdWithPage(questionQueryDTO);
        List<QuestionShowDTO> questionShowDTOList = questionSetCreatorName(questionList);
        paginationDTO.setData(questionShowDTOList);

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
    public PaginationDTO<QuestionShowDTO> selectQuestionListByTag(String tag, Integer page) {

        PaginationDTO<QuestionShowDTO> paginationDTO = redisService.getPaginationByTagWithPage(tag,page);
        if (paginationDTO == null){
            paginationDTO = new PaginationDTO();
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

            List<Question> questionList = questionExpandMapper.selectByTagWithPage(questionQueryDTO);

            List<QuestionShowDTO> questionShowDTOList = questionSetCreatorName(questionList);
            paginationDTO.setData(questionShowDTOList);

            redisService.addPaginationByTagWithPage(paginationDTO,tag,page);
        }

        return paginationDTO;
    }

    /**
     * questionSetData 封装返回问题结果集的方法
     *
     * @param questionList
     * @author: 不会飞的小鹏
     * @date: 2020/7/1 22:59
     * @return: void
     */
    public List<QuestionShowDTO> questionSetCreatorName(List<Question> questionList) {

        // 获取去重的评论人id
        Set<Long> creatorSet = questionList.stream().map(Question::getCreator).collect(Collectors.toSet());
        // 从数据库中获取去重的评论人信息
        List<User> userList = userService.selectUserMessageListByCreatorSet(creatorSet);
        // 将评论人信息转化为map
        Map<Long, User> userMap = userList.stream().collect(Collectors.toMap(user -> user.getId(), user -> user));
        List<QuestionShowDTO> questionShowDTOList = questionList.stream().map(question -> {
            QuestionShowDTO questionShowDTO = new QuestionShowDTO();
            BeanUtils.copyProperties(question, questionShowDTO);
            questionShowDTO.setCreatorName(userMap.get(question.getCreator()).getName());
            questionShowDTO.setCreatorAvatarUrl(userMap.get(question.getCreator()).getAvatarUrl());
            return questionShowDTO;
        }).collect(Collectors.toList());

        return questionShowDTOList;
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
    public List<Question> listTitleByCreator(Long creator) {

        List<Question> questionList = questionExpandMapper.selectByCreatorWithTen(creator);

        return questionList;
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
    public List<Question> listTitleByTag(Long id, String tag) {

        // 将问题标签转化为正则匹配
        String regexpTag = Arrays.stream(tag.split("，")).collect(Collectors.joining("|"));
        Question conditionQuestion = new Question();
        conditionQuestion.setId(id);
        conditionQuestion.setTag(regexpTag);
        List<Question> questionList = questionExpandMapper.selectByTagWithTen(conditionQuestion);

        return questionList;
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

    public Question selectQuestionTitleById(Long id){

        Question question = questionExpandMapper.selectQuestionTitleById(id);

        return question;
    }

    /**
     *
     * selectQuestion by 根据问题id查找问题
     *
     * @author: 不会飞的小鹏
     * @date: 2020/8/1 18:27
     * @param id
     * @param userId
     * @return: QuestionMessageDTO
     */
    public QuestionMessageDTO selectQuestion(Long id,Long userId){

        // 从缓存中获取问题信息
        QuestionMessageDTO questionMessageDTO = selectQuestionByRedis(id);
        // 根据用户id设置问题点赞情况
        setQuestionLike(questionMessageDTO,userId);

        // 将评论分组并组装
        List<CommentMessageDTO> questionCommentList = groupComment(questionMessageDTO.getQuestionCommentList());
        questionMessageDTO.setQuestionCommentList(questionCommentList);

        return questionMessageDTO;
    }

    /**
     *
     * selectQuestionByRedis by 根据id从缓存中获取问题信息
     *
     * @author: 不会飞的小鹏
     * @date: 2020/8/1 18:28
     * @param id
     * @return: QuestionMessageDTO
     */
    public QuestionMessageDTO selectQuestionByRedis(Long id) {

        // 从redis中获取问题信息
        QuestionMessageDTO questionMessageDTO = selectQuestionMessageByRedis(id);
        // redis中不存在，从数据库中获取
        if (questionMessageDTO == null){
            questionMessageDTO = selectQuestionByQuestionId(id);
            if (questionMessageDTO == null){
                return null;
            }else {
                // 将获取的问题信息写入缓存
                addQuestionToRedis(questionMessageDTO);
            }
        }

        return questionMessageDTO;
    }

    /**
     *
     * setQuestionLike by 装载问题点赞信息
     *
     * @author: 不会飞的小鹏
     * @date: 2020/8/1 18:31
     * @param questionMessageDTO
     * @param userId
     * @return: void
     */
    private void setQuestionLike(QuestionMessageDTO questionMessageDTO, Long userId) {

        // 用户已登录
        if (userId > 0){
            // 查询问题点赞情况
            CommunityLike like = new CommunityLike();
            like.setParentId(questionMessageDTO.getId());
            like.setUserId(userId);
            like.setType(TypeConstant.LIKE_TYPE_QUESTION);
            CommunityLike communityLike = likeService.selectLikeByParentIdWithUserId(like);
            if (communityLike == null || TypeConstant.IS_DELETE_YES.equals(communityLike.getIsDelete())){
                questionMessageDTO.setIsLike(false);
            }else {
                questionMessageDTO.setIsLike(true);
            }

            // 评论点赞情况
            List<CommentMessageDTO> commentMessageDTOList = questionMessageDTO.getQuestionCommentList();
            List<Long> parentIdList = likeService.selectCommentLikeListByParentIdList(like);
            setCommentLike(commentMessageDTOList,parentIdList);
        }
    }

    /**
     *
     * groupComment by 将评论进行分组并进行组装
     *
     * @author: 不会飞的小鹏
     * @date: 2020/8/1 18:36
     * @param commentMessageDTOList
     * @return: List<CommentMessageDTO>
     */
    private List<CommentMessageDTO> groupComment(List<CommentMessageDTO> commentMessageDTOList) {

        Map<Byte, List<CommentMessageDTO>> commentMap = commentMessageDTOList.stream().collect(Collectors.groupingBy(CommentMessageDTO::getType));
        List<CommentMessageDTO> questionCommentList = commentMap.get(TypeConstant.COMMENT_TYPE_QUESTION);
        List<CommentMessageDTO> commentCommentList = commentMap.get(TypeConstant.COMMENT_TYPE_COMMENT);

        Map<Long, List<CommentMessageDTO>> commentCommentMap = commentCommentList.stream().collect(Collectors.groupingBy(CommentMessageDTO::getParentId));
        Set<Long> parentIdSet = commentCommentMap.keySet();
        questionCommentList.stream()
                .filter(commentMessageDTO -> parentIdSet.contains(commentMessageDTO.getId()))
                .forEach(commentMessageDTO -> commentMessageDTO.setCommentCommentList(commentCommentMap.get(commentMessageDTO.getId())));

        return questionCommentList;
    }

    /**
     *
     * selectQuestionByQuestionId by 根据问题id返回问题的所有信息
     *
     * @author: 不会飞的小鹏
     * @date: 2020/7/26 16:14
     * @param id
     * @return: QuestionMessageDTO
     */
    public QuestionMessageDTO selectQuestionByQuestionId(Long id) {

        // 根据questionId获取问题
        Question question = questionMapper.selectByPrimaryKey(id);

        if (question == null || TypeConstant.IS_DELETE_YES.equals(question.getIsDelete())){
            return null;
        }

        // 根据creator获取作者信息
        Long creatorId = question.getCreator();
        User creator = userService.selectUserMessageByUserId(creatorId);
        QuestionMessageDTO questionMessageDTO = questionSetMessage(question, creator);

        // 获取十条相同标签问题的标题
        List<Question> dependentQuestionTitleList = listTitleByTag(questionMessageDTO.getId(), questionMessageDTO.getTag());
        questionMessageDTO.setDependentQuestionTitleList(dependentQuestionTitleList);

        // 获取作者的十条问题标题
        List<Question> myQuestionTitleDaoList = listTitleByCreator(creatorId);
        questionMessageDTO.setThisCreatorQuestionTitleList(myQuestionTitleDaoList);

        // 若评论不为空
        if (question.getCommentCount() > 0) {
            List<CommentMessageDTO> questionCommentMessageDTOList = selectCommentMessageListByQuestionId(id);
            questionMessageDTO.setQuestionCommentList(questionCommentMessageDTOList);
        }

        return questionMessageDTO;
    }

    /**
     *
     * selectCommentMessageListByQuestionId by 获取问题的评论列表
     *
     * @author: 不会飞的小鹏
     * @date: 2020/7/28 17:57
     * @param id
     * @return: List<CommentMessageDTO>
     */
    private List<CommentMessageDTO> selectCommentMessageListByQuestionId(Long id) {

        // 获取此问题的评论
        List<Comment> questionCommentList = commentService.selectCommentListByQuestionId(id, CommentTypeEnum.QUESTION.getType());

        setDeleteTypeComment(questionCommentList);
        List<Long> commentIdList = questionCommentList.stream().map(Comment::getId).collect(Collectors.toList());

        // 获取此问题评论的回复（二级评论）
        List<Comment> commentCommentList = commentService.selectCommentListByCommentIdList(commentIdList,TypeConstant.COMMENT_TYPE_COMMENT);

        // 合并一级评论和二级
        questionCommentList.addAll(commentCommentList);

        // 获取去重的评论人信息
        Set<Long> creatorSet = questionCommentList.stream().map(Comment::getCommentator).collect(Collectors.toSet());
        List<User> userList = userService.selectUserMessageListByCreatorSet(creatorSet);

        // 转化评论信息
        List<CommentMessageDTO> questionCommentMessageDTOList = setCommentCreatorMessage(questionCommentList, userList);

        return questionCommentMessageDTOList;
    }

    /**
     *
     * questionSetMessage by 装配question的累计信息
     *
     * @author: 不会飞的小鹏
     * @date: 2020/7/28 17:50
     * @param question
     * @param user
     * @return: QuestionMessageDTO
     */
    private QuestionMessageDTO questionSetMessage(Question question, User user) {

        // 将Question转为QuestionMessageDTO
        QuestionMessageDTO questionMessageDTO = new QuestionMessageDTO();
        BeanUtils.copyProperties(question,questionMessageDTO);
        questionMessageDTO.setCreatorName(user.getName());
        questionMessageDTO.setCreatorAvatarUrl(user.getAvatarUrl());

        // 从缓存中读取未更新到数据库的累计数
        QuestionCumulativeDTO questionCumulativeDTO = cacheService.getQuestionCumulativeCacheById(question.getId());
        if (questionCumulativeDTO != null){
            Long viewCount = questionMessageDTO.getViewCount();
            Long commentCount = questionMessageDTO.getCommentCount();
            Long likeCount = questionMessageDTO.getLikeCount();
            questionMessageDTO.setViewCount(questionCumulativeDTO.getViewCount() + viewCount);
            questionMessageDTO.setCommentCount(questionCumulativeDTO.getCommentCount() + commentCount);
            questionMessageDTO.setLikeCount(questionCumulativeDTO.getLikeCount() + likeCount);
        }

        return questionMessageDTO;
    }

    /**
     *
     * setCommentLike by 装配评论的点赞信息
     *
     * @author: 不会飞的小鹏
     * @date: 2020/8/1 18:42
     * @param commentMessageDTOList
     * @param parentIdList
     * @return: void
     */
    private void setCommentLike(List<CommentMessageDTO> commentMessageDTOList, List<Long> parentIdList) {

        commentMessageDTOList.stream().filter(commentMessageDTO -> parentIdList.contains(commentMessageDTO.getId())).forEach(commentMessageDTO -> commentMessageDTO.setIsLike(true));

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

        Map<Long, CommentCumulativeDTO> commentCumulativeMap = cacheService.getCommentCumulativeMap();
        questionCommentList.stream().forEach(comment -> {
            if (TypeConstant.IS_DELETE_YES.equals(comment.getIsDelete())){
                comment.setContent("该评论已删除");
            }
            CommentCumulativeDTO commentCumulativeDTO = commentCumulativeMap.get(comment.getId());
            if (commentCumulativeDTO != null){
                Long commentCount = comment.getCommentCount();
                Long likeCount = comment.getLikeCount();
                comment.setCommentCount(commentCount + commentCumulativeDTO.getCommentCount());
                comment.setLikeCount(likeCount + commentCumulativeDTO.getLikeCount());
            }
        });

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
    private List<CommentMessageDTO> setCommentCreatorMessage(List<Comment> commentList, List<User> userMessageDaoList) {

        Map<Long, User> userMap = userMessageDaoList.stream().collect(Collectors.toMap((User::getId), user -> user));

        List<CommentMessageDTO> commentMessageDTOList = commentList.stream().map(comment -> {
            CommentMessageDTO commentMessageDTO = new CommentMessageDTO();
            BeanUtils.copyProperties(comment, commentMessageDTO);
            User user = userMap.get(comment.getCommentator());
            commentMessageDTO.setCreatorName(user.getName());
            commentMessageDTO.setCreatorAvatarUrl(user.getAvatarUrl());
            return commentMessageDTO;
        }).collect(Collectors.toList());

        return commentMessageDTOList;
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
        cacheService.cumulativeQuestionViewCount(id);
    }

    /**
     *
     * cumulativeCommentCount by 增加和删除评论时累计问题的阅读数
     *
     * @author: 不会飞的小鹏
     * @date: 2020/8/2 0:47
     * @param question
     * @return: void
     */
    public void cumulativeCommentCount(Question question){
        cacheService.cumulativeQuestionCommentCount(question);
    }

    /**
     *
     * selectQuestionMessageByRedis by 从redis中获取问题信息
     *
     * @author: 不会飞的小鹏
     * @date: 2020/8/1 20:14
     * @param id
     * @return: QuestionMessageDTO
     */
    public QuestionMessageDTO selectQuestionMessageByRedis(Long id) {

        QuestionMessageDTO questionMessageDTO = redisService.selectQuestionById(id);

        return questionMessageDTO;
    }

    /**
     *
     * addQuestionToRedis by 将问题信息存入redis
     *
     * @author: 不会飞的小鹏
     * @date: 2020/8/1 20:23
     * @param questionMessageDTO
     * @return: void
     */
    public void addQuestionToRedis(QuestionMessageDTO questionMessageDTO) {
        redisService.addQuestion(questionMessageDTO);
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
    public Question getQuestionPublishModelById(Long id) {

        Question question = questionExpandMapper.selectQuestionPublishModelById(id);

        return question;
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

        Question question = questionExpandMapper.selectQuestionCreatorModelById(id);
        if (question == null){
            return null;
        }
        if (userId.equals(question.getCreator())){
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

        redisService.deleteQuestionById(id);
        Question question = new Question();
        question.setId(id);
        question.setIsDelete(TypeConstant.IS_DELETE_YES);
        questionMapper.updateByPrimaryKeySelective(question);

    }


    /**
     *
     * countByCreator by 统计作者的问题数
     *
     * @author: 不会飞的小鹏
     * @date: 2020/7/26 16:13
     * @param creator
     * @return: Long
     */
    public Long countByCreator(Long creator) {

        QuestionExample example = new QuestionExample();
        example.createCriteria().andCreatorEqualTo(creator);
        Long questionCount = questionMapper.countByExample(example);

        return questionCount;
    }

    /**
     *
     * selectAllQuestionShowModelList by 查询所有问题的热度相关信息
     *
     * @author: 不会飞的小鹏
     * @date: 2020/7/26 16:12
     * @param
     * @return: List<Question>
     */
    public List<Question> selectAllQuestionShowModelList() {

        List<Question> questionList = questionExpandMapper.selectAllQuestionShowModelList();

        return questionList;
    }

    /**
     *
     * selectAllQuestionTagList by 查询所有问题的标签热度相关信息
     *
     * @author: 不会飞的小鹏
     * @date: 2020/7/26 16:11
     * @param
     * @return: List<Question>
     */
    public List<Question> selectAllQuestionTagList() {

        List<Question> questionList =  questionExpandMapper.selectAllQuestionTagList();

        return questionList;
    }

    /**
     *
     * questionCumulative by 批量更新问题的累计数
     *
     * @author: 不会飞的小鹏
     * @date: 2020/7/26 16:11
     * @param cumulativeList
     * @return: void
     */
    public void questionCumulative(List<QuestionCumulativeDTO> cumulativeList) {
        questionExpandMapper.questionCumulative(cumulativeList);
    }

}
