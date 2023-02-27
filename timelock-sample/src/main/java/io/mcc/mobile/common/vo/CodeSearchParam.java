package io.mcc.mobile.common.vo;

import org.apache.ibatis.type.Alias;

import lombok.Data;

@Data
@Alias("codeSearchParam")
public class CodeSearchParam {

    private String cd;

    private String cdGrp;

    private String attr1;

    private String attr2;

    private String attr3;

    private String attr4;

    private String attr5;

    private String attr6;

    private String attr7;

    private String attr8;

    private String attr9;

    private String attr10;
}
