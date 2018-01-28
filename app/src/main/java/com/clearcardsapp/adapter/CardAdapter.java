package com.clearcardsapp.adapter;

import java.util.List;

import com.clearcardsapp.R;
import com.clearcardsapp.SettingsActivity;
import com.clearcardsapp.WebViewActivity;
import com.clearcardsapp.YouTubePlayerActivity;
import com.clearcardsapp.model.Card;
import com.clearcardsapp.util.AppPref;
import com.clearcardsapp.util.ImageUtil;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class CardAdapter extends BaseAdapter {

	private LayoutInflater inflater = null;
	Context context = null;
	private List<Card> cards;
	AppPref appPref;
	int bodyAlpha = 160;
	int headerAlpha = 160;
	int footerAlpha = 160;
	
	public CardAdapter(Context context, List<Card> routes) {
		this.context = context;
		this.appPref = new AppPref(context);
		this.cards = routes;
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return cards.size();
	}

	@Override
	public Object getItem(int position) {
		return cards.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		View rootView = convertView;
		final Card card = cards.get(position);
		final ViewHolder holder;




		// Inflate the layout containing a title and body text.
//		ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_screen_slide_page, container, false);
		Typeface typeFaceL = Typeface.createFromAsset(context.getAssets(), "fonts/Ubuntu-L.ttf");
		Typeface typeFaceI = Typeface.createFromAsset(context.getAssets(), "fonts/Ubuntu-LI.ttf");
		Typeface typeFaceR = Typeface.createFromAsset(context.getAssets(), "fonts/Ubuntu-R.ttf");
		Typeface typeFaceRobotoL = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Light.ttf");
		Typeface typeFaceRobotoLI = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-LightItalic.ttf");
		Typeface typeFaceRobotoR = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Regular.ttf");
		Typeface typeFaceRobotoT = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Thin.ttf");
		Typeface typeFaceRobotoTI = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-ThinItalic.ttf");

		if (rootView == null) {
			holder = new ViewHolder();
			rootView = inflater.inflate(R.layout.adapter_card, null);

			holder.cardView = (CardView) rootView.findViewById(R.id.card_view);
			holder.parentView = (RelativeLayout) rootView.findViewById(R.id.parent_view);
			holder.header = (LinearLayout) rootView.findViewById(R.id.header);
			holder.body = (LinearLayout) rootView.findViewById(R.id.body);
			holder.footer = (LinearLayout) rootView.findViewById(R.id.footer);
			// Set the title view to show the page number.
			holder.cardType = (TextView) rootView.findViewById(R.id.card_type);
			holder.cardText = (TextView) rootView.findViewById(R.id.card_text);
			holder.cardSource = (TextView) rootView.findViewById(R.id.card_source);
//		cardTypeIcon = (ImageView) rootView.findViewById(R.id.cardTypeIcon);
			holder.awesome = (TextView) rootView.findViewById(R.id.awesome);
			holder.share = (TextView) rootView.findViewById(R.id.share);
			holder.settings = (TextView) rootView.findViewById(R.id.settings);
			holder.premium = (TextView) rootView.findViewById(R.id.premium);
			holder.title = (TextView) rootView.findViewById(R.id.title);
			holder.cardBackground = (ImageView) rootView.findViewById(R.id.card_background);
			
			rootView.setTag(holder);
		} else {
			holder = (ViewHolder) rootView.getTag();
		}



		if (appPref.getBoolean("show_menu")) {
			holder.header.setVisibility(View.VISIBLE);
			holder.footer.setVisibility(View.VISIBLE);
		} else {
			holder.header.setVisibility(View.GONE);
			holder.footer.setVisibility(View.GONE);
		}


		holder.cardText.setTypeface(typeFaceRobotoL);
		holder.cardSource.setTypeface(typeFaceRobotoLI);
		holder.cardType.setTypeface(typeFaceRobotoLI);
		holder.awesome.setTypeface(typeFaceRobotoR);
		holder.share.setTypeface(typeFaceRobotoR);
		holder.settings.setTypeface(typeFaceRobotoR);
		holder.title.setTypeface(typeFaceRobotoR);

		if (appPref.getBoolean("background_transparency")) {
			holder.header.getBackground().setAlpha(headerAlpha);
			holder.footer.getBackground().setAlpha(footerAlpha);

			holder.body.getBackground().setAlpha(bodyAlpha);
		}

			setData(card, holder);

			holder.cardSource.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					if (card.link != null && !"".equals(card.link)) {
						if ("Videos".equals(card.type)) {
							Intent intent = new Intent(context, YouTubePlayerActivity.class);
							intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
							intent.putExtra("card_link", card.link);
							context.startActivity(intent);
						} else {
							Intent intent = new Intent(context, WebViewActivity.class);
							intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
							intent.putExtra("card_link", card.link);
							context.startActivity(intent);
						}
					}
				}
			});

			holder.share.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View view) {
					Intent sharingIntent = new Intent(Intent.ACTION_SEND);
					sharingIntent.setType("text/plain");
					String shareBody = card.text;
					if ("Quotes".equals(card.type)) {
						shareBody += "\n- " + card.source + " ";
					} else if ("Facts".equals(card.type) || "Article".equals(card.type) || "Videos".equals(card.type)) {
						shareBody += "\n" + card.link + " ";
					}
					shareBody += "\n\nFor more download the app: clearcardsapp.com";
					shareBody = shareBody.replace("<br>", "\n");
					String shareHeader = "Clear Cards - " + card.type;
					sharingIntent.putExtra(Intent.EXTRA_SUBJECT, shareHeader);
					sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
					context.startActivity(Intent.createChooser(sharingIntent, "Clear Cards"));
				}
			});

			holder.awesome.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					card.appreciate = (card.appreciate != null && card.appreciate) ? false : true;
					Card.update(context, card);
					if (card.appreciate != null && card.appreciate) {
						holder.awesome.setTextColor(ContextCompat.getColor(context, R.color.card_green));
						holder.awesome.setBackgroundResource(R.drawable.border_footer_option_green);
					} else {
						holder.awesome.setTextColor(ContextCompat.getColor(context, R.color.source_text));
						holder.awesome.setBackgroundResource(R.drawable.border_footer_option);
					}

				}
			});

			if (appPref.getBoolean("is_premium")) {
				holder.settings.setVisibility(View.VISIBLE);
				holder.premium.setVisibility(View.GONE);
			} else {
				holder.settings.setVisibility(View.GONE);
				holder.premium.setVisibility(View.VISIBLE);
			}

			holder.settings.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					Intent intent = new Intent(context, SettingsActivity.class);
					intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
					context.startActivity(intent);
				}
			});

			holder.cardText.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View view) {
//					toggleMenu();
//					cardFragmentListner.onCardClik();
				}
			});

			holder.cardBackground.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
