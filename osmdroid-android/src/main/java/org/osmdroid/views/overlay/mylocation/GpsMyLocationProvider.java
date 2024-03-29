package org.osmdroid.views.overlay.mylocation;

import org.osmdroid.util.NetworkLocationIgnorer;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

public class GpsMyLocationProvider implements IMyLocationProvider, LocationListener {
	private final LocationManager mLocationManager;
	private Location mLocation;

	private IMyLocationConsumer mMyLocationConsumer;
	private long mLocationUpdateMinTime = 0;
	private float mLocationUpdateMinDistance = 0.0f;
	private final NetworkLocationIgnorer mIgnorer = new NetworkLocationIgnorer();

	public GpsMyLocationProvider(Context context) {
		mLocationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public long getLocationUpdateMinTime() {
		return mLocationUpdateMinTime;
	}

	/**
	 * Set the minimum interval for location updates. See {@link
	 * LocationManager.requestLocationUpdates(String, long, float, LocationListener)}. Note that you
	 * should call this before calling {@link enableMyLocation()}.
	 * 
	 * @param milliSeconds
	 */
	public void setLocationUpdateMinTime(final long milliSeconds) {
		mLocationUpdateMinTime = milliSeconds;
	}

	public float getLocationUpdateMinDistance() {
		return mLocationUpdateMinDistance;
	}

	/**
	 * Set the minimum distance for location updates. See
	 * {@link LocationManager.requestLocationUpdates}. Note that you should call this before calling
	 * {@link enableMyLocation()}.
	 * 
	 * @param meters
	 */
	public void setLocationUpdateMinDistance(final float meters) {
		mLocationUpdateMinDistance = meters;
	}

	//
	// IMyLocationProvider
	//

	/**
	 * Enable location updates and show your current location on the map. By default this will
	 * request location updates as frequently as possible, but you can change the frequency and/or
	 * distance by calling {@link setLocationUpdateMinTime(long)} and/or {@link
	 * setLocationUpdateMinDistance(float)} before calling this method.
	 */
	
	public boolean startLocationProvider(IMyLocationConsumer myLocationConsumer) {
		mMyLocationConsumer = myLocationConsumer;
		boolean result = false;
		for (final String provider : mLocationManager.getProviders(true)) {
			if (LocationManager.GPS_PROVIDER.equals(provider)
					|| LocationManager.NETWORK_PROVIDER.equals(provider)) {
				result = true;
				mLocationManager.requestLocationUpdates(provider, mLocationUpdateMinTime,
						mLocationUpdateMinDistance, this);
			}
		}
		return result;
	}

	
	public void stopLocationProvider() {
		mMyLocationConsumer = null;
		mLocationManager.removeUpdates(this);
	}

	
	public Location getLastKnownLocation() {
		return mLocation;
	}

	//
	// LocationListener
	//

	
	public void onLocationChanged(final Location location) {
		// ignore temporary non-gps fix
		if (mIgnorer.shouldIgnore(location.getProvider(), System.currentTimeMillis()))
			return;

		mLocation = location;
		if (mMyLocationConsumer != null)
			mMyLocationConsumer.onLocationChanged(mLocation, this);
	}

	
	public void onProviderDisabled(final String provider) {
	}

	
	public void onProviderEnabled(final String provider) {
	}

	
	public void onStatusChanged(final String provider, final int status, final Bundle extras) {
	}
}
