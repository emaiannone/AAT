package ch.bailu.aat.services.background;

import android.content.Context;
import android.support.annotation.NonNull;

import ch.bailu.util_java.foc.Foc;

public abstract class FileTask extends BackgroundTask {
    private final Foc file;

    private Tasks tasks = null;


    public FileTask(Foc f) {
        file = f;
    }


    @NonNull
    @Override
    public String toString() {
        return file.toString();
    }

    public Foc getFile() {
        return file;
    }

    public String getID() {
        return file.getPath();
    }


    public void register(Tasks t) {
        if (tasks == null)
            tasks = t;
    }


    @Override
    public void onInsert(Context c) {
        if (tasks != null) tasks.add(c, this);
    }


    @Override
    public void onRemove(Context c) {
        if (tasks != null) {
            tasks.remove(c, this);
        }
    }
}
