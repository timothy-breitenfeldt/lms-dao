package com.smoothstack.lms.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.smoothstack.lms.data_layer.DatabaseConnection;
import com.smoothstack.lms.entity.*;

public class AppData {

    private static void modifyData(String sql) {
        Statement statement = null;
        Connection connection;

        try {
            connection = DatabaseConnection.getConnection();

            connection.setAutoCommit(false);
            statement = connection.createStatement();

            statement.executeUpdate(sql);
            statement.close();
            connection.commit();
            connection.close();
        } catch (SQLException | ClassNotFoundException e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }

    public static LibraryBranch getLibraryBranchById(int id) {
        return new LibraryBranch(id);
    }

    public static Book getBookById(int id) {
        return new Book(id);
    }

    public static List<LibraryBranch> getLibraryBranches() {
        Statement statement;
        List<LibraryBranch> libraryBranches = new ArrayList<>();

        try {
            Connection connection = DatabaseConnection.getConnection();
            connection.setAutoCommit(false);
            statement = connection.createStatement();

            ResultSet result = statement.executeQuery("SELECT * FROM library_branch");

            while (result.next()) {
                libraryBranches.add(new LibraryBranch(result.getInt("branchId"), result.getString("branchName"),
                        result.getString("branchAddress")));
            }
            result.close();
            statement.close();
            connection.commit();
            connection.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }

        return libraryBranches;
    }

    public static void updateAuthor(int authorId, String name) {
        modifyData("UPDATE author SET authorName='" + name + "' WHERE authorId = " + authorId);
    }

    public static void updateBook(int bookId, String title, int authorId, int publisherId) {
        try {
            Connection connection = DatabaseConnection.getConnection();
            connection.setAutoCommit(false);
            Statement statement = connection.createStatement();
            ResultSet result = null;

            result = statement.executeQuery("SELECT authorId FROM author WHERE authorId = " + authorId);

            if (!result.next()) {
                throw new IllegalArgumentException("Invalid author ID.");
            }

            result = statement.executeQuery("SELECT publisherId FROM publisher WHERE publisherId = " + publisherId);

            if (!result.next()) {
                throw new IllegalArgumentException("Invalid publisher ID.");
            }

            statement.executeUpdate("UPDATE book SET title='" + title + "', authId=" + authorId + ", pubId="
                    + publisherId + " WHERE bookId = " + bookId);

            connection.commit();
            result.close();
            statement.close();
            connection.close();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            System.exit(0);
        }
    }

    public static void updateLibraryBranch(int id, String name, String address) {
        modifyData("UPDATE library_branch SET branchName = '" + name + "', branchAddress = '" + address
                + "' WHERE branchId = " + id);
    }

    public static void checkoutBook(int cardNumber, int bookId, int branchId) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
        LocalDate dateOutObject = LocalDate.now();
        LocalDate dueDateObject = dateOutObject.plusWeeks(1);
        String dateOut = dateOutObject.format(formatter);
        String dueDate = dueDateObject.format(formatter);
        modifyData("INSERT INTO book_loans (bookId, branchId, cardNo, dateOut, dueDate) VALUES (" + bookId + ", "
                + branchId + ", " + cardNumber + ", '" + dateOut + "', '" + dueDate + "')");
        modifyData("UPDATE  book_copies SET noOfCopies = noOfCopies - 1 WHERE bookId = " + bookId + " AND branchId = "
                + branchId);
    }

    public static void deleteLibraryBranch(int branchId) {
        modifyData("DELETE FROM library_branch WHERE branchId = " + branchId);
    }

    public static void deleteAuthor(int authorId) {
        modifyData("DELETE FROM author WHERE authorId = " + authorId);
    }

    public static void updatePublisher(int publisherId, String name, String address, String phoneNumber) {
        modifyData("UPDATE publisher SET publisherName='" + name + "', publisherAddress='" + address
                + "', publisherPhone='" + phoneNumber + "' WHERE publisherId = " + publisherId);
    }

    public static void deletePublisher(int publisherId) {
        modifyData("DELETE FROM publisher WHERE publisherId = " + publisherId);
    }

    public static void addLibraryBranch(String name, String address) {
        modifyData("INSERT INTO library_branch (branchName, branchAddress) VALUES ('" + name + "', '" + address + "')");
    }

