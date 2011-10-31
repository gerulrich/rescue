package net.cloudengine.web;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;

import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

/**
 * Este controller permite modificar el nivel de logging de los loggers
 * configurados en el archivo log4j.xml
 * @author German Ulrich
 *
 */
@Controller
public class LoggerController {

	private static final String CHANGED_LOGGERS = "changedLoggers";
	private static final String LOGGER_NAME = "loggerName";
	private static final String LOGGER_LEVEL = "loggerLevel";

	/**
	 * Modifica el nivel de logging de los loggers especificados.
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/admin/logger", method = RequestMethod.POST)
	public ModelAndView changeLoggers(WebRequest request) { 
		// FIXME tratar de usar un javabean en lugar de usar el request directamente.
		
		ArrayList<String> changedLoggers = new ArrayList<String>();
		
		String thisLevel = request.getParameter(LOGGER_LEVEL);
		String[] names = request.getParameterValues(LOGGER_NAME);
		if ( names != null && thisLevel != null ) {

			for (String name : names) {
				
				if ("root".equals(name)) {
					Logger rootLogger = LogManager.getRootLogger();
					Level lev = Level.toLevel(thisLevel);
					rootLogger.setLevel(lev);
					changedLoggers.add(name);
				} else {
					Logger logger = Logger.getLogger(name);
					Level lev = Level.toLevel(thisLevel);
					logger.setLevel(lev);
					changedLoggers.add(name);
				}
			}
		}
		
		ModelAndView mav = new ModelAndView(CHANGED_LOGGERS);
		mav.addObject(CHANGED_LOGGERS, changedLoggers);
		mav.addObject("level", thisLevel);
		return mav;

	}

	/**
	 * Retorna una lista con todos los loggers de log4j.
	 * @return
	 */
	@RequestMapping(value = "/admin/logger", method = RequestMethod.GET)
	public ModelAndView viewLoggers() {

		ArrayList<LoggerOption> loggerList = new ArrayList<LoggerOption>();

		// Obtengo el Root Logger

		Logger rootLogger = LogManager.getRootLogger();
		loggerList.add(new LoggerOption(rootLogger.getName(), rootLogger.getEffectiveLevel().toString(), ""));

		// All Other Loggers
		@SuppressWarnings("unchecked")
		Enumeration<Logger> e = LogManager.getCurrentLoggers();

		while (e.hasMoreElements()) {

			Logger logger = e.nextElement();

			String thisParent = "";
			if (logger.getParent() != null) {
				thisParent = logger.getParent().getName();
			}
			loggerList.add(new LoggerOption(logger.getName(), logger.getEffectiveLevel().toString(), thisParent));
			

		}
		Collections.sort(loggerList);
		
		ModelAndView mav = new ModelAndView("loggerList");
		mav.addObject("loggerList", loggerList);
		return mav;

	}
	
	
	public class LoggerOption implements Comparable<LoggerOption> {
		
		private String name;
		private String level;
		private String parent;
		
		public LoggerOption(String name, String level, String parent) {
			super();
			this.name = name;
			this.level = level;
			this.parent = parent;
		}

		public String getName() {
			return name;
		}

		public String getLevel() {
			return level;
		}

		public String getParent() {
			return parent;
		}

		@Override
		public int compareTo(LoggerOption other) {
			return name.compareTo(other.name);
		}
		
	}

}
