package com.clearcardsapp.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.clearcardsapp.R;
import com.clearcardsapp.SettingsActivity;
import com.clearcardsapp.WebViewActivity;
import com.clearcardsapp.YouTubePlayerActivity;
import com.clearcardsapp.model.Card;
import com.clearcardsapp.util.AppPref;
import com.clearcardsapp.util.ImageUtil;

public class ScreenSlidePageFragment extends Fragment {

	public static final String ARG_PAGE = "page";
	private static final String TAG = ">--ScreenSlidePageFragment-->";
	public AppPref appPref;
	Card card;
	private int mPageNumber;
	CardView cardView;
	RelativeLayout parentView;
	LinearLayout header, body, footer;
	TextView cardText, cardSource, cardType, awesome, share, settings, premium, title;
	ImageView cardTypeIcon, cardBackground;
	CardFragmentListner cardFragmentListner;
	int videosIconBlack = R.drawable.ic_play_circle_outline_black_48dp;
	int newsIconBlack = R.drawable.ic_insert_link_black_48dp;
	int quotesIconBlack = R.drawable.ic_format_quote_black_48dp;
	int factsIconBlack = R.drawable.ic_content_paste_black_48dp;
	int funnyIconBlack = R.drawable.ic_insert_emoticon_black_48dp;
	int bodyAlpha = 160;
	int headerAlpha = 160;
	int footerAlpha = 160;
	boolean menuDisplayed = false;
	boolean backgroundClicked = false;
	boolean bodyClicked = false;
	CountDownTimer menuCountDownTimer;

	public interface CardFragmentListner {
		public void onCardClik();
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

//		if (context instanceof Activity){
			try {
				cardFragmentListner = (CardFragmentListner) activity;
			} catch (ClassCastException e) {
			}
//		}
	}

	public static ScreenSlidePageFragment create(int pageNumber) {
		ScreenSlidePageFragment fragment = new ScreenSlidePageFragment();
		Bundle args = new Bundle();
		args.putInt(ARG_PAGE, pageNumber);
		fragment.setArguments(args);
		return fragment;
	}

	public ScreenSlidePageFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mPageNumber = getArguments().getInt(ARG_PAGE);
		if (Card.cards.size() >= mPageNumber){
			card = Card.cards.get(mPageNumber);
		} else {
			getActivity().finish();
			startActivity(getActivity().getIntent());
		}
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		appPref = new AppPref(getActivity());

		// Inflate the layout containing a title and body text.
		ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_screen_slide_page, container, false);
		Typeface typeFaceL = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Ubuntu-L.ttf");
		Typeface typeFaceI = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Ubuntu-LI.ttf");
		Typeface typeFaceR = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Ubuntu-R.ttf");
		Typeface typeFaceRobotoL = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Roboto-Light.ttf");
		Typeface typeFaceRobotoLI = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Roboto-LightItalic.ttf");
		Typeface typeFaceRobotoR = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Roboto-Regular.ttf");
		Typeface typeFaceRobotoT = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Roboto-Thin.ttf");
		Typeface typeFaceRobotoTI = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Roboto-ThinItalic.ttf");

		cardView = (CardView) rootView.findViewById(R.id.card_view);
		parentView = (RelativeLayout) rootView.findViewById(R.id.parent_view);
		header = (LinearLayout) rootView.findViewById(R.id.header);
		body = (LinearLayout) rootView.findViewById(R.id.body);
		footer = (LinearLayout) rootView.findViewById(R.id.footer);
		// Set the title view to show the page number.
		cardType = (TextView) rootView.findViewById(R.id.card_type);
		cardText = (TextView) rootView.findViewById(R.id.card_text);
		cardSource = (TextView) rootView.findViewById(R.id.card_source);
