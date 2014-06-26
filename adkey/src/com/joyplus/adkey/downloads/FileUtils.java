package com.joyplus.adkey.downloads;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import com.joyplus.adkey.widget.Log;
import android.annotation.SuppressLint;
import android.os.Environment;

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
    	Log.d("deleteFiles "+((path==null)?"null":path));
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
        try {
            InputStream in = new FileInputStream(srcFile);
            if (dstFile.exists()) {
                dstFile.delete();
            }
        
            OutputStream out = new FileOutputStream(dstFile);
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
    
}
