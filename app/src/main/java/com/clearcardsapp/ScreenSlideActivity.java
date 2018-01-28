package com.clearcardsapp;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.clearcardsapp.fragment.CardBackFragment;
import com.clearcardsapp.fragment.ScreenSlidePageFragment;
import com.clearcardsapp.model.Card;
import com.clearcardsapp.util.AppPref;
import com.clearcardsapp.util.Constants;
import com.clearcardsapp.util.DepthPageTransformer;
import com.clearcardsapp.util.Util;

public class ScreenSlideActivity extends FragmentActivity
		implements FragmentManager.OnBackStackChangedListener, ScreenSlidePageFragment.CardFragmentListner {

	// TODO: Implement google analytics "developers.google.com/analytics/devguides/collection/android/v4/"
//	private int NUM_PAGES = Constants.CARD_CLUSTER_SIZE + 1;
	// private static final int NUM_PAGES = Card.cards.size();
	private int NUM_PAGES = 0;
	private FrameLayout container;
	private ViewPager mPager;
	private PagerAdapter mPagerAdapter;
	public Context context;
	public AppPref appPref;
	public int currentPos = 0;
	public int lastPos = 0;
	private Handler mHandler = new Handler();

	public RelativeLayout parentViewGuide;
	public LinearLayout bodyGuide;
	public ImageView iconGuide;
	public TextView textGuide;
	private int positionGuide = 0;

	private boolean mShowingBack = false;
	private static final String TAG = ">--HomeActivity-->";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_screen_slide);
		context = this;

		appPref = new AppPref(context);

		container = (FrameLayout) findViewById(R.id.container);

		parentViewGuide = (RelativeLayout) findViewById(R.id.parent_view_guide);
		iconGuide = (ImageView) findViewById(R.id.icon_guide);
		textGuide = (TextView) findViewById(R.id.text_guide);

		setGuideData("load");

		// Enabeling night view
		enableNightView();

		/*parentViewGuide.setOnTouchListener(new OnSwipeTouchListener(context) {
			public void onSwipeTop() {
//                Toast.makeText(context, "top", Toast.LENGTH_SHORT).show();
			}

			public void onSwipeRight() {
				if (positionGuide > 0)
					positionGuide--;
				setGuideData();
			}

			public void onSwipeLeft() {
				positionGuide++;
				setGuideData();
			}

			public void onSwipeBottom() {
//                Toast.makeText(context, "bottom", Toast.LENGTH_SHORT).show();
			}

			public void onTap() {
				Toast.makeText(context, "tap", Toast.LENGTH_SHORT).show();
			}

		});*/

		Card.cards = Card.getAll(context);
		
		NUM_PAGES = Card.cards.size();

		if (NUM_PAGES > 0) {
			// Since NUM_PAGE is the number of cards

			if (savedInstanceState == null) {
				// If there is no saved instance state, add a fragment
				// representing the
				// front of the card to this activity. If there is saved
				// instance state,
				// this fragment will have already been added to the activity.
				// Instantiate a ViewPager and a PagerAdapter.
				mPager = (ViewPager) findViewById(R.id.pager);
				mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
				mPager.setAdapter(mPagerAdapter);
//				final int currentPosition = appPref.getCurrentCard();
				currentPos = appPref.getCurrentCard();
				mPager.setCurrentItem(currentPos, true);
				mPager.post(new Runnable() {
					@Override
					public void run() {
						mPager.setCurrentItem(currentPos);
					}
				});
				mPager.setPageTransformer(true, new DepthPageTransformer());
				mPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
					@Override
					public void onPageSelected(int position) {
						currentPos = position;
						if (currentPos > lastPos) {
							setGuideData("swipeLeft");
						} else if (currentPos < lastPos) {
							setGuideData("swipeRight");
						}
						lastPos = position;
						// When changing pages, reset the action bar actions
						// since they are dependent
						// on which page is currently active. An alternative
						// approach is to have each
						// fragment expose actions itself (rather than the
						// activity exposing actions),
						// but for simplicity, the activity provides the actions
						// in this sample.
						// invalidateOptionsMenu();
					}
				});
			} else {
				mShowingBack = (getFragmentManager().getBackStackEntryCount() > 0);
			}

			// Monitor back stack changes to ensure the action bar shows the
			// appropriate
			// button (either "photo" or "info").
			getSupportFragmentManager().addOnBackStackChangedListener(this);

		} else {

			Log.d(TAG, "Cards size less than cluster size. Size: " + Card.cards.size());

			String alertDialogTitle = "";
			String alertDialogText = "";
			if (appPref.getCardViewed() > 0) {
				alertDialogText = Constants.NO_CARD_TEXT;
				alertDialogTitle = Constants.NO_CARD_TITLE;
			} else {
				alertDialogText = Constants.NO_INTERNET_TEXT;
				alertDialogTitle = Constants.NO_INTERNET_TITLE;
			}
			AlertDialog.Builder alt_bld = new AlertDialog.Builder(context);
			alt_bld.setMessage(alertDialogText).setCancelable(false).setTitle(alertDialogTitle).setPositiveButton("Ok",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							dialog.cancel();

							((Activity) context).finish();
						}
					});

			AlertDialog alert = alt_bld.create();
			alert.show();
		}
	}

	@Override
	protected void onResume() {

		super.onResume();

		Util.cancelNotificationAlert(context);
	}

	@Override
	protected void onPause() {
		super.onPause();
		updatePref();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		updatePref();
	}

	private void setGuideData(String action) {
		if (appPref.getBoolean("IS_GUIDED")) {
			// If already guided hide guide parent view
			parentViewGuide.setVisibility(View.GONE);
		} else {
			if ("swipeLeft".equalsIgnoreCase(action) && positionGuide == 0) {
				// If swipeLeft action performed and was showing swipeLeft instruct i.e. postion was 0 than set postion to 1
				positionGuide = 1;
			} else if ("swipeRight".equalsIgnoreCase(action) && positionGuide == 1) {
				// If swipeRight action performed and was showing swipeRight instruct i.e. postion was 1 than set postion to 2
				positionGuide = 2;
			} else if ("tap".equalsIgnoreCase(action) && positionGuide == 2) {
				// If tap action performed and was showing tap instruct i.e. postion was 2 than set postion to 3
				positionGuide = 3;
			}

			if (positionGuide == 0) {
				// Intial shows siple left instruct
				iconGuide.setBackgroundResource(R.drawable.swipe_left_white);
				textGuide.setText("Swipe left for next card");
			} else if (positionGuide == 1) {
				// If postion is 1 i.e. swipe left action performed, show next intruct of swip right
				iconGuide.setBackgroundResource(R.drawable.swipe_right_white);
				textGuide.setText("Swipe right for previous card");
			} else if (positionGuide == 2 ) {
				// If postion is 2 i.e. swipe right action performed, show next intruct of tap
				iconGuide.setBackgroundResource(R.drawable.click_white);
				textGuide.setText("Tap anywhere to get more options");
			} else if (positionGuide == 3) {
				// If postion is 3 i.e. tap action performed, hide intruct view and set appPref IS_GUIDED to true
				appPref.setBoolean("IS_GUIDED", true);
				parentViewGuide.setVisibility(View.GONE);
			}
		}
	}

	private void updatePref() {
//		if (currentPos + 1 <= Constants.CARD_CLUSTER_SIZE) {
			new AppPref(context).setCurrentCard(currentPos);
//		} else {
//			if (appPref.getCardExisted() - appPref.getCardViewed() >= Constants.CARD_CLUSTER_SIZE * 2) {
//				new AppPref(context).setCurrentCard(0);
//				new AppPref(context).setCardViewed(Constants.CARD_CLUSTER_SIZE);
//			}
//		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		// getMenuInflater().inflate(R.menu.activity_screen_slide, menu);
		//
		// menu.findItem(R.id.action_previous).setEnabled(mPager.getCurrentItem()
		// > 0);
		//
		// // Add either a "next" or "finish" button to the action bar,
		// depending on which page
		// // is currently selected.
		// MenuItem item = menu.add(Menu.NONE, R.id.action_next, Menu.NONE,
		// (mPager.getCurrentItem() == mPagerAdapter.getCount() - 1)
		// ? R.string.action_finish
		// : R.string.action_next);
		// item.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM |
		// MenuItem.SHOW_AS_ACTION_WITH_TEXT);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {

		case R.id.action_previous:
			mPager.setCurrentItem(mPager.getCurrentItem() - 1);
			return true;

		case R.id.action_next:
			mPager.setCurrentItem(mPager.getCurrentItem() + 1);
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	private void flipCard() {
		if (mShowingBack) {
			getFragmentManager().popBackStack();
			return;
		}

		// Flip to the back.

		mShowingBack = true;

		// Create and commit a new fragment transaction that adds the fragment
		// for the back of
		// the card, uses custom animations, and is part of the fragment
		// manager's back stack.

		getFragmentManager().beginTransaction()

		// Replace the default fragment animations with animator resources
		// representing
		// rotations when switching to the back of the card, as well as animator
		// resources representing rotations when flipping back to the front
		// (e.g. when
		// the system Back button is pressed).
				.setCustomAnimations(R.animator.card_flip_right_in, R.animator.card_flip_right_out,
						R.animator.card_flip_left_in, R.animator.card_flip_left_out)

		// Replace any fragments currently in the container view with a fragment
		// representing the next page (indicated by the just-incremented
		// currentPage
		// variable).
				.replace(R.id.container, new CardBackFragment())

		// Add this transaction to the back stack, allowing users to press Back
		// to get to the front of the card.
				.addToBackStack(null)

		// Commit the transaction.
				.commit();

		// Defer an invalidation of the options menu (on modern devices, the
		// action bar). This
		// can't be done immediately because the transaction may not yet be
		// committed. Commits
		// are asynchronous in that they are posted to the main thread's message
		// loop.
		mHandler.post(new Runnable() {
			@Override
			public void run() {
				invalidateOptionsMenu();
			}
		});
	}

	@Override
	public void onBackStackChanged() {
		mShowingBack = (getFragmentManager().getBackStackEntryCount() > 0);

		// When the back stack changes, invalidate the options menu (action
		// bar).
		invalidateOptionsMenu();
	}


	/**
	 * A fragment representing the back of the card.
	 */
//	public static class CardBackFragment extends Fragment {
//		public CardBackFragment() {
//		}
//
//		@Override
//		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//			return inflater.inflate(R.layout.fragment_card_back, container, false);
//		}
//	}

	private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
		public ScreenSlidePagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			return ScreenSlidePageFragment.create(position);
		}

		@Override
		public int getCount() {
			return NUM_PAGES;
		}
	}

	@Override
	public void onCardClik() {
		setGuideData("tap");
//		flipCard();
	}

	private void enableNightView() {
		if (appPref.getBoolean("night_view")) {
			container.setBackgroundColor(getResources().getColor(R.color.night));
		}
	}
}
