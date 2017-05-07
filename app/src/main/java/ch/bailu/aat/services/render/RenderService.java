package ch.bailu.aat.services.render;

import android.content.SharedPreferences;

import org.mapsforge.core.graphics.TileBitmap;
import org.mapsforge.core.model.Tile;
import org.mapsforge.map.rendertheme.XmlRenderTheme;

import java.io.File;
import java.util.ArrayList;

import ch.bailu.aat.preferences.SolidMapsForgeDirectory;
import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.services.VirtualService;
import ch.bailu.aat.services.cache.MapsForgeTileObject;

public class RenderService  extends VirtualService
        implements SharedPreferences.OnSharedPreferenceChangeListener {

    private final Cache cache = new Cache();

    private final SolidMapsForgeDirectory sdirectory;

    private MapList mapList;
    private RendererList rendererList;


    public RenderService(ServiceContext sc) {
        super(sc);

        sdirectory = new SolidMapsForgeDirectory(sc.getContext());

        rendererList = new RendererList(cache);


        mapList = new MapList(sdirectory.getValueAsFile());

        sdirectory.getStorage().register(this);
    }



    public TileBitmap getTile(Tile tile, XmlRenderTheme theme) {
        ArrayList<File> files = mapList.getFiles(tile);
        return rendererList.getTile(files, tile, theme);
    }


    public void lockToCache(MapsForgeTileObject o) {
        cache.lockToCache(o);
    }
    public void freeFromCache(MapsForgeTileObject o) {
        cache.freeFromCache(o);
    }



    @Override
    public void appendStatusText(StringBuilder builder) {

    }

    @Override
    public void close() {
        sdirectory.getStorage().unregister(this);
        rendererList.destroy();
        cache.destroy();
    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (sdirectory.hasKey(key)) {
            mapList = new MapList(sdirectory.getValueAsFile());
        }
    }
}
