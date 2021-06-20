package org.lixl.acm;

import java.util.Scanner;

/**
 * Created by Administrator on 2/26/2018.
 *
 * 最简单的ACM题：输入A 和 B，输出 A + B 的值
 * 运行时，
 *      如果使用 Scanner 来添加 输入参数，则 以 run 'AddInFromConsole.main()' with Coverage ，即可输入参数
 *      如果使用 args 来添加 输入参数，则 Run/Debug Configuration 中配置 program arguments 即可。
 */
public class AddIntFromConsole {

    public static void main(String[] args) throws Exception{
        Scanner scanner = new Scanner(System.in);
        int a = scanner.nextInt();
        int b = scanner.nextInt();
        System.out.println(a + b);

        int c = Integer.parseInt(args[0]);
        int d = Integer.parseInt(args[1]);
        System.out.println(args[0] + " " + args[1]);
        System.out.println(c+d);
    }
}
