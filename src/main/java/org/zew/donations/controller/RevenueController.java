package org.zew.donations.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.zew.donations.converter.EntityConverter;
import org.zew.donations.model.Revenue;
import org.zew.donations.model.response.EntityResponse;
import org.zew.donations.service.RevenueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path="/revenues", produces="application/json")

public class RevenueController {

    @Autowired
    private RevenueService revenueService;

    @GetMapping("/findByOwnerId")
    public List<Revenue> findByOwnerId(@RequestParam String ownerId) {
        return revenueService.getAllRevenuesByOwnerId(ownerId);
    }

    @GetMapping("/{ownerId}")
    public ResponseEntity<Map<String, Object>> getFinancialsByOwnerId(
            @PathVariable String ownerId,
            @RequestParam(defaultValue = "USD") String currency) {

        Map<String, Object> response = revenueService.getFinancialsByOwnerId(ownerId, currency);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public EntityResponse create(@RequestBody @Valid Revenue revenue){
        try {
            return EntityConverter.fromEntityToResponse(revenueService.create(revenue));
        }catch (RuntimeException e) {
            throw new RevenueAlreadyExistsException();
        }
    }

    @ResponseStatus(value= HttpStatus.CONFLICT, reason="Wallet Already Exists")
    public class RevenueAlreadyExistsException extends RuntimeException {}
}
