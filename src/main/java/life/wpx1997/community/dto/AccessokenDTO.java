package life.wpx1997.community.dto;

import lombok.Data;

@Data
public  class AccessokenDTO {
    private String client_id;
    private String client_secret;
    private String code;
    private String redirect_uri;
    private String state;
}
