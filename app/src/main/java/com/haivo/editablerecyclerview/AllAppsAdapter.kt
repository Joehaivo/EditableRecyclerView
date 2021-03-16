package com.haivo.editablerecyclerview

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.ActivityUtils


/**
 * @description: 所有应用列表适配器
 **/
class AllAppsAdapter : RecyclerView.Adapter<AppsHolder>() {
    private val TAG = "AllAppsAdapter"
    
    var isInEditing = false //是否处于编辑状态, 编辑状态时点击不能跳转
    
    @Volatile
    var data: MutableList<AppBean> = mutableListOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    var onAddBtnClickListener: OnAddBtnClickListener? = null
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppsHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_home_apps, parent, false)
        return AppsHolder(view)
    }
    
    override fun getItemCount(): Int {
        return data.size
    }
    
    @SuppressLint("ClickableViewAccessibility")
    override fun onBindViewHolder(holder: AppsHolder, position: Int) {
        val appBean = data[holder.adapterPosition]
        holder.binding.ivAppIcon.setImageResource(appBean.iconRes)
        holder.binding.ivOption.setImageResource(when (appBean.option) {
            AppBean.Companion.Option.ADD -> {
                holder.binding.ivOption.visibility = View.VISIBLE
                holder.binding.ivOption.setOnClickListener {
                    onAddBtnClickListener?.onClick(it, appBean)
                }
                R.drawable.ic_add
            }
            AppBean.Companion.Option.REMOVE -> {
                holder.binding.ivOption.visibility = View.VISIBLE
                holder.binding.ivOption.setOnClickListener {
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
        holder.binding.ivAppIcon.setImageResource(appBean.iconRes)
        holder.binding.tvAppName.text = appBean.name
        holder.funcUrl = appBean.uid
        holder.binding.root.setOnClickListener {
            if (appBean.action != null && !isInEditing) {
                ActivityUtils.getTopActivity().startActivity(appBean.action)
            }
        }
    }
}

interface OnAddBtnClickListener {
    fun onClick(view: View, appBean: AppBean)
}