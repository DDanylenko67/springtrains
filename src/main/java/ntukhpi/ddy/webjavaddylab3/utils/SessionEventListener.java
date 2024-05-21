package ntukhpi.ddy.webjavaddylab3.utils;

import jakarta.servlet.annotation.WebListener;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.HttpSessionEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.web.context.support.WebApplicationContextUtils;
@WebListener
public class SessionEventListener extends HttpSessionEventPublisher {


    @Override
    public void sessionCreated(HttpSessionEvent event) {
        super.sessionCreated(event);
        event.getSession().setMaxInactiveInterval(60 * 10);
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent event) {
        SessionRegistry sessionRegistry = getBean(event, "sessionRegistry");
        SessionInformation sessionInfo = (sessionRegistry != null)
                ? sessionRegistry.getSessionInformation(event.getSession().getId())
                : null;

        if (sessionInfo != null) {
            UserDetails userDetails = (UserDetails) sessionInfo.getPrincipal();
            String username = userDetails.getUsername();
        }
        super.sessionDestroyed(event);
    }

    private SessionRegistry getBean(HttpSessionEvent event, String beanName) {
        try {
            ApplicationContext ctx = WebApplicationContextUtils
                    .getWebApplicationContext(event.getSession().getServletContext());
            return (SessionRegistry) ctx.getBean(beanName);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}