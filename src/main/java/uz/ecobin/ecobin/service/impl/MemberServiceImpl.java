package uz.ecobin.ecobin.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import uz.ecobin.ecobin.exceptions.CommonException;
import uz.ecobin.ecobin.model.Member;
import uz.ecobin.ecobin.repository.MemberRepository;
import uz.ecobin.ecobin.service.MemberService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
    private final MemberRepository memberRepository;

    @Override
    public ResponseEntity<List<Member>> findAllTeamMembers() {
        List<Member> all = memberRepository.findAll();
        return ResponseEntity.ok(all);
    }

    @Override
    public ResponseEntity<Member> createMember(Member member) {
        Member save = memberRepository.save(member);
        return ResponseEntity.ok(save);
    }

    @Override
    public ResponseEntity<?> delete(Long id) {
        try {
            memberRepository.deleteById(id);
            return ResponseEntity.ok("Member successfully deleted");
        } catch (EmptyResultDataAccessException e) {
            throw new CommonException("Something is wrong !!!", HttpStatus.CONFLICT);
        }
    }
}