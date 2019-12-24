package com.smoothstack.lms.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.smoothstack.lms.entity.Publisher;

public class PublisherDAO extends BaseDAO<Publisher> {
    
    public PublisherDAO(Connection connection) {
        super(connection);
    }
    
    public Integer createPublisher(String publisherName, String publisherAddress, String publisherPhone) throws SQLException {
        String sql = "INSERT INTO tbl_publisher (publisherName, publisherAddress, publisherPhone) VALUES (?, ?, ?);";
        Object[] parameters = new Object[] {publisherName, publisherAddress, publisherPhone};
        return super.insertData(sql, parameters);
    }
    
    public List<Publisher> getPublishers(int publisherId) throws SQLException {
        String sql = "SELECT * FROM tbl_publisher WHERE publisherId = ?;";
        Object[] parameters = new Object[] {publisherId};
        return super.getData(sql, parameters);
    }
    
    public Publisher getPublisher(int publisherId) throws SQLException {
        String sql = "SELECT * FROM tbl_publisher WHERE publisherId = ?;";
        Object[] parameters = new Object[] {publisherId};
        List<Publisher> publishers = super.getData(sql, parameters);
        Publisher publisher = null;
        
        if (publishers.size() != 0) {
            publisher = publishers.get(0);
        }
        
        return publisher;
    }
    
    public void updatePublisher(int publisherId, String publisherName, String publisherAddress, String publisherPhone) throws SQLException {
        String sql = "UPDATE tbl_publisher SET publisherName=?, publisherAddress=?, publisherPhone=? WHERE publisherId = ?";
        Object[] parameters = new Object[] {publisherName, publisherAddress, publisherPhone, publisherId};
        super.modifyData(sql, parameters);
    }
    
    public void deletePublisher(int publisherId) throws SQLException {
        String sql = "DELETE FROM tbl_publisher WHERE publisherId = ?;";
        Object[] parameters = new Object[] {publisherId};
        super.getData(sql, parameters);
    }

    @Override
    protected void processData(ResultSet resultSet, List<Publisher> list) throws SQLException {
        while (resultSet.next()) {
            Publisher publisher = new Publisher();
            publisher.setPublisherId(resultSet.getInt("publisherId"));
            publisher.setName(resultSet.getString("publisherName"));
            publisher.setAddress(resultSet.getString("publisherAddress"));
            publisher.setPhoneNumber(resultSet.getString("publisherPhone"));
            list.add(publisher);
        }
    }

}
