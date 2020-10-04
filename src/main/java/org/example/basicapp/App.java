package org.example.basicapp;

import java.io.File;

public class App {
    public static void main(String[] args) {
        String process = args[0];
        String keyAlias = args[1];

        var client = new S3CSEClient(keyAlias);
        var s3cse = new S3cse(client);
        File file;

        try {
            switch (process) {
                case "upload":
                    file = new File(args[2]);
                    s3cse.upload(file, args[3]);
                    break;
                case "download":
                    file = new File(args[3]);
                    s3cse.download(file, args[2]);
                    break;
                default:
                    System.out.println("第一引数には upload か download を指定してください。");
            }
        } catch (Exception e) {
            System.out.println("失敗しました：" + e);
        }
    }
}
