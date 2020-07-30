package com.lishuaihua.albumdemo

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.lishuaihua.durban.Durban
import com.tbruyelle.rxpermissions2.Permission
import com.tbruyelle.rxpermissions2.RxPermissions
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File

class MainActivity : AppCompatActivity() {
    internal var permissions = arrayOf(
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CHANGE_NETWORK_STATE,
            Manifest.permission.CHANGE_WIFI_STATE,
            Manifest.permission.ACCESS_WIFI_STATE)
    /**
     * 运行时请求权限
     */
    private fun initRxPermissions(i: Int, permissions: Array<String>) {
        val rxPermissions = RxPermissions(this)
        rxPermissions.requestEach(*permissions)
                .compose<Permission>(RxUtil.rxObservableSchedulerHelper<Permission>())
                .subscribe(
                        Consumer { pemission ->
                            //获取单个权限成功，会多次 执行
                            pemission.granted
                        },
                        Consumer { error ->
                            Log.d("error","error")
                        },
                        Action {
                            //全部完成的回调，只执行一次
                            Handler().postDelayed(Runnable {    },2000)
                        })
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initRxPermissions(0,permissions)
        bt.setOnClickListener {
            DialogUtils.showBottomDialog(this@MainActivity, 1)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            AppConstants.LOCAL_IMAGE -> {
                if (resultCode == Activity.RESULT_OK) {
                    val arrayList = Durban.parseResult(data!!)
                    if (arrayList != null) {
                        if (arrayList.size > 0) {
                            val file = File(arrayList[0])
                            Log.d("file",file.absolutePath)
                            runOnUiThread {
                                Glide.with(this@MainActivity).load(file).into(iv)
                            }
                        }
                    }
                }
            }
        }
    }
}