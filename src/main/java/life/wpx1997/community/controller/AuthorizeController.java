package life.wpx1997.community.controller;

import life.wpx1997.community.dto.AccessokenDTO;
import life.wpx1997.community.dto.GithubUser;
import life.wpx1997.community.provider.GithubProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

    @GetMapping("/callback")
    public String callback(@RequestParam(name = "code") String code,@RequestParam(name = "state") String state){
        AccessokenDTO accessokenDTO = new AccessokenDTO();
        accessokenDTO.setClient_id(clientId);
        accessokenDTO.setClient_secret(clientSecret);
        accessokenDTO.setCode(code);
        accessokenDTO.setRedirect_uri(redirectUri);
        accessokenDTO.setState(state);
        String accessToken = githubProvider.getAccessToken(accessokenDTO);
        GithubUser user = githubProvider.getUser(accessToken);
        System.out.println(user.getName());
        return "index";
    }
}
