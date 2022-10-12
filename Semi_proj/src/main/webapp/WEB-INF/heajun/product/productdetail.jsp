<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%
    String ctxPath = request.getContextPath();
%>


<jsp:include page="/WEB-INF/hyerin/header.jsp"></jsp:include> 


<style type="text/css">
/*div {
		margin: 10px, 0px;
	}
	
	
td{padding: 10px;}
    


  ul{
    list-style-type: none;
    position:relative;
    right: 30px;
  }

  div.info {
   position:relative;
   left: 600px;
   bottom:700px;
  }

  div.keyImage{
   padding: 40px;

  }
  
  button#submit{
   margin-right:-4px;
  }

  button#submit2{
   margin-right: 7px;
  }

  button#submit3{
   margin-left:-3px;
   background-color: black;
   color: white;
  }
   */
  .carousel-inner > .item > img {
      top: 0;
      left: 0;
      min-width: 100%;
      min-height: 400px;
    } 
  
  /* 
table#code{
position:relative;
right:35px;
}
 */
.form-select{
 border-color: #e6e6e6;
 border-radius:5px;
 width:12px;
}

</style>



<script type="text/javascript">

	
$(document).ready(function() {
	
	  // === 주문개수 스피터 달기 === //
	   $("input#spinner").spinner( {
		   spin: function(event, ui) {
			   if(ui.value > 100) {
				   $(this).spinner("value", 100);
				   return false;
			   }
			   else if(ui.value < 1) {
				   $(this).spinner("value", 1);
				   return false;
			   }
		   }
	   } );// end of $("input#spinner").spinner({});------------- 
	   
	   
	 
	   
});//end of $(document).ready(function() {--------------------

	
//Function Declaration

function checkCart() {
   
   // === 주문량에 대한 유효성 검사하기 === //
    
    const frm = document.cartFrm;
    const regExp = /^[0-9]+$/; // 숫자만 체크하는 정규표현식
    const qnty = frm.qnty.value; 
    const bool = regExp.test(qnty);
    
    if(!bool) {
       // 숫자 이외의 값이 들어온 경우 
       alert("수량은 1개 이상이어야 합니다.");
       frm.qnty.value = "1";
       frm.qnty.focus();
       return; // 종료
    }
    
    // 문자형태로 숫자로만 들어온 경우
    if( Number(qnty) < 1 ) {
       alert("수량은 1개 이상이어야 합니다.");
       frm.qnty.value = "1";
       frm.qnty.focus();
       return; // 종료
    }
}


// 관심상품 바로가기
function goWish() {
   
   checkCart();
   
   const frm = document.cartFrm;
      frm.method = "POST";
      frm.action = "<%=request.getContextPath()%>/heajun/product/addWish.sue";
      frm.submit();

}


// 장바구니 바로가기
function goCart() {
      
      checkCart();
      
      // 주문개수가 1개 이상인 경우
      const frm = document.cartFrm;
      frm.method = "POST";
      frm.action = "<%=request.getContextPath()%>/heajun/product/addCart.sue";
      frm.submit();
      
}// end of function goCart()------------------------------ 


function goOrder() {

   checkCart();
   
   // 주문개수가 1개 이상인 경우
   const frm = document.cartFrm;
   
   let totalPrice = Number("${requestScope.pvo2.prod_price}")*Number($("input#spinner").val()); // 정가 * 수량
   let sumtotalPrice = Number("${requestScope.pvo2.prod_saleprice}")*Number($("input#spinner").val()); // 판매가 * 수량
   let sumtotalpoint = Number("${requestScope.pvo2.prod_point}")*Number($("input#spinner").val()); // 포인트 * 수량
      
   frm.totalPrice.value=totalPrice;
   frm.sumtotalPrice.value=sumtotalPrice;
   frm.sumtotalpoint.value=sumtotalpoint;
   
   frm.method = "POST";
      frm.action = "<%=request.getContextPath()%>/heajun/product/directOrder.sue";
   frm.submit();

}// end of function goOrder()------------------------------
	

</script>

<form name="cartFrm">
	<input type="hidden" id="prod_code" name="prod_code" value="${requestScope.pvo2.prod_code}" />				
	<input type="hidden" name="totalPrice" value=""/>
	<input type="hidden" name="sumtotalPrice" value=""/>
	<input type="hidden" name="sumtotalpoint" value=""/>
	<input type="hidden" name="price" value="${requestScope.pvo2.prod_price}"/>
	<input type="hidden" name="saleprice" value="${requestScope.pvo2.prod_saleprice}"/>
</form>

