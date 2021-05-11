package ru.adnroid.myapplication;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class Note implements Parcelable {

    private String id;
    private int type;
    private String title;
    private String description;
    private String date;
    private int colour;

    // Описывает контент и возвращает некторое числовое значение
    @Override
    public int describeContents() {
        return 0;
    }

    public Note(String id, int type, String title, String description, String date, int colour) {
        this.id = id;
        this.type = type;
        this.title = title;
        this.description = description;
        this.date = date;
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

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setColour(int colour) {
        this.colour = colour;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("type", type);
        result.put("date", date);
        result.put("title", title);
        result.put("description", description);
        result.put("colour", colour);
        return result;
    }

    //  Пишет в объект Parcel содержимое объекта
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeInt(type);
        dest.writeString(title);
        dest.writeString(description);
        dest.writeString(date);
        dest.writeInt(colour);
    }

    // Создания  ранее сериализованных данных исходно объекта
    public static final Creator<Note> CREATOR = new Creator<Note>() {
        @Override
        public Note createFromParcel(Parcel source) {
            String id = source.readString();
            int type = source.readInt();
            String title = source.readString();
            String description = source.readString();
            String date = source.readString();
            int colour = source.readInt();
            return new Note(id, type, title, description, date, colour);
        }

        @Override
        public Note[] newArray(int size) {
            return new Note[size];
        }
    };
}
