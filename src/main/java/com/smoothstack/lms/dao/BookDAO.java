package com.smoothstack.lms.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.smoothstack.lms.entity.Book;

public class BookDAO extends BaseDAO<Book> {
    
    public BookDAO(Connection connection) {
        super(connection);
    }
    
    public Integer insertBook(String title, int publisherId) throws SQLException {
        String sql = "INSERT INTO tbl_book (title, pubId) VALUES (?, ?);";
        Object[] parameters = new Object[] {title, publisherId};
        return super.insertData(sql, parameters);
    }
    
    public Integer insertBookAuthor(int bookId, int authorId) throws SQLException {
        String sql = "INSERT INTO tbl_book_authors (bookId, authorId) VALUES (?, ?);";
        Object[] parameters = new Object[] {bookId, authorId};
        return super.insertData(sql, parameters);
    }
    
    public List<Book> getBooks() throws SQLException {
        String sql = "SELECT * FROM tbl_book;";
        return super.getData(sql);
    }
    
    public List<Book> getAvailableBooksNotCheckedOut(int branchId, int cardNumber) throws SQLException {
        String sql = "SELECT bookId, title, pubId"
                + " FROM tbl_book NATURAL JOIN tbl_book_copies WHERE branchId = ? AND cardNumber = ? AND noOfCopies > 0 AND dateIn != NULL;";
        Object[] parameters = new Object[] {branchId, cardNumber};
        return super.getData(sql, parameters);
    }

    public Book getBook(int bookId) throws SQLException {
        String sql = "SELECT * FROM tbl_book WHERE bookId = ?;";
        Object[] parameters = new Object[] {bookId};
        List<Book> books = super.getData(sql, parameters);
        Book book = null;
        
        if (books.size() != 0) {
            book = books.get(0);
        }
        
        return book;
    }
    
    public void updateBook(int bookId, String title, int publisherId) throws SQLException {
        String sql = "UPDATE tbl_book SET title=?, pubId=? WHERE bookId = ?";
        Object[] parameters = new Object[] {title, publisherId, bookId};
        super.modifyData(sql, parameters);
    }
    
    public void deleteBook(int bookId) throws SQLException {
        String sql = "DELETE FROM tbl_book WHERE bookId = ?;";
        Object[] parameters = new Object[] {bookId};
        super.modifyData(sql, parameters);
    }

    @Override
    protected void processData(ResultSet resultSet, List<Book> list) throws SQLException {
        while (resultSet.next()) {
            Book book = new Book();
            book.setBookId(resultSet.getInt("bookId"));
            book.setTitle(resultSet.getString("title"));
                    list.add(book);
        }
    }

}
