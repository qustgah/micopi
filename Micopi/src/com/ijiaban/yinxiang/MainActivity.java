/*
 * Copyright (C) 2014 Easy Target
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ijiaban.yinxiang;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast; 
import android.widget.AdapterView.OnItemClickListener;
import cn.pedant.SweetAlert.SweetAlertDialog;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.ijiaban.micopi.fragment.GroupFragment.PassGroup;
import com.ijiaban.micopi.fragment.GroupFrameFragment;
import com.ijiaban.micopi.fragment.LocalImageFragment;
import com.ijiaban.micopi.fragment.LocalImageFragment.PassData;
import com.ijiaban.micopi.fragment.SystemPicFragment;
import com.ijiaban.wedgits.pagertab.PagerSlidingTabStrip;  
import com.ijiaban.yinxiang.R;
import com.ijiaban.yinxiang.engine.ColorUtilities;
import com.ijiaban.yinxiang.engine.ImageFactory;
import com.ijiaban.yinxiang.util.CommonViewAdapter;
import com.ijiaban.yinxiang.util.NativeImageLoader;
import com.squareup.picasso.Picasso;
import com.umeng.analytics.MobclickAgent;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Activity that displays the generated image and all the options.
 *
 * Created by Michel on 03.02.14.
 */
public class MainActivity extends ActionBarActivity{

    /**
     * Key for Contact object, used for instance saving and restoration
     */
    private static final String STORED_CONTACT  = "storedContact";
    private AssetManager assetManager;

    /**
     * Key for image object, used for instance saving and restoration
     */
    private static final String STORED_IMAGE    = "storedImage";

    /**
     * Key for boolean object, used for instance saving and restoration
     */
    private static final String STORED_PICKED   = "storedPicked";

    /**
     * Key for screen width, used for instance saving and restoration
     */
    private static final String STORED_WIDTH    = "storedWidth";

    /**
     * This activity is the general Context
     */
    private Context mContext = this;

    /**
     * Displays the contact name
     */
//    private TextView mNameTextView;

    /**
     * Displays a small description text
     */
//    private TextView mDescriptionTextView;

    /**
     * Displays the generated image
     */
    private ImageView mIconImageView;

    /**
     * Currently handled contact
     */
    private Contact mContact;

    /**
     * Will be set to false after first contact
     */
    private boolean mHasPickedContact = false;

    /**
     * Generated image
     */
    private Bitmap mGeneratedBitmap = null;

    /**
     * Keeps the user from performing any input while performing a task such as generating an image
     */
    private boolean mGuiIsLocked = false;

    /**
     * Last time the back button was pressed
     */
    private Date backButtonDate;

    /**
     * Horizontal resolution of portrait mode
     */
    private int mScreenWidthInPixels = -1;
    private GridView gridview;
    private List<String> picpaths;
    
//    private String ANIMAL ;
//    private String CARTOON;
//    private String LOVE;
//    private String MUSIC; 
//    
//    private String SPORTS; 
//    private String BUSINESS;
//    private String CARS;
//    private String DISHES;
//    
//    private String LOCAL;
//    private Bitmap localImage = null;
//    private boolean islocal = false;
    
    
    /**
     * 图片、文件夹Map类
     */ 
//    private boolean isTop = false;  
//    private PagerSlidingTabStrip tabs;
//	private ViewPager pager;
//	private CommonPagerAdapter pagerAdapter;
	private String path = null;
	private boolean firstdisp = true;
	private AdView mAdView;
	private CommonViewAdapter adapter;
	 
    /*
    ACTIVITY OVERRIDES
     */

    @SuppressLint("HandlerLeak")
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.w("MainActivity: onCreate()", "ONCREATE");
        
        super.onCreate(savedInstanceState); 
        assetManager = getAssets();
//        ANIMAL = getResources().getString(R.string.Animal);
//        CARTOON = getResources().getString(R.string.Cartoon);
//        LOVE = getResources().getString(R.string.Love);
//        MUSIC = getResources().getString(R.string.Music);
//        SPORTS = getResources().getString(R.string.Sports);
//      BUSINESS = getResources().getString(R.string.Business);
//      CARS = getResources().getString(R.string.Cars);
//      DISHES = getResources().getString(R.string.Dishes);
//      
//      LOCAL = getResources().getString(R.string.Local); 
        
