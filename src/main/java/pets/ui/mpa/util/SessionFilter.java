package pets.ui.mpa.util;

import java.io.IOException;
import java.util.Date;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SessionFilter implements Filter {
	private static final Logger logger = LoggerFactory.getLogger(SessionFilter.class);

	@SuppressWarnings("unused")
	private FilterConfig filterConfig = null;

	public void init(FilterConfig filterConfig) throws ServletException {
		this.filterConfig = filterConfig;
	}

	public void destroy() {
		this.filterConfig = null;
	}

	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain)
			throws IOException, ServletException {

		Date startTime;
		double totalTime;
		String inUri;

		startTime = new Date();
		inUri = ((HttpServletRequest) servletRequest).getRequestURI();
		
		chain.doFilter(servletRequest, servletResponse);

		totalTime = new Date().getTime() - startTime.getTime();
		totalTime = totalTime / 1000;

		logger.info("Total elapsed time is: " + totalTime + " seconds inUri: " + inUri);
	}
}
