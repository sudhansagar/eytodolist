package com.ey.todo.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import com.ey.todo.dao.Tasks;
import com.ey.todo.dao.Users;
import com.ey.todo.services.ResourceService;

@Controller
@SessionAttributes("userObj")
public class AppController {

	@Autowired
	private ResourceService resourceService;

	private static final Logger LOGGER = LoggerFactory.getLogger(AppController.class);

	@ModelAttribute("Users")
	public Users user() {
		return new Users();
	}

	@RequestMapping("/")
	public String homePage() {
		return "login";
	}

	@RequestMapping("/new/{id}")
	public String addNewTask(Model model, @PathVariable(name = "id") String id) {
		LOGGER.info("------------Inside addNewTask()---------------");
		try {
			Tasks tasks = new Tasks();
			LOGGER.debug("Inside :::" + id);
			model.addAttribute("task", tasks);
		} catch (Exception e) {
			throw new RuntimeException();
		}
		LOGGER.info("------------Exiting addNewTask()---------------");

		return "addtask";
	}

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String login(ModelMap model, @RequestParam String username, @RequestParam String password) {
		LOGGER.info("------------Inside login()---------------");
		try {
			int userID = resourceService.validateUsers(username, password);
			LOGGER.debug("UserID is::" + userID);
			if (userID == 0) {
				model.put("errorMessage", "Invalid Credentials");
				return "login";
			} else {
				model.addAttribute("userObj", userID);
			}
		} catch (Exception ex) {
			throw new RuntimeException();
		}
		LOGGER.info("------------Exiting login()---------------");

		return "home";
	}

	@RequestMapping("/viewtask/{id}")
	public String viewTasks(ModelMap model, @PathVariable(name = "id") String id) {
		LOGGER.info("------------Inside viewTasks()---------------");
		LOGGER.debug("user loggedin id:::" + id);
		
		try {
			List<Tasks> listtasks = resourceService.getTask(Integer.parseInt(id));
			LOGGER.debug("Task size::" + listtasks.size());
			int userID = Integer.parseInt(id);
			model.put("userObj", userID);
			model.put("listtasks", listtasks);
			model.get("userObj");
		} catch (Exception e) {
			throw new RuntimeException();
		}
		LOGGER.info("------------Exiting viewTasks()---------------");

		return "viewtask";
	}

	@RequestMapping(value = "/save/{id}", method = RequestMethod.POST)
	public String addTask(ModelMap model, @ModelAttribute("task") Tasks task, @PathVariable(name = "id") String id) {
		LOGGER.info("------------Inside addTask()---------------");
		try {
			LOGGER.debug("task description" + task.getTaskDescription());
			LOGGER.debug("UserID::" + Integer.parseInt(id));
			task.setUserId(Integer.parseInt(id));
			resourceService.addTask(task);
			model.put("userObj", Integer.parseInt(id));
		} catch (Exception e) {
			throw new RuntimeException();
		}
		LOGGER.info("------------Exiting addTask()---------------");

		return "home";
	}

	@RequestMapping("/edit/{taskid}/{userid}")
	public ModelAndView showEdittask(@PathVariable(name = "taskid") String taskid,
			@PathVariable(name = "userid") String userid) {
		LOGGER.info("------------Inside showEdittask()---------------");

		ModelAndView mav = new ModelAndView("edit");
		try {
			LOGGER.debug("Tasks id::" + taskid);
			LOGGER.debug("User id::" + userid);
			Tasks task = resourceService.gettask(Integer.parseInt(taskid));
			mav.addObject("userObj", Integer.parseInt(userid));
			mav.addObject("task", task);
		} catch (Exception e) {
			throw new RuntimeException();
		}
		LOGGER.info("------------Existing showEdittask()---------------");

		return mav;

	}

	@RequestMapping(value = "/update/{id}", method = RequestMethod.POST)
	public String updateTask(ModelMap model, @RequestParam String taskId, @RequestParam String taskDesc,
			@RequestParam(required = false) String checkbox,@PathVariable(name ="id") String id) {
		LOGGER.info("------------Inside updateTask()---------------");
	
		try {
			LOGGER.debug("Inside update tasks:::::::::");
			LOGGER.debug("Description id::" + taskDesc);
			LOGGER.debug("Tasks id::" + taskId);
			LOGGER.debug("Status id::" + checkbox);
			LOGGER.debug("Users id::" + id);
			if(checkbox==null){
				checkbox="FALSE";
			}else {
			checkbox="TRUE";
			}
			LOGGER.debug("CheckBOX::"+checkbox);
		    resourceService.updateTask(taskDesc,Integer.parseInt(taskId),checkbox);
		    LOGGER.debug("updated Task successfully::");
		} catch (Exception e) {
			e.printStackTrace();
		}
		LOGGER.info("------------Exiting updateTask()---------------");

		return "home";
	}

	@RequestMapping("/delete/{id}")
	public String deleteProduct(@PathVariable(name = "id") String id) {
		LOGGER.info("------------inside deleteProduct()---------------");
		try {
			LOGGER.debug("Delete id:::" + id);
			resourceService.delete(Integer.parseInt(id));
			LOGGER.debug("Successfully deleted record for task Id:::" + id);
		} catch (Exception e) {
			throw new RuntimeException();
		}		
		LOGGER.info("------------exiting deleteProduct()---------------");
		return "home";
	}

	
	 @ExceptionHandler(value=RuntimeException.class) 
	 public String	 exceptionHandler()
	 {
		 return "error";
	 }
	
}
