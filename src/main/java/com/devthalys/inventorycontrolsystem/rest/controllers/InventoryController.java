package com.devthalys.inventorycontrolsystem.rest.controllers;

import com.devthalys.inventorycontrolsystem.dtos.FieldsListInventoryDto;
import com.devthalys.inventorycontrolsystem.dtos.MovementUpdateDto;
import com.devthalys.inventorycontrolsystem.enums.MovementType;
import com.devthalys.inventorycontrolsystem.exceptions.InventoryException;
import com.devthalys.inventorycontrolsystem.exceptions.ProductAlreadyExistsException;
import com.devthalys.inventorycontrolsystem.exceptions.ProductNotFoundException;
import com.devthalys.inventorycontrolsystem.models.ProductModel;
import com.devthalys.inventorycontrolsystem.models.InventoryModel;
import com.devthalys.inventorycontrolsystem.services.impl.ProductServiceImpl;
import com.devthalys.inventorycontrolsystem.services.impl.InventoryServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@CrossOrigin(value = "*")
@RequestMapping(value = "/inventory")
@Api(value = "Inventory Control System")
public class InventoryController {

    @Autowired
    private InventoryServiceImpl inventoryService;

    @Autowired
    private ProductServiceImpl productService;

    @GetMapping(value = "/search_by_name/{productName}")
    @ApiOperation(value = "Releases by product")
    @ApiResponse(code = 200, message = "OK")
    public ResponseEntity<List<InventoryModel>> findByProduct(@PathVariable String productName){
        if(!inventoryService.existsByProductName(productName)){
            throw new ProductNotFoundException("Produto não cadastrado no sistema.");
        }
        return ResponseEntity.status(HttpStatus.OK).body(inventoryService.findByProductName(productName));
    }

    @GetMapping(value = "/search_between_periods")
    @ApiOperation(value = "Entries between periods")
    @ApiResponse(code = 200, message = "OK")
    public ResponseEntity<List<InventoryModel>> findByDateMovement(@RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
                                                                   @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate){
        if(!inventoryService.existsByDateMovementBetween(startDate, endDate)){
            throw new ProductNotFoundException("Produto não cadastrado no sistema.");
        }
        return ResponseEntity.status(HttpStatus.OK).body(inventoryService.findByDateMovementBetween(startDate, endDate));
    }

    @GetMapping(value = "/search_by_movement_type/{movementType}")
    @ApiOperation(value = "Releases by movement type")
    @ApiResponse(code = 200, message = "OK")
    public ResponseEntity<List<InventoryModel>> findByMovementType(@PathVariable MovementType movementType) {
        if (!inventoryService.existsByMovementType(movementType)) {
            throw new ProductNotFoundException("Produto não cadastrado no sistema.");
        }
        return ResponseEntity.status(HttpStatus.OK).body(inventoryService.findByMovementType(movementType));
    }

    @GetMapping(value = "/list_order_by_product")
    @ApiOperation(value = "Entries order by products")
    @ApiResponse(code = 200, message = "OK")
    public ResponseEntity<List<InventoryModel>> findByOrderByProduct(){
        return ResponseEntity.status(HttpStatus.OK).body(inventoryService.findByOrderByProduct());
    }

    @GetMapping(value = "/list_order_by_movement_date")
    @ApiOperation(value = "Entries order by periods")
    @ApiResponse(code = 200, message = "OK")
    public ResponseEntity<List<InventoryModel>> findByOrderByDateMovement(){
        return ResponseEntity.status(HttpStatus.OK).body(inventoryService.findByOrderByDateMovement());
    }

    @GetMapping(value = "/list_inventory")
    @ApiOperation(value = "Product list on inventory")
    @ApiResponse(code = 200, message = "OK")
    public ResponseEntity<List<FieldsListInventoryDto>> listByProductInventory() {
        return ResponseEntity.status(HttpStatus.OK).body(inventoryService.listByProductInventory());
    }

