package cn.gogoal.im.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;

import cn.gogoal.im.ui.view.AutoScrollViewPager;

/**
 * Created by lixs on 2015/12/8.
 */
public class DisallowParentTouchViewPager extends AutoScrollViewPager {

  private ViewGroup parent;

  public DisallowParentTouchViewPager(Context context) {
    super(context);
  }

  public DisallowParentTouchViewPager(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public void setNestParent(ViewGroup parent) {
    this.parent = parent;
  }

  @Override
  public boolean dispatchTouchEvent(MotionEvent ev) {
    if (parent != null) {
      parent.requestDisallowInterceptTouchEvent(true);
    }
    return super.dispatchTouchEvent(ev);
  }

  @Override
  public boolean onInterceptTouchEvent(MotionEvent ev) {
    if (parent != null) {
      parent.requestDisallowInterceptTouchEvent(true);
    }
    return super.onInterceptTouchEvent(ev);
  }

  @Override
  public boolean onTouchEvent(MotionEvent ev) {
    if (parent != null) {
      parent.requestDisallowInterceptTouchEvent(true);
    }
    return super.onTouchEvent(ev);
  }
}