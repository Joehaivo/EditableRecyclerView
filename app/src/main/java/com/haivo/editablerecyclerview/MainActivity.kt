package com.haivo.editablerecyclerview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.haivo.editablerecyclerview.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val commonApps = arrayOf(
            AppBean().apply {
                uid = "func_uid_001"
                name = "人车查询"
            },
            AppBean().apply {
                uid = "func_uid_002"
                name = "访客登记"
            },
            AppBean().apply {
                uid = "func_uid_003"
                name = "事件上报"
            },
            AppBean().apply {
                uid = "func_uid_004"
                name = "通行证"
            },
            AppBean().apply {
                uid = "func_uid_005"
                name = "云打印"
            },
            AppBean().apply {
                uid = "func_uid_006"
                name = "最多跑一次"
            },
        )

        val allApps = arrayOf(
            AppBean().apply {
                uid = "func_uid_001"
                name = "人车查询"
            },
            AppBean().apply {
                uid = "func_uid_002"
                name = "访客登记"
            },
            AppBean().apply {
                uid = "func_uid_003"
                name = "事件上报"
            },
            AppBean().apply {
                uid = "func_uid_004"
                name = "通行证"
            },
            AppBean().apply {
                uid = "func_uid_005"
                name = "云打印"
            },
            AppBean().apply {
                uid = "func_uid_006"
                name = "最多跑一次"
            },
            AppBean().apply {
                uid = "func_uid_007"
                name = "垃圾分类"
            },
            AppBean().apply {
                uid = "func_uid_008"
                name = "健康码"
            },
            AppBean().apply {
                uid = "func_uid_009"
                name = "访客预约"
            },
            AppBean().apply {
                uid = "func_uid_010"
                name = "园区看护"
            },
            AppBean().apply {
                uid = "func_leadership_view"
                name = "领导视图"
            },
            AppBean().apply {
                uid = "func_query_parking"
                name = "车位查询"
            },
        )

        binding.tvMoreApp.setOnClickListener {
            startActivity(MoreAppActivity.newIntent(commonApps, allApps))
        }
    }
}