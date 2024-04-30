package dev.andreasgeorgatos.tsilikos.controller.rewards;

import dev.andreasgeorgatos.tsilikos.model.rewards.MembershipCard;
import dev.andreasgeorgatos.tsilikos.service.rewards.MembershipCardService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/membership-card")
public class MembershipCardController {

    private final MembershipCardService membershipCardService;

    @Autowired
    public MembershipCardController(MembershipCardService membershipCardService) {
        this.membershipCardService = membershipCardService;
    }

    @GetMapping
    public ResponseEntity<List<MembershipCard>> getAllMembershipCards() {
        return membershipCardService.getAllMembershipCards();
    }

    @GetMapping("/{id}")
    public ResponseEntity<MembershipCard> getMembershipCardById(@PathVariable Long id) {
        return membershipCardService.getMembershipCardById(id);
    }

    @PostMapping
    public ResponseEntity<?> createMembershipCard(@Valid @RequestBody MembershipCard membershipCard, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).toList();
            return ResponseEntity.badRequest().body(errors);
        }
        return membershipCardService.createMembershipCard(membershipCard);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editMembershipCardById(@Valid @PathVariable Long id, @RequestBody MembershipCard membershipCard, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).toList();
            return ResponseEntity.badRequest().body(errors);
        }
        return membershipCardService.editMembershipCardById(id, membershipCard);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MembershipCard> deleteMembershipCardById(@PathVariable Long id) {
        return membershipCardService.deleteMembershipCardById(id);
    }

}
