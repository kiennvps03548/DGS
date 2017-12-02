package dgs.example.pungkook.dgs_android.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.daniribalbert.customfontlib.views.CustomFontTextView;


import java.util.ArrayList;

import dgs.example.pungkook.dgs_android.R;
import dgs.example.pungkook.dgs_android.model.CommonClass;

/**
 * Created by Julian on 5/11/2017.
 */

public class CommonAdapter extends BaseAdapter {

    Context context;
    int resource;
    ViewHolder viewHolder;
    ArrayList<CommonClass> list;


    private static class ViewHolder {
        private CustomFontTextView itemView;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public CommonClass getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    public CommonAdapter(ArrayList<CommonClass> objects, Context context) {
        this.context = context;
        this.list = objects;
    }

    public CustomFontTextView getTextView() {
        return viewHolder.itemView;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(context)
                    .inflate(R.layout.row_common, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.itemView = (CustomFontTextView) convertView.findViewById(R.id.textView);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        CommonClass item = getItem(position);
        if (item!= null) {
            viewHolder.itemView.setText(item.getName());
        }

        return convertView;
    }

}

