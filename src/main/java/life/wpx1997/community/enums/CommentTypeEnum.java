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
    QUESTION((byte)1),

    /**
     * @author: 不会飞的小鹏
     * @date: 2020/7/4 17:14
     * @description: CommentTypeEnum's COMMENT is 回复类型为评论回复
     */
    COMMENT((byte)2);

    private Byte type;

    public Byte getType() {
        return type;
    }

    CommentTypeEnum(Byte type) {
        this.type = type;
    }
}
