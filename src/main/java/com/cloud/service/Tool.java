package com.cloud.service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;

import org.springframework.web.multipart.MultipartFile;
import com.google.api.client.http.InputStreamContent;

public abstract class Tool {

	public static boolean uploadFile(MultipartFile filePart, String folderPathName, String fileName) {
		java.io.File folderPath = new java.io.File(folderPathName);
		java.io.File filePath = new java.io.File(folderPathName + fileName);
		if(!folderPath.exists()) {
			folderPath.mkdirs();
		}
		if(!filePath.exists()) {
			try {
				filePath.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				return false;
			}
		}
		try(InputStream inp = filePart.getInputStream();
				OutputStream out = new FileOutputStream(filePath)) {
			byte[] data = new byte[2048];
			int length = 0;
			while((length = inp.read(data)) != -1) {
				out.write(data, 0, length);
			}
			return true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}

	public static boolean deleteFile(java.io.File file) {
		if(file.exists()) {
			if(file.isFile()) {
				return file.delete();
			} else if(file.isDirectory()) {
				java.io.File[] files = file.listFiles();
				boolean flag = true;
				for(java.io.File tmp : files) {
					flag &= deleteFile(tmp);
				}
				flag &= file.delete();
				return flag;
			}
		}
		return false;
	}

	public static String getExtension(String file) {
		int lasDotIndex = file.lastIndexOf('.');
		if(lasDotIndex > 0) {
			file = file.substring(lasDotIndex + 1);
			return file;
		}
		return null;
	}
}
