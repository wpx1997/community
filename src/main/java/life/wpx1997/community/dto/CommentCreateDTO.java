package life.wpx1997.community.dto;

import lombok.Data;

@Data
public class CommentCreateDTO {
    private Long questionId;
    private String content;
    private Integer type;
}
