package com.white.tooth;

import java.io.Flushable;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.flurry.android.FlurryAgent;
import com.white.tooth.dlg.DlgToothShade;
import com.white.tooth.dlg.DlgToothShade.OnToothShadeDialogClickListener;
import com.white.tooth.util.IabHelper;
import com.white.tooth.util.IabHelper.OnIabPurchaseFinishedListener;
import com.white.tooth.util.IabHelper.QueryInventoryFinishedListener;
import com.white.tooth.util.IabResult;
import com.white.tooth.util.Inventory;
import com.white.tooth.util.Purchase;

public class DsahBoardScreen extends BaseActivity implements OnClickListener,OnToothShadeDialogClickListener {
	private static final String TAG = "paymentExample";
	private LinearLayout ll_tutorial, ll_history, ll_refill, ll_session, ll_live_support;
	private TextView tv_tutorial, tv_history, tv_session, tv_refill, tv_live_support;
	private Intent i;

	boolean mIsPremium = false;

	boolean mSubscribedToInfiniteGas = false;

	static final String SKU_STARTER_KIT = "starterkit";
	static final String SKU_TEN_MINUTES_KIT = "tenminuteskit";
	static final String SKU_TWENTY_MINUTES_KIT = "twentyminuteskit";
	static final String SKU_THIRTY_MINUTES_KIT = "thirtyminuteskit";
	static final String SKU_FOURTY_MINUTES_KIT = "fourtyminuteskit";

	private static final String pay = "pay";

	public IabHelper mHelper;
	public OnIabPurchaseFinishedListener mPurchaseFinishedListener;

