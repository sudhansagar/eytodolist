package com.ey.todo.services;

import java.sql.Timestamp;
import java.util.List;

import com.ey.todo.controller.AppController;
import com.ey.todo.dao.Tasks;
import com.ey.todo.dao.Users;
import com.ey.todo.repo.TaskRepo;
import com.ey.todo.repo.UsersRepo;
import com.ey.todo.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ResourceService {

	private static final Logger LOGGER = LoggerFactory.getLogger(ResourceService.class);

	@Autowired
    UsersRepo usersRepo;
	
	@Autowired
    TaskRepo taskRepo;
	
	
	public int validateUsers(String username,String pwd) {
		int userID=0;
		List<Users>	users=usersRepo.findByUserName(username,pwd);
		if(users!=null && !users.isEmpty()) {
			 userID=users.get(0).getId();
		}
		return userID;
	}

	public List<Tasks> getTask(int userId) {
		List<Tasks> tasks=taskRepo.findAllByUserID(userId);
	for(int i=0;i<tasks.size();i++) {
		LOGGER.debug("Tasks:"+tasks.get(i));
	}
		LOGGER.debug("GetTask.........");
		return tasks;
	}

	public void addTask(Tasks task)   {		
		task.setTaskDescription(task.getTaskDescription());	
		task.setFlag(task.getFlag());
		
		LOGGER.debug("Inside addTasks:::::"+task.getFlag());
		task.setUserId(task.getUserId());
		Timestamp currdate= Utils.getCurrentTimeStamp();
		taskRepo.saveTask(task.getTaskDescription(), currdate, task.getFlag(),task.getUserId());
		LOGGER.debug("Inserted into Task table...");
	}

	public void removeTask(Tasks tasks) {
		taskRepo.delete(tasks);
	}

	public void delete(int id) {
		taskRepo.deleteById(id);
	}

	public  Tasks gettask(int id) {
		// TODO Auto-generated method stub
		return taskRepo.findById(id).get();
	}

	public void updateTask(String taskDesc, int taskId, String status) {
		taskRepo.updateTask(taskDesc, taskId, status);
	}
}
