package codesquad.server.template;

import codesquad.codestagram.domain.article.domain.Article;
import codesquad.codestagram.domain.comment.domain.Comment;
import codesquad.codestagram.domain.user.domain.User;
import java.util.List;

public class TemplateHTMLGenerator {

    private TemplateHTMLGenerator() {
    }

    public static String anonymousUserHeader() {
        return """
            <header class="header">
              <a href="/"><img src="./img/signiture.svg"/></a>
              <ul class="header__menu">
                <li class="header__menu__item">
                  <a class="btn btn_contained btn_size_s" href="/login">로그인</a>
                </li>
                <li class="header__menu__item">
                  <a class="btn btn_ghost btn_size_s" href="/registration">
                    회원 가입
                  </a>
                </li>
              </ul>
            </header>
            """;
    }

    public static String loginUserHeader(String username) {
        return """
              <header class="header">
                <a href="/main"><img src="../img/signiture.svg"/></a>
                <ul class="header__menu">
                  <li class="header__menu__item">
                    <a class="btn btn_ghost btn_size_s">{{username}} 님 안녕하세요!</a>
                  </li>
                  <li class="header__menu__item">
                    <a class="btn btn_contained btn_size_s" href="/user/list">유저 조회</a>
                  </li>
                  <li class="header__menu__item">
                    <a class="btn btn_contained btn_size_s" href="/article">글쓰기</a>
                  </li>
                  <li class="header__menu__item">
                    <form action="/logout" method="post">
                      <button type="submit" id="logout-btn" class="btn btn_ghost btn_size_s">
                        로그아웃
                      </button>
                    </form>
                  </li>
                </ul>
              </header>
            """.replace("{{username}}", username);
    }

    public static String post(Article article) {
        return "<div class=\"post\">\n"
            + "      <div class=\"post__account\">\n"
            + "        <img class=\"post__account__img\"/>\n"
            + "        <p class=\"post__account__nickname\">" + article.getUsername() + "</p>\n"
            + "      </div>\n"
            + "      <img class=\"post__img\"/>\n"
            + "      <div class=\"post__menu\">\n"
            + "        <ul class=\"post__menu__personal\">\n"
            + "          <li>\n"
            + "            <button class=\"post__menu__btn\">\n"
            + "              <img src=\"./img/like.svg\"/>\n"
            + "            </button>\n"
            + "          </li>\n"
            + "          <li>\n"
            + "            <button class=\"post__menu__btn\">\n"
            + "              <img src=\"./img/sendLink.svg\"/>\n"
            + "            </button>\n"
            + "          </li>\n"
            + "        </ul>\n"
            + "        <button class=\"post__menu__btn\">\n"
            + "          <img src=\"./img/bookMark.svg\"/>\n"
            + "        </button>\n"
            + "      </div>\n"
            + "      <p class=\"post\">\n"
            + "        <b>제목: " + article.getTitle() + "</b>\n"
            + "      </p>"
            + "      <p class=\"post__article\">\n"
            + article.getBody()
            + "\n      </p>\n"
            + "    </div>\n";
    }

    public static String navigationBar(Article article) {
        return "    <nav class=\"nav\">\n"
            + "      <ul class=\"nav__menu\">\n"
            + "        <li class=\"nav__menu__item\">\n"
            + "          <a class=\"nav__menu__item__btn\" href=\"index.html?id=" + (
            article.getId() - 1 < 0 ? 1 : article.getId() - 1) + "\">\n"
            + "            <img\n"
            + "                class=\"nav__menu__item__img\"\n"
            + "                src=\"./img/ci_chevron-left.svg\"\n"
            + "            />\n"
            + "            이전 글\n"
            + "          </a>\n"
            + "        </li>\n"
            + "        <li class=\"nav__menu__item\">\n"
            + "          <a class=\"btn btn_ghost btn_size_m\" href=\"/write\">글쓰기</a>\n"
            + "        </li>\n"
            + "        <li class=\"nav__menu__item\">\n"
            + "          <a class=\"btn btn_ghost btn_size_m\" href=\"/comment?articleId="
            + article.getId() + "\">댓글 작성</a>\n"
            + "        </li>\n"
            + "        <li class=\"nav__menu__item\">\n"
            + "          <a class=\"nav__menu__item__btn\" href=\"index.html?id=" + (article.getId()
            + 1) + "\">\n"
            + "            다음 글\n"
            + "            <img\n"
            + "                class=\"nav__menu__item__img\"\n"
            + "                src=\"./img/ci_chevron-right.svg\"\n"
            + "            />\n"
            + "          </a>\n"
            + "        </li>\n"
            + "      </ul>\n"
            + "    </nav>";
    }

    public static String commentList(List<Comment> comments) {
        StringBuilder value = new StringBuilder(" <ul class=\"comment\">\n");
        for (Comment comment : comments) {
            value.append(commentSingle(comment));
        }
        value.append("    </ul>\n");
        return value.toString();
    }

    public static String userList(List<User> users) {
        StringBuilder value = new StringBuilder();
        for (User user : users) {
            value.append("<tr>\n<th class=\"btn btn_size_s btn_ghost\">").append(user.getUserId())
                .append("</th>\n<th class=\"btn btn_size_s btn_ghost\">").append(user.getName())
                .append("</th>\n</tr>\n");
        }
        return value.toString();
    }

    private static String commentSingle(Comment comment) {
        return "      <li class=\"comment__item\">\n"
            + "        <div class=\"comment__item__user\">\n"
            + "          <img class=\"comment__item__user__img\"/>\n"
            + "          <p class=\"comment__item__user__nickname\">" + comment.getUsername()
            + "</p>\n"
            + "        </div>\n"
            + "        <p class=\"comment__item__article\">\n"
            + comment.getBody()
            + "\n        </p>\n"
            + "      </li>\n";
    }
}
