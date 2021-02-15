package ru.adnroid.myapplication;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class NoteParams implements Parcelable {
    private final String title;
    private final String description;
    private final int colour;
    private final Date date;

    // Создания  ранее сериализованных данных исходно объекта
    public static final Creator<NoteParams> CREATOR = new Creator<NoteParams>() {
        @Override
        public NoteParams createFromParcel(Parcel source) {
            String title = source.readString();
            String description = source.readString();
            int colour = source.readInt();
            Date date = new Date();

            return new NoteParams(title, description, colour, date);
        }

        @Override
        public NoteParams[] newArray(int size) {
            return new NoteParams[size];
        }
    };


    public NoteParams(String title, String description, int colour, Date date) {
        this.title = title;
        this.description = description;
        this.colour = colour;
        this.date = date;
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

    public Date getDate() {
        return date;
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
        dest.writeString(date.toString());
    }
}
