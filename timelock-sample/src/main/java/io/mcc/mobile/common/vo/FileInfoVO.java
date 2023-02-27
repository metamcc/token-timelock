package io.mcc.mobile.common.vo;

import org.apache.ibatis.type.Alias;

import lombok.Data;

@Data
@Alias("fileInfoVO")
public class FileInfoVO {
	
	private long fileSeq;

	private String fileTypeCd;
	
	private String fileContentType;

	private String originalFileName;
	
	private String changeFileName;
	
	private String thumbnailFileName;
	
	private String filePath;
	
	private long fileSize;
	
	private String fileUrl;
	
	private String delYn;
	
	private String thumbnailFileUrl;

	//x2ho.add.2021.05.20. extended resolution
	private String thumbnailXRFileName;
	private String thumbnailXRFileUrl;
}
