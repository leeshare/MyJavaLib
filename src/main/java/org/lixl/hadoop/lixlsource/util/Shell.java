package org.lixl.hadoop.lixlsource.util;

import com.google.common.annotations.VisibleForTesting;
import org.lixl.hadoop.lixlsource.classification.InterfaceAudience;
import org.lixl.hadoop.lixlsource.classification.InterfaceStability;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.Charset;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 一个基本类 用于执行shell命令
 * Shell能被用于执行shell命令，像 du 或 df。
 */
@InterfaceAudience.Public
@InterfaceStability.Evolving
public abstract class Shell {
    private static final Map<Shell, Object> CHILD_SHELLS =
            Collections.synchronizedMap(new WeakHashMap<Shell, Object>());
    public static final Logger LOG = LoggerFactory.getLogger(Shell.class);

    private static final String WINDOWS_PROBLEMS = "https://wiki.apache.org/hadoop/WindowsProblems";
    /**
     * windows通用二进制 名称
     */
    static final String WINUTILS_EXE = "winutils.exe";
    /**
     * 系统属性 用于 hadoop home路径
     */
    public static final String SYSPROP_HADOOP_HOME_DIR = "hadoop.home.dir";
    /**
     * 环境变量 用于 Hadoop's home路径
     */
    public static final String ENV_HADOOP_HOME = "HADOOP_HOME";

    /**
     * 查询看系统是否是java7或更新的
     * @return
     */
    @Deprecated
    public static boolean isJava70rAbove() {
        return true;
    }

    private static final int JAVA_SPEC_VER = Math.max(8,
            Integer.parseInt(System.getProperty("java.specification.version").split("\\.")[0]));

    /**
     * 查询看是否java规范的主要版本等于或大于参数
     * @param version 8，9，10 etc.
     * @return
     */
    public static boolean isJavaVersionAtLeast(int version) {
        return JAVA_SPEC_VER >= version;
    }

    /**
     * Windows中的最大命令行长度
     */
    public static final int WINDOWS_MAX_SHELL_LENGTH = 8191;
    /**
     * 错误的拼写
     */
    @Deprecated
    public static final int WINDOWS_MAX_SHELL_LENGHT = WINDOWS_MAX_SHELL_LENGTH;

    public static void checkWindowsCommandLineLength(String...commands) throws IOException {
        int len = 0;
        for(String s : commands) {
            len += s.length();
        }
        if(len > WINDOWS_MAX_SHELL_LENGTH) {
            throw new IOException(String.format("此命令行有一个长度 %d 超过最大允许长度 %d. 命令开始于：%s",
                    len, WINDOWS_MAX_SHELL_LENGTH, StringUtils.join("", commands).substring(0, 100)));
        }
    }

    /**
     * 引号 在参数中，bash将其当做一个单独的值
     * @param arg
     * @return
     */
    static String bashQuote(String arg) {
        StringBuilder buffer = new StringBuilder(arg.length() + 2);
        buffer.append('\'');
        buffer.append(arg.replace("'", "'\\''"));
        buffer.append('\'');
        return buffer.toString();
    }

    /** 一个Unix命令 用于获取当前用户名 */
    public static final String USER_NAME_COMMAND = "whoami";
    /** Windows 创建进程 同步对象 */
    public static final Object WindowsProcessLaunchLock = new Object();

    public enum OSType {
        OS_TYPE_LINUX,
        OS_TYPE_WIN,
        OS_TYPE_SOLARIS,
        OS_TYPE_MAC,
        OS_TYPE_FREEBSD,
        OS_TYPE_OTHER
    }

    public static final OSType osType = getOSType();

