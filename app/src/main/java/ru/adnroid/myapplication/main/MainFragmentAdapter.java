package ru.adnroid.myapplication.main;

import android.content.res.Configuration;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import ru.adnroid.myapplication.DetailsFragment;
import ru.adnroid.myapplication.Notes;
import ru.adnroid.myapplication.R;

import static ru.adnroid.myapplication.main.MainFragment.DETAILS_FRAGMENT_TAG;

public class MainFragmentAdapter extends RecyclerView.Adapter<MainFragmentAdapter.MainFragmentViewHolder> {

    public static Notes[] notes = new Notes[0];
    private static FragmentManager fragmentManager;
    private static int orientation;

    public MainFragmentAdapter(Notes[] notes, FragmentManager manager, int orientationApp) {
        MainFragmentAdapter.notes = notes;
        fragmentManager = manager;
        orientation = orientationApp;
    }

    public static Notes[] getNotes() {
        return notes;
    }

    @NonNull
    @Override
    public MainFragmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new MainFragmentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MainFragmentViewHolder holder, int position) {
        holder.bind(notes[position].getTitle());
    }

    @Override
    public int getItemCount() {
        return notes.length;
    }

    static class MainFragmentViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView;

        public MainFragmentViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.list_item_text_view);
            initClickListener(itemView);
        }

        private void initClickListener(@NonNull View itemView) {
            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                Notes notes = getNotes()[position];
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    transaction.replace(R.id.details_container, DetailsFragment.newInstance(notes), DETAILS_FRAGMENT_TAG);
                } else {
                    transaction.replace(R.id.list_container, DetailsFragment.newInstance(notes), DETAILS_FRAGMENT_TAG);
                }
                transaction.addToBackStack(null);
                transaction.commitAllowingStateLoss();
            });
        }

        private void bind(String notes) {
            textView.setText(notes);
        }
    }
}
