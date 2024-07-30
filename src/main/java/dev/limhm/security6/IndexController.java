package dev.limhm.security6;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IndexController {

  @GetMapping("/")
  public String index(String customParam) {
    if (customParam != null) {
      return "customPage";
    } else {
      return "index";
    }
  }

  @GetMapping("/home")
  public String home() {
    return "home";
  }

  @GetMapping("/loginPage")
  public String loginPage() {
    return "loginPage";
  }

  @GetMapping("/anonymous")
  public String anonymous() {
    return "anonymous";
  }

  @GetMapping("/anonymousContext")
  public String anonymousContext(@CurrentSecurityContext SecurityContext context) {
    return context.getAuthentication().getName();
  }

  @GetMapping("/authentication")
  public String authentication(Authentication authentication) {  // 인증을 받았다면, 인증 받은 사용자의 객체가 들어온다. 인증 받지 못한 사용자는 null 이 들어온다.
    if (authentication instanceof AnonymousAuthenticationToken) {
      return "anonymous";
    } else {
      return "not anonymous";
    }
  }

  @GetMapping("/logoutSuccess")
  public String logoutSuccess() {
    return "logoutSuccess";
  }
}
