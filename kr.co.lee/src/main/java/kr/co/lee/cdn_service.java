package kr.co.lee;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class cdn_service {

	@Autowired
	cdn_mapper mp;
	
	public int cdn_delete(String AIDX) {
		int result = this.mp.cdn_delete(AIDX);
		return result;
	}
	
	
	public int cdn_insert(file_DTO dto) {
		int result = this.mp.cdn_insert(dto);
		return result;
	}
	
	public List<file_DTO> all(Integer part,file_DTO dto){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("part", part);
		map.put("AIDX", dto.getAIDX());
		List<file_DTO> result = this.mp.cdn_select(map);
		return result;
	}
	
	public List<file_DTO> cdn_images(String APINO){
		List<file_DTO> result = this.mp.cdn_images(APINO);
		return result;
	}
	
}


