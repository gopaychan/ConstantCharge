package com.gopaychan.constantcharge;

import android.os.Bundle;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;

import butterknife.BindView;

public class MainActivity extends BaseActivity {

    @BindView(R.id.mainBottomNavigationBarId)  BottomNavigationBar mBottomNavigationBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void initView(){
        mBottomNavigationBar.setMode(BottomNavigationBar.MODE_DEFAULT);
        mBottomNavigationBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_RIPPLE);
//        mBottomNavigationBar.setBarBackgroundColor(R.color.colorTheme);
        mBottomNavigationBar.addItem(new BottomNavigationItem(R.drawable.bottom_navigation_bar_img1, R.string.bottom_navigation_bar_txt1).setActiveColorResource(R.color.colorTheme))
                .addItem(new BottomNavigationItem(R.drawable.bottom_navigation_bar_img2, R.string.bottom_navigation_bar_txt2).setActiveColorResource(R.color.colorTheme))
                .addItem(new BottomNavigationItem(R.drawable.bottom_navigation_bar_img3, R.string.bottom_navigation_bar_txt3).setActiveColorResource(R.color.colorTheme))
                .addItem(new BottomNavigationItem(R.drawable.bottom_navigation_bar_img5, R.string.bottom_navigation_bar_txt5))//依次添加item,分别icon和名称
                .setFirstSelectedPosition(0)//设置默认选择item
                .initialise();//初始化
    }
}
