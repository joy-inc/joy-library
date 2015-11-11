package com.joy.library.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.io.Reader;
import java.io.Writer;

/**
 * IO工具类
 */
public class IOUtil {

	public static byte[] getFileBytes(String filePath) {
		
		FileInputStream input = null;
		ByteArrayOutputStream baos = null;
		try {

			input = new FileInputStream(filePath);
			baos = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int len = -1;
			while ((len = input.read(buffer)) != -1) {
				baos.write(buffer, 0, len);
			}
			return baos.toByteArray();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeInStream(input);
			closeOutStream(baos);
		}
		return null;
	}

	public static void closeInStream(InputStream input) {
		
		try {
		
			if (input != null)
				input.close();
		
		} catch (Exception e) {
		
			e.printStackTrace();
		}
	}

	public static void closeOutStream(OutputStream output) {
		
		try {
		
			if (output != null)
				output.close();
		
		} catch (Exception e) {
			
			e.printStackTrace();
		}
	}

	public static void closeReader(Reader reader) {
		
		try {

			if (reader != null)
				reader.close();

		} catch (Exception e) {
			
			e.printStackTrace();
		}
	}

	public static void closeWriter(Writer writer) {
		
		try {

			if (writer != null)
				writer.close();

		} catch (Exception e) {
		
			e.printStackTrace();
		}
	}

	public static void writeObj(Object obj, File dir, String fileName) {
		
		try{
			
			writeObj(obj, new File(dir, fileName));
			
		}catch(Exception e){
			
			e.printStackTrace();
		}
	}
	
	public static void writeObj(Object obj, File f) {
		
		ObjectOutputStream oos = null;
		try {

			oos = new ObjectOutputStream(new FileOutputStream(f));
			oos.writeObject(obj);

		} catch (Exception e) {
		
			e.printStackTrace();
		
		} finally {
			closeOutStream(oos);
		}
	}
	
	public static void closeRandomAccessFile(RandomAccessFile file){
		
		try{
			
			if(file != null)
				file.close();
			
		}catch(Exception e){
			
			e.printStackTrace();
		}
	}
	
	public static Object readObj(File dir, String fileName){
		
		try{
			
			File f = new File(dir, fileName);
			if(f.exists())
				return readObj(f);
			
		}catch(Exception e){
			
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static Object readObj(File f) {
		
		Object obj = null;
		ObjectInputStream oic = null;
		try {

			oic = new ObjectInputStream(new FileInputStream(f));
			obj = oic.readObject();

		} catch (Exception e) {
			
			e.printStackTrace();
		} finally {
			
			closeInStream(oic);
		}
		return obj;
	}
	
	public static boolean copyFileFromRaw(InputStream in, File outPutFile) {
		
		boolean result = false;
		FileOutputStream fos = null;

		try {
			
			fos = new FileOutputStream(outPutFile);
			
			byte[] buffer = new byte[1024];
			int count = 0;
			while ((count = in.read(buffer)) != -1) {
				
				fos.write(buffer, 0, count);
			}
			
			result = true;
			
		} catch (Exception e) {
			
			e.printStackTrace();
		} finally {
			
			closeInStream(in);
			closeOutStream(fos);
		}

		return result;
	}
	
	public static void ensureDirExist(String filePath) {
		
		File dirFile = new File(filePath);
		if (!dirFile.exists()) {
			dirFile.mkdirs();
		}
	}
	
	public static boolean deleteFile(File f){
		
		try{
			
			return f.exists() ? f.delete() : false;

		}catch(Exception e){
			
			return false;
		}
	}
	
	public static void deleteDir(File file, boolean delDirSelf) {

		if (file == null || !file.exists())
			return;

		if (file.isFile()) {

			file.delete();
		} else {

			File[] files = file.listFiles();
			if (files != null && files.length != 0) {

				for (File f : files) {
					deleteDir(f, true);// 递归删除每一个文件
				}
			}
			
			if(delDirSelf)
				file.delete();
		}
	}
	
	public static long getFileSize(File f){
		
		if(f == null || !f.exists())
			return 0;
		
		if(f.isDirectory()){
			
			File[] files = f.listFiles();
			if(files == null || files.length == 0)
				return 0;
			
			long totalSize = 0;
			for(int i=0; i<files.length; i++){
				
				totalSize += getFileSize(files[i]);
			}
			return totalSize;
					
		}else{
			
			return f.length();
		}
	}
	
	public static void writeTextToFile(File f, String text){
		
		FileWriter writer = null;
		try {
			
			writer = new FileWriter(f) ;
			writer.write(text);
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			
			closeWriter(writer);
		}
	}
}
