package com.tommy.service.web.store.book.Application.Config;


import javax.servlet.*;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;
//import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

public class SpringWebInitializer implements WebApplicationInitializer {
	
	@Override
	public void onStartup(ServletContext container) throws ServletException{
		// Create the root Context
		AnnotationConfigWebApplicationContext rootContext = new AnnotationConfigWebApplicationContext();
		rootContext.register(AppConfig.class);
		
		// Manage Lifecycle of the root Context (????)
		container.addListener(new ContextLoaderListener(rootContext));
		
		// Create dispatcher context
		AnnotationConfigWebApplicationContext servletContext = new AnnotationConfigWebApplicationContext();
		servletContext.register(ServletConfig.class);
		
		// Register and map the dispatcher servlet
        ServletRegistration.Dynamic dispatcher = container.addServlet("dispatcher", new DispatcherServlet(servletContext));
        dispatcher.setLoadOnStartup(1);
        dispatcher.addMapping("/");
     }
}

/*
public class SpringWebInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {
	
    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class[] { SpringRestJwt.war.class };
    }
 
    @Override
    protected String[] getServletMappings() {
        return new String[] { "/" };
    }
 
    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class[] { AppConfig.class };
    }
}
*/