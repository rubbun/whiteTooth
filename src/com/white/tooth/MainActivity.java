package com.white.tooth;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.white.tooth.util.IabHelper;
import com.white.tooth.util.IabHelper.OnIabPurchaseFinishedListener;
import com.white.tooth.util.IabHelper.QueryInventoryFinishedListener;
import com.white.tooth.util.IabResult;
import com.white.tooth.util.Inventory;
import com.white.tooth.util.Purchase;

public class MainActivity extends Activity {
	static final String TAG = "TrivialDrive";

	boolean mIsPremium = false;

	boolean mSubscribedToInfiniteGas = false;

	static final String SKU_GAS = "gas";
	private static final String pay = "pay";

	public IabHelper mHelper;
	public OnIabPurchaseFinishedListener mPurchaseFinishedListener;

	static final int RC_REQUEST = 10001;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_1);
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

	// User clicked the "Buy Gas" button
	public void onBuyGasButtonClicked(View arg0) {
		Log.d(TAG, "Buy gas button clicked.");

		String payload = "";

		mHelper.launchPurchaseFlow(this, SKU_GAS, RC_REQUEST, mPurchaseFinishedListener, payload);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();

		Log.d(TAG, "Destroying helper.");
		if (mHelper != null) {
			mHelper.dispose();
			mHelper = null;
		}
	}

	public boolean verifyDeveloperPayload(Purchase p) {
		String payload = p.getDeveloperPayload();
		return true;
	}
}
