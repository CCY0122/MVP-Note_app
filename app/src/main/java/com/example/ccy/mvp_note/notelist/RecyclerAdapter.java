package com.example.ccy.mvp_note.notelist;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.example.ccy.mvp_note.R;
import com.example.ccy.mvp_note.data.NoteBean;

import java.util.List;

/**
 * Created by ccy on 2017-07-11.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private List<NoteBean> data;
    private OnNoteItemClickListener listener;

    public RecyclerAdapter(List<NoteBean> data, OnNoteItemClickListener l){
        this.data = data;
        this.listener = l;
    }

    //更换数据
    public void replaceData(List<NoteBean> data){
        this.data = data;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_rv_item,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        NoteBean bean = data.get(position);
        holder.checkBox.setChecked(!bean.isActive);
        holder.title.setText(bean.title+"");
        holder.content.setText(bean.content+"");
        initListener(holder,position);

    }
    private void initListener(final ViewHolder vh,final int pos) {
        if(listener != null){
            vh.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onNoteClick(data.get(pos));
                }
            });
            vh.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    return listener.onLongClick(v,data.get(pos));
                }
            });
            //一个坑：不要使用setOnCheckedChangeListener,这个监听会在每次绑定item时就调用一次
            vh.checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                        listener.onCheckChanged(data.get(pos),vh.checkBox.isChecked());
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        private TextView title;
        private TextView content;
        private CheckBox checkBox;

        public ViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            content = (TextView)itemView.findViewById(R.id.content);
            checkBox = (CheckBox) itemView.findViewById(R.id.checkbox);
        }

    }



    interface OnNoteItemClickListener {
        /**
         * item点击回调
         * @param note
         */
        void onNoteClick(NoteBean note);

        /**
         * checkBox点击回调
         * @param note
         * @param isChecked
         */
        void onCheckChanged(NoteBean note,boolean isChecked);

        /**
         *
         * @param note
         * @return  是否消费
         */
        boolean onLongClick(View v,NoteBean note);
    }
}
