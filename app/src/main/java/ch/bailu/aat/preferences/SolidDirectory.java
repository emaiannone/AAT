package ch.bailu.aat.preferences;

import android.content.Context;
import ch.bailu.aat.services.directory.GpxDbConstants;

public class SolidDirectory extends SolidString {
    private static final String KEY_DIR_DIRECTORY="DIR_DIRECTORY";
    private static final String KEY_DIR_INDEX="DIR_INDEX_";

    private final static String KEY_DATE_START="DATE_START_";
    private final static String KEY_DATE_END="DATE_END_";
    private final static String KEY_USE_DATE_START="USE_DATE_START_";
    private final static String KEY_USE_DATE_END="USE_DATE_END_";

    private final static String KEY_USE_GEO="USE_GEO_";

    private final static String KEY_BOUNDING_BOX = "BOX_";
    

    public SolidDirectory(Context c) {
        super(Storage.global(c), KEY_DIR_DIRECTORY);

    }

    public boolean containsKey(String s) {
        return s.contains(getValueAsString());
    }
    
    public SolidInteger getPosition() {
        return new SolidInteger(getStorage(), KEY_DIR_INDEX+ getValueAsString());
    }


    public SolidBoolean getUseDateStart() {
        return new SolidBoolean(getStorage(), KEY_USE_DATE_START+ getValueAsString());
    }


    public SolidBoolean getUseDateEnd() {
        return new SolidBoolean(getStorage(), KEY_USE_DATE_END+ getValueAsString());
    }


    public SolidLong getDateStart() {
        return new SolidLong(getStorage(), KEY_DATE_START+ getValueAsString());
    }


    public SolidLong getDateEnd() {
        return new SolidLong(getStorage(), KEY_DATE_END+ getValueAsString());
    }

    public SolidBoolean getUseGeo() {
        return new SolidBoolean(getStorage(), KEY_USE_GEO+ getValueAsString());

    }

    public SolidBoundingBox getBoundingBox() {
        return new SolidBoundingBox(getStorage(), KEY_BOUNDING_BOX+ getValueAsString());
    }

    
    
    public String createSelectionString() {
        final String d = createSelectionStringDate();
        final String b = createSelectionStringBounding(); 
        
        if (d.length()>0 && b.length()>0) {
            return "(" + d + ") AND (" + b + ")";
        } else {
            return d + b;
        }
    }
    
    
    public String createSelectionStringBounding() {
        if (getUseGeo().getValue()) {
            return getBoundingBox().createSelectionStringInside();
        }
        
        return "";
    }
    
    
    private final static long DAY = 1000*60*60*24; // ms*sec*min*h = d
    public String createSelectionStringDate() {
        String selection="";


        if (getUseDateStart().getValue() || getUseDateEnd().getValue()) {
            long start, end;

            if (getUseDateStart().getValue()) {
                start = getDateStart().getValue();
            } else {
                start = 0;
            }

            if (getUseDateEnd().getValue()) {
                end = getDateEnd().getValue();
            } else {
                end = System.currentTimeMillis()/DAY;
                end = end + 5;
                end = end * DAY;
            }


            selection = GpxDbConstants.KEY_START_TIME 
                    + " BETWEEN " 
                    + String.valueOf(Math.min(start, end)) 
                    + " AND " 
                    + String.valueOf(Math.max(start, end)) ;
        }

        return selection;
    }
}



