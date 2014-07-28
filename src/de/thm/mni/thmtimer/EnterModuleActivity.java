package de.thm.mni.thmtimer;

import java.lang.reflect.Field;

import de.thm.mni.thmtimer.util.DepthPageTransformer;
import de.thm.mni.thmtimer.util.FixedSpeedScroller;
import de.thm.mni.thmtimer.util.TabFactory;
import de.thm.mni.thmtimer.util.TabPagerAdapter;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.MenuItem;

public class EnterModuleActivity extends FragmentActivity {

	private ModuleDetailFragment mModuleDetail;
	private TeacherCreateCourseFragment mTeacherCreateCourse;
	private ModuleSearchFragment mModuleSearch;
	private CourseSearchFragment mCourseSearch;
	
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
			
			mModuleSearch = (ModuleSearchFragment)mFragmentManager.getFragment(savedInstanceState, "moduleSearch");
			mModuleDetail = (ModuleDetailFragment)mFragmentManager.getFragment(savedInstanceState, "moduleDetail");
			mTeacherCreateCourse = (TeacherCreateCourseFragment)mFragmentManager.getFragment(savedInstanceState, "teacherCreateCourse");
		}
		
		if(mSourceFragment.equals("teacher")) {
		
			if(mModuleSearch == null)
				mModuleSearch = new ModuleSearchFragment();
		}
		else {
			
			if(mCourseSearch == null)
				mCourseSearch = new CourseSearchFragment();
		}
		

		mPager = (ViewPager) findViewById(R.id.entermodulepager);
		mPagerAdapter = new TabPagerAdapter(getSupportFragmentManager(), new TabFactory() {

			@Override
			public Fragment firstTab() {
				
				if(mSourceFragment.equals("teacher"))
					return mModuleSearch;
				else
					return mCourseSearch;
			}

			@Override
			public Fragment secondTab() {
				
				if(mSourceFragment.equals("teacher"))
					return mTeacherCreateCourse;
				else
					return mModuleDetail;
			}

			@Override
			public int getNumberOfTabs() {
				
				if(mSourceFragment.equals("teacher"))
					return (mTeacherCreateCourse == null) ? 1 : 2;
				else
					return (mModuleDetail == null) ? 1 : 2;
			}
		});
		mPager.setAdapter(mPagerAdapter);
		mPager.setPageTransformer(true, new DepthPageTransformer());
		
		try {
			
			Field scroller = ViewPager.class.getDeclaredField("mScroller");
			scroller.setAccessible(true);
			scroller.set(mPager, new FixedSpeedScroller(this));
		}
		catch (NoSuchFieldException e) {}
		catch (IllegalArgumentException e) {}
		catch (IllegalAccessException e) {}
	}

	public void openSearch() {
		
		mPager.setCurrentItem(0);
	}
	
	
	public void onEnterCourse(Long courseID) {
		
		Intent result = new Intent();
		result.putExtra("course_id", courseID);
		
		setResult(Activity.RESULT_OK, result);
		
		finish();
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
		
		if(mModuleSearch != null)
			mModuleSearch.clearFilter();
		
		if(mCourseSearch != null)
			mCourseSearch.clearFilter();
	}
}
