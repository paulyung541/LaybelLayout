# LaybelLayout
一个标签布局

原理剖析请看http://www.jianshu.com/p/82f0053b8726</br>
开发当中经常用到的标签布局，支持自适应宽高 `wrap_content`，效果如下图所示

![image](https://github.com/paulyung541/LaybelLayout/blob/master/demo.jpg)

##添加依赖
```groovy
compile 'com.paulyung:laybellayout:1.0.0'
```


自定义属性：
```xml
app:line_padding="Integer"
```
表示每一行的上下内边距

##示例
在xml里面添加该控件
```xml
<com.paulyung.laybellayout.LaybelLayout
        android:id="@+id/laybel_layout"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:padding="5dp"
        app:line_padding="5"/>
```

在代码里为该控件添加子控件，子控件支持margin外边距
```java
laybelLayout = (LaybelLayout) findViewById(R.id.laybel_layout);

for (int i = 0; i < Content.content.length; i++) {
    TextView tv = new TextView(this);
    tv.setText(Content.content[i]);
    tv.setTextSize(20);
    ViewGroup.MarginLayoutParams lp = new ViewGroup.MarginLayoutParams(
            ViewGroup.MarginLayoutParams.WRAP_CONTENT,
            ViewGroup.MarginLayoutParams.WRAP_CONTENT);
    lp.leftMargin = 20;
    lp.rightMargin = 20;
    tv.setBackground(getDrawable(R.drawable.background));
    tv.setTextColor(getResources().getColor(R.color.colorAccent));
    laybelLayout.addView(tv, lp);
}
```