package com.shayer.samebirthday.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.shayer.samebirthday.R;

import java.util.ArrayList;

import com.shayer.samebirthday.model.ResultBirthdateModel;

/**
 * Created by Shreeya Patel on 3/8/2016.
 */
public class ResultBirthdateAdapter extends BaseAdapter {
    ViewHolder holder;
    private LayoutInflater inflater;
    private Context mcontext;
    private ArrayList<ResultBirthdateModel> productItemList;
    SharedPreferences pref;
    SharedPreferences.Editor editor;

    class ViewHolder {
        TextView tvUserName, tvGender, tvBirthdate, tvUserId;
        ImageView ivArrow;

        ViewHolder() {
        }
    }

    public ResultBirthdateAdapter(ArrayList<ResultBirthdateModel> tempItemList, Context context) {
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
            convertView = inflater.inflate(R.layout.row_match_birthdate_listview, null);
            Log.d("TAG", "POSITION : " + position);
            holder = new ViewHolder();

            holder.tvUserName = (TextView) convertView.findViewById(R.id.tvMatchedUsername);
            holder.tvGender = (TextView) convertView.findViewById(R.id.tvUserGender);
            holder.tvBirthdate = (TextView) convertView.findViewById(R.id.tvUserBirthDate);
            holder.tvUserId = (TextView) convertView.findViewById(R.id.tvMatchedUserId);
            holder.ivArrow = (ImageView) convertView.findViewById(R.id.ivArrow);
            convertView.setTag(this.holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tvUserName.setText((productItemList.get(position)).getUserName());
        holder.tvGender.setText(productItemList.get(position).getUserGender());
        holder.tvBirthdate.setText(productItemList.get(position).getUserBirthdate());
        holder.tvUserId.setText(productItemList.get(position).getUserId());

//        holder.ivArrow.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i = new Intent(mcontext, ChatBoardActivity.class);
//                i.putExtra("UserId", holder.UserId);
//                Toast.makeText(mcontext,  holder.UserId + "", Toast.LENGTH_LONG).show();
//                mcontext.startActivity(i);
//            }
//        });
        return convertView;
    }
}
