package me.xiaobailong24.mvvmarms.weather.mvvm.view.fragment;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import me.xiaobailong24.mvvmarms.base.ArmsFragment;
import me.xiaobailong24.mvvmarms.weather.R;
import me.xiaobailong24.mvvmarms.weather.app.EventBusTags;
import me.xiaobailong24.mvvmarms.weather.databinding.FragmentWeatherNowBinding;
import me.xiaobailong24.mvvmarms.weather.mvvm.model.pojo.TextContent;
import me.xiaobailong24.mvvmarms.weather.mvvm.view.adapter.TextContentAdapter;
import me.xiaobailong24.mvvmarms.weather.mvvm.viewModel.WeatherNowViewModel;
import timber.log.Timber;


/**
 * Created by xiaobailong24 on 2017/7/15.
 */

public class WeatherNowFragment extends ArmsFragment<FragmentWeatherNowBinding, WeatherNowViewModel> {

    private static final String LOCATION_KEY = "location";

    private TextContentAdapter mAdapter;
    private LiveData<List<TextContent>> mWeatherData;

    public static WeatherNowFragment newInstance(String location) {
        WeatherNowFragment weatherNowFragment = new WeatherNowFragment();
        Bundle args = new Bundle();
        args.putString(LOCATION_KEY, location);
        weatherNowFragment.setArguments(args);
        return weatherNowFragment;
    }


    @Override
    public View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mViewModel = ViewModelProviders.of(this, mViewModelFactory).get(WeatherNowViewModel.class);
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_weather_now, container, false);
        mBinding.setViewModel(mViewModel);//设置ViewModel
        //RecyclerView设置Adapter
        mAdapter = new TextContentAdapter(R.layout.super_text_item, null);
        mBinding.recyclerWeatherNow.setAdapter(mAdapter);
        //设置Refresh
        mBinding.refresh.setColorSchemeColors(
                ContextCompat.getColor(getContext(), R.color.colorPrimary),
                ContextCompat.getColor(getContext(), R.color.colorAccent),
                ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));

        return mBinding.getRoot();
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        if (savedInstanceState == null)
            observerWeather(getArguments().getString(LOCATION_KEY));
    }

    @Override
    public void setData(Object data) {
        Message message = (Message) data;
        Timber.i("setData: message.what--->" + message.what +
                ", message.obj--->" + message.obj);
        switch (message.what) {
            case EventBusTags.FRAGMENT_MESSAGE_WEATHER_NOW:
                observerWeather(String.valueOf(message.obj));
                break;
            default:
                break;
        }
    }

    //调用ViewModel的方法获取天气
    private void observerWeather(String location) {
        if (mWeatherData != null)//防止重复订阅
            mWeatherData.removeObservers(this);
        //如果位置是全路径，则截取城市名
        if (location.contains(","))
            location = location.substring(0, location.indexOf(","));
        mWeatherData = mViewModel.getWeatherNow(location);
        mWeatherData.observe(this, textContents -> {
            mAdapter.replaceData(textContents);
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mWeatherData = null;
        this.mAdapter = null;
    }
}
