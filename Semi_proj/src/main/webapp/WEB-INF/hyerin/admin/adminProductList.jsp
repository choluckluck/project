<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
    String ctxPath = request.getContextPath();
%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<jsp:include page="/WEB-INF/hyerin/header.jsp"></jsp:include>
<style>
	form[name='productList_search_form']{
		float:right;
	}
	
	
	#product_search{
		border: none;
		border-bottom: solid 1px black;
		font-size: 10pt;
	}
	
	.productSort {
		border: solid 1px #d9d9d9;
		height: 30px;
		font-size: 10pt;
	}
	
	.admin_productList_th{
		font-weight:normal;
		border-top:solid 1px black;
		border-bottom: solid 1px black;
	}
	
	.admin_productList_tbody{
		border-bottom: solid 1px #d9d9d9;
	}
	
	#productSearch_btn{
		float: right;
		width:150px;
		height:40px;
	}
</style>

<script>
	$(document).ready(function(){
		
		
		$("#byRegisterdayOrders").change(function(e){
			selectBySort();
		});
		
		
		
		/* $("#byRegisterdayOrders").change(function(e){
			selectBySort();
			
		});	
		
		$("#byKind").change(function(){
			selectBySort();
		});	
		
		if()
		
		if(${requestScope.byRegisterdayOrders} != null ){
			$("#byRegisterdayOrders").val("${requestScope.byRegisterdayOrders}");
		};
		
		if(${requestScope.byKind} != null){
			$("#byKind").val("${requestScope.byKind}");
		};  */
		
		
	});//end of ready
	
	/////////////////////////////////////////////////////////////////////////////////////////////////
	
	//function declarartion

	
	
	
	function product_edit(prod_code){
		// 회원 정보 수정하기 팝업창 띄우기
		const url = "<%= ctxPath%>/hyerin/admin/adminProductEdit.sue";
		
		//너비 800, 높이 600인 팝업창을 화면 가운데 위치시키기
		
		const pop_width = 800; //팝업 px은 생략가능 (더하기 할 경우 => 생략)
		const pop_height = 600;
		const pop_left = Math.ceil( (window.screen.width - pop_width)/2 ); //Math.ceil(1.5) => 2가 나옴 (1.5보다 큰 최소의 정수) Math.floor(1.5) => 1이 나옴 (1.5보다 작은 최대의 정수)
		const pop_top = Math.ceil( (window.screen.height - pop_height)/2 ); //Math.ceil(1.5) => 2가 나옴 (1.5보다 큰 최소의 정수) Math.floor(1.5) => 1이 나옴 (1.5보다 작은 최대의 정수)
		
		
		window.open(url, "productEdit",
				    "left="+pop_left+", top="+pop_top+", width="+pop_width+", height="+pop_height);
		
	};
	
	
	/* function selectBySort(){
		const frm = document.prodSelectFrm;
		frm.action = "adminProductList.sue";
		frm.method = "get";
		frm.submit();
	}; */
	
</script>

<div class="row container-fluid mt-5">
	<jsp:include page="/WEB-INF/hyerin/admin/adminSidebar.jsp" />
	<div id="contents" class="col-9 ml-5 mt-3 mb-5">
		
		<div id="productList">
			<div style="font-weight:bold;">
				<form name="prodSelectFrm">
					<span class="mr-3" style="font-size:20pt;">상품목록</span>
					<span class="mr-1">
						<select id="byRegisterdayOrders" name="byRegisterdayOrders" class="productSort">
							<option value="latest" selected>최신순</option>
							<option value="orders">주문수</option>
						</select>
					</span>
					<span>
						<select id="byKind" name="byKind" class="productSort">
							<option value="product_kind" selected>상품종류별</option>
							<option value="flat">플랫</option>
							<option value="loafer">로퍼</option>
							<option value="pumps">펌프스</option>
							<option value="ankle">앵클</option>
							<option value="boots">부츠</option>
							<option value="sneakers">스니커즈</option>
							<option value="slingback">슬링백</option>
							<option value="mule">뮬</option>
						</select>
					</span>
					<span id="productSearch_btn">
						<span><input type="text" id="product_search" name="searchName" placeholder="상품명으로 검색"/></span>
						<span><button type="button" id="product_search_btn" name="product_search_btn" style="border:none; background-color: transparent;">
							<img src="<%= ctxPath%>/images/hyerin/search_icon.png" width="25px"/>
						</button></span>
					</span>
				</form>
			</div>
			<form name="admin_productList_frm">
				<table id="admin_productList" class="mt-4 w-100" style="font-size:10pt; border-right:none; border-left:none;"> <%-- 글은 10개까지만 보여주고 그 이상은 다음페이지로 넘기기 --%>
					<thead>
						<tr>
							<th width="5%" height="50px" class="admin_productList_th text-center"><input type="checkbox" id="productAll" name="productList_chx"/></th>
							<th width="10%" height="50px" class="admin_productList_th text-center">No</th>
							<th width="10%" class="admin_productList_th text-center">상품 종류</th>
							<th width="15%" class="admin_productList_th text-center">상품 이미지</th>
							<th width="15%" class="admin_productList_th text-center" >상품명</th>
							<th width="10%" class="admin_productList_th text-center" >상품 가격</th>
							<th width="10%" class="admin_productList_th text-center" >재고 수량</th>
							<th width="5%" class="admin_productList_th text-center" >메인노출</th>
							<th width="5%" class="admin_productList_th text-center" >수정</th>
							<th width="5%" class="admin_productList_th text-center" >삭제</th>
						</tr>
					</thead>
					<tbody>
						
					</tbody>
				</table>
				<div class="mt-3">
					<span><button type="button" id="" class="white" style="height:30px;">선택노출/숨김</button></span>
					<span><button type="button" id="" class="black" style="height:30px;">선택삭제</button></span>
				</div>
			</form>
			<nav aria-label="Page navigation">
			  <ul class="pagination justify-content-center pagination-sm my-5">
			    <li class="page-item">
			      <a class="page-link" href="#" aria-label="Previous">
			        <span aria-hidden="true">&laquo;</span>
			      </a>
			    </li>
			    <li class="page-item"><a class="page-link" href="#">1</a></li>
			    <li class="page-item"><a class="page-link" href="#">2</a></li>
			    <li class="page-item"><a class="page-link" href="#">3</a></li>
			    <li class="page-item">
			      <a class="page-link" href="#" aria-label="Next">
			        <span aria-hidden="true">&raquo;</span>
			      </a>
			    </li>
			  </ul>
			</nav>
		</div>
	</div>
</div>
<jsp:include page="/WEB-INF/hyerin/footer.jsp"></jsp:include>