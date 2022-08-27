# SwipePanel
**实现可下滑收起的面板。**

官方组件：BottomSheetBehavior, 有以下缺点

- 依赖面板内容：若面板内有多个嵌套滚动的View，则无法正确响应
- 扩展性低：内部关键属性和方法不可配置或者重写，导致无法定制化自己想要的效果
- 复杂：BottomSheetBehavior有三种状态：折叠，展开和隐藏，大部分场景只需要展开和隐藏即可

## 思路

1. 面板内容可分为背景区、内容区；
2. 内容区又分为嵌套滚动区和非嵌套滚动区；
3. 根据嵌套滚动的向上传递性质，可以将内容区整个变为嵌套滚动区域。但是并不想整个内容区真的可以滚动，因此自定义NestedScrollBridgeView：桥接父子View直接的嵌套滚动事件，布局则直接继承自FrameLayout.
4. 背景区域的滚动监听则通过CoordinateLayout.Behavior实现。(这里没有实际完成，现在背景仍旧由NestedScrollBridgeView提供，这样只实现嵌套滚动逻辑即可，不用实现onInterceptTouch的逻辑)

## 使用

如下最外层为CoordinatorLayout。ViewPager2为内容区域，NestedScrollBridgeView充当背景并包裹内容，设置SwipeBehavior为NestedScrollBridgeView的Behavior即可。

```xml
<?xml version="1.0" encoding="utf-8"?>
<androidx.coordinatorlayout.widget.CoordinatorLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context=".MainActivity"
    android:background="@color/black">

    <com.sr01.swipepanel.NestedScrollBridgeView
        android:id="@+id/bv"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        app:layout_behavior=".SwipeBehavior">
        <androidx.viewpager2.widget.ViewPager2
            android:id="@+id/vp"
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            android:layout_marginTop="200dp"
            android:background="@color/white"/>
    </com.sr01.swipepanel.NestedScrollBridgeView>

</androidx.coordinatorlayout.widget.CoordinatorLayout>
```

## 演示

![演示](img/964vu-mu7jj.gif)