//					toggleMenu();
//					cardFragmentListner.onCardClik();
				}
			});

			holder.body.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
//					toggleMenu();
//					cardFragmentListner.onCardClik();
				}
			});

		return rootView;
	}

	private void setData(Card card, ViewHolder holder) {

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
			ImageUtil.loadImage(context, card.image, holder.cardBackground, R.drawable.ic_blank_120);
		}

		holder.cardText.setText(Html.fromHtml(card.text));

		if ("Quotes".equals(card.type)) {
			holder.cardSource.setText("- " + card.source + " ");
		} else if ("Facts".equals(card.type) || "Article".equals(card.type) || "Videos".equals(card.type)) {
			holder.cardSource.setText(card.source + " ");
		} else {
			holder.cardSource.setVisibility(View.GONE);
		}

		if (card.link != null && !"".equals(card.link)) {
			holder.cardSource.setTextColor(ContextCompat.getColor(context, R.color.card_link));
		}

		if ("Funny".equals(card.type)) {
//			title.setText("Clear Cards - Funny");
			holder.cardType.setText("Funny ");
//			cardTypeIcon.setBackgroundResource(funnyIconBlack);
		} else if ("Facts".equals(card.type)) {
//			title.setText("Clear Cards - Fact");
			holder.cardType.setText("Fact ");
//			cardTypeIcon.setBackgroundResource(factsIconBlack);
		} else if ("Quotes".equals(card.type)) {
//			title.setText("Clear Cards - Quote");
			holder.cardType.setText("Quote ");
//			cardTypeIcon.setBackgroundResource(quotesIconBlack);
		} else if ("Videos".equals(card.type)) {
//			title.setText("Clear Cards - Video");
			holder.cardType.setText("Video ");
//			cardTypeIcon.setBackgroundResource(videosIconBlack);
		} else if ("Article".equals(card.type)) {
//			title.setText("Clear Cards - Article");
			holder.cardType.setText("Article ");
//			cardTypeIcon.setBackgroundResource(newsIconBlack);
		}
		/*}*/

		if (card.appreciate != null && card.appreciate) {
			holder.awesome.setTextColor(ContextCompat.getColor(context, R.color.card_green));
			holder.awesome.setBackgroundResource(R.drawable.border_footer_option_green);
		} else {
			holder.awesome.setTextColor(ContextCompat.getColor(context, R.color.source_text));
			holder.awesome.setBackgroundResource(R.drawable.border_footer_option);
		}
	}

	class ViewHolder {
		CardView cardView;
		RelativeLayout parentView;
		LinearLayout header, body, footer;
		TextView cardText, cardSource, cardType, awesome, share, settings, premium, title;
		ImageView cardTypeIcon, cardBackground;
	}
}
