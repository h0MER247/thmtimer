package de.thm.mni.thmtimer;

import java.lang.reflect.Field;

import de.thm.mni.thmtimer.util.DepthPageTransformer;
import de.thm.mni.thmtimer.util.FixedSpeedScroller;
import de.thm.mni.thmtimer.util.TabFactory;
import de.thm.mni.thmtimer.util.TabPagerAdapter;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.MenuItem;

public class EnterModuleActivity extends FragmentActivity {

	private Fragment mModuleDetail, mTeacherCreateCourse;
	private ModuleSearchFragment mModuleSearch;
	private FragmentManager mFragmentManager;
	private ViewPager mPager;
	private TabPagerAdapter mPagerAdapter;
	private String mSourceFragment;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.entermoduleactivity);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		
		mSourceFragment = getIntent().getExtras().getString("fragment");
		
		if(mFragmentManager == null)
			mFragmentManager = getSupportFragmentManager();
		
		if(savedInstanceState != null) {
			
			mModuleSearch = (ModuleSearchFragment) mFragmentManager.getFragment(savedInstanceState, "moduleSearch");
			mModuleDetail = mFragmentManager.getFragment(savedInstanceState, "moduleDetail");
			mTeacherCreateCourse = mFragmentManager.getFragment(savedInstanceState, "teacherCreateCourse");
		}
		
		if(mModuleSearch == null)
			mModuleSearch = new ModuleSearchFragment();
		

		mPager = (ViewPager) findViewById(R.id.entermodulepager);
		mPagerAdapter = new TabPagerAdapter(getSupportFragmentManager(), new TabFactory() {

			@Override
			public Fragment firstTab() {
				
				return mModuleSearch;
			}

			@Override
			public Fragment secondTab() {
				
				if(mSourceFragment.equals("teacher"))
					return mTeacherCreateCourse;
				else if(mSourceFragment.equals("student"))
					return mModuleDetail;
				
				return null;
			}

			@Override
			public int getNumberOfTabs() {
				
				if(mSourceFragment.equals("teacher"))
					return mTeacherCreateCourse == null ? 1 : 2;
				else
					return mModuleDetail == null ? 1 : 2;
			}
		});
		mPager.setAdapter(mPagerAdapter);
		mPager.setPageTransformer(true, new DepthPageTransformer());
		try {
			Field scroller = ViewPager.class.getDeclaredField("mScroller");
			scroller.setAccessible(true);
			scroller.set(mPager, new FixedSpeedScroller(this));
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void openSearch() {
		mPager.setCurrentItem(0);
	}
	
	public void onEnterCourse(long id) {
		
		// TODO
	}
	public void onCreateCourse() {
		
		// TODO
	}
	

	public void closeSearch(long id) {

		if(mSourceFragment.equals("teacher")){
			Log.d("LOG", "OPEN");
			mTeacherCreateCourse = new TeacherCreateCourseFragment();
			Bundle b = new Bundle();
			b.putLong("id", id); // id ist jetzt ne kursid!!!
			mTeacherCreateCourse.setArguments(b);
			mPagerAdapter.notifyDataSetChanged();

			mPager.setCurrentItem(1);
	
		} 
		if(mSourceFragment.equals("student")){
			mModuleDetail = new ModuleDetailFragment();
			Bundle b = new Bundle();
			b.putLong("id", id); // id ist jetzt ne kursid!!!
			mModuleDetail.setArguments(b);
			mPagerAdapter.notifyDataSetChanged();

			mPager.setCurrentItem(1);
		}
		
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mFragmentManager.putFragment(outState, "moduleSearch", mModuleSearch);
		if (mModuleDetail != null) {
			mFragmentManager.putFragment(outState, "moduleDetail", mModuleDetail);
		}
		if (mTeacherCreateCourse != null) {
			mFragmentManager.putFragment(outState, "teacherCreateCourse", mTeacherCreateCourse);
		} 
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			onBack();
			return true;
		default:
			return false;
		}
	}

	@Override
	public void onBackPressed() {
		onBack();
	}

	private void onBack() {
		int cur = mPager.getCurrentItem();
		if (cur == 0) {
			finish();
		} else {
			openSearch();
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mModuleSearch.clearFilter();
	}
}
