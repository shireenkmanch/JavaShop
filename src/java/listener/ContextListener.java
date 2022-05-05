package listener;


import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.apache.log4j.PropertyConfigurator;

@WebListener("application context listener")
public class ContextListener implements ServletContextListener {

	/**
	 * Initialize log4j when the application is being started
	 */
	@Override
	public void contextInitialized(ServletContextEvent event) {
		
		// initialize log4j here
		PropertyConfigurator.configure(ContextListener.class.getClassLoader().getResourceAsStream("log4j.properties"));

	}

	@Override
	public void contextDestroyed(ServletContextEvent event) {
		// do nothing
	}
}