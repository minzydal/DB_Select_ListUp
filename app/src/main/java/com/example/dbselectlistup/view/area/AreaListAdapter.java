package com.example.dbselectlistup.view.area;

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
import com.example.dbselectlistup.view.vehiclemodel.VehicleModelFragment;

import java.util.ArrayList;

public class AreaListAdapter extends RecyclerView.Adapter<AreaListAdapter.Holder> {

    private Context context;
    private ArrayList<AreaItem> items = new ArrayList<>();

    public AreaListAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.areaitem, parent, false);

        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        holder.setItem(items.get(position));

        // 지역 리스트 항목 선택 시
        holder.areaText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //선택한 지역이랑 몇번째 지역 선택했는지 Shared Preference에 저장
                SharedPreferences preferences = context.getSharedPreferences("selectionList", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("area", holder.areaText.getText().toString());
                editor.putInt("areaPosition", holder.getAdapterPosition());
                editor.commit();

                VehicleModelFragment modelFragment = new VehicleModelFragment();
                ((FragmentActivity) context).getSupportFragmentManager().beginTransaction().replace(R.id.fragmentLayout, modelFragment).commit();

                ((MainActivity) MainActivity.context_main).setButtonNotClicked();
                ((MainActivity) MainActivity.context_main).setButtonModelClicked();
                String areaText = holder.areaText.getText().toString();
                ((MainActivity) MainActivity.context_main).setAreaText(areaText);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (items != null) { return items.size(); }
        return 0;
    }

    public void addItem(ArrayList<AreaItem> list) {
        this.items = list;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }


    class Holder extends RecyclerView.ViewHolder {
        TextView areaText;

        public Holder(@NonNull View itemView) {
            super(itemView);
            areaText = itemView.findViewById(R.id.areatext);
        }

        public void setItem(AreaItem areaItem) {
            areaText.setText(areaItem.getFileName());
        }
    }
}