    public static void updateBookCopy(int bookId, int libraryBranchId, int amount) {
        modifyData("UPDATE book_copies SET noOfCopies=" + amount + " WHERE bookId = " + bookId + " AND branchId = "
                + libraryBranchId);
    }

    public static void addBookCopies(int bookId, int libraryBranchId, int amount) {
        modifyData("INSERT INTO book_copies (bookId, branchId, noOfCopies) VALUES (" + bookId + ", " + libraryBranchId
                + ", " + amount + ")");
    }

    public static List<Book> getAvailableBooksNotCheckedOut(int cardNumber, int libraryBranchId) {
        List<Book> books = new ArrayList<Book>();
        Statement statement = null;

        try {
            Connection connection = DatabaseConnection.getConnection();
            String sql = "SELECT bookId, title, authId, pubId, branchId, noOfCopies"
                    + " FROM book NATURAL JOIN book_copies WHERE branchId = " + libraryBranchId + " AND noOfCopies > 0 AND "
                            + cardNumber + " NOT IN ( SELECT cardNo FROM book_loans )";

            connection.setAutoCommit(false);
            statement = connection.createStatement();

            ResultSet result = statement.executeQuery(sql);

            while (result.next()) {
                books.add(new Book(result.getInt("bookId"), result.getString("title"), result.getInt("authId"),
                        result.getInt("pubId")));
            }

            result.close();
            statement.close();
            connection.commit();
            connection.close();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            System.exit(0);
        }

        return books;
    }

    public static void addBorrower(String name, String address, String phoneNumber) {
        modifyData("INSERT INTO borrower (name, address, phone) VALUES ('" + name + "', '" + address + "', '"
                + phoneNumber + "')");
    }

    public static void updateBorrower(int cardNumber, String name, String address, String phoneNumber) {
        modifyData("UPDATE borrower SET cardNo='" + cardNumber + "', name='" + name + "', address='" + address
                + "', phone='" + phoneNumber + "' WHERE cardNo = '" + cardNumber + "'");
    }

    public static void deleteBorrower(int cardNumber) {
        modifyData("DELETE FROM borrower where cardNo = '" + cardNumber + "'");
    }

    public static void overrideDueDate(int cardNumber, int bookId, int libraryBranchId, String dueDate) {
        modifyData("UPDATE book_loans SET bookId= '" + bookId + "', branchId= '" + libraryBranchId + "', cardNo= '"
                + cardNumber + "', dueDate= '" + dueDate + "' WHERE bookId = '" + bookId + "' AND branchId = '"
                + libraryBranchId + "' and cardNo = '" + cardNumber + "'");
    }

