package life.wpx1997.community.dto;

import lombok.Data;

/**
 * @author 不会飞的小鹏
 */
@Data
public class GitHubUser {
    private String name;
    private Long id;
    private String bio;
    private String avatar_url;

    @Override
    public String toString() {
        return "GithubUser{" +
                "name='" + name + '\'' +
                ", id=" + id +
                ", bio='" + bio + '\'' +
                '}';
    }
}
