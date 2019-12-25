package com.smoothstack.lms.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.smoothstack.lms.entity.Genre;

public class GenreDAO extends BaseDAO<Genre> {

    public GenreDAO(Connection connection) {
        super(connection);
    }

    public Integer createGenre(String genreName) throws SQLException {
        String sql = "INSERT INTO tbl_genre (genreName) VALUES (?);";
        Object[] parameters = new Object[] {genreName};
        return super.insertData(sql, parameters);
    }
    
    public List<Genre> getGenres() throws SQLException {
        String sql = "SELECT * FROM tbl_genre;";
        return super.getData(sql);
    }
    
    public Genre getGenre(int genreId) throws SQLException {
        String sql = "SELECT * FROM tbl_genre WHERE genreId = ?";
        Object[] parameters = new Object[] {genreId};
        List<Genre> genres = super.getData(sql, parameters);
        Genre genre = null;
        
        if (genres.size() != 0) {
            genre = genres.get(0);
        }
        
        return genre;
    }
    
    public void updateGenre(int genreId, String genreName) throws SQLException {
        String sql = "UPDATE tbl_genre SET genreName=? WHERE genreId = ?;";
        Object[] parameters = new Object[] {genreId, genreName};
        super.modifyData(sql, parameters);
    }
    
    public void deleteGenre(int genreId) throws SQLException {
        String sql = "DELETE FROM tbl_genre WHERE genreId = ?;";
        Object[] parameters = new Object[] {genreId};
        super.modifyData(sql, parameters);
    }
    
    @Override
    protected void processData(ResultSet resultSet, List<Genre> list) throws SQLException {
        while (resultSet.next()) {
            Genre genre = new Genre();
            genre.setGenreId(resultSet.getInt("genreId"));
            genre.setName(resultSet.getString("genreName"));
            list.add(genre);
        }
    }

}
