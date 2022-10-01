package jihee.product.controller;

import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import common.controller.AbstractController;
import jihee.product.model.*;




public class ProductAb extends AbstractController {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		
		InterProductDAO pdao = new ProductDAO();
		
		
		/*
		
		if( productList.size() > 0) {
			for(ProductVO pvo : productList) { 
				System.out.println( pvo.getProd_color() + "  " + pvo.getProd_code());
			}
		}
		*/
		
		// ** 페이징 처리를 한 모든 상품 또는 검색한 상품 보여주기 ** //
		
		
		Map<String, String> paraMap = new HashMap<>();
		
		String sizePerPage = "20";
		// 한 페이지당 화면상에 보여줄 카드의 개수

		/*
		 * if(sizePerPage == null || !("3".equals(sizePerPage) ||
		 * "5".equals(sizePerPage) || "10".equals(sizePerPage))) { sizePerPage = "10"; }
		 */
		
		String currentShowPageNo = request.getParameter("currentShowPageNo");
		// currentShowPageNo 은 사용자가 보고자하는 페이지바의 페이지번호 이다.
         // 메뉴에서 회원목록 만을 클릭했을 경우에는 currentShowPageNo 은 null 이 된다.
         // currentShowPageNo 이 null 이라면 currentShowPageNo 을 1 페이지로 바꾸어야 한다.
		
		if(currentShowPageNo == null) {
			currentShowPageNo = "1";
		}
		
		// === GET 방식이므로 사용자가 웹브라우저 주소창에서 currentShowPageNo 에 숫자가 아닌 문자를 입력한 경우 또는 
         //     int 범위를 초과한 숫자를 입력한 경우라면 currentShowPageNo 는 1 페이지로 만들도록 한다. ==== // 
		try {
			if(Integer.parseInt(currentShowPageNo) < 1) {
				currentShowPageNo = "1";
			}
			
		}catch(NumberFormatException e) {
			currentShowPageNo = "1"; //숫자가 아닌 문자를 get방식으로 입력했을 때 무조건 1로 바꾸어준다
		}
		
		
		paraMap.put("sizePerPage", sizePerPage);
		paraMap.put("currentShowPageNo", currentShowPageNo);
		
		

		// 페이징 처리를 위한 검색이 있는 또는 검색이 없는 전체회원에 대한 총페이지 알아오기
		int totalPage = pdao.getTotalPage(paraMap);
		//System.out.println(totalPage);
		//sizePerPage가 10일때 totalPage는 21
		//sizePerPage가  5일때 totalPage는 41
		//sizePerPage가  3일때 totalPage는 68
		
		
		// 시작 <= GET방식이므로 사용자가 웹브라우저 주소창에서 currentShowPageNo에 토탈페이지수보다 큰 값을 입력하여 장난친 경우에는 1페이지로 가게끔 막아줌
		if(Integer.parseInt(currentShowPageNo) > totalPage ) {
			currentShowPageNo = "1";
		}
		
		// 끝 <= GET방식이므로 사용자가 웹브라우저 주소창에서 currentShowPageNo에 토탈페이지수보다 큰 값을 입력하여 장난친 경우에는 1페이지로 가게끔 막아줌

		//  --------------------------------------------------------------------
		
		// 상품 목록 가져오기 (검색어 포함)
		
		String searchWord = request.getParameter("searchWord");
		
		// System.out.println("searchWord 확인용 : " +searchWord);
		
		paraMap.put("searchWord", searchWord);
		
		List<ProductVO> productList = pdao.selectProductAll(paraMap);
	
		request.setAttribute("productList", productList);
		request.setAttribute("sizePerPage", sizePerPage);
		request.setAttribute("searchWord", searchWord);
		
		//ProductVO pvo = new ProductVO();
		
		
	
	
		// ******** === 페이지바 만들기 시작 === ******** //
		
		/*
         1개 블럭당 10개씩 잘라서 페이지 만든다.
         1개 페이지당 3개행 또는 5개행 또는  10개행을 보여주는데
             만약에 1개 페이지당 5개행을 보여준다라면 
             총 몇개 블럭이 나와야 할까? 
             총 회원수가 207명 이고, 1개 페이지당 보여줄 회원수가 5 이라면
         207/5 = 41.4 ==> 42(totalPage)        
             
         1블럭               1 2 3 4 5 6 7 8 9 10 [다음]
         2블럭   [맨처음][이전] 11 12 13 14 15 16 17 18 19 20 [다음][마지막]
         3블럭   [맨처음][이전] 21 22 23 24 25 26 27 28 29 30 [다음][마지막]
         4블럭   [맨처음][이전] 31 32 33 34 35 36 37 38 39 40 [다음][마지막]
         5블럭   [맨처음][이전] 41 42 
      */
     
