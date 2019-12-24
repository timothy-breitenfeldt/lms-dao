package com.smoothstack.lms.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.smoothstack.lms.entity.Author;

public class AuthorDAO extends BaseDAO<Author> {
    
    public AuthorDAO(Connection connection) {
        super(connection);
    }

    public Integer createAuthor(String authorName) throws SQLException {
        String sql = "INSERT INTO tbl_author (authorName) VALUES (?);";
        Object[] parameters = new Object[] {authorName};
        return super.insertData(sql, parameters);
    }
    
    public List<Author> getAuthors() throws SQLException {
        String sql = "SELECT * FROM tbl_author;";
        return super.getData(sql);
    }
    
    public Author getAuthor(int authorId) throws SQLException {
        String sql = "SELECT * FROM tbl_author WHERE authorId = ?";
        Object[] parameters = new Object[] {authorId};
        List<Author> authors = super.getData(sql, parameters);
        Author author = null;
        
        if (authors.size() != 0) {
            author = authors.get(0);
        }
        
        return author;
    }
    
    public void updateAuthor(int authorId, String authorName) throws SQLException {
        String sql = "UPDATE tbl_author SET authorName=? WHERE authorId = ?;";
        Object[] parameters = new Object[] {authorId, authorName};
        super.modifyData(sql, parameters);
    }
    
    public void deleteAuthor(int authorId) throws SQLException {
        String sql = "DELETE FROM tbl_author WHERE authorId = ?;";
        Object[] parameters = new Object[] {authorId};
        super.modifyData(sql, parameters);
    }
    
    @Override
    protected void processData(ResultSet resultSet, List<Author> list) throws SQLException {
        while (resultSet.next()) {
            Author author = new Author();
            author.setAuthorId(resultSet.getInt("authorId"));
            author.setName(resultSet.getString("authorName"));
            list.add(author);
        }
    }
    
}
