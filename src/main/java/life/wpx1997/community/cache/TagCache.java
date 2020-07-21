package life.wpx1997.community.cache;

import life.wpx1997.community.dto.TagDTO;
import org.apache.commons.lang3.StringUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * created on 2019/8/25 21:58
 * @author 不会飞的小鹏
 */
public class TagCache {
    public static List<TagDTO> get() {
        ArrayList<TagDTO> tagDTOList = new ArrayList<>();

        TagDTO program = new TagDTO();
        program.setCategoryName("开发语言");
        program.setTags(Arrays.asList("Java", "C++", "C", "PHP", "css", "node.js", "python", "JavaScript", "HTML", "HTML5", "golang", "objective-c", "typescript", "shell", "swift", "sass", "ruby", "bash", "less", "asp.net", "lua", "scala", "coffeescript", "actionscript", "rust", "erlang", "perl"));
        tagDTOList.add(program);

        TagDTO framework = new TagDTO();
        framework.setCategoryName("平台框架");
        framework.setTags(Arrays.asList("laravel", "Spring", "express", "django", "flask", "yii", "ruby-on-rails", "tornado", "koa", "struts"));
        tagDTOList.add(framework);

        TagDTO server = new TagDTO();
        server.setCategoryName("服务器");
        server.setTags(Arrays.asList("linux", "nginx", "docker", "apache", "ubuntu", "centos", "缓存", "tomcat", "负载均衡", "unix", "hadoop", "windows-server"));
        tagDTOList.add(server);

        TagDTO db = new TagDTO();
        db.setCategoryName("数据库和缓存");
        db.setTags(Arrays.asList("mysql", "redis", "mongodb", "sql", "oracle", "nosql", "memcached", "sqlserver", "postgresql", "sqlite"));
        tagDTOList.add(db);

        TagDTO tool = new TagDTO();
        tool.setCategoryName("开发工具");
        tool.setTags(Arrays.asList("git", "github", "visual-studio-code", "vim", "sublime-text", "xcode", "intellij-idea", "eclipse", "maven", "ide", "svn", "visual-studio", "atom", "emacs", "textmate", "hg"));
        tagDTOList.add(tool);

        return tagDTOList;
    }

    public static String filterInvalid(String tags) {
        String[] split = StringUtils.split(tags, "，");
        List<TagDTO> tagDTOList = get();

        List<String> tagList = tagDTOList.stream().flatMap(tag -> tag.getTags().stream()).collect(Collectors.toList());
        String invalid = Arrays.stream(split).filter(t -> !tagList.contains(t)).collect(Collectors.joining("，"));

        return invalid;

    }

}