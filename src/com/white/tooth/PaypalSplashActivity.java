package com.white.tooth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.white.tooth.util.IabHelper;
import com.white.tooth.util.IabHelper.OnIabPurchaseFinishedListener;
import com.white.tooth.util.IabHelper.QueryInventoryFinishedListener;
import com.white.tooth.util.IabResult;
import com.white.tooth.util.Inventory;
import com.white.tooth.util.Purchase;

public class PaypalSplashActivity extends BaseActivity implements OnClickListener {
	private Button btn_add_to_cart, btn_skip;
	/*private static final String TAG = "paymentExample";
	private static final String CONFIG_ENVIRONMENT = PayPalConfiguration.ENVIRONMENT_NO_NETWORK;

	private static final String CONFIG_CLIENT_ID = "pearlywhitescenter@gmail.com";
	private static final int REQUEST_CODE_PAYMENT = 1;

	private static PayPalConfiguration config = new PayPalConfiguration().environment(CONFIG_ENVIRONMENT).clientId(CONFIG_CLIENT_ID).merchantName("Pearly Whites Center").merchantPrivacyPolicyUri(Uri.parse("https://www.example.com/privacy")).merchantUserAgreementUri(Uri.parse("https://www.example.com/legal"));
*/
	private static final String pay = "pay";
	static final int RC_REQUEST = 10001;
	public static IabHelper mHelper;
	public OnIabPurchaseFinishedListener mPurchaseFinishedListener;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.addtocart);
		btn_add_to_cart = (Button) findViewById(R.id.btn_add_to_cart);
		btn_skip = (Button) findViewById(R.id.btn_skip);
		btn_add_to_cart.setOnClickListener(this);
		btn_skip.setOnClickListener(this);
		/*Intent intent = new Intent(this, PayPalService.class);
		intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
		startService(intent);*/
		
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
						//isPayed = true;
					}
				}
			};
			
			
			try {
				String base64PublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAmGclpuB6CKUVdVZSGsJfhLz8z18q/86GFIzakPjcRw7OZBwLvAiwgpoBCfsIglY9zEmeOC/vaY9MgIaMdVsbkuWL5gd3T7x6B1mZErMwN60VhdMsNaRQdctswT41ynyZLQL1U4pn1IQ+KLWFwQBEkH8OfzWpTk8A6yi+Ahl1ue+cV/hqCeZsXFPJTBQfuk8l50aPgbBTfnyS4vvBsYyADzUhsHZVluWMzi6BMiQ9CA0ybehRYIABuwwIkxTYzl2vMN1uCXNAQqZkOQhAaJ66GZhwLB/g6HUUcW0Dt84fbyusbaO1gH50xr9gcVMCPFApJmJb1zrdjr3MxeENoUqsqQIDAQAB";
				mHelper = new IabHelper(this, base64PublicKey);
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
								//isPayed = (wheelPurchase != null && verifyDeveloperPayload(wheelPurchase));
							}
						});
					}
				});
			} catch (Exception e) {
			}
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.btn_add_to_cart:
			/*Intent httpIntent = new Intent(Intent.ACTION_VIEW);
			httpIntent.setData(Uri.parse("https://squareup.com/market/whitetooth/white-tooth-starter-kit"));

			startActivity(httpIntent);*/
			String payload = "";
			mHelper.launchPurchaseFlow(PaypalSplashActivity.this, pay, RC_REQUEST, mPurchaseFinishedListener, payload);
			break;
			

		case R.id.btn_skip:
			Intent i = new Intent(PaypalSplashActivity.this, DsahBoardScreen.class);
			startActivity(i);
			finish();
			break;

		}

	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    if (mHelper == null) return;

	    
	    if (!mHelper.handleActivityResult(requestCode, resultCode, data)) {
	       super.onActivityResult(requestCode, resultCode, data);
	    }
	}
	public boolean verifyDeveloperPayload(Purchase p) {
		String payload = p.getDeveloperPayload();
		return true;
	}
	@Override
	public void onDestroy() {
		super.onDestroy();
		if (mHelper != null) mHelper.dispose();
		mHelper = null;
	}
	/*@SuppressWarnings("unused")
	private PayPalPayment getThingToBuy(String paymentIntent) {

		return new PayPalPayment(new BigDecimal("14.99"), "USD", "Starter Kit", paymentIntent);

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_CODE_PAYMENT) {
			if (resultCode == Activity.RESULT_OK) {
				PaymentConfirmation confirm = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
				if (confirm != null) {
					try {
						Log.i(TAG, confirm.toJSONObject().toString(4));
						Log.i(TAG, confirm.getPayment().toJSONObject().toString(4));

						Toast.makeText(getApplicationContext(), "PaymentConfirmation info received from PayPal", Toast.LENGTH_LONG).show();

					} catch (JSONException e) {
						Log.e(TAG, "an extremely unlikely failure occurred: ", e);
					}
				}
			} else if (resultCode == Activity.RESULT_CANCELED) {
				Log.i(TAG, "The user canceled.");
			} else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
				Log.i(TAG, "An invalid Payment or PayPalConfiguration was submitted. Please see the docs.");
			}
		}
	}

	@SuppressWarnings("unused")
	private void enableShippingAddressRetrieval(PayPalPayment paypalPayment, boolean enable) {
		paypalPayment.enablePayPalShippingAddressesRetrieval(enable);
	}

	@Override
	public void onDestroy() {
		stopService(new Intent(this, PayPalService.class));
		super.onDestroy();
	}*/
}
