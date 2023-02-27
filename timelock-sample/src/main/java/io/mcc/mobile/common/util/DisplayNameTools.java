package io.mcc.mobile.common.util;

public class DisplayNameTools {

    public static String displayName(String famnm, String givnmn, String nationCD) {
        String dispName = "";
        if ("KOR".equalsIgnoreCase(nationCD)) {
            dispName = (famnm==null?"":famnm) + " " + (givnmn==null?"":givnmn);
        } else {
            dispName = (givnmn==null?"":givnmn) + " " + (famnm==null?"":famnm);
        }
        return dispName.trim();
    }
}
