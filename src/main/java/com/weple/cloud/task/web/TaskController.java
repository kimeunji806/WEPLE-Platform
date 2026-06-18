package com.weple.cloud.task.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class TaskController {
    
	@GetMapping("/task")
    public String projectTaskList(Model model) {
		model.addAttribute("currentMenu", "task");
        return "weple/task/list";
    }
	
	@GetMapping("/task/insert")
    public String projectTaskListInsert(Model model) {
		model.addAttribute("currentMenu", "task");
        return "weple/task/register";
    }


}
