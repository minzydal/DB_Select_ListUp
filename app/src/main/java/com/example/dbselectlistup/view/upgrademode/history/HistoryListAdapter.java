package com.example.dbselectlistup.view.upgrademode.history;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dbselectlistup.R;

import java.util.ArrayList;

public class HistoryListAdapter extends RecyclerView.Adapter<HistoryListAdapter.Holder> {
    private Context context;
    private ArrayList<HistoryItem> items = new ArrayList<>();

    public HistoryListAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.selectedlistitem, parent, false);

        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        holder.setItem(items.get(position));

        SharedPreferences preferences = context.getSharedPreferences("selectionList", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("selectedEvent", "");
        editor.commit();
    }

    @Override
    public int getItemCount() {
        if (items != null) {
            return items.size();
        }
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public void addItem(HistoryItem historyItem) {
        items.add(historyItem);
        notifyDataSetChanged();
    }


    class Holder extends RecyclerView.ViewHolder {
        TextView history;

        public Holder(@NonNull View itemView) {
            super(itemView);

            history = itemView.findViewById(R.id.selectedList);
        }

        public void setItem(HistoryItem item) {
            history.setText(item.getHistory());
        }
    }
}
