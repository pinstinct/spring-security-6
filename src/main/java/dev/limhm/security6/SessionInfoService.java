package dev.limhm.security6;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SessionInfoService {

  private final SessionRegistry sessionRegistry;

  public void sessionInfo() {
    for (Object principal : sessionRegistry.getAllPrincipals()) {
      List<SessionInformation> allSessions = sessionRegistry.getAllSessions(principal, false);
      for (SessionInformation sessionInformation : allSessions) {
        System.out.println(
            "사용자: " + principal + " 세션 ID: " + sessionInformation.getSessionId() + " 최종 요청 시간 :"
                + sessionInformation.getLastRequest());
      }
    }
  }
}
