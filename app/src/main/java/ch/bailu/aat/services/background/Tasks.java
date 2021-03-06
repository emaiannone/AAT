package ch.bailu.aat.services.background;


import android.content.Context;

import java.util.ArrayList;

import ch.bailu.aat.util.AppBroadcaster;
import ch.bailu.util_java.foc.Foc;
import ch.bailu.util_java.util.Objects;

public final class Tasks {

    private final ArrayList<FileTask> downloads = new ArrayList<>(10);

    public Tasks() {}


    public synchronized void add(Context c, FileTask t) {
        if (!contains(t)) {
            downloads.add(t);
            changed(c, t);
        }
    }


    public boolean contains(FileTask t) {
        return contains(t.getFile());
    }

    public synchronized FileTask get(Foc file) {
        for (FileTask t : downloads) {
            if (Objects.equals(t.getFile(), file)) {
                return t;
            }
        }
        return null;

    }

    public synchronized boolean contains(Foc file) {
        return get(file) != null;
    }


    public synchronized void remove(Context c, FileTask t) {
        if (downloads.remove(t)) {
            changed(c, t);
        }
    }

    private synchronized void changed(Context c, FileTask t) {

        if (t instanceof DownloadTask) {
            AppBroadcaster.broadcast(c,
                    AppBroadcaster.FILE_BACKGROND_TASK_CHANGED,
                    t.getFile(),
                    ((DownloadTask) t).getSource());

        } else {
            AppBroadcaster.broadcast(c,
                    AppBroadcaster.FILE_BACKGROND_TASK_CHANGED, t.getFile());
        }
    }
}
