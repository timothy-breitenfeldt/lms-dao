package com.smoothstack.lms.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.smoothstack.lms.dao.AuthorDAO;
import com.smoothstack.lms.dao.BookDAO;
import com.smoothstack.lms.dao.PublisherDAO;
import com.smoothstack.lms.entity.Author;
import com.smoothstack.lms.entity.Book;
import com.smoothstack.lms.entity.Publisher;

public class AdminService {

    public List<Author> getAuthors() throws SQLException {
        List<Author> authors = new ArrayList<>();
        Connection connection = null;

        try {
            connection = DatabaseUtil.getConnection();
            AuthorDAO adao = new AuthorDAO(connection);
            authors = adao.getAuthors();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            System.out.println("Reading authors faiiled");
        } finally {
            if (connection != null) {
                connection.close();
            }
        }

        return authors;
    }

    public String addBook(String title, int publisherId) throws SQLException {
        Connection connection = null;
        
        try {
            connection = DatabaseUtil.getConnection();
            BookDAO bookDAO = new BookDAO(connection);
            PublisherDAO publisherDAO = new PublisherDAO(connection);
            Publisher publisher = publisherDAO.getPublisher(publisherId);

            if (publisher == null) {
                return "Invalid publisher ID.";
            }
            
            bookDAO.insertBook(title, publisherId);
            
            connection.commit();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            System.out.println("Adding Book failed");
            
            if (connection != null) {
                connection.rollback();
            }
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
        
        return "Book added successfully";
    }
    
    public String deleteBook(int bookId) throws SQLException {
        Connection connection = null;
        
        try {
            connection = DatabaseUtil.getConnection();
            BookDAO bookDAO = new BookDAO(connection);
            bookDAO.deleteBook(bookId);
            connection.commit();
    } catch(ClassNotFoundException | SQLException e) {
        e.printStackTrace();
        System.out.println("Adding Book failed");
        
        if (connection != null) {
            connection.rollback();
        }
    } finally {
        if (connection != null) {
            connection.close();
        }
    }
        
        return "Successfully deleted book.";
    }
    
    public String updateBook(int bookId, String title, int publisherId) throws SQLException {
        Connection connection = null;
        
        try {
            connection = DatabaseUtil.getConnection();
            BookDAO bookDAO = new BookDAO(connection);
            PublisherDAO publisherDAO = new PublisherDAO(connection);
            Publisher publisher = publisherDAO.getPublisher(publisherId);

            if (publisher == null) {
                return "Invalid publisher ID.";
            }
            
            bookDAO.updateBook(bookId, title, publisherId);
            connection.commit();
    } catch(ClassNotFoundException | SQLException e) {
        e.printStackTrace();
        System.out.println("Adding Book failed");
        
        if (connection != null) {
            connection.rollback();
        }
    } finally {
        if (connection != null) {
            connection.close();
        }
    }
        
        return "Successfully updated book.";
    }
    
}
