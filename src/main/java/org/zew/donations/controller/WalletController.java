package org.zew.donations.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.zew.donations.converter.EntityConverter;
import org.zew.donations.converter.WalletConverter;
import org.zew.donations.model.Wallet;
import org.zew.donations.model.request.WalletUpdateRequest;
import org.zew.donations.model.response.EntityResponse;
import org.zew.donations.model.response.WalletResponse;
import org.zew.donations.service.WalletService;

import javax.validation.Valid;

@RestController
@RequestMapping(path="/wallet", produces="application/json")
public class WalletController {

    @Autowired
    private WalletService walletService;

    @PostMapping
    public EntityResponse create(@RequestBody @Valid Wallet wallet) {
        try {
            return EntityConverter.fromEntityToResponse(walletService.create(wallet));
        } catch (RuntimeException e) {
            throw new WalletAlreadyExistsException();
        }
    }

    @ResponseStatus(value=HttpStatus.CONFLICT, reason="Wallet Already Exists")
    public class WalletAlreadyExistsException extends RuntimeException {}

    @GetMapping("/{id}")
    public WalletResponse getById(@PathVariable String id) {
        try {
            return WalletConverter.fromWalletToResponse(walletService.findById(id));
        } catch (RuntimeException e) {
            throw new WalletNotFoundException();
        }
    }

    @PutMapping("/{id}")
    public EntityResponse updateAmounts(@PathVariable String id, @RequestBody @Valid WalletUpdateRequest request) {
        try {
            return EntityConverter.fromEntityToResponse(walletService.updateAmounts(id, request));
        } catch (RuntimeException e) {
            throw new WalletNotFoundException();
        }
    }

    @ResponseStatus(value=HttpStatus.NOT_FOUND, reason="Wallet Not Found")
    public class WalletNotFoundException extends RuntimeException {}

}
