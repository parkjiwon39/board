package action;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import domain.BoardVO;
import persistence.BoardDAO;
import utils.FileUploadUtils;

public class WriteAction implements Action{
	
	private String path;
	
	public WriteAction(String path) {
		this.path = path;
	}
	
	@Override
	public ActionForward execute(HttpServletRequest req, HttpServletResponse res) 
		throws Exception{
		
		FileUploadUtils upload = new FileUploadUtils();
		HashMap<String, String> uploadMap = upload.upload(req);
		
		String name = uploadMap.get("name");
		String title = uploadMap.get("title");
		String content = uploadMap.get("content");
		String password = uploadMap.get("password");
		String attach = uploadMap.get("attach");
		
		//페이지 1 / criteria, keyword == ""
		String page = "1";
		String criteria ="";
		String keyword = "";
		
		//DB작업
		BoardDAO dao = new BoardDAO();
		BoardVO vo = new BoardVO();
		vo.setName(name);
		vo.setTitle(title);
		vo.setContent(content);
		vo.setPassword(password);
		vo.setAttach(attach);
		if(uploadMap.containsKey("attach")) {
			vo.setAttach(attach);
		}
		
		int result = dao.insertArticle(vo);
		if(result==0) {
			path="view/qna_board_write.jsp";
		}
		return new ActionForward(path,true);
	}

}
