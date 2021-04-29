package ru.adnroid.myapplication.main;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ru.adnroid.myapplication.Note;
import ru.adnroid.myapplication.R;
import ru.adnroid.myapplication.main.MainFragment.onClickItem;
public class MainFragmentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int NOTE_TYPE = 0;
    public static final int REMINDER_TYPE = 1;
    public static final int HEADER_TYPE = 2;

    private ArrayList<Note> notes;
    private final onClickItem onClickItem;

    public MainFragmentAdapter(ArrayList<Note> notes, onClickItem onClickItem) {
        this.notes = notes;
        this.onClickItem = onClickItem;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view;
        switch (viewType) {
            case REMINDER_TYPE:
                view = inflater.inflate(R.layout.list_item_remind, parent, false);
                return new MainFragmentViewHolderReminder(view);
            case HEADER_TYPE:
                view = inflater.inflate(R.layout.list_item_header, parent, false);
                return new MainFragmentViewHolderHeader(view);
            case NOTE_TYPE:
            default:
                view = inflater.inflate(R.layout.list_item_note, parent, false);
                return new MainFragmentViewHolderNote(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        int type = notes.get(position).getType();
        Note note = notes.get(position);
        switch (type) {
            case REMINDER_TYPE:
                ((MainFragmentViewHolderReminder) holder).bind(note.getTitle(), note.getDate());
                break;
            case HEADER_TYPE:
                ((MainFragmentViewHolderHeader) holder).bind();
                break;
            default:
                ((MainFragmentViewHolderNote) holder).bind(note.getTitle(), note.getDescription(), note.getColour());
        }
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    @Override
    public int getItemViewType(int position) {
        return notes.get(position).getType();
    }

    void setNewList(ArrayList<Note> notes) {
        this.notes = notes;
        notifyDataSetChanged();
    }

    void appendItem() {
        notes.add(generateItem());
        notifyItemInserted(notes.size() - 1);
    }

    Note generateItem() {
        return new Note(NOTE_TYPE);
    }

    ///Region Holder
    class MainFragmentViewHolderNote extends RecyclerView.ViewHolder {
        private final TextView textView;
        private final TextView textViewDescription;
        private final CardView cardView;
        private Note note;
        private int position;

        public MainFragmentViewHolderNote(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.list_item_text_view);
            textViewDescription = itemView.findViewById(R.id.description);
            cardView = itemView.findViewById(R.id.card_view);
            itemView.setOnClickListener(v -> onClickItem.onClick(notes.get(getAdapterPosition()), getAdapterPosition()));
            itemView.findViewById(R.id.add_item).setOnClickListener(v -> addItem());
            itemView.findViewById(R.id.remove_item).setOnClickListener(v -> removeItem());
            itemView.findViewById(R.id.moveItemUp).setOnClickListener(v -> moveItemUp());
            itemView.findViewById(R.id.moveItemDown).setOnClickListener(v -> moveItemDown());
        }

        private void addItem() {
            notes.add(getLayoutPosition(), generateItem());
            notifyItemInserted(getLayoutPosition());
        }

        private void removeItem() {
            notes.remove(getLayoutPosition());
            notifyItemRemoved(getLayoutPosition());
        }

        private void bind(String notes, String description, int color) {
            textView.setText(notes);
            textViewDescription.setText(description);
            if (color == 0) {
                color = R.color.white;
            }
            cardView.setBackgroundColor(ContextCompat.getColor(textView.getContext(), color));

        }

        private void moveItemUp() {
            position = getLayoutPosition();
            if (position > 1) {
                note = notes.get(position - 1);
                notes.remove(notes.get(position - 1));
                notes.add(position, note);
                notifyItemMoved(position, position - 1);
            }
        }

        private void moveItemDown() {
            position = getLayoutPosition();
            if (position < getItemCount() - 1) {
                note = notes.get(position + 1);
                notes.remove(notes.get(position + 1));
                notes.add(position, note);
                notifyItemMoved(position, position + 1);
            }
        }
    }

    class MainFragmentViewHolderReminder extends RecyclerView.ViewHolder {

        private final TextView textView;
        private final TextView date;

        public MainFragmentViewHolderReminder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.list_item_reminder);
            date = itemView.findViewById(R.id.list_item_date);
            itemView.setOnClickListener(v -> onClickItem.onClick(notes.get(getAdapterPosition()), getAdapterPosition()));
        }

        private void bind(String reminder, String reminderDate) {
            textView.setText(reminder);
            date.setText(reminderDate);
        }
    }

    class MainFragmentViewHolderHeader extends RecyclerView.ViewHolder {

        private final TextView textView;

        public MainFragmentViewHolderHeader(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.list_item_header);
            itemView.setOnClickListener(v -> onClickItem.onClick(notes.get(getAdapterPosition()), getAdapterPosition()));
        }

        private void bind() {
            textView.setText(R.string.list_header);
        }
    }
    ///End Region Holder
}
