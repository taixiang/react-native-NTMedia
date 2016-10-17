package com.ntutilmedia;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;

import com.facebook.react.bridge.Callback;

import java.io.IOException;

/**
 * Created by taixiang on 16/9/30.
 */
public class NTMediaPlayer extends MediaPlayer{

	private Handler handler;
	private Runnable updateTimeTask;
	private boolean isPrepared = false;
	public void initMedia(String url, final Callback callback){
		try {
			reset();
			setAudioStreamType(AudioManager.STREAM_SYSTEM);
			setDataSource(url);
			prepareAsync();
			setOnCompletionListener(new OnCompletionListener() {
				@Override
				public void onCompletion(MediaPlayer mp) {
					NotifyReactNative.notifyMediaEnd();
				}
			});
			setOnErrorListener(new OnErrorListener() {
				@Override
				public boolean onError(MediaPlayer mp, int what, int extra) {
					reset();
					release();
					return true;
				}
			});
			setOnPreparedListener(new OnPreparedListener() {
				@Override
				public void onPrepared(MediaPlayer mp) {
					long totalDuration = getDuration();
					callback.invoke(String.valueOf(totalDuration));
					isPrepared = true;
				}
			});

			handler = new Handler();
			updateTimeTask = new Runnable() {
				@Override
				public void run() {
					if(isPrepared){
						long currentDuration = getCurrentPosition();
						NotifyReactNative.notifyDuration(String.valueOf(currentDuration));
					}
					handler.postDelayed(this, 1000);
				}
			};
			handler.postDelayed(updateTimeTask, 1000);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void playMedia(){
		if(isPrepared){
			start();
		}
	}

	public void pauseMedia(){
		if(isPrepared){
			pause();
		}
	}

	/**
	 * 释放mediaPlayer资源
	 */
	public void releaseMedia(){
		isPrepared = false;
		stop();
		release();
		if(handler != null && updateTimeTask != null){
			handler.removeCallbacks(updateTimeTask);
		}
	}

}
