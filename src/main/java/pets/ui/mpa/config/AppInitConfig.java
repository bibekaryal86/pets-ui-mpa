package pets.ui.mpa.config;

import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

import org.displaytag.filter.ResponseOverrideFilter;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import pets.ui.mpa.util.SessionFilter;
import pets.ui.mpa.util.SessionListener;

public class AppInitConfig implements WebApplicationInitializer {

	@Override
	public void onStartup(ServletContext container) throws ServletException {
		AnnotationConfigWebApplicationContext rootContext = new AnnotationConfigWebApplicationContext();
		rootContext.register(SpringContextConfig.class, SpringSecurityConfig.class);
		container.addListener(new ContextLoaderListener(rootContext));
		container.addListener(new SessionListener());

		ServletRegistration.Dynamic dispatcher = container.addServlet("dispatcher", new DispatcherServlet(rootContext));
		dispatcher.setLoadOnStartup(1);
		dispatcher.addMapping("/");
		dispatcher.setInitParameter("throwExceptionIfNoHandlerFound", "true");
		
		FilterRegistration.Dynamic displayTagFilter = container.addFilter("displayTagFilter", new ResponseOverrideFilter());
        displayTagFilter.addMappingForUrlPatterns(null, false, "*.pets");
        
        // For this could just use @WebFilter annotation, check previous version of SessionFilter
        FilterRegistration.Dynamic checkSessionFilter = container.addFilter("checkSessionFilter", new SessionFilter());
        checkSessionFilter.addMappingForUrlPatterns(null, false, "*.pets");
        checkSessionFilter.addMappingForServletNames(null, false, "/servlet/*");
	}
}
