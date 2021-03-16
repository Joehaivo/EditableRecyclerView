# EditableRecyclerView
用RecyclerView实现类似支付宝应用图标拖拽排序以及增删管理的功能

### 1. 效果图
![WeChat_20210315170631.gif](https://github.com/Joehaivo/EditableRecyclerView/blob/master/WeChat_20210315170631.gif)

### 2. 基本的功能
0. 在非编辑状态下可以直接点击图标进行跳转
0. 在编辑状态可以拖拽、添加、删除操作
0. 已被添加过的不能再次添加

### 3. 实现的思路
> 用两个RecyclerView实现，同时维护两个数据源，上部是常用应用，最多可以放8个；下部是全部应用。

#### 1. 每个应用图标的状态用枚举Option表示

```kotlin
// 当前的操作状态
enum class Option {
    ADD, REMOVE, NONE
}
```
#### 2. 在处于编辑状态时创建ItemTouchHelper对象并attch到RecyclerView上
```kotlin
if (enable) {
    itemTouchHelper.attachToRecyclerView(binding.rvApps)
} 
```
其中，在实现ItemTouchHelper.Callback接口的onMove() 函数中，此时表示用户已经抬手，而图标位置已经发生了变动，此时将界面上图标的顺序同步回Adapter的数据源中:
```kotlin
val newData = mutableListOf<Pair<String, Int>>()
commonAppsAdapter.data.forEachIndexed { index, _ ->
    val holder = recyclerView.findViewHolderForAdapterPosition(index) as AppsHolder
    newData.add(Pair(holder.funcUrl, index))
}
for (i in newData) {
     val sameFuncIndex = commonAppsAdapter.data.indexOfFirst { i.first == it.uid }
     Collections.swap(commonAppsAdapter.data, i.second, sameFuncIndex)
}
```
#### 3. 当应用图标从上部被删除时，需要将其添加到下部，并将其重新设为可添加状态
```kotlin
commonAppsAdapter.onRemoveBtnClickListener = object : OnRemoveBtnClickListener {
   override fun onClick(view: View, appBean: AppBean) {
       val theSameElementIndex = allAppsAdapter.data.indexOfFirst { it.uid == appBean.uid }
       if (theSameElementIndex < 0) return
       allAppsAdapter.data[theSameElementIndex].option = AppBean.Companion.Option.ADD
       allAppsAdapter.notifyItemChanged(theSameElementIndex)
   }
}
```
[源码请前往Github](https://github.com/Joehaivo/EditableRecyclerView)
#### PS: 仅为个人工作中经验总结，并未认真梳理代码，仅供参考实现的思路
