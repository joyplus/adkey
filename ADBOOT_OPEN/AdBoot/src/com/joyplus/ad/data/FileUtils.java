package com.joyplus.ad.data;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.joyplus.ad.AdSDKManager;
import com.joyplus.ad.AdSDKManager.CUSTOMTYPE;
import com.joyplus.ad.config.Log;

import android.annotation.SuppressLint;
import android.os.Environment;
import android.os.StatFs;

/** File copy/delete/move */
@SuppressLint("NewApi")
public class FileUtils {
    /*Delete file*/
	public static boolean deleteFile(final String filepath){
		Log.d("deleteFile "+((filepath==null)?"null":filepath));
		boolean ret = true;
		File f = new File(filepath);
		if(!f.exists())return true;
		if(f.isFile())
			ret = f.delete() == false || ret == false ? false : true;
		return ret;
	}
    /** Delete files or folders */
    public static boolean deleteFiles(final String path) {
        boolean ret = true;
        File f = new File(path);
        if(!f.exists())return true;
        if (!f.isDirectory()) {
            ret = f.delete() == false || ret == false ? false : true;
        }else {
            File[] files = f.listFiles();
            if (files != null) {
                // Get folders list
                for (int i = 0; i < files.length; i++) {
                    ret = deleteFiles(files[i].getPath()) == false || ret == false ? false : true;
                    if (ret == false)
                        return false;
                }
            }
            ret = f.delete() == false || ret == false ? false : true;
        }
        return ret;
    }
    /*add 777 for file*/
    public static boolean Chmod(File srcFile){
    	if(srcFile==null || !srcFile.exists())return false;
    	return Chmod(srcFile,"777");
    }
    
    @SuppressLint("NewApi")
	public static boolean Chmod(File srcFile,String mode){
    	 boolean resault = true;
    	 if(srcFile == null || !srcFile.exists())return false;
         try {
        	srcFile.setWritable(true);
        	srcFile.setReadable(true);
            Process p = Runtime.getRuntime().exec("chmod "+mode+" "+srcFile.toString());
            Log.d(" "+("chmod "+mode+" "+srcFile.toString()));
            int status = p.waitFor();
            if (status == 0) {
               resault = true;
            } else {
               resault = false;
            }
         } catch (IOException e) {
           e.printStackTrace();
         } catch (InterruptedException e) {
           e.printStackTrace();
         }
         return resault;
    }
    /** Copy folders */
    private static boolean copyDir(File srcDir, File dstDir) {
        if (dstDir.exists()) {
            if (dstDir.isDirectory() == false) {
                return false;
            }
        } else {
            if (dstDir.mkdirs() == false) {
                return false;
            }
        }
        if (dstDir.canWrite() == false) {
            return false;
        }

        File[] files = srcDir.listFiles();
        if (files == null) {
            return false;
        }
        
        for (int i = 0; i < files.length; i++) {
            File dstFile = new File(dstDir, files[i].getName());
            if (files[i].isDirectory()) {
                copyDir(files[i], dstFile);
            } else {
                copyFile(files[i], dstFile);
            }
        }
        
        return true;
    }

    /** Move files or folders */
    public static boolean moveFiles(File srcFile, File dstFile) {
        boolean ret = true;
        ret = srcFile.compareTo(dstFile) != 0
                && (srcFile.renameTo(dstFile) || copyFiles(srcFile, dstFile));
        if (ret) {
            deleteFiles(srcFile.getPath());
        }
        return ret;
    }

    /** Copy files or folders */
    public static boolean copyFiles(File srcFile, File dstFile) {
        boolean ret = true;

        if (srcFile.getParent().equals(dstFile)) {
            return false;
        }
        
        if (srcFile.isDirectory()) {
            if (dstFile.getPath().indexOf(srcFile.getPath()) == 0) {
                return false;
            }
            else {
                if (copyDir(srcFile, dstFile) == false) {
                    return false;
                }
            }
        }
        else {
            ret = copyFile(srcFile, dstFile);
        }

        return ret;
    }

    /** Copy binary file */
    public static boolean copyFile(File srcFile, File dstFile) {
    	Log.d("copyFile from "+srcFile.toString()+" to "+dstFile.toString());
        try {
        	if(CUSTOMTYPE.LENOVO==AdSDKManager.GetCustomType()
        			&&!CheckSpaceSize(srcFile))return dstFile.exists();
        	File temp = new File(dstFile.toString()+".tmp");
            InputStream in = new FileInputStream(srcFile);
            if (!deleteFile(temp.toString()))return false;
            OutputStream out = new FileOutputStream(temp);
            try {
                int cnt;
                byte[] buf = new byte[4096];
                while ((cnt = in.read(buf)) >= 0) {
                    out.write(buf, 0, cnt);
                }
            } finally {
                out.close();
                in.close();
            }
            if (!deleteFile(dstFile.toString())){
            	deleteFile(temp.toString());
            	return false;
            }
            temp.renameTo(dstFile);
            return true;
        } catch (IOException e) {
            return false;
        }
    }
    
    public static boolean fileExist(String fileName){
        boolean ret = false;        
        File f = new File(fileName);
        if (f.exists()) {
            ret = true;
        }
        return ret;
    }
    
    public static boolean SDExist(){
        boolean ret = false;        
        if (Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
            ret = true;
        }        
        return ret;
    }
    
    public static String getSDPath(){ 
        File sd = null;
        sd = Environment.getExternalStorageDirectory();
        return sd.toString(); 
    }
    public static long getFileSizes(File f){
    	  FileInputStream fis = null;
	      try {
	    	  fis = new FileInputStream(f);
	    	  return fis.available();
		  } catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		  } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		  }finally{
				try {
					 if(fis!=null)fis.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		  }
	      return -1;
    }
    
    private static boolean CheckSpaceSize(File srcFile){
    	if(srcFile==null||!srcFile.exists())return false;
    	long size = getSystemFreeSpace();
    	Log.d("FreeSpace="+size+",srcFileSize="+srcFile.length());
    	if(srcFile.length()>0&& size>srcFile.length())return true;
    	return false;
    }
    //////////////////////////////////////////////////
    //lenovo提供的方法,只在联想机器上使用
    public static long getSystemFreeSpace() {
        File root = Environment.getDataDirectory();
        StatFs sf = new StatFs(root.getPath());
        long blockSize = sf.getBlockSize();
        long availCount = sf.getAvailableBlocks();
        long sysspace = availCount * blockSize;
        Log.d("getSystemSpace", "getSystemSpace=" + sysspace);
        return sysspace;
    }

    
}
