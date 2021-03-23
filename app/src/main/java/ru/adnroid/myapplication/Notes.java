package ru.adnroid.myapplication;

import android.os.Parcel;
import android.os.Parcelable;

public class Notes implements Parcelable {
    private int type;
    private String date;
    private String title;
    private String description;
    private int colour;

    // Создания  ранее сериализованных данных исходно объекта
    public static final Creator<Notes> CREATOR = new Creator<Notes>() {
        @Override
        public Notes createFromParcel(Parcel source) {
            String title = source.readString();
            String description = source.readString();
            int colour = source.readInt();
            return new Notes(title, description, colour);
        }

        @Override
        public Notes[] newArray(int size) {
            return new Notes[size];
        }
    };

    public Notes(String title, String description, int colour) {
        this.title = title;
        this.description = description;
        this.colour = colour;
    }

    public Notes() {
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getColour() {
        return colour;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    // Описывает контент и возвращает некторое числовое значение
    @Override
    public int describeContents() {
        return 0;
    }

    //  Пишет в объект Parcel содержимое объекта
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(description);
        dest.writeInt(colour);
    }

    public Notes getInstance() {
        return new Notes(title, description, colour);
    }

}
