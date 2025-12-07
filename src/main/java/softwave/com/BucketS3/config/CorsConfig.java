package softwave.com.BucketS3.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;
import java.util.List;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // Lê origens permitidas de variável de ambiente ou usa valores padrão para desenvolvimento
        String allowedOriginsEnv = System.getenv("CORS_ALLOWED_ORIGINS");
        String[] allowedOrigins;
        
        if (allowedOriginsEnv != null && !allowedOriginsEnv.isEmpty()) {
            // Suporta múltiplas origens separadas por vírgula
            allowedOrigins = allowedOriginsEnv.split(",");
        } else {
            // Valores padrão para desenvolvimento local
            allowedOrigins = new String[]{
                "http://localhost:5173",
                "http://localhost:3000",
                "http://localhost:8080",
                "http://52.3.112.88:80",
                "http://52.3.112.88",
                "http://52.3.112.88:8080"
            };
        }
        
        registry.addMapping("/**")
                .allowedOrigins(allowedOrigins)
                .allowedMethods(
                        HttpMethod.GET.name(),
                        HttpMethod.POST.name(),
                        HttpMethod.DELETE.name(),
                        HttpMethod.OPTIONS.name(),
                        HttpMethod.PUT.name(),
                        HttpMethod.PATCH.name()
                )
                .allowedHeaders(
                        "Authorization",
                        "Content-Type",
                        "X-Requested-With",
                        "Accept",
                        "Origin",
                        "Access-Control-Request-Method",
                        "Access-Control-Request-Headers"
                )
                .exposedHeaders(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "Authorization",
                        "Content-Type"
                )
                .allowCredentials(true)
                .maxAge(3600);
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:5173")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true)
                .exposedHeaders("Set-Cookie"); // Permite exposição de cookies
    }
}