    private static OSType getOSType() {
        String osName = System.getProperty("os.name");
        if(osName.startsWith("Windows")) {
            return OSType.OS_TYPE_WIN;
        } else if(osName.contains("SunOS") || osName.contains("Solaris")) {
            return OSType.OS_TYPE_SOLARIS;
        } else if(osName.contains("FreeBSD")) {
            return OSType.OS_TYPE_FREEBSD;
        } else if(osName.contains("Mac")) {
            return OSType.OS_TYPE_MAC;
        } else if(osName.startsWith("Linux")) {
            return OSType.OS_TYPE_LINUX;
        } else {
            return OSType.OS_TYPE_OTHER;
        }
    }
    // 帮助者 静态变量 对应各个平台
    public static final boolean WINDOWS = (osType == OSType.OS_TYPE_WIN);
    public static final boolean SOLARIS = (osType == OSType.OS_TYPE_SOLARIS);
    public static final boolean MAC     = (osType == OSType.OS_TYPE_MAC);
    public static final boolean FREEBSD = (osType == OSType.OS_TYPE_FREEBSD);
    public static final boolean LINUX   = (osType == OSType.OS_TYPE_LINUX);
    public static final boolean OTHER   = (osType == OSType.OS_TYPE_OTHER);

    public static String[] getGroupsCommand() {
        return (WINDOWS) ? new String[]{"cmd", "/c", "groups"} : new String[]{"groups"};
    }

    /**
     * 一个命令：获取用户分组列表
     * 如果不是win，命令将首先获取用户的主要分组，其次获取包含主要分组的分组列表
     * @param user
     * @return
     */
    public static String[] getGroupsForUserCommand(final String user) {
        if(WINDOWS) {
            return new String[] {getWinUtilsPath(), "groups", "-F", "\"" + user + "\""};
        } else {
            String quoteUser = bashQuote(user);
            return new String[] {"bash", "-c", "id -gn" + quoteUser + "; id -Gn" + quoteUser};
        }
    }

    /**
     * 一个命令 获取用户分组id列表
     * @param user
     * @return
     */
    public static String[] getGroupsIDForUserCommand(final String user) {
        if(WINDOWS) {
            return new String[]{getWinUtilsPath(), "groups", "-F", "\"" + user + "\""};
        } else {
            String quoteUser = bashQuote(user);
            return new String[]{"bash", "-c", "id -g" + quoteUser + "; id -G " + quoteUser};
        }
    }
    /** 一个命令 获取给定netgroup的用户列表 */
    public static String[] getUsersForNetgroupCommand(final String netgroup) {
        return new String[] {"getent", "netgroup", netgroup};
    }
    /** 返回一个命令: 获取权限信息的命令 */
    public static String[] getGetPermissionCommand() {
        return (WINDOWS) ? new String[] { getWinUtilsPath(), "ls", "-F"} : new String[] {"ls", "-ld"};
    }
    /** 返回一个命令 设置权限 */
    public static String[] getSetPermissionCommand(String perm, boolean recursive) {
        if(recursive) {
            return (WINDOWS) ? new String[] { getWinUtilsPath(), "chmod", "-R", perm }
            : new String[] { "chmod", "-R", perm };
        } else {
            return (WINDOWS) ? new String[] { getWinUtilsPath(), "chmod", perm }
            : new String[] { "chmod", perm };
        }
    }

    /**
     * 返回
     * @param perm
     * @param recursive
     * @param file
     * @return
     */
    public static String[] getSetPermissionCommand(String perm, boolean recursive, String file) {
        String[] baseCmd = getSetPermissionCommand(perm, recursive);
        String[] cmdWithFile = Arrays.copyOf(baseCmd, baseCmd.length + 1);
        cmdWithFile[cmdWithFile.length - 1] = file;
        return cmdWithFile;
    }
    /** 返回一条命令 设置使用者 */
    public static String[] getSetOwnerCommand(String owner) {
        return (WINDOWS)
                ? new String[] { getWinUtilsPath(), "chown", "\"" + owner + "\"" }
                : new String[] { "chown", owner };
    }
    /** 返回一条命令 创建 动态链接 */
    public static String[] getSymlinkCommand(String target, String link) {
        return WINDOWS
                ? new String[] { getWinUtilsPath(), "symlink", link, target }
                : new String[] { "ln", "-s", target, link };
    }
    /** 返回一个命令 用于读动态链接目标的命令  */
    public static String[] getReadlinkCommand(String link) {
        return WINDOWS
                ? new String[] { getWinUtilsPath(), "readlink", link }
                : new String[] { "readlink", link };
    }
    /** 返回一个命令 用于判定 指定pid的进程是否还活着 */
    public static String[] getCheckProcessIsAliveCommand(String pid) {
        return getSignalKillCommand(0, pid);
    }
    /** 返回一个命令 发送一个信号给指定pid */
    public static String[] getSignalKillCommand(int code, String pid) {
        //Code == 0 表示还 活着
        if(Shell.WINDOWS) {
            if(0 == code) {
                return new String[] { Shell.getWinUtilsPath(), "task", "isAlive", pid };
            } else {
                return new String[] { Shell.getWinUtilsPath(), "task", "kill", pid };
            }
        }

        final String quotePid = bashQuote(pid);
        if(isSetsidAvailable) {
            return new String[] { "bash", "-c", "kill -" + code + " -- -" + quotePid };
        } else {
            return new String[] { "bash", "-c", "kill -" + code + " " + quotePid };
        }
    }

