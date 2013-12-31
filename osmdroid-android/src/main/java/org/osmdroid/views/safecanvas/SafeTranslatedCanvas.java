package org.osmdroid.views.safecanvas;


import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.DrawFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Picture;
import android.graphics.PorterDuff.Mode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;
import android.graphics.Region.Op;

/**
 * An implementation of {@link ISafeCanvas} that wraps a {@link Canvas} and adjusts drawing calls to
 * the wrapped Canvas so that they are relative to an origin that is always at the center of the
 * screen.<br />
 * <br />
 * See {@link ISafeCanvas} for details<br />
 * 
 * @author Marc Kurtz
 * 
 */
public class SafeTranslatedCanvas extends Canvas implements ISafeCanvas {
	private final static Matrix sMatrix = new Matrix();
	private final static RectF sRectF = new RectF();
	private static float[] sFloatAry = new float[0];
	private Canvas mCanvas;
	private final Matrix mMatrix = new Matrix();
	public int xOffset;
	public int yOffset;

	public SafeTranslatedCanvas() {
		//
	}

	public SafeTranslatedCanvas(Canvas canvas) {
		this.setCanvas(canvas);
	}

	public Canvas getSafeCanvas() {
		return this;
	}

	
	public int getXOffset() {
		return xOffset;
	}

	
	public int getYOffset() {
		return yOffset;
	}

	public void setCanvas(Canvas canvas) {
		mCanvas = canvas;
		canvas.getMatrix(mMatrix);
	}

	public void getUnsafeCanvas(UnsafeCanvasHandler handler) {
		this.save();
		this.setMatrix(this.getOriginalMatrix());
		handler.onUnsafeCanvas(mCanvas);
		this.restore();
	}

	public Canvas getWrappedCanvas() {
		return mCanvas;
	}

	public Matrix getOriginalMatrix() {
		return mMatrix;
	}

	
	public boolean clipPath(SafeTranslatedPath path, Op op) {
		return getWrappedCanvas().clipPath(path, op);
	}

	
	public boolean clipPath(SafeTranslatedPath path) {
		return getWrappedCanvas().clipPath(path);
	}

	
	public boolean clipRect(double left, double top, double right, double bottom, Op op) {
		return getWrappedCanvas().clipRect((float) (left + xOffset), (float) (top + yOffset),
				(float) (right + xOffset), (float) (bottom + yOffset), op);
	}

	
	public boolean clipRect(double left, double top, double right, double bottom) {
		return getWrappedCanvas().clipRect((float) (left + xOffset), (float) (top + yOffset),
				(float) (right + xOffset), (float) (bottom + yOffset));
	}

	
	public boolean clipRect(int left, int top, int right, int bottom) {
		return getWrappedCanvas().clipRect(left + xOffset, top + yOffset, right + xOffset,
				bottom + yOffset);
	}

	
	public boolean clipRect(Rect rect, Op op) {
		rect.offset(xOffset, yOffset);
		return getWrappedCanvas().clipRect(rect, op);
	}

	
	public boolean clipRect(Rect rect) {
		rect.offset(xOffset, yOffset);
		return getWrappedCanvas().clipRect(rect);
	}

	
	public boolean clipRegion(Region region, Op op) {
		region.translate(xOffset, yOffset);
		return getWrappedCanvas().clipRegion(region, op);
	}

	
	public boolean clipRegion(Region region) {
		region.translate(xOffset, yOffset);
		return getWrappedCanvas().clipRegion(region);
	}

	
	public void concat(Matrix matrix) {
		getWrappedCanvas().concat(matrix);
	}

	
	public void drawARGB(int a, int r, int g, int b) {
		getWrappedCanvas().drawARGB(a, r, g, b);
	}

	
	public void drawArc(Rect oval, float startAngle, float sweepAngle, boolean useCenter,
			SafePaint paint) {
		getWrappedCanvas().drawArc(this.toOffsetRectF(oval, sRectF), startAngle, sweepAngle,
				useCenter, paint);
	}

	
	public void drawBitmap(Bitmap bitmap, double left, double top, SafePaint paint) {
		getWrappedCanvas().drawBitmap(bitmap, (float) (left + xOffset), (float) (top + yOffset),
				paint);
	}

	
	public void drawBitmap(Bitmap bitmap, Matrix matrix, SafePaint paint) {
		sMatrix.set(matrix);
		sMatrix.postTranslate(xOffset, yOffset);
		getWrappedCanvas().drawBitmap(bitmap, sMatrix, paint);
	}

	
	public void drawBitmap(Bitmap bitmap, Rect src, Rect dst, SafePaint paint) {
		dst.offset(xOffset, yOffset);
		getWrappedCanvas().drawBitmap(bitmap, src, dst, paint);
		dst.offset(-xOffset, -yOffset);
	}

