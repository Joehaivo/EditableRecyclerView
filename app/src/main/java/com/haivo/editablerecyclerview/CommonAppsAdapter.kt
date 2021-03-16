package com.haivo.editablerecyclerview

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.ActivityUtils
import com.haivo.editablerecyclerview.databinding.ItemHomeAppsBinding


/**
 * @description: 常用应用列表适配器
 **/
class CommonAppsAdapter : RecyclerView.Adapter<AppsHolder>() {
    private val TAG = "CommonAppsAdapter"
    
    // 最大item个数, 超出个数不予显示
    var maxCount = 8
    var onRemoveBtnClickListener: OnRemoveBtnClickListener? = null
    var isInEditing = false //是否处于编辑状态, 编辑状态时点击不能跳转
    
    @Volatile
    var data: MutableList<AppBean> = mutableListOf()
        set(value) {
            field = if (value.size > maxCount) {
                Log.e(TAG, "the data size is more than maxCount(${maxCount})")
                value.take(maxCount).toMutableList()
            } else {
                value
            }
            notifyDataSetChanged()
        }
    var dragOverListener: DragOverListener? = null
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppsHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_home_apps, parent, false)
        return AppsHolder(view)
    }
    
    override fun getItemCount(): Int {
        return if (data.size > maxCount) maxCount else data.size
    }
    
    @SuppressLint("ClickableViewAccessibility")
    override fun onBindViewHolder(holder: AppsHolder, position: Int) {
        val appBean = data[holder.adapterPosition]
        holder.binding.ivAppIcon.setImageResource(appBean.iconRes)
        holder.binding.ivOption.setImageResource(when (appBean.option) {
            AppBean.Companion.Option.ADD -> {
                holder.binding.ivOption.visibility = View.VISIBLE
                R.drawable.ic_add
            }
            AppBean.Companion.Option.REMOVE -> {
                holder.binding.ivOption.visibility = View.VISIBLE
                holder.binding.ivOption.setOnClickListener {
                    onRemoveBtnClickListener?.onClick(it, appBean)
                    data.removeAt(holder.adapterPosition)
                    notifyItemRemoved(holder.adapterPosition)
                }
                R.drawable.ic_remove
            }
            AppBean.Companion.Option.NONE -> {
                holder.binding.ivOption.visibility = View.GONE
                R.drawable.null_palceholder
            }
        })
        
        holder.binding.tvAppName.text = appBean.name
        holder.funcUrl = appBean.uid
        holder.binding.root.setOnClickListener {
            if (appBean.action != null && !isInEditing) {
                ActivityUtils.getTopActivity().startActivity(appBean.action)
            }
        }
        // drag item with onTouch
        if (dragOverListener != null) {
            holder.binding.root.setOnTouchListener { _, event ->
                if (event.action == MotionEvent.ACTION_DOWN) {
                    dragOverListener?.startDragItem(holder)
                }
                return@setOnTouchListener false
            }
        }
    }
}

class AppsHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val binding = ItemHomeAppsBinding.bind(itemView)
    var funcUrl = ""
}

interface DragOverListener {
    fun startDragItem(holder: RecyclerView.ViewHolder)
}

interface OnRemoveBtnClickListener {
    fun onClick(view: View, appBean: AppBean)
}