package domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PageVO {
	//page 나누기와 관련된 정보를 담는 객체
	private int startPage;
	private int endPage;
	private boolean prev;
	private boolean next;
	
	private SearchVO search;
	private int total;//전체 게시물 수
	
	public PageVO(int total,SearchVO search) {
		this.total=total;
		this.search=search;
		
		
		//현재 화면의 마지막 페이지 수
		endPage=(int)(Math.ceil(search.getPage()/10.0))*10;
		
		//현재 화면의 시작 페이지 수		
		//1 2 3 4...10[다음]
		startPage=endPage-9;
		
		int realEnd = (int)(Math.ceil((this.total/1.0)/search.getAmount()));
		if(realEnd<this.endPage) {
			this.endPage=realEnd;
		}
		this.prev = this.startPage > 1;
		this.next = this.endPage < realEnd;
	}

}
