package com.white.tooth;

import java.math.BigDecimal;

import org.json.JSONException;

import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class PaypalSplashActivity___ extends BaseActivity implements OnClickListener {
	private Button btn_add_to_cart, btn_skip;
	private static final String TAG = "paymentExample";
	private static final String CONFIG_ENVIRONMENT = PayPalConfiguration.ENVIRONMENT_NO_NETWORK;

	// note that these credentials will differ between live & sandbox
	// environments.
	// private static final String CONFIG_CLIENT_ID =
	// "credential from developer.paypal.com";
	private static final String CONFIG_CLIENT_ID = "pearlywhitescenter@gmail.com";
	private static final int REQUEST_CODE_PAYMENT = 1;

	private static PayPalConfiguration config = new PayPalConfiguration().environment(CONFIG_ENVIRONMENT).clientId(CONFIG_CLIENT_ID)
	// The following are only used in PayPalFuturePaymentActivity.
			.merchantName("Pearly Whites Center").merchantPrivacyPolicyUri(Uri.parse("https://www.example.com/privacy")).merchantUserAgreementUri(Uri.parse("https://www.example.com/legal"));

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.addtocart);
		btn_add_to_cart = (Button) findViewById(R.id.btn_add_to_cart);
		btn_skip = (Button) findViewById(R.id.btn_skip);
		btn_add_to_cart.setOnClickListener(this);
		btn_skip.setOnClickListener(this);
		Intent intent = new Intent(this, PayPalService.class);
		intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
		startService(intent);
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.btn_add_to_cart:
			/*PayPalPayment thingToBuy = getThingToBuy(PayPalPayment.PAYMENT_INTENT_SALE);

			Intent intent = new Intent(PaypalSplashActivity.this, PaymentActivity.class);

			intent.putExtra(PaymentActivity.EXTRA_PAYMENT, thingToBuy);

			startActivityForResult(intent, REQUEST_CODE_PAYMENT);*/
			Intent httpIntent = new Intent(Intent.ACTION_VIEW);
			httpIntent.setData(Uri.parse("https://squareup.com/market/whitetooth/white-tooth-starter-kit"));

			startActivity(httpIntent); 
			break;

		case R.id.btn_skip:
			Intent i = new Intent(PaypalSplashActivity___.this, DsahBoardScreen.class);
			startActivity(i);
			finish();
			break;

		}

	}

	@SuppressWarnings("unused")
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

	/*
	 * Enable retrieval of shipping addresses from buyer's PayPal account
	 */
	@SuppressWarnings("unused")
	private void enableShippingAddressRetrieval(PayPalPayment paypalPayment, boolean enable) {
		paypalPayment.enablePayPalShippingAddressesRetrieval(enable);
	}

	@Override
	public void onDestroy() {
		// Stop service when done
		stopService(new Intent(this, PayPalService.class));
		super.onDestroy();
	}
}
