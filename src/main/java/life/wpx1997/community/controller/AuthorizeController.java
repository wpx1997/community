package life.wpx1997.community.controller;

import life.wpx1997.community.dto.AccessokenDTO;
import life.wpx1997.community.dto.GithubUser;
import life.wpx1997.community.mapper.UserMapper;
import life.wpx1997.community.model.User;
import life.wpx1997.community.provider.GithubProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Controller
public class AuthorizeController {
    @Autowired
    private GithubProvider githubProvider;

    @Value("${github.client.id}")
    private String clientId;

    @Value("${github.client.secret}")
    private String clientSecret;

    @Value("${github.redirect.uri}")
    private String redirectUri;

    @Autowired
    private UserMapper userMapper;

    @GetMapping("/callback")
    public String callback(@RequestParam(name = "code") String code, @RequestParam(name = "state") String state, HttpServletRequest request, HttpServletResponse response){
        AccessokenDTO accessokenDTO = new AccessokenDTO();
        accessokenDTO.setClient_id(clientId);
        accessokenDTO.setClient_secret(clientSecret);
        accessokenDTO.setCode(code);
        accessokenDTO.setRedirect_uri(redirectUri);
        accessokenDTO.setState(state);
        String accessToken = githubProvider.getAccessToken(accessokenDTO);
        GithubUser githubUser = githubProvider.getUser(accessToken);

        if(githubUser != null){
            User user = new User();
            String token = UUID.randomUUID().toString();
            user.setToken(token);
            user.setName(githubUser.getName());
            user.setAccountId(String.valueOf(githubUser.getId()));
            user.setGmtCreate(System.currentTimeMillis());
            user.setGmtModified(user.getGmtCreate());

            //登录成功，写cookie和session
            userMapper.insert(user);
            response.addCookie(new Cookie("token",token));
            return "redirect:/";
        }else {
            //登录失败，重新登录
            return "redirect:/";
        }
    }
}
