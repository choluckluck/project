package heajun.product.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;


public class ProductDetailDAO implements InterProductDetailDAO {
	private DataSource ds;   //DataSource ds 는 아파치톰캣이 제공하는 DBCP(DB Connection Pool)
	   private Connection conn;
	   private PreparedStatement pstmt;
	   private ResultSet rs;
	   
	   // 생성자
	   public ProductDetailDAO() {
	      try {
	         Context initContext = new InitialContext();
	          Context envContext  = (Context)initContext.lookup("java:/comp/env");
	          ds = (DataSource)envContext.lookup("jdbc/semi");
	      } catch(NamingException e) {
	         e.printStackTrace();
	      }
	   }
	   
	   //사용한 자원을 반납하는 close() 메소드 생성하기
	   private void close() {
	      try {
	         if(rs != null) { rs.close(); rs = null;}
	         if(pstmt != null) { pstmt.close(); pstmt = null;}
	         if(conn != null) { conn.close(); conn = null;}
	      } catch (SQLException e) {
	         e.printStackTrace();
	      }
	      
	   }
	// 제품번호를 가지고서 해당 제품의 정보를 조회해오기 
	@Override
	public ProductVO_HJ selectProduct(String prod_code) throws SQLException {
	        
		ProductVO_HJ pvo = new ProductVO_HJ();
		
		try {
			
			 conn = ds.getConnection();
			
			  
		     String sql = " select prod_code, prod_name, prod_kind, prod_image, prod_high, prod_price, prod_registerday, md_pick_yn, prod_saleprice, prod_description, prod_point "+
						  " from tbl_product "+
						  " where prod_code =   ? ";
	
		     
		     
		     
		     
		     System.out.println(prod_code);
			 
			 pstmt = conn.prepareStatement(sql);
			 pstmt.setString(1, prod_code);
			 
			 			 
			 rs = pstmt.executeQuery();
			 
			 if(rs.next()) {
				 
				 
				 pvo.setProd_code(rs.getString(1));
				 pvo.setProd_name(rs.getString(2));
				 pvo.setProd_kind(rs.getString(3));
				 pvo.setProd_image(rs.getString(4));
				 pvo.setProd_high(rs.getString(5));
				 pvo.setProd_price(rs.getInt(6));
				 pvo.setProd_registerday(rs.getString(7));
				 pvo.setMd_pick_yn(rs.getString(8));
				 pvo.setProd_saleprice(rs.getInt(9));
				 pvo.setProd_description(rs.getString(10));
				 pvo.setProd_point(rs.getInt(11));
				 
				 
				 
			 }// end of while-----------------------------
			 
		} finally {
			close();
		}
		
		return pvo;		
	}

	// 옵션 선택을 위한 해당 상품의 옵션 정보가져오기 
	@Override
	public List<ProductVO_HJ> selectProductOption(String prod_code) throws SQLException {

		List<ProductVO_HJ> option = new ArrayList<>();
		
		
		try {
			conn = ds.getConnection();
			
			   String sql = " select prod_code, prod_name, prod_kind,  prod_image, prod_high, prod_price, prod_registerday, md_pick_yn, prod_saleprice, prod_description, prod_point, prod_color, prod_size, prod_stock , fk_userid ,qnty "+
					   "		            from (  " +
					   "		                   select prod_code, prod_name, prod_kind,  prod_image, prod_high, prod_price, prod_registerday, md_pick_yn, prod_saleprice, prod_description, prod_point, prod_color, prod_size, prod_stock ,fk_userid ,qnty "+
					   "		                   from tbl_product P  " +
					   "		                   join tbl_prod_detail O  " +
					   "		                   on P.prod_code = O.fk_prod_code " +
					   "		                   join tbl_cart C  "+
					   "		                   on O.fk_prod_code = C.fk_prod_code  "+
					   "		                  ) V  " +
					   "		                where prod_code = ?  " ;
					
			
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, prod_code);
		
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				
				
				ProductVO_HJ pvo = new ProductVO_HJ();
				
				 pvo.setProd_code(rs.getString(1));
				 pvo.setProd_name(rs.getString(2));
				 pvo.setProd_kind(rs.getString(3));
				 pvo.setProd_image(rs.getString(4));
				 pvo.setProd_high(rs.getString(5));
				 pvo.setProd_price(rs.getInt(6));
				 pvo.setProd_registerday(rs.getString(7));
				 pvo.setMd_pick_yn(rs.getString(8));
				 pvo.setProd_saleprice(rs.getInt(9));
				 pvo.setProd_description(rs.getString(10));
				 pvo.setProd_point(rs.getInt(11));
				 
				 ProductDetailVO ovo = new ProductDetailVO();
				 
				 ovo.setProd_color(rs.getString(12));
				 ovo.setProd_size(rs.getString(13));
				 ovo.setProd_stock(rs.getInt(14));
				
				 pvo.setFk_prod_code(ovo);
				
				  CartVO_HJ cvo = new CartVO_HJ();
				    cvo.setFk_userid(rs.getString(15));
				    cvo.setQnty(rs.getInt(16));
				    
				    ovo.setFK_prod_code(cvo);
				    
				option.add(pvo);
			}// end of while(rs.next())-----------------------
			
		} finally {
			close();
		}		
	
		return option;
	}


	

	//위시리스트에 값보내기
	@Override
	public int addWish(String userid , String prod_code , String prod_color, String prod_size) throws SQLException {
		
		int result = 0;
	      
	      try {
	         conn = ds.getConnection();
	     
		            
	         String sql = " insert into tbl_like(like_code, fk_userid, fk_prode_code,  fk_prode_color ,fk_prod_size) "
		               + " values(seq_like_code.nextval, ?, ?, ?, ? , ?) ";
		            
	            pstmt.setString(1, userid );
	            pstmt.setString(2, prod_code);
	            pstmt.setString(3, prod_color);
	            pstmt.setString(4, prod_size);
	            
		            result = pstmt.executeUpdate();
	      } finally {
		         close();
		      }
		      
		      return result;		
		}  
		      
	

	//장바구니에 값 보내기
	@Override
	public int addCart(String userid , String prod_code, String prod_color, String prod_size, String qnty) throws SQLException {
		
		int result = 0;
		
	      try {
	         conn = ds.getConnection();
	            
	           String sql = " insert into tbl_cart(cart_code, fk_userid , fk_prod_code, qnty, fk_prod_color , fk_prod_size) "
	               + " values(seq_cart_code.nextval,  ?, ?, ?, ? ) ";
	            
	            pstmt = conn.prepareStatement(sql);
	            pstmt.setString(1, userid );
	            pstmt.setString(2, prod_code);
	            pstmt.setInt(3, Integer.parseInt(qnty));
	            pstmt.setString(4, prod_color);
	            pstmt.setString(5, prod_size);
	            
	            pstmt.executeUpdate();
	         
	         
	      } finally {
	         close();
	      }
	      
	      return result;		
	}

	
	
	

	  
	
		
}
