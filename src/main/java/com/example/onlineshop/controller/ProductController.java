package com.example.onlineshop.controller;

import com.example.onlineshop.exception.ProductNotFoundException;
import com.example.onlineshop.model.Product;
import com.example.onlineshop.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@CrossOrigin("http://localhost:3000/")
public class ProductController {
    @Autowired
    private ProductRepository productRepository;

    private MultipartFile multipartFile;

    private byte[] imageBytes;

    @PostMapping("/product")
    Product newProduct(@RequestBody Product newProduct) throws IOException {
        newProduct.setImage(imageBytes);
      return  productRepository.save(newProduct);
    }

    @RequestMapping(value = "/upload-files", headers = "content-type=multipart/*", method = RequestMethod.POST)
    public void upload(@RequestParam(value = "file0", required = true) MultipartFile multipartFile, Integer adminOid) throws IOException {
        System.out.println(StringUtils.cleanPath(multipartFile.getOriginalFilename()));
        this.multipartFile = multipartFile;
       // System.out.println(multipartFile.getOriginalFilename());
        imageBytes = multipartFile.getBytes();
    }

    @GetMapping("/products")
    List<Product> getAllProducts(){
        return productRepository.findAll();
    }


    @GetMapping("/product/{id}")
    Product getUserById(@PathVariable Long id){
        return productRepository.findById(id)
                .orElseThrow(() -> new  ProductNotFoundException(id));
    }

    @DeleteMapping("/delete/product/{id}")
    java.lang.String deleteProduct(@PathVariable Long id) {
        if (!productRepository.existsById(id)) {
            throw new ProductNotFoundException(id);
        } else {
            productRepository.deleteById(id);
            return "Product with id " + id + " has been deleted";
        }
    }

    @PutMapping("/edit/product/{id}")
    Product updateProduct(@RequestBody Product newProduct, @PathVariable Long id) {
        return productRepository.findById(id)
                .map(product -> {
                    product.setName(newProduct.getName());
                    product.setPrice(newProduct.getPrice());
                    product.setImage(imageBytes);
                    product.setQuantity(newProduct.getQuantity());
                    product.setCategory(newProduct.getCategory());
                    return productRepository.save(product);
                }).orElseThrow(() -> new ProductNotFoundException(id));
    }



}
