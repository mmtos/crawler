package localSupProJ.util;

public class util {
	public static String chkNull(Object object){
		String reStr = "";
		if(object == null){
			reStr = "";
		}else{
			reStr = object.toString();
		}
		
		return reStr;
	}
	
	public static boolean isNum(String value) {
		try {
			Double.parseDouble( value );
			return true;
		} catch(NumberFormatException nfe) {
			return false;
		}
	}
}
