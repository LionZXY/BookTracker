package com.litrpg.booktracker.mysql;

import com.lionzxy.vkapi.util.ListHelper;
import com.lionzxy.vkapi.util.Logger;
import com.litrpg.booktracker.authors.Author;
import com.litrpg.booktracker.books.IBook;
import com.mysql.fabric.jdbc.FabricMySQLDriver;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * com.litrpg.booktracker.mysql
 * Created by LionZXY on 02.01.2016.
 * BookTracker
 */
public class MySql {

    public Connection connection;
    public Statement statement;

    //Только для локал-хост MySQL базы. Предпологается, что в этом проекте удаленная база использоваться не будет. Пока.
    //Предпологается, что БД уже созданна и её заново создавать не нужно
    public MySql(String bd, String login, String pswd) {
        try {
            Driver driver = new FabricMySQLDriver();
            DriverManager.registerDriver(driver);

            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + bd, login, pswd);
            statement = connection.createStatement();

            if (!connection.isClosed())
                Logger.getLogger().print("Соединение с БД установленно!");
        } catch (SQLException e) {
            Logger.getLogger().print("Соединение с БД не установленно!");
            e.printStackTrace();
            System.exit(-1);
        }
    }

    public void addBookInTable(IBook book) {
        //TODO
        HashMap<String, String> column = new HashMap<>();
        column.put("name", book.getNameBook());
        column.put("authors", ListHelper.getAsString(book.getAuthors()));
    }

    public int getIdBook(IBook book) {
        //TODO If book not exist in table
        return -1;
    }

    public int getIdAuthor(Author author) {
        //TODO If book not exist in table
        return -1;
    }

    public void addInTable(String table, LinkedHashMap<String, String> request) {
        StringBuilder sqlReq = new StringBuilder();
        sqlReq.append("INSERT INTO ").append(table).append("(");
        List<String> names = new ArrayList<>();
        for (String name : request.keySet()) {
            sqlReq.append(name);
            sqlReq.append(',');
            names.add(name);
        }
        sqlReq.deleteCharAt(sqlReq.length() - 1);
        sqlReq.append(") values (");
        for (int i = 0; i < request.size(); i++) {
            if (i != 0) sqlReq.append(',');
            sqlReq.append('?');
        }
        sqlReq.append(')');
        PreparedStatement stmt = null;
        try {
            System.out.println();
            stmt = connection.prepareStatement(sqlReq.toString());
            for (int i = 1; i <= request.size(); i++)
                stmt.setString(i, request.get(names.get(i - 1)));
            stmt.executeUpdate();
            stmt.close();
            Logger.getLogger().print("MySQL add to table " + table + " successful");
        } catch (SQLException e) {
            Logger.getLogger().print("Error while try add in table " + table);
            e.printStackTrace();
        }
    }

    public List<HashMap<String, Object>> getFullTable(List<String> columns, String tableName) {
        List<HashMap<String, Object>> toExit = new ArrayList<>();

        try {
            ResultSet resultSet = statement.executeQuery("SELECT * FROM " + tableName);
            while (resultSet.next()) {
                HashMap<String, Object> row = new HashMap<>();
                for (String column : columns)
                    row.put(column, resultSet.getObject(column));
                toExit.add(row);
            }
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return toExit;
    }

    public boolean removeRowFromTable(String whereCondition, String tableName) {
        try {
            statement.execute("DELETE FROM " + tableName + " WHERE " + whereCondition + ";");
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

}
