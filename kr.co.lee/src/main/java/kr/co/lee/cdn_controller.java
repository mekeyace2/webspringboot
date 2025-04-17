package kr.co.lee;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

import org.apache.commons.io.FileUtils;
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
import jakarta.servlet.http.HttpServletResponse;

//CDN 전용 컨트롤러
@Controller
public class cdn_controller {
	
	Logger log = LoggerFactory.getLogger(this.getClass()); 
	
	//@Autowired
	//dbinfos dbinfs;
	
	@GetMapping("/cdn/mysql.do")
	public String mysql_list() throws Exception {
		//mysql data로드
		/*
		Connection con = this.dbinfs.mysqldb().getConnection();
		String sql = "select * from event";
		PreparedStatement ps = con.prepareStatement(sql);
		ResultSet rs = ps.executeQuery();
		rs.next();
		this.log.info(rs.getString("ename"));
		
		rs.close();
		ps.close();
		con.close();
		*/
		return null;
	}
	
	
	
	
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
	/*
	 응용문제 : FTP에 파일이 없을 경우 DB가 삭제 되지 않는 버그가 발생함
	 해당 버그를 수정하여 FTP에 파일이 없어도 DB에 데이터가 삭제 되도록 수정 
	*/
	
	
	//사용자가 해당 파일을 클릭시 파일을 자신의 PC, Mobile에 다운로드 할 수 있는 API 메소드
	@GetMapping("/cdn/download.do/{filenm}")
	public void downloads(@PathVariable(name="filenm")String filenm,
			HttpServletResponse res) throws Exception {
		//외부에서 이미지 및 파일, 동영상을 FTP URL로 읽어서 byte로 변환시 무조건 File 객체 이용
		/* ftp://
		File f = new File(filenm);	//FTP 경로를 로드한 후에 byte 변환
		byte[] files = FileUtils.readFileToByteArray(f);
		*/
		
		//서버 경로에 맞는 파일을 로드
		URL url = new URL("http://kbsn.or.kr/apink/" + filenm);
		//http 통신
		HttpURLConnection httpcon = (HttpURLConnection)url.openConnection();
		//해당 경로에 이미지를 byte 읽어들임
		InputStream is = new BufferedInputStream(httpcon.getInputStream());
		
		//해당 Controller에서 파일을 다운로드 받을 수 있도록 처리
		res.setHeader("content-transfer-encoding", "binary");
		res.setContentType("application/x-download");
		
		OutputStream os = res.getOutputStream();	//파일을 저장 할 수 있도록 설정
		IOUtils.copy(is, os);	//서버에 있는 값을 PC로 복제하는 역활
		os.flush();
		os.close();
		is.close();
	}
		
	@PostMapping("/cdn/cdn_delete.do")
	public String cdn_delete(@RequestParam(name = "cbox")String cbox[]) throws Exception {
		//this.log.info(String.valueOf(cbox.length));
		
		int countck = 0;	//성공한 결과만 +1 증가시키는 변수
		
		for(String idx : cbox) {
			this.file_DTO.setAIDX(Integer.parseInt(idx));	//필드에 있는 dto에 setter로 이관
			List<file_DTO> one = this.cs.all(1, this.file_DTO);	//service에 DTO값 전달
			boolean result = this.cdn_model.cdn_ftpdel(one.get(0).getNEW_FILE(),idx);	//개발자가 생성한 파일명을 출력
			if(result == true) {	//파일 삭제도 되고, Database도 삭제가 된 경우
				countck++;
			}
		}
		if(cbox.length == countck) {
			this.log.info("정상적으로 모두 삭제 완료 되었습니다.");
		}else {
			this.log.info("삭제 되지 않은 데이터가 있습니다.");
		}
		//해당 do 로 이동하는 방식
		return "redirect:/cdn/cdn_filelist.do";
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
	public String cdn_filelist(Model m,
			@RequestParam(name="word", defaultValue = "", required = false)String word
		) throws Exception {
		
		List<file_DTO> all = null;
		
		if(word.equals("")) { //검색어 없을 경우
			all = this.cs.all(2,this.file_DTO);
		}
		else {	//검색어가 있을 경우
			this.file_DTO.setWord(word);	//setter로 DTO에 값을 이관
			all = this.cs.all(3,this.file_DTO);
		}
		
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



