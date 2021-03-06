package ch.bailu.aat.map;

import android.graphics.Rect;

import org.mapsforge.core.model.BoundingBox;
import org.mapsforge.core.model.LatLong;

import ch.bailu.aat.coordinates.BoundingBoxE6;
import ch.bailu.aat.coordinates.LatLongE6Interface;
import ch.bailu.aat.util.graphic.Pixel;
import ch.bailu.aat.util.ui.AppDensity;

public interface MapMetrics {
    AppDensity getDensity();

    int getLeft();
    int getRight();
    int getTop();
    int getBottom();
    int getWidth();
    int getHeight();


    float pixelToDistance(int pixel);
    int distanceToPixel(float meter);
    int getShortDistance();

    Pixel getCenterPixel();

    boolean isVisible(BoundingBoxE6 box);
    boolean isVisible(LatLongE6Interface point);
    Rect toMapPixels(BoundingBoxE6 box);
    Pixel toPixel(LatLongE6Interface tp);
    Pixel toPixel(LatLong p);
    LatLong fromPixel(int x, int y);

    BoundingBox getBoundingBox();

    int getZoomLevel();
}
