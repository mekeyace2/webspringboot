package kr.co.lee;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface cdn_mapper {
	int cdn_insert(file_DTO dto);
}
