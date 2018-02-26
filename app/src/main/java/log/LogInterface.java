package log;

import android.annotation.Nullable;

/**
 * 一句话注释。
 * 详细内容。
 *
 * @author sky on 2018/2/26
 */
interface LogInterface {
    void handleMsg(int priority, String tag, String msg, @Nullable Throwable tr);
}
