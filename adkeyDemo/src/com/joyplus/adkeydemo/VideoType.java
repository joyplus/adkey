package com.joyplus.adkeydemo;

import java.util.ArrayList;
import java.util.HashMap;

public class VideoType {
	
	  private static final HashMap<Integer, String> mVIDEO;
	  private static final HashMap<Integer, String> VIDEOTYPE;
	  
	  static{
		  mVIDEO     = new HashMap<Integer, String>();
		  //video
		  mVIDEO.put(VIDEO.MOVIE.toInt(), VIDEO.MOVIE.toString());
		  mVIDEO.put(VIDEO.TV.toInt(), VIDEO.TV.toString());
		  mVIDEO.put(VIDEO.ZONGYI.toInt(), VIDEO.ZONGYI.toString());
		  mVIDEO.put(VIDEO.DONGMAN.toInt(), VIDEO.DONGMAN.toString());
		  
		  VIDEOTYPE = new HashMap<Integer, String>();
		  //电影
		  VIDEOTYPE.put(VIDEOTYPE_MOVIE.KONGBU.toInt(), VIDEOTYPE_MOVIE.KONGBU.toString());
		  VIDEOTYPE.put(VIDEOTYPE_MOVIE.JINGSONG.toInt(), VIDEOTYPE_MOVIE.JINGSONG.toString());
		  VIDEOTYPE.put(VIDEOTYPE_MOVIE.XUNAYI.toInt(), VIDEOTYPE_MOVIE.XUNAYI.toString());
		  VIDEOTYPE.put(VIDEOTYPE_MOVIE.LUNLI.toInt(), VIDEOTYPE_MOVIE.LUNLI.toString());
		  VIDEOTYPE.put(VIDEOTYPE_MOVIE.AIQING.toInt(), VIDEOTYPE_MOVIE.AIQING.toString());
		  VIDEOTYPE.put(VIDEOTYPE_MOVIE.JUQING.toInt(), VIDEOTYPE_MOVIE.JUQING.toString());
		  VIDEOTYPE.put(VIDEOTYPE_MOVIE.XIJU.toInt(), VIDEOTYPE_MOVIE.XIJU.toString());
		  VIDEOTYPE.put(VIDEOTYPE_MOVIE.KEHUAN.toInt(), VIDEOTYPE_MOVIE.KEHUAN.toString());
		  VIDEOTYPE.put(VIDEOTYPE_MOVIE.DONGZUO.toInt(), VIDEOTYPE_MOVIE.DONGZUO.toString());
		  VIDEOTYPE.put(VIDEOTYPE_MOVIE.ZHANZHENG.toInt(), VIDEOTYPE_MOVIE.ZHANZHENG.toString());
		  VIDEOTYPE.put(VIDEOTYPE_MOVIE.MAOXIAN.toInt(), VIDEOTYPE_MOVIE.MAOXIAN.toString());
		  VIDEOTYPE.put(VIDEOTYPE_MOVIE.YINYUE.toInt(), VIDEOTYPE_MOVIE.YINYUE.toString());
		  VIDEOTYPE.put(VIDEOTYPE_MOVIE.DONGHUA.toInt(), VIDEOTYPE_MOVIE.DONGHUA.toString());
		  VIDEOTYPE.put(VIDEOTYPE_MOVIE.YUNDONG.toInt(), VIDEOTYPE_MOVIE.YUNDONG.toString());
		  VIDEOTYPE.put(VIDEOTYPE_MOVIE.QIHUAN.toInt(), VIDEOTYPE_MOVIE.QIHUAN.toString());
		  VIDEOTYPE.put(VIDEOTYPE_MOVIE.ZHUANJI.toInt(), VIDEOTYPE_MOVIE.ZHUANJI.toString());
		  VIDEOTYPE.put(VIDEOTYPE_MOVIE.GUZHUANG.toInt(), VIDEOTYPE_MOVIE.GUZHUANG.toString());
		  VIDEOTYPE.put(VIDEOTYPE_MOVIE.FANZUI.toInt(), VIDEOTYPE_MOVIE.FANZUI.toString());
		  VIDEOTYPE.put(VIDEOTYPE_MOVIE.WUXIA.toInt(), VIDEOTYPE_MOVIE.WUXIA.toString());
		  VIDEOTYPE.put(VIDEOTYPE_MOVIE.QITA.toInt(), VIDEOTYPE_MOVIE.QITA.toString());
		  //电视剧
		  VIDEOTYPE.put(VIDEOTYPE_TV.JUQING.toInt(), VIDEOTYPE_TV.JUQING.toString());
		  VIDEOTYPE.put(VIDEOTYPE_TV.QINGGAN.toInt(), VIDEOTYPE_TV.QINGGAN.toString());
		  VIDEOTYPE.put(VIDEOTYPE_TV.QINGCHUNOUXIANG.toInt(), VIDEOTYPE_TV.QINGCHUNOUXIANG.toString());
		  VIDEOTYPE.put(VIDEOTYPE_TV.JIATINGLUNLI.toInt(), VIDEOTYPE_TV.JIATINGLUNLI.toString());
		  VIDEOTYPE.put(VIDEOTYPE_TV.XIJU.toInt(), VIDEOTYPE_TV.XIJU.toString());
		  VIDEOTYPE.put(VIDEOTYPE_TV.FANZUI.toInt(), VIDEOTYPE_TV.FANZUI.toString());
		  VIDEOTYPE.put(VIDEOTYPE_TV.ZHANZHENG.toInt(), VIDEOTYPE_TV.ZHANZHENG.toString());
		  VIDEOTYPE.put(VIDEOTYPE_TV.GUZHUANG.toInt(), VIDEOTYPE_TV.GUZHUANG.toString());
		  VIDEOTYPE.put(VIDEOTYPE_TV.DONGZUO.toInt(), VIDEOTYPE_TV.DONGZUO.toString());
		  VIDEOTYPE.put(VIDEOTYPE_TV.QIHUAN.toInt(), VIDEOTYPE_TV.QIHUAN.toString());
		  VIDEOTYPE.put(VIDEOTYPE_TV.JINGDIAN.toInt(), VIDEOTYPE_TV.JINGDIAN.toString());
		  VIDEOTYPE.put(VIDEOTYPE_TV.XIANGCUN.toInt(), VIDEOTYPE_TV.XIANGCUN.toString());
		  VIDEOTYPE.put(VIDEOTYPE_TV.SHANGZHAN.toInt(), VIDEOTYPE_TV.SHANGZHAN.toString());
		  VIDEOTYPE.put(VIDEOTYPE_TV.LISHI.toInt(), VIDEOTYPE_TV.LISHI.toString());
		  VIDEOTYPE.put(VIDEOTYPE_TV.QINGJING.toInt(), VIDEOTYPE_TV.QINGJING.toString());
		  VIDEOTYPE.put(VIDEOTYPE_TV.TVB.toInt(), VIDEOTYPE_TV.TVB.toString());
		  VIDEOTYPE.put(VIDEOTYPE_TV.QITA.toInt(), VIDEOTYPE_TV.QITA.toString());
		  //综艺
		  VIDEOTYPE.put(VIDEOTYPE_ZONGYI.ZONGYI.toInt(), VIDEOTYPE_ZONGYI.ZONGYI.toString());
		  VIDEOTYPE.put(VIDEOTYPE_ZONGYI.XUANXIU.toInt(), VIDEOTYPE_ZONGYI.XUANXIU.toString());
		  VIDEOTYPE.put(VIDEOTYPE_ZONGYI.QINGGAN.toInt(), VIDEOTYPE_ZONGYI.QINGGAN.toString());
		  VIDEOTYPE.put(VIDEOTYPE_ZONGYI.FANGTAN.toInt(), VIDEOTYPE_ZONGYI.FANGTAN.toString());
		  VIDEOTYPE.put(VIDEOTYPE_ZONGYI.BOBAO.toInt(), VIDEOTYPE_ZONGYI.BOBAO.toString());
		  VIDEOTYPE.put(VIDEOTYPE_ZONGYI.LVYOU.toInt(), VIDEOTYPE_ZONGYI.LVYOU.toString());
		  VIDEOTYPE.put(VIDEOTYPE_ZONGYI.YINYUE.toInt(), VIDEOTYPE_ZONGYI.YINYUE.toString());
		  VIDEOTYPE.put(VIDEOTYPE_ZONGYI.MEISHI.toInt(), VIDEOTYPE_ZONGYI.MEISHI.toString());
		  VIDEOTYPE.put(VIDEOTYPE_ZONGYI.JISHI.toInt(), VIDEOTYPE_ZONGYI.JISHI.toString());
		  VIDEOTYPE.put(VIDEOTYPE_ZONGYI.QUYI.toInt(), VIDEOTYPE_ZONGYI.QUYI.toString());
		  VIDEOTYPE.put(VIDEOTYPE_ZONGYI.SHENGHUO.toInt(), VIDEOTYPE_ZONGYI.SHENGHUO.toString());
		  VIDEOTYPE.put(VIDEOTYPE_ZONGYI.YOUXIHUDONG.toInt(), VIDEOTYPE_ZONGYI.YOUXIHUDONG.toString());
		  VIDEOTYPE.put(VIDEOTYPE_ZONGYI.CAIJING.toInt(), VIDEOTYPE_ZONGYI.CAIJING.toString());
		  VIDEOTYPE.put(VIDEOTYPE_ZONGYI.QIUZHI.toInt(), VIDEOTYPE_ZONGYI.QIUZHI.toString());
		  VIDEOTYPE.put(VIDEOTYPE_ZONGYI.QITA.toInt(), VIDEOTYPE_ZONGYI.QITA.toString());
		  //动漫
		  VIDEOTYPE.put(VIDEOTYPE_DONGMAN.QINGGAN.toInt(), VIDEOTYPE_DONGMAN.QINGGAN.toString());
		  VIDEOTYPE.put(VIDEOTYPE_DONGMAN.KEHUAN.toInt(), VIDEOTYPE_DONGMAN.KEHUAN.toString());
		  VIDEOTYPE.put(VIDEOTYPE_DONGMAN.REXUE.toInt(), VIDEOTYPE_DONGMAN.REXUE.toString());
		  VIDEOTYPE.put(VIDEOTYPE_DONGMAN.TUILI.toInt(), VIDEOTYPE_DONGMAN.TUILI.toString());
		  VIDEOTYPE.put(VIDEOTYPE_DONGMAN.GAOXIAO.toInt(), VIDEOTYPE_DONGMAN.GAOXIAO.toString());
		  VIDEOTYPE.put(VIDEOTYPE_DONGMAN.MAOXIAN.toInt(), VIDEOTYPE_DONGMAN.MAOXIAN.toString());
		  VIDEOTYPE.put(VIDEOTYPE_DONGMAN.LUOLI.toInt(), VIDEOTYPE_DONGMAN.LUOLI.toString());
		  VIDEOTYPE.put(VIDEOTYPE_DONGMAN.XIAOYUAN.toInt(), VIDEOTYPE_DONGMAN.XIAOYUAN.toString());
		  VIDEOTYPE.put(VIDEOTYPE_DONGMAN.DONGZUO.toInt(), VIDEOTYPE_DONGMAN.DONGZUO.toString());
		  VIDEOTYPE.put(VIDEOTYPE_DONGMAN.JIZHAN.toInt(), VIDEOTYPE_DONGMAN.JIZHAN.toString());
		  VIDEOTYPE.put(VIDEOTYPE_DONGMAN.DANMEI.toInt(), VIDEOTYPE_DONGMAN.DANMEI.toString());
		  VIDEOTYPE.put(VIDEOTYPE_DONGMAN.ZHANZHENG.toInt(), VIDEOTYPE_DONGMAN.ZHANZHENG.toString());
		  VIDEOTYPE.put(VIDEOTYPE_DONGMAN.SHAONIAN.toInt(), VIDEOTYPE_DONGMAN.SHAONIAN.toString());
		  VIDEOTYPE.put(VIDEOTYPE_DONGMAN.SHAONV.toInt(), VIDEOTYPE_DONGMAN.SHAONV.toString());
		  VIDEOTYPE.put(VIDEOTYPE_DONGMAN.SHEHUI.toInt(), VIDEOTYPE_DONGMAN.SHEHUI.toString());
		  VIDEOTYPE.put(VIDEOTYPE_DONGMAN.YUANCHUANG.toInt(), VIDEOTYPE_DONGMAN.YUANCHUANG.toString());
		  VIDEOTYPE.put(VIDEOTYPE_DONGMAN.QINZI.toInt(), VIDEOTYPE_DONGMAN.QINZI.toString());
		  VIDEOTYPE.put(VIDEOTYPE_DONGMAN.YIZHI.toInt(), VIDEOTYPE_DONGMAN.YIZHI.toString());
		  VIDEOTYPE.put(VIDEOTYPE_DONGMAN.LIZHI.toInt(), VIDEOTYPE_DONGMAN.LIZHI.toString());
		  VIDEOTYPE.put(VIDEOTYPE_DONGMAN.BAIHE.toInt(), VIDEOTYPE_DONGMAN.BAIHE.toString());
		  VIDEOTYPE.put(VIDEOTYPE_DONGMAN.QITA.toInt(), VIDEOTYPE_DONGMAN.QITA.toString());
	  }
	  
