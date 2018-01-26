package homeautomation.circularblue.com.iit;

import android.view.View;

/**
 * Created by rohan on 26-01-2018.
 */

public interface ListItem {
    int getVisibilityPercents(View view);
    void setActive(View newActiveView, int newActiveViewPosition);
    void deactivate(View currentView, int position);
}