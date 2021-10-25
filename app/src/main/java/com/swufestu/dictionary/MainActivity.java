package com.swufestu.dictionary;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{
    ListView list;
    SearchView searchView;
    ArrayList<HashMap<String,String>> listItems = new ArrayList<HashMap<String,String>>();


    private static final String TAG = "onCreate";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        list = findViewById(R.id.list);

        //读取dictionary.txt文件，json解析
        String result = getJson("dictionary.txt");
        try {
            JSONArray json = new JSONArray(result);
            for(int i=0;i<json.length();i++) {
                HashMap<String,String> map = new HashMap<String,String>();
                JSONObject jb=json.getJSONObject(i);
                //按照文档格式，拆分出word:,sent:和type:内容
                map.put("word",jb.getString("word"));
                map.put("sent",jb.getString("sent"));
                map.put("type",jb.getString("type"));
                //Log.i(TAG, "onCreate: map:"+map);
                listItems.add(map);
            }
            Log.i(TAG, "onCreate: list:"+listItems);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i(TAG, "handleMessage: listItems"+listItems);

        SimpleAdapter listAdapter = new SimpleAdapter(
               MainActivity.this,
                listItems,
                R.layout.list_item,
                new String[]{"word"},
                new int[]{R.id.wordpaper});
        list.setAdapter(listAdapter);

        list.setOnItemClickListener(this);

        //设置查找
        searchView = findViewById(R.id.searchview);
        searchView.setIconifiedByDefault(false);
        //设置该SearchView显示搜索按钮
        searchView.setSubmitButtonEnabled(true);
        //为该SearchView组件设置事件监听器
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            //单机搜索按钮时激发该方法
            @Override
            public boolean onQueryTextSubmit(String query) {
                //实际应用中应该在该方法内执行实际查询，此处仅使用Toast显示用户输入的查询内容
                Toast.makeText(MainActivity.this, "你的选择是：" + query,
                        Toast.LENGTH_SHORT).show();
                return false;
            }

            //用户输入字符时激发该方法
            @Override
            public boolean onQueryTextChange(String newText) {
                if (!TextUtils.isEmpty(newText)) {
                    listAdapter.getFilter().filter(newText);
                } else {
                    list.clearTextFilter();
                }
                return true;
            }
        });
    }

    //按照文件名，读取assets文件夹内的文件
    public String getJson(String fileName) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            AssetManager assetManager = MainActivity.this.getAssets();
            BufferedReader bf = new BufferedReader(new InputStreamReader(assetManager.open(fileName)));
            String line;
            while ((line = bf.readLine()) != null) {
                stringBuilder.append(line);
                //Log.d("AAA", line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        Object itemAtPosition = list.getItemAtPosition(position);
        HashMap<String,String> map = (HashMap<String,String>)itemAtPosition;
        String word = map.get("word");
        String sent = map.get("sent");
        String type = map.get("type");
        Log.i(TAG, "onItemClick: word:"+word);
        Log.i(TAG, "onItemClick: sent:"+sent);
        Log.i(TAG, "onItemClick: type:"+type);

        Intent config = new Intent(this,WordPage.class);
        Bundle bdl = new Bundle();
        bdl.putString("word",word);
        bdl.putString("sent",sent);
        bdl.putString("type",type);
        config.putExtras(bdl);
        startActivityForResult(config,1);

    }

}