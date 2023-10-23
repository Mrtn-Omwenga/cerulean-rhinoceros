package org.zew.donations.controller;

import org.zew.donations.model.Revenue;
import org.zew.donations.service.RevenueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
}
