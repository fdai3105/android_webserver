package com.example.database_webserver_hanghoa.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.database_webserver_hanghoa.HangHoa;
import com.example.database_webserver_hanghoa.R;

import java.util.ArrayList;

public class HangHoaAdapter extends RecyclerView.Adapter<HangHoaAdapter.HangHoaHolder> {
    private Context context;
    private ArrayList<HangHoa> hangHoas;

    public HangHoaAdapter(Context context, ArrayList<HangHoa> hangHoas) {
        this.context = context;
        this.hangHoas = hangHoas;
    }

    /*********************/
    //setOnClick > line 78
    private static OnItemClickListener listener;

    public interface OnItemClickListener  {
        void onItemClick(View itemView, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
    /*********************/


    @NonNull
    @Override
    public HangHoaAdapter.HangHoaHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_hanghoa, parent, false);
        return new HangHoaHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull HangHoaAdapter.HangHoaHolder holder, int position) {
        Glide.with(context).load(hangHoas.get(position).getAnhSP()).into(holder.iv_imageSP);
        holder.tv_idSP.setText(hangHoas.get(position).getIdSP()+"");
        holder.tv_tenSP.setText(hangHoas.get(position).getTenSP() + "");
        holder.tv_giatienSP.setText(hangHoas.get(position).getGiaSP() + "");
        holder.tv_motaSP.setText(hangHoas.get(position).getMotaSP() + "");

    }

    @Override
    public int getItemCount() {
        return hangHoas.size();
    }

    public class HangHoaHolder extends RecyclerView.ViewHolder {
        TextView tv_idSP;
        ImageView iv_imageSP;
        TextView tv_tenSP;
        TextView tv_giatienSP;
        TextView tv_motaSP;

        public HangHoaHolder(@NonNull final View itemView) {
            super(itemView);
            tv_idSP = itemView.findViewById(R.id.tv_idSP);
            iv_imageSP = itemView.findViewById(R.id.iv_imageSP);
            tv_tenSP = itemView.findViewById(R.id.tv_tenSP);
            tv_giatienSP = itemView.findViewById(R.id.tv_giatienSP);
            tv_motaSP = itemView.findViewById(R.id.tv_motaSP);

            //setOnItemClick
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null)
                        listener.onItemClick(itemView, getLayoutPosition());
                }
            });
        }
    }
}
