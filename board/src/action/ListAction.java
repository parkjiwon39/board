package action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import domain.BoardVO;
import domain.PageVO;
import domain.SearchVO;
import persistence.BoardDAO;

public class ListAction implements Action{
	
	private String path;
	
	public ListAction(String path) {
		this.path = path;
	}
	
	@Override
	public ActionForward execute(HttpServletRequest req, HttpServletResponse rep) 
		throws Exception{
		
		// search에서 넘어오는 값 가져오기
		//criteria, keyword
		String criteria = req.getParameter("criteria");
		String keyword = req.getParameter("keyword");
		
		/*****페이지 나누기*****/
		int page=1;
		if(req.getParameter("page")!=null)//페이지 값 넘어온 경우
			page=(Integer.parseInt(req.getParameter("page")));
		
		//한페이지당 보여줄 게시물 수
		int amount=10;
		BoardDAO dao = new BoardDAO();
		SearchVO search = new SearchVO(criteria, keyword,page, amount);
		
		//전체 게시물 수
		int total = dao.totalRows(criteria, keyword);
		PageVO pageVO = new PageVO(total, search);
		List<BoardVO> list = dao.getList(search);
		
		if(!list.isEmpty())
		req.setAttribute("list", list);
		req.setAttribute("pageVO", pageVO);
		
		return new ActionForward(path, false);
	}
}
