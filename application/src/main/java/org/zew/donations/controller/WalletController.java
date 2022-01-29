package org.zew.donations.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.zew.donations.service.WalletService;

@Controller
@RequestMapping(path = "/wallet", produces = MediaType.APPLICATION_JSON_VALUE)
public class WalletController {

    private final WalletService walletService;

    @Autowired
    public WalletController(WalletService walletService) {
        this.walletService = walletService;
    }

    @PostMapping(value = "/insert")
    public ResponseEntity<String> insertWallet(String todoAddInputHere) {
        walletService.insertWallet(); // todo (TL)
        return null;
    }

}
