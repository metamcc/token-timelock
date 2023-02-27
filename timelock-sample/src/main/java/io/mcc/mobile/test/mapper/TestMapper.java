package io.mcc.mobile.test.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import io.mcc.common.vo.CommonVO;

@Mapper
public interface TestMapper {

	CommonVO selectMyFruitData(@Param("user_seq")long user_seq);
}
