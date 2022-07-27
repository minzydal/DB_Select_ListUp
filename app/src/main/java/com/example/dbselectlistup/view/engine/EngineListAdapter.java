package com.example.dbselectlistup.view.engine;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dbselectlistup.MainActivity;
import com.example.dbselectlistup.R;

import java.util.ArrayList;

public class EngineListAdapter extends RecyclerView.Adapter<EngineListAdapter.Holder> {

    private Context context;
    private ArrayList<EngineItem> items = new ArrayList<>();

    EngineListAdapter(Context context){
        this.context = context;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.engineitem, parent, false);

        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        holder.setItem(items.get(position));

        int pos = position;
        holder.engineText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String engineText = holder.engineText.getText().toString();
                ((MainActivity)MainActivity.context_main).setEngineText(engineText);

                SharedPreferences preferences = context.getSharedPreferences("selectionList", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("engine", holder.engineText.getText().toString());
                editor.commit();
            }
        });
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public void addItem(ArrayList<EngineItem> list) {
        this.items = list;
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        if (items != null){ return items.size(); }
        return 0;
    }

    class Holder extends RecyclerView.ViewHolder{
        TextView engineText;

        public Holder(@NonNull View itemView){
            super(itemView);

            engineText = itemView.findViewById(R.id.enginetext);
        }

        public void setItem(EngineItem engineItem){
            engineText.setText(engineItem.getEngine());
        }
    }
}