        setContentView(R.layout.activity_main); 
        MobclickAgent.updateOnlineConfig(this);
        LinearLayout layout = (LinearLayout) findViewById(R.id.adlayoutmainfragment);
        mAdView = new AdView(this);
		mAdView.setAdUnitId(getResources().getString(R.string.ad_unit_id));
		mAdView.setAdSize(AdSize.BANNER);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
		layout.addView(mAdView, params);
		mAdView.loadAd(new AdRequest.Builder().build());
          
		
//        mNameTextView           = (TextView) findViewById(R.id.nameTextView);
//        mDescriptionTextView    = (TextView) findViewById(R.id.descriptionTextView);
        mIconImageView          = (ImageView) findViewById(R.id.iconImageView);
        gridview                =  (GridView) findViewById(R.id.grid_sys_pics);
        gridview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) { 
				
				changePath((String)gridview.getItemAtPosition(arg2));
				MobclickAgent.onEvent(getApplicationContext(),"Christmas",(String)gridview.getItemAtPosition(arg2)); 
			}
		});
		picpaths  = getPicList("Christmas");
		InitialAdapter();
		gridview.setAdapter(adapter); 
//        tabs                    = (PagerSlidingTabStrip) findViewById(R.id.maintabs);
//        pager                   = (ViewPager) findViewById(R.id.pager);
//        pagerAdapter            = new CommonPagerAdapter(getSupportFragmentManager());
//        pager.setAdapter(pagerAdapter);
//        final int pageMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources().getDisplayMetrics());
//        pager.setPageMargin(pageMargin);
//        tabs.setViewPager(pager);
        
        
        
        // Check whether we're recreating a previously destroyed instance
        if (savedInstanceState != null) {
            Log.d("MainActivity", "onRestoreInstanceState()");
            
            
            // Always call the superclass so it can restore the view hierarchy
            super.onRestoreInstanceState(savedInstanceState);

            // Restore state members from saved instance
            byte[] imageBytes = savedInstanceState.getByteArray(STORED_IMAGE);
            if (imageBytes != null) { 
                mGeneratedBitmap        = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
            }
            mContact                = savedInstanceState.getParcelable(STORED_CONTACT);
            mHasPickedContact       = savedInstanceState.getBoolean(STORED_PICKED);
            mScreenWidthInPixels    = savedInstanceState.getInt(STORED_WIDTH); 

            if (mHasPickedContact && mContact != null && mGeneratedBitmap != null) {
                Log.d("Restoring generated bitmap", mGeneratedBitmap.getHeight() + "");
                Log.d("Restoring contact object", mContact.getFullName());
                showContactData();
            }
        }

        if(!mHasPickedContact) {
            Log.d("MainActivity: onCreate()", "No contact picked yet.");
            pickContact();
        }
    } 
    /**
     * Populates the GUI elements
     */
    private void showContactData() {
        Drawable generatedDrawable = new BitmapDrawable(getResources(), mGeneratedBitmap);
        mIconImageView.setImageDrawable(generatedDrawable); 
//        tabs.setVisibility(View.VISIBLE);
//        pager.setVisibility(View.VISIBLE);
        gridview.setVisibility(View.VISIBLE);
        
        // Change the app colour to the average colour of the generated image.
        setColor(ColorUtilities.getAverageColor(mGeneratedBitmap));
    }
    
    

    @Override
    public void onBackPressed() {
//    	if(isTop){
//    		FragmentManager fmb = getSupportFragmentManager();
//			fmb.popBackStack();
//			
//    	}else{
    		if (backButtonDate == null) {
                backButtonDate = new Date();
            } else if (backButtonDate.getTime() - System.currentTimeMillis() <= 4000) {
                finish();
            }

            backButtonDate.setTime(System.currentTimeMillis());
            if(!mGuiIsLocked) pickContact();
//    	}
//    	isTop = false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSearchRequested() {
        if(!mGuiIsLocked) pickContact();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(!mGuiIsLocked) {
            // Handle presses on the action bar items.
            switch (item.getItemId()) {
                case R.id.action_assign:
                    confirmAssignContactImage();
                    return true;
//                case R.id.action_previous_image:
//                    mContact.modifyRetryFactor(false);
//                    new generateImageTask().execute();
//                    return true;
//                case R.id.action_next_image:
//                    mContact.modifyRetryFactor(true);
//                    new generateImageTask().execute();
//                    return true;
                case R.id.action_search:
                    pickContact();
                    return true;
                case R.id.action_save:
                    new SaveImageTask().execute();
                    return true;
                default:
                    return super.onOptionsItemSelected(item);
            }
        }

        return true;
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        if (mGeneratedBitmap != null) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            mGeneratedBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] bytes = stream.toByteArray();
            savedInstanceState.putByteArray(STORED_IMAGE, bytes);
        } else {
            savedInstanceState.putByteArray(STORED_IMAGE, null);
        }

        savedInstanceState.putParcelable(STORED_CONTACT, mContact);
        savedInstanceState.putBoolean(STORED_PICKED, mHasPickedContact);
        savedInstanceState.putInt(STORED_WIDTH, mScreenWidthInPixels);

        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
        Log.d("MainActivity", "onSaveInstanceState()");
    }

    /*
    GUI ACTION
     */

    /**
     * Locks / unlocks the GUI through boolean field and
     * hides / shows the progress bar.
     *
     * @param isBusy Will be applied to mGuiIsLocked field
     */
    private void setGuiIsBusy(boolean isBusy) {
        mGuiIsLocked = isBusy;
        ProgressBar mLoadingCircle = (ProgressBar) findViewById(R.id.progressBar);
        if(isBusy) mLoadingCircle.setVisibility(View.VISIBLE);
        else mLoadingCircle.setVisibility(View.GONE);
    }

    /**
     * Return code of contact picker
     */
    private static final int PICK_CONTACT = 1;

    /**
     * Opens the contact picker and allows the user to chose a contact.
     * onActivityResult will be called when returning to MainActivity.
     */
    public void pickContact() {
        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(intent, PICK_CONTACT);
    }

    /**
     * Method called by contact picker when a person was chosen.
     *
     * @param reqCode Request code (1 when coming from contact picker)
     * @param resultCode Result code
     * @param data Contact data
     */
    @Override
    public void onActivityResult(int reqCode, int resultCode, Intent data) {
        // Close the app if the back button was pressed on first contact picker. 
        if(!mHasPickedContact && resultCode != RESULT_OK) 
        	finish();

        // Check if the activity result is ok and check the request code.
        // The latter should be 1 indicating a picked contact.
        if(resultCode == RESULT_OK && reqCode == PICK_CONTACT) {
            backButtonDate = null;
            mHasPickedContact = true;
            mContact = new Contact(mContext, data);
            new generateImageTask().execute();
        }

        super.onActivityResult(reqCode, resultCode, data);
    } 
    /**
     * Opens a YES/NO dialog for the user to confirm that the contact's image will be overwritten.
     */
    public void confirmAssignContactImage() {
    	new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
        .setTitleText("Change the picture?")
        .setContentText("Do you confirm to change"+mContact.getFullName()+"'s picture？")
        .setCancelText("NO")
        .setConfirmText("YES")
        .showCancelButton(true)
        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sDialog) {
                sDialog.dismiss();
            }
        })
        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sDialog) {
            	new AssignContactImageTask(sDialog).execute();
            }
        })
        .show(); 
    }

    /*
    THREADS
     */

    /**
     * Constructs a contact from the given Intent data.
     */
    private class generateImageTask extends AsyncTask<Void, Void, Bitmap> {

        @Override
        protected void onPreExecute() {
            setGuiIsBusy(true);
            if(firstdisp){
            	mIconImageView.setImageDrawable(null);
//            	tabs.setVisibility(View.GONE);
//                pager.setVisibility(View.GONE);
            	gridview.setVisibility(View.GONE);
                firstdisp = false;
            } 
            // Hide all text views.
//            mNameTextView.setVisibility(View.GONE);
//            mDescriptionTextView.setVisibility(View.GONE); 
            // Reset the activity colours.
            int defaultColor = getResources().getColor(R.color.primary);
            setColor(defaultColor);
        }
        @SuppressLint("NewApi")
		@Override
        protected Bitmap doInBackground(Void... params) {
            if (mContact == null) {
                Log.e("generateImageTask", "ERROR: Contact is null.");
                return null;
            }

            // Calculate the horizontal pixels.
            if (mScreenWidthInPixels == -1) {
                // Only bother checking the resolution for Android >= 3.0.
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    Configuration config = getResources().getConfiguration();
                    DisplayMetrics dm = getResources().getDisplayMetrics();

                    // Store the height value as screen width, if in landscape mode.
                    if (config.orientation == Configuration.ORIENTATION_PORTRAIT) {
                        mScreenWidthInPixels = (int) (config.screenWidthDp * dm.density);
                    } else {
                        mScreenWidthInPixels = (int) (config.screenHeightDp * dm.density);
                    }
                } else {
                    // On old android versions, a generic, small screen resolution is assumed.
                    mScreenWidthInPixels = 480;
                }
            }
            Log.d("Screen Width in Pixels", mScreenWidthInPixels + "");
            AssetManager assetManager = getAssets();
            try {
            	Bitmap bitmap = null;
            	if(path != null){
            		InputStream is = assetManager.open(path); 
    				bitmap = BitmapFactory.decodeStream(is);
            	}
            	else{
            		bitmap = null;
            	}
				return ImageFactory.generateBitmap(mContact, mScreenWidthInPixels,bitmap);
			} catch (IOException e) { 
				e.printStackTrace();
			}
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap generatedBitmap) {
            //If a new bitmap was generated, store it in the field,
            //display it and show the contact name.

            if(generatedBitmap != null) {
                mGeneratedBitmap = generatedBitmap;
                showContactData();
            } else {
                Log.e("ConstructContactAndGenerateImageTask", "generatedBitmap is null.");
//                mNameTextView.setText(R.string.no_contact_selected);
                if(getApplicationContext() != null) {
                    Toast.makeText(
                            getApplicationContext(),
                            getResources().getString(R.string.error_generating),
                            Toast.LENGTH_LONG
                   ).show();
               }
            }
            setGuiIsBusy(false);
        }
    }
    /**
     * Assigns the bitmap to the contact.
     */
    private class AssignContactImageTask extends AsyncTask< Void, Void, Boolean > {

    	SweetAlertDialog mdialog;
        @Override
        protected void onPreExecute() {
            setGuiIsBusy(true);
        }
        public AssignContactImageTask() { 
		}
        public AssignContactImageTask(SweetAlertDialog dialog) {
        	mdialog = dialog;
		}
        

        @Override
        protected Boolean doInBackground(Void... params) {
            return mGeneratedBitmap != null && mContact.assignImage(mGeneratedBitmap);
        }

        @Override
        protected void onPostExecute(Boolean didSuccessfully) {

            if(didSuccessfully && getApplicationContext() != null) {
            	mdialog.setTitleText("Successfully changed!")
              .setContentText(String.format(getResources().getString(R.string.success_applying_image), mContact.getFullName()))
              .setConfirmText("OK")
              .showCancelButton(false)
              .setCancelClickListener(null)
              .setConfirmClickListener(null)
              .changeAlertType(SweetAlertDialog.SUCCESS_TYPE); 
            } else if(!didSuccessfully && getApplicationContext() != null) {
                mdialog.setTitleText("Failed to change!")
                .setContentText(String.format(getResources().getString(R.string.error_assign), mContact.getFullName()))
                .setConfirmText("OK")
                .showCancelButton(false)
                .setCancelClickListener(null)
                .setConfirmClickListener(null)
                .changeAlertType(SweetAlertDialog.ERROR_TYPE); 
            } else {
                Log.e("AssignContactImageTask",
                        "Could not assign the image AND applicationContext is null.");
            } 
            setGuiIsBusy(false);
        }
    }

    /**
     * Save the generated image to a file on the device
     */
    private class SaveImageTask extends AsyncTask< Void, Void, String > {

        @Override
        protected void onPreExecute() {
            setGuiIsBusy(true);
        }

        @Override
        protected String doInBackground(Void... params) {
            String fileName = "";

            if(mGeneratedBitmap != null && mContact != null) {
                MediaFileHandler fileHandler = new MediaFileHandler();
                fileName = fileHandler.saveContactImageFile(
                        mContext,
                        mGeneratedBitmap,
                        mContact.getFullName(),
                        mContact.getMD5EncryptedString().charAt(0)
                );
            }

            return fileName;
        }

        @Override
        protected void onPostExecute(String fileName) {
            setGuiIsBusy(false);

            if(fileName.length() > 1) {
                Toast.makeText(
                        mContext,
                        String.format(
                                getResources().getString(R.string.success_saving_image),
                                fileName),
                        Toast.LENGTH_LONG
               ).show();
            } else {
                Toast.makeText(
                        mContext,
                        String.format(
                                getResources().getString(R.string.error_saving),
                                fileName),
                        Toast.LENGTH_LONG
               ).show();
            }
        }
    }

    /**
     * Changes the color of the action bar and status bar
     * @param color ARGB Color to apply
     */
    @SuppressLint("NewApi")
	private void setColor(int color) {
        View mainView = findViewById(R.id.rootView);
        if (mainView == null) {
            Log.e("MainActivity:setColor()", "WARNING: Did not find root view.");
        } else {
            mainView.setBackgroundColor(color);
        }

        /*
        Set the action bar colour to the average colour of the generated image and
        the status bar colour for Android Version >= 5.0 accordingly.
        */
        try {
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(color));
        } catch (NullPointerException nullError) {
            Log.e("MainActivity:generateImageTask()", nullError.toString());
        } catch (NoSuchMethodError methodError) {
            Log.e("MainActivity:generateImageTask()", methodError.toString());
        }

        Log.d(
                "MainActivity:generateImageTask()",
                "Changing status bar & action bar colour."
        );

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // Get the window through a reference to the activity.
            Activity parent = (Activity) mContext;
            Window window = parent.getWindow();
            // Set the status bar colour of this window.
            int statusColor = ColorUtilities.getDarkenedColor(color);
            window.setStatusBarColor(statusColor);
        }
    }
