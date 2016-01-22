package com.litrpg.booktracker.user;

/**
 * com.litrpg.booktracker.user
 * Created by LionZXY on 22.01.2016.
 * BookTracker
 */
public enum UsersType {
    VK("vkid"),
    Unknow("unkn");

    String ind;

    UsersType(String ind) {
        this.ind = ind;
    }

    public String getInd() {
        return ind;
    }

    public static UsersType getType(String typeInDB) {
        for (UsersType type : UsersType.values())
            if (typeInDB.startsWith(type.getInd()))
                return type;
        return Unknow;
    }

    @Override
    public String toString() {
        return ind;
    }
}
