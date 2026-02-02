package com.netcracker.solutions.mkt.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigInteger;
import java.util.Date;
import java.util.Map;

@Controller
public class UserController {

    private final UserDao userDao;

    // UserDao внедряется автоматически
    @Autowired
    public UserController(UserDao userDao) {
        this.userDao = userDao;
    }
/*
    @RequestMapping(value="/users/{id}", method=RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getUser(@PathVariable("id") int id,
                                       HttpServletRequest request) {
        String currentUser = (String) request.getAttribute("authenticatedUser");
        Map<String, Object> user = new HashMap<String, Object>();
        user.put("id", id);
        user.put("name", "Elena");
        user.put("role", "Developer");
        user.put("loggedInAs", currentUser);
        return user; // сериализация в JSON через Jackson
    }
*/

    @RequestMapping(value="/employee", method=RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> getEmployee(HttpServletRequest request, @RequestBody Map<String, Object> payload) {
        String currentUser = (String) request.getAttribute("authenticatedUser");
        BigInteger userId = userDao.findUserIdByLogin(currentUser);
        Employee employee = userDao.getEmployee(userId);
        if (payload.containsKey("message")) {
            payload.put("check", "there is message");
        }
        // имитация создания пользователя
        //payload.put("status", "created");
        payload.put("object_id", employee != null ? employee.getId() : "not found");
        payload.put("name", employee != null ? employee.getName() : "not found");
        payload.put("currentUser", currentUser);
//        payload.put("stacktrace", stacktrace);
        return payload;
    }

    @RequestMapping(value="/user", method=RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> getUser(HttpServletRequest request, @RequestBody Map<String, Object> payload) {
        String currentUser = (String) request.getAttribute("authenticatedUser");
        if (payload.containsKey("message")) {
            payload.put("check", "there is message");
        }
        payload.put("currentUser", currentUser);
        payload.put("userId", userDao.findUserIdByLogin(currentUser));
/*
        CloseWOJob job = new CloseWOJob();
        payload.put("user id", job.findUser(currentUser));
*/
        return payload;
    }

    @RequestMapping(value="/test", method=RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> test(HttpServletRequest request, @RequestBody Map<String, Object> payload) {
        String currentUser = (String) request.getAttribute("authenticatedUser");
        if (payload.containsKey("message")) {
            payload.put("check", "there is message");
        }
        payload.put("currentUser", currentUser);
        return payload;
    }

    @RequestMapping(value="/operation", method=RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> operation(HttpServletRequest request, @RequestBody Map<String, Object> payload) {
        String currentUser = (String) request.getAttribute("authenticatedUser");
        Integer key = null;
        String str = "";
        if (payload.containsKey("date")) {
            str = (String) payload.get("date");
        }
        BigInteger result = userDao.putOperation(key, str, userDao.findUserIdByLogin(currentUser));
        payload.put("currentUser", currentUser);
        payload.put("result", result);
        return payload;
    }
}
