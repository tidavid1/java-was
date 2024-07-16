package codesquad.codestagram.endpoint;

import codesquad.codestagram.domain.article.domain.Article;
import codesquad.codestagram.domain.article.storage.ArticleDao;
import codesquad.codestagram.domain.comment.domain.Comment;
import codesquad.codestagram.domain.comment.storage.CommentDao;
import codesquad.codestagram.domain.user.domain.User;
import codesquad.codestagram.domain.user.storage.UserDao;
import codesquad.server.endpoint.EndPoint;
import codesquad.server.endpoint.EndPointRegister;
import codesquad.server.endpoint.EndPointStorage;
import codesquad.server.http.exception.HttpCommonException;
import codesquad.server.http.servlet.HttpServletRequest;
import codesquad.server.http.servlet.HttpServletResponse;
import codesquad.server.http.servlet.enums.HttpMethod;
import codesquad.server.http.servlet.enums.StatusCode;
import codesquad.server.http.servlet.values.HttpQueryParams;
import codesquad.server.http.session.Session;
import codesquad.server.http.session.SessionContext;
import codesquad.server.template.TemplateHTMLFactory;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;

public class GetEndPointRegister implements EndPointRegister {

    private final EndPointStorage endPointStorage;
    private final TemplateHTMLFactory templateHtmlFactory;
    private final UserDao userDao;
    private final ArticleDao articleDao;
    private final CommentDao commentDao;

    private GetEndPointRegister(EndPointStorage endPointStorage,
        TemplateHTMLFactory templateHtmlFactory,
        UserDao userDao, ArticleDao articleDao, CommentDao commentDao) {
        this.endPointStorage = endPointStorage;
        this.templateHtmlFactory = templateHtmlFactory;
        this.userDao = userDao;
        this.articleDao = articleDao;
        this.commentDao = commentDao;
    }

    @Override
    public void provideAll() {
        String mainPagePath = "/index.html";
        String writePagePath = "/write.html";
        registerEndPoint(mainPagePath, this::handleHome);
        registerRedirectEndPoint("/", mainPagePath);
        registerRedirectEndPoint("/main", mainPagePath);
        registerRedirectEndPoint("/main/index.html", mainPagePath);
        registerEndPoint("/registration/index.html", this::handleRegistration);
        registerRedirectEndPoint("/registration", "/registration/index.html");
        registerEndPoint("/user/list", this::handleUserList);
        registerRedirectEndPoint("/user/index.html", "/user/list");
        registerEndPoint("/login/index.html", this::handleLogin);
        registerRedirectEndPoint("/login", "/login/index.html");
        registerEndPoint("/login/login_failed.html", this::handleLoginFail);
        registerEndPoint(writePagePath, this::handleWrite);
        registerRedirectEndPoint("/write", writePagePath);
        registerRedirectEndPoint("/write/index.html", writePagePath);
        registerRedirectEndPoint("/article", writePagePath);
        registerRedirectEndPoint("/article/index.html", writePagePath);
        registerEndPoint("/comment", this::handleComment);
        registerRedirectEndPoint("/comment/index.html", "/comment");
    }

    public void handleHome(HttpServletRequest request, HttpServletResponse response) {
        HttpQueryParams queryParams = request.getRequest().getRequestLine().getQueryParams();
        Long id = Long.parseLong(queryParams.getParameter("id").orElse("1"));
        Session session = SessionContext.getSession();
        Optional<Article> optionalArticle = articleDao.findById(id);
        optionalArticle.ifPresentOrElse(
            article -> {
                List<Comment> comments = commentDao.findAllByArticleId(article.getId());
                byte[] data = session == null ? templateHtmlFactory.mainPage(article, comments)
                    : templateHtmlFactory.mainPage(
                        (User) session.getAttribute("user"), article, comments);
                setHtmlResponse(response, data);
            },
            () -> setException(request, "아티클을 찾을 수 없습니다.", StatusCode.BAD_REQUEST)
        );
    }

    public void handleRegistration(HttpServletRequest request, HttpServletResponse response) {
        Session session = SessionContext.getSession();
        if (session == null) {
            redirectToMain(response);
            return;
        }
        setHtmlResponse(response, templateHtmlFactory.registrationPage());
    }

    public void handleUserList(HttpServletRequest request, HttpServletResponse response) {
        User user = (User) SessionContext.getSession().getAttribute("user");
        List<User> users = userDao.findAll();
        setHtmlResponse(response, templateHtmlFactory.userListPage(user, users));
    }

    public void handleLogin(HttpServletRequest request, HttpServletResponse response) {
        if (SessionContext.getSession() != null) {
            redirectToMain(response);
            return;
        }
        setHtmlResponse(response, templateHtmlFactory.loginPage());
    }

    public void handleLoginFail(HttpServletRequest request, HttpServletResponse response) {
        if (SessionContext.getSession() != null) {
            redirectToMain(response);
            return;
        }
        setHtmlResponse(response, templateHtmlFactory.loginFailPage());
    }

    public void handleWrite(HttpServletRequest request, HttpServletResponse response) {
        User user = (User) SessionContext.getSession().getAttribute("user");
        setHtmlResponse(response, templateHtmlFactory.articlePage(user));
    }

    public void handleComment(HttpServletRequest request, HttpServletResponse response) {
        HttpQueryParams queryParams = request.getRequest().getRequestLine().getQueryParams();
        try {
            Long articleId = Long.parseLong(queryParams.getParameter("articleId")
                .orElseThrow(() -> new IllegalArgumentException("댓글을 달 아티클이 존재하지 않습니다.")));
            Article article = articleDao.findById(articleId)
                .orElseThrow(() -> new IllegalArgumentException("댓글을 달 아티클이 존재하지 않습니다."));
            User user = (User) SessionContext.getSession().getAttribute("user");
            setHtmlResponse(response, templateHtmlFactory.commentPage(user, article));
        } catch (IllegalArgumentException e) {
            setException(request, e.getMessage(), StatusCode.NOT_FOUND);
        }
    }

    private void registerEndPoint(String path,
        BiConsumer<HttpServletRequest, HttpServletResponse> biConsumer) {
        endPointStorage.addEndpoint(HttpMethod.GET, EndPoint.of(path, biConsumer));
    }

    private void registerRedirectEndPoint(String path, String redirectPath) {
        EndPoint endPoint = EndPoint.of(path, (req, res) -> res.sendRedirect(redirectPath));
        endPointStorage.addEndpoint(HttpMethod.GET, endPoint);
    }

    private void setException(HttpServletRequest request, String message, StatusCode statusCode) {
        request.setAttribute("exception", new HttpCommonException(message, statusCode));
    }

    private void redirectToMain(HttpServletResponse httpServletResponse) {
        httpServletResponse.sendRedirect("/index.html");
    }

    private void setHtmlResponse(HttpServletResponse response, byte[] body) {
        response.setStatus(StatusCode.OK);
        response.setHeader("Content-Type", "text/html");
        response.setBody(body);
    }

}
