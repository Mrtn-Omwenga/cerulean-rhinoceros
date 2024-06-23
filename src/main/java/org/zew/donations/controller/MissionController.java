package org.zew.donations.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.zew.donations.dto.SupportedMissionResponse;
import org.zew.donations.service.MissionService;
import org.zew.donations.service.WalletService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path="/mission", produces="application/json")
public class MissionController {

    @Autowired
    private WalletService walletService;

    @Autowired
    private MissionService missionService;

    @GetMapping("/total-donors/{id}")
    public int getTotalMissionDonors(@PathVariable String id){
        return this.walletService.getCountByMissionIdGroupByOwnerId(id);
    }

    @GetMapping("/supported-missions/{id}")
    public ResponseEntity<Map<String, Object>> getSupportedMissions(@RequestParam String contactId) {
        List<SupportedMissionResponse> supportedMissions = missionService.getSupportedMissions(contactId);
        Map<String, Object> response = Map.of("supportedMissions", supportedMissions);
        return ResponseEntity.ok(response);
    }
}
