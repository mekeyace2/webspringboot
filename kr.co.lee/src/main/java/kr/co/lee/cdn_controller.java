package kr.co.lee;

import java.io.InputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import io.netty.util.ByteProcessor.IndexOfProcessor;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletResponse;

//CDN 전용 컨트롤러
@Controller
public class cdn_controller {
	
	Logger log = LoggerFactory.getLogger(this.getClass()); 
	
	//@Resource로 Controller에서 호출과 Model에서 호출이 다름
	@Resource(name="file_DTO")
	file_DTO file_DTO;
	
	@Resource(name="cdn_model")
	cdn_model cdn_model;
	
	@Autowired
	private cdn_service cs;
		
	PrintWriter pw = null;
	
	/*
	 Controller에서 모든 list 화면을 출력시
	 1. 한 페이지당 출력하는 데이터 갯수
	 2. 데이터 검색어를 받을 경우
	 3. 사용자가 페이지 번호를 누를 경우
	*/
	
	
	@PostMapping("/cdn/cdn_delete.do")
	public String cdn_delete() {
		
		return null;
	}
	
	//@ResponseBody : return 결과값을 즉시실행 하여 해당 메소드에 출력하는 역활
	@GetMapping("/cdn/image/{api_id}")
	@ResponseBody
	public byte[] image(@PathVariable(name="api_id")String id) throws Exception {	//ResponseBody 해당 메소드에서 출력
		byte[] img = null;
		
		if(!id.equals(null) || !id.equals("")) {
			List<file_DTO> result = this.cs.cdn_images(id);	
			try {
				URL url = new URL(result.get(0).getFILE_URL());
				HttpURLConnection hc = (HttpURLConnection)url.openConnection(); 
				InputStream is = hc.getInputStream();
				img = IOUtils.toByteArray(is);
			}catch(Exception e) {
				this.log.info("CDN URL 연결실패!!");
			}
		}
		
		return img;
	}
	
	
	@GetMapping("/cdn/cdn_filelist.do")
	public String cdn_filelist(Model m) {
		List<file_DTO> all = this.cs.all();
		m.addAttribute("all",all);
		return null;
	}
		
	
	
	//CDN Server로 파일 전송 및 DB 저장
	@PostMapping("/cdn/cdn_fileok.do")
	public String cdn_fileok(
		@RequestParam(name="mfile")MultipartFile mfile[],
		@RequestParam(name="url") String url, ServletResponse res) throws Exception {
		//사용자가 업로드한 파일명을 개발자가 원하는 이름으로 변경 후 FTP 전송
		boolean result = this.cdn_model.cdn_ftp(mfile[0],url);
		res.setContentType("text/html;charset=utf-8");
		this.pw = res.getWriter();
		if(result == true) {
				this.pw.print("<script>"
						+ "alert('정상적으로 등록 완료 되었습니다.');"
						+ "location.href='./cdn_filelist.do';"
						+ "</script>");
		}else {
			this.pw.print("<script>"
					+ "alert('올바르게 저장 되지 않았습니다.');"
					+ "history.go(-1);"
					+ "</script>");
		}
		return null;
	}
	
}



