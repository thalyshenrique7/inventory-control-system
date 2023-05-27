package com.devthalys.inventorycontrolsystem.rest.controllers;

import com.devthalys.inventorycontrolsystem.dtos.FieldsListInventoryDto;
import com.devthalys.inventorycontrolsystem.dtos.MovementUpdateDto;
import com.devthalys.inventorycontrolsystem.enums.MovementType;
import com.devthalys.inventorycontrolsystem.exceptions.ProductAlreadyExistsException;
import com.devthalys.inventorycontrolsystem.exceptions.ProductNotFoundException;
import com.devthalys.inventorycontrolsystem.exceptions.UnauthorizedAccessException;
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
    public ResponseEntity<List<FieldsListInventoryDto>> listByProductStock() {
        return ResponseEntity.status(HttpStatus.OK).body(inventoryService.listByProductStock());
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
    public ResponseEntity<Object> update(@PathVariable Long id, @RequestBody @Valid MovementUpdateDto stockDto){
        ProductModel findStock = productService.findById(id);
        if(findStock == null){
            throw new ProductNotFoundException("Produto não cadastrado no sistema.");
        }

        InventoryModel newStock = findStock.getInventory();
        newStock.setProduct(findStock.getInventory().getProduct());
        newStock.setMovementType(stockDto.getMovementType());
        newStock.setBalance(stockDto.getBalance());
        newStock.setMovementDate(LocalDateTime.now());
        newStock.setReason(stockDto.getReason());
        newStock.setDocument(stockDto.getDocument());
        newStock.setProductCategory(stockDto.getProductCategory());

        inventoryService.update(newStock);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Registro atualizado com sucesso.");
    }
}
