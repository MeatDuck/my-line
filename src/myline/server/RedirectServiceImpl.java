package myline.server;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import myline.shared.ClientConstants;

public class RedirectServiceImpl extends HttpServlet {
    private static final String HTTP_VK_COM = "http://vk.com/app2037135";
    private static final long serialVersionUID = 5658913779884029494L;
    private static Logger LOG = Logger.getLogger(RedirectServiceImpl.class.toString());

    // oauth_token=123&oauth_verifier=123
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(true);

        String verifier = checkNull(req.getParameter("oauth_verifier"));

        LOG.info("try Save token after redirect  oauth_verifier = " + verifier);

        session.setAttribute(ClientConstants.REQUEST_VERIFIER, verifier);

        resp.sendRedirect(HTTP_VK_COM);
    }

    private String checkNull(String s) {
        if (s == null) {
            return "";
        }
        return s;
    }
}
