package action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import domain.BoardVO;
import persistence.BoardDAO;

public class ViewAction implements Action{
	
	private String path;
	
	public ViewAction(String path) {
		this.path = path;
	}
	
	@Override
	public ActionForward execute(HttpServletRequest req, HttpServletResponse res)throws Exception{
		//list.do => 제목 클릭 (view.do?bno=해당번호)
		
		//bno 가져오기
		int bno = Integer.parseInt(req.getParameter("bno"));
		//bno에 해당하는 게시물 DB에서 가져오기
		BoardDAO dao = new BoardDAO();
		BoardVO vo = dao.getRow(bno);
		//가져온 게시물 request에 담고 페이지 이동
		if(vo!=null) {
			req.setAttribute("vo", vo);
		}else {
			path="/list.do";
			return new ActionForward(path,true);
		}
		return new ActionForward(path,false);
	}

}
