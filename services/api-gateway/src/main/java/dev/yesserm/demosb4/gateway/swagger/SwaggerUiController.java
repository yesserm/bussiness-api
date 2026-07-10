package dev.yesserm.demosb4.gateway.swagger;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
class SwaggerUiController {

    @GetMapping(path = {"/swagger-ui.html", "/swagger-ui/index.html"}, produces = MediaType.TEXT_HTML_VALUE)
    String swaggerUi() {
        return """
                <!doctype html>
                <html lang="en">
                <head>
                  <meta charset="utf-8">
                  <meta name="viewport" content="width=device-width, initial-scale=1">
                  <title>demosb4 Swagger UI</title>
                  <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/swagger-ui-dist@5/swagger-ui.css">
                </head>
                <body>
                  <div id="swagger-ui"></div>
                  <script src="https://cdn.jsdelivr.net/npm/swagger-ui-dist@5/swagger-ui-bundle.js"></script>
                  <script>
                    window.ui = SwaggerUIBundle({
                      dom_id: '#swagger-ui',
                      urls: [
                        { name: 'auth-service', url: '/v3/api-docs/auth' },
                        { name: 'user-service', url: '/v3/api-docs/users' },
                        { name: 'business-service', url: '/v3/api-docs/business' }
                      ],
                      deepLinking: true,
                      presets: [SwaggerUIBundle.presets.apis],
                      layout: 'StandaloneLayout'
                    });
                  </script>
                </body>
                </html>
                """;
    }
}
