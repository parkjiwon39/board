package action;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import domain.BoardVO;
import persistence.BoardDAO;
import utils.FileUploadUtils;

public class UpdateAction implements Action {

	private String path;
	
	public UpdateAction(String path) {
		this.path = path;
	}
	
	@Override
	public ActionForward execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
		//qna_board_modify.jsp 넘긴 값 가져오기
		//enctype 들어있음 => FileUploadUtils 사용 
		FileUploadUtils upload1 = new FileUploadUtils();	
		HashMap<String, String> uploadMap = upload1.upload(req);		
		
		int bno = Integer.parseInt(uploadMap.get("bno"));
		String title = uploadMap.get("title");
		String content = uploadMap.get("content");
		String password = uploadMap.get("password");		
		String attach = uploadMap.get("attach");	
		
		//DB작업
		BoardVO vo = new BoardVO();	
		vo.setBno(bno);
		vo.setTitle(title);
		vo.setContent(content);
		vo.setPassword(password);
		if(uploadMap.containsKey("attach")) {
			vo.setAttach(attach);
		}
		
		//데이터베이스 수정 작업
		BoardDAO dao = new BoardDAO();
		int result = dao.updateRow(vo);
		if(result>0) {
			path+="?bno="+bno;
		}else {
			path="modify.do?bno="+bno;
		}
		
		return new ActionForward(path, true);
	}
}




















