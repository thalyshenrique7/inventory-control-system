package com.devthalys.inventorycontrolsystem.rest.controllers;

import com.devthalys.inventorycontrolsystem.exceptions.InvoiceException;
import com.devthalys.inventorycontrolsystem.models.InvoiceModel;
import com.devthalys.inventorycontrolsystem.services.impl.InvoiceServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(value = "*")
@RequestMapping(value = "/invoice")
@Api(value = "Inventory Control System")
public class InvoiceController {

    @Autowired
    private InvoiceServiceImpl invoiceService;

    @GetMapping(value = "/{numberInvoice}")
    @ApiOperation(value = "Find Invoice By Number")
    @ApiResponses({ @ApiResponse(code = 200, message = "OK"),
                    @ApiResponse(code = 404, message = "Invoice Not Found")})
    public ResponseEntity<InvoiceModel> findByNumberInvoice(@PathVariable Long numberInvoice){
        if(!invoiceService.existsNumberInvoice(numberInvoice)){
            throw new InvoiceException("Nota Fiscal não encontrada no sistema.");
        }
        return ResponseEntity.status(HttpStatus.OK).body(invoiceService.findByNumberInvoice(numberInvoice));
    }

    @Transactional
    @DeleteMapping(value = "/cancel/{numberInvoice}")
    @ApiOperation(value = "Cancel Invoice")
    @ApiResponses({ @ApiResponse(code = 204, message = "Invoice deleted success"),
                    @ApiResponse(code = 404, message = "Invoice Not Found")})
    public ResponseEntity<Object> cancelInvoice(@PathVariable Long numberInvoice){
        if(!invoiceService.existsNumberInvoice(numberInvoice)){
            throw new InvoiceException("Nota Fiscal não encontrada no sistema.");
        }
        invoiceService.deleteByNumberInvoice(numberInvoice);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Nota Fiscal deletada com sucesso.");
    }
}
