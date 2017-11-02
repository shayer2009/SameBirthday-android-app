package com.shayer.samebirthday.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.shayer.samebirthday.R;
import com.shayer.samebirthday.model.MessageHistoryModel;

import java.util.ArrayList;

/**
 * Created by Shreeya Patel on 3/16/2016.
 */
public class MessageHistoryAdapter extends BaseAdapter {

    ViewHolder holder;
    private LayoutInflater inflater;
    private Context mcontext;
    private ArrayList<MessageHistoryModel> productItemList;

    class ViewHolder {
        TextView tvMessageUserName, tvMessageDate, tvHistoryMessage, tvMessageUserId;
        ViewHolder() {
        }
    }

    public MessageHistoryAdapter(ArrayList<MessageHistoryModel> tempItemList, Context context) {
        inflater = null;
        productItemList = tempItemList;
        mcontext = context;
        inflater = (LayoutInflater) this.mcontext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return productItemList.size();
    }

    @Override
    public Object getItem(int position) {
        return productItemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return (long) position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.row_msg_history, null);
            Log.d("TAG", "POSITION : " + position);
            holder = new ViewHolder();

            holder.tvMessageUserName = (TextView) convertView.findViewById(R.id.tvMsgHistoryUsername);
            holder.tvHistoryMessage = (TextView) convertView.findViewById(R.id.tvMsgHistoryMessage);
            holder.tvMessageDate = (TextView) convertView.findViewById(R.id.tvMessageDate);
            holder.tvMessageUserId = (TextView) convertView.findViewById(R.id.tvMsgHistoryUserId);
            convertView.setTag(this.holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tvMessageUserName.setText((productItemList.get(position)).getHistoryMessageUserName());
        holder.tvHistoryMessage.setText(productItemList.get(position).getHistoryMessage());
        holder.tvMessageDate.setText(productItemList.get(position).getHistoryMessageDate());
        holder.tvMessageUserId.setText(productItemList.get(position).getHistoryMessageUserId());

        return convertView;
    }
}
