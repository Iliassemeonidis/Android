package ru.adnroid.myapplication;

import android.os.Parcel;
import android.os.Parcelable;

public class Notes implements Parcelable {
    private static String title;
    private static String description;
    private static int colour;

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
        Notes.title = title;
        Notes.description = description;
        Notes.colour = colour;
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

    public static Notes getInstance() {
        return new Notes("Москва", "Описание", R.color.purple_700);
    }
}
