package com.devthalys.inventorycontrolsystem.rest.controllers;

import com.devthalys.inventorycontrolsystem.dtos.ProductUpdateDto;
import com.devthalys.inventorycontrolsystem.exceptions.ProductAlreadyExistsException;
import com.devthalys.inventorycontrolsystem.exceptions.ProductNotFoundException;
import com.devthalys.inventorycontrolsystem.models.ProductModel;
import com.devthalys.inventorycontrolsystem.services.impl.ProductServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(value = "*")
@RequestMapping(value = "/products")
@Api(value = "Inventory Control System")
public class ProductController {

    @Autowired
    private ProductServiceImpl productService;

    @GetMapping(value = "/search_by_name/{name}")
    @ApiOperation(value = "Find by product name")
    @ApiResponse(code = 200, message = "OK")
    public ResponseEntity<List<ProductModel>> findByName(@PathVariable String name){
        if(productService.findByNameIgnoreCase(name) == null){
            ResponseEntity.status(HttpStatus.NOT_FOUND).body("Produto não encontrado");
        }
        return ResponseEntity.status(HttpStatus.OK).body(productService.findByNameIgnoreCase(name));
    }

    @GetMapping(value = "/search_barcode/{barcode}")
    public ResponseEntity<ProductModel> findByBarcode(@PathVariable Long barcode){
        if(!productService.existsByBarCode(barcode)){
            throw new ProductNotFoundException("Produto não cadastrado no sistema");
        }
        return ResponseEntity.status(HttpStatus.OK).body(productService.findByBarCode(barcode));
    }

    @Transactional
    @PostMapping(value = "/save")
    @ApiOperation(value = "Product Register")
    @ApiResponses({ @ApiResponse(code = 201, message = "Product register success"),
                    @ApiResponse(code = 409, message = "Conflict: Barcode already have registered on system")})
    public ResponseEntity<ProductModel> save(@RequestBody ProductModel product){
        if(productService.existsByBarCode(product.getBarCode())){
            throw new ProductAlreadyExistsException("Código de Barras já possui cadastro no sistema.");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.save(product));
    }

    @Transactional
    @DeleteMapping(value = "/delete/{id}")
    @ApiOperation(value = "Delete Product")
    @ApiResponses({ @ApiResponse(code = 200, message = "Product deleted success"),
                    @ApiResponse(code = 404, message = "Product do not have registered on system")})
    public ResponseEntity<Object> delete(@PathVariable Long id){
        if(productService.findById(id) == null){
            throw new ProductNotFoundException("Produto não cadastrado no sistema");
        }
        productService.deleteById(id);
        return ResponseEntity.status(HttpStatus.OK).body("Produto deletado com sucesso");
    }

    @Transactional
    @PutMapping(value = "/update/{id}")
    @ApiOperation(value = "Update Product")
    @ApiResponses({ @ApiResponse(code = 201, message = "Product updated success"),
                    @ApiResponse(code = 404, message = "Product do not registered on system")})
    public ResponseEntity<Object> updateProduct(@PathVariable Long id, @RequestBody @Valid ProductUpdateDto product){
        ProductModel findProduct = productService.findById(id);
        if(findProduct == null){
            throw new ProductNotFoundException("Produto não cadastrado no sistema.");
        }

        findProduct.setName(product.getName());
        findProduct.setQuantityMin(product.getQuantityMin());
        findProduct.setPrice(product.getPriceUnit());
        findProduct.getInventory().setProductCategory(product.getProductCategory());

        productService.update(findProduct);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Produto atualizado com sucesso.");
    }
}
