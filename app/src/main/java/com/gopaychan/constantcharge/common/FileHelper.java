package com.gopaychan.constantcharge.common;

import android.content.Context;
import android.os.Environment;

import java.io.File;

/**
 * 文件助手
 * @author BehindMayday
 *
 */
public class FileHelper {
	
//	private static final String TAG = "FileHelper";
	
	/**
	 * 检查手机是否有SDCard
	 * @return
	 */
	public static boolean isHasSDCard()
	{
		if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	/**
	 * 获取手机的根目录
	 * @param context
	 * @return
	 */
	public static String getRootFilePath(Context context)
	{
		if(isHasSDCard())
		{
			return Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator;
		}
		else
		{
			return context.getCacheDir().getAbsolutePath() + File.separator;
		}
	}
	
	/**
	 * 获取文件保存的目录
	 * @param context
	 * @return
	 */
	public static String getFileSavePath(Context context, String fileDirectoryName)
	{
		return getRootFilePath(context) + "ConstantCharge/" + fileDirectoryName + File.separator;
	}
	
	/**
	 * 创建指定目录
	 * @param filePath
	 * @return
	 */
	public static boolean createDirectory(String filePath)
	{
		if(filePath == null)
		{
			return false;
		}
		else
		{
			File file = new File(filePath);
			if(file.exists())
			{
				return true;
			}
			else
			{
				return file.mkdirs();
			}
		}
	}
	
	/**
	 * 删除指定目录
	 * @param filePath
	 * @return
	 */
	public static boolean deleteDirectory(String filePath)
	{
		if(filePath == null)
		{
			return false;
		}
		else
		{
			File file = new File(filePath);
			if(file == null || !file.exists())
			{
				return false;
			}
			else
			{
				if(file.isDirectory())
				{
					File[] fileList = file.listFiles();
					for(int i = 0; i < fileList.length; i++)
					{
						if(fileList[i].isDirectory())
						{
							deleteDirectory(fileList[i].getAbsolutePath());
						}
						else
						{
							fileList[i].delete();
						}
					}
					return true;
				}
				else
				{
					file.delete();
					return true;
				}
			}
		}
		
	}
	/**
	 * 获取一个File 的大小，以字节Byte为单位
	 * @param file
	 * @return
	 */
	 public static long getDirSize(File file) {
	        //判断文件是否存在     
	        if (file.exists()) {     
	            //如果是目录则递归计算其内容的总大小    
	            if (file.isDirectory()) {     
	                File[] children = file.listFiles();
	                long size = 0;     
	                for (File f : children)
	                    size += getDirSize(f);     
	                return size;     
	            } else {//如果是文件则直接返回其大小,以“字节”为单位   
	                long size = file.length();        
	                return size;     
	            }     
	        } else {     
	            return 0;     
	        }     
	    } 

}
