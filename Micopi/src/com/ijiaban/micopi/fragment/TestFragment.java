package com.ijiaban.micopi.fragment;
 
import com.ijiaban.yinxiang.R;

import android.os.Bundle;
import android.support.v4.app.Fragment; 
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class TestFragment extends Fragment {
	
	private TextView textview ;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) { 
		
		View view = inflater.inflate(R.layout.fragment_test, null);
		textview  = (TextView) view.findViewById(R.id.text_test); 
		return view;
	}
	 
	

}
