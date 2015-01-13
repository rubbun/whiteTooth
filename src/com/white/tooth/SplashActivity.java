package com.white.tooth;

import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.MediaController;
import android.widget.VideoView;

public class SplashActivity extends BaseActivity{
//http://www.techotopia.com/index.php/Integrating_Google_Play_In-app_Billing_into_an_Android_Application_%E2%80%93_A_Tutorial	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);		
		showVideo();
	}
	
	private void showVideo()
    {
        VideoView videoView = (VideoView)findViewById(R.id.videoview);
         Uri uri = Uri.parse("android.resource://com.white.tooth/"+R.raw.b);
         /* MediaController mc = new MediaController(this);
        vd.setMediaController(mc);
        vd.setVideoURI(uri);
        vd.start();*/
        
        
        videoView.setVideoURI(uri);
        MediaController mc = new MediaController(SplashActivity.this);
        videoView.setMediaController(mc);
        videoView.requestFocus();
        videoView.start();
        mc.setVisibility(View.GONE);
        
        videoView.setOnCompletionListener(new OnCompletionListener() {
			
			@Override
			public void onCompletion(MediaPlayer mp) {
				Intent i = new Intent(SplashActivity.this,InstructionActivity.class);
				startActivity(i);
				finish();
				
			}
		});
        
       
    }
}
