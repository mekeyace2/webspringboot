package kr.co.lee.thymeleaf;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.annotation.Resource;


//Thymeleaf + Spring-boot => Model(extends or implements or AOP)
@Controller
public class thymeleaf_controller {
		Logger log = LoggerFactory.getLogger(this.getClass()); 
	
		//AOP와 공용으로 사용하는 DTO 입니다.
		@Resource(name="all_DTO")
		all_DTO all_inject;
	
	
		@GetMapping("/shop_login.do")
		public String shop_login(Model m) {
			//해당 객체명으로 DTO에 있는 배열값을 thymeleaf HTML로 이관
			m.addAttribute("menulist",this.all_inject.getMenus());
			
			return "/login.html";
		}
	
		@GetMapping("/shop.do")
		public String shop(Model m) {	//실제 메인 페이지
			m.addAttribute("menulist",this.all_inject.getMenus());
			
			return "/main.html";
		}
	
		
		//작업용 파일
		/*
		@GetMapping("/indextest.do")
		public String indextest() {
			return "/index.html";
		}
		*/
	
	/*
	  1. 기본 return null 사용시 => webapp
	  2. return "aaa"	=> webapp => aaa.jsp
	  3. return "/aaa"  => templates => aaa.jsp
	  4. return "/aaa.html" => templates => aaa.html
	*/
	@GetMapping("/sample2.do")
	public String sample2(Model m) {
		String menu = "Admin 관리자 등록";
		String copy = "Copyright 2025 WEB By Design..";
		m.addAttribute("menu",menu);
		m.addAttribute("copy",copy);
		return "/subpage.html";
	}
	
		
	//Thymeleaf View에 Model로 생성 후 .html에 값을 전달
	@GetMapping("/sample.do")
	public String sample(Model m) {
		String product = "냉장고";
		m.addAttribute("product",product);
		
		return "/sample.html";
	}
	
}
