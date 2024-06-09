package org.phamxuantruong.asm2.Controller;

import java.util.List;

import org.phamxuantruong.asm2.Entity.Categories;
import org.phamxuantruong.asm2.Entity.Product;
import org.phamxuantruong.asm2.Interface.CartDAO;
import org.phamxuantruong.asm2.Interface.CategoriesDAO;
import org.phamxuantruong.asm2.Interface.ProductDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpSession;

@Controller
public class homeIndexController {
    @Autowired
    ProductDAO productDAO;
    @Autowired
    CategoriesDAO categoriesDAO;
    @Autowired
    CartDAO cartDAO;
    
    
    @RequestMapping("/")
    public String home(Model model, HttpSession session) {
        Pageable pageable = PageRequest.of(0, 12);

        String username = (String) session.getAttribute("loggedInUser");
        
        List<Product> products = productDAO.findAll(pageable).getContent();
        List<Categories> categories = categoriesDAO.findAll();
        model.addAttribute("products", products);
        model.addAttribute("categories", categories);
        model.addAttribute("sizeCart", username == null ? 0 : cartDAO.countByUsername(username));
        return "home/index";
    }

}