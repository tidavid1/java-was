package codesquad.template;

import codesquad.model.User;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class HTMLConvertor {

    public byte[] renderUsername(byte[] htmlBytes, String username) {
        String htmlString = convertBytesToString(htmlBytes);
        htmlString = htmlString.replace("{{username}}", username);
        return htmlString.getBytes();
    }

    public byte[] renderUserList(byte[] htmlBytes, String username, List<User> userList) {
        String htmlString = convertBytesToString(htmlBytes);
        htmlString = htmlString.replace("{{username}}", username);
        StringBuilder sb = new StringBuilder();
        for (User user : userList) {
            sb.append("<tr>\n<th class=\"btn btn_size_s btn_ghost\">").append(user.getUserId())
                .append("</th>\n<th class=\"btn btn_size_s btn_ghost\">").append(user.getName())
                .append("</th>\n</tr>\n");
        }
        int insertPointIdx = htmlString.indexOf("</tr>") + 5;
        htmlString =
            htmlString.substring(0, insertPointIdx) + sb + htmlString.substring(insertPointIdx);
        return htmlString.getBytes();
    }

    private String convertBytesToString(byte[] bytes) {
        return new String(bytes, StandardCharsets.UTF_8);
    }

}
