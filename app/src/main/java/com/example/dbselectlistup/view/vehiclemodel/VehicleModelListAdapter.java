package com.example.dbselectlistup.view.vehiclemodel;

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
import com.example.dbselectlistup.view.modelyear.ModelYearFragment;

import java.util.ArrayList;

public class VehicleModelListAdapter extends RecyclerView.Adapter<VehicleModelListAdapter.Holder> {

    private Context context;
    private ArrayList<VehicleModelItem> items = new ArrayList<>();

    public VehicleModelListAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.modelitem, parent, false);

        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        holder.setItem(items.get(position));

        holder.modelText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String modelText = holder.modelText.getText().toString();

                SharedPreferences preferences = context.getSharedPreferences("selectionList", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("model", modelText);
                editor.commit();

                ModelYearFragment modelYearFragment = new ModelYearFragment();
                ((FragmentActivity) context).getSupportFragmentManager().beginTransaction().replace(R.id.fragmentLayout, modelYearFragment).commit();

                ((MainActivity) MainActivity.context_main).setButtonNotClicked();
                ((MainActivity) MainActivity.context_main).setButtonModelYearClicked();
                ((MainActivity) MainActivity.context_main).setModelText(modelText);
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

    public void addItem(ArrayList<VehicleModelItem> list) {
        this.items = list;
        notifyDataSetChanged();
    }

    class Holder extends RecyclerView.ViewHolder {
        TextView modelText;

        public Holder(@NonNull View itemView) {
            super(itemView);
            modelText = itemView.findViewById(R.id.modeltext);
        }

        public void setItem(VehicleModelItem vehicleModelItem) {
            modelText.setText(vehicleModelItem.getModel());
        }
    }
}
