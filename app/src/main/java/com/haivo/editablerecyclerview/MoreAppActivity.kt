package com.haivo.editablerecyclerview

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.CloneUtils
import com.blankj.utilcode.util.ToastUtils
import com.haivo.editablerecyclerview.databinding.ActivityMoreAppsBinding
import com.haivo.editablerecyclerview.helper.ItemTouchHelper
import java.lang.reflect.Type
import java.util.*

/**
 * @description: 更多应用, 包含两个recyclerView
 **/

class MoreAppActivity : AppCompatActivity() {
    private val TAG = "MoreAppActivity"
    private lateinit var binding: ActivityMoreAppsBinding
    private lateinit var commonAppsAdapter: CommonAppsAdapter
    private lateinit var allAppsAdapter: AllAppsAdapter

    companion object {
        const val ARGS_COMMON_APPS = "COMMON_APPS_LIST"
        const val ARGS_ALL_APPS = "ALL_APPS_LIST"

        @JvmStatic
        fun newIntent(commonApps: Array<AppBean>, allApps: Array<AppBean>) =
            Intent(ActivityUtils.getTopActivity(), MoreAppActivity::class.java).apply {
                putExtra(ARGS_COMMON_APPS, commonApps)
                putExtra(ARGS_ALL_APPS, allApps)
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMoreAppsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // ----------------- 列表1 常用应用------------------
        commonAppsAdapter = CommonAppsAdapter()
        binding.rvApps.apply {
            layoutManager =
                GridLayoutManager(this@MoreAppActivity, 4, LinearLayoutManager.VERTICAL, false)
            adapter = commonAppsAdapter
        }

        // ----------------- 列表2 所有应用------------------
        allAppsAdapter = AllAppsAdapter()
        binding.rvAllApps.apply {
            layoutManager =
                GridLayoutManager(this@MoreAppActivity, 4, LinearLayoutManager.VERTICAL, false)
            adapter = allAppsAdapter
        }
        setClick()
    }

    private fun setClick() {
        binding.tvEditApps.setOnClickListener {
            it.isSelected = !it.isSelected
            if (it.isSelected) {
                binding.tvEditApps.text = "完成"
                commonAppsAdapter.isInEditing = true
                binding.tvEditApps.setTextColor(Color.parseColor("#008EFF"))
                commonAppsAdapter.data.onEach { it1 ->
                    it1.option = AppBean.Companion.Option.REMOVE
                }
                commonAppsAdapter.notifyDataSetChanged()

                allAppsAdapter.isInEditing = true
                enableDragItem(true)
                allAppsAdapter.data.onEach { it2 ->
                    // 遍历列表1中元素, 如果与列表2图标相同, 则不显示+号
                    val isExistSameElement =
                        commonAppsAdapter.data.any { it4 -> it4.uid == it2.uid }
                    it2.option =
                        if (isExistSameElement) AppBean.Companion.Option.NONE else AppBean.Companion.Option.ADD
                }
                allAppsAdapter.notifyDataSetChanged()
            } else {
                binding.tvEditApps.text = "编辑"
                commonAppsAdapter.isInEditing = false
                binding.tvEditApps.setTextColor(Color.parseColor("#A0A0A0"))
                commonAppsAdapter.data.onEach { it1 -> it1.option = AppBean.Companion.Option.NONE }
                commonAppsAdapter.notifyDataSetChanged()

                allAppsAdapter.isInEditing = false
                enableDragItem(false)
                allAppsAdapter.data.onEach { it2 -> it2.option = AppBean.Companion.Option.NONE }
                allAppsAdapter.notifyDataSetChanged()
                ToastUtils.showShort("保存成功")
            }
        }
        commonAppsAdapter.onRemoveBtnClickListener = object : OnRemoveBtnClickListener {
            override fun onClick(view: View, appBean: AppBean) {
                // 找到列表2中与列表1被删除的图标相同的那个元素, 并将其重新变为可添加状态
                val theSameElementIndex =
                    allAppsAdapter.data.indexOfFirst { it.uid == appBean.uid }
                if (theSameElementIndex < 0) return
                allAppsAdapter.data[theSameElementIndex].option = AppBean.Companion.Option.ADD
                allAppsAdapter.notifyItemChanged(theSameElementIndex)
            }
        }
        allAppsAdapter.onAddBtnClickListener = object : OnAddBtnClickListener {
            override fun onClick(view: View, appBean: AppBean) {
                if (commonAppsAdapter.data.size >= commonAppsAdapter.maxCount) {
                    ToastUtils.showShort("超出最大个数限制, 无法再添加")
                    return
                }
                val newAppBean = AppBean().apply {
                    iconRes = appBean.iconRes
                    option = AppBean.Companion.Option.REMOVE
                    name = appBean.name
                    uid = appBean.uid
                }
                appBean.option = AppBean.Companion.Option.NONE
                commonAppsAdapter.data.add(newAppBean)
                commonAppsAdapter.notifyItemInserted(commonAppsAdapter.data.size)
                allAppsAdapter.notifyDataSetChanged()
            }
        }
    }

    private fun enableDragItem(enable: Boolean) {
        if (enable) {
            val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.Callback() {
                override fun getMovementFlags(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder
                ): Int {
                    return makeMovementFlags(
                        ItemTouchHelper.UP or ItemTouchHelper.DOWN or ItemTouchHelper.START or ItemTouchHelper.END,
                        0
                    )
                }

                override fun onMove(
                    recyclerView: RecyclerView,
                    oldHolder: RecyclerView.ViewHolder,
                    targetHolder: RecyclerView.ViewHolder
                ): Boolean {
                    commonAppsAdapter.notifyItemMoved(
                        oldHolder.adapterPosition,
                        targetHolder.adapterPosition
                    )
                    // 在每次移动后, 将界面上图标的顺序同步到appsAdapter.data中
//                    val newData = mutableListOf<Pair<String, Int>>()
//                    commonAppsAdapter.data.forEachIndexed { index, _ ->
//                        val holder =
//                            recyclerView.findViewHolderForAdapterPosition(index) as AppsHolder
//                        newData.add(Pair(holder.funcUrl, index))
//                    }
//                    for (i in newData) {
//                        val sameFuncIndex =
//                            commonAppsAdapter.data.indexOfFirst { i.first == it.uid }
//                        Collections.swap(commonAppsAdapter.data, i.second, sameFuncIndex)
//                    }
                    return true
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {}

                override fun canDropOver(
                    recyclerView: RecyclerView,
                    current: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ) = true

                override fun isLongPressDragEnabled() = false

                override fun onMerge(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ) {
                    Log.i(TAG, "onMerge: $viewHolder $target")
                    val removedIndex = viewHolder.adapterPosition
                    val targetIndex = target.adapterPosition
                    val targetAppBean = commonAppsAdapter.data[targetIndex]
                    if (targetAppBean.subAppBeans.size < 3) {
                        targetAppBean.subAppBeans.add(commonAppsAdapter.data[removedIndex])
                        targetAppBean.subAppBeans.add(targetAppBean)
                        commonAppsAdapter.data.removeAt(removedIndex)
                        commonAppsAdapter.notifyDataSetChanged()
                    }
                }
            })

            commonAppsAdapter.dragOverListener = object : DragOverListener {
                override fun startDragItem(holder: RecyclerView.ViewHolder) {
                    itemTouchHelper.startDrag(holder)
                }
            }
            itemTouchHelper.attachToRecyclerView(binding.rvApps)
        } else {
            commonAppsAdapter.dragOverListener = null
        }
    }

    override fun onResume() {
        super.onResume()
        val commonAppsArray = intent.getParcelableArrayExtra(ARGS_COMMON_APPS)
        if (!commonAppsArray.isNullOrEmpty()) {
            commonAppsAdapter.data = commonAppsArray.map {
                it as AppBean
                it.option = AppBean.Companion.Option.NONE
                it
            }.toMutableList()
        }
        val allAppsArray = intent.getParcelableArrayExtra(ARGS_ALL_APPS)
        if (!allAppsArray.isNullOrEmpty()) {
            allAppsAdapter.data = allAppsArray.map {
                it as AppBean
                it.option = AppBean.Companion.Option.NONE
                it
            }.toMutableList()
        }
    }
}