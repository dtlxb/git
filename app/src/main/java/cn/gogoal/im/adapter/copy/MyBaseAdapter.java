package cn.gogoal.im.adapter.copy;

import android.widget.BaseAdapter;

import java.util.ArrayList;

/*
* 通用的适配器
* */
public abstract class MyBaseAdapter<T> extends BaseAdapter{

	protected ArrayList<T> list;
	public MyBaseAdapter(ArrayList<T> list) {
		this.list=list;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
}
