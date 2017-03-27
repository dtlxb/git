package cn.gogoal.im.common;

import android.os.AsyncTask;

/**
 * author wangjd on 2017/2/23 0023.
 * Staff_id 1375
 * phone 18930640263
 */
public class AsyncTaskUtil {

    public static void doAsync(final AsyncCallBack callBack) {
        if (callBack == null) {
            return;
        }
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected void onPreExecute() {
                callBack.onPreExecute();
            }

            @Override
            protected Void doInBackground(Void... params) {
                callBack.doInBackground();
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                callBack.onPostExecute();
            }
        }.execute();
    }

    public interface AsyncCallBack {

        void onPreExecute();

        void doInBackground();

        void onPostExecute();
    }
}
