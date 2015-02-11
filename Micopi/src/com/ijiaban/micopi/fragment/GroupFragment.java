package com.ijiaban.micopi.fragment;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;




import com.ijiaban.yinxiang.R;
import com.ijiaban.yinxiang.bean.GroupBean;
import com.ijiaban.yinxiang.util.GroupAdapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.Toast;

public class GroupFragment extends Fragment {
	
    private GridView groupGrid;
    private HashMap<String, List<String>> groupMap;
    private List<GroupBean> list;
    private GroupAdapter gAdapter;
   
    private Context context;
    private PassGroup send;
    private Handler handler;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_groupimage, container, false);
		
		getImages();
	    groupGrid = (GridView) view.findViewById(R.id.groupGrid);
	    groupMap = new HashMap<String, List<String>>();
	    list = new ArrayList<GroupBean>();
	    init();
		return view; 
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		context = getActivity();
		send = (PassGroup) context;
	}

    
	@SuppressLint("HandlerLeak")
	private void init() {
		handler = new Handler(){

			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				switch(msg.what){
				case 0x123:
					list = subGroupOfImage(groupMap);
					gAdapter = new GroupAdapter(getActivity(), list, groupGrid);
					groupGrid.setAdapter(gAdapter);
			    break;
			    
				}
				
			}
	    	
	    };
		
		
		groupGrid.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				List<String> childList = groupMap.get(list.get(position).getFolderName());
			    send.sendGroup(childList);
			}
	    	
	    });
	}
	
	
	/**
	 * 回调接口
	 * @author Admin
	 *
	 */
	public interface PassGroup{
		public void sendGroup(List<String> list);
	} 
	/**
	 * 利用ContentProvider扫描手机中的图片，此方法在运行在子线程中
	 */
	private void getImages() {
		if(!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
			Toast.makeText(getActivity(), "暂无外部存储", Toast.LENGTH_SHORT).show();
			return;
		}
		
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
				ContentResolver mContentResolver = getActivity().getContentResolver();

				//只查询jpeg和png的图片
				Cursor mCursor = mContentResolver.query(mImageUri, null,
						MediaStore.Images.Media.MIME_TYPE + "=? or "
								+ MediaStore.Images.Media.MIME_TYPE + "=?",
						new String[] { "image/jpeg", "image/png" }, MediaStore.Images.Media.DATE_MODIFIED);
				while (mCursor.moveToNext()) {
					//获取图片的路径
					String path = mCursor.getString(mCursor
							.getColumnIndex(MediaStore.Images.Media.DATA));
					
					//获取该图片的父路径名
					String parentName = new File(path).getParentFile().getName();

					 
					//根据父路径名将图片放入到mGruopMap中
					if (!groupMap.containsKey(parentName)) {
						List<String> chileList = new ArrayList<String>();
						chileList.add(path);
						groupMap.put(parentName, chileList);
					} else {
						groupMap.get(parentName).add(path);
					}
				}
				
				mCursor.close();
				
				//通知Handler扫描图片完成
				handler.sendEmptyMessage(0x123);
				
			}
		}).start(); 
	}
	
	
	
	/**
	 * 组装分组界面GridView的数据源，因为我们扫描手机的时候将图片信息放在HashMap中
	 * 所以需要遍历HashMap将数据组装成List
	 * 
	 * @param mGruopMap
	 * @return
	 */
	private List<GroupBean> subGroupOfImage(HashMap<String, List<String>> mGruopMap){
		if(mGruopMap.size() == 0){
			return null;
		}
		List<GroupBean> list = new ArrayList<GroupBean>();
		
		Iterator<Map.Entry<String, List<String>>> it = mGruopMap.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, List<String>> entry = it.next();
			GroupBean mImageBean = new GroupBean();
			String key = entry.getKey();
			List<String> value = entry.getValue();
			
			mImageBean.setFolderName(key);
			mImageBean.setImageCounts(value.size());
			mImageBean.setTopImagePath(value.get(0));//获取该组的第一张图片
			
			list.add(mImageBean);
		}
		
		return list;
		
	}
	

	

}
