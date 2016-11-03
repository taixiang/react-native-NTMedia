package com.ntutilmedia;

import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;

/**
 * Created by taixiang on 16/10/14.
 */
public class NTMediaModule extends ReactContextBaseJavaModule {

	private NTMediaPlayer mediaPlayer;
	public static ReactApplicationContext context = null;

	public NTMediaModule(ReactApplicationContext reactContext) {
		super(reactContext);
		context = reactContext;
	}


	@Override
	public String getName() {
		return "NTMediaModule";
	}

	/**
	 * 初始化media
	 * @param url
	 * @param callback
	 */
	@ReactMethod
	public void initMedia(String url, Callback callback){
		mediaPlayer = new NTMediaPlayer();
		mediaPlayer.initMedia(url, callback);
	}

	/**
	 * 播放
	 */
	@ReactMethod
	public void playMedia(){
		mediaPlayer.playMedia();
	}

	/**
	 * 暂停
	 */
	@ReactMethod
	public void pauseMedia(){
		mediaPlayer.pauseMedia();
	}

	/**
	 * 释放mediaPlayer资源
	 */
	@ReactMethod
	public void releaseMedia(){
		mediaPlayer.releaseMedia();
	}

	/**
	 * 拖动播放,拖动到nSec秒
	 * @param nSec
	 */
	@ReactMethod
	public void sliderPlayMedia(int nSec) {
		mediaPlayer.seekToInSec(nSec);
	}

}
