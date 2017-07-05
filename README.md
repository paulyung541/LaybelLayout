# LaybelLayout
这是一个可以根据内容自动换行的标签布局

原理剖析请看http://www.jianshu.com/p/82f0053b8726</br>

![image](https://github.com/paulyung541/LaybelLayout/blob/master/pic/demo1.jpg)
![image](https://github.com/paulyung541/LaybelLayout/blob/master/pic/demo2.jpg)
![image](https://github.com/paulyung541/LaybelLayout/blob/master/pic/demo3.jpg)

##添加依赖
在Projectd的gradle文件里添加如下：
```groovy
maven { url 'https://jitpack.io' }
```
在项目主Module里添加如下：
```groovy
compile 'com.github.paulyung541:LaybelLayout:v1.2.0'
```


自定义属性：
```xml
app:line_padding="Integer" <!-- 表示每一行的上下内边距 -->
app:text_background="reference" <!-- 表示TextView的自定义背景 -->
```

## 示例
在xml里面添加该控件
```xml
<com.paulyung.laybellayout.LaybelLayout
        android:id="@+id/laybel_layout"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:padding="5dp"
        app:text_background="@drawable/custom_back"
        app:line_padding="5"/>
```

设置Adapter提供数据<br>
**Note：** 这个Adapter可不是ListView的Adapter哦，是自己定义的，不过用法差不多
```java
laybelLayout = (LaybelLayout) findViewById(R.id.laybel_layout);
laybelLayout.setAdapter(new LaybelLayout.Adapter(Content.content));
```

如果要使用其它子View，比如ImageView，则需要重写`Adapter.getView()`方法
```java
laybelLayout = (LaybelLayout) findViewById(R.id.laybelLayout);
laybelLayout.setAdapter(new LaybelLayout.Adapter(Content.images) {
    @Override
    public View getView() {
        ImageView imageView = new ImageView(ImageActivity.this);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        ViewGroup.MarginLayoutParams params = new ViewGroup.MarginLayoutParams(getWidth() / 3, getWidth() / 3);
        imageView.setLayoutParams(params);
        return imageView;
    }

    @Override
    public void onDataSet(View v, String data) {
        Glide.with(ImageActivity.this).load(data).into((ImageView) v);
    }
});
```
添加点击事件
```java
laybelLayout.setOnItemClickListener(new LaybelLayout.OnItemClickListener() {
    @Override
    public void onItemClick(int p) {
        Toast.makeText(DefaultTextViewActivity.this, Content.content[p], Toast.LENGTH_SHORT).show();
    }
});
```