package com.grantgz.listfragment;

import android.view.View;
import androidx.fragment.app.Fragment;

public class BaseFragment extends Fragment implements View.OnClickListener  {
    protected String TAG = this.getClass().getSimpleName();
    protected String IDENTIFY = String.valueOf(hashCode());


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    protected void setOnClickListener(View[] views) {
        for (View v : views) {
            v.setOnClickListener(this);
        }
    }

    protected void setOnClickListener(int[] ids) {
        View view = getView();
        if (view == null)
            return;
        for (int id : ids) {
            view.findViewById(id).setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onStart() {
        super.onStart();
        if (!isHidden())
            onShow();
    }

    @Override
    public void onStop() {
        super.onStop();
        onHide();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            onHide();
        } else {
            onShow();
        }
    }

    protected void onShow() {

    }

    protected void onHide() {

    }
}
