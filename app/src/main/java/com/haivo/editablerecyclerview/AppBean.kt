package com.haivo.editablerecyclerview

import android.content.Intent
import android.os.Parcel
import android.os.Parcelable
import com.blankj.utilcode.util.ActivityUtils


/**
 * @description: 应用bean
 **/
class AppBean() : Parcelable {
    var subAppBeans: MutableList<AppBean> = mutableListOf()
    var name: String = ""
    var uid: String = ""
    var iconRes: Int = -1
        get() = if (getFunctionDrawable(uid) != -1) getFunctionDrawable(uid) else R.drawable.photobadge

    // 图标目前所处的显示状态
    var option: Option = Option.NONE
    // 图标跳转
    var action: Intent? = null
        get() = getFunctionActionByUrl(uid)

    constructor(parcel: Parcel) : this() {
        name =parcel.readString() ?: ""
        uid = parcel.readString() ?: ""
        iconRes = parcel.readInt()
        action = parcel.readParcelable(Intent::class.java.classLoader)
    }

    companion object {
        // item当前的操作状态
        enum class Option {
            ADD, REMOVE, NONE
        }

        @JvmField
        val CREATOR = object : Parcelable.Creator<AppBean> {
            override fun createFromParcel(parcel: Parcel): AppBean {
                return AppBean(parcel)
            }

            override fun newArray(size: Int): Array<AppBean?> {
                return arrayOfNulls(size)
            }
        }
        
        fun getFunctionDrawable(appUid: String): Int {
            val map = mapOf(
                    Pair("func_uid_001", R.drawable.ic_person_car_query), // 人车查询
                    Pair("func_uid_002", R.drawable.ic_visitor_record), // 访客登记
                    Pair("func_uid_003", R.drawable.ic_event_report), // 事件上报
                    Pair("func_uid_004", R.drawable.ic_passport), // 通行证
                    Pair("func_uid_005", R.drawable.ic_cloud_print), // 云打印
                    Pair("func_uid_006", R.drawable.ic_most_once), // 最多跑一次
                    Pair("func_uid_007", R.drawable.ic_garbage), // 垃圾分类
                    Pair("func_uid_008", R.drawable.ic_health_code), // 健康码
                    Pair("func_uid_009", R.drawable.ic_visitor_order), //  访客预约
                    Pair("func_uid_010", R.drawable.ic_park_monitor), // 园区看护
                    Pair("func_uid_011", R.drawable.ic_wiki), // 物管知识库
                    Pair("func_uid_012", R.drawable.ic_survy), // 调查问卷
                    Pair("func_uid_013", R.drawable.ic_emergence_report), // 应急上报
                    Pair("func_leadership_view", R.drawable.ic_leadership_view), // 领导视图
                    Pair("func_query_parking", R.drawable.ic_query_parking) // 车位查询
            )
            return map[appUid] ?: -1
        }
    
        fun getFunctionActionByUrl(funcUrl: String): Intent? {
            val topActivity = ActivityUtils.getTopActivity()
            val map = mapOf(
                    // 应用部分
                    Pair("func_uid_001", Intent(topActivity, QueryPersonCarActivity::class.java)),
                    Pair("func_uid_002", null),
                    Pair("func_uid_003", null),
                    Pair("func_uid_004", null),
                    Pair("func_uid_005", null),
                    Pair("func_uid_006", null),
                    Pair("func_uid_007", null),
                    Pair("func_uid_008", null),
                    Pair("func_uid_009", null),
                    Pair("func_uid_010", null),
                    Pair("func_uid_011", null),
                    Pair("func_uid_012", null),
                    Pair("func_uid_013", null),
                    Pair("func_leadership_view", null),
                    Pair("func_query_parking", null),

                    Pair("func_uid_101",null),
                    Pair("func_uid_102", null),
                    Pair("func_uid_103", null),
                    Pair("func_uid_104", null),
            )
            return map[funcUrl]
        }
    
        // 由于接口返回中, 有icon和url两个字段, 所以icon匹配不到的话再匹配url
//        fun getFunctionActionByIcon(icon: String): Intent? {
//            val map = mapOf(
//                    Pair("MSG_ICON_PATROL", PatrolListActivity.newIntent()), // 电子巡更
//                    Pair("MSG_ICON_INSPECTION", OpListActivity.newIntent(OpListActivity.UI_TYPE_INSPECTION)), // 设备巡检
//                    Pair("MSG_ICON_MAINTENANCE", OpListActivity.newIntent(OpListActivity.UI_TYPE_MAINTENANCE)), // 设备维保
//            )
//            return map[icon]
//        }
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(uid)
        parcel.writeInt(iconRes)
        parcel.writeParcelable(action, flags)
    }

    override fun describeContents(): Int {
        return 0
    }
}