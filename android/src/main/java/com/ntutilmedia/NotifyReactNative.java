package com.ntutilmedia;

import android.support.annotation.Nullable;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;

public class NotifyReactNative {

	// send event for notify react-native
	public static void sendEvent(@Nullable WritableMap params) {
		sendNativeEvent(NTMediaModule.context, "cl_msg", params);
	}

	/*
	 * 基础函数，发送通知
	 */
	public static void sendNativeEvent(ReactContext reactContext, String eventName,
									   @Nullable WritableMap params) {
		reactContext.getJSModule(
				DeviceEventManagerModule.RCTDeviceEventEmitter.class).emit(
				eventName, params);
	}

	/**
	 * 播放时长
	 * @param currentDuration
	 */
	public static void notifyDuration(String currentDuration){
		WritableMap params = Arguments.createMap();
		params.putInt("type", 5001);
		params.putString("currentDuration", currentDuration);
		sendEvent(params);
	}

	/**
	 * 播放结束
	 */
	public static void notifyMediaEnd(){
		WritableMap params = Arguments.createMap();
		params.putInt("type", 5002);
		sendEvent(params);
	}

}
