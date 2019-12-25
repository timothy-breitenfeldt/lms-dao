package com.smoothstack.lms.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.smoothstack.lms.entity.BookCopy;

public class BookCopyDAO extends BaseDAO<BookCopy> {
    
    public BookCopyDAO(Connection connection) {
        super(connection);
    }

    public Integer createBookCopy(int bookId, int branchId, int amount) throws SQLException {
        String sql = "INSERT INTO tbl_book_copies (bookId, branchId, noOfCopies) VALUES (?, ?, ?);";
        Object[] parameters = new Object[] {bookId, branchId, amount};
        return super.insertData(sql, parameters);
    }
    
    public List<BookCopy> getBookCopys() throws SQLException {
        String sql = "SELECT * FROM tbl_book_copies;";
        return super.getData(sql);
    }
    
    public BookCopy getBookCopy(int bookId, int branchId) throws SQLException {
        String sql = "SELECT * FROM tbl_book_copies WHERE bookId = ? AND branchId = ?";
        Object[] parameters = new Object[] {bookId, branchId};
        List<BookCopy> bookCopys = super.getData(sql, parameters);
        BookCopy bookCopy = null;
        
        if (bookCopys.size() != 0) {
            bookCopy = bookCopys.get(0);
        }
        
        return bookCopy;
    }
    
    public void updateBookCopy(int bookId, int branchId, int amount) throws SQLException {
        String sql = "UPDATE tbl_book_copies SET noOfCopies=? WHERE bookId = ? AND branchId = ?;";
        Object[] parameters = new Object[] {amount, bookId, branchId};
        super.modifyData(sql, parameters);
    }
    
    public void deleteBookCopy(int bookId, int branchId) throws SQLException {
        String sql = "DELETE FROM tbl_book_copies WHERE bookId = ? AND branchId = ?;";
        Object[] parameters = new Object[] {bookId, branchId};
        super.modifyData(sql, parameters);
    }
    
    @Override
    protected void processData(ResultSet resultSet, List<BookCopy> list) throws SQLException {
        while (resultSet.next()) {
            BookCopy bookCopy = new BookCopy();
            bookCopy.setBookId(resultSet.getInt("bookId"));
            bookCopy.setBranchId(resultSet.getInt("branchId"));
            bookCopy.setAmount(resultSet.getInt("noOfCopies"));
            list.add(bookCopy);
        }
    }

}
