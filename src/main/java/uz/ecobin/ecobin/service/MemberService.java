package uz.ecobin.ecobin.service;

import org.springframework.http.ResponseEntity;
import uz.ecobin.ecobin.model.Member;

import java.util.List;

public interface MemberService {
    ResponseEntity<List<Member>> findAllTeamMembers();

    ResponseEntity<Member> createMember(Member member);

    ResponseEntity<?> delete(Long id);
}
