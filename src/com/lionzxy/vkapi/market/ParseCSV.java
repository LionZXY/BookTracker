package com.lionzxy.vkapi.market;

import com.lionzxy.vkapi.VKUser;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * com.lionzxy.vkapi.market
 * Created by LionZXY on 23.01.2016.
 * BookTracker
 */
public class ParseCSV {
    public List<HashMap<String, String>> table = new ArrayList<>();

    public ParseCSV(File csvFile) {
        try {
            List<String> rows = new ArrayList<>();
            InputStreamReader inputStream = new InputStreamReader(new FileInputStream(csvFile), "windows-1251");
            StringBuilder thisRow = new StringBuilder();

            char[] buffer = new char[4096];
            int bytesRead = -1;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                for (int i = 0; i < bytesRead; i++) {
                    if ((byte) buffer[i] == 13) {
                        rows.add(thisRow.toString());
                        thisRow = new StringBuilder();
                    } else thisRow.append(buffer[i]);
                }
            }
            String[] mainRow = rows.get(0).split(";");
            for (int i = 1; i < rows.size(); i++) {
                HashMap<String, String> row = new HashMap<>();
                String[] cells = rows.get(i).split(";");
                for (int j = 0; j < cells.length; j++)
                    row.put(mainRow[j], cells[j]);
                table.add(row);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addAllInMarket(VKUser vkUser, int groupId) {
        for (HashMap<String, String> row : table) {
            String name = row.get("Название ");
            String decr = row.get("Описание");
            int cat;
            if (row.get("Категория").length() != 0)
                cat = Categories.getIdOnName(vkUser, row.get("Категория"), 304);
            else cat = 304;
            int price = Integer.parseInt(row.get("Стоимость").substring(0, row.get("Стоимость").indexOf(" руб")));
            String[] addPhoto = row.get("Дополнительные фотографии").split(",");
            String[] linkPhoto = new String[addPhoto.length + 1];
            linkPhoto[0] = row.get("Основная фотография");
            for (int i = 1; i < linkPhoto.length; i++)
                linkPhoto[i] = addPhoto[i - 1];
            Item.addItem(vkUser, groupId, name, decr, cat, price, linkPhoto);
        }
    }
}
