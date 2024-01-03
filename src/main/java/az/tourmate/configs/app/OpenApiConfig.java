package az.tourmate.configs.app;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;


@OpenAPIDefinition(
        info = @Info(
                contact = @Contact(
                        name = "tourmate",
                        email = "yusifzehmeti@gmail.com",
                        url = "https://www.tourmate.az"

                ),
                description = "Security documentation",
                title = "Title",
                version = "3.0",
                license = @License(
                        name = "License name",
                        url = "https://www.tourmate.az"

                ),
                termsOfService = "Nesede"
        ),
        servers = {
                @Server(
                        description = "local env",
                        url = "http://localhost:8080"
                ),
                @Server(
                        description = "prod env",
                        url = "https://www.tourmate.az"
                )
        },
        security = {
                @SecurityRequirement(
                        name = "bearerAuth"
                )
        }

)
@SecurityScheme(
        name = "bearerTokenAuth",
        description = "JWT",
        scheme = "bearer",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER
)
public class OpenApiConfig {


}