	  public static String GetType(int index){
		  return VIDEOTYPE.get(index);
	  }
	  public static String GetVideo(int index){
		  return mVIDEO.get(index);
	  }
	  public static String[] GetVideoItems(){
		  String[] items = new String[4];
		  for(int i=0;i<4;i++){
			  items[i]=mVIDEO.get(i+1);
		  }
		  return items;
	  }
	  public static String[] GetVideo_MOVIE(){
		  String[] items = new String[20];
		  for(int i=0;i<20;i++){
			  items[i]=VIDEOTYPE.get(i+101);
		  }
		  return items;
	  }
	  public static String[] GetVideo_TV(){
		  String[] items = new String[17];
		  for(int i=0;i<17;i++){
			  items[i]=VIDEOTYPE.get(i+201);
		  }
		  return items;
	  }
	  public static String[] GetVideo_ZONGYI(){
		  String[] items = new String[15];
		  for(int i=0;i<15;i++){
			  items[i]=VIDEOTYPE.get(i+301);
		  }
		  return items;
	  }
	  public static String[] GetVideo_DONGMAN(){
		  String[] items = new String[21];
		  for(int i=0;i<21;i++){
			  items[i]=VIDEOTYPE.get(i+401);
		  }
		  return items;
	  }
	  public enum VIDEO{
		  MOVIE   (1,"电影"),
		  TV      (2,"电视剧"),
		  ZONGYI  (3,"综艺"),
		  DONGMAN (4,"动漫");
		  private int Index;
    	  private String Type;
    	  VIDEO(int index,String type){
    		  Type  = type;
    		  Index = index;
    	  }
    	  public int toInt(){
    		  return Index;
    	  }
    	  public String toString(){
    		  return Type;
    	  }
	  }
      public enum VIDEOTYPE_MOVIE{
    	  KONGBU    (101,"恐怖"),
    	  JINGSONG  (102,"惊悚"),
    	  XUNAYI    (103,"悬疑"),
    	  LUNLI     (104,"伦理"),
    	  AIQING    (105,"爱情"),
    	  JUQING    (106,"剧情"),
    	  XIJU      (107,"喜剧"),
    	  KEHUAN    (108,"科幻"),
    	  DONGZUO   (109,"动作"),
    	  ZHANZHENG (110,"战争"),
    	  MAOXIAN   (111,"冒险"),
    	  YINYUE    (112,"音乐"),
    	  DONGHUA   (113,"动画"),
    	  YUNDONG   (114,"运动"),
    	  QIHUAN    (115,"奇幻"),
    	  ZHUANJI   (116,"传记"),
    	  GUZHUANG  (117,"古装"),
    	  FANZUI    (118,"犯罪"),
    	  WUXIA     (119,"武侠"),
    	  QITA      (120,"其它");    	  
    	  
