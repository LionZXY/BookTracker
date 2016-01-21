package com.litrpg.booktracker.mysql;

import com.lionzxy.vkapi.util.ListHelper;
import com.lionzxy.vkapi.util.Logger;
import com.litrpg.booktracker.authors.Author;
import com.litrpg.booktracker.books.Book;
import com.litrpg.booktracker.books.IBook;
import com.litrpg.booktracker.enums.Genres;
import com.litrpg.booktracker.enums.TypeSite;
import com.litrpg.booktracker.parsers.MainParser;
import com.mysql.fabric.jdbc.FabricMySQLDriver;
import sun.util.calendar.BaseCalendar;
import sun.util.calendar.CalendarDate;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;
import java.util.stream.Collectors;

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
        LinkedHashMap<String, String> column = new LinkedHashMap<>();
        column.put("name", book.getNameBook());
        column.put("authors", ListHelper.getAsStringAuthor(book.getAuthors()));
        column.put("annotation", book.getAnnotation());
        column.put("url", book.getUrl());
        column.put("lastUpdate", dateToString(book.getLastUpdate()));
        column.put("lastChecked", dateToString(book.getLastChecked()));
        column.put("genres", ListHelper.getAsStringGenr(book.getGenres()));
        column.put("size", String.valueOf(book.getSize()));
        addInTable("books", column);
        getIdBook(book);
        Logger.getLogger().print("Добавленна книга \"" + book.getNameBook() + "\"");
    }

    public void addAuthorInTable(Author author) {
        LinkedHashMap<String, String> column = new LinkedHashMap<>();
        column.put("name", author.getName());
        column.put("url", author.getUrl());
        column.put("books", author.getBooks());
        addInTable("authors", column);
        getIdAuthor(author);
    }

    public List<IBook> getBooksFromTable() {
        List<IBook> books = new ArrayList<>();
        for (HashMap<String, Object> row :
                getFullTable(ListHelper.getStringList("name", "id", "authors", "annotation",
                        "url", "lastUpdate", "lastChecked", "genres", "size"), "books")) {
            Book book = new Book(TypeSite.getTypeFromUrl((String) row.get("url")),
                    (String) row.get("name"),
                    (String) row.get("annotation"),
                    (String) row.get("url"),
                    MainParser.getAllAuthorById(ListHelper.parseString((String) row.get("authors"))),
                    new Date(((Timestamp) row.get("lastUpdate")).getTime()),
                    Genres.getGenrFromString((String) row.get("genres")),
                    (Integer) row.get("size")
            );
            for (Author author : book.getAuthors())
                author.addBook(book);
            books.add(book);

        }
        return books;
    }

    public List<Author> getAuthorsFromTable() {
        List<Author> authors = getFullTable(ListHelper.getStringList("id", "name", "url"), "authors").stream().map(row -> new Author((String) row.get("name"))
                .setIdDB((Integer) row.get("id"))
                .setURL((String) row.get("url"))).collect(Collectors.toList());
        return authors;
    }

    public int getIdBook(IBook book) {
        try {
            ResultSet set = statement.executeQuery("SELECT id FROM books WHERE url = \"" + book.getUrl() + "\"");
            set.next();
            int id = set.getInt("id");
            book.setDBid(id);
            return id;
        } catch (SQLException e) {
            addBookInTable(book);
            return getIdBook(book);
        }
    }

    public int getIdAuthor(Author author) {
        try {
            ResultSet set = statement.executeQuery("SELECT id FROM authors WHERE url = \"" + author.getUrl() + "\"");
            set.next();
            int id = set.getInt("id");
            author.setIdDB(id);
            return id;
        } catch (SQLException e) {
            e.printStackTrace();
            addAuthorInTable(author);
            return getIdAuthor(author);
        }
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

    public void updateAuthorBook(Author author) {
        try {
            statement.execute("UPDATE authors SET books = \"" + author.getBooks() + "\" WHERE id = " + author.getInDB() + ";");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static String dateToString(java.util.Date date) {
        //YYYY-MM-DD HH:MM:SS
        CalendarDate calendarDate = BaseCalendar.getGregorianCalendar().getCalendarDate(date.getTime());
        String dateStr = calendarDate.getYear() + "-" + calendarDate.getMonth() + "-" + calendarDate.getDayOfMonth();
        dateStr += " " + calendarDate.getHours() + ":" + calendarDate.getMinutes() + ":" + calendarDate.getSeconds();
        return dateStr;
    }

    public static Date stringToDate(String date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        try {
            return simpleDateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

}