//		cardTypeIcon = (ImageView) rootView.findViewById(R.id.cardTypeIcon);
		awesome = (TextView) rootView.findViewById(R.id.awesome);
		share = (TextView) rootView.findViewById(R.id.share);
		settings = (TextView) rootView.findViewById(R.id.settings);
		premium = (TextView) rootView.findViewById(R.id.premium);
		title = (TextView) rootView.findViewById(R.id.title);
		cardBackground = (ImageView) rootView.findViewById(R.id.card_background);

		if (appPref.getBoolean("show_menu")) {
			header.setVisibility(View.VISIBLE);
			footer.setVisibility(View.VISIBLE);
		} else {
			header.setVisibility(View.GONE);
			footer.setVisibility(View.GONE);
		}


		cardText.setTypeface(typeFaceRobotoL);
		cardSource.setTypeface(typeFaceRobotoLI);
		cardType.setTypeface(typeFaceRobotoLI);
		awesome.setTypeface(typeFaceRobotoR);
		share.setTypeface(typeFaceRobotoR);
		settings.setTypeface(typeFaceRobotoR);
		title.setTypeface(typeFaceRobotoR);

		if (appPref.getBoolean("background_transparency")) {
			header.getBackground().setAlpha(headerAlpha);
			footer.getBackground().setAlpha(footerAlpha);

			body.getBackground().setAlpha(bodyAlpha);
		}

		// Enabeling night view
		enableNightView();


//		if (mPageNumber + 1 > Constants.CARD_CLUSTER_SIZE) {
//			cardText.setText(Constants.CARD_END_TEXT);
//		} else {
			if (Card.cards != null && Card.cards.size() > mPageNumber && Card.cards.get(mPageNumber) != null) {
				
				setData();

				cardSource.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						if (card.link != null && !"".equals(card.link)) {
							if ("Videos".equals(card.type)) {
								Intent intent = new Intent(getActivity(), YouTubePlayerActivity.class);
								intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
								intent.putExtra("card_link", card.link);
								getActivity().startActivity(intent);
							} else {
								Intent intent = new Intent(getActivity(), WebViewActivity.class);
								intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
								intent.putExtra("card_link", card.link);
								getActivity().startActivity(intent);
							}
						}
					}
				});
				
				share.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View view) {
						Intent sharingIntent = new Intent(Intent.ACTION_SEND);
						sharingIntent.setType("text/plain");
						String shareBody = Card.cards.get(mPageNumber).text;
						if ("Quotes".equals(card.type)) {
							shareBody += "\n- " + card.source + " ";
						} else if ("Facts".equals(card.type) || "Article".equals(card.type) || "Videos".equals(card.type)) {
							shareBody += "\n" + card.link + " ";
						}
						shareBody += "\n\nFor more download the app: clearcardsapp.com";
						shareBody = shareBody.replace("<br>", "\n");
						String shareHeader = "Clear Cards - " + Card.cards.get(mPageNumber).type;
						sharingIntent.putExtra(Intent.EXTRA_SUBJECT, shareHeader);
						sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
						startActivity(Intent.createChooser(sharingIntent, "Clear Cards"));
					}
				});

				awesome.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						card.appreciate = (card.appreciate != null && card.appreciate) ? false : true;
						Card.update(getActivity(), card);
						if (card.appreciate != null && card.appreciate) {
							awesome.setTextColor(ContextCompat.getColor(getActivity(), R.color.card_green));
							awesome.setBackgroundResource(R.drawable.border_footer_option_green);
						} else {
							awesome.setTextColor(ContextCompat.getColor(getActivity(), R.color.source_text));
							awesome.setBackgroundResource(R.drawable.border_footer_option);
						}

					}
				});

				if (appPref.getBoolean("is_premium")) {
					settings.setVisibility(View.VISIBLE);
					premium.setVisibility(View.GONE);
				} else {
					settings.setVisibility(View.GONE);
					premium.setVisibility(View.GONE);
				}

				settings.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {

						Intent intent = new Intent(getActivity(), SettingsActivity.class);
						intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
						getActivity().startActivity(intent);
					}
				});

				cardText.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View view) {
						toggleMenu();
						cardFragmentListner.onCardClik();
					}
				});

				cardBackground.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						toggleMenu();
						cardFragmentListner.onCardClik();
					}
				});

				body.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						toggleMenu();
						cardFragmentListner.onCardClik();
					}
				});
			}
