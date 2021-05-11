package ru.adnroid.myapplication.main;

import java.util.ArrayList;

import ru.adnroid.myapplication.Note;

public interface CardsSource {
    CardsSource init(MainFragment.CardsSourceResponse cardsSourceResponse);

    Note getCardData(int position);

    ArrayList<Note> getNotes();

    int size();

    void deleteCardData(int position);

    void updateCardData(int position, Note cardData);

    void addCardData(Note cardData);

    void clearCardData();

}
