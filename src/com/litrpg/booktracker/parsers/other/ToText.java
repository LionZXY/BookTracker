package com.litrpg.booktracker.parsers.other;

import com.lionzxy.vkapi.util.UsersFile;
import com.litrpg.booktracker.authors.Author;
import com.litrpg.booktracker.books.IBook;
import com.litrpg.booktracker.enums.Genres;
import com.litrpg.booktracker.enums.TypeSite;
import com.litrpg.booktracker.helper.URLHelper;
import com.litrpg.booktracker.parsers.LitEraParser;

import java.io.File;
import java.util.Date;
import java.util.LinkedHashMap;

/**
 * com.litrpg.booktracker.parsers.other
 * Created by LionZXY on 12.01.2016.
 * BookTracker
 */
public class ToText {

    public static File getAsFile(IBook book) {
        if (book.getType().equals(TypeSite.LITERA)) {
            LinkedHashMap<Integer, String> partValue = new LinkedHashMap<>();
            for (String part : new LitEraParser(book.getUrl()).findWord("<option value=\"\">Содержание</option>", "</select>").replaceFirst("                                                                                                            ", "").split("                                                                                                                                                "))
                partValue.put(Integer.parseInt(LitEraParser.findWord(part, "<option value=\"", "\">")), LitEraParser.findWord(part, ">", "<"));
            StringBuilder bookText = new StringBuilder();
            addFB2Info(bookText, book);
            for (Integer part : partValue.keySet()) {
                bookText.append("\n<section>\n").append("<title><p>" + partValue.get(part) + "</p></title>\n");
                String html = URLHelper.getSiteAsString(book.getUrl().replaceFirst("https://lit-era.com/book/", "https://lit-era.com/reader/") + "?c=" + part, "utf8");
                if (!html.contains("Эта книга находится в платной подписке. Чтобы продолжить чтение, пожалуйста, оплатите доступ.")) {
                    String workOnText = temlareText(LitEraParser.findWord(html, "<div class=\"reader-text font-size-medium\"", "</div>"));
                    workOnText = workOnText.substring(workOnText.indexOf("</h2>") + "</h2>".length() + 1)
                            .replaceAll("&mdash;", "-")
                            .replaceAll("&nbsp;", "")
                            .replaceAll("&hellip;", "...")
                            //.replaceAll("<br />", "\n")
                            .replaceAll("&ndash;", "-");
                    bookText.append(workOnText);
                } else
                    bookText.append("<p>Эта книга находится в платной подписке. Чтобы продолжить чтение, пожалуйста, оплатите доступ.</p>");
                bookText.append("</section>");
            }
            bookText.append("</body>").append("</FictionBook>");
            return UsersFile.save(bookText.toString(), book.getUrl().replace(book.getType().getSite(), "").replace("/", "-").replace("\\", "-").replace(" ", "").replace(".", "").replace(":", "") + ".fb2");
        }
        return null;
    }

    public static String temlareText(String s) {
        while (s.contains("<p style=")) {
            int found = s.indexOf("<p style=\"");
            s = s.replaceAll(s.substring(found, s.indexOf("\">", found) + 2), "");

        }
        return s;
    }

    public static void addFB2Info(StringBuilder builder, IBook book) {
        builder.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<FictionBook xmlns=\"http://www.gribuser.ru/xml/fictionbook/2.0\" xmlns:l=\"http://www.w3.org/1999/xlink\">" +
                " <description>" +
                "  <title-info>");
        for (Genres genres : book.getGenres())
            for (String genString : genres.getParseCod())
                builder.append("<genre>").append(genString).append("</genre>");
        builder.append("<author><first-name></first-name><last-name>").append(Author.getAsString(book.getAuthors())).append("</last-name></author>");
        builder.append("<book-title>").append(book.getNameBook()).append("</book-title>");
        builder.append("<annotation><p>").append(book.getAnnotation().replaceAll("\n", "</p><p>")).append("</p></annotation>");
        builder.append("<date>").append(book.getLastUpdate()).append("</date>");
        builder.append("<lang>ru</lang>");
        builder.append("</title-info>");
        builder.append("<document-info><author><first-name></first-name><last-name>LionZXY</last-name></author>");
        builder.append("<program-used>LionZXY's BookTracker</program-used><date>").append(new Date()).append("</date><id>87db358a-df26-4bb4-bd4c-989488e56877</id><version>1.0</version></document-info>");
        builder.append("        <publish-info>" +
                "            <publisher>Microsoft</publisher>" +
                "        </publish-info>");
        builder.append("</description><body>");
    }


}
