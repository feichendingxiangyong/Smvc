package com.smvc.framework.test;

import java.util.Map;

import org.apache.log4j.Logger;

import com.smvc.framework.mvc.annotation.Controller;
import com.smvc.framework.mvc.annotation.RequestMapping;
//requestmapping = testController.do
@Controller
@RequestMapping(value={"testController/*"})
public class TestController {
	/**
	 * logger
	 */
	public static final Logger logger = Logger.getLogger(TestController.class);
	
	@RequestMapping(value={"test.do"}, params = {"method=test"})
	public String test(UserModel userModel, Map<String, Object> resultMap)
	{
	    if (userModel != null)
	    {
	        logger.debug("Hello world, this is SMVC! And my name is [" + userModel.getUserName() + "].");
	        logger.debug("Hello world, this is SMVC! And my psw is [" + userModel.getPsw() + "].");
	        
	        //set result to result map
	        resultMap.put("userName", userModel.getUserName());
	        resultMap.put("psw", userModel.getPsw());
	    }
	    
		return "success";
	}
}
