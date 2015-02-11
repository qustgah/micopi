package com.ijiaban.micopi.fragment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.ijiaban.yinxiang.R;
import com.ijiaban.yinxiang.util.CommonViewAdapter;
import com.squareup.picasso.Picasso;
import com.umeng.analytics.MobclickAgent;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;
/**
 * 系统模板fragment
 * @author Administrator
 *
 */
public abstract class SystemPicFragment extends Fragment{

	private String kindname;
	private GridView gridview;
	private AssetManager assetManager;
	private CommonViewAdapter adapter;
	private List<String> picpaths; 
	
	public SystemPicFragment() {
	}
	
	public SystemPicFragment(String kindname) { 
		this.kindname =kindname;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) { 
		
		View view = inflater.inflate(R.layout.fragment_systempic, null);
		assetManager = getActivity().getAssets();
		gridview  = (GridView) view.findViewById(R.id.grid_sys_pics);
		gridview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) { 
				
				changePath((String)gridview.getItemAtPosition(arg2));
				MobclickAgent.onEvent(getActivity(),kindname,(String)gridview.getItemAtPosition(arg2)); 
			}
		});
		picpaths  = getPicList(kindname);
		InitialAdapter();
		gridview.setAdapter(adapter); 
		return view;
	}
	
	public void InitialAdapter(){
		adapter = new CommonViewAdapter(picpaths,getActivity()){
			@Override
			public View setView(View convertView, int position, ViewGroup parent) { 
				ImageView imageview ;
				if(convertView == null){
					convertView = getActivity().getLayoutInflater().inflate(R.layout.item_grid_systempic, null);
					imageview   = (ImageView) convertView.findViewById(R.id.img_systempic); 
				}else{
					imageview = (ImageView) convertView.findViewById(R.id.img_systempic);
				}
				Picasso.with(getActivity()).load("file:///android_asset/"+picpaths.get(position)).into(imageview); 
				return convertView;
			} 
			@Override
			public long setItemId(int position) { 
				return position;
			}
		}; 
	} 
	/**
	 * 获取一个类型的所有本类图片的集合
	 * @param kindname
	 * @return
	 */
	public List<String> getPicList(String kindname){
		List<String> list = new ArrayList<String>(); 
		String photoName = null;  
		// 使用list()方法获取某文件夹下所有文件的名字
		String[] photos;
		try {
			photos = assetManager.list(kindname);
			for (int i = 0; i < photos.length; i++) {
				photoName = photos[i];
				String str = kindname + "/" + photoName;
				list.add(str);
			}
			return list;
		} catch (IOException e) { 
			e.printStackTrace();
		} 
		return null;
	} 
	@Override
	public void onPause() { 
		super.onPause();
		MobclickAgent.onPageEnd(kindname);
	}
	@Override
	public void onResume() { 
		super.onResume();
		MobclickAgent.onPageStart(kindname);
	}
	 
	public abstract void changePath(String changepath); 
}
