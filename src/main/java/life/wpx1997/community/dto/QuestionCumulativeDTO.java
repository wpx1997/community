package life.wpx1997.community.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.stereotype.Component;

/**
 * @author 不会飞的小鹏
 * create on 2020/7/26 12:18
 * @Description QuestionCumulativeDTO is
 */
@Getter
@Setter
@ToString
public class QuestionCumulativeDTO {
    private Long id;
    private Long viewCount;
    private Long commentCount;
    private Long likeCount;
    public QuestionCumulativeDTO(Long id){
        this.id = id;
        this.viewCount = 0L;
        this.commentCount = 0L;
        this.likeCount = 0L;
    }
}
