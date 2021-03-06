package me.xiaobailong24.mvvmarms.base;

import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.LifecycleRegistryOwner;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import me.xiaobailong24.mvvmarms.base.delegate.IFragment;

/**
 * Created by xiaobailong24 on 2017/6/16.
 * MVVM BaseFragment
 */
public abstract class BaseFragment extends Fragment
        implements IFragment, LifecycleRegistryOwner {
    protected final String TAG = this.getClass().getName();

    //LifecycleRegistryOwner
    private LifecycleRegistry mLifecycleRegistry = new LifecycleRegistry(this);

    public BaseFragment() {
        setArguments(new Bundle());
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return initView(inflater, container, savedInstanceState);
    }

    public boolean useEventBus() {
        return true;
    }

    @Override
    public boolean injectable() {
        return true;//默认进行依赖注入
    }

    @Override
    public LifecycleRegistry getLifecycle() {
        return this.mLifecycleRegistry;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mLifecycleRegistry = null;
    }
}
