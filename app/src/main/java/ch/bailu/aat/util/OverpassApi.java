package ch.bailu.aat.util;

import android.content.Context;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Locale;

import ch.bailu.aat.R;
import ch.bailu.aat.coordinates.BoundingBoxE6;
import ch.bailu.aat.util.fs.AppDirectory;
import ch.bailu.util_java.foc.Foc;

public class OverpassApi extends OsmApiHelper {

    public final String NAME;
    public final static String EXT=".osm";
    public final static String URL="http://overpass-api.de/api/interpreter?data=("; // data=node
    public final static String POST=">;);out;";

    
    private final String bounding;
    private final Foc directory;


    
    public OverpassApi(Context context, BoundingBoxE6 b) throws SecurityException, IOException {
        NAME=context.getString(R.string.query_overpass);
        bounding = toString(b);
        directory = AppDirectory.getDataDirectory(context, AppDirectory.DIR_OVERPASS);
    }

    
    @Override
    public String getApiName() {
        return NAME;
    }

    
    
    private static String toString(BoundingBoxE6 bounding) {
        final double lo1 = bounding.getLonWestE6()/1E6;
        final double la1 = bounding.getLatSouthE6()/1E6;
        final double lo2 = bounding.getLonEastE6()/1E6;
        final double la2 = bounding.getLatNorthE6()/1E6;

        return String.format(Locale.ROOT,"(%.2f,%.2f,%.2f,%.2f);",
                Math.min(la1, la2),
                Math.min(lo1, lo2),
                Math.max(la1, la2),
                Math.max(lo1, lo2));
    }


    /**
     * See: http://overpass-api.de/command_line.html
     * @throws UnsupportedEncodingException 
     */    
    public String getUrl(String query) throws UnsupportedEncodingException {
        final String[] queries = query.split(";");

        final StringBuilder url = new StringBuilder();
        url.setLength(0);
        url.append(URL);

        for (String q : queries) {
            q = q.trim();

            if (q.length() > 0) {
                if (q.charAt(0) == '[') {
                    url.append("node");
                    url.append(URLEncoder.encode(q, "UTF-8"));
                    url.append(URLEncoder.encode(bounding, "UTF-8"));

                    url.append("rel");
                    url.append(URLEncoder.encode(q, "UTF-8"));
                    url.append(URLEncoder.encode(bounding, "UTF-8"));

                    url.append("way");
                    url.append(URLEncoder.encode(q, "UTF-8"));
                    url.append(URLEncoder.encode(bounding, "UTF-8"));
                } else {
                    url.append(URLEncoder.encode(q, "UTF-8"));
                    url.append(URLEncoder.encode(bounding, "UTF-8"));
                }
            }

        }
        
        url.append(URLEncoder.encode(POST,"UTF-8"));
        
        return url.toString();
    }

    @Override
    public String getUrlPreview(String query) {
        final String[] queries = query.split(";");

        final StringBuilder url = new StringBuilder();
        url.setLength(0);
        url.append(URL);

        url.append('\n');
        for (String q : queries) {
            q = q.trim();

            if (q.length() > 0) {
                if (q.charAt(0) == '[') {
                    url.append("node");
                    url.append(q);
                    url.append(bounding);
                    url.append('\n');

                    url.append("rel");
                    url.append(q);
                    url.append(bounding);
                    url.append('\n');

                    url.append("way");
                    url.append(q);
                    url.append(bounding);
                    url.append('\n');

                } else {
                    url.append(q);
                    url.append(bounding);
                    url.append('\n');
                }
            }

        }

        url.append(POST);

        return url.toString();
    }

    
    @Override
    public String getUrlStart() {
        return URL+"...";
    }

/*
    @Override
    public String getUrlEnd() {
        return POST;
    }
*/

    @Override
    public Foc getBaseDirectory() {
        return directory;
    }

    @Override
    public String getFileExtension() {
        return EXT;
    }

}
