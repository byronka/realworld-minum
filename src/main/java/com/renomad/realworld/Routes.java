package com.renomad.realworld;

import com.renomad.minum.templating.TemplateProcessor;
import com.renomad.minum.web.Response;
import com.renomad.minum.web.WebFramework;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

import static com.renomad.minum.web.RequestLine.Method.GET;

public class Routes {

    private static final Path templatePath = Path.of("src/main/webapp/templates/");
    private final WebFramework wf;

    public Routes(WebFramework wf) {
        this.wf = wf;
    }

    public void register() throws IOException {

        // page sections

        String headRendered = renderTemplate("head.html", Map.of());
        String headerRendered = renderTemplate("header_unauth.html", Map.of());
        String footerRendered = renderTemplate("footer.html", Map.of());

        // a helper for building the full page
        var page = new BasicPage(headRendered, headerRendered, footerRendered);

        // main sections

        String homeRendered = renderTemplate("home.html", Map.of());
        String articleRendered = renderTemplate("article.html", Map.of());
        String loginRendered = renderTemplate("login.html", Map.of());
        String registerRendered = renderTemplate("register.html", Map.of());

        // register routes

        wf.registerPath(GET, "", request -> Response.htmlOk(page.render(homeRendered)));
        wf.registerPartialPath(GET, "article", request -> Response.htmlOk(page.render(articleRendered)));
        wf.registerPath(GET, "login", request -> Response.htmlOk(page.render(loginRendered)));
        wf.registerPath(GET, "register", request -> Response.htmlOk(page.render(registerRendered)));

    }

    private static String renderTemplate(String templateName, Map<String, String> mappings) throws IOException {
        String templateString = Files.readString(templatePath.resolve(templateName));
        TemplateProcessor templateProcessor = TemplateProcessor.buildProcessor(templateString);
        return templateProcessor.renderTemplate(mappings);
    }

    private record BasicPage(String headRendered, String headerRendered, String footerRendered) {

        public String render(String renderedTemplateForMain) throws IOException {
                return renderTemplate("fullpage.html", Map.of(
                        "head", headRendered,
                        "header", headerRendered,
                        "main", renderedTemplateForMain,
                        "footer", footerRendered));
            }
        }
}
