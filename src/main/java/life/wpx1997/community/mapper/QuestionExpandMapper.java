package life.wpx1997.community.mapper;

import life.wpx1997.community.model.*;
import life.wpx1997.community.dto.QuestionQueryDTO;

import java.util.List;

/**
 * @author 不会飞的小鹏
 */
public interface QuestionExpandMapper {

    /**
     *
     * countQuestion by 查询所有问题数量
     *
     * @author: 不会飞的小鹏
     * @date: 2020/7/16 19:56
     * @param
     * @return: Integer
     */
    Integer countQuestion();

    /**
     *
     * cumulativeView 累计阅读数
     *
     * @author: 不会飞的小鹏
     * @date: 2020/7/4 20:25
     * @param record
     * @return: Integer
     */
    Integer cumulativeView(Question record);

    /**
     *
     * cumulativeCommentCount 累计回复数
     *
     * @author: 不会飞的小鹏
     * @date: 2020/7/4 20:25
     * @param record
     * @return: Integer
     */
    Integer cumulativeCommentCount(Question record);

    /**
     *
     * selectByTag 根据标签查询十条相关问题
     *
     * @author: 不会飞的小鹏
     * @date: 2020/7/4 20:26
     * @param question
     * @return: List<Question>
     */
    List<QuestionTitleModel> selectByTagWithTen(Question question);

    /**
     *
     * countByTag 根据问题标签查询相关问题数量
     *
     * @author: 不会飞的小鹏
     * @date: 2020/7/4 20:26
     * @param questionQueryDTO
     * @return: Integer
     */
    Integer countByTag(QuestionQueryDTO questionQueryDTO);

    /**
     *
     * selectByTagWithPage 根据标签分页查询问题
     *
     * @author: 不会飞的小鹏
     * @date: 2020/7/4 20:28
     * @param questionQueryDTO
     * @return: List<Question>
     */
    List<QuestionShowModel> selectByTagWithPage(QuestionQueryDTO questionQueryDTO);

    /**
     *
     * countBySearch 根据search累计问题数目
     *
     * @author: 不会飞的小鹏
     * @date: 2020/7/4 20:29
     * @param questionQueryDTO
     * @return: Integer
     */
    Integer countBySearch(QuestionQueryDTO questionQueryDTO);

    /**
     *
     * selectBySearchWithPage 根据search分页查询问题
     *
     * @author: 不会飞的小鹏
     * @date: 2020/7/4 20:29
     * @param questionQueryDTO
     * @return: List<Question>
     */
    List<QuestionShowModel> selectBySearchWithPage(QuestionQueryDTO questionQueryDTO);

    /**
     *
     * selectByUserIdWithPage 根据用户id分页查询问题展示信息
     *
     * @author: 不会飞的小鹏
     * @date: 2020/7/4 22:23
     * @param questionQueryDTO
     * @return: List<QuestionShowDao>
     */
    List<QuestionShowModel> selectByUserIdWithPage(QuestionQueryDTO questionQueryDTO);

    /**
     *
     * selectByCreatorWithTen 根据文章作者查询十条本人文章的标题
     *
     * @author: 不会飞的小鹏
     * @date: 2020/7/4 22:37
     * @param creator
     * @return: List<QuestionTitleDao>
     */
    List<QuestionTitleModel> selectByCreatorWithTen(Long creator);

    /**
     *
     * selectTagListByCreator by 根据作者id查询所有问题标签
     *
     * @author: 不会飞的小鹏
     * @date: 2020/7/13 23:21
     * @param creator
     * @return: List<String>
     */
    List<String> selectTagListByCreator(Long creator);

    /**
     *
     * selectQuestionShowDaoListByPage by 根据页码查询首页问题展示信息
     *
     * @author: 不会飞的小鹏
     * @date: 2020/7/16 20:08
     * @param offset
     * @return: List<QuestionShowModel>
     */
    List<QuestionShowModel> selectQuestionShowModelListByOffset(Integer offset);

    /**
     *
     * selectQuestionPublishModelById by 根据问题id查找问题的编辑页面信息
     *
     * @author: 不会飞的小鹏
     * @date: 2020/7/21 16:37
     * @param id
     * @return: QuestionPublishModel
     */
    QuestionPublishModel selectQuestionPublishModelById(Long id);

    /**
     *
     * selectQuestionCreatorModelById by 根据问题id查询作者id
     *
     * @author: 不会飞的小鹏
     * @date: 2020/7/23 8:53
     * @param id
     * @return: QuestionCreatorModel
     */
    QuestionCreatorModel selectQuestionCreatorModelById(Long id);
}