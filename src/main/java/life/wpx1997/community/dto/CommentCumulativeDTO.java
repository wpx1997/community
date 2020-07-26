package life.wpx1997.community.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author 不会飞的小鹏
 * create on 2020/7/26 15:45
 * @Description CommentCumulativeDTO is
 */
@Getter
@Setter
@ToString
public class CommentCumulativeDTO {
    private Long id;
    private Long commentCount;
    private Long likeCount;
    public CommentCumulativeDTO(Long id){
        this.id = id;
        this.commentCount = 0L;
        this.likeCount = 0L;
    }
}
