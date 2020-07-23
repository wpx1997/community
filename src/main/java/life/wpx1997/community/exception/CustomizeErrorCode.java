package life.wpx1997.community.exception;

/**
 * @author 不会飞的小鹏
 */

public enum CustomizeErrorCode implements ICustomizeErrorCode {

    /**
     * @author: 不会飞的小鹏
     * @date: 2020/7/5 17:23
     * @description: CustomizeErrorCode's QUESTION_NOT_FOUND 修改问题不存在
     */
    QUESTION_NOT_FOUND(2001,"你修改的问题已经不存在，要不重新发布一个？"),

    /**
     * @author: 不会飞的小鹏
     * @date: 2020/7/5 17:24
     * @description: CustomizeErrorCode's TARGET_PARAM_NOT_FOUND
     */
    TARGET_PARAM_NOT_FOUND(2002,"未选中任何问题和评论，请重新选择！"),

    /**
     * @author: 不会飞的小鹏
     * @date: 2020/7/5 17:25
     * @description: CustomizeErrorCode's NOT_LOGIN 用户未登录
     */
    NOT_LOGIN(2003,"你未进行登录！"),

    /**
     * @author: 不会飞的小鹏
     * @date: 2020/7/5 17:25
     * @description: CustomizeErrorCode's SYS_ERROR 服务器出错
     */
    SYS_ERROR(2004,"服务器炸了，稍等一下!!!"),

    /**
     * @author: 不会飞的小鹏
     * @date: 2020/7/5 17:26
     * @description: CustomizeErrorCode's TYPE_PARAM_WRONG 评论类型出错
     */
    TYPE_PARAM_WRONG(2005,"评论类型不存在!"),

    /**
     * @author: 不会飞的小鹏
     * @date: 2020/7/5 17:27
     * @description: CustomizeErrorCode's COMMENT_NOT_FOUND 所回复的评论不存在
     */
    COMMENT_NOT_FOUND(2006,"该评论不存在了，要不换一个吧！"),

    /**
     * @author: 不会飞的小鹏
     * @date: 2020/7/5 17:27
     * @description: CustomizeErrorCode's CONTENT_IS_EMPTY 回复的内容为空
     */
    CONTENT_IS_EMPTY(2007,"输入内容不能为空，请先增加你的见解！"),

    /**
     * @author: 不会飞的小鹏
     * @date: 2020/7/5 17:28
     * @description: CustomizeErrorCode's READ_NOTIFICATION_FAIL 跳转其他用户的个人主页
     */
    READ_NOTIFICATION_FAIL(2008,"兄弟，你这是读别人的信息呢！"),

    /**
     * @author: 不会飞的小鹏
     * @date: 2020/7/5 17:28
     * @description: CustomizeErrorCode's NOTIFICATION_NOT_FOUND 通知不存在
     */
    NOTIFICATION_NOT_FOUND(2009,"消息不见了，我也找不到！"),

    /**
     * @author: 不会飞的小鹏
     * @date: 2020/7/5 17:29
     * @description: CustomizeErrorCode's FILE_UPLOAD_FAIL 图片上传失败
     */
    FILE_UPLOAD_FAIL(2010,"图片上传失败！"),

    /**
     * @author: 不会飞的小鹏
     * @date: 2020/7/21 11:42
     * @description: CustomizeErrorCode's SEARCH_NOT_FOUND 查询内容不存在
     */
    SEARCH_NOT_FOUND(2011,"查询内容不存在!"),

    /**
     * @author: 不会飞的小鹏
     * @date: 2020/7/21 18:07
     * @description: CustomizeErrorCode's ILLEGAL_TAG 问题标签非法
     */
    ILLEGAL_TAG(2012,"输入非法标签"),

    /**
     * @author: 不会飞的小鹏
     * @date: 2020/7/21 18:09
     * @description: CustomizeErrorCode's QUESTION_UPDATE_ERROR 问题修改失败
     */
    QUESTION_UPDATE_ERROR(2013,"问题修改失败"),

    /**
     * @author: 不会飞的小鹏
     * @date: 2020/7/23 9:14
     * @description: CustomizeErrorCode's QUESTION_CREATOR_NOT_YOU 非问题作者修改问题
     */
    QUESTION_CREATOR_NOT_YOU(2014,"你不是问题作者，无权限修改"),

    /**
     * @author: 不会飞的小鹏
     * @date: 2020/7/23 11:53
     * @description: CustomizeErrorCode's COMMENT_CREATOR_NOT_YOU 非评论作者修改评论
     */
    COMMENT_CREATOR_NOT_YOU(2015,"你不是评论作者，无权限修改")
    ;

    @Override
    public String getMessage(){
        return message;
    }

    @Override
    public Integer getCode() {
        return code;
    }

    private Integer code;
    private String message;

    CustomizeErrorCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

}
