package com.ntutilmedia;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.util.Log;

import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReactMethod;

import java.io.IOException;

/**
 * Created by taixiang on 16/9/30.
 */
public class NTMediaPlayer extends MediaPlayer {

	private Handler handler;
	private Runnable updateTimeTask;
	private boolean isPrepared = false;
	private AudioManager am = null;
	private int maxVolume = 0;
	private int prevVolume = 0;
	private Context context = null;


	public NTMediaPlayer(Context ctx) {
		this.context = ctx;
	}

	public void initMedia(String url, final Callback callback) {
		try {
			reset();
			setAudioStreamType(AudioManager.STREAM_MUSIC);
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

			// 音量调节范围,用的是 STREAM_MUSIC
			am = (AudioManager) (context.getSystemService(Context.AUDIO_SERVICE));
			maxVolume = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
			prevVolume = am.getStreamVolume(AudioManager.STREAM_MUSIC);
			float x = (float) (1.0 * prevVolume / maxVolume);
			this.setVolume(x, x);

			handler = new Handler();
			updateTimeTask = new Runnable() {
				@Override
				public void run() {
					long currentDuration = getCurrentPosition();
//					Log.d("NTMediaPlayer", "isPrepared=" + String.valueOf(isPrepared) + ", current=" + currentDuration);
					if (isPrepared) {
						int currentVolume = am.getStreamVolume(AudioManager.STREAM_MUSIC);
						if (currentVolume != prevVolume) {
							float y = (float) (1.0 * currentVolume / maxVolume);
							NTMediaPlayer.this.setVolume(y, y);
							prevVolume = currentVolume;
						}
						Log.d("NTMediaPlayer", "prevVolume=" + String.valueOf(prevVolume) + "current="
								+ String.valueOf(currentVolume) + ", max=" + String.valueOf(maxVolume));
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

	/**
	 * 播放
	 */
	public void playMedia() {
		if (isPrepared) {
			start();
		}
	}

	/**
	 * 暂停
	 */
	public void pauseMedia() {
		if (isPrepared) {
			pause();
		}
	}

	/**
	 * 跳跃播放
	 *
	 * @param nSec 秒数
	 */
	public void seekToInSec(int nSec) {
		this.seekTo(nSec * 1000);
	}

	/**
	 * 释放mediaPlayer资源
	 */
	public void releaseMedia() {
		isPrepared = false;
		stop();
		release();
		if (handler != null && updateTimeTask != null) {
			handler.removeCallbacks(updateTimeTask);
		}
	}
}
