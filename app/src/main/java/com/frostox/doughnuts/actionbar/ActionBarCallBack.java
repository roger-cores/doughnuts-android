package com.frostox.doughnuts.actionbar;

import android.support.v7.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;

import com.frostox.doughnuts.R;

/**
 * Created by roger on 11/1/2016.
 */
public class ActionBarCallBack implements android.view.ActionMode.Callback {

    @Override
    public boolean onActionItemClicked(android.view.ActionMode mode, MenuItem item) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean onCreateActionMode(android.view.ActionMode mode, Menu menu) {
        // TODO Auto-generated method stub
        mode.getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public void onDestroyActionMode(android.view.ActionMode mode) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean onPrepareActionMode(android.view.ActionMode mode, Menu menu) {
        // TODO Auto-generated method stub

        mode.setTitle("CheckBox is Checked");
        return false;
    }

}