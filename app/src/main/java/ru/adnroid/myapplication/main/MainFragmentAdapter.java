package ru.adnroid.myapplication.main;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ru.adnroid.myapplication.Notes;
import ru.adnroid.myapplication.R;
import ru.adnroid.myapplication.main.MainFragment.onClickItem;

public class MainFragmentAdapter extends RecyclerView.Adapter<MainFragmentAdapter.MainFragmentViewHolder> {

    private ArrayList<Notes> notes;
    private final onClickItem onClickItem;

    public MainFragmentAdapter(ArrayList<Notes> notes, onClickItem onClickItem) {
        this.notes = notes;
        this.onClickItem = onClickItem;
    }

    @NonNull
    @Override
    public MainFragmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new MainFragmentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MainFragmentViewHolder holder, int position) {
        holder.bind(notes.get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    void setNewList(ArrayList<Notes> notes) {
        this.notes = notes;
        notifyDataSetChanged();
    }

    class MainFragmentViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView;

        public MainFragmentViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.list_item_text_view);
            itemView.setOnClickListener(v -> {
                onClickItem.onClick(notes.get(getAdapterPosition()));
            });
        }

        private void bind(String notes) {
            textView.setText(notes);
        }
    }
}
