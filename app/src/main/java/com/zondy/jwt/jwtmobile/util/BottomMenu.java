package com.zondy.jwt.jwtmobile.util;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;

import com.zondy.jwt.jwtmobile.R;

/**
 * Created by sheep on 2017/2/27.
 */

public class BottomMenu implements View.OnClickListener, View.OnTouchListener {

    private PopupWindow popupWindow;
    private Button btn1, btn2, btnCancel;
    private View mMenuView;
    private Activity mContext;
    private View.OnClickListener listener;

    public BottomMenu(Activity context, View.OnClickListener listener) {
        LayoutInflater inflater = LayoutInflater.from(context);
        this.listener=listener;
        mContext = context;
        mMenuView = inflater.inflate(R.layout.popwindow_bottommenu, null);
        btn1 = (Button) mMenuView.findViewById(R.id.btn1);
        btn2 = (Button) mMenuView.findViewById(R.id.btn2);
        btnCancel = (Button) mMenuView.findViewById(R.id.btn_cancel);
        btnCancel.setOnClickListener(this);
        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        popupWindow=new PopupWindow(mMenuView, ViewGroup.LayoutParams.MATCH_PARENT, 450,true);
        popupWindow.setAnimationStyle(R.style.popwin_anim_style);
        mMenuView.setOnTouchListener(this);
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
            case R.id.btn_cancel:
                popupWindow.dismiss();
                break;
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
