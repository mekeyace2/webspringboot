package kr.co.lee;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
//@Component : @Controller, Service, Repository 모두 포함된 어노테이션
/*
 @Service : 클래스를 해당 코드 로직에 정보를 담고 있는 어노테이션이며, interface를 호출시
 작동되는 클래스를 말함 (@Autowired로 호출)
*/
@Service
public class membershop_dao implements membership_service {

	Logger log = LoggerFactory.getLogger(this.getClass()); 
	
	@Autowired
	private mapper mp;		//Mapper를 로드하여 SQL Query문을 실행
		
	@Override
	public int join_member(membership_dto dto) {
		int result = this.mp.save_member(dto);
		return result;
	}
	
	
	
	
	//selectList, selectone, insert, delete, update (X)
	@Override
	public List<membership_dto> alldata() {
		List<membership_dto> all = this.mp.alldata();
		this.log.info(all.toString());
		return all;
	}

}
