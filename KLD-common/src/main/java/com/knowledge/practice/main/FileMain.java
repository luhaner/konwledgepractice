package com.knowledge.practice.main;

import java.io.*;
import java.net.*;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * File类练习
 */
public class FileMain {

    public static void main(String[] args) throws IOException {
        /*copyFile();
        readFileContent();
        testURLClass();*/
        testSocket();
    }



    public static void copyFile() throws IOException {
        FilePractice filePractice = new FilePractice();
        String projectPath = System.getProperty("user.dir");
        String filePath = projectPath + "/KLD-common/src/main/java/com/knowledge/practice/main/";
        File file = new File(filePath + "filetest1.txt");
        if (!file.exists()) {
            file.createNewFile();
        }
        filePractice.copyFile(filePath + "filetest.txt", filePath + "filetest1.txt");
    }

    public static void readFileContent() throws IOException {
        FilePractice filePractice = new FilePractice();
        String projectPath = System.getProperty("user.dir");
        String filePath = projectPath + "/KLD-common/src/main/java/com/knowledge/practice/main/";
        filePractice.readFileContent(filePath + "filetest.txt");
    }

    public static void testURLClass() throws IOException {
        FilePractice filePractice = new FilePractice();
        filePractice.testURLClass();
    }

    public static void testSocket() {
        FilePractice filePractice = new FilePractice();
        filePractice.testSocket();
    }
}


class FilePractice {
    /**
     * 文件操作，递归列出一个文件下所有文件
     */
    public void listAllFiles(File file) {
        if (null == file || !file.exists()) {
            return;
        }
        if (file.isFile()) {
            System.out.println(file.getName());
            return;
        }
        for (File sFile : file.listFiles()) {
            listAllFiles(sFile);
        }
    }

    /**
     * 字节操作，实现文件复制
     */
    public void copyFile(String src, String dist) throws IOException {
        FileInputStream in = new FileInputStream(src);
        FileOutputStream out = new FileOutputStream(dist);
        byte[] buff = new byte[20 * 2048];
        int count;
        while ((count = in.read(buff, 0, buff.length)) != -1) {
            out.write(buff, 0, count);
        }
        in.close();
        out.close();
    }

    public void readFileContent(String filePath) throws IOException {
        FileReader fileReader = new FileReader(filePath);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        String line;
        if ((line = bufferedReader.readLine()) != null) {
            System.out.println(line);
        }

        // 装饰者模式使得 BufferedReader 组合了一个 Reader 对象
        // 在调用 BufferedReader 的 close() 方法时会去调用 Reader 的 close() 方法
        // 因此只要一个 close() 调用即可
        bufferedReader.close();
    }

    public void testURLClass() throws IOException {
        URL url = new URL("http://www.baidu.com");
        InputStream inputStream = url.openStream();
        InputStreamReader reader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(reader);
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            System.out.println(line);
        }
    }

    public void testSocket() {
        int count = 2;
        CyclicBarrier cyclicBarrier = new CyclicBarrier(count);
        Runnable serverRunnable = new Runnable() {
            @Override
            public synchronized void run() {
                try {
                    ServerSocket serverSocket = new ServerSocket(8777);
                    System.out.println("服务器端已准备");
                    // 获取socket
                    Socket socket = serverSocket.accept();
                    // 获取输入字节流
                    InputStream inputStream = socket.getInputStream();
                    // 获取输入字符流
                    InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                    // 获取输入字符流的装饰者
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                    cyclicBarrier.await();
                    String line;
                    while((line = bufferedReader.readLine()) != null) {
                        System.out.println(line);
                    }
                    bufferedReader.close();
                    socket.close();
                    serverSocket.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        Runnable clientRunnable = new Runnable() {
            @Override
            public synchronized void run() {
                try {
                    Socket socket = new Socket("10.126.1.138", 8777);
                    System.out.println("客户端已准备");
                    // 获取输出字节流
                    OutputStream outputStream = socket.getOutputStream();
                    // 获取输出字符流
                    OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream);
                    // 获取输出字符流的装饰者
                    BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);
                    cyclicBarrier.await();
                    // 输出数据
                    bufferedWriter.write("你才不是一个没有故事的女同学");
                    bufferedWriter.close();
                    socket.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        executorService.execute(serverRunnable);
        executorService.execute(clientRunnable);
        executorService.shutdown();
    }
}
