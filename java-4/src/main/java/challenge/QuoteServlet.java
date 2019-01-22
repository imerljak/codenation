package challenge;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(value = "/v1/quote/*", loadOnStartup = 1)
public class QuoteServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private static final ObjectMapper MAPPER = new ObjectMapper();
	private static final QuoteDao dao = new QuoteDao();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
			Quote quote;
			String pathInfo = req.getPathInfo();
			if (pathInfo == null || pathInfo.equals("/")) {

				quote = dao.getQuote();
			} else {
				String actor = pathInfo.split("/")[1];
				quote = dao.getQuoteByActor(actor);
			}

			resp.setContentType("application/json");
			resp.getWriter().write(MAPPER.writeValueAsString(quote));
			//resp.setStatus(HttpServletResponse.SC_NOT_IMPLEMENTED);
		} catch (Exception e) {
			e.printStackTrace();
			resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
	}

}
