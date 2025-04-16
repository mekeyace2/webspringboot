package kr.co.lee;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class cdn_service {

	@Autowired
	cdn_mapper mp;
	
	public int cdn_insert(file_DTO dto) {
		int result = this.mp.cdn_insert(dto);
		return result;
	}
	
	public List<file_DTO> all(){
		List<file_DTO> result = this.mp.cdn_select();
		return result;
	}
	
	public List<file_DTO> cdn_images(String APINO){
		List<file_DTO> result = this.mp.cdn_images(APINO);
		return result;
	}
	
}


