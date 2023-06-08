package com.devthalys.inventorycontrolsystem.rest.controllers;

import com.devthalys.inventorycontrolsystem.exceptions.BinException;
import com.devthalys.inventorycontrolsystem.models.BinModel;
import com.devthalys.inventorycontrolsystem.services.impl.BinServiceImpl;
import com.devthalys.inventorycontrolsystem.services.impl.InventoryServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(value = "*")
@RequestMapping(value = "/bin")
@Api(value = "Inventory Control System")
public class BinController {

    @Autowired
    private BinServiceImpl binService;

    @Autowired
    private InventoryServiceImpl inventoryService;

    @GetMapping(value = "/")
    @ApiOperation(value = "Find all reports in bin")
    @ApiResponse(code = 200, message = "OK")
    public ResponseEntity<List<BinModel>> findAll(){
        return ResponseEntity.status(HttpStatus.OK).body(binService.findAll());
    }

    @PostMapping(value = "/restore/{id}")
    public ResponseEntity<Object> restore(@PathVariable Long id){
        BinModel binId = binService.findById(id);
        if(binId == null){
            throw new BinException("Lixeira não encontrada");
        }
        binService.restoreBin(binId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Relatório restaurado com sucesso.");
    }

    @Transactional
    @DeleteMapping(value = "/delete/{id}")
    @ApiOperation(value = "Delete report in bin")
    @ApiResponses({ @ApiResponse(code=204, message = "Report in bin deleted success"),
                    @ApiResponse(code = 404, message = "Report in bin not found")})
    public ResponseEntity<Object> delete(@PathVariable Long id){
        if(binService.findById(id) == null){
            throw new RuntimeException("Item na lixeira não foi encontrado.");
        }
        binService.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Item da lixeira excluído com sucesso.");
    }
}
