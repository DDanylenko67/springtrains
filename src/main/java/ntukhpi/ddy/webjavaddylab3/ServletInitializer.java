package ntukhpi.ddy.webjavaddylab3;

import jakarta.servlet.ServletContext;
import ntukhpi.ddy.webjavaddylab3.utils.SessionEventListener;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

public class ServletInitializer extends SpringBootServletInitializer {
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(WebJavaDdyLab3Application.class);
    }

}
