package life.wpx1997.community.controller;

import life.wpx1997.community.dto.AccessokenDTO;
import life.wpx1997.community.dto.GitHubUser;
import life.wpx1997.community.model.User;
import life.wpx1997.community.provider.GitHubProvider;
import life.wpx1997.community.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

/**
 * @author 小case
 */
@Controller
@Slf4j
public class AuthorizeController {
    @Autowired
    private GitHubProvider gitHubProvider;

    @Value("${github.client.id}")
    private String clientId;

    @Value("${github.client.secret}")
    private String clientSecret;

    @Value("${github.redirect.uri}")
    private String redirectUri;

    @Autowired
    private UserService userService;

    @GetMapping("/callback")
    public String callback(@RequestParam(name = "code") String code,
                           @RequestParam(name = "state") String state,
                           HttpServletResponse response){
        AccessokenDTO accessokenDTO = new AccessokenDTO();
        accessokenDTO.setClient_id(clientId);
        accessokenDTO.setClient_secret(clientSecret);
        accessokenDTO.setCode(code);
        accessokenDTO.setRedirect_uri(redirectUri);
        accessokenDTO.setState(state);
        String accessToken = gitHubProvider.getAccessToken(accessokenDTO);
        GitHubUser gitHubUser = gitHubProvider.getUser(accessToken);

        if(gitHubUser != null){
            User user = new User();
            String token = UUID.randomUUID().toString();
            user.setToken(token);
            user.setName(gitHubUser.getName());
            user.setAccountId(String.valueOf(gitHubUser.getId()));
            user.setAvatarUrl(gitHubUser.getAvatar_url());

            userService.createOrUpdate(user);

            response.addCookie(new Cookie("token",token));
            return "redirect:/";
        }else {
            log.error("callback get github error,{}", (Object) null);
            //登录失败，重新登录
            return "redirect:/";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request,
                         HttpServletResponse response){
        request.getSession().removeAttribute("user");
        Cookie cookie = new Cookie("token",null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return "redirect:/";
    }


}
