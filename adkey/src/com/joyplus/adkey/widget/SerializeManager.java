package com.joyplus.adkey.widget;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;

/**
 * @author yyc
 *
 */
public class SerializeManager{
	/**
	 * @param path Set a Serializble file to local file
	 * @param o,the Object you write must implements Serializable
	 */
	public void writeSerializableData(String path, Object o){
		
		try
		{
			FileOutputStream fop = new FileOutputStream(path);
			ObjectOutputStream oos = new ObjectOutputStream(fop);
			oos.writeObject(o);
			oos.close();
		} catch (FileNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			
		}
	}
	
	/*
	 * recovery a class from path's file
	 */
	@SuppressWarnings("finally")
	public Object readSerializableData(String path){
		Object yyc = null ;
		try
		{
			FileInputStream fis = new FileInputStream(path);
			ObjectInputStream ois = new ObjectInputStream(fis);
			yyc = (Object)ois.readObject();
			ois.close();
			return yyc;
		} catch (FileNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (StreamCorruptedException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			return yyc;
		}
	}
}