package org.zew.donations.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.zew.donations.converter.EntityConverter;
import org.zew.donations.converter.WalletConverter;
import org.zew.donations.model.Wallet;
import org.zew.donations.model.response.EntityResponse;
import org.zew.donations.model.response.WalletResponse;
import org.zew.donations.service.WalletService;

import java.util.NoSuchElementException;

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

    @ResponseStatus(value=HttpStatus.CONFLICT, reason="Wallet Already Exists")
    @ExceptionHandler(RuntimeException.class)
    public void walletAlreadyExists() {}

    @GetMapping("/{id}")
    public WalletResponse getById(@PathVariable String id) {
        return WalletConverter.fromWalletToResponse(walletService.findById(id));
    }

    @ResponseStatus(value=HttpStatus.NOT_FOUND, reason="Wallet Not Found")
    @ExceptionHandler(NoSuchElementException.class)
    public void walletNotFound() {}

}