    	  private int Index;
    	  private String Type;
    	  VIDEOTYPE_MOVIE(int index,String type){
    		  Type  = type;
    		  Index = index;
    	  }
    	  public int toInt(){
    		  return Index;
    	  }
    	  public String toString(){
    		  return Type;
    	  }
      }
      
      public enum VIDEOTYPE_TV{
    	  JUQING          (201,"剧情"),
    	  QINGGAN         (202,"情感"),
    	  QINGCHUNOUXIANG (203,"青春偶像"),
    	  JIATINGLUNLI    (204,"家庭伦理"),
    	  XIJU            (205,"喜剧"),
    	  FANZUI          (206,"犯罪"),
    	  ZHANZHENG       (207,"战争"),
    	  GUZHUANG        (208,"古装"),
    	  DONGZUO         (209,"动作"),
    	  QIHUAN          (210,"奇幻"),
    	  JINGDIAN        (211,"经典"),
    	  XIANGCUN        (212,"乡村"),
    	  SHANGZHAN       (213,"商战"),
    	  LISHI           (214,"历史"),
    	  QINGJING        (215,"情景"),
    	  TVB             (216,"TVB"),
    	  QITA            (217,"其它");
    	  private int    Index;
    	  private String Type;
    	  VIDEOTYPE_TV(int index,String type){
    		  Index = index;
    		  Type  = type;
    	  }
    	  public int toInt(){
    		  return Index;
    	  }
    	  public String toString(){
    		  return Type;
    	  }
      }
      