	static final int RC_REQUEST = 10001;
	static final int REQUEST_IMAGE_CAPTURE = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dashboard);

		new AlertDialog.Builder(DsahBoardScreen.this).setCancelable(false).setMessage("This APP will not whiten your teeth without the use of the Whitening Gel.  You must apply the Whitening Gel to your teeth before using the APP Acceleration Light.").setNeutralButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				Constant.isDialogOpen = true;
				dialog.dismiss();
			}
		}).show();

		tv_tutorial = (TextView) findViewById(R.id.tv_tutorial);
		tv_tutorial.setTypeface(agecyTypeface());

		tv_history = (TextView) findViewById(R.id.tv_history);
		tv_history.setTypeface(agecyTypeface());

		tv_session = (TextView) findViewById(R.id.tv_session);
		tv_session.setTypeface(agecyTypeface());

		tv_refill = (TextView) findViewById(R.id.tv_refill);
		tv_refill.setTypeface(agecyTypeface());

		tv_live_support = (TextView) findViewById(R.id.tv_live_support);
		tv_live_support.setTypeface(agecyTypeface());

		ll_tutorial = (LinearLayout) findViewById(R.id.ll_tutorial);
		ll_tutorial.setOnClickListener(this);

		ll_history = (LinearLayout) findViewById(R.id.ll_history);
		ll_history.setOnClickListener(this);

		ll_refill = (LinearLayout) findViewById(R.id.ll_refill);
		ll_refill.setOnClickListener(this);

		ll_session = (LinearLayout) findViewById(R.id.ll_session);
		ll_session.setOnClickListener(this);

		ll_live_support = (LinearLayout) findViewById(R.id.ll_live_support);
		ll_live_support.setOnClickListener(this);

		mPurchaseFinishedListener = new OnIabPurchaseFinishedListener() {

			@Override
			public void onIabPurchaseFinished(IabResult result, Purchase purchase) {
				if (mHelper == null)
					return;

				if (result.isFailure()) {
					return;
				}
				if (!verifyDeveloperPayload(purchase)) {
					return;
				}

				if (purchase.getSku().equals(pay)) {
					// isPayed = true;
				}
			}
		};

		try {
			String base64EncodedPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAmGclpuB6CKUVdVZSGsJfhLz8z18q/86GFIzakPjcRw7OZBwLvAiwgpoBCfsIglY9zEmeOC/vaY9MgIaMdVsbkuWL5gd3T7x6B1mZErMwN60VhdMsNaRQdctswT41ynyZLQL1U4pn1IQ+KLWFwQBEkH8OfzWpTk8A6yi+Ahl1ue+cV/hqCeZsXFPJTBQfuk8l50aPgbBTfnyS4vvBsYyADzUhsHZVluWMzi6BMiQ9CA0ybehRYIABuwwIkxTYzl2vMN1uCXNAQqZkOQhAaJ66GZhwLB/g6HUUcW0Dt84fbyusbaO1gH50xr9gcVMCPFApJmJb1zrdjr3MxeENoUqsqQIDAQAB";

			mHelper = new IabHelper(this, base64EncodedPublicKey);
			mHelper.enableDebugLogging(true);
			mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
				public void onIabSetupFinished(IabResult result) {
					if (!result.isSuccess()) {
						return;
					}
					if (mHelper == null)
						return;
					mHelper.queryInventoryAsync(new QueryInventoryFinishedListener() {
						@Override
						public void onQueryInventoryFinished(IabResult result, Inventory inventory) {
							if (mHelper == null)
								return;
							if (result.isFailure()) {
								return;
							}

							Purchase wheelPurchase = inventory.getPurchase(pay);
							// isPayed = (wheelPurchase != null &&
							// verifyDeveloperPayload(wheelPurchase));
						}
					});
				}
			});
		} catch (Exception e) {
		}
	}

	public boolean verifyDeveloperPayload(Purchase p) {
		String payload = p.getDeveloperPayload();
		return true;
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.ll_history:
			FlurryAgent.logEvent("HistoryData");
			i = new Intent(getApplicationContext(), History.class);
			startActivity(i);
			break;

		case R.id.ll_tutorial:
			i = new Intent(getApplicationContext(), Tutorial.class);
			startActivity(i);
			FlurryAgent.logEvent("TutorialScreenData");
			
			break;
		case R.id.ll_session:
			
			new DlgToothShade(this, this).show();
			
			FlurryAgent.logEvent("StartSessionData");

			break;
		case R.id.ll_refill:

			FlurryAgent.logEvent("BuyStarterKitData");
			
			final Dialog dialog = new Dialog(DsahBoardScreen.this);
			dialog.setTitle("Choose your product option");
			dialog.setContentView(R.layout.product_purchase_dialog);

			LinearLayout ll_starter = (LinearLayout) dialog.findViewById(R.id.ll_starter_kit);
			ll_starter.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					String payload = "";
					mHelper.launchPurchaseFlow(DsahBoardScreen.this, SKU_STARTER_KIT, RC_REQUEST, mPurchaseFinishedListener, payload);
					dialog.cancel();
				}
			});
			LinearLayout ll_ten_minutes = (LinearLayout) dialog.findViewById(R.id.ll_ten_minutes);
			ll_ten_minutes.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					String payload = "";
					mHelper.launchPurchaseFlow(DsahBoardScreen.this, SKU_TEN_MINUTES_KIT, RC_REQUEST, mPurchaseFinishedListener, payload);
					dialog.cancel();
				}
			});
			LinearLayout ll_twenty_minutes = (LinearLayout) dialog.findViewById(R.id.ll_twenty_minutes);
			ll_twenty_minutes.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					String payload = "";
					mHelper.launchPurchaseFlow(DsahBoardScreen.this, SKU_TWENTY_MINUTES_KIT, RC_REQUEST, mPurchaseFinishedListener, payload);
					dialog.cancel();
				}
			});
			LinearLayout ll_thirty_minutes = (LinearLayout) dialog.findViewById(R.id.ll_thirty_minutes);
			ll_thirty_minutes.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					String payload = "";
					mHelper.launchPurchaseFlow(DsahBoardScreen.this, SKU_THIRTY_MINUTES_KIT, RC_REQUEST, mPurchaseFinishedListener, payload);
					dialog.cancel();
				}
			});
			LinearLayout ll_fourty_minutes = (LinearLayout) dialog.findViewById(R.id.ll_fourty_minutes);
			ll_fourty_minutes.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					String payload = "";
					mHelper.launchPurchaseFlow(DsahBoardScreen.this, SKU_FOURTY_MINUTES_KIT, RC_REQUEST, mPurchaseFinishedListener, payload);
					dialog.cancel();
				}
			});
			dialog.setCancelable(true);
			dialog.show();
			break;

		case R.id.ll_live_support:
			FlurryAgent.logEvent("LiveSupportData");
			i = new Intent(getApplicationContext(), SmsActivity.class);
			startActivity(i);
			break;
		}
	}

	protected void onStop() {
		super.onStop();
		handler.removeCallbacks(runnable);
	}

	@Override
	public void onBackPressed() {
		
		if (Constant.isRunning) {
			handler.removeCallbacks(runnable);
		}
		Constant.isDialogOpen = false;
		finish();
		super.onBackPressed();

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		if(requestCode == 201){
			Intent i1 = new Intent(getApplicationContext(), TimerActivity.class);
			startActivity(i1);
		}
		
		if (mHelper == null)
			return;

		if (!mHelper.handleActivityResult(requestCode, resultCode, data)) {
			super.onActivityResult(requestCode, resultCode, data);
		}
	}

	


	@Override
	public void onDestroy() {
		Log.d(TAG, "Destroying helper.");
		if (mHelper != null) {
			mHelper.dispose();
			mHelper = null;
		}
		super.onDestroy();
	}

	@Override
	public void onYesButtonClick() {
		app.setShadeActive(true);
		Intent i1 = new Intent(getApplicationContext(), ToothShadeActivity.class);
		i1.putExtra("val", 1);
		startActivityForResult(i1, 201);
		
	}

	@Override
	public void onNoButtonClick() {
		Intent i1 = new Intent(getApplicationContext(), TimerActivity.class);
		startActivity(i1);
		
	}

}
