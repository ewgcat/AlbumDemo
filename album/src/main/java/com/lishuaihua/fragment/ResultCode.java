package com.lishuaihua.fragment;


import androidx.annotation.IntDef;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


@IntDef({NoFragment.RESULT_OK, NoFragment.RESULT_CANCELED})
@Retention(RetentionPolicy.SOURCE)
public @interface ResultCode {
}
