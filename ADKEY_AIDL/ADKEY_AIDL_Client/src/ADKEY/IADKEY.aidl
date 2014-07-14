package ADKEY;
import  ADKEY.AD;
interface IADKEY {	
	AD	   getNetWorkAD(String publisherId,String TYPE);
	AD	   getLocalAD(String publisherId);
	void   ReportShow(String publisherId);
	void   ReportClick(String publisherId);
}