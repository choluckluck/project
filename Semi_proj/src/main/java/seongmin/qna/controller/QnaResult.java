package seongmin.qna.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import common.controller.AbstractController;
import seongmin.login.model.MemberVO;
import seongmin.qna.model.InterQnaDAO;
import seongmin.qna.model.QnaDAO;

public class QnaResult extends AbstractController {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String method = request.getMethod();
		
		if("POST".equalsIgnoreCase(method)) {
		
			HttpSession session = request.getSession();
			MemberVO loginuser = (MemberVO) session.getAttribute("loginuser");		
			String userid = loginuser.getUserid();	
			
			String prod_code = request.getParameter("prod_code");
			String contents = request.getParameter("contents");
			String subject = request.getParameter("subject");
			String category = request.getParameter("category");
		
		
			Map<String, String> paraMap = new HashMap<>();
			paraMap.put("userid", userid);
			paraMap.put("prod_code", prod_code);
			paraMap.put("contents", contents);
			paraMap.put("subject", subject);
			paraMap.put("category", category);
			
			InterQnaDAO qdao = new QnaDAO();
			
			int n = qdao.qnaRegister(paraMap);
			
			
			if(n == 1 ) {
				super.setViewPage("/heajun/product/productdetail.sue?prod_code="+prod_code);
			}			
			
			
			
			
			
			
			
		}
			
			
			
	}

}
