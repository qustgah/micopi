package com.ijiaban.micopi.fragment;

import java.util.List;

import com.ijiaban.yinxiang.R;
import com.ijiaban.yinxiang.util.CommonViewAdapter;
import com.ijiaban.yinxiang.util.MImageView;
import com.ijiaban.yinxiang.util.NativeImageLoader;
import com.ijiaban.yinxiang.util.MImageView.OnMeasureListener;
import com.ijiaban.yinxiang.util.NativeImageLoader.NativeImageCallBack;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;

public class LocalImageFragment extends Fragment{
	private GridView gridView;
	private CommonViewAdapter adapter;
	private List<String> list;
	private Context context;
	private PassData send;
	private Point point; //用来封装ImageView的宽和高的对象
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_localimage, container, false);
		gridView = (GridView) view.findViewById(R.id.imageView); 
		list = getArguments().getStringArrayList("data");
		point = new Point(0, 0);
		init();
		
		return view;
	}
	
	private void init(){
		adapter = new CommonViewAdapter(list, getActivity()){
            Holder holder;
			@Override
			public View setView(View convertView, int position, ViewGroup parent) {
				String path = list.get(position);
			    if(convertView == null){
			    	holder = new Holder();
			    	convertView = common_inflater.inflate(R.layout.item_fragmentlocalimage, null);
			    	holder.image = (MImageView) convertView.findViewById(R.id.image);
			    	
			    	//监听imageview的宽和高
			    	holder.image.setOnMeasureListener(new OnMeasureListener(){

						@Override
						public void onMeasureSize(int width, int height) {
							point.set(width, height);
						}
			    		
			    	});
			    	convertView.setTag(holder);
			    }else{
			    	holder = (Holder) convertView.getTag();
			    }
			    holder.image.setTag(path);
			    Bitmap bitmap = NativeImageLoader.getInstance().loadNativeImage(path, point, new NativeImageCallBack(){

					@Override
					public void onImageLoader(Bitmap bitmap, String path) {
						ImageView mImageView = (ImageView) gridView.findViewWithTag(path);
						if(bitmap != null && mImageView != null){
							mImageView.setImageBitmap(bitmap);
						}
					}
			    	
			    });
			    if(bitmap != null){
			    	holder.image.setImageBitmap(bitmap);
			    }else{
			    	holder.image.setImageResource(R.drawable.ic_launcher);;
			    }
				return convertView;
			}

			@Override
			public long setItemId(int position) {
				// TODO Auto-generated method stub
				return position;
			}
			
		};
		gridView.setAdapter(adapter);
		
		gridView.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				send.passDatatoActivity(list.get(position));
			}
			
		});
		
	}
	class Holder{
		MImageView image;
	}
	
	public interface PassData{
		public void passDatatoActivity(String url);
		
	}
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		context = getActivity();
		send = (PassData) context;
	}

	
}