    public static List<Author> getAuthors() {
        Statement statement;
        List<Author> authors = new ArrayList<>();

        try {
            Connection connection = DatabaseConnection.getConnection();
            connection.setAutoCommit(false);
            statement = connection.createStatement();

            ResultSet result = statement.executeQuery("SELECT * FROM author");

            while (result.next()) {
                authors.add(new Author(result.getInt("authorId"), result.getString("authorName")));
            }
            result.close();
            statement.close();
            connection.commit();
            connection.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
        return authors;
    }

    public static Map<Integer, Author> getAuthorsMap() {
        Statement statement;
        Map<Integer, Author> authors = new HashMap<>();

        try {
            Connection connection = DatabaseConnection.getConnection();
            connection.setAutoCommit(false);
            statement = connection.createStatement();

            ResultSet result = statement.executeQuery("SELECT * FROM author");

            while (result.next()) {
                authors.put(result.getInt("authorId"),
                        new Author(result.getInt("authorId"), result.getString("authorName")));
            }
            result.close();
            statement.close();
            connection.commit();
            connection.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
        return authors;
    }

    public static boolean borrowerExists(int cardNumber) {
        String sql = "SELECT * FROM borrower WHERE cardNo = " + cardNumber;
        try {
            Connection connection = DatabaseConnection.getConnection();
            connection.setAutoCommit(false);
            Statement statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery(sql);
            boolean exists = resultSet.next() ? true : false;

            resultSet.close();
            statement.close();
            connection.commit();
            connection.close();
            return exists;
        } catch (SQLException | ClassNotFoundException e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        return false;
    }

    public static BookCopy getBookCopy(int bookId, int libraryBranchId) {
        String sql = "SELECT * FROM book_copies WHERE bookId = " + bookId + " AND branchId = " + libraryBranchId;
        BookCopy bookCopy = null;

        try {
            Connection connection = DatabaseConnection.getConnection();
            connection.setAutoCommit(false);
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            if (resultSet.next()) {
                bookCopy = new BookCopy(resultSet.getInt("bookId"), resultSet.getInt("branchId"),
                        resultSet.getInt("noOfCopies"));
            }
            resultSet.close();
            statement.close();
            connection.commit();
            connection.close();

        } catch (SQLException | ClassNotFoundException e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        return bookCopy;
    }

    public static BookLoan getBookLoan(int bookId, int branchId, int cardNo) {
        String sql = "SELECT * FROM book_loans WHERE bookId = " + bookId + " AND branchId = " + branchId
                + " AND cardNo = " + cardNo;
        BookLoan bookLoan = new BookLoan();
        try {
            Connection connection = DatabaseConnection.getConnection();
            connection.setAutoCommit(false);
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            if (resultSet.next()) {
                bookLoan = new BookLoan(resultSet.getInt("bookId"), resultSet.getInt("branchId"),
                        resultSet.getInt("noOfCopies"), resultSet.getString("dateOut"), resultSet.getString("dateIn"));
            }

            resultSet.close();
            statement.close();
            connection.commit();
            connection.close();
        } catch (SQLException | ClassNotFoundException e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        return bookLoan;
    }

    public static List<BookLoan> getBookLoans(int branchId, int cardNo) {
        String sql = "SELECT DISTINCT * FROM book_loans WHERE branchId = " + branchId + " AND cardNo = " + cardNo;

        List<BookLoan> list = new ArrayList<>();
        try {
            Connection connection = DatabaseConnection.getConnection();
            connection.setAutoCommit(false);
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {
                BookLoan bookLoan = new BookLoan(resultSet.getInt("bookId"), resultSet.getInt("branchId"),
                        resultSet.getInt("cardNo"), resultSet.getString("dateOut"), resultSet.getString("dueDate"));
                list.add(bookLoan);
            }

            resultSet.close();
            statement.close();
            connection.commit();
            connection.close();
        } catch (SQLException | ClassNotFoundException e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        return list;
    }

    public static void checkinBook(int bookId, int branchId, int cardNo) {
        modifyData("UPDATE  book_copies SET noOfCopies = noOfCopies + " + 1 + " WHERE bookId = " + bookId
                + " AND branchId = " + branchId);
        modifyData("DELETE FROM book_loans WHERE bookId = " + bookId);
    }

    public static void deleteBook(int id) {
        modifyData("DELETE FROM book WHERE  bookId = " + id);
    }

    public static void addAuthor(String name) {
        modifyData("INSERT INTO author (authorName) VALUES ('" + name + "')");
    }

    public static void addPublisher(String name, String address, String phoneNumber) {
        modifyData("INSERT INTO publisher (publisherName, publisherAddress, publisherPhone) VALUES ('" + name + "', '"
                + address + "', '" + phoneNumber + "')");
    }

    public static List<Book> getBorrowerBooks(int cardNumber, int libraryBranchId) {
        List<Book> books = new ArrayList<Book>();
        Statement statement = null;

        try {
            Connection connection = DatabaseConnection.getConnection();
            String sql = "SELECT DISTINCT book.bookId, book.title, book.authId, book.pubId FROM book "
                    + "INNER JOIN book_loans ON book_loans.bookId = book.bookId "
                    + "INNER JOIN borrower ON borrower.cardNo = book_loans.cardNo "
                    + "INNER JOIN library_branch ON library_branch.branchId = book_loans.branchId "
                    + "WHERE borrower.cardNo = " + cardNumber + " AND library_branch.branchId = " + libraryBranchId;

            connection.setAutoCommit(false);
            statement = connection.createStatement();

            ResultSet result = statement.executeQuery(sql);

            while (result.next()) {
                books.add(new Book(result.getInt("bookId"), result.getString("title"), result.getInt("authId"),
                        result.getInt("pubId")));
            }

            result.close();
            statement.close();
            connection.commit();
            connection.close();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            System.exit(0);
        }

        return books;
    }
}