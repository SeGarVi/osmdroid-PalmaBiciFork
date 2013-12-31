package org.osmdroid.tileprovider.util;

import org.osmdroid.tileprovider.IRegisterReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

public class SimpleRegisterReceiver implements IRegisterReceiver {

	private final Context mContext;

	public SimpleRegisterReceiver(final Context pContext) {
		super();
		mContext = pContext;
	}

	
	public Intent registerReceiver(final BroadcastReceiver aReceiver, final IntentFilter aFilter) {
		return mContext.registerReceiver(aReceiver, aFilter);
	}

	
	public void unregisterReceiver(final BroadcastReceiver aReceiver) {
		mContext.unregisterReceiver(aReceiver);
	}
}
