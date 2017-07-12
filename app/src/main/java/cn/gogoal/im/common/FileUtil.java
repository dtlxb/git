package cn.gogoal.im.common;

import android.os.Environment;
import android.text.TextUtils;

import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Locale;

/**
 * Created by Administrator on 2017/1/15 0015.
 */

public class FileUtil {

    public static void writeSDcard(String str, String fileName) {
        FileOutputStream outFileStream = null;
        try {
            // 判断是否存在SD卡
            if (Environment.getExternalStorageState().equals(
                    Environment.MEDIA_MOUNTED)) {
                // 获取SD卡的目录
                File sdDire = Environment.getExternalStorageDirectory();
                outFileStream = new FileOutputStream(
                        sdDire.getCanonicalPath() + "//" + fileName);
                outFileStream.write(str.getBytes());
                outFileStream.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeIO(outFileStream);
        }
    }

    public static void writeRequestResponse(String response) {
    }

    public static void writeRequestResponse(String response, String name) {
    }

    /**
     * 关闭流
     */
    public static void closeIO(Closeable... closeables) {
        if (null == closeables || closeables.length <= 0) {
            return;
        }
        for (Closeable cb : closeables) {
            try {
                if (null == cb) {
                    continue;
                }
                cb.close();
            } catch (IOException e) {
                throw new RuntimeException(
                        FileUtil.class.getClass().getName(), e);
            }
        }
    }

    //删除文件夹
    public static boolean deleteDir(File dir) {
        if (dir == null) return false;
        // 目录不存在返回true
        if (!dir.exists()) return true;
        // 不是目录返回false
        if (!dir.isDirectory()) return false;
        // 现在文件存在且是文件夹
        File[] files = dir.listFiles();
        if (files != null && files.length != 0) {
            for (File file : files) {
                if (file.isFile()) {
                    if (!deleteFile(file)) return false;
                } else if (file.isDirectory()) {
                    if (!deleteDir(file)) return false;
                }
            }
        }
        return dir.delete();
    }

    //删除文件
    public static boolean deleteFile(File file) {
        return file != null && (!file.exists() || file.isFile() && file.delete());
    }

    /**
     * 获取文件大小
     *
     * @param file 文件
     * @return 文件大小
     */
    public static String getFileSize(File file) {
        if ((file == null) || (!file.exists())) return "";
        return byte2FitSize(file.length());
    }

    /**
     * 字节数转合适大小
     * <p>保留3位小数</p>
     *
     * @param byteNum 字节数
     * @return 1...1024 unit
     */
    private static String byte2FitSize(long byteNum) {
        if (byteNum < 0) {
            return "shouldn't be less than zero!";
        } else if (byteNum < 1024) {
            return String.format(Locale.getDefault(), "%.3fB", (double) byteNum);
        } else if (byteNum < 1048576) {
            return String.format(Locale.getDefault(), "%.3fKB", (double) byteNum / 1024);
        } else if (byteNum < 1073741824) {
            return String.format(Locale.getDefault(), "%.3fMB", (double) byteNum / 1048576);
        } else {
            return String.format(Locale.getDefault(), "%.3fGB", (double) byteNum / 1073741824);
        }
    }

    /**
     * 字节数转合适内存大小
     * <p>保留3位小数</p>
     *
     * @param byteNum 字节数
     * @return 合适内存大小
     */
    public static String byte2FitMemorySize(long byteNum) {
        if (byteNum < 0) {
            return "shouldn't be less than zero!";
        } else if (byteNum < 1024) {
            return String.format(Locale.CHINA, "%.3fB", byteNum + 0.0005);
        } else if (byteNum < 1048576) {
            return String.format(Locale.CHINA, "%.3fKB", byteNum / 1024 + 0.0005);
        } else if (byteNum < 1073741824) {
            return String.format(Locale.CHINA, "%.3fMB", byteNum / 1048576 + 0.0005);
        } else {
            return String.format(Locale.CHINA, "%.3fGB", byteNum / 1073741824 + 0.0005);
        }
    }

    //获取文件夹大小
    public static String getDirSize(File dir) {
        long len = getDirLength(dir);
        return len == -1 ? "" : byte2FitMemorySize(len);
    }

    /**
     * 获取目录长度
     *
     * @param dir 目录
     * @return 文件大小
     */
    public static long getDirLength(File dir) {
        if (!(dir != null && dir.exists() && dir.isDirectory())) {
            return -1;
        }
        long len = 0;
        File[] files = dir.listFiles();
        if (files != null && files.length != 0) {
            for (File file : files) {
                if (file.isDirectory()) {
                    len += getDirLength(file);
                } else {
                    len += file.length();
                }
            }
        }
        return len;
    }

    /**
     * 根据文件路径获取文件
     *
     * @param filePath 文件路径
     * @return 文件
     */
    public static File getFileByPath(String filePath) {
        return TextUtils.isEmpty(filePath) ? null : new File(filePath);
    }
}
