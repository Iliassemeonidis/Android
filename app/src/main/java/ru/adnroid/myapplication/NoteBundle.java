package ru.adnroid.myapplication;

import android.os.Parcel;
import android.os.Parcelable;

public class NoteBundle implements Parcelable {
    private final String title;
    private final String description;
    private final int colour;

    // Создания  ранее сериализованных данных исходно объекта
    public static final Creator<NoteBundle> CREATOR = new Creator<NoteBundle>() {
        @Override
        public NoteBundle createFromParcel(Parcel source) {
            String title = source.readString();
            String description = source.readString();
            int colour = source.readInt();

            return new NoteBundle(title, description, colour);
        }

        @Override
        public NoteBundle[] newArray(int size) {
            return new NoteBundle[size];
        }
    };


    public NoteBundle(String title, String description, int colour) {
        this.title = title;
        this.description = description;
        this.colour = colour;
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
}