    @GetMapping(value = "/list_balance_less")
    @ApiOperation(value = "Product list by balance less than quantity minimum")
    @ApiResponse(code = 200, message = "OK")
    public ResponseEntity<List<InventoryModel>> findProductByBalanceLessThanQuantityMin(){
        List<InventoryModel> inventory = inventoryService.findProductByBalanceLessThanQuantityMin();
        if(inventory == null){
            ResponseEntity.status(HttpStatus.NOT_FOUND).body("Lista de produtos não encontrada ou não existe.");
        }
        return ResponseEntity.status(HttpStatus.OK).body(inventoryService.findProductByBalanceLessThanQuantityMin());
    }

    @GetMapping(value = "/list_balance_greater")
    @ApiOperation(value = "Product list by balance greater than quantity minimum")
    @ApiResponse(code = 200, message = "OK")
    public ResponseEntity<List<InventoryModel>> findProductByBalanceGreaterThanQuantityMin(){
        List<InventoryModel> inventory = inventoryService.findProductByBalanceGreaterThanQuantityMin();
        if(inventory == null){
            ResponseEntity.status(HttpStatus.NOT_FOUND).body("Lista de produtos não encontrada ou não existe.");
        }
        return ResponseEntity.status(HttpStatus.OK).body(inventoryService.findProductByBalanceGreaterThanQuantityMin());
    }

    @GetMapping(value = "/best_seller")
    public ResponseEntity<String> findByBestSeller(){
        String bestSeller = inventoryService.verifyBestSeller();
        if(bestSeller.isEmpty()){
            throw new ProductNotFoundException("Produto não cadastrado no sistema.");
        }
        return ResponseEntity.status(HttpStatus.OK).body(bestSeller);
    }

    @Transactional
    @PostMapping("/save")
    @ApiOperation(value = "Save Movement")
    @ApiResponses({ @ApiResponse(code = 201, message = "Movement on inventory registered success"),
                    @ApiResponse(code = 403, message = "User do not authorized")})
    public ResponseEntity<String> save(@RequestBody InventoryModel inventory) {
        Long product = inventory.getProduct().getBarCode();
        if(productService.existsByBarCode(product)){
            throw new ProductAlreadyExistsException("Código de Barras já possui cadastro no sistema.");
        }

        inventoryService.save(inventory);
        return ResponseEntity.status(HttpStatus.CREATED).body("Movimentação de estoque registrada com sucesso.");
    }

    @Transactional
    @PutMapping(value = "/update/{id}")
    @ApiOperation(value = "Update Register")
    @ApiResponses({ @ApiResponse(code = 204, message = "Update Register Success"),
                    @ApiResponse(code = 404, message = "Product do not registered in system")})
    public ResponseEntity<Object> update(@PathVariable Long id, @RequestBody @Valid MovementUpdateDto inventoryDto){
        ProductModel product = productService.findById(id);
        if(product == null){
            throw new ProductNotFoundException("Produto não cadastrado no sistema.");
        }

        InventoryModel newInventory = product.getInventory();
        newInventory.setProduct(product.getInventory().getProduct());
        newInventory.setMovementType(inventoryDto.getMovementType());
        newInventory.setQuantity(inventoryDto.getQuantity());
        newInventory.setMovementDate(LocalDateTime.now());
        newInventory.setReason(inventoryDto.getReason());
        newInventory.setDocument(inventoryDto.getDocument());
        newInventory.setProductCategory(inventoryDto.getProductCategory());

        inventoryService.update(newInventory);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Registro atualizado com sucesso.");
    }

    @Transactional
    @DeleteMapping(value = "/delete/{id}")
    @ApiOperation(value = "Delete Register")
    @ApiResponses({ @ApiResponse(code = 200, message = "Delete Register Success"),
                    @ApiResponse(code = 404, message = "Inventory do not registered in system or not exists.")})
    public ResponseEntity<Object> delete(@PathVariable Long id){
        InventoryModel inventory = inventoryService.findById(id);
        if(inventory == null){
            throw new InventoryException("Estoque não cadastrado no sistema ou não existe.");
        }
        inventoryService.delete(inventory);
        return ResponseEntity.status(HttpStatus.OK).body("Estoque deletado com sucesso");
    }
}