	/* This is used by Drawable.draw(Canvas), so also we adjust here */
	
	public void drawBitmap(Bitmap bitmap, Rect src, Rect dst, Paint paint) {
		dst.offset(xOffset, yOffset);
		getWrappedCanvas().drawBitmap(bitmap, src, dst, paint);
		dst.offset(-xOffset, -yOffset);
	}

	
	public void drawBitmap(int[] colors, int offset, int stride, double x, double y, int width,
			int height, boolean hasAlpha, SafePaint paint) {
		getWrappedCanvas().drawBitmap(colors, offset, stride, (float) (x + xOffset),
				(float) (y + yOffset), width, height, hasAlpha, paint);
	}

	
	public void drawBitmap(int[] colors, int offset, int stride, int x, int y, int width,
			int height, boolean hasAlpha, SafePaint paint) {
		getWrappedCanvas().drawBitmap(colors, offset, stride, x + offset, y + offset, width,
				height, hasAlpha, paint);
	}

	
	public void drawBitmapMesh(Bitmap bitmap, int meshWidth, int meshHeight, double[] verts,
			int vertOffset, int[] colors, int colorOffset, SafePaint paint) {
		getWrappedCanvas().drawBitmapMesh(bitmap, meshWidth, meshHeight,
				this.toOffsetFloatAry(verts, sFloatAry), vertOffset, colors, colorOffset, paint);
	}

	
	public void drawCircle(double cx, double cy, float radius, SafePaint paint) {
		getWrappedCanvas()
				.drawCircle((float) (cx + xOffset), (float) (cy + yOffset), radius, paint);
	}

	
	public void drawColor(int color, Mode mode) {

		getWrappedCanvas().drawColor(color, mode);
	}

	
	public void drawColor(int color) {

		getWrappedCanvas().drawColor(color);
	}

	
	public void drawLine(double startX, double startY, double stopX, double stopY, SafePaint paint) {
		startX += xOffset;
		startY += yOffset;
		stopX += xOffset;
		stopY += yOffset;
		getWrappedCanvas().drawLine((float) startX, (float) startY, (float) stopX, (float) stopY,
				paint);
	}

	
	public void drawLines(double[] pts, int offset, int count, SafePaint paint) {
		getWrappedCanvas().drawLines(this.toOffsetFloatAry(pts, sFloatAry), offset, count, paint);
	}

	
	public void drawLines(double[] pts, SafePaint paint) {
		getWrappedCanvas().drawLines(this.toOffsetFloatAry(pts, sFloatAry), paint);
	}

	
	public void drawOval(Rect oval, SafePaint paint) {
		getWrappedCanvas().drawOval(this.toOffsetRectF(oval, sRectF), paint);
	}

	
	public void drawPaint(SafePaint paint) {
		getWrappedCanvas().drawPaint(paint);
	}

	
	public void drawPath(SafeTranslatedPath path, SafePaint paint) {
		getWrappedCanvas().drawPath(path, paint);
	}

	
	public void drawPicture(Picture picture, Rect dst) {
		dst.offset(xOffset, yOffset);
		getWrappedCanvas().drawPicture(picture, dst);
		dst.offset(-xOffset, -yOffset);
	}

	
	public void drawPicture(Picture picture) {
		getWrappedCanvas().drawPicture(picture);
	}

	
	public void drawPoint(double x, double y, SafePaint paint) {
		x += xOffset;
		y += yOffset;
		getWrappedCanvas().drawPoint((float) x, (float) y, paint);
	}

	
	public void drawPoints(double[] pts, int offset, int count, SafePaint paint) {
		getWrappedCanvas().drawPoints(this.toOffsetFloatAry(pts, sFloatAry), offset, count, paint);
	}

	
	public void drawPoints(double[] pts, SafePaint paint) {
		getWrappedCanvas().drawPoints(this.toOffsetFloatAry(pts, sFloatAry), paint);
	}

	
	public void drawPosText(char[] text, int index, int count, double[] pos, SafePaint paint) {
		getWrappedCanvas().drawPosText(text, index, count, this.toOffsetFloatAry(pos, sFloatAry),
				paint);
	}

	
	public void drawPosText(String text, double[] pos, SafePaint paint) {
		getWrappedCanvas().drawPosText(text, this.toOffsetFloatAry(pos, sFloatAry), paint);
	}

	
	public void drawRGB(int r, int g, int b) {
		getWrappedCanvas().drawRGB(r, g, b);
	}

	
	public void drawRect(double left, double top, double right, double bottom, SafePaint paint) {
		left += xOffset;
		right += xOffset;
		top += yOffset;
		bottom += yOffset;
		getWrappedCanvas()
				.drawRect((float) left, (float) top, (float) right, (float) bottom, paint);
	}

	
	public void drawRect(Rect r, SafePaint paint) {
		r.offset(xOffset, yOffset);
		getWrappedCanvas().drawRect(r, paint);
		r.offset(-xOffset, -yOffset);
	}

	
	public void drawRoundRect(Rect rect, float rx, float ry, SafePaint paint) {
		getWrappedCanvas().drawRoundRect(this.toOffsetRectF(rect, sRectF), rx, ry, paint);
	}

	
	public void drawText(String text, double x, double y, SafePaint paint) {
		getWrappedCanvas().drawText(text, (float) (x + xOffset), (float) (y + yOffset), paint);
	}

	
	public void drawText(char[] text, int index, int count, double x, double y, SafePaint paint) {
		getWrappedCanvas().drawText(text, index, count, (float) (x + xOffset),
				(float) (y + yOffset), paint);
	}

	
	public void drawText(CharSequence text, int start, int end, double x, double y, SafePaint paint) {
		getWrappedCanvas().drawText(text, start, end, (float) (x + xOffset), (float) (y + yOffset),
				paint);
	}

	
	public void drawText(String text, int start, int end, double x, double y, SafePaint paint) {
		getWrappedCanvas().drawText(text, start, end, (float) (x + xOffset), (float) (y + yOffset),
				paint);
	}

	
	public void drawTextOnPath(char[] text, int index, int count, SafeTranslatedPath path,
			float hOffset, float vOffset, SafePaint paint) {
		getWrappedCanvas().drawTextOnPath(text, index, count, path, hOffset, vOffset, paint);
	}

	
	public void drawTextOnPath(String text, SafeTranslatedPath path, float hOffset, float vOffset,
			SafePaint paint) {
		getWrappedCanvas().drawTextOnPath(text, path, hOffset, vOffset, paint);
	}

	
	public void drawVertices(VertexMode mode, int vertexCount, double[] verts, int vertOffset,
			float[] texs, int texOffset, int[] colors, int colorOffset, short[] indices,
			int indexOffset, int indexCount, SafePaint paint) {
		getWrappedCanvas().drawVertices(mode, vertexCount, this.toOffsetFloatAry(verts, sFloatAry),
				vertOffset, texs, texOffset, colors, colorOffset, indices, indexOffset, indexCount,
				paint);
	}

	
	public boolean getClipBounds(Rect bounds) {
		boolean success = getWrappedCanvas().getClipBounds(bounds);
		if (bounds != null)
			bounds.offset(-xOffset, -yOffset);
		return success;
	}

	
	public int getDensity() {

		return getWrappedCanvas().getDensity();
	}

	
	public DrawFilter getDrawFilter() {

		return getWrappedCanvas().getDrawFilter();
	}

	
	public int getHeight() {

		return getWrappedCanvas().getHeight();
	}

	
	public void getMatrix(Matrix ctm) {

		getWrappedCanvas().getMatrix(ctm);
	}

	
	public int getSaveCount() {

		return getWrappedCanvas().getSaveCount();
	}

	
	public int getWidth() {

		return getWrappedCanvas().getWidth();
	}

	
	public boolean isOpaque() {

		return getWrappedCanvas().isOpaque();
	}

	
	public boolean quickReject(double left, double top, double right, double bottom, EdgeType type) {
		left += xOffset;
		right += xOffset;
		top += yOffset;
		bottom += yOffset;
		return getWrappedCanvas().quickReject((float) left, (float) top, (float) right,
				(float) bottom, type);
	}

	
	public boolean quickReject(SafeTranslatedPath path, EdgeType type) {
		return getWrappedCanvas().quickReject(path, type);
	}

	
	public boolean quickReject(Rect rect, EdgeType type) {

		return getWrappedCanvas().quickReject(this.toOffsetRectF(rect, sRectF), type);
	}

	
	public void restore() {

		getWrappedCanvas().restore();
	}

	
	public void restoreToCount(int saveCount) {

		getWrappedCanvas().restoreToCount(saveCount);
	}

	
	public void rotate(float degrees) {
		getWrappedCanvas().translate(this.xOffset, this.yOffset);
		getWrappedCanvas().rotate(degrees);
		getWrappedCanvas().translate(-this.xOffset, -this.yOffset);
	}

	
	public void rotate(float degrees, double px, double py) {
		getWrappedCanvas().rotate(degrees, (float) (px + xOffset), (float) (py + yOffset));
	}

	
	public int save() {

		return getWrappedCanvas().save();
	}

	
	public int save(int saveFlags) {

		return getWrappedCanvas().save(saveFlags);
	}

	
	public int saveLayer(double left, double top, double right, double bottom, SafePaint paint,
			int saveFlags) {
		return getWrappedCanvas().saveLayer((float) (left + xOffset), (float) (top + yOffset),
				(float) (right + xOffset), (float) (bottom + yOffset), paint, saveFlags);
	}

	
	public int saveLayer(Rect bounds, SafePaint paint, int saveFlags) {
		int result = getWrappedCanvas().saveLayer(this.toOffsetRectF(bounds, sRectF), paint,
				saveFlags);
		return result;
	}

	
	public int saveLayerAlpha(double left, double top, double right, double bottom, int alpha,
			int saveFlags) {
		return getWrappedCanvas().saveLayerAlpha((float) (left + xOffset), (float) (top + yOffset),
				(float) (right + xOffset), (float) (bottom + yOffset), alpha, saveFlags);
	}

	
	public int saveLayerAlpha(Rect bounds, int alpha, int saveFlags) {
		return getWrappedCanvas().saveLayerAlpha(this.toOffsetRectF(bounds, sRectF), alpha,
				saveFlags);
	}

	
	public void scale(float sx, float sy) {
		getWrappedCanvas().scale(sx, sy);
	}

	
	public void scale(float sx, float sy, double px, double py) {
		getWrappedCanvas().scale(sx, sy, (float) (px + xOffset), (float) (py + yOffset));
	}

	
	public void setBitmap(Bitmap bitmap) {
		getWrappedCanvas().setBitmap(bitmap);
	}

	
	public void setDensity(int density) {
		getWrappedCanvas().setDensity(density);
	}

	
	public void setDrawFilter(DrawFilter filter) {
		getWrappedCanvas().setDrawFilter(filter);
	}

	
	public void setMatrix(Matrix matrix) {
		getWrappedCanvas().setMatrix(matrix);
	}

	
	public void skew(float sx, float sy) {
		getWrappedCanvas().skew(sx, sy);
	}

	
	public void translate(float dx, float dy) {
		getWrappedCanvas().translate(dx, dy);
	}

	
	protected Object clone() throws CloneNotSupportedException {
		SafeTranslatedCanvas c = new SafeTranslatedCanvas();
		c.setCanvas(mCanvas);
		return c;
	}

	
	public boolean equals(Object o) {
		return getWrappedCanvas().equals(o);
	}

	
	public int hashCode() {
		return getWrappedCanvas().hashCode();
	}

	
	public String toString() {
		return getWrappedCanvas().toString();
	}

	/**
	 * Helper function to convert a Rect to RectF and adjust the values of the Rect by the offsets.
	 */
	protected final RectF toOffsetRectF(Rect rect, RectF reuse) {
		if (reuse == null)
			reuse = new RectF();

		reuse.set(rect.left + xOffset, rect.top + yOffset, rect.right + xOffset, rect.bottom
				+ yOffset);
		return reuse;
	}

	/**
	 * Helper function to convert a Rect to RectF and adjust the values of the Rect by the offsets.
	 */
	protected final float[] toOffsetFloatAry(double[] rect, float[] reuse) {
		if (reuse == null || reuse.length < rect.length)
			reuse = new float[rect.length];

		for (int a = 0; a < rect.length; a++) {
			reuse[a] = (float) (rect[a] + (a % 2 == 0 ? xOffset : yOffset));
		}
		return reuse;
	}
}