<div class="row container-fluid" style="font-size: 12pt; margin:50px 0;  padding:0;">
	<div class="col-1"></div>
	<div class="col-3">
		<img src="<%= ctxPath%>/images/product/${requestScope.pvo2.prod_image}" width="500px" alt="..." >
	</div>
	<div class="col-1"></div>
	<div class="col-7">
		<table style="border:none;">
			<tr height="40px">
				<td colspan="3" style="font-size: 13pt; vertical-align: bottom;" width="10%" >
					${requestScope.pvo2.prod_name}<span class="ml-2">(${requestScope.pvo.prod_high}cm)</span>
				</td>
			</tr>
			<tr>
				<td colspan="3" class="pb-5">
					<span style="font-weight:bold; font-size:14pt;"><fmt:formatNumber value="${requestScope.pvo2.prod_saleprice}" pattern="#,###"/>원</span>
					<c:if test="${requestScope.pvo2.prod_saleprice ne requestScope.pvo2.prod_price}">
						<span style="color:#bfbfbf; text-decoration: line-through;" class="ml-2"><fmt:formatNumber value="${requestScope.pvo2.prod_price}" pattern="#,###"/>원</span>
					</c:if>
				</td>
			</tr>
			<tr height="40px">
				<td >PROD CODE</td>
				<td>${requestScope.pvo2.prod_code}</td>
			</tr>
			<tr height="40px">
				<td>적립금</td>
				<td><fmt:formatNumber value="${requestScope.pvo2.prod_point}" pattern="#,###"/>원</td>
			</tr>
			<tr height="40px">
				<td>배송비</td>
				<td>2,500원<span class="ml-2" style="font-size:10pt;">(70,000원 이상 구매 시 무료)</span></td>
			</tr>
			<tr height="50px">
				<td>COLOR</td>
				<td>
					<select class="pvoList form-select" name="prod_color" style="width: 250px; font-size:15px; margin-left:1px; height:30px;">    
						<c:forTokens var="prod_color" items="${requestScope.pvo2.pdvo.prod_color}" delims=",">
							<option value="${prod_color}">${prod_color}</option>   
						</c:forTokens>  
					</select>
				</td>
			</tr>
			<tr height="50px">
				<td>SIZE</td>
				<td>
					<select  class="pvoList form-select" name="prod_size" style="width: 250px; font-size:15px; margin-left:1px; height:30px; ">      
						<c:forTokens var="prod_size" items="${requestScope.pvo2.pdvo.prod_size}" delims=",">
							<option value="${prod_size}">${prod_size}</option>   
						</c:forTokens>  
					</select>
				</td>
			</tr>
			<tr height="50px">
				<td class="align-items-baseline"><label for="spinner">주문수량</label></td>
				<td>
					<input id="spinner" name="qnty" value="1" style="width: 25px;">
				</td>
			</tr>
			<tr height="40px">
			</tr>
			<tr height="40px">
				<td colspan="3">
					<button type ="button" id="button2" style="width:10%;" class="buttons border btn btn-outline-secondary" onclick="goWish();">♡</button>
					<button type ="button" id="button"  style="width:40%;" class="buttons border btn btn-outline-secondary"  onclick="goCart();">장바구니</button>
					<button type ="button" id="button3" style="width:40%;" class="buttons border btn btn-outline-secondary" onclick="goOrder();">바로구매</button>
				</td>
			</tr>
		</table>
	</div>
	
</div>

<div style="height:50px; clear:both;"></div>
<!-- 상품정보, 리뷰, 상품문의-->
<div class="row container-fluid" style="text-align:center; font-size:12pt; height:60px; border-bottom:solid 1px #d9d9d9;">
	<div class="col-4" style="padding:10px;  color:gray; ">
		<a href="#" style="color:gray; text-decoration:none;">상품정보</a>
	</div>
	<div class="col-4" style="padding:10px; ">
		<a href="#" style="color:gray; text-decoration:none;">리뷰(<span id=""></span>)</a>
	</div>
	<div class="col-4" style="padding:10px; ">
		<a href="#" style="color:gray; text-decoration:none;">상품문의</a>
	</div>	
</div>

<%-- 상품 상세 이미지 보여주기 시작 --%>

<div class="row container-fluid" style="margin-top:120px;">
	<c:forTokens var="product_image_file" items="${requestScope.ivo.product_image_file}" delims=",">
		<div class="col-12" align="center">
			<img src="<%= ctxPath%>/images/product/${product_image_file}" style="margin: 0 auto; width:50%; object-fit:cover; position:relative;" alt="...">
		</div>
	</c:forTokens>
	<div class="col-12" align="center">
		<img src="<%= ctxPath%>/images/heajun/Pdetailimage1.jpg" style="margin: 0 auto; width:50%; object-fit:cover; position:relative;" alt="...">
	</div>
	<div class="col-12" align="center">
		<img src="<%= ctxPath%>/images/heajun/Pdetailimage2.jpg" style="margin: 0 auto; width:50%; object-fit:cover; position:relative;" alt="...">
	</div>
</div>  


<div id="prdreview" style="width:125%; margin: 0 auto;">	
	<jsp:include page="/WEB-INF/seongmin/review_include.jsp"></jsp:include> 
</div> 
<jsp:include page="/WEB-INF/seongmin/qnaList_include.jsp"></jsp:include> 
<jsp:include page="/WEB-INF/hyerin/footer.jsp"></jsp:include> 