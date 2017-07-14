package com.example.hw.hwcontact.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hw.hwcontact.R;
import com.example.hw.hwcontact.bean.ContactBean;
import com.example.hw.hwcontact.util.ColorGenerator;
import com.example.hw.hwcontact.util.TextDrawable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HaodaHw on 2017/6/22.
 * 自定义适配器
 */

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.MyRecycleHolder> {
    private List<ContactBean> contactBeanList;
    private Context mContext;

    private ColorGenerator mColorGenerator = ColorGenerator.MATERIAL;
    private TextDrawable.IBuilder mDrawableBuilder = TextDrawable.builder().round();

    public ContactAdapter(Context context) {
        mContext = context;
        contactBeanList = new ArrayList<>();
    }

    public void addAll(List<ContactBean> beanList) {
        if (contactBeanList.size() > 0) {
            contactBeanList.clear();
        }
        contactBeanList.addAll(beanList);
        notifyDataSetChanged();
    }

    public void add(ContactBean bean, int position) {
        contactBeanList.add(position, bean);
        notifyItemInserted(position);
    }

    public void add(ContactBean bean) {
        contactBeanList.add(bean);
        notifyItemChanged(contactBeanList.size() - 1);
    }

    @Override
    public MyRecycleHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_item_layout, parent, false);//创建view，false是有意义的
        return new MyRecycleHolder(view);
    }

    @Override
    public void onBindViewHolder(MyRecycleHolder holder, int position) {
        if (contactBeanList == null || contactBeanList.size() == 0 || contactBeanList.size() <= position)
            return;

        ContactBean bean = contactBeanList.get(position);
        if (bean != null) {
            holder.mTextView.setText(bean.getName());
            TextDrawable drawable = mDrawableBuilder.build(String.valueOf(bean.getName().charAt(0)), mColorGenerator.getColor(bean.getName()));
            holder.mImageView.setImageDrawable(drawable);
        }
    }


    @Override
    public int getItemCount() {
        return contactBeanList.size();
    }

    //创建静态ViewHolder
    public static class MyRecycleHolder extends RecyclerView.ViewHolder {
        private ImageView mImageView;
        private TextView mTextView;

        public MyRecycleHolder(View itemView) {
            super(itemView);

            mImageView = (ImageView) itemView.findViewById(R.id.iv_img);
            mTextView = (TextView) itemView.findViewById(R.id.tv_name);
        }
    }
}
