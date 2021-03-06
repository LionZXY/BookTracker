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
import com.litrpg.booktracker.updaters.event.AuthorUpdateEvent;
import com.litrpg.booktracker.updaters.event.BookUpdateEvent;
import com.litrpg.booktracker.updaters.event.IBookUpdateListiner;
import com.litrpg.booktracker.user.IUser;
import com.litrpg.booktracker.user.UsersType;
import com.litrpg.booktracker.user.VKUser;
import com.mysql.fabric.jdbc.FabricMySQLDriver;
import sun.util.calendar.BaseCalendar;
import sun.util.calendar.CalendarDate;

import java.sql.*;
import java.util.*;
import java.util.Date;

/**
 * com.litrpg.booktracker.mysql
 * Created by LionZXY on 02.01.2016.
 * BookTracker
 */
public class MySql implements IBookUpdateListiner {

    public Connection connection;
    public Statement statement;
    Logger log = new Logger("[MYSQL]");

    //Только для локал-хост MySQL базы. Предпологается, что в этом проекте удаленная база использоваться не будет. Пока.
    //Предпологается, что БД уже созданна и её заново создавать не нужно
    public MySql(String bd, String login, String pswd) {
        try {
            Driver driver = new FabricMySQLDriver();
            DriverManager.registerDriver(driver);

            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + bd, login, pswd);
            statement = connection.createStatement();

            if (!connection.isClosed())
                log.print("Соединение с БД установленно!");
        } catch (SQLException e) {
            log.print("Соединение с БД не установленно!");
            e.printStackTrace();
            System.exit(-1);
        }
        Updater.subscribe.subscribe(this);
    }

    public void addBookInTable(IBook book) {
        log.debug("Идет добавление в БД книги " + book.getAuthors().get(0).getInDB() + ':' + book.getNameBook() + " .Ссылка " + book.getUrl());
        LinkedHashMap<String, String> column = new LinkedHashMap<>();
        if (!MainParser.books.contains(book))
            MainParser.books.add(book);
        column.put("name", book.getNameBook());
        column.put("authors", ListHelper.getAsStringAuthor(book.getAuthors()));
        column.put("annotation", book.getAnnotation());
        column.put("url", book.getUrl());
        column.put("lastUpdate", dateToString(book.getLastUpdate()));
        column.put("lastChecked", dateToString(book.getLastCheck()));
        column.put("genres", ListHelper.getAsStringGenr(book.getGenres()));
        column.put("size", String.valueOf(book.getSize()));
        column.put("photo", book.getPhotoUrl());
        addInTable("books", column);
        getIdBook(book);
        log.print("Добавленна книга \"" + book.getNameBook() + "\"");
    }

    public void addAuthorInTable(Author author) {
        log.debug("Идет добавление в БД автора " + author.getName());
        LinkedHashMap<String, String> column = new LinkedHashMap<>();
        if (!MainParser.authors.contains(author))
            MainParser.authors.add(author);
        column.put("name", author.getName());
        column.put("url", author.getUrl());
        column.put("books", author.getBooks());
        column.put("lastUpdate", dateToString(author.getLastUpdate()));
        column.put("lastCheck", dateToString(author.getLastCheck()));
        column.put("photo", author.photoUrl);
        addInTable("authors", column);
        getIdAuthor(author);
    }

    public void addUserInTable(IUser user) {
        LinkedHashMap<String, String> column = new LinkedHashMap<>();
        column.put("userid", user.getType() + "_" + user.getTypeID());
        column.put("subscribes", user.getSubAsString());
        column.put("permission", String.valueOf(user.getPerm()));
        addInTable("users", column);
        getIdUser(user);
    }

    public List<IBook> getBooksFromTable() {
        List<IBook> books = new ArrayList<>();
        for (HashMap<String, Object> row :
                getFullTable(ListHelper.getStringList("name", "id", "authors", "annotation",
                        "url", "lastUpdate", "lastChecked", "genres", "size", "photo"), "books")) {
            Book book = new Book(TypeSite.getTypeFromUrl((String) row.get("url")),
                    (String) row.get("name"),
                    (String) row.get("annotation"),
                    (String) row.get("url"),
                    MainParser.getAllAuthorById(ListHelper.parseString((String) row.get("authors"))),
                    (Timestamp) row.get("lastUpdate"),
                    Genres.getGenrFromString((String) row.get("genres")),
                    (Integer) row.get("size")
            ).setPhotoUrl((String) row.get("photo"));
            book.setLastCheck((Timestamp) row.get("lastChecked"));
            for (Author author : book.getAuthors())
                author.addBook(book);
            books.add(book);

        }
        return books;
    }

    public List<IUser> getUsersFromTable() {
        List<IUser> users = new ArrayList<>();
        for (HashMap<String, Object> row : getFullTable(ListHelper.getStringList("id", "userid", "subscribes", "permission", "sizeUpdate"), "users")) {
            switch (UsersType.getType((String) row.get("userid"))) {
                case VK: {
                    IUser user = new VKUser((String) row.get("userid"), (String) row.get("subscribes")).setIdInDB((Integer) row.get("id")).setPerm((Integer) row.get("permission"));
                    if (row.get("sizeUpdate") != null)
                        user.setSizeUpdate((Integer) row.get("sizeUpdate"));
                    users.add(user);

                }
            }
        }
        return users;
    }

    public List<Author> getAuthorsFromTable() {
        List<Author> authors = new ArrayList<>();
        for (HashMap<String, Object> row : getFullTable(ListHelper.getStringList("id", "name", "url", "books", "lastUpdate", "lastCheck", "photo"), "authors")) {
            Author a = new Author((String) row.get("name"), (String) row.get("url")).setIdDB((Integer) row.get("id")).setLastCheck((Timestamp) row.get("lastCheck")).setLastUpdate((Timestamp) row.get("lastUpdate"));
            a.photoUrl = (String) row.get("photo");
            authors.add(a);
        }
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
            log.print("MySQL add to table " + table + " successful");
        } catch (SQLException e) {
            log.print(e.getMessage() + ".Error while try add in table " + table);
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
            statement.execute("UPDATE authors SET books = \"" + author.getBooks() + "\",lastCheck = \"" + dateToString(author.getLastCheck()) + "\", lastUpdate = \"" + new Timestamp(author.getLastUpdate().getTime()) + "\" WHERE id = " + author.getInDB() + ";");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void checkNowBook() {
        try {
            statement.execute("UPDATE books SET lastChecked = \"" + dateToString(new Date()) + "\";");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void checkNowAuthor() {
        try {
            statement.execute("UPDATE authors SET lastCheck = \"" + dateToString(new Date()) + "\";");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateBook(Book book) {
        try {
            statement.execute("UPDATE books SET lastChecked = \"" + dateToString(book.getLastCheck()) + "\", authors = \"" + ListHelper.getAsStringAuthor(book.getAuthors()) + "\",lastUpdate = \"" + dateToString(book.getLastUpdate()) + "\" WHERE id = " + book.getIdInDB() + ";");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateUser(IUser user) {
        try {
            statement.execute("UPDATE users SET subscribes = \"" + user.getSubAsString() + "\", permission = \"" + user.getPerm() + "\", sizeUpdate = \"" + user.getSizeUpdate() + "\" WHERE id = " + user.getDBId() + ";");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static String dateToString(java.util.Date date) {
        if(date == null)
            return null;
        String te = new Timestamp(date.getTime()).toString();
        return te.substring(0, te.lastIndexOf("."));
        //YYYY-MM-DD HH:MM:SS
        /*if (date == null)
            return dateToString(new Date());
        CalendarDate calendarDate = BaseCalendar.getGregorianCalendar().getCalendarDate(date.getTime());
        String dateStr = calendarDate.getYear() + "-" + calendarDate.getMonth() + "-" + calendarDate.getDayOfMonth();
        dateStr += " " + calendarDate.getHours() + ":" + calendarDate.getMinutes() + ":" + calendarDate.getSeconds();
        return dateStr;*/
    }


    @Override
    public void bookUpdate(BookUpdateEvent e) {
        StringBuilder sb = new StringBuilder();
        sb.append("UPDATE books SET annotation = ?, lastUpdate = ?, size = ? WHERE id = " + e.book.getIdInDB());
        PreparedStatement stmt = null;
        try {
            stmt = connection.prepareStatement(sb.toString());
            stmt.setString(1, e.book.getAnnotation());
            stmt.setString(2, new Timestamp(e.updateTime.getTime()).toString());
            stmt.setString(3, e.book.getSize() + "");
            stmt.executeUpdate();
            stmt.close();
            log.print("Информация о книге \"" + e.book.getNameBook() + "\" обновленна");
        } catch (SQLException ex) {
            log.print("Ошибка при обновлении книги в базе данных");
            ex.printStackTrace();
        }
    }

    @Override
    public void authorUpdate(AuthorUpdateEvent e) {
        updateAuthorBook(e.getAuthor());
    }
}
