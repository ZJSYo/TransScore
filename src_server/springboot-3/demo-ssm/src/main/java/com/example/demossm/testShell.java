package com.example.demossm;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class testShell {
    public static void main(String[] args) {
        List<String> commands = new ArrayList<>();
        commands.add("ls"); // 第一条指令
        commands.add("pwd"); // 第二条指令

        // 使用ProcessBuilder执行多条指令
        ProcessBuilder processBuilder = new ProcessBuilder(commands);

        try {
            Process process = processBuilder.start();
            // 读取进程的输出
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
