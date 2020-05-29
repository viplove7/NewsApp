package com.example.viplove.newsapp;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class NewsAdapter extends ArrayAdapter<News> {


    private int[] colors = new int[]{0x30FF0000, 0x300000FF};

    public NewsAdapter(Context context, ArrayList<News> array) {
        super(context, 0, array);


    }

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }


        News currentnews = getItem(position);

        int colorPos = position % colors.length;
        listItemView.setBackgroundColor(colors[colorPos]);

        TextView sectionname = (TextView) listItemView.findViewById(R.id.section_name);
        String section = currentnews.getSectionName();
        sectionname.setText(section);

        TextView webtitle = (TextView) listItemView.findViewById(R.id.web_title);
        String title = currentnews.getWebTitle();
        webtitle.setText(title);

        String dateandtime[] = date1(currentnews.getDate());

        TextView textdate = (TextView) listItemView.findViewById(R.id.date);
        textdate.setText(dateandtime[0]);

        TextView time = (TextView) listItemView.findViewById(R.id.time);
        time.setText(dateandtime[1]);

        TextView author=(TextView)listItemView.findViewById(R.id.author);
        String author1=currentnews.getAuthor();
        author.setText(author1);

        return listItemView;

    }

    public static String[] date1(String date) {
        int index = date.indexOf("T");
        int end = date.length();
        String dateandtime[] = new String[2];
        dateandtime[0] = date.substring(0, index);
        dateandtime[1] = date.substring(index + 1, end - 1);
        return dateandtime;
    }

}
