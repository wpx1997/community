package life.wpx1997.community.exception;

public enum CustomizeErrorCode implements ICustomizeErrorCode {

    QUESTION_NOT_FOUND(2001,"你修改的问题已经不存在，要不重新发布一个？"),
    TAGET_PARAM_NOT_FOUND(2002,"未选中任何问题和评论，请重新选择！"),
    NOT_LOGIN(2003,"请登录后再进行评论！"),
    SYS_ERROR(2004,"服务器炸了，稍等一下!!!"),
    TYPE_PARAM_WRONG(2005,"评论类型不存在!"),
    COMMENT_NOT_FOUND(2006,"你回复的评论不存在了，要不换一个吧！"),
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
