package action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import domain.BoardVO;
import persistence.BoardDAO;

public class ModifyAction implements Action {

	private String path;
	
	public ModifyAction(String path) {
		this.path = path;
	}
	
	@Override
	public ActionForward execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
		// modify.do => 내용보기 화면에서 수정 버튼 클릭
		
		//  bno 가져오기
		int bno = Integer.parseInt(req.getParameter("bno"));
		//  bno 에 해당하는 게시물 DB 에서 가져오기
		BoardDAO dao = new BoardDAO();
		BoardVO vo = dao.getRow(bno);
		//  가져온 게시물 request 에 담고 페이지 이동
		if(vo!=null) {
			req.setAttribute("vo", vo);
		}else {
			path = "/view.do?bno="+bno;
			return new ActionForward(path, true);
		}
		return new ActionForward(path, false);
	}
}




















