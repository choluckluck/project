package common.controller;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import hasol.purchase.model.J_MemberVO;
import heajun.community.model.InterNoticeDAO;
import heajun.community.model.NoticeDAO;

public abstract class AbstractController implements InterCommand {
	/*
		=== 다음의 나오는 것은 우리끼리한 약속이다. ===
		
		※ view 단 페이지(.jsp)로 이동시 forward 방법(dispatcher)으로 이동시키고자 한다라면 
		자식클래스에서는 부모클래스에서 생성해둔 메소드 호출시 아래와 같이 하면 되게끔 한다.
		
		super.setRedirect(false); 
		super.setViewPage("/WEB-INF/index.jsp");
		    
		    
		※ URL 주소를 변경하여 페이지 이동시키고자 한다라면
		즉, sendRedirect 를 하고자 한다라면    
		자식클래스에서는 부모클래스에서 생성해둔 메소드 호출시 아래와 같이 하면 되게끔 한다.
		          
		super.setRedirect(true);
		super.setViewPage("registerMember.up");               
	*/   
	private boolean isRedirect = false;
	// isRedirect 변수의 값이 false 이라면 view단 페이지(.jsp)로  forward 방법(dispatcher)으로 이동시키겠다. 
	// isRedirect 변수의 값이 true 이라면 sendRedirect 로 페이지이동을 시키겠다.
	
	
	private String viewPage;
	// viewPage 는 isRedirect 값이 false 이라면 view단 페이지(.jsp)의 경로명 이고,
	// isRedirect 값이 true 이라면 이동해야할 페이지 URL 주소 이다.


	public boolean isRedirect() {
		return isRedirect;
	}


	public void setRedirect(boolean isRedirect) {
		this.isRedirect = isRedirect;
	}


	public String getViewPage() {
		return viewPage;
	}


	public void setViewPage(String viewPage) {
		this.viewPage = viewPage;
	}
	
	////////////////////////////////////////////////////////////////////

	////////////////////////////////////////////////////////////////////

	//!!! 하솔 쓰려고 만들었음 !!!! 로그인 유무를 검사 => 로그인했으면 true를 리턴, 로그인안했으면 false를 리턴 
	public boolean checkLogin(HttpServletRequest request) {
		HttpSession session = request.getSession();
		J_MemberVO loginuser = (J_MemberVO)session.getAttribute("loginuser");
		
		if(loginuser != null) { //로그인 한경우
			return true;
		}
		else { //로그인 안한경우
			return false;
		}
	}//end of checkLogin

	
		// 공지사항 목록 보여줄 메소드 생성하기 (혜준 사용)//
		// VO 를 사용하지 않고 Map으로 처리 => 한 번만 실행하려고 abstract에서 실행
			public void getNotice(HttpServletRequest request) throws Exception{
			InterNoticeDAO ndao = new NoticeDAO();
			
			List<HashMap<String,String>> Notice = ndao.getNotice();
			
			request.setAttribute("Notice", Notice);
			
		}
		
}