    public static final String ENV_NAME_REGEX = "[A-Za-z_][A-Za-z0-9_]*";

    public static String getEnvironmentVariableRegex() {
        return (WINDOWS)
                ? "%(" + ENV_NAME_REGEX + "?)%"
                : "\\$(" + ENV_NAME_REGEX + ")";
    }

    /**
     * 返回一个文件 引用一个给定名称和父路径的脚本
     * @param parent
     * @param basename
     * @return
     */
    public static File appendScriptExtension(File parent, String basename) {
        return new File(parent, appendScriptExtension(basename));
    }

    public static String appendScriptExtension(String basename) {
        return basename + (WINDOWS ? ".cmd" : ".sh");
    }

    public static String[] getRunScriptCommand(File script) {
        String absolutePath = script.getAbsolutePath();
        return WINDOWS
                ? new String[] {"cmd", "/c", absolutePath}
                : new String[] {"bash", bashQuote(absolutePath)};
    }

    public static final String SET_PERMISSION_COMMAND = "chmod";
    public static final String SET_OWNER_COMMAND = "chown";
    public static final String SET_GROUP_COMMAND = "chgrp";
    public static final String LINK_COMMAND = "ln";
    public static final String READ_LINK_COMMAND = "readlink";

    protected long timeOutInterval = 0L;
    private final AtomicBoolean timedOut = new AtomicBoolean(false);
    /** 指示 父环境变量 是否可继承 */
    protected boolean inheritParentEnv = true;

    /**
     * 集中逻辑 查找和验证hadoop home目录的有效性
     * @return
     * @throws FileNotFoundException
     */
    private static File checkHadoopHome() throws FileNotFoundException {
        String home = System.getProperty(SYSPROP_HADOOP_HOME_DIR);
        if(home == null) {
            home = System.getenv(ENV_HADOOP_HOME);
        }
        return checkHadoopHomeInner(home);
    }

    static final String E_DOES_NOT_EXIST = "不存在";
    static final String E_IS_RELATIVE = "不是一个绝对路径";
    static final String E_NOT_DIRECTORY = "不能一个路径";
    static final String E_NO_EXECUTABLE = "不能定位hadoop可执行文件";
    static final String E_NOT_EXECUTABLE_FILE = "不是一个可执行文件";
    static final String E_HADOOP_PROPS_UNSET = ENV_HADOOP_HOME + " and " + SYSPROP_HADOOP_HOME_DIR + " are unset.";
    static final String E_HADOOP_PROPS_EMPTY = ENV_HADOOP_HOME + " or " + SYSPROP_HADOOP_HOME_DIR + " set to an empty string.";
    static final String E_NOT_A_WINDOWS_SYSTEM = "不是一个Windows系统";

    @VisibleForTesting
    static File checkHadoopHomeInner(String home) throws FileNotFoundException {
        if(home == null) {
            throw new FileNotFoundException(E_HADOOP_PROPS_UNSET);
        }
        while (home.startsWith("\"")) {
            home = home.substring(1);
        }
        while(home.endsWith("\"")) {
            home = home.substring(0, home.length() - 1);
        }

        if(home.isEmpty()) {
            throw new FileNotFoundException(E_HADOOP_PROPS_EMPTY);
        }

        File homedir = new File(home);
        if(!homedir.isAbsolute()) {
            throw new FileNotFoundException("Hadoop home directory " + homedir + " " + E_IS_RELATIVE);
        }
        if(!homedir.exists()) {
            throw new FileNotFoundException("hadoop home directory " + homedir + " " + E_DOES_NOT_EXIST);
        }
        if(!homedir.isDirectory()) {
            throw new FileNotFoundException("Hadoop home directory " + homedir + " " + E_NOT_DIRECTORY);
        }
        return homedir;
    }

