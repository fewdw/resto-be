import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;

@Configuration
public class SessionConfig {
    @Value("${frontend.url}")
    private String frontendUrl;

    @Bean
    public CookieSerializer cookieSerializer() {
        DefaultCookieSerializer serializer = new DefaultCookieSerializer();
        serializer.setSameSite("Lax"); // or "None" if using https
        serializer.setUseSecureCookie(true); // for HTTPS
        serializer.setDomainNamePattern("^.+?\\.(\\w+\\.[a-z]+)$"); // Allows cookies across subdomains
        return serializer;
    }

    @Bean
    public ServletWebServerFactory servletContainer() {
        TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory();
        tomcat.getSession().setTimeout(Duration.ofMinutes(30));
        return tomcat;
    }
}