package com.jiyun.asus.shixunlianxi;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jiyun.asus.shixunlianxi.bean.Bean;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DataActivity extends AppCompatActivity {

    private ListView mListView;
    private MyAdapter myAdapter;
    private List<Bean.PositionBean> mList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);
        initView();
        initAdapter();
        initData();
        initLiner();
    }

    private void initLiner() {
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(DataActivity.this,TuActivity.class);
                intent.putExtra("list",mList.get(i));
                startActivity(intent);
            }
        });

        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                return true;
            }
        });
    }

    private void initData() {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url("http://123.206.14.104:8080/yuekao/gaoji.json").build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("TAG",e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String string = response.body().string();
                Gson gson = new Gson();
                Bean bean = gson.fromJson(string, Bean.class);
                List<Bean.PositionBean> position = bean.getPosition();
                mList.addAll(position);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        myAdapter.notifyDataSetChanged();
                    }
                });
            }
        });
    }

    private void initAdapter() {
        myAdapter = new MyAdapter();
        mListView.setAdapter(myAdapter);
    }

    private void initView() {
        mListView = (ListView) findViewById(R.id.mListView);
    }

    private class MyAdapter extends BaseAdapter {

        private ViewHolder viewHolder;

        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            if (view == null) {
                view = View.inflate(DataActivity.this, R.layout.item, null);
                viewHolder = new ViewHolder(view);
                view.setTag(viewHolder);
            }else {
                viewHolder = (ViewHolder) view.getTag();
            }
            viewHolder.mName.setText(mList.get(i).getCity());
            return view;
        }

        public  class ViewHolder {
            public View rootView;
            public TextView mName;

            public ViewHolder(View rootView) {
                this.rootView = rootView;
                this.mName = (TextView) rootView.findViewById(R.id.mName);
            }

        }
    }
}
