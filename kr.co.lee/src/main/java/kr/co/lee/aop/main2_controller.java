package kr.co.lee.aop;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import jakarta.annotation.Resource;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class main2_controller {

	Logger log = LoggerFactory.getLogger(this.getClass()); 
	
		@Resource(name="user_dto")
		user_dto dto;
	
		@GetMapping("/testaop_logout.do")
		public String testaop2(Model m, 
			@SessionAttribute(name = "userid", required = false) String userid,
			HttpServletRequest req
			) {
			this.log.info(userid);
			HttpSession hs = req.getSession();
			hs.invalidate();
			this.log.info("올바르게 로그아웃 되었습니다.");
			return null;
		}
	
		@GetMapping("/testaop.do")
		public String testaop(Model m) {
			
			String mid = "hong";
			String mname = "홍길동";
			
			this.dto.setMid(mid);
			this.dto.setMname(mname);
			
			if(mid.equals("hong")) {
				this.log.info("아이디를 확인 하였습니다.");
			}
			
			m.addAttribute("mname",mname);
			return null;
		}
}
