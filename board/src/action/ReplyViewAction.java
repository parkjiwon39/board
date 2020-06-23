package action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import domain.BoardVO;
import domain.SearchVO;
import persistence.BoardDAO;

public class ReplyViewAction implements Action {

	private String path;
	
	public ReplyViewAction(String path) {
		this.path = path;
	}
	
	@Override
	public ActionForward execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
		
		
		//  bno 가져오기
		int bno = Integer.parseInt(req.getParameter("bno"));
		
		// 페이지 나누기
		int page = Integer.parseInt(req.getParameter("page"));
		String criteria = req.getParameter("criteria");
		String keyword = req.getParameter("keyword");
		SearchVO search = new SearchVO(criteria, keyword, page, 10);
		
		//  bno 에 해당하는 게시물 DB 에서 가져오기
		BoardDAO dao = new BoardDAO();		
		BoardVO vo = dao.getRow(bno);
		
		//  가져온 게시물 request 에 담고 페이지 이동
		if(vo!=null) {
			req.setAttribute("vo", vo);
			req.setAttribute("search", search);
		}else {
			path = "/view.do?bno="+bno+"&page="+page+"&criteria="+criteria+"&keyword="+keyword;
			return new ActionForward(path, true);
		}
		return new ActionForward(path, false);
	}
}




















