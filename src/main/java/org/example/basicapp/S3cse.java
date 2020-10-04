package org.example.basicapp;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class S3cse {
	private S3CSEClient client;

	public S3cse(S3CSEClient client) {
		this.client = client;
	}

	public void upload(File file, String s3uri) throws FileNotFoundException {
		s3uri += s3uri.endsWith("/") ? file.getName() : "";

		if (!file.exists())
			throw new FileNotFoundException(file.getAbsolutePath());
		if (file.isDirectory())
			throw new UnsupportedOperationException("Recursive upload is not implemented");

		if (!client.exists(s3uri) || isUserConfirmed()) {
			client.putObject(s3uri, file);
			System.out.println("upload 完了しました。");
		} else {
			System.out.println("処理を中断しました。");
		}
	}

	public void download(File file, String s3uri) {
		if (s3uri.endsWith("/"))
			throw new UnsupportedOperationException("Recursive download is not implemented");

		if (file.isDirectory()) {
			String fileName = file.getName() + File.separator + client.getBasename(s3uri);
			file = new File(fileName);
		}

		if (!file.exists() || isUserConfirmed()) {
			client.getObject(s3uri, file);
			System.out.println("download 完了しました。");
		} else {
			System.out.println("処理を中断しました。");
		}
	}

	private boolean isUserConfirmed() {
		String env = System.getenv("S3CSE_OVERWRITE");

		if (env != null) {
			return true;
		}

		System.out.println("すでに同じ名前のファイルが存在しています。上書き保存しますか？ [Y/N]");

		try (var scanner = new Scanner(System.in)) {
			String line = scanner.nextLine();
			return "y".equals(line.toLowerCase());
		}
	}
}
