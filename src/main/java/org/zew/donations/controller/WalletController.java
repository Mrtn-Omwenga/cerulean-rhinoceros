package org.zew.donations.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.zew.donations.converter.EntityConverter;
import org.zew.donations.model.Wallet;
import org.zew.donations.model.response.EntityResponse;
import org.zew.donations.service.WalletService;

import javax.validation.Valid;

@RestController
@RequestMapping(path="/wallet", produces="application/json")
public class WalletController {

    @Autowired
    private WalletService walletService;

    @PostMapping
    public EntityResponse create(@RequestBody @Valid Wallet wallet) {
        return EntityConverter.fromEntityToResponse(walletService.create(wallet));
    }

    @GetMapping("/{id}")
    public Wallet getById(@PathVariable String id) {
        return walletService.findById(id);
    }

}
