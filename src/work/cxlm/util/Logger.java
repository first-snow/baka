package work.cxlm.util;

import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author cxlm
 * 2020/5/2 20:43
 * 公用日志
 */
public class Logger {

    public enum Level {
        NORMAL("I", System.out), ERROR("E", System.err), DEBUG("D", System.out);

        private final PrintStream stream;
        private final String prefix;

        Level(String prefix, PrintStream stream) {
            this.prefix = prefix;
            this.stream = stream;
        }

        void log(String msg) {
            stream.println(msg);
        }
    }

    private final Class<?> targetClass;

    private Logger(Class<?> cls) {
        targetClass = cls;
    }

    /**
     * 工厂方法，获取日志实例
     *
     * @param targetClass 日志作用的类对象
     * @return 日志实例
     */
    public static Logger getLogger(Class<?> targetClass) {
        return new Logger(targetClass);
    }

    /**
     * 通过日志记录信息，日志输出格式如下: <br/>
     * [yyyy.MM.dd HH:mm:ss] 全限定类名:行号_方法名 - 日志信息
     *
     * @param level 日志级别，NORMAL，ERROR
     * @param msg   日志信息
     */
    public void log(Level level, String msg) {
        if (level == Level.DEBUG && Config.get("debug") == null) return;  // 未开启 debug 忽略 debug 级别日志
        SimpleDateFormat format = new SimpleDateFormat("[yyyy.MM.dd HH:mm:ss]");
        String nowTime = format.format(new Date());
        String className = targetClass.getName();
        StackTraceElement stackElement = Thread.currentThread().getStackTrace()[2];
        String methodName = stackElement.getMethodName();
        int methodLine = stackElement.getLineNumber();
        level.log(String.format("%s: %s %s:%d_%s - [%s]", level.prefix, nowTime, className, methodLine, methodName, msg));
    }

    public void info(String msg) {
        log(Level.NORMAL, msg);
    }

    public void debug(String msg) {
        log(Level.DEBUG, msg);
    }

    public void err(String msg) {
        log(Level.ERROR, msg);
    }
}
