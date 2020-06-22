package action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import persistence.BoardDAO;

public class DeleteAction implements Action {
	private String path;
	
	public DeleteAction(String path) {
		super();
		this.path = path;
	}

	@Override
	public ActionForward execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
		//bno,password가져오기
		int bno = Integer.parseInt(req.getParameter("bno"));
		String password = req.getParameter("password");
		//DB작업
		BoardDAO dao = new BoardDAO();
		int result = dao.deleteArticle(bno, password);
		if(result==0) {
			path="view/qna_board_pwdCheck.jsp?bno="+bno;
		}
		return new ActionForward(path,true);
	}

}
