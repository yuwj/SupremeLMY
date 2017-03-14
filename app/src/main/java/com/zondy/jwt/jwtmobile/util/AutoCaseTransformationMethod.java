package com.zondy.jwt.jwtmobile.util;

import android.text.method.ReplacementTransformationMethod;

/**
 * Created by sheep on 2017/3/14.
 */

public class AutoCaseTransformationMethod extends ReplacementTransformationMethod {
    /**
     * 获取要改变的字符。
     * @return 将你希望被改变的字符数组返回。
     */
    @Override
    protected char[] getOriginal() {
        return new char[]{'a', 'b', 'c', 'd', 'e',
                'f', 'g', 'h', 'i', 'j', 'k', 'l',
                'm', 'n', 'o', 'p', 'q', 'r', 's',
                't', 'u', 'v', 'w', 'x', 'y', 'z'};
    }

    /**
     * 获取要替换的字符。
     * @return 将你希望用来替换的字符数组返回。
     */
    @Override
    protected char[] getReplacement() {
        return new char[]{ 'A', 'B', 'C', 'D', 'E',
                'F', 'G', 'H', 'I', 'J','K','L','M',
                'N','O','P','Q','R','S','T','U','V','W','X','Y','Z' };
    }
}