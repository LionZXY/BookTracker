package com.litrpg.booktracker.mysql;

import com.lionzxy.vkapi.util.Logger;
import com.litrpg.booktracker.Main;
import com.litrpg.booktracker.books.IBook;
import com.mysql.fabric.jdbc.FabricMySQLDriver;

import java.sql.*;
import java.util.*;

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

    public void addBookInDB(IBook book) {
        //TODO
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
            Logger.getLogger().print("MySQL add to table " + table + " successful");
        } catch (SQLException e) {
            Logger.getLogger().print("Error while try add in table " + table);
            e.printStackTrace();
        }

    }
}
