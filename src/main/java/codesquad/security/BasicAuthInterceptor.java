package codesquad.security;

import codesquad.domain.User;
import codesquad.dto.LoginDTO;
import codesquad.exception.UserVerificationException;
import codesquad.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.Charset;
import java.util.Base64;

public class BasicAuthInterceptor extends HandlerInterceptorAdapter {
    private static final Logger log = LoggerFactory.getLogger(BasicAuthInterceptor.class);

    @Autowired
    private UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        String authorization = request.getHeader("Authorization");
        log.debug("Authorization : {}", authorization);
        if (authorization == null || !authorization.startsWith("Basic")) {
            return true;
        }

        String base64Credentials = authorization.substring("Basic".length()).trim();
        String credentials = new String(Base64.getDecoder().decode(base64Credentials), Charset.forName("UTF-8"));
        final String[] values = credentials.split(":", 2);
        log.debug("username : {}", values[0]);
        log.debug("password : {}", values[1]);

        try {
            LoginDTO loginDTO = new LoginDTO()
                    .setEmail(values[0])
                    .setPassword(values[1]);
            User user = userService.login(loginDTO);
            log.debug("Login Success : {}", user);
            request.getSession().setAttribute(SessionUtils.USER_SESSION_KEY, user);
            return true;
        } catch (UserVerificationException e) {
            return true;
        }
    }
}
