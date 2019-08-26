package life.wpx1997.community.dto;

import life.wpx1997.community.model.User;
import lombok.Data;

/**
 * created by 小case on 2019/8/26 16:45
 */
@Data
public class NotificationDTO {
    private Long id;
    private Long gmtCreate;
    private Long notifier;
    private String notifierName;
    private Integer status;
    private Long outerId;
    private String outerTitle;
    private Integer type;
    private String typeName;
}
