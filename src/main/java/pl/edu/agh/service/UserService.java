package pl.edu.agh.service;

import pl.edu.agh.dao.UserDAO;
import pl.edu.agh.model.User;

import java.util.List;

public class UserService {

    private UserDAO userDAO = new UserDAO();

    public List<User> getAllUsers() {
        return userDAO.getAllUsers();
    }

    public void saveUser(User user) {
        userDAO.saveUser(user);
    }

    public void saveUserFriends(User user) {
        userDAO.saveFriends(user);
    }

    public User findByName(String name) {
        return userDAO.findByName(name);
    }
    
    public User findByUrl(String url) {
    	return userDAO.findByUrl(url);
    }

}
