package ru.adnroid.myapplication.main;

import java.util.HashMap;
import java.util.Map;

import ru.adnroid.myapplication.Note;

public class CardDataMapping {

    public static class Fields {
        public final static String TITLE = "title";
        public final static String DESCRIPTION = "description";
        public final static String COLOUR = "like";
    }

    public static Note toCardData(String id, Map<String, Object> doc) {
        Note answer = new Note((String) doc.get(Fields.TITLE),
                1,
                (String) doc.get(Fields.TITLE),
                (String) doc.get(Fields.DESCRIPTION),
                "",
                000);
        answer.setId(id);
        return answer;
    }

    public static Map<String, Object> toDocument(Note cardData) {
        Map<String, Object> answer = new HashMap<>();
        answer.put(Fields.TITLE, cardData.getTitle());
        answer.put(Fields.DESCRIPTION, cardData.getDescription());
        answer.put(Fields.COLOUR, cardData.getColour());
        return answer;
    }
}
