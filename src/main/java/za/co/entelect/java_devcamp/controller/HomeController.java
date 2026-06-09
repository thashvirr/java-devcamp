package za.co.entelect.java_devcamp.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

	@GetMapping("/")
	public String home() {
		logger.info("Home page requested");
		logger.error("Error occurred");
		logger.warn("Warning occurred");
		logger.debug("Debug message");
		logger.trace("Trace message");
		return "index";
	}

}
