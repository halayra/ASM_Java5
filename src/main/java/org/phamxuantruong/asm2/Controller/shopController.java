package org.phamxuantruong.asm2.Controller;

import org.phamxuantruong.asm2.Entity.Categories;
import org.phamxuantruong.asm2.Entity.Product;
import org.phamxuantruong.asm2.Interface.CategoriesDAO;
import org.phamxuantruong.asm2.Interface.ProductDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Controller
public class shopController {
    @Autowired
    ProductDAO productDAO;
    @Autowired
    CategoriesDAO categoriesDAO;

    @RequestMapping("shop")
    public String shop(Model model,
                       @RequestParam(value = "field", required = false) Optional<String> field,
                       @RequestParam(value = "page", required = false) Optional<String> page,
                       @RequestParam(value = "size", required = false) Optional<Integer> size,
                       @RequestParam(value = "name", required = false) Optional<String> name,
                       @RequestParam(value = "category", required = false) Optional<String> categoryString
    ){
        String sortField = field.orElse("price");
        int currentPage = page.map(Integer::parseInt).orElse(0);
        int pageSize = size.orElse(9);
        String productName = name.orElse("");

        // Chuyển đổi Optional<String> thành Long
        Long categoryId = categoryString.map(Long::valueOf).orElse(null);

        Sort sort = Sort.by(Sort.Direction.ASC, sortField);
        Pageable pageable = PageRequest.of(currentPage, pageSize, sort);
        Page<Product> productPage;

        if (categoryId != null) {
            productPage = productDAO.findByCategoryIDAndNameContaining(categoryId, productName, pageable);
        } else {
            productPage = productDAO.findByNameContaining(productName, pageable);
        }
        // Lấy giá trị cho selectedCategory từ tham số 'category' của yêu cầu
        String selectedCategory = categoryString.orElse(null);

// Thêm selectedCategory vào model nếu nó không phải là null
        if (selectedCategory != null) {
            model.addAttribute("selectedCategory", selectedCategory);
        }

        List<Categories> categories =categoriesDAO.findAll();
        model.addAttribute("categories", categories);
        model.addAttribute("items", productPage.getContent());
        model.addAttribute("productPage", productPage);
        model.addAttribute("field", sortField);
        model.addAttribute("name", productName);
        return "home/shop";
    }
    @RequestMapping("detail")
    public String detail(Model model) {
        List<Product> products = productDAO.findAll();
        model.addAttribute("products", products);
        return "home/detail";
    }
    @GetMapping("/chitiet/{id}")
    public String xemchitiet(@PathVariable("id") Long id, Model model) {

        Product product = productDAO.findById(id).orElse(null);
        if(product == null){
            return "redirect:/";
        }
        model.addAttribute("product", product);
        return "home/detail";
    }

}
