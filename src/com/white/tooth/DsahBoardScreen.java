package com.white.tooth;

import java.math.BigDecimal;

import org.json.JSONException;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.DashPathEffect;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;
import com.white.tooth.util.IabHelper;
import com.white.tooth.util.IabResult;
import com.white.tooth.util.Inventory;
import com.white.tooth.util.Purchase;
import com.white.tooth.util.IabHelper.OnIabPurchaseFinishedListener;
import com.white.tooth.util.IabHelper.QueryInventoryFinishedListener;

public class DsahBoardScreen extends BaseActivity implements OnClickListener {
	private static final String TAG = "paymentExample";
	private LinearLayout ll_tutorial, ll_history, ll_refill, ll_session,
			ll_live_support;
	private TextView tv_tutorial, tv_history, tv_session, tv_refill,
			tv_live_support;
	private Intent i;
	private static final String CONFIG_ENVIRONMENT = PayPalConfiguration.ENVIRONMENT_NO_NETWORK;

	// note that these credentials will differ between live & sandbox
	// environments.
	// private static final String CONFIG_CLIENT_ID =
	// "credential from developer.paypal.com";
	private static final String CONFIG_CLIENT_ID = "pearlywhitescenter@gmail.com";
	private static final int REQUEST_CODE_PAYMENT = 1;

	private int val = 0;

	private static PayPalConfiguration config = new PayPalConfiguration()
			.environment(CONFIG_ENVIRONMENT)
			.clientId(CONFIG_CLIENT_ID)
			// The following are only used in PayPalFuturePaymentActivity.
			.merchantName("Pearly Whites Center")
			.merchantPrivacyPolicyUri(
					Uri.parse("https://www.example.com/privacy"))
			.merchantUserAgreementUri(
					Uri.parse("https://www.example.com/legal"));
	
	
	/**
	 * For In app variable
	 */
	
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
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dashboard);

		new AlertDialog.Builder(DsahBoardScreen.this)
		.setCancelable(false)
				.setMessage(
						"This APP will not whiten your teeth without the use of the Whitening Gel.  You must apply the Whitening Gel to your teeth before using the APP Acceleration Light.")
				.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
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
			i = new Intent(getApplicationContext(), History.class);
			startActivity(i);
			break;

		case R.id.ll_tutorial:
			i = new Intent(getApplicationContext(), Tutorial.class);
			startActivity(i);

			break;
		case R.id.ll_session:

			i = new Intent(getApplicationContext(), TimerActivity.class);
			startActivity(i);

			break;
		case R.id.ll_refill:
			
			final Dialog dialog = new Dialog(DsahBoardScreen.this);
			dialog.setTitle("Choose your product option");
			dialog.setContentView(R.layout.product_purchase_dialog);
			
			LinearLayout ll_starter = (LinearLayout)dialog.findViewById(R.id.ll_starter_kit);
			ll_starter.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					String payload = "";
					mHelper.launchPurchaseFlow(DsahBoardScreen.this, SKU_STARTER_KIT, RC_REQUEST, mPurchaseFinishedListener, payload);
					dialog.cancel();
				}
			});
			LinearLayout ll_ten_minutes = (LinearLayout)dialog.findViewById(R.id.ll_ten_minutes);
			ll_ten_minutes.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					String payload = "";
					mHelper.launchPurchaseFlow(DsahBoardScreen.this, SKU_TEN_MINUTES_KIT, RC_REQUEST, mPurchaseFinishedListener, payload);
					dialog.cancel();
				}
			});
			LinearLayout ll_twenty_minutes = (LinearLayout)dialog.findViewById(R.id.ll_twenty_minutes);
			ll_twenty_minutes.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					String payload = "";
					mHelper.launchPurchaseFlow(DsahBoardScreen.this, SKU_TWENTY_MINUTES_KIT, RC_REQUEST, mPurchaseFinishedListener, payload);
					dialog.cancel();
				}
			});
			LinearLayout ll_thirty_minutes = (LinearLayout)dialog.findViewById(R.id.ll_thirty_minutes);
			ll_thirty_minutes.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					String payload = "";
					mHelper.launchPurchaseFlow(DsahBoardScreen.this, SKU_THIRTY_MINUTES_KIT, RC_REQUEST, mPurchaseFinishedListener, payload);
					dialog.cancel();
				}
			});
			LinearLayout ll_fourty_minutes = (LinearLayout)dialog.findViewById(R.id.ll_fourty_minutes);
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
			/*Intent httpIntent = new Intent(Intent.ACTION_VIEW);
			httpIntent.setData(Uri.parse("https://squareup.com/market/whitetooth/white-tooth-starter-kit"));

			startActivity(httpIntent);*/

			/*AlertDialog.Builder builder = new AlertDialog.Builder(this);

			builder.setTitle("REFILL");

			builder.setItems(new CharSequence[] { "Starter Kit: $12",
					"Teeth whitening pen:$5.95" },
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {

							if (which == 0) {
								val = 0;
								PayPalPayment thingToBuy = getThingToBuy(PayPalPayment.PAYMENT_INTENT_SALE);

								Intent intent = new Intent(
										DsahBoardScreen.this,
										PaymentActivity.class);

								intent.putExtra(PaymentActivity.EXTRA_PAYMENT,
										thingToBuy);

								startActivityForResult(intent,
										REQUEST_CODE_PAYMENT);
							} else {
								val = 1;
								PayPalPayment thingToBuy = getThingToBuy(PayPalPayment.PAYMENT_INTENT_SALE);

								Intent intent = new Intent(
										DsahBoardScreen.this,
										PaymentActivity.class);

								intent.putExtra(PaymentActivity.EXTRA_PAYMENT,
										thingToBuy);

								startActivityForResult(intent,
										REQUEST_CODE_PAYMENT);
							}

							}
					});
			builder.show();*/

			/*
			 * PayPalPayment thingToBuy =
			 * getThingToBuy(PayPalPayment.PAYMENT_INTENT_SALE);
			 * 
			 * Intent intent = new Intent(DsahBoardScreen.this,
			 * PaymentActivity.class);
			 * 
			 * intent.putExtra(PaymentActivity.EXTRA_PAYMENT, thingToBuy);
			 * 
			 * startActivityForResult(intent, REQUEST_CODE_PAYMENT);
			 */

			break;

		case R.id.ll_live_support:
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
		Log.e("!!ffffffffffffffffffffffff here", "reach here"
				+ Constant.isRunning);

		if (Constant.isRunning) {
			Log.e("!!ffffffffffffffffffffffff here", "reach here");
			handler.removeCallbacks(runnable);
		}
		Constant.isDialogOpen = false;
		finish();
		super.onBackPressed();

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (mHelper == null)
			return;

		// Pass on the activity result to the helper for handling
		if (!mHelper.handleActivityResult(requestCode, resultCode, data)) {
			// not handled, so handle it ourselves (here's where you'd
			// perform any handling of activity results not related to in-app
			// billing...
			super.onActivityResult(requestCode, resultCode, data);
		}
	}

	/*
	 * Enable retrieval of shipping addresses from buyer's PayPal account
	 */
	private void enableShippingAddressRetrieval(PayPalPayment paypalPayment,
			boolean enable) {
		paypalPayment.enablePayPalShippingAddressesRetrieval(enable);
	}

	@Override
	public void onDestroy() {
		// Stop service when done
		Log.d(TAG, "Destroying helper.");
		if (mHelper != null) {
			mHelper.dispose();
			mHelper = null;
		}
		super.onDestroy();
	}
	
}
