package com.example.hw.hwcontact;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;

import com.example.hw.hwcontact.adapter.ContactAdapter;
import com.example.hw.hwcontact.bean.ContactBean;
import com.example.hw.hwcontact.util.CommonUtil;
import com.example.hw.hwcontact.view.SliderBar;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SliderBar.indexChangeListen {
    private List<ContactBean> nameList = new ArrayList<>();
    private CustomItemDecoration mItemDecoration;
    private LinearLayoutManager layoutManager;
    private RecyclerView mRecyclerView;
    private ContactAdapter mAdapter;
    private SliderBar mSliderBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
    }

    private void initViews() {
        mRecyclerView = (RecyclerView) findViewById(R.id.rl_recycle_view);
        mSliderBar = (SliderBar) findViewById(R.id.side_bar);
        mSliderBar.setindexChangeListen(MainActivity.this);
        mAdapter = new ContactAdapter(MainActivity.this);
        layoutManager = new LinearLayoutManager(MainActivity.this);
        mRecyclerView.setLayoutManager(layoutManager);

        mItemDecoration = new CustomItemDecoration(MainActivity.this);
        mRecyclerView.addItemDecoration(mItemDecoration);

        initDatas();
        mRecyclerView.setAdapter(mAdapter);
    }

    //加载数据
    private void initDatas() {
        final List<String> names = getContacter();
        Log.d("MainActivity", "联系人总数：" + names.size());

        for (int i = 0; i < names.size(); i++) {
            ContactBean bean = new ContactBean();
            bean.setName(names.get(i));
            nameList.add(bean);
        }
        //对数据源进行排序
        CommonUtil.sortData(nameList);
        //返回一个包含所有Tag字母在内的字符串并赋值给tagsStr
        String tagsStr = CommonUtil.getTags(nameList);
        mSliderBar.setIndexStr(tagsStr);
        mItemDecoration.setDatas(nameList, tagsStr);
        mAdapter.addAll(nameList);
    }

    //slideBar监听
    @Override
    public void indexChange(String tag) {
        if (TextUtils.isEmpty(tag) && nameList.size() <= 0) {
            return;
        }
        for (int i = 0; i < nameList.size(); i++) {
            if (tag.equals(nameList.get(i).getIndexTag())) {
                layoutManager.scrollToPositionWithOffset(i, 0);
            }
        }
    }

    /**
     * 获取联系人
     * 针对魅族一个bug：去除客服助手，因为其不是联系人
     */
    public List<String> getContacter() {
        List<String> names = new ArrayList<>();
        final ContentResolver mContentResolver = this.getContentResolver();
        Uri uri = Uri.parse("content://com.android.contacts/raw_contacts");
        Uri dataUri = Uri.parse("content://com.android.contacts/data");
        final Cursor cursor = mContentResolver.query(uri, null, null, null, null);
        while (cursor.moveToNext()) {
            String id = cursor.getString(cursor.getColumnIndex("_id"));
            String title = cursor.getString(cursor.getColumnIndex("display_name"));//获取联系人姓名
            String firstHeadLetter = cursor.getString(cursor.getColumnIndex("phonebook_label"));//这个字段保存了每个联系人首字的拼音的首字母
            if (!("客服助手").equals(title)) {
                names.add(title);
            }
        }
        return names;
    }
}
