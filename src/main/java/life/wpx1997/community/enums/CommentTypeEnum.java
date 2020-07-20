package life.wpx1997.community.enums;

/**
 * @author 不会飞的小鹏
 */

public enum CommentTypeEnum {

    /**
     * @author: 不会飞的小鹏
     * @date: 2020/7/4 17:13
     * @description: CommentTypeEnum's QUESTION is 回复类型为问题评论
     */
    QUESTION(1),

    /**
     * @author: 不会飞的小鹏
     * @date: 2020/7/4 17:14
     * @description: CommentTypeEnum's COMMENT is 回复类型为评论回复
     */
    COMMENT(2);

    private Integer type;

    public static boolean isExist(Integer type) {
        for (CommentTypeEnum commentTypeEnum : CommentTypeEnum.values()){
            if (commentTypeEnum.getType().equals(type)){
                return true;
            }
        }
        return false;
    }

    public Integer getType() {
        return type;
    }

    CommentTypeEnum(Integer type) {
        this.type = type;
    }
}
