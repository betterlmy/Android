# 第四周作业 带ProcessBar，SeekBar的TODO功能/

## 知识点
### 1.不同acitivity之间传参
##### a.Bundle bundle=intent.getExtras();<br>
##### b.intent.pueExtras(bundle)<br>
##### c.Intent intent=new Intent(MainActivity.this,another_activity.class);<br>
##### d.startActivityForResult(Intent intent,int number);<br>
##### e.onActivityResult(int requestCode, int resultCode, Intent data)<br>
##### f.setResult( 1, intent );<br>
##### g.finish();<br><br>

### 2.RecyclerList的点击事件
##### holder.itemView.setOnClickListenernew View.OnClickListener() {
##### @Override
##### public void onClick(View v) {<br>}<br>}<br>

### 3.ProcessBar,SeekBar的使用
##### SeekBar的监听事件<br><br>
```java 
private SeekBar.OnSeekBarChangeListener seekBarChangeListener= new SeekBar.OnSeekBarChangeListener() {
            @Override
              public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                  tv5.setText( progress+"%" );
              }
      
              @Override
              public void onStartTrackingTouch(SeekBar seekBar) {
                  Toast.makeText( adjust.this,"开始滑动",Toast.LENGTH_SHORT).show();
      
              }
      
              @Override
              public void onStopTrackingTouch(SeekBar seekBar) {
                  Toast.makeText( adjust.this,"结束滑动",Toast.LENGTH_SHORT).show();
              }
          } ; 
```

