package io.mcc.mobile.common.config.event;

/**
 * 시스템 환경 조작 변수 
 */
public class StaticSystemConfig {

	
	/** MCC 시스템 점검 여부 **/
	public static boolean MCC_SYSTEM_CHECK_YN = false;
	
	
    public static void setMCC_SYSTEM_CHECK_YN(boolean val) {
    	MCC_SYSTEM_CHECK_YN = val;
    }
    
}