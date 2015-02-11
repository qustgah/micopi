package com.ijiaban.yinxiang.util;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public abstract class CommonViewAdapter extends BaseAdapter{

	/**
	 * 要加载的数据
	 */
	public List<?> common_datas = new ArrayList<Object>(); 
	
	public LayoutInflater      common_inflater;
	/**
	 * 上下文
	 */
	public Context common_context; 
	
	public CommonViewAdapter(List<?> list,Context context) {
		// TODO 初始值
		this.common_datas = list; 
		this.common_context = context;

		this.common_inflater = LayoutInflater.from(context);

	} 
	@Override
	public int getCount() {
		// TODO 获取行数
		return common_datas.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO  获取某个item的数据
		return common_datas.get(position);
	} 
	@Override
	public long getItemId(int position) {
		// TODO 获取标识
		return setItemId(position);
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO 初始化每一个item
		return setView(convertView, position, parent);
	}
	/**
	 * 初始化 getView()
	 * @param convertView
	 * @param position
	 * @param parent
	 * @return
	 */
	public abstract View setView(View convertView, int position,ViewGroup parent);
	/**
	 * 获取itemid
	 * @param position
	 * @return
	 */
	public abstract long setItemId(int position );
	
	public void refreshData(List<?> datas) {
		this.common_datas = datas;
		notifyDataSetChanged();
	}  
}
