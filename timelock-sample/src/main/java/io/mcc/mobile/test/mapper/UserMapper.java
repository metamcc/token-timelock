package io.mcc.mobile.test.mapper;

import io.mcc.common.resolver.UserExtraRole;
import io.mcc.common.vo.CommonVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


@Mapper
public interface UserMapper {

	CommonVO<String, Object> getUserInfo(CommonVO param);

	UserExtraRole getUserExtraRole(@Param("user_seq") long user_seq);
	


	
}