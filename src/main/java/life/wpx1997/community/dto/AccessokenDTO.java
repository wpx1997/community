package life.wpx1997.community.dto;

import lombok.Data;

/**
 * @author 不会飞的小鹏
 */
@Data
public  class AccessokenDTO {
    private String client_id;
    private String client_secret;
    private String code;
    private String redirect_uri;
    private String state;
}
