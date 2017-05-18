package cn.gogoal.im.ui.copy;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListAdapter;
import android.widget.ListView;

import hply.com.niugu.R;


public class GGListView extends ListView {
    private PullBottomListener listener;
    private View mListViewFooter;
    private GGListView myself = this;
    private boolean canRefrash = true;
    private boolean hasMore = true;

    public GGListView(Context context) {
        super(context);
        init(context);
    }

    public GGListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public GGListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mListViewFooter = LayoutInflater.from(context).inflate(R.layout.listview_footer, null, false);
        addFooterView(mListViewFooter);
        this.setOnScrollListener(new MyScrollListener());
    }

    public void setPullBottomListener(PullBottomListener listener) {
        this.listener = listener;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return super.onTouchEvent(ev);
    }

    public void loadMoreComplate() {
        myself.removeFooterView(mListViewFooter);
        canRefrash = true;
    }

    private class MyScrollListener implements OnScrollListener {
        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            if (scrollState == SCROLL_STATE_IDLE) {
                if (hasMore && canRefrash && view.getLastVisiblePosition() == (view.getCount() - 1)) {
                    myself.addFooterView(mListViewFooter);
                    canRefrash = false;
                    if (listener != null) {
                        listener.Run();
                    }
                }

                if (view.getFirstVisiblePosition() == 0) {
                }
            }
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            hasMore = totalItemCount > visibleItemCount;
        }
    }

    public interface PullBottomListener {
        void Run();
    }

    @Override
    public void setAdapter(ListAdapter adapter) {
        super.setAdapter(adapter);
        myself.removeFooterView(mListViewFooter);
    }
}