//		}

		return rootView;
	}

	private void toggleMenu() {
		if (!appPref.getBoolean("show_menu")) {
			if (menuDisplayed) {
				menuDisplayed = false;
				hideMenu();
			} else {
				menuDisplayed = true;
				showMenu();
			}
		}
	}

	private void hideMenu() {
		menuCountDownTimer.cancel();
		header.setVisibility(View.GONE);
		footer.setVisibility(View.GONE);
	}

	private void showMenu() {
		header.setVisibility(View.VISIBLE);
		footer.setVisibility(View.VISIBLE);
		menuCountDownTimer = new CountDownTimer(5000, 5000) {

			public void onTick(long millisUntilFinished) {
			}

			public void onFinish() {
				hideMenu();
				menuCountDownTimer.cancel();
			}
		};
		menuCountDownTimer.start();
	}

	private void setData() {

//		String imageUrl = "http://www.planwallpaper.com/static/images/2022725-wallpaper_625864_Iz6NK8G.jpg";
//		String imageUrl = "http://static.independent.co.uk/s3fs-public/thumbnails/image/2014/08/19/09/rowling3.jpg";
//		card.image = "http://ichef.bbci.co.uk/news/660/media/images/65445000/jpg/_65445024_p06-0013-adetail1.jpg";
//		String videoUrl = "https://www.youtube.com/watch?v=Mam4TFEiWWI";
		/*if ("Videos".equals(card.type)) {
			String pattern = "(?<=watch\\?v=|/videos/|embed\\/)[^#\\&\\?]*";

			Pattern compiledPattern = Pattern.compile(pattern);
			Matcher matcher = compiledPattern.matcher(card.link);

			if (matcher.find()) {
				String videoId = matcher.group();

				YouTubeVideoFragment youTubeVideoFragment = YouTubeVideoFragment.newInstance(videoId);
				getChildFragmentManager().beginTransaction().replace(R.id.youtube_fragment, youTubeVideoFragment).commit();
			}
			share.setVisibility(View.GONE);
			body.setVisibility(View.GONE);
		} else {*/

			if (card.image != null && !"".equals(card.image) && appPref.getBoolean("background_image")) {
				ImageUtil.loadImage(getActivity(), card.image, cardBackground, R.drawable.ic_blank_120);
			}

			cardText.setText(Html.fromHtml(card.text));

			if ("Quotes".equals(card.type)) {
				cardSource.setText("- " + card.source + " ");
			} else if ("Facts".equals(card.type) || "Article".equals(card.type) || "Videos".equals(card.type)) {
				cardSource.setText(card.source + " ");
			} else {
				cardSource.setVisibility(View.GONE);
			}

			if (card.link != null && !"".equals(card.link)) {
				cardSource.setTextColor(ContextCompat.getColor(getActivity(), R.color.card_link));
			}

			if ("Funny".equals(card.type)) {
//			title.setText("Clear Cards - Funny");
				cardType.setText("Funny ");
//			cardTypeIcon.setBackgroundResource(funnyIconBlack);
			} else if ("Facts".equals(card.type)) {
//			title.setText("Clear Cards - Fact");
				cardType.setText("Fact ");
//			cardTypeIcon.setBackgroundResource(factsIconBlack);
			} else if ("Quotes".equals(card.type)) {
//			title.setText("Clear Cards - Quote");
				cardType.setText("Quote ");
//			cardTypeIcon.setBackgroundResource(quotesIconBlack);
			} else if ("Videos".equals(card.type)) {
//			title.setText("Clear Cards - Video");
				cardType.setText("Video ");
//			cardTypeIcon.setBackgroundResource(videosIconBlack);
			} else if ("Article".equals(card.type)) {
//			title.setText("Clear Cards - Article");
				cardType.setText("Article ");
//			cardTypeIcon.setBackgroundResource(newsIconBlack);
			}
		/*}*/

		if (card.appreciate != null && card.appreciate) {
			awesome.setTextColor(ContextCompat.getColor(getActivity(), R.color.card_green));
			awesome.setBackgroundResource(R.drawable.border_footer_option_green);
		} else {
			awesome.setTextColor(ContextCompat.getColor(getActivity(), R.color.source_text));
			awesome.setBackgroundResource(R.drawable.border_footer_option);
		}
	}

	/**
	 * Returns the page number represented by this fragment object.
	 */
	public int getPageNumber() {
		return mPageNumber;
	}

	private void enableNightView() {
		if (appPref.getBoolean("night_view")) {
			parentView.setBackgroundColor(getResources().getColor(R.color.night));
		}
	}
}