//    public class CommonPagerAdapter extends FragmentStatePagerAdapter {
//    	 
//		private ArrayList<Fragment> fragmentList;  
//		private final String[] TITLES = {LOCAL,ANIMAL,CARTOON,LOVE,MUSIC,SPORTS,BUSINESS,CARS,DISHES}; 
//		private final String[] paths  = {"Animal","Cartoon","Love","Music","Sports","Business","Cars","Dishes"};
//		GroupFrameFragment gf = new GroupFrameFragment();  
//		public CommonPagerAdapter(FragmentManager fm) {
//			super(fm);
//			fragmentList = new ArrayList<Fragment>();
//			fragmentList.add(gf);
//			for(int i = 0;i<paths.length;i++){
//				SystemPicFragment sf = new SystemPicFragment(paths[i]){ 
//					@Override
//					public void changePath(String changepath) { 
//						path = changepath;
//						 mContact.modifyRetryFactor(true);
//				        new generateImageTask().execute();
//					} 
//				};
//				fragmentList.add(sf);
//			}  
//		}
//		@Override
//		public int getItemPosition(Object object) { 
//			return super.getItemPosition(object);
//		} 
//		@Override
//		public Object instantiateItem(ViewGroup container, int position) {
//			return super.instantiateItem(container, position);
//		}
//
//		@Override
//		public void destroyItem(ViewGroup container, int position, Object object){
//			super.destroyItem(container, position, object);
//		}
//
//		@Override
//		public CharSequence getPageTitle(int position) {
//			return TITLES[position];
//		}
//
//		@Override
//		public int getCount() {
//			return TITLES.length;
//		} 
//		@Override
//		public Fragment getItem(int position) {
//			return fragmentList.get(position); 
//		}
//	} 
//    @Override
//    protected void onPause() {
//    	// TODO Auto-generated method stub
//    	super.onPause();
//    	MobclickAgent.onPause(this);
//    	
//    }
//    @Override
//    protected void onResume() {
//    	// TODO Auto-generated method stub
//    	super.onResume();
//    	MobclickAgent.onResume(this);
//    } 
//  
//	@Override
//	public void sendGroup(List<String> list) {
//		Fragment fr = new LocalImageFragment();
//		FragmentManager fm = getSupportFragmentManager();
//		FragmentTransaction ft = fm.beginTransaction();
//		Bundle bundle = new Bundle();
//		bundle.putStringArrayList("data", (ArrayList<String>) list);
//		fr.setArguments(bundle);
//		ft.addToBackStack("groupframetag");
//		ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
//		ft.replace(R.id.groupframe, fr);
//		ft.commit();
//		isTop = true;
//	}
//	@Override
//	public void passDatatoActivity(String url){
//		
//		localImage = NativeImageLoader.getInstance().loadNativeImage(url, null, null);
//		
//		islocal = true;
//		new generateImageTask().execute();
//	} 
//	public void changePic(String changepath){
//		path = changepath;
//		 mContact.modifyRetryFactor(true);
//        new generateImageTask().execute();
//	}
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
    public void changePath(String changepath) { 
		path = changepath;
		 mContact.modifyRetryFactor(true);
        new generateImageTask().execute();
	}
    public void InitialAdapter(){
		adapter = new CommonViewAdapter(picpaths,this){
			@Override
			public View setView(View convertView, int position, ViewGroup parent) { 
				ImageView imageview ;
				if(convertView == null){
					convertView = getLayoutInflater().inflate(R.layout.item_grid_systempic, null);
					imageview   = (ImageView) convertView.findViewById(R.id.img_systempic); 
				}else{
					imageview = (ImageView) convertView.findViewById(R.id.img_systempic);
				}
				Picasso.with(MainActivity.this).load("file:///android_asset/"+picpaths.get(position)).into(imageview); 
				return convertView;
			} 
			@Override
			public long setItemId(int position) { 
				return position;
			}
		}; 
	}
}
