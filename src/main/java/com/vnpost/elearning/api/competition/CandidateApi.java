package com.vnpost.elearning.api.competition;

import com.vnpost.elearning.converter.RoundTestConverter;
import com.vnpost.elearning.dto.competition.RoundTestDTO;
import com.vnpost.elearning.dto.ServiceResult;
import com.vnpost.elearning.security.MyUser;
import com.vnpost.elearning.service.CandidateService;
import com.vnpost.elearning.service.RoundTestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CandidateApi {
    @Autowired
    private CandidateService candidateService;
    @Autowired
    private RoundTestService roundTestService;

    @Autowired private RoundTestConverter roundTestConverter;

    @GetMapping("/api/candidate/{id_user}/{countTime}/{id_round_test}")
    public ResponseEntity<ServiceResult> getCandidatesRoundTest(@PathVariable(name = "id_user") Long id_user,
                                                                @PathVariable(name = "countTime") String countTime,
                                                                @PathVariable(name = "id_round_test") Long id_round_test) {
        RoundTestDTO roundTest = roundTestConverter.convertToDTO(roundTestService.findById(id_round_test).get());
        MyUser user = (MyUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if(roundTest.getShowResutl()==0){
            return new ResponseEntity<>(new ServiceResult(candidateService.
                    findByIdUserIdRoundTestCountTest(user.getId() + "", id_round_test + "", countTime),"success","200") , HttpStatus.OK);
        }else{
            return new ResponseEntity<>(new ServiceResult(null,"Không cho hiển thị kết quả","200") , HttpStatus.OK);
        }
    }


    @GetMapping("/api/candidate/result/{idRoundTest}")
    public ResponseEntity<ServiceResult> listResult(
            @PathVariable(name = "idRoundTest") Long idRoundTest) {
        MyUser myUser = (MyUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return new ResponseEntity<>(new ServiceResult(candidateService.findByidUserIdRound(myUser.getId(),idRoundTest),"Thanh Cong","200"), HttpStatus.OK);
    }
}
