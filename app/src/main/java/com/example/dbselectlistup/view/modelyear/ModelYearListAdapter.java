package com.example.dbselectlistup.view.modelyear;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dbselectlistup.MainActivity;
import com.example.dbselectlistup.R;
import com.example.dbselectlistup.view.engine.EngineFragment;

import java.util.ArrayList;

public class ModelYearListAdapter extends RecyclerView.Adapter<ModelYearListAdapter.Holder> {
    private Context context;
    private ArrayList<ModelYearItem> items = new ArrayList<>();

    public ModelYearListAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.modelyearitem, parent, false);

        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        holder.setItem(items.get(position));

        int pos = position;
        holder.modelYearText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String modelYearText = holder.modelYearText.getText().toString();

                SharedPreferences preferences = context.getSharedPreferences("selectionList", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("modelYear", modelYearText);
                editor.commit();

                EngineFragment engineFragment = new EngineFragment();
                ((FragmentActivity) context).getSupportFragmentManager().beginTransaction().replace(R.id.fragmentLayout, engineFragment).commit();

                ((MainActivity) MainActivity.context_main).setButtonNotClicked();
                ((MainActivity) MainActivity.context_main).setButtonEngineClicked();
                ((MainActivity) MainActivity.context_main).setModelYearText(modelYearText);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (items != null) { return items.size(); }
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public void addItem(ModelYearItem item) {
        items.add(item);
        notifyDataSetChanged();
    }

    class Holder extends RecyclerView.ViewHolder {
        TextView modelYearText;

        public Holder(@NonNull View itemView) {
            super(itemView);
            modelYearText = itemView.findViewById(R.id.modelyeartext);
        }

        public void setItem(ModelYearItem item) {
            modelYearText.setText(item.getModelYear());
        }
    }
}
