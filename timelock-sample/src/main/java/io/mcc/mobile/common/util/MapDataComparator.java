package io.mcc.mobile.common.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import io.mcc.common.vo.CommonVO;

public class MapDataComparator implements Comparator<CommonVO>
{
	private String key1;
	private boolean sort = false;
	private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	public MapDataComparator(String k){
		this.key1 = k;
	}
	
	public MapDataComparator(String k, boolean sort){
		this.key1 = k;
		this.sort = sort;
	}
	
	@Override
	public int compare(CommonVO o1, CommonVO o2) {
		//%Y-%m-%d %H:%i:%s
		if(o1.get(key1) != null){
			if(o1.get(key1) instanceof java.lang.Number) {
				int i = objToInt(o1.get(key1));
				int j = objToInt(o2.get(key1));
				if(sort){
					return compareDesc(i, j);
				} else {
					return compareAsc(i, j);
				}	
			} else if(o1.get(key1) instanceof java.lang.Float || o1.get(key1) instanceof java.lang.Double){
				float i = objToFloat(o1.get(key1));
				float j = objToFloat(o2.get(key1));
				if(sort){
					return compareDesc(i, j);
				} else {
					return compareAsc(i, j);
				}	
			} else {
				try {
					Date date =	dateFormat.parse(String.valueOf(o1.get(key1)));
					Date date2 = dateFormat.parse(String.valueOf(o2.get(key1)));
					int i = date.compareTo(date2);
					if(sort){
						return i;
					} else {
						if (i < 0) {
							return 1;
						} else if(i == 0) {
							return 0;
						} else {
							return -1;
						}
					}
				} catch (Exception e) {
					return 0;
				}
			}
		} else{
			return 0;
		}
	}
	
	private float objToFloat(Object i) {
		try {
			return Float.parseFloat(String.valueOf(i));
		} catch(Exception e) {
			return 0;
		}
	}
	
	private int objToInt(Object i) {
		try {
			return Integer.parseInt(String.valueOf(i));
		} catch(Exception e) {
			return 0;
		}
	}
	
	public int compare(CommonVO o1, CommonVO o2, String key) {
		int i = compare(o1, o2);
		return i;
	}
	
	private int compareDesc(float i, float j) {
		if(i > j){
			return -1;
		} else if(i < j){
			return 1;
		} else{
			return 0;
		}
	}
	
	private int compareAsc(float i, float j) {
		if(i > j){
			return 1;
		} else if(i < j){
			return -1;
		} else{
			return 0;
		}
	}
	
	private int compareDesc(int i, int j) {
		if(i > j){
			return -1;
		} else if(i < j){
			return 1;
		} else{
			return 0;
		}
	}
	
	private int compareAsc(int i, int j) {
		if(i > j){
			return 1;
		} else if(i < j){
			return -1;
		} else{
			return 0;
		}
	}
	
	
    @SuppressWarnings("unchecked")
    public CommonVO[] setDistanceList(List<CommonVO> rList, MapDataComparator m){
    	CommonVO[]  j = new CommonVO[rList.size()];
    	rList.toArray(j);
    	Arrays.sort(j,   m);
    	return j;
    }
    
    public void setSeq(List<CommonVO> list) {
    	SimpleDateFormat frmat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    	//고유 인덱스 생성
    	String setSeq =  null;
    	Date date =	null;
    	for(CommonVO vo : list) {
    		setSeq = vo.getString("recv_set_seq");
    		try {
    			date = frmat.parse(setSeq);
    			vo.put("recv_seed_seq", date.getTime());
    		} catch(Exception e) {
    		}
    	}	
    }
	
    
    
    public static void main(String[] args) {
    	List<CommonVO> list = new ArrayList<CommonVO>();
    	
    	CommonVO map1 = new CommonVO();
    	CommonVO map2 = new CommonVO();
    	CommonVO map3 = new CommonVO();
    	CommonVO map4 = new CommonVO();
    	
    	map1.put("recv_dt", "2019-03-24 15:10:50");
    	map1.put("key",     "1");
    	
    	map2.put("recv_dt", "2019-03-24 14:12:50");
    	map2.put("key",     "2");
    	
    	map3.put("recv_dt", "2019-03-24 13:10:50");
    	map3.put("key",     "3");
    	
    	map4.put("recv_dt", "2019-03-24 13:10:50");
    	map4.put("key",     "4");
    	list.add(map4);
    	list.add(map1);
    	list.add(map2);
    	list.add(map3);
    	
    	MapDataComparator k  = new MapDataComparator("recv_dt", false);
    	Collections.sort(list, k);
    	
    	System.out.println(list);
    }
}
