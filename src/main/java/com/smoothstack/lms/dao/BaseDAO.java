package com.smoothstack.lms.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseDAO<T> {
    
    private Connection connection;
    
    public BaseDAO(Connection connection) {
        this.connection = connection;
    }

    protected void modifyData(String sql, Object[] parameters) throws SQLException {
            PreparedStatement statement = this.connection.prepareStatement(sql);
this.setParameters(parameters, statement);
            statement.executeUpdate();
        }
    
    protected void modifyData(String sql) throws SQLException {
        this.modifyData(sql, new Object[0]);
    }
    
    protected Integer insertData(String sql, Object[] parameters) throws SQLException {
        PreparedStatement statement = this.connection.prepareStatement(sql);
        ResultSet resultSet = null;
        Integer id = null;
        
this.setParameters(parameters, statement);
        statement.executeUpdate();
        resultSet = statement.getGeneratedKeys();
        
        if (resultSet.next()) {
            id = resultSet.getInt(1);
        }
        
        return id;
    }
    
    protected Integer insertData(String sql) throws SQLException {
        return this.insertData(sql, new Object[0]);
    }
        
        protected List<T> getData(String sql, Object[] parameters) throws SQLException {
            List<T> list = new ArrayList<>();
            PreparedStatement statement = this.connection.prepareStatement(sql);
            ResultSet resultSet = null;
            
            this.setParameters(parameters, statement);
                resultSet = statement.executeQuery();
                this.processData(resultSet, list);
            return list;
        }
        
        protected List<T> getData(String sql) throws SQLException {
            return this.getData(sql, new Object[0]);
        }
        
        private void setParameters(Object[] parameters, PreparedStatement statement) throws SQLException {
            for (int i = 0; i < parameters.length; i++) {
                statement.setObject(i,  parameters[i]);
            }
        }

        protected abstract void processData(ResultSet resultSet, List<T> list) throws SQLException;
        
}
