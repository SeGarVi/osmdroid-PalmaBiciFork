package org.osmdroid.util;

import org.osmdroid.api.IPosition;

public class Position implements IPosition {
	private final double mLatitude;
	private final double mLongitude;
	private boolean mHasBearing;
	private float mBearing;
	private boolean mHasZoomLevel;
	private float mZoomLevel;

	public Position(final double aLatitude, final double aLongitude) {
		mLatitude = aLatitude;
		mLongitude = aLongitude;
	}

	
	public double getLatitude() {
		return mLatitude;
	}

	
	public double getLongitude() {
		return mLongitude;
	}

	
	public boolean hasBearing() {
		return mHasBearing;
	}

	
	public float getBearing() {
		return mBearing;
	}

	public void setBearing(final float aBearing) {
		mHasBearing = true;
		mBearing = aBearing;
	}

	
	public boolean hasZoomLevel() {
		return mHasZoomLevel;
	}

	
	public float getZoomLevel() {
		return mZoomLevel;
	}

	public void setZoomLevel(final float aZoomLevel) {
		mHasZoomLevel = true;
		mZoomLevel = aZoomLevel;
	}
}
