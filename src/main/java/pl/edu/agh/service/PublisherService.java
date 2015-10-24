package pl.edu.agh.service;

import pl.edu.agh.dao.PublisherDAO;
import pl.edu.agh.model.Publisher;

import java.util.List;

public class PublisherService {
    private PublisherDAO publisherDAO = new PublisherDAO();

    public List<Publisher> getAllPublishers() {
        return publisherDAO.getAllPublishers();
    }

    public void savePublisher(Publisher publisher) {
        publisherDAO.savePublisher(publisher);
    }
}
