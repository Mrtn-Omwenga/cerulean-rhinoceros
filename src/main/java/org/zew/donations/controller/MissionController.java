package org.zew.donations.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.zew.donations.service.WalletService;

@RestController
@RequestMapping(path="/mission", produces="application/json")
public class MissionController {

    @Autowired
    private WalletService walletService;

    @GetMapping("/total-donors/{id}")
    public int getTotalMissionDonors(@PathVariable String id){
        return this.walletService.getCountByMissionIdGroupByOwnerId(id);
    }
}
