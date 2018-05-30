package com.example.srivi.messageme;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by srivi on 25-04-2018.
 */

public class EmailAdapter extends ArrayAdapter<Email> {

    public EmailAdapter(@NonNull Context context, int resource, @NonNull List<Email> objects) {
        super( context, resource, objects );
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Email email = getItem( position );
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_row, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.sender= (TextView) convertView.findViewById(R.id.tvSender);
            viewHolder.createdat= (TextView) convertView.findViewById(R.id.tvCreatedat);
            viewHolder.circle=(ImageView) convertView.findViewById(R.id.imageView);
            viewHolder.message=(TextView) convertView.findViewById(R.id.tvMsg);
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder=(ViewHolder) convertView.getTag();
        }
        viewHolder.sender.setText(email.senderName);
        viewHolder.message.setText(email.text);
        viewHolder.createdat.setText(email.time);
        if(email.isRead==true) {
            viewHolder.circle.setImageResource(R.drawable.grey);
        }
        else
            viewHolder.circle.setImageResource(R.drawable.blue);
        return convertView;

    }
    private static class ViewHolder{
        TextView sender;
        TextView message;
        ImageView circle;
        TextView createdat;
    }

}
