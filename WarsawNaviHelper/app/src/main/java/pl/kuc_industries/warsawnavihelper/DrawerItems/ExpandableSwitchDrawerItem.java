package pl.kuc_industries.warsawnavihelper.DrawerItems;

import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.model.AbstractSwitchableDrawerItem;

/**
 * Created by mateusz on 4/28/17.
 */

public class ExpandableSwitchDrawerItem extends AbstractSwitchableDrawerItem<ExpandableSwitchDrawerItem> {

    @Override
    public ExpandableSwitchDrawerItem withOnDrawerItemClickListener(Drawer.OnDrawerItemClickListener onDrawerItemClickListener) {
        return this;
    }
}
