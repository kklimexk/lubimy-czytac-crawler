package pl.edu.agh.service;

import pl.edu.agh.dao.UserDAO;
import pl.edu.agh.model.User;

import java.util.List;

public class UserService {

    private UserDAO userDAO;

    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public List<User> getAllUsers() {
        return userDAO.getAllUsers();
    }

    public void saveUser(User user) {
        userDAO.saveUser(user);
    }

}
