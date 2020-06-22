package action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import persistence.BoardDAO;

public class HitUpdateAction implements Action {
	
	private String path;
	
	public HitUpdateAction(String path) {
		this.path = path;
	}

	@Override
	public ActionForward execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
		//bno 가져오기
		int bno = Integer.parseInt(req.getParameter("bno"));
		//bno에 해당하는 게시물 DB에서 가져오기
		BoardDAO dao = new BoardDAO();
		//조회수 업데이트
		int result = dao.hitUpdate(bno);
		
		if(result>0) {//view.do?bno=3
			path+="?bno="+bno;
		}
		//view.do
		return new ActionForward(path,true);
	}

}
