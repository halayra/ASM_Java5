package org.phamxuantruong.asm2.Controller;

import java.util.ArrayList;
import java.util.List;

import org.phamxuantruong.asm2.Entity.Bill;
import org.phamxuantruong.asm2.Entity.Cart;
import org.phamxuantruong.asm2.Entity.Product;
import org.phamxuantruong.asm2.Interface.BillDAO;
import org.phamxuantruong.asm2.Interface.CartDAO;
import org.phamxuantruong.asm2.Interface.ProductDAO;
import org.phamxuantruong.asm2.Interface.UsersDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;

@Controller
public class CartController {
	@Autowired
	UsersDAO userDAO;
    @Autowired
    ProductDAO productDAO;
    @Autowired
    CartDAO cartDAO;
	@Autowired
	BillDAO billDAO;
    
    @GetMapping("/cart")
    public String getCart(Model model, HttpSession session) {
    	String username = (String) session.getAttribute("loggedInUser");
    	if(username == null) {
    		model.addAttribute("error", "Vui lòng đăng nhập tài khoản");
    		return "redirect:/user/login";
    	}
    	
    	List<Cart> data = cartDAO.findByUsername(username);
    	Double amount = 0.0, shiping = 0.0, total = 0.0;

        for (Cart cart : data) {
        	amount += cart.getTotal();
        }
        
        shiping = amount * 0.05;
        total = amount + shiping;

    	model.addAttribute("listCart", data);
    	model.addAttribute("amount", amount);
    	model.addAttribute("shiping", shiping);
    	model.addAttribute("total", total);
    	model.addAttribute("sizeCart", data.size());
    	return "home/cart";
    }
    
    @GetMapping("/addcart")
	public String addCart(Model model, HttpSession session, @RequestParam("id") Long proID) {
    	String username = (String) session.getAttribute("loggedInUser");
    	if(username == null) {
    		model.addAttribute("error", "Vui lòng đăng nhập tài khoản");
    		return "redirect:/user/login";
    	}
    	
    	System.out.println("Adding product to cart with ID: " + proID);
    	Long idCart = cartDAO.findByUsernameAndProductID(username, proID);
    	Product pro = productDAO.findById(proID).get();
    	Cart cart = null;
    	
    	if(idCart == null) cart = new Cart(null, userDAO.findById(username).get(), pro, 1, 0.0, pro.getPrice());
    	else {
    		cart = cartDAO.findById(idCart).get();
    		cart.setQuantity(cart.getQuantity() + 1);
    		cart.setTotal(pro.getPrice() * cart.getQuantity());
    	}
    	
    	cartDAO.saveAndFlush(cart);
		
		return "redirect:/shop";
	}
    
    @GetMapping("/setcart")
    public String setCart(Model model, HttpSession session, @RequestParam("id") Long proID, @RequestParam("quantity") int quantity) {
    	String username = (String) session.getAttribute("loggedInUser");
    	if(username == null) {
    		model.addAttribute("error", "Vui lòng đăng nhập tài khoản");
    		return "redirect:/user/login";
    	}
    	
    	Long idCart = cartDAO.findByUsernameAndProductID(username, proID);
    	System.out.println("Update product in cart with ID: " + idCart);
    	Cart oldCart = cartDAO.findById(idCart).get();
    	oldCart.setQuantity(quantity);
    	oldCart.setTotal(oldCart.getProduct().getPrice() * quantity);
    	cartDAO.saveAndFlush(oldCart);
    	
    	return "redirect:/cart";
    }

	@GetMapping("/delcart/{id}")
	public String delCart(@PathVariable Long id, Model model) {
		System.out.println("Deleting cart with ID: " + id);
		// Kiểm tra xem có bản ghi nào trong bảng `Bill` tham chiếu đến `CartID` này không
		List<Bill> bills = billDAO.findByCartId(id);
		if (!bills.isEmpty()) {
			// Nếu có, thông báo cho người dùng và không xóa bản ghi từ `Cart`
			model.addAttribute("error", "Không thể xóa giỏ hàng vì đã có hóa đơn tham chiếu đến nó.");
			return "redirect:/cart";
		}

		// Nếu không có bản ghi nào tham chiếu, tiến hành xóa bản ghi từ `Cart`
		cartDAO.deleteById(id);
		System.out.println("Cart with ID " + id + " has been deleted.");
		return "redirect:/cart";
	}


	@GetMapping("/checkout")
	public String getCheckOut(Model model, HttpSession session) {
		String username = (String) session.getAttribute("loggedInUser");
		if(username == null) {
			model.addAttribute("error", "Vui lòng đăng nhập tài khoản");
			return "redirect:/user/login";
		}

		List<Cart> data = cartDAO.findByUsername(username);
		Double amount = 0.0, shiping = 0.0, total = 0.0;
		List<Long> billIds = new ArrayList<>();

		for (Cart cart : data) {
			amount += cart.getTotal();
		}

		shiping = amount * 0.05;
		total = amount + shiping;

		// Lưu thông tin hóa đơn vào database và lưu trữ BillID
		for (Cart cart : data) {
			Bill bill = new Bill();
			bill.setCartId(cart.getCartId());
			bill.setProductId(cart.getProduct().getProductID());
			bill = billDAO.save(bill);
			billIds.add(bill.getBillID()); // Lưu trữ BillID sau khi lưu vào database
		}

		model.addAttribute("listCart", data);
		model.addAttribute("amount", amount);
		model.addAttribute("shiping", shiping);
		model.addAttribute("total", total);
		model.addAttribute("sizeCart", data.size());
		model.addAttribute("user", userDAO.findById(username).get());
		model.addAttribute("billIds", billIds); // Thêm danh sách BillID vào model

		return "home/checkout";
	}

}