    /** Hadoop home路径 */
    private static final File HADOOP_HOME_FILE;
    /** 再次抛出原因：决定hadoop home路径失败 */
    private static final IOException HADOOP_HOME_DIR_FAILURE_CAUSE;

    static {
        File home;
        IOException ex;
        try {
            home = checkHadoopHome();
            ex = null;
        } catch (IOException ioe) {
            if(LOG.isDebugEnabled()) {
                LOG.debug("失败了 查明一个有效的hadoop home目录", ioe);
            }
            ex = ioe;
            home = null;
        }
        HADOOP_HOME_FILE = home;
        HADOOP_HOME_DIR_FAILURE_CAUSE = ex;
    }

    private static String addOsText(String message) {
        return WINDOWS ? (message + " -see " + WINDOWS_PROBLEMS) : message;
    }

    private static FileNotFoundException fileNotFoundException(String text, Exception ex) {
        return (FileNotFoundException) new FileNotFoundException(text).initCause(ex);
    }

    public static String getHadoopHome() throws IOException {
        return getHadoopHomeDir().getCanonicalPath();
    }

    private static File getHadoopHomeDir() throws FileNotFoundException {
        if(HADOOP_HOME_DIR_FAILURE_CAUSE != null) {
            throw fileNotFoundException(addOsText(HADOOP_HOME_DIR_FAILURE_CAUSE.toString()), HADOOP_HOME_DIR_FAILURE_CAUSE);
        }
        return HADOOP_HOME_FILE;
    }

    public static File getQualifiedBin(String executable)
            throws FileNotFoundException {
        // construct hadoop bin path to the specified executable
        return getQualifiedBinInner(getHadoopHomeDir(), executable);
    }

    /**
     * 内部逻辑 getQualifiedBin
     * @param hadoopHomeDir
     * @param executable
     * @return
     * @throws FileNotFoundException
     */
    static File getQualifiedBinInner(File hadoopHomeDir, String executable) throws FileNotFoundException {
        String binDirText = "Hadoop bin directory ";
        File bin = new File(hadoopHomeDir, "bin");
        if(!bin.exists()) {
            throw new FileNotFoundException(addOsText(binDirText + E_DOES_NOT_EXIST + ": " + bin));
        }
        if(!bin.isDirectory()) {
            throw new FileNotFoundException(addOsText(binDirText + E_NOT_DIRECTORY + ": " + bin));
        }

        File exeFile = new File(bin, executable);
        if(!exeFile.exists()) {
            throw new FileNotFoundException(addOsText(E_NO_EXECUTABLE + ": " + exeFile));
        }
        if(!exeFile.isFile()) {
            throw new FileNotFoundException(addOsText(E_NOT_EXECUTABLE_FILE + ": " + exeFile));
        }
        try {
            return exeFile.getCanonicalFile();
        } catch (IOException e) {
            throw fileNotFoundException(e.toString(), e);
        }
    }

    public static String getQualifiedBinPath(String executable) throws IOException {
        //return getQualifiedBin(executable).
        return null;
    }

    @Deprecated
    public static final String WINUTILS;

    private static final String WINUTILS_PATH;

    private static final File WINUTILS_FILE;

    private static final IOException WINUTILS_FAILURE;

    static {
        IOException ioe = null;
        String path = null;
        File file = null;
        if(WINDOWS) {
            try {
                file = getQualifiedBin(WINUTILS_EXE);
                path = file.getCanonicalPath();
                ioe = null;
            } catch (IOException e) {
                LOG.warn("找不到 {}: {}", WINUTILS_EXE, e);
                LOG.debug("寻找 {} {}失败了", WINUTILS_EXE, e);
                file = null;
                path = null;
                ioe = e;
            }
        } else {
            ioe = new FileNotFoundException(E_NOT_A_WINDOWS_SYSTEM);
        }
        WINUTILS_PATH = path;
        WINUTILS_FILE = file;

        WINUTILS = path;
        WINUTILS_FAILURE = ioe;
    }

