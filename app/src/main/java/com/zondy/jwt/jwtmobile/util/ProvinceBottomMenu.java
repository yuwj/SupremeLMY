package com.zondy.jwt.jwtmobile.util;

import android.app.Activity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.zondy.jwt.jwtmobile.R;
import com.zondy.jwt.jwtmobile.view.impl.XunlpcActivity;

import static com.zondy.jwt.jwtmobile.R.id.btn1;
import static com.zondy.jwt.jwtmobile.R.id.btn2;

/**
 * Created by sheep on 2017/2/27.
 */

public class ProvinceBottomMenu implements View.OnClickListener, View.OnTouchListener {

    private PopupWindow popupWindow;
    private View mMenuView;
    private Activity mContext;
    private View.OnClickListener listener;

    public ProvinceBottomMenu(Activity context, View.OnClickListener listener, TextView textView) {
        LayoutInflater inflater = LayoutInflater.from(context);
        this.listener=listener;
        mContext = context;
        mMenuView = inflater.inflate(R.layout.province_popup_bottommenu, null);
        popupWindow=new PopupWindow(mMenuView, ViewGroup.LayoutParams.MATCH_PARENT, 530,true);
        popupWindow.setAnimationStyle(R.style.popwin_anim_style);
        mMenuView.setOnTouchListener(this);
        KeyboardUtil keyboardUtil = new KeyboardUtil(mContext,mMenuView, textView,popupWindow);
        keyboardUtil.showKeyboard();
    }


    /**
     * 显示菜单
     */
    public void show(){
        //得到当前activity的rootView
        View rootView=((ViewGroup)mContext.findViewById(android.R.id.content)).getChildAt(0);
        popupWindow.showAtLocation(rootView, Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            default:
                listener.onClick(v);
                popupWindow.dismiss();
                break;
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int height = mMenuView.findViewById(R.id.pop_layout).getTop();
        int y=(int) event.getY();
        if(event.getAction()==MotionEvent.ACTION_UP){
            if(y<height){
                popupWindow. dismiss();
            }
        }
        return true;
    }
}
