package org.osmdroid.tileprovider.tilesource;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Random;

import org.osmdroid.ResourceProxy;
import org.osmdroid.ResourceProxy.string;
import org.osmdroid.tileprovider.BitmapPool;
import org.osmdroid.tileprovider.MapTile;
import org.osmdroid.tileprovider.ReusableBitmapDrawable;
import org.osmdroid.tileprovider.constants.OpenStreetMapTileProviderConstants;
//////import org.slf4j.Logger;
//////import org.slf4j.LoggerFactory;




import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;

public abstract class BitmapTileSourceBase implements ITileSource,
		OpenStreetMapTileProviderConstants {

	//private static final Logger logger = LoggerFactory.getLogger(BitmapTileSourceBase.class);

	private static int globalOrdinal = 0;

	private final int mMinimumZoomLevel;
	private final int mMaximumZoomLevel;

	private final int mOrdinal;
	protected final String mName;
	protected final String mImageFilenameEnding;
	protected final Random random = new Random();

	private final int mTileSizePixels;

	private final string mResourceId;

	public BitmapTileSourceBase(final String aName, final string aResourceId,
			final int aZoomMinLevel, final int aZoomMaxLevel, final int aTileSizePixels,
			final String aImageFilenameEnding) {
		mResourceId = aResourceId;
		mOrdinal = globalOrdinal++;
		mName = aName;
		mMinimumZoomLevel = aZoomMinLevel;
		mMaximumZoomLevel = aZoomMaxLevel;
		mTileSizePixels = aTileSizePixels;
		mImageFilenameEnding = aImageFilenameEnding;
	}

	
	public int ordinal() {
		return mOrdinal;
	}

	
	public String name() {
		return mName;
	}

	public String pathBase() {
		return mName;
	}

	public String imageFilenameEnding() {
		return mImageFilenameEnding;
	}

	
	public int getMinimumZoomLevel() {
		return mMinimumZoomLevel;
	}

	
	public int getMaximumZoomLevel() {
		return mMaximumZoomLevel;
	}

	
	public int getTileSizePixels() {
		return mTileSizePixels;
	}

	
	public String localizedName(final ResourceProxy proxy) {
		return proxy.getString(mResourceId);
	}

	
	public Drawable getDrawable(final String aFilePath) {
		try {
			// default implementation will load the file as a bitmap and create
			// a BitmapDrawable from it
			/*BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
			BitmapPool.getInstance().applyReusableOptions(bitmapOptions);
			final Bitmap bitmap = BitmapFactory.decodeFile(aFilePath, bitmapOptions);*/
			Bitmap bitmap = BitmapFactory.decodeStream
				     (new BufferedInputStream(new FileInputStream(aFilePath)));
			if (bitmap != null) {
				return new ReusableBitmapDrawable(bitmap);
			} else {
				// if we couldn't load it then it's invalid - delete it
				try {
					new File(aFilePath).delete();
				} catch (final Throwable e) {
					//logger.error("Error deleting invalid file: " + aFilePath, e);
				}
			}
		} catch (final OutOfMemoryError e) {
			//logger.error("OutOfMemoryError loading bitmap: " + aFilePath);
			System.gc();
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return null;
	}

	
	public String getTileRelativeFilenameString(final MapTile tile) {
		final StringBuilder sb = new StringBuilder();
		sb.append(pathBase());
		sb.append('/');
		sb.append(tile.getZoomLevel());
		sb.append('/');
		sb.append(tile.getX());
		sb.append('/');
		sb.append(tile.getY());
		sb.append(imageFilenameEnding());
		return sb.toString();
	}

	
	public Drawable getDrawable(final InputStream aFileInputStream) throws LowMemoryException {
		try {
			// default implementation will load the file as a bitmap and create
			// a BitmapDrawable from it
			BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
			BitmapPool.getInstance().applyReusableOptions(bitmapOptions);
			final Bitmap bitmap = BitmapFactory.decodeStream(aFileInputStream, null, bitmapOptions);
			if (bitmap != null) {
				return new ReusableBitmapDrawable(bitmap);
			}
		} catch (final OutOfMemoryError e) {
			//logger.error("OutOfMemoryError loading bitmap");
			System.gc();
			throw new LowMemoryException(e);
		}
		return null;
	}

	public final class LowMemoryException extends Exception {
		private static final long serialVersionUID = 146526524087765134L;

		public LowMemoryException(final String pDetailMessage) {
			super(pDetailMessage);
		}

		public LowMemoryException(final Throwable pThrowable) {
			super(pThrowable);
		}
	}
}