    public static boolean hasWinutilsPath() {
        return WINUTILS_PATH != null;
    }

    public static String getWinUtilsPath() {
        if(WINUTILS_FAILURE == null) {
            return WINUTILS_PATH;
        } else {
            throw new RuntimeException(WINUTILS_FAILURE.toString(), WINUTILS_FAILURE);
        }
    }

    public static File getWinUtilsFile() throws FileNotFoundException {
        if(WINUTILS_FAILURE == null) {
            return WINUTILS_FILE;
        } else {
            throw fileNotFoundException(WINUTILS_FAILURE.toString(), WINUTILS_FAILURE);
        }
    }

    public static boolean checkIsBashSupported() throws InterruptedException {
        if(Shell.WINDOWS) {
            return false;
        }

        ShellCommandExecutor shexec;
        boolean supported = true;
        try {
            String[] args = {"bash", "-c", "echo 1000"};
            shexec = new ShellCommandExecutor(args);
            shexec.execute();
        } catch (InterruptedIOException iioe) {
            LOG.warn("中断，不能确定是否支持bash", iioe);
            //throw iioe;
        } catch(IOException ioe) {
            LOG.warn("Bash不支持在此系统", ioe);
            supported = false;
        } catch(SecurityException se) {
            LOG.info("此JVM安全管理 不允许Bash执行。认为它不支持。");
            supported = false;
        }
        return supported;
    }

    public static final boolean isSetsidAvailable = isSetsidSupported();

    private static boolean isSetsidSupported() {
        if(Shell.WINDOWS) {
            return false;
        }
        ShellCommandExecutor shexec = null;
        boolean setsidSupported = true;
        try {
            String[] args = {"setsid", "bash", "-c", "echo $$"};
            shexec = new ShellCommandExecutor(args);
            shexec.execute();
        } catch (IOException ioe) {
            LOG.debug("setsid在此机器上不可用。所以不要使用它。");
            setsidSupported = false;
        } catch(SecurityException se) {
            LOG.debug("setsid 不允许JVM安全管理执行。所以不要使用它。");
            setsidSupported = false;
        } catch (Error err) {
            if(err.getMessage() != null
                    && err.getMessage().contains("posix_spawn is not a supported process launch mechanism")
                    && (Shell.FREEBSD || Shell.MAC)) {
                LOG.info("避免JKD-8047340在BSD系统。", err);
                setsidSupported = false;
            }
        } finally {
            if(LOG.isDebugEnabled()) {
                LOG.debug("setsid退出，突出编码 " + (shexec != null ? shexec.getExitCode() : "(null executor)"));
            }
        }
        return setsidSupported;
    }

    public static final String TOKEN_SEPARATOR_REGEX = WINDOWS ? "[!\n\r]" : "[ \t\n\r\f]";

    private long interval;
    private long lastTime;
    private final boolean redirectErrorStream;
    private Map<String, String> environment;
    private File dir;
    private Process process;
    private int exitCode;
    private Thread waitingThread;

    private final AtomicBoolean completed = new AtomicBoolean(false);

    protected Shell() {
        this(0L);
    }
    protected Shell(long interval) {
        this(interval, false);
    }
    protected Shell(long interval, boolean redirectErrorStream) {
        this.interval = interval;
        this.lastTime = (interval < 0) ? 0 : -interval;
        this.redirectErrorStream = redirectErrorStream;
    }

    protected void setEnvironment(Map<String, String> env) {
        this.environment = env;
    }
    protected void setWorkingDirectory(File dir) {
        this.dir = dir;
    }
    protected void run() throws IOException {
        if(lastTime + interval > Time.monotonicNow()) {
            return;
        }
        exitCode = 0;   //reset 用于下次执行
        if(Shell.MAC) {
            System.setProperty("jdk.lang.Process.launchMechanism", "POSIX_SPAWN");
        }
        runCommand();
    }

