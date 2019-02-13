package com.fengxing.mobile.commonutils.test;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.fengxing.mobile.commonutils.R;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;

public class TestDeleteListActivity extends AppCompatActivity {
    private DeleteListView myListView;

    private MyAdapter adapter;

    private List<String> contentList = new ArrayList<String>();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //testDeleteList();

        setContentView(R.layout.test_rotation_rocket_view);


    }

    private void testDeleteList() {
        setContentView(R.layout.test_delete_list_activity);
        initList();
        myListView = (DeleteListView) findViewById(R.id.my_list_view);
        myListView.setOnDeleteListener(new DeleteListView.OnDeleteListener() {
            @Override
            public void onDelete(int index) {
                contentList.remove(index);
                adapter.notifyDataSetChanged();
            }
        });
        adapter = new MyAdapter(this, 0, contentList);
        myListView.setAdapter(adapter);
    }

    private void initList() {
        contentList.add("Content Item 1");
        contentList.add("Content Item 2");
        contentList.add("Content Item 3");
        contentList.add("Content Item 4");
        contentList.add("Content Item 5");
        contentList.add("Content Item 6");
        contentList.add("Content Item 7");
        contentList.add("Content Item 8");
        contentList.add("Content Item 9");
        contentList.add("Content Item 10");
        contentList.add("Content Item 11");
        contentList.add("Content Item 12");
        contentList.add("Content Item 13");
        contentList.add("Content Item 14");
        contentList.add("Content Item 15");
        contentList.add("Content Item 16");
        contentList.add("Content Item 17");
        contentList.add("Content Item 18");
        contentList.add("Content Item 19");
        contentList.add("Content Item 20");

    }

    public class MyAdapter extends ArrayAdapter<String> {

        public MyAdapter(Context context, int textViewResourceId, List<String> objects) {
            super(context, textViewResourceId, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view;
            if (convertView == null) {
                view = LayoutInflater.from(getContext()).inflate(R.layout.test_list_view_item, null);
            } else {
                view = convertView;
            }
            TextView textView = (TextView) view.findViewById(R.id.text_view);
            textView.setText(getItem(position));
            return view;
        }

    }
}
