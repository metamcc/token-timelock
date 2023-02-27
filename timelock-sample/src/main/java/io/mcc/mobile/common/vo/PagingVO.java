package io.mcc.mobile.common.vo;

import org.apache.ibatis.type.Alias;

import io.mcc.common.vo.CommonVO;
import lombok.Data;

@Data
@Alias("pagingVO")
public class PagingVO extends CommonVO {

	public PagingVO(long pageCount, long pageSize) {
		this.put("pageCount", pageCount);
		this.put("pageSize", pageSize);
		this.put("offset", (pageCount-1)*pageSize);
		
		pagingInfoVO = new CommonVO();
		pagingInfoVO.putAll(this);
	}
	
	private CommonVO getPagingInfo() {
		
		return pagingInfoVO;
	}
	CommonVO pagingInfoVO = null;
	
	private long totalElements;
	
	private long offset;

	private long size;

	public PagingVO(long pageCount, long pageSize, long totalElements) {
		this.put("pageCount", pageCount);
		this.put("pageSize", pageSize);
		
		this.totalElements = totalElements;
		this.offset = (pageCount - 1) * pageSize;
		this.size = pageSize;
		
		this.put("totalPages", getTotalPages());
		this.put("page", getPage());
		this.put("hasPrevious", hasPrevious());
		this.put("hasNext", hasNext());
		this.put("isFirst", isFirst());
		this.put("isLast", isLast());
	}
	
	private long getTotalPages() {
		return this.size == 0 ? 1 : (long) Math.ceil((double) getTotalElements() / (double) getSize());
	}
	
	private long getPage() {
		return (getOffset() / getSize()) + 1;
	}
	
	private boolean hasPrevious() {
		return getPage() > 1;
	}
	
	private boolean hasNext() {
		return getPage() < getTotalPages();
	}
	
	private boolean isFirst() {
		return !hasPrevious();
	}
	
	private boolean isLast() {
		return !hasNext();
	}
}