    private void runCommand() throws IOException {
        ProcessBuilder builder = new ProcessBuilder(getExecString());
        Timer timeOutTimer = null;
        ShellTimeoutTimerTask timeoutTimerTask = null;
        timedOut.set(false);
        completed.set(false);

        if(!inheritParentEnv) {
            builder.environment().clear();
        }
        if(environment != null) {
            builder.environment().putAll(this.environment);
        }
        if(dir != null) {
            builder.directory(this.dir);
        }
        builder.redirectErrorStream(redirectErrorStream);
        if(Shell.WINDOWS) {
            synchronized (WindowsProcessLaunchLock) {
                //
                process = builder.start();
            }
        }else {
            process = builder.start();
        }

        waitingThread = Thread.currentThread();
        CHILD_SHELLS.put(this, null);

        if(timeOutInterval > 0) {
            timeOutTimer = new Timer("Shell command timeout");
            timeoutTimerTask = new ShellTimeoutTimerTask(this);
            timeOutTimer.schedule(timeoutTimerTask, timeOutInterval);
        }
        final BufferedReader errReader = new BufferedReader(new InputStreamReader(process.getErrorStream(), Charset.defaultCharset()));
        BufferedReader inReader = new BufferedReader(new InputStreamReader(process.getInputStream(), Charset.defaultCharset()));
        final StringBuffer errMsg = new StringBuffer();

        Thread errThread = new Thread() {
            @Override
            public void run() {
                try {
                    String line = errReader.readLine();
                    while((line != null) && !isInterrupted()) {
                        errMsg.append(line);
                        errMsg.append(System.getProperty("line.separator"));
                        line = errReader.readLine();
                    }
                } catch (IOException ioe) {
                    if(!isTimedOut()) {
                        LOG.warn("Error reading the error stream", ioe);
                    } else {
                        LOG.debug("Error reading the error stream due to shell command timeout", ioe);
                    }
                }
            }
        };
        try {
            errThread.start();
        } catch (IllegalStateException ise) {

        } catch (OutOfMemoryError oe) {
            LOG.error("Caught " + oe + ". 一个可能原因是不限的设置'最大用户进程'太小了。如果是这样，执行'ulimit -u <largerNum>' 并再尝试");
            throw oe;
        }
        try {
            parseExecResult(inReader);
            String line = inReader.readLine();
            while(line != null) {
                line = inReader.readLine();
            }
            exitCode = process.waitFor();
            joinThread(errThread);
            completed.set(true);
            if(exitCode != 0) {
                throw new ExitCodeException(exitCode, errMsg.toString());
            }
        } catch (InterruptedException ie) {
            InterruptedIOException iie = new InterruptedIOException(ie.toString());
            iie.initCause(ie);
            throw iie;
        } finally {
            if(timeOutTimer != null) {
                timeOutTimer.cancel();
            }
            try {
                InputStream stdout = process.getInputStream();
                synchronized (stdout) {
                    inReader.close();
                }
            } catch (IOException ioe) {
                LOG.warn("当关闭输入流时出错", ioe);
            }
            if(!completed.get()) {
                errThread.interrupt();
                joinThread(errThread);
            }
            try {
                InputStream stderr = process.getErrorStream();
                synchronized (stderr) {
                    errReader.close();
                }
            } catch (IOException ioe) {
                LOG.warn("当关闭错误流时出错", ioe);
            }
            process.destroy();
            waitingThread = null;
            CHILD_SHELLS.remove(this);
            lastTime = Time.monotonicNow();
        }
    }

    private static void joinThread(Thread t) {
        while(t.isAlive()) {
            try {
                t.join();
            } catch (InterruptedException ie) {
                if(LOG.isWarnEnabled()) {
                    LOG.warn("当连接时中断: " + t, ie);
                }
                t.interrupt();
            }
        }
    }

    protected abstract String[] getExecString();

    protected abstract void parseExecResult(BufferedReader lines) throws IOException;

    public String getEnvironment(String env) {
        return environment.get(env);
    }
    public Process getProcess() {
        return process;
    }
    public int getExitCode() {
        return exitCode;
    }
    public Thread getWaitingThread() {
        return waitingThread;
    }

    public static class ExitCodeException extends IOException {
        private final int exitCode;

        public ExitCodeException(int exitCode, String message) {
            super(message);
            this.exitCode = exitCode;
        }
        public int getExitCode() {
            return exitCode;
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("ExitCodeException ");
            sb.append("exitCode=").append(exitCode)
                    .append(": ");
            sb.append(super.getMessage());
            return sb.toString();
        }
    }

