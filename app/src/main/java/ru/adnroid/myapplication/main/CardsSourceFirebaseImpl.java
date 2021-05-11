package ru.adnroid.myapplication.main;

import android.util.Log;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Map;

import ru.adnroid.myapplication.Note;

public class CardsSourceFirebaseImpl implements CardsSource {

    private static final String CARDS_COLLECTION = "cards";
    private static final String TAG = "[FirebaseImpl]";

    // База данных Firestore
    private FirebaseFirestore store = FirebaseFirestore.getInstance();

    // Коллекция документов
    private CollectionReference collection = store.collection(CARDS_COLLECTION);

    // Загружаемый список карточек
    private ArrayList<Note> cardsData = new ArrayList<>();

    @Override
    public CardsSource init(final MainFragment.CardsSourceResponse cardsSourceResponse) {
        // Получить всю коллекцию отсортированную по полю "дата"
        // При удачном считывании данных загрузим список карточек
        collection.orderBy(CardDataMapping.Fields.TITLE, Query.Direction.DESCENDING).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        cardsData = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Map<String, Object> doc = document.getData();
                            String id = document.getId();
                            Note cardData = CardDataMapping.toCardData(id, doc);
                            cardsData.add(cardData);
                        }
                        Log.wtf(TAG, "success " + cardsData.size() + " qnt");
                        cardsSourceResponse.initialized(CardsSourceFirebaseImpl.this);
                    } else {
                        Log.d(TAG, "get failed with ", task.getException());
                    }
                })
                .addOnFailureListener(e -> Log.d(TAG, "get failed with ", e));
        return this;
    }

    @Override
    public Note getCardData(int position) {
        return cardsData.get(position);
    }

    @Override
    public ArrayList<Note> getNotes() {
        return cardsData;
    }

    @Override
    public int size() {
        if (cardsData == null) {
            return 0;
        }
        return cardsData.size();
    }

    @Override
    public void deleteCardData(int position) {
        // Удалить документ с определенном идентификатором
        collection.document(cardsData.get(position).getId()).delete();
        cardsData.remove(position);
    }

    @Override
    public void updateCardData(int position, Note cardData) {
        String id = cardData.getId();
        // Изменить документ по идентификатору
        collection.document(id).set(CardDataMapping.toDocument(cardData));
    }

    @Override
    public void addCardData(final Note cardData) {
        // Добавить документ
        collection.add(CardDataMapping.toDocument(cardData)).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                cardData.setId(documentReference.getId());
            }
        });
    }

    @Override
    public void clearCardData() {
        for (Note cardData : cardsData) {
            collection.document(cardData.getId()).delete();
        }
        cardsData = new ArrayList<Note>();
    }
}