     // ==== !!! pageNo 구하는 공식 !!! ==== // 
      /*
          1  2  3  4  5  6  7  8  9  10  -- 첫번째 블럭의 페이지번호 시작값(pageNo)은  1 이다.
          11 12 13 14 15 16 17 18 19 20  -- 두번째 블럭의 페이지번호 시작값(pageNo)은 11 이다.   
          21 22 23 24 25 26 27 28 29 30  -- 세번째 블럭의 페이지번호 시작값(pageNo)은 21 이다.
          
           currentShowPageNo        pageNo  ==> ( (currentShowPageNo - 1)/blockSize ) * blockSize + 1 
          ---------------------------------------------------------------------------------------------
                 1                   1 = ( (1 - 1)/10 ) * 10 + 1 
                 2                   1 = ( (2 - 1)/10 ) * 10 + 1 
                 3                   1 = ( (3 - 1)/10 ) * 10 + 1 
                 4                   1 = ( (4 - 1)/10 ) * 10 + 1  
                 5                   1 = ( (5 - 1)/10 ) * 10 + 1 
                 6                   1 = ( (6 - 1)/10 ) * 10 + 1 
                 7                   1 = ( (7 - 1)/10 ) * 10 + 1 
                 8                   1 = ( (8 - 1)/10 ) * 10 + 1 
                 9                   1 = ( (9 - 1)/10 ) * 10 + 1 
                10                   1 = ( (10 - 1)/10 ) * 10 + 1 
                 
                11                  11 = ( (11 - 1)/10 ) * 10 + 1 
                12                  11 = ( (12 - 1)/10 ) * 10 + 1
                13                  11 = ( (13 - 1)/10 ) * 10 + 1
                14                  11 = ( (14 - 1)/10 ) * 10 + 1
                15                  11 = ( (15 - 1)/10 ) * 10 + 1
                16                  11 = ( (16 - 1)/10 ) * 10 + 1
                17                  11 = ( (17 - 1)/10 ) * 10 + 1
                18                  11 = ( (18 - 1)/10 ) * 10 + 1 
                19                  11 = ( (19 - 1)/10 ) * 10 + 1
                20                  11 = ( (20 - 1)/10 ) * 10 + 1
                 
                21                  21 = ( (21 - 1)/10 ) * 10 + 1 
                22                  21 = ( (22 - 1)/10 ) * 10 + 1
                23                  21 = ( (23 - 1)/10 ) * 10 + 1
                24                  21 = ( (24 - 1)/10 ) * 10 + 1
                25                  21 = ( (25 - 1)/10 ) * 10 + 1
                26                  21 = ( (26 - 1)/10 ) * 10 + 1
                27                  21 = ( (27 - 1)/10 ) * 10 + 1
                28                  21 = ( (28 - 1)/10 ) * 10 + 1 
                29                  21 = ( (29 - 1)/10 ) * 10 + 1
                30                  21 = ( (30 - 1)/10 ) * 10 + 1                    

       */
		
		String pageBar = "";
		
		int blockSize = 10; //blockSize는 블럭(토막)당 보여지는 페이지 번호의 개수
		
		int loop = 1; //loop는 1부터 증가하여 1개 블럭을 이루는 페이지번호의 개수(지금은 10개) 까지만 증가하는 용도

		
		// ==== !!! pageNo 구하는 공식 !!! ==== // 
        //currentShowPageNo        pageNo  ==> ( (currentShowPageNo - 1)/blockSize ) * blockSize + 1 
		int pageNo = ( (Integer.parseInt(currentShowPageNo) - 1)/blockSize ) * blockSize + 1;
		//pageNo는 페이지바에서 보여지는 첫번째 번호
		
		
		
		/////////////////////////////////////////////////////////////////////
		
		
		// **** [맨처음][이전] 만들기 **** //
		if(pageNo != 1) {
			//pageBar += "<li class='page-item'><a class='page-link' href='productHp.sue?sizePerPage="+sizePerPage+"&currentShowPageNo=1"'>" + "</a></li>";
			pageBar += "<li class='page-item'><a class='page-link' href='productHp.sue?sizePerPage="+sizePerPage+"&currentShowPageNo="+(pageNo-1)+"'>" + "</a></li>";
		}
		
		// **** [맨처음][이전] 만들기 끝 **** //
		
		
		while( !(loop > blockSize || pageNo > totalPage) ) { //loop가 blocksize보다 커지면 탈출
			if(pageNo == Integer.parseInt(currentShowPageNo)) {
				pageBar += "<li class='page-item active'><a class='page-link' href=''>" + pageNo + "</a></li>";
				
			}
			else {
				pageBar += "<li class='page-item'><a class='page-link' href='productHp.sue?sizePerPage="+sizePerPage+"&currentShowPageNo="+pageNo+"'>" + pageNo + "</a></li>";
			}
			loop++; // 1 2 3 4 5 6 7 8 9 10
			pageNo++; // 1   2  3  4  5  6  7  8  9 10
					  // 11 12 13 14 15 16 17 18 19 20
					  // 21 22 23 24 25 26 27 28 29 30 ...
					  // sizePerPAge가 10일때
			
			
		}//end of while
		
		//첫번째 블럭( 1   2  3  4  5  6  7  8  9 10 )인경우 pageNo 11이고
		//두번째 블럭( 11 12 13 14 15 16 17 18 19 20 )인경우 pageNo 21이고
		//세번째 블럭( 21 )인경우 pageNo 22
		
		// **** [다음][마지막] 만들기 **** //
		if( pageNo <= totalPage ) { //페이지가 totalPage보다 작거나 같을때만 (마지막 블럭 제외)  
			pageBar += "<li class='page-item'><a class='page-link' href='productHp.sue?sizePerPage="+sizePerPage+"&currentShowPageNo="+pageNo+"'>" + "</a></li>";
			pageBar += "<li class='page-item'><a class='page-link' href='productHp.sue?sizePerPage="+sizePerPage+"&currentShowPageNo="+totalPage+"'>" + "</a></li>";
		}
		// **** [다음][마지막] 만들기 끝**** //
		
						
		request.setAttribute("pageBar", pageBar);
		
		
		// ******** === 페이지바 만들기 끝 === ******** //

		//super.setRedirect(false);
		super.setViewPage("/WEB-INF/jihee/prodouct/5.productAbSide.jsp");
		

	}

}