    public interface CommandExecutor {
        void execute() throws IOException;
        int getExitCode() throws IOException;
        String getOutput() throws IOException;
        void close();
    }

    /**
     * 一个简单的shell命令执行器
     */
    public static class ShellCommandExecutor extends Shell implements CommandExecutor {
        private String[] command;
        private StringBuffer output;

        public ShellCommandExecutor(String[] execString) {
            this(execString, null);
        }
        public ShellCommandExecutor(String[] execString, File dir) {
            this(execString, dir, null);
        }
        public ShellCommandExecutor(String[] execString, File dir, Map<String, String> env) {
            this(execString, dir, env, 0L);
        }
        public ShellCommandExecutor(String[] execString, File dir, Map<String, String> env, long  timeout) {
            this(execString, dir, env, timeout, true);
        }

        public ShellCommandExecutor(String[] execString, File dir,
                                    Map<String, String> env, long timeout, boolean inheritParentEnv) {
            command = execString.clone();
            if(dir != null) {
                setWorkingDirectory(dir);
            }
            if(env != null) {
                setEnvironment(env);
            }
            timeOutInterval = timeout;
            this.inheritParentEnv = inheritParentEnv;
        }
        @VisibleForTesting
        public long getTimeoutInterval() {
            return timeOutInterval;
        }

        public void execute() throws IOException {
            for(String s : command) {
                if(s == null) {
                    throw new IOException("(null)输入在命令数组中：" + StringUtils.join(" ", command));
                }
            }
            this.run();
        }

        @Override
        public String[] getExecString() {
            return command;
        }

        @Override
        protected void parseExecResult(BufferedReader lines) throws IOException {
            output = new StringBuffer();
            char[] buf = new char[512];
            int nRead;
            while( (nRead = lines.read(buf, 0, buf.length)) > 0 ) {
                output.append(buf, 0, nRead);
            }
        }

        public String getOutput() {
            return (output == null) ? "" : output.toString();
        }

        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder();
            String[] args = getExecString();
            for(String s : args) {
                if(s.indexOf(' ') >= 0) {
                    builder.append('"').append(s).append('"');
                } else {
                    builder.append(s);
                }
                builder.append(' ');
            }
            return builder.toString();
        }

        @Override
        public void close() {

        }

    }

    public boolean isTimedOut() {
        return timedOut.get();
    }
    private void setTimedOut() {
        this.timedOut.set(true);
    }

    public static String execCommand(String ... cmd) throws IOException {
        return execCommand(null, cmd, 0L);
    }

    public static String execCommand(Map<String, String> env, String[] cmd, long timeout) throws IOException {
        ShellCommandExecutor exec = new ShellCommandExecutor(cmd, null, env, timeout);
        exec.execute();
        return exec.getOutput();
    }

    public static String execCommand(Map<String, String> env, String ... cmd) throws IOException {
        return execCommand(env, cmd, 0L);
    }


    private static class ShellTimeoutTimerTask extends TimerTask {
        private final Shell shell;
        public ShellTimeoutTimerTask(Shell shell) {
            this.shell = shell;
        }

        @Override
        public void run() {
            Process p = shell.getProcess();
            try {
                p.exitValue();
            } catch (Exception e) {
                //进程未终止 所以检查它是否完成 如果没有就destory
                if(p != null && !shell.completed.get()) {
                    shell.setTimedOut();
                    p.destroy();
                }
            }
        }
    }

    public static void destroyAllShellProcesses() {
        synchronized (CHILD_SHELLS) {
            for(Shell shell : CHILD_SHELLS.keySet()) {
                if(shell.getProcess() != null) {
                    shell.getProcess().destroy();
                }
            }
            CHILD_SHELLS.clear();
        }
    }

    /**
     * Map 转 Set
     * @return
     */
    public static Set<Shell> getAllShells() {
        synchronized (CHILD_SHELLS) {
            return new HashSet<>(CHILD_SHELLS.keySet());
        }
    }
    /**
     * 静态方法 返回内存锁限制 用于 datanode
     * @return
     */
    public static Long getMemlockLimit(Long ulimit) {
        if(WINDOWS) {
            return Math.min(Integer.MAX_VALUE, ulimit);
        }
        return ulimit;
    }


}