      public enum VIDEOTYPE_ZONGYI{
    	  ZONGYI      (301,"综艺"),
    	  XUANXIU     (302,"选秀"),
    	  QINGGAN     (303,"情感"),
    	  FANGTAN     (304,"访谈"),
    	  BOBAO       (305,"播报"),
    	  LVYOU       (306,"旅游"),
    	  YINYUE      (307,"音乐"),
    	  MEISHI      (308,"美食"),
    	  JISHI       (309,"纪实"),
    	  QUYI        (310,"曲艺"),
    	  SHENGHUO    (311,"生活"),
    	  YOUXIHUDONG (312,"游戏互动"),
    	  CAIJING     (313,"财经"),
    	  QIUZHI      (314,"求职"),
    	  QITA        (315,"其它");
    	  private int    Index;
    	  private String Type;
    	  VIDEOTYPE_ZONGYI(int index,String type){
    		  Index = index;
    		  Type  = type;
    	  }
    	  public int toInt(){
    		  return Index;
    	  }
    	  public String toString(){
    		  return Type;
    	  }
      }
      
     public enum VIDEOTYPE_DONGMAN{
    	  QINGGAN    (401,"情感"),
    	  KEHUAN     (402,"科幻"),
    	  REXUE      (403,"热血"),
    	  TUILI      (404,"推理"),
    	  GAOXIAO    (405,"搞笑"),
    	  MAOXIAN    (406,"冒险"),
    	  LUOLI      (407,"萝莉"),
    	  XIAOYUAN   (408,"校园"),
    	  DONGZUO    (409,"动作"),
    	  JIZHAN     (410,"机战"),
    	  DANMEI     (411,"耽美"),
    	  ZHANZHENG  (412,"战争"),
    	  SHAONIAN   (413,"少年"),
    	  SHAONV     (414,"少女"),
    	  SHEHUI     (415,"社会"),
    	  YUANCHUANG (416,"原创"),
    	  QINZI      (417,"亲子"),
    	  YIZHI      (418,"益智"),
    	  LIZHI      (419,"励志"),
    	  BAIHE      (420,"百合"),
    	  QITA       (421,"其它");
    	  private int    Index;
    	  private String Type;
    	  VIDEOTYPE_DONGMAN(int index,String type){
    		  Index = index;
    		  Type  = type;
    	  }
    	  public int toInt(){
    		  return Index;
    	  }
    	  public String toString(){
    		  return Type;
    	  }
      }
}
