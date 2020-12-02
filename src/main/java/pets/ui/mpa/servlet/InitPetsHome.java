package pets.ui.mpa.servlet;

import static pets.ui.mpa.util.ContextProvider.getApplicationContext;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import pets.ui.mpa.service.TestsService;

@WebServlet(name = "InitPetsHome", urlPatterns = { "/servlet/InitPetsHome" })
public class InitPetsHome extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse)
			throws ServletException, IOException {
		doPost(httpServletRequest, httpServletResponse);
	}

	protected void doPost(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse)
			throws ServletException, IOException {
		httpServletResponse.getWriter().write(getApplicationContext().getBean(TestsService.class).pingTest());
	}
}
