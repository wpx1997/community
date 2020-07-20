package life.wpx1997.community.enums;

import java.util.PriorityQueue;

/**
 * @author 不会飞的小鹏
 * create on 2020/7/4 23:56
 * @Description ActionTypeEnum is
 */
public enum ActionTypeEnum {

    /**
     * @author: 不会飞的小鹏
     * @date: 2020/7/5 0:01
     * @description: ActionTypeEnum's 本人问题页标识
     */
    QUESTIONS("questions"),

    /**
     * @author: 不会飞的小鹏
     * @date: 2020/7/5 0:01
     * @description: ActionTypeEnum's 本人通知页标识
     */
    REPLIES("replies"),
    ;

    private String type;

    ActionTypeEnum(String type){
        this.type = type;
    }

    public String getType(){
        return this.type;
    }

}
