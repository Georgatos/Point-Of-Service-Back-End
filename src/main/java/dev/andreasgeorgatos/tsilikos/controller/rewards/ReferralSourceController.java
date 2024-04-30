package dev.andreasgeorgatos.tsilikos.controller.rewards;

import dev.andreasgeorgatos.tsilikos.model.rewards.ReferralSource;
import dev.andreasgeorgatos.tsilikos.service.rewards.ReferralSourceService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/referral-source")
public class ReferralSourceController {

    private final ReferralSourceService referralSourceService;

    @Autowired
    public ReferralSourceController(ReferralSourceService referralSourceService) {
        this.referralSourceService = referralSourceService;
    }

    @GetMapping
    public ResponseEntity<List<ReferralSource>> getAllReferrals() {
        return referralSourceService.getAllReferrals();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReferralSource> getReferralById(@PathVariable Long id) {
        return referralSourceService.getReferralById(id);
    }

    @PostMapping
    public ResponseEntity<?> createReferral(@Valid @RequestBody ReferralSource referralSource, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).toList();
            return ResponseEntity.badRequest().body(errors);
        }
        return referralSourceService.createReferral(referralSource);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editReferralById(@Valid @PathVariable Long id, @RequestBody ReferralSource referralSource, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).toList();
            return ResponseEntity.badRequest().body(errors);
        }
        return referralSourceService.editReferralById(id, referralSource);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ReferralSource> deleteReferralById(@PathVariable Long id) {
        return referralSourceService.deleteReferralById(id);
    }
}
