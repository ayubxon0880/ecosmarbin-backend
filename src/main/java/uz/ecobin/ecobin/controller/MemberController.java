package uz.ecobin.ecobin.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.ecobin.ecobin.model.Member;
import uz.ecobin.ecobin.service.MemberService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/team")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/all")
    public ResponseEntity<List<Member>> getAllTeamMembers() {
        return memberService.findAllTeamMembers();
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Member> save(@RequestBody Member member) {
        return memberService.createMember(member);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        return memberService.delete(id);
    }
}
