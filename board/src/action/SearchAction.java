package action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import domain.BoardVO;
import domain.SearchVO;
import persistence.BoardDAO;

public class SearchAction implements Action {
	
	private String path;
	
	public SearchAction(String path) {
		this.path = path;
	}

	@Override
	public ActionForward execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
		// search에서 넘어오는 값 가져오기
		//criteria, keyword
		String criteria = req.getParameter("criteria");
		String keyword = req.getParameter("keyword");
		//DB작업
		BoardDAO dao = new BoardDAO();
		List<BoardVO> list = dao.getSearchList(criteria, keyword);
		//결과를 req에 담고 페이지 이동
		req.setAttribute("list", list);
		req.setAttribute("search", new SearchVO(criteria,keyword));
		
		return new ActionForward(path, false);
	}

}
