package com.litrpg.booktracker.mysql;

import com.lionzxy.vkapi.util.ListHelper;
import com.lionzxy.vkapi.util.Logger;
import com.litrpg.booktracker.authors.Author;
import com.litrpg.booktracker.books.Book;
import com.litrpg.booktracker.books.IBook;
import com.litrpg.booktracker.enums.Genres;
import com.litrpg.booktracker.enums.TypeSite;
import com.litrpg.booktracker.parsers.MainParser;
import com.litrpg.booktracker.updaters.Updater;
import com.litrpg.booktracker.updaters.event.BookUpdateEvent;
import com.litrpg.booktracker.updaters.event.IBookUpdateListiner;
import com.litrpg.booktracker.user.IUser;
import com.litrpg.booktracker.user.UsersType;
import com.litrpg.booktracker.user.VKUser;
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
public class MySql implements IBookUpdateListiner {

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
        Updater.subscribe.subscribe(this);
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

    public void addUserInTable(IUser user) {
        LinkedHashMap<String, String> column = new LinkedHashMap<>();
        column.put("userid", user.getType() + "_" + user.getTypeID());
        column.put("subscribes", user.getSubAsString());
        addInTable("users", column);
        getIdUser(user);
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

    public List<IUser> getUsersFromTable() {
        List<IUser> users = new ArrayList<>();
        for (HashMap<String, Object> row : getFullTable(ListHelper.getStringList("id", "userid", "subscribes"), "users")) {
            switch (UsersType.getType((String) row.get("userid"))) {
                case VK:
                    users.add(new VKUser((String) row.get("userid"), (String) row.get("subscribes")).setIdInDB((Integer) row.get("id")));
            }
        }
        return users;
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

    public int getIdUser(IUser user) {
        try {
            ResultSet set = statement.executeQuery("SELECT id FROM users WHERE userid = \"" + user.getType() + "_" + user.getTypeID() + "\"");
            set.next();
            int id = set.getInt("id");
            user.setIdInDB(id);
            return id;
        } catch (SQLException e) {
            e.printStackTrace();
            addUserInTable(user);
            return getIdUser(user);
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

    public void updateUser(IUser user) {
        try {
            statement.execute("UPDATE users SET subscribes = \"" + user.getSubAsString() + "\" WHERE id = " + user.getDBId() + ";");
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


    @Override
    public void bookUpdate(BookUpdateEvent e) {
        StringBuilder sb = new StringBuilder();
        sb.append("UPDATE books SET annotation = ?, lastUpdate = ?, size = ? WHERE id = " + e.book.getIdInDB());
        PreparedStatement stmt = null;
        try {
            stmt = connection.prepareStatement(sb.toString());
            stmt.setString(1, e.book.getAnnotation());
            stmt.setString(2, dateToString(e.updateTime));
            stmt.setString(3, e.book.getSize() + "");
            stmt.executeUpdate();
            stmt.close();
            Logger.getLogger().print("Информация о книге \"" + e.book.getNameBook() + "\" обновленна");
        } catch (SQLException ex) {
            Logger.getLogger().print("Ошибка при обновлении книги в базе данных");
            ex.printStackTrace();
        }
    }
}
