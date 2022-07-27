package com.example.dbselectlistup.view.upgrademode.all;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dbselectlistup.R;
import com.example.dbselectlistup.view.upgrademode.UpgradeListItem;

import java.util.ArrayList;

public class AllEventListAdapter extends RecyclerView.Adapter<AllEventListAdapter.Holder> {
    private Context context;
    private ArrayList<UpgradeListItem> items = new ArrayList<>();

    private int lastCheckedPosition = -1;

    public AllEventListAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.eventlistitem, parent, false);

        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        holder.setItem(items.get(position));
        holder.radioButton.setChecked(position == lastCheckedPosition);

        holder.radioButton.setClickable(false);
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int copyOfLastCheckedPosition = lastCheckedPosition;
                lastCheckedPosition = holder.getAdapterPosition();
                notifyItemChanged(copyOfLastCheckedPosition);
                notifyItemChanged(lastCheckedPosition);

                SharedPreferences preferences = context.getSharedPreferences("selectionList", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                String selectedEvent = holder.eventtxt.getText().toString();
                editor.putString("selectedEvent", selectedEvent);
                int idx = selectedEvent.indexOf(".");
                String selectEvent = selectedEvent.substring(0, idx);
                editor.putString("eventNumber", selectEvent);
                editor.commit();
            }
        });
    }

    public void initializeEventList(){
        SharedPreferences preferences = context.getSharedPreferences("selectionList", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("selectedEvent", "");
        editor.commit();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        if (items != null) {
            return items.size();
        }
        return 0;
    }

    public void addItem(ArrayList<UpgradeListItem> list) {
        this.items = list;
        notifyDataSetChanged();
    }

    class Holder extends RecyclerView.ViewHolder {
        RadioButton radioButton;
        ImageView vcitxt;
        TextView eventtxt;
        TextView typetxt;
        TextView statustxt;
        ImageView tsbtxt;
        LinearLayout layout;

        public Holder(@NonNull View itemView) {
            super(itemView);

            radioButton = itemView.findViewById(R.id.checkBox);
            vcitxt = itemView.findViewById(R.id.vcitxt);
            eventtxt = itemView.findViewById(R.id.eventtxt);
            typetxt = itemView.findViewById(R.id.typetxt);
            statustxt = itemView.findViewById(R.id.statustxt);
            tsbtxt = itemView.findViewById(R.id.tsbtxt);
            layout = itemView.findViewById(R.id.allEventListItem);
        }

        public void setItem(UpgradeListItem item) {
            vcitxt.setImageResource(item.getVci());
            eventtxt.setText(item.getEventtxt());
            typetxt.setText(item.getTypetxt());
            statustxt.setText(item.getStatustxt());
            tsbtxt.setImageResource(item.getTsbtxt());
        }
    }
}
